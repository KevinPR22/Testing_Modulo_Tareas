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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                gestor.guardarTareas();
                System.exit(0);
            }
        });
        setLocationRelativeTo(null);
        inicializarComponentes();
        actualizarTabla(); 
    }


    private void inicializarComponentes() {
        setLayout(new BorderLayout());

       
        modeloTabla = new DefaultTableModel(new Object[]{"Título", "Descripción", "Prioridad", "Fecha Límite", "Estado"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

   

        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, BorderLayout.CENTER);
        // Panel de botones        
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10)); 

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
            JTextField campoTitulo = new JTextField();
            campoTitulo.setDocument(new JTextFieldLimit(20));
            int opcionTitulo = JOptionPane.showConfirmDialog(this, campoTitulo, "Título de la tarea", JOptionPane.OK_CANCEL_OPTION);
            if (opcionTitulo != JOptionPane.OK_OPTION) return;
            titulo = campoTitulo.getText();
            if (titulo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String descripcion = "";
        while (descripcion.trim().isEmpty()) {
            JTextField campoDescripcion = new JTextField();
            campoDescripcion.setDocument(new JTextFieldLimit(50));
            int opcionDescripcion = JOptionPane.showConfirmDialog(this, campoDescripcion, "Descripción de la tarea", JOptionPane.OK_CANCEL_OPTION);
            if (opcionDescripcion != JOptionPane.OK_OPTION) return;
            descripcion = campoDescripcion.getText();
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
    
        String dia = "";
        String mes = "";
        String ano = "";
        String fecha = "";
        while (true) {
            
            JPanel panel = new JPanel();
            JTextField campoDia = new JTextField(02);
            JTextField campoMes = new JTextField(02);
            JTextField campoAno = new JTextField(04);
            panel.add(new JLabel("Día:"));
            panel.add(campoDia);
            panel.add(new JLabel("Mes:"));
            panel.add(campoMes);
            panel.add(new JLabel("Año:"));
            panel.add(campoAno);
            
            int opcion = JOptionPane.showConfirmDialog(null, panel, "Introduce la fecha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (opcion == JOptionPane.CANCEL_OPTION){
                return;
            }
            
            dia = campoDia.getText();
            mes = campoMes.getText();
            ano = campoAno.getText();
            
            if (dia == null || mes == null || ano == null) return;  
            if (dia.trim().isEmpty() || mes.trim().isEmpty() || ano.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else  {
            	try {
            	    if(dia.length() == 1){
            	        dia = "0" + dia;
            	    }

            	    if(mes.length() == 1){
            	        mes = "0" + mes;
            	    }

            	    fecha = ano + "-" + mes + "-" + dia;
            	    LocalDate.parse(fecha);  
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
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa DD-MM-YYYY.");
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
                	LocalDate.parse(nuevaFecha);  
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
        String prioridad = JOptionPane.showInputDialog(this, 
            "Filtrar por prioridad (Alta, Media, Baja) o escribe 'todas' para ver todas:");

        if (prioridad == null) return;

        prioridad = prioridad.trim();

        if (prioridad.isEmpty() || prioridad.equalsIgnoreCase("todas")) {
            gestor.restablecerTareas(); // Mostrar todas las tareas
        } else if (!prioridad.equalsIgnoreCase("Alta") &&
                !prioridad.equalsIgnoreCase("Media") &&
                !prioridad.equalsIgnoreCase("Baja")) {
            JOptionPane.showMessageDialog(this, 
                "Prioridad inválida. Solo se permite 'Alta', 'Media' o 'Baja'.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
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

class JTextFieldLimit extends javax.swing.text.PlainDocument {
    private int limite;

    public JTextFieldLimit(int limite) {
        this.limite = limite;
    }

    @Override
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        if (str == null) return;
        if ((getLength() + str.length()) <= limite) {
            super.insertString(offset, str, attr);
        }
    }
}


