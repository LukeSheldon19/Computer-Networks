package pa;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;

    // Folder where client will save downloaded files
    public static final String RECEIVED_PATH = "received/";

    public Client() {
        try {
            socket = new Socket("localhost", Server.PORT);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            scanner = new Scanner(System.in);

            runProtocol();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runProtocol() throws IOException {

        System.out.print("Type \"rdy\" to initiate protocol \n");
        String initConn = scanner.nextLine();

        while (!initConn.equals("rdy")) {
            System.out.print("Type \"rdy\" to initiate protocol \n");
            initConn = scanner.nextLine();
        }

        out.writeUTF(initConn);
        out.flush();

        String msg = in.readUTF();
        if (!msg.equals("rdy")) {
            System.out.println("Server did not accept handshake");
            return;
        }

        // Ask user which file to download
        System.out.print("Enter the file name (with extension) to download: ");
        String filename = scanner.nextLine();

        // 2. Request filename
        out.writeUTF("fName");
        out.writeUTF(filename);
        out.flush();

        // Receive actual filename or error
        String serverResponse = in.readUTF();
        if (serverResponse.equals("ERR_NO_FILE")) {
            System.out.println("Server: File not found.");
            return;
        }

        String returnedFileName = serverResponse;
        System.out.println("Server will send: " + returnedFileName);

        // 3. Ready to download
        out.writeUTF("rdyD");
        out.flush();

        // Receive size and bytes
        int size = in.readInt();
        byte[] fileData = in.readNBytes(size);

        // Ensure received directory exists
        new File(RECEIVED_PATH).mkdirs();

        // Save file
        File outFile = new File(RECEIVED_PATH + returnedFileName);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(fileData);
        }

        System.out.println("Downloaded and saved to: " + outFile.getAbsolutePath());

        // 4. Close connection
        out.writeUTF("close");
        out.flush();
        socket.close();
    }

    public static void main(String[] args) {
        new Client();
    }
}

