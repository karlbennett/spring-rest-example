package scratch.spring.webapp.steps;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Test configuration that does not have the @EnableWebMvc annotation because the tests are not running with a
 * JEE container.
 *
 * @author Karl Bennett
 */
@Configuration
public class CucumberScratchConfiguration {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {

        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("cucumber.properties"));

        return propertyPlaceholderConfigurer;
    }

    @Bean
    public static WebTarget client(@Value("${rest.baseUrl}") String url) {

        final Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class).build();

        return client.target(url);
    }

    @Bean
    public static PropertyObject user() {

        return new PropertyObject();
    }

    @Bean
    public static Responses responses() {

        return new Responses();
    }
}
