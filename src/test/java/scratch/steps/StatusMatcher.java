package scratch.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class StatusMatcher extends TypeSafeMatcher<ClientResponse> {

    public static Matcher<ClientResponse> statusEquals(int status) {
        return new StatusMatcher(status);
    }

    private final int status;

    protected StatusMatcher(int status) {

        this.status = status;
    }

    @Override
    protected boolean matchesSafely(ClientResponse response) {

        return this.status == response.getStatus();
    }

    @Override
    public void describeTo(Description description) {

        description.appendValue(status);
    }
}
