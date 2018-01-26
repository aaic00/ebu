package name.aaic.sys.client;

import java.nio.charset.Charset;

import org.apache.http.client.methods.HttpUriRequest;


public interface HttpClientInterface<T> {

  T execute(HttpUriRequest request, Charset charset) throws HttpClientException;

  T execute(HttpUriRequest request) throws HttpClientException;

}
