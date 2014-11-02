package scratch.spring.webapp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import scratch.user.Address;
import scratch.user.User;
import scratch.user.Users;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Repository
public class RepositoryUsers implements Users {

    private final UserRepository repository;

    @Autowired
    public RepositoryUsers(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long create(User user) {

        // Null out the ID's to make sure that an attempt is made at a create not an update.
        user.setId(null);

        final Address address = user.getAddress();
        if (null != address) {
            address.setId(null);
        }

        return repository.save(user).getId();
    }

    @Override
    public User retrieve(Long id) {

        checkExists(id);

        return repository.findOne(id);
    }

    @Override
    public Iterable<User> retrieve() {

        return repository.findAll();
    }

    @Override
    public void update(User user) {

        checkExists(user.getId());

        repository.save(user);
    }

    @Override
    public void delete(Long id) {

        checkExists(id);

        repository.delete(id);
    }

    @Override
    public void deleteAll() {

        repository.deleteAll();
    }

    private void checkExists(Long id) {

        if (!repository.exists(id)) {
            throw new EntityNotFoundException(format("A user with the ID (%d) could not be found.", id));
        }
    }
}
