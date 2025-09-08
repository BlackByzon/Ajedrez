package com.example.pruebastableroajedrezfx;

import java.util.ArrayList;

public class Rey extends Pieza{
    public Rey(String color, boolean enCampo, Casilla casillaActual, int id, String imagen) {
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
        if(Math.abs(posicion[1]-posicionAMover[1])==1 && posicion[0]==posicionAMover[0] && comprobarCasillaVaciaOColorOpuesto(posicionAMover, getColor())){
            res = true;
        }else if(Math.abs(posicion[0]-posicionAMover[0])==1 && posicion[1]==posicionAMover[1] && comprobarCasillaVaciaOColorOpuesto(posicionAMover, getColor())){
            res = true;
        }else if (Math.abs(posicion[1]-posicionAMover[1])==1 && Math.abs(posicion[0]-posicionAMover[0])==1 && comprobarCasillaVaciaOColorOpuesto(posicionAMover, getColor())){
            res = true;
        }
        return res;
    }
    public String prueba(){
        return "rey";
    }
}
