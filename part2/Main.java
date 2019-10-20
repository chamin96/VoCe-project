import java.net.InetAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Check the whether the arguments are given
        if (args.length != 1) {
            System.out.println("Peer IP missing!");
            return;
        }

        Transmitter transmitter = null;
        Reciever reciever = null;

        try{

            transmitter = new Transmitter(InetAddress.getByName(args[0]));
            transmitter.start();

            reciever = new Reciever(InetAddress.getByName(args[0]));
            reciever.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);
        boolean state = true; // playing

        while ((transmitter != null) && (reciever != null)) {
            in.nextLine();
            if (state) {
                transmitter.stopCapture();
                reciever.startPlay();
                System.out.println("Playing...");
                state = false;
            } else {
                reciever.stopPlay();
                transmitter.startCapture();
                System.out.println("Capturing...");
                state = true;
            }
        }
    }

}
