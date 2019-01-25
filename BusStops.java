/*
 * This class represents a route towards a specific destination.
 * It contains an ArrayList of bus stop numbers and another ArrayList of their names.
 */

import java.util.ArrayList;

public class BusStops {
    private String destination;
    private ArrayList<String> busStopNumbers;
    private ArrayList<String> busStopNames;

    public BusStops(String destination, ArrayList<String> busStopNumbers, ArrayList<String> busStopNames) {
        this.destination = destination;
        this.busStopNumbers = busStopNumbers;
        this.busStopNames = busStopNames;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ArrayList<String> getBusStopNumbers() {
        return busStopNumbers;
    }

    public void setBusStopNumbers(ArrayList<String> busStopNumbers) {
        this.busStopNumbers = busStopNumbers;
    }

    public ArrayList<String> getBusStopNames() {
        return busStopNames;
    }

    public void setBusStopNames(ArrayList<String> busStopNames) {
        this.busStopNames = busStopNames;
    }
}
