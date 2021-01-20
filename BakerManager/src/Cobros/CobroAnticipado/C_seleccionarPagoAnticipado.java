/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Interface.RecibirCtaCteCabeceraCallback;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirFacturaSinPagoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarPagoAnticipado extends MouseAdapter implements ActionListener, KeyListener {

    public static final int TIPO_COBRO = 1, TIPO_DIRECTO = 2;
    private static final String ENTER_KEY = "Entrar";

    public M_seleccionarPagoAnticipado modelo;
    public V_seleccionarPagoAnticipado vista;
    RecibirCtaCteDetalleCallback callback;

    public C_seleccionarPagoAnticipado(M_seleccionarPagoAnticipado modelo, V_seleccionarPagoAnticipado vista, RecibirCtaCteDetalleCallback callback) {
        this.modelo = modelo;
        this.vista = vista;
        this.callback = callback;
        inicializarVista();
        agregarListeners();
        displayQueryResults();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtPagosAnticipados.setModel(modelo.getTM());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtPagosAnticipados.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtPagosAnticipados.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarAnticipo();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtPagosAnticipados, 1);
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        //this.vista.jbFuncionario.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtPagosAnticipados.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtPagosAnticipados.addKeyListener(this);
    }

    public void displayQueryResults() {
        /*
         * Para permitir que los mensajes puedan ser desplegados, no se ejecuta
         * el query directamente, sino que se lo coloca en una cola de eventos
         * para que se ejecute luego de los eventos pendientes.
         */

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String desc = vista.jtfBuscar.getText();
                if (desc.length() > 30) {
                    JOptionPane.showMessageDialog(vista, "El texto ingresado supera el máximo permitido de 30 caracteres.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                modelo.consultarAdelantos();
                Utilities.c_packColumn.packColumns(vista.jtPagosAnticipados, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        //this.vista.jtfFuncionario.setText("");
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        //this.vista.jcbCondVenta.setSelectedIndex(0);
    }

    private void seleccionarAnticipo() {
        int fila = vista.jtPagosAnticipados.getSelectedRow();
        int columna = vista.jtPagosAnticipados.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            callback.recibirCtaCteDetalle(modelo.getTM().getList().get(fila), 0);
            cerrar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarAnticipo();
            this.vista.jtfBuscar.requestFocusInWindow();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.jtfBuscar.selectAll();
                }
            });
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBuscar) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbSalir) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtPagosAnticipados.rowAtPoint(e.getPoint());
        int columna = this.vista.jtPagosAnticipados.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarAnticipo();
                this.vista.jtfBuscar.requestFocusInWindow();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (this.vista.jtfBuscar.hasFocus()) {
            displayQueryResults();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.vista.jtfBuscar.hasFocus()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN: {
                    vista.jtPagosAnticipados.requestFocusInWindow();
                    break;
                }
                case KeyEvent.VK_ESCAPE: {
                    cerrar();
                    break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
