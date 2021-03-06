/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verTimbrado implements ActionListener, KeyListener {

    public M_verTimbrado modelo;
    public V_crearTimbrado vista;
    NumberFormat nf, nf2;

    public C_verTimbrado(M_verTimbrado modelo, V_crearTimbrado vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVariables();
        agregarListeners();
    }

    private void inicializarVariables() {
        nf = new DecimalFormat("000");
        nf2 = new DecimalFormat("0000000");
    }

    public void cargarDatos(E_Timbrado timbrado) {
        this.modelo.cargarDatos(timbrado);
        String creador = modelo.getCabecera().getCreador().getNombre();
        String fechaCreacion = modelo.getCabecera().getFechaCreacion() + "";
        this.vista.setTitle(V_crearTimbrado.READ_TITLE + " (Creado por " + creador + " el " + fechaCreacion + ")");
        inicializarVista();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtfDescripcion.setEditable(false);
        this.vista.jtfNroTimbrado.setEditable(false);
        this.vista.jtfNroSucursal.setEditable(false);
        this.vista.jtfPuntoVenta.setEditable(false);
        this.vista.jtfBoletaInicial.setEditable(false);
        this.vista.jtfBoletaFinal.setEditable(false);
        this.vista.jdcFechaVencimiento.setEnabled(false);
        this.vista.jbAceptar.setVisible(false);
        this.vista.jtfDescripcion.setText(modelo.getCabecera().getDescripcion());
        this.vista.jdcFechaVencimiento.setDate(modelo.getCabecera().getFechaVencimiento());
        this.vista.jtfNroTimbrado.setText(nf.format(modelo.getCabecera().getNroTimbrado()));
        this.vista.jtfNroSucursal.setText(nf.format(modelo.getCabecera().getNroSucursal()));
        this.vista.jtfPuntoVenta.setText(nf.format(modelo.getCabecera().getNroPuntoVenta()));
        this.vista.jtfBoletaInicial.setText(nf2.format(modelo.getCabecera().getNroBoletaInicial()));
        this.vista.jtfBoletaFinal.setText(nf2.format(modelo.getCabecera().getNroBoletaFinal()));
        for (int i = 0; i < modelo.getPlantillas().size(); i++) {
            this.vista.jcbPlantillas.addItem(modelo.getPlantillas().get(i));
        }
        this.vista.jcbPlantillas.setSelectedItem(modelo.getCabecera().getPlantillaImpresion());
        //SE AGREGA AQUI EL LISTENER PARA EVITAR LLAMADAS MIENTRAS SE CARGA EL COMBOBOX
        this.vista.jcbPlantillas.addActionListener(this);
    }

    private void agregarListeners() {
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jtfNroTimbrado.addKeyListener(this);
        this.vista.jtfNroSucursal.addKeyListener(this);
        this.vista.jtfPuntoVenta.addKeyListener(this);
        this.vista.jtfBoletaInicial.addKeyListener(this);
        this.vista.jtfBoletaFinal.addKeyListener(this);
        this.vista.jdcFechaVencimiento.addKeyListener(this);
    }

    private void jcbPlantillasHandler() {

        int opcion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea cambiar la plantilla de impresión?", "Atención", JOptionPane.INFORMATION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            int idImpresionPlantilla = vista.jcbPlantillas.getItemAt(vista.jcbPlantillas.getSelectedIndex()).getId();
            modelo.actualizarPlantillaImpresion(idImpresionPlantilla);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCancelar)) {
            cerrar();
        }
        if (source.equals(this.vista.jcbPlantillas)) {
            jcbPlantillasHandler();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
