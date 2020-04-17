package com.example.tfg.modelo;

import java.util.ArrayList;

public class Usuario {
    private String nombre;
    private String pass;
    private String correo;
    ArrayList<DiaEntreno> entrenos;
    ArrayList<DiaDieta> dietas;

    public Usuario(String nombre, String pass, String correo, ArrayList<DiaEntreno> entrenos, ArrayList<DiaDieta> dietas) {
        this.nombre = nombre;
        this.pass = pass;
        this.correo = correo;
        this.entrenos = entrenos;
        this.dietas = dietas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ArrayList<DiaEntreno> getEntrenos() {
        return entrenos;
    }

    public void setEntrenos(ArrayList<DiaEntreno> entrenos) {
        this.entrenos = entrenos;
    }

    public ArrayList<DiaDieta> getDietas() {
        return dietas;
    }

    public void setDietas(ArrayList<DiaDieta> dietas) {
        this.dietas = dietas;
    }
}
