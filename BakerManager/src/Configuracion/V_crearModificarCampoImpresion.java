/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Egresos.C_crear_egreso;
import Entities.M_producto;
import Producto.C_seleccionarProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearModificarCampoImpresion extends javax.swing.JDialog implements ActionListener, KeyListener {

    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlCoordenadaX, jlCoordenadaY, jlCampo;
    private javax.swing.JTextField jtfCoordenadaX, jtfCoordenadaY, jtfCampo;
    int row;
    int tipo;

    public V_crearModificarCampoImpresion(JFrame parent) {
        super(parent, true);
        inicializarVista(parent);
        initComponents();
    }

    private void inicializarVista(JFrame parent) {
        setTitle("Crear parametro");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlCoordenadaX = new javax.swing.JLabel("Coordenada X");
        jlCoordenadaY = new javax.swing.JLabel("Coordenada Y");
        jlCampo = new javax.swing.JLabel("Parámetro");
        jtfCoordenadaY = new javax.swing.JTextField("0.00");
        jtfCoordenadaX = new javax.swing.JTextField("0.00");
        jtfCampo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        jtfCoordenadaX.addKeyListener(this);
        jtfCoordenadaY.addKeyListener(this);
        jtfCampo.addKeyListener(this);
        getContentPane().add(jlCoordenadaX);
        getContentPane().add(jtfCoordenadaX, "width :200:,grow,wrap");
        getContentPane().add(jlCoordenadaY);
        getContentPane().add(jtfCoordenadaY, "width :200:,grow,wrap");
        getContentPane().add(jlCampo);
        getContentPane().add(jtfCampo, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfCoordenadaX.selectAll();
    }

    public void enviarCantidad() {

    }

    private boolean checkearCantidad() {

        return true;
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
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
