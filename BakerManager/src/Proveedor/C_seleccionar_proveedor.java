/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import DB.DB_Proveedor;
import Egresos.C_buscar_detalle;
import Egresos.C_crear_egreso;
import Egresos.C_gestionEgresos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Entities.M_proveedor;
import Producto.C_crear_producto;
import Producto.C_gestion_producto;
import Producto.C_seleccionarProducto;
import Producto.Crear_producto_proveedor;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionar_proveedor extends MouseAdapter implements ActionListener, KeyListener {

    public static final int GESTION_EGRESO = 1;
    public static final int CREAR_EGRESO = 2;
    public static final int CREAR_PRODUCTO = 3;
    public static final int ASIGNAR_PRODUCTO_PROVEEDOR = 4;
    public static final int BUSCAR_DETALLE_EGRESO = 5;
    public static final int GESTION_PRODUCTO = 6;
    public static final int SELECCIONAR_PRODUCTO = 7;
    private int idProveedor, tipo;
    private M_proveedor proveedor;
    private V_seleccionar_proveedor vista;
    private C_crear_egreso c_egresos;
    private Crear_producto_proveedor prodProv;
    private C_gestionEgresos gestion_egreso;
    private C_gestion_producto gestion_producto;
    private C_crear_producto crear_producto;
    private C_buscar_detalle buscarDetalleEgreso;
    private C_seleccionarProducto seleccionarProducto;

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_gestionEgresos gestion_egreso) {
        this.gestion_egreso = gestion_egreso;
        this.tipo = GESTION_EGRESO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_crear_egreso c_egresos) {
        this.c_egresos = c_egresos;
        this.tipo = CREAR_EGRESO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_crear_producto crear_producto) {
        this.crear_producto = crear_producto;
        this.tipo = CREAR_PRODUCTO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, Crear_producto_proveedor prodProv) {
        this.prodProv = prodProv;
        this.tipo = ASIGNAR_PRODUCTO_PROVEEDOR;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_buscar_detalle buscarDetalleEgreso) {
        this.buscarDetalleEgreso = buscarDetalleEgreso;
        this.tipo = BUSCAR_DETALLE_EGRESO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_gestion_producto gestion_producto) {
        this.gestion_producto = gestion_producto;
        this.tipo = GESTION_PRODUCTO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_proveedor(V_seleccionar_proveedor vista, C_seleccionarProducto seleccionarProducto) {
        this.seleccionarProducto = seleccionarProducto;
        this.tipo = SELECCIONAR_PRODUCTO;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor(" ", true, true, true));
        Utilities.c_packColumn.packColumns(this.vista.jtProveedor, 1);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidad.addActionListener(this);
        this.vista.jckbNombre.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtProveedor.addMouseListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void seleccionarProveedor(M_proveedor proveedor) {
        switch (tipo) {
            case GESTION_EGRESO: {
                this.gestion_egreso.recibirProveedor(proveedor);
                break;
            }
            case CREAR_EGRESO: {
                this.c_egresos.recibirProveedor(proveedor);
                break;
            }
            case GESTION_PRODUCTO: {
                this.gestion_producto.recibirProveedor(proveedor);
                break;
            }
            case CREAR_PRODUCTO: {
                this.crear_producto.recibirProveedor(proveedor);
                break;
            }
            case ASIGNAR_PRODUCTO_PROVEEDOR: {
                this.prodProv.recibirProveedor(proveedor);
                break;
            }
            case BUSCAR_DETALLE_EGRESO: {
                this.buscarDetalleEgreso.recibirProveedor(proveedor);
                break;
            }
            case SELECCIONAR_PRODUCTO: {
                this.seleccionarProducto.recibirProveedor(proveedor);
                break;
            }
            default: {
                cerrar();
                break;
            }
        }
        cerrar();
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String proveedor = vista.jtfBuscar.getText();
                boolean entidad = vista.jckbEntidad.isSelected();
                boolean nombre = vista.jckbNombre.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtProveedor.setModel(DB_Proveedor.consultarProveedor(proveedor.toLowerCase(), entidad, nombre, exclusivo));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbAceptar) {
            seleccionarProveedor(proveedor);
        } else if (ae.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbEntidad) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbNombre) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbExclusivo) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jrbInclusivo) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jbCancelar) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtProveedor.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProveedor.columnAtPoint(e.getPoint());
        idProveedor = Integer.valueOf(String.valueOf(this.vista.jtProveedor.getValueAt(fila, 0)));
        proveedor = DB_Proveedor.obtenerDatosProveedorID(idProveedor);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarProveedor(proveedor);
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
}
