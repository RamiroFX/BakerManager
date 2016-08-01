/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import DB_manager.DB_Proveedor;
import DB_manager.ResultSetTableModel;
import Entities.M_contacto;
import Entities.M_proveedor;
import Entities.M_sucursal;
import Entities.M_telefono;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro
 */
public class M_crear_proveedor {

    DefaultTableModel dtmSucursal, dtmTelefono, dtmContacto;
    M_proveedor proveedor;
    ArrayList<M_contacto> contactos;

    public M_crear_proveedor() {

        dtmContacto = new DefaultTableModel();
        dtmContacto.addColumn("Nombre");
        dtmContacto.addColumn("Apellido");
        dtmContacto.addColumn("Nro. telefono");
        dtmSucursal = new DefaultTableModel();
        dtmSucursal.addColumn("Direccion");
        dtmSucursal.addColumn("Telefono");
        dtmTelefono = new DefaultTableModel();
        dtmTelefono.addColumn("Telefono");
        dtmTelefono.addColumn("Tipo Telefono");
        dtmTelefono.addColumn("Observacion");
        contactos = new ArrayList<>();
    }

    public boolean insertarProveedor(M_proveedor proveedor, M_sucursal[] sucursal, M_telefono[] telefono) {
        M_proveedor prov = DB_Proveedor.obtenerDatosProveedor(proveedor.getEntidad());
        boolean b = DB_Proveedor.existeRuc(proveedor.getRuc());
        if (b) {
            JOptionPane.showMessageDialog(null, "El R.U.C. seleccionado se encuentra en uso", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (prov == null) {
            DB_Proveedor.insertarProveedor(proveedor, sucursal, telefono, contactos);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Proveedor existente", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ResultSetTableModel consultarProveedor(String proveedor, boolean entidad, boolean ruc, boolean isExclusivo) {
        return DB_Proveedor.consultarProveedor(proveedor, entidad, ruc, isExclusivo);
    }
}
