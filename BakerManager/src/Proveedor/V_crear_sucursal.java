/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Usuario
 */
public class V_crear_sucursal extends JDialog {

    C_crear_proveedor c_crear_proveedor;
    JButton jbAceptar, jbCancelar;
    JLabel jlDireccion, jlTelefono;
    JTextField jtfDireccion, jtfTelefono;

    public V_crear_sucursal(JDialog jdialog) {
        super(jdialog, "Agregar sucursal", DEFAULT_MODALITY_TYPE);
        setPreferredSize(new java.awt.Dimension(400, 120));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(false);
        setName("jdCrearSucursal");
        setLocationRelativeTo(jdialog);
        setResizable(false);
        initComponents();
        pack();
    }

    private void initComponents() {
        this.jlDireccion = new JLabel("Dirección(*)");
        this.jtfDireccion = new JTextField();
        this.jlTelefono = new JLabel("Nro. de telefono");
        this.jtfTelefono = new JTextField();
        this.jbAceptar = new JButton("Aceptar");
        this.jbCancelar = new JButton("Cancelar");
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jlDireccion);
        getContentPane().add(jtfDireccion, "width :300:,growx,wrap");
        getContentPane().add(jlTelefono);
        getContentPane().add(jtfTelefono, "width :300:,growx,wrap");
        getContentPane().add(jbAceptar);
        getContentPane().add(jbCancelar);
    }
}
