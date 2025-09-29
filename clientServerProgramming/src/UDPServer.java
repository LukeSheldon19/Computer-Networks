import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        int serverPort = 12000;

        try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {
            System.out.println("The server is ready to receive");

            byte[] receiveBuffer = new byte[2048];

            while (true) {
                // Receive packet
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String modifiedMessage = message.toUpperCase();

                // Send back modified message
                byte[] sendBuffer = modifiedMessage.getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
