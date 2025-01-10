package Vista;

import controller.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VentanaAsociar extends JFrame {

    private final ControladorPersona controladorPersona = new ControladorPersona();
    private final ControladorVehiculo controladorVehiculo = new ControladorVehiculo();
    private final ControladorRegistro controladorRegistro = new ControladorRegistro();

    private JPanel panelTablas;

    private final String[] nombreColumnasPersonas = {"Nombre", "DNI"};
    private final String[] nombreColumnasVehiculos = {"Matricula", "Año", "Marca", "Modelo"};
    private Object[][] datosPersonas;
    private Object[][] datosVehiculos;

    private JTable tablaPersonas;
    private JTable tablaVehiculos;
    private JScrollPane spPersonas;
    private JScrollPane spVehiculos;
    private DefaultTableModel dtmPersonas;
    private DefaultTableModel dtmVehiculos;

    private JButton asociar;
    private JCheckBox cbMostrarTodosVehiculos;

    public VentanaAsociar() {
        configurarPanel();
        configurarVentana();
        actualizarTablas();
        listeners();
    }

    private void configurarPanel() {
        panelTablas = new JPanel();
        panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.X_AXIS));

        // Configuración de las tablas y los scroll panes
        dtmPersonas = new DefaultTableModel(datosPersonas, nombreColumnasPersonas){
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaPersonas = new JTable(dtmPersonas);
        spPersonas = new JScrollPane(tablaPersonas);

        dtmVehiculos = new DefaultTableModel(datosVehiculos, nombreColumnasVehiculos);
        tablaVehiculos = new JTable(dtmVehiculos);
        spVehiculos = new JScrollPane(tablaVehiculos);

        // Crear el CheckBox y un panel de contención para centrarlo
        cbMostrarTodosVehiculos = new JCheckBox("Mostrar todos");
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));

        // Añadir espacios a los lados para centrar el checkbox
        checkBoxPanel.add(Box.createRigidArea(new Dimension(25, 0)));
        checkBoxPanel.add(cbMostrarTodosVehiculos);
        checkBoxPanel.add(Box.createRigidArea(new Dimension(25, 0)));

        // Botón para asociar
        asociar = new JButton("Asociar");

        // Añadir la primera tabla, el panel con el checkbox y la segunda tabla al panel principal
        panelTablas.add(spPersonas);
        panelTablas.add(checkBoxPanel);
        panelTablas.add(spVehiculos);
    }

    private void configurarVentana() {
        setLayout(new BorderLayout());
        add(panelTablas, BorderLayout.CENTER);
        add(asociar, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    //Metodo para actualizar las tablas
    private void actualizarTablas() {
        //Se obtienen todas las personas
        datosPersonas = controladorPersona.obtenerTodo();

        //Se comprueba si hay vehiculos sin propietario
        if (controladorVehiculo.obtenerSinPropietario() != null) {
            //Si hay alguno se añaden esos vehiculos a la tabla y el check box se muestra desseleccionado
            datosVehiculos = controladorVehiculo.obtenerSinPropietario();
            cbMostrarTodosVehiculos.setSelected(false);
        } else {
            //Si no hay ningun vehiculo sin propietario se obtienen todos lo vehiculos y el check box se muestra seleccionado
            cbMostrarTodosVehiculos.setSelected(true);
            datosVehiculos = controladorVehiculo.obtenerTodo();
        }
        // Actualiza el contenido de los modelos de tabla
        dtmPersonas.setDataVector(datosPersonas, nombreColumnasPersonas);
        dtmVehiculos.setDataVector(datosVehiculos, nombreColumnasVehiculos);
    }

    //Metodo con las acciones a realizar
    private void listeners() {
        cbMostrarTodosVehiculos.addActionListener(e -> accionMostrarVehiculos());
        asociar.addActionListener(e -> validarYAgregarRegistro());
    }

    //Metodo que cambia los datos de la tabla de vehiculos dependiendo de si el check box esta seleccionado o no
    private void accionMostrarVehiculos() {
        if (cbMostrarTodosVehiculos.isSelected()) {
            datosVehiculos = controladorVehiculo.obtenerTodo();
        } else {
            datosVehiculos = controladorVehiculo.obtenerSinPropietario();
        }
        dtmVehiculos.setDataVector(datosVehiculos, nombreColumnasVehiculos);

    }

    //Metodo que valida los datos e intenta insertar el registro
    private void validarYAgregarRegistro() {
        int filaPersonasSeleccionada = tablaPersonas.getSelectedRow();
        int filaVehiculosSeleccionada = tablaVehiculos.getSelectedRow();

        //Se valida si se ha seleccionado una fila de cada tabla
        if (filaPersonasSeleccionada >= 0 && filaVehiculosSeleccionada >= 0) {

            //Se obtiene el id correspondiente
            int idPersona = (int) datosPersonas[filaPersonasSeleccionada][2];
            int idVehiculo = (int) datosVehiculos[filaVehiculosSeleccionada][4];

            //Se muestra un mensaje de confirmacion de la asociacion
            int añadir = JOptionPane.showConfirmDialog(this, mostrarAntiguoPropietario(idVehiculo), "¿Confirmar?", JOptionPane.YES_NO_OPTION);

            if (añadir == JOptionPane.YES_OPTION) {

                //Si se confirma se comprueba si es un vehiculo con un propietario anterior
                if (!cbMostrarTodosVehiculos.isSelected()) {
                    //Si es un vehiculo sin propietario se inserta un nuevo registro
                    if (controladorRegistro.insertarRegistro(idPersona, idVehiculo)) {
                        //Si se ha asociado correctamente se muestra un mensaje y se actualizan las tablas
                        mostrarMensaje("Asociado correctamente", "Asociado");
                        actualizarTablas();
                    } else {
                        mostrarMensajeError("Ha habido algun error al asociar", "Error");
                    }
                } else {
                    //Si el vehiculo tenia un propietario anterior se llama a actualizar regitro que cierra el registro anteerior y crea uno nuevo
                    if (controladorRegistro.actualizarRegistro(idPersona, idVehiculo)) {
                        //Si se ha asociado correctamente se muestra un mensaje y se actualizan las tablas
                        mostrarMensaje("Asociacion actualizada correctamente", "Asociado");
                        actualizarTablas();
                    } else {
                        mostrarMensajeError("Ha habido algun problema", "Error");
                    }
                }
            }
        } else {
            mostrarMensajeError("Debes elegir un registro de cada tabla", "Error");
        }
    }

    private void mostrarMensajeError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    //Metodo que muestra el mensaje de confirmacion dependiendo de si el vehiculo tenia un propietario anterior o no
    private String mostrarAntiguoPropietario(int idVehiculo) {

        String nombreAntiguoPropietario = controladorRegistro.obtenerAntiguoPropietario(idVehiculo);
        String nombreNuevoPropietario = (String) datosPersonas[tablaPersonas.getSelectedRow()][0];
        String marca = (String) datosVehiculos[tablaVehiculos.getSelectedRow()][2];
        String modelo = (String) datosVehiculos[tablaVehiculos.getSelectedRow()][3];
        String texto;

        if (nombreAntiguoPropietario == null) {
            //Si el vehiculo no tenia propietario anterior devuelve un mensaje de confirmacion con el nombre
            //de la persona a asociar y datos del vehiculo
            texto = "Estas seguro que quieres asociar a " + nombreNuevoPropietario
                    + " con el vehiculo " + marca + " " + modelo;
        } else {
            //Si el vehiculo tenia un propietario anterior devuelve un mensaje de confirmacion con el nombre
            //de la persona a asociar, datos del vehiculo y el nombre del antiguo propietario
            texto = "Estas seguro que quieres asociar a " + nombreNuevoPropietario
                    + " con el vehiculo " + marca + " " + modelo + " y antiguo propietario " + nombreAntiguoPropietario;
        }
        return texto;
    }

}
