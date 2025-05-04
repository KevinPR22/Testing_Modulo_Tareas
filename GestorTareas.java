package ModuloTareas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GestorTareas {
    private List<Tarea> listaTareas;
    private List<Tarea> listaTareasFiltradas;

    public GestorTareas() {
        listaTareas = new ArrayList<>();
        listaTareasFiltradas = new ArrayList<>(listaTareas);
    }

    // Agregar nueva tarea
    public void agregarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser null.");
        }
        listaTareas.add(tarea);
        listaTareasFiltradas.add(tarea);  // Añadimos a la lista filtrada también
    }

    // Eliminar tarea
    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
        listaTareasFiltradas.remove(tarea); // También eliminamos de la lista filtrada
    }

    // Actualizar tarea (reemplazo completo)
    public void actualizarTarea(Tarea tareaOriginal, Tarea tareaEditada) {
        int index = listaTareas.indexOf(tareaOriginal);
        if (index != -1) {
            listaTareas.set(index, tareaEditada);
            listaTareasFiltradas.set(index, tareaEditada); // Actualizamos también en la lista filtrada
        }
    }

    // Editar campos de una tarea por índice
    public void editarTarea(int indice, String titulo, String descripcion, LocalDate fecha, String prioridad) {
        if (indice >= 0 && indice < listaTareas.size()) {
            Tarea t = listaTareas.get(indice);
            t.setTitulo(titulo);
            t.setDescripcion(descripcion);
            t.setFechaLimite(fecha);
            t.setPrioridad(prioridad);

            // También actualizamos la lista filtrada
            listaTareasFiltradas.set(indice, t);
        }
    }

    // Marcar tarea como completada o pendiente
    public void marcarComoCompletada(int indice, boolean estado) {
        if (indice >= 0 && indice < listaTareas.size()) {
            listaTareas.get(indice).setCompletada(estado);
            listaTareasFiltradas.get(indice).setCompletada(estado); // Actualizamos la lista filtrada
        }
    }

    // Obtener tareas visibles (ya filtradas si corresponde)
    public List<Tarea> obtenerTareas() {
        return new ArrayList<>(listaTareasFiltradas);
    }

    // Filtrar tareas por prioridad
    public void filtrarPorPrioridad(String prioridad) {
        listaTareasFiltradas = listaTareas.stream()
            .filter(t -> t.getPrioridad().equalsIgnoreCase(prioridad))
            .collect(Collectors.toList());
    }

    // Ordenar tareas por fecha límite
    public void ordenarPorFechaLimite() {
        listaTareasFiltradas.sort(Comparator.comparing(Tarea::getFechaLimite));
    }

    // Ordenar tareas por prioridad
    public void ordenarPorPrioridad() {
        listaTareasFiltradas.sort(Comparator.comparing(Tarea::getPrioridad));
    }

    // Restablecer la lista de tareas filtradas
    public void restablecerTareas() {
        listaTareasFiltradas = new ArrayList<>(listaTareas); // Volver a todas las tareas
    }
}
