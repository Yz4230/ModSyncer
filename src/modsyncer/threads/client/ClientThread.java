package modsyncer.threads.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private String serverIp;
    private int port;

    public ClientThread(String ipAddress, int portNumber) {
        this.serverIp = ipAddress;
        this.port = portNumber;
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.serverIp, this.port);
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
            outputStream.writeInt(114);
            outputStream.writeUTF("hash");
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
