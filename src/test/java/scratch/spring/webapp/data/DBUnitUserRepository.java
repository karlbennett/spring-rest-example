package scratch.spring.webapp.data;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.operation.DatabaseOperation;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * This user repository has been implemented with DBUnit so that an alternate method other than the production code is
 * used to populating the database with test data.
 */
public class DBUnitUserRepository {

    public static final String USER = "User";

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public final DataSource dataSource;

    public DBUnitUserRepository(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public <S extends User> S save(S user) {

        createUser(user.getEmail(), user.getFirstName(), user.getLastName());

        return (S) map(retrieveUser(user.getEmail()));
    }

    public User findOne(Long id) {

        return map(retrieveUser(id));
    }

    public boolean exists(Long id) {

        return 1 == retrieveUser(id).getRowCount();
    }

    private static User map(final ITable table) {

        return map(0, table);
    }

    private static User map(final int index, final ITable table) {

        return wrapCheckedException(new Callable<User>() {
            @Override
            public User call() throws DataSetException {

                final Long id = Long.valueOf(table.getValue(index, ID).toString());
                final String email = table.getValue(index, EMAIL).toString();
                final String firstName = table.getValue(index, FIRST_NAME).toString();
                final String lastName = table.getValue(index, LAST_NAME).toString();

                final User user = new User(email, firstName, lastName);
                user.setId(id);

                return user;
            }
        });
    }

    public void deleteAll() {

        clearUsers();
    }

    private void createUser(String email, String firstName, String lastName) {

        userOperation(DatabaseOperation.INSERT, null, email, firstName, lastName);
    }

    private void userOperation(final DatabaseOperation operation, final Long id, final String email,
                               final String firstName, final String lastName) {

        final DefaultTable userTable = userTable();

        wrapCheckedException(new WithConnection<Void>() {
            @Override
            public Void call(IDatabaseConnection connection) throws Exception {

                userTable.addRow(new Object[]{id, email, firstName, lastName});

                operation.execute(connection, new DefaultDataSet(userTable));

                return null;
            }
        });
    }

    private ITable retrieveUser(final Long id) {

        return retrieve(format("SELECT * FROM User WHERE id = %d", id));
    }

    private ITable retrieveUser(final String email) {

        return retrieve(format("SELECT * FROM User WHERE email = '%s'", email));
    }

    private ITable retrieve(final String sql) {

        return wrapCheckedException(new WithConnection<ITable>() {
            @Override
            public ITable call(IDatabaseConnection connection) throws SQLException, DataSetException {

                return connection.createQueryTable(USER, sql);
            }
        });
    }

    private void clearUsers() {

        final DefaultTable userTable = userTable();

        wrapCheckedException(new WithConnection<Void>() {
            @Override
            public Void call(IDatabaseConnection connection) throws DatabaseUnitException, SQLException {

                DatabaseOperation.DELETE_ALL.execute(connection, new DefaultDataSet(userTable));

                return null;
            }
        });
    }

    private DefaultTable userTable() {

        final Column id = new Column(ID, DataType.BIGINT);
        final Column email = new Column(EMAIL, DataType.VARCHAR);
        final Column firstName = new Column(FIRST_NAME, DataType.VARCHAR);
        final Column lastName = new Column(LAST_NAME, DataType.VARCHAR);

        final Column[] columns = {id, email, firstName, lastName};

        return new DefaultTable(USER, columns);
    }

    private static <T> T wrapCheckedException(Callable<T> callable) {

        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private abstract class WithConnection<T> implements Callable<T> {

        @Override
        public T call() throws Exception {

            final IDatabaseConnection connection = new DatabaseConnection(dataSource.getConnection());

            try {
                return call(connection);
            } finally {
                connection.close();
            }
        }

        protected abstract T call(IDatabaseConnection connection) throws Exception;
    }
}
