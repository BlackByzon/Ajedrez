package com.example.pruebastableroajedrezfx;

import javafx.scene.media.Media;

import java.util.ArrayList;

public class Peon extends Pieza {
    private boolean casillaInicial;

    public Peon( String color, boolean enCampo, Casilla casillaActual, int id, String imagen) {
        super(color, enCampo, casillaActual, id, imagen);
        this.casillaInicial = true;
    }

    @Override
    public boolean realizarMovimiento(int[] posicionAMover) {

        boolean res = false;
        if (comprobarMovimiento(posicionAMover)) {
            casillaInicial= false;
            Main.moverPiezas(getPosicionX(), posicionAMover);
            res = true;
        }
        return res;
    }
    public String dimeQueSoy(){
        int[] pos = getPosicionX();
        return "Hola, soy el peon "+id+" en la posicion "+pos[0]+", "+pos[1]+", color "+color;
    }

    public boolean comprobarMovimiento(int[] eleccionPosicion) {
        int[] getposicionXX= getPosicionX();
        boolean resDevuelto = false;
        int multiplicador = 0;
        if (getColor().equals("blanco")) {
            multiplicador = -1;
        } else {
            multiplicador = 1;
        }
        // Movimiento vertical de una casilla
        if ( eleccionPosicion[0] == getPosicionX()[0] + (1 * multiplicador) && eleccionPosicion[1] == getPosicionX()[1] && comprobarCasillaVacia(eleccionPosicion)) {
            resDevuelto = true;
        } else if (casillaInicial && getPosicionX()[0] + (2 * multiplicador) == eleccionPosicion[0] && getPosicionX()[1] == eleccionPosicion[1] && comprobarCasillaVacia(eleccionPosicion)) {
            resDevuelto = true;}
        if (getPosicionX()[0] + (1 * multiplicador) == eleccionPosicion[0] && (getPosicionX()[1] + 1 == eleccionPosicion[1] || getPosicionX()[1] - 1 == eleccionPosicion[1])) {
            resDevuelto = comprobarCasillaOcupadaPorColorOpuesto(eleccionPosicion, getColor());
        }

        return resDevuelto;
    }

    // Método de prueba
    @Override
    public String prueba() {
        return "peon";
    }
    @Override
    public String toString(){
        return "Peon, color: "+color+", Id: "+id;
    }

}
