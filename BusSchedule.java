/*
 * Client program retrieves the bus schedules from Community Transit website at
 * https://www.communitytransit.org/busservice/schedules/ and provides the user
 * with the schedule of bus stops for a particular bus route number.
 * Java regex API is used for extracting the data.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;

public class BusSchedule {

    public static void main(String[] args) throws IOException {
        ArrayList<City> cities = findBusNumbers();  //returns list of all City objects
        if (outputBusNumbers(cities)) {             //if city that starts with given letter is found
            outputBusStopsOnRoute();
        }
    }

    // Prints all the cities that start with user inputted letter, along with their associated bus numbers
    // Returns true if at least one city found
    public static boolean outputBusNumbers(ArrayList<City> list) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the letter your destination starts with: ");
        while (!input.hasNext("[a-zA-Z]")) {    //ensures input is single character
            System.out.println("Not valid.\nPlease enter the letter your destination starts with: ");
            input.next();
        }
        String startLetter = input.next();


        String cityName;
        ArrayList<String> busNumbers;
        boolean found = false;

        for (City c : list) {
            cityName = c.getName();
            busNumbers = c.getBuses();
            if (cityName.substring(0,1).toLowerCase().equals(startLetter)) {
                found = true;
                System.out.println("Destination: " + cityName);
                for (int i = 0; i < busNumbers.size(); i++) {
                    System.out.println("Bus Number: " + busNumbers.get(i));
                }
                System.out.println("+++++++++++++++++++++++++++++++++++");
            }
        }

        if (!found) {
            System.out.println("No destinations start with: " + startLetter);
            return false;
        }
        return true;
    }

    // Prints the link for a particular route number, along with all the destination/bus stops on that route
    public static void outputBusStopsOnRoute() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a route ID as a string (ex. 111, 230):");
        String routeID = input.next();

        String route = "https://www.communitytransit.org/busservice/schedules/route/" + routeID;
        try {
            ArrayList<BusStops> destinations = findBusStopSchedules(route);     //all BusStops on given route number
            System.out.println("The link for your route is: " + route + "\n");
            for (BusStops b : destinations) {
                System.out.println("Destination: " + b.getDestination());
                for (int i = 0; i < b.getBusStopNumbers().size(); i++) {
                    System.out.print("Stop number: " + b.getBusStopNumbers().get(i));
                    System.out.println(" is " + b.getBusStopNames().get(i));
                }
                System.out.println("+++++++++++++++++++++++++++++++++++");
            }
        } catch (IOException io) {
            System.out.println("IO Exception.");
        }
    }

    // Returns list of City objects, extracted from HTML source through regex API
    public static ArrayList<City> findBusNumbers() throws IOException {

        String text = readHTML("https://www.communitytransit.org/busservice/schedules/");

        String subtext, name;
        ArrayList<City> results = new ArrayList<>();


        Pattern pattern = Pattern.compile("(<h3>.*?(<hr|<div id=\"RoutesByRoute\"))");  //isolates relevant text
        Pattern pattern2 = Pattern.compile("<h3>(.*?)</h3>");           //gives name of city
        Pattern pattern3 = Pattern.compile("<a href.*?>(.*?)</a>");     //gives bus number

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            subtext = matcher.group(0);
            Matcher matcher2 = pattern2.matcher(subtext);
            if (matcher2.find()) {
                name = matcher2.group(1);
                Matcher matcher3 = pattern3.matcher(subtext);
                ArrayList<String> buses = new ArrayList<>();
                while (matcher3.find()) {
                    buses.add(matcher3.group(1));       //successively add each bus number to list of buses
                }

                City c = new City(name, buses);         //create city with extracted name and buses
                results.add(c);
            }
        }

        return results;
    }

    // Returns list of BusStops objects, extracted from HTML source through regex API
    public static ArrayList<BusStops> findBusStopSchedules(String route) throws IOException{
        String text = readHTML(route);

        String subtext, subtext2, destination;
        ArrayList<BusStops> destinations = new ArrayList<>();

        Pattern pattern = Pattern.compile("(<div id=\"RouteMap\".*<div class=\"\" data-sf-element=\"Row\">)");
        Pattern pattern2 = Pattern.compile("Weekday<small>(.*?)</small>(.*?)</thead>"); // gives chunk of text pertaining to each destination
        Pattern pattern3 = Pattern.compile("<strong class=.*?>(.*?)</strong>"); // gives stop number
        Pattern pattern4 = Pattern.compile("<p>(.*?)</p>"); //gives name of stop

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            subtext = matcher.group(0);
            Matcher matcher2 = pattern2.matcher(subtext);
            while (matcher2.find()) {
                destination = matcher2.group(1);
                subtext2 = matcher2.group(2);
                ArrayList<String> busStopNumbers = new ArrayList<>();
                ArrayList<String> busStopNames = new ArrayList<>();
                Matcher matcher3 = pattern3.matcher(subtext2);
                Matcher matcher4 = pattern4.matcher(subtext2);
                while(matcher3.find()) {
                    busStopNumbers.add(matcher3.group(1));
                }
                while(matcher4.find()) {
                    busStopNames.add(matcher4.group(1).replace("&amp;", "&"));
                }
                BusStops b = new BusStops(destination, busStopNumbers, busStopNames);
                destinations.add(b);
            }
        }

        return destinations;
    }

    // Reads source HTML from given url
    // Returns entire HTML code with all new lines removed
    public static String readHTML(String url) throws IOException{
        URLConnection bc = new URL(url).openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(bc.getInputStream()));
        String inputLine;
        String text = "";

        while ((inputLine = in.readLine()) != null) {
            text += inputLine;
        }
        in.close();

        return text;
    }

}

