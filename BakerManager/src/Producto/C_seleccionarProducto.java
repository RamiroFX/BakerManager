/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB_manager.DB_Producto;
import DB_manager.DB_manager;
import Egresos.C_crear_egreso;
import Entities.M_producto;
import Entities.M_proveedor;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
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
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarProducto extends MouseAdapter implements ActionListener, KeyListener {

    public static final int CREAR_INGRESO_RAPIDO = 1;
    public static final int CREAR_EGRESO = 2;
    public static final int VER_MESA = 4;
    public static final int CREAR_PEDIDO = 5;
    public static final int AGREGAR_PEDIDO_DETALLE = 6;
    int idProducto, tipo;
    M_producto producto;
    M_proveedor proveedor;
    public V_seleccionarProducto vista;
    C_crear_egreso c_egresos;
    C_crearVentaRapida crearVenta;
    C_verMesa verMesa;
    C_crearPedido crearPedido;
    C_verPedido verPedido;

    public C_seleccionarProducto(V_seleccionarProducto vista, C_crear_egreso c_egresos) {
        this.tipo = CREAR_EGRESO;
        this.c_egresos = c_egresos;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionarProducto(V_seleccionarProducto vista, C_crearVentaRapida crearVenta) {
        this.tipo = CREAR_INGRESO_RAPIDO;
        this.crearVenta = crearVenta;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionarProducto(V_seleccionarProducto vista, C_verMesa verMesa) {
        this.tipo = VER_MESA;
        this.verMesa = verMesa;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionarProducto(V_seleccionarProducto vista, C_crearPedido crearPedido) {
        this.tipo = CREAR_PEDIDO;
        this.crearPedido = crearPedido;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionarProducto(V_seleccionarProducto vista, C_verPedido verPedido) {
        this.tipo = AGREGAR_PEDIDO_DETALLE;
        this.verPedido = verPedido;
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
        this.vista.jtProducto.setModel(DB_Producto.consultarProducto(""));
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);

        Vector marca = DB_manager.obtenerMarca();
        this.vista.jcbMarca.addItem("Todos");
        for (int i = 0; i < marca.size(); i++) {
            this.vista.jcbMarca.addItem(marca.get(i));
        }
        Vector rubro = DB_manager.obtenerCategoria();
        this.vista.jcbRubro.addItem("Todos");
        for (int i = 0; i < rubro.size(); i++) {
            this.vista.jcbRubro.addItem(rubro.get(i));
        }
        Vector impuesto = DB_manager.obtenerImpuesto();
        this.vista.jcbImpuesto.addItem("Todos");
        for (int i = 0; i < impuesto.size(); i++) {
            this.vista.jcbImpuesto.addItem(impuesto.get(i));
        }
        Vector estado = DB_manager.obtenerEstado();
        this.vista.jcbEstado.addItem("Todos");
        for (int i = 0; i < estado.size(); i++) {
            this.vista.jcbEstado.addItem(estado.get(i));
        }
        this.vista.jcbBusqueda.addItem("Exclusiva");
        this.vista.jcbBusqueda.addItem("Inclusiva");
    }

    private void agregarListeners() {
        this.vista.jbCrearProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jtProducto.addMouseListener(this);
        this.vista.jtfBuscar.addActionListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jcbBusqueda.addActionListener(this);
        this.vista.jbProveedor.addActionListener(this);
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
                String marca = vista.jcbMarca.getSelectedItem().toString();
                String rubro = vista.jcbRubro.getSelectedItem().toString();
                String impuesto = vista.jcbImpuesto.getSelectedItem().toString();
                String estado = vista.jcbEstado.getSelectedItem().toString();
                String proveedor = proveedor();
                String busqueda = vista.jcbBusqueda.getSelectedItem().toString();
                /*
                 * Se utiliza el objeto factory para obtener un TableModel
                 * para los resultados del query.
                 */
                vista.jtProducto.setModel(DB_Producto.consultarProducto(desc.toLowerCase(), proveedor, marca, rubro, impuesto, estado, busqueda));
                Utilities.c_packColumn.packColumns(vista.jtProducto, 1);
            }
        });
    }

    private String proveedor() {
        if (this.vista.jtfProveedor.getText().isEmpty()) {
            return "Todos";
        }
        return proveedor.getEntidad();
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, producto);
            scp.setVisible(true);
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
        if (e.getSource() == this.vista.jbProveedor) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this);
            sp.mostrarVista();
        }
        if (e.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        }
        if (e.getSource() == this.vista.jcbBusqueda) {
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
        idProducto = Integer.valueOf(String.valueOf(this.vista.jtProducto.getValueAt(fila, 0)));
        producto = DB_Producto.obtenerDatosProductoID(idProducto);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                SeleccionarCantidadProduducto scp = new SeleccionarCantidadProduducto(this, producto);
                scp.setVisible(true);
                this.vista.jtfBuscar.requestFocusInWindow();
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void recibirProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
        String nombre = this.proveedor.getNombre();
        String entidad = this.proveedor.getEntidad();
        this.vista.jtfProveedor.setText(nombre + " (" + entidad + ")");
    }

    private void borrarParametros() {
        this.proveedor = new M_proveedor();
        this.vista.jtfProveedor.setText("");
        this.vista.jtfBuscar.setText("");
        this.vista.jtfBuscar.requestFocusInWindow();
        this.vista.jcbEstado.setSelectedIndex(0);
        this.vista.jcbImpuesto.setSelectedIndex(0);
        this.vista.jcbMarca.setSelectedIndex(0);
        this.vista.jcbRubro.setSelectedIndex(0);
    }
}
