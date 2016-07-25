/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB_manager.DB_Cliente;
import Entities.M_cliente;
import Pedido.C_crearPedido;
import Pedido.C_gestionPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C0_gestionVentas;
import Ventas.C_verMesa;
import Ventas.ConfigurarMesa;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionar_cliente extends MouseAdapter implements ActionListener, KeyListener {

    public static final int GESTION_VENTA = 1;
    public static final int CREAR_VENTA = 2;
    public static final int CONFIGURAR_MESA = 3;
    public static final int VER_MESA = 5;
    public static final int GESTION_PEDIDO = 6;
    public static final int CREAR_PEDIDO = 7;
    public static final int VER_PEDIDO = 8;
    int idCliente, tipo;
    M_cliente cliente;
    V_seleccionar_cliente vista;
    C_crearVentaRapida c_ingreso;
    C0_gestionVentas gestion_venta;
    ConfigurarMesa configurarMesa;
    C_verMesa verMesa;
    C_gestionPedido gestionPedido;
    C_crearPedido crearPedido;
    C_verPedido verPedido;

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_crearVentaRapida c_ingreso) {
        this.c_ingreso = c_ingreso;
        this.vista = vista;
        this.tipo = CREAR_VENTA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C0_gestionVentas gestion_venta) {
        this.gestion_venta = gestion_venta;
        this.vista = vista;
        this.tipo = GESTION_VENTA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, ConfigurarMesa configurarMesa) {
        this.configurarMesa = configurarMesa;
        this.vista = vista;
        this.tipo = CONFIGURAR_MESA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_verMesa verMesa) {
        this.verMesa = verMesa;
        this.vista = vista;
        this.tipo = VER_MESA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_gestionPedido gestionPedido) {
        this.gestionPedido = gestionPedido;
        this.vista = vista;
        this.tipo = GESTION_PEDIDO;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_crearPedido crearPedido) {
        this.crearPedido = crearPedido;
        this.vista = vista;
        this.tipo = CREAR_PEDIDO;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_verPedido verPedido) {
        this.verPedido = verPedido;
        this.vista = vista;
        this.tipo = VER_PEDIDO;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtCliente.setModel(DB_Cliente.consultarCliente("", false, true, true));
        Utilities.c_packColumn.packColumns(this.vista.jtCliente, 1);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidadNombre.addActionListener(this);
        this.vista.jckbRUC.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtCliente.addMouseListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void seleccionarProveedor(M_cliente cliente) {
        switch (tipo) {
            case GESTION_VENTA: {
                this.gestion_venta.recibirCliente(cliente);
                cerrar();
                break;
            }
            case CREAR_VENTA: {
                this.c_ingreso.recibirCliente(cliente);
                cerrar();
                break;
            }
            case CONFIGURAR_MESA: {
                this.configurarMesa.recibirCliente(cliente);
                cerrar();
                break;
            }
            case VER_MESA: {
                this.verMesa.recibirCliente(cliente);
                cerrar();
                break;
            }
            case GESTION_PEDIDO: {
                this.gestionPedido.recibirCliente(cliente);
                cerrar();
                break;
            }
            case CREAR_PEDIDO: {
                this.crearPedido.recibirCliente(cliente);
                cerrar();
                break;
            }
            case VER_PEDIDO: {
                this.verPedido.recibirCliente(cliente);
                cerrar();
                break;
            }
            default: {
                cerrar();
                break;
            }
        }
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String cliente = vista.jtfBuscar.getText();
                boolean entidad = vista.jckbEntidadNombre.isSelected();
                boolean ruc = vista.jckbRUC.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtCliente.setModel(DB_Cliente.consultarCliente(cliente.toLowerCase(), entidad, ruc, exclusivo));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbAceptar) {
            seleccionarProveedor(cliente);
        } else if (ae.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbEntidadNombre) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbRUC) {
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
        int fila = this.vista.jtCliente.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCliente.columnAtPoint(e.getPoint());
        idCliente = Integer.valueOf(String.valueOf(this.vista.jtCliente.getValueAt(fila, 0)));
        cliente = DB_Cliente.obtenerDatosClienteID(idCliente);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarProveedor(cliente);
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
