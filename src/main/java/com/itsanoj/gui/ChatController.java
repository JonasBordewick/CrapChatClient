package com.itsanoj.gui;

import com.itsanoj.chat.ChatClient;
import com.itsanoj.chat.MessageListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController implements MessageListener {

    private final ChatClient client;

    @FXML
    private TextArea chatEnterField;

    @FXML
    private TextArea chatViewField;

    private ObservableList<String> messages = FXCollections.observableArrayList();;
    @FXML
    private Button sendButton, logoutButton;

    @FXML
    private void handleSendButton(ActionEvent event){
        String msg = chatEnterField.getText();
        msg = msg.trim();
        msg = msg.replaceAll("\n", " ");
        msg = msg.replaceAll("\t", "    ");
        if (!msg.equalsIgnoreCase("")){
            try {
                client.msg(msg);
            } catch (IOException e) {
                serverException();
            }
        }
        chatEnterField.setText("");
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) throws IOException {
        client.logout();
        Platform.exit();
        System.exit(0);
    }

    public void logout() throws IOException {
        client.logout();
        Platform.exit();
        System.exit(0);
    }

    public ChatController(ChatClient client){
        this.client = client;
        client.addMessageListener(this);
    }


    @FXML
    public void initialize(){
        Image send = new Image(ChatController.class.getResourceAsStream("send.png"));
        sendButton.setGraphic(new ImageView(send));
        client.startMessageReader(this);
    }

    public synchronized void serverException(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add
                (new Image(ChatController.class.getResourceAsStream("icon.png")));
        alert.setTitle("Keine Verbindung zum CrapChat™ Server ");
        alert.setHeaderText("Der CrapChat™ Server konnte nicht erreicht werden.");
        alert.setContentText("Der CrapChat™ Server ist vermutlich offline, versuche es später erneut.");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myAlert");
        alert.showAndWait();
        Platform.exit();
    }

    @Override
    public void onMessage(String msg) {
        chatViewField.appendText(msg + "\n");
    }
}
