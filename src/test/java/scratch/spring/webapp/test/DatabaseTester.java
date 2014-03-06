package scratch.spring.webapp.test;

import org.dbunit.dataset.ITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scratch.spring.webapp.data.User;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static scratch.spring.webapp.test.DBUnit.EMAIL;
import static scratch.spring.webapp.test.DBUnit.EMAIL_ONE;
import static scratch.spring.webapp.test.DBUnit.EMAIL_THREE;
import static scratch.spring.webapp.test.DBUnit.EMAIL_TWO;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME_ONE;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME_THREE;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME_TWO;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME_ONE;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME_THREE;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME_TWO;

/**
 * This class contains prebuilt tests that can be used to check database integration.
 */
@Component
public class DatabaseTester {

    private final DBUnit dbUnit;

    @Autowired
    public DatabaseTester(DBUnit dbUnit) {
        this.dbUnit = dbUnit;
    }

    public void setUp() {

        dbUnit.createUser(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE);
    }

    public void tearDown() {

        dbUnit.clearUsers();
    }

    public void createTest(WithUser withUser) throws Exception {

        assertEquals("only one user should exist before the create.", 1, dbUnit.retrieveUsers().getRowCount());

        final User user = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        final User createdUser = withUser.call(user);

        user.setId(createdUser.getId());

        assertEquals("created user should be correct.", user, createdUser);

        final ITable users = dbUnit.retrieveUsers();

        assertEquals("two users should exist after the create.", 2, users.getRowCount());

        assertEquals("the email should be correct for the second user.", EMAIL_TWO,
                users.getValue(1, EMAIL).toString());
        assertEquals("the first name should be correct for the second user.", FIRST_NAME_TWO,
                users.getValue(1, FIRST_NAME).toString());
        assertEquals("the last name should be correct for the second user.", LAST_NAME_TWO,
                users.getValue(1, LAST_NAME).toString());
    }

    public void createDuplicateTest(WithUser withUser) throws Exception {

        final User user = new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE);

