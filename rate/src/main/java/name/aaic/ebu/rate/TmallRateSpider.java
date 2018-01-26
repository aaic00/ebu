package name.aaic.ebu.rate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import name.aaic.sys.client.JsonTreeHttpClient;

@Service
public class TmallRateSpider extends JsonTreeHttpClient {

  @SuppressWarnings("unused")
  private static final Logger log = LoggerFactory.getLogger(TmallRateSpider.class);

  @Override
  protected String strip(final String response) {
    if (StringUtils.contains(response, "rgv587_flag")) {
      return "{}";
    }
    return StringUtils.join('{', StringUtils.replace(response, "<b></b>", "").substring(2), '}');
  }

}
