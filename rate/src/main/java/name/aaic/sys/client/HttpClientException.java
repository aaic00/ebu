package name.aaic.sys.client;

public class HttpClientException extends Exception {

  private static final long serialVersionUID = 1L;

  public HttpClientException() {
    super();
  }

  public HttpClientException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public HttpClientException(final String message) {
    super(message);
  }

  public HttpClientException(final Throwable cause) {
    super(cause);
  }
}
