/*
 * Copyright 2015 Karl Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.rest.spring.data;

import javax.ws.rs.core.Response;

import static example.rest.spring.data.Users.user;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
