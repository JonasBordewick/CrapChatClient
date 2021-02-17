package com.itsanoj.gui;


import com.itsanoj.chat.ChatClient;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class CrapChat extends Application {
    Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.getIcons().add(new Image(CrapChat.class.getResourceAsStream("icon.png")));
        FXMLLoader loginLoader = new FXMLLoader(CrapChat.class.getResource("loginframe.fxml"));
        LoginController loginController = new LoginController(this);
        loginLoader.setController(loginController);
        Parent root = loginLoader.load();
        window.setTitle("CrapChat™");
        window.setScene(new Scene(root));
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    loginController.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void switchToChat(ChatClient client) throws IOException {
        ChatController chatController = new ChatController(client);
        FXMLLoader chatLoader = new FXMLLoader(CrapChat.class.getResource("chat_frame.fxml"));
        chatLoader.setController(chatController);
        Parent root = chatLoader.load();
        window.setScene(new Scene(root));
        window.setTitle("CrapChat™");
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    chatController.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        window.show();
    }

}