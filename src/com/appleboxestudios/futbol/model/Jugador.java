package com.appleboxestudios.futbol.model;

/**
 * Entidad principal que representa a un jugador de fútbol.
 * Contiene datos personales y una referencia a sus estadísticas deportivas.
 * El ID es asignado por el servicio y es inmutable una vez establecido.
 */
public class Jugador {

    private int id;
    private String nombre;
    private Posicion posicion;
    private int edad;
    private String equipo;
    private Estadistica estadistica;

    /**
     * Constructor completo para crear un jugador.
     * La estadística se inicializa en 0 automáticamente.
     * @param nombre nombre completo del jugador
     * @param posicion posición en la cancha
     * @param edad edad del jugador
     * @param equipo nombre del equipo
     */
    public Jugador(String nombre, Posicion posicion, int edad, String equipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.equipo = equipo;
        this.estadistica = new Estadistica();
    }

    // --- Getters y Setters ---

    public int getId() {
        return id;
    }

    /**
     * El ID solo debe ser asignado por JugadorService.
     * @param id identificador único autogenerado
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public Estadistica getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(Estadistica estadistica) {
        this.estadistica = estadistica;
    }

    @Override
    public String toString() {
        return "Jugador{id=" + id + ", nombre='" + nombre + "', posicion=" + posicion
                + ", edad=" + edad + ", equipo='" + equipo + "', " + estadistica + "}";
    }
}
