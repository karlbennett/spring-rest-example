package scratch.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.springframework.http.HttpStatus.CREATED;

public class Responses extends Holder<Deque<ClientResponse>> implements Iterable<ClientResponse> {

    public Responses() {
        this(new ArrayDeque<ClientResponse>());
    }

    public Responses(Deque<ClientResponse> responses) {
        super(responses);
    }

    public void add(ClientResponse response) {
        get().push(response);
    }

    public ClientResponse latest() {
        return get().peek();
    }

    public Responses created() {

        return filter(CREATED);
    }

    public Responses filter(HttpStatus httpStatus) {

        final Responses responses = new Responses();

        for (ClientResponse response : get()) {

            if (httpStatus.value() == response.getStatus()) {
                responses.get().addLast(response);
            }
        }

        return responses;
    }

    public void clear() {
        get().clear();
    }

    @Override
    public Iterator<ClientResponse> iterator() {
        return get().iterator();
    }
}
