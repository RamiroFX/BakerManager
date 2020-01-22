/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionTipo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verProduccion extends MouseAdapter implements ActionListener, KeyListener {

    private static final String PRODUCCION_TITULO = "Ver produccion";

    public M_verProduccion modelo;
    public V_crearProduccion vista;

    public C_verProduccion(M_verProduccion modelo, V_crearProduccion vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        vista.dispose();
    }

    private void inicializarVista() {
        this.vista.setTitle(PRODUCCION_TITULO);
        this.vista.jtProduccionDetalle.setModel(modelo.getTm());
        //E_productoClasificacion pc1 = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "Productos terminados");
        //E_productoClasificacion pc2 = new E_productoClasificacion(E_productoClasificacion.PRODUCTO_TERMINADO, "Rollos");
        ArrayList<E_produccionTipo> tipoProduccion = modelo.obtenerProduccionTipo();
        for (int i = 0; i < tipoProduccion.size(); i++) {
            this.vista.jcbTipoProduccion.addItem(tipoProduccion.get(i));
        }
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
        establecerTipoProduccion();
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void establecerTipoProduccion() {
        int selectedIndex = this.vista.jcbTipoProduccion.getSelectedIndex();
        E_produccionTipo tipo = this.vista.jcbTipoProduccion.getItemAt(selectedIndex);
        modelo.getProduccionCabecera().setTipo(tipo);
    }

    private void imprimir() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbSalir)) {
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F2: {
                imprimir();
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

    public void verPedidoRegistrado(int idProduccion) {
        modelo.obtenerProduccionCabecera(idProduccion);
        this.vista.jcbTipoProduccion.setSelectedItem(modelo.getProduccionCabecera().getTipo());
        this.vista.jtfNroOrdenTrabajo.setText(modelo.getProduccionCabecera().getNroOrdenTrabajo() + "");
        this.vista.jtfFuncionario.setText(modelo.getProduccionCabecera().getFuncionarioProduccion().getNombre());
        this.vista.jdcFechaEntrega.setDate(modelo.getProduccionCabecera().getFechaProduccion());
        this.vista.setTitle(PRODUCCION_TITULO + " (Tiempo de registro: " + modelo.getFechaProduccionFormateada() + ") - (Registrado por: " + modelo.getProduccionCabecera().getFuncionarioSistema().getNombre() + ")");
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jbFuncionario.setEnabled(false);
        this.vista.jcbTipoProduccion.setEnabled(false);
        this.vista.jdcFechaEntrega.setEnabled(false);
        this.vista.jtfNroOrdenTrabajo.setEnabled(false);
        this.vista.jbSeleccionarProducto.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);

    }
}
