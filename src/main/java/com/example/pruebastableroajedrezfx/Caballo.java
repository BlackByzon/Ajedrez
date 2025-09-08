package com.example.pruebastableroajedrezfx;

import java.util.ArrayList;

public class Caballo extends Pieza{

    public Caballo(String color, boolean enCampo, Casilla casillaActual, int id, String imagen) {
        super(color, enCampo, casillaActual, id, imagen);
    }

    public boolean realizarMovimiento(int[] posicionAMover) {
        boolean res = false;
        if (comprobarMovimiento(posicionAMover)) {
            Main.moverPiezas(getPosicionX(), posicionAMover);
            res = true;
        }
        return res;
    }

    public boolean comprobarMovimiento(int[] posicionAMover) {
        boolean res = false;
        int[]posicion = getPosicionX();
        if(Math.abs(posicion[1]-posicionAMover[1])==2 &&Math.abs(posicion[0]-posicionAMover[0])==1 && comprobarCasillaVaciaOColorOpuesto(posicionAMover, getColor())){
            res = true;
        }else if(Math.abs(posicion[0]-posicionAMover[0])==2 &&Math.abs(posicion[1]-posicionAMover[1])==1 && comprobarCasillaVaciaOColorOpuesto(posicionAMover, getColor())){
            res = true;
        }
        return res;
    }

    @Override
    public String prueba() {
        return "caballo";
    }
}

