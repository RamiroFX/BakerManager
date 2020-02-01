/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Empleado.Seleccionar_funcionario;
import Entities.E_tipoOperacion;
import Entities.M_funcionario;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarFacturaPendiente extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback {

    private static final String ENTER_KEY = "Entrar";
    public M_seleccionarFacturaPendiente modelo;
    public V_seleccionarFacturaPendiente vista;

    private RecibirProductoCallback callback;

    public C_seleccionarFacturaPendiente(M_seleccionarFacturaPendiente modelo, V_seleccionarFacturaPendiente vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<E_tipoOperacion> tipoOperaciones = modelo.obtenerTipoOperacion();
        for (int i = 0; i < tipoOperaciones.size(); i++) {
            this.vista.jcbCondVenta.addItem(tipoOperaciones.get(i));
        }
        //this.vista.jtFacturaPendiente.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtFacturaPendiente.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtFacturaPendiente.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarProducto();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaPendiente, 1);

    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtFacturaPendiente.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jtFacturaPendiente.addKeyListener(this);
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

                //vista.jtFacturaPendiente.setModel(modelo.consultarFacturasPendiente());
                Utilities.c_packColumn.packColumns(vista.jtFacturaPendiente, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {
        this.vista.jtfFuncionario.setText("");
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        this.vista.jcbCondVenta.setSelectedIndex(0);
    }

    private void seleccionarProducto() {
        int fila = vista.jtFacturaPendiente.getSelectedRow();
        int columna = vista.jtFacturaPendiente.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idFacturaPendiente = Integer.valueOf(String.valueOf(vista.jtFacturaPendiente.getValueAt(fila, 0)));
            //M_facturaCabecera cabecera = modelo.obtenerFacturaCabecera(idFacturaPendiente);
            vista.jbAceptar.setEnabled(true);
            /*SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, cabecera);
            scp.setCallback(callback);
            scp.setVisible(true);*/
            vista.jtfBuscar.requestFocusInWindow();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            /*SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, producto);
            scp.setCallback(callback);
            scp.setVisible(true);*/
            this.vista.jtfBuscar.requestFocusInWindow();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.jtfBuscar.selectAll();
                }
            });
        }
        if (e.getSource() == this.vista.jbFuncionario) {
            Seleccionar_funcionario sp = new Seleccionar_funcionario(this.vista);
            sp.setCallback(this);
            sp.mostrarVista();
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
        int fila = this.vista.jtFacturaPendiente.rowAtPoint(e.getPoint());
        int columna = this.vista.jtFacturaPendiente.columnAtPoint(e.getPoint());
        int idFacturaCabecera = Integer.valueOf(String.valueOf(this.vista.jtFacturaPendiente.getValueAt(fila, 0)));
        //M_facturaCabecera cabecera = modelo.obtenerFactura(idFacturaCabecera);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                /*SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, producto);
                scp.setCallback(callback);
                scp.setVisible(true);*/
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
                    vista.jtFacturaPendiente.requestFocusInWindow();
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

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        //this.modelo.setFuncionario(funcionario);
        this.vista.jtfFuncionario.setText(funcionario.getNombre());
    }
}
