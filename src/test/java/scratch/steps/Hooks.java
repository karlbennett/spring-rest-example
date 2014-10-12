package scratch.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static scratch.steps.UserFields.ID;

/**
 * Global hooks to run before and after even scenario.
 */
@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class Hooks {

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @Before
    public void setup() {

        user.clear();
        responses.clear();
    }

    @After
    public void tearDown() {

        for (ClientResponse response : responses.created()) {

            @SuppressWarnings("unchecked")
            final Map<String, Object> body = response.readEntity(Map.class);

            client.path(body.get(ID).toString()).request(MediaType.APPLICATION_JSON_TYPE).delete();
        }
    }
}
