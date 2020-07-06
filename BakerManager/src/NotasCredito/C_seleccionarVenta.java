/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Entities.E_facturaCabecera;
import Entities.E_tipoOperacion;
import bauplast.*;
import Entities.Estado;
import Entities.M_facturaCabecera;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.ProductoCategoria;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirProductoCallback;
import MenuPrincipal.DatosUsuario;
import Produccion.SeleccionCantidadProductoSimple;
import Producto.C_crear_producto;
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
public class C_seleccionarVenta extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_seleccionarVenta modelo;
    private V_seleccionarVenta vista;
    private InterfaceRecibirProduccionFilm callback;
    private RecibirProductoCallback productoCallback;
    private boolean isProductoTerminado;//productoTerminado=true;rollo=false

    public C_seleccionarVenta(M_seleccionarVenta modelo, V_seleccionarVenta vista) {
        this.vista = vista;
        this.modelo = modelo;
        this.isProductoTerminado = false;
        inicializarVista();
        agregarListeners();
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.isProductoTerminado = false;
        this.callback = callback;
    }

    public void setProductoCallback(RecibirProductoCallback productoCallback) {
        this.isProductoTerminado = true;
        this.productoCallback = productoCallback;
    }

    public void mostrarVista() {
        displayQueryResults();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
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
                int jcbTipoOperacionIndex = vista.jcbTipoOperacion.getSelectedIndex();
                E_tipoOperacion tiop = vista.jcbTipoOperacion.getItemAt(jcbTipoOperacionIndex);
                //modelo.consultarVenta(tiop);
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

    private void seleccionarVenta() {
        int fila = vista.jtVentaCabecera.getSelectedRow();
        if (fila > -1) {
            M_facturaCabecera faca = modelo.getTm().getFacturaCabeceraList().get(fila);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarVenta();
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
