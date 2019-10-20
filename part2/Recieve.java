import java.net.*;
import java.util.Arrays;

public class Recieve extends Audio {

    private final int packetsize = 256;
    private final int port = 4446;
    private boolean stopPlay = false;
    private MulticastSocket socket = null;
    private InetAddress host = null;

    public Recieve(InetAddress host) {
        this.host = host;
    }

    @Override
    public void run() {

        try{
            // Construct the socket
            this.socket = new MulticastSocket(this.port);
            this.socket.joinGroup(this.host);
            System.out.println("The server is ready");

            // Create a packet
            DatagramPacket packet = new DatagramPacket(new byte[this.packetsize], (this.packetsize));
            this.playAudio();

            while (true) {
                if (!this.stopPlay) {
                    try{

                        // Receive a packet (blocking)
                        this.socket.receive(packet);
                        this.getSourceDataLine().write(packet.getData(), 0, this.packetsize); //playing the audio
                        System.out.println("Packet" + Arrays.toString(packet.getData()));
                        packet.setLength(this.packetsize);

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
