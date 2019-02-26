import java.util.*;

public class Person implements Runnable {
    public int start_floor;
    public int end_floor;
    public int person_weight;
    public int trolly_weight;
    public Map<Integer, Person> requests;
    public int TOTAL_FLOORS;
    public String name;

    public void run() {
        int id = 0;
		while (true) {
			int randomInterval = (int) Math.round(Math.random() * 500);
			if (randomInterval > 450) {
                name = "Person_" + String.valueOf(id);
                Random r = new Random();
                double tmp_person_weight = r.nextGaussian() * 15 + 73; 
                trolly_weight = (int) Math.round(Math.random() * 9);
                this.person_weight = (int) tmp_person_weight;
                
                do {
                    this.start_floor = (int) Math.round(Math.random() * 9);
                    this.end_floor = (int) Math.round(Math.random() * 9);
                }
                while(start_floor == end_floor);

                Thread t = Thread.currentThread(); // get this current thread
                System.out.println("Hi, my name is " + name);
                System.out.println("I weigh " +  person_weight);
                System.out.println("I am on floor " +  start_floor + " and am going to floor " + end_floor);

				id++;
			}
			try {
				Thread.sleep(randomInterval);
			} catch (InterruptedException e) {Thread.currentThread().interrupt();}

		}

    }

    public Person(Map<Integer, Person> requests, int TOTAL_FLOORS) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
    }
}