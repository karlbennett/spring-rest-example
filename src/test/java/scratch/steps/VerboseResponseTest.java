package scratch.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedMap;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.Map.Entry;
import static javax.ws.rs.core.Response.StatusType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerboseResponseTest {

    private static final String NEW_LINE = System.lineSeparator();

    private static final int STATUS_CODE = 999;
    private static final String STATUS_REASON = "Test Reason";
    private static final String HEADER_NAME = "Test";
    private static final String HEADER_VALUE = "Header";
    private static final String BODY = "Test Body";

    @Test
    @SuppressWarnings("unchecked")
    public void I_can_to_string_a_verbose_response() {

        final StatusType statusType = mock(StatusType.class);
        when(statusType.getReasonPhrase()).thenReturn(STATUS_REASON);

        final Entry header = mock(Entry.class);
        when(header.getKey()).thenReturn(HEADER_NAME);
        when(header.getValue()).thenReturn(HEADER_VALUE);

        final MultivaluedMap headers = mock(MultivaluedMap.class);
        when(headers.entrySet()).thenReturn(singleton(header));

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(STATUS_CODE);
        when(response.getStatusInfo()).thenReturn(statusType);
        when(response.getHeaders()).thenReturn(headers);
        when(response.readEntity(String.class)).thenReturn(BODY);

        assertEquals("the to string should be correct.",
                format("%sHTTP/1.1 %d %s%s%s: %s%s%s%s%s",
                        NEW_LINE,
                        STATUS_CODE, STATUS_REASON, NEW_LINE,
                        HEADER_NAME, HEADER_VALUE, NEW_LINE,
                        NEW_LINE,
                        BODY, NEW_LINE),
                new VerboseResponse(response).toString());

    }
}
