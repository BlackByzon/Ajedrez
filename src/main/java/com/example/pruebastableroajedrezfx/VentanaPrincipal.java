package com.example.pruebastableroajedrezfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VentanaPrincipal extends Application {
    static HelloApplication ajedrezApp = new HelloApplication();
    public static HelloApplication mandarInstancia (){
        return ajedrezApp;
    }

    @Override
    public void start(Stage primaryStage) {
        Button botonAjedrez = new Button("Abrir Tablero de Ajedrez");
        Button botonCSV = new Button("Entrar generador de comienzos ");
        Button CSVCustomizado = new Button("Entrar a carpeta de CSV customizados ");

        botonAjedrez.setOnAction(e -> abrirVentanaAjedrez(primaryStage));

        botonCSV.setOnAction(e -> {
            abrirVentanaCSV(primaryStage);
        });
        CSVCustomizado.setOnAction(actionEvent -> abrirCarpeta(primaryStage));

        StackPane root = new StackPane();
        root.getChildren().addAll(botonAjedrez, botonCSV, CSVCustomizado);

        botonAjedrez.setTranslateY(-30);
        botonCSV.setTranslateY(30);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Ventana Principal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void abrirVentanaAjedrez(Stage primaryStage) {
        ajedrezApp = new HelloApplication();
        try {
            Main.inicio("juegoNormal", true);
            ajedrezApp.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void abrirVentanaCSV(Stage primaryStage) {
        VentanaGenerarCSV App = new VentanaGenerarCSV();
        try {
            Main.inicio("null", false);
            App.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void abrirCarpeta(Stage primaryStage) {
        TablaCarpetaCSV ajedrezApp = new TablaCarpetaCSV();
        try {
            ajedrezApp.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
