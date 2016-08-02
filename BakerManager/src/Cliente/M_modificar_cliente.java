/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import DB_manager.DB_Cliente;
import DB_manager.DB_manager;
import DB_manager.ResultSetTableModel;
import Entities.M_cliente;
import Entities.M_cliente_contacto;
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
class M_modificar_cliente {

    public DefaultTableModel dtmSucursal, dtmTelefono, dtmContacto;
    M_cliente cliente;
    ArrayList<M_cliente_contacto> contactos;

    public M_modificar_cliente(int idCliente) {
        this.cliente = DB_Cliente.obtenerDatosClienteID(idCliente);
        contactos = new ArrayList<>();
        dtmTelefono = new DefaultTableModel();
        dtmTelefono.addColumn("ID");
        dtmTelefono.addColumn("Telefono");
        dtmTelefono.addColumn("Tipo Telefono");
        dtmTelefono.addColumn("Observacion");
        TableModel tm = DB_Cliente.obtenerClienteTelefonoCompleto(idCliente);
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
        tm = DB_Cliente.obtenerSucursal(idCliente);
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

    public Vector obtenerTipoCliente() {
        return DB_Cliente.obtenerTipoCliente();
    }

    public Vector obtenerCategoriaCliente() {
        return DB_Cliente.obtenerCategoriaCliente();
    }

    public ResultSetTableModel obtenerClienteTelefono() {
        return DB_Cliente.obtenerClienteTelefono(cliente.getIdCliente());
    }

    public ResultSetTableModel obtenerSucursal() {
        return DB_Cliente.obtenerSucursal(cliente.getIdCliente());
    }

    public ResultSetTableModel obtenerClienteContacto() {
        return DB_Cliente.obtenerClienteContacto(cliente.getIdCliente());
    }

    public void insertarContacto(M_cliente_contacto contacto) {
        DB_Cliente.insertarContacto(cliente.getIdCliente(), contacto);
    }

    void eliminarContacto(M_cliente_contacto contacto) {
        DB_Cliente.eliminarContacto(contacto.getId_persona(), contacto.getIdClienteContacto(), contacto.getIdCliente());
    }

    public boolean actualizarCliente(M_cliente clienteModificado) {
        M_cliente clie = DB_Cliente.obtenerDatosCliente(clienteModificado.getEntidad());
        int idActual = this.cliente.getIdCliente();
        int idNuevo = -1;
        String rucActual = this.cliente.getRuc();
        String rucNuevo = clienteModificado.getRuc();
        if (clie != null) {
            idNuevo = clie.getIdCliente();
        }
        if (idActual == idNuevo) {
            //Es el mismo cliente. La entidad(unico) es igual. Verificar R.U.C.
            if (rucActual == null ? rucNuevo == null : rucActual.equals(rucNuevo)) {
                int idCategoria = DB_Cliente.obtenerIdCategoria(clienteModificado.getCategoria());
                int idTipo = DB_Cliente.obtenerIdTipo(clienteModificado.getTipo());
                clienteModificado.setIdCliente(this.cliente.getIdCliente());
                clienteModificado.setIdCategoria(idCategoria);
                clienteModificado.setIdTipo(idTipo);
                DB_Cliente.actualizarCliente(clienteModificado);
                return true;
            } else if (DB_Cliente.existeRuc(rucNuevo)) {
                JOptionPane.showMessageDialog(null, "El R.U.C. seleccionado se encuentra en uso", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                int idCategoria = DB_Cliente.obtenerIdCategoria(clienteModificado.getCategoria());
                int idTipo = DB_Cliente.obtenerIdTipo(clienteModificado.getTipo());
                clienteModificado.setIdCliente(this.cliente.getIdCliente());
                clienteModificado.setIdCategoria(idCategoria);
                clienteModificado.setIdTipo(idTipo);
                DB_Cliente.actualizarCliente(clienteModificado);
                return true;
            }
        } else if (rucActual == null ? rucNuevo == null : rucActual.equals(rucNuevo)) {
            int idCategoria = DB_Cliente.obtenerIdCategoria(clienteModificado.getCategoria());
            int idTipo = DB_Cliente.obtenerIdTipo(clienteModificado.getTipo());
            clienteModificado.setIdCliente(this.cliente.getIdCliente());
            clienteModificado.setIdCategoria(idCategoria);
            clienteModificado.setIdTipo(idTipo);
            DB_Cliente.actualizarCliente(clienteModificado);
            return true;
        } else if (DB_Cliente.existeRuc(clienteModificado.getRuc())) {
            JOptionPane.showMessageDialog(null, "El R.U.C. seleccionado se encuentra en uso", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            int idCategoria = DB_Cliente.obtenerIdCategoria(clienteModificado.getCategoria());
            int idTipo = DB_Cliente.obtenerIdTipo(clienteModificado.getTipo());
            clienteModificado.setIdCliente(this.cliente.getIdCliente());
            clienteModificado.setIdCategoria(idCategoria);
            clienteModificado.setIdTipo(idTipo);
            DB_Cliente.actualizarCliente(clienteModificado);
            return true;
        }
    }

    public ResultSetTableModel consultarCliente(String string, boolean b, boolean b0, boolean b1) {
        return DB_Cliente.consultarCliente("", false, true, true);
    }

    public M_cliente_contacto obtenerDatosClienteContactoID(int idContacto) {
        return DB_Cliente.obtenerDatosClienteContactoID(idContacto);
    }

    public void modificarClienteContacto(M_cliente_contacto contacto) {
        DB_Cliente.modificarContacto(contacto);
    }

    public void insertarSucursal(String direccion, String telefono) {
        DB_Cliente.insertarSucursal(this.cliente.getIdCliente(), direccion, telefono);
    }

    public void modificarSucursal(int id_sucursal, String direccion, String telefono) {
        DB_Cliente.modificarSucursal(id_sucursal, direccion, telefono);
    }

    public void eliminarSucursal(int id_sucursal) {
        DB_Cliente.eliminarSucursal(id_sucursal);
    }

    public void insertarTelefono(String tipoTelefono, String nroTelefono, String observacion) {
        boolean b = DB_manager.existeTelefono(nroTelefono);
        if (b) {
            JOptionPane.showMessageDialog(null, "El telefono se encuentra en uso.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            DB_Cliente.insertarTelefono(this.cliente.getIdCliente(), tipoTelefono, nroTelefono, observacion);
        }
    }

    public ResultSetTableModel obtenerProveedorTelefonoCompleto() {
        return DB_Cliente.obtenerClienteTelefonoCompleto(this.cliente.getIdCliente());
    }

    public void modificarTelefono(int id_telefono, String tipoTelefono, String nroTelefono, String observacion) {
        boolean b = DB_manager.existeTelefono(nroTelefono);
        if (b) {
            ArrayList<M_telefono> telefonos = DB_Cliente.obtenerTelefonos(this.cliente.getIdCliente());
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
                    DB_Cliente.modificarTelefono(id_telefono, tipoTelefono, nroTelefono, observacion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void eliminarTelefonoProveedor(int id_telefono) {
        DB_Cliente.eliminarTelefono(id_telefono);
    }
}
