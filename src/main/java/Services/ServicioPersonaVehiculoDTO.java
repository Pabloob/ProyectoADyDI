package Services;

import DAO.PersonaDAO;
import DAO.RegistroDAO;
import DAO.VehiculoDAO;
import DTO.PersonaVehiculoDTO;
import entity.Filtro;
import entity.Persona;
import entity.Registro;
import entity.Vehiculo;
import java.util.ArrayList;
import java.util.List;

/*
Clase que conecta la tabla registro con los datos que se mostrarán en la ventana principal
Se obtienen las personas y vehículos a través de los IDs correspondientes en el registro
 */
public class ServicioPersonaVehiculoDTO {

    // Se crean los DAOs necesarios para acceder a las tablas Persona, Vehiculo y Registro
    private final PersonaDAO personaDAO = new PersonaDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final RegistroDAO registroDAO = new RegistroDAO();

    // Método que llama al método obtenerPaginado de RegistroDAO y lo transforma en PersonaVehiculoDTO con los datos correspondientes
    public List<PersonaVehiculoDTO> obtener(int pagina) {

        List<PersonaVehiculoDTO> datosTabla = new ArrayList<>();  // Lista donde se almacenarán los objetos PersonaVehiculoDTO
        List<Registro> registros = registroDAO.obtenerPaginado(pagina);  // Obtiene los registros de la tabla Registro según la página
        if (registros == null) {  // Si no hay registros, devuelve null
            return null;
        }
        for (Registro registro : registros) {  // Itera sobre cada registro
            Persona persona = personaDAO.obtenerPorID(registro.getPersonaID());  // Obtiene la persona correspondiente al ID
            Vehiculo vehiculo = vehiculoDAO.obtenerPorID(registro.getVehiculoID());  // Obtiene el vehículo correspondiente al ID

            // Crea un objeto PersonaVehiculoDTO con los datos obtenidos y lo mete en la lista
            datosTabla.add(new PersonaVehiculoDTO(
                    persona.getNombre(),
                    persona.getDNI(),
                    vehiculo.getMatricula(),
                    vehiculo.getAnio(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    persona.getID(),
                    vehiculo.getID(),
                    registroDAO.obtenerNumPersonasPorID(registro.getVehiculoID())  // Obtiene el número de personas asociadas al vehículo
            ));
        }

        return datosTabla;  // Devuelve la lista de PersonaVehiculoDTO
    }

    /*
    Método que llama al método obtenerFiltrado de RegistroDAO pasándole los filtros y la página,
    y lo transforma en PersonaVehiculoDTO con los datos correspondientes
     */
    public List<PersonaVehiculoDTO> obtenerFiltrado(Filtro filtro, int pagina) {
        if (filtro == null) {  // Si no hay filtros, devuelve null
            return null;
        }
        List<PersonaVehiculoDTO> datosTabla = new ArrayList<>();  // Lista donde se almacenarán los objetos PersonaVehiculoDTO
        List<Registro> registros = registroDAO.obtenerFiltrado(pagina, filtro);  // Obtiene los registros filtrados según la página y los filtros
        if (registros == null) {  // Si no hay registros, devuelve null
            return null;
        }
        for (Registro registro : registros) {  // Itera sobre cada registro filtrado
            Persona persona = personaDAO.obtenerPorID(registro.getPersonaID());  // Obtiene la persona correspondiente al ID
            Vehiculo vehiculo = vehiculoDAO.obtenerPorID(registro.getVehiculoID());  // Obtiene el vehículo correspondiente al ID

            // Crea un objeto PersonaVehiculoDTO con los datos obtenidos y lo mete en la lista
            datosTabla.add(new PersonaVehiculoDTO(
                    persona.getNombre(),
                    persona.getDNI(),
                    vehiculo.getMatricula(),
                    vehiculo.getAnio(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    persona.getID(),
                    vehiculo.getID(),
                    registroDAO.obtenerNumPersonasPorID(registro.getVehiculoID())  // Obtiene el número de personas asociadas al vehículo
            ));
        }

        return datosTabla;  // Devuelve la lista de PersonaVehiculoDTO
    }
}
        