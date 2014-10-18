package scratch.spring.webapp.data;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;

import java.util.concurrent.Callable;

public class DBUnits {

    public static final String USER = "User";
    public static final String ADDRESS = "address";

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String ADDRESS_ID = "addressId";

    public static final String NUMBER = "number";
    public static final String STREET = "street";
    public static final String SUBURB = "suburb";
    public static final String CITY = "city";
    public static final String POSTCODE = "postcode";

    public static User mapUser(final ITable table) {

        return mapUser(table, 0);
    }

    public static User mapUser(ITable userTable, int index) {

        final Long id = Long.valueOf(columnValue(userTable, ID, index));
        final String email = columnValue(userTable, EMAIL, index);
        final String firstName = columnValue(userTable, FIRST_NAME, index);
        final String lastName = columnValue(userTable, LAST_NAME, index);
        final String phoneNumber = columnValue(userTable, PHONE_NUMBER, index);

        final User user = new User(email, firstName, lastName, phoneNumber, null);
        user.setId(id);

        return user;
    }

    public static Address mapAddress(ITable addressTable) {

        return mapAddress(addressTable, 0);
    }

    public static Address mapAddress(ITable addressTable, int index) {

        final Long id = Long.valueOf(columnValue(addressTable, ID, index));
        final Integer number = Integer.valueOf(columnValue(addressTable, NUMBER, index));
        final String street = columnValue(addressTable, STREET, index);
        final String suburb = columnValue(addressTable, SUBURB, index);
        final String city = columnValue(addressTable, CITY, index);
        final String postcode = columnValue(addressTable, POSTCODE, index);

        final Address address = new Address(number, street, suburb, city, postcode);
        address.setId(id);

        return address;
    }

    public static String columnValue(final ITable table, final String column) {

        return columnValue(table, column, 0);
    }

    public static String columnValue(final ITable table, final String column, final int row) {

        return wrapCheckedException(new Callable<String>() {
            @Override
            public String call() throws DataSetException {

                return table.getValue(row, column).toString();
            }
        });
    }

    public static DefaultTable userTable() {

        final Column id = new Column(ID, DataType.BIGINT);
        final Column email = new Column(EMAIL, DataType.VARCHAR);
        final Column firstName = new Column(FIRST_NAME, DataType.VARCHAR);
        final Column lastName = new Column(LAST_NAME, DataType.VARCHAR);
        final Column phoneNumber = new Column(PHONE_NUMBER, DataType.VARCHAR);
        final Column addressId = new Column(ADDRESS_ID, DataType.BIGINT);

        final Column[] columns = {id, email, firstName, lastName, phoneNumber, addressId};

        return new DefaultTable(USER, columns);
    }

    public static DefaultTable addressTable() {

        final Column id = new Column(ID, DataType.BIGINT);
        final Column number = new Column(NUMBER, DataType.INTEGER);
        final Column street = new Column(STREET, DataType.VARCHAR);
        final Column suburb = new Column(SUBURB, DataType.VARCHAR);
        final Column city = new Column(CITY, DataType.VARCHAR);
        final Column postcode = new Column(POSTCODE, DataType.VARCHAR);

        final Column[] columns = {id, number, street, suburb, city, postcode};

        return new DefaultTable(ADDRESS, columns);
    }

    public static <T> T wrapCheckedException(Callable<T> callable) {

        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
