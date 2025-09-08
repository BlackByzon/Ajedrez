package com.example.pruebastableroajedrezfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Pieza reyBlanco = null;
    static Pieza reyNegro = null;
    static ArrayList<Pieza> piezasNegras = new ArrayList<>();

    static ArrayList<Pieza> piezasBlancas = new ArrayList<>();
    static Scanner scn = new Scanner(System.in);
    static ArrayList <ArrayList> tablero = new ArrayList<>();
    static ArrayList <ArrayList> tablerobackup = new ArrayList<>();
    static ArrayList<String> registroPartida = new ArrayList<>();
    static ArrayList<String> registroPartidaConFormato = new ArrayList<>();
    static String baseImagen = "file:src/main/resources/images/";
    static boolean freeze = false;
    public static void main (String[]args){
        lectorCSV("juegoNormal");
    }

    public static void inicio(String nombre, boolean leerCSV) {

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
    public static void lectorCSV(String nombre){
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
    public static void crearPieza(ObjetoCSV obj){
        int[] ubicacion = Herramientas.convertirAIndices(obj.getCasilla());
        Casilla casilla = (Casilla) tablero.get(ubicacion[0]).get(ubicacion[1]);
        Pieza pieza = null;
        String link = baseImagen+obj.getNombre()+obj.getColor()+".png";
        switch (obj.getNombre()){
            case "peon":
                pieza = new Peon (obj.getColor(), true,casilla, obj.getId(), link);
                break;
            case "caballo":
                pieza = new Caballo (obj.getColor(), true,casilla, obj.getId(), link);
                break;
            case "rey":
                pieza = new Rey (obj.getColor(), true,casilla, obj.getId(), link);
                if(obj.getColor().equals("blanco")){
                    reyBlanco = pieza;
                }else{
                    reyNegro = pieza;
                }
                break;
            case "reina":
                pieza = new Reina (obj.getColor(), true,casilla, obj.getId(), link);
                break;
            case "torre":
                pieza = new Torre (obj.getColor(), true,casilla, obj.getId(), link);
                break;
            case "alfil":
                pieza = new Alfil (obj.getColor(), true,casilla, obj.getId(), link);
                break;
        }
        if (obj.getColor().equals("blanco")){
            piezasBlancas.add(pieza);
        }else{
            piezasNegras.add(pieza);
        }
        casilla.setPiezaActual(pieza);
    }

    public static ArrayList<ArrayList> getTablero() {
        return tablero;
    }
    public static void printTablero(ArrayList<ArrayList> tableroMostrado) {
        System.out.println("");
        for (ArrayList<Casilla> row : tableroMostrado) {
            for (Casilla cell : row) {
                String piece = (cell.getPiezaActual() != null) ? String.valueOf(cell.getPiezaActual().getId()) : "null";
                System.out.print(piece + " ");
            }
            System.out.println();
        }
    }
    public static void borrarTodasPiezasTablero(){
        tablero.clear();
    }

    public static void comprobarPosRey(){
        int []reyblanco = reyBlanco.getPosicionX();
        int []reynegro = reyNegro.getPosicionX();
        System.out.println("Posicion rey blanco "+reyblanco[1]+", "+reyblanco[0]);
        System.out.println("Posicion rey negro "+reynegro[1]+", "+reynegro[0]);

    }
    public static void moverPiezas(int[]entrada, int[]salida){
        boolean capture = false;
        boolean check = false;
        Casilla casillaEntrada = (Casilla) tablero.get(entrada[0]).get(entrada[1]);
        Casilla casillaSalida = (Casilla) tablero.get(salida[0]).get(salida[1]);
        if(casillaSalida.getPiezaActual()!=null){
            casillaSalida.getPiezaActual().setEnCampo(false);
            System.out.println("En campo "+casillaSalida.getPiezaActual().isEnCampo());
        }else{
            System.out.println("la casilla esta vacia");
        }
        Pieza piezaMovida=casillaEntrada.getPiezaActual();
        boolean colorBlanco = false;
        if (piezaMovida.getColor().equals("blanco")){
            colorBlanco = true;
        }
        System.out.println(piezaMovida.toString());
        casillaEntrada.setPiezaActual(null);
        Pieza piezaCapturada = null;
        if(casillaSalida.getPiezaActual()!=null){
            capture = true;
            if(casillaSalida.getPiezaActual().getColor().equals("blanco")){
                piezaCapturada = casillaSalida.getPiezaActual();
            piezasBlancas.remove(casillaSalida.getPiezaActual());
            }else{
                piezaCapturada = casillaSalida.getPiezaActual();
                piezasNegras.remove(casillaSalida.getPiezaActual());
            }
        }
        casillaSalida.setPiezaActual(piezaMovida);
        casillaSalida.getPiezaActual().setCasillaActual(casillaSalida);
        comprobarPosRey();
        if(colorBlanco){
            if(comprobacionJaque(piezasBlancas, reyNegro.getPosicionX(), false)){
                System.out.println("El Rey negro esta en jaque");
                check = true;
            }
        }else{
            if(comprobacionJaque(piezasNegras, reyBlanco.getPosicionX(), false)){
                System.out.println("El Rey blanco esta en jaque");
                check = true;
            }
        }
        if(check){
            HelloApplication.reproducirSonido("/sounds/notify.mp3");
        }else if(capture){
            HelloApplication.reproducirSonido("/sounds/capture.mp3");
        }else{
            HelloApplication.reproducirSonido("/sounds/move-self.mp3");
        }
        printTablero(tablero);
        printTablero(tablerobackup);
        System.out.println(piezasBlancas.size());
        System.out.println(piezasNegras.size());
        if(piezaCapturada!=null){
            registroPartida.add("ID:"+casillaSalida.getPiezaActual().getId()+",posicionInicial:"+casillaEntrada.getNombre()+",posicionAMover:"+casillaSalida.getNombre()+",IDPiezaCapturada:"+piezaCapturada.getId()+",null");
            registroPartidaConFormato.add(piezaALetra(casillaSalida)+"x"+casillaSalida.getNombre().toLowerCase());
            VentanaPrincipal.ajedrezApp.capturarPieza(piezaCapturada);
        }else{
            registroPartida.add("ID:"+casillaSalida.getPiezaActual().getId()+",posicionInicial:"+casillaEntrada.getNombre()+",posicionAMover:"+casillaSalida.getNombre()+",IDPiezaCapturada:null,null");
            registroPartidaConFormato.add(piezaALetra(casillaSalida)+casillaSalida.getNombre().toLowerCase());
        }
        verReyes();
    }
    public static String piezaALetra (Casilla casilla){
        switch (casilla.getPiezaActual().prueba()){
            case "rey":
                return "R";
            case "reina":
                return "Q";
            case "torre":
                return "T";
            case "alfil":
                return "A";
            case "peon":
                return "";
            case "caballo":
                return "C";
        }
        return "";
    }
    public static boolean comprobarJaques(){
        boolean res = false;
        if (comprobacionJaque(piezasNegras, reyBlanco.getPosicionX(), false) || comprobacionJaque(piezasBlancas, reyNegro.getPosicionX(), false)){
            res = true;
        }
        return res;
    }
    public static void verReyes(){
        if (!reyBlanco.isEnCampo()){
            HelloApplication.showAlert("Blancos pierden");
            System.out.println("Blancos pierden");
        }else if(!reyNegro.isEnCampo()){
            HelloApplication.showAlert("Negros pierden");
            System.out.println("Negros pierden");
        }
    }

    public static void partida (){
        System.out.println("Indica posición que quieres seleccionar");
        String eleccion = scn.nextLine().toUpperCase();
        int[] indices = Herramientas.convertirAIndices(eleccion);
        Casilla casillaSeleccionada = (Casilla) tablero.get(indices[0]).get(indices[1]);
        System.out.println("Pieza seleccionada: "+casillaSeleccionada.getPiezaActual().toString()+", casilla a realizar movimiento:");
        String eleccionB = scn.nextLine().toUpperCase();
        int[] indicesB = Herramientas.convertirAIndices(eleccionB);
        casillaSeleccionada.getPiezaActual().realizarMovimiento(indicesB);
        printTablero(tablero);
    }
    public static boolean comprobacionJaque(ArrayList<Pieza> piezas, int[] rey, boolean repetido){
        boolean jackeEncontrado=false;
        for (Pieza pieza : piezas) {
            if(pieza.comprobarMovimiento(rey)){
                jackeEncontrado=true;
                break;
            }
        }
        if(jackeEncontrado && !repetido) {
            if (reyBlanco.getPosicionX() == rey) {
                System.out.println("Entramos comprobación rey blanco");
                reyEnJaqueMate(reyBlanco, piezasNegras);
            } else {
                System.out.println("Entramos comprobación rey negro");
                reyEnJaqueMate(reyNegro, piezasBlancas);
            }
        }
        return jackeEncontrado;
    }
    public static void comprobarTodosMovimientosPieza(Pieza pieza){
        for (ArrayList<Casilla> fila : tablero) {
            for (Casilla casilla : fila) {
                if (pieza.comprobarMovimiento(Herramientas.convertirAIndices(casilla.getNombre()))){
                    System.out.println(casilla.getNombre());
                }
            }
        }
    }
    public static boolean reyEnJaqueMate(Pieza rey, ArrayList<Pieza> piezas){
        System.out.println("Rey esta en jaque pero en duda");
        boolean ret = false;
        for (ArrayList<Casilla> fila : tablero) {
            for (Casilla casilla : fila) {
                if (rey.comprobarMovimiento(Herramientas.convertirAIndices(casilla.getNombre()))){
                    if(!comprobacionJaque(piezas, Herramientas.convertirAIndices(casilla.getNombre()), true)){
                        ret = true;
                    }
                }
            }
        }
        if (ret){
            System.out.println("El rey sigue con vida");
        }else{
            System.out.println("Jaque mate");
        }
        return ret;
    }
    public static void upgradePeon(Casilla casilla, String eleccion){
        Peon peon = (Peon)casilla.getPiezaActual();
        Pieza nueva = null;
        if (peon.getColor().equals("blanco")){
            piezasBlancas.remove(peon);
            switch(eleccion){
                case "reina":
                    nueva = new Reina(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/reinaBlanco.png");
                    break;
                case "caballo":
                    nueva = new Caballo(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/caballoBlanco.png");
                    break;
                    case "alfil":
                        nueva = new Alfil(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/alfilBlanco.png");
                        break;
                        case "torre":
                            nueva = new Torre(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/torreBlanco.png");
                            break;
            }
            piezasBlancas.add(nueva);
            casilla.setPiezaActual(nueva);
        }else{
            piezasNegras.remove(peon);
            switch(eleccion){
                case "reina":
                    nueva = new Reina(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/reinaNegro.png");
                    break;
                case "caballo":
                    nueva = new Caballo(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/caballoNegro.png");
                    break;
                case "alfil":
                    nueva = new Alfil(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/alfilNegro.png");
                    break;
                case "torre":
                    nueva = new Torre(peon.getColor(), true, casilla, peon.getId(), "file:src/main/resources/images/torreNegro.png");
                    break;
            }
            piezasNegras.add(nueva);

            casilla.setPiezaActual(nueva);
        }
        System.out.println("Debería entrar ahi");
        Herramientas.escribirCSV("pruebaDeRegistro", registroPartida);
        Herramientas.escribirTxt("pruebaDeRegistroConFormato", registroPartidaConFormato);
    }
}