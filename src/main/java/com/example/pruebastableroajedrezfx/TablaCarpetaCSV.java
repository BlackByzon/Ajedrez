package com.example.pruebastableroajedrezfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TablaCarpetaCSV extends Application {
    GridPane gridPane = null;
    ArrayList <ArrayList> tablero = new ArrayList<>();
    String baseImagen = "file:src/main/resources/images/";
    String archivoSeleccionado = null;

    @Override
    public void start(Stage primaryStage) {
        // Crear el contenedor BorderPane
        BorderPane borderPane = new BorderPane();

        // Crear el ListView para mostrar los archivos
        ListView<String> fileListView = new ListView<>();

        // Ruta de carpeta proporcionada (puedes cambiarla aquí)
        String folderPath = "src/main/resources/startFiles/"; // Cambiar a la ruta que desees

        // Crear el archivo de la ruta proporcionada
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            // Obtener archivos de la carpeta proporcionada
            File[] files = folder.listFiles();
            if (files != null) {
                // Añadir solo los archivos (no carpetas) a la ListView
                Arrays.stream(files)
                        .filter(File::isFile)  // Solo mostrar archivos
                        .map(File::getName)    // Obtener solo el nombre de cada archivo
                        .forEach(fileListView.getItems()::add);  // Añadir a la lista
            }
        } else {
            // Mostrar un error si la carpeta no existe
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Carpeta no encontrada");
            alert.setContentText("La carpeta especificada no existe o no es un directorio.");
            alert.showAndWait();
        }

        // Hacer que los archivos sean clickeables
        fileListView.setOnMouseClicked(event -> {
            String selectedFile = fileListView.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                tablero.clear();
                System.out.println(selectedFile);
                String sinExtension = selectedFile.replace(".csv", "");
                archivoSeleccionado = sinExtension;
                inicio(sinExtension, true);
                crearTablero();
            }
        });
        gridPane = new GridPane();
        borderPane.setCenter(gridPane);

        // Colocar el ListView en la parte izquierda del BorderPane
        borderPane.setLeft(fileListView);

        // Crear una escena y añadirla al stage
        Scene scene = new Scene(borderPane, 800, 500); // Tamaño de la ventana ajustable

        Button boton = new Button ("Empezar");
        boton.setOnAction(actionEvent -> abrirVentanaAjedrez(primaryStage));
        borderPane.setBottom(boton);
        primaryStage.setTitle("Visualizador de Archivos");
        primaryStage.setScene(scene);
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                VentanaPrincipal App = new VentanaPrincipal();
                try {
                    Main.inicio("null", false);
                    App.start(new Stage());
                    primaryStage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.show();
    }
    private void abrirVentanaAjedrez(Stage primaryStage) {
        HelloApplication ajedrezApp = new HelloApplication();
        try {
            Main.inicio(archivoSeleccionado, true);
            ajedrezApp.start(new Stage()); // Crear y mostrar la ventana secundaria (HelloApplication)
            primaryStage.close(); // Cerrar la ventana principal
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void crearTablero() {
        gridPane.getChildren().clear(); // Limpiar las celdas previas del tablero

        // Volver a agregar las casillas y piezas al tablero
        for (int row = 0; row < tablero.size(); row++) {
            for (int col = 0; col < tablero.get(row).size(); col++) {
                Casilla casilla = (Casilla) tablero.get(row).get(col);

                // Crear un botón para la casilla
                Button button = new Button();
                button.setPrefSize(60, 60);

                // Estilo del botón basado en el color de la casilla
                if (casilla.getColor().equals("white")) {
                    button.setStyle("-fx-background-color: beige;");
                } else {
                    button.setStyle("-fx-background-color: #8c5c42;");
                }

                // Crear un StackPane para superponer botón e imagen
                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(button);

                // Si hay una pieza en la casilla, añadir su imagen
                if (casilla.getPiezaActual() != null) {
                    Pieza pieza = casilla.getPiezaActual();
                    ImageView pieceImage = new ImageView(new Image(getImagePath(pieza)));
                    pieceImage.setSmooth(false);
                    pieceImage.setPreserveRatio(true);
                    pieceImage.setFitWidth(60 * 0.9);
                    pieceImage.setFitHeight(60 * 0.9);
                    stackPane.getChildren().add(pieceImage);
                }
                // Añadir funcionalidad al botón
                // Añadir la celda al GridPane
                gridPane.add(stackPane, col, row);
            }
        }
    }
    private String getImagePath(Pieza pieza) {
        return pieza.getImagen();
    }
    public void inicio(String nombre, boolean leerCSV) {

        for(int i = 0; i<8;i++){
            tablero.add(new ArrayList<Casilla>());
        }
        for (int i = 7; i >=0; i--) {
            boolean white;
            if(i%2==0) {
                white = true;
            }else{
                white = false;
            }

            for (int j = 0; j < 8; j++) {
                String letra = null;
                switch (j) {
                    case 0:
                        letra = "A";
                        break;
                    case 1:
                        letra = "B";
                        break;
                    case 2:
                        letra = "C";
                        break;
                    case 3:
                        letra = "D";
                        break;
                    case 4:
                        letra = "E";
                        break;
                    case 5:
                        letra = "F";
                        break;
                    case 6:
                        letra = "G";
                        break;
                    case 7:
                        letra = "H";
                        break;
                }
                String color = null;
                if (white) {
                    color = "white";
                    white = false;
                } else {
                    color = "black";
                    white=true;
                }
                tablero.get(7-i).add(new Casilla(letra + (i+1), color, (i*8+j)));
            }
        }
        if (leerCSV) {
            lectorCSV(nombre);
        }
    }
    public void lectorCSV(String nombre){
        String filePath = "src/main/resources/startFiles/"+nombre+".csv";
        List<ObjetoCSV> objetos = new ArrayList<>();

        // Leer el archivo CSV y crear objetos
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Leer la primera línea (encabezado) y no hacer nada con ella
            if ((line = br.readLine()) != null) {
                // Ignorar la primera línea (encabezado)
                System.out.println("Encabezado: " + line);
            }

            // Leer el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Separar la línea en valores (por coma)
                String[] values = line.split(",");

                // Verificar que la línea tenga la cantidad esperada de valores (4 en este caso)
                if (values.length == 4) {
                    try {
                        int id = Integer.parseInt(values[0].trim());  // Convertir el ID a int
                        String name = values[1].trim();              // El nombre como String
                        String casilla = values[2].trim();             // La casilla como String
                        String color = values[3].trim();               // El color como String

                        // Crear y añadir el objeto a la lista
                        objetos.add(new ObjetoCSV(id, name, casilla, color));
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir el ID en la línea: " + line);
                    }
                } else {
                    System.err.println("Línea mal formada: " + line);
                }
            }

            // Mostrar los objetos para verificar que se leyeron correctamente
            for (ObjetoCSV obj : objetos) {
                crearPieza(obj);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
    public void crearPieza(ObjetoCSV obj) {
        int[] ubicacion = Herramientas.convertirAIndices(obj.getCasilla());
        Casilla casilla = (Casilla) tablero.get(ubicacion[0]).get(ubicacion[1]);
        Pieza pieza = null;
        String link = baseImagen + obj.getNombre() + obj.getColor() + ".png";
        switch (obj.getNombre()) {
            case "peon":
                pieza = new Peon(obj.getColor(), true, casilla, obj.getId(), link);
                break;
            case "caballo":
                pieza = new Caballo(obj.getColor(), true, casilla, obj.getId(), link);
                break;
            case "rey":
                pieza = new Rey(obj.getColor(), true, casilla, obj.getId(), link);
                if (obj.getColor().equals("blanco")) {

                } else {
                }
                break;
            case "reina":
                pieza = new Reina(obj.getColor(), true, casilla, obj.getId(), link);
                break;
            case "torre":
                pieza = new Torre(obj.getColor(), true, casilla, obj.getId(), link);
                break;
            case "alfil":
                pieza = new Alfil(obj.getColor(), true, casilla, obj.getId(), link);
                break;
        }
        casilla.setPiezaActual(pieza);
    }
    public static void main(String[] args) {
        launch(args);
    }
}