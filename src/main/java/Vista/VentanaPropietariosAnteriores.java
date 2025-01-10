package Vista;

import controller.ControladorRegistro;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class VentanaPropietariosAnteriores extends JFrame {

    // Crear el controlador
    private final ControladorRegistro controlador = new ControladorRegistro();

    // Componentes de la ventana
    private final JPanel panel;
    private final JTable tabla;
    private final DefaultTableModel dtm;
    private final Object[][] datos;
    private final String[] nombreColumnas;
    // Constructor que recibe el id del vehículo para mostrar los propietarios anteriores
    public VentanaPropietariosAnteriores(int idVehiculo) {
        // Obtener los datos de los propietarios anteriores a partir del id del vehículo
         datos = controlador.obtenerPropietarios(idVehiculo);

        // Nombres de las columnas de la tabla
         nombreColumnas = new String[]{"Propietario", "Fecha adquisicion", "Fecha venta"};

        // Inicialización del panel y configuración de bordes
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear la tabla y asociar el modelo con los datos y las columnas
        tabla = new JTable();
        dtm = new DefaultTableModel(datos, nombreColumnas){
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla.setModel(dtm);

        // Añadir la tabla al panel dentro de un JScrollPane para permitir el desplazamiento
        panel.add(new JScrollPane(tabla));

        // Configurar la ventana
        setTitle("Antiguos Propietarios");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(panel);
        pack();
        setVisible(true);
    }
}
