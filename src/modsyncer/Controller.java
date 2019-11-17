package modsyncer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import modsyncer.Sides.Client;
import modsyncer.Sides.Server;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UpdateModFiles();
        modsPass_TF.setText(Main.MODS_FILEPATH);
    }

    @FXML
    protected void onUpdateButtonClick(ActionEvent event) {
        Main.MODS_FILEPATH = modsPass_TF.getText();
        modList_LV.getItems().clear();
        UpdateModFiles();
    }

    @FXML
    public void onSyncButtonClick(ActionEvent event) {
        if (ip_address_TF.getText().isEmpty()) return;
        if (server_mode_CBX.isSelected()) {
            Main.serverInst = new Server();
            Main.serverInst.parseAddress(ip_address_TF.getText());
        } else {
            Main.clientInst = new Client();
            Main.clientInst.parseAddress(ip_address_TF.getText());
        }
    }

    @FXML
    public void onSelect(InputEvent inputEvent) {
        if (inputEvent instanceof KeyEvent && !((KeyEvent) inputEvent).getCode().isArrowKey()) return;
        String fullPath = Main.MODS_FILEPATH + "\\" + modList_LV.getSelectionModel().getSelectedItem();
        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(Paths.get(fullPath), BasicFileAttributes.class);
            FileTime time = attributes.creationTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date_added_TX.setText(dateFormat.format(new Date(time.toMillis())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        full_path_TX.setText(fullPath);
    }

    public void onServerModeCheckBoxChanged(ActionEvent event) {
        try {
            Main.ipChecker.join();
            if (server_mode_CBX.isSelected())
                ip_address_TF.setPromptText("[port] here, your global IP address is '" + IpChecker.globalIpAddr + "'");
            else
                ip_address_TF.setPromptText("[IP address]:[port] here");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void UpdateModFiles() {
        File dir = new File(Main.MODS_FILEPATH);
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.stream(files).filter(File::isFile).forEach(f -> modList_LV.getItems().add(f.getName()));
            num_of_files_TX.setText(String.valueOf(files.length));
        }
    }
}
