/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import Entities.Moneda;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarMoneda extends JDialog implements ActionListener {

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
        jlCantidad = new JLabel("Cantidad");
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
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);

        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void constructWindows(ArqueoCaja arqueoCaja) {
        setPreferredSize(new Dimension(250, 200));
        setLocationRelativeTo(arqueoCaja);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initializeLogic() {
        ArrayList<Moneda> monedas = DB_Caja.obtenerMonedas();
        for (Moneda moneda : monedas) {
            this.jcbMonedas.addItem(moneda);
        }
    }

    private void enviarMonedas() {
        
        //arqueoCaja.recibirMoneda(arqueoDetalle);
    }

    private void addListeners() {
        jbAceptar.addActionListener(this);
        jbCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
    }

}
