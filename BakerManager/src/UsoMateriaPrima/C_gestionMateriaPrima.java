/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import Empleado.SeleccionarFuncionario;
import Entities.Estado;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.RecibirEmpleadoCallback;
import Utilities.UtilizacionMPCellRenderer;
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
public class C_gestionMateriaPrima implements GestionInterface, RecibirEmpleadoCallback {

    private static final String VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            PRODUCTION_ANULATED_MSG = "El registro seleccionado ya esta anulado",
            VALIDAR_TITULO = "Atención",
            CONFIRMAR_MSG = "¿Desea confirmas esta operación?";
    public M_gestionMateriaPrima modelo;
    public V_gestionMateriaPrima vista;
    public C_inicio c_inicio;

    public C_gestionMateriaPrima(M_gestionMateriaPrima modelo, V_gestionMateriaPrima vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            Estado get = estados.get(i);
            this.vista.jcbEstado.addItem(get);
        }
        Date date = Calendar.getInstance().getTime();
        this.vista.jddFinal.setDate(date);
        this.vista.jddInicio.setDate(date);
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
        this.vista.jtUtilizacionMPCabecera.setDefaultRenderer(Object.class, new UtilizacionMPCellRenderer(0));
    }

    @Override
    public final void concederPermisos() {
        //TODO add access
        this.vista.jbRegistroMateriaPrima.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbAnular.addActionListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbResumen.addActionListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jtUtilizacionMPCabecera.addMouseListener(this);
        this.vista.jtUtilizacionMPCabecera.addKeyListener(this);
    }

    private void verificarPermiso() {
    }

    @Override
    public void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    @Override
    public void cerrar() {
        try {
            this.vista.setClosed(true);
            System.runFinalization();
        } catch (PropertyVetoException ex) {
        }
    }

    private void ConsultarUtilizacionMP() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!validarFechas()) {
                    return;
                }
                if (!validarOrdenTrabajo()) {
                    return;
                }
                Date fecha_inicio = vista.jddInicio.getDate();
                Date fecha_fin = vista.jddFinal.getDate();
                int nroOrdenTrabajo = -1;
                if (!vista.jtfNroOrdenTrabajo.getText().trim().isEmpty()) {
                    nroOrdenTrabajo = Integer.valueOf(vista.jtfNroOrdenTrabajo.getText().trim());
                }
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                vista.jtUtilizacionMPCabecera.setModel(modelo.consultarUtilizacionMP(fecha_inicio, fecha_fin, nroOrdenTrabajo, estado));
                Utilities.c_packColumn.packColumns(vista.jtUtilizacionMPCabecera, 1);
                vista.jbDetalle.setEnabled(false);
                vista.jbAnular.setEnabled(false);
            }
        });
    }

    private void obtenerUtilizacionMPDetalle(MouseEvent e) {
        int fila = this.vista.jtUtilizacionMPCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtUtilizacionMPCabecera.columnAtPoint(e.getPoint());
        Integer idUtilizacionMP = Integer.valueOf(String.valueOf(this.vista.jtUtilizacionMPCabecera.getValueAt(fila, 0)));
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAnular.setEnabled(true);
            this.vista.jbDetalle.setEnabled(true);
            this.vista.jtUtilizacionMPDetalle.setModel(modelo.obtenerRegistroMateriaPrimaDetalle(idUtilizacionMP));
            Utilities.c_packColumn.packColumns(this.vista.jtUtilizacionMPDetalle, 1);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbDetalle.isEnabled()) {
                verDetalle();
            }
        }
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

    private boolean validarOrdenTrabajo() {
        int ordenTrabajo = -1;
        if (this.vista.jtfNroOrdenTrabajo.getText().trim().isEmpty()) {
            return true;
        } else {
            try {
                ordenTrabajo = Integer.valueOf(this.vista.jtfNroOrdenTrabajo.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_2, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (ordenTrabajo < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_3, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void verDetalle() {
        int row = this.vista.jtUtilizacionMPCabecera.getSelectedRow();
        int idUtilizacionMP = Integer.valueOf(String.valueOf(this.vista.jtUtilizacionMPCabecera.getValueAt(row, 0)));
        VerUtilizacion vp = new VerUtilizacion(c_inicio);
        vp.verRegistro(idUtilizacionMP);
        vp.mostrarVista();
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);

    }

    private void borrarParametros() {
        modelo.borrarDatos();
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroOrdenTrabajo.setText("");
        this.vista.jcbEstado.setSelectedIndex(0);
    }

    private void anularRegistroMateriaPrima() {
        int fila = this.vista.jtUtilizacionMPCabecera.getSelectedRow();
        if (modelo.getUsoMateriPrimaEstado(fila).getId() == Estado.INACTIVO) {
            JOptionPane.showMessageDialog(vista, PRODUCTION_ANULATED_MSG, VALIDAR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista, CONFIRMAR_MSG, VALIDAR_TITULO, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Integer idUtilizacionMP = Integer.valueOf(String.valueOf(this.vista.jtUtilizacionMPCabecera.getValueAt(fila, 0)));
            this.modelo.anularUsoMateriaPrima(idUtilizacionMP);
            ConsultarUtilizacionMP();
            Utilities.c_packColumn.packColumns(this.vista.jtUtilizacionMPCabecera, 1);
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void resumenProduccion() {
        Date fecha_inicio = vista.jddInicio.getDate();
        Date fecha_fin = vista.jddFinal.getDate();
        ResumenUtilizacionMP rump = new ResumenUtilizacionMP(this.c_inicio.vista, this.modelo.getUtilizacionMPCabeceraTM(), fecha_inicio, fecha_fin);
        rump.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbRegistroMateriaPrima)) {
            UtilizarMateriaPrima ump = new UtilizarMateriaPrima(c_inicio);
            ump.mostrarVista();
        } else if (source.equals(this.vista.jbEmpleado)) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (source.equals(this.vista.jbBuscar)) {
            ConsultarUtilizacionMP();
        } else if (source.equals(this.vista.jbBorrar)) {
            borrarParametros();
        } else if (source.equals(this.vista.jbDetalle)) {
            verDetalle();
        } else if (source.equals(this.vista.jbAnular)) {
            anularRegistroMateriaPrima();
        } else if (source.equals(this.vista.jbResumen)) {
            resumenProduccion();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtUtilizacionMPCabecera)) {
            obtenerUtilizacionMPDetalle(e);
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(funcionario.getNombre());
    }
}
