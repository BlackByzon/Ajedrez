package com.example.pruebastableroajedrezfx;

import java.util.ArrayList;

public class Pieza {
    ArrayList<ArrayList>tablero = Main.getTablero();
    String color;
    boolean enCampo;
    Casilla casillaActual;
    int id;
    String imagen;

    public Pieza(String color, boolean enCampo, Casilla casillaActual, int id, String imagen) {
        this.color = color;
        this.enCampo = enCampo;
        this.casillaActual = casillaActual;
        this.id = id;
        this.imagen = imagen;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEnCampo() {
        return enCampo;
    }

    public void setEnCampo(boolean enCampo) {
        this.enCampo = enCampo;
    }

    public Casilla getCasillaActual() {
        return casillaActual;
    }

    public void setCasillaActual(Casilla casillaActual) {
        this.casillaActual = casillaActual;
    }

    public int[] getPosicionX() {
        String cas = casillaActual.getNombre();
        char letraColumna = cas.charAt(0);
        int numeroFila = Character.getNumericValue(cas.charAt(1));
        int columna = letraColumna - 'A';
        int fila = 8 - numeroFila;
        return new int[]{fila, columna};
    }

    public boolean comprobarCasillaVacia(int[] posicion) {
        boolean respuesta = false;
        Casilla casilla = (Casilla) tablero.get(posicion[0]).get(posicion[1]);
        if (casilla.getPiezaActual() == null) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean comprobarCasillasVacias(int[] posicionFinal) {
        boolean respuesta = true;
        int[] posActual = getPosicionX();
        int movimientoHorizontal = posicionFinal[1] - posActual[1];
        int multiplicadorHorizontal = (movimientoHorizontal > 0) ? 1 : -1;
        movimientoHorizontal = Math.abs(movimientoHorizontal);
        int movimientoVertical = posicionFinal[0] - posActual[0];
        int multiplicadorVertical = (movimientoVertical > 0) ? 1 : -1;
        movimientoVertical = Math.abs(movimientoVertical);

        if (movimientoHorizontal != 0 && movimientoVertical != 0) {
            for (int i = 1; i <=movimientoHorizontal-1; i++) {
                Casilla casillaAMirar = (Casilla) tablero.get(posActual[0] + (i * multiplicadorVertical)).get(posActual[1] + (i * multiplicadorHorizontal));
                if (casillaAMirar.getPiezaActual() != null) {
                    respuesta = false;
                    break;
                }
            }
        } else if (movimientoHorizontal == 0) {
            for (int i = 1; i <= movimientoVertical-1; i++) {
                Casilla casillaAMirar = (Casilla) tablero.get(posActual[0] + (i * multiplicadorVertical)).get(posActual[1]);
                if (casillaAMirar.getPiezaActual() != null) {
                    respuesta = false;
                    break;
                }
            }
        } else {
            for (int i = 1; i <=movimientoHorizontal-1; i++) {
                Casilla casillaAMirar = (Casilla) tablero.get(posActual[0]).get(posActual[1] + (i * multiplicadorHorizontal));
                if (casillaAMirar.getPiezaActual() != null) {
                    respuesta = false;
                    break;
                }
            }
        }
        if(!comprobarCasillaVaciaOColorOpuesto(posicionFinal, getColor())){
            respuesta = false;
        }
        return respuesta;
    }

    public boolean comprobarCasillaOcupadaPorColorOpuesto(int[] posicion, String color) {
        boolean respuesta = false;
        Casilla casilla = (Casilla) tablero.get(posicion[0]).get(posicion[1]);
        String colorOtraPieza = (casilla.getPiezaActual() != null) ? casilla.getPiezaActual().getColor() : "gris";
        if (casilla.getPiezaActual() != null && !color.equals(colorOtraPieza)) {
            respuesta = true;
        }
        return respuesta;
    }
    public boolean comprobarCasillaVaciaOColorOpuesto(int[] posicion, String color) {
        boolean respuesta = false;
        Casilla casilla = (Casilla) tablero.get(posicion[0]).get(posicion[1]);
        String colorOtraPieza = (casilla.getPiezaActual() != null) ? casilla.getPiezaActual().getColor() : "gris";
        if (casilla.getPiezaActual() == null) {
            respuesta = true;
        }else if(!color.equals(colorOtraPieza)){
            respuesta=true;
        }
        return respuesta;
    }
    public boolean realizarMovimiento(int[] posicionAMover) {
        System.out.println("Este metodo es para que los demás puedan usarlo");
        return false;
    }

    public String prueba(){
        return "hola, soy todo";
    }

    @Override
    public String toString() {
        return "Color:" + color + ", id:" + id;
    }
    public boolean comprobarMovimiento(int[] posicionAMover) {
        boolean res = false;
        System.out.println("Este es el método base para que se activen los de las piezas");
        return res;
    }
    public String transformarCSV (){
        return id+","+prueba()+","+getCasillaActual().getNombre()+","+color;
    }

}
