/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cliente.Seleccionar_cliente;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import MenuPrincipal.DatosUsuario;
import Ventas.Ver_ingreso;
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
public class C_cobroPendiente extends MouseAdapter implements ActionListener, KeyListener, RecibirEmpleadoCallback, RecibirClienteCallback, InterfaceNotificarCambio {

    V_cobroPendiente vista;
    M_cobroPendiente modelo;

    public C_cobroPendiente(M_cobroPendiente modelo, V_cobroPendiente vista) {
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
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);
        /*ArrayList<E_tipoOperacion> estados = modelo.obtenerTipoOperacion();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbTipoOperacion.addItem(estados.get(i));
        }*/
        this.vista.jtCobroCabecera.setModel(this.modelo.getTm());
        this.vista.jtCobroDetalle.setModel(this.modelo.getTmDetalle());
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
        this.vista.jbDetalleCobro.addActionListener(this);
        this.vista.jbBuscarCobro.addActionListener(this);
        this.vista.jbBuscarPendiente.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbBorrarCobro.addActionListener(this);
        //END TODO
        this.vista.jtCobroCabecera.addMouseListener(this);
        this.vista.jtCobroCabecera.addKeyListener(this);

        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jbDetalleCobro.addKeyListener(this);
        this.vista.jbBuscarCobro.addKeyListener(this);
        this.vista.jbBuscarPendiente.addKeyListener(this);
        this.vista.jbBorrarCobro.addKeyListener(this);
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
                if (!validarFechas(vista.jddInicioCobro.getDate(), vista.jddFinalCobro.getDate())) {
                    return;
                }
                Date fechaInicio = vista.jddInicioCobro.getDate();
                Date fechaFinal = vista.jddFinalCobro.getDate();
                //E_tipoOperacion condCompra = vista.jcbTipoOperacion.getItemAt(vista.jcbTipoOperacion.getSelectedIndex());
                int nroFactura = -1;
                if (!vista.jtfNroFactura.getText().trim().isEmpty()) {
                    nroFactura = Integer.valueOf(vista.jtfNroFactura.getText().trim());
                }
                M_cliente cliente = modelo.getFacturaCabecera().getCliente();
                //M_funcionario funcionario = modelo.getFacturaCabecera().getFuncionario();

                modelo.getTm().setList(modelo.obtenerCobroPendiente(cliente, fechaInicio, fechaFinal, nroFactura, true));
                Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void consultarCobrosPendiente() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Date fechaInicio = vista.jddInicioCobro.getDate();
                Date fechaFinal = vista.jddFinalCobro.getDate();
                int nroFactura = -1;
                M_cliente cliente = modelo.getFacturaCabecera().getCliente();

                modelo.getTm().setList(modelo.obtenerCobroPendiente(cliente, fechaInicio, fechaFinal, nroFactura, false));
                Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void invocarVistaResumen() {
        ResumenCobroPendiente cc = new ResumenCobroPendiente(vista, modelo.getTm());
        cc.mostrarVista();
    }

    private void invocarVistaVerDetalle() {
        int fila = this.vista.jtCobroCabecera.getSelectedRow();
        int columna = this.vista.jtCobroCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int idFactura = (int) this.vista.jtCobroCabecera.getValueAt(fila, 0);
            Ver_ingreso vc = new Ver_ingreso(this.vista, idFactura);
            vc.mostrarVista();
        }
        this.vista.jbDetalleCobro.setEnabled(false);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        /*this.modelo.getFacturaCabecera().setFuncionario(funcionario);
        this.vista.jtfEmpCobro.setText(this.modelo.obtenerNombreFuncionario());*/
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.getFacturaCabecera().setCliente(cliente);
        this.vista.jtfCliente.setText(this.modelo.obtenerNombreCliente());
    }

    private void borrarDatos() {
        Date date = Calendar.getInstance().getTime();
        this.modelo.borrarDatos();
        this.modelo.limpiarDetalle();
        this.vista.jtfCliente.setText("");
        this.vista.jddInicioCobro.setDate(date);
        this.vista.jddFinalCobro.setDate(date);
    }

    private boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            } else {
                Date today = Calendar.getInstance().getTime();
                vista.jddInicioCobro.setDate(today);
                vista.jddFinalCobro.setDate(today);
                vista.jddInicioCobro.updateUI();
                vista.jddFinalCobro.updateUI();
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
                modelo.getFacturaCabecera().setNroFactura(nroFac);
            } else {
                modelo.getFacturaCabecera().setIdFacturaCabecera(null);
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
        if (src.equals(this.vista.jbBuscarCobro)) {
            consultarCobros();
        } else if (src.equals(this.vista.jbBuscarPendiente)) {
            consultarCobrosPendiente();
        } else if (src.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarCobro)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetalleCobro)) {
            invocarVistaVerDetalle();
        } else if (src.equals(this.vista.jbResumen)) {
            invocarVistaResumen();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtCobroCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCobroCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            Integer idCabecera = Integer.valueOf(String.valueOf(this.vista.jtCobroCabecera.getValueAt(fila, 0)));
            this.vista.jbDetalleCobro.setEnabled(true);
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
