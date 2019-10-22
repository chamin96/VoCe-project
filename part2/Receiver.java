import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Arrays;

public class Receiver extends Audio {

    private static int count = 0;
    private final int PACKET_SIZE = 512;
    private final int PORT = 4446;
    private InetAddress host;
    private boolean playState = true;
    private DatagramSocket socket = null;
    private ByteArrayInputStream byteArrayInputStream;
    private ObjectInputStream objectInputStream;

    public Receiver(InetAddress host) {
        this.host = host;
    }

    @Override
    public void run() {
        System.out.println("Receiver thread");
        try{
            // Construct the socket
            this.socket = new MulticastSocket(this.PORT);
//            this.socket.joinGroup(this.host);
            System.out.println("The Receiver is ready");
            System.out.println(this.getAudioFormat());
            // Create a packet
            this.playAudio();
            DatagramPacket packet = new DatagramPacket(new byte[this.PACKET_SIZE], (this.PACKET_SIZE));
//            while (true) {
//                if (this.playState) {
//                    System.out.println("RECEIVE");
//                    try{
//                        // Receive a packet (blocking)
//                        this.socket.receive(packet);
//                        byte[] data = packet.getData();
//                        ByteArrayInputStream in = new ByteArrayInputStream(data);
//                        ObjectInputStream inputStream = new ObjectInputStream(in);
//                        AudioPacket audioPacket = null;
//                        try{
//                            audioPacket = (AudioPacket) inputStream.readObject();
//                            this.getSourceDataLine().write(audioPacket.getAudioData(), 0, this.PACKET_SIZE / 2); //playing the audio
//                        } catch (ClassNotFoundException | EOFException e) {
//                            e.printStackTrace();
//                        } finally {
//                            inputStream.close();
//                        }
//                        if (audioPacket != null) {
//                            System.out.println("Receiving Packet : " + audioPacket.toString());
//                        } else {
//                            System.out.println("Cannot serialize Packet : " + Arrays.toString(packet.getData()));
//                        }
//                        packet.setLength(this.PACKET_SIZE);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

            this.receiveAudio(packet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    private void receiveAudio(DatagramPacket packet) {
        System.out.println("RECEIVE AUDIO");
        try{
            this.socket = new MulticastSocket(PORT);                                    //create a datagram socket

            //DatagramSocket socket = new DatagramSocket(4200);							//create another socket for packet handling
            //DatagramPacket packet1 = new DatagramPacket(new byte[16], 16);

            while (this.playState) {
                System.out.println("RECCCC");
                this.socket.receive(packet);

                //socket.receive(packet1);

                //byte[] errorData = packet1.getData();
                //printOnce(errorData);

                long end_time = System.nanoTime();

                byte[] data = packet.getData();
                byteArrayInputStream = new ByteArrayInputStream(data);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);

                try{
                    AudioPacket audioPacket = (AudioPacket) objectInputStream.readObject();
                    System.out.println(audioPacket.getSequenceNo());
                    int val = audioPacket.getSequenceNo() - count;

                    if (val > 1) {
                        int tempPort = 4200;
//                        InetAddress addr = InetAddress.getByName(obj.to);
                        System.out.println("packet " + (count + 1) + " was lost");
//                        packetHandler.handlePacketLoss(addr, tempPort);

                    } else {
                        this.getSourceDataLine().write(audioPacket.getAudioData(), 0, this.PACKET_SIZE); //playing the audio	                        //write data received from the packet to the dataline
                        System.out.println("Packet No:" + audioPacket.getSequenceNo());
                    }

                    count++;

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }


        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        } finally {
            System.out.println("turning off peer receiver.....");
            this.socket.close();
        }
    }

    public boolean getPlayState() {
        return playState;
    }

    public void stopPlay() {
        this.playState = false;
    }

    public void startPlay() {
        this.playState = true;
    }

}
