/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

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
public class C_verUtilizacion extends MouseAdapter implements ActionListener, KeyListener {

    private static final String PRODUCCION_TITULO = "Ver utilizacion de MP";

    public M_verUtilizacion modelo;
    public V_utilizarMateriaPrima vista;

    public C_verUtilizacion(M_verUtilizacion modelo, V_utilizarMateriaPrima vista) {
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
        this.vista.jtMateriaPrimaDetalle.setModel(modelo.getTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        this.vista.jdcFechaEntrega.setDate(calendar.getTime());
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
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

    public void verRegistro(int idUtilizacionMP) {
        modelo.obtenerUtilizacionCabecera(idUtilizacionMP);
        this.vista.jtfNroOrdenTrabajo.setText(modelo.getCabecera().getNroOrdenTrabajo() + "");
        this.vista.jtfFuncionario.setText(modelo.getCabecera().getFuncionarioProduccion().getNombre());
        this.vista.jdcFechaEntrega.setDate(modelo.getCabecera().getFechaUtilizacion());
        this.vista.setTitle(PRODUCCION_TITULO + " (Tiempo de registro: " + modelo.getFechaRegistroFormateada() + ") - (Registrado por: " + modelo.getCabecera().getFuncionarioSistema().getNombre() + ")");
        this.vista.jbAceptar.setEnabled(false);
        this.vista.jbFuncionario.setEnabled(false);
        this.vista.jdcFechaEntrega.setEnabled(false);
        this.vista.jtfNroOrdenTrabajo.setEnabled(false);
        this.vista.jbSeleccionarProducto.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
    }
}
