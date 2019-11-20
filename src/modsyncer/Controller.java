package modsyncer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import modsyncer.threads.IpChecker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    public static Controller INSTANCE;
    public Text date_added_TX;
    public Text full_path_TX;
    public Text num_of_files_TX;
    public CheckBox server_mode_CBX;
    public ListView<String> modList_LV;
    public TextField modsPass_TF;
    public TextField ip_address_TF;
    public Button sync_BTN;
    public ProgressBar progress_PI;


    public Controller() {
        INSTANCE = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.updateModFiles();
        this.modsPass_TF.setText(Settings.MODS_FILEPATH);
        this.server_mode_CBX.setSelected(Settings.SERVER_MODE);
        this.updateIpAddressTF();
        this.progress_PI.setDisable(true)   ;
    }

    @FXML
    protected void onUpdateButtonClick(ActionEvent event) {
        Settings.MODS_FILEPATH = this.modsPass_TF.getText();
        this.modList_LV.getItems().clear();
        this.updateModFiles();
    }

    @FXML
    public void onSyncButtonClick(ActionEvent event) {
        if (this.ip_address_TF.getText().isEmpty()) return;
        if (Settings.SERVER_MODE) {
            Settings.IP_SERVER = this.ip_address_TF.getText();
            if (Main.serverInst.isRunning()) {
                Main.serverInst.terminate();
                this.sync_BTN.setText("Sync");
                this.server_mode_CBX.setDisable(false);
            } else {
                Main.serverInst.parseAddress(this.ip_address_TF.getText());
                Main.serverInst.sync();
                this.sync_BTN.setText("Stop");
                this.server_mode_CBX.setDisable(true);
            }
        } else {
            this.progress_PI.setDisable(false);
            Settings.IP_CLIENT = this.ip_address_TF.getText();
            Main.clientInst.parseAddress(this.ip_address_TF.getText());
            Main.clientInst.sync();
        }
    }

    @FXML
    public void onListItemSelect(InputEvent inputEvent) {
        if (inputEvent instanceof KeyEvent && !((KeyEvent) inputEvent).getCode().isArrowKey()) return;
        String fullPath = Settings.MODS_FILEPATH + "\\" + this.modList_LV.getSelectionModel().getSelectedItem();
        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(Paths.get(fullPath), BasicFileAttributes.class);
            FileTime time = attributes.creationTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.date_added_TX.setText(dateFormat.format(new Date(time.toMillis())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.full_path_TX.setText(fullPath);
    }

    public void onServerModeCheckBoxChanged(ActionEvent event) {
        if (this.server_mode_CBX.isSelected()) {
            Settings.IP_CLIENT = this.ip_address_TF.getText();
        } else {
            Settings.IP_SERVER = this.ip_address_TF.getText();
        }
        this.updateIpAddressTF();
    }

    public void updateModFiles() {
        File dir = new File(Settings.MODS_FILEPATH);
        File[] files = dir.listFiles();
        Main.modFiles.clear();
        Main.modNames.clear();
        if (files != null) {
            Main.modFiles.addAll(Arrays.stream(files).filter(File::isFile).collect(Collectors.toList()));
            Main.modNames.addAll(Main.modFiles.stream().map(File::getName).collect(Collectors.toList()));
        }
        this.modList_LV.getItems().addAll(Main.modNames);
        this.num_of_files_TX.setText(String.valueOf(Main.modFiles.size()));
    }

    public void updateIpAddressTF() {
        if (this.server_mode_CBX.isSelected()) {
            Settings.SERVER_MODE = true;
            String message = "[port] here";
            if (!IpChecker.globalIpAddr.isEmpty())
                message += ", your global IP address is '" + IpChecker.globalIpAddr + "'";
            this.ip_address_TF.setPromptText(message);
            this.ip_address_TF.setText(Settings.IP_SERVER);
        } else {
            Settings.SERVER_MODE = false;
            this.ip_address_TF.setPromptText("[IP address]:[port] here");
            this.ip_address_TF.setText(Settings.IP_CLIENT);
        }
    }
}
