package example.rest.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This is the bootstrap that starts up the whole Spring Boot framework.
 * <p/>
 * It's position in the package hierarchy is very important. We are using auto-configuration to setup our datasource so
 * this class must be placed in a package higher than {@code example.rest.spring.data}. This is so that the
 * {@link EnableAutoConfiguration} component scanning will pick up the {@link example.rest.spring.data.User} and other
 * entity classes.
 *
 * @author Karl Bennett
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SpringBootRestServlet extends SpringBootServletInitializer {

    /**
     * This override will start up Spring Boot if this class is instantiated inside of a JEE
     * {@link javax.servlet.Servlet}.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootRestServlet.class);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        final ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/rest/*");
        return registration;
    }

    /**
     * The standard Spring Boot main method. It is used when the packaged war is executed with {@code java -jar}
     */
    public static void main(String[] args) {

        SpringApplication.run(SpringBootRestServlet.class, args);
    }
}
