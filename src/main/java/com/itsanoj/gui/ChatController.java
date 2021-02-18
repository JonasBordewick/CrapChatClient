package com.itsanoj.gui;

import com.itsanoj.chat.ChatClient;
import com.itsanoj.chat.MessageListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class ChatController implements MessageListener {

    private final ChatClient client;
    private final CrapChat main;

    @FXML
    private TextArea chatEnterField;

    @FXML
    private TextArea chatViewField;

    private ObservableList<String> messages = FXCollections.observableArrayList();;
    @FXML
    private Button sendButton, logoutButton;

    @FXML
    public void handleSendButton(ActionEvent event){
        send();
    }

    @FXML
    public void onEnter(KeyEvent e) {
        if(e.getCode().toString().equalsIgnoreCase("enter")) {
            send();
        }
    }

    public void send(){
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

    public ChatController(ChatClient client, CrapChat main){
        this.main = main;
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
        main.serverOfflineAlert();
    }

    @Override
    public void onMessage(String msg) {
        chatViewField.appendText(msg + "\n");
    }
}
