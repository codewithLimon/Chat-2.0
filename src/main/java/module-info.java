module com.example.messenger {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.messenger.Server to javafx.fxml;
    exports com.example.messenger.Server;

    opens com.example.messenger.Client to javafx.fxml;
    exports com.example.messenger.Client;
}