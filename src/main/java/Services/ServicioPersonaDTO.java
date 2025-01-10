package Services;

import DAO.PersonaDAO;
import DTO.PersonaDTO;
import entity.Persona;
import java.util.ArrayList;
import java.util.List;

/*
Clase que conecta la tabla persona con las operaciones que se realizarán en la vista sobre las personas
 */
public class ServicioPersonaDTO {

    // Se crea el objeto DAO de Persona para interactuar con la base de datos
    private final PersonaDAO personaDAO = new PersonaDAO();

    // Método para obtener todas las personas desde la base de datos y convertirlas en objetos PersonaDTO
    public List<PersonaDTO> obtenerTodo() {
        List<PersonaDTO> personasDTO = new ArrayList<>();  // Lista que almacenará los objetos PersonaDTO
        List<Persona> personas = personaDAO.obtenerTodo();  // Obtiene todas las personas desde la base de datos
        if (personas == null) {  // Si no se encuentran personas, devuelve null
            return null;
        }
        for (Persona persona : personas) {  // Itera sobre la lista de personas obtenidas
            PersonaDTO personaDTO = new PersonaDTO(persona);  // Convierte cada objeto Persona en un PersonaDTO
            personasDTO.add(personaDTO);  // Agrega el PersonaDTO a la lista
        }

        return personasDTO;  // Devuelve la lista de PersonaDTO
    }

    // Método para insertar una nueva Persona en la base de datos a partir de un objeto PersonaDTO
    public boolean insertar(PersonaDTO personaDTO) {
        // Convierte el PersonaDTO a un objeto Persona y lo pasa al método insertar del DAO
        return personaDAO.insertar(new Persona(personaDTO.getNombre(), personaDTO.getDNI(), personaDTO.getSexo()));
    }

    //Metodo para comprobar si el DNI existe ya
    public boolean DNIexiste(String DNI) {
        return personaDAO.DNIexiste(DNI); //Devuelve si el DNI existe
    }

}
