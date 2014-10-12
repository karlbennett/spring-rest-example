package scratch.steps;

import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static scratch.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserDeleteSteps {

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @When("^I delete an existing user$")
    public void I_delete_an_existing_user() {

        final ClientResponse createResponse = responses.created().latest();

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = createResponse.readEntity(Map.class);

        I_delete_a_user_with_an_ID_of(body.get(ID).toString());
    }

    @When("^I delete a user with an ID of \"([^\"]*)\"$")
    public void I_delete_a_user_with_an_ID_of(String id) {

        client.path(id).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }
}
