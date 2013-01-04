package scratch.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import scratch.webapp.data.User;
import scratch.webapp.data.UserRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * A simple controller with a request mapping to the root of the servlet.
 *
 * @author Karl Bennett
 */
@Controller
public class ScratchController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Map any request to the servlet root to this method.
     *
     * @param request  the servlet request object.
     * @param response the servlet response object.
     * @return a map that will be converted by Spring into JSON because of the {@code produces} value in
     *         {@code @RequestMapping}.
     */
    @RequestMapping(value = "/", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<Object, Object> handle(HttpServletRequest request, HttpServletResponse response) {

        Map<Object, Object> body = new HashMap<Object, Object>();

        body.put("scratched", true);

        return body;
    }

    @RequestMapping(value = "/users", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public User create(@RequestBody User user) {

        return userRepository.save(user);
    }

    @RequestMapping(value = "/users/{id}", method = GET, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public User retrieve(@PathVariable Long id) {

        return userRepository.findOne(id);
    }

    @RequestMapping(value = "/users/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public User retrieve(@PathVariable Long id, @RequestBody User user) {

        user.setId(id);

        return userRepository.save(user);
    }
}
