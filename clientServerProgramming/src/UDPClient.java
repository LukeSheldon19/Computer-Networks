import java.net.*;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        String serverName = "localhost";
        int serverPort = 12000;

        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Input lowercase sentence: ");
            String message = scanner.nextLine();

            byte[] sendBuffer = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName(serverName);

            // Send message
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            // Receive response
            byte[] receiveBuffer = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            String modifiedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("FROM SERVER: " + modifiedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
