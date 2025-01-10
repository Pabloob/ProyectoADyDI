package controller;

import DTO.PersonaVehiculoDTO;
import Services.ServicioPersonaVehiculoDTO;
import entity.Filtro;
import java.util.List;

public class ControladorPersonaVehiculo {
    // Se crea una instancia del servicio que maneja la lógica de los datos de persona-vehículo
    private final ServicioPersonaVehiculoDTO servicio = new ServicioPersonaVehiculoDTO();

    // Método para obtener los datos de persona-vehículo en base al número de página
    public Object[][] obtenerDatos(int numeroPagina) {
        List<PersonaVehiculoDTO> listaDatosTabla = servicio.obtener(numeroPagina);  // Llama al servicio para obtener los datos
        return listaAArray(listaDatosTabla);  // Convierte la lista de DTO a un array
    }

    // Método para obtener los datos filtrados de persona-vehículo en base al filtro y número de página
    public Object[][] obtenerDatosFiltrados(Filtro filtro, int numeroPagina) {
        List<PersonaVehiculoDTO> listaDatosTabla = servicio.obtenerFiltrado(filtro, numeroPagina);  // Llama al servicio con el filtro
        return listaAArray(listaDatosTabla);  // Convierte la lista de DTO filtrados a un array
    }

    // Método privado que convierte una lista de PersonaVehiculoDTO en un array
    private Object[][] listaAArray(List<PersonaVehiculoDTO> listaDatosTabla) {
        if (listaDatosTabla == null) {
            return null;  // Si la lista es nula, se retorna null
        }
        Object[][] datos = new Object[listaDatosTabla.size()][8];  // Crea un array de tamaño adecuado

        for (int i = 0; i < listaDatosTabla.size(); i++) {
            PersonaVehiculoDTO aux = listaDatosTabla.get(i);  // Se obtiene el DTO de la lista
            datos[i][0] = aux.getNombre();  // Asigna el nombre de la persona
            datos[i][1] = aux.getDNI();  // Asigna el DNI de la persona
            datos[i][2] = aux.getMatricula();  // Asigna la matrícula del vehículo
            datos[i][3] = aux.getAnio();  // Asigna el año del vehículo
            datos[i][4] = aux.getMarca();  // Asigna la marca del vehículo
            datos[i][5] = aux.getModelo();  // Asigna el modelo del vehículo
            datos[i][6] = aux.getnVehiculos();  // Asigna el número de vehículos que tiene la persona
            datos[i][7] = aux.getVehiculoID();  // Asigna el ID del vehículo
        }
        return datos;  // Retorna el array con los datos
    }
}