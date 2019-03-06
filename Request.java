// structure to model requests for the hashmap
// since now there is no real "Person" object
import java.util.*;

public class Request {
    public int dest;
    public int personTrollyWeight;
    public String personName;
    public int startFloor;
    public Request(int startFloor, int dest, int personTrollyWeight, String personName) {
        this.startFloor = startFloor;
        this.dest = dest;
        this.personTrollyWeight = personTrollyWeight;
        this.personName = personName;
    }
}