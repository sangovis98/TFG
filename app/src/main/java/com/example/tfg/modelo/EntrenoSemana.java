package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EntrenoSemana implements Parcelable {

    private int id;
    private String nombre;
    private ArrayList<Ejercicio> ejEntrenoSemanal;

    public EntrenoSemana() {}

    public EntrenoSemana(int id, String nombre, ArrayList<Ejercicio> ejEntrenoSemanal) {
        this.id = id;
        this.nombre = nombre;
        this.ejEntrenoSemanal = ejEntrenoSemanal;
    }

    protected EntrenoSemana(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        ejEntrenoSemanal = in.createTypedArrayList(Ejercicio.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeTypedList(ejEntrenoSemanal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EntrenoSemana> CREATOR = new Creator<EntrenoSemana>() {
        @Override
        public EntrenoSemana createFromParcel(Parcel in) {
            return new EntrenoSemana(in);
        }

        @Override
        public EntrenoSemana[] newArray(int size) {
            return new EntrenoSemana[size];
        }
    };

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

    public ArrayList<Ejercicio> getEjEntrenoSemanal() {
        return ejEntrenoSemanal;
    }

    public void setEjEntrenoSemanal(ArrayList<Ejercicio> ejEntrenoSemanal) {
        this.ejEntrenoSemanal = ejEntrenoSemanal;
    }
}
