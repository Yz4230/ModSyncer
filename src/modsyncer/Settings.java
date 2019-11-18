package modsyncer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    public static Properties properties = new Properties();
    private static String filePath;
    public static final String TAG_MODS_PATH = "mods_folder_path";
    public static final String TAG_SERVER_IP = "server_ip";

    public static void load(String filePath) {
        Settings.filePath = filePath;
        try {
            properties.load(new FileReader(Settings.filePath));
            Main.SERVER_IP = properties.getProperty("server_ip", "0.0.0.0");
            Main.MODS_FILEPATH = properties.getProperty("mods_folder_path", "%APPDATA%\\Roaming\\.minecraft\\mods");
            System.out.println("Config file " + Settings.filePath + " was loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            properties.setProperty("server_ip", Main.SERVER_IP);
            properties.setProperty("mods_folder_path", Main.MODS_FILEPATH);
            properties.store(new FileWriter(filePath), "MMS config file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
