package ModuloTareas;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestorTareas {
    private List<Tarea> listaTareas;
    private List<Tarea> listaTareasFiltradas;
    private static final String ARCHIVO_TAREAS = "tareas.dat";

    public GestorTareas() {
        listaTareas = new ArrayList<>();
        cargarTareas();
        if (listaTareasFiltradas == null) {
            listaTareasFiltradas = new ArrayList<>(listaTareas);
        }
    }

    public void agregarTarea(Tarea tarea) {
        if (tarea == null) throw new IllegalArgumentException("La tarea no puede ser null.");
        if (existeTareaDuplicada(tarea)) {
            throw new IllegalArgumentException("Ya existe una tarea con el mismo título y descripción.");
        }
        listaTareas.add(tarea);
        listaTareasFiltradas = new ArrayList<>(listaTareas);
        guardarTareas();
    }

    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
        listaTareasFiltradas.remove(tarea);
        guardarTareas();
    }

    public void editarTarea(int indice, String titulo, String descripcion, LocalDate fecha, String prioridad) {
        if (indice >= 0 && indice < listaTareas.size()) {
            Tarea tareaActual = listaTareas.get(indice);
            Tarea tareaEditada = new Tarea(titulo, descripcion, fecha, prioridad);

            // Si la nueva tarea editada (sin contar esta misma) ya existe, no permitir
            for (int i = 0; i < listaTareas.size(); i++) {
                if (i != indice && listaTareas.get(i).equals(tareaEditada)) {
                    throw new IllegalArgumentException("Ya existe otra tarea con el mismo título y descripción.");
                }
            }

            tareaActual.setTitulo(titulo);
            tareaActual.setDescripcion(descripcion);
            tareaActual.setFechaLimite(fecha);
            tareaActual.setPrioridad(prioridad);
            listaTareasFiltradas.set(indice, tareaActual);
            guardarTareas();
        }
    }

    private boolean existeTareaDuplicada(Tarea tarea) {
        return listaTareas.stream().anyMatch(t -> t.equals(tarea));
    }

    public void marcarComoCompletada(int indice, boolean estado) {
        if (indice >= 0 && indice < listaTareas.size()) {
            Tarea tareaSeleccionada = listaTareasFiltradas.get(indice);
            for (Tarea t : listaTareas) {
                if (t == tareaSeleccionada) {
                    t.setCompletada(estado);
                    break;
                }
            }
            tareaSeleccionada.setCompletada(estado);
            guardarTareas();
        }
    }

    public List<Tarea> obtenerTareas() {
        return new ArrayList<>(listaTareasFiltradas);
    }

    public void filtrarPorPrioridad(String prioridad) {
        listaTareasFiltradas = listaTareas.stream()
            .filter(t -> t.getPrioridad().equalsIgnoreCase(prioridad))
            .collect(Collectors.toList());
    }

    public void ordenarPorFechaLimite() {
        listaTareasFiltradas.sort(Comparator.comparing(Tarea::getFechaLimite));
    }

    public void ordenarPorPrioridad() {
        Map<String, Integer> prioridadOrden = new HashMap<>();
        prioridadOrden.put("Alta", 1);
        prioridadOrden.put("Media", 2);
        prioridadOrden.put("Baja", 3);

        listaTareasFiltradas.sort(Comparator.comparingInt(t ->
            prioridadOrden.getOrDefault(t.getPrioridad(), Integer.MAX_VALUE)
        ));
    }

    public void restablecerTareas() {
        listaTareasFiltradas = new ArrayList<>(listaTareas);
    }

    public void guardarTareas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TAREAS))) {
            oos.writeObject(listaTareas);
        } catch (IOException e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarTareas() {
        File archivo = new File(ARCHIVO_TAREAS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                listaTareas = (List<Tarea>) ois.readObject();
                listaTareasFiltradas = new ArrayList<>(listaTareas);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar tareas: " + e.getMessage());
                listaTareas = new ArrayList<>();
                listaTareasFiltradas = new ArrayList<>();
            }
        } else {
            listaTareas = new ArrayList<>();
            listaTareasFiltradas = new ArrayList<>();
        }
    }
}
