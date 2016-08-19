/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import DB.DB_rol;
import DB.ResultSetTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class M_gestion_rol {

    public void eliminarRol(int idRol) {
        if (DB_rol.rolEnUso(idRol)) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar el rol porque está en uso. Quite el rol a cada usuario para poder eliminar.", "Atención", JOptionPane.ERROR_MESSAGE);
        } else {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el rol seleccionado? Ésta acción no puede ser revertida.", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                DB_rol.eliminarRol(idRol);
            }
        }
    }

    public ResultSetTableModel consultarPermisos(int idRol) {
        return DB_rol.consultarPermisos(idRol);
    }

    public ResultSetTableModel consultarRoles(String descripcion) {
        return DB_rol.consultarRoles(descripcion);
    }
}
