/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Cliente.SeleccionarCliente;
import Egresos.Ver_Egresos;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Entities.M_proveedor;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProveedorCallback;
import MenuPrincipal.DatosUsuario;
import Proveedor.Seleccionar_proveedor;
import Ventas.VerIngreso;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_pagoPendiente extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback, RecibirProveedorCallback, InterfaceNotificarCambio {

    V_pagoPendiente vista;
    M_pagoPendiente modelo;

    public C_pagoPendiente(M_pagoPendiente modelo, V_pagoPendiente vista) {
        this.vista = vista;
        this.modelo = modelo;
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
    }

    public void inicializarVista() {
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicioPago.setDate(date);
        this.vista.jddFinalPago.setDate(date);
        /*ArrayList<E_tipoOperacion> estados = modelo.obtenerTipoOperacion();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbTipoOperacion.addItem(estados.get(i));
        }*/
        this.vista.jtPagoCabecera.setModel(this.modelo.getTm());
        this.vista.jtPagoDetalle.setModel(this.modelo.getTmDetalle());
    }

    public void mostraVista() {
        this.vista.setVisible(true);
    }

    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        /*for (M_menu_item acceso : accesos) {
            if (this.vista.jbCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCobro.setEnabled(true);
                this.vista.jbCobro.addActionListener(this);
            }
            if (this.vista.jbDetalleCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetalleCobro.addActionListener(this);
            }
            if (this.vista.jbBuscarCobro.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscarCobro.setEnabled(true);
                this.vista.jddFinalCobro.setEnabled(true);
                this.vista.jddInicioCobro.setEnabled(true);
                this.vista.jbBuscarCobro.addActionListener(this);
                this.vista.jbEmpCobro.addActionListener(this);
                this.vista.jbBorrarCobro.addActionListener(this);
            }
            //PAGO
            if (this.vista.jbPago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbPago.setEnabled(true);
                this.vista.jbPago.addActionListener(this);
            }
            if (this.vista.jbDetallePago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbDetallePago.addActionListener(this);
            }
            if (this.vista.jbBuscarPago.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBuscarPago.setEnabled(true);
                this.vista.jddFinalPago.setEnabled(true);
                this.vista.jddInicioPago.setEnabled(true);
                this.vista.jbBuscarPago.addActionListener(this);
                this.vista.jbEmpPago.addActionListener(this);
                this.vista.jbBorrarPago.addActionListener(this);
            }
        }*/
        //TODO conceder permisos
        this.vista.jbResumen.addActionListener(this);
        this.vista.jbDetallePago.addActionListener(this);
        this.vista.jbBuscarPago.addActionListener(this);
        this.vista.jbBuscarPendiente.addActionListener(this);
        this.vista.jbProveedor.addActionListener(this);
        this.vista.jbBorrarPago.addActionListener(this);
        //END TODO
        this.vista.jtPagoCabecera.addMouseListener(this);
        this.vista.jtPagoCabecera.addKeyListener(this);

        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbDetallePago.addKeyListener(this);
        this.vista.jbBuscarPago.addKeyListener(this);
        this.vista.jbBuscarPendiente.addKeyListener(this);
        this.vista.jbBorrarPago.addKeyListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void consultarCobros() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarNroFactura()) {
                    return;
                }
                if (!validarFechas(vista.jddInicioPago.getDate(), vista.jddFinalPago.getDate())) {
                    return;
                }
                Date fechaInicio = vista.jddInicioPago.getDate();
                Date fechaFinal = vista.jddFinalPago.getDate();
                //E_tipoOperacion condCompra = vista.jcbTipoOperacion.getItemAt(vista.jcbTipoOperacion.getSelectedIndex());
                int nroFactura = -1;
                if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                    nroFactura = Integer.valueOf(vista.jtfNroFactura.getText().trim());
                }
                M_proveedor proveedor = modelo.getCabecera().getProveedor();
                //M_funcionario funcionario = modelo.getFacturaCabecera().getFuncionario();

                modelo.getTm().setList(modelo.obtenerCobroPendiente(proveedor, fechaInicio, fechaFinal, nroFactura, true));
                Utilities.c_packColumn.packColumns(vista.jtPagoCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void consultarCobrosPendiente() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date fechaInicio = vista.jddInicioPago.getDate();
                Date fechaFinal = vista.jddFinalPago.getDate();
                int nroFactura = -1;
                M_proveedor proveedor = modelo.getCabecera().getProveedor();

                modelo.getTm().setList(modelo.obtenerCobroPendiente(proveedor, fechaInicio, fechaFinal, nroFactura, false));
                Utilities.c_packColumn.packColumns(vista.jtPagoCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void invocarVistaResumen() {
        ResumenPagoPendiente cc = new ResumenPagoPendiente(vista, modelo.getTm());
        cc.mostrarVista();
    }

    private void invocarVistaVerDetalle() {
        int fila = this.vista.jtPagoCabecera.getSelectedRow();
        int columna = this.vista.jtPagoCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idFactura = (int) this.vista.jtPagoCabecera.getValueAt(fila, 0);
            Ver_Egresos ve = new Ver_Egresos(this.vista, idFactura);
            ve.mostrarVista();
        }
        this.vista.jbDetallePago.setEnabled(false);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        /*this.modelo.getFacturaCabecera().setFuncionario(funcionario);
        this.vista.jtfEmpCobro.setText(this.modelo.obtenerNombreFuncionario());*/
    }

    @Override
    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.getCabecera().setProveedor(proveedor);
        this.vista.jtfProveedor.setText(this.modelo.obtenerNombreProveedor());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.modelo.limpiarDetalle();
        this.vista.jtfProveedor.setText("");
        this.vista.jddInicioPago.setDate(date);
        this.vista.jddFinalPago.setDate(date);
    }

    private boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            } else {
                Date today = Calendar.getInstance().getTime();
                vista.jddInicioPago.setDate(today);
                vista.jddFinalPago.setDate(today);
                vista.jddInicioPago.updateUI();
                vista.jddFinalPago.updateUI();
                JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private boolean validarNroFactura() {
        try {
            if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                int nroFac = Integer.valueOf(vista.jtfNroFactura.getText());
                modelo.getCabecera().setNro_factura(nroFac);
            } else {
                modelo.getCabecera().setId_cabecera(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número entero válido para Nro. factura", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCliente() {

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbBuscarPago)) {
            consultarCobros();
        } else if (src.equals(this.vista.jbBuscarPendiente)) {
            consultarCobrosPendiente();
        } else if (src.equals(this.vista.jbProveedor)) {
            Seleccionar_proveedor sc = new Seleccionar_proveedor(this.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarPago)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetallePago)) {
            invocarVistaVerDetalle();
        } else if (src.equals(this.vista.jbResumen)) {
            invocarVistaResumen();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtPagoCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtPagoCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            Integer idCabecera = Integer.valueOf(String.valueOf(this.vista.jtPagoCabecera.getValueAt(fila, 0)));
            this.vista.jbDetallePago.setEnabled(true);
            this.modelo.actualizarDetalle(idCabecera);
            if (e.getClickCount() == 2) {
                invocarVistaVerDetalle();
            }
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
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void notificarCambio() {
        JOptionPane.showMessageDialog(vista, "Cobro registrado con éxito");

    }

}
