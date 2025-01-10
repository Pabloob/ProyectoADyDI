package services;

import DAO.PersonaDAO;
import DAO.RegistroDAO;
import DTO.PersonaDTO;
import entity.Filtro;
import entity.Persona;
import entity.Registro;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Clase que comunica la tabla Registro con las operaciones relacionadas que se harán en la vista
public class ServicioRegistro {

    private Registro registro;  // Instancia de la clase Registro

    // Se crean los DAOs necesarios para acceder a las tablas Registro y Persona
    private final RegistroDAO registroDAO = new RegistroDAO();
    private final PersonaDAO personaDAO = new PersonaDAO();

    // Se define el formato de fecha para las operaciones de registro
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String fechaFormateada = sdf.format(new Date());  // Fecha actual formateada en "yyyy-MM-dd"

    // Método que llama a insertar con el nuevo registro
    // Inserta un registro con la fecha actual, sin fecha de venta, y los IDs de la persona y el vehículo
    public boolean insertarRegistro(int idPersona, int idVehiculo) {
        registro = new Registro(fechaFormateada, null, idVehiculo, idPersona);
        return registroDAO.insertar(registro);  // Llama al DAO para insertar el registro
    }

    // Método que llama a cerrar registro con el nuevo registro
    // Cierra un registro con la fecha actual y los IDs de la persona y el vehículo
    public boolean cerrarRegistro(int idPersona, int idVehiculo) {
        registro = new Registro(fechaFormateada, null, idVehiculo, idPersona);
        return registroDAO.cerrarRegistro(registro);  // Llama al DAO para cerrar el registro
    }

    // Método que devuelve el número total de registros sin fecha de venta
    public int obtenerNumRegistros() {
        return registroDAO.obtenerNumRegistros();  // Llama al DAO para obtener el número total de registros
    }

    // Método que devuelve el número de registros filtrados sin fecha de venta
    public int obtenerNumRegistrosFiltrados(Filtro filtro) {
        return registroDAO.obtenerNumRegistrosFiltrados(filtro);  // Llama al DAO para obtener los registros filtrados
    }

    // Método para obtener el anterior propietario de un vehículo
    // Devuelve los datos del propietario anterior de un vehículo dado su ID
    public PersonaDTO obtenerAntiguoPropietario(int idVehiculo) {
        registro = registroDAO.obtenerPorID(idVehiculo);  // Obtiene el registro del vehículo
        if (registro == null) {  // Si no se encuentra el registro, devuelve null
            return null;
        }
        Persona p = personaDAO.obtenerPorID(registro.getPersonaID());  // Obtiene la persona asociada al registro
        return new PersonaDTO(p.getNombre(), p.getDNI(), p.getSexo());  // Devuelve los datos del propietario anterior en un DTO
    }

    // Método para devolver los datos necesarios de los anteriores propietarios
    // Devuelve un array de objetos con los datos de los anteriores propietarios de un vehículo
    public Object[][] obtenerPropietarios(int idVehiculo) {
        List<String[]> aux = new ArrayList<>();  // Lista temporal para almacenar los datos de los propietarios
        if (registroDAO.obtenerPropietarios(idVehiculo) == null) {  // Si no hay propietarios, devuelve null
            return null;
        }
        for (Registro registroVehiculo : registroDAO.obtenerPropietarios(idVehiculo)) {  // Itera sobre los registros de propietarios
            Persona propietario = personaDAO.obtenerPorID(registroVehiculo.getPersonaID());  // Obtiene la persona asociada al registro
            aux.add(new String[]{propietario.getNombre(), registroVehiculo.getFechaAdquisicion(), registroVehiculo.getFechaVenta()});  // Añade los datos a la lista temporal
        }

        Object[][] propietarios = new Object[aux.size()][3];  // Crea un array para los propietarios
        for (int j = 0; j < aux.size(); j++) {  // Itera sobre la lista para rellenar el array
            propietarios[j][0] = aux.get(j)[0];  // Nombre del propietario
            propietarios[j][1] = aux.get(j)[1];  // Fecha de adquisición
            propietarios[j][2] = aux.get(j)[2];  // Fecha de venta
        }

        return propietarios;  // Devuelve el array con los datos de los propietarios
    }
}
