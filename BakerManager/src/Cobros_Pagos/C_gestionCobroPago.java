/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.GestionInterface;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionCobroPago implements GestionInterface, RecibirEmpleadoCallback, RecibirClienteCallback {

    V_gestionCobroPago vista;
    M_gestionCobroPago modelo;
    public C_inicio c_inicio;

    public C_gestionCobroPago(V_gestionCobroPago vista, M_gestionCobroPago modelo, C_inicio c_inicio) {
        this.vista = vista;
        this.modelo = modelo;
        this.c_inicio = c_inicio;
        this.vista.setLocation(c_inicio.centrarPantalla(this.vista));
        callMethods();
    }

    private void callMethods() {
        inicializarVista();
        concederPermisos();
    }

    @Override
    public void inicializarVista() {
        Date date = Calendar.getInstance().getTime();
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);

        this.vista.jddInicioPago.setDate(date);
        this.vista.jddFinalPago.setDate(date);
        ArrayList<E_tipoOperacion> tipoOperaciones = modelo.obtenerTipoOperacion();
        for (int i = 0; i < tipoOperaciones.size(); i++) {
            this.vista.jcbCondVenta.addItem(tipoOperaciones.get(i));
        }

    }

    @Override
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
        this.vista.jbBuscarCobro.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpCobro.addActionListener(this);
        this.vista.jbBorrarCobro.addActionListener(this);
        this.vista.jbCobroPendientes.addActionListener(this);
        //END TODO
        this.vista.jtCobroCabecera.addMouseListener(this);
        this.vista.jtCobroCabecera.addKeyListener(this);
        this.vista.jtPago.addMouseListener(this);
        this.vista.jtPago.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        //cobro
        this.vista.jbCobro.addKeyListener(this);
        this.vista.jbDetalleCobro.addKeyListener(this);
        this.vista.jbBuscarCobro.addKeyListener(this);
        this.vista.jbEmpCobro.addKeyListener(this);
        this.vista.jbBorrarCobro.addKeyListener(this);
        //pago
        this.vista.jbPago.addKeyListener(this);
        this.vista.jbDetallePago.addKeyListener(this);
        this.vista.jbBuscarPago.addKeyListener(this);
        this.vista.jbEmpPago.addKeyListener(this);
        this.vista.jbBorrarPago.addKeyListener(this);
    }

    @Override
    public void mostrarVista() {
        this.c_inicio.agregarVentana(vista);
    }

    @Override
    public void cerrar() {
        try {
            this.vista.setClosed(true);
        } catch (PropertyVetoException ex) {
        }
    }

    private void consultarCobros() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                        int nroFac = Integer.valueOf(vista.jtfNroFactura.getText());
                        modelo.getFacturaCabecera().setNroFactura(nroFac);
                    } else {
                        modelo.getFacturaCabecera().setIdFacturaCabecera(null);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un número entero válido para Nro. factura", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (validarFechas(vista.jddInicioCobro.getDate(), vista.jddFinalCobro.getDate())) {
                    //Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                    Date fechaInicio = vista.jddInicioCobro.getDate();
                    Date fechaFinal = vista.jddFinalCobro.getDate();
                    E_tipoOperacion condCompra = vista.jcbCondVenta.getItemAt(vista.jcbCondVenta.getSelectedIndex());
                    String nroFactura = vista.jtfNroFactura.getText().trim();
                    M_cliente cliente = modelo.getCliente();
                    M_funcionario funcionario = modelo.getFuncionario();
                    vista.jtCobroCabecera.setModel(modelo.obtenerCobro(cliente, funcionario, fechaInicio, fechaFinal, condCompra, nroFactura));
                    Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
                } else {
                    vista.jddFinalCobro.setDate(vista.jddInicioCobro.getDate());
                    vista.jddFinalCobro.updateUI();
                    JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void consultarCobrosPendiente() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                E_tipoOperacion condCompra = new E_tipoOperacion(-1, 0, "Todos");
                M_cliente cliente = modelo.getCliente();
                vista.jtCobroCabecera.setModel(modelo.obtenerCobroPendiente(cliente, condCompra));
                Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
            }
        });
    }

    private void invocarVistaSaldarCaja() {
        //TO DO
    }

    private void invocarVistaVerCaja() {
        int fila = this.vista.jtCobroCabecera.getSelectedRow();
        int columna = this.vista.jtCobroCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idCaja = Integer.valueOf(String.valueOf(this.vista.jtCobroCabecera.getValueAt(fila, 0)));
            ////TO DO
        }
        this.vista.jbDetalleCobro.setEnabled(false);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        System.out.println("Cobros_Pagos.C_gestionCobroPago.recibirFuncionario()");
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpCobro.setText(this.modelo.obtenerNombreFuncionario());
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.setCliente(cliente);
        this.vista.jtfCliente.setText(this.modelo.obtenerNombreCliente());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.vista.jtfEmpCobro.setText("");
        this.vista.jtfCliente.setText("");
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);
    }

    private boolean validarFechas(Date f_inicio, Date f_final) {
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
        if (src.equals(this.vista.jbCobro)) {
            invocarVistaSaldarCaja();
        } else if (src.equals(this.vista.jbBuscarCobro)) {
            consultarCobros();
        } else if (src.equals(this.vista.jbEmpCobro)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarCobro)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetalleCobro)) {
            invocarVistaVerCaja();
        } else if (src.equals(this.vista.jbCobroPendientes)) {
            consultarCobrosPendiente();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtCobroCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCobroCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbDetalleCobro.setEnabled(true);
            if (e.getClickCount() == 2) {
                invocarVistaVerCaja();
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
            case KeyEvent.VK_F1: {
                if (vista.jbCobro.isEnabled()) {
                    invocarVistaSaldarCaja();
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
