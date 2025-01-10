package DAO;

import entity.Filtro;
import entity.Registro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Clase donde se interactua con la tabla Registro y extiende la clase genericaDAO
Se crean las conexiones y las PreparedStatements en un try-catch para cerrar las conexiones
al acabar automaticamente y un ResultSet en un try catch tambien por lo mismo
 */
public class RegistroDAO extends GenericaDAO<Registro> {

    // Método para obtener un registro por su id de vehículo
    @Override
    public Registro obtenerPorID(int idVehiculo) {
        String sql = "SELECT * FROM Registro WHERE Fecha_venta IS NULL AND VehiculoID = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo); // Asigna el id del vehículo al PreparedStatement
            try (ResultSet rs = ps.executeQuery()) { // Ejecuta la consulta y obtiene el ResultSet
                if (rs.next()) { // Si hay un resultado
                    return obtenerResultSet(rs); // Devuelve el objeto Registro construido
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si no hay coincidencias
    }

    // Método para obtener registros de 10 en 10, correspondiente a cada página solicitada
    public List<Registro> obtenerPaginado(int pagina) {
        List<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM Registro WHERE Fecha_venta IS NULL ORDER BY PersonaID ASC LIMIT 10 OFFSET ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pagina * 10); // Calcula el desplazamiento (offset) según la página
            try (ResultSet rs = ps.executeQuery()) { // Ejecuta la consulta y recorre el ResultSet
                while (rs.next()) {
                    registros.add((obtenerResultSet(rs))); // Añade cada registro a la lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros.isEmpty() ? null : registros; // Devuelve null si no hay registros
    }

    /// Método para obtener registros de 10 en 10 a partir de filtros, para cada página
    public List<Registro> obtenerFiltrado(int pagina, Filtro filtro) {
        List<Registro> registros = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT r.* FROM Registro r "
                + "JOIN persona p ON r.PersonaID = p.ID "
                + "JOIN vehiculo v ON r.VehiculoID = v.ID WHERE r.Fecha_venta IS NULL ");
        List<Object> parametros = new ArrayList<>();

        // Agrega condiciones de filtro a partir de los valores del Filtro
        if (filtro.getNombre() != null) {
            query.append(" AND p.Nombre = ?");
            parametros.add(filtro.getNombre());
        }
        if (filtro.getMarca() != null) {
            query.append(" AND v.Marca = ?");
            parametros.add(filtro.getMarca());
        }
        if (filtro.getModelo() != null) {
            query.append(" AND v.Modelo = ?");
            parametros.add(filtro.getModelo());
        }
        if (filtro.getSexo() != null) {
            query.append(" AND p.Sexo = ?");
            parametros.add(filtro.getSexo());
        }
        if (filtro.getAnio() != 0) {
            query.append(" AND v.Anio = ?");
            parametros.add(filtro.getAnio());
        }
        if (filtro.getNumVehiculos() > 0) {
            query.append(" AND r.PersonaID IN ("
                    + "SELECT r2.PersonaID FROM Registro r2 "
                    + "JOIN vehiculo v2 ON r2.VehiculoID = v2.ID "
                    + "WHERE r2.Fecha_venta IS NULL ");

            // Incluye los filtros de marca, modelo y año en la subconsulta si están presentes
            if (filtro.getMarca() != null) {
                query.append("AND v2.Marca = ? ");
                parametros.add(filtro.getMarca());
            }
            if (filtro.getModelo() != null) {
                query.append(" AND v2.Modelo = ?");
                parametros.add(filtro.getModelo());
            }
            if (filtro.getAnio() != 0) {
                query.append("AND v2.Anio = ? ");
                parametros.add(filtro.getAnio());
            }

            query.append("GROUP BY r2.PersonaID "
                    + "HAVING COUNT(r2.VehiculoID) = ?"
                    + ")");
            parametros.add(filtro.getNumVehiculos());
        }
        query.append(" ORDER BY r.PersonaID ASC LIMIT 10 OFFSET ?");
        parametros.add(pagina * 10); // Añade el número de página como último parámetro

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i)); // Asigna los valores de parámetros al PreparedStatement
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registros.add(obtenerResultSet(rs)); // Añade cada registro filtrado a la lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros.isEmpty() ? null : registros; // Devuelve null si no hay registros
    }

