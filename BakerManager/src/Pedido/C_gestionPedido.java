/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Charts.Diagramas;
import Cliente.Seleccionar_cliente;
import DB_manager.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.Gestion;
import Resumen.Resumen;
import bakermanager.C_inicio;
import empleado.Seleccionar_funcionario;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionPedido implements Gestion {

    public M_gestionPedido modelo;
    public V_gestionPedido vista;
    public C_inicio c_inicio;

    public C_gestionPedido(M_gestionPedido modelo, V_gestionPedido vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        agregarListeners();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbBuscarDetalle.setEnabled(false);
        Vector condCompra = modelo.obtenerTipoOperacion();
        this.vista.jcbCondVenta.addItem("Todos");
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        Vector estadoPedido = modelo.obtenerEstado();
        this.vista.jcbEstadoPedido.addItem("Todos");
        for (int i = 0; i < estadoPedido.size(); i++) {
            this.vista.jcbEstadoPedido.addItem(estadoPedido.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        this.vista.jbCancelarPedido.setEnabled(false);
        this.vista.jbPagoPedido.setEnabled(false);
        this.vista.jbDetalle.setEnabled(false);
    }

    @Override
    public final void agregarListeners() {
        this.vista.jbAgregar.addActionListener(this);
        this.vista.jbPagoPedido.addActionListener(this);
        this.vista.jbCancelarPedido.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbPedidosPendientes.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbResumen.addActionListener(this);
        this.vista.jbCharts.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jtPedido.addMouseListener(this);
        this.vista.jtPedido.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    @Override
    public void cerrar() {
        this.vista.dispose();
    }

    private void displayQueryResults() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date inicio = vista.jddInicio.getDate();
                Date fin = vista.jddFinal.getDate();
                if (modelo.validarFechas(inicio, fin)) {
                    boolean esTiempoRecepcionOEntrega = true;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fecha_inicio = sdf.format(vista.jddInicio.getDate()) + " 00:00:00.00";
                    String fecha_fin = sdf.format(vista.jddFinal.getDate()) + " 23:59:59.00";
                    String nroPedido = vista.jtfNroPedido.getText();
                    String conVenta = vista.jcbCondVenta.getSelectedItem().toString();
                    String estado = vista.jcbEstadoPedido.getSelectedItem().toString();
                    vista.jtPedido.setModel(modelo.obtenerVentas(esTiempoRecepcionOEntrega, fecha_inicio, fecha_fin, conVenta, nroPedido, estado));
                    modelo.setRstmPedido((ResultSetTableModel) vista.jtPedido.getModel());
                    Utilities.c_packColumn.packColumns(vista.jtPedido, 1);
                    vista.jtPedidoDetalle.setModel(modelo.getDtm());
                    vista.jbDetalle.setEnabled(false);
                } else {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void recibirCliente(M_cliente cliente) {
        this.modelo.getPedido().setCliente(cliente);
        String nombre = this.modelo.getPedido().getCliente().getNombre();
        String entidad = this.modelo.getPedido().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.getPedido().setFuncionario(funcionario);
        String alias = this.modelo.getPedido().getFuncionario().getAlias();
        String nombre = this.modelo.getPedido().getFuncionario().getNombre();
        String apellido = this.modelo.getPedido().getFuncionario().getApellido();
        this.vista.jtfEmpleado.setText(alias + "-(" + nombre + " " + apellido + ")");
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroPedido.setText("");
        this.vista.jcbCondVenta.setSelectedItem("Todos");
        this.vista.jcbEstadoPedido.setSelectedItem("Todos");
    }

    private void obtenerPedidoDetalle(MouseEvent e) {
        int fila = this.vista.jtPedido.rowAtPoint(e.getPoint());
        int columna = this.vista.jtPedido.columnAtPoint(e.getPoint());
        Integer idPedido = Integer.valueOf(String.valueOf(this.vista.jtPedido.getValueAt(fila, 0)));
        //this.modelo.setPedido(modelo.obtenerPedido(idPedido));
        controlarTablaPedido();
        /**/
        if ((fila > -1) && (columna > -1)) {
            String estado = String.valueOf(this.vista.jtPedido.getValueAt(fila, 6));
            if (!estado.equals("Entregado")) {
                this.vista.jbPagoPedido.setEnabled(true);
                this.vista.jbCancelarPedido.setEnabled(true);
            } else {
                this.vista.jbPagoPedido.setEnabled(false);
                this.vista.jbCancelarPedido.setEnabled(false);
            }
            this.vista.jtPedidoDetalle.setModel(modelo.obtenerPedidoDetalle(idPedido));
            Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
        }
        if (e.getClickCount() == 2) {
            verDetalle();
        }
    }

    private void verDetalle() {
        int row = this.vista.jtPedido.getSelectedRow();
        int idPedido = Integer.valueOf(String.valueOf(this.vista.jtPedido.getValueAt(row, 0)));
        VerPedido vp = new VerPedido(this, idPedido);
        vp.mostrarVista();
    }

    private void verResumen() {
        Resumen resumen = new Resumen(this);
        resumen.mostrarVista();
    }

    private void verDiagramas() {
        Diagramas resumen = new Diagramas(this);
        resumen.mostrarVista();
    }

    private void crearPedido() {
        CrearPedido crearPedido = new CrearPedido(this);
        crearPedido.mostrarVista();
    }

    private void controlarTablaPedido() {
        if (this.vista.jtPedido.getRowCount() > 0) {
            this.vista.jbResumen.setEnabled(true);
            this.vista.jbPagoPedido.setEnabled(true);
            this.vista.jbCancelarPedido.setEnabled(true);
            this.vista.jbDetalle.setEnabled(true);
        } else {
            this.vista.jbResumen.setEnabled(false);
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbPagoPedido.setEnabled(false);
            this.vista.jbCancelarPedido.setEnabled(false);
        }
    }

    private void pagarPedido() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmas esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            int fila = this.vista.jtPedido.getSelectedRow();
            Integer idPedido = Integer.valueOf(String.valueOf(this.vista.jtPedido.getValueAt(fila, 0)));
            this.modelo.pagarPedido(idPedido);
            this.vista.jtPedido.setModel(this.modelo.getPedidosPendientes());
            this.vista.jtPedidoDetalle.setModel(this.modelo.getDtm());
            Utilities.c_packColumn.packColumns(this.vista.jtPedido, 1);
            controlarTablaPedido();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbPagoPedido.setEnabled(false);
            this.vista.jbCancelarPedido.setEnabled(false);
        }
    }

    private void cancelarPedido() {
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea confirmas esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            int fila = this.vista.jtPedido.getSelectedRow();
            Integer idPedido = Integer.valueOf(String.valueOf(this.vista.jtPedido.getValueAt(fila, 0)));
            this.modelo.cancelarPedido(idPedido);
            this.vista.jtPedido.setModel(this.modelo.getPedidosPendientes());
            this.vista.jtPedidoDetalle.setModel(this.modelo.getDtm());
            Utilities.c_packColumn.packColumns(this.vista.jtPedido, 1);
            controlarTablaPedido();
            this.modelo.borrarDatos();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbPagoPedido.setEnabled(false);
            this.vista.jbCancelarPedido.setEnabled(false);
        }
    }

    private void jbPedidosPendientesButtonHandler() {
        this.vista.jtPedido.setModel(this.modelo.getPedidosPendientes());
        this.vista.jtPedidoDetalle.setModel(this.modelo.getDtm());
        Utilities.c_packColumn.packColumns(this.vista.jtPedido, 1);
    }

    public void keyReleasedHandler(KeyEvent e) {
        if (e.getSource().equals(this.vista.jtPedido)) {
            int row = this.vista.jtPedido.getSelectedRow();
            int columna = this.vista.jtPedido.getSelectedRow();
            int idPedido = Integer.valueOf(String.valueOf(this.vista.jtPedido.getValueAt(row, 0)));
            //this.modelo.setPedido(modelo.obtenerPedido(idPedido));
            controlarTablaPedido();
            /*if (this.modelo.getPedido().getIdEstado() == 1) {
             this.vista.jbPagoPedido.setEnabled(true);
             this.vista.jbCancelarPedido.setEnabled(true);
             } else {
             this.vista.jbPagoPedido.setEnabled(false);
             this.vista.jbCancelarPedido.setEnabled(false);
             }*/
            if ((row > -1) && (columna > -1)) {
                String estado = String.valueOf(this.vista.jtPedido.getValueAt(row, 6));
                if (!estado.equals("Entregado")) {
                    this.vista.jbPagoPedido.setEnabled(true);
                    this.vista.jbCancelarPedido.setEnabled(true);
                } else {
                    this.vista.jbPagoPedido.setEnabled(false);
                    this.vista.jbCancelarPedido.setEnabled(false);
                }
                this.vista.jtPedidoDetalle.setModel(modelo.obtenerPedidoDetalle(idPedido));
                Utilities.c_packColumn.packColumns(this.vista.jtPedidoDetalle, 1);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbAgregar)) {
            crearPedido();
        } else if (e.getSource().equals(this.vista.jbPedidosPendientes)) {
            jbPedidosPendientesButtonHandler();
        } else if (e.getSource().equals(this.vista.jbPagoPedido)) {
            pagarPedido();
        } else if (e.getSource().equals(this.vista.jbCancelarPedido)) {
            cancelarPedido();
        } else if (e.getSource().equals(this.vista.jbBuscar)) {
            displayQueryResults();
        } else if (e.getSource().equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this);
            sc.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
            sf.mostrarVista();
        } else if (e.getSource().equals(this.vista.jbBorrar)) {
            borrarDatos();
        } else if (e.getSource().equals(this.vista.jbResumen)) {
            verResumen();
        } else if (e.getSource().equals(this.vista.jbCharts)) {
            verDiagramas();
        } else if (e.getSource().equals(this.vista.jbDetalle)) {
            verDetalle();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtPedido)) {
            obtenerPedidoDetalle(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyReleasedHandler(e);
    }
}
