package scratch.spring.webapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A simple user class that contains an email, first, and last names. The email has been annotated to indicate it should
 * be unique. All attributes have been annotated to be not null.
 *
 * @author Karl Bennett
 */
@Entity
public class User implements Serializable {

    @JsonIgnore
    @Transient
    private transient static UserRepository repository;

    /**
     * @return the global {@link UserRepository} instance.
     */
    @JsonIgnore
    @Transient
    public static UserRepository getRepository() {

        return repository;
    }

    /**
     * Set the {@link UserRepository} that will be used for this lifecycle of the JVM.
     *
     * @param repository the {@link UserRepository} instance to use.
     * @throws IllegalStateException if a second attempt is made and setting the repository for a second time.
     */
    @JsonIgnore
    @Transient
    public static void setRepository(UserRepository repository) {

        if (null != User.repository) {

            throw new IllegalStateException("the User repository has already been set.");
        }

        User.repository = repository;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    /**
     * Create a new user with the supplied values.
     * <p/>
     * Note: If Spring compile time weaving isn't used to inject the users repository then all the persistence methods
     * will fail with an {@link IllegalStateException}.
     *
     * @param id        the id of the user.
     * @param email     the users email.
     * @param firstName the users first name.
     * @param lastName  the users last name.
     */
    public User(Long id, String email, String firstName, String lastName) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Instantiate a new {@code User} and populate it from a persisted user with the same ID.
     * <p/>
     * Note: If Spring compile time weaving isn't used to inject the users repository then all the persistence methods
     * will fail with an {@link IllegalStateException}.
     *
     * @param id the ID of the persisted user that will be used to populate this users fields.
     * @throws EntityNotFoundException if there is no persisted user with the supplied ID.
     */
    public User(Long id) {

        User user = repository.findOne(id);

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

        return repository.save(new User(getId(), getEmail(), getFirstName(), getLastName()));
    }

    /**
     * Retrieve all the users that have been persisted.
     *
     * @return all the persisted users.
     */
    @JsonIgnore
    @Transient
    public static Iterable<User> all() {

        return repository.findAll();
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

        return repository.save(this);
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

        repository.delete(this);

        return this;
    }


    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
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
