/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_productoClasificacion;
import Entities.M_producto;
import Entities.ProductoCategoria;
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
public class C_crearDesperdicioRapido extends MouseAdapter implements ActionListener, KeyListener,
        RecibirProductoCallback {

    private static final String VALIDAR_RESPONSABLE_MSG = "Seleccione un responsable de producción",
            VALIDAR_ORDEN_TRABAJO_MSG_1 = "Ingrese una orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_2 = "Ingrese solo números enteros en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_3 = "Ingrese solo números enteros y positivos en orden de trabajo",
            VALIDAR_ORDEN_TRABAJO_MSG_4 = "El número de orden de trabajo ingresado ya se encuentra en uso.",
            VALIDAR_FECHA_PRODUCCION_MSG_1 = "La fecha seleccionada no es valida.",
            VALIDAR_CANT_PRODUCTOS_MSG = "Seleccione por lo menos un producto.",
            CONFIRMAR_SALIR_MSG = "¿Cancelar producción?",
            VALIDAR_TITULO = "Atención";

    public M_crearDesperdicioRapido modelo;
    public V_crearDesperdicioRapido vista;
    private boolean esModoCreacion;

    public C_crearDesperdicioRapido(M_crearDesperdicioRapido modelo, V_crearDesperdicioRapido vista) {
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
        this.modelo.consultarProduccion();
        Utilities.c_packColumn.packColumns(vista.jtDesperdicio, 1);
    }

    private void inicializarVista() {
        this.vista.jdcFechaDesperdicio.setDate(Calendar.getInstance().getTime());
        this.vista.jtfFuncionario.setEditable(false);
    }

    private void agregarListeners() {
        this.vista.jbSeleccionarDesperdicio.addActionListener(this);
        this.vista.jbModificarDesperdicio.addActionListener(this);
        this.vista.jbEliminarDesperdicio.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
    }

    private void inicializarLogica() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
        this.vista.jtDesperdicio.setModel(modelo.getDesperdicioTM());
    }

    public void cargarDatos() {
        esModoCreacion = false;
    }

    private void invocarSeleccionDesperdicio() {
        ProductoCategoria pc = new ProductoCategoria(E_productoClasificacion.PROD_TERMINADO, E_productoClasificacion.S_MATERIA_PRIMA);
        SeleccionarProductoPorClasif sp = new SeleccionarProductoPorClasif(vista);
        sp.setProductoCallback(this);
        sp.setProductoClasificacion(pc);
        sp.mostrarVista();
    }

    private void invocarModificarRecuperados() {
        int index = vista.jtDesperdicio.getSelectedRow();
        if (index > -1) {
            M_producto producto = modelo.getDesperdicioTM().getList().get(index).getProducto();
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
        int index = vista.jtDesperdicio.getSelectedRow();
        if (index > -1) {
            if (esModoCreacion) {
                modelo.removerDesperdicio(index);
            } else {
                int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.removerDesperdicioPosterior(index);
                }
            }
        }
    }

    private boolean validarFecha() {
        Date entrega = null;
        try {
            entrega = vista.jdcFechaDesperdicio.getDate();
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
        if (modelo.getDesperdicioTM().getList().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un desperdicio", "Atención", JOptionPane.WARNING_MESSAGE);
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
        modelo.produccionDesperdicioCabecera.setObservacion(observacion);
        modelo.produccionDesperdicioCabecera.setTiempo(vista.jdcFechaDesperdicio.getDate());
        System.out.println("bauplast.desperdicio.C_crearDesperdicioRapido.guardar()");
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
            invocarModificarRecuperados();
        }
        if (source.equals(vista.jbEliminarDesperdicio)) {
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

    @Override
    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.agregarDesperdicio(cantidad, producto);
        } else {
            modelo.agregarDesperdicioPosterior(cantidad, producto);
        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicio, 1);
    }

    @Override
    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion) {
        if (esModoCreacion) {
            modelo.modificarDesperdicio(posicion, cantidad);
        } else {
            //TODO validar estado actual
            modelo.modificarDesperdicioPosterior(posicion, cantidad);
        }
        Utilities.c_packColumn.packColumns(vista.jtDesperdicio, 1);
    }
}
