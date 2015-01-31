package example.rest.spring.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * This class can be extended by any {@link javax.persistence.Entity} that needs an Id.
 *
 * @author Karl Bennett
 */
@MappedSuperclass
public class Id implements Serializable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A default constructor is required by serialisation and ORM API's.
     */
    Id() {
    }

    public Id(Id id) {
        this(id.getId());
    }

    public Id(Long id) {
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (!(object instanceof Id)) {
            return false;
        }

        final Id that = (Id) object;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {

        return "Id {" +
                "id = " + id +
                '}';
    }
}
