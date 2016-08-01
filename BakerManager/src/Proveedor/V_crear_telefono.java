/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Usuario
 */
public class V_crear_telefono extends JDialog {

    C_crear_proveedor c_crear_proveedor;
    JButton jbAceptar, jbCancelar;
    JLabel jlTipoTelefono, jlTelefono, jlObservacion;
    JTextField jtfTelefono, jtfObservacion;
    JComboBox jcbCategoria;

    public V_crear_telefono(JDialog jdialog) {
        super(jdialog, "Agregar telefono", DEFAULT_MODALITY_TYPE);
        setPreferredSize(new java.awt.Dimension(400, 160));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(false);
        setName("jdCrearTelefono");
        setLocationRelativeTo(jdialog);
        setResizable(false);
        initComponents();
        pack();
    }

    private void initComponents() {
        this.jlTipoTelefono = new JLabel("Tipo telefono");
        this.jcbCategoria = new JComboBox();
        this.jlTelefono = new JLabel("Nro. de telefono");
        this.jtfTelefono = new JTextField();
        this.jlObservacion = new JLabel("Observación");
        this.jtfObservacion = new JTextField();
        this.jbAceptar = new JButton("Aceptar");
        this.jbCancelar = new JButton("Cancelar");
        getContentPane().setLayout(new MigLayout());
        getContentPane().add(jlTelefono);
        getContentPane().add(jtfTelefono, "width :300:,growx,wrap");
        getContentPane().add(jlTipoTelefono);
        getContentPane().add(jcbCategoria, "width :300:,growx,wrap");
        getContentPane().add(jlObservacion);
        getContentPane().add(jtfObservacion, "width :300:,growx,wrap");
        getContentPane().add(jbAceptar);
        getContentPane().add(jbCancelar);
    }
}
