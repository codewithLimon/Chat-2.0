package com.example.messenger.Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private VBox chatBox;
    private TextArea chatArea;

    private final int PORT = 1234;

    @Override
    public void start(Stage primaryStage) throws Exception {

        chatArea = new TextArea();
        chatArea.setEditable(false);

        chatBox = new VBox();
        chatBox.setPadding(new Insets(10, 10, 10, 10));
        chatBox.setSpacing(10);
        chatBox.getChildren().add(chatArea);

        Scene scene = new Scene(chatBox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.show();

        startServer();
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                while (true) {
                    socket = serverSocket.accept();
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF("Connected to server.");

                    new Thread(() -> {
                        try {
                            while (true) {
                                String message = input.readUTF();
                                Platform.runLater(() -> {
                                    addMessageToChatBox(message, false);
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addMessageToChatBox(String message, boolean isClient) {
        Label messageLabel = new Label(message);
        if (isClient) {
            messageLabel.setStyle("-fx-background-color: #add8e6; -fx-padding: 5px; -fx-border-radius: 10px;");
            messageLabel.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        } else {
            messageLabel.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 5px; -fx-border-radius: 10px;");
            messageLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        }
        chatBox.getChildren().add(messageLabel);
        chatArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
