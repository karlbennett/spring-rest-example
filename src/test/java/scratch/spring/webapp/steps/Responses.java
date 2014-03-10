package scratch.spring.webapp.steps;

import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.springframework.http.HttpStatus.CREATED;

public class Responses extends Holder<Deque<Response>> implements Iterable<Response> {

    public Responses() {
        this(new ArrayDeque<Response>());
    }

    public Responses(Deque<Response> responses) {
        super(responses);
    }

    public void add(Response response) {
        get().push(response);
    }

    public Response latest() {
        return get().peek();
    }

    public Responses created() {

        return filter(CREATED);
    }

    public Responses filter(HttpStatus httpStatus) {

        final Responses responses = new Responses();

        for (Response response : get()) {

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
    public Iterator<Response> iterator() {
        return get().iterator();
    }
}
