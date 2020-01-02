/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.E_facturacionCabecera;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturacionCabeceraTableModel;
import Ventas.Resumen_ingreso;
import Ventas.Ver_ingreso;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_historialFacturacion implements GestionInterface, RecibirEmpleadoCallback, RecibirClienteCallback {

    public M_historialFacturacion modelo;
    public V_historialFacturacion vista;
    public C_inicio c_inicio;

    public C_historialFacturacion(M_historialFacturacion modelo, V_historialFacturacion vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jbFacturacionDetalle.setEnabled(false);
        this.vista.jbVentaDetalle.setEnabled(false);
        ArrayList<E_tipoOperacion> condVenta = modelo.obtenerTipoOperaciones();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbCondVenta.addItem(condVenta.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        /*this.vista.jbBuscar.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jcbCondVenta.setEnabled(false);
        this.vista.jbSalir.setEnabled(false);*/
    }

    @Override
    public final void concederPermisos() {
        /*ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbBuscar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbBuscar.setEnabled(true);
                this.vista.jbBuscar.addActionListener(this);
                this.vista.jbCliente.setEnabled(true);
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbEmpleado.setEnabled(true);
                this.vista.jbEmpleado.addActionListener(this);
                this.vista.jcbCondVenta.setEnabled(true);
                this.vista.jbBorrar.addActionListener(this);
            }
            if (this.vista.jbFacturacionDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbFacturacionDetalle.addActionListener(this);
            }
            if (this.vista.jbVentaDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbVentaDetalle.addActionListener(this);
            }
        }*/
        //TODO remove
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbVentaDetalle.addActionListener(this);
        this.vista.jbFacturacionDetalle.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);

        this.vista.jtFacturacion.addMouseListener(this);
        this.vista.jtFacturacion.addKeyListener(this);
        this.vista.jtVentas.addMouseListener(this);
        this.vista.jtVentas.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jcbCondVenta.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbVentaDetalle.addKeyListener(this);
        this.vista.jbFacturacionDetalle.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    @Override
    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    @Override
    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.cabecera.setCliente(cliente);
        String nombre = this.modelo.cabecera.getCliente().getNombre();
        String entidad = this.modelo.cabecera.getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.cabecera.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbVentaDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbVentaDetalle.setEnabled(true);
            }
            if (this.vista.jbFacturacionDetalle.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbFacturacionDetalle.setEnabled(true);
            }
        }
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroFactura.setText("");
        this.vista.jcbCondVenta.setSelectedIndex(0);
    }

    private void consultarFacturaciones() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //VALIDAR NRO FACTURA
                try {
                    if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                        int nroFac = Integer.valueOf(vista.jtfNroFactura.getText());
                        modelo.getCabecera().setNroFactura(nroFac);
                    } else {
                        modelo.getCabecera().setNroFactura(-1);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un número entero válido para Nro. factura", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //VALIDAR FECHA
                if (!modelo.validarFechas(vista.jddInicio.getDate(), vista.jddFinal.getDate())) {
                    vista.jddFinal.setDate(vista.jddInicio.getDate());
                    vista.jddFinal.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                int idTipoOperacion = vista.jcbCondVenta.getItemAt(vista.jcbCondVenta.getSelectedIndex()).getId();
                vista.jtFacturacion.setModel(modelo.obtenerFacturacion(fechaInicio, fechaFinal, idTipoOperacion));
                Utilities.c_packColumn.packColumns(vista.jtFacturacion, 1);
            }
        });
    }

    private void consultarVentas() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int fila = vista.jtFacturacion.getSelectedRow();
                if (fila > -1) {
                    int idFacturacion = Integer.valueOf(String.valueOf(vista.jtFacturacion.getValueAt(fila, 0).toString()));
                    vista.jtVentas.setModel(modelo.obtenerVentasPorFacturacion(idFacturacion));
                    Utilities.c_packColumn.packColumns(vista.jtVentas, 1);
                }
            }
        });
    }

    private void facturacionMouseHandler(MouseEvent e) {
        int fila = this.vista.jtFacturacion.rowAtPoint(e.getPoint());
        int columna = this.vista.jtFacturacion.columnAtPoint(e.getPoint());
        Integer idFacturacion = Integer.valueOf(String.valueOf(this.vista.jtFacturacion.getValueAt(fila, 0)));
        if ((fila > -1) && (columna > -1)) {
            //TODO add verificarPermiso();
            this.vista.jtVentas.setModel(modelo.obtenerVentasPorFacturacion(idFacturacion));
            this.vista.jbFacturacionDetalle.setEnabled(true);//TODO si tiene permiso
        } else {
            this.vista.jbFacturacionDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbFacturacionDetalle.isEnabled()) {
                facturacionDetalle();
                this.vista.jbFacturacionDetalle.setEnabled(false);
            }
        }
    }

    private void ventasMouseHandler(MouseEvent e) {
        int fila = this.vista.jtVentas.rowAtPoint(e.getPoint());
        int columna = this.vista.jtVentas.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            //TODO add verificarPermiso();
            this.vista.jbVentaDetalle.setEnabled(true);//TODO si tiene permiso
        } else {
            this.vista.jbVentaDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbVentaDetalle.isEnabled()) {
                ventaDetalle();
                this.vista.jbVentaDetalle.setEnabled(false);
            }
        }
    }

    private void facturacionDetalle() {
        int fila = vista.jtFacturacion.getSelectedRow();
        if (fila > -1) {
            Integer idFacturacion = Integer.valueOf(String.valueOf(this.vista.jtFacturacion.getValueAt(fila, 0)));
            FacturacionCabeceraTableModel tm = (FacturacionCabeceraTableModel) this.vista.jtFacturacion.getModel();
            E_facturacionCabecera facturacionCabecera = tm.getFacturacionCabeceraList().get(fila);
            Resumen_ingreso re = new Resumen_ingreso(c_inicio);
            re.inicializarDatos(facturacionCabecera);
            re.setVisible(true);
        }
    }

    private void ventaDetalle() {
        int fila = this.vista.jtVentas.getSelectedRow();
        if (fila > -1) {
            //verificarPermiso();
            Integer idVentaCabecera = Integer.valueOf(String.valueOf(this.vista.jtVentas.getValueAt(fila, 0)));
            Ver_ingreso ver_egreso = new Ver_ingreso(c_inicio, idVentaCabecera);
            ver_egreso.mostrarVista();
            this.vista.jbVentaDetalle.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbBuscar)) {
            consultarFacturaciones();
        }
        if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        }
        if (source.equals(this.vista.jbFacturacionDetalle)) {
            facturacionDetalle();
        }
        if (source.equals(this.vista.jbVentaDetalle)) {
            ventaDetalle();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jtFacturacion)) {
            //verificarPermiso();
            facturacionMouseHandler(e);
        }
        if (source.equals(this.vista.jtVentas)) {
            ventasMouseHandler(e);
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
                break;
            }
            case KeyEvent.VK_F2: {
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
        if (this.vista.jtFacturacion.hasFocus()) {
            consultarVentas();
        }
    }
}
