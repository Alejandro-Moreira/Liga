package com.appleboxestudios.futbol.model;

/**
 * Enum que representa las posiciones disponibles para un jugador de fútbol.
 * Cada posición tiene una descripción legible para mostrar en la UI.
 */
public enum Posicion {
    PORTERO("Portero"),
    DEFENSA("Defensa"),
    MEDIOCAMPISTA("Mediocampista"),
    DELANTERO("Delantero");

    private final String descripcion;

    Posicion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
