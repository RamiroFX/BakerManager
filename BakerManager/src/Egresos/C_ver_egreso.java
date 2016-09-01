/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Proveedor;
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
        int cantFilas = this.vista.jtEgresoDetalle.getRowCount();
        Integer totalExenta = 0, total5 = 0, total10 = 0, total = 0;
        for (int i = 0; i < cantFilas; i++) {
            Integer ivaExenta = Integer.valueOf(String.valueOf(this.vista.jtEgresoDetalle.getValueAt(i, 5)));
            totalExenta = totalExenta + ivaExenta;
            Integer iva5 = Integer.valueOf(String.valueOf(this.vista.jtEgresoDetalle.getValueAt(i, 6)));
            total5 = total5 + iva5;
            Integer iva10 = Integer.valueOf(String.valueOf(this.vista.jtEgresoDetalle.getValueAt(i, 7)));
            total10 = total10 + iva10;
        }
        total = totalExenta + total5 + total10;
        this.vista.jftTotal.setValue(total);
        this.vista.jftExenta.setValue(totalExenta);
        this.vista.jftIva5.setValue(total5);
        this.vista.jftIva10.setValue(total10);
    }

    private void initComp() {
        this.modelo.egreso_cabecera = DB_Egreso.obtenerEgresoCabeceraID(idEgresoCabecera);
        this.modelo.proveedor = DB_Proveedor.obtenerDatosProveedorID(this.modelo.egreso_cabecera.getId_proveedor());
        this.modelo.empleado = DB_Funcionario.obtenerDatosFuncionarioID(modelo.egreso_cabecera.getId_empleado());
        this.vista.jtfProveedor.setText(this.modelo.proveedor.getNombre());
        this.vista.jtfNroFactura.setText(this.modelo.egreso_cabecera.getNro_factura().toString());
        this.vista.jtfFuncionario.setText(this.modelo.empleado.getNombre());
        this.vista.jtfProvDireccion.setText(this.modelo.proveedor.getDireccion());
        this.vista.jtfProvRuc.setText(this.modelo.proveedor.getRuc() + "-" + this.modelo.proveedor.getRuc_id());
        this.vista.jtEgresoDetalle.setModel(DB_Egreso.obtenerEgresoDetalle(idEgresoCabecera));
        switch (this.modelo.egreso_cabecera.getId_condVenta()) {
            case (Parametros.TipoOperacion.CONTADO): {
                this.vista.jrbContado.setSelected(true);
                break;
            }
            case (Parametros.TipoOperacion.CREDITO): {
                this.vista.jrbCredito.setSelected(true);
                break;
            }
        }
        Utilities.c_packColumn.packColumns(this.vista.jtEgresoDetalle, 1);
        sumarTotal();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
    }
}
