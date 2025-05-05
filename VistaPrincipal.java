package ModuloTareas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VistaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private GestorTareas gestor;
    private JTable tablaTareas;
    private DefaultTableModel modeloTabla;

    public VistaPrincipal() {
        gestor = new GestorTareas();
        setTitle("Gestor de Tareas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Tabla lo que sale en la parte de arriba
        modeloTabla = new DefaultTableModel(new Object[]{"Título", "Descripción", "Prioridad", "Fecha Límite", "Estado"}, 0);
        tablaTareas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 6, 10, 10)); // Añadimos una columna extra para los nuevos botones
        JButton btnAgregar = new JButton("Agregar Tarea");
        JButton btnEditar = new JButton("Editar Tarea");
        JButton btnEliminar = new JButton("Eliminar Tarea");
        JButton btnMarcarEstado = new JButton("Marcar Completada/Pendiente");
        JButton btnSalir = new JButton("Salir");
        JButton btnFiltrarPrioridad = new JButton("Filtrar por Prioridad");
        JButton btnOrdenarPorFecha = new JButton("Ordenar por Fecha");
        JButton btnOrdenarPorPrioridad = new JButton("Ordenar por Prioridad");

        //Botones al panel
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnMarcarEstado);
        panelBotones.add(btnSalir);
        panelBotones.add(btnFiltrarPrioridad);
        panelBotones.add(btnOrdenarPorFecha);
        panelBotones.add(btnOrdenarPorPrioridad);
        add(panelBotones, BorderLayout.SOUTH);

        // ActionListeners
        btnAgregar.addActionListener(e -> agregarTarea());
        btnEditar.addActionListener(e -> editarTareaSeleccionada());
        btnEliminar.addActionListener(e -> eliminarTareaSeleccionada());
        btnMarcarEstado.addActionListener(e -> cambiarEstadoTareaSeleccionada());
        btnSalir.addActionListener(e -> System.exit(0));

        // Filtros y ordenamientos
        btnFiltrarPrioridad.addActionListener(e -> filtrarPorPrioridad());
        btnOrdenarPorFecha.addActionListener(e -> ordenarPorFecha());
        btnOrdenarPorPrioridad.addActionListener(e -> ordenarPorPrioridad());
    }

    private void agregarTarea() {
        String titulo = "";
        while (titulo.trim().isEmpty()) {
            titulo = JOptionPane.showInputDialog(this, "Título de la tarea:");
            if (titulo == null) return;  
            if (titulo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        String descripcion = "";
        while (descripcion.trim().isEmpty()) {
            descripcion = JOptionPane.showInputDialog(this, "Descripción:");
            if (descripcion == null) return;  
            if (descripcion.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String prioridad = "";
        while (true) {
            prioridad = JOptionPane.showInputDialog(this, "Prioridad (Alta, Media, Baja):");
            if (prioridad == null) return;  
            if (prioridad.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La prioridad no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!prioridad.equalsIgnoreCase("Alta") && !prioridad.equalsIgnoreCase("Media") && !prioridad.equalsIgnoreCase("Baja")) {
                JOptionPane.showMessageDialog(this, "La prioridad debe ser 'Alta', 'Media' o 'Baja'.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;  
            }
        }
    
        String fecha = "";
        while (true) {
            fecha = JOptionPane.showInputDialog(this, "Fecha límite (YYYY-MM-DD):");
            if (fecha == null) return;  
            if (fecha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    LocalDate fechaLimite = LocalDate.parse(fecha);  
                    break; 
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        try {
            LocalDate fechaLimite = LocalDate.parse(fecha);
            gestor.agregarTarea(new Tarea(titulo, descripcion, fechaLimite, prioridad));
            actualizarTabla();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de prioridad", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void editarTareaSeleccionada() {
        int fila = tablaTareas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para editar.");
            return;
        }

        Tarea tarea = gestor.obtenerTareas().get(fila);

        String nuevoTitulo = "";
        while (nuevoTitulo.trim().isEmpty()) {
            nuevoTitulo = JOptionPane.showInputDialog(this, "Nuevo título:", tarea.getTitulo());
            if (nuevoTitulo == null) return;  
            if (nuevoTitulo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        String nuevaDescripcion = "";
        while (nuevaDescripcion.trim().isEmpty()) {
            nuevaDescripcion = JOptionPane.showInputDialog(this, "Nueva descripción:", tarea.getDescripcion());
            if (nuevaDescripcion == null) return;  
            if (nuevaDescripcion.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        String nuevaPrioridad = "";
        while (true) {
            nuevaPrioridad = JOptionPane.showInputDialog(this, "Nueva prioridad:", tarea.getPrioridad());
            if (nuevaPrioridad == null) return;  
            if (nuevaPrioridad.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La prioridad no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!nuevaPrioridad.equalsIgnoreCase("Alta") && !nuevaPrioridad.equalsIgnoreCase("Media") && !nuevaPrioridad.equalsIgnoreCase("Baja")) {
                JOptionPane.showMessageDialog(this, "La prioridad debe ser 'Alta', 'Media' o 'Baja'.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;  
            }
        }
   
        String nuevaFecha = "";
        while (true) {
            nuevaFecha = JOptionPane.showInputDialog(this, "Nueva fecha límite (YYYY-MM-DD):", tarea.getFechaLimite().toString());
            if (nuevaFecha == null) return; 
            if (nuevaFecha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    LocalDate fechaLimite = LocalDate.parse(nuevaFecha);  
                    break; 
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
 
        try {
            LocalDate fechaLimite = LocalDate.parse(nuevaFecha);
            gestor.editarTarea(fila, nuevoTitulo, nuevaDescripcion, fechaLimite, nuevaPrioridad);
            actualizarTabla();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de prioridad", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTareaSeleccionada() {
        int fila = tablaTareas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar esta tarea?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Tarea tarea = gestor.obtenerTareas().get(fila);
            gestor.eliminarTarea(tarea);
            actualizarTabla();
        }
    }

    private void cambiarEstadoTareaSeleccionada() {
        int fila = tablaTareas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para cambiar el estado.");
            return;
        }

        Tarea tarea = gestor.obtenerTareas().get(fila);
        boolean nuevoEstado = !tarea.isCompletada();
        gestor.marcarComoCompletada(fila, nuevoEstado);
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Tarea> lista = gestor.obtenerTareas();
        for (Tarea t : lista) {
            modeloTabla.addRow(new Object[]{t.getTitulo(), t.getDescripcion(), t.getPrioridad(), t.getFechaLimite(), t.isCompletada() ? "Completada" : "Pendiente"
            });
        }
    }
    

    // Filtrar por prioridad
    private void filtrarPorPrioridad() {
    	// Esto si desean lo cambiamos revisen 
        String prioridad = JOptionPane.showInputDialog(this, "Filtrar por prioridad (Alta, Media, Baja) o escribe 'todas' para ver todas:");
        if (prioridad == null) return;

        prioridad = prioridad.trim();

        if (prioridad.isEmpty() || prioridad.equalsIgnoreCase("todas")) {
            gestor.restablecerTareas(); // Mostrar todas las tareas
        } else {
            gestor.filtrarPorPrioridad(prioridad);
        }

        actualizarTabla();
    }

    // Ordenar tareas por fecha
    private void ordenarPorFecha() {
        gestor.ordenarPorFechaLimite();
        actualizarTabla();
    }

    // Ordenar tareas por prioridad
    private void ordenarPorPrioridad() {
        gestor.ordenarPorPrioridad();
        actualizarTabla();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaPrincipal().setVisible(true));
    }
}
