/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.EstadoCuenta;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_estadoCuenta implements ActionListener, KeyListener {

    private M_estadoCuenta modelo;
    private V_estadoCuenta vista;

    public C_estadoCuenta(M_estadoCuenta modelo, V_estadoCuenta vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListener();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtCobros.setModel(modelo.obtenerTableModel());
        Utilities.c_packColumn.packColumns(this.vista.jtCobros, 1);
        BigInteger total = new BigInteger("0");
        BigInteger totalCheque = new BigInteger("0");
        BigInteger totalEfectivo = new BigInteger("0");
        total = total.add(totalCheque).add(totalEfectivo);
        this.vista.jftTotalEfectivo.setValue(totalEfectivo);
        this.vista.jftTotalCheque.setValue(totalCheque);
        this.vista.jftTotalCobrado.setValue(total);
    }

    private void agregarListener() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImportarXLS.addActionListener(this);
    }

    private void importarExcelIndividual() {
    }

    private void importarExcelResumido() {
    }

    private void exportHandler() {
        Object[] options = {"Individual",
            "Resumido"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga tipo de reporte",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                //Individual
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelIndividual();
                    }
                });
                break;
            }
            case 1: {
                //Resumido
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelResumido();
                    }
                });
                break;
            }
        }
    }

    private void keyPressedHandler(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: {
                        cerrar();
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (ae.getSource().equals(this.vista.jbImportarXLS)) {
            exportHandler();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedHandler(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
