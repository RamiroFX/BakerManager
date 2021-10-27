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
import Producto.Movimientos.MovimientoProducto;
import Utilities.ProductionCellRenderer;
import bakermanager.C_inicio;
import bauplast.BuscarProduccionDetalle;
import bauplast.buscarMateriaPrima.BuscarMateriaPrimaDetalle;
import bauplast.crearProductoTerminado.BuscarProductosTerminados;
import bauplast.crearProductoTerminado.CrearProductoTerminado;
import bauplast.crearRollo.CrearRollo;
import bauplast.desperdicio.CrearDesperdicio;
import bauplast.desperdicio.CrearDesperdicioRapido;
import bauplast.desperdicio.buscarDesperdicio.BuscarDesperdicioDetalle;
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
        //calTest.set(Calendar.YEAR, (calTest.get(Calendar.YEAR) - 1));
        //calTest.set(Calendar.MONTH, (calTest.get(Calendar.MONTH) - 1));
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
        this.vista.jbMasOpciones.addActionListener(this);
        this.vista.jbCrearDesperdicio.addActionListener(this);
        this.vista.jbCrearDesperdicioRapido.addActionListener(this);
        this.vista.jbCrearRollo.addActionListener(this);
        this.vista.jbCrearProductoTerminado.addActionListener(this);
        this.vista.jbEmpleado.addActionListener(this);
        this.vista.jbBuscar.addActionListener(this);
        this.vista.jbBuscarDetalle.addActionListener(this);
        this.vista.jbBuscarDetalleDesperdicio.addActionListener(this);
        this.vista.jbBuscarMateriaPrimaBaja.addActionListener(this);
        this.vista.jbBuscarTerminados.addActionListener(this);
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
        this.vista.jbMasOpciones.addKeyListener(this);
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
                E_produccionTipo tipoProd = vista.jcbTipoProduccion.getItemAt(vista.jcbTipoProduccion.getSelectedIndex());
                Estado estado = vista.jcbEstado.getItemAt(vista.jcbEstado.getSelectedIndex());
                modelo.consultarProduccion(fecha_inicio, fecha_fin, tipoProd, nroOrdenTrabajo, estado, conFecha);
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

    private void buscarProduccionRollos() {
        BuscarProduccionDetalle bpc = new BuscarProduccionDetalle(this.c_inicio.vista);
        bpc.mostrarVista();
    }

    private void buscarDesperdicio() {
        BuscarDesperdicioDetalle bpc = new BuscarDesperdicioDetalle(this.c_inicio.vista);
        bpc.mostrarVista();
    }

    private void buscarBajaMateriaPrima() {
        BuscarMateriaPrimaDetalle bbmp = new BuscarMateriaPrimaDetalle(this.c_inicio.vista);
        bbmp.mostrarVista();
    }

    private void buscarProductosTerminados() {
        BuscarProductosTerminados bpt = new BuscarProductosTerminados(this.c_inicio.vista);
        bpt.mostrarVista();
    }

    private void invocarCrearDesperdicio() {
        int row = this.vista.jtProduccionCabecera.getSelectedRow();
        if (row > -1) {
            E_produccionCabecera pc = modelo.getProduccionCabeceraTM().getList().get(row);
            if (modelo.existeProduccionDesperdicio(pc.getId())) {
                CrearDesperdicio bpc = new CrearDesperdicio(this.c_inicio.vista);
                bpc.setProduccionCabeceraDesperdicio(modelo.obtenerProduccionDesperdicioCabecera(pc.getId()));
                bpc.establecerModoActualizacion();
                bpc.mostrarVista();
            } else {
                CrearDesperdicio bpc = new CrearDesperdicio(this.c_inicio.vista);
                bpc.setProduccion(pc);
                bpc.mostrarVista();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione una producción", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void invocarCrearDesperdicioRapido() {
        CrearDesperdicioRapido cdr = new CrearDesperdicioRapido(this.c_inicio.vista);
        cdr.mostrarVista();
    }

    private void invocarMovimientoProductos() {
        MovimientoProducto cdr = new MovimientoProducto(this.c_inicio.vista);
        cdr.mostrarVista();
    }

    private void masOpciones() {
        Object[] options = {"Movimiento de productos", "Baja de Rollos"};
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
                        invocarMovimientoProductos();
                    }
                });
                break;
            }
            case 1: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        invocarCrearDesperdicioRapido();
                        /*TODO
                        ArrayList<M_menu_item> accesos = modelo.getAccesos();
                        System.err.println("vista.jbCrearDesperdicioRapido.getName(): " + vista.jbCrearDesperdicioRapido.getName());
                        for (int i = 0; i < accesos.size(); i++) {
                            System.out.println("accesos.get(i).getItemDescripcion(): " + accesos.get(i).getItemDescripcion());
                            if (vista.jbCrearDesperdicioRapido.getName().equals(accesos.get(i).getItemDescripcion())) {
                                invocarCrearDesperdicioRapido();
                                return;
                            }
                        }*/
                    }
                });
                break;
            }
        }
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
            buscarProduccionRollos();
        } else if (source.equals(this.vista.jbBuscarDetalleDesperdicio)) {
            buscarDesperdicio();
        } else if (source.equals(this.vista.jbBuscarMateriaPrimaBaja)) {
            buscarBajaMateriaPrima();
        } else if (source.equals(this.vista.jtfNroOrdenTrabajo)) {
            ConsultarProduccion(false);
        } else if (source.equals(this.vista.jbCrearDesperdicio)) {
            invocarCrearDesperdicio();
        } else if (source.equals(this.vista.jbBuscarTerminados)) {
            buscarProductosTerminados();
        } else if (source.equals(this.vista.jbMasOpciones)) {
            masOpciones();
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
