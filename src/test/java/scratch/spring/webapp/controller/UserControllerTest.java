package scratch.spring.webapp.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import scratch.spring.webapp.ScratchSpringBootRestServlet;
import scratch.spring.webapp.data.UserSteps;
import scratch.user.Address;
import scratch.user.User;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static scratch.spring.webapp.controller.Tests.assertBadRequest;
import static scratch.spring.webapp.controller.Tests.assertConstraintViolation;
import static scratch.spring.webapp.controller.Tests.assertMissingBody;
import static scratch.spring.webapp.controller.Tests.assertNoFound;
import static scratch.spring.webapp.controller.Tests.assertValidationError;
import static scratch.spring.webapp.controller.Tests.equalTo;
import static scratch.spring.webapp.controller.Tests.hasKeys;
import static scratch.spring.webapp.controller.Tests.id;
import static scratch.spring.webapp.controller.Tests.json;
import static scratch.spring.webapp.controller.Tests.user;
import static scratch.spring.webapp.data.Users.EMAIL_ONE;
import static scratch.spring.webapp.data.Users.FIRST_NAME_ONE;
import static scratch.spring.webapp.data.Users.LAST_NAME_ONE;
import static scratch.spring.webapp.data.Users.userOne;
import static scratch.spring.webapp.data.Users.userThree;
import static scratch.spring.webapp.data.Users.userTwo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScratchSpringBootRestServlet.class)
@WebAppConfiguration("classpath:")
public class UserControllerTest {

    @Autowired
    private UserSteps steps;

    @Autowired
    private WebApplicationContext wac;

    private User persistedUser;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        steps.all_users_are_cleaned_up();

