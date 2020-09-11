/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import DB.DB_manager;
import Entities.E_clienteproducto;
import Entities.E_impuesto;
import Entities.M_cliente;
import Interface.RecibirClienteProductoPreferenciaCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarPreferenciaProducto extends javax.swing.JDialog implements ActionListener, KeyListener {

    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlImpuesto, jlPrecio;
    private javax.swing.JTextField jtfPrecio;
    private javax.swing.JComboBox<E_impuesto> jcbImpuesto;
    int row;
    private M_cliente cliente;
    RecibirClienteProductoPreferenciaCallback callback;

    public SeleccionarPreferenciaProducto(JDialog dialog) {
        super(dialog, true);
        setTitle("Preferencia de cliente");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(dialog);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.row = -1;
        initComponents();
        inicializarVista();
        agregarListeners();
    }

    public void setCallback(RecibirClienteProductoPreferenciaCallback callback) {
        this.callback = callback;
    }

    private void inicializarVista() {
        for (E_impuesto impuesto : DB_manager.obtenerImpuestos()) {
            this.jcbImpuesto.addItem(impuesto);
        }
    }

    private void agregarListeners() {
        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);
        jtfPrecio.addKeyListener(this);
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton("OK");
        jbCancel = new javax.swing.JButton("Cancel");
        jlImpuesto = new javax.swing.JLabel("Impuesto");
        jlPrecio = new javax.swing.JLabel("Precio");
        jtfPrecio = new javax.swing.JTextField();
        jcbImpuesto = new javax.swing.JComboBox<>();
        getContentPane().add(jlPrecio);
        getContentPane().add(jtfPrecio, "width :200:,grow,wrap");
        getContentPane().add(jlImpuesto);
        getContentPane().add(jcbImpuesto, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void enviarPreferencia() {
        if (!validarPrecio()) {
            return;
        }
        int precio = Integer.valueOf(jtfPrecio.getText().trim());
        E_clienteproducto cp = new E_clienteproducto();
        cp.setCliente(cliente);
        cp.setPrecio(precio);
        cp.setImpuesto(jcbImpuesto.getItemAt(jcbImpuesto.getSelectedIndex()));
        callback.recibirClienteProducto(cp);
        dispose();
    }

    private boolean validarPrecio() {
        int precio = -1;
        if (this.jtfPrecio.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfPrecio.requestFocusInWindow();
            return false;
        }
        try {
            precio = Integer.valueOf(String.valueOf(this.jtfPrecio.getText().trim()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfPrecio.requestFocusInWindow();
            return false;
        }
        if (precio < 1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Precio.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfPrecio.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbOK)) {
            enviarPreferencia();
        } else if (e.getSource().equals(jbCancel)) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (jtfPrecio.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarPreferencia();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                this.dispose();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    public void cargarDatos(int precio, E_impuesto impuesto, M_cliente cliente) {
        this.cliente = cliente;
        this.jtfPrecio.setText(precio + "");
        this.jcbImpuesto.setSelectedItem(impuesto);
    }
}
