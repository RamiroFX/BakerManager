/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Cliente.Seleccionar_cliente;
import Empleado.Seleccionar_funcionario;
import Entities.M_cliente;
import Entities.M_funcionario;
import Interface.RecibirClienteCallback;
import Interface.RecibirEmpleadoCallback;
import Utilities.CellRenderers.NotaCreditoStatusCellRenderer;
import bakermanager.C_inicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_gestionRetencion implements ActionListener, MouseListener, KeyListener, RecibirEmpleadoCallback, RecibirClienteCallback {

    public M_gestionRetencion modelo;
    public V_gestionRetencion vista;
    public C_inicio c_inicio;
    private NotaCreditoStatusCellRenderer scr;

    public C_gestionRetencion(M_gestionRetencion modelo, V_gestionRetencion vista, C_inicio c_inicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.c_inicio = c_inicio;
        inicializarVista();
        concederPermisos();
    }

    public final void inicializarVista() {
        
    }

    public final void concederPermisos() {
        this.vista.jbNueva.addActionListener(this);
    }

    public final void mostrarVista() {
        this.vista.setVisible(true);
    }

    public final void cerrar() {
        this.vista.dispose();
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
    }

    private void borrarDatos() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbBuscar)) {
        }
        if (source.equals(this.vista.jbCliente)) {
            Seleccionar_cliente sc = new Seleccionar_cliente(this.c_inicio.vista);
            sc.setCallback(this);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbNueva)) {
            CrearRetencionVenta sc = new CrearRetencionVenta(this.c_inicio.vista);
            sc.mostrarVista();
        }
        if (source.equals(this.vista.jbEmpleado)) {
            Seleccionar_funcionario sf = new Seleccionar_funcionario(this.c_inicio.vista);
            sf.setCallback(this);
            sf.mostrarVista();
        }
        if (source.equals(this.vista.jbBorrar)) {
            borrarDatos();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F1: {
                break;
            }
            case KeyEvent.VK_F2: {
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
        if (this.vista.jtCabecera.hasFocus()) {
        }
    }
}
