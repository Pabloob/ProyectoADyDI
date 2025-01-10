package Vista;

import controller.ControladorDatos;
import controller.ControladorPersona;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class VentanaCrearPersona extends JFrame {
    
    private final ControladorDatos controlador = new ControladorDatos();
    private final ControladorPersona controladorPersona = new ControladorPersona();
    
    private final JTextField tfNombre = new JTextField();
    private final JTextField tfDNI = new JTextField();
    private final JRadioButton rbHombre = new JRadioButton("Hombre");
    private final JRadioButton rbMujer = new JRadioButton("Mujer");
    private final JPanel panelAñadirVehiculo = new JPanel(new GridLayout(4, 2, 5, 5));
    private final JButton bOK = new JButton("OK");
    private final JButton bCancelar = new JButton("Cancelar");
    
    public VentanaCrearPersona() {
        configurarPanel();
        configurarListeners();
        configurarVentana();
    }
    
    private void configurarPanel() {
        ButtonGroup grupoSexo = new ButtonGroup();
        grupoSexo.add(rbHombre);
        grupoSexo.add(rbMujer);
        
        panelAñadirVehiculo.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelAñadirVehiculo.add(new JLabel("Nombre:"));
        panelAñadirVehiculo.add(tfNombre);
        panelAñadirVehiculo.add(new JLabel("DNI:"));
        panelAñadirVehiculo.add(tfDNI);
        panelAñadirVehiculo.add(new JLabel("Sexo:"));

        // Crear un panel para los botones de sexo y añadirlo
        JPanel panelSexo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelSexo.add(rbHombre);
        panelSexo.add(rbMujer);
        panelAñadirVehiculo.add(panelSexo);
        
        panelAñadirVehiculo.add(bOK);
        panelAñadirVehiculo.add(bCancelar);
    }
    
    private void configurarVentana() {
        add(panelAñadirVehiculo);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    private void configurarListeners() {
        bOK.addActionListener(e -> accionBotonOK());
        bCancelar.addActionListener(e -> dispose());
    }

    //Metodo para la accion a realizar cuando se pulsa el boton OK
    private void accionBotonOK() {
        String nombre = tfNombre.getText().trim();
        String DNI = tfDNI.getText().trim();
        String sexo = rbHombre.isSelected() ? "M" : rbMujer.isSelected() ? "F" : null;

        //Se valida que los campos no esten vacios
        if (camposValidos(nombre, DNI, sexo)) {
            validarYAgregarPersona(new Object[]{nombre, DNI, sexo});
        } else {
            mostrarMensaje("No puede haber campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo que comprueba que los campos no esten vacios
    private boolean camposValidos(String nombre, String DNI, String sexo) {
        return !nombre.isEmpty() && !DNI.isEmpty() && sexo != null;
    }

    //Metodo que valida los datos e intenta insertar la persona
    private void validarYAgregarPersona(Object[] datos) {
        //Se comprueba que los datos tengan el formato correcto y que no contengan sentencias SQL
        switch (controlador.validarDatosPersona(datos)) {
            //Si no hay ningun error con la validacion de datos se intenta añadir
            case 0 -> {
                //Se comprueba si el DNI existe ya
                if (!controladorPersona.DNIexiste((String) datos[1])) {
                    //Si no existe se intenta insertar la persona
                    if (controladorPersona.insertar(datos)) {
                        mostrarMensaje("Añadido correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        vaciarCampos();
                    } else {
                        mostrarMensaje("Ha ocurrido algun problema al añadir la persona", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    mostrarMensaje("El DNI ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            //Si hay algun fallo se muestra el mensaje correspondiente
            case 1 ->
                mostrarMensaje("El nombre contiene caracteres prohibidos", "Error", JOptionPane.ERROR_MESSAGE);
            case 2 ->
                mostrarMensaje("El nombre contiene palabras prohibidas", "Error", JOptionPane.ERROR_MESSAGE);
            case 3 ->
                mostrarMensaje("Formato de DNI incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
            default ->
                throw new AssertionError("Error en la validación de datos");
        }
    }

    //Metodo para vaciar los campos
    private void vaciarCampos() {
        tfNombre.setText(null);
        tfDNI.setText(null);
        rbHombre.setSelected(false);
        rbMujer.setSelected(false);
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
    }
}
