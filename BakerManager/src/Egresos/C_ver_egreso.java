/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB_manager.DB_Egreso;
import DB_manager.DB_Proveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ramiro Ferreira
 */
class C_ver_egreso implements ActionListener {

    public V_Ver_Egresos vista;
    M_Egresos modelo;
    int idEgresoCabecera;

    public C_ver_egreso(V_Ver_Egresos vista, M_Egresos modelo) {
        this.vista = vista;
        this.modelo = modelo;
        initComp();
        agregarListeners();
    }

    public C_ver_egreso(int idEgresoCabecera, V_Ver_Egresos vista, M_Egresos modelo) {
        this.idEgresoCabecera = idEgresoCabecera;
        this.vista = vista;
        this.modelo = modelo;
        initComp();
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

    private void initComp() {
        this.modelo.egreso_cabecera = DB_Egreso.obtenerEgresoCabeceraID(idEgresoCabecera);
        this.modelo.proveedor = DB_Proveedor.obtenerDatosProveedorID(this.modelo.egreso_cabecera.getId_proveedor());
        this.vista.jtfProveedor.setText(this.modelo.proveedor.getNombre());
        this.vista.jtProductos.setModel(DB_Egreso.obtenerEgresoDetalle(idEgresoCabecera));
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
