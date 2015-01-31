package example.rest.spring.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A simple user class that contains an email, first name, last name, phone number, and address. The email has been
 * annotated to indicate it should be unique. Some others have been annotated to be not null.
 *
 * @author Karl Bennett
 */
@Entity
public class User extends Id implements Serializable {

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
    @JoinColumn(name = "address_id")
    private Address address;

    /**
     * A default constructor is required by serialisation and ORM API's.
     */
    User() {
    }

    public User(String email, String firstName, String lastName, String phoneNumber, Address address) {
        this(null, email, firstName, lastName, phoneNumber, address);
    }

    public User(User user) {
        this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                user.getAddress());
    }

    public User(Long id, String email, String firstName, String lastName, String phoneNumber, Address address) {
        super(id);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setAddress(address);
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

        if (null == address) {
            return null;
        }

        return new Address(address);
    }

    public void setAddress(Address address) {

        this.address = null == address ? null : new Address(address);
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (!(object instanceof User)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }

        final User that = (User) object;

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

        int result = super.hashCode();
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
              /**/"id = " + getId() +
                ", email = '" + email + '\'' +
                ", firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", phoneNumber = '" + phoneNumber + '\'' +
                ", address = " + address +
                '}';
    }
}
