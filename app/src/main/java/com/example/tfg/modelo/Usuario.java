package com.example.tfg.modelo;

import java.util.ArrayList;

public class Usuario {
    private String nombre;
    private String pass;
    private String correo;
    ArrayList<DiaDieta> dietas;

    public Usuario(String nombre, String pass, String correo, ArrayList<DiaDieta> dietas) {
        this.nombre = nombre;
        this.pass = pass;
        this.correo = correo;
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

    public ArrayList<DiaDieta> getDietas() {
        return dietas;
    }

    public void setDietas(ArrayList<DiaDieta> dietas) {
        this.dietas = dietas;
    }
}
