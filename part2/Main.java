import java.net.InetAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Check the whether the arguments are given
        if (args.length != 1) {
            System.out.println("Peer IP missing!");
            return;
        }

        Transmit cap = null;
        Recieve ply = null;

        try{

            cap = new Transmit(InetAddress.getByName(args[0]));
            cap.start();

            ply = new Recieve(InetAddress.getByName(args[0]));
            ply.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);
        boolean state = true; // playing

        while (true && (cap != null) && (ply != null)) {
            in.nextLine();
            if (state) {
                cap.stopCapture();
                ply.startPlay();
                System.out.println("Playing...");
                state = false;
            } else {
                ply.stopPlay();
                cap.startCapture();
                System.out.println("Capturing...");
                state = true;
            }
        }
    }

}
