package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controller.ControladorDatos;
import controller.ControladorPersonaVehiculo;
import controller.ControladorRegistro;
import controller.ControladorVehiculo;
import entity.Filtro;
import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import vista.VentanaCrearVehiculo;

/*
Ventana principal donde se muestra por primera vez una tabla con los datos de las personas y su vehiculo actual
 */
public class VentanaPrincipal extends JFrame {

    // Controladores
    private final ControladorDatos controladorDatos = new ControladorDatos();
    private final ControladorVehiculo controladorVehiculo = new ControladorVehiculo();
    private final ControladorRegistro controladorRegistro = new ControladorRegistro();
    private final ControladorPersonaVehiculo controladorPersonaVehiculo = new ControladorPersonaVehiculo();

    // Componentes del menú
    private JMenuBar menu;
    private JMenu opciones;
    private JMenu crear;
    private JMenuItem crearPersona;
    private JMenuItem crearVehiculo;
    private JMenuItem asociar;

    // Componentes de la tabla
    private final String[] nombreColumnas = {"Nombre", "DNI", "Matricula", "Año", "Marca", "Modelo", "Propietarios totales"};
    private Object[][] datos;
    private DefaultTableModel dtm;
    private JTable tabla;
    private JScrollPane spTabla;

    // Botones
    private JButton aplicarFiltros;
    private JButton borrarFiltros;
    private JButton filtrosAvanzados;
    private JButton siguiente;
    private JButton anterior;
    private JButton mostrarAnterioresPropietarios;

    // Campos de texto
    private JTextField tfNombre;
    private JTextField tfAñoMatriculacion;
    private JTextField tfNVehiculos;

    // Radio button
    private JRadioButton hombre;
    private JRadioButton mujer;

    // Desplegables
    private JComboBox<String> cbMarca;
    private JComboBox<String> cbModelo;

    //Texto
    private JTextArea numPaginaYRegistros;

    // Paneles
    private JPanel panelTabla;
    private JPanel panelBotonesTabla;
    private JPanel panelSuperior;
    private JPanel panelOpciones;
    private JPanel panelAvanzado;

    // Variables de control
    private boolean filtrosAvanzadosVisible = false;
    private int numeroPagina = 0;
    private int numeroRegistros;
    private int errorCode;

    //Ventanas
    private VentanaAsociar ventanaAsociar = null;
    private VentanaCrearPersona ventanaCrearPersona = null;
    private VentanaCrearVehiculo ventanaCrearVehiculo = null;
    private VentanaPropietariosAnteriores ventanaPropietariosAnteriores = null;

    public VentanaPrincipal() {
        // Inicializar componentes
        inicializarComponentes();
        // Configurar paneles y añadir componentes
        configurarPaneles();
        // Configuración de la ventana
        configurarVentana();
        // Actualizar datos
        actualizarDatosTabla(obtenerFiltros());
        //Añadir listeners
        añadirListeners();
    }

