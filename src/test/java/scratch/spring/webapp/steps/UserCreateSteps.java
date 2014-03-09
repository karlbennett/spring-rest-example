package scratch.spring.webapp.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Deque;
import java.util.Map;

import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserCreateSteps {

    public static final String ID = "id";

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Holder<Deque<Response>> responses;

    @When("^I create the user$")
    public void I_create_the_user() {

        post(user);
    }

    private void post(PropertyObject user) {

        final Response response = client.request(MediaType.APPLICATION_JSON_TYPE).post(json(user.toMap()));
        response.bufferEntity();

        responses.get().push(response);
    }

    @And("^the new user should be persisted$")
    public void the_new_user_should_be_persisted() {

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = responses.get().peek().readEntity(Map.class);

        assertEquals("the user should have been persisted.", body, get(body.get(ID).toString()).readEntity(Map.class));
    }

    private Response get(String id) {

        return client.path(id).request(MediaType.APPLICATION_JSON_TYPE).get();
    }
}
