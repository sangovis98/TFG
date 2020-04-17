package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable{

    private int id;
    private String nombre;
    private double proteinas;
    private double hidratos;
    private double grasas;

    public Producto() {}

    public Producto(int id, String nombre, double proteinas, double hidratos, double grasas) {
        this.id = id;
        this.nombre = nombre;
        this.proteinas = proteinas;
        this.hidratos = hidratos;
        this.grasas = grasas;
    }

    protected Producto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        proteinas = in.readDouble();
        hidratos = in.readDouble();
        grasas = in.readDouble();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
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

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getHidratos() {
        return hidratos;
    }

    public void setHidratos(double hidratos) {
        this.hidratos = hidratos;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeDouble(proteinas);
        dest.writeDouble(hidratos);
        dest.writeDouble(grasas);
    }
}