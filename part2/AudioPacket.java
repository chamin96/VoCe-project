import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AudioPacket implements Serializable {

    String from;
    String to;
    int sequenceNo;
    LocalDateTime timestamp;
    byte[] audioData;

    public AudioPacket(String from, String to, int sequenceNo, LocalDateTime timestamp, int size) {
        this.from = from;
        this.to = to;
        this.sequenceNo = sequenceNo;
        this.timestamp = timestamp;
        audioData = new byte[size];
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

}
