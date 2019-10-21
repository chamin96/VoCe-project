import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer {

    public byte[] serialize(AudioPacket audioPacket) throws IOException {

        byte[] serializedData;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);

        oos.writeObject(audioPacket);
        oos.flush();

        serializedData = byteArrayOutputStream.toByteArray();

        return serializedData;

    }

}
