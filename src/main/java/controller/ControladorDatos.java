package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Clase que valida los datos que llegarán a la base de datos
public class ControladorDatos {

    // Método que valida los datos del vehículo
    public int validarDatosVehiculo(Object[] datos) {
        /*
        Errores:
        0 - Todo correcto
        1 - Formato año incorrecto
        2 - Formato matrícula incorrecto
        3 - Matrícula contiene elementos prohibidos
         */
        int errores = 0;

        String matricula = (String) datos[0];  // Matrícula del vehículo
        String añoStr = (String) datos[1];     // Año del vehículo
        boolean matriculaElementosValidos = true;
        boolean formatoMatriculaCorrecto = false;

        // Valida que la matrícula no tenga caracteres o palabras prohibidas
        if (contieneCaracteresProhibidos(matricula) || contienePalabrasProhibidas(matricula)) {
            matriculaElementosValidos = false; // La matrícula contiene elementos prohibidos
        }

        // Valida que la matrícula tenga 4 números seguidos de 3 letras y no contenga caracteres o palabras prohibidas
        if (matricula.matches("\\d{4}[A-Z]{3}")) {
            formatoMatriculaCorrecto = true;  // La matrícula tiene el formato correcto
        }

        boolean formatoAñoCorrecto = false;

        // Valida que el año tenga 4 números y esté entre 1970 y 2024
        try {
            int año = Integer.parseInt(añoStr);
            if (año >= 1970 && año <= 2024) {
                formatoAñoCorrecto = true;  // El año es válido
            }
        } catch (NumberFormatException e) {
            // Si el formato del año es incorrecto, no hace nada
        }

        // Se comprueban los errores
        if (!matriculaElementosValidos) {
            errores |= 3;  // Matrícula contiene elementos prohibidos
        }
        if (!formatoMatriculaCorrecto) {
            errores |= 2;  // El formato de matrícula es incorrecto
        }
        if (!formatoAñoCorrecto) {
            errores |= 1;  // El formato de año es incorrecto
        }

        return errores;  // Devuelve el código de error
    }

    // Método que valida los datos de la persona
    public int validarDatosPersona(Object[] datos) {
        /*
        Errores:
        0 - Todo correcto
        1 - Nombre contiene algún carácter prohibido
        2 - Nombre contiene alguna palabra prohibida
        3 - Formato DNI incorrecto
         */

        String nombre = (String) datos[0];  // Nombre de la persona
        String DNI = (String) datos[1];     // DNI de la persona

        // Se valida que el nombre no contenga ningún carácter no válido
        if ((nombre != null && contieneCaracteresProhibidos(nombre))) {
            return 1;  // El nombre contiene caracteres prohibidos
        }

        if ((nombre != null && contienePalabrasProhibidas(nombre))) {
            return 2;  // El nombre contiene palabras prohibidas
        }

        // Valida que el formato del DNI sea correcto (8 dígitos seguidos de una letra)
        boolean formatoDNICorrecto = DNI.matches("\\d{8}[A-Z]") && !contienePalabrasProhibidas(DNI) && !contieneCaracteresProhibidos(DNI);

        if (!formatoDNICorrecto) {
            return 3;  // El DNI tiene un formato incorrecto
        }

        return 0;  // Devuelve el código de error
    }

    // Método que valida los filtros
    public int validarFiltros(Object[] filtro) {
        /*
        Errores:
        0 - Todo correcto
        1 - Contiene nombre marca o modelo invalido
        2 - El formato de año no es válido
        3 - El formato de número de vehículos no es válido
         */
        String fnombre = (String) filtro[0];  // Filtro por nombre
        String fmodelo = (String) filtro[1];  // Filtro por modelo
        String fmarca = (String) filtro[2];   // Filtro por marca
        String faño = (String) filtro[4];     // Filtro por año
        String fnVehiculos = (String) filtro[5];  // Filtro por número de vehículos

        // Validación de caracteres prohibidos en los filtros
        String[] filtros = {fnombre, fmodelo, fmarca};
        for (String filtroValido : filtros) {
            if (filtroValido != null && (contieneCaracteresProhibidos(filtroValido) || contienePalabrasProhibidas(filtroValido))) {
                return 1;  // El filtro contiene caracteres o palabras prohibidas
            }
        }

        // Validación del formato de año
        if (faño != null && !faño.isEmpty()) {
            try {
                int año = Integer.parseInt(faño);  // Convierte el año a entero
                if (año < 1900 || año > 2024) {
                    return 2;  // El año está fuera del rango permitido
                }
            } catch (NumberFormatException e) {
                return 2;  // El formato de año no es válido
            }
        }

        // Validación del formato de número de vehículos
        if (fnVehiculos != null && !fnVehiculos.isEmpty()) {
            try {
                int nVehiculos = Integer.parseInt(fnVehiculos);  // Convierte el número de vehículos a entero
                if (nVehiculos < 0) {
                    return 3;  // El número de vehículos no puede ser negativo
                }
            } catch (NumberFormatException e) {
                return 3;  // El formato de número de vehículos no es válido
            }
        }

        return 0;  // Todo correcto
    }

    // Método que valida si un texto contiene caracteres prohibidos
    private boolean contieneCaracteresProhibidos(String texto) {
        // Lee el archivo de caracteres prohibidos
        List<String> caracteresProhibidos = leerArchivo("caracteresProhibidos.txt");
        if (texto != null) {
            for (String caracter : caracteresProhibidos) {
                
                // Se hace que se compare correctamente y no de error al hacer el matches con ( ".* en el if de debajo)
                String cEscapado = caracter.replaceAll("([\\\\*+\\[\\]{}()\\^$|?])", "\\\\$1");

                // Usamos \\b para comprobar si está presente en alguna parte del texto
                if (texto.toUpperCase().matches(".*\\b" + cEscapado.toUpperCase() + "\\b.*")) {
                    return true;  // Si contiene algún carácter prohibido, devuelve true
                }
            }
        }
        return false;  // Si no contiene caracteres prohibidos, devuelve false
    }

    // Método que valida si un texto contiene palabras prohibidas
    private boolean contienePalabrasProhibidas(String texto) {
        // Lee el archivo de palabras prohibidas
        List<String> palabrasProhibidas = leerArchivo("palabrasProhibidas.txt");
        if (texto != null) {
            for (String palabra : palabrasProhibidas) {
                // Usamos \\b para comprobar si esta presente en alguna parte de el texto
                if (texto.toUpperCase().matches(".*\\b" + palabra.toUpperCase() + "\\b.*")) {
                    return true;  // Si el texto contiene la palabra prohibida, devuelve true
                }
            }
        }
        return false;  // Si no contiene palabras prohibidas, devuelve false
    }

    // Método que lee un archivo y retorna su contenido como un conjunto de líneas únicas
    private List<String> leerArchivo(String archivo) {
        List<String> contenido = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.add(linea.trim());  // Añade cada línea al conjunto
            }
        } catch (IOException e) {
            e.printStackTrace();  // Si hay error al leer el archivo, muestra el error
        }
        return contenido;  // Devuelve el contenido como un conjunto
    }
}
