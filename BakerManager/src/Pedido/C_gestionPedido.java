/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import Charts.Diagramas;
import Cliente.Seleccionar_cliente;
import DB.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import MenuPrincipal.DatosUsuario;
import Resumen.Resumen;
import bakermanager.C_inicio;
import Empleado.Seleccionar_funcionario;
import Utilities.MyColorCellRenderer;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionPedido implements GestionInterface {

    public M_gestionPedido modelo;
    public V_gestionPedido vista;
    public C_inicio c_inicio;

    public C_gestionPedido(M_gestionPedido modelo, V_gestionPedido vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
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
        this.vista.jbAgregar.setEnabled(false);
        this.vista.jbBuscar.setEnabled(false);
        this.vista.jbPedidosPendientes.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jbBorrar.setEnabled(false);
        this.vista.jbResumen.setEnabled(false);
        this.vista.jbCharts.setEnabled(false);
        this.vista.jbCancelarPedido.setEnabled(false);
        this.vista.jbPagoPedido.setEnabled(false);
        this.vista.jbDetalle.setEnabled(false);
        //this.vista.jtPedido.getColumnModel().getColumn(6).setCellRenderer(new MyColorCellRenderer());
        this.vista.jtPedido.setDefaultRenderer(Object.class, new MyColorCellRenderer(6));
    }

    @Override
    public final void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbAgregar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAgregar.setEnabled(true);
                this.vista.jbAgregar.addActionListener(this);
            }
            if (this.vista.jbPagoPedido.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbPagoPedido.addActionListener(this);
            }
            if (this.vista.jbCancelarPedido.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCancelarPedido.addActionListener(this);
            }
            if (this.vista.jbBuscar.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbPedidosPendientes.addActionListener(this);
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jbBorrar.addActionListener(this);
                this.vista.jbPedidosPendientes.setEnabled(true);
                this.vista.jbCliente.setEnabled(true);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbBorrar.setEnabled(true);
                this.vista.jbBuscar.setEnabled(true);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbCharts.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCharts.setEnabled(true);
                this.vista.jbCharts.addActionListener(this);
            }
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.addActionListener(this);
            }
        }
        this.vista.jtPedido.addMouseListener(this);
        this.vista.jtPedido.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbBuscarDetalle.addKeyListener(this);
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbPagoPedido.addKeyListener(this);
        this.vista.jbCancelarPedido.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbCharts.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (this.vista.jbDetalle.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalle.setEnabled(true);
            }
            if (this.vista.jbPagoPedido.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbPagoPedido.setEnabled(true);
            }
            if (this.vista.jbCancelarPedido.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCancelarPedido.setEnabled(true);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.setEnabled(true);
            }
            if (this.vista.jbCharts.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCharts.setEnabled(true);
            }
        }
    }

    @Override
    public void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    @Override
    public void cerrar() {
        try {
            this.vista.setClosed(true);
            System.runFinalization();
        } catch (PropertyVetoException ex) {
        }
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
                    vista.jtPedido.setModel(modelo.obtenerPedidos(esTiempoRecepcionOEntrega, fecha_inicio, fecha_fin, conVenta, nroPedido, estado));
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
            if (vista.jbDetalle.isEnabled()) {
                verDetalle();
            }
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
        verificarPermiso();
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
            controlarTablaPedido();
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                if (vista.jbAgregar.isEnabled()) {
                    crearPedido();
                }
                break;
            }
            case KeyEvent.VK_F2: {
                if (vista.jbResumen.isEnabled()) {
                    verResumen();
                }
                break;
            }
            case KeyEvent.VK_F3: {
                if (vista.jbCharts.isEnabled()) {
                    verDiagramas();
                }
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyReleasedHandler(e);
    }
}
