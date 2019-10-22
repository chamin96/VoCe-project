import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public class Main {

    /**
     * Driver main method to manage multi-cast transmitter and receiver threads
     *
     * @param args arg[0] should contain the peer IP Address
     */
    public static void main(String[] args) {

        // Check the whether the arguments are given
        if (args.length != 1) {
            System.out.println("Peer IP missing!");
            return;
        }

        Transmitter transmitter = null;
        Receiver receiver = null;

        try{

            transmitter = new Transmitter(InetAddress.getByName(args[0]), getCurrentIPAddress());
//            transmitter.start();

            receiver = new Receiver(InetAddress.getByName(args[0]));
//            receiver.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);
        String input;
        while ((transmitter != null) && (receiver != null)) {
            input = in.nextLine();
            switch (input) {
                case "c":
                    receiver.stopPlay();
                    transmitter.startCapture();
                    transmitter.start();
                    try{
                        receiver.wait();
                    } catch (InterruptedException e) {
                        System.out.println("RECV wait error");
                        e.printStackTrace();
                    }
                    System.out.println("Capture state: " + transmitter.getCaptureState());
                    System.out.println("Play state: " + receiver.getPlayState());
//                    System.out.println("Capturing...");
                    break;
                case "p":
                    transmitter.stopCapture();
                    receiver.startPlay();
                    receiver.start();
                    try{
                        transmitter.wait();
                    } catch (InterruptedException e) {
                        System.out.println("TRANS wait error");
                        e.printStackTrace();
                    }
                    System.out.println("Capture state: " + transmitter.getCaptureState());
                    System.out.println("Play state: " + receiver.getPlayState());
//                    System.out.println("Playing...");
                    break;
                default:
                    receiver.startPlay();
                    transmitter.startCapture();
                    System.out.println("Capture state: " + transmitter.getCaptureState());
                    System.out.println("Play state: " + receiver.getPlayState());
                    break;
            }
            System.out.println(transmitter.isAlive());
            System.out.println(receiver.isAlive());
        }
    }

    /**
     * Get Current IP Address
     * Select interface for ip
     *
     * @return [String] Current/Host IP address
     */
    public static String getCurrentIPAddress() {

        String ipAddress = "";

        try{
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            System.out.println("Please choose network interface from the following and press <Enter>");
            Scanner scanner = new Scanner(System.in);
            String input = "";
            List<String> networkList = new ArrayList<>();
            for (NetworkInterface networkInterface : networkInterfaceList) {
                networkList.add(networkInterface.getName());
                System.out.println(networkInterface.getName());
            }
            while (!networkList.contains(input)) {
                System.out.print("Please select a valid interface : ");
                input = scanner.nextLine();
            }
            for (NetworkInterface networkInterface : networkInterfaceList) {
                if (networkInterface.getName().equals(input)) {
                    Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                    System.out.println(networkInterface.getDisplayName());
//                    System.out.println(networkInterface.get);
                    System.out.println(address.hasMoreElements());
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
                        String[] vals = addr.getHostAddress().split("\\.");
                        if (vals.length > 1) {
                            ipAddress = addr.getHostAddress();
                        }
                    }
                }
            }
//            while (networkInterfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = networkInterfaces.nextElement();
//            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        System.out.println(ipAddress);
        return ipAddress;

    }

}