    private void inicializarComponentes() {
        //Inicializar numero de registros
        numeroRegistros = controladorRegistro.obtenerNumRegistros();

        //Inicializar menu
        menu = new JMenuBar();
        opciones = new JMenu("Opciones");
        crear = new JMenu("Crear");
        asociar = new JMenuItem("Asociar");
        crearPersona = new JMenuItem("Persona");
        crearVehiculo = new JMenuItem("Vehiculo");
        setJMenuBar(menu);
        menu.add(opciones);
        opciones.add(crear);
        crear.add(crearPersona);
        crear.add(crearVehiculo);
        opciones.add(asociar);

        // Inicializar tabla y modelo y no permitir la edicion de los campos
        dtm = new DefaultTableModel(datos, nombreColumnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(dtm);
        spTabla = new JScrollPane(tabla);

        // Inicializar botones
        aplicarFiltros = new JButton("Aplicar Filtros");
        borrarFiltros = new JButton("Borrar Filtros");
        filtrosAvanzados = new JButton("Mostrar filtros avanzados   \u25BC");

        siguiente = new JButton("Siguiente (0)");
        anterior = new JButton("Anterior (0)");

        anterior.setEnabled(false);
        mostrarAnterioresPropietarios = new JButton("Mostrar anteriores propietarios");
        mostrarAnterioresPropietarios.setEnabled(false);

        // Inicializar radio buttons
        hombre = new JRadioButton("Hombre");
        mujer = new JRadioButton("Mujer");
        hombre.setSelected(false);
        mujer.setSelected(false);

        // Inicializar desplegables
        cbMarca = new JComboBox<>(controladorVehiculo.obtenerMarcas());
        cbModelo = new JComboBox<>(controladorVehiculo.obtenerModelos());

        // Inicializar campos de texto
        tfNombre = new JTextField(10);
        tfAñoMatriculacion = new JTextField(5);
        tfNVehiculos = new JTextField(5);

        //Inicializar textos
        numPaginaYRegistros = new JTextArea("""
                                            Numero de pagina:
                                            Numero de registros: 
                                             """);
        numPaginaYRegistros.setEditable(false);
        numPaginaYRegistros.setOpaque(false);
        numPaginaYRegistros.setFocusable(false);
        // Crear una fuente específica: nombre, estilo y tamaño
        Font fuentePersonalizada = new Font("Arial", Font.BOLD, 12);
        
        // Aplicar la fuente al JTextArea
        numPaginaYRegistros.setFont(fuentePersonalizada);

        // Inicializar paneles
        panelTabla = new JPanel(new BorderLayout());
        panelBotonesTabla = new JPanel(new GridLayout(1, 4, 5, 5));
        panelSuperior = new JPanel(new GridLayout(3, 1, 5, 5));
        panelOpciones = new JPanel(new FlowLayout());
        panelAvanzado = new JPanel(new FlowLayout());

    }

    private void configurarPaneles() {
        // Configuración de botones del panel superior
        aplicarFiltros.setPreferredSize(new Dimension(120, 25));
        borrarFiltros.setPreferredSize(new Dimension(120, 25));
        filtrosAvanzados.setPreferredSize(new Dimension(200, 25));

        // Configuración de desplegables
        cbMarca.setPreferredSize(new Dimension(70, 25));
        cbModelo.setPreferredSize(new Dimension(70, 25));

        // Configuración de paneles
        panelTabla.setBorder(new EmptyBorder(20, 0, 0, 0));
        panelSuperior.setBorder(new EmptyBorder(20, 0, 0, 0));
        panelAvanzado.setVisible(false);

        // Añadir componentes a sus paneles 
        // Panel de opciones
        panelOpciones.add(new JLabel("Nombre: "));
        panelOpciones.add(tfNombre);
        panelOpciones.add(hombre);
        panelOpciones.add(mujer);
        panelOpciones.add(new JLabel("Marca: "));
        panelOpciones.add(cbMarca);
        panelOpciones.add(filtrosAvanzados);
        panelOpciones.add(aplicarFiltros);
        panelOpciones.add(borrarFiltros);

        // Panel de opciones avanzadas
        panelAvanzado.add(new JLabel("Modelo: "));
        panelAvanzado.add(cbModelo);
        panelAvanzado.add(new JLabel("Año de Matriculacion: "));
        panelAvanzado.add(tfAñoMatriculacion);
        panelAvanzado.add(new JLabel("Nº d Vehiculos: "));
        panelAvanzado.add(tfNVehiculos);

        //Panel de botones de la tabla
        panelBotonesTabla.add(anterior);
        panelBotonesTabla.add(numPaginaYRegistros);
        panelBotonesTabla.add(siguiente);
        panelBotonesTabla.add(mostrarAnterioresPropietarios);
        // Panel de la tabla
        panelTabla.add(spTabla, BorderLayout.CENTER);
        panelTabla.add(panelBotonesTabla, BorderLayout.SOUTH);

        // Añadir paneles de opciones a el panel superior
        panelSuperior.add(panelOpciones);
        panelSuperior.add(panelAvanzado);
    }

    private void configurarVentana() {
        // Definimos las propiedades de la ventana y añadimos los paneles
        setLayout(new BorderLayout());
        add(panelSuperior, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    //Metodo donde se añaden todas las acciones a realizar a los botones y menus
    private void añadirListeners() {
        //Acciones de los items del menu
        crearVehiculo.addActionListener(e -> mostrarVentanaCrearVehiculo());
        crearPersona.addActionListener(e -> mostrarVentanaCrearPersona());
        asociar.addActionListener(e -> mostrarVentanaAsociar());

        //Acciones de los botones del panel inferior
        siguiente.addActionListener(e -> cambiarPagina(1));
        anterior.addActionListener(e -> cambiarPagina(-1));
        mostrarAnterioresPropietarios.addActionListener(e -> mostrarPropietariosAnteriores());
        botonesSexo();

        //Acciones de los botones del menu superior
        filtrosAvanzados.addActionListener(e -> manejoPanelFiltrosAvanzados());
        aplicarFiltros.addActionListener(e -> aplicarFiltros());
        borrarFiltros.addActionListener(e -> resetearCamposYVariables());

        //Accion de el clicado sobre una fila de la tabla
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                habilitarBotonMostrarPropietarios();
            }
        });

        aplicarFiltrosEnter();
    }

