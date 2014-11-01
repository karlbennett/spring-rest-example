package scratch.spring.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;
import scratch.ScratchSpringBootServlet;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserRepository;

import javax.annotation.PostConstruct;

/**
 * This is the bootstrap that starts up the whole Spring Boot framework.
 *
 * @author Karl Bennett
 */
@Configuration
@Import(ScratchSpringBootServlet.class)
public class ScratchSpringBootRestServlet {

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

        SpringApplication.run(ScratchSpringBootRestServlet.class, args);
    }
}
