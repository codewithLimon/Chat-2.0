package com.example.messenger.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private VBox chatBox;
    private TextArea chatArea;
    private TextField messageField;

    private final String SERVER_ADDRESS = "localhost";
    private final int PORT = 1234;

    @Override
    public void start(Stage primaryStage) throws Exception {

        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageField = new TextField();
        messageField.setPrefWidth(350);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                sendMessageToServer(message);
                messageField.clear();
            }
        });

        HBox messageBox = new HBox();
        messageBox.setSpacing(10);
        messageBox.getChildren().addAll(messageField, sendButton);

        chatBox = new VBox();
        chatBox.setPadding(new Insets(10, 10, 10, 10));
        chatBox.setSpacing(10);
        chatBox.getChildren().addAll(chatArea, messageBox);

        Scene scene = new Scene(chatBox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, PORT);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("Connected to server.");

                new Thread(() -> {
                    try {
                        while (true) {
                            String message = input.readUTF();
                            if (message.startsWith("[image]")) {
                                String imageName = message.substring(7);
                                Platform.runLater(() -> {
                                    addImageToChatBox(imageName, true);
                                });
                            } else {
                                Platform.runLater(() -> {
                                    addMessageToChatBox(message, true);
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessageToServer(String message) {
        try {
            output.writeUTF(message);
            addMessageToChatBox(message, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMessageToChatBox(String message, boolean isClient) {
        Label messageLabel = new Label(message);
        if (isClient) {
            messageLabel.setStyle("-fx-background-color: #add8e6; -fx-padding: 5px; -fx-border-radius: 10px;");
            messageLabel.setAlignment(Pos.CENTER_LEFT);
        } else {
            messageLabel.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 5px; -fx-border-radius: 10px;");
            messageLabel.setAlignment(Pos.CENTER_RIGHT);
        }
        chatBox.getChildren().add(messageLabel);
        chatArea.appendText(message + "\n");
    }

    private void addImageToChatBox(String imageName, boolean isClient) {
        Image image = new Image(getClass().getResourceAsStream(imageName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        if (isClient) {
            imageView.setLayoutX(10);
            imageView.setLayoutY(10);
        } else {
            imageView.setLayoutX(290);
            imageView.setLayoutY(10);
        }
        chatBox.getChildren().add(imageView);
        chatArea.appendText("[Image: " + imageName + "]\n");
    }
}
