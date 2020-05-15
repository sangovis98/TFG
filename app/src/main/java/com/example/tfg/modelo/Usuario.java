package com.example.tfg.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Usuario implements Parcelable {

    private String nombre;
    private String pass;
    private String correo;
    private String sexo;
    private double peso;
    private String actividad;
    private double objetivo;
    private double consumoProteinas;
    private double consumoHidratos;
    private double consumoGrasas;
    private double totalProteinas;
    private double totalHidratos;
    private double totalGrasas;
    private Date fInicio;
    private Date fFinal;
    private ArrayList<DiaDieta> diasDietas;
    private ArrayList<EntrenoSemana> entrenosSemanales;
    private String entrenoSemanaEnUso;
    private String porcentaje;
    private double totalKcal;
    private int altura;
    private int edad;
    private boolean primeraVez;

    public Usuario() {
    }

    public Usuario(String nombre, String pass, String correo, String sexo, double peso, String actividad, double objetivo, double consumoProteinas, double consumoHidratos, double consumoGrasas, double totalProteinas, double totalHidratos, double totalGrasas, Date fInicio, Date fFinal, ArrayList<DiaDieta> diasDietas, ArrayList<EntrenoSemana> entrenosSemanales, String entrenoSemanaEnUso, String porcentaje, double totalKcal, int altura, int edad, boolean primeraVez) {
        this.nombre = nombre;
        this.pass = pass;
        this.correo = correo;
        this.sexo = sexo;
        this.peso = peso;
        this.actividad = actividad;
        this.objetivo = objetivo;
        this.consumoProteinas = consumoProteinas;
        this.consumoHidratos = consumoHidratos;
        this.consumoGrasas = consumoGrasas;
        this.totalProteinas = totalProteinas;
        this.totalHidratos = totalHidratos;
        this.totalGrasas = totalGrasas;
        this.fInicio = fInicio;
        this.fFinal = fFinal;
        this.diasDietas = diasDietas;
        this.entrenosSemanales = entrenosSemanales;
        this.entrenoSemanaEnUso = entrenoSemanaEnUso;
        this.porcentaje = porcentaje;
        this.totalKcal = totalKcal;
        this.altura = altura;
        this.edad = edad;
        this.primeraVez = primeraVez;
    }

    protected Usuario(Parcel in) {
        nombre = in.readString();
        pass = in.readString();
        correo = in.readString();
        sexo = in.readString();
        peso = in.readDouble();
        actividad = in.readString();
        objetivo = in.readDouble();
        consumoProteinas = in.readDouble();
        consumoHidratos = in.readDouble();
        consumoGrasas = in.readDouble();
        totalProteinas = in.readDouble();
        totalHidratos = in.readDouble();
        totalGrasas = in.readDouble();
        diasDietas = in.createTypedArrayList(DiaDieta.CREATOR);
        entrenosSemanales = in.createTypedArrayList(EntrenoSemana.CREATOR);
        entrenoSemanaEnUso = in.readString();
        porcentaje = in.readString();
        totalKcal = in.readDouble();
        altura = in.readInt();
        edad = in.readInt();
        primeraVez = in.readByte() != 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public double getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(double objetivo) {
        this.objetivo = objetivo;
    }

    public double getConsumoProteinas() {
        return consumoProteinas;
    }

    public void setConsumoProteinas(double consumoProteinas) {
        this.consumoProteinas = consumoProteinas;
    }

    public double getConsumoHidratos() {
        return consumoHidratos;
    }

    public void setConsumoHidratos(double consumoHidratos) {
        this.consumoHidratos = consumoHidratos;
    }

    public double getConsumoGrasas() {
        return consumoGrasas;
    }

    public void setConsumoGrasas(double consumoGrasas) {
        this.consumoGrasas = consumoGrasas;
    }

    public double getTotalProteinas() {
        return totalProteinas;
    }

    public void setTotalProteinas(double totalProteinas) {
        this.totalProteinas = totalProteinas;
    }

    public double getTotalHidratos() {
        return totalHidratos;
    }

    public void setTotalHidratos(double totalHidratos) {
        this.totalHidratos = totalHidratos;
    }

    public double getTotalGrasas() {
        return totalGrasas;
    }

    public void setTotalGrasas(double totalGrasas) {
        this.totalGrasas = totalGrasas;
    }

    public Date getfInicio() {
        return fInicio;
    }

    public void setfInicio(Date fInicio) {
        this.fInicio = fInicio;
    }

    public Date getfFinal() {
        return fFinal;
    }

    public void setfFinal(Date fFinal) {
        this.fFinal = fFinal;
    }

    public ArrayList<DiaDieta> getDiasDietas() {
        return diasDietas;
    }

    public void setDiasDietas(ArrayList<DiaDieta> diasDietas) {
        this.diasDietas = diasDietas;
    }

    public ArrayList<EntrenoSemana> getEntrenosSemanales() {
        return entrenosSemanales;
    }

    public void setEntrenosSemanales(ArrayList<EntrenoSemana> entrenosSemanales) {
        this.entrenosSemanales = entrenosSemanales;
    }

    public String getEntrenoSemanaEnUso() {
        return entrenoSemanaEnUso;
    }

    public void setEntrenoSemanaEnUso(String entrenoSemanaEnUso) {
        this.entrenoSemanaEnUso = entrenoSemanaEnUso;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(double totalKcal) {
        this.totalKcal = totalKcal;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(pass);
        dest.writeString(correo);
        dest.writeString(sexo);
        dest.writeDouble(peso);
        dest.writeString(actividad);
        dest.writeDouble(objetivo);
        dest.writeDouble(consumoProteinas);
        dest.writeDouble(consumoHidratos);
        dest.writeDouble(consumoGrasas);
        dest.writeDouble(totalProteinas);
        dest.writeDouble(totalHidratos);
        dest.writeDouble(totalGrasas);
        dest.writeTypedList(diasDietas);
        dest.writeTypedList(entrenosSemanales);
        dest.writeString(entrenoSemanaEnUso);
        dest.writeString(porcentaje);
        dest.writeDouble(totalKcal);
        dest.writeInt(altura);
        dest.writeInt(edad);
        dest.writeByte((byte) (primeraVez ? 1 : 0));
    }
}
