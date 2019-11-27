/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Facturacion.V_facturacion;
import DB.DB_Ingreso;
import Entities.E_facturacionCabecera;
import Interface.InterfaceConfirmarFacturacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class ConfirmarFacturacion extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final String TITULO = "Confirmar facturación",
            S_ALERTA = "¿Está seguro que desea continuar? Accion irreversible.",
            S_NRO_FACTURA = "Nro. Factura:",
            S_CLIENTE = "Cliente:",
            S_COND_VENTA = "Condición de venta:";
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlCliente, jlCondVenta, jlNroFactura, jtfMensaje;
    private javax.swing.JTextField jtfCliente, jtfCondVenta, jtfNroFactura;

    private InterfaceConfirmarFacturacion interfaceConfirmarFacturacion;

    public ConfirmarFacturacion(V_facturacion vista) {
        super(vista, true);
        setTitle(TITULO);
        setSize(new java.awt.Dimension(350, 220));
        setLocationRelativeTo(vista);
        initComponents();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void inicializarVista(String cliente, String condVenta) {
        jtfCliente.setText(cliente);
        jtfCondVenta.setText(condVenta);
        jtfNroFactura.setText(getNroFactura() + "");
    }

    public void setInterfaceConfirmarFacturacion(InterfaceConfirmarFacturacion interfaceConfirmarFacturacion) {
        this.interfaceConfirmarFacturacion = interfaceConfirmarFacturacion;
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jtfMensaje = new javax.swing.JLabel(S_ALERTA);
        jlCliente = new javax.swing.JLabel(S_CLIENTE);
        jlCondVenta = new javax.swing.JLabel(S_COND_VENTA);
        jlNroFactura = new javax.swing.JLabel(S_NRO_FACTURA);
        jtfCliente = new javax.swing.JTextField();
        jtfCliente.setEditable(false);
        jtfCondVenta = new javax.swing.JTextField();
        jtfCondVenta.setEditable(false);
        jtfNroFactura = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        jtfCliente.addKeyListener(this);
        jtfCondVenta.addKeyListener(this);
        jtfMensaje.addKeyListener(this);
        jtfNroFactura.addKeyListener(this);
        getContentPane().add(jtfMensaje, "span,grow,wrap");
        getContentPane().add(jlCliente);
        getContentPane().add(jtfCliente, "width :200:,grow,wrap");
        getContentPane().add(jlCondVenta);
        getContentPane().add(jtfCondVenta, "width :200:,grow,wrap");
        getContentPane().add(jlNroFactura);
        getContentPane().add(jtfNroFactura, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfCliente.selectAll();
    }

    public void enviarCantidad() {
        if (!checkearNroFactura()) {
            return;
        }
        int nroFactura = 0;
        try {
            nroFactura = Integer.valueOf(String.valueOf(jtfNroFactura.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Inserte un valor válido para Nro. Factura", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        E_facturacionCabecera e_facturacionCabecera = new E_facturacionCabecera();
        e_facturacionCabecera.setNroFactura(nroFactura);
        interfaceConfirmarFacturacion.recibirFacturacion(e_facturacionCabecera);
        dispose();
    }

    private boolean checkearNroFactura() {
        Integer nroFactura = null;
        if (this.jtfNroFactura.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro: "
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Nro. factura.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroFactura.setText(getNroFactura() + "");
            return false;
        }
        try {
            String cantidad = this.jtfNroFactura.getText();
            nroFactura = Integer.valueOf(cantidad);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Nro. factura.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroFactura.setText(getNroFactura() + "");
            return false;
        }
        if (!nroFacturaEnUso(nroFactura)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "El número de factura introducido ya\n"
                    + "se encuentra en uso.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroFactura.setText(getNroFactura() + "");
            return false;
        }
        return true;
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public int getNroFactura() {
        int nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        int nroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion() + 1;
        if (nroFactura >= nroFacturacion) {
            return nroFactura;
        } else {
            return nroFacturacion;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbOK)) {
            enviarCantidad();
        } else if (e.getSource().equals(jbCancel)) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (jtfCliente.hasFocus() || jtfCondVenta.hasFocus() || jtfMensaje.hasFocus() || jtfNroFactura.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarCantidad();
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

}
