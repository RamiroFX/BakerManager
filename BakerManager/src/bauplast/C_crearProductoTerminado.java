/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Empleado.Seleccionar_funcionario;
import Entities.E_produccionCabecera;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_productoClasificacion;
import Entities.M_funcionario;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
class C_crearProductoTerminado extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, InterfaceRecibirProduccionFilm, RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_4 = "El número de orden de trabajo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            VALIDAR_CANT_ROLLOS_MSG = "Seleccione por lo menos un rollo.",
            CONFIRMAR_SALIR_MSG = "¿Cancelar producción?",
            VALIDAR_TITULO = "Atención";

    public M_crearProductoTerminado modelo;
    public V_crearProductoTerminado vista;
    private boolean esModoCreacion;

    public C_crearProductoTerminado(M_crearProductoTerminado modelo, V_crearProductoTerminado vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.esModoCreacion = true;
        inicializarVista();
        agregarListeners();
    }

    public void cargarDatos(E_produccionCabecera pc) {
        modelo.setProduccionCabecera(pc);
        //CAMBIAR TITULO DE LA VENTANA
        this.vista.setTitle("Ver detalle de producción" + " (Tiempo de registro: " + modelo.getFechaProduccionFormateada() + ") - (Registrado por: " + modelo.getProduccionCabecera().getFuncionarioSistema().getNombre() + ")");
        //ESTABLECER MODO LECTURA DE DATOS
        this.esModoCreacion = false;
        //INABILITAR LOS CONTROLES
        this.vista.jtProduccionDetalle.removeMouseListener(this);
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jbSeleccionarProducto.setEnabled(false);
        this.vista.jbEliminarProducto.setEnabled(false);
        this.vista.jbModificarProducto.setEnabled(false);
        this.vista.jbSeleccionarRollo.setEnabled(false);
        this.vista.jbModificarRollo.setEnabled(false);
        this.vista.jbEliminarRollo.setEnabled(false);
        this.vista.jbFuncionario.setEnabled(false);
        this.vista.jdcFechaEntrega.setEnabled(false);
        this.vista.jtfNroOrdenTrabajo.setEditable(false);
        //CARGAR DATOS EN LA VISTA 
        this.vista.jtProduccionDetalle.setModel(modelo.getProductosTerminadosTM());
        this.vista.jtRolloUtilizado.setModel(modelo.getRolloUtilizadoTm());
        this.vista.jdcFechaEntrega.setDate(pc.getFechaProduccion());
        this.vista.jtfFuncionario.setText(pc.getFuncionarioProduccion().getNombre());
        this.vista.jtfNroOrdenTrabajo.setText(pc.getNroOrdenTrabajo() + "");
        this.modelo.consultarProduccion();
        Utilities.c_packColumn.packColumns(this.vista.jtProduccionDetalle, 1);
        Utilities.c_packColumn.packColumns(this.vista.jtRolloUtilizado, 1);
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getRolloUtilizadoTm().getList().isEmpty() && modelo.getProductosTerminadosTM().getList().isEmpty() || !esModoCreacion) {
                    vista.dispose();
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, CONFIRMAR_SALIR_MSG, VALIDAR_TITULO, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        vista.dispose();
                    }
                }
            }
        });
    }

    private void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getProductosTerminadosTM());
        this.vista.jtRolloUtilizado.setModel(modelo.getRolloUtilizadoTm());
        this.vista.jbModificarProducto.setEnabled(false);
        this.vista.jbEliminarProducto.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jtProduccionDetalle.addMouseListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbEliminarProducto.addActionListener(this);
        this.vista.jbModificarProducto.addActionListener(this);
        this.vista.jbSeleccionarRollo.addActionListener(this);
        this.vista.jbModificarRollo.addActionListener(this);
        this.vista.jbEliminarRollo.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSeleccionarProducto.addKeyListener(this);
        this.vista.jbSeleccionarRollo.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void eliminarDetalleProdTerminadoDetalle() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            modelo.removerProductoTerminado(fila);
        }
    }

    public void modificarDetalleProdTerminado() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            M_producto producto = modelo.getProductosTerminadosTM().getList().get(fila).getProducto();
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(this.vista, fila);
            scp.setProducto(producto);
            scp.setProductoCallback(this);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    public void modificarRollo() {
        int fila = this.vista.jtRolloUtilizado.getSelectedRow();
        if (fila > -1) {
            E_produccionFilm film = modelo.getRolloUtilizadoTm().getList().get(fila);
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(this.vista, fila);
            scp.setFilm(film);
            scp.setFilmCallback(this);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    private void eliminarRollo() {
        int fila = this.vista.jtRolloUtilizado.getSelectedRow();
        if (fila > -1) {
            modelo.removerRolloUtilizado(fila);
        }
    }

    private void guardarProduccion() {
        if (!validarOrdenTrabajo()) {
            return;
        }
        if (!validarResponsable()) {
            return;
        }
        if (!validarFechaProduccion()) {
            return;
        }
        if (!validarCantidadProductos()) {
            return;
        }
        if (!validarCantidadRollos()) {
            return;
        }
        Date fechaProduccion = vista.jdcFechaEntrega.getDate();
        int ordenTrabajo = Integer.valueOf(this.vista.jtfNroOrdenTrabajo.getText().trim());
        modelo.getProduccionCabecera().setFechaProduccion(fechaProduccion);
        modelo.getProduccionCabecera().setNroOrdenTrabajo(ordenTrabajo);
        modelo.guardarProduccion();
        limpiarCampos();
        cerrar();
    }

    private boolean validarFechaProduccion() {
        //Date now = Calendar.getInstance().getTime();
        Date entrega = null;
        try {
            entrega = vista.jdcFechaEntrega.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarResponsable() {
        if (this.vista.jtfFuncionario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_RESPONSABLE_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarOrdenTrabajo() {
        int ordenTrabajo = -1;
        if (this.vista.jtfNroOrdenTrabajo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_1, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
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
        E_produccionTipo pt = new E_produccionTipo(E_produccionTipo.PRODUCTO_TERMINADO, "Producto terminado");
        if (modelo.existeOrdenTrabajoPorProduccion(ordenTrabajo, pt)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadProductos() {
        if (modelo.getProductosTerminadosTM().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_PRODUCTOS_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadRollos() {
        if (modelo.getRolloUtilizadoTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_ROLLOS_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        this.modelo.limpiarCampos();
        this.vista.jtfFuncionario.setText("");
        this.vista.jtfNroOrdenTrabajo.setText("");
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
    }

    private void imprimir() {
    }

    private void invocarSeleccionarFuncionario() {
        Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    private void invocarSeleccionarProducto() {
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista);
        E_productoClasificacion pc = new E_productoClasificacion(E_productoClasificacion.PROD_TERMINADO, "");
        sp.setProductoClasificacion(pc);
        sp.setProductoCallback(this);
        sp.mostrarVista();
    }

    private void invocarSeleccionarFilmDisponible() {
        SeleccionarFilm sf = new SeleccionarFilm(vista);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarProduccion();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            invocarSeleccionarProducto();
        } else if (source.equals(this.vista.jbModificarProducto)) {
            modificarDetalleProdTerminado();
        } else if (source.equals(this.vista.jbEliminarProducto)) {
            eliminarDetalleProdTerminadoDetalle();
        } else if (source.equals(this.vista.jbSeleccionarRollo)) {
            invocarSeleccionarFilmDisponible();
        } else if (source.equals(this.vista.jbModificarRollo)) {
            modificarRollo();
        } else if (source.equals(this.vista.jbEliminarRollo)) {
            eliminarRollo();
        } else if (source.equals(this.vista.jbFuncionario)) {
            invocarSeleccionarFuncionario();
        } else if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtProduccionDetalle)) {
            this.vista.jbModificarProducto.setEnabled(true);
            this.vista.jbEliminarProducto.setEnabled(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                guardarProduccion();
                break;
            }
            case KeyEvent.VK_F2: {
                imprimir();
                break;
            }
            case KeyEvent.VK_F3: {
                Seleccionar_funcionario sf = new Seleccionar_funcionario(this.vista);
                sf.setCallback(this);
                sf.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                invocarSeleccionarProducto();
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

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.vista.jtfFuncionario.setText(funcionario.getNombre());
        this.modelo.getProduccionCabecera().setFuncionarioProduccion(funcionario);
    }

    @Override
    public void recibirFilm(E_produccionFilm detalle) {
        System.out.println("bauplast.C_crearProductoTerminado.recibirFilm()");
        System.out.println("detalle: " + detalle.getProductoClasificacion().getDescripcion());
        modelo.agregarRolloUtilizado(detalle);
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        modelo.modifacarRolloUtilizado(index, detalle);
    }

    @Override
    public void recibirProducto(double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.agregarProductoTerminado(cantidad, producto);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.modificarProductoTerminado(posicion, cantidad);
    }

}
