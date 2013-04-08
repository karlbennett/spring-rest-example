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
// This is a Spring Aspect annotation which will cause any dependencies annotated with @Autowired to be weaved into
// the classes definition so that it will contain the dependencies in every instantiation. The dependencies will be
// wired in before the constructor execution so that the dependency is available in constructors.
@Configurable(preConstruction = true)
public class User extends AbstractPersistable<Long> {

    @Autowired // Tell Spring to weave this dependency into the classes runtime definition.
    @JsonIgnore
    @Transient
    private transient UserRepository repository;

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
     *
     * Note: If Spring compile time weaving isn't used to inject the users repository then all the persistence methods
     * will fail with an {@link IllegalStateException}.
     */
    public User() {
    }

    /**
     * Create a new user with a repository that will be used for all the persistence operations.
     *
     * @param repository the persistence repository.
     */
    public User(UserRepository repository) {

        this.repository = repository;
    }

    /**
     * Create a new user with the supplied values.
     *
     * Note: If Spring compile time weaving isn't used to inject the users repository then all the persistence methods
     * will fail with an {@link IllegalStateException}.
     *
     * @param id the id of the user.
     * @param email the users email.
     * @param firstName the users first name.
     * @param lastName the users last name.
     */
    public User(Long id, String email, String firstName, String lastName) {

        this.setId(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Create a new user with the supplied values.
     *
     * @param repository the persistence repository.
     * @param id the id of the user.
     * @param email the users email.
     * @param firstName the users first name.
     * @param lastName the users last name.
     */
    public User(UserRepository repository, Long id, String email, String firstName, String lastName) {

        this(id, email, firstName, lastName);

        this.repository = repository;
    }

    /**
     * Copy constructor.
     *
     * @param user the user to copy.
     */
    public User(User user) {

        this.repository = user.repository;

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    /**
     * Instantiate a new {@code User} and populate it from a persisted user with the same ID.
     *
     * Note: If Spring compile time weaving isn't used to inject the users repository then all the persistence methods
     * will fail with an {@link IllegalStateException}.
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
    public Iterable<User> all() {

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
     * Have overridden this method to annotate it to tell Jackson and Hibernate to ignore it on serialisation.
     */
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {

        return super.isNew();
    }

    @JsonIgnore
    @Transient
    private UserRepository repository() {

        if (null == repository) {

            throw new IllegalStateException("The " + this.getClass().getName() + " repository cannot be null.");
        }

        return repository;
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


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return (getId() == null ? user.getId() == null : getId().equals(user.getId())) &&
                (email == null ? user.email == null : email.equals(user.email)) &&
                (firstName == null ? user.firstName == null : firstName.equals(user.firstName)) &&
                (lastName == null ? user.lastName != null : lastName.equals(user.lastName));
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();

        result = 31 * result + (getId() != null ? getId().hashCode() : 0);

        result = 31 * result + (email != null ? email.hashCode() : 0);

        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);

        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {

        return "User {" +
              /**/"id = '" + getId() + '\'' +
                ", email = '" + email + '\'' +
                ", firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                '}';
    }
}
