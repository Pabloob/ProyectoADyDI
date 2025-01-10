package controller;

import DTO.VehiculoDTO;
import Services.ServicioVehiculoDTO;
import java.util.List;

public class ControladorVehiculo {

    // Instancia del servicio que maneja la lógica de negocio para los vehículos
    private final ServicioVehiculoDTO servicio = new ServicioVehiculoDTO();

    // Método para obtener todas las marcas de los vehículos
    public String[] obtenerMarcas() {
        String[] marcasServicio = servicio.obtenerMarcas();  // Obtiene las marcas del servicio
        String[] marcas = new String[marcasServicio.length + 1];  // Crea un array con una posición extra para "Todos"

        marcas[0] = "Todos";  // Asigna "Todos" a la primera posición
        System.arraycopy(marcasServicio, 0, marcas, 1, marcasServicio.length);  // Copia el resto de las marcas en las posiciones siguientes

        return marcas;  // Devuelve el array con "Todos" como primer elemento
    }

    // Método para obtener todos los modelos de los vehículos
    public String[] obtenerModelos() {
        String[] modelosServicio = servicio.obtenerModelos();  // Obtiene los modelos del servicio
        String[] modelos = new String[modelosServicio.length + 1];  // Crea un array con una posición extra para "Todos"

        modelos[0] = "Todos";  // Asigna "Todos" a la primera posición
        System.arraycopy(modelosServicio, 0, modelos, 1, modelosServicio.length);  // Copia el resto de los modelos en las posiciones siguientes
        return modelos;  // Devuelve el array con "Todos" como primer elemento
    }

    // Método que obtiene todos los vehículos y los convierte a un formato adecuado para mostrar en la vista
    public Object[][] obtenerTodo() {
        List<VehiculoDTO> vehiculos = servicio.obtenerTodo();  // Obtiene la lista de vehículos desde el servicio
        return listaAArray(vehiculos);  // Convierte la lista de vehículos en un array de objetos para la vista
    }

    // Método que obtiene los vehículos que no tienen propietario y los convierte a un formato adecuado para mostrar
    public Object[][] obtenerSinPropietario() {
        List<VehiculoDTO> vehiculos = servicio.obtenerSinPropietario();  // Obtiene los vehículos sin propietario desde el servicio
        return listaAArray(vehiculos);  // Convierte la lista de vehículos en un array de objetos para la vista
    }

    // Método para insertar un vehículo en la base de datos a partir de los datos proporcionados
    public boolean insertar(Object[] datos) {
        // Variables que almacenarán los datos del vehículo
        String matricula = null;
        String añoStr = null;
        int año = 0;
        String marca = null;
        String modelo = null;

        try {
            // Se obtienen los datos del vehículo desde el array de entrada
            matricula = (String) datos[0];  // Obtiene la matrícula
            añoStr = (String) datos[1];      // Obtiene el año en formato String
            año = Integer.parseInt(añoStr);  // Convierte el año a un entero
            marca = (String) datos[2];       // Obtiene la marca
            modelo = (String) datos[3];      // Obtiene el modelo
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Llama al servicio para insertar el vehículo y devuelve el resultado
        return servicio.insertar(new VehiculoDTO(matricula, año, marca, modelo));
    }

    // Método auxiliar que convierte una lista de VehiculoDTO a un array de objetos
    private Object[][] listaAArray(List<VehiculoDTO> vehiculos) {
        if (vehiculos == null) {
            return null;  // Si la lista de vehículos es nula, retorna null
        }

        // Crea un array con el tamaño de la lista de vehículos
        Object[][] datos = new Object[vehiculos.size()][5];

        // Recorre la lista de vehículos y convierte cada VehiculoDTO en una fila del array
        for (int i = 0; i < vehiculos.size(); i++) {
            // Asigna los valores del vehículo a cada columna correspondiente
            datos[i][0] = vehiculos.get(i).getMatricula(); //Matricula del vehiculo
            datos[i][1] = vehiculos.get(i).getAnio();       //Año del vehiculo
            datos[i][2] = vehiculos.get(i).getMarca();     //Marca del vehiculo
            datos[i][3] = vehiculos.get(i).getModelo();    //Modelo del vehiculo
            datos[i][4] = vehiculos.get(i).getID();        // ID del vehículo
        }

        // Devuelve el array con los datos de los vehículos
        return datos;
    }

    public boolean matriculaExiste(String matricula) {
        return servicio.matriculaExiste(matricula);
    }
}
