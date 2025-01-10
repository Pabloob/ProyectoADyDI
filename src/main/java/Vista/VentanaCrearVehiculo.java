package vista;

import controller.ControladorDatos;
import controller.ControladorVehiculo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

public class VentanaCrearVehiculo extends JFrame {

    private final ControladorDatos controlador = new ControladorDatos();
    private final ControladorVehiculo controladorVehiculo = new ControladorVehiculo();

    private final JTextField tfMatricula = new JTextField();
    private final JTextField tfAño = new JTextField();
    private final JComboBox<String> cbMarcaVehiculos = crearComboMarcas();
    private final JComboBox<String> cbModeloVehiculos = new JComboBox<>();
    private final JPanel panelAñadirVehiculo = new JPanel(new GridLayout(5, 2, 5, 5));
    private final JButton bOK = new JButton("OK");
    private final JButton bCancelar = new JButton("Cancelar");

    public VentanaCrearVehiculo() {
        cbMarcaVehiculos.setSelectedItem(null);
        configurarPanel();
        configurarListeners();
        configurarVentana();
    }

    private JComboBox<String> crearComboMarcas() {
        String[] marcas = {"Toyota", "Ford", "BMW", "Audi", "Mercedes", "Volkswagen", "Peugeot", "Renault", "Seat", "Citro�n", "Chevrolet", "Fiat", "Kia", "Hyundai", "Nissan", "Mazda", "Honda"};
        return new JComboBox<>(marcas);
    }

    private void configurarPanel() {
        panelAñadirVehiculo.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelAñadirVehiculo.add(new JLabel("Matricula:"));
        panelAñadirVehiculo.add(tfMatricula);
        panelAñadirVehiculo.add(new JLabel("Año:"));
        panelAñadirVehiculo.add(tfAño);
        panelAñadirVehiculo.add(new JLabel("Marca:"));
        panelAñadirVehiculo.add(cbMarcaVehiculos);
        panelAñadirVehiculo.add(new JLabel("Modelo:"));
        panelAñadirVehiculo.add(cbModeloVehiculos);
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
        cbMarcaVehiculos.addActionListener(e -> actualizarModelos());
        bOK.addActionListener(e -> accionBotonOK());
        bCancelar.addActionListener(e -> dispose());
    }

    //Metodo que actualiza los modelos segun la marca seleccionada
    private void actualizarModelos() {
        cbModeloVehiculos.removeAllItems();

        String marca = (String) cbMarcaVehiculos.getSelectedItem();
        if (marca != null) {
            switch (marca) {
                case "Ford" ->
                    cargarModelos("Focus", "Fiesta", "Mondeo", "Mustang");
                case "BMW" ->
                    cargarModelos("Serie 1", "Serie 3", "X5");
                case "Audi" ->
                    cargarModelos("A3", "A4", "Q5");
                case "Mercedes" ->
                    cargarModelos("Clase A", "Clase B", "Clase C");
                case "Volkswagen" ->
                    cargarModelos("Golf", "Passat", "Tiguan", "T-Roc");
                case "Peugeot" ->
                    cargarModelos("208", "508", "3008");
                case "Renault" ->
                    cargarModelos("Clio", "Captur", "Zoe");
                case "Seat" ->
                    cargarModelos("Ibiza", "Leon", "Arona");
                case "Citroën" ->
                    cargarModelos("C3", "Berlingo");
                case "Chevrolet" ->
                    cargarModelos("Cruze", "Spark", "Aveo");
                case "Fiat" ->
                    cargarModelos("500", "Panda");
                case "Kia" ->
                    cargarModelos("Sportage", "Seltos");
                case "Hyundai" ->
                    cargarModelos("Elantra", "Kona");
                case "Nissan" ->
                    cargarModelos("Qashqai", "Micra");
                case "Mazda" ->
                    cargarModelos("CX-5", "CX-3", "Mazda3");
                case "Honda" ->
                    cargarModelos("Civic", "HR-V");
            }
        }
    }

    //Metodo que añade a el desplegable todos los modelos 
    private void cargarModelos(String... modelos) {
        for (String modelo : modelos) {
            cbModeloVehiculos.addItem(modelo);
        }
    }

    //Metodo de la accion cuando se pulsa el boton OK
    private void accionBotonOK() {
        String matricula = tfMatricula.getText().toUpperCase();
        String añoStr = tfAño.getText();
        String marca = (String) cbMarcaVehiculos.getSelectedItem();
        String modelo = (String) cbModeloVehiculos.getSelectedItem();

        //Se comprueba si los datos estan vacios 
        if (camposValidos(matricula, añoStr, marca, modelo)) {
            //Se intenta agregar el vehiculo
            validarYAgregarVehiculo(new Object[]{matricula, añoStr, marca, modelo});
        } else {
            //Si hay algun dato vacio se muestra un mensaje de erro
            mostrarMensaje("No puede haber campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Se comprueba si los datos estan vacios
    private boolean camposValidos(String matricula, String añoStr, String marca, String modelo) {
        return !matricula.isEmpty() && !añoStr.isEmpty() && marca != null && modelo != null;
    }

    //Metodo que intenta añadir el vehiculo
    private void validarYAgregarVehiculo(Object[] datos) {
        //Se llama a el controlador de datos y se validan los datos
        //Este metodo valida que no se añadan sentencias SQL y el formato de los datos
        switch (controlador.validarDatosVehiculo(datos)) {
            //Si el valor el 0 es que no hay ningun error de validacion de datos
            case 0 -> {
                //Se comprueba si la matricula existe
                if (!controladorVehiculo.matriculaExiste((String) datos[0])) {
                    //Si la matricula no existe se intenta añadir el vehiculo
                    if (controladorVehiculo.insertar(datos)) {
                        mostrarMensaje("Añadido correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        resetearCampos();
                    } else {
                        mostrarMensaje("Ha ocurrido algun error al añadir el vehiculo", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    mostrarMensaje("La matricula ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            //Si hay algun error se muestra el mensaje correspondiente
            case 1 ->
                mostrarMensaje("Formato de año incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
            case 2 ->
                mostrarMensaje("Formato de matrícula incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
            case 3 ->
                mostrarMensaje("Matricula incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            default ->
                throw new AssertionError("Error en la validación de datos");
        }
    }

    //Metodo para resetear los campos
    private void resetearCampos() {
        tfMatricula.setText(null);
        tfAño.setText(null);
        cbMarcaVehiculos.setSelectedItem(null);
        cbModeloVehiculos.setSelectedItem(null);
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
    }
}
