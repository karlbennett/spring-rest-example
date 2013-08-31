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
    private transient static UserRepository staticRepository;

    /**
     * @return the global {@link UserRepository} instance.
     */
    @JsonIgnore
    @Transient
    public static UserRepository getStaticRepository() {

        return staticRepository;
    }

    /**
     * Set the {@link UserRepository} that will be used for this lifecycle of the JVM.
     *
     * @param repository the {@link UserRepository} instance to use.
     * @throws IllegalStateException if a second attempt is made and setting the repository for a second time.
     */
    @JsonIgnore
    @Transient
    public static void setStaticRepository(UserRepository repository) {

        if (null != User.staticRepository) {

            throw new IllegalStateException("the User repository has already been set.");
        }

        User.staticRepository = repository;
    }

    @JsonIgnore
    @Transient
    private final UserRepository repository;

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
     * Create a new {@code User} that will use the supplied {@code UserRepository} to create, update, and delete it's
     * self.
     *
     * @param repository the repository to use for {@code User} CUD operations.
     */
    public User(UserRepository repository) {

        this.repository = repository;
    }

    /**
     * A default constructor is required by serialisation and ORM API's.
     */
    public User() {

        this(getStaticRepository());
    }

    /**
     * Create a new user with the supplied repository and values.
     *
     * @param repository the repository to use for {@code User} CUD operations.
     * @param id         the id of the user.
     * @param email      the users email.
     * @param firstName  the users first name.
     * @param lastName   the users last name.
     */
    public User(UserRepository repository, Long id, String email, String firstName, String lastName) {

        this(repository);

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Create a new user with the supplied values. It will use the static {@code UserRepository} accessed from
     * {@link #getStaticRepository()} for CUD operations.
     *
     * @param id        the id of the user.
     * @param email     the users email.
     * @param firstName the users first name.
     * @param lastName  the users last name.
     */
    public User(Long id, String email, String firstName, String lastName) {

        this(getStaticRepository(), id, email, firstName, lastName);
    }

    /**
     * Instantiate a new {@code User} and populate it from a persisted user with the same ID that has been retrieved
     * from the supplied {@code UserRepository}.
     *
     * @param repository the repository to use for {@code User} CUD operations.
     * @param id         the ID of the persisted user that will be used to populate this users fields.
     * @throws EntityNotFoundException if there is no persisted user with the supplied ID.
     */
    public User(UserRepository repository, Long id) {

        this(repository);

        User user = this.repository.findOne(id);

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    /**
     * Instantiate a new {@code User} and populate it from a persisted user with the same ID that has been retrieved
     * from the static {@code UserRepository}.
     *
     * @param id the ID of the persisted user that will be used to populate this users fields.
     */
    public User(Long id) {

        this(getStaticRepository(), id);
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

        return getStaticRepository().findAll();
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


    public UserRepository getRepository() {

        return repository;
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
