package name.aaic.sys.client;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

@Service
public class TextHttpClient extends AbstractHttpClient<String> {

  public String execute(final HttpUriRequest request, final Charset charset)
      throws HttpClientException {
    try (final CloseableHttpClient httpClient = this.buildHttpClient()) {
      return httpClient.execute(request, this.getResponseHandler(charset));
    } catch (final IOException e) {
      throw new HttpClientException(e);
    }
  }

  protected ResponseHandler<String> getResponseHandler(final Charset charset) {
    return TextResponseHandler.getInstance(charset);
  }

  @Override
  protected ResponseHandler<String> getResponseHandler() {
    return TextResponseHandler.getInstance();
  }

}
