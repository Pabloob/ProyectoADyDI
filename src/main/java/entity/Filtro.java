package entity;

public class Filtro {

    private String nombre;
    private String marca;
    private String modelo;
    private String sexo;
    private int anio;
    private int numVehiculos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getNumVehiculos() {
        return numVehiculos;
    }

    public void setNumVehiculos(int numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    public Filtro() {
    }

    public Filtro(String nombre, String modelo, String marca, String sexo, int anio, int numVehiculos) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.marca = marca;
        this.sexo = sexo;
        this.anio = anio;
        this.numVehiculos = numVehiculos;
    }

    @Override
    public String toString() {
        return "Filtro{" + "nombre=" + nombre + ", modelo=" + modelo + ", marca=" + marca + ", sexo=" + sexo + ",anio=" + anio + ", numVehiculos=" + numVehiculos + '}';
    }
    
}
