package scratch.spring.webapp.data;

import scratch.user.Address;

/**
 * Data for user persistence tests.
 *
 * @author Karl Bennett
 */
public class Addresses {

    public static final Integer NUMBER = 11;
    public static final String STREET = "Test Road";
    public static final String SUBURB = "Testerton";
    public static final String CITY = "Testopolis";
    public static final String POST_CODE = "TST123";

    public static final Integer NUMBER_ONE = 22;
    public static final String STREET_ONE = "Test1 Road";
    public static final String SUBURB_ONE = "Testerton1";
    public static final String CITY_ONE = "Testopolis1";
    public static final String POST_CODE_ONE = "TST124";

    public static final Integer NUMBER_TWO = 33;
    public static final String STREET_TWO = "Test Road2";
    public static final String SUBURB_TWO = "Testerton2";
    public static final String CITY_TWO = "Testopolis2";
    public static final String POST_CODE_TWO = "TST125";

    public static final Integer NUMBER_THREE = 44;
    public static final String STREET_THREE = "Test Road3";
    public static final String SUBURB_THREE = "Testerton3";
    public static final String CITY_THREE = "Testopolis3";
    public static final String POST_CODE_THREE = "TST126";

    public static Address address() {

        return new Address(NUMBER, STREET, SUBURB, CITY, POST_CODE);
    }

    public static Address addressOne() {

        return new Address(NUMBER_ONE, STREET_ONE, SUBURB_ONE, CITY_ONE, POST_CODE_ONE);
    }

    public static Address addressTwo() {

        return new Address(NUMBER_TWO, STREET_TWO, SUBURB_TWO, CITY_TWO, POST_CODE_TWO);
    }

    public static Address addressThree() {

        return new Address(NUMBER_THREE, STREET_THREE, SUBURB_THREE, CITY_THREE, POST_CODE_THREE);
    }
}
