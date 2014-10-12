package scratch.spring.webapp.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import scratch.spring.webapp.config.TestScratchConfiguration;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserSteps;
import scratch.spring.webapp.data.UserTestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestScratchConfiguration.class)
@WebAppConfiguration("classpath:")
public class UserControllerTest implements UserTestTemplate {

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

    @After
    public void tearDown() {

    }

    @Test
    public void I_can_create_a_user() {

    }

    @Test
    public void I_cannot_create_the_same_user_twice() {

    }

    @Test
    public void I_cannot_create_two_users_with_the_same_email() {

    }

    @Test
    public void I_can_create_two_users_with_the_same_first_name() {

    }

    @Test
    public void I_can_create_two_users_with_the_same_last_name() {

    }

    @Test
    public void I_cannot_create_a_user_with_a_null_email() {

    }

    @Test
    public void I_cannot_create_a_user_with_a_null_first_name() {

    }

    @Test
    public void I_cannot_create_a_user_with_a_null_last_name() {

    }

    @Test
    public void I_can_retrieve_a_user() {

    }

    @Test
    public void I_cannot_retrieve_a_user_with_an_invalid_id() {

    }

    @Test
    public void I_cannot_retrieve_a_user_with_a_null_id() {

    }

    @Test
    public void I_can_retrieve_all_the_persisted_users() {

    }

    @Test
    public void I_can_update_a_user() {

    }

    @Test
    public void I_cannot_update_a_user_to_be_equal_to_an_existing_user() {

    }

    @Test
    public void I_cannot_update_a_user_to_have_the_same_email_as_an_existing_user() {

    }

    @Test
    public void I_can_update_a_user_to_have_the_same_first_name_as_an_existing_user() {

    }

    @Test
    public void I_can_update_a_user_to_have_the_same_last_name_as_an_existing_user() {

    }

    @Test
    public void I_cannot_update_a_user_with_a_null_id() {

    }

    @Test
    public void I_cannot_update_a_user_with_a_null_email() {

    }

    @Test
    public void I_cannot_update_a_user_with_a_null_first_name() {

    }

    @Test
    public void I_cannot_update_a_user_with_a_null_last_name() {

    }

    @Test
    public void I_can_delete_a_user() {

    }

    @Test
    public void I_cannot_delete_a_user_with_an_invalid_id() {

    }

    @Test
    public void I_cannot_delete_a_user_with_a_null_id() {

    }
}
