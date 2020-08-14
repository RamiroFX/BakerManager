/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.E_retencionVenta;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import Utilities.CellRenderers.RetencionVentaStatusCellRenderer;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionRetencion implements ActionListener, MouseListener, KeyListener, RecibirEmpleadoCallback, RecibirClienteCallback {

    private static final String VALIDAR_NRO_RETENCION_1 = "Ingrese solo números enteros en número de retención",
            VALIDAR_NRO_RETENCION_2 = "Ingrese solo números enteros y positivos en número de retención";
    public M_gestionRetencion modelo;
    public V_gestionRetencion vista;
    public C_inicio c_inicio;
    private RetencionVentaStatusCellRenderer scr;

    public C_gestionRetencion(M_gestionRetencion modelo, V_gestionRetencion vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    public final void inicializarVista() {
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            this.vista.jcbEstado.addItem(estados.get(i));
        }
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        /*this.vista.jbNueva.setEnabled(false);
        this.vista.jbBuscar.setEnabled(false);
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbResumen.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbEmpleado.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);*/
        this.vista.jtCabecera.setModel(modelo.getTm());
        this.scr = new RetencionVentaStatusCellRenderer(this.modelo.getTm().getList());
        this.vista.jtCabecera.setDefaultRenderer(Object.class, scr);
    }

    public final void concederPermisos() {
        //TODO remove
        this.vista.jtfNroRetencion.addActionListener(this);
        this.vista.jbResumen.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbAnular.addActionListener(this);
        this.vista.jbNueva.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);

        this.vista.jtCabecera.addMouseListener(this);
        this.vista.jtCabecera.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbNueva.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
    }

    private void validarPermiso() {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
            this.vista.jbDetalle.setEnabled(true);
            this.vista.jbAnular.setEnabled(true);
        }
    }

    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        this.modelo.getCabecera().getVenta().setCliente(cliente);
        String nombre = this.modelo.getCabecera().getVenta().getCliente().getNombre();
        String entidad = this.modelo.getCabecera().getVenta().getCliente().getEntidad();
        this.vista.jtfCliente.setText(nombre + "-(" + entidad + ")");
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.getCabecera().setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(this.modelo.obtenerNombreFuncionario());
    }

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfCliente.setText("");
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroRetencion.setText("");
        this.vista.jcbEstado.setSelectedItem(new Estado(1, "Activo"));
    }

    private boolean validarFechas() {
        Date inicio = vista.jddInicio.getDate();
        Date fin = vista.jddFinal.getDate();
        if (inicio != null && fin != null) {
            int dateValue = inicio.compareTo(fin);
            if (dateValue <= 0) {
                return true;
            }
        }
        vista.jddFinal.setDate(vista.jddInicio.getDate());
        vista.jddFinal.updateUI();
        JOptionPane.showMessageDialog(vista, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private boolean validarNroRetencion() {
        int nroRetencion = -1;
        if (this.vista.jtfNroRetencion.getText().trim().isEmpty()) {
            return true;
        } else {
            try {
                nroRetencion = Integer.valueOf(this.vista.jtfNroRetencion.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroRetencion < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private int obtenerNroRetencion() {
        String nroNotaCredito = vista.jtfNroRetencion.getText().trim();
        int value = -1;
        if (nroNotaCredito.isEmpty()) {
            return value;
        }
        try {
            value = Integer.valueOf(nroNotaCredito);
        } catch (Exception e) {
            return -1;
        }
        if (value > 0) {
            return value;
        } else {
            return -1;
        }
    }

    private void displayQueryResults(final boolean conFecha) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarFechas()) {
                    return;
                }
                if (!validarNroRetencion()) {
                    return;
                }
                int nroRetencion = obtenerNroRetencion();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                long startTime = System.nanoTime();
                modelo.getTm().setList(modelo.obtenerRetenciones(fechaInicio, fechaFinal, nroRetencion, estado, conFecha));
                scr.setList(modelo.getTm().getList());
                long elapsedTime = System.nanoTime() - startTime;
                System.out.println("retenciones: Tiempo total de busqueda  in millis: " + elapsedTime / 1000000);
                Utilities.c_packColumn.packColumns(vista.jtCabecera, 1);
            }
        });
    }

    private void invocarVistaVerRetencionDetalle() {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
            E_retencionVenta unaRetencion = modelo.getTm().getList().get(fila);
            VerRetencion vr = new VerRetencion(c_inicio.vista);
            vr.cargarDatos(unaRetencion);
            vr.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void anularRetencion() {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
            int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea continuar? Accion irreversible.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                E_retencionVenta cab = modelo.getTm().getList().get(fila);
                if (cab.getNroRetencion() > 0) {
                    int opcion2 = JOptionPane.showConfirmDialog(vista, "¿Desea recuperar el número de Retención?.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    if (opcion2 == JOptionPane.YES_OPTION) {
                        modelo.anularRetencion(cab.getId(), Estado.INACTIVO, true);
                    } else {
                        modelo.anularRetencion(cab.getId(), Estado.INACTIVO, false);
                    }
                } else {
                    modelo.anularRetencion(cab.getId(), Estado.INACTIVO, false);
                }
            }
            displayQueryResults(true);
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void invocarVistaResumen() {
        ResumenRetencion resumen = new ResumenRetencion(c_inicio);
        resumen.inicializarDatos(modelo.getTm());
        resumen.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (e.getSource().equals(this.vista.jbBuscar)) {
            displayQueryResults(true);
        }
        if (e.getSource().equals(this.vista.jtfNroRetencion)) {
            displayQueryResults(false);
        }
        if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbNueva)) {
            CrearRetencionVenta sc = new CrearRetencionVenta(this.c_inicio.vista);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (source.equals(this.vista.jbAnular)) {
            anularRetencion();
        }
        if (source.equals(this.vista.jbDetalle)) {
            invocarVistaVerRetencionDetalle();
        }
        if (source.equals(this.vista.jbResumen)) {
            invocarVistaResumen();
        }
        if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jtCabecera)) {
            validarPermiso();
            if (e.getClickCount() == 2) {
                invocarVistaVerRetencionDetalle();
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
    }
}
