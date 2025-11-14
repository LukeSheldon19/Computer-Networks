package pa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 3030;

    // Folder containing files the server can send
    public static final String FILES_PATH = "files/";

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> new ClientConnection(client).handleClient()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

