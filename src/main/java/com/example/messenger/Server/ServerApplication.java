package com.example.messenger.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root= FXMLLoader.load(ServerApplication.class.getResource("Server.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.show();
        Image icon=new Image(ServerApplication.class.getResource("database.png").openStream());
        primaryStage.getIcons().add(icon);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