    //Metodo donde se actualiza la tabla con los datos que se recogen de la peticion de usuario
    private void actualizarDatosTabla(Filtro filtro) {
        //Si no hay ningun filtro se obtienen todos los datos de registros con vehiculos y su actual propietario
        //Y el numero de registros que hay
        if (filtro == null) {
            datos = controladorPersonaVehiculo.obtenerDatos(numeroPagina);
            numeroRegistros = controladorRegistro.obtenerNumRegistros();
        } else {
            //Si hay algun filtro se obtienen los registros filtrados y se actualiza el numero de registros obtenidos
            datos = controladorPersonaVehiculo.obtenerDatosFiltrados(filtro, numeroPagina);
            numeroRegistros = controladorRegistro.obtenerNumRegistrosFiltrados(filtro);
        }

        //Si los datos que se han obtenido no son nulos se actualiza la tabla con los datos obtenidos
        //Y se llama a el metodo que actualiza el funcionamiento de los botones de siguiente y anterior
        if (datos != null) {
            dtm.setDataVector(datos, nombreColumnas);
            actualizarBotonesAnteriorSiguiente();
            mostrarAnterioresPropietarios.setEnabled(false);
        } else {
            //Si no se ha obtenido ningun dato se muestra un mensaje de error dependiendo de si se han usado filtros o no
            mostrarDialogoError(filtro == null ? "No hay datos disponibles" : "No hay datos con los filtros aplicados");
            resetearCamposYVariables();
            //Se muestra una pagina con todos los datos
            numeroPagina = 0;
            actualizarDatosTabla(null);
        }
        actualizarTexto();
    }

    //Metodo que actuliza el texto de los botones inferiores
    public void actualizarTexto() {
        if (numeroRegistros != 0) {
            siguiente.setText("Siguiente ( " + (numeroPagina + 2) + " )");
            if (numeroPagina != 0) {
                anterior.setText("Anterior ( " + (numeroPagina - 1) + " )");
            } else {
                anterior.setText("Anterior (0)");
            }
        } else {
            siguiente.setText("Siguiente (0)");
            anterior.setText("Anterior (0)");
        }

        numPaginaYRegistros.setText("Numero de pagina: " + (numeroPagina + 1)
                + "\nNumero de registros totales: " + numeroRegistros);
    }

    //Metodo donde se obtienen los filtros de los campos del panel superior
    private Filtro obtenerFiltros() {
        String fnombre = getNombreFiltro();
        String fsexo = getSexoFiltro();
        String fmarca = getMarcaFiltro();
        String fmodelo = null;
        String faño = null;
        String fnVehiculos = null;

        if (filtrosAvanzadosVisible) {
            fmodelo = getModeloFiltro();
            faño = getAñoMatriculacionFiltro();
            fnVehiculos = getNumVehiculosFiltro();
        }

        //Si no hay ningun filtro se devuelve un null
        if (fnombre == null
                && fsexo == null
                && fmarca == null
                && fmodelo == null
                && faño == null
                && fnVehiculos == null) {
            return null;
        }

        //Si hay algun filtro se crea un array con los filtros
        Object[] filtro = {fnombre, fmodelo, fmarca, fsexo, faño, fnVehiculos};

        //Se comprueba si los datos son validos y se obtiene el numero de error correspondiente
        errorCode = controladorDatos.validarFiltros(filtro);

        //Si el numero de error no ha sido 0 (0 es que no hay ningun fallo), se muestra un mensaje de error correspondiente y se devuelve null
        if (errorCode != 0) {
            mostrarMensajeError(errorCode);
            resetearCamposYVariables();
            return null;
        }
        //Si los datos son validos se devuelve el nuevo filtro
        return new Filtro(fnombre, fmodelo, fmarca, fsexo, parseIntOCero(faño), parseIntOCero(fnVehiculos));
    }

