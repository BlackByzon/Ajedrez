package com.example.pruebastableroajedrezfx;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Herramientas {
    public static void generarCSV (String nombre, ArrayList<ArrayList>tablero){

        String filePath = "src/main/resources/startFiles/"+nombre+".csv";

        // Contenido del archivo
        String content = "id, nombre, casilla, color";
        for (ArrayList<Casilla> row : tablero) {
            for (Casilla cell : row) {
                if(cell.getPiezaActual()!=null){
                    content+="\r\n"+cell.getPiezaActual().transformarCSV();
                }
            }
            System.out.println();
        }
        generateTxtFile(filePath, content);
    }
    public static void escribirCSV (String nombre, ArrayList<String> statements){
        System.out.println("Entro");

        String filePath = "src/main/resources/gameRegisters/"+nombre+".csv";

        // Contenido del archivo
        String content = "id, nombre, casilla, color";
        for (String linea : statements) {
            content+="\r\n"+linea;
        }
        generateTxtFile(filePath, content);
    }
    public static void escribirTxt (String nombre, ArrayList<String> statements){
        System.out.println("Entro");

        String filePath = "src/main/resources/gameRegisters/"+nombre+".txt";

        // Contenido del archivo
        String content = "";
        for (String linea : statements) {
            content+="\r\n"+linea;
        }
        generateTxtFile(filePath, content);
    }

    public static void generateTxtFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir el contenido en el archivo
            writer.write(content);
            System.out.println("Archivo .csv generado correctamente en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al generar el archivo .csv: " + e.getMessage());
        }
    }
    public static int[] convertirAIndices(String posicion) {
        // Convierte la columna (letra) en un índice de columna (0-7)
        char columna = posicion.charAt(0);
        int columnaIndice = columna - 'A';  // 'A' se convierte a 0, 'B' a 1, ..., 'H' a 7

        // Convierte la fila (número) en un índice de fila (0-7)
        int fila = Character.getNumericValue(posicion.charAt(1));
        int filaIndice = 8 - fila;  // Fila "1" se convierte a 7, "8" se convierte a 0

        return new int[] { filaIndice, columnaIndice };
    }
}
