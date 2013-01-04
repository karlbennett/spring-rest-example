package scratch.webapp.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A simple user class that contains an email, first, and last names. The email has been annotated to indicate it should
 * be unique. All attributes have been annotated to be not null.
 *
 * @author Karl Bennett
 */
@Entity
public class User extends AbstractPersistable<Long> {

    @NotNull(message = "email.null")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "firstName.null")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "lastName.null")
    @Column(nullable = false)
    private String lastName;


    public User() {
    }

    public User(Long id, String email, String firstName, String lastName) {

        this.setId(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
