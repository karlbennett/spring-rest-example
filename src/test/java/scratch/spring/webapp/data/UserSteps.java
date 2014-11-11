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

    public void then_the_user_should_be_created(Long id, User expected) {

        final User actual = dbUnitRepository.findOne(id);

        expected.setId(id);

        final Address address = expected.getAddress();
        if (null != address) {
            address.setId(actual.getAddress().getId());
        }

        assertEquals("the user should be created.", expected, actual);
    }

    public void then_the_user_should_be_updated(User user) {

        assertEquals("the user should be updated.", user, dbUnitRepository.findOne(user.getId()));
    }

    public User given_a_user_has_been_persisted() {

        return given_a_user_has_been_persisted(user());
    }

    public User given_a_user_has_been_persisted(User user) {

        final User persistedUser = dbUnitRepository.save(user);

        assertTrue("the user should exist.", dbUnitRepository.exists(persistedUser.getId()));

        return persistedUser;
    }

    public void then_the_user_should_no_longer_be_persisted(User user) {

        assertFalse("the user should no longer be persisted.", dbUnitRepository.exists(user.getId()));
    }
}
