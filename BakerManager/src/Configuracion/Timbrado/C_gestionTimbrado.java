/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import Entities.Estado;
import Utilities.CellRenderers.TimbradoVentaStatusCellRenderer;
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
public class C_gestionTimbrado implements ActionListener, MouseListener, KeyListener {

    private static final String VALIDAR_NRO_RETENCION_1 = "Ingrese solo números enteros en número de retención",
            VALIDAR_NRO_RETENCION_2 = "Ingrese solo números enteros y positivos en número de retención";
    public M_gestionTimbrado modelo;
    public V_gestionTimbrado vista;
    public C_inicio c_inicio;
    private TimbradoVentaStatusCellRenderer scr;

    public C_gestionTimbrado(M_gestionTimbrado modelo, V_gestionTimbrado vista, C_inicio c_inicio) {
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
        ArrayList tipoFechas = modelo.getTipoFechas();
        for (int i = 0; i < tipoFechas.size(); i++) {
            this.vista.jcbFecha.addItem(tipoFechas.get(i));
        }
        handleDateParams();
        Date today = Calendar.getInstance().getTime();
        this.vista.jddInicio.setDate(today);
        this.vista.jddFinal.setDate(today);
        this.vista.jtCabecera.setModel(modelo.getTm());
        Utilities.c_packColumn.packColumns(vista.jtCabecera, 1);
        this.scr = new TimbradoVentaStatusCellRenderer(this.modelo.getTm().getList());
        this.vista.jtCabecera.setDefaultRenderer(Object.class, scr);
    }

    private void handleDateParams() {
        if (this.vista.jcbActivarFecha.isSelected()) {
            this.vista.jcbFecha.setEnabled(true);
            this.vista.jddFinal.setEnabled(true);
            this.vista.jddInicio.setEnabled(true);
        } else {
            this.vista.jcbFecha.setEnabled(false);
            this.vista.jddFinal.setEnabled(false);
            this.vista.jddInicio.setEnabled(false);
        }
    }

    public final void concederPermisos() {
        //TODO remove
        this.vista.jtfNroTimbrado.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbAnular.addActionListener(this);
        this.vista.jbNueva.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jcbActivarFecha.addActionListener(this);

        this.vista.jtCabecera.addMouseListener(this);
        this.vista.jtCabecera.addKeyListener(this);
        /**
         * **ESCAPE HOTKEY/
         */
        this.vista.jbBuscar.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbNueva.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jcbActivarFecha.addKeyListener(this);
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

    private void borrarDatos() {
        this.modelo.borrarDatos();
        this.vista.jtfNroTimbrado.setText("");
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

    private boolean validarNroTimbrado() {
        int nroTimbrado = -1;
        if (this.vista.jtfNroTimbrado.getText().trim().isEmpty()) {
            return true;
        } else {
            try {
                nroTimbrado = Integer.valueOf(this.vista.jtfNroTimbrado.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroTimbrado < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_RETENCION_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private int obtenerNroTimbrado() {
        String nroTimbrado = vista.jtfNroTimbrado.getText().trim();
        int value = -1;
        if (nroTimbrado.isEmpty()) {
            return value;
        }
        try {
            value = Integer.valueOf(nroTimbrado);
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
                if (!validarNroTimbrado()) {
                    return;
                }
                int nroTimbrado = obtenerNroTimbrado();
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                Date fechaInicio = vista.jddInicio.getDate();
                Date fechaFinal = vista.jddFinal.getDate();
                String tipoFecha = vista.jcbFecha.getSelectedItem() + "";
                boolean esConFecha = vista.jcbActivarFecha.isSelected();
                long startTime = System.nanoTime();
                modelo.getTm().setList(modelo.obtenerTimbradoVentas(fechaInicio, fechaFinal, nroTimbrado, estado, esConFecha, tipoFecha));
                scr.setList(modelo.getTm().getList());
                long elapsedTime = System.nanoTime() - startTime;
                System.out.println("timbrados: Tiempo total de busqueda  in millis: " + elapsedTime / 1000000);
                Utilities.c_packColumn.packColumns(vista.jtCabecera, 1);
            }
        });
    }

    private void invocarVistaVerTimbrado() {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
            E_Timbrado unTimbrado = modelo.getTm().getList().get(fila);
            VerTimbrado vt = new VerTimbrado(c_inicio);
            vt.cargarDatos(unTimbrado);
            vt.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void invocarVistaCrear() {
        CrearTimbrado crearTimbrado = new CrearTimbrado(vista);
        crearTimbrado.mostrarVista();
    }

    private void anularTimbrado() {
        int fila = this.vista.jtCabecera.getSelectedRow();
        if (fila > -1) {
            int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea continuar? Accion irreversible.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                E_Timbrado cab = modelo.getTm().getList().get(fila);
                modelo.anularTimbrado(cab.getId(), Estado.INACTIVO, false);
            }
            displayQueryResults(true);
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (e.getSource().equals(this.vista.jbBuscar)) {
            displayQueryResults(true);
        }
        if (e.getSource().equals(this.vista.jtfNroTimbrado)) {
            displayQueryResults(false);
        }
        if (source.equals(this.vista.jbNueva)) {
            invocarVistaCrear();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
        if (source.equals(this.vista.jbAnular)) {
            anularTimbrado();
        }
        if (source.equals(this.vista.jbDetalle)) {
            invocarVistaVerTimbrado();
        }
        if (source.equals(this.vista.jcbActivarFecha)) {
            handleDateParams();
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
                invocarVistaVerTimbrado();
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
