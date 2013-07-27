package scratch.spring.webapp.test;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import scratch.spring.webapp.data.User;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class that contains all the static values and methods used by the tests.
 *
 * @author Karl Bennett
 */
public abstract class Utils {

    private Utils() {
    }

    public static final String USER_TABLE = "user";

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public static final Long ID_ONE = 1L;
    public static final Long ID_TWO = 2L;
    public static final Long ID_THREE = 3L;
    public static final Long ID_FOUR = 4L;

    public static final String EMAIL_ONE = "test_user_one@test.com";
    public static final String EMAIL_TWO = "test_user_two@test.com";
    public static final String EMAIL_THREE = "test_user_three@test.com";
    public static final String EMAIL_FOUR = "test_user_four@test.com";

    public static final String FIRST_NAME_ONE = "One";
    public static final String FIRST_NAME_TWO = "Two";
    public static final String FIRST_NAME_THREE = "Three";
    public static final String FIRST_NAME_FOUR = "Four";

    public static final String LAST_NAME_VALUE = "Test";

    public static final User USER_ONE = new User(ID_ONE, EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_VALUE);
    public static final User USER_TWO = new User(ID_TWO, EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_VALUE);
    public static final User USER_THREE = new User(ID_THREE, EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_VALUE);
    public static final User USER_FOUR = new User(ID_FOUR, EMAIL_FOUR, FIRST_NAME_FOUR, LAST_NAME_VALUE);

    public static final List<User> USERS = Arrays.asList(USER_ONE, USER_TWO, USER_THREE);

    public static final Column[] COLUMNS = {
            new Column("id", DataType.BIGINT),
            new Column("email", DataType.VARCHAR),
            new Column("firstName", DataType.VARCHAR),
            new Column("lastName", DataType.VARCHAR)
    };

    public static final ITable USER_FOUR_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_FOUR, EMAIL_FOUR, FIRST_NAME_FOUR, LAST_NAME_VALUE);
    }};

    public static final ITable UPDATE_USER_TABLE = new DefaultTestTable("user_four_table", COLUMNS) {{
        addRow(ID_TWO, EMAIL_FOUR, FIRST_NAME_TWO, LAST_NAME_VALUE);
    }};


    /**
     * A {@link org.dbunit.dataset.DefaultTable} that has an {@link #addRow(Object...)} method that doesn't throw a checked exception.
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
}
