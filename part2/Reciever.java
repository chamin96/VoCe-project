import java.net.*;
import java.util.Arrays;

public class Reciever extends Audio {

    private final int PACKET_SIZE = 256;
    private final int PORT = 4446;
    private InetAddress host;
    private boolean stopPlay = false;
    private MulticastSocket socket = null;

    public Reciever(InetAddress host) {
        this.host = host;
    }

    @Override
    public void run() {

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
                        this.getSourceDataLine().write(packet.getData(), 0, this.PACKET_SIZE); //playing the audio
                        System.out.println("Packet" + Arrays.toString(packet.getData()));
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
