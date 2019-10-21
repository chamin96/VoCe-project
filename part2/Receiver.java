import test.Student;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Arrays;

public class Receiver extends Audio {

    private final int PACKET_SIZE = 256;
    private final int PORT = 4446;
    private InetAddress host;
    private boolean stopPlay = false;
    private MulticastSocket socket = null;

    public Receiver(InetAddress host) {
        this.host = host;
    }

    @Override
    public void run() {
        System.out.println("Receiver thread");
        try{
            // Construct the socket
            this.socket = new MulticastSocket(this.PORT);
            this.socket.joinGroup(this.host);
            System.out.println("The server is ready");
            // Create a packet
            DatagramPacket packet = new DatagramPacket(new byte[this.PACKET_SIZE], (this.PACKET_SIZE));
            this.playAudio();

            while (true) {
                if (!this.stopPlay) {
                    try{
                        // Receive a packet (blocking)
                        this.socket.receive(packet);
                        byte[] data = packet.getData();
                        ByteArrayInputStream in = new ByteArrayInputStream(data);
                        ObjectInputStream inputStream = new ObjectInputStream(in);
                        AudioPacket audioPacket = null;
                        try{
                            audioPacket = (AudioPacket) inputStream.readObject();
                            this.getSourceDataLine().write(packet.getData(), 0, this.PACKET_SIZE); //playing the audio
                        } catch (ClassNotFoundException | EOFException e) {
                            e.printStackTrace();
                        } finally {
                            inputStream.close();
                        }
                        if (audioPacket != null) {
                            System.out.println("Receiving Packet : " + audioPacket.toString());
                        } else {
                            System.out.println("Cannot serialize Packet : " + Arrays.toString(packet.getData()));
                        }
                        packet.setLength(this.PACKET_SIZE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    public void stopPlay() {
        this.stopPlay = true;
    }

    public void startPlay() {
        this.stopPlay = false;
    }

}
