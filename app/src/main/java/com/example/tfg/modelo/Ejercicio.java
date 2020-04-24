package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Ejercicio implements Parcelable {

    private int id;
    private String nombre;
    private int repeticiones;
    private int series;
    private String grupo;
    private String nDia;

    public Ejercicio() {}

    public Ejercicio(int id, String nombre, int repeticiones, int series, String grupo, String nDia) {
        this.id = id;
        this.nombre = nombre;
        this.repeticiones = repeticiones;
        this.series = series;
        this.grupo = grupo;
        this.nDia = nDia;
    }

    protected Ejercicio(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        repeticiones = in.readInt();
        series = in.readInt();
        grupo = in.readString();
        nDia = in.readString();
    }

    public static final Creator<Ejercicio> CREATOR = new Creator<Ejercicio>() {
        @Override
        public Ejercicio createFromParcel(Parcel in) {
            return new Ejercicio(in);
        }

        @Override
        public Ejercicio[] newArray(int size) {
            return new Ejercicio[size];
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

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getnDia() {
        return nDia;
    }

    public void setnDia(String nDia) {
        this.nDia = nDia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeInt(repeticiones);
        dest.writeInt(series);
        dest.writeString(grupo);
        dest.writeString(nDia);
    }
}
