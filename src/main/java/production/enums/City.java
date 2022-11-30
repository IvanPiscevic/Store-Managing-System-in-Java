package production.enums;

public enum City {

    CITY_ZAGREB("Zagreb", "10000"),
    CITY_SPLIT("Split", "21000"),
    CITY_SIBENIK("Sibenik", "22000"),
    CITY_VUKOVAR("Vukovar", "32000"),
    CITY_SAMOBOR("Samobor", "10430"),
    CITY_ZAPRESIC("Zapresic", "10290"),
    CITY_DUBROVNIK("Dubrovnik", "20000"),
    CITY_KOPRIVNICA("Koprivnica", "48000"),
    CITY_CUSTOM("NO_VALUE", "NO_VALUE");

    public final String cityName;
    public final String cityPostalCode;

    City(String cityName, String cityPostalCode) {
        this.cityName = cityName;
        this.cityPostalCode = cityPostalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityPostalCode() {
        return cityPostalCode;
    }

    @Override
    public String toString() {
        return this.cityName;
    }
}
