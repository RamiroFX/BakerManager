/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Producto;
import DB.DB_manager;
import Egresos.C_crear_egreso;
import Entities.Estado;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_proveedor;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirProductoCallback;
import MenuPrincipal.DatosUsuario;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
import Producto.C_crear_producto;
import Proveedor.Seleccionar_proveedor;
import Ventas.C_crearVentaRapida;
import Ventas.C_verMesa;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarRollo extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_seleccionarRollo modelo;
    private V_seleccionarRollo vista;
    private InterfaceRecibirProduccionFilm callback;

    public C_seleccionarRollo(M_seleccionarRollo modelo, V_seleccionarRollo vista) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
        agregarListeners();
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.callback = callback;
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbCrearProducto.setEnabled(false);
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearProducto.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearProducto.setEnabled(true);
            }
        }
        this.vista.jtProducto.setModel(modelo.getTm());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtProducto.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtProducto.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarRollo();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);

        ArrayList<Estado> estado = modelo.obtenerEstado();
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
        this.vista.jcbOrdenarPor.addItem("ID");
        this.vista.jcbOrdenarPor.addItem("Descripción");
        this.vista.jcbOrdenarPor.addItem("Código");
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCrearProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtProducto.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
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
                int jcbEstadoIndex = vista.jcbEstado.getSelectedIndex();
                Estado estado = vista.jcbEstado.getItemAt(jcbEstadoIndex);
                String ordenarPor = vista.jcbOrdenarPor.getSelectedItem().toString();
                modelo.consultarRollos(desc, estado, ordenarPor);
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
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
        this.vista.jcbEstado.setSelectedIndex(1);
    }

    private void seleccionarRollo() {
        int fila = vista.jtProducto.getSelectedRow();
        if (fila > -1) {
            M_producto producto = modelo.getTm().getProductoList().get(fila);
            vista.jbAceptar.setEnabled(true);
            CrearFilm crearFilm = new CrearFilm(this.vista);
            crearFilm.setCallback(callback);
            crearFilm.rellenarVista(producto);
            crearFilm.mostrarVista();
            vista.jtfBuscar.requestFocusInWindow();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarRollo();
            this.vista.jtfBuscar.requestFocusInWindow();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.jtfBuscar.selectAll();
                }
            });
        }

        if (e.getSource() == this.vista.jbCrearProducto) {
            C_crear_producto sp = new C_crear_producto(vista);
            sp.mostrarVista();
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbOrdenarPor) {
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
        int fila = this.vista.jtProducto.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProducto.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            int index = this.vista.jtProducto.getSelectedRow();
            M_producto producto = modelo.getTm().getProductoList().get(index);
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                vista.jbAceptar.setEnabled(true);
                CrearFilm crearFilm = new CrearFilm(this.vista);
                crearFilm.setCallback(callback);
                crearFilm.rellenarVista(producto);
                crearFilm.mostrarVista();
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
                    vista.jtProducto.requestFocusInWindow();
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
