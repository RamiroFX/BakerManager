/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.buscarMateriaPrima;

import bauplast.desperdicio.buscarDesperdicio.*;
import Entities.E_produccionTipoBaja;
import bauplast.desperdicio.ResumenDesperdicioDetalle;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_buscarMateriaPrimaDetalle extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_buscarMateriaPrimaDetalle modelo;
    private V_buscarMateriaPrimaDetalle vista;

    public C_buscarMateriaPrimaDetalle(M_buscarMateriaPrimaDetalle modelo, V_buscarMateriaPrimaDetalle vista) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        displayQueryResults();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaInicio.setDate(calendarInicio.getTime());
        this.vista.jdcFechaFinal.setDate(calendar.getTime());
        this.vista.jtProducto.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtProducto.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);

        this.vista.jcbBuscarPor.addItem("Todos");
        this.vista.jcbBuscarPor.addItem("OT");
        this.vista.jcbBuscarPor.addItem("Producto");
        this.vista.jcbBuscarPor.addItem("Código");
        this.vista.jcbClasificarPor.addItem("Fecha");
        this.vista.jcbClasificarPor.addItem("OT");
        this.vista.jcbClasificarPor.addItem("Producto");
        this.vista.jcbClasificarPor.addItem("Código");
        this.vista.jcbOrdenarPor.addItem("Descendente");
        this.vista.jcbOrdenarPor.addItem("Ascendente");
        handleDateParams();
    }

    private void handleDateParams() {
        if (this.vista.jcbActivarFecha.isSelected()) {
            this.vista.jdcFechaFinal.setEnabled(true);
            this.vista.jdcFechaInicio.setEnabled(true);
        } else {
            this.vista.jdcFechaFinal.setEnabled(false);
            this.vista.jdcFechaInicio.setEnabled(false);
        }
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        //this.vista.jbCrearProducto.addActionListener(this);
        this.vista.jcbActivarFecha.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        this.vista.jcbOrdenarPor.addActionListener(this);
        this.vista.jcbBuscarPor.addActionListener(this);
        this.vista.jbResumen.addActionListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtProducto.addKeyListener(this);
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
                if (desc.length() > 50) {
                    JOptionPane.showMessageDialog(vista, "El texto ingresado supera el máximo permitido de 50 caracteres.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!validarFechas()) {
                    return;
                }
                boolean porFecha = vista.jcbActivarFecha.isSelected();
                Date fechaInicio = vista.jdcFechaInicio.getDate();
                Date fechaFinal = vista.jdcFechaFinal.getDate();
                String buscarPor = vista.jcbBuscarPor.getSelectedItem().toString();
                String ordenarPor = vista.jcbOrdenarPor.getSelectedItem().toString();
                String clasificarPor = vista.jcbClasificarPor.getSelectedItem().toString();
                modelo.consultarMateriaPrima(desc.toLowerCase(), buscarPor, ordenarPor, clasificarPor, "", porFecha, fechaInicio, fechaFinal);
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private boolean validarFechas() {
        Date inicio = vista.jdcFechaInicio.getDate();
        Date fin = vista.jdcFechaFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jdcFechaFinal.setDate(vista.jdcFechaInicio.getDate());
        vista.jdcFechaFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
    }

    private void invocarResumen() {
        ResumenBajaMateriaPrima rbmp = new ResumenBajaMateriaPrima(vista, modelo.getTm());
        rbmp.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbBuscarPor) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jcbOrdenarPor) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBuscar) {
            displayQueryResults();
        } else if (e.getSource() == this.vista.jbBorrar) {
            borrarParametros();
        } else if (e.getSource() == this.vista.jbResumen) {
            invocarResumen();
        }
        if (e.getSource().equals(this.vista.jcbActivarFecha)) {
            handleDateParams();
        }
        if (e.getSource() == this.vista.jbSalir) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
                    vista.jtProducto.requestFocusInWindow();
                    break;
                }
                case KeyEvent.VK_ESCAPE: {
                    cerrar();
                    break;
                }
            }
        }
        if (this.vista.jtProducto.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
