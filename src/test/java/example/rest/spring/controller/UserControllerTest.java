package example.rest.spring.controller;

import example.rest.spring.SpringBootRestServlet;
import example.rest.spring.data.Id;
import example.rest.spring.data.User;
import example.rest.spring.data.UserSteps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static example.rest.spring.data.Users.EMAIL_ONE;
import static example.rest.spring.data.Users.FIRST_NAME_ONE;
import static example.rest.spring.data.Users.LAST_NAME_ONE;
import static example.rest.spring.data.Users.userOne;
import static example.rest.spring.data.Users.userThree;
import static example.rest.spring.data.Users.userTwo;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootRestServlet.class)
@WebAppConfiguration("classpath:")
@IntegrationTest({"server.port=0", "management.port=0"})
public class UserControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserSteps steps;

    private User persistedUser;

    private WebTarget target;

    @Before
    public void setup() {

        steps.all_users_are_cleaned_up();

        persistedUser = steps.given_a_user_has_been_persisted();

        target = ClientBuilder.newClient().target(format("http://localhost:%d/rest/", port)).path("users");
    }

    @Test
    public void I_can_create_a_user() throws Exception {

        final User user = userOne();

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_create_a_user_with_an_id_and_the_id_is_ignored() throws Exception {

        final Long expected = 99999999999L;

        final User user = userOne();
        user.setId(expected);

        final Response response = create(user);

        final Id actual = steps.then_the_user_should_be_created(response, user);

        assertNotEquals(expected, actual.getId());
    }

    @Test
    public void I_cannot_create_a_user_with_no_data() throws Exception {

        final Response response = create("");

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_create_an_existing_user() throws Exception {

        final Response response = create(persistedUser);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_create_the_same_user_twice() throws Exception {

        final User user = userOne();

        create(user);
        final Response response = create(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_create_two_users_with_the_same_email() throws Exception {

        final User user = userOne();
        user.setEmail(persistedUser.getEmail());

        final Response response = create(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_can_create_two_users_with_the_same_first_name() throws Exception {

        final User user = userOne();
        user.setFirstName(persistedUser.getFirstName());

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_last_name() throws Exception {

        final User user = userOne();
        user.setLastName(persistedUser.getLastName());

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_phone_number() throws Exception {

        final User user = userOne();
        user.setPhoneNumber(persistedUser.getPhoneNumber());

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_address() throws Exception {

        final User user = userOne();

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_cannot_create_a_user_with_no_email() throws Exception {

        final User user = userOne();
        user.setEmail(null);

        final Response response = create(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_create_a_user_with_no_first_name() throws Exception {

        final User user = userOne();
        user.setFirstName(null);

        final Response response = create(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_create_a_user_with_no_last_name() throws Exception {

        final User user = userOne();
        user.setLastName(null);

        final Response response = create(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_can_create_a_user_with_no_phone_number() throws Exception {

        final User user = userOne();
        user.setPhoneNumber(null);

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_create_a_user_with_no_address() throws Exception {

        final User user = userOne();
        user.setAddress(null);

        final Response response = create(user);

        steps.then_the_user_should_be_created(response, user);
    }

    @Test
    public void I_can_retrieve_a_user() throws Exception {

        final Response response = retrieve(persistedUser.getId());

        assertStatus(OK, response);

        final User user = response.readEntity(User.class);

        assertEquals(persistedUser, user);
    }

    @Test
    public void I_cannot_retrieve_a_user_with_an_invalid_id() throws Exception {

        final Response notFound = retrieve(-1L);

        assertErrorResponse(NOT_FOUND, notFound);

        final Response badRequest = retrieve("invalid");

        assertErrorResponse(BAD_REQUEST, badRequest);
    }

    @Test
    public void I_can_retrieve_all_the_persisted_users() throws Exception {

        final List<User> expected = new ArrayList<>();
        expected.add(persistedUser);
        expected.add(steps.given_a_user_has_been_persisted(userOne()));
        expected.add(steps.given_a_user_has_been_persisted(userTwo()));
        expected.add(steps.given_a_user_has_been_persisted(userThree()));

        final Response response = retrieve();

        assertStatus(OK, response);

        final List<User> actual = response.readEntity(new GenericType<List<User>>() {
        });

        assertEquals(expected, actual);
    }

    @Test
    public void I_can_update_a_user() throws Exception {

        persistedUser.setEmail(EMAIL_ONE);
        persistedUser.setFirstName(FIRST_NAME_ONE);
        persistedUser.setLastName(LAST_NAME_ONE);

        final Response response = update(persistedUser);

        assertEmptyResponse(response);

        steps.then_the_user_should_be_updated(persistedUser);
    }

    @Test
    public void I_cannot_update_a_user_with_no_data() throws Exception {

        final Response response = update(persistedUser.getId(), "");

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_update_a_user_to_be_equal_to_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setEmail(persistedUser.getEmail());
        user.setFirstName(persistedUser.getFirstName());
        user.setLastName(persistedUser.getLastName());

        final Response response = update(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_cannot_update_a_user_to_have_the_same_email_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setEmail(persistedUser.getEmail());

        final Response response = update(user);

        assertErrorResponse(BAD_REQUEST, response);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_first_name_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setFirstName(persistedUser.getFirstName());

        final Response response = update(user);

        assertEmptyResponse(response);

        steps.then_the_user_should_be_updated(user);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_last_name_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setLastName(persistedUser.getLastName());

        final Response response = update(user);

        assertEmptyResponse(response);

        steps.then_the_user_should_be_updated(user);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_phone_number_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setPhoneNumber(persistedUser.getPhoneNumber());

        final Response response = update(user);

        assertEmptyResponse(response);

        steps.then_the_user_should_be_updated(user);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_address_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());

        final Response response = update(user);

        assertEmptyResponse(response);

        steps.then_the_user_should_be_updated(user);
    }

    @Test
    public void I_cannot_update_a_user_with_an_invalid_id() throws Exception {

        persistedUser.setId(-1L);

        final Response notFound = update(persistedUser);

        assertErrorResponse(NOT_FOUND, notFound);

        final Response badRequest = update("invalid", persistedUser);

        assertErrorResponse(BAD_REQUEST, badRequest);

        final Response forbidden = update("", persistedUser);

        assertStatus(FORBIDDEN, forbidden);
    }

    @Test
    public void I_cannot_update_a_user_with_no_email() throws Exception {

        persistedUser.setEmail(null);

        final Response notFound = update(persistedUser);

        assertErrorResponse(BAD_REQUEST, notFound);
    }

    @Test
    public void I_cannot_update_a_user_with_no_first_name() throws Exception {

        persistedUser.setFirstName(null);

        final Response notFound = update(persistedUser);

        assertErrorResponse(BAD_REQUEST, notFound);
    }

    @Test
    public void I_cannot_update_a_user_with_no_last_name() throws Exception {

        persistedUser.setLastName(null);

        final Response notFound = update(persistedUser);

        assertErrorResponse(BAD_REQUEST, notFound);
    }

    @Test
    public void I_can_delete_a_user() throws Exception {

        final Response response = delete(persistedUser);

        assertEmptyResponse(response);

        steps.then_the_user_should_no_longer_be_persisted(persistedUser);
    }

    @Test
    public void I_cannot_delete_a_user_with_an_invalid_id() throws Exception {

        final Response notFound = delete(-1L);

        assertErrorResponse(NOT_FOUND, notFound);

        final Response badRequest = delete("invalid");

        assertErrorResponse(BAD_REQUEST, badRequest);
    }

    @Test
    public void I_can_delete_all_users() throws Exception {

        final User userOne = steps.given_a_user_has_been_persisted(userOne());
        final User userTwo = steps.given_a_user_has_been_persisted(userTwo());
        final User userThree = steps.given_a_user_has_been_persisted(userThree());

        final Response response = delete();

        assertEmptyResponse(response);

        steps.then_the_user_should_no_longer_be_persisted(persistedUser);
        steps.then_the_user_should_no_longer_be_persisted(userOne);
        steps.then_the_user_should_no_longer_be_persisted(userTwo);
        steps.then_the_user_should_no_longer_be_persisted(userThree);
    }

    private Response create(Object user) {
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(entity(user, MediaType.APPLICATION_JSON_TYPE));
    }

    private Response retrieve(Long id) {
        return retrieve(id.toString());
    }

    private Response retrieve() {
        return retrieve("");
    }

    private Response retrieve(String id) {
        return target.path(id).request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    private Response update(User user) {
        return update(user.getId(), user);
    }

    private Response update(Long id, Object user) {
        return update(id.toString(), user);
    }

    private Response update(String id, Object user) {
        return target.path(id).request().put(entity(user, MediaType.APPLICATION_JSON_TYPE));
    }

    private Response delete(User user) {
        return delete(user.getId());
    }

    private Response delete(Long id) {
        return delete(id.toString());
    }

    private Response delete() {
        return delete("");
    }

    private Response delete(String id) {
        return target.path(id).request().delete();
    }

    private static void assertErrorResponse(Status status, Response response) {

        assertStatus(status, response);

        final Map<String, ?> error = response.readEntity(new GenericType<Map<String, ?>>() {
        });

        assertThat(error, hasKey("error"));
        assertThat(error, hasKey("message"));
    }

    private static void assertEmptyResponse(Response response) {

        assertStatus(NO_CONTENT, response);

        assertThat(response.readEntity(String.class), isEmptyString());
    }

    private static void assertStatus(Status status, Response response) {
        assertEquals(status.getReasonPhrase(), response.getStatusInfo().getReasonPhrase());
    }
}
