package modsyncer.threads.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private int port;

    public ServerThread(int portNumber) {
        this.port = portNumber;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server is Running");
            while (true) {
                Socket socket = serverSocket.accept();
                if (socket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    if (inputStream.readUTF().equals("terminate-server")) {
                        socket.close();
                        System.out.println("Server Closed");
                        break;
                    }
                }
                System.out.println("Client is Coming");
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }
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
            outputStream.writeUTF("terminate-server");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
