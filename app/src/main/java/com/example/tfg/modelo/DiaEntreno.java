package com.example.tfg.modelo;

import java.util.ArrayList;

public class DiaEntreno {
    private String nombre;
    private int gastoCalorico;

    public DiaEntreno(String nombre, int gastoCalorico, ArrayList<Ejercicio> pectorales, ArrayList<Ejercicio> espalda, ArrayList<Ejercicio> piernas, ArrayList<Ejercicio> hombros, ArrayList<Ejercicio> brazos, ArrayList<Ejercicio> pantorrillas) {
        this.nombre = nombre;
        this.gastoCalorico = gastoCalorico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGastoCalorico() {
        return gastoCalorico;
    }

    public void setGastoCalorico(int gastoCalorico) {
        this.gastoCalorico = gastoCalorico;
    }
}
