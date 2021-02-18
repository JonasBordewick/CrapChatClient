package com.itsanoj.chat;

import com.itsanoj.gui.ChatController;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {

    private final int port;
    private final String serverName;
    private Socket socket;
    private BufferedReader in;
    private List<MessageListener> messageListeners;
    private static final String LINE_SEPARTOR = System.getProperty("line.separator");
    private OutputStream serverOut;
    private InputStream serverIn;

    public ChatClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
        messageListeners = new ArrayList<>();
    }

    /**
     * Connects this Client to the Server.
     * @throws IOException if the Server does not exist.
     */
    public void connect() throws IOException {
        this.socket = new Socket(serverName, port);
        this.serverOut = socket.getOutputStream();
        this.serverIn = socket.getInputStream();
        this.in = new BufferedReader(new InputStreamReader(serverIn, StandardCharsets.UTF_8));
    }

    /**
     * Disconnect this Client from the Server.
     * @throws IOException if an I/O error occurs when closing this socket.
     */
    public void disconnect() throws IOException {
        socket.close();
    }

    /**
     * Executes the Login Command
     * @param login nickname
     * @throws LoginException if the Login was not successful
     * @throws IOException if the server output stream does not exist.
     */
    public void login(String login) throws LoginException, IOException {
        System.out.println("[Client] Logging in");
        String cmd = "login "+ login +LINE_SEPARTOR;
        serverOut.write(cmd.getBytes(StandardCharsets.UTF_8));
        if (!loginAvailable()){
            in = new BufferedReader(new InputStreamReader(serverIn, StandardCharsets.UTF_8));
            throw new LoginException();
        }
    }

    /**
     * Checks if the login is successful or not.
     * @return eiter true or false
     * @throws IOException if the server in does not exist.
     */
    private boolean loginAvailable() throws IOException {
        String line;
        in.mark(8192);
        while ((line = in.readLine()) != null){
            if (line.equalsIgnoreCase("login Exception")){
                in.reset();
                return false;
            } else if (line.equalsIgnoreCase("[Server] Erfolgreich eingeloggt")){
                in.reset();
                return true;
            }
        }
        in.reset();
        return false;
    }

    
    public void msg(String msg) throws IOException {
        System.out.println("[Client] Sending message");
        String cmd = "msg "+ msg +LINE_SEPARTOR;
        serverOut.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    public void logout() throws IOException {
        System.out.println("[Client] Try to logout");
        String cmd = "logout"+LINE_SEPARTOR;
        serverOut.write(cmd.getBytes(StandardCharsets.UTF_8));
    }

    private void handleMessage(String msg){
        for (MessageListener ml : messageListeners)
            ml.onMessage(msg);
    }
    public void addMessageListener(MessageListener messageListener){
        messageListeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener){
        messageListeners.remove(messageListener);
    }

    public void startMessageReader(ChatController controller){
        System.out.println("[Information] Message reader started.");
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    readMessageLoop();
                } catch (IOException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.serverException();
                        }
                    });
                }
            }
        };
        thread.start();
    }

    private void readMessageLoop() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            handleMessage(line);
        }
    }
}
