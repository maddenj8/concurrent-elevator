import java.util.*;


public class Elevator implements Runnable {
    private int maxWeight = 1000; // in kg obviously
    private int currentFloor = 1; // start at floor one
    private LinkedList<Person> peopleInElevator = new LinkedList<Person>(); // keep track of people in elevator at one time
    public static Map<Integer, Request> requests;
    public int TOTAL_FLOORS;

    public Elevator(Map<Integer, Request> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
        this.currentFloor = currentFloor;
    }

    public void run() {
        System.out.println("This is the elevator thread");
        try {
            while(true){
		Iterator entries = requests.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    Integer startFloor = (Integer)entry.getKey();
		    Request request = (Request)entry.getValue();
		    System.out.println(request);
		    //eh how on earth you use request 
		    System.out.println(request.name);
		}

		    Thread.sleep(1000);
	    //System.out.println(currentFloor);
	    }
	    
        } catch (Exception e) {}
        System.out.println(currentFloor);
    }
}



