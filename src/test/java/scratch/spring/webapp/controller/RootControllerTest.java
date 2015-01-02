package scratch.spring.webapp.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.spring.webapp.SpringBootRestServlet;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootRestServlet.class)
@WebAppConfiguration("classpath:")
@IntegrationTest({"server.port=0", "management.port=0"})
public class RootControllerTest {

    @Value("${local.server.port}")
    private int port;

    private WebTarget target;

    @Before
    public void setUp() {

        target = ClientBuilder.newClient().target(format("http://localhost:%d/rest/", port));
    }

    @Test
    public void the_scratch_endpoint_returns_the_correct_response() throws Exception {

        final Response response = target.path("/").request(APPLICATION_JSON).get();

        assertEquals(singletonMap("running", true), response.readEntity(Map.class));
    }
}
