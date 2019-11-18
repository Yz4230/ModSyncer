package modsyncer.threads.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private final String CMD_HALT_SERVER = "halt_server";
    private int port;

    public ServerThread(int portNumber) {
        this.port = portNumber;
    }

    @Override
    public void run() {
        System.out.println("Server was Launched");
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                if (socket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    if (inputStream.readUTF().equals(this.CMD_HALT_SERVER)) {
                        socket.close();
                        serverSocket.close();
                        System.out.println("Server was Closed");
                        break;
                    } else {
                        System.out.println("DEBUG:Local Host was Connected");
                    }
                }
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            Socket socket = new Socket("localhost", this.port);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(this.CMD_HALT_SERVER);
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
