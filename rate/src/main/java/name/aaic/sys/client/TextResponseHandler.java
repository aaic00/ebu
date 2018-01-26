package name.aaic.sys.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;

public final class TextResponseHandler extends AbstractResponseHandler<String> {

  private static final Map<Charset, TextResponseHandler> instanceMap = new HashMap<>();
  private static final TextResponseHandler instance = new TextResponseHandler(null);

  private static final Charset defaultCharset = StandardCharsets.UTF_8;

  private final Charset charset;

  private TextResponseHandler(final Charset charset) {
    this.charset = charset;
  }

  public static ResponseHandler<String> getInstance() {
    return TextResponseHandler.instance;
  }

  public static TextResponseHandler getInstance(final Charset charset) {
    if (!TextResponseHandler.instanceMap.containsKey(charset)) {
      TextResponseHandler.instanceMap.put(charset, new TextResponseHandler(charset));
    }
    return TextResponseHandler.instanceMap.get(charset);
  }

  @Override
  public String handleEntity(final HttpEntity entity) throws IOException {
    return new String(EntityUtils.toByteArray(entity), this.resolveCharset(entity));
  }

  private Charset resolveCharset(final HttpEntity entity) {
    // 优先使用客户端传递的
    if (this.charset != null) {
      return this.charset;
    }

    // 其次使用Response中定义的
    final ContentType contentType = ContentType.get(entity);
    if (contentType != null) {
      final Charset charset = contentType.getCharset();
      if (charset != null) {
        return charset;
      }
    }

    // 实在不行用默认的
    return TextResponseHandler.defaultCharset;
  }


}
