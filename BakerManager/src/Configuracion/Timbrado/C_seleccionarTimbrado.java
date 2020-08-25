/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import DB.DB_Producto;
import Egresos.C_crear_egreso;
import Entities.E_Timbrado;
import Entities.M_menu_item;
import Entities.M_producto;
import Entities.M_proveedor;
import Interface.RecibirTimbradoVentaCallback;
import MenuPrincipal.DatosUsuario;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C_verMesa;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarTimbrado extends MouseAdapter implements ActionListener, KeyListener {

    public static final int CREAR_INGRESO_RAPIDO = 1;
    public static final int CREAR_EGRESO = 2;
    public static final int VER_MESA = 4;
    public static final int CREAR_PEDIDO = 5;
    public static final int AGREGAR_PEDIDO_DETALLE = 6;
    public static final int CREAR_INGRESO_POR_CODIGO = 12;
    private static final String ENTER_KEY = "Entrar";
    int idProducto, tipo;
    M_producto producto;
    M_proveedor proveedor;
    public M_seleccionarTimbrado modelo;
    public V_seleccionarTimbrado vista;
    C_crear_egreso c_egresos;
    C_crearVentaRapida crearVenta;
    C_verMesa verMesa;
    C_crearPedido crearPedido;
    C_verPedido verPedido;

    private RecibirTimbradoVentaCallback callback;
    private boolean esModoCreacion;

    public C_seleccionarTimbrado(M_seleccionarTimbrado modelo, V_seleccionarTimbrado vista, RecibirTimbradoVentaCallback callback) {
        this.tipo = CREAR_EGRESO;
        this.callback = callback;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    public void activarModoCreacion() {
        this.esModoCreacion = true;
    }

    private void inicializarVista() {
        this.esModoCreacion = false;
        this.vista.jbCrearTimbrado.setEnabled(false);
        this.vista.jbAceptar.setEnabled(false);
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCrearTimbrado.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCrearTimbrado.setEnabled(true);
            }
        }
        this.vista.jtTimbrado.setModel(DB_Producto.consultarProducto(""));
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtTimbrado.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtTimbrado.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarProducto();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtTimbrado, 1);
        this.vista.jcbTipoFecha.addItem("Inclusiva");
        this.vista.jcbTipoFecha.addItem("Exclusiva");
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCrearTimbrado.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jcbTipoFecha.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtTimbrado.addMouseListener(this);
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
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                //vista.jtTimbrado.setModel(DB_Producto.consultarProducto(desc.toLowerCase(), proveedor, marca, rubro, impuesto, estado, busqueda));
                Utilities.c_packColumn.packColumns(vista.jtTimbrado, 1);
            }
        });
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void borrarParametros() {

    }

    private void seleccionarProducto() {
        int fila = vista.jtTimbrado.getSelectedRow();
        int columna = vista.jtTimbrado.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            if (esModoCreacion) {
                E_Timbrado timbrado = new E_Timbrado();
                callback.recibirTimbrado(timbrado);
                cerrar();
            } else {
                idProducto = Integer.valueOf(String.valueOf(vista.jtTimbrado.getValueAt(fila, 0)));
                producto = DB_Producto.obtenerDatosProductoID(idProducto);
                vista.jbAceptar.setEnabled(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarProducto();
        }

        if (e.getSource() == this.vista.jbCrearTimbrado) {
            CrearTimbrado sp = new CrearTimbrado(vista);
            sp.mostrarVista();
        }
        if (e.getSource() == this.vista.jcbPorFecha) {
            //displayQueryResults();
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
        int fila = this.vista.jtTimbrado.rowAtPoint(e.getPoint());
        int columna = this.vista.jtTimbrado.columnAtPoint(e.getPoint());
        idProducto = Integer.valueOf(String.valueOf(this.vista.jtTimbrado.getValueAt(fila, 0)));
        producto = DB_Producto.obtenerDatosProductoID(idProducto);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarProducto();
            }
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
