package production.model;

import production.enums.City;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an Address of a Factory
 * Contains Builder Pattern for initialization.
 */
public class Address implements Serializable {

    private String street;
    private String houseNumber;
    private City city;

    /**
     * Builder Pattern for Address class
     */
    public static class Builder {
        private String street;
        private String houseNumber;
        private City city;

        /**
         * Constructor without parameters
         */
        public Builder() {}

        /**
         * Builder pattern constructor for street variable
         * @param street String street.
         * @return Builder class with street.
         */
        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        /**
         * Builder pattern constructor for house number.
         * @param houseNumber String house number
         * @return Builder class with house number.
         */
        public Builder withHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        /**
         * Builder pattern constructor for city.
         * @param city String city name.
         * @return Builder class with city.
         */
        public Builder withCity(City city) {
            this.city = city;
            return this;
        }

        /**
         * Builds Address class with given Builder arguments.
         *
         * @return Address object.
         */
        public Address build() {
            Address address = new Address();
            address.street = this.street;
            address.houseNumber = this.houseNumber;
            address.city = this.city;
            return address;
        }
    }

    /**
     * Private constructor for Address class.
     * Constructor is private because of Builder pattern.
     */
    private Address() {}

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public City getCity() {
        return this.city;
    }
    public String getPostalCode() {
        return city.getCityPostalCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, city);
    }

    @Override
    public String toString() {
        return this.getStreet() + " " + this.getHouseNumber() + " | " + this.getPostalCode() + " | " + this.getCity();
    }
}
