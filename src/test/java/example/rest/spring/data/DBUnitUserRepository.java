package example.rest.spring.data;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static example.rest.spring.data.DBUnits.ADDRESS;
import static example.rest.spring.data.DBUnits.ADDRESS_ID;
import static example.rest.spring.data.DBUnits.ID;
import static example.rest.spring.data.DBUnits.USER;
import static example.rest.spring.data.DBUnits.addressTable;
import static example.rest.spring.data.DBUnits.columnValue;
import static example.rest.spring.data.DBUnits.mapAddress;
import static example.rest.spring.data.DBUnits.mapUser;
import static example.rest.spring.data.DBUnits.userTable;
import static example.rest.spring.data.DBUnits.wrapCheckedException;
import static java.lang.String.format;

/**
 * This user repository has been implemented with DBUnit so that an alternate method other than the production code is
 * used to populating the database with test data.
 */
public class DBUnitUserRepository {

    public final DataSource dataSource;

    public DBUnitUserRepository(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public <S extends User> S save(S user) {

        final Address address = user.getAddress();

        if (null == address) {

            createUser(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), null);

            return (S) mapUser(retrieveUser(user.getEmail()));
        }

        createAddress(address.getNumber(), address.getStreet(), address.getSuburb(), address.getCity(),
                address.getPostcode());

        final ITable addressTable = retrieveAddress(address.getNumber(), address.getStreet(), address.getSuburb(),
                address.getCity(), address.getPostcode());

        createUser(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                Long.valueOf(columnValue(addressTable, ID)));

        final User createdUser = mapUser(retrieveUser(user.getEmail()));
        createdUser.setAddress(mapAddress(addressTable));

        return (S) createdUser;
    }

    public User findOne(Id id) {

        final ITable userTable = retrieveUser(id);

        final String addressId = columnValue(userTable, ADDRESS_ID);

        if (null == addressId) {
            return mapUser(userTable);
        }

        final ITable addressTable = retrieveAddress(Long.valueOf(addressId));

        final User user = mapUser(userTable);
        user.setAddress(mapAddress(addressTable));

        return user;
    }

    public boolean exists(Id id) {

        return 1 == retrieveUser(id).getRowCount();
    }

    public void deleteAll() {

        clearUsers();
        clearAddresses();
    }

    private void createUser(String email, String firstName, String lastName, String phoneNumber, Long addressId) {

        operation(userTable(), DatabaseOperation.INSERT, null, email, firstName, lastName, phoneNumber, addressId);
    }

    private void createAddress(Integer number, String street, String suburb, String city, String postcode) {

        operation(addressTable(), DatabaseOperation.INSERT, null, number, street, suburb, city, postcode);
    }

    private void operation(final DefaultTable table, final DatabaseOperation operation, final Object... values) {

        wrapCheckedException(new WithConnection<Void>() {
            @Override
            public Void call(IDatabaseConnection connection) throws Exception {

                table.addRow(values);

                operation.execute(connection, new DefaultDataSet(table));

                return null;
            }
        });
    }

    private ITable retrieveUser(Id id) {

        return retrieve(USER, format("SELECT * FROM User WHERE id = %d", id.getId()));
    }

    private ITable retrieveUser(String email) {

        return retrieve(USER, format("SELECT * FROM User WHERE email = '%s'", email));
    }

    private ITable retrieveAddress(Long id) {

        return retrieve(ADDRESS, format("SELECT * FROM Address WHERE id = %d", id));
    }

    private ITable retrieveAddress(Integer number, String street, String suburb, String city, String postcode) {

        return retrieve(ADDRESS, format(
  /**/"SELECT * FROM Address WHERE number = %d AND street = '%s' AND suburb = '%s' AND city = '%s' AND postcode = '%s'",
                number, street, suburb, city, postcode));
    }

    private ITable retrieve(final String tableName, final String sql) {

        return wrapCheckedException(new WithConnection<ITable>() {
            @Override
            public ITable call(IDatabaseConnection connection) throws SQLException, DataSetException {

                return connection.createQueryTable(tableName, sql);
            }
        });
    }

    private void clearUsers() {

        clear(userTable());
    }

    private void clearAddresses() {

        clear(addressTable());
    }

    private void clear(final DefaultTable table) {

        wrapCheckedException(new WithConnection<Void>() {
            @Override
            public Void call(IDatabaseConnection connection) throws DatabaseUnitException, SQLException {

                DatabaseOperation.DELETE_ALL.execute(connection, new DefaultDataSet(table));

                return null;
            }
        });
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
