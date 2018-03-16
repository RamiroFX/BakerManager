/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB.DB_Cliente;
import Entities.M_cliente;
import Pedido.C_crearPedido;
import Pedido.C_gestionPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C0_gestionVentas;
import Ventas.C_buscar_venta_detalle;
import Ventas.C_verMesa;
import Ventas.ConfigurarMesa;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;

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
    public static final int BUSCAR_VENTA_DETALLE = 9;
    private static final String ENTER_KEY = "Entrar";
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
    C_buscar_venta_detalle ventaDetalle;

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

    public C_seleccionar_cliente(V_seleccionar_cliente vista, C_buscar_venta_detalle ventaDetalle) {
        this.ventaDetalle = ventaDetalle;
        this.vista = vista;
        this.tipo = BUSCAR_VENTA_DETALLE;
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
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtCliente.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtCliente.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCliente();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtCliente, 1);
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidadNombre.addActionListener(this);
        this.vista.jckbRUC.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtCliente.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtfBuscar.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jckbEntidadNombre.addKeyListener(this);
        this.vista.jckbRUC.addKeyListener(this);
        this.vista.jrbExclusivo.addKeyListener(this);
        this.vista.jrbInclusivo.addKeyListener(this);
        this.vista.jtCliente.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void seleccionarCliente(M_cliente cliente) {
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

            case BUSCAR_VENTA_DETALLE: {
                this.ventaDetalle.recibirCliente(cliente);
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

    private void seleccionarCliente() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtCliente.getSelectedRow();
                int columna = vista.jtCliente.getSelectedColumn();
                if ((fila > -1) && (columna > -1)) {
                    idCliente = Integer.valueOf(String.valueOf(vista.jtCliente.getValueAt(fila, 0)));
                    cliente = DB_Cliente.obtenerDatosClienteID(idCliente);
                    seleccionarCliente(cliente);
                }
            }
        });
    }

    private void controlarFilaSeleccionada() {
        int fila = this.vista.jtCliente.getSelectedRow();
        int columna = this.vista.jtCliente.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
        } else {
            this.vista.jbAceptar.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbAceptar) {
            seleccionarCliente(cliente);
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
                seleccionarCliente(cliente);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayQueryResults();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
        if (this.vista.jtfBuscar.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                vista.jtCliente.requestFocusInWindow();
            }
        }
        if (this.vista.jtCliente.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                controlarFilaSeleccionada();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
