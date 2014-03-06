package scratch.spring.webapp.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scratch.spring.webapp.config.TestScratchConfiguration;
import scratch.spring.webapp.test.DatabaseTester;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static scratch.spring.webapp.test.DBUnit.EMAIL_ONE;
import static scratch.spring.webapp.test.DBUnit.EMAIL_TWO;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME_ONE;
import static scratch.spring.webapp.test.DBUnit.FIRST_NAME_TWO;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME_ONE;
import static scratch.spring.webapp.test.DBUnit.LAST_NAME_TWO;
import static scratch.spring.webapp.test.DatabaseTester.WithId;
import static scratch.spring.webapp.test.DatabaseTester.WithUser;
import static scratch.spring.webapp.test.DatabaseTester.WithUserAndId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestScratchConfiguration.class)
public class UserTest {

    @Autowired
    private DatabaseTester databaseTester;

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() {

        databaseTester.setUp();
    }

    @After
    public void tearDown() {

        databaseTester.tearDown();
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
    public void testCreate() throws Exception {

        databaseTester.createTest(new WithUser() {
            @Override
            public User call(User user) throws Exception {
                return new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO).create();
            }
        });
    }

    @Test
    public void testRetrieve() throws Exception {

        databaseTester.retrieveTest(new WithId() {
            @Override
            public User call(Long id) throws Exception {
                return User.retrieve(id);
            }
        });
    }

    @Test
    public void testAll() throws Exception {

        databaseTester.retrieveAllTest(new Callable<Iterable<User>>() {
            @Override
            public Iterable<User> call() throws Exception {
                return User.all();
            }
        });
    }

    @Test
    public void testUpdate() throws Exception {

        databaseTester.updateTest(new WithUserAndId() {
            @Override
            public User call(Long id, User user) throws Exception {

                user.setId(id);

                return user.update();
            }
        });
    }

    @Test
    public void testDelete() throws Exception {

        databaseTester.deleteTest(new WithId() {
            @Override
            public User call(Long id) throws Exception {

                final User user = User.retrieve(id);

                return user.delete();
            }
        });
    }

    @Test
    public void testGetRepository() {

        assertEquals("the repository should be correct.", repository,
                new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO).getRepository());
    }

    @Test
    public void testEquals() {

        assertEquals("the users should be equal.", new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE),
                new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE));

        assertThat("the users should not be equal.", new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE),
                not(equalTo(new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO))));
    }

    @Test
    public void testHashCode() {

        assertEquals("the user hash codes should be equal.",
                new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE).hashCode(),
                new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE).hashCode());

        assertThat("the user hash codes should not be equal.",
                new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE).hashCode(),
                not(equalTo(new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO).hashCode())));
    }

    @Test
    public void testToString() {

        assertNotNull("toString should produce a string.",
                new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE).toString());
    }
}
