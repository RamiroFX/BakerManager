/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Ramiro
 */
public class V_gestion_rol extends JDialog {

    private JPanel jpRoles, jpPermisos;
    private JScrollPane jspRoles, jspPermisos;
    public JTable jtRoles, jtPermisos;
    public JButton jbCrearRol, jbModificarRol, jbEliminarRol;

    public V_gestion_rol(JFrame frame) {
        super(frame);
        setTitle("Gestión de roles");
        setName("roles");
        setSize(600, 400);
        setLocationRelativeTo(frame);
        iniciarlizarVista();
    }

    public V_gestion_rol(JDialog dialog) {
        super(dialog);
        setTitle("Gestión de roles");
        setName("roles");
        setSize(600, 400);
        setLocationRelativeTo(dialog);
        iniciarlizarVista();
    }

    private void iniciarlizarVista() {
        this.jbCrearRol = new JButton("Crear rol");
        this.jbModificarRol = new JButton("Modificar rol");
        this.jbEliminarRol = new JButton("Eliminar rol");

        this.jtRoles = new JTable();
        this.jtRoles.getTableHeader().setReorderingAllowed(false);
        this.jtPermisos = new JTable();
        this.jtPermisos.getTableHeader().setReorderingAllowed(false);
        this.jspRoles = new JScrollPane(jtRoles);
        this.jspPermisos = new JScrollPane(jtPermisos);

        this.jpRoles = new JPanel(new BorderLayout());
        this.jpRoles.setBorder(new TitledBorder("Roles"));
        this.jpPermisos = new JPanel(new BorderLayout());
        this.jpPermisos.setBorder(new TitledBorder("Permisos"));

        this.jpRoles.add(jspRoles);
        this.jpPermisos.add(jspPermisos);

        JPanel jpBotones = new JPanel();
        jpBotones.add(this.jbCrearRol);
        jpBotones.add(this.jbModificarRol);
        jpBotones.add(this.jbEliminarRol);

        JPanel jpTablas = new JPanel(new GridLayout(1, 2));
        jpTablas.add(this.jpRoles);
        jpTablas.add(this.jpPermisos);
        getContentPane().add(jpTablas, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

}
