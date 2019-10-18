import java.net.*;
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

public class Recieve extends Main implements Runnable {

    private final int packetSize = 100;
    private final int port = 55001;

    public void run() {

        try {

            // initialize the socket
            DatagramSocket socket = new DatagramSocket(this.port);
            System.out.println("The reciever is ready.");

            // create a new data packet
            DatagramPacket packet = new DatagramPacket(new byte[this.packetSize], (this.packetSize));
            this.playAudio();


            for (;;) {

                try {

                    // Receive a packet (blocking)
                    socket.receive(packet);
                  
                    // Print the packet
                    this.getSourceDataLine().write(packet.getData(), 0, this.packetSize); //playing the audio   

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    
}