// structure to model requests for the hashmap
// since now there is no real "Person" object
import java.util.*;

public class Request {
    public int dest;
    public int totalWeight;
    public String personName;
    public int startFloor;
    public Request(int startFloor, int dest, int totalWeight, String personName) {
        this.startFloor = startFloor;
        this.dest = dest;
        this.totalWeight = totalWeight;
        this.personName = personName;
    }
}