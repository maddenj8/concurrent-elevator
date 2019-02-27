// structure to model requests for the hashmap
// since now there is no real "Person" object
import java.util.*;

public class Request {
    public int dest;
    public int personWeight;
    public String personName;
    public Request(int dest, int personWeight, String personName) {
        this.dest = dest;
        this.personWeight = personWeight;
        this.personName = personName;
    }
}