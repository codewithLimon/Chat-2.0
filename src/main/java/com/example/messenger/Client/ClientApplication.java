package com.example.messenger.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root= FXMLLoader.load(ClientApplication.class.getResource("client.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
        Image icon=new Image(ClientApplication.class.getResource("patient.png").openStream());
        primaryStage.getIcons().add(icon);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
