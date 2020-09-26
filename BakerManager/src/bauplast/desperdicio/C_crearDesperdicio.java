/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Empleado.SeleccionarFuncionario;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_productoClasificacion;
import Entities.M_funcionario;
import Entities.M_menu_item;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
import Interface.RecibirEmpleadoCallback;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import Produccion.V_gestionProduccion;
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
class C_crearDesperdicio extends MouseAdapter implements ActionListener, KeyListener,
        RecibirEmpleadoCallback, InterfaceRecibirProduccionFilm, InterfaceRecibirProduccionTerminados {

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
    }

    private void cargarDatos() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
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
        SeleccionarProduccion sp = new SeleccionarProduccion(vista);
        sp.establecerProduccionCabecera(modelo.produccionCabecera.getProduccionCabecera());
        sp.setRolloCallback(this);
        sp.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jbSeleccionarDesperdicio)) {
            invocarSeleccionDesperdicio();
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
        modelo.getProduccionRollosTM().agregarDatos(detalle);
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirFilmPosterior(E_produccionFilm detalle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirProductoTerminado(E_produccionDetalle detalle) {
        modelo.produccionTerminadosTM.agregarDetalle(detalle);
    }

    @Override
    public void modificarFilm(int index, E_produccionDetalle detalle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
