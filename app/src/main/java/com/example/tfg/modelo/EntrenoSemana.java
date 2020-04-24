package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EntrenoSemana implements Parcelable {

    private int id;
    private String nombre;
    private int gastoCalorico;
    private ArrayList<Ejercicio> ejEntrenoSemanal;

    public EntrenoSemana() {}

    public EntrenoSemana(int id, String nombre, int gastoCalorico, ArrayList<Ejercicio> ejEntrenoSemanal) {
        this.id = id;
        this.nombre = nombre;
        this.gastoCalorico = gastoCalorico;
        this.ejEntrenoSemanal = ejEntrenoSemanal;
    }

    protected EntrenoSemana(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        gastoCalorico = in.readInt();
        ejEntrenoSemanal = in.createTypedArrayList(Ejercicio.CREATOR);
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

    public int getGastoCalorico() {
        return gastoCalorico;
    }

    public void setGastoCalorico(int gastoCalorico) {
        this.gastoCalorico = gastoCalorico;
    }

    public ArrayList<Ejercicio> getEjEntrenoSemanal() {
        return ejEntrenoSemanal;
    }

    public void setEjEntrenoSemanal(ArrayList<Ejercicio> ejEntrenoSemanal) {
        this.ejEntrenoSemanal = ejEntrenoSemanal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeInt(gastoCalorico);
        dest.writeTypedList(ejEntrenoSemanal);
    }
}
