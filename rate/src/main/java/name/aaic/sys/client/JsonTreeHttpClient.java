package name.aaic.sys.client;

import java.nio.charset.Charset;

import javax.annotation.Resource;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class JsonTreeHttpClient implements HttpClientInterface<JsonNode> {

  @Resource
  private TextHttpClient textHttpClient;

  @Override
  public JsonNode execute(final HttpUriRequest request, final Charset charset)
      throws HttpClientException {
    return JsonMapper.readTree(this.strip(this.textHttpClient.execute(request, charset)));
  }

  @Override
  public JsonNode execute(final HttpUriRequest request) throws HttpClientException {
    return JsonMapper.readTree(this.strip(this.textHttpClient.execute(request)));
  }

  protected String strip(final String response) {
    return response;
  }

}
