package name.aaic.sys.client;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:client-http.properties", ignoreResourceNotFound = true)
public class HttpClientConfiguration {
  private static final Logger log = LoggerFactory.getLogger(HttpClientConfiguration.class);

  @Resource
  private HttpClientProperties httpClientProperties;

  private PoolingHttpClientConnectionManager httpClientConnectionManager;

  @Bean
  public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {

    log.info("config:{}", this.httpClientProperties);

    final PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
    manager.setMaxTotal(this.httpClientProperties.getMaxTotal());
    manager.setDefaultMaxPerRoute(this.httpClientProperties.getDefaultMaxPerRoute());
    this.httpClientConnectionManager = manager;
    return manager;
  }

  @PreDestroy
  public void destroy() {
    HttpClientConfiguration.log.info("HttpClientConnectionPool is Closed.");
    this.httpClientConnectionManager.close();
  }

}
