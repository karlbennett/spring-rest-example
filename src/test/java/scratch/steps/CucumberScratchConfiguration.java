package scratch.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.WebTarget;

/**
 * Test configuration that does not have the @EnableWebMvc annotation because the tests are not running with a
 * JEE container.
 *
 * @author Karl Bennett
 */
@Configuration
public class CucumberScratchConfiguration {

    /**
     * Create a new property configurer that will enable the injection of any properties within the configured
     * properties files with the {@link Value} annotation.
     */
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {

        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("cucumber.properties"));

        return propertyPlaceholderConfigurer;
    }

    /**
     * AN object that helps with setting up test data.
     */
    @Bean
    public static PropertyObject user() {

        return new PropertyObject();
    }

    /**
     * A history of all the responses that has been returned during a scenario.
     */
    @Bean
    public Responses responses() {

        return new Responses();
    }

    /**
     * @param url this parameters value is set from a value with a properties file.
     */
    @Bean
    public WebTarget client(@Value("${rest.baseUrl}") String url) {

        final Responses responses = responses();

        final Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .register(new ClientResponseFilter() {
                    @Override
                    public void filter(ClientRequestContext requestContext,
                                       ClientResponseContext responseContext) {

                        final ClientResponse response = (ClientResponse) responseContext;
                        response.bufferEntity();

                        responses.add(response);
                    }
                })
                .build();

        return client.target(url);
    }
}
