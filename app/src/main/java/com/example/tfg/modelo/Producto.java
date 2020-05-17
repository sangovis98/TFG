package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable{

    private int id;
    private String nombre;
    private double proteinas100;
    private double hidratos100;
    private double grasas100;
    private String img;
    private double gramos;

    public Producto() {}

    public Producto(int id, String nombre, double proteinas100, double hidratos100, double grasas100, String img, double gramos) {
        this.id = id;
        this.nombre = nombre;
        this.proteinas100 = proteinas100;
        this.hidratos100 = hidratos100;
        this.grasas100 = grasas100;
        this.img = img;
        this.gramos = gramos;
    }

    protected Producto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        proteinas100 = in.readDouble();
        hidratos100 = in.readDouble();
        grasas100 = in.readDouble();
        img = in.readString();
        gramos = in.readDouble();
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

    public double getProteinas100() {
        return proteinas100;
    }

    public void setProteinas100(double proteinas100) {
        this.proteinas100 = proteinas100;
    }

    public double getHidratos100() {
        return hidratos100;
    }

    public void setHidratos100(double hidratos100) {
        this.hidratos100 = hidratos100;
    }

    public double getGrasas100() {
        return grasas100;
    }

    public void setGrasas100(double grasas100) {
        this.grasas100 = grasas100;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getGramos() {
        return gramos;
    }

    public void setGramos(double gramos) {
        this.gramos = gramos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeDouble(proteinas100);
        dest.writeDouble(hidratos100);
        dest.writeDouble(grasas100);
        dest.writeString(img);
        dest.writeDouble(gramos);
    }
}