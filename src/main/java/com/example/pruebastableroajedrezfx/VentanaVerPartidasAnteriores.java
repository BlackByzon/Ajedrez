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
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class VentanaVerPartidasAnteriores extends Application {




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
    private String url = "file:src/main/resources/gameRegisters/";
    private int id= 0;
    private boolean reyBlancoEnCampo = false;
    private boolean reyNegroEnCampo = false;
    private ImageView imageView = null;
    private HashMap<Integer,RegistrosMovimientos> registrosMovimientos = new HashMap<>();
    private ArrayList<Pieza> piezas = new ArrayList<>();




    public void inicio(String archivo) {
        url +=archivo;

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
        readArchivo(url);
    }
    public void realizarAccion (RegistrosMovimientos reg){

    }
    public void revertirAccion (RegistrosMovimientos reg){

    }
    public void readArchivo(String filePath){

        // Leer el archivo CSV y crear objetos
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Leer la primera línea (encabezado) y no hacer nada con ella
            if ((line = br.readLine()) != null) {
                // Ignorar la primera línea (encabezado)
                System.out.println("Encabezado: " + line);
            }

            // Leer el archivo línea por línea
            int id = 0;
            while ((line = br.readLine()) != null) {
                // Separar la línea en valores (por coma)
                String[] values = line.split(",");

                // Verificar que la línea tenga la cantidad esperada de valores (4 en este caso)
                Pieza piezaAgregada = null;
                Pieza piezaBorrada = null;
                if (values.length == 4) {
                    try {
                        String idPiezaPrincipal = values[0].trim();              // El nombre como String
                        String casillaComienzo = values[1].trim();             // La casilla como String
                        String casillaFin = values[2].trim();               // El color como String
                        String idPiezaEliminada = values[3].trim();               // El color como String
                        String transformacionPeon = values[4].trim();               // El color como String
                        for (Pieza p : piezas) {
                            if (p.getId() == Integer.parseInt(idPiezaPrincipal)) {
                                piezaAgregada= p;
                                break;
                            }
                        }
                        for (Pieza p : piezas) {
                            if(!idPiezaEliminada.equals("null")) {
                                if (p.getId() == Integer.parseInt(idPiezaPrincipal)) {
                                    piezaBorrada = p;
                                    break;
                                }
                            }
                        }

                        // Crear y añadir el objeto a la lista
                        registrosMovimientos.put(id, new RegistrosMovimientos(piezaAgregada, casillaComienzo, casillaFin, piezaBorrada, transformacionPeon));
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir el ID en la línea: " + line);
                    }
                } else {
                    System.err.println("Línea mal formada: " + line);
                }
                id +=1;
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
    @Override
    public void start(Stage primaryStage) {
        tablero.clear();
        id = 0;
        inicio("pruebaDeRegistro.csv");

        stagePrincipal = primaryStage; // Guardar la referencia al Stage principal
        borderPane = new BorderPane(); //
        gridPane = new GridPane(); // Inicializar el GridPane

        Button avanzar = new Button("Avanzar");
        Button retroceder = new Button("Retroceder");

        HBox buttonContainer = new HBox(10); // Espaciado de 10 píxeles entre botones
        buttonContainer.getChildren().addAll(retroceder, avanzar);

        // Agregar el VBox con los botones a la región izquierda del BorderPane
        borderPane.setLeft(buttonContainer);

        borderPane.setCenter(gridPane);
        borderPane.setBottom(buttonContainer);
        borderPane.setMargin(buttonContainer, new Insets(20, 20, 20, 10));

        crearTablero(); // Crear el tablero inicialmente

        Scene scene = new Scene(borderPane, TILE_SIZE * 10, TILE_SIZE * 9);
        primaryStage.setTitle("Tablero de Ajedrez");
        primaryStage.setScene(scene);
        primaryStage.show();
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

                gridPane.add(stackPane, col, row);
            }
        }
    }


    private Node getNodeByRowColumnIndex(int row, int col, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
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
    private String getImagePath(Pieza pieza) {
        return pieza.getImagen();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