    //Metodo donde se resetea todo y se hace un nuevo actualizado con todos los datos
    private void resetearCamposYVariables() {
        tfNombre.setText(null);
        hombre.setSelected(false);
        mujer.setSelected(false);
        tfAñoMatriculacion.setText(null);
        tfNVehiculos.setText(null);
        cbMarca.setSelectedItem("Todos");
        cbModelo.setSelectedItem("Todos");
        numeroPagina = 0;
        numeroRegistros = controladorRegistro.obtenerNumRegistros();
        actualizarDatosTabla(obtenerFiltros());
        filtrosAvanzadosVisible = true;
        manejoPanelFiltrosAvanzados();
    }

    //Metodo donde se cambia la pagina segun el parametro que se pase
    private void cambiarPagina(int incremento) {
        //Primero se calcula la pagina en la que nos encontramos
        numeroPagina += incremento;
        //Se actualizan los botones de anterior y siguiente
        actualizarBotonesAnteriorSiguiente();
        //Se actualizan los datos de la tabla con la nueva pagina
        actualizarDatosTabla(obtenerFiltros());

        //Cambiar el texto del boton de siguiente
        int numeroSiguiente = numeroPagina + 2;

        siguiente.setText("Siguiente (" + numeroSiguiente + ") ");

        //Cambiar el texto del boton de anterior
        int numeroAnterior = numeroPagina > 1 ? numeroPagina - 1 : numeroPagina;
        anterior.setText("Anterior (" + numeroAnterior + ") ");

    }

    //Metodo de control del panel de filtros avanzados
    private void manejoPanelFiltrosAvanzados() {
        //Se setea el panel avanzado como visible o no dependiendo de si ya estaba siendo mostrado o no
        panelAvanzado.setVisible(!filtrosAvanzadosVisible);
        //Se añade el texto correspondiente a el boton de filtros avanzados
        filtrosAvanzados.setText(filtrosAvanzadosVisible ? "Mostrar filtros avanzados   \u25BC" : "Ocultar filtros avanzados   \u25B2");

        //Se cambia el color del boton
        filtrosAvanzados.setBackground(!filtrosAvanzadosVisible ? Color.LIGHT_GRAY : aplicarFiltros.getBackground());

        //Se actualiza el valor de si el panel esta siendo mostrado o no
        filtrosAvanzadosVisible = !filtrosAvanzadosVisible;
    }

    //Metodo donde se actualiza la tabla con los filtros y se resetea el numero de pagina en 0 para mostrar los datos desde el principio
    private void aplicarFiltros() {
        numeroPagina = 0;
        actualizarDatosTabla(obtenerFiltros());
    }

    //Metodo que habilita o deshabilita los botones de siguiente y anterior
    private void actualizarBotonesAnteriorSiguiente() {
        //Si la pagina es mayor a 0 el boton de anterior esta habilitado ya que hay datos 
        anterior.setEnabled(numeroPagina > 0);
        //Se comprueba si los registros de la pagina siguiente a la actual son mayores a el total de registros
        //Y si hay algun registro
        siguiente.setEnabled((numeroPagina + 1) * 10 < numeroRegistros && numeroRegistros > 0);
    }

    //Metodo que muestra el menasje de error correspondiente a traves del numero de error que se pasa
    private void mostrarMensajeError(int numeroError) {
        String mensaje = switch (numeroError) {
            case 1 ->
                "Error: Datos de entrada inválidos";
            case 2 ->
                "Error: Número de vehículos inválido.";
            case 3 ->
                "Error: Número de año inválido.";
            default ->
                "Error desconocido.";
        };
        mostrarDialogoError(mensaje);
    }

    //Metodo de mensaje de error para no repetir codigo
    private void mostrarDialogoError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //Metodos que devuelve el valor de los campos de filtros
    //Si los campos estan vacios devuelve un null si no el valor de este campo
    private String getNombreFiltro() {
        String fnombre = tfNombre.getText().trim();
        return fnombre.isEmpty() ? null : fnombre;
    }

    private String getAñoMatriculacionFiltro() {
        String fañoMatriculacion = tfAñoMatriculacion.getText().trim();
        return fañoMatriculacion.isEmpty() ? null : fañoMatriculacion;
    }

