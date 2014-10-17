package scratch.spring.webapp.data;

import org.junit.Test;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddressTest {

    @Test
    public void I_can_check_the_equality_of_a_user() {

        final Equals<Address> eq = new Equals<Address>() {
            @Override
            public boolean equal(Address left, Object right) {
                return left.equals(right);
            }
        };

        I_can_check_the_equality_of_a_address(2L, 3, "different", new CreateWithId(), eq);
        I_can_check_the_equality_of_a_address(null, null, null, new CreateWithId(), eq);
        I_can_check_the_equality_of_a_address(2L, 3, "different", new CreateWithNull(), eq);
    }

    @Test
    public void I_can_check_the_hash_code_of_a_address() {

        final Equals<Address> eq = new Equals<Address>() {
            @Override
            public boolean equal(Address left, Object right) {

                if (null == right) {
                    return false;
                }

                return left.hashCode() == right.hashCode();
            }
        };

        I_can_check_the_equality_of_a_address(2L, 3, "different", new CreateWithId(), eq);
        I_can_check_the_equality_of_a_address(null, null, null, new CreateWithId(), eq);
        I_can_check_the_equality_of_a_address(2L, 3, "different", new CreateWithNull(), eq);
    }

    public static void I_can_check_the_equality_of_a_address(
            Long differentId, Integer differentInteger, String differentString, Create create, Equals<Address> eq) {

        final Address left = create.address();
        final Address right = create.address();

        assertTrue("a address is equal to it's self.", eq.equal(left, left));
        assertTrue("a address is equal to another address with the same data.", eq.equal(left, right));

        final Address differentIdAddress = create.address();
        differentIdAddress.setId(differentId);
        assertFalse("a address is not equal to a address with a different id.", eq.equal(left, differentIdAddress));

        final Address differentNumberAddress = create.address();
        differentNumberAddress.setNumber(differentInteger);
        assertFalse("a address is not equal to a address with a different number.",
                eq.equal(left, differentNumberAddress));

        final Address differentStreetAddress = create.address();
        differentStreetAddress.setStreet(differentString);
        assertFalse("a address is not equal to a address with a different street.",
                eq.equal(left, differentStreetAddress));

        final Address differentSuburbAddress = create.address();
        differentSuburbAddress.setSuburb(differentString);
        assertFalse("a address is not equal to a address with a different suburb.",
                eq.equal(left, differentSuburbAddress));

        final Address differentCityAddress = create.address();
        differentCityAddress.setCity(differentString);
        assertFalse("a address is not equal to a address with a different city.", eq.equal(left, differentCityAddress));

        final Address differentPostCodeAddress = create.address();
        differentPostCodeAddress.setPostcode(differentString);
        assertFalse("a address is not equal to a address with a different post code.",
                eq.equal(left, differentPostCodeAddress));

        assertFalse("a address is not equal to an object.", eq.equal(left, new Object()));

        assertFalse("a address is not equal to null.", eq.equal(left, null));
    }

    @Test
    public void I_can_to_string_a_address() {

        final Address address = Addresses.address();

        assertEquals("the address should produce the correct toString value.",
                format(
                        "Address {id = %d, number = %d, street = '%s', suburb = '%s', city = '%s', postcode = '%s'}",
                        address.getId(),
                        address.getNumber(),
                        address.getStreet(),
                        address.getSuburb(),
                        address.getCity(),
                        address.getPostcode()
                ),
                address.toString());
    }

    private static interface Create {

        public Address address();
    }

    private static class CreateWithId implements Create {

        @Override
        public Address address() {

            final Address address = Addresses.address();
            address.setId(1L);

            return address;
        }
    }

    private static class CreateWithNull implements Create {

        @Override
        public Address address() {

            return new Address();
        }
    }
}
