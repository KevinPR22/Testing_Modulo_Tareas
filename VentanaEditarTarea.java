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
            try {
                LocalDate fecha = LocalDate.parse(campoFecha.getText());
                tarea.setTitulo(campoTitulo.getText());
                tarea.setDescripcion(campoDescripcion.getText());
                tarea.setPrioridad(comboPrioridad.getSelectedItem().toString());
                tarea.setFechaLimite(fecha);
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
