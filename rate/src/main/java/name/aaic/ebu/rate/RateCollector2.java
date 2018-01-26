package name.aaic.ebu.rate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import name.aaic.sys.client.Charsets;
import name.aaic.sys.client.HttpClientException;

@Service
public class RateCollector2 {

  private static final String URL =
      "https://rate.tmall.com/list_detail_rate.htm?itemId=%s&spuId=%s&sellerId=%s&order=3&currentPage=%s&append=0&content=1&tagId=&posi=&picture=";
  // private static final String UC =
  // "&ua=018UW5TcyMNYQwiAiwQRHhBfEF8QXtHcklnMWc%3D%7CUm5OcktxRX5EeEF%2FRHlAey0%3D%7CU2xMHDJ7G2AHYg8hAS8XKQcnCVU0Uj5ZJ11zJXM%3D%7CVGhXd1llXGZSaVNvVmhTbldsW2ZEe0F1TnVMd0h8QH9DdkJ7Q207%7CVWldfS0SMg0xBSUZJgYoRXBZZF1nX3VPd1J3WQ9Z%7CVmhIGCUFOBgkGCwRMQo1Cj8fIxcoFTUJNAE8HCAUKxY2CjcPMmQy%7CV25OHjAePgs1CSkVLBgjAzgBO207%7CWGFBET8RMQU5ACAcJR8rCzENNWM1%7CWWBAED4QMAwxCjQUKBEpEzMJMw5YDg%3D%3D%7CWmNDEz0TMwc%2FAyMfIhYuDjUPMQVTBQ%3D%3D%7CW2JCEjwSMgs%2BBiYaJx4qCjYINg4xZzE%3D%7CXGVFFTsVNQ4yDCwQLBIoCDQKMw4wZjA%3D%7CXWREFDoUNA4wBSUZJhktDTEPNg4wZjA%3D%7CXmdHFzkXNwI6Di4SLBEsDDAONgs1YzU%3D%7CX2ZGFjgWNgI5ASEdIxogADwCOgI7bTs%3D%7CQHlZCScJKRUoEikJNQs%2FACAcIhkkEUcR%7CQXlZCScJKXlHf0pqVW5RaEh3QndXaEh0S35eZ10LKxY2GDYWLxIpEioQKX8p%7CQnpaCiQKKnpBeUJiXGRdYkJ5QH5eZVhhQX9FZVtvORkkBCoEJB0gGiMeIRpMGg%3D%3D%7CQ3pHelpnR3hYZF1hQX9HfV1kRHhFZVFxRGRefkVlXX1EZFp6RWVZeU1tWA4%3D&isg=ApSUQ7EisHWkFCsO1JTSnLPgZdLImLjXONff3C5615-XGTBjV_6HZr7Tb6Z7&needFold=0&_ksTS=1479675510174_1577";
  private static final String FILE = "c:\\tmp\\%s.txt";
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final Random random = new Random();

  @Resource
  private TmallRateSpider tmallRateSpider;

  public void collect(final long itemId, final int spuId, final long sellerId, final int total)
      throws HttpClientException, IOException {
    // itemId=539426199658&spuId=524491332&sellerId=3002164976

    int count = 0;
    int page = 1;
    final File file = new File(String.format(RateCollector2.FILE, itemId));

    final JsonNode root = this.get(itemId, spuId, sellerId, page);

    final int totalPage = root.get("rateDetail").get("paginator").get("lastPage").intValue();

    for (page = 10; page <= totalPage; page++) {
      final JsonNode ratePage = this.get(itemId, spuId, sellerId, page);
      if (!ratePage.has("rateDetail")) {
        continue;
      }
      final JsonNode rateList = ratePage.get("rateDetail").get("rateList");
      for (final JsonNode rate : rateList) {
        final String content =
            rate.get("rateContent").textValue().replace("\n", "").replaceAll("\r", "");
        FileUtils.write(file, content, "UTF8", true);



        final LocalDateTime rateDateTime =
            LocalDateTime.parse(rate.get("rateDate").textValue(), formatter);
        final LocalDateTime dateTime =
            LocalDateTime.of(2017, Month.OCTOBER, 25, rateDateTime.getHour(), random.nextInt(60),
                random.nextInt(60)).minusDays(random.nextInt(180));
        FileUtils.write(file, "\t", "UTF8", true);
        FileUtils.write(file, dateTime.format(formatter), "UTF8", true);

        FileUtils.write(file, "\n", "UTF8", true);
        count++;
      }
      if (count >= total) {
        break;
      }
    }
  }

  private JsonNode get(final long itemId, final int spuId, final long sellerId, final int page)
      throws HttpClientException {
    final String url = String.format(URL, itemId, spuId, sellerId, page);
    return this.tmallRateSpider.execute(RequestBuilder.get(url).build(), Charsets.GBK);
  }

}
