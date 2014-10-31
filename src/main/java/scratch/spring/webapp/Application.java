package scratch.spring.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserRepository;

import javax.annotation.PostConstruct;

/**
 * This is the bootstrap that starts up the whole Spring Boot framework.
 *
 * @author Karl Bennett
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer {

    /**
     * This override will start up Spring Boot if this class is instantiated inside of a JEE
     * {@link javax.servlet.Servlet}.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
     * Configure the servlet context for this webapp. All it's endpoints will fall under {@code /rest}.
     */
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        final ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/rest/*");
        return registration;
    }

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void postConstruct() {

        User.setStaticRepository(userRepository);
    }

    /**
     * The standard Spring Boot main method. It is used when the packaged war is executed with {@code java -jar}
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
