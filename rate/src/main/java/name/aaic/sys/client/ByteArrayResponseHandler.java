package name.aaic.sys.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;

public class ByteArrayResponseHandler extends AbstractResponseHandler<byte[]> {

  private static final ByteArrayResponseHandler instance = new ByteArrayResponseHandler();

  private ByteArrayResponseHandler() {

  }

  public static ByteArrayResponseHandler getInstance() {
    return ByteArrayResponseHandler.instance;
  }

  @Override
  public byte[] handleEntity(final HttpEntity entity) throws IOException {
    return EntityUtils.toByteArray(entity);
  }
}
