import java.net.*;
import java.util.TimerTask;
import java.util.Timer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Transmit extends Main implements Runnable {

    private final int packetSize = 256; // packet size
    private final int port = 49990; // sender port
    private InetAddress host = null;
    private DatagramSocket socket = null;
    private ByteArrayOutputStream byteArrayOutputStream = null;
    private byte tempBuffer[] = new byte[this.packetSize];
    private boolean stopCapture = false;
    public static int countSent = 0;

    private void captureAndTransmit() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.stopCapture = false;
        try {
            int readCount;
            while (!this.stopCapture) {
                readCount = getTargetDataLine().read(this.tempBuffer, 0, this.tempBuffer.length); // record voice to
                                                                                                  // tempBuffer

                if (readCount > 0) {
                    this.byteArrayOutputStream.write(this.tempBuffer, 0, readCount);

                    // Construct the datagram packet
                    DatagramPacket packet = new DatagramPacket(this.tempBuffer, this.tempBuffer.length, this.host,
                            49900);

                    // Send the packet
                    this.socket.send(packet);

                    // countSent++;
                    // System.out.println(packet.getLength());
                }
            }
            this.byteArrayOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSentCount(){
        return countSent;
    }



    public void run() {
        try {
            this.socket = new DatagramSocket(this.port);
            this.captureAudio();
            this.captureAndTransmit();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            this.socket.close();
        }
    }

    public Transmit(InetAddress host) {
        this.host = host;
    }

    public Transmit() {
        super();
    }

    public static void main(String[] args) {

        // check for the args
        if (args.length != 1) {
            System.out.println("Peer IP missing!");
            return;
        }

        try {

            Thread tr = new Thread(new Transmit(InetAddress.getByName(args[0])));
            tr.start();

            Thread rcv = new Thread(new Recieve());
            rcv.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // PrintPacketInfo timerTask = new PrintPacketInfo();
        // //running timer task as daemon thread
        // Timer timer = new Timer(true);
        // timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
        
    }
}