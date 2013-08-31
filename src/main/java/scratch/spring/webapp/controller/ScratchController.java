package scratch.spring.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * A simple controller with a request mapping to the root of the servlet.
 *
 * @author Karl Bennett
 */
@Controller
@RequestMapping("/")
public class ScratchController {

    /**
     * Map any request to the servlet root to this method.
     *
     * @param request  the servlet request object.
     * @param response the servlet response object.
     * @return a map that will be converted by Spring into {@code JSON} because of the {@code produces} value in
     *         {@code @RequestMapping}.
     */
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<Object, Object> handle(HttpServletRequest request, HttpServletResponse response) {

        Map<Object, Object> body = new HashMap<Object, Object>();

        body.put("scratched", true);

        return body;
    }
}
