/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import Entities.E_produccionFilm;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import bauplast.crearProductoTerminado.SeleccionarFilm;
import bauplast.desperdicio.M_crearDesperdicioRapido;
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
public class C_crearAjuste extends MouseAdapter implements ActionListener, KeyListener,
        RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            VALIDAR_TITULO = "Atención";

    public M_crearAjuste modelo;
    public V_crearAjuste vista;
    private boolean esModoCreacion;

    public C_crearAjuste(M_crearAjuste modelo, V_crearAjuste vista) {
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
        this.vista.setTitle(V_crearAjuste.UPDATE_TITLE);
        this.vista.jtfFuncionario.setEditable(false);
        this.modelo.consultarConteo();
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
    }

    private void inicializarVista() {
        this.vista.jdcFecha.setDate(Calendar.getInstance().getTime());
        this.vista.jtfFuncionario.setEditable(false);
    }

    private void agregarListeners() {
        this.vista.jbSeleccionarProducto.addActionListener(this);
        this.vista.jbModificarProducto.addActionListener(this);
        this.vista.jbEliminarProducto.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
    }

    private void inicializarLogica() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
        this.vista.jtDetalle.setModel(modelo.getTmDetalle());
    }

    public void cargarDatos() {
        esModoCreacion = false;
    }

    private void invocarSeleccionProduccion() {
//        ProductoCategoria pc = new ProductoCategoria(E_productoClasificacion.PROD_TERMINADO, E_productoClasificacion.S_MATERIA_PRIMA);
//        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista, SeleccionarProductoTableModel.DETALLE);
//        sp.setProductoCallback(this);
//        sp.setProductoClasificacion(pc);
//        sp.mostrarVista();
    }

    private void buscarProduccion() {
//        SeleccionarFilm sf = new SeleccionarFilm(vista);
//        if (!esModoCreacion) {
//            int opcion = JOptionPane.showConfirmDialog(vista, "Al cargar un nuevo rollo ya no se podrá revertir la acción. ¿Está seguro que desea continuar?.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
//            if (opcion != JOptionPane.YES_OPTION) {
//                return;
//            }
//            sf.desactivarModoCreacion();
//        }
//        sf.setCallback(this);
//        sf.mostrarVista();
    }

    private void invocarModificarBaja() {
        int fila = this.vista.jtDetalle.getSelectedRow();
        if (fila > -1) {
            M_producto prod = modelo.getTmDetalle().getList().get(fila).getProducto();
            if (!esModoCreacion) {
//                E_produccionFilm filmAux = modelo.obtenerRollo(fila);
//                double pesoDisponible = filmAux.getPesoActual();
//                double pesoActual = film.getPeso();
//                film.setPeso(filmAux.getPeso());
//                film.setPesoUtilizado(filmAux.getPesoUtilizado());
//                film.setPesoActual(pesoDisponible + pesoActual);
            }
//            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(this.vista, false);
//            scp.setUpdateIndex(fila);
//            scp.setTipo(SeleccionCantidadProductoSimple.ROLLO);
//            scp.setFilm(prod);
//            scp.setFilmCallback(this);
//            scp.inicializarVista();
//            scp.setVisible(true);
        }
    }

    private void eliminarProducto() {
        int index = vista.jtDetalle.getSelectedRow();
        if (index > -1) {
            modelo.removerProducto(index);
//            if (esModoCreacion) {
//            } else {
//                int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
//                if (opcion == JOptionPane.YES_OPTION) {
//                    modelo.removerBajaPosterior(index);
//                }
//            }
        }
    }

    private boolean validarFecha() {
        Date entrega = null;
        try {
            entrega = vista.jdcFecha.getDate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, VALIDAR_FECHA_PRODUCCION_MSG_1, "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (entrega.after(Calendar.getInstance().getTime())) {
            JOptionPane.showMessageDialog(vista, "La fecha del desperdicio no puede ser mayor al tiempo actual", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
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
        if (modelo.getTmDetalle().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un desperdicio", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCambioTipoBaja() {
        if (!modelo.getTmDetalle().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Vacíe la selección antes de cambiar el tipo de baja", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
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
        modelo.getCabecera().setTiempo(vista.jdcFecha.getDate());
        modelo.guardar();
        cerrar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbSeleccionarProducto)) {
            buscarProduccion();
        }
        if (source.equals(vista.jbModificarProducto)) {
            invocarModificarBaja();
        }
        if (source.equals(vista.jbEliminarProducto)) {
            eliminarProducto();
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

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        modelo.agregarProducto(cantidad, producto);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        modelo.modificarProducto(posicion, cantidad, producto, observacion);
        Utilities.c_packColumn.packColumns(vista.jtDetalle, 1);
    }
}
