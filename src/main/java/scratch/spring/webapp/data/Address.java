package scratch.spring.webapp.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * A simple address class that is an aggregate of the {@link User} class.
 *
 * @author Karl Bennett
 */
@Entity
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Integer number;

    @Column
    private String street;

    @Column
    private String suburb;

    @Column
    private String city;

    @Column
    private String postcode;

    public Address() {
    }

    public Address(Integer number, String street, String suburb, String city, String postcode) {

        this.number = number;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.postcode = postcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }

        final Address address = (Address) o;

        if (city != null ? !city.equals(address.city) : address.city != null) {
            return false;
        }
        if (id != null ? !id.equals(address.id) : address.id != null) {
            return false;
        }
        if (number != null ? !number.equals(address.number) : address.number != null) {
            return false;
        }
        if (postcode != null ? !postcode.equals(address.postcode) : address.postcode != null) {
            return false;
        }
        if (street != null ? !street.equals(address.street) : address.street != null) {
            return false;
        }
        if (suburb != null ? !suburb.equals(address.suburb) : address.suburb != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (suburb != null ? suburb.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Address {" +
              /**/"id = " + id +
                ", number = " + number +
                ", street = '" + street + '\'' +
                ", suburb = '" + suburb + '\'' +
                ", city = '" + city + '\'' +
                ", postcode = '" + postcode + '\'' +
                '}';
    }
}
