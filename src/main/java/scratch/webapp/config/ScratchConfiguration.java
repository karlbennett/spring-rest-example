package scratch.webapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import scratch.webapp.controller.ScratchController;

/**
 * Configuration class used to configure the Spring framework.
 *
 * @author Karl Bennett
 */
@Configuration
@EnableWebMvc // Enable the Spring Web MVC environment, this includes support for XML/JSON conversion and validation.
// Tell Spring which package to look in for controller classes. This has been done by providing a class from the
// required package.
@ComponentScan(basePackageClasses = {ScratchController.class})
public class ScratchConfiguration {
}
