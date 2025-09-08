package com.example.pruebastableroajedrezfx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class popUpEleccionAcension {
    static String baseImagen = "file:src/main/resources/images/";
    public static String mostrar(Stage owner, String color) {
        final String[] opcionSeleccionada = {null}; // Usar un array para capturar la opción

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(owner);
        popupStage.setTitle("Selecciona una opción");

        // Crear botones
        Button btnOpcion1 = new Button();
        String imagePathCaballo = baseImagen+"caballo"+color+".png";

        Image imageCaballo = new Image(imagePathCaballo); // Cargar la imagen desde la ruta

        ImageView imageViewCaballo = new ImageView(imageCaballo);
        imageViewCaballo.setFitHeight(60);
        imageViewCaballo.setFitWidth(60);
        imageViewCaballo.setPreserveRatio(true);

        btnOpcion1.setGraphic(imageViewCaballo);
        Button btnOpcion2 = new Button();
        String imagePathReina = baseImagen+"reina"+color+".png";
        Button btnOpcion3 = new Button();
        Image imageReina = new Image(imagePathReina);

        ImageView imageViewReina = new ImageView(imageReina);
        imageViewReina.setFitHeight(60);
        imageViewReina.setFitWidth(60);
        imageViewReina.setPreserveRatio(true);

        btnOpcion2.setGraphic(imageViewReina);

        String imagePathTorre = baseImagen+"torre"+color+".png";

        Image imageTorre = new Image(imagePathTorre);

        ImageView imageViewTorre = new ImageView(imageTorre);
        imageViewTorre.setFitHeight(60);
        imageViewTorre.setFitWidth(60);
        imageViewTorre.setPreserveRatio(true);

        btnOpcion3.setGraphic(imageViewTorre);
        String imagePathAlfil = baseImagen+"alfil"+color+".png";

        Image imageAlfil = new Image(imagePathAlfil);

        ImageView imageViewAlfil = new ImageView(imageAlfil);
        imageViewAlfil.setFitHeight(60);
        imageViewAlfil.setFitWidth(60);
        imageViewAlfil.setPreserveRatio(true);

        Button btnOpcion4 = new Button();
        btnOpcion4.setGraphic(imageViewAlfil);


        // Acciones de los botones
        btnOpcion1.setOnAction(e -> {
            opcionSeleccionada[0] = "caballo";
            popupStage.close();
        });
        btnOpcion2.setOnAction(e -> {
            opcionSeleccionada[0] = "reina";
            popupStage.close();
        });
        btnOpcion3.setOnAction(e -> {
            opcionSeleccionada[0] = "torre";
            popupStage.close();
        });
        btnOpcion4.setOnAction(e -> {
            opcionSeleccionada[0] = "alfil";
            popupStage.close();
        });

        // Layout
        HBox layout = new HBox(10, btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Escena
        Scene scene = new Scene(layout, 500, 200);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Espera hasta que el popup sea cerrado

        return opcionSeleccionada[0]; // Devuelve la opción seleccionada
    }
}

