/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import Entities.E_produccionFilm;
import Interface.InterfaceRecibirProduccionFilm;
import Produccion.SeleccionCantidadProductoSimple;
import bauplast.crearProductoTerminado.SeleccionarFilm;
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
        InterfaceRecibirProduccionFilm {

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
        Utilities.c_packColumn.packColumns(vista.jtBajaFilm, 1);
    }

    private void inicializarVista() {
        this.vista.jdcFechaDesperdicio.setDate(Calendar.getInstance().getTime());
        this.vista.jtfFuncionario.setEditable(false);
        modelo.obtenerTipoBajas().forEach((unaBaja) -> {
            this.vista.jcbTipoBaja.addItem(unaBaja);
        });
    }

    private void agregarListeners() {
        this.vista.jbSeleccionarDesperdicio.addActionListener(this);
        this.vista.jbModificarDesperdicio.addActionListener(this);
        this.vista.jbEliminarDesperdicio.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jcbTipoBaja.addActionListener(this);
    }

    private void inicializarLogica() {
        this.vista.jtfFuncionario.setText(modelo.obtenerFuncionario());
        this.vista.jtBajaFilm.setModel(modelo.getDesperdicioTM());
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
        SeleccionarFilm sf = new SeleccionarFilm(vista);
        if (!esModoCreacion) {
            int opcion = JOptionPane.showConfirmDialog(vista, "Al cargar un nuevo rollo ya no se podrá revertir la acción. ¿Está seguro que desea continuar?.", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
            sf.desactivarModoCreacion();
        }
        sf.setCallback(this);
        sf.mostrarVista();
    }

    private void invocarModificarBaja() {
        int fila = this.vista.jtBajaFilm.getSelectedRow();
        if (fila > -1) {
            E_produccionFilm film = modelo.getDesperdicioTM().getList().get(fila);
            if (!esModoCreacion) {
//                E_produccionFilm filmAux = modelo.obtenerRollo(fila);
//                double pesoDisponible = filmAux.getPesoActual();
//                double pesoActual = film.getPeso();
//                film.setPeso(filmAux.getPeso());
//                film.setPesoUtilizado(filmAux.getPesoUtilizado());
//                film.setPesoActual(pesoDisponible + pesoActual);
            }
            SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(this.vista, false);
            scp.setUpdateIndex(fila);
            scp.setTipo(SeleccionCantidadProductoSimple.ROLLO);
            scp.setFilm(film);
            scp.setFilmCallback(this);
            scp.inicializarVista();
            scp.setVisible(true);
        }
    }

    private void eliminarBaja() {
        int index = vista.jtBajaFilm.getSelectedRow();
        if (index > -1) {
            if (esModoCreacion) {
                modelo.removerBaja(index);
            } else {
                int opcion = JOptionPane.showConfirmDialog(vista, "Confirmar", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.removerBajaPosterior(index);
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

    private boolean validarCambioTipoBaja() {
        if (!modelo.getDesperdicioTM().getList().isEmpty()) {
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
            buscarProduccion();
        }
        if (source.equals(vista.jbModificarDesperdicio)) {
            invocarModificarBaja();
        }
        if (source.equals(vista.jbEliminarDesperdicio)) {
            eliminarBaja();
        }
        if (source.equals(vista.jbAceptar)) {
            guardar();
        }
        if (source.equals(vista.jcbTipoBaja)) {
            validarCambioTipoBaja();
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
    public void recibirFilm(E_produccionFilm detalle) {
        modelo.agregarBajaFilm(detalle);
        Utilities.c_packColumn.packColumns(vista.jtBajaFilm, 1);
    }

    @Override
    public void modificarFilm(int index, E_produccionFilm detalle) {
        modelo.modificarBajaFilm(index, detalle);
        Utilities.c_packColumn.packColumns(vista.jtBajaFilm, 1);
    }
}
