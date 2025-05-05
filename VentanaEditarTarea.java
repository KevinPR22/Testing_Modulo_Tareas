package ModuloTareas;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class VentanaEditarTarea extends JDialog {
	private static final long serialVersionUID = 1L;
    private JTextField campoTitulo;
    private JTextField campoDescripcion;
    private JComboBox<String> comboPrioridad;
    private JTextField campoFecha;
    private boolean tareaEditada = false;

    private Tarea tarea;

    public VentanaEditarTarea(JFrame padre, Tarea tareaExistente) {
        super(padre, "Editar Tarea", true);
        this.tarea = tareaExistente;
        setSize(400, 300);
        setLocationRelativeTo(padre);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Título:"));
        campoTitulo = new JTextField(tarea.getTitulo());
        add(campoTitulo);

        add(new JLabel("Descripción:"));
        campoDescripcion = new JTextField(tarea.getDescripcion());
        add(campoDescripcion);

        add(new JLabel("Prioridad:"));
        comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        comboPrioridad.setSelectedItem(tarea.getPrioridad());
        add(comboPrioridad);

        add(new JLabel("Fecha límite (YYYY-MM-DD):"));
        campoFecha = new JTextField(tarea.getFechaLimite().toString());
        add(campoFecha);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            while (campoTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                campoTitulo.requestFocus(); 
                return; 
            }
            while (campoDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                campoDescripcion.requestFocus(); 
                return; 
            }
            String nuevaPrioridad = (String) comboPrioridad.getSelectedItem();
            while (nuevaPrioridad == null || (!nuevaPrioridad.equals("Alta") && !nuevaPrioridad.equals("Media") && !nuevaPrioridad.equals("Baja"))) {
                JOptionPane.showMessageDialog(this, "La prioridad debe ser 'Alta', 'Media' o 'Baja'.", "Error", JOptionPane.ERROR_MESSAGE);
                nuevaPrioridad = (String) JOptionPane.showInputDialog(this, "Nueva prioridad (Alta, Media, Baja):", tarea.getPrioridad());
                if (nuevaPrioridad == null) return; 
            }

            String nuevaFecha = campoFecha.getText();
            while (nuevaFecha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                nuevaFecha = JOptionPane.showInputDialog(this, "Nueva fecha límite (YYYY-MM-DD):", tarea.getFechaLimite().toString());
                if (nuevaFecha == null) return;
            }
            try {
                LocalDate fechaLimite = LocalDate.parse(nuevaFecha);
                tarea.setTitulo(campoTitulo.getText());
                tarea.setDescripcion(campoDescripcion.getText());
                tarea.setPrioridad(nuevaPrioridad);
                tarea.setFechaLimite(fechaLimite);
                tareaEditada = true;
                dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.");
            }
        });
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    public boolean seEditoTarea() {
        return tareaEditada;
    }

    public Tarea obtenerTareaEditada() {
        return tarea;
    }
}

