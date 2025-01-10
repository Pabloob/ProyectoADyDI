package DTO;

import entity.Persona;

public class PersonaDTO {

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

    public PersonaDTO() {
    }
    
    public PersonaDTO(Persona persona) {
        this.ID = persona.getID();
        this.nombre = persona.getNombre();
        this.DNI = persona.getDNI();
        this.sexo = persona.getSexo();
    }

    public PersonaDTO(String nombre, String DNI, String sexo) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.sexo = sexo;
    }
    public PersonaDTO(int ID, String nombre, String DNI, String sexo) {
        this.ID = ID;
        this.nombre = nombre;
        this.DNI = DNI;
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "PersonaDTO{" + "ID=" + ID + ", nombre=" + nombre + ", DNI=" + DNI + ", sexo=" + sexo + '}';
    }

}
