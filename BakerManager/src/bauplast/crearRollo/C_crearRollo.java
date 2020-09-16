/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import Empleado.SeleccionarFuncionario;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_productoClasificacion;
import Entities.M_funcionario;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import bauplast.SeleccionarProductoPorClasif;
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
class C_crearRollo extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, InterfaceRecibirProduccionFilm, RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_4 = "El número de orden de trabajo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            CONFIRMAR_SALIR_MSG = "¿Cancelar producción?",
            VALIDAR_TITULO = "Atención";

    public M_crearRollo modelo;
    public V_crearRollo vista;
    private boolean esModoCreacion;

    public C_crearRollo(M_crearRollo modelo, V_crearRollo vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.esModoCreacion = true;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modelo.getTm().getList().isEmpty() || !esModoCreacion) {
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
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbFuncionario.setEnabled(false);
        this.vista.jdcFechaEntrega.setEnabled(false);
        this.vista.jtfNroOrdenTrabajo.setEditable(false);
        //CARGAR DATOS EN LA VISTA 
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        this.vista.jdcFechaEntrega.setDate(pc.getFechaProduccion());
        this.vista.jtfFuncionario.setText(pc.getFuncionarioProduccion().getNombre());
        this.vista.jtfNroOrdenTrabajo.setText(pc.getNroOrdenTrabajo() + "");
        this.modelo.consultarProduccion();
        Utilities.c_packColumn.packColumns(this.vista.jtProduccionDetalle, 1);
    }

    private void inicializarVista() {
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        this.vista.jtMateriaPrimaUtilizada.setModel(modelo.getProduccionDetalleTM());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jtProduccionDetalle.addMouseListener(this);
        this.vista.jbFuncionario.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSeleccionarMP.addActionListener(this);
        this.vista.jbModificarMP.addActionListener(this);
        this.vista.jbEliminarMP.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSeleccionarProducto.addKeyListener(this);
        this.vista.jbFuncionario.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtfFuncionario.addKeyListener(this);
        this.vista.jtfNroOrdenTrabajo.addKeyListener(this);
    }

    private void eliminarDetalle() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            modelo.removerDetalle(fila);
        }
    }

    public void modificarDetalle() {
        int fila = this.vista.jtProduccionDetalle.getSelectedRow();
        if (fila > -1) {
            E_produccionFilm producto = modelo.getTm().getList().get(fila);
            CrearFilm crearFilm = new CrearFilm(this.vista);
            crearFilm.setCallback(this);
            crearFilm.modificarRollo(fila, producto);
            crearFilm.mostrarVista();
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
        E_produccionTipo pt = new E_produccionTipo(E_produccionTipo.ROLLO, "Rollo");
        if (modelo.existeOrdenTrabajoPorProduccion(ordenTrabajo, pt)) {
            JOptionPane.showMessageDialog(vista, VALIDAR_ORDEN_TRABAJO_MSG_4, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCantidadProductos() {
        if (modelo.getTm().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_CANT_PRODUCTOS_MSG, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
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

    private void invocarSeleccionarRollo() {
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista);
        E_productoClasificacion pc = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "");
        sp.setProductoClasificacion(pc);
        sp.setCallback(this);
        sp.mostrarVista();
    }

    private void invocarSeleccionarMP() {
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista);
        E_productoClasificacion pc = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "");
        sp.setProductoClasificacion(pc);
        sp.setProductoCallback(this);
        sp.mostrarVista();
    }

    public void modificarMP() {
        int fila = this.vista.jtMateriaPrimaUtilizada.getSelectedRow();
        if (fila > -1) {
            M_producto producto = modelo.getProduccionDetalleTM().getList().get(fila).getProducto();
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(this.vista, fila);
            scp.setProducto(producto);
            scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_MODIFICAR_PROD);
            scp.setProductoCallback(this);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    private void eliminarMP() {
        int fila = this.vista.jtMateriaPrimaUtilizada.getSelectedRow();
        if (fila > -1) {
            modelo.removerMPDetalle(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            guardarProduccion();
        } else if (source.equals(this.vista.jbSeleccionarProducto)) {
            invocarSeleccionarRollo();
        } else if (source.equals(this.vista.jbFuncionario)) {
            SeleccionarFuncionario sf = new SeleccionarFuncionario(this.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            modificarDetalle();
        } else if (source.equals(this.vista.jbSalir)) {
            cerrar();
        } else if (source.equals(this.vista.jbSeleccionarMP)) {
            invocarSeleccionarMP();
        } else if (source.equals(this.vista.jbModificarMP)) {
            modificarMP();
        } else if (source.equals(this.vista.jbEliminarMP)) {
            eliminarMP();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtProduccionDetalle)) {
            this.vista.jbModificarDetalle.setEnabled(true);
            this.vista.jbEliminarDetalle.setEnabled(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                if (!esModoCreacion) {
                    return;
                }
                guardarProduccion();
                break;
            }
            case KeyEvent.VK_F2: {
                imprimir();
                break;
            }
            case KeyEvent.VK_F3: {
                if (!esModoCreacion) {
                    return;
                }
                SeleccionarFuncionario sf = new SeleccionarFuncionario(this.vista);
                sf.setCallback(this);
                sf.mostrarVista();
                break;
            }
            case KeyEvent.VK_F4: {
                if (!esModoCreacion) {
                    return;
                }
                invocarSeleccionarRollo();
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
        modelo.agregarDetalle(detalle);
        Utilities.c_packColumn.packColumns(vista.jtProduccionDetalle, 1);
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        modelo.modificarDetalle(index, detalle);
        Utilities.c_packColumn.packColumns(vista.jtProduccionDetalle, 1);
    }

    @Override
    public void recibirFilmPosterior(E_produccionFilm detalle) {
    }

    @Override
    public void recibirProducto(double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.agregarMPDetalle(cantidad, producto);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        modelo.modificarMPDetalle(posicion, cantidad);
    }

}
