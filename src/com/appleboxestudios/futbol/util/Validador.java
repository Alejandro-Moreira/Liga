package com.appleboxestudios.futbol.util;

/**
 * Clase utilitaria con métodos estáticos para validación de datos.
 * Se usará desde la capa UI para validar entrada del usuario.
 */
public class Validador {

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
}
