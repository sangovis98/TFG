package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DiaDieta implements Parcelable {

    private int id;
    private String nDia;
    private int calorias;
    private double proteinas;
    private double hidratos;
    private double grasas;
    private ArrayList<Producto> productos;

    public DiaDieta() {}

    public DiaDieta(int id, String nDia, int calorias, double proteinas, double hidratos, double grasas, ArrayList<Producto> productos) {
        this.id = id;
        this.nDia = nDia;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.hidratos = hidratos;
        this.grasas = grasas;
        this.productos = productos;
    }

    protected DiaDieta(Parcel in) {
        id = in.readInt();
        nDia = in.readString();
        calorias = in.readInt();
        proteinas = in.readDouble();
        hidratos = in.readDouble();
        grasas = in.readDouble();
        productos = in.createTypedArrayList(Producto.CREATOR);
    }

    public static final Creator<DiaDieta> CREATOR = new Creator<DiaDieta>() {
        @Override
        public DiaDieta createFromParcel(Parcel in) {
            return new DiaDieta(in);
        }

        @Override
        public DiaDieta[] newArray(int size) {
            return new DiaDieta[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getnDia() {
        return nDia;
    }

    public void setnDia(String nDia) {
        this.nDia = nDia;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
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

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nDia);
        dest.writeInt(calorias);
        dest.writeDouble(proteinas);
        dest.writeDouble(hidratos);
        dest.writeDouble(grasas);
        dest.writeTypedList(productos);
    }
}
