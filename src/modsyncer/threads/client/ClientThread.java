package modsyncer.threads.client;

import modsyncer.Main;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;

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
        System.out.println("Client was Launched");
        try {
            this.socket = new Socket(this.serverIp, this.port);
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
            if (this.serverIp.equals("127.0.0.1"))
                outputStream.writeUTF("hello_server");
            outputStream.writeUTF(this.createJson().toString());
            outputStream.close();
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

    private JSONObject createJson() {
        JSONObject json = new JSONObject();
        json.put("mods_list", Arrays.stream(Main.modFiles).map(File::getName).toArray());
        return json;
    }
}
