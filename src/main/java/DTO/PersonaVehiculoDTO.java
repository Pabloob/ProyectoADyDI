package DTO;

public class PersonaVehiculoDTO {

    private String nombre;
    private String DNI;
    private String matricula;
    private int anio;
    private String marca;
    private String modelo;
    private int propietarioID;
    private int vehiculoID;
    private int nVehiculos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
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

    public int getPropietarioID() {
        return propietarioID;
    }

    public void setPropietarioID(int propietarioID) {
        this.propietarioID = propietarioID;
    }

    public int getVehiculoID() {
        return vehiculoID;
    }

    public void setVehiculoID(int vehiculoID) {
        this.vehiculoID = vehiculoID;
    }

    public int getnVehiculos() {
        return nVehiculos;
    }

    public void setnVehiculos(int nVehiculos) {
        this.nVehiculos = nVehiculos;
    }

    public PersonaVehiculoDTO() {
    }
    
    public PersonaVehiculoDTO(String nombre, String DNI, String matricula, int anio, String marca, String modelo, int propietarioID, int vehiculoID,int nVehiculos) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
        this.propietarioID = propietarioID;
        this.vehiculoID = vehiculoID;
        this.nVehiculos = nVehiculos;
    }

    @Override
    public String toString() {
        return "PersonaVehiculoDTO{" + "nombre=" + nombre + ", DNI=" + DNI + ", matricula=" + matricula + ", anio=" + anio + ", marca=" + marca + ", modelo=" + modelo + ", propietarioID=" + propietarioID + ", vehiculoID=" + vehiculoID + '}';
    }


}
