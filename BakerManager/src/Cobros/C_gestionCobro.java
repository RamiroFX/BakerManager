/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cliente.Seleccionar_cliente;
import Cobros.Retencion.GestionRetencion;
import Empleado.Seleccionar_funcionario;
import Entities.E_cuentaCorrienteConcepto;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Excel.ExportarEstadoCuentaCliente;
import Interface.GestionInterface;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import MenuPrincipal.DatosUsuario;
import Utilities.CellRenderers.CobroClienteStatusCellRenderer;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionCobro implements GestionInterface, RecibirEmpleadoCallback,
        RecibirClienteCallback, InterfaceNotificarCambio {

    V_gestionCobro vista;
    M_gestionCobroPago modelo;
    public C_inicio c_inicio;
    private int tipoCliente;//para utilizar en el buscado de clientes
    private static final int TIPO_BUSCADOR = 1, TIPO_ESTADO_CUENTA = 2;
    private CobroClienteStatusCellRenderer scr;

    public C_gestionCobro(V_gestionCobro vista, M_gestionCobroPago modelo, C_inicio c_inicio) {
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
//
//        this.vista.jddInicioPago.setDate(date);
//        this.vista.jddFinalPago.setDate(date);
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        this.vista.jtCobroCabecera.setModel(this.modelo.getTm());
        this.scr = new CobroClienteStatusCellRenderer(this.modelo.getTm().getList());
        this.vista.jtCobroCabecera.setDefaultRenderer(Object.class, scr);
        this.vista.jtCobroDetalle.setModel(this.modelo.getTmDetalle());
    }

    @Override
    public void concederPermisos() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
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
                this.vista.jbCliente.addActionListener(this);
                this.vista.jbBorrarCobro.addActionListener(this);
                this.vista.jbBorrarCobro.addActionListener(this);
            }
            //PAGO
            if (this.vista.jbAnular.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbAnular.addActionListener(this);
            }
            if (this.vista.jbResumen.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbResumen.addActionListener(this);
            }
            if (this.vista.jbCobroPendientes.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbCobroPendientes.setEnabled(true);
                this.vista.jbCobroPendientes.addActionListener(this);
            }
            /*if (this.vista.jbBanco.getName().equals(acceso.getItemDescripcion())) {
                this.vista.jbBanco.setEnabled(true);
                this.vista.jbBanco.addActionListener(this);
            }*/
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
        this.vista.jtCobroCabecera.addMouseListener(this);
        this.vista.jtCobroCabecera.addKeyListener(this);

        /**
         * **ESCAPE HOTKEY/
         */
        //cobro
        String cerrar = "cerrar";
        String crearCobro = "Referesh";
        this.vista.jbCliente.addKeyListener(this);
        Action buttonAction = new AbstractAction(crearCobro) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                invocarVistaCobrar();
            }
        };
        Action cerrarAction = new AbstractAction(cerrar) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cerrar();
            }
        };
        this.vista.jbCobro.setAction(buttonAction);
        this.vista.jbCobro.setAction(cerrarAction);
        this.vista.jbCobro.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), crearCobro);
        this.vista.jbCobro.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cerrar);
        this.vista.jbCobro.getActionMap().put(crearCobro, buttonAction);
        this.vista.jbCobro.getActionMap().put(cerrar, cerrarAction);

        this.vista.jbEmpCobro.addKeyListener(this);
        this.vista.jddInicioCobro.addKeyListener(this);
        this.vista.jddFinalCobro.addKeyListener(this);
        this.vista.jcbEstado.addKeyListener(this);
        this.vista.jbBuscarCobro.addKeyListener(this);
        this.vista.jbBorrarCobro.addKeyListener(this);
        this.vista.jbCobroPendientes.addKeyListener(this);
        this.vista.jtfNroRecibo.addKeyListener(this);
        this.vista.jbCobro.addKeyListener(this);
        this.vista.jbDetalleCobro.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        //this.vista.jbBanco.addKeyListener(this);
        this.vista.jbMasOpciones.addKeyListener(this);
    }

    private void verificarPermiso() {
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbCobro.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbCobro.setEnabled(true);
            }
            if (this.vista.jbDetalleCobro.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.vista.jbDetalleCobro.setEnabled(true);
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

    private void consultarCobros(final boolean conFecha) {
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
                int nroRecibo = -1;
                if (!vista.jtfNroRecibo.getText().trim().isEmpty()) {
                    nroRecibo = Integer.valueOf(vista.jtfNroRecibo.getText().trim());
                }
                M_cliente cliente = modelo.getCliente();
                M_funcionario funcionario = modelo.getFuncionario();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());

                modelo.getTm().setList(modelo.obtenerCobro(cliente, funcionario, fechaInicio, fechaFinal, estado, nroRecibo, conFecha));
                scr.setList(modelo.getTm().getList());
                Utilities.c_packColumn.packColumns(vista.jtCobroCabecera, 1);
                modelo.limpiarDetalle();
            }
        });
    }

    private void consultarCobrosPendiente() {
        CobroPendiente cp = new CobroPendiente(this.c_inicio.vista);
        cp.mostrarVista();
    }

    private void invocarVistaCobrar() {
        CrearCobro cc = new CrearCobro(c_inicio);
        cc.mostrarVista();
    }

    private void anularCobro() {
        int fila = this.vista.jtCobroCabecera.getSelectedRow();
        if (fila < 0) {
            return;
        }
        if (modelo.getCobroEstado(fila).getId() == Estado.INACTIVO) {
            JOptionPane.showMessageDialog(vista, "El cobro ya se encuentra anulado", "Atenciòn", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Esta seguro que desea continuar?", "Atenciòn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Integer idCabecera = modelo.getTm().getList().get(fila).getId();
            this.modelo.anularCobro(idCabecera);
            consultarCobros(true);
            modelo.limpiarDetalle();
            Utilities.c_packColumn.packColumns(this.vista.jtCobroCabecera, 1);
            this.vista.jbDetalleCobro.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void invocarVistaResumen() {
        ResumenCobro cc = new ResumenCobro(c_inicio.vista, modelo.getTm());
        cc.mostrarVista();
    }

    private void invocarVistaBancos() {
        BancosParametros bp = new BancosParametros(this.c_inicio.vista);
        bp.setVisible(true);
    }

    private void invocarVistaChequesPendientes() {
        ChequesPendientes bp = new ChequesPendientes(this.c_inicio.vista, E_cuentaCorrienteConcepto.VENTAS);
        bp.setVisible(true);
    }

    private void invocarVistaVerDetalle() {
        int fila = this.vista.jtCobroCabecera.getSelectedRow();
        int columna = this.vista.jtCobroCabecera.getSelectedColumn();
        if ((fila > -1) && (columna > -1)) {
            int index = this.vista.jtCobroCabecera.getSelectedRow();
            VerCobro vc = new VerCobro(this.c_inicio.vista);
            vc.setCtaCteCabecera(modelo.getTm().getList().get(index));
            vc.mostrarVista();
        }
        this.vista.jbDetalleCobro.setEnabled(false);
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpCobro.setText(this.modelo.obtenerNombreFuncionario());
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        switch (tipoCliente) {
            case TIPO_BUSCADOR: {
                this.modelo.setCliente(cliente);
                this.vista.jtfCliente.setText(this.modelo.obtenerNombreCliente());
                break;
            }
            case TIPO_ESTADO_CUENTA: {
                modelo.setEstadoCuentaCliente(cliente);
                prepararReporteEstadoCuentas();
                break;
            }
        }
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
            if (!vista.jtfNroRecibo.getText().trim().isEmpty()) {
                int nroFac = Integer.valueOf(vista.jtfNroRecibo.getText());
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

    private void esperar() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(C_gestionCobro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mostrarOpciones() {
        Object[] options = {"Estado de cuenta", "Retención de I.V.A.", "Bancos"};
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
                        invocarEstadoCuentas();
                    }
                });
                break;
            }
            case 1: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        invocarRetencionIVA();
                    }
                });
                break;
            }
            case 2: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        invocarVistaBancos();
                    }
                });
                break;
            }
            default: {

                break;
            }
        }
    }

    private void invocarEstadoCuentas() {
        this.tipoCliente = TIPO_ESTADO_CUENTA;
        Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }

    private void invocarRetencionIVA() {
        GestionRetencion sc = new GestionRetencion(this.c_inicio);
        sc.mostrarVista();
    }

    private void prepararReporteEstadoCuentas() {
        Object[] options = {"Histórico", "Resumido"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga su opción",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            //HISTORICO
            case 0: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ExportarEstadoCuentaCliente eecc = new ExportarEstadoCuentaCliente("Estado de cuenta", modelo.getEstadoCuentaCliente());
                        eecc.exportacionHistorica();
                    }
                });
                break;
            }
            //RESUMIDO
            case 1: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ExportarEstadoCuentaCliente eecc = new ExportarEstadoCuentaCliente("Estado de cuenta", modelo.getEstadoCuentaCliente());
                        eecc.exportacionResumida();
                    }
                });
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbCobro)) {
            invocarVistaCobrar();
        } else if (src.equals(this.vista.jbBuscarCobro)) {
            consultarCobros(true);
        } else if (src.equals(this.vista.jbAnular)) {
            anularCobro();
        } else if (src.equals(this.vista.jbCheques)) {
            invocarVistaChequesPendientes();
        } else if (src.equals(this.vista.jbEmpCobro)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (src.equals(this.vista.jbCliente)) {
            this.tipoCliente = TIPO_BUSCADOR;
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        } else if (src.equals(this.vista.jbBorrarCobro)) {
            borrarDatos();
        } else if (src.equals(this.vista.jbDetalleCobro)) {
            invocarVistaVerDetalle();
        } else if (src.equals(this.vista.jbResumen)) {
            invocarVistaResumen();
        } else if (src.equals(this.vista.jbCobroPendientes)) {
            consultarCobrosPendiente();
        } else if (src.equals(this.vista.jbMasOpciones)) {
            mostrarOpciones();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtCobroCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtCobroCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            Integer idCabecera = modelo.getTm().getList().get(fila).getId();
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
                if (vista.jbCobro.isEnabled()) {
                    invocarVistaCobrar();
                }
                break;
            }
            case KeyEvent.VK_F2: {
                if (vista.jbResumen.isEnabled()) {
                    invocarVistaResumen();
                }
                break;
            }
            case KeyEvent.VK_F3: {
                if (vista.jbCobroPendientes.isEnabled()) {
                    consultarCobrosPendiente();
                }
                break;
            }
            case KeyEvent.VK_F4: {
                mostrarOpciones();
                break;
            }
            case KeyEvent.VK_ENTER: {
                if (vista.jtfNroRecibo.hasFocus()) {
                    consultarCobros(false);
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
