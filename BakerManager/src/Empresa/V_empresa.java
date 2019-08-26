/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class V_empresa extends JDialog {

    public javax.swing.JTextField jtfEntidad, jtfNombre, jtfRUC,
            jtfObservacion, jtfDireccion, jtfPaginaWeb, jtfEmail, jtfTelefono;
    private javax.swing.JLabel jlEntidad, jlNombre, jlRUC, jlObservacion, jlDireccion,
            jlTelefono, jlPaginaWeb, jlEmail;
    private javax.swing.JPanel jpDatosEmpresariales, jpSouth;
    public javax.swing.JButton jbGuardar, jbSalir;

    public V_empresa(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Datos de la empresa");
        setAlwaysOnTop(false);
        setName("datos_empresa");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);
        initNorthPanel();
        initSouthPanel();
        buildGui();
    }

    private void initNorthPanel() {
        jlEntidad = new javax.swing.JLabel();
        jlEntidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEntidad.setText("Entidad");
        jtfEntidad = new javax.swing.JTextField();
        jlNombre = new javax.swing.JLabel();
        jlNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNombre.setText("Nombre de fantasía");
        jtfNombre = new javax.swing.JTextField();
        jlRUC = new javax.swing.JLabel();
        jlRUC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRUC.setText("R.U.C.");
        jtfRUC = new javax.swing.JTextField();
        jlObservacion = new javax.swing.JLabel();
        jlObservacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlObservacion.setText("Descripción");
        jtfObservacion = new javax.swing.JTextField();
        jlDireccion = new javax.swing.JLabel();
        jlDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDireccion.setText("Direccion");
        jtfDireccion = new javax.swing.JTextField();
        jlPaginaWeb = new javax.swing.JLabel();
        jlPaginaWeb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPaginaWeb.setText("Página web");
        jtfPaginaWeb = new javax.swing.JTextField();
        jlEmail = new javax.swing.JLabel();
        jlEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEmail.setText("Correo electrónico");
        jtfEmail = new javax.swing.JTextField();
        jlTelefono = new javax.swing.JLabel();
        jlTelefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTelefono.setText("Telefono");
        jtfTelefono = new javax.swing.JTextField();

        jpDatosEmpresariales = new javax.swing.JPanel(new MigLayout());
        jpDatosEmpresariales.add(jlEntidad);
        jpDatosEmpresariales.add(jtfEntidad, "growx, push, wrap");
        jpDatosEmpresariales.add(jlNombre);
        jpDatosEmpresariales.add(jtfNombre, "growx, push, wrap");
        jpDatosEmpresariales.add(jlRUC);
        jpDatosEmpresariales.add(jtfRUC, "growx, push, wrap");
        jpDatosEmpresariales.add(jlDireccion);
        jpDatosEmpresariales.add(jtfDireccion, "growx, push, wrap");
        jpDatosEmpresariales.add(jlPaginaWeb);
        jpDatosEmpresariales.add(jtfPaginaWeb, "growx, push, wrap");
        jpDatosEmpresariales.add(jlEmail);
        jpDatosEmpresariales.add(jtfEmail, "growx, push, wrap");
        jpDatosEmpresariales.add(jlObservacion);
        jpDatosEmpresariales.add(jtfObservacion, "growx, push, wrap");
        jpDatosEmpresariales.add(jlTelefono);
        jpDatosEmpresariales.add(jtfTelefono, "growx, push");
    }

    private void initSouthPanel() {
        jpSouth = new JPanel();
        jbGuardar = new JButton("Guardar");
        jbSalir = new JButton("Salir");
        jpSouth.add(jbGuardar);
        jpSouth.add(jbSalir);
    }

    private void buildGui() {
        getContentPane().add(jpDatosEmpresariales, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }
}
