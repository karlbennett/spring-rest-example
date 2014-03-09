package scratch.spring.webapp.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.CREATED;
import static scratch.spring.webapp.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private DequeHolder<Response> responses;

    @Given("^there is a(?:nother)? new user$")
    public void there_is_a_new_user() {

        user.clear();
    }

    @Given("^the user has a[n]? \"([^\"]*)\" of \"([^\"]*)\"$")
    public void the_user_has_an_of(String propertyPath, String value) {

        if ("".equals(propertyPath)) {
            return;
        }

        if ("NULL".equals(value) || "null".equals(value)) {
            user.set(propertyPath, null);
            return;
        }

        user.set(propertyPath, value);
    }

    @Then("^I should receive a status code of (\\d+)$")
    public void I_should_receive_a_status_code_of(int status) {

        assertEquals("the response HTTP status code should be correct.", status, responses.peek().getStatus());
    }

    @Then("^the response body should contain the (?:new|requested) user$")
    public void the_response_body_should_contain_the_new_user() {

        final Map<String, Object> expected = user.toMap();

        @SuppressWarnings("unchecked")
        final Map<String, Object> actual = responses.peek().readEntity(Map.class);
        actual.remove(ID);

        assertEquals("the response body should contain the user.", expected, actual);
    }

    @Then("^the response body should contain all the requested users$")
    @SuppressWarnings("unchecked")
    public void the_response_body_should_contain_all_the_requested_users() {

        final Set<Map<String, Object>> retrievedUsers = responses.peek().readEntity(Set.class);

        final Set<Map<String, Object>> createdUsers = new HashSet<Map<String, Object>>();
        for (Response response : responses) {

            if (CREATED.value() == response.getStatus()) {
                createdUsers.add(response.readEntity(Map.class));
            }
        }

        assertEquals("the number of retrieved users should equal the number of create users.", createdUsers.size(),
                retrievedUsers.size());

        assertEquals("the retrieved users equal create users.", createdUsers, retrievedUsers);
    }
}
