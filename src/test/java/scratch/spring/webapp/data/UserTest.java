package scratch.spring.webapp.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static scratch.spring.webapp.data.Users.user;

public class UserTest {

    private static final Field STATIC_REPOSITORY_FIELD = getStaticRepositoryField();

    private static Field getStaticRepositoryField() {

        try {

            final Field staticRepositoryField = User.class.getDeclaredField("staticRepository");
            staticRepositoryField.setAccessible(true);

            return staticRepositoryField;

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);
        }
    }

    private UserRepository repository;

    private User user;
    private User userOne;

    @Before
    public void setUp() throws IllegalAccessException {

        repository = mock(UserRepository.class);

        STATIC_REPOSITORY_FIELD.set(null, repository);

        user = user(repository);

        userOne = mock(User.class);
    }

    @After
    public void tearDown() throws IllegalAccessException {

        // Must null static repository or the User class will not be able to be configured.
        STATIC_REPOSITORY_FIELD.set(null, null);
    }

    @Test
    public void I_can_instanciate_a_user() {

        new User();
    }

    @Test
    public void I_can_access_the_static_repository() {

        assertEquals("the repository should be correct.", repository, User.getStaticRepository());
    }

    @Test(expected = IllegalStateException.class)
    public void I_cannot_set_the_repository_in_the_users_static_context_once_it_has_been_configured() {

        User.setStaticRepository(repository);
    }

    @Test
    public void I_can_create_a_user() {

        when(repository.save(user)).thenReturn(userOne);

        assertEquals("the created user is returned.", userOne, user.create());
    }

    @Test
    public void the_users_id_is_set_to_null_when_created() {

        user.setId(1L);
        user.create();

        assertNull("the users id should be null.", user.getId());
    }

    @Test
    public void I_can_retrieve_a_user() {

        user.setId(1L);

        when(repository.findOne(user.getId())).thenReturn(userOne);

        assertEquals("the retrieved user is returned.", userOne, User.retrieve(user.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void an_error_occurs_when_no_user_is_retrieved() {

        User.retrieve(1L);
    }

    @Test
    public void I_can_retrieve_all_user() {

        @SuppressWarnings("unchecked")
        final List<User> users = mock(List.class);

        when(repository.findAll()).thenReturn(users);

        assertEquals("the retrieved users are returned.", users, User.all());
    }

    @Test
    public void I_can_update_a_user() {

        user.setId(1L);

        when(repository.save(user)).thenReturn(userOne);
        when(repository.exists(user.getId())).thenReturn(true);

        assertEquals("the updated user is returned.", userOne, user.update());
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_update_a_user_that_does_not_have_an_id() {

        user.setId(null);

        user.update();
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_update_a_user_that_does_not_exist() {

        user.setId(1L);

        when(repository.exists(user.getId())).thenReturn(false);

        user.update();
    }

    @Test
    public void I_can_delete_a_user() {

        assertEquals("the deleted user is returned.", user, user.delete());
    }

    @Test(expected = EntityNotFoundException.class)
    public void I_cannot_delete_a_user_that_does_not_exist() {

        user.setId(1L);

        doThrow(DataRetrievalFailureException.class).when(repository).delete(user.getId());

        user.delete();
    }

    @Test
    public void I_can_check_the_equality_of_a_user() {

        final Equals eq = new Equals() {
            @Override
            public boolean equal(User left, Object right) {
                return left.equals(right);
            }
        };

        I_can_check_the_equality_of_a_user(2L, "different", new CreateWithId(), eq);
        I_can_check_the_equality_of_a_user(null, null, new CreateWithId(), eq);
        I_can_check_the_equality_of_a_user(2L, "different", new CreateWithNull(), eq);
    }

    @Test
    public void I_can_check_the_hash_code_of_a_user() {

        final Equals eq = new Equals() {
            @Override
            public boolean equal(User left, Object right) {

                if (null == right) {
                    return false;
                }

                return left.hashCode() == right.hashCode();
            }
        };

        I_can_check_the_equality_of_a_user(2L, "different", new CreateWithId(), eq);
        I_can_check_the_equality_of_a_user(null, null, new CreateWithId(), eq);
        I_can_check_the_equality_of_a_user(2L, "different", new CreateWithNull(), eq);
    }

    public static void I_can_check_the_equality_of_a_user(Long differentId, String differentValue,
                                                          Create create, Equals eq) {

        final User left = create.user();
        final User right = create.user();

        assertTrue("a user is equal to it's self.", eq.equal(left, left));
        assertTrue("a user is equal to another user with the same data.", eq.equal(left, right));

        final User differentIdUser = create.user();
        differentIdUser.setId(differentId);
        assertFalse("a user is not equal to a user with a different id.", eq.equal(left, differentIdUser));

        final User differentEmailUser = create.user();
        differentEmailUser.setEmail(differentValue);
        assertFalse("a user is not equal to a user with a different email.", eq.equal(left, differentEmailUser));

        final User differentFirstNameUser = create.user();
        differentFirstNameUser.setFirstName(differentValue);
        assertFalse("a user is not equal to a user with a different first name.",
                eq.equal(left, differentFirstNameUser));

        final User differentLastNameUser = create.user();
        differentLastNameUser.setLastName(differentValue);
        assertFalse("a user is not equal to a user with a different last name.", eq.equal(left, differentLastNameUser));

        final User differentPhoneNumberUser = create.user();
        differentPhoneNumberUser.setPhoneNumber(differentValue);
        assertFalse("a user is not equal to a user with a different phone number.",
                eq.equal(left, differentPhoneNumberUser));

        assertFalse("a user is not equal to an object.", eq.equal(left, new Object()));

        assertFalse("a user is not equal to null.", eq.equal(left, null));
    }

    @Test
    public void I_can_to_string_a_user() {

        assertEquals("the user should produce the correct toString value.",
                format(
                        "User {id = %d, email = '%s', firstName = '%s', lastName = '%s', phoneNumber = '%s'}",
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber()
                ),
                user.toString());
    }

    private static interface Create {

        public User user();
    }

    private static class CreateWithId implements Create {

        @Override
        public User user() {

            final User user = Users.user();
            user.setId(1L);

            return user;
        }
    }

    private static class CreateWithNull implements Create {

        @Override
        public User user() {

            return new User();
        }
    }

    private static interface Equals {

        public boolean equal(User left, Object right);
    }
}
