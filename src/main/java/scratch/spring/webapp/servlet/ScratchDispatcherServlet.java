package scratch.spring.webapp.servlet;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author Karl Bennett
 */
@WebServlet(
        value = "/spring/*",
        initParams = {
                @WebInitParam(
                        name = "contextClass",
                        value = "org.springframework.web.context.support.AnnotationConfigWebApplicationContext"
                ),
                @WebInitParam(
                        name = "contextConfigLocation",
                        value = "scratch.spring.webapp.config.ScratchConfiguration"
                )},
        asyncSupported = true
)
public class ScratchDispatcherServlet extends DispatcherServlet {
}
