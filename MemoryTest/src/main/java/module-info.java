module com.example.memorytest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.memorytest to javafx.fxml;
    exports com.example.memorytest;
}