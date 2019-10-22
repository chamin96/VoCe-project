import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UDPSerializationUnitTest {

    private final int defaultSequenceNo = 1;
    private final byte[] defaultAudioData = {78, 111, 91, 0, 9, 116, 97, 116, 0, 2, 91, 66, 120, 112, 0, 0, 0, -61, 117, 114, 0, 2, 91, 66, -84, -13, 23, -8, 6, 8, 84, -32, 2, 0, 0, 120, 112, 0, 0, 1, 0, 0};
    private final byte[] defaultAudioPacketData = {-84, -19, 0, 5, 115, 114, 0, 11, 65, 117, 100, 105, 111, 80, 97, 99, 107, 101, 116, 82, -81, -87, -41, -48, -72, -29, -1, 3, 0, 2, 73, 0, 10, 115, 101, 113, 117, 101, 110, 99, 101, 78, 111, 91, 0, 9, 97, 117, 100, 105, 111, 68, 97, 116, 97, 116, 0, 2, 91, 66, 120, 112, 0, 0, 1, 126, 117, 114, 0, 2, 91, 66, -84, -13, 23, -8, 6, 8, 84, -32, 2, 0, 0, 120, 112, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 120};

    AudioPacket audioPacket;
    ByteArrayInputStream byteArrayInputStream;
    ByteArrayOutputStream byteArrayOutputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    @Before
    public void setup() {
        audioPacket = new AudioPacket(defaultSequenceNo, 256);
    }

    @Test
    public void whenSerializingAndDeserializing_ThenObjectIsTheSame() throws IOException, ClassNotFoundException {

        byteArrayOutputStream = new ByteArrayOutputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("yourfile.txt");
        try{
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(audioPacket);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byteArrayInputStream = new ByteArrayInputStream(defaultAudioPacketData);
        FileInputStream fileInputStream = new FileInputStream("yourfile.txt");
        int read = fileInputStream.read();
        System.out.println(fileInputStream.getFD().valid());
        objectInputStream = new ObjectInputStream(fileInputStream);
        AudioPacket new_AudioPacket = null;
        try{
            new_AudioPacket = (AudioPacket) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println(new_AudioPacket.equals(null));
        System.out.println(new_AudioPacket.toString());
//        assertTrue(new_AudioPacket.getSequenceNo() == audioPacket.getSequenceNo());
    }

    @After
    public void tearDown() {

    }

}
