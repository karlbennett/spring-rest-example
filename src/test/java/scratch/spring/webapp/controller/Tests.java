package scratch.spring.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import scratch.spring.webapp.data.Address;
import scratch.spring.webapp.data.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.containsString;
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

    public static User user(MvcResult result) throws UnsupportedEncodingException {

        final String json = result.getResponse().getContentAsString();

        return fromJson(json, User.class);
    }

    private static <T> T fromJson(String json, Class<T> type) {

        try {

            return MAPPER.readValue(json, type);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public static void assertConstraintViolation(ResultActions resultActions) throws Exception {

        assertDataError(resultActions, containsString("ConstraintViolationException"));
    }

    public static void assertValidationError(ResultActions resultActions) throws Exception {

        assertDataError(resultActions, containsString("PropertyValueException"));
    }

    public static void assertDataError(ResultActions resultActions, Matcher<String> messageMatcher) throws Exception {

        assertBadRequest(resultActions, Matchers.equalTo("DataIntegrityViolationException"), messageMatcher);
    }

    public static void assertMissingBody(ResultActions resultActions) throws Exception {

        assertBadRequest(resultActions, Matchers.equalTo("HttpMessageNotReadableException"),
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

    public static Matcher<JSONObject> equalTo(User user) {
        return new UserMatcher(user);
    }

    public static Matcher<JSONObject> equalTo(Address address) {
        return new AddressMatcher(address);
    }

    private static boolean isNotEqual(final Object expected, JSONObject item, String key) {

        return isNotEqual(expected, item.get(key));
    }

    private static boolean isNotEqual(final Object expected, Object actual) {

        if (null == expected) {
            return !(expected == actual);
        }

        if (null == actual) {
            return true;
        }

        if (expected.toString().equals(actual.toString())) {
            return false;
        }

        return true;
    }

    private static class UserMatcher extends TypeSafeMatcher<JSONObject> {

        private final User user;

        public UserMatcher(User user) {
            this.user = user;
        }

        @Override
        public boolean matchesSafely(JSONObject jsonObject) {

            // If the expected ID is null then this must be matching  against a create so the ID cannot be compared.
            if (null != user.getId() && isNotEqual(user.getId(), jsonObject, "id")) {
                return false;
            }

            if (isNotEqual(user.getEmail(), jsonObject, "email")) {
                return false;
            }

            if (isNotEqual(user.getFirstName(), jsonObject, "firstName")) {
                return false;
            }

            if (isNotEqual(user.getLastName(), jsonObject, "lastName")) {
                return false;
            }

            if (isNotEqual(user.getPhoneNumber(), jsonObject, "phoneNumber")) {
                return false;
            }

            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(userMap());
        }

        private Map<String, Object> userMap() {

            final Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("email", user.getEmail());
            map.put("firstName", user.getFirstName());
            map.put("lastName", user.getLastName());
            map.put("phoneNumber", user.getPhoneNumber());

            return map;
        }
    }

    private static class AddressMatcher extends BaseMatcher<JSONObject> {

        private final Address address;

        public AddressMatcher(Address address) {
            this.address = address;
        }

        @Override
        public boolean matches(Object item) {

            final JSONObject jsonObject = (JSONObject) item;

            if (noAddressSupplied()) {
                return noAddressReturned(jsonObject);
            }

            // If the expected ID is null then this must be matching  against a create so the ID cannot be compared.
            if (null != address.getId() && isNotEqual(address.getId(), jsonObject, "id")) {
                return false;
            }

            if (isNotEqual(address.getNumber(), jsonObject, "number")) {
                return false;
            }

            if (isNotEqual(address.getStreet(), jsonObject, "street")) {
                return false;
            }

            if (isNotEqual(address.getSuburb(), jsonObject, "suburb")) {
                return false;
            }

            if (isNotEqual(address.getCity(), jsonObject, "city")) {
                return false;
            }

            if (isNotEqual(address.getPostcode(), jsonObject, "postcode")) {
                return false;
            }

            return true;
        }

        private static boolean noAddressReturned(JSONObject item) {
            return null == item;
        }

        private boolean noAddressSupplied() {
            return null == address;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(addressMap());
        }

        private Map<String, Object> addressMap() {

            if (noAddressSupplied()) {
                return emptyMap();
            }

            final Map<String, Object> map = new HashMap<>();
            map.put("id", address.getId());
            map.put("number", address.getNumber());
            map.put("street", address.getStreet());
            map.put("suburb", address.getSuburb());
            map.put("city", address.getCity());
            map.put("postcode", address.getPostcode());

            return map;
        }
    }
}
