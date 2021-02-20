/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Cobros.BancosParametros;
import Cobros.ChequesPendientes;
import Empleado.SeleccionarFuncionario;
import Entities.E_cuentaCorrienteConcepto;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Entities.M_proveedor;
import Interface.GestionInterface;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProveedorCallback;
import MenuPrincipal.DatosUsuario;
import Pagos.BuscarCheques.BuscarChequesPagos;
import Proveedor.Seleccionar_proveedor;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionPago implements GestionInterface, RecibirEmpleadoCallback, RecibirProveedorCallback, InterfaceNotificarCambio {

    V_gestionPago vista;
    M_gestionPago modelo;
    public C_inicio c_inicio;

    public C_gestionPago(V_gestionPago vista, M_gestionPago modelo, C_inicio c_inicio) {
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
        this.vista.jddInicioPago.setDate(date);
        this.vista.jddFinalPago.setDate(date);
//
//        this.vista.jddInicioPago.setDate(date);
//        this.vista.jddFinalPago.setDate(date);
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        this.vista.jtPagoCabecera.setModel(this.modelo.getTm());
        this.vista.jtPagoDetalle.setModel(this.modelo.getTmDetalle());
    }

    @Override
    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
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
                this.vista.jbEmpCobro.addActionListener(this);
                this.vista.jbProveedor.addActionListener(this);
                this.vista.jbBorrarPago.addActionListener(this);
                this.vista.jbBorrarPago.addActionListener(this);
            }
            //PAGO
            if (this.vista.jbAnular.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAnular.addActionListener(this);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbPagoPendientes.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbPagoPendientes.setEnabled(true);
                this.vista.jbPagoPendientes.addActionListener(this);
            }
            if (this.vista.jbBanco.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBanco.setEnabled(true);
                this.vista.jbBanco.addActionListener(this);
            }
            if (this.vista.jbCheques.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCheques.setEnabled(true);
                this.vista.jbCheques.addActionListener(this);
            }
        }
        this.vista.jbMasOpciones.addActionListener(this);
        //TODO conceder permisos
        //this.vista.jbCobro.addActionListener(this);
        //this.vista.jbDetalleCobro.addActionListener(this);
        //this.vista.jbBuscarCobro.addActionListener(this);
        //this.vista.jbCliente.addActionListener(this);
        //this.vista.jbEmpCobro.addActionListener(this);
        //this.vista.jbAnular.addActionListener(this);
        //this.vista.jbResumen.addActionListener(this);
        //this.vista.jbBorrarCobro.addActionListener(this);
        //this.vista.jbBanco.addActionListener(this);
        //this.vista.jbCobroPendientes.addActionListener(this);
        //END TODO
        this.vista.jtPagoCabecera.addMouseListener(this);
        this.vista.jtPagoCabecera.addKeyListener(this);

        /**
         * **ESCAPE HOTKEY/
         */
        //cobro
        this.vista.jbPago.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbBanco.addKeyListener(this);
        this.vista.jbDetallePago.addKeyListener(this);
        this.vista.jbBuscarPago.addKeyListener(this);
        this.vista.jbEmpCobro.addKeyListener(this);
        this.vista.jbBorrarPago.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbPago.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbPago.setEnabled(true);
            }
            if (this.vista.jbDetallePago.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbDetallePago.setEnabled(true);
            }
            if (this.vista.jbAnular.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbAnular.setEnabled(true);
            }
            if (this.vista.jbCheques.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCheques.setEnabled(true);
            }
        }
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

    private void consultarPagos() {
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
                int nroRecibo = -1;
                if (!vista.jtfNroRecibo.getText().trim().isEmpty()) {
                    nroRecibo = Integer.valueOf(vista.jtfNroRecibo.getText().trim());
                }
                M_proveedor proveedor = modelo.getProveedor();
                M_funcionario funcionario = modelo.getFuncionario();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());

                modelo.getTm().setList(modelo.obtenerPago(proveedor, funcionario, fechaInicio, fechaFinal, estado, nroRecibo));
                Utilities.c_packColumn.packColumns(vista.jtPagoCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void consultarCobrosPendiente() {
        PagoPendiente pp = new PagoPendiente(this.c_inicio.vista);
        pp.mostrarVista();
    }

    private void invocarVistaPagar() {
        CrearPago cp = new CrearPago(c_inicio);
        cp.mostrarVista();
    }

    private void anularPago() {
        int fila = this.vista.jtPagoCabecera.getSelectedRow();
        if (fila < 0) {
            return;
        }
        if (modelo.getCobroEstado(fila).getId() == Estado.INACTIVO) {
            JOptionPane.showMessageDialog(vista, "El pago ya se encuentra anulado", "Atenciòn", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Esta seguro que desea continuar?", "Atenciòn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Integer idCabecera = Integer.valueOf(String.valueOf(this.vista.jtPagoCabecera.getValueAt(fila, 0)));
            this.modelo.anularPago(idCabecera);
            consultarPagos();
            modelo.limpiarDetalle();
            Utilities.c_packColumn.packColumns(this.vista.jtPagoCabecera, 1);
            this.vista.jbDetallePago.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void invocarVistaResumen() {
        ResumenPago cc = new ResumenPago(c_inicio.vista, modelo.getTm());
        cc.mostrarVista();
    }

    private void invocarVistaBancos() {
        BancosParametros bp = new BancosParametros(this.c_inicio.vista);
        bp.setVisible(true);
    }

    private void invocarVistaChequesPendientes() {
        ChequesPendientes bp = new ChequesPendientes(this.c_inicio.vista, E_cuentaCorrienteConcepto.COMPRAS);
        bp.setVisible(true);
    }

    private void invocarVistaVerDetalle() {
        int fila = this.vista.jtPagoCabecera.getSelectedRow();
        int columna = this.vista.jtPagoCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int index = this.vista.jtPagoCabecera.getSelectedRow();
            VerPago vc = new VerPago(this.c_inicio.vista);
            vc.setReciboPagoCabecera(modelo.getTm().getList().get(index));
            vc.mostrarVista();
        }
        this.vista.jbDetallePago.setEnabled(false);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpCobro.setText(this.modelo.obtenerNombreFuncionario());
    }

    @Override
    public void recibirProveedor(M_proveedor proveedor) {
        this.modelo.setProveedor(proveedor);
        this.vista.jtfCliente.setText(this.modelo.obtenerNombreProveedor());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.vista.jtfEmpCobro.setText("");
        this.vista.jtfCliente.setText("");
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
            if (!vista.jtfNroRecibo.getText().trim().isEmpty()) {
                int nroFac = Integer.valueOf(vista.jtfNroRecibo.getText());
                modelo.getFacturaCabecera().setNroFactura(nroFac);
            } else {
                modelo.getFacturaCabecera().setNroFactura(null);
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

    private void invocarChequesEmitidos() {
        BuscarChequesPagos bcp = new BuscarChequesPagos(c_inicio.vista);
        bcp.mostrarVista();
    }

    private void invocarMasOpciones() {
        Object[] options = {"Estado de cuenta", "Bancos", "Cheques emitidos"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga su opción",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(vista, "Implementando", "Estado de cuenta", JOptionPane.PLAIN_MESSAGE);
                    }
                });
                break;
            }
            case 1: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        invocarVistaBancos();
                    }
                });
                break;
            }
            case 2: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        invocarChequesEmitidos();
                    }
                });
                break;
            }
            default: {

                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbPago)) {
            invocarVistaPagar();
        } else if (src.equals(this.vista.jbBuscarPago)) {
            consultarPagos();
        } else if (src.equals(this.vista.jbAnular)) {
            anularPago();
        } else if (src.equals(this.vista.jbMasOpciones)) {
            invocarMasOpciones();
        } else if (src.equals(this.vista.jbCheques)) {
            invocarVistaChequesPendientes();
        } else if (src.equals(this.vista.jbEmpCobro)) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbProveedor)) {
            Seleccionar_proveedor sp = new Seleccionar_proveedor(this.c_inicio.vista);
            sp.setCallback(this);
            sp.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarPago)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetallePago)) {
            invocarVistaVerDetalle();
        } else if (src.equals(this.vista.jbResumen)) {
            invocarVistaResumen();
        } else if (src.equals(this.vista.jbPagoPendientes)) {
            consultarCobrosPendiente();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtPagoCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtPagoCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            int idCabecera = modelo.getTm().getList().get(fila).getId();
            /*this.vista.jbCobro.setEnabled(true);
            this.vista.jbDetalleCobro.setEnabled(true);
            this.vista.jbAnular.setEnabled(true);*/
            verificarPermiso();
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
            case KeyEvent.VK_F1: {
                if (vista.jbPago.isEnabled()) {
                    invocarVistaPagar();
                }
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
