package modsyncer.threads.client;

import modsyncer.Controller;
import modsyncer.Main;
import modsyncer.Settings;
import org.json.JSONObject;

import java.io.*;
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
        System.out.println("Client was Launched.");
        try {
            this.socket = new Socket(this.serverIp, this.port);
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());

            if (this.serverIp.equals("127.0.0.1")) {
                outputStream.writeUTF("hello_server");
            }
            outputStream.writeUTF(this.createJson().toString());
            int numModsDownload = inputStream.readInt();
            if (numModsDownload > 0) {
                System.out.println(numModsDownload + " mod(s) will be downloaded.");
                for (int i = 0; i < numModsDownload; i++) {
                    String fileName = inputStream.readUTF();
                    if (!Settings.MODS_FILEPATH.matches(".*\\&")) {
                        fileName = "\\" + fileName;
                    }
                    File file = new File(Settings.MODS_FILEPATH + fileName);
                    file.createNewFile();
                    System.out.println("Downloaded : " + Settings.MODS_FILEPATH + file.getPath());
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] data = new byte[4096];
                    int size;
                    while ((size = inputStream.read(data)) != -1) {
                        fileOutputStream.write(data, 0, size);
                    }
                    fileOutputStream.close();
                    Controller.INSTANCE.updateModFiles();
                }
                System.out.println("Mod(s) has(have) been updated successfully!");
            } else System.out.println("Mod(s) has(have) been already updated.");

            inputStream.close();
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
        json.put("mods_list", Main.modFiles.stream().map(File::getName).toArray());
        return json;
    }
}
