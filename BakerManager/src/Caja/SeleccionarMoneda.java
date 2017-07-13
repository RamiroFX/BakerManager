/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import Entities.ArqueoCajaDetalle;
import Entities.Moneda;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarMoneda extends JDialog implements ActionListener, KeyListener {

    private JButton jbAceptar, jbCancelar;
    private JLabel jlMoneda, jlCantidad;
    private JTextField jtfCantidad;
    private JComboBox<Moneda> jcbMonedas;
    private ArqueoCaja arqueoCaja;

    public SeleccionarMoneda(ArqueoCaja arqueoCaja) {
        super(arqueoCaja, "Agregar moneda", true);
        this.arqueoCaja = arqueoCaja;
        initializeVariables();
        constructLayout();
        constructWindows(arqueoCaja);
        initializeLogic();
        addListeners();
    }

    private void initializeVariables() {
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jlMoneda = new JLabel("Moneda");
        jlMoneda.setHorizontalAlignment(SwingConstants.CENTER);
        jlCantidad = new JLabel("Cantidad");
        jlCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        jtfCantidad = new JTextField();
        jcbMonedas = new JComboBox<>();
    }

    private void constructLayout() {
        JPanel jpCenter = new JPanel(new GridLayout(2, 2));
        jpCenter.add(jlMoneda);
        jpCenter.add(jcbMonedas);
        jpCenter.add(jlCantidad);
        jpCenter.add(jtfCantidad);
        JPanel jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);

        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void constructWindows(ArqueoCaja arqueoCaja) {
        setSize(280, 180);
        setLocationRelativeTo(arqueoCaja);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initializeLogic() {
        ArrayList<Moneda> monedas = DB_Caja.obtenerMonedas();
        for (Moneda moneda : monedas) {
            this.jcbMonedas.addItem(moneda);
        }
    }

    private void cerrar() {
        this.dispose();
    }

    private void enviarMonedas() {
        if (checkearCantidad()) {
            int cantidad = Integer.valueOf(String.valueOf(this.jtfCantidad.getText().trim()));
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(cantidad);
            Moneda moneda = (Moneda) jcbMonedas.getSelectedItem();
            acd.getMoneda().setIdMoneda(moneda.getIdMoneda());
            acd.getMoneda().setDescripcion(moneda.getDescripcion());
            acd.getMoneda().setValor(moneda.getValor());
            arqueoCaja.recibirMoneda(acd);
            this.jtfCantidad.setText("");
            this.jcbMonedas.requestFocusInWindow();
        }
    }

    private void addListeners() {
        jbAceptar.addActionListener(this);
        jbCancelar.addActionListener(this);
        jtfCantidad.addKeyListener(this);
        jcbMonedas.addKeyListener(this);
    }

    private boolean checkearCantidad() {
        Integer cantidad = null;
        if (this.jtfCantidad.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidad.setText("0");
            this.jtfCantidad.requestFocusInWindow();
            return false;
        }
        try {
            cantidad = Integer.valueOf(String.valueOf(this.jtfCantidad.getText().trim()));
            if (cantidad <= 0.0) {
                JOptionPane.showMessageDialog(this, "Inserte un valor mayor a 0 en Cantidad.", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidad.setText("0");
            this.jtfCantidad.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(jbAceptar)) {
            enviarMonedas();
        } else if (src.equals(jbCancelar)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
            case KeyEvent.VK_ENTER: {
                enviarMonedas();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
