package scratch.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static scratch.steps.StatusMatcher.statusEquals;
import static scratch.steps.UserFields.ADDRESS_ID;
import static scratch.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private Responses responses;

    @Given("^there is a(?:nother)? new user$")
    public void there_is_a_new_user() {

        user.clear();
    }

    @Given("^the user has a[n]? \"([^\"]*)\" of \"([^\"]*)\"$")
    public void the_user_has_an_of(final String propertyPath, final String value) {

        the_user_has_an_of(user, propertyPath, value, new Runnable() {
            @Override
            public void run() {

                user.set(propertyPath, value);
            }
        });
    }

    @Given("^the user has a[n]? \"([^\"]*)\" of ?(\\w*)$")
    public void the_user_has_a_of(final String propertyPath, final String value) {

        the_user_has_an_of(user, propertyPath, value, new Runnable() {
            @Override
            public void run() {

                if ("".equals(value)) {
                    user.set(propertyPath, null);
                    return;
                }

                user.set(propertyPath, Integer.valueOf(value));
            }
        });
    }

    private static void the_user_has_an_of(PropertyObject user, String propertyPath, String value, Runnable setter) {

        if ("".equals(propertyPath)) {
            // We ignore empty property names so that it is possible to write Scenario Examples that don't set some
            // properties.
            return;
        }

        if ("NULL".equals(value) || "null".equals(value)) {
            user.set(propertyPath, null);
            return;
        }

        setter.run();
    }

    @Then("^I should receive a status code of (\\d+)$")
    public void I_should_receive_a_status_code_of(int status) {

        assertThat("the response HTTP status code should be correct.", new VerboseResponse(responses.latest()),
                statusEquals(status));
    }

    @Then("^the response body should contain the (?:new|requested|updated) user$")
    @SuppressWarnings("unchecked")
    public void the_response_body_should_contain_the_user() {

        final PropertyObject expected = new PropertyObject(user);

        final PropertyObject actual = new PropertyObject(responses.latest().readEntity(Map.class));
        actual.remove(ID);
        actual.remove(ADDRESS_ID);

        assertEquals("the response body should contain the user.", expected, actual);
    }

    @Then("^the response body should contain all the requested users$")
    @SuppressWarnings("unchecked")
    public void the_response_body_should_contain_all_the_requested_users() {

        final Set<Map<String, Object>> retrievedUsers = responses.latest().readEntity(Set.class);

        final Set<Map<String, Object>> createdUsers = new HashSet<>();
        for (ClientResponse response : responses.created()) {

            createdUsers.add(response.readEntity(Map.class));
        }

        assertEquals("the number of retrieved users should equal the number of create users.", createdUsers.size(),
                retrievedUsers.size());

        assertEquals("the retrieved users equal create users.", createdUsers, retrievedUsers);
    }

    @When("^the user has no \"([^\"]*)\" field$")
    public void the_user_has_no_field(String fieldName) {

        user.remove(fieldName);
    }
}
