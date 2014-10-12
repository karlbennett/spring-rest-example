package scratch.steps;

import cucumber.api.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Set;

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

        @SuppressWarnings("unchecked")
        final Set<Map<String, Object>> users =
                client.request(MediaType.APPLICATION_JSON_TYPE).get().readEntity(Set.class);

        for (Map<String, Object> user : users) {

            client.path(user.get(ID).toString()).request(MediaType.APPLICATION_JSON_TYPE).delete();
        }
    }
}
