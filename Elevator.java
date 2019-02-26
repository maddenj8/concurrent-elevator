import java.util.*;

public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentFloor = 1; // start at floor one
    private LinkedList<Person> peopleInElevator = new LinkedList<Person>(); // keep track of people in elevator at one time
    public static Map<Integer, Person> requests;
    public int TOTAL_FLOORS;

    public Elevator(Map<Integer, Person> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
    }

    public void run() {
        System.out.println("This is the elevator thread");
    }
}
