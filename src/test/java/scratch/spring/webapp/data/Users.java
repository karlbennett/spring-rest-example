package scratch.spring.webapp.data;

/**
 * Data for user persistence tests.
 *
 * @author Karl Bennett
 */
public class Users {

    public static final String EMAIL = "test@email.com";
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "User";

    public static final String EMAIL_ONE = "test@email.one";
    public static final String FIRST_NAME_ONE = "Test1";
    public static final String LAST_NAME_ONE = "User1";

    public static final String EMAIL_TWO = "test@email.two";
    public static final String FIRST_NAME_TWO = "Test2";
    public static final String LAST_NAME_TWO = "User2";

    public static final String EMAIL_THREE = "test@email.three";
    public static final String FIRST_NAME_THREE = "Test3";
    public static final String LAST_NAME_THREE = "User3";

    public static User user() {

        return new User(EMAIL, FIRST_NAME, LAST_NAME);
    }

    public static User user(UserRepository repository) {

        return new User(repository, EMAIL, FIRST_NAME, LAST_NAME);
    }

    public static User userOne() {

        return new User(EMAIL_ONE, FIRST_NAME_ONE, LAST_NAME_ONE);
    }

    public static User userTwo() {

        return new User(EMAIL_TWO, FIRST_NAME_TWO, LAST_NAME_TWO);
    }

    public static User userThree() {

        return new User(EMAIL_THREE, FIRST_NAME_THREE, LAST_NAME_THREE);
    }
}
