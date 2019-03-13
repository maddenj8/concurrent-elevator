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
    private Music music = new Music();

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
            System.out.println(this.requests.get(this.currentFloor) + " are on the current floor " + this.currentFloor); // null if nobody is on that floor
            this.determinePeopleMovement();
            this.chooseDirection();
        }
    }

    private void chooseDirection() {
        if (this.currentFloor == TOTAL_FLOORS) {
            this.state = "DOWN";
        }
        else if (this.currentFloor == 0) {
            this.state = "UP";
        }

        if (this.state == "UP") {
            this.currentFloor++;
            try {Thread.sleep(this.TRAVEL_TIME);} catch(Exception e) {}
        }
        else {
            this.currentFloor--;
            try {Thread.sleep(this.TRAVEL_TIME);} catch(Exception e) {}
        }

        System.out.println(">>> On floor " + this.currentFloor + " and I am going " + this.state);
        // System.out.println("Lowest Requested " + this.lowestRequested + " Higest Requested " + this.highestRequested);
    }

    private void determinePeopleMovement() {
        // this is where we determine who gets to get out
        for ( Request requestLl :  peopleInElevator ) {
            if (requestLl.dest == this.currentFloor ) {
                System.out.println(requestLl.personName + " Gets off at " +  this.currentFloor + " he will be missed " );
                this.currentWeight -= requestLl.totalWeight;
                peopleInElevator.remove(requestLl); // gets off the elevator 
                System.out.println("People in the elevator now " + this.peopleInElevator);
            }
        }     

        ArrayList<Request> toRemove = new ArrayList<Request>();
        for (Object request : requests.get(this.currentFloor)) {
            Request req = (Request) request;
            if (req.totalWeight + this.currentWeight < this.maxWeight && req.startFloor == this.currentFloor) {
                peopleInElevator.add(req);
                this.currentWeight += req.totalWeight;
                System.out.println(req.personName + " is getting on at floor " + this.currentFloor + " and is heading to floor " + req.dest);
                toRemove.add(req);
            }
        }
        requests.get(this.currentFloor).removeAll(toRemove);
        System.out.println("Requests on this floor now: " + requests.get(this.currentFloor));
        
        // System.out.println("HIT THIS ONE: " + requests.get(this.currentFloor));
        // requests.get(this.currentFloor)
        
        // for (Map.Entry<Integer, Request> request : requests.entrySet()) {
        //     if ( request.getValue().startFloor == this.currentFloor ) {
        //         if (request.getValue().totalWeight + this.currentWeight <= 1000 && request.getValue().startFloor == this.currentFloor) { // if you fit and it's on your floor get in
        //             Request reqForLl = new Request(request.getValue().startFloor, request.getValue().dest, request.getValue().totalWeight, request.getValue().personName);
        //             peopleInElevator.add(reqForLl);
        //             this.currentWeight += request.getValue().totalWeight;
        //             System.out.println(request.getValue().personName + " Gets on at " +  this.currentFloor); //get on 
        //             requests.remove(request); //remove here so people are not added twice
        //         }
        //     }
        // }
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
            if (request.startFloor == this.currentFloor && request.totalWeight + this.currentWeight <= this.maxWeight) {
                this.peopleInElevator.add(request);
                music.ding();
            }

            else {
                requests.get(request.startFloor).add(request);
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
