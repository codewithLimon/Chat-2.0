package com.example.messenger.Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientController implements Initializable {

    @FXML
    ScrollPane _scrollBody;

    @FXML
    VBox _vboxBody;

    @FXML
    Button _sendButton;

    @FXML
    TextField _messageBox;

    private PrintWriter printWriter=null;
    private Socket socket;

    private Scanner readMessage=null;
    private final int _port=22222;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            socket = new Socket("localhost", _port);
            System.out.println("Accepted");

            printWriter = new PrintWriter(socket.getOutputStream(), true);
            readMessage = new Scanner(socket.getInputStream());
            Thread t = new Thread(() -> {
                while(true){
                    String input = readMessage.nextLine();

                    Text text = new Text(input);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-fill: white;" +
                            "-fx-background-color: #00ffa4;" +
                            "-fx-font-size: 13px;" +
                            "-fx-background-radius: 25px;" +
                            "-fx-padding: 10px");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    textFlow.setMaxWidth(300);

                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    hBox.getChildren().add(textFlow);
                    _vboxBody.getChildren().add(hBox);
                }
            });
            t.start();
        } catch (Exception e) {
        }

        _sendButton.setOnAction(event -> {
            String message = _messageBox.getText().trim();

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-fill: white;" +
                    "-fx-background-color: #00ffa4;" +
                    "-fx-font-size: 13px;" +
                    "-fx-background-radius: 25px;" +
                    "-fx-padding: 10px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            textFlow.setMaxWidth(300);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    printWriter.println(message);
                }
            }).start();


            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            hBox.getChildren().add(textFlow);
            _vboxBody.getChildren().add(hBox);
        });

        try{
            socket=new Socket("Localhost",_port);
            System.out.println("Connected to server..");

            printWriter=new PrintWriter(socket.getOutputStream(),true);
            readMessage=new Scanner(socket.getInputStream());

        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
