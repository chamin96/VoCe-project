import java.util.TimerTask;

/**
 * printPacketInfo
 */
public class PrintPacketInfo extends TimerTask {

    Transmit tr = new Transmit();
    // Recieve cap = new Recieve();

    public void run() {
        System.out.println("Packets Sent: " + tr.getSentCount());
        System.out.println("Packets Recieved: ");
    }

}