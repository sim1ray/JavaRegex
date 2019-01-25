/*
 * This object represents a city with all the different buses that go to and from it.
 */

import java.util.ArrayList;

public class City {
    private String name;
    private ArrayList<String> buses;

    City(String name, ArrayList<String> buses) {
        this.name = name;
        this.buses = buses;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getBuses() {
        return this.buses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuses(ArrayList<String> buses) {
        this.buses = buses;
    }
}
