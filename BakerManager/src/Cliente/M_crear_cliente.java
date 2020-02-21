/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB.DB_Cliente;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_cliente_contacto;
import Entities.M_sucursal;
import Entities.M_telefono;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro
 */
class M_crear_cliente {

    public DefaultTableModel dtmSucursal, dtmTelefono, dtmContacto;
    M_cliente cliente;
    ArrayList<M_cliente_contacto> contactos;

    public M_crear_cliente() {

        dtmContacto = new DefaultTableModel();
        dtmContacto.addColumn("Nombre");
        dtmContacto.addColumn("Apellido");
        dtmContacto.addColumn("Nro. telefono");

        dtmSucursal = new DefaultTableModel();
        dtmSucursal.addColumn("Direccion");
        dtmSucursal.addColumn("Telefono");

        dtmSucursal = new DefaultTableModel();
        dtmSucursal.addColumn("Direccion");
        dtmSucursal.addColumn("Telefono");

        dtmTelefono = new DefaultTableModel();
        dtmTelefono.addColumn("Telefono");
        dtmTelefono.addColumn("Tipo Telefono");
        dtmTelefono.addColumn("Observacion");

        contactos = new ArrayList<>();
    }

    public Vector obtenerTipoCliente() {
        return DB_Cliente.obtenerTipoCliente();
    }

    public Vector obtenerCategoriaCliente() {
        return DB_Cliente.obtenerCategoriaCliente();
    }

    public boolean insertarCliente(M_cliente cliente, M_telefono[] telefono, M_sucursal[] sucursal) {
        //M_cliente prov = DB_Cliente.obtenerDatosCliente(cliente.getEntidad());
        boolean b = DB_Cliente.existeRuc(cliente.getRuc());
        if (b) {
            JOptionPane.showMessageDialog(null, "El R.U.C. seleccionado se encuentra en uso", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        DB_Cliente.insertarCliente(cliente, telefono, sucursal, contactos);
        /*if (prov == null) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Cliente existente", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }*/
        return true;
    }

    public ResultSetTableModel consultarCliente(String string, boolean b, boolean b0, boolean b1) {
        return DB_Cliente.consultarCliente("", false, true, true);
    }

    boolean existeTelefono(String nroTelefono) {
        return DB_manager.existeTelefono(nroTelefono);
    }

}
