package modsyncer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import modsyncer.Sides.Client;
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

public class Controller implements Initializable {
    public Text date_added_TX;
    public Text full_path_TX;
    public Text num_of_files_TX;
    public CheckBox server_mode_CBX;
    public ListView<String> modList_LV;
    public TextField modsPass_TF;
    public TextField ip_address_TF;
    public Button sync_BTN;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.UpdateModFiles();
        this.modsPass_TF.setText(Main.MODS_FILEPATH);
    }

    @FXML
    protected void onUpdateButtonClick(ActionEvent event) {
        Main.MODS_FILEPATH = this.modsPass_TF.getText();
        this.modList_LV.getItems().clear();
        this.UpdateModFiles();
    }

    @FXML
    public void onSyncButtonClick(ActionEvent event) {
        if (this.ip_address_TF.getText().isEmpty()) return;
        if (this.server_mode_CBX.isSelected()) {
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
            Main.clientInst = new Client();
            Main.clientInst.parseAddress(this.ip_address_TF.getText());
            Main.clientInst.sync();
        }
    }

    @FXML
    public void onSelect(InputEvent inputEvent) {
        if (inputEvent instanceof KeyEvent && !((KeyEvent) inputEvent).getCode().isArrowKey()) return;
        String fullPath = Main.MODS_FILEPATH + "\\" + this.modList_LV.getSelectionModel().getSelectedItem();
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
        try {
            Main.ipChecker.join();
            if (this.server_mode_CBX.isSelected())
                this.ip_address_TF.setPromptText("[port] here, your global IP address is '" + IpChecker.globalIpAddr + "'");
            else
                this.ip_address_TF.setPromptText("[IP address]:[port] here");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void UpdateModFiles() {
        File dir = new File(Main.MODS_FILEPATH);
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.stream(files).filter(File::isFile).forEach(f -> this.modList_LV.getItems().add(f.getName()));
            this.num_of_files_TX.setText(String.valueOf(files.length));
        }
    }
}
