package com.example.pruebastableroajedrezfx;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Herramientas {
    public static void revisarCSVdeBDD() throws Exception{
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT nombre, archivo FROM comienzoscustom";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                InputStream archivo = rs.getBinaryStream("archivo");

                if (archivo != null) {
                    String rutaSalida = "src/main/resources/startFiles/" + nombre;
                    File file = new File(rutaSalida);

                    if (file.exists()) {
                        System.out.println("Ya existe: " + rutaSalida + " → no se hace nada.");
                    } else {
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = archivo.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }
                        System.out.println("Archivo exportado: " + rutaSalida);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void subirArchivos(){
        String url = System.getenv("DB_URL");
        String usuario = System.getenv("DB_USER");
        String clave = System.getenv("DB_PASS");

        String rutaCarpeta = "src/main/resources/startFiles/"; // Cambia por tu carpeta
        File carpeta = new File(rutaCarpeta);
        File[] archivos = carpeta.listFiles();

        if (archivos == null || archivos.length == 0) {
            System.out.println("No hay archivos en la carpeta.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, usuario, clave)) {

            // Preparar statements
            String verificarSQL = "SELECT COUNT(*) FROM comienzoscustom WHERE nombre = ?";
            PreparedStatement verificarStmt = conn.prepareStatement(verificarSQL);

            String insertarSQL = "INSERT INTO comienzoscustom (nombre, archivo) VALUES (?, ?)";
            PreparedStatement insertarStmt = conn.prepareStatement(insertarSQL);

            for (File archivo : archivos) {
                if (archivo.isFile()) {

                    // Verificar si ya existe
                    verificarStmt.setString(1, archivo.getName());
                    ResultSet rs = verificarStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);

                    if (count == 0) {
                        // Insertar archivo
                        try (FileInputStream fis = new FileInputStream(archivo)) {
                            insertarStmt.setString(1, archivo.getName());
                            insertarStmt.setBlob(2, fis);
                            insertarStmt.executeUpdate();
                            System.out.println("Subido: " + archivo.getName());
                        }
                    } else {
                        System.out.println("Ya existe: " + archivo.getName());
                    }
                }
            }

            System.out.println("✅ Proceso finalizado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
