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
@Configurable(preConstruction = true)
@Entity
public class User extends AbstractPersistable<Long> {

    @JsonIgnore
    @Transient
    @Autowired
    private UserRepository repository;

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

    public User(UserRepository repository) {
        this.repository = repository;
    }

    public User(Long id, String email, String firstName, String lastName) {

        this.setId(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(User user) {

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    public User(Long id) {

        if (!repository.exists(id)) throwNotFound();

        User user = repository.findOne(id);

        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }


    @JsonIgnore
    @Transient
    public User create() {

        if (exists()) throwExists();

        return repository.save(new User(getId(), getEmail(), getFirstName(), getLastName()));
    }

    @JsonIgnore
    @Transient
    public boolean exists() {

        return !isNew() && repository.exists(getId());
    }

    @JsonIgnore
    @Transient
    public Iterable<User> all() {

        return repository.findAll();
    }

    @JsonIgnore
    @Transient
    public User update() {

        if (!exists()) throwNotFound();

        return repository.save(this);
    }

    @JsonIgnore
    @Transient
    public User delete() {

        if (!exists()) throwNotFound();

        repository.delete(this);

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
}
