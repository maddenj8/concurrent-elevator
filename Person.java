import java.util.*;

public class Person implements Runnable {
    public void run() {
	Thread t = Thread.currentThread(); // get this current thread
        System.out.println("Hi, my name is " + t.getName());
    }
}
