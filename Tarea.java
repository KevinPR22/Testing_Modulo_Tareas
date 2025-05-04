package ModuloTareas;

import java.time.LocalDate;

public class Tarea {
    private String titulo;
    private String descripcion;
    private LocalDate fechaLimite;
    private String prioridad; // "Alta", "Media", "Baja"
    private boolean completada;

    public Tarea(String titulo, String descripcion, LocalDate fechaLimite, String prioridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        setPrioridad(prioridad); // Validacion
        this.completada = false;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) {
        if (!prioridad.equalsIgnoreCase("Alta") &&
            !prioridad.equalsIgnoreCase("Media") &&
            !prioridad.equalsIgnoreCase("Baja")) {
            throw new IllegalArgumentException("Prioridad inválida solo puede ser Alta, Media o Baja.");
        }
        this.prioridad = prioridad;
    }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    @Override
    public String toString() {
        return "Tarea{" + "titulo='" + titulo + '\'' + ", descripcion='" + descripcion + '\'' + ", fechaLimite=" + fechaLimite + ", prioridad='" + prioridad + '\'' + ", completada=" + completada + '}';
    }
}
