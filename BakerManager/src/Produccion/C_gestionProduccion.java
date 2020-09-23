/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Empleado.SeleccionarFuncionario;
import Entities.E_produccionCabecera;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.RecibirEmpleadoCallback;
import Utilities.ProductionCellRenderer;
import bakermanager.C_inicio;
import bauplast.BuscarProduccionDetalle;
import bauplast.crearProductoTerminado.CrearProductoTerminado;
import bauplast.crearRollo.CrearRollo;
import bauplast.desperdicio.CrearDesperdicio;
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
public class C_gestionProduccion implements GestionInterface, RecibirEmpleadoCallback {

    private static final String VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            PRODUCTION_ANULATED_MSG = "La producción seleccionada ya esta anulada",
            VALIDAR_TITULO = "Atención",
            CONFIRMAR_MSG = "¿Desea confirmas esta operación?";
    public M_gestionProduccion modelo;
    public V_gestionProduccion vista;
    public C_inicio c_inicio;

    public C_gestionProduccion(M_gestionProduccion modelo, V_gestionProduccion vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getProduccionDetalleTM());
        this.vista.jtProduccionCabecera.setModel(modelo.getProduccionCabeceraTM());
        ArrayList<E_produccionTipo> tipoProduccion = modelo.obtenerProduccionTipo();
        for (int i = 0; i < tipoProduccion.size(); i++) {
            this.vista.jcbTipoProduccion.addItem(tipoProduccion.get(i));
        }
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            Estado get = estados.get(i);
            this.vista.jcbEstado.addItem(get);
        }
        Calendar calTest = Calendar.getInstance();
        calTest.set(Calendar.YEAR, 2020);
        calTest.set(Calendar.MONTH, 0);
        calTest.set(Calendar.DAY_OF_MONTH, 1);
        Date date = Calendar.getInstance().getTime();
        this.vista.jddFinal.setDate(date);
        //this.vista.jddInicio.setDate(date);
        this.vista.jddInicio.setDate(calTest.getTime());
        this.vista.jbDetalle.setEnabled(false);
        this.vista.jbAnular.setEnabled(false);
        this.vista.jtProduccionCabecera.setDefaultRenderer(Object.class, new ProductionCellRenderer(0));
    }

    @Override
    public final void concederPermisos() {
        //TODO add access
        this.vista.jbCrearDesperdicio.addActionListener(this);
        this.vista.jbCrearRollo.addActionListener(this);
        this.vista.jbCrearProductoTerminado.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBuscarDetalle.addActionListener(this);
        this.vista.jbBorrar.addActionListener(this);
        this.vista.jbAnular.addActionListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.jbResumen.addActionListener(this);
        this.vista.jtfEmpleado.addKeyListener(this);
        this.vista.jtfNroOrdenTrabajo.addActionListener(this);
        this.vista.jbEmpleado.addKeyListener(this);
        this.vista.jbBorrar.addKeyListener(this);
        this.vista.jbAnular.addKeyListener(this);
        this.vista.jbDetalle.addKeyListener(this);
        this.vista.jbResumen.addKeyListener(this);
        this.vista.jtProduccionCabecera.addMouseListener(this);
        this.vista.jtProduccionCabecera.addKeyListener(this);
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

    private void ConsultarProduccion(final boolean conFecha) {
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
                E_produccionTipo conVenta = vista.jcbTipoProduccion.getItemAt(vista.jcbTipoProduccion.getSelectedIndex());
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                modelo.consultarProduccion(fecha_inicio, fecha_fin, conVenta, nroOrdenTrabajo, estado, conFecha);
                Utilities.c_packColumn.packColumns(vista.jtProduccionCabecera, 1);
                vista.jbDetalle.setEnabled(false);
                vista.jbAnular.setEnabled(false);
            }
        });
    }

    private void obtenerPedidoDetalle(MouseEvent e) {
        int fila = this.vista.jtProduccionCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProduccionCabecera.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            Integer idProduccion = modelo.getProduccionCabeceraTM().getList().get(fila).getId();
            this.vista.jbAnular.setEnabled(true);
            this.vista.jbDetalle.setEnabled(true);
            modelo.obtenerProduccionDetalle(idProduccion);
            Utilities.c_packColumn.packColumns(this.vista.jtProduccionDetalle, 1);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbDetalle.isEnabled()) {
                verDetalle();
            }
        }
    }

    private void keyRelasedHandler() {
        int fila = this.vista.jtProduccionCabecera.getSelectedRow();
        if ((fila > -1)) {
            Integer idProduccion = modelo.getProduccionCabeceraTM().getList().get(fila).getId();
            this.vista.jbAnular.setEnabled(true);
            this.vista.jbDetalle.setEnabled(true);
            modelo.obtenerProduccionDetalle(idProduccion);
            Utilities.c_packColumn.packColumns(this.vista.jtProduccionDetalle, 1);
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
        int row = this.vista.jtProduccionCabecera.getSelectedRow();
        if (row > -1) {
            E_produccionCabecera pc = modelo.getProduccionCabeceraTM().getList().get(row);
            /*int idProduccion = Integer.valueOf(String.valueOf(this.vista.jtProduccionCabecera.getValueAt(row, 0)));
            VerProduccion vp = new VerProduccion(c_inicio);
            vp.verPedidoRegistrado(idProduccion);
            vp.mostrarVista();
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);*/
            ///BAUPLAST MOD
            switch (pc.getTipo().getId()) {
                case E_produccionTipo.PRODUCTO_TERMINADO: {
                    CrearProductoTerminado cpt = new CrearProductoTerminado(c_inicio);
                    cpt.cargarDatos(pc);
                    cpt.mostrarVista();
                    break;
                }
                case E_produccionTipo.ROLLO: {
                    CrearRollo cr = new CrearRollo(c_inicio);
                    cr.cargarDatos(pc);
                    cr.mostrarVista();
                    break;
                }
            }
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void borrarParametros() {
        modelo.borrarDatos();
        this.vista.jtfEmpleado.setText("");
        this.vista.jtfNroOrdenTrabajo.setText("");
        this.vista.jcbEstado.setSelectedIndex(0);
        this.vista.jcbTipoProduccion.setSelectedIndex(0);
    }

    private void anularProduccion() {
        int fila = this.vista.jtProduccionCabecera.getSelectedRow();
        if (modelo.getProduccionEstado(fila).getId() == Estado.INACTIVO) {
            JOptionPane.showMessageDialog(vista, PRODUCTION_ANULATED_MSG, VALIDAR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista, CONFIRMAR_MSG, VALIDAR_TITULO, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            Integer idProduccion = modelo.getProduccionCabeceraTM().getList().get(fila).getId();
            this.modelo.anularProduccion(idProduccion);
            ConsultarProduccion(true);
            Utilities.c_packColumn.packColumns(this.vista.jtProduccionCabecera, 1);
            this.vista.jbDetalle.setEnabled(false);
            this.vista.jbAnular.setEnabled(false);
        }
    }

    private void resumenProduccion() {
        Date fecha_inicio = vista.jddInicio.getDate();
        Date fecha_fin = vista.jddFinal.getDate();
        ResumenProduccion rp = new ResumenProduccion(this.c_inicio.vista, this.modelo.getProduccionCabeceraTM(), fecha_inicio, fecha_fin);
        rp.mostrarVista();
    }

    private void invocarCrearRolloMenu() {
        CrearRollo cp = new CrearRollo(c_inicio);
        cp.mostrarVista();
    }

    private void invocarCrearProductoTerminadonMenu() {
        CrearProductoTerminado cp = new CrearProductoTerminado(c_inicio);
        cp.mostrarVista();
    }

    private void buscarProduccion() {
        BuscarProduccionDetalle bpc = new BuscarProduccionDetalle(this.c_inicio.vista);
        bpc.mostrarVista();
    }

    private void invocarCrearDesperdicio() {
        CrearDesperdicio bpc = new CrearDesperdicio(this.c_inicio.vista);
        bpc.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCrearRollo)) {
            invocarCrearRolloMenu();
        } else if (source.equals(this.vista.jbCrearProductoTerminado)) {
            invocarCrearProductoTerminadonMenu();
        } else if (source.equals(this.vista.jbEmpleado)) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (source.equals(this.vista.jbBuscar)) {
            ConsultarProduccion(true);
        } else if (source.equals(this.vista.jbBorrar)) {
            borrarParametros();
        } else if (source.equals(this.vista.jbDetalle)) {
            verDetalle();
        } else if (source.equals(this.vista.jbAnular)) {
            anularProduccion();
        } else if (source.equals(this.vista.jbResumen)) {
            resumenProduccion();
        } else if (source.equals(this.vista.jbBuscarDetalle)) {
            buscarProduccion();
        } else if (source.equals(this.vista.jtfNroOrdenTrabajo)) {
            ConsultarProduccion(false);
        } else if (source.equals(this.vista.jbCrearDesperdicio)) {
            invocarCrearDesperdicio();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtProduccionCabecera)) {
            obtenerPedidoDetalle(e);
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
                invocarCrearRolloMenu();
                break;
            }
            case KeyEvent.VK_F2: {
                invocarCrearProductoTerminadonMenu();
                break;
            }
            case KeyEvent.VK_F3: {
                resumenProduccion();
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
        if (e.getSource().equals(this.vista.jtProduccionCabecera)) {
            keyRelasedHandler();
        }
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.modelo.setFuncionario(funcionario);
        this.vista.jtfEmpleado.setText(funcionario.getNombre());
    }
}
