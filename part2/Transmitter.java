import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Transmitter extends Audio {

    private final int PACKET_SIZE = 256;
    private final int PORT = 4445;
    private InetAddress host;
    private String ipAddress;
    private MulticastSocket socket = null;
    private boolean stopCapture = true;
    private int sequenceNo = 1;
    private byte[] tempBuffer = new byte[this.PACKET_SIZE];
    private ByteArrayOutputStream byteArrayOutputStream;

    public Transmitter(InetAddress host, String ipAddress) {
        this.host = host;
        this.ipAddress = ipAddress;
    }

    /**
     * Capture and Send Audio Packets
     */
    private void captureAndSend() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.stopCapture = false;
        try{
            int readCount;
            while (!this.stopCapture) {
                readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer

                if (readCount > 0) {

                    AudioPacket audioPacket = new AudioPacket(sequenceNo, PACKET_SIZE);
                    ObjectOutputStream os = new ObjectOutputStream(this.byteArrayOutputStream);
                    os.writeObject(audioPacket);
                    os.flush();
                    os.close();
                    System.out.println("Transmitting packet : " + audioPacket.toString());
                    byte[] data = this.byteArrayOutputStream.toByteArray();

                    // Construct the datagram packet
                    DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE, this.host, 4446);

                    // Send the packet
                    try{
                        this.socket.send(packet);
                        System.out.println(Arrays.toString(packet.getData()));
                    } catch (SocketException e) {
                        System.out.println("Packet send error");
                        e.printStackTrace();
                    }
                    System.out.println("Audio Message sent from transmitter");
                }
                sequenceNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                this.byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopCapture() {
        this.stopCapture = true;
    }

    public void startCapture() {
        this.stopCapture = false;
    }

    /**
     * Thread implementation for transmitting
     */
    public void run() {

        try{

            this.socket = new MulticastSocket();
            this.socket.joinGroup(this.host);
            this.captureAudio();
            this.captureAndSend();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }

    }

}
