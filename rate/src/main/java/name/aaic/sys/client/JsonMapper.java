package name.aaic.sys.client;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private JsonMapper() {}

  public static String writeString(final Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (final JsonProcessingException e) {
      throw new JsonProcessingRuntimeException(e);
    }
  }

  public static JsonNode readTree(final String content) {
    try {
      return objectMapper.readTree(content);
    } catch (final JsonParseException e) {
      throw new JsonParseRuntimeException(e);
    } catch (final JsonMappingException e) {
      throw new JsonMappingRuntimeException(e);
    } catch (final IOException e) {
      throw new JsonIORuntimeException(e);
    }
  }

  public static <T> T readValue(final String content, final Class<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (final JsonParseException e) {
      throw new JsonParseRuntimeException(e);
    } catch (final JsonMappingException e) {
      throw new JsonMappingRuntimeException(e);
    } catch (final IOException e) {
      throw new JsonIORuntimeException(e);
    }
  }

  public static class JsonRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonRuntimeException(final Throwable cause) {
      super(cause);
    }
  }

  public static class JsonParseRuntimeException extends JsonRuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonParseRuntimeException(final Throwable cause) {
      super(cause);
    }
  }

  public static class JsonMappingRuntimeException extends JsonRuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonMappingRuntimeException(final Throwable cause) {
      super(cause);
    }
  }

  public static class JsonIORuntimeException extends JsonRuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonIORuntimeException(final Throwable cause) {
      super(cause);
    }
  }

  public static class JsonProcessingRuntimeException extends JsonRuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonProcessingRuntimeException(final Throwable cause) {
      super(cause);
    }
  }
}
