package scratch.spring.webapp.data;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@Component
public class DBUnit {

    public static final String USER = "User";

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    private final DataSource dataSource;

    @Autowired
    public DBUnit(DataSource dataSource) throws SQLException, DatabaseUnitException {

        this.dataSource = dataSource;
    }

    public void createUser(final String email, final String firstName, final String lastName) {

        final DefaultTable userTable = userTable();

        final IDatabaseConnection connection = connection();

        wrapCheckException(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                try {

                    userTable.addRow(new Object[]{null, email, firstName, lastName});

                    DatabaseOperation.INSERT.execute(connection, new DefaultDataSet(userTable));

                } finally {
                    closeConnection(connection);
                }

                return null;
            }
        });
    }

    public ITable retrieveUser(final Long id) {

        final IDatabaseConnection connection = connection();

        return wrapCheckException(new Callable<ITable>() {
            @Override
            public ITable call() throws Exception {

                try {

                    return connection.createQueryTable(USER, "SELECT * FROM User WHERE id = " + id);

                } finally {
                    closeConnection(connection);
                }
            }
        });
    }

    public ITable retrieveUsers() {

        final IDatabaseConnection connection = connection();

        return wrapCheckException(new Callable<ITable>() {
            @Override
            public ITable call() throws Exception {

                try {

                    IDataSet dataSet = connection.createDataSet();

                    return dataSet.getTable(USER);

                } finally {
                    closeConnection(connection);
                }
            }
        });
    }

    public void clearUsers() {

        final DefaultTable userTable = userTable();

        final IDatabaseConnection connection = connection();

        wrapCheckException(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                try {

                    DatabaseOperation.DELETE_ALL.execute(connection, new DefaultDataSet(userTable));

                } finally {
                    closeConnection(connection);
                }

                return null;
            }
        });
    }

    private IDatabaseConnection connection() {

        return wrapCheckException(new Callable<IDatabaseConnection>() {
            @Override
            public IDatabaseConnection call() throws Exception {
                return new DatabaseConnection(dataSource.getConnection());
            }
        });
    }

    private void closeConnection(final IDatabaseConnection connection) {

        wrapCheckException(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                connection.close();
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

    private static <T> T wrapCheckException(Callable<T> callable) {

        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
