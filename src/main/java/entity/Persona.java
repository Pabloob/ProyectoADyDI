package entity;

public class Persona {

    private int ID;
    private String nombre;
    private String DNI;
    private String sexo;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Persona() {
    }

    public Persona(String nombre, String DNI, String sexo) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.sexo = sexo;
    }
    public Persona(int ID, String nombre, String DNI, String sexo) {
        this.ID = ID;
        this.nombre = nombre;
        this.DNI = DNI;
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Persona{" + "ID=" + ID + ", nombre=" + nombre + ", DNI=" + DNI + ", sexo=" + sexo + '}';
    }

}
