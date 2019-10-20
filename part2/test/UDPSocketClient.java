package test;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketClient {
    private DatagramSocket Socket;

    public UDPSocketClient() {

    }

    public static void main(String[] args) {
        UDPSocketClient client = new UDPSocketClient();
        client.createAndListenSocket();
    }

    public void createAndListenSocket() {
        try{

            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            Student student = new Student(1, "Bijoy", "Kerala");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(student);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
            Socket.send(sendPacket);
            System.out.println("Message sent from client");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            try{
                student = (Student) is.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Response from server:" + student.toString());
            Thread.sleep(200);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