        withUser.call(user);
        withUser.call(user);
    }

    public void createWithDuplicateEmailTest(WithUser withUser) throws Exception {

        final User userOne = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
        final User userTwo = new User(EMAIL_TWO, FIRST_NAME_THREE, LAST_NAME_THREE);

        withUser.call(userOne);
        withUser.call(userTwo);
    }

    public void createWithDuplicateFirstNameTest(WithUser withUser) throws Exception {

        final User userOne = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
        final User userTwo = new User(EMAIL_THREE, FIRST_NAME_TWO, LAST_NAME_THREE);

        withUser.call(userOne);

        final User user = withUser.call(userTwo);

        final ITable users = dbUnit.retrieveUser(user.getId());

        assertEquals("three users should exist after the create.", 3, dbUnit.retrieveUsers().getRowCount());

        assertEquals("the first name should be correct for the second user.", FIRST_NAME_TWO,
                users.getValue(0, FIRST_NAME).toString());
    }

    public void createWithDuplicateLastNameTest(WithUser withUser) throws Exception {

        final User userOne = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
        final User userTwo = new User(EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_TWO);

        withUser.call(userOne);

        final User user = withUser.call(userTwo);

        final ITable users = dbUnit.retrieveUser(user.getId());

        assertEquals("three users should exist after the create.", 3, dbUnit.retrieveUsers().getRowCount());

        assertEquals("the last name should be correct for the second user.", LAST_NAME_TWO,
                users.getValue(0, LAST_NAME).toString());
    }

    public void createWithNullEmailTest(WithUser withUser) throws Exception {

        final User user = new User(null, FIRST_NAME_ONE, LAST_NAME_ONE);

        withUser.call(user);
    }

    public void createWithNullFirstNameTest(WithUser withUser) throws Exception {

        final User user = new User(EMAIL_ONE, null, LAST_NAME_ONE);

        withUser.call(user);
    }

    public void createWithNullLastNameTest(WithUser withUser) throws Exception {

        final User user = new User(EMAIL_ONE, FIRST_NAME_ONE, null);

        withUser.call(user);
    }

    public void createWithNullUserTest(WithUser withUser) throws Exception {

        withUser.call(null);
    }

    public void retrieveTest(WithId withId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = withId.call(id);

        assertEquals("the email should be correct for the retrieved user.", EMAIL_ONE, user.getEmail());
        assertEquals("the first name should be correct for the retrieved user.", FIRST_NAME_ONE, user.getFirstName());
        assertEquals("the last name should be correct for the retrieved user.", LAST_NAME_ONE, user.getLastName());
    }

    public void retrieveWithInvalidIdTest(WithId withId) throws Exception {

        assertNull("no user should be returned.", withId.call(-1L));
    }

    public void retrieveWithNullIdTest(WithId withId) throws Exception {

        withId.call(null);
    }

    public void retrieveAllTest(Callable<Iterable<User>> callable) throws Exception {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
        dbUnit.createUser(EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_THREE);

        int count = 0;
        for (User user : callable.call()) {

            final ITable userTable = dbUnit.retrieveUser(user.getId());

            assertEquals("the email should be correct for the user " + count,
                    userTable.getValue(0, EMAIL).toString(), user.getEmail());
            assertEquals("the first name should be correct for the user " + count,
                    userTable.getValue(0, FIRST_NAME).toString(), user.getFirstName());
            assertEquals("the last name should be correct for the user " + count,
                    userTable.getValue(0, LAST_NAME).toString(), user.getLastName());

            count++;
        }

        assertEquals("three users should have been found.", 3, count);
    }

    public void updateTest(WithUserAndId withUserAndId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User();
        user.setEmail(EMAIL_TWO);
        user.setFirstName(FIRST_NAME_TWO);
        user.setLastName(LAST_NAME_TWO);

        assertEquals("updated user should be correct.", user, withUserAndId.call(id, user));

        final ITable userTable = dbUnit.retrieveUser(user.getId());

        assertEquals("the email should be correct for the retrieved user.", EMAIL_TWO, userTable.getValue(0, EMAIL));
        assertEquals("the first name should be correct for the retrieved user.", FIRST_NAME_TWO,
                userTable.getValue(0, FIRST_NAME));
        assertEquals("the last name should be correct for the retrieved user.", LAST_NAME_TWO,
                userTable.getValue(0, LAST_NAME));
    }

    public void updateDuplicateTest(WithUserAndId withUserAndId) throws Exception {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        withUserAndId.call(id, user);
    }

    public void updateWithDuplicateEmailTest(WithUserAndId withUserAndId) throws Exception {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(EMAIL_TWO, FIRST_NAME_THREE, LAST_NAME_THREE);

        withUserAndId.call(id, user);
    }

    public void updateWithDuplicateFirstNameTest(WithUserAndId withUserAndId) throws Exception {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        final Long id = dbUnit.retrieveUsersId();

        final User user = withUserAndId.call(id, new User(EMAIL_THREE, FIRST_NAME_TWO, LAST_NAME_THREE));

        final ITable users = dbUnit.retrieveUser(user.getId());

        assertEquals("two users should exist after the create.", 2, dbUnit.retrieveUsers().getRowCount());

        assertEquals("the first name should be correct for the second user.", FIRST_NAME_TWO,
                users.getValue(0, FIRST_NAME).toString());
    }

    public void updateWithDuplicateLastNameTest(WithUserAndId withUserAndId) throws Exception {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        final Long id = dbUnit.retrieveUsersId();

        final User user = withUserAndId.call(id, new User(EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_TWO));

        final ITable users = dbUnit.retrieveUser(user.getId());

        assertEquals("two users should exist after the create.", 2, dbUnit.retrieveUsers().getRowCount());

        assertEquals("the last name should be correct for the second user.", LAST_NAME_TWO,
                users.getValue(0, LAST_NAME).toString());
    }

    public void updateWithNullIdTest(WithUserAndId withUserAndId) throws Exception {

        final User user = new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);

        withUserAndId.call(null, user);

        assertEquals("two users should exist after the null id update.", 2, dbUnit.retrieveUsers().getRowCount());
    }

    public void updateWithNullEmailTest(WithUserAndId withUserAndId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(null, FIRST_NAME_ONE, LAST_NAME_ONE);

        withUserAndId.call(id, user);
    }

    public void updateWithNullFirstNameTest(WithUserAndId withUserAndId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(EMAIL_ONE, null, LAST_NAME_ONE);

        withUserAndId.call(id, user);
    }

    public void updateWithNullLastNameTest(WithUserAndId withUserAndId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(EMAIL_ONE, FIRST_NAME_ONE, null);

        withUserAndId.call(id, user);
    }

    public void updateWithNullUserTest(WithUserAndId withUserAndId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        withUserAndId.call(id, null);
    }

    public void deleteTest(WithId withId) throws Exception {

        final Long id = dbUnit.retrieveUsersId();

        final User user = new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE);
        user.setId(id);

        assertEquals("deleted user should be correct.", user, withId.call(id));

        assertEquals("no users should exist after the delete.", 0, dbUnit.retrieveUsers().getRowCount());
    }

    public void deleteWithInvalidIdTest(WithId withId) throws Exception {

        withId.call(-1L);
    }

    public void deleteWithNullIdTest(WithId withId) throws Exception {

        withId.call(null);
    }

    public static interface WithUser {

        public User call(User user) throws Exception;
    }

    public static interface WithId {

        public User call(Long id) throws Exception;
    }

    public static interface WithUserAndId {

        public User call(Long id, User user) throws Exception;
    }
}
