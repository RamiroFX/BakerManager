/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import DB.DB_Produccion;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
import Interface.RecibirProductoCallback;
import Produccion.SeleccionCantidadProductoSimple;
import static Produccion.SeleccionCantidadProductoSimple.PROD_TERMINADO_AGREGAR_ROLLO;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarProduccion extends MouseAdapter implements ActionListener, KeyListener {

    private static final String ENTER_KEY = "Entrar";

    private M_seleccionarProduccion modelo;
    private V_seleccionarProduccion vista;
    private InterfaceRecibirProduccionFilm callback;
    private InterfaceRecibirProduccionTerminados productoCallback;
    private boolean isProductoTerminado;//productoTerminado=true;rollo=false
    private boolean esModoCreacion;

    public C_seleccionarProduccion(M_seleccionarProduccion modelo, V_seleccionarProduccion vista) {
        this.vista = vista;
        this.modelo = modelo;
        this.isProductoTerminado = false;
        this.esModoCreacion = true;
        agregarListeners();
    }

    public void setRolloCallback(InterfaceRecibirProduccionFilm callback) {
        this.isProductoTerminado = false;
        this.callback = callback;
    }

    public void setProductoTerminadoCallback(InterfaceRecibirProduccionTerminados productoCallback) {
        this.isProductoTerminado = true;
        this.productoCallback = productoCallback;
    }

    public void mostrarVista() {
        inicializarVista();
        this.vista.setVisible(true);
        this.vista.requestFocus();
    }

    private void inicializarVista() {
        this.vista.jbAceptar.setEnabled(false);
        switch (modelo.obtenerTipoProduccion()) {
            case E_produccionTipo.PRODUCTO_TERMINADO: {
                this.vista.jtProducto.setModel(modelo.getProduccionTerminadosTM());
                break;
            }
            case E_produccionTipo.ROLLO: {
                this.vista.jtProducto.setModel(modelo.getProduccionRollosTM());
                break;
            }
        }
        modelo.consultarProduccion();
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.vista.jtProducto.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, ENTER_KEY);
        this.vista.jtProducto.getActionMap().put(ENTER_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarProducto();
            }
        });
        Utilities.c_packColumn.packColumns(this.vista.jtProducto, 1);
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtProducto.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jtProducto.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void seleccionarProducto() {
        int fila = vista.jtProducto.getSelectedRow();
        if (fila > -1) {
            switch (modelo.obtenerTipoProduccion()) {
                case E_produccionTipo.PRODUCTO_TERMINADO: {
                    E_produccionDetalle pf = modelo.getProduccionTerminadosTM().getList().get(fila);
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, -1);
                    if (esModoCreacion) {
                        scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_AGREGAR_PROD);
                    } else {
                        scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_MODIFICAR_PROD);
                    }
                    scp.setProducto(pf.getProducto());
                    scp.setProductoCallback(productoCallback);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
                case E_produccionTipo.ROLLO: {
                    E_produccionFilm pf = modelo.getProduccionRollosTM().getList().get(fila);
                    pf.setPesoActual(pf.getPeso());
                    SeleccionCantidadProductoSimple scp = new SeleccionCantidadProductoSimple(vista, -1);
                    if (esModoCreacion) {
                        scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_AGREGAR_ROLLO);
                    } else {
                        scp.setTipo(SeleccionCantidadProductoSimple.PROD_TERMINADO_ACTUALIZAR_ROLLO);
                    }
                    scp.setFilm(pf);
                    scp.setFilmCallback(callback);
                    scp.inicializarVista();
                    scp.setVisible(true);
                    break;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jbAceptar) {
            seleccionarProducto();
        }
        if (e.getSource() == this.vista.jbSalir) {
            cerrar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = this.vista.jtProducto.rowAtPoint(e.getPoint());
        int columna = this.vista.jtProducto.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)) {
            seleccionarProducto();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.vista.jtProducto.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
