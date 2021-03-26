/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_previsionStock extends MouseAdapter implements ActionListener, KeyListener {

    public V_previsionStock vista;
    public M_previsionStock modelo;

    public C_previsionStock(M_previsionStock modelo, V_previsionStock vista) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarComponentes();
        agregarListeners();
        this.modelo.actualizarTablaCabecera();
    }

    void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void inicializarComponentes() {
        this.vista.jbVerDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jtCabecera.setModel(this.modelo.getTmCabecera());
        this.vista.jtDetalle.setModel(this.modelo.getTmDetalle());
        Utilities.c_packColumn.packColumns(this.vista.jtCabecera, 1);
    }

    private void agregarListeners() {
        this.vista.jbCrearAjuste.addActionListener(this);
        this.vista.jbCrearAjuste.addKeyListener(this);
        this.vista.jbVerDetalle.addActionListener(this);
        this.vista.jbVerDetalle.addKeyListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtCabecera.addMouseListener(this);
        this.vista.jtCabecera.addKeyListener(this);
    }

    public void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void facturaCabeceraHandler(MouseEvent e) {
        int row = this.vista.jtCabecera.getSelectedRow();
        if (row > -1) {
            int idCabecera = modelo.getTmCabecera().getList().get(row).getId();
            consultarDetalle(idCabecera);
            this.vista.jbVerDetalle.setEnabled(true);
            this.vista.jbEliminarDetalle.setEnabled(true);
        } else {
            this.vista.jbVerDetalle.setEnabled(false);
            this.vista.jbEliminarDetalle.setEnabled(false);
        }
        if (e.getClickCount() == 2) {
            verDetalle();
            this.vista.jbVerDetalle.setEnabled(false);
            this.vista.jbEliminarDetalle.setEnabled(false);
        }
    }

    private void verDetalle() {
        int row = this.vista.jtCabecera.getSelectedRow();
        if (row < 0) {
            return;
        }
        int idCabecera = modelo.getTmCabecera().getList().get(row).getId();
        CrearAjuste ca = new CrearAjuste(vista, idCabecera, true);
        ca.mostrarVista();
        this.modelo.actualizarTablaCabecera();
        Utilities.c_packColumn.packColumns(this.vista.jtCabecera, 1);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbVerDetalle.setEnabled(false);
    }

    private void crearAjuste() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int id = modelo.crearAjusteStock();
                modelo.actualizarTablaCabecera();
                CrearAjuste ca = new CrearAjuste(vista, id, true);
                ca.mostrarVista();
            }
        });
        this.vista.jbVerDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
    }

    private void eliminarDetalle() {
        int row = this.vista.jtCabecera.getSelectedRow();
        if (row < 0) {
            return;
        }
        int option = JOptionPane.showConfirmDialog(this.vista, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            int idCabecera = modelo.getTmCabecera().getList().get(row).getId();
            this.modelo.eliminarAjusteStock(idCabecera);
            this.modelo.actualizarTablaCabecera();
            Utilities.c_packColumn.packColumns(this.vista.jtCabecera, 1);
            this.vista.jbEliminarDetalle.setEnabled(false);
            this.vista.jbVerDetalle.setEnabled(false);
        }
    }

    public void actualizarTablaCabecera() {
        modelo.actualizarTablaCabecera();
    }

    public void consultarDetalle(int idCabecera) {
        modelo.actualizarTablaDetalle(idCabecera);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jtCabecera)) {
            facturaCabeceraHandler(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            crearAjuste();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbCrearAjuste)) {
            crearAjuste();
        }
        if (e.getSource().equals(this.vista.jbEliminarDetalle)) {
            eliminarDetalle();
        }
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbVerDetalle)) {
            verDetalle();
        }
    }
}
