package scratch.spring.webapp.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Deque;
import java.util.Map;

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
    private Holder<Deque<Response>> responses;

    @Before
    public void setup() {

        user.clear();
    }

    @After
    public void tearDown() {

        for (Response response : responses.get()) {

            if (HttpStatus.CREATED.value() == response.getStatus()) {

                @SuppressWarnings("unchecked")
                final Map<String, Object> body = response.readEntity(Map.class);

                client.path(body.get("id").toString()).request(MediaType.APPLICATION_JSON_TYPE).delete();
            }
        }
    }
}
