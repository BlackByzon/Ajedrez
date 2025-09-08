package com.example.pruebastableroajedrezfx;

import java.util.ArrayList;

public class Reina extends Pieza{
    public Reina(String color, boolean enCampo, Casilla casillaActual, int id, String imagen) {
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
        int[] getposicionX= getPosicionX();
        if(posicionAMover[0]==getposicionX[0] && comprobarCasillasVacias(posicionAMover)) {
            if (posicionAMover[1] > -1 && posicionAMover[1] < 8) {
                res = true;
            }
        }else if(posicionAMover[1]==getposicionX[1] && comprobarCasillasVacias(posicionAMover)) {
            if (posicionAMover[0] > -1 && posicionAMover[0] < 8) {
                res = true;
            }
        }else if(Math.abs(posicionAMover[0]-getposicionX[0])==Math.abs(posicionAMover[1]-getposicionX[1]) && comprobarCasillasVacias(posicionAMover)) {
            if (posicionAMover[1] > -1 && posicionAMover[1] < 8 &&posicionAMover[0] > -1 && posicionAMover[0] <8) {
                res = true;
            }
        }
        return res;
    }
    public String prueba(){
        return "reina";
    }
}

