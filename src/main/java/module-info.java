module com.example.pruebastableroajedrezfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.pruebastableroajedrezfx to javafx.fxml;
    exports com.example.pruebastableroajedrezfx;
}