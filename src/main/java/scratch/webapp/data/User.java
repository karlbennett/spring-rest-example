package scratch.webapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A simple user class that contains an email, first, and last names. The email has been annotated to indicate it should
 * be unique. All attributes have been annotated to be not null.
 *
 * @author Karl Bennett
 */
@Entity
public class User extends AbstractPersistable<Long> {

    private static class RepositoryHolder {

        public static final UserRepository REPOSITORY = new RepositoryInjector();
    }

    @JsonIgnore
    @Transient
    private static UserRepository repository() {

        return RepositoryHolder.REPOSITORY;
    }


    @NotNull(message = "email.null")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "firstName.null")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "lastName.null")
    @Column(nullable = false)
    private String lastName;


    /**
     * A default constructor is required by serialisation and ORM API's.
     */
    public User() {
    }

    public User(Long id, String email, String firstName, String lastName) {

        this.setId(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Copy constructor.
     *
     * @param user the user to copy.
     */
    public User(User user) {

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    /**
     * Instantiate a new {@code User} and populate it from a persisted user with the same ID.
     *
     * @param id the ID of the persisted user that will be used to populate this users fields.
     * @throws EntityNotFoundException if there is no persisted user with the supplied ID.
     */
    public User(Long id) {

        if (!repository().exists(id)) throwNotFound();

        User user = repository().findOne(id);

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }


    /**
     * Persist this user and populate it with a new ID.
     *
     * @return this user with a newly populated ID.
     * @throws EntityExistsException if the user ID clashes with an already persisted user.
     */
    @JsonIgnore
    @Transient
    public User create() {

        if (exists()) throwExists();

        return repository().save(new User(getId(), getEmail(), getFirstName(), getLastName()));
    }

    /**
     * Check if this user has been previously persisted.
     *
     * @return this user.
     */
    @JsonIgnore
    @Transient
    public boolean exists() {

        return !isNew() && repository().exists(getId());
    }

    /**
     * Retrieve all the users that have been persisted.
     *
     * @return all the persisted users.
     */
    @JsonIgnore
    @Transient
    public static Iterable<User> all() {

        return repository().findAll();
    }

    /**
     * Persist any updates to this user.
     *
     * @return the updated user.
     * @throws EntityNotFoundException if the user hasn't already been persisted.
     */
    @JsonIgnore
    @Transient
    public User update() {

        if (!exists()) throwNotFound();

        return repository().save(this);
    }

    /**
     * Delete this user.
     *
     * @return the deleted user.
     * @throws EntityNotFoundException if the user hasn't already been persisted.
     */
    @JsonIgnore
    @Transient
    public User delete() {

        if (!exists()) throwNotFound();

        repository().delete(this);

        return this;
    }


    /**
     * {@inheritDoc}
     * <p/>
     * Have overridden this method to annotate it to tell Jackson to ignore it on serialisation to JSON.
     */
    @JsonIgnore
    @Override
    public boolean isNew() {

        return super.isNew();
    }


    public void setId(Long id) {

        super.setId(id);
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public void throwNotFound() {

        throw new EntityNotFoundException("A user with id " + getId() + " does not exist.");
    }

    public void throwExists() {

        throw new EntityExistsException("A user with id " + getId() + " already exists.");
    }


    /**
     * A wrapper class for the {@link UserRepository} that will be injected with the actual {@code UserRepository}.
     */
    // This is a Spring Aspect annotation which will cause any dependencies annotated with @Autowired to be weaved into the
    // classes definition so that it will contain the dependencies in every instantiation.
    // The dependencies will be wired in before the constructor execution so that the dependency is available in
    // constructors.
    @Configurable(preConstruction = true)
    private static class RepositoryInjector implements UserRepository {

        @Autowired // Tell Spring to weave this dependency into the classes runtime definition.
        private UserRepository repository;

        @Override
        public <S extends User> S save(S user) {

            return repository.save(user);
        }

        @Override
        public <S extends User> Iterable<S> save(Iterable<S> users) {

            return repository.save(users);
        }

        @Override
        public User findOne(Long id) {

            return repository.findOne(id);
        }

        @Override
        public boolean exists(Long id) {

            return repository.exists(id);
        }

        @Override
        public Iterable<User> findAll() {

            return repository.findAll();
        }

        @Override
        public Iterable<User> findAll(Iterable<Long> ids) {

            return repository.findAll(ids);
        }

        @Override
        public long count() {

            return repository.count();
        }

        @Override
        public void delete(Long id) {

            repository.delete(id);
        }

        @Override
        public void delete(User user) {

            repository.delete(user);
        }

        @Override
        public void delete(Iterable<? extends User> users) {

            repository.delete(users);
        }

        @Override
        public void deleteAll() {

            repository.deleteAll();
        }
    }
}
