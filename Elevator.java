import java.util.*;

public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentFloor = 1; // start at floor one
    private LinkedList<Person> peopleInElevator = new LinkedList<Person>(); // keep track of people in elevator at one time
    private LinkedList<Person> requests = new LinkedList<Person>(); // people who are waiting on the elevator to arrive at their floor

    public void run() {
        System.out.println("This is the elevator thread");
    }

    public static void main(String [] args) {
        (new Thread(new Elevator())).start();
        // do some stuff 
    }
}