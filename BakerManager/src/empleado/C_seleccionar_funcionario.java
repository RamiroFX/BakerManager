/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import DB.DB_Cliente;
import DB.DB_Funcionario;
import Egresos.C_buscar_detalle;
import Egresos.C_gestionEgresos;
import Entities.M_funcionario;
import Pedido.C_gestionPedido;
import Ventas.C0_gestionVentas;
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
public class C_seleccionar_funcionario extends MouseAdapter implements ActionListener, KeyListener {

    public static final int GESTION_VENTA = 1;
    public static final int GESTION_COMPRA = 2;
    public static final int BUSCAR_DETALLE_EGRESO = 3;
    public static final int GESTION_PEDIDO = 4;
    int idCliente, tipo;
    M_funcionario funcionario;
    V_seleccionar_funcionario vista;
    //C_crear_venta c_ingreso;
    private C0_gestionVentas gestion_venta;
    private C_gestionEgresos gestion_compra;
    private C_buscar_detalle buscarDetalleEgreso;
    private C_gestionPedido gestionPedido;

    public C_seleccionar_funcionario(V_seleccionar_funcionario vista, C0_gestionVentas gestion_venta) {
        this.gestion_venta = gestion_venta;
        this.vista = vista;
        this.tipo = GESTION_VENTA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_funcionario(V_seleccionar_funcionario vista, C_gestionEgresos gestion_compra) {
        this.gestion_compra = gestion_compra;
        this.vista = vista;
        this.tipo = GESTION_COMPRA;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_funcionario(V_seleccionar_funcionario vista, C_buscar_detalle buscarDetalleEgreso) {
        this.buscarDetalleEgreso = buscarDetalleEgreso;
        this.vista = vista;
        this.tipo = BUSCAR_DETALLE_EGRESO;
        inicializarVista();
        agregarListeners();
    }

    public C_seleccionar_funcionario(V_seleccionar_funcionario vista, C_gestionPedido gestionPedido) {
        this.gestionPedido = gestionPedido;
        this.vista = vista;
        this.tipo = GESTION_PEDIDO;
        inicializarVista();
        agregarListeners();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jtCliente.setModel(DB_Funcionario.consultarFuncionario("", false, true, true));
        Utilities.c_packColumn.packColumns(this.vista.jtCliente, 1);
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jckbEntidadNombre.addActionListener(this);
        this.vista.jckbCi.addActionListener(this);
        this.vista.jrbExclusivo.addActionListener(this);
        this.vista.jrbInclusivo.addActionListener(this);
        this.vista.jtCliente.addMouseListener(this);
        this.vista.jtfBuscar.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void seleccionarFuncionario(M_funcionario funcionario) {
        switch (tipo) {
            case GESTION_VENTA: {
                this.gestion_venta.recibirFuncionario(funcionario);
                cerrar();
                break;
            }
            case GESTION_COMPRA: {
                this.gestion_compra.recibirFuncionario(funcionario);
                cerrar();
                break;
            }
            case BUSCAR_DETALLE_EGRESO: {
                this.buscarDetalleEgreso.recibirFuncionario(funcionario);
                cerrar();
                break;
            }
            case GESTION_PEDIDO: {
                this.gestionPedido.recibirFuncionario(funcionario);
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
                boolean ruc = vista.jckbCi.isSelected();
                boolean exclusivo = vista.jrbExclusivo.isSelected();
                vista.jtCliente.setModel(DB_Cliente.consultarCliente(cliente.toLowerCase(), entidad, ruc, exclusivo));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.vista.jbAceptar) {
            seleccionarFuncionario(funcionario);
        } else if (ae.getSource() == this.vista.jtfBuscar) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbEntidadNombre) {
            displayQueryResults();
        } else if (ae.getSource() == this.vista.jckbCi) {
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
        funcionario = DB_Funcionario.obtenerDatosFuncionarioID(idCliente);
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAceptar.setEnabled(true);
            if (e.getClickCount() == 2) {
                seleccionarFuncionario(funcionario);
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
