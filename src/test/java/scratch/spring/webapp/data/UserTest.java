package scratch.spring.webapp.data;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scratch.spring.webapp.config.TestScratchConfiguration;

import static org.junit.Assert.assertEquals;
import static scratch.spring.webapp.data.DBUnit.EMAIL;
import static scratch.spring.webapp.data.DBUnit.FIRST_NAME;
import static scratch.spring.webapp.data.DBUnit.ID;
import static scratch.spring.webapp.data.DBUnit.LAST_NAME;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestScratchConfiguration.class)
public class UserTest {

    private static final String EMAIL_ONE = "test@email.one";
    private static final String FIRST_NAME_ONE = "Test1";
    private static final String LAST_NAME_ONE = "User1";

    private static final String EMAIL_TWO = "test@email.two";
    private static final String FIRST_NAME_TWO = "Test2";
    private static final String LAST_NAME_TWO = "User2";

    private static final String EMAIL_THREE = "test@email.three";
    private static final String FIRST_NAME_THREE = "Test3";
    private static final String LAST_NAME_THREE = "User3";

    @Autowired
    private DBUnit dbUnit;

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() {

        dbUnit.createUser(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE);
    }

    @After
    public void tearDown() {

        dbUnit.clearUsers();
    }

    @Test
    public void testGetStaticRepository() {

        assertEquals("the repository should be correct.", repository, User.getStaticRepository());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStaticRepository() {

        User.setStaticRepository(repository);
    }

    @Test
    public void testCreate() throws DataSetException {

        assertEquals("only one user should exist before the create", 1, dbUnit.retrieveUsers().getRowCount());

        new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO).create();

        final ITable users = dbUnit.retrieveUsers();

        assertEquals("two users should exist after the create", 2, users.getRowCount());

        assertEquals("the email should be correct for the second user.", EMAIL_TWO,
                users.getValue(1, EMAIL).toString());
        assertEquals("the first name should be correct for the second user.", FIRST_NAME_TWO,
                users.getValue(1, FIRST_NAME).toString());
        assertEquals("the last name should be correct for the second user.", LAST_NAME_TWO,
                users.getValue(1, LAST_NAME).toString());
    }

    @Test
    public void testRetrieve() throws DataSetException {

        final ITable users = dbUnit.retrieveUsers();

        final Long id = Long.valueOf(users.getValue(0, ID).toString());

        final User user = User.retrieve(id);

        assertEquals("the email should be correct for the retrieved user.", EMAIL_ONE, user.getEmail());
        assertEquals("the first name should be correct for the retrieved user.", FIRST_NAME_ONE, user.getFirstName());
        assertEquals("the last name should be correct for the retrieved user.", LAST_NAME_ONE, user.getLastName());
    }

    @Test
    public void testAll() throws DataSetException {

        dbUnit.createUser(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
        dbUnit.createUser(EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_THREE);

        int count = 0;
        for (User user : User.all()) {

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

    @Test
    public void testUpdate() {

    }

    @Test
    public void testDelete() {

    }

    @Test
    public void testGetRepository() {

    }

    @Test
    public void testGetId() {

    }

    @Test
    public void testSetId() {

    }

    @Test
    public void testGetEmail() {

    }

    @Test
    public void testSetEmail() {

    }

    @Test
    public void testGetFirstName() {

    }

    @Test
    public void testSetFirstName() {

    }

    @Test
    public void testGetLastName() {

    }

    @Test
    public void testSetLastName() {

    }

    @Test
    public void testEquals() {

    }

    @Test
    public void testHashCode() {

    }

    @Test
    public void testToString() {

    }
}
