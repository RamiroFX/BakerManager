/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB.DB_Cliente;
import Entities.M_cliente;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Pedido.C_crearPedido;
import Pedido.C_gestionPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C_gestionVentas;
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
public class C_seleccionar_cliente extends MouseAdapter implements ActionListener, KeyListener, InterfaceNotificarCambio {

    private static final String ENTER_KEY = "Entrar";
    int idCliente, tipo;
    M_cliente cliente;
    V_seleccionar_cliente vista;
    RecibirClienteCallback callback;

    public C_seleccionar_cliente(V_seleccionar_cliente vista) {
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void setCallback(RecibirClienteCallback rccb) {
        this.callback = rccb;
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
        this.vista.jbCrearCliente.addActionListener(this);
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
        this.vista.jbCrearCliente.addKeyListener(this);
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
        this.callback.recibirCliente(cliente);
        cerrar();
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
        } else if (ae.getSource() == this.vista.jbCrearCliente) {
            Crear_cliente crear_cliente = new Crear_cliente(this.vista);
            crear_cliente.setInterfaceNotificarCambio(this);
            crear_cliente.mostrarVista();
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

    @Override
    public void notificarCambio() {
        this.displayQueryResults();
    }
}
