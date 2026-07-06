package service;

import model.Jugador;
import java.util.List;

/**
 * Contrato para el servicio de gestión de jugadores.
 * Define las operaciones CRUD y de búsqueda disponibles.
 */
public interface IJugadorService {

    /**
     * Agrega un nuevo jugador al sistema. El ID se asigna automáticamente.
     * @param jugador jugador a registrar
     */
    void add(Jugador jugador);

    /**
     * Obtiene un jugador por su ID.
     * @param id identificador del jugador
     * @return el jugador encontrado, o null si no existe
     */
    Jugador getById(int id);

    /**
     * Obtiene la lista completa de jugadores registrados.
     * @return lista de todos los jugadores
     */
    List<Jugador> getAll();

    /**
     * Actualiza los datos de un jugador existente.
     * @param jugador jugador con los datos actualizados (debe tener un ID válido)
     * @return true si se actualizó correctamente, false si no se encontró
     */
    boolean update(Jugador jugador);

    /**
     * Elimina un jugador por su ID.
     * @param id identificador del jugador a eliminar
     * @return true si se eliminó correctamente, false si no se encontró
     */
    boolean delete(int id);

    /**
     * Busca jugadores cuyo nombre contenga el texto indicado (insensible a mayúsculas).
     * @param nombre texto a buscar
     * @return lista de jugadores que coinciden
     */
    List<Jugador> findByNombre(String nombre);

    /**
     * Obtiene la lista de jugadores ordenada por su ranking de rendimiento deportivo.
     * Criterio: Mayor puntaje primero. Si hay empate, menor cantidad de partidos jugados.
     * Si persiste, orden alfabético por nombre.
     * @return lista ordenada de jugadores
     */
    List<Jugador> getRanking();
}