        persistedUser = steps.given_a_user_has_been_persisted();

        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void I_can_create_a_user() throws Exception {

        final User user = userOne();

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_create_a_user_with_an_id_and_the_id_is_ignored() throws Exception {

        final User user = userOne();
        user.setId(99999999999L);

        final MvcResult result = mockMvc.perform(async(post("/users").content(json(user))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(not(equalTo(user.getId()))))
                .andReturn();

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_cannot_create_a_user_with_no_data() throws Exception {

        assertMissingBody(mockMvc.perform(post("/users").contentType(APPLICATION_JSON)));
    }

    @Test
    public void I_cannot_create_an_existing_user() throws Exception {

        assertConstraintViolation(mockMvc.perform(async(post("/users").content(json(persistedUser)))));
    }

    @Test
    public void I_cannot_create_the_same_user_twice() throws Exception {

        final User user = userOne();

        final String json = json(user);

        user(mockMvc.perform(async(post("/users").content(json)))
                .andExpect(status().isCreated())
                .andReturn());

        assertConstraintViolation(mockMvc.perform(async(post("/users").content(json))));
    }

    @Test
    public void I_cannot_create_two_users_with_the_same_email() throws Exception {

        final User user = userOne();
        user.setEmail(persistedUser.getEmail());

        assertConstraintViolation(mockMvc.perform(async(post("/users").content(json(user)))));
    }

    @Test
    public void I_can_create_two_users_with_the_same_first_name() throws Exception {

        final User user = userOne();
        user.setFirstName(persistedUser.getFirstName());

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_last_name() throws Exception {

        final User user = userOne();
        user.setLastName(persistedUser.getLastName());

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_phone_number() throws Exception {

        final User user = userOne();
        user.setPhoneNumber(persistedUser.getPhoneNumber());

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_address() throws Exception {

        final Address address = persistedUser.getAddress();
        address.setId(null); // Set the ID to null so the comparison doesn't fail from the new generated ID.

        final User user = userOne();
        user.setAddress(address);

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_cannot_create_a_user_with_no_email() throws Exception {

        final User user = userOne();
        user.setEmail(null);

        assertValidationError(mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json(user))),
                "email.null"
        );
    }

    @Test
    public void I_cannot_create_a_user_with_no_first_name() throws Exception {

        final User user = userOne();
        user.setFirstName(null);

        assertValidationError(mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json(user))),
                "firstName.null"
        );
    }

    @Test
    public void I_cannot_create_a_user_with_no_last_name() throws Exception {

        final User user = userOne();
        user.setLastName(null);

        assertValidationError(mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json(user))),
                "lastName.null"
        );
    }

    @Test
    public void I_can_create_a_user_with_no_phone_number() throws Exception {

        final User user = userOne();
        user.setPhoneNumber(null);

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_create_a_user_with_no_address() throws Exception {

        final User user = userOne();
        user.setAddress(null);

        final MvcResult result = assertUserCreated(post("/users"), user);

        steps.then_the_user_should_be_created(id(result), user);
    }

    @Test
    public void I_can_retrieve_a_user() throws Exception {

        mockMvc.perform(async(get(format("/users/%d", persistedUser.getId()))))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(equalTo(persistedUser)))
                .andExpect(jsonPath("$.address").value(equalTo(persistedUser.getAddress())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void I_cannot_retrieve_a_user_with_an_invalid_id() throws Exception {

        assertNoFound(mockMvc.perform(async(get("/users/-1"))), equalTo("EntityNotFoundException"),
                equalTo("A user with the ID (-1) could not be found."));

        assertBadRequest(mockMvc.perform(get("/users/invalid")), equalTo("TypeMismatchException"),
                containsString("NumberFormatException"));
    }

    @Test
    public void I_can_retrieve_all_the_persisted_users() throws Exception {

        final User userOne = steps.given_a_user_has_been_persisted(userOne());
        final User userTwo = steps.given_a_user_has_been_persisted(userTwo());
        final User userThree = steps.given_a_user_has_been_persisted(userThree());

        mockMvc.perform(async(get("/users")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(hasSize(4)))
                .andExpect(jsonPath("$[0]").value(equalTo(persistedUser)))
                .andExpect(jsonPath("$[0].address").value(equalTo(persistedUser.getAddress())))
                .andExpect(jsonPath("$[1]").value(equalTo(userOne)))
                .andExpect(jsonPath("$[1].address").value(equalTo(userOne.getAddress())))
                .andExpect(jsonPath("$[2]").value(equalTo(userTwo)))
                .andExpect(jsonPath("$[2].address").value(equalTo(userTwo.getAddress())))
                .andExpect(jsonPath("$[3]").value(equalTo(userThree)))
                .andExpect(jsonPath("$[3].address").value(equalTo(userThree.getAddress())));
    }

    @Test
    public void I_can_update_a_user() throws Exception {

        persistedUser.setEmail(EMAIL_ONE);
        persistedUser.setFirstName(FIRST_NAME_ONE);
        persistedUser.setLastName(LAST_NAME_ONE);

        assertUserUpdated(put(format("/users/%d", persistedUser.getId())), persistedUser);

        steps.then_the_user_should_be_updated(persistedUser);
    }

    @Test
    public void I_cannot_update_a_user_with_no_data() throws Exception {

        assertMissingBody(
                mockMvc.perform(put(format("/users/%d", persistedUser.getId())).contentType(APPLICATION_JSON)));
    }

    @Test
    public void I_cannot_update_a_user_to_be_equal_to_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setEmail(persistedUser.getEmail());
        user.setFirstName(persistedUser.getFirstName());
        user.setLastName(persistedUser.getLastName());

        assertConstraintViolation(mockMvc.perform(async(
                put(format("/users/%d", user.getId())).content(json(persistedUser)))));
    }

    @Test
    public void I_cannot_update_a_user_to_have_the_same_email_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setEmail(persistedUser.getEmail());

        assertConstraintViolation(mockMvc.perform(async(
                put(format("/users/%d", user.getId())).content(json(persistedUser)))));
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_first_name_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setFirstName(persistedUser.getFirstName());

        assertUserUpdated(put(format("/users/%d", user.getId())), user);

        steps.then_the_user_should_be_updated(persistedUser);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_last_name_as_an_existing_user() throws Exception {

        final User user = steps.given_a_user_has_been_persisted(userOne());
        user.setLastName(persistedUser.getLastName());

        assertUserUpdated(put(format("/users/%d", user.getId())), user);

        steps.then_the_user_should_be_updated(persistedUser);
    }

    @Test
    public void I_cannot_update_a_user_with_an_invalid_id() throws Exception {

        persistedUser.setId(-1L);

        assertNoFound(
                mockMvc.perform(async(put(format("/users/%d", persistedUser.getId())).content(json(persistedUser)))),
                equalTo("EntityNotFoundException"), equalTo("A user with the ID (-1) could not be found."));

        assertBadRequest(
                mockMvc.perform(put("/users/invalid").contentType(APPLICATION_JSON).content(json(persistedUser))),
                equalTo("TypeMismatchException"), containsString("NumberFormatException"));
    }

    @Test
    public void I_cannot_update_a_user_with_no_id() throws Exception {

        persistedUser.setEmail(EMAIL_ONE);

        mockMvc.perform(put("/users").content(json(persistedUser)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string(isEmptyString()));
    }

    @Test
    public void I_cannot_update_a_user_with_no_email() throws Exception {

        persistedUser.setEmail(null);

        assertValidationError(
                mockMvc.perform(put(format("/users/%d", persistedUser.getId()))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json(persistedUser))
                ),
                "email.null"
        );
    }

    @Test
    public void I_cannot_update_a_user_with_no_first_name() throws Exception {

        persistedUser.setFirstName(null);

        assertValidationError(
                mockMvc.perform(
                        put(format("/users/%d", persistedUser.getId()))
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(json(persistedUser))
                ),
                "firstName.null"
        );
    }

    @Test
    public void I_cannot_update_a_user_with_no_last_name() throws Exception {

        persistedUser.setLastName(null);

        assertValidationError(
                mockMvc.perform(put(format("/users/%d", persistedUser.getId()))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json(persistedUser))),
                "lastName.null"
        );
    }

    @Test
    public void I_can_delete_a_user() throws Exception {

        assertUserNoContent(delete(format("/users/%d", persistedUser.getId())));

        steps.then_the_user_should_no_longer_be_persisted(persistedUser);
    }

    @Test
    public void I_cannot_delete_a_user_with_an_invalid_id() throws Exception {

        assertNoFound(mockMvc.perform(async(delete("/users/-1"))), equalTo("EntityNotFoundException"),
                equalTo("A user with the ID (-1) could not be found."));

        assertBadRequest(mockMvc.perform(delete("/users/invalid")), equalTo("TypeMismatchException"),
                containsString("NumberFormatException"));
    }

    @Test
    public void I_cannot_delete_all_users() throws Exception {

        final User userOne = steps.given_a_user_has_been_persisted(userOne());
        final User userTwo = steps.given_a_user_has_been_persisted(userTwo());
        final User userThree = steps.given_a_user_has_been_persisted(userThree());

        assertUserNoContent(delete("/users"));

        steps.then_the_user_should_no_longer_be_persisted(persistedUser);
        steps.then_the_user_should_no_longer_be_persisted(userOne);
        steps.then_the_user_should_no_longer_be_persisted(userTwo);
        steps.then_the_user_should_no_longer_be_persisted(userThree);
    }

    private MvcResult assertUserCreated(MockHttpServletRequestBuilder builder, User user) throws Exception {

        return mockMvc.perform(async(builder.content(json(user))))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(hasKeys(singleton("id"))))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private void assertUserUpdated(MockHttpServletRequestBuilder builder, User user) throws Exception {

        assertUserNoContent(builder.content(json(user)));
    }

    private ResultActions assertUserNoContent(MockHttpServletRequestBuilder builder) throws Exception {

        return mockMvc.perform(async(builder))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(isEmptyString()));
    }

    private RequestBuilder async(MockHttpServletRequestBuilder requestBuilder)
            throws Exception {

        final MvcResult result = mockMvc.perform(requestBuilder.accept(APPLICATION_JSON).contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn();

        return asyncDispatch(result);
    }
}
