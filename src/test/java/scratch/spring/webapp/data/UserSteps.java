package scratch.spring.webapp.data;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
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

    public Id then_the_user_should_be_created(Response response, User expected) {

        assertEquals(CREATED.getReasonPhrase(), response.getStatusInfo().getReasonPhrase());

        final Id id = response.readEntity(Id.class);

        final User actual = dbUnitRepository.findOne(id);

        expected.setId(id.getId());

        assertEquals("the user should be created.", nullAddressId(expected), nullAddressId(actual));

        return id;
    }

    public void then_the_user_should_be_updated(User user) {

        assertEquals("the user should be updated.", nullAddressId(user), nullAddressId(dbUnitRepository.findOne(user)));
    }

    public User given_a_user_has_been_persisted() {

        return given_a_user_has_been_persisted(user());
    }

    public User given_a_user_has_been_persisted(User user) {

        final User persistedUser = dbUnitRepository.save(user);

        assertTrue("the user should exist.", dbUnitRepository.exists(persistedUser));

        return persistedUser;
    }

    public void then_the_user_should_no_longer_be_persisted(User user) {

        assertFalse("the user should no longer be persisted.", dbUnitRepository.exists(user));
    }

    private static User nullAddressId(User user) {

        final Address address = user.getAddress();

        if (null == address) {
            return new User(user);
        }

        final User updateUser = new User(user);

        address.setId(null);

        updateUser.setAddress(address);

        return updateUser;
    }
}
