package scratch.spring.webapp.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.spring.webapp.config.TestScratchConfiguration;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static scratch.spring.webapp.data.Users.userOne;
import static scratch.spring.webapp.data.Users.userThree;
import static scratch.spring.webapp.data.Users.userTwo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestScratchConfiguration.class)
@WebAppConfiguration("classpath:")
public class UserTest implements UserTestTemplate {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserSteps steps;

    private User persistedUser;

    @Before
    public void setUp() {

        steps.all_users_are_cleaned_up();

        persistedUser = steps.given_a_user_has_been_persisted();
    }

    @Test
    public void I_can_access_the_injectable_user_repository_from_user_static_context() {

        assertEquals("the repository should be correct.", repository, User.getStaticRepository());
    }

    @Test(expected = IllegalStateException.class)
    public void I_cannot_set_the_repository_in_the_users_static_context_once_it_has_been_configured() {

        User.setStaticRepository(repository);
    }

    @Test
    public void I_can_create_a_user() {

        final User user = userOne();

        user.create();

        steps.then_the_user_should_be_created(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_create_the_same_user_twice() {

        persistedUser.setId(null);
        persistedUser.create();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_create_two_users_with_the_same_email() {

        final User user = userOne();
        user.setEmail(persistedUser.getEmail());

        user.create();
    }

    @Test
    public void I_can_create_two_users_with_the_same_first_name() {

        final User user = userOne();
        user.setFirstName(persistedUser.getFirstName());

        user.create();

        steps.then_the_user_should_be_created(user);
    }

    @Test
    public void I_can_create_two_users_with_the_same_last_name() {

        final User user = userOne();
        user.setLastName(persistedUser.getLastName());

        user.create();

        steps.then_the_user_should_be_created(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_create_a_user_with_a_null_email() {

        final User user = userOne();
        user.setEmail(null);

        user.create();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_create_a_user_with_a_null_first_name() {

        final User user = userOne();
        user.setFirstName(null);

        user.create();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_create_a_user_with_a_null_last_name() {

        final User user = userOne();
        user.setLastName(null);

        user.create();
    }

    @Test
    public void I_can_retrieve_a_user() {

        steps.then_the_persisted_user_should_be_able_to_be_retrieved(User.retrieve(persistedUser.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_retrieve_a_user_with_an_invalid_id() {

        User.retrieve(-1L);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void I_cannot_retrieve_a_user_with_a_null_id() {

        User.retrieve(null);
    }

    @Test
    public void I_can_retrieve_all_the_persisted_users() {

        userOne().create();
        userTwo().create();
        userThree().create();

        steps.then_all_persisted_users_should_be_retrieved(User.all());
    }

    @Test
    public void I_can_update_a_user() {

        persistedUser.setEmail("updated@email.com");
        persistedUser.setFirstName("Updated");
        persistedUser.setLastName("Different");

        persistedUser.update();

        steps.then_the_user_should_be_updated(persistedUser);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_update_a_user_to_be_equal_to_an_existing_user() {

        final User user = userOne();
        user.create();

        user.setEmail(persistedUser.getEmail());
        user.setFirstName(persistedUser.getFirstName());
        user.setLastName(persistedUser.getLastName());

        user.update();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_update_a_user_to_have_the_same_email_as_an_existing_user() {

        final User user = userOne();
        user.create();

        user.setEmail(persistedUser.getEmail());

        user.update();
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_first_name_as_an_existing_user() {

        final User user = userOne();
        user.create();

        user.setFirstName(persistedUser.getFirstName());

        user.update();

        steps.then_the_user_should_be_updated(user);
    }

    @Test
    public void I_can_update_a_user_to_have_the_same_last_name_as_an_existing_user() {

        final User user = userOne();
        user.create();

        user.setLastName(persistedUser.getLastName());

        user.update();

        steps.then_the_user_should_be_updated(user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_update_a_user_with_a_null_id() {

        persistedUser.setId(null);

        persistedUser.update();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_update_a_user_with_a_null_email() {

        persistedUser.setEmail(null);

        persistedUser.update();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_update_a_user_with_a_null_first_name() {

        persistedUser.setFirstName(null);

        persistedUser.update();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void I_cannot_update_a_user_with_a_null_last_name() {

        persistedUser.setLastName(null);

        persistedUser.update();
    }

    @Test
    public void I_can_delete_a_user() {

        persistedUser.delete();

        steps.then_the_user_should_no_longer_be_persisted(persistedUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_delete_a_user_with_an_invalid_id() {

        persistedUser.setId(-1L);

        persistedUser.delete();
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void I_cannot_delete_a_user_with_a_null_id() {

        persistedUser.setId(null);

        persistedUser.delete();
    }

    @Test
    public void I_can_check_the_equality_of_a_user() {

        final User left = userOne();
        final User right = userOne();

        assertEquals("a user is equal to it's self.", left, left);
        assertEquals("a user is equal to another user with the same data.", left, right);

        final User differentIdUser = userOne();
        differentIdUser.setId(-1L);

        assertNotEquals("a user is not equal to a user with a different id.", left, differentIdUser);

        final User differentEmailUser = userOne();
        differentEmailUser.setEmail("different");

        assertNotEquals("a user is not equal to a user with a different email.", left, differentEmailUser);

        final User differentFirstNameUser = userOne();
        differentFirstNameUser.setFirstName("different");

        assertNotEquals("a user is not equal to a user with a different first name.", left, differentFirstNameUser);

        final User differentLastNameUser = userOne();
        differentLastNameUser.setLastName("different");

        assertNotEquals("a user is not equal to a user with a different last name.", left, differentLastNameUser);

        assertNotEquals("a user is not equal to an object.", left, new Object());

        assertNotEquals("a user is not equal to null.", left, null);
    }

    @Test
    public void I_can_check_the_has_code_of_a_user() {

        final User left = userOne();

        assertEquals("a users hash code is equal to the hash code of another user with the same data.", left.hashCode(),
                userOne().hashCode());

        assertNotEquals("a users hash code is not equal to the hash code of another user with a different data.",
                left.hashCode(), userThree().hashCode());
    }

    @Test
    public void I_can_to_string_a_user() {

        assertEquals("the user should produce the correct toString value.",
                format(
                        "User {id = %d, email = '%s', firstName = '%s', lastName = '%s'}",
                        persistedUser.getId(),
                        persistedUser.getEmail(),
                        persistedUser.getFirstName(),
                        persistedUser.getLastName()
                ),
                persistedUser.toString());
    }
}
