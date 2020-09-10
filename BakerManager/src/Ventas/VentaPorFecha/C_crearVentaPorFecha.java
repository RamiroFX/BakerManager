/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.VentaPorFecha;

import Ventas.C_crearVentaRapida;
import Ventas.M_crearVentaRapida;
import java.awt.EventQueue;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearVentaPorFecha extends C_crearVentaRapida {

    V_crearVentaPorFecha gui;

    public C_crearVentaPorFecha(M_crearVentaRapida modelo, V_crearVentaPorFecha vista) {
        super(modelo, vista);
        this.gui = vista;
        inicializarVista2();
    }

    public final void inicializarVista2() {
        Calendar calendar = Calendar.getInstance();
        gui.jdcFecha.setDate(calendar.getTime());
    }

    @Override
    protected void guardarVenta() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timestamp tiempo = new Timestamp(gui.jdcFecha.getDate().getTime());
                modelo.getCabecera().setTiempo(tiempo);
                modelo.guardarVentaConFecha();
                int opcion = JOptionPane.showConfirmDialog(vista, IMPRIMIR_VENTA, ATENCION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    modelo.imprimirVenta();
                }
                modelo.limpiarCampos();
                vista.jtFacturaDetalle.setModel(modelo.getTableModel());
                vista.jcbTipoVenta.setSelectedIndex(0);
                recibirCliente(modelo.getCabecera().getCliente());
                establecerNroFactura();
                establecerCondicionVenta();
                establecerTipoVenta();
                sumarTotal();
                vista.jtfCodProd.setText("");
            }
        });
    }
}
