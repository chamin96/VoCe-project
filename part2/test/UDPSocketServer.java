package test;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketServer {
    private DatagramSocket socket = null;

    public UDPSocketServer() {

    }

    public static void main(String[] args) {
        UDPSocketServer server = new UDPSocketServer();
        server.createAndListenSocket();
    }

    public void createAndListenSocket() {
        try{
            socket = new DatagramSocket(9876);
            byte[] incomingData = new byte[1024];

            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Student student = null;
                try{
                    student = (Student) is.readObject();
                    System.out.println("Student object received = " + student);
                    student.setName("Luke");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                String reply = "Thank you for the message";
                byte[] replyBytea = reply.getBytes();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(student);
                data = outputStream.toByteArray();
                DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);
                socket.send(replyPacket);
                Thread.sleep(200);
//                System.exit(0);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
