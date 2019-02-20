import java.util.*;

public class Person implements Runnable {
    public void run() {
        System.out.println("This is the Person Thread");
    }

    public static void main(String [] args) {
        (new Thread(new Person())).start();
    }
}