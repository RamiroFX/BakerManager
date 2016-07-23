/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import DB_manager.DB_rol;
import DB_manager.ResultSetTableModel;
import Entities.M_rol;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro
 */
public class M_modificar_rol {

    private M_rol rol;

    public M_modificar_rol(int idRol) {
        this.rol = DB_rol.obtenerRol(idRol);
    }

    /**
     * @return the rol
     */
    public M_rol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(M_rol rol) {
        this.rol = rol;
    }

    public boolean modificarRol(String nombreRol) {
        //maximo 30 caracteres
        if (nombreRol.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo nombre rol esta vacío", "Atención", JOptionPane.ERROR_MESSAGE);

        } else {
            if (nombreRol.length() > 30) {
                JOptionPane.showMessageDialog(null, "El campo nombre rol puede contener hasta 30 caracteres", "Atención", JOptionPane.ERROR_MESSAGE);
            } else {
                if (DB_rol.existeRol(getRol().getId(), nombreRol)) {
                    javax.swing.JOptionPane.showMessageDialog(null, "El nombre del Rol se encuentra en uso. Verifique el nombre del Rol", "Parametros incorrectos",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {
                    TableModel tabla = obtenerAccesoSelecc();
                    int cantFilas = tabla.getRowCount();
                    //preguntamos si está vacío
                    if (cantFilas < 1) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Seleccione al menos un acceso", "Parametros incorrectos",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else {
                        DB_rol.modificarNombreRol(this.rol.getId(), nombreRol);
                        javax.swing.JOptionPane.showMessageDialog(null, "Rol: " + this.rol.getDescripcion() + " modificado a: " + nombreRol, "Éxito",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ResultSetTableModel obtenerAccesosDispon() {
        return DB_rol.obtenerAccesosDisponibles();
    }

    public boolean agregarAcceso(int idMenu, int idItem) {
        TableModel tabla = obtenerAccesoSelecc();
        int cantFilas = tabla.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            int id = Integer.valueOf(String.valueOf(tabla.getValueAt(i, 2)));
            if (id == idItem) {
                JOptionPane.showMessageDialog(null, "El acceso seleccionado ya se ha encuentra", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        int i = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea agregar el acceso seleccionado?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.YES_OPTION) {
            DB_rol.agregarAccesoRol(this.rol.getId(), idMenu, idItem);
            return true;
        }
        return false;
    }

    public void quitarAcceso(int idMenu, int idItem) {
        int i = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea quitar el acceso seleccionado?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.YES_OPTION) {
            DB_rol.quitarAccesoRol(this.rol.getId(), idMenu, idItem);
        }
    }

    public ResultSetTableModel obtenerAccesoSelecc() {
        return DB_rol.consultarPermisos(this.rol.getId());
    }
}
