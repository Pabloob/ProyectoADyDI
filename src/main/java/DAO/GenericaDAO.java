package DAO;

import java.awt.Panel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

// Clase abstracta genérica con métodos comunes para todas las clases DAO
// Contiene lógica para manejar la conexión a la base de datos y métodos comunes como insertar y obtener por ID.
public abstract class GenericaDAO<T> {

    // Nombre del archivo de configuración
    private static final String ARCHIVO_CONF = "conexionBD.txt";

    // PoolDataSource compartido para todas las conexiones
    private static PoolDataSource poolDataSource;

    // Constructor estático para inicializar el pool al cargar la clase
    static {
        try {
            poolDataSource = PoolDataSourceFactory.getPoolDataSource();
            configurarPoolDataSource();
        } catch (SQLException e) {
            throw new RuntimeException("Error al configurar el PoolDataSource", e);
        }
    }

    // Métodos abstractos que deben implementar las clases concretas
    public abstract boolean insertar(T t);

    public abstract T obtenerPorID(int id);

    // Método para configurar el PoolDataSource
    private static void configurarPoolDataSource() throws SQLException {
        List<String> datosFichero = leerArchivoConfiguracion();

        if (datosFichero.size() < 3) {
            throw new IllegalArgumentException("Archivo de configuración incompleto. Se esperaban usuario, contraseña y URL.");
        }

        String usuario = datosFichero.get(0);
        String clave = datosFichero.get(1);
        String url = datosFichero.get(2);

        poolDataSource.setConnectionFactoryClassName("com.mysql.cj.jdbc.MysqlDataSource");
        poolDataSource.setURL(url);
        poolDataSource.setUser(usuario);
        poolDataSource.setPassword(clave);
        poolDataSource.setInitialPoolSize(5);
        poolDataSource.setMaxPoolSize(20);
    }

    // Método para leer el archivo de configuración
    private static List<String> leerArchivoConfiguracion() {
        List<String> datosFichero = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_CONF))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("=");
                if (partes.length >= 2) {
                    datosFichero.add(partes[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de configuración: " + ARCHIVO_CONF, e);
        }

        return datosFichero;
    }
    
    // Método protegido para obtener una conexión desde el pool
    protected Connection conectar() {
        try {
            return poolDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión desde el PoolDataSource", e);
        }
    }
    
}
