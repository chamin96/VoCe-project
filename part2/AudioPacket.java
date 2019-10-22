import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AudioPacket implements Serializable {

    private static final long serialVersionUID = 5958167576419296255L;
    int sequenceNo;
    byte[] audioData;

    public AudioPacket(int sequenceNo, int size) {
        this.sequenceNo = sequenceNo;
        audioData = new byte[size];
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }

    @Override
    public String toString() {
        return "Timestamp : " + LocalDateTime.now() + " SequenceNo : " + this.sequenceNo;
    }
}
