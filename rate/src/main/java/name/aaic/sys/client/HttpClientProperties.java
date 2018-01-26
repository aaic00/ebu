package name.aaic.sys.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("http-client-properties")
public class HttpClientProperties {

  private int maxTotal = 200;
  private int defaultMaxPerRoute = 50;
  private int connectTimeout = 1000;
  private int connectionRequestTimeout = 500;
  private int socketTimeout = 10000;
  private int retryCount = 5;
  private String defaultCharset = "UTF8";

  public int getMaxTotal() {
    return this.maxTotal;
  }

  public void setMaxTotal(final int maxTotal) {
    this.maxTotal = maxTotal;
  }

  public int getDefaultMaxPerRoute() {
    return this.defaultMaxPerRoute;
  }

  public void setDefaultMaxPerRoute(final int defaultMaxPerRoute) {
    this.defaultMaxPerRoute = defaultMaxPerRoute;
  }

  public int getConnectTimeout() {
    return this.connectTimeout;
  }

  public void setConnectTimeout(final int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getConnectionRequestTimeout() {
    return this.connectionRequestTimeout;
  }

  public void setConnectionRequestTimeout(final int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  public int getSocketTimeout() {
    return this.socketTimeout;
  }

  public void setSocketTimeout(final int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public int getRetryCount() {
    return this.retryCount;
  }

  public void setRetryCount(final int retryCount) {
    this.retryCount = retryCount;
  }

  public String getDefaultCharset() {
    return this.defaultCharset;
  }

  public void setDefaultCharset(final String defaultCharset) {
    this.defaultCharset = defaultCharset;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("HttpClientProperties [maxTotal=");
    builder.append(this.maxTotal);
    builder.append(", defaultMaxPerRoute=");
    builder.append(this.defaultMaxPerRoute);
    builder.append(", connectTimeout=");
    builder.append(this.connectTimeout);
    builder.append(", connectionRequestTimeout=");
    builder.append(this.connectionRequestTimeout);
    builder.append(", socketTimeout=");
    builder.append(this.socketTimeout);
    builder.append(", retryCount=");
    builder.append(this.retryCount);
    builder.append(", defaultCharset=");
    builder.append(this.defaultCharset);
    builder.append("]");
    return builder.toString();
  }

}
