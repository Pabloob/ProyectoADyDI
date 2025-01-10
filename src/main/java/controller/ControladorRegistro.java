package controller;

import entity.Filtro;
import services.ServicioRegistro;

public class ControladorRegistro {

    // Se crea una instancia del servicio que maneja la lógica de los registros
    private final ServicioRegistro servicio = new ServicioRegistro();

    // Método para obtener el número total de registros sin fecha de venta
    public int obtenerNumRegistros() {
        return servicio.obtenerNumRegistros();  // Llama al servicio y devuelve el número de registros sin fecha de venta
    }

    // Método para obtener el número de registros filtrados sin fecha de venta
    public int obtenerNumRegistrosFiltrados(Filtro filtro) {
        return servicio.obtenerNumRegistrosFiltrados(filtro);  // Llama al servicio con el filtro y devuelve el número de registros filtrados
    }

    // Método para insertar un nuevo registro de un vehículo y una persona
    public boolean insertarRegistro(int idPersona, int idVehiculo) {
        return servicio.insertarRegistro(idPersona, idVehiculo);  // Llama al servicio para insertar un nuevo registro
    }

    // Método para actualizar un registro (cerrar el registro de un vehículo con una persona)
    public boolean actualizarRegistro(int idPersona, int idVehiculo) {
        return servicio.cerrarRegistro(idPersona, idVehiculo);  // Llama al servicio para cerrar el registro del vehículo
    }

    // Método para obtener el nombre del antiguo propietario de un vehículo
    public String obtenerAntiguoPropietario(int idVehiculo) {
        if (servicio.obtenerAntiguoPropietario(idVehiculo) == null) {
            return null; //Si no hay antiguo propietario devuelve nulo
        } else {
            return servicio.obtenerAntiguoPropietario(idVehiculo).getNombre();  // Llama al servicio y obtiene el nombre del antiguo propietario
        }
    }

    // Método para obtener los propietarios anteriores de un vehículo
    public Object[][] obtenerPropietarios(int idVehiculo) {
        // Llama al servicio para obtener la lista de propietarios del vehículo
        Object[][] propietarios = servicio.obtenerPropietarios(idVehiculo);

        // Recorre los propietarios y, si no hay fecha de venta (es decir, es el propietario actual), se asigna el texto "Es el propietario actual"
        for (Object[] propietario : propietarios) {
            if (propietario[2] == null) {  // Si la fecha de venta es null, es el propietario actual
                propietario[2] = "Es el propietario actual";
            }
        }

        return propietarios;  // Devuelve el array con los propietarios
    }

}
