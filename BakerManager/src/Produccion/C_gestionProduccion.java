/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import DB.ResultSetTableModel;
import Empleado.Seleccionar_funcionario;
import Entities.E_produccionTipo;
import Entities.Estado;
import Entities.M_funcionario;
import Interface.GestionInterface;
import Interface.RecibirEmpleadoCallback;
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
public class C_gestionProduccion implements GestionInterface, RecibirEmpleadoCallback {

    private static final String VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_TITULO = "Atención";
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
        //E_productoClasificacion pc1 = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "Materia prima");
        //E_productoClasificacion pc2 = new E_productoClasificacion(E_productoClasificacion.PRODUCTO_TERMINADO, "Producto terminado");
        ArrayList<E_produccionTipo> tipoProduccion = modelo.obtenerProduccionTipo();
        for (int i = 0; i < tipoProduccion.size(); i++) {
            this.vista.jcbTipoProduccion.addItem(tipoProduccion.get(i));
        }
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            Estado get = estados.get(i);
            this.vista.jcbEstado.addItem(get);
        }
        Date date = Calendar.getInstance().getTime();
        this.vista.jddFinal.setDate(date);
        this.vista.jddInicio.setDate(date);
    }

    @Override
    public final void concederPermisos() {
        //TODO add access
        this.vista.jbCrearProduccion.addActionListener(this);
        this.vista.jbRegistroMateriaPrima.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
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

    private void ConsultarProduccion() {
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
                vista.jtProduccionCabecera.setModel(modelo.consultarProduccion(fecha_inicio, fecha_fin, conVenta, nroOrdenTrabajo, estado));
                Utilities.c_packColumn.packColumns(vista.jtProduccionCabecera, 1);
                vista.jbDetalle.setEnabled(false);

            }
        });
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
    
    private void verDetalle(){
        
    }

    private void obtenerPedidoDetalle(MouseEvent e) {
        int fila = this.vista.jtProduccionCabecera.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProduccionCabecera.columnAtPoint(e.getPoint());
        Integer idProduccion = Integer.valueOf(String.valueOf(this.vista.jtProduccionCabecera.getValueAt(fila, 0)));
        //this.modelo.setPedido(modelo.obtenerPedido(idPedido));
        //controlarTablaPedido();
        /**/
        if ((fila > -1) && (columna > -1)) {
            this.vista.jbAnular.setEnabled(true);
            this.vista.jtProduccionDetalle.setModel(modelo.obtenerProduccionDetalle(idProduccion));
            Utilities.c_packColumn.packColumns(this.vista.jtProduccionDetalle, 1);
        }
        if (e.getClickCount() == 2) {
            if (vista.jbDetalle.isEnabled()) {
                verDetalle();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCrearProduccion)) {
            CrearProduccion cp = new CrearProduccion(c_inicio);
            cp.mostrarVista();
        } else if (source.equals(this.vista.jbRegistroMateriaPrima)) {
            UtilizarMateriaPrima ump = new UtilizarMateriaPrima(c_inicio);
            ump.mostrarVista();
        } else if (source.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (source.equals(this.vista.jbBuscar)) {
            ConsultarProduccion();
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
