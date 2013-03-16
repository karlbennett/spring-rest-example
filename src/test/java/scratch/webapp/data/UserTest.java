package scratch.webapp.data;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.webapp.config.ScratchConfiguration;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.util.ResourceUtils.getFile;

/**
 * @author Karl Bennett
 */
@RunWith(SpringJUnit4ClassRunner.class)
// This annotation tells Spring where to find the configuration file.
@ContextConfiguration(classes = ScratchConfiguration.class)
// This annotation enables the Spring MVC framework.
@WebAppConfiguration
public class UserTest {

    private static final Long ID_ONE = 1L;
    private static final Long ID_TWO = 2L;
    private static final Long ID_THREE = 3L;
    private static final Long ID_FOUR = 4L;

    private static final String EMAIL_NAME_ONE = "test_user_one@test.com";
    private static final String EMAIL_NAME_TWO = "test_user_two@test.com";
    private static final String EMAIL_NAME_THREE = "test_user_three@test.com";
    private static final String EMAIL_NAME_FOUR = "test_user_four@test.com";

    private static final String FIRST_NAME_ONE = "One";
    private static final String FIRST_NAME_TWO = "Two";
    private static final String FIRST_NAME_THREE = "Three";
    private static final String FIRST_NAME_FOUR = "Four";

    private static final String LAST_NAME = "Test";

    private static final User USER_ONE = new User(ID_ONE, EMAIL_NAME_ONE, FIRST_NAME_ONE, LAST_NAME);
    private static final User USER_TWO = new User(ID_TWO, EMAIL_NAME_TWO, FIRST_NAME_TWO, LAST_NAME);
    private static final User USER_THREE = new User(ID_THREE, EMAIL_NAME_THREE, FIRST_NAME_THREE, LAST_NAME);

    private static final List<User> USERS = Arrays.asList(USER_ONE, USER_TWO, USER_THREE);

    private static final Column[] COLUMNS = {
            new Column("id", DataType.BIGINT),
            new Column("email", DataType.VARCHAR),
            new Column("firstName", DataType.VARCHAR),
            new Column("lastName", DataType.VARCHAR)
    };

    private static final ITable USER_FOUR_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_FOUR, EMAIL_NAME_FOUR, FIRST_NAME_FOUR, LAST_NAME);
    }};

    private static final ITable UPDATE_USER_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_TWO, EMAIL_NAME_FOUR, FIRST_NAME_TWO, LAST_NAME);
    }};


    private static class DefaultTestTable extends DefaultTable {

        private DefaultTestTable(String tableName, Column[] columns) {
            super(tableName, columns);
        }

        public void addRow(Object... values) {

            try {

                super.addRow(values);

            } catch (DataSetException e) {

                throw new RuntimeException(e);
            }
        }
    }

    private static ITable getTableRow(IDatabaseConnection connection, long id) throws Exception {

        final String tableName = "table";

        QueryDataSet dataSet = new QueryDataSet(connection);
        dataSet.addTable(tableName, "SELECT * FROM user WHERE id = " + id);

        return dataSet.getTable(tableName);
    }

    /**
     * Convenience method that wraps the {@link Assertion#assertEquals(ITable, ITable)} method to differentiate it from
     * the JUnit assertions.
     *
     * @param message       the assertion message that will be displayed on a failure.
     * @param expectedTable the table data that is expected.
     * @param actualTable   the actual table data.
     * @throws DatabaseUnitException when the tables don't match.
     */
    public static void assertTableEquals(String message, ITable expectedTable, ITable actualTable)
            throws DatabaseUnitException {

        try {

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (DatabaseUnitException e) {

            throw new DatabaseUnitException(message, e);
        }
    }


    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection connection;
    private IDataSet dataSet;


    @Before
    public void setUp() throws Exception {

        connection = new DatabaseConnection(dataSource.getConnection());
        dataSet = new FlatXmlDataSetBuilder().build(getFile("classpath:testData.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }

    @After
    public void tearDown() throws Exception {

        DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataSet);
        connection.close();
    }


    @Test
    public void testConstructUserExistingId() throws Exception {

        assertEquals("user one should be populated correctly.", USER_ONE, new User(1L));
        assertEquals("user two should be populated correctly.", USER_TWO, new User(2L));
        assertEquals("user three should be populated correctly.", USER_THREE, new User(3L));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testConstructUserUnknownId() throws Exception {

        new User(-1L);
    }

    @Test
    public void testCreate() throws Exception {

        User user = new User();
        user.setEmail(EMAIL_NAME_FOUR);
        user.setFirstName(FIRST_NAME_FOUR);
        user.setLastName(LAST_NAME);

        user.create();

        assertTableEquals("the new user should be persisted.", USER_FOUR_TABLE, getTableRow(connection, 4L));
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateExistingUser() throws Exception {

        USER_ONE.create();
    }

    @Test
    public void testExists() throws Exception {

        assertTrue("user one should exist.", USER_ONE.exists());
        assertTrue("user two should exist.", USER_TWO.exists());
        assertTrue("user three should exist.", USER_THREE.exists());
    }

    @Test
    public void testAll() throws Exception {

        assertEquals("all the persisted users should be returned.", USERS, User.all());

        DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataSet);

        assertEquals("no persisted users should be returned.", Collections.<User>emptyList(), User.all());
    }

    @Test
    public void testUpdate() throws Exception {

        User user = new User(2L);

        user.setEmail(EMAIL_NAME_FOUR);

        user.update();

        assertTableEquals("the user should be updated.", UPDATE_USER_TABLE, getTableRow(connection, 2L));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateUnknownUser() throws Exception {

        User user = new User(-1L);

        user.update();
    }

    @Test
    public void testDelete() throws Exception {

        new User(3L).delete();

        ITable table = getTableRow(connection, 3L);

        assertEquals("the row with user three should not exists.", 0, table.getRowCount());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUnkownUser() throws Exception {

        new User(-1L).delete();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testThrowNotFound() throws Exception {

        new User().throwNotFound();
    }

    @Test(expected = EntityExistsException.class)
    public void testThrowExists() throws Exception {

        new User().throwExists();
    }
}
