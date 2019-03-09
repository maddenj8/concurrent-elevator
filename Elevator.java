import java.util.*;

public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentFloor = 1; // start at floor one
    private LinkedList<Request> peopleInElevator = new LinkedList<Request>(); // keep track of people in elevator at one time
    public static Map<Integer, Request> requests;
    public int TOTAL_FLOORS;

    private String state = "SLEEP";
    private int lowestRequested = 10; // floor request that goes the lowest
    private int highestRequested = 0; // floor request that goes the highest
    private Boolean elevatorRequested = false;

    public Elevator(Map<Integer, Request> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
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

        while(true) {
            if (!this.elevatorRequested) {
                System.out.println("Sleeping");
                try {
                    Thread.sleep(1000); // wait time to check for state change
                } catch(Exception e) {}
            }
            else if (this.state == "UP" && this.currentFloor <= TOTAL_FLOORS) {
                if (this.currentFloor == this.highestRequested) {
                    this.state = "DOWN";
                }
                else {this.currentFloor++;}
                try {
                    Thread.sleep(1000); // travel time to next floor
                } catch(Exception e) {}
            }
            else if (this.state == "DOWN" && this.currentFloor >= 0) {
                if (this.currentFloor == this.lowestRequested) {
                    this.state = "UP";
                }
                else {this.currentFloor--;}
                try {
                    Thread.sleep(1000); // travel time to next floor
                } catch(Exception e) {}
            }
            System.out.println(">>> On floor " + this.currentFloor + " and I am going " + this.state);
            System.out.println("Lowest Requested " + this.lowestRequested + " Higest Requested " + this.highestRequested);
        }
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

            else if (request.startFloor > this.highestRequested) 
                this.highestRequested = request.startFloor;

            else if (request.startFloor < this.lowestRequested)
                this.lowestRequested = request.startFloor;
        }

        // otherwise nothing special has to be done and the request can
        // just go into the hashmap as standard
        // this must be synchronized
    }
}