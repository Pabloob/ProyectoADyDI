package DAO;

import entity.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Clase donde se interactua con la tabla Persona y extiende la clase genericaDAO
Se crean las conexiones y las PreparedStatements en un try-catch para cerrar las conexiones
automáticamente al acabar y un ResultSet en un try-catch también por el mismo motivo
 */
public class PersonaDAO extends GenericaDAO<Persona> {

    // Método para obtener una persona específica por su ID
    // Realiza una consulta a la base de datos para obtener una persona por su ID
    @Override
    public Persona obtenerPorID(int id) {
        String sql = "SELECT * FROM Persona WHERE ID = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);  // Establece el parámetro ID en la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {  // Si encuentra una persona
                    return obtenerResultSet(rs);  // Devuelve la persona correspondiente
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Si no encuentra la persona, devuelve null
    }

    // Método para obtener todas las personas de la base de datos
    // Realiza una consulta para obtener todos los registros de la tabla Persona
    public List<Persona> obtenerTodo() {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM Persona";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {  // Itera sobre cada fila del ResultSet
                personas.add(obtenerResultSet(rs));  // Añade cada persona a la lista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!personas.isEmpty()) {
            return personas;  // Si la lista contiene personas, la devuelve
        } else {
            return null;  // Si la lista está vacía, devuelve null
        }
    }

    // Método para insertar una persona en la base de datos
    // Realiza una consulta SQL para insertar los datos de una persona
    @Override
    public boolean insertar(Persona persona) {
        String sql = "INSERT INTO Persona (Nombre, DNI, Sexo) VALUES (?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());  // Establece el nombre de la persona
            ps.setString(2, persona.getDNI());     // Establece el DNI de la persona
            ps.setString(3, persona.getSexo());    // Establece el sexo de la persona
            return ps.executeUpdate() > 0;  // Si la inserción fue exitosa, devuelve true
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Si la inserción falla, devuelve false
    }

    //Metodo para comprobar si el DNI existe ya en la tabla persona
    public boolean DNIexiste(String DNI) {
        String sql = "SELECT COUNT(*) AS Total FROM persona WHERE DNI = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, DNI); // Establece el DNI de la persona a buscar
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Total") > 0; //Se comprueba si ha habido alguna coincidencia
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Si hay algun error devuelve false
    }

    // Método privado que crea una persona a partir de un ResultSet
    // Toma un ResultSet y devuelve una instancia de Persona con los datos correspondientes
    private Persona obtenerResultSet(ResultSet rs) throws SQLException {
        return new Persona(rs.getInt("ID"), rs.getString("Nombre"), rs.getString("DNI"), rs.getString("Sexo"));
    }
}
