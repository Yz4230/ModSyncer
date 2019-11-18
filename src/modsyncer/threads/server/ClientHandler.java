package modsyncer.threads.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
            int numMods = inputStream.readInt();
            String hash = inputStream.readUTF();
            System.out.println(numMods);
            System.out.println(hash);
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
