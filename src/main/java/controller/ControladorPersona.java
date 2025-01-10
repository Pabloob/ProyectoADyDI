package controller;

import DTO.PersonaDTO;
import Services.ServicioPersonaDTO;
import java.util.List;

public class ControladorPersona {
    // Se crea una instancia del servicio
    private final ServicioPersonaDTO persona = new ServicioPersonaDTO();

    // Método para obtener todos los datos de las personas
    public Object[][] obtenerTodo() {
        List<PersonaDTO> personas = persona.obtenerTodo();  // Obtiene la lista de todas las personas
        Object[][] datos = new Object[personas.size()][3];  // Se crea un array bidimensional con el tamaño adecuado
        for (int i = 0; i < personas.size(); i++) {
            datos[i][0] = personas.get(i).getNombre();  // Asigna el nombre de la persona
            datos[i][1] = personas.get(i).getDNI();  // Asigna el DNI de la persona
            datos[i][2] = personas.get(i).getID();  // Asigna el ID de la persona
        }
        return datos;  // Retorna el array con los datos de todas las personas
    }

    // Método para insertar una nueva persona
    public boolean insertar(Object[] datos) {
        String nombre = (String) datos[0];  // Se obtiene el nombre desde los datos proporcionados
        String DNI = (String) datos[1];  // Se obtiene el DNI desde los datos proporcionados
        String sexo = (String) datos[2];  // Se obtiene el sexo desde los datos proporcionados
        return persona.insertar(new PersonaDTO(nombre, DNI, sexo));  // Llama al servicio para insertar la nueva persona
    }

    public boolean DNIexiste(String DNI) {
        return persona.DNIexiste(DNI);
    }

}
