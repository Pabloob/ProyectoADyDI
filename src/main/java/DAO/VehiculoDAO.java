package DAO;

import entity.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Clase donde se interactua con la tabla Vehiculo y extiende la clase genericaDAO.
Se crean las conexiones y las PreparedStatements en un try-catch para cerrar las conexiones
automáticamente al acabar y un ResultSet también en un try-catch por el mismo motivo.
 */
public class VehiculoDAO extends GenericaDAO<Vehiculo> {

    // Método para obtener un Vehículo por su ID
    // Realiza una consulta a la base de datos para obtener un vehículo por su ID
    @Override
    public Vehiculo obtenerPorID(int id) {
        String query = "SELECT * FROM Vehiculo WHERE ID = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);  // Establece el parámetro ID en la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {  // Si encuentra resultados
                    return obtenerResultSet(rs);  // Devuelve un Vehiculo
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Si no se encuentra el vehículo, retorna null
    }

    // Método para obtener todos los vehículos
    // Realiza una consulta a la base de datos para obtener todos los vehículos
    public List<Vehiculo> obtenerTodo() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String query = "SELECT * FROM Vehiculo";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {  // Itera sobre cada fila del ResultSet 
                    vehiculos.add(obtenerResultSet(rs));  // Y añade un vehiculo a la lista de vehiculos
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!vehiculos.isEmpty()) {
            return vehiculos;  // Si la lista contiene elementos, la retorna
        } else {
            return null;  // Si la lista está vacía, retorna null
        }
    }

    // Método para obtener todas las marcas distintas de los vehículos
    // Realiza una consulta a la base de datos para obtener las marcas únicas de vehículos
    public List<String> obtenerMarcas() {
        List<String> marcas = new ArrayList<>();
        String query = "SELECT DISTINCT Marca FROM Vehiculo";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {  // Itera sobre cada fila del ResultSet
                    marcas.add(rs.getString("Marca"));  // Y añade la marca a la lista
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!marcas.isEmpty()) {
            return marcas;  // Si la lista contiene marcas, la retorna
        } else {
            return null;  // Si no hay marcas, retorna null
        }
    }

    // Método para obtener todos los modelos distintos de los vehículos
    // Realiza una consulta a la base de datos para obtener los modelos únicos de vehículos
    public List<String> obtenerModelos() {
        List<String> modelos = new ArrayList<>();
        String query = "SELECT DISTINCT Modelo FROM Vehiculo";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {  // Itera sobre cada fila del ResultSet
                    modelos.add(rs.getString("Modelo"));  // Y añade el modelo a la lista
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!modelos.isEmpty()) {
            return modelos;  // Si la lista contiene modelos, la retorna
        } else {
            return null;  // Si no hay modelos, retorna null
        }
    }

    // Método para obtener los vehículos que no están presentes en la tabla de registro
    // Realiza una consulta SQL compleja utilizando un LEFT JOIN y una subconsulta para obtener estos vehículos
    public List<Vehiculo> obtenerSinRegistro() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String query = """
                        SELECT v.*
                        FROM Vehiculo v
                        LEFT JOIN registro r ON v.ID = r.VehiculoID
                        WHERE (r.VehiculoID IS NULL OR r.Fecha_venta IS NOT NULL)
                        AND NOT EXISTS (
                        SELECT 1
                        FROM registro r2
                        WHERE r2.VehiculoID = v.ID AND r2.Fecha_venta IS NULL
                        )""";  // Consulta SQL para obtener los vehículos no registrados
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {  // Itera sobre cada fila del ResultSet
                    vehiculos.add(obtenerResultSet(rs));  // Y añade un vehiculo a la lista de vehiculos
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!vehiculos.isEmpty()) {
            return vehiculos;  // Si la lista contiene vehículos, la retorna
        } else {
            return null;  // Si no hay vehículos, retorna null
        }
    }

    // Método para insertar un vehículo en la base de datos
    // Realiza una consulta SQL para insertar los datos de un vehículo
    @Override
    public boolean insertar(Vehiculo vehiculo) {
        String query = "INSERT INTO Vehiculo (Matricula, Anio, Marca, Modelo) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, vehiculo.getMatricula());  // Establece el valor de la matrícula
            ps.setInt(2, vehiculo.getAnio());  // Establece el valor del año
            ps.setString(3, vehiculo.getMarca());  // Establece el valor de la marca
            ps.setString(4, vehiculo.getModelo());  // Establece el valor del modelo
            return ps.executeUpdate() > 0;  // Si la inserción fue exitosa, retorna true
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;  // Si la inserción falla, retorna false
    }

    //Metodo para comprobar si la matricula existe ya en la tabla vehiculo
    public boolean matriculaExiste(String matricula) {
        String sql = "SELECT COUNT(*) AS Total FROM vehiculo WHERE Matricula = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula); // Establece la matricula del vehiculo a buscar
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
    
    // Método privado que añade los datos de un ResultSet a un objeto Vehiculo
    private Vehiculo obtenerResultSet(ResultSet rs) throws SQLException {
        return new Vehiculo(rs.getInt("ID"), rs.getString("Matricula"), rs.getInt("Anio"), rs.getString("Marca"), rs.getString("Modelo"));
    }
}
