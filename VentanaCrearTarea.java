package ModuloTareas;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class VentanaCrearTarea extends JDialog {
	private static final long serialVersionUID = 1L;
    private JTextField campoTitulo;
    private JTextField campoDescripcion;
    private JComboBox<String> comboPrioridad;
    private JTextField campoFecha;
    private boolean tareaCreada = false;
    private Tarea nuevaTarea;

    public VentanaCrearTarea(JFrame padre) {
        super(padre, "Crear Tarea", true);
        setSize(400, 300);
        setLocationRelativeTo(padre);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        add(campoTitulo);

        add(new JLabel("Descripción:"));
        campoDescripcion = new JTextField();
        add(campoDescripcion);

        add(new JLabel("Prioridad:"));
        comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        add(comboPrioridad);

        add(new JLabel("Fecha límite (YYYY-MM-DD):"));
        campoFecha = new JTextField();
        add(campoFecha);

        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(e -> {
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
            String prioridad = (String) comboPrioridad.getSelectedItem();
            while (prioridad == null || (!prioridad.equals("Alta") && !prioridad.equals("Media") && !prioridad.equals("Baja"))) {
                JOptionPane.showMessageDialog(this, "La prioridad debe ser 'Alta', 'Media' o 'Baja'.", "Error", JOptionPane.ERROR_MESSAGE);
                prioridad = (String) JOptionPane.showInputDialog(this, "Nueva prioridad (Alta, Media, Baja):", "Prioridad", JOptionPane.PLAIN_MESSAGE);
                if (prioridad == null) return;  
            }

            String fecha = campoFecha.getText();
            while (fecha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                fecha = JOptionPane.showInputDialog(this, "Fecha límite (YYYY-MM-DD):");
                if (fecha == null) return;  
            }

            try {
                LocalDate fechaLimite = LocalDate.parse(fecha);  
                nuevaTarea = new Tarea(campoTitulo.getText(), campoDescripcion.getText(), fechaLimite, prioridad);
                tareaCreada = true;
                dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnCrear);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    public boolean seCreoTarea() {
        return tareaCreada;
    }

    public Tarea obtenerTarea() {
        return nuevaTarea;
    }
}

