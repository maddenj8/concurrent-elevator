import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentWeight = 0; // current weight carrying
    private int currentFloor = 1; // start at floor one
    BlockingQueue<Request> peopleInElevator = new ArrayBlockingQueue<>(50); // keep track of people in elevator at one time
    public static volatile Map<Integer, Request> requests;
    public int TOTAL_FLOORS;
    private int TRAVEL_TIME = 1000; // should be 100 but 1000 is easier to test
    private int tick_count = 0;

    private volatile String state = "SLEEP";
    private volatile int lowestRequested = 10; // floor request that goes the lowest
    private volatile int highestRequested = 0; // floor request that goes the highest
    private volatile Boolean elevatorRequested = false;

    public Elevator(Map<Integer, Request> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
        this.currentFloor = currentFloor;
    }

    public void run() {
        // PROBLEM: trying to detect when there is no request
        // when a request comes in we can set requested to true
        // but when do you say there is no request. 
        // if you hit the highestRequested how do you know that
        // the lowestRequested is actually still a valid request.
        // Best thing to do is to reset the value to null or something
        // when they are hit and if you get to the other end and they are
        // still not set you can assume there is no new requests made.
        // that is the next problem.

        // before checking what floors to move to and such
        // this is where you check who is to get off and 
        // who is in the hashmap waiting to get on
        // All of this should be separated into a set of
        // functions so that the loop simply checks the movement
        // of people, checks the movement of the elevator and goes
        // sleep until the next cycle.

        // remember when a person gets into the elevator, their
        // destination can sway the highest or lowest requested
        // floor so if the elevator and someone gets in wanting to
        // go higher, the elevator keeps going up rather than going down.
        
        while(true) {
            System.out.println(this.requests.get(this.currentFloor) + " are on the current floor " + this.currentFloor); // null if nobody is on that floor
            this.determinePeopleMovement();
            this.chooseDirection();
        }
    }

    private void chooseDirection() {
        if (this.state == "SLEEP") {
            System.out.println("Sleeping");
            try {
                Thread.sleep(TRAVEL_TIME); // wait time to check for state change
            } catch(Exception e) {}
        }
        else if (this.state == "UP" && this.currentFloor <= TOTAL_FLOORS) {
            if (this.currentFloor == this.highestRequested) {
                this.state = "DOWN";
            }
            else if (this.currentFloor < TOTAL_FLOORS) {this.currentFloor++;}
            else {this.state = "DOWN";}
            try {
                Thread.sleep(TRAVEL_TIME); // travel time to next floor
            } catch(Exception e) {}
        }
        else if (this.state == "DOWN" && this.currentFloor >= 0) {
            if (this.currentFloor == this.lowestRequested) {
                this.state = "UP";
            }
            else if (this.currentFloor > 0) {this.currentFloor--;}
            else {this.state = "UP";}
            try {
                Thread.sleep(TRAVEL_TIME); // travel time to next floor
            } catch(Exception e) {}
        }
        System.out.println(">>> On floor " + this.currentFloor + " and I am going " + this.state);
        System.out.println("Lowest Requested " + this.lowestRequested + " Higest Requested " + this.highestRequested);

        if (this.peopleInElevator.size() == 0) {
            this.elevatorRequested = false;
        }
    }

    private void determinePeopleMovement() {
        // this is where we determine who gets to get
        // on and how the people in the elevator change
        // the highest and lowest requested floors
    }

    public void newRequest(Request request) {
        // function to be called when request is added to the map

        // this is working on the assumption that
        // the requests to go up or down are solely
        // based on the destination but the direction
        // the elevator goes is also swayed by the 
        // start floor of the requests too.

        // SOLUTION: for the first incoming requests 
        // the direction is based on the start floor
        // but this can be altered when the people get on
        // the elevator handled by the other method.

        // also this should all be moved to the Controller
        // class since that is in charge of handling the 
        // requests and the movement of the elevators.
        
        synchronized(this) {
            this.elevatorRequested = true;
            if (this.state == "SLEEP") {
                if (request.startFloor < this.currentFloor) {this.state = "DOWN";}
                else {this.state = "UP";}
            }

            if (request.startFloor < this.lowestRequested) 
                this.lowestRequested = request.startFloor;

            else if (request.startFloor > this.highestRequested)
                this.highestRequested = request.startFloor;

            if (request.totalWeight + this.currentWeight <= 1000 && request.startFloor == this.currentFloor) { // if you fit and it's on your floor get in
                peopleInElevator.add(request);
                this.currentWeight += request.totalWeight;
            }
            else {
                requests.put(request.startFloor, request); // add to the hashmap
            }

            // System.out.println(this.state);
            // System.out.println(this.currentFloor);
            // System.out.println(this.currentWeight);
        }

        // otherwise nothing special has to be done and the request can
        // just go into the hashmap as standard
        // this must be synchronized
    }
}
