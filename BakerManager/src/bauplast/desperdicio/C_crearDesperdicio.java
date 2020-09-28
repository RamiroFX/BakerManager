/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_productoClasificacion;
import Entities.M_funcionario;
import Entities.M_producto;
import Entities.ProductoCategoria;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import bauplast.SeleccionarProductoPorClasif;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

/**
 *
 * @author Ramiro Ferreira
 */
class C_crearDesperdicio extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, InterfaceRecibirProduccionFilm, InterfaceRecibirProduccionTerminados, RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_4 = "El número de orden de trabajo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            CONFIRMAR_SALIR_MSG = "¿Cancelar producción?",
            VALIDAR_TITULO = "Atención";

    public M_crearDesperdicio modelo;
    public V_crearDesperdicio vista;
    private boolean esModoCreacion;

    public C_crearDesperdicio(M_crearDesperdicio modelo, V_crearDesperdicio vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.esModoCreacion = true;
        agregarListeners();
    }

    public void mostrarVista() {
        cargarDatos();
        vista.setVisible(true);
    }

    public void inicializarVista() {
        this.vista.jdcFechaEntrega.setDate(Calendar.getInstance().getTime());
        this.vista.jtfFuncionario.setEditable(false);
        this.vista.jtfNroOrdenTrabajo.setEditable(false);
    }

    public void agregarListeners() {
        this.vista.jbSeleccionarDesperdicio.addActionListener(this);
        this.vista.jbModificarDesperdicio.addActionListener(this);
        this.vista.jbEliminarDesperdicio.addActionListener(this);
        this.vista.jbSeleccionarRecuperado.addActionListener(this);
        this.vista.jbModificarRecuperado.addActionListener(this);
    }

    private void cargarDatos() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
        this.vista.jtDesperdicioRecuperado.setModel(modelo.getProduccionRecuperadosTM());
        this.vista.jtfNroOrdenTrabajo.setText(modelo.obtenerOrdenTrabajo());
        switch (modelo.obtenerTipoProduccion()) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                vista.jtProduccionDesperdicio.setModel(modelo.getProduccionTerminadosTM());
                break;
            }
            case E_produccionTipo.ROLLO: {
                vista.jtProduccionDesperdicio.setModel(modelo.getProduccionRollosTM());
                break;
            }
        }
    }

    private void invocarSeleccionDesperdicio() {
        SeleccionarProduccion sp = new SeleccionarProduccion(vista, true);
        sp.establecerProduccionCabecera(modelo.produccionCabecera.getProduccionCabecera());
        sp.setRolloCallback(this);
        sp.mostrarVista();
    }

    private void invocarModificarDesperdicio() {
        int index = vista.jtProduccionDesperdicio.getSelectedRow();
        if (index > -1) {
            switch (modelo.obtenerTipoProduccion()) {
                case E_produccionTipo.PRODUCTO_TERMINADO: {
                    E_produccionDetalle pf = modelo.getProduccionTerminadosTM().getList().get(index);
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, false);
                    scp.setUpdateIndex(index);
                    scp.setTipo(SeleccionCantidadProductoSimple.PRODUCCION_TERMINADOS);
                    scp.setProduccionTerminados(pf);
                    scp.setProduccionTerminadosCallback(this);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
                case E_produccionTipo.ROLLO: {
                    E_produccionFilm pf = modelo.getProduccionRollosTM().getList().get(index);
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, false);
                    scp.setUpdateIndex(index);
                    scp.setTipo(SeleccionCantidadProductoSimple.ROLLO);
                    scp.setFilm(pf);
                    scp.setFilmCallback(this);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
            }
        }
    }

    private void eliminarDesperdicio() {
        int index = vista.jtProduccionDesperdicio.getSelectedRow();
        if (index > -1) {
            switch (modelo.obtenerTipoProduccion()) {
                case E_produccionTipo.PRODUCTO_TERMINADO: {
                    if (esModoCreacion) {
                        modelo.removerTerminado(index);
                    } else {

                    }
                    break;
                }
                case E_produccionTipo.ROLLO: {
                    if (esModoCreacion) {
                        modelo.removerFilm(index);
                    } else {

                    }
                    break;
                }
            }
        }
    }

    private void invocarSeleccionRecuperados() {
        ProductoCategoria pc = new ProductoCategoria(E_productoClasificacion.MATERIA_PRIMA, E_productoClasificacion.S_MATERIA_PRIMA);
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista);
        sp.setProductoCallback(this);
        sp.setProductoClasificacion(pc);
        sp.mostrarVista();
    }

    private void invocarModificarRecuperados() {
        int index = vista.jtDesperdicioRecuperado.getSelectedRow();
        if (index > -1) {
            M_producto producto = modelo.getProduccionRecuperadosTM().getList().get(index).getProducto();
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, false);
            scp.setUpdateIndex(index);
            scp.setTipo(SeleccionCantidadProductoSimple.PRODUCTO);
            scp.setProducto(producto);
            scp.setProductoCallback(this);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    private void eliminarRecuperado() {
        int index = vista.jtDesperdicioRecuperado.getSelectedRow();
        if (index > -1) {
            if (esModoCreacion) {
                modelo.removerRecuperado(index);
            } else {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbSeleccionarDesperdicio)) {
            invocarSeleccionDesperdicio();
        }
        if (source.equals(vista.jbModificarDesperdicio)) {
            invocarModificarDesperdicio();
        }
        if (source.equals(vista.jbEliminarDesperdicio)) {
            eliminarDesperdicio();
        }
        if (source.equals(vista.jbSeleccionarRecuperado)) {
            invocarSeleccionRecuperados();
        }
        if (source.equals(vista.jbModificarRecuperado)) {
            invocarModificarRecuperados();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
    }

    @Override
    public void recibirFilm(E_produccionFilm detalle) {
        if (esModoCreacion) {
            modelo.agregarFilm(detalle);
        } else {

        }
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        if (esModoCreacion) {
            modelo.modificarFilm(index, detalle);
        } else {

        }
    }

    @Override
    public void recibirProductoTerminado(E_produccionDetalle detalle) {
        if (esModoCreacion) {
            modelo.agregarTerminados(detalle);
        } else {

        }
    }

    @Override
    public void modificarProductoTerminado(int index, E_produccionDetalle detalle) {
        if (esModoCreacion) {
            modelo.modificarTerminados(index, detalle);
        } else {

        }
    }

    @Override
    public void recibirProducto(double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.agregarRecuperados(cantidad, producto);
        } else {

        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicioRecuperado, 1);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, int precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.modificarRecuperados(posicion, cantidad);
        } else {

        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicioRecuperado, 1);
    }
}
