import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int serverPort = 12000;

        try (ServerSocket welcomeSocket = new ServerSocket(serverPort)) {
            System.out.println("The server is ready to receive");

            while (true) {
                // Wait for a client connection
                Socket connectionSocket = welcomeSocket.accept();

                // Create input and output streams
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // Read message from client
                String clientSentence = inFromClient.readLine();
                String capitalizedSentence = clientSentence.toUpperCase() + '\n';

                // Send modified message back
                outToClient.writeBytes(capitalizedSentence);

                // Close the connection
                connectionSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
