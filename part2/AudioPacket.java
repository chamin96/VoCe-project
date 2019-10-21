import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AudioPacket implements Serializable {

    private String from;
    private String to;
    private int sequenceNo;
    private LocalDateTime timestamp;
    private byte[] audioData;

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }

    @Override
    public String toString() {
        return "Timestamp : " + this.timestamp +
               " SequenceNo : " + this.sequenceNo +
               " From : " + this.from +
               " To : " + this.to;
    }
}
