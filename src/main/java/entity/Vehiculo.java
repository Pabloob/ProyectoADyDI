package entity;

public class Vehiculo {

    private int ID;
    private String matricula;
    private int anio;
    private String marca;
    private String modelo;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Vehiculo() {
    }

    public Vehiculo(String matricula, int anio, String marca, String modelo) {
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
    }

    public Vehiculo(int ID, String matricula, int anio, String marca, String modelo) {
        this.ID = ID;
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "ID=" + ID + ", matricula=" + matricula + ", anio=" + anio + ", marca=" + marca + ", modelo=" + modelo + '}';
    }

}
