import java.util.*;

public class Controller {
	public static void main (String [] args) {
		// make instance of elevator and person
		// get all data structures ready to share with them
		// start the threads.
		// this could also be a thread again but got rid of
		// the thread for now just to keep it simple

		final int TOTAL_FLOORS = 10;
		Map<Integer, Request> requests = Collections.synchronizedMap(new HashMap<Integer, Request>());
		Person person = new Person(requests, TOTAL_FLOORS);
		Elevator elevator = new Elevator(requests, TOTAL_FLOORS);
		new Thread(person).start();
		new Thread(elevator).start();
	}
}
