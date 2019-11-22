/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Entities.E_facturaCabeceraFX;
import Entities.M_facturaDetalle;
import Interface.InterfaceFacturaDetalle;
import Interface.InterfaceSeleccionVentaCabecera;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.SeleccionVentaCabecera;
import ModeloTabla.SeleccionVentaCabeceraTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class C_facturacion implements ActionListener, KeyListener, InterfaceSeleccionVentaCabecera, InterfaceFacturaDetalle {

    V_facturacion vista;
    M_facturacion modelo;

    public C_facturacion(M_facturacion modelo, V_facturacion vista) {
        this.modelo = modelo;
        this.vista = vista;
        completarCampos();
        concederPermisos();
    }

    private void completarCampos() {
        SeleccionVentaCabeceraTableModel tm = new SeleccionVentaCabeceraTableModel(this);
        ArrayList<SeleccionVentaCabecera> list = new ArrayList<>();
        for (E_facturaCabeceraFX ventaCabecera : modelo.obtenerVentasCabecera()) {
            list.add(new SeleccionVentaCabecera(ventaCabecera, false));
        }
        tm.setList(list);
        this.vista.jtVentasCabecera.setModel(tm);
        this.vista.jtfTotalItems.setText(V_facturacion.TOTAL_ITEMS + "0");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbFacturar)) {
            facturar();
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
            this.vista.jtVentasCabecera.getModel().setValueAt(false, i, 5);
        }
        modelo.setAgregarTodos(false);
        consultarDetalle();
    }

    private void seleccionarTodos() {
        modelo.setAgregarTodos(true);
        int rows = this.vista.jtVentasCabecera.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.vista.jtVentasCabecera.getModel().setValueAt(true, i, 5);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void consultarDetalle() {
        SeleccionVentaCabeceraTableModel vctm = (SeleccionVentaCabeceraTableModel) vista.jtVentasCabecera.getModel();
        FacturaDetalleTableModel tm = new FacturaDetalleTableModel(this);
        ArrayList<M_facturaDetalle> list = new ArrayList<>();
        for (M_facturaDetalle ventaCabecera : modelo.obtenerVentasDetalle(vctm.getList())) {
            list.add(ventaCabecera);
        }
        tm.setFacturaDetalleList(list);
        this.vista.jtVentasDetalle.setModel(tm);
        this.vista.jtfTotalItems.setText(V_facturacion.TOTAL_ITEMS + list.size()+" | "+V_facturacion.TOTAL_ITEMS);
        Utilities.c_packColumn.packColumns(this.vista.jtVentasDetalle, 1);
    }

    @Override
    public void notificarCambio() {
    }

    @Override
    public void notificarCambioSeleccion() {
        if (!modelo.isAgregarTodos()) {
            consultarDetalle();
        }
    }
}
