import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class Transmitter extends Audio {

    private final int PACKET_SIZE = 256;
    private final int PORT = 4445;
    private InetAddress host;
    private String ipAddress;
    private MulticastSocket socket = null;
    private boolean captureState = true;
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
//    public void captureAndSend() {
//        this.byteArrayOutputStream = new ByteArrayOutputStream();
//        this.captureState = false;
//        try{
//            int readCount;
//            while (this.captureState) {
//                readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer
//
//                if (readCount > 0) {
//
//                    AudioPacket audioPacket = new AudioPacket(sequenceNo, PACKET_SIZE);
//                    ObjectOutputStream os = new ObjectOutputStream(this.byteArrayOutputStream);
//                    os.writeObject(audioPacket);
//                    os.flush();
//                    os.close();
//                    byte[] data = this.byteArrayOutputStream.toByteArray();
//
//                    // Construct the datagram packet
//                    DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE, this.host, 4446);
//
//                    // Send the packet
//                    try{
//                        this.socket.send(packet);
//                        System.out.println("Audio Packet sent");
//                    } catch (SocketException e) {
//                        System.out.println("Packet send error");
//                        e.printStackTrace();
//                    }
//                    System.out.println("Audio Message sent from transmitter");
//                }
//                sequenceNo++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try{
//                this.byteArrayOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public void stopCapture() {
        this.captureState = false;
    }

    public void startCapture() {
        this.captureState = true;
    }

    public boolean getCaptureState() {
        return captureState;
    }

    /**
     * Thread implementation for transmitting
     */
    public void run() {
        System.out.println("Transmitter Thread");
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            this.socket = new MulticastSocket();
            this.socket.joinGroup(this.host);
            System.out.println("The Transmitter is ready");
            this.captureAudio();
            int readCount;
            while (true) {
                if (this.captureState) {
                    System.out.println("TRANSMIT");
                    readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer

                    if (readCount > 0) {
                        Serializer serializer = new Serializer();
                        AudioPacket audioPacket = new AudioPacket(sequenceNo, PACKET_SIZE);
                        ObjectOutputStream os = new ObjectOutputStream(this.byteArrayOutputStream);
                        os.writeObject(audioPacket);
                        os.flush();
                        os.close();
//                        byte[] data = this.byteArrayOutputStream.toByteArray();
                        byte[] data = serializer.serialize(audioPacket);
                        DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE, this.host, 4446);
                        // Construct the datagram packet

                        // Send the packet
                        try{
                            this.socket.send(packet);
                            System.out.println("Audio Packet sent");
                        } catch (SocketException e) {
                            System.out.println("Packet send error");
                            e.printStackTrace();
                        }
                        System.out.println("Audio Message sent from transmitter");
                    }
                    sequenceNo++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                this.byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.socket.close();
        }

//        try{
//
//            this.socket = new MulticastSocket();
//            this.socket.joinGroup(this.host);
//            System.out.println("The Transmitter is ready");
//            this.captureAudio();
//            this.captureAndSend();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            this.socket.close();
//        }

    }

}
