package name.aaic.sys.client;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

public final class Charsets {

  private static final Map<String, Charset> availableCharsets = Charset.availableCharsets();
  public static final Charset GBK = availableCharsets.get("GBK");

  public static void main(final String[] args) {
    final Map<String, Charset> availableCharsets = Charset.availableCharsets();
    for (final Entry<String, Charset> entry : availableCharsets.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue().name());
    }
  }

}
