package com.appleboxestudios.futbol.service;

import com.appleboxestudios.futbol.model.Jugador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación en memoria del servicio de gestión de jugadores.
 * Usa un HashMap interno para almacenamiento con acceso O(1) por ID.
 * Los datos se pierden al cerrar la aplicación (sin persistencia).
 */
public class JugadorService implements IJugadorService {

    private final Map<Integer, Jugador> jugadores;
    private int nextId;

    public JugadorService() {
        this.jugadores = new HashMap<>();
        this.nextId = 1;
    }

    @Override
    public void add(Jugador jugador) {
        jugador.setId(nextId);
        jugadores.put(nextId, jugador);
        nextId++;
    }

    @Override
    public Jugador getById(int id) {
        return jugadores.get(id);
    }

    @Override
    public List<Jugador> getAll() {
        return new ArrayList<>(jugadores.values());
    }

    @Override
    public boolean update(Jugador jugador) {
        if (!jugadores.containsKey(jugador.getId())) {
            return false;
        }
        jugadores.put(jugador.getId(), jugador);
        return true;
    }

    @Override
    public boolean delete(int id) {
        return jugadores.remove(id) != null;
    }

    @Override
    public List<Jugador> findByNombre(String nombre) {
        String busqueda = nombre.toLowerCase();
        List<Jugador> resultados = new ArrayList<>();
        for (Jugador jugador : jugadores.values()) {
            if (jugador.getNombre().toLowerCase().contains(busqueda)) {
                resultados.add(jugador);
            }
        }
        return resultados;
    }

    @Override
    public List<Jugador> getRanking() {
        List<Jugador> ranking = new ArrayList<>(jugadores.values());
        ranking.sort((j1, j2) -> {
            // 1. Criterio principal: Mayor puntaje primero (orden descendente)
            int comparacionPuntaje = Double.compare(j2.getPuntajeRendimiento(), j1.getPuntajeRendimiento());
            if (comparacionPuntaje != 0) {
                return comparacionPuntaje;
            }

            // 2. Criterio de desempate: Menos partidos jugados primero (orden ascendente)
            int comparacionPartidos = Integer.compare(
                    j1.getEstadistica().getPartidosJugados(),
                    j2.getEstadistica().getPartidosJugados()
            );
            if (comparacionPartidos != 0) {
                return comparacionPartidos;
            }

            // 3. Criterio final: Orden alfabético por nombre
            return j1.getNombre().compareToIgnoreCase(j2.getNombre());
        });
        return ranking;
    }
}
