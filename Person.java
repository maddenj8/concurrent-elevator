import java.util.*;

public class Person implements Runnable {
	public int MAX_TROLLY_WEIGHT = 32;
    public int startFloor;
    public int endFloor;
    public int personWeight;
    public int trollyWeight = 0;
    public Map<Integer, Request> requests;
    public int TOTAL_FLOORS;
    public String name;

    public void run() {
        int id = 0;
		while (true) {
			int randomInterval = (int) Math.round(Math.random() * 500);
			if (randomInterval > 450) {
                name = "Person_" + String.valueOf(id);
				Random r = new Random();
				if ((int) Math.round(Math.random()) == 1) { // 1= trolly; 0 = no trolly
					trollyWeight = (int) Math.round(Math.random() * MAX_TROLLY_WEIGHT); // 32 because that is the max you can check in with Ryanair
				}
				double tmp_person_weight = r.nextGaussian() * 15 + 73; 
                this.personWeight = (int) tmp_person_weight;
                
                do {
                    this.startFloor = (int) Math.round(Math.random() * 9);
                    this.endFloor = (int) Math.round(Math.random() * 9);
                }
                while(startFloor == endFloor);

                Thread t = Thread.currentThread(); // get this current thread
                System.out.println("Hi, my name is " + name);
                System.out.println("I weigh " +  personWeight + "kg");
				System.out.println("I am on floor " +  startFloor + " and am going to floor " + endFloor);
				System.out.println("My trolly weighs " + trollyWeight + "kg");
                Request request = new Request(this.endFloor, this.personWeight + this.trollyWeight, this.name);
                requests.put(startFloor, request);

				id++;
			}
			try {
				Thread.sleep(randomInterval);
			} catch (InterruptedException e) {Thread.currentThread().interrupt();}

		}

    }

    public Person(Map<Integer, Request> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
    }
}