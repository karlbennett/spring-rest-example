package scratch.spring.webapp.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.Deque;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private Holder<Deque<Response>> responses;

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

        assertEquals("the response HTTP status code should be correct.", status, responses.get().peek().getStatus());
    }

    @Then("^the response body should contain the new user$")
    public void the_response_body_should_contain_the_new_user() {

        final Map<String, Object> expected = user.toMap();

        @SuppressWarnings("unchecked")
        final Map<String, Object> actual = responses.get().peek().readEntity(Map.class);
        actual.remove("id");

        assertEquals("the response body should contain the user.", expected, actual);
    }
}
