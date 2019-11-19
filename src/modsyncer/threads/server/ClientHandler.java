package modsyncer.threads.server;

import modsyncer.Main;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
            JSONObject json = new JSONObject(inputStream.readUTF());
            List<String> clientModList = json
                    .getJSONArray("mods_list")
                    .toList()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            List<File> sendModList = Arrays.stream(Main.modFiles)
                    .filter(file -> clientModList.contains(file.getName()))
                    .collect(Collectors.toList());
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
            sendModList.forEach(file -> {
                byte[] data = new byte[4096];
                int count;
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    while ((count = fileInputStream.read(data)) > 0)
                        outputStream.write(data, 0, count);
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputStream.close();
            inputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
