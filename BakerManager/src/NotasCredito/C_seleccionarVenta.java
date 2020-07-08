/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Entities.E_facturaCabecera;
import Entities.E_tipoOperacion;
import Entities.M_facturaCabecera;
import Interface.RecibirFacturaCabeceraCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarVenta extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_seleccionarVenta modelo;
    private V_seleccionarVenta vista;
    private RecibirFacturaCabeceraCallback callback;

    public C_seleccionarVenta(M_seleccionarVenta modelo, V_seleccionarVenta vista) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    public void setCallback(RecibirFacturaCabeceraCallback callback) {
        this.callback = callback;
    }

    public void mostrarVista() {
        displayQueryResults();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jtVentaCabecera.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtVentaCabecera.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtVentaCabecera.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarVenta();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtVentaCabecera, 1);
        ArrayList<E_tipoOperacion> tipoOperacion = modelo.obtenerTipoOperacion();
        for (int i = 0; i < tipoOperacion.size(); i++) {
            this.vista.jcbTipoOperacion.addItem(tipoOperacion.get(i));
        }
        Calendar calendar = Calendar.getInstance();
        this.vista.jddInicio.setDate(calendar.getTime());
        this.vista.jddFinal.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        this.vista.jcbTipoOperacion.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtVentaCabecera.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtVentaCabecera.addKeyListener(this);
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
                int jcbTipoOperacionIndex = vista.jcbTipoOperacion.getSelectedIndex();
                E_tipoOperacion tiop = vista.jcbTipoOperacion.getItemAt(jcbTipoOperacionIndex);
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                modelo.consultarVenta(fechaInicio, fechaFinal, tiop, true);
                Utilities.c_packColumn.packColumns(vista.jtVentaCabecera, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        this.vista.jcbTipoOperacion.setSelectedIndex(1);
    }

    private boolean validarFechas() {
        Date inicio = vista.jddInicio.getDate();
        Date fin = vista.jddFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jddFinal.setDate(vista.jddInicio.getDate());
        vista.jddFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void seleccionarVenta() {
        int fila = vista.jtVentaCabecera.getSelectedRow();
        if (fila > -1) {
            M_facturaCabecera faca = modelo.getTm().getFacturaCabeceraList().get(fila);
            E_facturaCabecera facturaCabecera = new E_facturaCabecera();
            facturaCabecera.setCliente(faca.getCliente());
            facturaCabecera.setFuncionario(faca.getFuncionario());
            facturaCabecera.setIdFacturaCabecera(faca.getIdFacturaCabecera());
            callback.recibirVenta(facturaCabecera, modelo.obtenerFacturaDetalle(faca.getIdFacturaCabecera()));
            cerrar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarVenta();
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbTipoOperacion) {
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
        int fila = this.vista.jtVentaCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtVentaCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            if (e.getClickCount() == 2) {
                seleccionarVenta();
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
                    vista.jtVentaCabecera.requestFocusInWindow();
                    break;
                }
                case KeyEvent.VK_ESCAPE: {
                    cerrar();
                    break;
                }
            }
        }
        if (this.vista.jtVentaCabecera.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
