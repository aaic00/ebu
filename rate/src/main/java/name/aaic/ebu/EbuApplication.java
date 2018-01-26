package name.aaic.ebu;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import name.aaic.ebu.rate.JDTmallRateCollector;
import name.aaic.sys.client.HttpClientException;

@SpringBootApplication
@ComponentScan("name.aaic")
public class EbuApplication {

  private static ConfigurableApplicationContext context;

  public static void main(final String[] args) throws HttpClientException, IOException {
    context = SpringApplication.run(EbuApplication.class, args);
    collect();
  }

  static void collect() throws HttpClientException, IOException {
    final JDTmallRateCollector collector = context.getBean(JDTmallRateCollector.class);
    // collector.collect(539365221844L, 721519036L, 1062526268L, 10020164588L, 200);
    collector.collect(555208051619L, 861286867L, 1062526268L, 10020103053L, 200);

  }

}
