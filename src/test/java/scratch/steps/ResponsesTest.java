package scratch.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public class ResponsesTest {

    private ClientResponse responseOne;
    private ClientResponse responseTwo;
    private ClientResponse responseThree;

    private List<ClientResponse> responsesList;

    @Before
    public void setUp() {

        responseOne = mock(ClientResponse.class);
        responseTwo = mock(ClientResponse.class);
        responseThree = mock(ClientResponse.class);

        responsesList = asList(responseThree, responseTwo, responseOne);
    }

    @Test
    public void testCreateWithDeque() {

        final Deque<ClientResponse> responseDeque = new ArrayDeque<ClientResponse>();
        responseDeque.push(responseOne);
        responseDeque.push(responseTwo);
        responseDeque.push(responseThree);

        iteratorTest(new Responses(responseDeque));
    }

    @Test
    public void testAdd() {

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        iteratorTest(responses);
    }

    public void iteratorTest(Responses responses) {

        int count = 0;
        for (ClientResponse response : responses) {

            assertEquals("the response should be correct.", responsesList.get(count++), response);
        }

        assertEquals("the number of responses should be correct.", responsesList.size(), count);
    }

    @Test(expected = NullPointerException.class)
    public void testAddWithNull() {

        new Responses().add(null);
    }

    @Test
    public void testLatest() {

        final Responses responses = new Responses();

        responses.add(responseOne);
        assertEquals("the latest response should be correct.", responseOne, responses.latest());

        responses.add(responseTwo);
        assertEquals("the latest response should be correct.", responseTwo, responses.latest());

        responses.add(responseThree);
        assertEquals("the latest response should be correct.", responseThree, responses.latest());
    }

    @Test
    public void testLatestWithNoResponses() {

        final Responses responses = new Responses();

        assertNull("the latest response should be null.", responses.latest());
    }

    @Test
    public void testFilter() {

        when(responseOne.getStatus()).thenReturn(CREATED.value());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.value());
        when(responseThree.getStatus()).thenReturn(OK.value());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created response should be correct.", responses.filter(CREATED), hasItems(responseOne));
        assertThat("the accepted response should be correct.", responses.filter(ACCEPTED), hasItems(responseTwo));
        assertThat("the ok response should be correct.", responses.filter(OK), hasItems(responseThree));
        assertThat("the not found responses should be empty.", responses.filter(NOT_FOUND).get(), empty());
    }

    @Test
    public void testCreated() {

        when(responseOne.getStatus()).thenReturn(CREATED.value());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.value());
        when(responseThree.getStatus()).thenReturn(CREATED.value());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created responses should be correct.", responses.created(),
                hasItems(responseOne, responseThree));
    }

    @Test
    public void testCreatedWithNoCreatedResponses() {

        when(responseOne.getStatus()).thenReturn(OK.value());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.value());
        when(responseThree.getStatus()).thenReturn(NO_CONTENT.value());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created responses should be empty.", responses.created().get(), empty());
    }

    @Test
    public void testClear() {

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        responses.clear();

        for (ClientResponse response : responses) {
            fail("there should be no responses: " + response);
        }
    }
}
