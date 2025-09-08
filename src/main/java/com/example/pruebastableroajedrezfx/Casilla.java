package com.example.pruebastableroajedrezfx;

public class Casilla {
    String nombre;
    Pieza piezaActual;
    String color;
    int id;

    public Casilla(String nombre, String color, int id) {
        this.nombre = nombre;
        this.piezaActual = null;
        this.color = color;
        this.id = id;
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

    public Pieza getPiezaActual() {
        return piezaActual;
    }

    public void setPiezaActual(Pieza piezaActual) {
        this.piezaActual = piezaActual;
    }
    public Casilla copia() {
        return new Casilla(nombre, color, id);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Casilla{" +
                "nombre='" + nombre + '\'' +
                ", piezaActual=" + piezaActual.toString() +
                ", color='" + color + '\'' +
                ", id=" + id +
                '}';
    }
}
