/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import DB_manager.DB_Proveedor;
import DB_manager.DB_manager;
import DB_manager.ResultSetTableModel;
import Entities.M_contacto;
import Entities.M_proveedor;
import Entities.M_telefono;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_modificar_proveedor {

    DefaultTableModel dtmSucursal, dtmTelefono;
    M_proveedor proveedor;

    public M_modificar_proveedor(int idProveedor) {
        this.proveedor = DB_Proveedor.obtenerDatosProveedorID(idProveedor);

        dtmTelefono = new DefaultTableModel();
        dtmTelefono.addColumn("ID");
        dtmTelefono.addColumn("Telefono");
        dtmTelefono.addColumn("Tipo Telefono");
        dtmTelefono.addColumn("Observacion");
        TableModel tm = DB_Proveedor.obtenerProveedorTelefonoCompleto(idProveedor);
        int cantFila = tm.getRowCount();
        int cantCol = tm.getColumnCount();
        for (int i = 0; i < cantFila; i++) {
            Object[] o = new Object[cantCol];
            for (int x = 0; x < cantCol; x++) {
                o[x] = tm.getValueAt(i, x);
            }
            dtmTelefono.addRow(o);
        }
        dtmSucursal = new DefaultTableModel();
        dtmSucursal.addColumn("ID");
        dtmSucursal.addColumn("Direccion");
        dtmSucursal.addColumn("Telefono");
        tm = DB_Proveedor.obtenerSucursal(idProveedor);
        cantFila = tm.getRowCount();
        cantCol = tm.getColumnCount();
        for (int i = 0; i < cantFila; i++) {
            Object[] o = new Object[cantCol];
            for (int x = 0; x < cantCol; x++) {
                o[x] = tm.getValueAt(i, x);
            }
            dtmSucursal.addRow(o);
        }

    }

    public boolean modificarProveedor(M_proveedor proveedorModificado) {
        String entidadActual = this.proveedor.getEntidad();
        String entidadNueva = proveedorModificado.getEntidad();
        String rucActual = this.proveedor.getRuc();
        String rucNuevo = proveedorModificado.getRuc();
        if (null == this.proveedor.getRuc()) {
            rucActual = "";
        }
        if (null == proveedorModificado.getRuc()) {
            rucNuevo = "";
        }
        if (!entidadActual.equalsIgnoreCase(entidadNueva)) {
            M_proveedor prov = DB_Proveedor.obtenerDatosProveedor(entidadNueva);
            if (prov != null) {
                JOptionPane.showMessageDialog(null, "La razón social ya existe", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (!rucActual.equalsIgnoreCase(rucNuevo)) {
            boolean b = DB_Proveedor.existeRuc(rucNuevo);
            if (b) {
                JOptionPane.showMessageDialog(null, "El R.U.C. seleccionado se encuentra en uso", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        proveedorModificado.setId(this.proveedor.getId());
        DB_Proveedor.modificarProveedor(proveedorModificado);
        return true;
    }

    public ResultSetTableModel obtenerProveedorContacto() {
        return DB_Proveedor.obtenerProveedorContacto(this.proveedor.getId());
    }

    public void insertarTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        boolean b = DB_manager.existeTelefono(nroTelefono);
        if (b) {
            JOptionPane.showMessageDialog(null, "El telefono se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            DB_Proveedor.insertarTelefono(proveedor.getId(), tipoTelefono, nroTelefono, observacion);
        }
    }

    public ResultSetTableModel obtenerProveedorTelefonoCompleto() {
        return DB_Proveedor.obtenerProveedorTelefonoCompleto(proveedor.getId());
    }

    public void modificarTelefono(int id_telefono, String tipoTelefono, String nroTelefono, String observacion) {
        boolean b = DB_manager.existeTelefono(nroTelefono);
        if (b) {
            ArrayList<M_telefono> telefonos = DB_Proveedor.obtenerTelefonos(proveedor.getId());
            for (M_telefono telefono : telefonos) {
                if (telefono.getNumero().equals(nroTelefono)) {
                    JOptionPane.showMessageDialog(null, "El telefono no ha cambiado.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "El telefono se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            int opt = JOptionPane.showConfirmDialog(null, "¿Desea confirmar esta operación?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                try {
                    DB_Proveedor.modificarTelefono(id_telefono, tipoTelefono, nroTelefono, observacion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void eliminarTelefonoProveedor(int id_telefono) {
        DB_Proveedor.eliminarTelefonoProveedor(id_telefono);
    }

    public void insertarSucursal(String direccion, String telefono) {
        DB_Proveedor.insertarSucursal(proveedor.getId(), direccion, telefono);
    }

    public ResultSetTableModel obtenerSucursal() {
        return DB_Proveedor.obtenerSucursal(proveedor.getId());
    }

    public void modificarSucursal(int id_sucursal, String direccion, String telefono) {
        DB_Proveedor.modificarSucursal(id_sucursal, direccion, telefono);
    }

    public void eliminarSucursal(int id_sucursal) {
        DB_Proveedor.eliminarSucursal(id_sucursal);
    }

    public void insertarProveedorContacto(M_contacto contacto) {
        DB_Proveedor.insertarProveedorContacto(proveedor.getId(), contacto);
    }

    public void modificarProveedorContacto(M_contacto contacto) {
        DB_Proveedor.modificarProveedorContacto(contacto);
    }

    public M_contacto obtenerDatosContactoIdContacto(int idContacto) {
        return DB_Proveedor.obtenerDatosContactoIdContacto(idContacto);
    }

    public void eliminarProveedorContacto(M_contacto contacto_temp) {
        DB_Proveedor.eliminarProveedorContacto(contacto_temp);
    }

    public ResultSetTableModel consultarProveedor(String entidad, boolean b, boolean b0, boolean b1) {
        return DB_Proveedor.consultarProveedor(entidad, b1, b1, b1);
    }
}
