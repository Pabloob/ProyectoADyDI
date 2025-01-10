package Services;

import DAO.VehiculoDAO;
import DTO.VehiculoDTO;
import entity.Vehiculo;
import java.util.ArrayList;
import java.util.List;

/*
Clase que comunica la tabla Vehiculo con las operaciones relacionadas que se realizan en la vista
 */
public class ServicioVehiculoDTO {

    // Se crea el DAO de Vehiculo para interactuar con la base de datos
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    // Método que devuelve todas las marcas distintas de vehículos
    // Llama al DAO para obtener las marcas y las devuelve como un array de String
    public String[] obtenerMarcas() {
        List<String> aux = vehiculoDAO.obtenerMarcas();  // Obtiene las marcas desde el DAO
        if (aux == null) {  // Si no hay marcas, devuelve null
            return null;
        }
        return aux.toArray(String[]::new);  // Convierte la lista de marcas en un array y lo devuelve
    }

    // Método que devuelve todos los modelos distintos de vehículos
    // Llama al DAO para obtener los modelos y los devuelve como un array de String
    public String[] obtenerModelos() {
        List<String> aux = vehiculoDAO.obtenerModelos();  // Obtiene los modelos desde el DAO
        if (aux == null) {  // Si no hay modelos, devuelve null
            return null;
        }
        return aux.toArray(String[]::new);  // Convierte la lista de modelos en un array y lo devuelve
    }

    // Método que transforma los vehículos en VehiculoDTO y devuelve todos los vehículos
    // Llama al DAO para obtener todos los vehículos, los convierte a DTOs y los devuelve en una lista
    public List<VehiculoDTO> obtenerTodo() {
        List<VehiculoDTO> vehiculosDTO = new ArrayList<>();  // Lista para almacenar los DTOs de los vehículos
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerTodo();  // Obtiene todos los vehículos desde el DAO
        if (vehiculos == null) {  // Si no hay vehículos, devuelve null
            return null;
        }
        for (Vehiculo vehiculo : vehiculos) {  // Itera sobre todos los vehículos
            // Convierte cada vehículo en un VehiculoDTO y lo añade a la lista
            VehiculoDTO vehiculoDTO = new VehiculoDTO(vehiculo.getID(), vehiculo.getMatricula(), vehiculo.getAnio(), vehiculo.getMarca(), vehiculo.getModelo());
            vehiculosDTO.add(vehiculoDTO);  // Añade el DTO a la lista
        }
        return vehiculosDTO;  // Devuelve la lista de VehiculoDTOs
    }

    // Método que transforma los vehículos en VehiculoDTO y devuelve los vehículos que no tienen propietario
    // Llama al DAO para obtener los vehículos sin propietario (sin registro)
    public List<VehiculoDTO> obtenerSinPropietario() {
        VehiculoDAO v = new VehiculoDAO();  // Crea una nueva instancia del DAO de Vehículo
        List<VehiculoDTO> vehiculosDTO = new ArrayList<>();  // Lista para almacenar los DTOs de los vehículos sin propietario
        List<Vehiculo> vehiculos = v.obtenerSinRegistro();  // Obtiene los vehículos sin propietario desde el DAO
        if (vehiculos == null) {  // Si no hay vehículos sin propietario, devuelve null
            return null;
        }
        for (Vehiculo vehiculo : vehiculos) {  // Itera sobre los vehículos sin propietario
            // Convierte cada vehículo en un VehiculoDTO y lo añade a la lista
            VehiculoDTO vehiculoDTO = new VehiculoDTO(vehiculo.getID(), vehiculo.getMatricula(), vehiculo.getAnio(), vehiculo.getMarca(), vehiculo.getModelo());
            vehiculosDTO.add(vehiculoDTO);  // Añade el DTO a la lista
        }
        return vehiculosDTO;  // Devuelve la lista de VehiculoDTOs sin propietario
    }

    // Método que inserta un vehículo a través de un VehiculoDTO
    // Crea un nuevo Vehículo a partir del DTO y lo inserta en la base de datos
    public boolean insertar(VehiculoDTO v) {
        VehiculoDAO vehiculo = new VehiculoDAO();  // Crea una nueva instancia del DAO de Vehículo
        return vehiculo.insertar(new Vehiculo(v.getMatricula(), v.getAnio(), v.getMarca(), v.getModelo()));  // Inserta el vehículo en la base de datos
    }
    
    //Metodo para comprobar si la matricula existe ya
    public boolean matriculaExiste(String matricula) {
        return vehiculoDAO.matriculaExiste(matricula); //Devuelve si la matricula existe
    }

}
