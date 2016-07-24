/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB_manager.DB_Cliente;
import DB_manager.DB_Ingreso;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_ver_ingreso implements ActionListener {

    public V_ver_ingreso vista;
    int idEgresoCabecera;

    public C_ver_ingreso(int idEgresoCabecera, V_ver_ingreso vista) {
        this.idEgresoCabecera = idEgresoCabecera;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void sumarTotal() {
        int cantFilas = this.vista.jtProductos.getRowCount();
        Integer totalExenta = 0, total5 = 0, total10 = 0, total = 0;
        for (int i = 0; i < cantFilas; i++) {
            Integer ivaExenta = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 5)));
            totalExenta = totalExenta + ivaExenta;
            Integer iva5 = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 6)));
            total5 = total5 + iva5;
            Integer iva10 = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 7)));
            total10 = total10 + iva10;
        }
        total = totalExenta + total5 + total10;
        this.vista.jftTotal.setValue(total);
    }

    private void inicializarVista() {
        M_facturaCabecera faca = DB_Ingreso.obtenerIngresoCabeceraID(idEgresoCabecera);
        M_cliente cliente = DB_Cliente.obtenerDatosClienteID(faca.getIdCliente());
        this.vista.jtfCliente.setText(cliente.getNombre() + " - " + cliente.getEntidad());
        this.vista.jtProductos.setModel(DB_Ingreso.obtenerIngresoDetalle(idEgresoCabecera));
        Utilities.c_packColumn.packColumns(this.vista.jtProductos, 1);
        sumarTotal();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
    }
}
