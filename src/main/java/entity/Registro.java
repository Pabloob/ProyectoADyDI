package entity;

public class Registro {

    private int ID;
    private String FechaAdquisicion;
    private String FechaVenta;
    private int vehiculoID;
    private int personaID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFechaAdquisicion() {
        return FechaAdquisicion;
    }

    public void setFechaAdquisicion(String FechaAdquisicion) {
        this.FechaAdquisicion = FechaAdquisicion;
    }

    public String getFechaVenta() {
        return FechaVenta;
    }

    public void setFechaVenta(String FechaVenta) {
        this.FechaVenta = FechaVenta;
    }

    public int getVehiculoID() {
        return vehiculoID;
    }

    public void setVehiculoID(int vehiculoID) {
        this.vehiculoID = vehiculoID;
    }

    public int getPersonaID() {
        return personaID;
    }

    public void setPersonaID(int personaID) {
        this.personaID = personaID;
    }

    public Registro() {
    }

    public Registro(String FechaAdquisicion, String FechaVenta, int vehiculoID, int personaID) {
        this.FechaAdquisicion = FechaAdquisicion;
        this.FechaVenta = FechaVenta;
        this.vehiculoID = vehiculoID;
        this.personaID = personaID;
    }

    public Registro(int ID, String FechaAdquisicion, String FechaVenta, int vehiculoID, int personaID) {
        this.ID = ID;
        this.FechaAdquisicion = FechaAdquisicion;
        this.FechaVenta = FechaVenta;
        this.vehiculoID = vehiculoID;
        this.personaID = personaID;
    }

    @Override
    public String toString() {
        return "Registro{" + "ID=" + ID + ", FechaAdquisicion=" + FechaAdquisicion + ", FechaVenta=" + FechaVenta + ", vehiculoID=" + vehiculoID + ", personaID=" + personaID + '}';
    }

}
