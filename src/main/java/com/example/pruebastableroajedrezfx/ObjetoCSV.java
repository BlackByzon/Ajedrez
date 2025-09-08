package com.example.pruebastableroajedrezfx;

public class ObjetoCSV {
    int id;
    String nombre;
    String casilla;
    String color;

    public ObjetoCSV(int id, String nombre, String casilla, String color) {
        this.id = id;
        this.nombre = nombre;
        this.casilla = casilla;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCasilla() {
        return casilla;
    }

    public void setCasilla(String casilla) {
        this.casilla = casilla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
