package com.example.pruebastableroajedrezfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class HelloApplication extends Application {
    private boolean turnoBlanco = true;
    private Button ultimoBotonSeleccionado = null;

    private boolean piezaSeleccionada = false;
    public Casilla casillaSeleccionada = null;
    private static final int TILE_SIZE = 80; // Tamaño de cada celda
    private static ArrayList<ArrayList> tablero = Main.getTablero();
    private GridPane gridPane; // Definir el GridPane a nivel de clase para poder manipularlo desde varios métodos.
    private  Pieza piezaMovida = null;
    private Stage stagePrincipal = null;
    private int timer1Seconds = 300; // 10 minutos en segundos para el Timer 1
    private int timer2Seconds = 300; // 10 minutos en segundos para el Timer 2
    private Timeline timer1;
    private Timeline timer2;
    private int tiempoDevuelto=0;
    private ObservableList<Pieza> piezasCapturadasBlancas = FXCollections.observableArrayList();
    private ObservableList<Pieza> piezasCapturadasNegras = FXCollections.observableArrayList();
    private FlowPane panelBlancas;
    private FlowPane panelNegras;


    private BorderPane borderPane; //
    @Override
    public void start(Stage primaryStage) {
        stagePrincipal = primaryStage; // Guardar la referencia al Stage principal
        borderPane = new BorderPane(); //
        gridPane = new GridPane(); // Inicializar el GridPane

        Button resetTablero = new Button("Reset");
        resetTablero.setPrefSize(120, 30);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(resetTablero);
        VBox timerBox = new VBox();
        Label timer1Label = new Label(formatTime(timer1Seconds));
        Label timer2Label = new Label(formatTime(timer2Seconds));
        Button toggleButton = new Button("Iniciar");

        // Timer 1
        timer1 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timer1Seconds > 0) {
                timer1Seconds--;
                timer1Label.setText(formatTime(timer1Seconds));
            }else{
                timer1.stop(); // Detener el Timer 1
                reproducirSonido("/sounds/capture.mp3");
                showAlert("Negro ha perdido"); // Mensaje en consola
                System.out.println("Negros pierden");
            }
        }));
        timer1.setCycleCount(Timeline.INDEFINITE);
        timer1Label.setStyle("-fx-font-size: 36px;");

        // Timer 2
        timer2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timer2Seconds > 0) {
                timer2Seconds--;
                timer2Label.setText(formatTime(timer2Seconds));
            }else{
                timer2.stop();
                reproducirSonido("/sounds/move-self.mp3");
                showAlert("Blanco ha perdido");
                System.out.println("Blancos pierden");
            }
        }));
        timer2.setCycleCount(Timeline.INDEFINITE);
        timer2Label.setStyle("-fx-font-size: 36px;");
        Region spacer = new Region();
        timerBox.setVgrow(spacer, Priority.ALWAYS); // Permitir que el espaciador crezca
        timerBox.getChildren().addAll(timer1Label,spacer, timer2Label);



        panelBlancas = new FlowPane(); // Para piezas negras capturadas por blancas
        panelNegras = new FlowPane();  // Para piezas blancas capturadas por negras

        panelBlancas.setPrefSize(200, 100); // ancho máximo antes de hacer wrap
        panelBlancas.setStyle("-fx-border-color: black; -fx-background-color: olive;");
        panelNegras.setPrefSize(200, 100);
        panelNegras.setStyle("-fx-border-color: black; -fx-background-color: olive;");

        panelBlancas.setHgap(4);
        panelBlancas.setVgap(4);

        panelNegras.setHgap(4);
        panelNegras.setVgap(4);
        piezasCapturadasBlancas.addListener((ListChangeListener<Pieza>) change -> {
            for (Pieza pieza : piezasCapturadasBlancas) {
                panelBlancas.getChildren().add(crearImageViewCapturada(pieza));
            }
        });

        piezasCapturadasNegras.addListener((ListChangeListener<Pieza>) change -> {
            for (Pieza pieza : piezasCapturadasNegras) {
                panelNegras.getChildren().add(crearImageViewCapturada(pieza));
            }
        });



        StackPane centrar = new StackPane(gridPane);
        timerBox.setPrefWidth(200);
        stackPane.setPrefWidth(450);
        borderPane.setCenter(centrar);
        borderPane.setLeft(stackPane); // Añadir el botón al BorderPane
        borderPane.setRight(timerBox);
        borderPane.setBottom(panelNegras);
        borderPane.setTop(panelBlancas);
        resetTablero.setOnAction(e -> onResetClick());

        crearTablero(); // Crear el tablero inicialmente

        // Crear la escena y mostrarla
        Scene scene = new Scene(borderPane, TILE_SIZE * 9, TILE_SIZE * 8);
        primaryStage.setTitle("Tablero de Ajedrez");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    public ImageView crearImageViewCapturada(Pieza pieza) {
        System.out.println("/images/iconosSmall/"+pieza.prueba()+pieza.getColor() + ".png");
        String ruta = "/images/iconosSmall/"+pieza.prueba()+pieza.getColor() + ".png";
        Image imagen = new Image(getClass().getResourceAsStream(ruta), 32, 64, false, false);
        ImageView iv = new ImageView(imagen);



        return iv;
    }




    private void onResetClick(){
        Main.borrarTodasPiezasTablero();
        Main.inicio("juegoNormal", true);
        turnoBlanco = true;
        timer2.stop();
        timer1.stop();
        timer1Seconds = 300;
        timer2Seconds = 300;
        piezasCapturadasBlancas.clear();
        piezasCapturadasNegras.clear();
        panelNegras.getChildren().clear();
        panelBlancas.getChildren().clear();
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
                    ImageView pieceImage = new ImageView(new Image(getImagePath(pieza),TILE_SIZE * 0.9, TILE_SIZE * 0.9, false, false));
                    pieceImage.setSmooth(false);
                    pieceImage.setPreserveRatio(true);
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
        System.out.println(casilla.getNombre());
        // Obtener la referencia al botón actual desde el GridPane
        int row = Herramientas.convertirAIndices(casilla.getNombre())[0];
        int col = Herramientas.convertirAIndices(casilla.getNombre())[1];

        // Buscar el StackPane que contiene el botón en esa celda
        StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
        if (stackPane != null) {
            Button button = (Button) stackPane.getChildren().get(0);

            if (ultimoBotonSeleccionado != null) {
                ultimoBotonSeleccionado.setStyle("-fx-background-color: beige;"); // O el color predeterminado
            }

            if(casilla.getColor().equals("white")) {
                button.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-background-color: beige;");
            }else{
                button.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-background-color: #8c5c42;");
            }

            // Actualizar el botón seleccionado
            ultimoBotonSeleccionado = button;
        }

        if (!piezaSeleccionada) {
            // Seleccionar una pieza
            if (casilla.getPiezaActual() != null) {
                if(casilla.getPiezaActual().getColor().equals("blanco")&&turnoBlanco) {
                    System.out.println("Pieza seleccionada: " + casilla.getPiezaActual());
                    piezaSeleccionada = true;
                    casillaSeleccionada = casilla;
                    piezaMovida = casillaSeleccionada.getPiezaActual();
                }else if (casilla.getPiezaActual().getColor().equals("negro")&&!turnoBlanco){
                    System.out.println("Pieza seleccionada: " + casilla.getPiezaActual());
                    piezaSeleccionada = true;
                    casillaSeleccionada = casilla;
//                    piezaMovida = casillaSeleccionada.getPiezaActual();
                }else {
                    System.out.println("No es el turno de este color");
                }
            } else {
                System.out.println("Casilla vacía seleccionada.");
            }

            if(casillaSeleccionada.getPiezaActual() != null) {
                Main.comprobarTodosMovimientosPieza(casillaSeleccionada.getPiezaActual());
            }
        } else {

            int[] movimiento = Herramientas.convertirAIndices(casilla.getNombre());
            System.out.println("Moviendo pieza a: " + casilla.getNombre());
            boolean movimientoRealizado = casillaSeleccionada.getPiezaActual().realizarMovimiento(movimiento);
            piezaSeleccionada = false;
            piezaMovida = casilla.getPiezaActual();
                if(piezaMovida.prueba().equals("peon")&& piezaMovida.getColor().equals("blanco") && piezaMovida.getPosicionX()[0]==0){
                    System.out.println("Peon blanco se debe transformar");
                    crearTablero();
                    Main.upgradePeon(casilla,  popUpEleccionAcension.mostrar(stagePrincipal, "blanco"));
                }else if (piezaMovida.prueba().equals("peon" )&& piezaMovida.getColor().equals("negro") && piezaMovida.getPosicionX()[0]==7){
                    System.out.println("Peon negro se debe transformar");
                    crearTablero();
                    Main.upgradePeon(casilla, popUpEleccionAcension.mostrar(stagePrincipal, "negro"));
                }
                if (piezaMovida.prueba().equals("peon" )){
                    System.out.println("Pieza es un peon");
                }
                if(piezaMovida.getColor().equals("blanco")){
                    System.out.println("Pieza es blanca");
                }
                if (piezaMovida.getPosicionX()[0]==0){
                    System.out.println("Ultima posicion");
                }

            if(movimientoRealizado && turnoBlanco){
                turnoBlanco = false;
                System.out.println("Cambia a negro");
                swapReloj();
            }else if (movimientoRealizado && !turnoBlanco){
                turnoBlanco = true;
                System.out.println("Cambia a blanco");
                swapReloj();
            }else {
                piezaSeleccionada = false;
                System.out.println("Movimiento inválido o fuera de juego");
            }
            if(Main.comprobarJaques()){
            }


            crearTablero();
        }
    }
    public static void reproducirSonido(String ruta){
        String soundFile = HelloApplication.class.getResource(ruta).toExternalForm();
        Media soundMedia = new Media(soundFile);
        MediaPlayer sound = new MediaPlayer(soundMedia);
        sound.play();
    }

    private Node getNodeByRowColumnIndex(int row, int col, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }
    private void swapReloj(){
        if (timer1.getStatus() == Timeline.Status.RUNNING) {
            timer1.pause();
            timer2.play();
            timer1Seconds+=tiempoDevuelto;
        } else if (timer2.getStatus() == Timeline.Status.RUNNING) {
            timer2.pause();
            timer1.play();
            timer2Seconds+=tiempoDevuelto;
        } else { // Empezar con el Timer 1
            timer1.play();
        }
    }
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    private String getImagePath(Pieza pieza) {
        return pieza.getImagen();
    }

    public static void main(String[] args) {
        launch(args);
    }
    static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Tipo de alerta: Información
        alert.setTitle("Timer Finalizado"); // Título de la alerta
        alert.setHeaderText(null); // Sin encabezado
        alert.setContentText(message); // Mensaje que se muestra
        alert.showAndWait(); // Mostrar y esperar a que el usuario cierre la alerta
    }
    public void capturarPieza(Pieza pieza){
        System.out.println("capturaGeneral");
        if (pieza.getColor().equals("blanco")){
            piezasCapturadasBlancas.add(pieza);
            System.out.println("capturaBlanco");
        }else{
            piezasCapturadasNegras.add(pieza);
            System.out.println("capturaNegro");
        }
    }
}
