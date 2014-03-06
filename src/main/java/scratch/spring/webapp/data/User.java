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

        if (null != staticRepository) {

            throw new IllegalStateException("the User repository has already been set.");
        }

        staticRepository = repository;
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
     * @param email      the users email.
     * @param firstName  the users first name.
     * @param lastName   the users last name.
     */
    public User(UserRepository repository, String email, String firstName, String lastName) {

        this(repository);

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Create a new user with the supplied values. It will use the static {@code UserRepository} accessed from
     * {@link #getStaticRepository()} for CUD operations.
     *
     * @param email     the users email.
     * @param firstName the users first name.
     * @param lastName  the users last name.
     */
    public User(String email, String firstName, String lastName) {

        this(getStaticRepository(), email, firstName, lastName);
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

        return repository.save(new User(email, firstName, lastName));
    }

    /**
     * Retrieve all the users that have been persisted.
     *
     * @return all the persisted users.
     */
    @JsonIgnore
    @Transient
    public static User retrieve(long id) {

        return getStaticRepository().findOne(id);
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

        User user = (User) o;

        return (null == id ? null == user.id : id.equals(user.id)) &&
                (null == email ? null == user.email : email.equals(user.email)) &&
                (null == firstName ? null == user.firstName : firstName.equals(user.firstName)) &&
                (null == lastName ? null == user.lastName : lastName.equals(user.lastName));
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {

        return "User {" +
              /**/"id = '" + id + '\'' +
                ", email = '" + email + '\'' +
                ", firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                '}';
    }
}