    private String getNumVehiculosFiltro() {
        String fnVehiculos = tfNVehiculos.getText().trim();
        return fnVehiculos.isEmpty() ? null : fnVehiculos;
    }

    //Si ninguno de los botones de sexo esta seleccionado se devuelve un nulo si no se devuelve el sexo seleccionado
    private String getSexoFiltro() {
        if (!hombre.isSelected() && !mujer.isSelected()) {
            return null;
        }
        return hombre.isSelected() ? "M" : "F";
    }

    //Si el valor de los desplegables es Todos se devuelve un nulo
    private String getModeloFiltro() {
        return cbModelo.getSelectedItem().equals("Todos") ? null : (String) cbModelo.getSelectedItem();
    }

    private String getMarcaFiltro() {
        return cbMarca.getSelectedItem().equals("Todos") ? null : (String) cbMarca.getSelectedItem();
    }

    //Metodo para intentar transformar una cadena a un numero
    private int parseIntOCero(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    //Metodo para que no sea posible elegir dos sexos y que si se pulsa dos veces sobre el mismo sexo se quite
    private void botonesSexo() {
        hombre.addActionListener((e) -> {
            mujer.setSelected(false);
        });
        mujer.addActionListener((e) -> {
            hombre.setSelected(false);
        });
    }

    //Metodo que comprueba si el vehiculo de la fila que se ha seleccionado tiene algun propietario anterio
    private void habilitarBotonMostrarPropietarios() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Obtener el número de propietarios totales de la columna 6 que es el numero de propietarios que ha tenido el vehiculo
            int numPropietariosTotales = (int) dtm.getValueAt(filaSeleccionada, 6);

            // Habilitar o deshabilitar el boton si el numero de propietarios es mayor a 1
            mostrarAnterioresPropietarios.setEnabled(numPropietariosTotales > 1);
        }
    }

    //Metodos que crean la ventana correspondiente si no hay una ventana del mismo tipo creada anteriormente
    //Y al cerrar la ventana se vuelve a dejar la ventana de ese tipo como nula para que se pueda crear nuevamente
    private void mostrarVentanaAsociar() {
        if (ventanaAsociar == null) {
            ventanaAsociar = new VentanaAsociar();
            ventanaAsociar.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent we) {
                    //Se actualizan los datos para que las posibles nuevas asociaciones se muestren
                    actualizarDatosTabla(obtenerFiltros());
                    ventanaAsociar = null;
                }
            });
        }
    }

    private void mostrarPropietariosAnteriores() {
        if (ventanaPropietariosAnteriores == null) {
            //Se obtiene el numero de la fila seleccionada
            int numeroFila = tabla.getSelectedRow();
            //Se comprueba si el numero de la fila es igual o mayor a 0 
            if (numeroFila >= 0) {
                //Se obtiene el id del vehiculo que se encuentra en el array de datos en la posicion 7
                int idVehiculo = (int) datos[numeroFila][7];
                ventanaPropietariosAnteriores = new VentanaPropietariosAnteriores(idVehiculo);
                ventanaPropietariosAnteriores.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent we) {
                        ventanaPropietariosAnteriores = null;
                    }
                });
            }
        }
    }

    private void mostrarVentanaCrearPersona() {
        if (ventanaCrearPersona == null) {
            ventanaCrearPersona = new VentanaCrearPersona();
            ventanaCrearPersona.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent we) {
                    ventanaCrearPersona = null;
                }
            });
        }
    }

    private void mostrarVentanaCrearVehiculo() {
        if (ventanaCrearVehiculo == null) {
            ventanaCrearVehiculo = new VentanaCrearVehiculo();
            ventanaCrearVehiculo.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent we) {
                    ventanaCrearVehiculo = null;
                }
            });
        }
    }

    //Metodo para aplicar filtros si se pulsa enter 
    private void aplicarFiltrosEnter() {
        //Creamos el evento que captura si se pulsa enter dentro de el panel superior
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEvent e) -> {
            // Verificamos si se ha pulsado una tecla y si esa tecla es el enter
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
                //Verificamos si el panelSuperior tiene el foco
                if (panelSuperior.isAncestorOf(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner())) {
                    //Llamamos a actualizarDatosTabla
                    actualizarDatosTabla(obtenerFiltros());
                    return true;
                }
            }
            return false;
        });
    }

}
