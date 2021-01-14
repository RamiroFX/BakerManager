/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Cliente.SeleccionarCliente;
import Entities.M_cliente;
import Interface.RecibirClienteCallback;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirEmpleadoCallback;
import bakermanager.C_inicio;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_relacionarAnticipo extends MouseAdapter implements ActionListener, KeyListener,
        RecibirClienteCallback {
    
    M_relacionarAnticipo modelo;
    V_relacionarAnticipo vista;
    private C_inicio inicio;

    public C_relacionarAnticipo(M_relacionarAnticipo modelo, V_relacionarAnticipo vista, C_inicio inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.inicio = inicio;
        inicializarVista();
        agregarListeners();
    }
    
    public void mostrarVista(){
        this.vista.setVisible(true);
    }

    private void cerrar() {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                if (modelo.getCtaCteDetalleTm().getList().isEmpty()) {
//                    vista.dispose();
//                } else {
//                    int opcion = JOptionPane.showConfirmDialog(vista, CONFIRMAR_SALIR_MSG, VALIDAR_TITULO, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (opcion == JOptionPane.YES_OPTION) {
//                        vista.dispose();
//                    }
//                }
//            }
//        });
    }

    private void inicializarVista() {
//        this.vista.jtReciboDetalle.setModel(modelo.getCtaCteDetalleTm());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
    }

    private void agregarListeners() {
        //ACTION LISTENERS
        this.vista.jbCliente.addActionListener(this);
        this.vista.jtfNroRecibo.addActionListener(this);
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        //MOUSE LISTENERS
        this.vista.jtReciboDetalle.addMouseListener(this);
        //KEY LISTENERS
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfNroRecibo.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jbEliminarDetalle.addKeyListener(this);
        this.vista.jbModificarDetalle.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }
    
    private boolean validarDetalleReciboVacio() {
//        if (!this.modelo.getCtaCteDetalleTm().getList().isEmpty()) {
//            JOptionPane.showMessageDialog(vista, VALIDAR_DETALLE_RECIBO, VALIDAR_TITULO, JOptionPane.WARNING_MESSAGE);
//            return false;
//        }
        return true;
    }
    private void invocarVistaSeleccionCliente() {
        if (!validarDetalleReciboVacio()) {
            return;
        }
        SeleccionarCliente sc = new SeleccionarCliente(inicio.vista);
        sc.setCallback(this);
        sc.mostrarVista();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        Object source = e.getSource();
        if (source.equals(this.vista.jbAceptar)) {
            //guardar();
        } else if (source.equals(this.vista.jbAgregarFactura)) {
            //invocarVistaSeleccionFacturaPendiente();
        } else if (source.equals(this.vista.jbCliente)) {
            invocarVistaSeleccionCliente();
        } else if (source.equals(this.vista.jbEliminarDetalle)) {
            //eliminarDetalle();
        } else if (source.equals(this.vista.jbModificarDetalle)) {
            //modificarDetalle();
        } else if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        //modelo.getCabecera().setCliente(cliente);
        this.vista.jtfCliente.setText(cliente.getEntidad() + "(" + cliente.getRuc() + "-" + cliente.getRucId() + ")");
    }
}
