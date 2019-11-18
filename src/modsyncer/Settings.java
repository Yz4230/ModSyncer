package modsyncer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static String filePath;
    public static Properties properties = new Properties();
    public static final String TAG_MODS_FILEPATH = "mods_filepath";
    public static final String TAG_IP_CLIENT = "ip_client";
    public static final String TAG_IP_SERVER = "ip_server";
    public static final String TAG_SERVER_MODE = "server_mode";

    public static String MODS_FILEPATH;
    public static String IP_CLIENT;
    public static String IP_SERVER;
    public static boolean SERVER_MODE;

    public static void load(String filePath) {
        Settings.filePath = filePath;
        try {
            File file = new File(Settings.filePath);
            if (!file.exists())
                file.createNewFile();
            properties.load(new FileReader(file));
            MODS_FILEPATH = properties.getProperty(TAG_MODS_FILEPATH, "");
            IP_CLIENT = properties.getProperty(TAG_IP_CLIENT, "");
            IP_SERVER = properties.getProperty(TAG_IP_SERVER, "");
            SERVER_MODE = Boolean.parseBoolean(properties.getProperty(TAG_SERVER_MODE, "false"));

            System.out.println("Config file " + Settings.filePath + " was loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            properties.setProperty(TAG_MODS_FILEPATH, MODS_FILEPATH);
            properties.setProperty(TAG_IP_CLIENT, IP_CLIENT);
            properties.setProperty(TAG_IP_SERVER, IP_SERVER);
            properties.setProperty(TAG_SERVER_MODE, Boolean.toString(SERVER_MODE));

            properties.store(new FileWriter(filePath), "Minecraft Mod Syncer config file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
