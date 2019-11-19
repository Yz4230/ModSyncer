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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    public static final String CONFIG_FILEPATH = "./modsyncer_config.properties";
    public static IpChecker ipChecker = new IpChecker();

    public static Server serverInst = new Server();
    public static Client clientInst = new Client();

    public static List<File> modFiles = new ArrayList<>();
    public static List<String> modNames = new ArrayList<>();

    public static void main(String[] args) {
        ipChecker.start();
        Settings.load(CONFIG_FILEPATH);
        if (args.length > 0) {
            List<String> argList = Arrays.asList(args);
            if (argList.contains("-s")) Settings.SERVER_MODE = true;
            else if (argList.contains("-c")) Settings.SERVER_MODE = false;
            if (argList.contains("-p")) {
                Settings.MODS_FILEPATH = argList.get(argList.indexOf("-p") + 1);
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("Main.fxml"));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("update_icon.png")));
        primaryStage.setTitle("Minecraft Mod Syncer");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() {
        Settings.save();
    }
}
