package modsyncer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modsyncer.Sides.Client;
import modsyncer.Sides.Server;
import modsyncer.threads.IpChecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {
    public static final String CONFIG_FILEPATH = "./modsyncer_config.properties";
    public static String MODS_FILEPATH;
    public static String SERVER_IP;
    public static IpChecker ipChecker = new IpChecker();

    public static Server serverInst = new Server();
    public static Client clientInst = new Client();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("update_icon.png")));
        primaryStage.setTitle("Minecraft Mod Syncer");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        File configFile = new File(CONFIG_FILEPATH);
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            properties.setProperty("server_ip", SERVER_IP);
            properties.setProperty("mods_folder_path", MODS_FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        File configFile = new File(CONFIG_FILEPATH);
        Properties properties = new Properties();
        try {
            if (!configFile.exists()) configFile.createNewFile();
            InputStream inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            SERVER_IP = properties.getProperty("server_ip");
            MODS_FILEPATH = properties.getProperty("mods_folder_path");
            System.out.println(SERVER_IP);
            System.out.println(MODS_FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ipChecker.start();
        launch(args);
    }
}
