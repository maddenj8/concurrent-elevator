// javac -classpath . *.java -source 1.7 -target 1.7
// forget about this it's just to work on my windows 

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentWeight = 0; // current weight carrying
    private int currentFloor = 0; // start at floor one
    BlockingQueue<Request> peopleInElevator = new ArrayBlockingQueue<>(50); // keep track of people in elevator at one time
    public static volatile Map<Integer, ArrayList> requests;
    public int TOTAL_FLOORS;
    private int TRAVEL_TIME = 1000; // should be 100 but 1000 is easier to test
    private int tick_count = 0;

    private volatile String state = "SLEEP";
    private volatile int lowestRequested = 10; // floor request that goes the lowest
    private volatile int highestRequested = 0; // floor request that goes the highest
    private volatile Boolean elevatorRequested = false;
    // private Music music = new Music();

    public Elevator(Map<Integer, ArrayList> requests, int TOTAL_FLOORS) {
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
            this.determinePeopleMovement();
            // ElevatorController.chooseDirection(this.currentFloor);
            this.chooseDirection();
        }
    }

    private void chooseDirection() {
        if (this.currentFloor == this.highestRequested || this.currentFloor == TOTAL_FLOORS) {
            if (this.lowestRequested == this.TOTAL_FLOORS) { // if highest requested hasn't changed, no new requests have come in.
                this.state = "SLEEP";
                this.elevatorRequested = false;
            }
            else {
                this.state = "DOWN";
                this.highestRequested = 0; // set back to make first next request guarantee be the highest
            }
        }
        else if (this.currentFloor == this.lowestRequested || this.currentFloor == 0) {
            if (this.highestRequested == 0) { // if highest requested hasn't changed then no new request came in
                this.state = "SLEEP";
                this.elevatorRequested = false;
            }
            else {
                this.state = "UP";
                this.lowestRequested = this.TOTAL_FLOORS; // set back to make the first next request guarantee be the lowest.
            }
        }

        if (this.state == "UP" && this.currentFloor < this.TOTAL_FLOORS) {
            this.currentFloor++;
            try {Thread.sleep(this.TRAVEL_TIME);} catch(Exception e) {}
        }
        else if (this.state == "DOWN" && this.currentFloor > 0) {
            this.currentFloor--;
            try {Thread.sleep(this.TRAVEL_TIME);} catch(Exception e) {}
        }
        else {
            if (this.elevatorRequested) {
                this.state = "DOWN";
            }
            else {
                try {
                    Thread.sleep(100);
                } catch(Exception e) {}
            }
        }
        System.out.println("-----------------");
        System.out.println(this.currentFloor);

        System.out.println(this.lowestRequested + " " + this.highestRequested);
    }

    private void determinePeopleMovement() {
        // this is where we determine who gets out
        for (Request req :  peopleInElevator) {
            if (req.dest == this.currentFloor) {
                // System.out.println(requestLl.personName + " Gets off at " +  this.currentFloor + " he will be missed " );
                Generator.writeToFile(req, "DEPART");
                this.currentWeight -= req.totalWeight;
                peopleInElevator.remove(req); // gets off the elevator 
            }
        }     

        ArrayList<Request> toRemove = new ArrayList<Request>();
        // System.out.println(">>>>>> " + requests);
        // System.out.println(this.currentFloor);
        for (Object request : requests.get(this.currentFloor)) {
            Request req = (Request) request;
            if (req.totalWeight + this.currentWeight >= this.maxWeight && req.startFloor == this.currentFloor){
					Generator.writeToFile(req, "FULL");
            }
            if (req.startFloor == this.currentFloor && req.totalWeight + this.currentWeight <= this.maxWeight) {
                    peopleInElevator.add(req);
                    this.currentWeight += req.totalWeight;
                    Generator.writeToFile(req, "BOARD");
                    toRemove.add(req);
                    if (req.dest > this.highestRequested) {this.highestRequested = req.dest;}
                    else if (req.dest < this.lowestRequested) {this.lowestRequested = req.dest;}
                }
            }
        
        requests.get(this.currentFloor).removeAll(toRemove);
    }
    	
    public void newRequest(Request request) {
        
        synchronized(this) {
            if (request.startFloor == this.currentFloor) {
                if (request.totalWeight + this.currentWeight >= this.maxWeight) {
                    Generator.writeToFile(request, "FULL");
                }
                else {
                    peopleInElevator.add(request);
                    this.currentWeight += request.totalWeight;
                    Generator.writeToFile(request, "BOARD");
                }
            }
            else {
                requests.get(request.startFloor).add(request);
                // System.out.println(requests.get(request.startFloor));
            }

            if (request.startFloor > this.currentFloor && request.startFloor > this.highestRequested) {
                this.highestRequested = request.startFloor;
            }
            else if (request.startFloor < this.currentFloor && request.startFloor < this.lowestRequested) {
                this.lowestRequested = request.startFloor;
            }
            // System.out.println("lowest requested floor: " + this.lowestRequested);
            // System.out.println("highest requested floor: " + this.highestRequested);

            // System.out.println(this.state);
            // System.out.println(this.currentFloor);
            // System.out.println(this.currentWeight);
        }
    }
}