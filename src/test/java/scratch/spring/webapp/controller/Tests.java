package scratch.spring.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import scratch.spring.webapp.data.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Tests {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String json(Object value) {

        try {

            return MAPPER.writeValueAsString(value);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    public static User user(MvcResult result) throws IOException {

        final String json = result.getResponse().getContentAsString();

        return MAPPER.readValue(json, User.class);
    }

    public static void assertConstraintViolation(ResultActions resultActions) throws Exception {

        assertDataError(resultActions, containsString("ConstraintViolationException"));
    }

    public static void assertValidationError(ResultActions resultActions) throws Exception {

        assertDataError(resultActions, containsString("PropertyValueException"));
    }

    public static void assertDataError(ResultActions resultActions, Matcher<String> messageMatcher) throws Exception {

        assertBadRequest(resultActions, equalTo("DataIntegrityViolationException"), messageMatcher);
    }

    public static void assertMissingBody(ResultActions resultActions) throws Exception {

        assertBadRequest(resultActions, equalTo("HttpMessageNotReadableException"),
                containsString("Required request body content is missing"));
    }

    public static void assertBadRequest(ResultActions resultActions, Matcher<String> errorMatcher,
                                        Matcher<String> messageMatcher) throws Exception {

        assertError(resultActions.andExpect(status().isBadRequest()), errorMatcher, messageMatcher);
    }

    public static void assertNoFound(ResultActions resultActions, Matcher<String> errorMatcher,
                                     Matcher<String> messageMatcher) throws Exception {

        assertError(resultActions.andExpect(status().isNotFound()), errorMatcher, messageMatcher);
    }

    public static void assertError(ResultActions resultActions, Matcher<String> errorMatcher,
                                   Matcher<String> messageMatcher) throws Exception {

        resultActions
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("error").value(errorMatcher))
                .andExpect(jsonPath("message").value(messageMatcher));
    }

    public static class UserMatcher extends TypeSafeMatcher<JSONObject> {

        private final User user;

        public UserMatcher(User user) {
            this.user = user;
        }

        @Override
        public boolean matchesSafely(JSONObject item) {

            if (!getValue(item, "email").equals(user.getEmail())) {
                return false;
            }

            if (!getValue(item, "firstName").equals(user.getFirstName())) {
                return false;
            }

            if (!getValue(item, "lastName").equals(user.getLastName())) {
                return false;
            }

            if (!getValue(item, "phoneNumber").equals(user.getPhoneNumber())) {
                return false;
            }

            return true;
        }

        private static String getValue(JSONObject item, String key) {

            final Object value = item.get(key);

            if (null == value) {
                return "";
            }

            return value.toString();
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(userMap());
        }

        private Map<String, Object> userMap() {

            final Map<String, Object> map = new HashMap<>();
            map.put("email", user.getEmail());
            map.put("firstName", user.getFirstName());
            map.put("lastName", user.getLastName());

            return map;
        }
    }
}
