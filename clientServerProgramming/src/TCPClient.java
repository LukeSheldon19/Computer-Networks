import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        String serverName = "localhost";
        int serverPort = 12000;

        try (Socket clientSocket = new Socket(serverName, serverPort);
             Scanner scanner = new Scanner(System.in);
             DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
             BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.print("Input lowercase sentence: ");
            String sentence = scanner.nextLine();

            // Send message to server
            outToServer.writeBytes(sentence + '\n');

            // Receive response from server
            String modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
