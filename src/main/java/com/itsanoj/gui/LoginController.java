package com.itsanoj.gui;

import com.itsanoj.Config;
import com.itsanoj.chat.ChatClient;
import com.itsanoj.chat.LoginException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginController {

    private final ChatClient client;
    private CrapChat main;
    private Config config = null;
    private File configFile;

    public LoginController(CrapChat main) throws IOException {
        this.main = main;
        this.client = new ChatClient("82.165.114.226", 8818);
        //this.client = new ChatClient("localhost", 8818);
        setConfig();

        try {
            client.connect();
        } catch (IOException e) {
            main.serverOfflineAlert();
        }
    }


    private void setConfig() throws IOException {
        File dir = new File(System.getProperty("user.home")+File.separator+"CrapChatServer");
        dir.mkdir();
        this.configFile = new File(dir.getPath() + File.separator + "config.food");
        if (!configFile.exists()){
            configFile.createNewFile();
        } else {
            try {
                this.config = Config.loadConfig(configFile);
            } catch (Exception e) {
                System.out.println("[LoginController] Cannot load config, creating new config.");
            }
        }
        if (config == null)
            config = new Config();
    }

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    public void initialize(){
        loginField.requestFocus();
        if (config.getLastNickname() != null)
            loginField.setText(config.getLastNickname());
    }

    @FXML
    void handleLoginButton(ActionEvent event) throws IOException {
        String login = loginField.getText();
        login = login.trim();
        login = login.replace(" ", "");
        if (!login.equalsIgnoreCase("")){
            try {
                client.login(login);
            } catch (LoginException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add
                        (new Image(LoginController.class.getResourceAsStream("icon.png")));
                alert.setTitle("CrapChat™ Nickname schon vergeben.");
                alert.setHeaderText("Der Nickname " + loginField.getText() + " ist bereits vergeben.");
                alert.setContentText("Der Nickname " + loginField.getText() + " ist bereits vergeben. Versuche einen anderen Nickname.");
                alert.getDialogPane().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("myAlert");
                alert.showAndWait();
                return;
            }
            config.setLastNickname(login);
            config.saveConfig(configFile);
            main.switchToChat(client);
        }
    }

    public void disconnect() throws IOException {
        client.disconnect();
        Platform.exit();
    }

}
