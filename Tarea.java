package ModuloTareas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;
    private String prioridad;
    private boolean completada;

    public Tarea(String titulo, String descripcion, LocalDate fechaLimite, String prioridad) {
        if (fechaLimite.isBefore(LocalDate.of(2025, 1, 1))) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior al año 2025.");
        }
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        setPrioridad(prioridad);
        this.completada = false;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) {
        if (fechaLimite == null) {
            throw new IllegalArgumentException("La fecha límite no puede ser nula.");
        }
        if (fechaLimite.isBefore(LocalDate.of(2025, 1, 1))) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior al año 2025.");
        }
        this.fechaLimite = fechaLimite;
    }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) {
        if (!prioridad.equalsIgnoreCase("Alta") &&
            !prioridad.equalsIgnoreCase("Media") &&
            !prioridad.equalsIgnoreCase("Baja")) {
            throw new IllegalArgumentException("Prioridad inválida solo puede ser Alta, Media o Baja.");
        }
        this.prioridad = prioridad.substring(0, 1).toUpperCase() + prioridad.substring(1).toLowerCase();
    }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    @Override
    public String toString() {
        return "Tarea{" + "titulo='" + titulo + '\'' + ", descripcion='" + descripcion + '\'' + ", fechaLimite=" + fechaLimite + ", prioridad='" + prioridad + '\'' + ", completada=" + completada + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return Objects.equals(titulo, tarea.titulo) &&
               Objects.equals(descripcion, tarea.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, descripcion);
    }
}

