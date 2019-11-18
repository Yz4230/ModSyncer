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
            System.out.println("Server was Launched");
            while (true) {
                Socket socket = serverSocket.accept();
                if (socket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    if (inputStream.readUTF().equals("terminate-server")) {
                        socket.close();
                        serverSocket.close();
                        System.out.println("Server was Closed");
                        break;
                    }
                }
                System.out.println("Client is Coming");
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
            outputStream.writeUTF("terminate-server");
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
