package scratch.spring.webapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.dao.DataRetrievalFailureException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static java.lang.String.format;

/**
 * A simple user class that contains an email, first name, last name, and phone number. The email has been annotated to
 * indicate it should be unique. Some others have been annotated to be not null.
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
    @Size(min = 1)
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "firstName.null")
    @Size(min = 1)
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "lastName.null")
    @Size(min = 1)
    @Column(nullable = false)
    private String lastName;

    @Column
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "addressId")
    private Address address;


    /**
     * Create a new user with the supplied values. It will use the static {@code UserRepository} accessed from
     * {@link #getStaticRepository()} for CUD operations.
     */
    public User(String email, String firstName, String lastName, String phoneNumber, Address address) {

        this(getStaticRepository(), email, firstName, lastName, phoneNumber, address);
    }

    /**
     * Create a new user with the supplied repository and values.
     *
     * @param repository the repository to use for {@code User} CUD operations.
     */
    public User(UserRepository repository, String email, String firstName, String lastName, String phoneNumber,
                Address address) {

        this(repository);

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /**
     * A default constructor is required by serialisation and ORM API's.
     */
    public User() {

        this(getStaticRepository());
    }

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
     * Persist this user and populate it with a new ID.
     *
     * @return this user with a newly populated ID.
     * @throws EntityExistsException if the user ID clashes with an already persisted user.
     */
    @JsonIgnore
    @Transient
    public User create() {

        // Remove any ID's to make sure we actually attempt a create and not an update.
        id = null;

        if (null != address) {
            address.setId(null);
        }

        return repository.save(this);
    }

    /**
     * Retrieve the user with the supplied ID.
     *
     * @param id the ID of the user to retrieved.
     * @return the persisted user.
     * @throws EntityNotFoundException it the user does not exist.
     */
    @JsonIgnore
    @Transient
    public static User retrieve(Long id) {

        final User user = getStaticRepository().findOne(id);

        if (null == user) {
            throw new EntityNotFoundException(format("A user with the ID (%d) could not be found.", id));
        }

        return user;
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

        if (null == id || !repository.exists(id)) {
            throw new EntityNotFoundException(this + " has not yet been persisted.");
        }

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

        try {

            repository.delete(id);

        } catch (DataRetrievalFailureException e) {

            throw new EntityNotFoundException(this + " has not yet been persisted.");
        }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User that = (User) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (email != null ? !email.equals(that.email) : that.email != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) {
            return false;
        }
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) {
            return false;
        }
        if (address != null ? !address.equals(that.address) : that.address != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {

        return "User {" +
              /**/"id = " + id +
                ", email = '" + email + '\'' +
                ", firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", phoneNumber = '" + phoneNumber + '\'' +
                ", address = " + address +
                '}';
    }
}