    // Método para cerrar el último registro de un vehículo con la fecha de compra del nuevo propietario
    public boolean cerrarRegistro(Registro registro) {
        String queryUpdate = "UPDATE Registro SET Fecha_venta = ? WHERE VehiculoID = ? AND Fecha_venta IS NULL";
        try (Connection con = conectar(); PreparedStatement psUpdate = con.prepareStatement(queryUpdate)) {
            psUpdate.setString(1, registro.getFechaAdquisicion()); // Asigna la fecha de venta
            psUpdate.setInt(2, registro.getVehiculoID()); // Asigna el ID del vehículo
            return psUpdate.executeUpdate() > 0 && insertar(registro); // Ejecuta la actualización y añade un nuevo registro
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Devuelve false si falla la operación
    }

    // Método para contar los registros de vehículos sin venta (actual propietario)
    public int obtenerNumRegistros() {
        String sql = "SELECT COUNT(*) AS total FROM Registro WHERE Fecha_venta IS NULL";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total"); // Devuelve el numero total
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Devuelve 0 si ocurre un error
    }

    // Método para contar los registros de vehículos sin venta filtrado
    public int obtenerNumRegistrosFiltrados(Filtro filtro) {
        List<Object> parametros = new ArrayList<>();
        int registros = 0;
        StringBuilder query = new StringBuilder(
                "SELECT COUNT(*) AS total FROM Registro r "
                + "JOIN persona p ON r.PersonaID = p.ID "
                + "JOIN vehiculo v ON r.VehiculoID = v.ID "
                + "WHERE r.Fecha_venta IS NULL "
        );

        // Agrega condiciones de filtro a partir de los valores del Filtro
        if (filtro.getNombre() != null) {
            query.append(" AND p.Nombre = ?");
            parametros.add(filtro.getNombre());
        }
        if (filtro.getMarca() != null) {
            query.append(" AND v.Marca = ?");
            parametros.add(filtro.getMarca());
        }
        if (filtro.getModelo() != null) {
            query.append(" AND v.Modelo = ?");
            parametros.add(filtro.getModelo());
        }
        if (filtro.getSexo() != null) {
            query.append(" AND p.Sexo = ?");
            parametros.add(filtro.getSexo());
        }
        if (filtro.getAnio() != 0) {
            query.append(" AND v.Anio = ?");
            parametros.add(filtro.getAnio());
        }
        if (filtro.getNumVehiculos() > 0) {
            query.append(" AND r.PersonaID IN ("
                    + "SELECT r2.PersonaID FROM Registro r2 "
                    + "JOIN vehiculo v2 ON r2.VehiculoID = v2.ID "
                    + "WHERE r2.Fecha_venta IS NULL ");

            // Incluye filtros de marca, modelo y año en la subconsulta si están presentes
            if (filtro.getMarca() != null) {
                query.append("AND v2.Marca = ? ");
                parametros.add(filtro.getMarca());
            }
            if (filtro.getModelo() != null) {
                query.append(" AND v2.Modelo = ?");
                parametros.add(filtro.getModelo());
            }
            if (filtro.getAnio() != 0) {
                query.append("AND v2.Anio = ? ");
                parametros.add(filtro.getAnio());
            }

            query.append("GROUP BY r2.PersonaID "
                    + "HAVING COUNT(r2.VehiculoID) = ?"
                    + ")");
            parametros.add(filtro.getNumVehiculos());
        }

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i)); // Asigna los valores de parámetros al PreparedStatement
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    registros = rs.getInt("total"); // Almacena el numero total de registros filtrados
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros; // Devuelve el total de registros filtrados
    }

    // Método para obtener todos los propietarios previos y el actual de un vehículo, en orden cronológico
    public List<Registro> obtenerPropietarios(int idVehiculo) {
        List<Registro> propietarios = new ArrayList<>();
        String queryPropietarioActual = "SELECT * FROM Registro WHERE VehiculoID = ? AND Fecha_venta IS NULL";
        String queryPropietariosAnteriores = "SELECT * FROM Registro WHERE VehiculoID = ? AND Fecha_venta IS NOT NULL ORDER BY Fecha_adquisicion DESC";
        try (Connection con = conectar(); PreparedStatement psPropietarioActual = con.prepareStatement(queryPropietarioActual); PreparedStatement psPropietariosAnteriores = con.prepareStatement(queryPropietariosAnteriores)) {
            psPropietarioActual.setInt(1, idVehiculo); // Define el ID del vehículo para el propietario actual
            try (ResultSet rsActual = psPropietarioActual.executeQuery()) {
                while (rsActual.next()) {
                    propietarios.add(obtenerResultSet(rsActual));
                }
            }
            psPropietariosAnteriores.setInt(1, idVehiculo); // Define el ID del vehículo para los propietarios anteriores
            try (ResultSet rsAnteriores = psPropietariosAnteriores.executeQuery()) {
                while (rsAnteriores.next()) {
                    propietarios.add(obtenerResultSet(rsAnteriores));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propietarios; // Devuelve la lista de propietarios
    }

    // Método para contar el número de propietarios que ha tenido un vehículo
    public int obtenerNumPersonasPorID(int idVehiculo) {
        int numVehiculos = 0;
        String query = "SELECT COUNT(*) AS total FROM Registro WHERE VehiculoID = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idVehiculo); // Asigna el ID del vehículo al PreparedStatement
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    numVehiculos = rs.getInt("total"); // Obtiene el numero total de propietarios
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return numVehiculos; // Devuelve el número de propietarios
    }

    // Método para añadir un registro de un vehículo
    @Override
    public boolean insertar(Registro registro) {
        String sql = "INSERT INTO Registro(Fecha_adquisicion, Fecha_venta, VehiculoID, PersonaID) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, registro.getFechaAdquisicion()); // Asigna la fecha de adquisición
            ps.setString(2, registro.getFechaVenta()); // Asigna la fecha de venta
            ps.setInt(3, registro.getVehiculoID()); // Asigna el ID del vehículo
            ps.setInt(4, registro.getPersonaID()); // Asigna el ID de la persona
            return ps.executeUpdate() > 0; // Devuelve true si la inserción fue exitosa
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Devuelve false si ocurre un error.
    }

    // Convierte el ResultSet a un objeto Registro
    private Registro obtenerResultSet(ResultSet rs) throws SQLException {
        return new Registro(
                rs.getInt("ID"),
                rs.getString("Fecha_adquisicion"),
                rs.getString("Fecha_venta"),
                rs.getInt("VehiculoID"),
                rs.getInt("PersonaID")
        );
    }
}
