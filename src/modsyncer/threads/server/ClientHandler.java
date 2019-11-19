package modsyncer.threads.server;

import modsyncer.Main;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());

            JSONObject json = new JSONObject(inputStream.readUTF());
            List<String> clientModList = json
                    .getJSONArray("mods_list")
                    .toList()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            List<File> sendModList = new ArrayList<>(Main.modFiles);
            sendModList.removeIf(file -> clientModList.contains(file.getName()));

            outputStream.writeInt(sendModList.size());
            sendModList.forEach(file -> {
                byte[] data = new byte[4096];
                int size;
                try {
                    outputStream.writeUTF(file.getName());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    while ((size = fileInputStream.read(data)) != -1)
                        outputStream.write(data, 0, size);
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            inputStream.close();
            outputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
