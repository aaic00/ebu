package name.aaic.ebu.rate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import name.aaic.sys.client.ByteArrayHttpClient;
import name.aaic.sys.client.Charsets;
import name.aaic.sys.client.HttpClientException;

@Component
public class JDTmallRateCollector {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final Logger log = LoggerFactory.getLogger(JDTmallRateCollector.class);

  private final String tmp = System.getProperty("java.io.tmpdir");

  @Autowired
  private TmallRateSpider tmallRateSpider;

  @Autowired
  private ByteArrayHttpClient byteArrayHttpClient;

  public void collect(final Long itemId, final Long spuId, final Long sellerId, final Long jdId,
      final int count) throws HttpClientException, IOException {
    final List<JDRate> jdRateList = this.collectRate(itemId, spuId, sellerId, jdId, count);
    this.downloadPics(jdId, jdRateList);
    this.writeCsv1(jdId, jdRateList);
    this.writeCsv2(jdId, jdRateList);
  }

  private void writeCsv1(final Long jdId, final List<JDRate> jdRateList) throws IOException {
    final File file = Paths.get(this.tmp, "rate", jdId.toString(), jdId + "-nopic.csv").toFile();
    final List<String[]> data =
        jdRateList.stream().map(JDRate::toCsv1).collect(Collectors.toList());
    try (final CSVPrinter printer = CSVFormat.DEFAULT.print(file, Charsets.GBK)) {
      printer.printRecords(data);
      printer.flush();
    }
  }

  private void writeCsv2(final Long jdId, final List<JDRate> jdRateList) throws IOException {
    final File file = Paths.get(this.tmp, "rate", jdId.toString(), jdId + ".csv").toFile();
    final List<String[]> data = new ArrayList<>();
    int count = 1;
    for (final JDRate jdRate : jdRateList) {
      final String[] row = jdRate.toCsv2(count);
      data.add(row);
      final int rowLength = row.length;
      if (rowLength > 4) {
        count += rowLength - 6;
      }
    }
    try (final CSVPrinter printer = CSVFormat.DEFAULT.print(file, Charsets.GBK)) {
      printer.printRecords(data);
      printer.flush();
    }
  }

  private void downloadPics(final Long jdId, final List<JDRate> jdRateList) {
    final String basepath =
        Paths.get(this.tmp, "rate", jdId.toString()).toAbsolutePath().toString();
    log.info("PATH :{}", basepath);

    final List<Img> imgList = this.genImgList(jdId, jdRateList, basepath);

    imgList.parallelStream().forEach(g -> {
      try {
        final byte[] data = this.byteArrayHttpClient.execute(RequestBuilder.get(g.url).build());
        FileUtils.writeByteArrayToFile(g.file, data);
      } catch (final HttpClientException | IOException e) {
        log.error("DOWNLOAD {} {}", g.url, g.file, e);
      }
    });
  }

  private List<Img> genImgList(final Long jdId, final List<JDRate> jdRateList,
      final String basepath) {
    int count = 1;
    final List<Img> imgList = new ArrayList<>();
    for (final JDRate jdRate : jdRateList) { // NOSONAR
      final List<String> pics = jdRate.getPics();
      if (pics == null || pics.isEmpty()) {
        continue;
      }
      for (int i = 0; i < Math.min(pics.size(), 5); i++) {
        final File file =
            Paths.get(basepath, jdId + "+" + (count < 10 ? "0" : "") + count + ".jpg").toFile();
        final String url = "http:".concat(pics.get(i));
        imgList.add(new Img(url, file));
        count++;
        if (count > 99) {
          return imgList;
        }
      }
    }
    return imgList;
  }

  private List<JDRate> collectRate(final Long itemId, final Long spuId, final Long sellerId,
      final Long jdId, final int count) throws HttpClientException {

    final List<JDRate> jdRateList = new ArrayList<>();
    for (Integer currentPage = 0; currentPage <= 99; currentPage++) { // NOSONAR
      final JsonNode rateDetail = this.getTMRate(itemId, spuId, sellerId, currentPage);
      if (rateDetail == null) {
        continue;
      }

      final JsonNode rateList = rateDetail.get("rateList");
      for (final JsonNode tmRate : rateList) {
        final JDRate jdRate = this.genJdRate(tmRate, jdId);
        if (jdRate != null) {
          jdRateList.add(jdRate);
        }
        if (jdRateList.size() >= count) {
          break;
        }
      }

      if (jdRateList.size() >= count) {
        break;
      }
    }
    return jdRateList;
  }

  private JsonNode getTMRate(final Long itemId, final Long spuId, final Long sellerId,
      final Integer currentPage) throws HttpClientException {
    final HttpUriRequest request = RequestBuilder.get("https://rate.tmall.com/list_detail_rate.htm")
        .addParameter("itemId", itemId.toString()).addParameter("spuId", spuId.toString())
        .addParameter("sellerId", sellerId.toString()).addParameter("order", "3")
        .addParameter("append", "0").addParameter("content", "1").addParameter("tagId", "")
        .addParameter("posi", "").addParameter("picture", "")
        .addParameter("currentPage", currentPage.toString()).build();

    final JsonNode ratePage = this.tmallRateSpider.execute(request, Charsets.GBK);
    return ratePage.get("rateDetail");
  }

  private JDRate genJdRate(final JsonNode tmRate, final Long jdId) {
    final JDRate jdRate = new JDRate();
    jdRate.setId(jdId);
    final String content =
        tmRate.get("rateContent").textValue().replace("\n", "").replaceAll("\r", "");
    if (Objects.equals("此用户没有填写评论!", content)) {
      final JsonNode appendComment = tmRate.get("appendComment");
      if (appendComment.isObject()) {
        jdRate.setContent(
            appendComment.get("content").textValue().replace("\n", "").replaceAll("\r", ""));
      } else {
        return null;
      }
    } else {
      jdRate.setContent(content);
    }
    jdRate.setDatetime(LocalDateTime.parse(tmRate.get("rateDate").textValue(), formatter));

    final JsonNode pics = tmRate.get("pics");
    if (pics.isArray()) {
      final List<String> picList = new ArrayList<>();
      for (final JsonNode pic : pics) {
        picList.add(pic.asText());
      }
      jdRate.setPics(picList);
    }
    return jdRate;
  }

  class Img {
    private final String url;
    private final File file;

    private Img(final String url, final File file) {
      this.url = url;
      this.file = file;
    }

  }
}
