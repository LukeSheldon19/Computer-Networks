package pa;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ClientConnection {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClient() {
        try {
            // 1. Expect "rdy"
            String msg = in.readUTF();
            if (!msg.equals("rdy")) return;

            // Respond "rdy"
            out.writeUTF("rdy");
            out.flush();

            // 2. Expect "fName"
            msg = in.readUTF();
            if (!msg.equals("fName")) return;

            // Receive actual filename request
            String requestedFile = in.readUTF();
            File file = new File(Server.FILES_PATH + requestedFile);

            if (!file.exists()) {
                out.writeUTF("ERR_NO_FILE");
                out.flush();
                return;
            }

            // Send file name
            out.writeUTF(file.getName());
            out.flush();

            // 3. Expect "rdyD"
            msg = in.readUTF();
            if (!msg.equals("rdyD")) return;

            // Send the file bytes
            sendFileBytes(file);

            // 4. Expect "close"
            msg = in.readUTF();
            if (msg.equals("close")) {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFileBytes(File file) throws IOException {
        byte[] data = Files.readAllBytes(file.toPath());
        out.writeInt(data.length);   // send length first
        out.write(data);             // send raw file bytes
        out.flush();
    }
}

