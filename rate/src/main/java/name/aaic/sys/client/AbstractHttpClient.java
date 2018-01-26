package name.aaic.sys.client;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpClient<T> {
  private static final Logger log = LoggerFactory.getLogger(AbstractHttpClient.class);

  @Resource
  private HttpClientProperties httpClientProperties;

  @Resource
  private PoolingHttpClientConnectionManager httpClientConnectionManager;

  public T execute(final HttpUriRequest request) throws HttpClientException {
    AbstractHttpClient.log.info("{}", request);
    try (final CloseableHttpClient httpClient = this.buildHttpClient()) {
      return httpClient.execute(request, this.getResponseHandler());
    } catch (final IOException e) {
      throw new HttpClientException(e);
    }
  }

  protected abstract ResponseHandler<T> getResponseHandler();

  protected CloseableHttpClient buildHttpClient() {
    return HttpClients.custom().setConnectionManager(this.httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectTimeout(this.httpClientProperties.getConnectTimeout())
            .setConnectionRequestTimeout(this.httpClientProperties.getConnectionRequestTimeout())
            .setSocketTimeout(this.httpClientProperties.getSocketTimeout()).build())
        .setRetryHandler(
            new StandardHttpRequestRetryHandler(this.httpClientProperties.getRetryCount(), true))
        .build();
  }

}
