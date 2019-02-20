import java.util.*;

public class Person implements Runnable {
    public int start_floor;
    public int end_floor;
    public int  person_weight;

    public void run() {
	Thread t = Thread.currentThread(); // get this current thread
        System.out.println("Hi, my name is " + t.getName());
        System.out.println("weight" +  person_weight);

    }

    public Person(){
	Random r = new Random();
	double tmp_person_weight = r.nextGaussian() * 15 + 73; 
	this.person_weight = (int)tmp_person_weight;
	do {
	this.start_floor = (int) Math.round(Math.random() *9);
	this.end_floor = (int) Math.round(Math.random() * 9);
	}
	while(  start_floor == end_floor );
	}
}

