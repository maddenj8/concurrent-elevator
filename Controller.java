import java.util.*;

public class Controller implements Runnable {
	public void run () {
		int id = 0;
		while (true) {
			int randomInterval = (int) Math.round(Math.random() * 500);
			if (randomInterval > 450) {
				(new Thread(new Person(), "person_" + id)).start();
				id++;
			}
			try {
				Thread.sleep(randomInterval);
			} catch (InterruptedException e) {Thread.currentThread().interrupt();}
		}
	}

	public static void main (String [] args) {
		(new Thread(new Controller())).start();
	}
}
