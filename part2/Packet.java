import java.io.Serializable;
import java.time.LocalDateTime;

public class Packet implements Serializable {

    String from;
    String to;
    int sequenceNo;
    LocalDateTime timestamp;
    byte[] audioData;

    public Packet(String from, String to, int sequenceNo, LocalDateTime timestamp, int size) {
        this.from = from;
        this.to = to;
        this.sequenceNo = sequenceNo;
        this.timestamp = timestamp;
        audioData = new byte[size];
    }

}
