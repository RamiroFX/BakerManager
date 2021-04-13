/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import Configuracion.Timbrado.SeleccionarNroFactura;
import Entities.E_Timbrado;
import Entities.E_facturaCabecera;
import Entities.E_facturacionCabecera;
import Entities.M_facturaCabecera;
import Entities.E_facturaDetalle;
import Impresora.Impresora;
import Interface.InterfaceConfirmarFacturacion;
import Interface.InterfaceFacturaDetalle;
import Interface.InterfaceSeleccionVentaCabecera;
import Interface.RecibirTimbradoVentaCallback;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.SeleccionVentaCabecera;
import ModeloTabla.SeleccionVentaCabeceraTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_facturacion implements ActionListener, KeyListener, InterfaceSeleccionVentaCabecera,
        InterfaceFacturaDetalle, InterfaceConfirmarFacturacion, RecibirTimbradoVentaCallback {

    private static final String MESSAGE = "La cantidad actual sobrepasa el limite permitido ", TITLE = "Atención";
    private static final String MESSAGE2 = "Seleccione por lo menos una venta";
    private static final String ATENCION = "Atención";
    private static final String IMPRIMIR_VENTA = "¿Desea imprimir la factura?";
    V_facturacion vista;
    M_facturacion modelo;

    public C_facturacion(M_facturacion modelo, V_facturacion vista) {
        this.modelo = modelo;
        this.vista = vista;
        completarCampos();
        concederPermisos();
    }

    private void completarCampos() {
        modelo.getTm().setInterface(this);
        modelo.getTmd().setInterface(this);
        ArrayList<SeleccionVentaCabecera> list = new ArrayList<>();
        for (E_facturaCabecera ventaCabecera : modelo.obtenerVentasCabecera()) {
            list.add(new SeleccionVentaCabecera(ventaCabecera, false));
        }
        modelo.getTm().setList(list);
        this.vista.jtVentasCabecera.setModel(modelo.getTm());
        this.vista.jtVentasDetalle.setModel(modelo.getTmd());
        this.vista.jftTotalItems.setValue(0);
        this.vista.jftTotal.setValue(0);
        Utilities.c_packColumn.packColumns(this.vista.jtVentasCabecera, 1);
    }

    private void concederPermisos() {
        this.vista.jbFacturar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jbAgregar.addActionListener(this);
        this.vista.jbQuitar.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.vista.jbFacturar.addKeyListener(this);
        this.vista.jbCancelar.addKeyListener(this);
        this.vista.jbAgregar.addKeyListener(this);
        this.vista.jbQuitar.addKeyListener(this);
        this.vista.jtVentasCabecera.addKeyListener(this);
    }

    boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    private void seleccionarNroFactura() {
        E_Timbrado timbradoPred = modelo.obtenerTimbradoPredeterminado();
        SeleccionarNroFactura snf = new SeleccionarNroFactura(vista, this, timbradoPred);
        snf.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbFacturar)) {
            seleccionarNroFactura();
        } else if (src.equals(this.vista.jbQuitar)) {
            quitarTodos();
        } else if (src.equals(this.vista.jbAgregar)) {
            seleccionarTodos();
        } else if (src.equals(this.vista.jbCancelar)) {
            cerrar();
        }

    }

    private void quitarTodos() {
        modelo.setAgregarTodos(true);
        int rows = this.vista.jtVentasCabecera.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            modelo.getTmd().setValueAt(false, rows, 7);
        }
        modelo.setAgregarTodos(false);
        consultarDetalle();
    }

    private void seleccionarTodos() {
        modelo.setAgregarTodos(true);
        int rows = this.vista.jtVentasCabecera.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            modelo.getTmd().setValueAt(true, rows, 7);
        }
        modelo.setAgregarTodos(false);
        consultarDetalle();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
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

    private void facturar() {
        if (!controlarCantidadItems()) {
            return;
        }
        ConfirmarFacturacion cf = new ConfirmarFacturacion(vista);
        cf.setInterfaceConfirmarFacturacion(this);
        cf.inicializarVista(modelo.obtenerCliente().getEntidad(), modelo.obtenerTipoOperacion().getDescripcion());
        cf.mostrarVista();
    }

    private boolean verificarTimbrados() {
        String mensaje = modelo.verificarTimbrados();
        if (mensaje.equals("OK")) {
            return true;
        } else {
            int opcion = JOptionPane.showConfirmDialog(vista, mensaje, "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (opcion != JOptionPane.YES_OPTION) {
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean controlarCantidadItems() {
        FacturaDetalleTableModel fdtm = (FacturaDetalleTableModel) vista.jtVentasDetalle.getModel();
        int cantidadMaxima = Impresora.PREF_PRINT_FACTURA.getMaxProducts();
        int cantidadActual = fdtm.getRowCount();
        if (cantidadActual > cantidadMaxima) {
            JOptionPane.showMessageDialog(vista, MESSAGE + " (" + cantidadMaxima + ")", TITLE, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cantidadActual <= 0) {
            JOptionPane.showMessageDialog(vista, MESSAGE2, TITLE, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void consultarDetalle() {
        FacturaDetalleTableModel tm = new FacturaDetalleTableModel(this);
        ArrayList<E_facturaDetalle> list = new ArrayList<>();
        double total = 0;
        for (E_facturaDetalle ventaDetalle : modelo.obtenerVentasDetalle(modelo.getTm().getList())) {
            list.add(ventaDetalle);
            total = total + ventaDetalle.calcularSubTotal();
        }
        tm.setFacturaDetalleList(list);
        this.vista.jtVentasDetalle.setModel(tm);
        this.vista.jftTotalItems.setValue(list.size());
        this.vista.jftTotal.setValue(total);
        Utilities.c_packColumn.packColumns(this.vista.jtVentasDetalle, 1);
    }

    @Override
    public void notificarCambioFacturaDetalle() {
    }

    @Override
    public void notificarCambioSeleccion() {
        if (!modelo.isAgregarTodos()) {
            consultarDetalle();
        }
    }

    @Override
    public void recibirFacturacion(E_facturacionCabecera facturacionCabecera) {
        ArrayList<E_facturaCabecera> facalist = new ArrayList<>();
        SeleccionVentaCabeceraTableModel vctm = (SeleccionVentaCabeceraTableModel) vista.jtVentasCabecera.getModel();
        ArrayList<SeleccionVentaCabecera> sefacalist = vctm.getList();
        for (int i = 0; i < sefacalist.size(); i++) {
            SeleccionVentaCabecera get = sefacalist.get(i);
            if (get.isEstaSeleccionado()) {
                facalist.add(get.getFacturaCabecera());
            }
        }
        //modelo.facturar(facalist, facturacionCabecera.getNroFactura(), modelo.obtenerTipoOperacion().getId());
        //IMPRIMIR FACTURA
        int opcion = JOptionPane.showConfirmDialog(vista, IMPRIMIR_VENTA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            M_facturaCabecera facturaCabecera = new M_facturaCabecera();
            facturaCabecera.setCliente(modelo.obtenerCliente());
            facturaCabecera.setNroFactura(facturacionCabecera.getNroFactura());
            facturaCabecera.setIdCondVenta(modelo.obtenerTipoOperacion().getId());
            Calendar c = Calendar.getInstance();
            facturaCabecera.setTiempo(new Timestamp(c.getTimeInMillis()));
            FacturaDetalleTableModel tm = (FacturaDetalleTableModel) vista.jtVentasDetalle.getModel();
            ArrayList<E_facturaDetalle> facturaDetalle = (ArrayList<E_facturaDetalle>) tm.getList();
            Impresora.imprimirFacturaVenta(facturaCabecera, facturaDetalle);
        }
        completarCampos();
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura) {
        if (!verificarTimbrados()) {
            return;
        }
        ArrayList<E_facturaCabecera> facalist = new ArrayList<>();
        ArrayList<SeleccionVentaCabecera> sefacalist = modelo.getTm().getList();
        for (int i = 0; i < sefacalist.size(); i++) {
            SeleccionVentaCabecera get = sefacalist.get(i);
            if (get.isEstaSeleccionado()) {
                facalist.add(get.getFacturaCabecera());
            }
        }
        modelo.facturar(facalist, timbrado.getId(), nroFactura, modelo.obtenerTipoOperacion().getId());
        //IMPRIMIR FACTURA
        int opcion = JOptionPane.showConfirmDialog(vista, IMPRIMIR_VENTA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            M_facturaCabecera facturaCabecera = new M_facturaCabecera();
            facturaCabecera.setCliente(modelo.obtenerCliente());
            facturaCabecera.setNroFactura(nroFactura);
            facturaCabecera.setIdCondVenta(modelo.obtenerTipoOperacion().getId());
            Calendar c = Calendar.getInstance();
            facturaCabecera.setTiempo(new Timestamp(c.getTimeInMillis()));
            ArrayList<E_facturaDetalle> facturaDetalle = (ArrayList<E_facturaDetalle>) modelo.getTmd().getList();
            Impresora.imprimirFacturaVenta(facturaCabecera, facturaDetalle);
        }
        completarCampos();
    }
}
