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
import Entities.M_producto;
import Entities.ProductoCategoria;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
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
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
class C_crearDesperdicio extends MouseAdapter implements ActionListener, KeyListener,
        InterfaceRecibirProduccionFilm, InterfaceRecibirProduccionTerminados, RecibirProductoCallback {

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
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        inicializarLogica();
        vista.setVisible(true);
    }

    public void cerrar() {
        vista.dispose();
        System.runFinalization();
    }

    public void esablecerModoActualizacion() {
        this.esModoCreacion = false;
        this.vista.setTitle(V_crearDesperdicio.UPDATE_TITLE);
        this.vista.jtfFuncionario.setEditable(false);
        this.vista.jtfNroOrdenTrabajo.setEditable(false);
        this.modelo.consultarProduccion();
        Utilities.c_packColumn.packColumns(vista.jtProduccionDesperdicio, 1);
        Utilities.c_packColumn.packColumns(vista.jtDesperdicioRecuperado, 1);
    }

    private void inicializarVista() {
        this.vista.jdcFechaEntrega.setDate(Calendar.getInstance().getTime());
        this.vista.jtfFuncionario.setEditable(false);
        this.vista.jtfNroOrdenTrabajo.setEditable(false);
    }

    private void agregarListeners() {
        this.vista.jbSeleccionarDesperdicio.addActionListener(this);
        this.vista.jbModificarDesperdicio.addActionListener(this);
        this.vista.jbEliminarDesperdicio.addActionListener(this);
        this.vista.jbSeleccionarRecuperado.addActionListener(this);
        this.vista.jbModificarRecuperado.addActionListener(this);
        this.vista.jbEliminarRecuperado.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
    }

    private void inicializarLogica() {
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

    public void cargarDatos() {
        esModoCreacion = false;
    }

    private void invocarSeleccionDesperdicio() {
        SeleccionarProduccion sp = new SeleccionarProduccion(vista, true);
        sp.establecerProduccionCabecera(modelo.produccionDesperdicioCabecera.getProduccionCabecera());
        switch (modelo.obtenerTipoProduccion()) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                sp.setProductoTerminadoCallback(this);
                break;
            }
            case E_produccionTipo.ROLLO: {
                sp.setRolloCallback(this);
                break;
            }
        }
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
                    if (!esModoCreacion) {
                        E_produccionFilm pfAux = modelo.obtenerRollo(pf.getId());
                        pf.setPesoActual(pfAux.getPesoActual());
                    }
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
                        int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                        if (opcion == JOptionPane.YES_OPTION) {
                            modelo.removerTerminadoPosterior(index);
                        }
                    }
                    break;
                }
                case E_produccionTipo.ROLLO: {
                    if (esModoCreacion) {
                        modelo.removerFilm(index);
                    } else {
                        int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                        if (opcion == JOptionPane.YES_OPTION) {
                            modelo.removerFilmPosterior(index);
                        }
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
                int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.removerRecuperadoPosterior(index);
                }
            }
        }
    }

    private boolean validarFecha() {
        Date entrega = null;
        try {
            entrega = vista.jdcFechaEntrega.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (entrega.before(modelo.produccionDesperdicioCabecera.getProduccionCabecera().getFechaProduccion())) {
            JOptionPane.showMessageDialog(vista, "La fecha del desperdicio no puede ser menor a la de producción", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarObservacion() {
        String obs = vista.jtfObservacion.getText().trim();
        if (obs.length() > 200) {
            JOptionPane.showMessageDialog(vista, "Solo se permiten 200 caracteres en observación. Cant. actual: " + obs.length(), VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarFilas() {
        switch (modelo.obtenerTipoProduccion()) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                if (modelo.getProduccionTerminadosTM().getList().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un desperdicio", VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            }
            case E_produccionTipo.ROLLO: {
                if (modelo.getProduccionRollosTM().getList().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese un desperdicio", "Atención", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            }
        }
        return true;
    }

    private void guardar() {
        if (!esModoCreacion) {
            return;
        }
        if (!validarFilas()) {
            return;
        }
        if (!validarFecha()) {
            return;
        }
        if (!validarObservacion()) {
            return;
        }
        String observacion = vista.jtfObservacion.getText().trim();
        modelo.produccionDesperdicioCabecera.setObservacion(observacion);
        modelo.guardar();
        cerrar();
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
        if (source.equals(vista.jbEliminarRecuperado)) {
            eliminarRecuperado();
        }
        if (source.equals(vista.jbAceptar)) {
            guardar();
        }
        if (source.equals(vista.jbSalir)) {
            cerrar();
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

    /*
    Para cargar bobinas(films)
     */
    @Override
    public void recibirFilm(E_produccionFilm detalle) {
        double peso = detalle.getPeso();
        double pesoDisponible = modelo.obtenerRollo(detalle.getId()).getPesoActual();
        if (peso > pesoDisponible) {
            JOptionPane.showMessageDialog(vista, "La cantidad seleccionada supera a la disponible \n Cantidad seleccionada: " + peso + " \n Cantidad disponible: " + pesoDisponible, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (esModoCreacion) {
            modelo.agregarFilm(detalle);
        } else {
            modelo.agregarFilmPosterior(detalle);
        }
        Utilities.c_packColumn.packColumns(vista.jtProduccionDesperdicio, 1);
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        double peso = detalle.getPeso();
        double pesoDisponible = modelo.obtenerRollo(detalle.getId()).getPesoActual();
        if (peso > pesoDisponible) {
            JOptionPane.showMessageDialog(vista, "La cantidad seleccionada supera a la disponible \n Cantidad seleccionada: " + peso + " \n Cantidad disponible: " + pesoDisponible, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (esModoCreacion) {
            modelo.modificarFilm(index, detalle);
        } else {
            modelo.modificarFilmPosterior(index, detalle);
        }
        Utilities.c_packColumn.packColumns(vista.jtProduccionDesperdicio, 1);
    }

    /*
    Para cargar productos terminados
     */
    @Override
    public void recibirProductoTerminado(E_produccionDetalle detalle) {
        double cantidad = detalle.getCantidad();
        double cantDisponible = modelo.obtenerProduccionDetalle(detalle.getId()).getCantidad();
        if (cantidad > cantDisponible) {
            JOptionPane.showMessageDialog(vista, "La cantidad seleccionada supera a la disponible \n Cantidad seleccionada: " + cantidad + " \n Cantidad disponible: " + cantDisponible, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (esModoCreacion) {
            modelo.agregarTerminados(detalle);
        } else {
            modelo.agregarTerminadosPosterior(detalle);
        }
        Utilities.c_packColumn.packColumns(vista.jtProduccionDesperdicio, 1);
    }

    @Override
    public void modificarProductoTerminado(int index, E_produccionDetalle detalle) {
        int idProduccionDetalle = modelo.obtenerProduccionDesperdicioDetallePorId(detalle.getId()).getProduccionDetalle().getId();
        double cantidad = detalle.getCantidad();
        double cantDisponible = modelo.obtenerProduccionDetalle(idProduccionDetalle).getCantidad();
        if (cantidad > cantDisponible) {
            JOptionPane.showMessageDialog(vista, "La cantidad seleccionada supera a la disponible \n Cantidad seleccionada: " + cantidad + " \n Cantidad disponible: " + cantDisponible, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (esModoCreacion) {
            modelo.modificarTerminados(index, detalle);
        } else {
            modelo.modificarTerminadosPosterior(index, detalle);
        }
        Utilities.c_packColumn.packColumns(vista.jtProduccionDesperdicio, 1);
    }

    /*
    Para cargar materia prima
     */
    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.agregarRecuperado(cantidad, producto);
        } else {
            modelo.agregarRecuperadoPosterior(cantidad, producto);
        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicioRecuperado, 1);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.modificarRecuperado(posicion, cantidad);
        } else {
            //TODO validar estado actual
            modelo.modificarRecuperadoPosterior(posicion, cantidad);
        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicioRecuperado, 1);
    }
}
