package com.appleboxestudios.futbol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria con métodos estáticos para validación de datos.
 * Se usa desde la capa UI para validar la entrada del usuario.
 * Los métodos individuales validan un dato; validarJugador() agrupa todas las reglas.
 */
public class Validador {

    /** Edad mínima permitida para un jugador */
    private static final int EDAD_MINIMA = 1;

    /** Edad máxima permitida para un jugador */
    private static final int EDAD_MAXIMA = 60;

    /**
     * Verifica que un texto no sea null ni esté vacío (después de trim).
     * @param texto el texto a validar
     * @return true si el texto es válido (no vacío)
     */
    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Verifica que un String represente un entero positivo (>= 1).
     * @param texto el texto a validar
     * @return true si es un entero positivo
     */
    public static boolean esEnteroPositivo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica que un String represente un entero no negativo (>= 0).
     * @param texto el texto a validar
     * @return true si es un entero no negativo
     */
    public static boolean esEnteroNoNegativo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida todos los campos de un jugador y retorna una lista con los errores encontrados.
     * Si la lista está vacía, todos los datos son válidos.
     *
     * @param nombre  nombre del jugador
     * @param edad    texto del campo edad
     * @param equipo  nombre del equipo
     * @return lista de mensajes de error (vacía si no hay errores)
     */
    public static List<String> validarJugador(String nombre, String edad, String equipo) {
        List<String> errores = new ArrayList<>();

        // Validar nombre
        if (!esTextoValido(nombre)) {
            errores.add("El campo 'Nombre' es obligatorio.");
        }

        // Validar edad: primero que no esté vacío, luego formato numérico, luego rango
        if (!esTextoValido(edad)) {
            errores.add("El campo 'Edad' es obligatorio.");
        } else {
            try {
                int edadNum = Integer.parseInt(edad.trim());
                if (edadNum < EDAD_MINIMA) {
                    errores.add("La edad debe ser al menos " + EDAD_MINIMA + ".");
                } else if (edadNum > EDAD_MAXIMA) {
                    errores.add("La edad no puede ser mayor a " + EDAD_MAXIMA + ".");
                }
            } catch (NumberFormatException e) {
                errores.add("El campo 'Edad' debe ser un número entero válido (se ingresó: \"" + edad.trim() + "\").");
            }
        }

        // Validar equipo
        if (!esTextoValido(equipo)) {
            errores.add("El campo 'Equipo' es obligatorio.");
        }

        return errores;
    }

    /**
     * Valida los campos de estadísticas deportivas.
     *
     * @param goles           texto del campo goles
     * @param asistencias     texto del campo asistencias
     * @param partidosJugados texto del campo partidos jugados
     * @return lista de mensajes de error (vacía si no hay errores)
     */
    public static List<String> validarEstadisticas(String goles, String asistencias, String partidosJugados) {
        List<String> errores = new ArrayList<>();

        validarCampoNoNegativo(goles, "Goles", errores);
        validarCampoNoNegativo(asistencias, "Asistencias", errores);
        validarCampoNoNegativo(partidosJugados, "Partidos Jugados", errores);

        return errores;
    }

    /**
     * Método auxiliar para validar que un campo sea un entero no negativo.
     * Agrega el error a la lista si es inválido.
     */
    private static void validarCampoNoNegativo(String texto, String nombreCampo, List<String> errores) {
        if (!esTextoValido(texto)) {
            errores.add("El campo '" + nombreCampo + "' es obligatorio.");
        } else {
            try {
                int valor = Integer.parseInt(texto.trim());
                if (valor < 0) {
                    errores.add("El campo '" + nombreCampo + "' no puede ser negativo.");
                }
            } catch (NumberFormatException e) {
                errores.add("El campo '" + nombreCampo + "' debe ser un número entero válido (se ingresó: \"" + texto.trim() + "\").");
            }
        }
    }

    /**
     * Formatea una lista de errores como un único String con viñetas, listo para mostrar en JOptionPane.
     * @param errores lista de mensajes de error
     * @return String formateado con saltos de línea, o null si no hay errores
     */
    public static String formatearErrores(List<String> errores) {
        if (errores == null || errores.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("Se encontraron los siguientes errores:\n\n");
        for (String error : errores) {
            sb.append("• ").append(error).append("\n");
        }
        return sb.toString();
    }
}
