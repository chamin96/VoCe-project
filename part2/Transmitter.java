import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Transmitter extends Audio {

    private final int PACKET_SIZE = 512;
    private final int PORT = 4445;
    private InetAddress host;
    private String ipAddress;
    private MulticastSocket socket = null;
    private boolean stopCapture = true;
    private int sequenceNo = 1;
    private byte[] tempBuffer = new byte[this.PACKET_SIZE];

    public Transmitter(InetAddress host, String ipAddress) {
        this.host = host;
        this.ipAddress = ipAddress;
    }

    /**
     * Capture and Send Audio Packets
     */
    private void captureAndSend() {
        this.stopCapture = true;
        try{
            int readCount;
            while (true) {
                if (!this.stopCapture) {
                    readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer

                    if (readCount > 0) {

                        AudioPacket audioPacket = new AudioPacket(ipAddress, host.getHostAddress(), sequenceNo, LocalDateTime.now(), PACKET_SIZE);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream(outputStream);
                        os.writeObject(audioPacket);
                        System.out.println("Transmitting packet : " + audioPacket.toString());
                        byte[] data = outputStream.toByteArray();
                        // Construct the datagram packet
                        DatagramPacket packet = new DatagramPacket(data, data.length, this.host, 4446);
                        System.out.println(Arrays.toString(packet.getData()));

                        // Send the packet
                        this.socket.send(packet);
                        System.out.println("Message sent from client");
                    }
                }
                sequenceNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
