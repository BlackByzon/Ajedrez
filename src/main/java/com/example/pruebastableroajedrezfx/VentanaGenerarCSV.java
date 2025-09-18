package com.example.pruebastableroajedrezfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaGenerarCSV extends Application {

    private Button ultimoBotonSeleccionado = null;

    public Casilla casillaSeleccionada = null;
    private static final int TILE_SIZE = 80; // Tamaño de cada celda
    private static ArrayList<ArrayList> tablero = Main.getTablero();
    private GridPane gridPane; // Definir el GridPane a nivel de clase para poder manipularlo desde varios métodos.
    private  Pieza piezaMovida = null;
    private Stage stagePrincipal = null;

    private BorderPane borderPane; //
    private String piezaSeleccionada = "peon";
    private String color = "blanco";
    private boolean borrar = false;
    private String baseImagen = "file:src/main/resources/images/";
    private int id= 0;
    private boolean reyBlancoEnCampo = false;
    private boolean reyNegroEnCampo = false;
    private ImageView imageView = null;

    public static void inicio() {

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
    }
    @Override
    public void start(Stage primaryStage) {
        tablero.clear();
        id = 0;
        inicio();

        stagePrincipal = primaryStage; // Guardar la referencia al Stage principal
        borderPane = new BorderPane(); //
        gridPane = new GridPane(); // Inicializar el GridPane

        Button resetTablero = new Button("Reset");
        resetTablero.setPrefSize(120, 30);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(resetTablero);
        VBox buttonContainer = new VBox(10); // Espaciado de 10 píxeles entre botones

        // Crear los 7 botones con listeners
        for (int i = 1; i <= 9; i++) {
            String nombre= null;
            switch (i){
                case 1:
                    nombre= "Peón (A)";
                    break;
                case 2:
                    nombre= "Alfil (S)";
                    break;
                case 3:
                    nombre= "Torre (D)";
                    break;
                case 4:
                    nombre= "Caballo (F)";
                    break;
                case 5:
                    nombre= "Rey (G)";
                    break;
                case 6:
                    nombre= "Reina (H)";
                    break;
                case 7:
                    nombre= "Borrar (X)";
                    break;
                case 8:
                    nombre= "Blanco (B)";
                    break;
                case 9:
                    nombre= "Negro (N)";
                    break;

            }
            Button button = new Button(nombre);
            int buttonNumber = i; // Variable para usar dentro del lambda

            // Agregar un listener (EventHandler) a cada botón
            button.setOnAction(event -> {
                switch (buttonNumber){
                    case 1:
                        piezaSeleccionada = "peon";
                        borrar = false;
                        break;
                    case 2:
                        piezaSeleccionada = "alfil";
                        borrar = false;
                        break;
                    case 3:
                        piezaSeleccionada = "torre";
                        borrar = false;
                        break;
                    case 4:
                        piezaSeleccionada = "caballo";
                        borrar = false;
                        break;
                    case 5:
                        piezaSeleccionada = "rey";
                        borrar = false;
                        break;
                    case 6:
                        piezaSeleccionada = "reina";
                        borrar = false;
                        break;
                    case 7:
                        borrar = true;
                        break;
                    case 8:
                        color = "blanco";
                        break;
                    case 9:
                        color = "negro";
                        break;
                    case 10:
                        Herramientas.generarCSV("prueba", tablero);
                        break;
                }
                refreshImage();
            });


            // Añadir el botón al contenedor
            buttonContainer.getChildren().add(button);
        }
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS); // El espaciador crece para llenar el espacio disponible
        buttonContainer.getChildren().add(spacer);
        try {
            // Cargar la imagen desde los recursos
            Image image = new Image((baseImagen + piezaSeleccionada + color + ".png"));
            imageView = new ImageView(image);

            // Ajustar las dimensiones de la imagen (opcional)
            imageView.setFitWidth(100); // Ajustar el ancho
            imageView.setPreserveRatio(true); // Mantener la relación de aspecto

            // Añadir la imagen al final del VBox
            buttonContainer.getChildren().add(imageView);

        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        // Agregar el VBox con los botones a la región izquierda del BorderPane
        borderPane.setLeft(buttonContainer);


        borderPane.setCenter(gridPane);
        borderPane.setLeft(buttonContainer);
        resetTablero.setOnAction(e -> onResetClick());
        borderPane.setMargin(buttonContainer, new Insets(20, 20, 20, 10));

        crearTablero(); // Crear el tablero inicialmente

        HBox panelTop = new HBox(10);
        panelTop.setAlignment(Pos.CENTER);

        Button CSV = new Button("CSV");
        TextField nombreArchivo = new TextField();
        CSV.setOnAction(e -> llamarCSV(nombreArchivo.getText()));

        panelTop.getChildren().addAll(CSV, nombreArchivo);

        borderPane.setTop(panelTop);
        BorderPane.setMargin(panelTop, new Insets(5, 5, 5, 5));
        BorderPane.setAlignment(panelTop, Pos.CENTER);


        Scene scene = new Scene(borderPane, TILE_SIZE * 10, TILE_SIZE * 9);
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.N) {
                color = "negro";
            }
            if (event.getCode() == KeyCode.B) {
                color = "blanco";
            }
            if (event.getCode() == KeyCode.X) {
                borrar = true;
            }
            if (event.getCode() == KeyCode.A) {
                piezaSeleccionada = "peon";
                borrar = false;
            }
            if (event.getCode() == KeyCode.S) {
                piezaSeleccionada = "alfil";
                borrar = false;
            }
            if (event.getCode() == KeyCode.D) {
                piezaSeleccionada = "torre";
                borrar = false;
            }
            if (event.getCode() == KeyCode.F) {
                piezaSeleccionada = "caballo";
                borrar = false;
            }
            if (event.getCode() == KeyCode.G) {
                piezaSeleccionada = "rey";
                borrar = false;
            }
            if (event.getCode() == KeyCode.H) {
                piezaSeleccionada = "reina";
                borrar = false;
            }
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
            refreshImage();
        });
        primaryStage.setTitle("Tablero de Ajedrez");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void llamarCSV (String nombre){
        if(reyBlancoEnCampo&&reyNegroEnCampo){
            Herramientas.generarCSV(nombre, tablero);
            Herramientas.subirArchivos();
        }else{
            System.out.println("Pon los 2 reyes parguela");
        }
    }
    private void refreshImage() {
        try {
            // Cargar una nueva imagen desde los recursos
            Image image = new Image((baseImagen + piezaSeleccionada + color + ".png"));

            // Verificar si la imagen se cargó correctamente
            if (image.isError()) {
                System.err.println("Error al cargar la nueva imagen.");
                return;
            }

            // Establecer la nueva imagen en el ImageView
            imageView.setImage(image);

        } catch (Exception e) {
            System.err.println("Error al refrescar la imagen: " + e.getMessage());
        }
    }


    private void onResetClick(){
        Main.borrarTodasPiezasTablero();
        Main.inicio("null", false);
        crearTablero();
    }

    private void crearTablero() {
        gridPane.getChildren().clear(); // Limpiar las celdas previas del tablero

        // Volver a agregar las casillas y piezas al tablero
        for (int row = 0; row < tablero.size(); row++) {
            for (int col = 0; col < tablero.get(row).size(); col++) {
                Casilla casilla = (Casilla) tablero.get(row).get(col);

                // Crear un botón para la casilla
                Button button = new Button();
                button.setPrefSize(TILE_SIZE, TILE_SIZE);

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
                    pieceImage.setFitWidth(TILE_SIZE * 0.9);
                    pieceImage.setFitHeight(TILE_SIZE * 0.9);
                    stackPane.getChildren().add(pieceImage);
                }

                // Añadir funcionalidad al botón
                button.setOnAction(e -> onCellClicked(casilla));
                stackPane.setOnMouseClicked(e -> onCellClicked(casilla));

                // Añadir la celda al GridPane
                gridPane.add(stackPane, col, row);
            }
        }
    }
    private void onCellClicked(Casilla casilla) {
        if(!borrar) {
            Pieza piezaCreada = null;
            if (casilla.getPiezaActual()!=null){
                System.out.println("Casilla ocupada");
            }else {
                switch (piezaSeleccionada) {
                    case "peon":
                        piezaCreada = new Peon(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                        break;
                    case "torre":
                        piezaCreada = new Torre(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                        break;
                    case "reina":
                        piezaCreada = new Reina(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                        break;
                    case "rey":
                        boolean eleccion= false;
                        if(color.equals("blanco")){
                            eleccion = reyBlancoEnCampo;
                        }else{
                            eleccion = reyNegroEnCampo;
                        }
                        if(!eleccion) {
                            piezaCreada = new Rey(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                            if(color.equals("blanco")){
                                reyBlancoEnCampo = true;
                            }else{
                                reyNegroEnCampo = true;
                            }
                        }else{
                            System.out.println("Solo puedes tener 1 rey en campo por cada color");
                        }
                        break;
                    case "alfil":
                        piezaCreada = new Alfil(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                        break;
                    case "caballo":
                        piezaCreada = new Caballo(color, true, casilla, id, baseImagen + piezaSeleccionada + color + ".png");
                        break;
                }
                casilla.setPiezaActual(piezaCreada);
                id += 1;
                System.out.println(piezaCreada.transformarCSV());
            }
        }else{
            if(casilla.getPiezaActual()==null){
                System.out.println("Casilla vacía");
            }
            if (casilla.getPiezaActual().prueba().equals("rey")){
                if(casilla.getPiezaActual().getColor().equals("blanco")){
                    reyBlancoEnCampo=false;
                    System.out.println("estado del rey blanco: "+reyBlancoEnCampo);
                }else{
                    reyNegroEnCampo=false;
                    System.out.println("estado del rey negro: "+reyNegroEnCampo);
                }
            }
            casilla.setPiezaActual(null);
        }
        crearTablero();
    }

    private Node getNodeByRowColumnIndex(int row, int col, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }
    private String getImagePath(Pieza pieza) {
        return pieza.getImagen();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
