import java.net.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Transmit extends Audio {

    private final int packetsize = 256;
    private final int port = 4445;
    private InetAddress host = null;
    private MulticastSocket socket = null;
    private byte[] tempBuffer = new byte[this.packetsize];
    private boolean stopCapture = true;

    public Transmit(InetAddress host) {
        this.host = host;
    }

    private void captureAndSend() {
        this.stopCapture = true;
        try{
            int readCount;
            while (true) {
                if (!this.stopCapture) {
                    readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer

                    if (readCount > 0) {

                        // Construct the datagram packet
                        DatagramPacket packet = new DatagramPacket(this.tempBuffer, this.tempBuffer.length, this.host, 4446);
                        System.out.println(Arrays.toString(packet.getData()));
                        // Send the packet
                        this.socket.send(packet);
                    }
                }
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
