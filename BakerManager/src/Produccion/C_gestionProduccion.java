/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.Estado;
import Interface.GestionInterface;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionProduccion implements GestionInterface {

    public M_gestionProduccion modelo;
    public V_gestionProduccion vista;
    public C_inicio c_inicio;

    public C_gestionProduccion(M_gestionProduccion modelo, V_gestionProduccion vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    @Override
    public final void inicializarVista() {
        E_productoClasificacion pc1 = new E_productoClasificacion(E_productoClasificacion.MATERIA_PRIMA, "Materia prima");
        E_productoClasificacion pc2 = new E_productoClasificacion(E_productoClasificacion.PRODUCTO_TERMINADO, "Producto terminado");
        this.vista.jcbCondVenta.addItem(pc1);
        this.vista.jcbCondVenta.addItem(pc2);
        ArrayList<Estado> estados = modelo.obtenerEstados();
        for (int i = 0; i < estados.size(); i++) {
            Estado get = estados.get(i);
            this.vista.jcbEstado.addItem(get);
        }
    }

    @Override
    public final void concederPermisos() {
        //TODO add access
        this.vista.jbAgregar.addActionListener(this);
    }

    private void verificarPermiso() {
    }

    @Override
    public void mostrarVista() {
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        this.c_inicio.agregarVentana(this.vista);
    }

    @Override
    public void cerrar() {
        try {
            this.vista.setClosed(true);
            System.runFinalization();
        } catch (PropertyVetoException ex) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbAgregar)) {
            CrearProduccion cp = new CrearProduccion(c_inicio);
            cp.mostrarVista();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
}
