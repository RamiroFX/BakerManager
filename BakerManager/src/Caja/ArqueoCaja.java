/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Entities.ArqueoCajaDetalle;
import Entities.Moneda;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Ramiro Ferreira
 */
public class ArqueoCaja extends JDialog implements ActionListener, MouseListener, KeyListener {

    private JButton jbAceptar, jbCancelar, jbAgregar, jbQuitar;
    private JLabel jlTotalEfectivo;
    private JFormattedTextField jftTotalEfectivo;
    private JTable jtEfectivo;
    private JScrollPane jspEfectivo;

    public ArqueoCaja(JFrame jframe) {
        super(jframe, "Arqueo de caja", true);
        initializeVariables();
        constructLayout();
        addListeners();
        initializeLogic();
        constructWindows(jframe);
    }

    private void initializeVariables() {
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jbAgregar = new JButton("Agregar moneda");
        jbQuitar = new JButton("Quitar moneda");
        jlTotalEfectivo = new JLabel("Total efectivo");
        jftTotalEfectivo = new JFormattedTextField();
        jtEfectivo = new JTable();
        jspEfectivo = new JScrollPane(jtEfectivo);
    }

    private void constructLayout() {
        JPanel jpEastButtons = new JPanel(new GridLayout(2, 1));
        jpEastButtons.setBorder(new EtchedBorder());
        jpEastButtons.add(jbAgregar);
        jpEastButtons.add(jbQuitar);
        JPanel jpEastComponents = new JPanel(new GridLayout(2, 1));
        jpEastComponents.setBorder(new EtchedBorder());
        jpEastComponents.add(jlTotalEfectivo);
        jpEastComponents.add(jftTotalEfectivo);
        JPanel jpEast = new JPanel();
        jpEast.add(jpEastButtons);
        jpEast.add(jpEastComponents);
        JPanel jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jpSouth.add(jbAceptar);
        jpSouth.add(jbCancelar);

        getContentPane().add(jspEfectivo, BorderLayout.CENTER);
        getContentPane().add(jpEast, BorderLayout.EAST);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void addListeners() {
        jbAceptar.addActionListener(this);
        jbCancelar.addActionListener(this);
        jbAgregar.addActionListener(this);
        jbQuitar.addActionListener(this);
        jtEfectivo.addMouseListener(this);
        /*
        KEYLISTENER
         */
        jbAceptar.addKeyListener(this);
        jbCancelar.addKeyListener(this);
        jbAgregar.addKeyListener(this);
        jbQuitar.addKeyListener(this);
        jtEfectivo.addKeyListener(this);
    }

    private void initializeLogic() {

    }

    private void constructWindows(JFrame frame) {
        this.setPreferredSize(new Dimension(500, 360));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(frame);
    }

    
    public void recibirMoneda(ArqueoCajaDetalle arqueoDetalle) {

    }

    private void quitarMoneda() {

    }

    private void guardarArqueoCaja() {

    }

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(jbAceptar)) {
            guardarArqueoCaja();
        } else if (src.equals(jbCancelar)) {
            cerrar();
        } else if (src.equals(jbAgregar)) {
            SeleccionarMoneda seleccionarMoneda = new SeleccionarMoneda(this);
            seleccionarMoneda.setVisible(true);
        } else if (src.equals(jbQuitar)) {
            quitarMoneda();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if (src.equals(jtEfectivo)) {
            int row = jtEfectivo.getSelectedRow();
            int col = jtEfectivo.getSelectedColumn();
            if (row > -1 && col > -1) {
                jbQuitar.setEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
