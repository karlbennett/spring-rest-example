package scratch.spring.webapp.steps;

import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static scratch.spring.webapp.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserDeleteSteps {

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @When("^I delete an existing user$")
    public void I_request_an_existing_user() {

        final Response createResponse = responses.created().latest();

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = createResponse.readEntity(Map.class);

        I_request_a_user_with_an_ID_of(body.get(ID).toString());
    }

    @When("^I delete a user with an ID of \"([^\"]*)\"$")
    public void I_request_a_user_with_an_ID_of(String id) {

        final Response response = client.path(id).request(MediaType.APPLICATION_JSON_TYPE).delete();
        response.bufferEntity();

        responses.get().push(response);
    }
}
