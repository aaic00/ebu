package name.aaic.sys.client;

import org.apache.http.client.ResponseHandler;
import org.springframework.stereotype.Service;

@Service
public class ByteArrayHttpClient extends AbstractHttpClient<byte[]> {

  @Override
  protected ResponseHandler<byte[]> getResponseHandler() {
    return ByteArrayResponseHandler.getInstance();
  }

}
