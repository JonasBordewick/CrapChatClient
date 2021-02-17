package com.itsanoj.gui;

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

import java.io.IOException;

public class LoginController {

    private final ChatClient client;
    private CrapChat main;

    public LoginController(CrapChat main){
        this.main = main;
        this.client = new ChatClient("82.165.114.226", 8818);
        try {
            client.connect();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add
                    (new Image(LoginController.class.getResourceAsStream("icon.png")));
            alert.setTitle("CrapChat™ Server ist offline");
            alert.setHeaderText("Der CrapChat™ Server ist offline");
            alert.setContentText("Der CrapChat™ Server  ist offline, versuche es später erneut.");
            alert.getDialogPane().getStylesheets().add(LoginController.class.getResource("style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("myAlert");
            alert.showAndWait();
            Platform.exit();
        }
    }

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    public void initialize(){
        loginField.requestFocus();
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
            main.switchToChat(client);
        }
    }

    public void disconnect() throws IOException {
        client.disconnect();
        Platform.exit();
    }

}
