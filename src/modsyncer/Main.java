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

public class Main extends Application {
    public static final String CONFIG_FILEPATH = "./modsyncer_config.properties";
    public static IpChecker ipChecker = new IpChecker();

    public static Server serverInst = new Server();
    public static Client clientInst = new Client();

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

    public static void main(String[] args) {
        Settings.load(CONFIG_FILEPATH);
        ipChecker.start();
        launch(args);
    }
}
