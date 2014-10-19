package scratch.steps;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Map.Entry;
import static org.mockito.Mockito.mock;

/**
 * This implementation of the {@link ClientResponse} simply wraps an existing response and makes it's
 * {@link #toString()} method print more information like the body contents.
 *
 * @author Karl Bennett
 */
public class VerboseResponse extends ClientResponse {

    private static final String NEW_LINE = System.lineSeparator();

    private final ClientResponse response;

    public VerboseResponse(ClientResponse response) {
        super(mock(Response.StatusType.class), mock(ClientRequest.class));

        this.response = response;
    }

    @Override
    public String toString() {

        final StringBuilder toString = new StringBuilder(NEW_LINE);

        toString.append(format("HTTP/1.1 %d %s", response.getStatus(), response.getStatusInfo().getReasonPhrase()));

        toString.append(NEW_LINE);

        for (Entry<String, List<String>> header : response.getHeaders().entrySet()) {
            toString.append(format("%s: %s", header.getKey(), header.getValue())).append(NEW_LINE);
        }

        toString.append(NEW_LINE);

        toString.append(response.readEntity(String.class)).append(NEW_LINE);

        return toString.toString();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return response.getCookies();
    }

    @Override
    public Object getEntity() throws IllegalStateException {
        return response.getEntity();
    }

    @Override
    public Set<Link> getLinks() {
        return response.getLinks();
    }

    @Override
    public ClientRequest getRequestContext() {
        return response.getRequestContext();
    }

    @Override
    public URI getResolvedRequestUri() {
        return response.getResolvedRequestUri();
    }

    @Override
    public ServiceLocator getServiceLocator() {
        return response.getServiceLocator();
    }

    @Override
    public int getStatus() {
        return response.getStatus();
    }

    @Override
    public Response.StatusType getStatusInfo() {
        return response.getStatusInfo();
    }

    @Override
    public <T> T readEntity(Class<T> entityType) throws ProcessingException, IllegalStateException {
        return response.readEntity(entityType);
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) throws ProcessingException, IllegalStateException {
        return response.readEntity(entityType, annotations);
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) throws ProcessingException, IllegalStateException {
        return response.readEntity(entityType);
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) throws ProcessingException, IllegalStateException {
        return response.readEntity(entityType, annotations);
    }

    @Override
    public void setResolvedRequestUri(URI uri) {
        response.setResolvedRequestUri(uri);
    }

    @Override
    public void setStatus(int code) {
        response.setStatus(code);
    }

    @Override
    public void setStatusInfo(Response.StatusType status) {
        response.setStatusInfo(status);
    }
}
