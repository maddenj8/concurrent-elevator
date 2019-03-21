import java.util.*;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   
import java.io.*;

public class Generator implements Runnable {
	public int MAX_TROLLY_WEIGHT = 32;
    public int startFloor;
    public int endFloor;
    public int personWeight;
    public int trollyWeight = 0;
    public Map<Integer, Request> requests;
    public int TOTAL_FLOORS;
    public String name;
    private Elevator elevator;
    private Elevator drawing;

    public void run() {
        int id = 0;
        Request request = this.getRequest(id);

        elevator.newRequest(request);
        Generator.writeToFile(request, "REQUEST");
        id++;
	    while (id < 100) { // just for testing while(true) normally
            int randomInterval = (int) Math.round(Math.random() * 200);
			if (randomInterval > 180) {
                
                Thread t = Thread.currentThread(); // get this current thread
                request = this.getRequest(id);
                elevator.newRequest(request);
                Generator.writeToFile(request, "REQUEST");

				id++;
			}
			try {
				Thread.sleep(randomInterval);
			} catch (InterruptedException e) {Thread.currentThread().interrupt();}

		}

    }

    public Generator(ConcurrentHashMap requests, int TOTAL_FLOORS, Elevator elevator) {
        this.requests = requests;
        this.TOTAL_FLOORS = TOTAL_FLOORS;
        this.elevator = elevator;
    }

    public static ConcurrentHashMap createRequestMap() {
        ConcurrentHashMap map = new ConcurrentHashMap();
        for (int i = 0; i < 11; i++) {
            map.put(i, new ArrayList<Request>());
        }
        return map;
    }

    private Request getRequest(int id) {
        name = "Person_" + String.valueOf(id);
        Random r = new Random();
        if ((int) Math.round(Math.random()) == 1) { // 1= trolly; 0 = no trolly
            trollyWeight = (int) Math.round(Math.random() * MAX_TROLLY_WEIGHT); // 32 because that is the max you can check in with Ryanair
        }
        double tmp_person_weight = r.nextGaussian() * 15 + 73; 
        this.personWeight = (int) tmp_person_weight;
        
        this.startFloor = (int) Math.round(Math.random() * 9);
        this.endFloor = (int) Math.round(Math.random() * 9);

        while(startFloor == endFloor) {
            this.startFloor = (int) Math.round(Math.random() * 9);
            this.endFloor = (int) Math.round(Math.random() * 9);
        }

        return new Request(this.startFloor, this.endFloor, this.personWeight + this.trollyWeight, this.name);
    }

    public static void main(String [] args) {
        ExecutorService es = Executors.newFixedThreadPool(3);
		final int TOTAL_FLOORS = 10;
        ConcurrentHashMap requests = Generator.createRequestMap();
		Elevator elevator = new Elevator(requests, TOTAL_FLOORS);
        Music music = new Music();
        Generator generator = new Generator(requests, TOTAL_FLOORS, elevator);
        if (args.length == 0 || !(args[0].equals("--silent"))) {
            es.submit(music);
        }
        es.submit(generator);
        es.submit(elevator);
    }

    public synchronized static void writeToFile(Request request, String state) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String timeStamp = dtf.format(ldt);
        String output = "";

        if (state == "REQUEST") { // getting on the elevator
            output = request.personName + " makes request at " + timeStamp + " starting at floor " + request.startFloor + " with the destination floor " + request.dest;
        }
        else if (state == "BOARD") {
            output = request.personName + " is getting in the elevator at floor " + request.startFloor;
        }
        else if (state == "DEPART") { // getting off the elevator
            output = request.personName + " is getting out of the elevator at floor " + request.dest;
        }
        else if (state == "FULL") { // getting off the elevator
            output = request.personName + " is not getting on the elevator as it is full  " + request.dest;
        }
        try {
            File file = new File("output.txt");

            if (!(file.exists())) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            fw.write(output + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {e.printStackTrace();}
    }
}
