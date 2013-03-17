package scratch.webapp;

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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.webapp.config.ScratchConfiguration;
import scratch.webapp.data.User;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ResourceUtils.getFile;

/**
 * @author Karl Bennett
 */
@RunWith(SpringJUnit4ClassRunner.class)
// This annotation tells Spring where to find the configuration file.
@ContextConfiguration(classes = ScratchConfiguration.class)
// This annotation enables the Spring MVC framework.
@WebAppConfiguration
public class AbstractDataTester {

    public static final String USER_TABLE = "user";

    public static final Long ID_ONE = 1L;
    public static final Long ID_TWO = 2L;
    public static final Long ID_THREE = 3L;
    public static final Long ID_FOUR = 4L;

    public static final String EMAIL_NAME_ONE = "test_user_one@test.com";
    public static final String EMAIL_NAME_TWO = "test_user_two@test.com";
    public static final String EMAIL_NAME_THREE = "test_user_three@test.com";
    public static final String EMAIL_NAME_FOUR = "test_user_four@test.com";

    public static final String FIRST_NAME_ONE = "One";
    public static final String FIRST_NAME_TWO = "Two";
    public static final String FIRST_NAME_THREE = "Three";
    public static final String FIRST_NAME_FOUR = "Four";

    public static final String LAST_NAME = "Test";

    public static final User USER_ONE = new User(ID_ONE, EMAIL_NAME_ONE, FIRST_NAME_ONE, LAST_NAME);
    public static final User USER_TWO = new User(ID_TWO, EMAIL_NAME_TWO, FIRST_NAME_TWO, LAST_NAME);
    public static final User USER_THREE = new User(ID_THREE, EMAIL_NAME_THREE, FIRST_NAME_THREE, LAST_NAME);

    public static final List<User> USERS = Arrays.asList(USER_ONE, USER_TWO, USER_THREE);

    public static final Column[] COLUMNS = {
            new Column("id", DataType.BIGINT),
            new Column("email", DataType.VARCHAR),
            new Column("firstName", DataType.VARCHAR),
            new Column("lastName", DataType.VARCHAR)
    };

    public static final ITable USER_FOUR_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_FOUR, EMAIL_NAME_FOUR, FIRST_NAME_FOUR, LAST_NAME);
    }};

    public static final ITable UPDATE_USER_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_TWO, EMAIL_NAME_FOUR, FIRST_NAME_TWO, LAST_NAME);
    }};


    /**
     * A {@link DefaultTable} that has an {@link #addRow(Object...)} method that doesn't throw a checked exception.
     */
    private static class DefaultTestTable extends DefaultTable {

        private DefaultTestTable(String tableName, Column[] columns) {
            super(tableName, columns);
        }

        /**
         * Add the supplied values as a row to the table.
         *
         * @param values the values to add.
         */
        public void addRow(Object... values) {

            try {

                super.addRow(values);

            } catch (DataSetException e) {

                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Convenience method that wraps the {@link org.dbunit.Assertion#assertEquals(ITable, ITable)} method to
     * differentiate it from the JUnit assertions.
     *
     * @param message       the assertion message that will be displayed on a failure.
     * @param expectedTable the table data that is expected.
     * @param actualTable   the actual table data.
     * @throws org.dbunit.DatabaseUnitException
     *          when the tables don't match.
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
    public void dataSetUp() throws Exception {

        connection = new DatabaseConnection(dataSource.getConnection());
        dataSet = new FlatXmlDataSetBuilder().build(getFile("classpath:testData.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }

    @After
    public void dataTearDown() throws Exception {

        truncateTable("user");
        connection.close();
    }


    /**
     * Truncate the table with the supplied name.
     *
     * @param tableName the name of the table to truncate.
     * @throws Exception when the truncation fails.
     */
    public void truncateTable(String tableName) throws Exception {

        DatabaseOperation.TRUNCATE_TABLE.execute(connection, new DefaultDataSet(new DefaultTable(tableName)));
    }

    /**
     * Get the row with the supplied {@code id} form the table with the supplied {@code name}.
     *
     * @param name the name of the table to request the row from.
     * @param id   the id of the row to request.
     * @return the table containing only the request rwo.
     * @throws Exception if the table or row could not be found.
     */
    public ITable getTableRow(String name, long id) throws Exception {

        // This is the name given to the DBUnit table object not the name of the table in the database.
        final String tableObjectName = "table";

        QueryDataSet dataSet = new QueryDataSet(connection);
        dataSet.addTable(tableObjectName, "SELECT * FROM " + name + " WHERE id = " + id);

        return dataSet.getTable(tableObjectName);
    }
}
