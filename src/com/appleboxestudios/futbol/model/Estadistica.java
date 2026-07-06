package com.appleboxestudios.futbol.model;

/**
 * Clase que encapsula las estadísticas deportivas de un jugador.
 * Incluye goles, asistencias y partidos jugados.
 * Los valores se inicializan en 0 por defecto.
 */
public class Estadistica {

    private int goles;
    private int asistencias;
    private int partidosJugados;

    /**
     * Constructor por defecto. Inicializa todas las estadísticas en 0.
     */
    public Estadistica() {
        this.goles = 0;
        this.asistencias = 0;
        this.partidosJugados = 0;
    }

    /**
     * Constructor con valores iniciales.
     * @param goles cantidad de goles marcados
     * @param asistencias cantidad de asistencias realizadas
     * @param partidosJugados cantidad de partidos jugados
     */
    public Estadistica(int goles, int asistencias, int partidosJugados) {
        this.goles = goles;
        this.asistencias = asistencias;
        this.partidosJugados = partidosJugados;
    }

    // --- Getters y Setters ---

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    @Override
    public String toString() {
        return "Goles=" + goles + ", Asistencias=" + asistencias + ", Partidos=" + partidosJugados;
    }
}
