package scratch.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import scratch.webapp.data.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * A simple controller with a request mapping to the root of the servlet.
 *
 * @author Karl Bennett
 */
@Controller
public class ScratchController {

    /**
     * Map any request to the servlet root to this method.
     *
     * @param request  the servlet request object.
     * @param response the servlet response object.
     * @return a map that will be converted by Spring into {@code JSON} because of the {@code produces} value in
     *         {@code @RequestMapping}.
     */
    @RequestMapping(value = "/", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<Object, Object> handle(HttpServletRequest request, HttpServletResponse response) {

        Map<Object, Object> body = new HashMap<Object, Object>();

        body.put("scratched", true);

        return body;
    }

    /**
     * Persist a new user using the user object that has been deserialised from the {@code JSON} in the body of the
     * {@code POST} request.
     * <p/>
     * This operation will fail if a user exists with the emil supplied in the new user. Also if an ID is supplied it
     * will be ignored unless it is an ID for an existing user at which point the persistence will fail.
     *
     * @param user the user to persist.
     * @return the newly persisted user.
     * @throws javax.persistence.EntityExistsException if the deserialised user contains an ID that is contained by an
     *          existing user.
     */
    @RequestMapping(value = "/users", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> create(@Valid @RequestBody final User user) {

        return new Callable<User>() {

            @Override
            public User call() throws Exception {

                return user.create();
            }
        };
    }

    /**
     * Retrieve the user with the supplied ID.
     *
     * @param id the is of the user to retrieve.
     * @return the requested user.
     * @throws javax.persistence.EntityNotFoundException if no user exists with the supplied id.
     */
    @RequestMapping(value = "/users/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> retrieve(@PathVariable final Long id) {

        return new Callable<User>() {

            @Override
            public User call() throws Exception {

                return new User(id);
            }
        };
    }

    /**
     * Retrieve all the persisted user.
     *
     * @return all the users that have been persisted.
     */
    @RequestMapping(value = "/users", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<Iterable<User>> retrieve() {

        return new Callable<Iterable<User>>() {

            @Override
            public Iterable<User> call() throws Exception {

                return User.all();
            }
        };
    }

    /**
     * Updated the user that has been deserialised from the {@code JSON} in the body of the {@code PUT} request.
     *
     * @param id   the ID of the user to update.
     * @param user the deserialised user minus the ID.
     * @return the updated user.
     * @throws javax.persistence.EntityNotFoundException if no user exists with the supplied id.
     */
    @RequestMapping(value = "/users/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> update(@PathVariable Long id, @Valid @RequestBody final User user) {

        user.setId(id);

        return new Callable<User>() {

            @Override
            public User call() throws Exception {

                return user.update();
            }
        };
    }

    /**
     * Delete the user with the supplied ID.
     *
     * @param id the ID of the user to delete.
     * @return the delete user.
     * @throws javax.persistence.EntityNotFoundException if no user exists with the supplied id.
     */
    @RequestMapping(value = "/users/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> delete(@PathVariable final Long id) {

        return new Callable<User>() {

            @Override
            public User call() throws Exception {

                return new User(id).delete();
            }
        };
    }
}
