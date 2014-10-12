package scratch.spring.webapp.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static scratch.spring.webapp.data.Users.user;

public class UserSteps {

    private final DBUnitUserRepository dbUnitRepository;

    public UserSteps(DBUnitUserRepository dbUnitRepository) {

        this.dbUnitRepository = dbUnitRepository;
    }

    public void all_users_are_cleaned_up() {

        dbUnitRepository.deleteAll();
    }

    public void then_the_user_should_be_created(User user) {

        assertEquals("the user should be created.", user, dbUnitRepository.findOne(user.getId()));
    }

    public void then_the_user_should_be_updated(User user) {

        assertEquals("the user should be updated.", user, dbUnitRepository.findOne(user.getId()));
    }

    public User given_a_user_has_been_persisted() {

        final User user = dbUnitRepository.save(user());

        assertTrue("the user should exist.", dbUnitRepository.exists(user.getId()));

        return user;
    }

    public void then_the_persisted_user_should_be_able_to_be_retrieved(User user) {

        assertEquals("the retrieved user should be correct.", dbUnitRepository.findOne(user.getId()), user);
    }

    public void then_all_persisted_users_should_be_retrieved(Iterable<User> all) {

        assertEquals("all the persisted users should be retrieved.", dbUnitRepository.findAll(), all);
    }

    public void then_the_user_should_no_longer_be_persisted(User user) {

        assertFalse("the user should no longer be persisted.", dbUnitRepository.exists(user.getId()));
    }
}
