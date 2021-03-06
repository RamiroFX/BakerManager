/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Ramiro
 */
public class V_crear_rol extends JDialog {

    private JLabel jlNombreRol;
    public JTextField jtfNombreRol;
    private JPanel jpPermisosSeleccionados, jpPermisosDisponibles;
    private JScrollPane jspPermisosSeleccionados, jspPermisosDisponibles;
    public JTable jtPermisosSeleccionados, jtPermisosDisponibles;
    public JButton jbQuitar, jbAgregar, jbAceptar, jbCancelar;

    public V_crear_rol(JFrame frame) {
        super(frame, "Crear rol", DEFAULT_MODALITY_TYPE);
        setName("crearRol");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(frame);
        inicializarComponentes();
    }

    public V_crear_rol(JDialog dialog) {
        super(dialog, "Crear rol", DEFAULT_MODALITY_TYPE);
        setName("crearRol");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(dialog);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        jlNombreRol = new JLabel("Nombre de rol");
        jtfNombreRol = new JTextField();
        jpPermisosSeleccionados = new JPanel(new BorderLayout());
        jpPermisosSeleccionados.setBorder(new TitledBorder("Accesos seleccionados"));
        jpPermisosDisponibles = new JPanel(new BorderLayout());
        jpPermisosDisponibles.setBorder(new TitledBorder("Accesos disponibles"));
        jtPermisosSeleccionados = new JTable();
        jtPermisosSeleccionados.getTableHeader().setReorderingAllowed(false);
        jtPermisosDisponibles = new JTable();
        jtPermisosDisponibles.getTableHeader().setReorderingAllowed(false);
        jspPermisosSeleccionados = new JScrollPane(jtPermisosSeleccionados);
        jspPermisosDisponibles = new JScrollPane(jtPermisosDisponibles);
        jbQuitar = new JButton("Quitar");
        jbAgregar = new JButton("Agregar");
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");

        JPanel jpSouth = new JPanel();
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);

        jpPermisosDisponibles.add(jspPermisosDisponibles, BorderLayout.CENTER);
        jpPermisosDisponibles.add(jbAgregar, BorderLayout.SOUTH);
        jpPermisosSeleccionados.add(jspPermisosSeleccionados, BorderLayout.CENTER);
        jpPermisosSeleccionados.add(jbQuitar, BorderLayout.SOUTH);

        JPanel jpCenter = new JPanel(new GridLayout(1, 2));
        jpCenter.add(jpPermisosSeleccionados);
        jpCenter.add(jpPermisosDisponibles);

        JPanel jpNorth = new JPanel(new BorderLayout());
        jpNorth.add(jlNombreRol, BorderLayout.WEST);
        jpNorth.add(jtfNombreRol, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }
}
