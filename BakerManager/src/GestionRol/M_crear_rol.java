/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import DB_manager.DB_rol;
import DB_manager.ResultSetTableModel;
import Entities.M_menu_item;
import Entities.M_rol;
import Utils.ArrayListTableModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro
 */
public class M_crear_rol {

    private M_rol rol;
    private ArrayList<M_menu_item> accesos;

    public M_crear_rol() {
        this.rol = new M_rol();
        this.accesos = new ArrayList<>();
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

    public boolean crearRol(String nombreRol) {
        //maximo 30 caracteres
        if (nombreRol.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo nombre rol esta vacío", "Atención", JOptionPane.ERROR_MESSAGE);

        } else {
            if (nombreRol.length() > 30) {
                JOptionPane.showMessageDialog(null, "El campo nombre rol puede contener hasta 30 caracteres", "Atención", JOptionPane.ERROR_MESSAGE);
            } else {
                if (DB_rol.existeRolPrimeraVez(nombreRol)) {
                    javax.swing.JOptionPane.showMessageDialog(null, "El nombre del Rol se encuentra en uso. Verifique el nombre del Rol", "Parametros incorrectos",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {
                    if (accesos.isEmpty()) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Seleccione al menos un acceso", "Parametros incorrectos",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else {
                        DB_rol.crearRol(nombreRol, accesos);
                        javax.swing.JOptionPane.showMessageDialog(null, "Nuevo rol creado: " + nombreRol, "Éxito",
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

    public void agregarAcceso(int idMenu, String menuDescripcion, int idItem, String itemDescripcion) {
        for (int i = 0; i < accesos.size(); i++) {
            if (accesos.get(i).getItemId() == idItem) {
                JOptionPane.showMessageDialog(null, "El acceso seleccionado ya se ha encuentra", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        M_menu_item menu_item = new M_menu_item(idMenu, menuDescripcion, idItem, itemDescripcion);
        accesos.add(menu_item);
    }

    public void quitarAcceso(int idItem) {
        for (int i = 0; i < accesos.size(); i++) {
            if (accesos.get(i).getItemId() == idItem) {
                accesos.remove(i);
                return;
            }
        }
    }

    public TableModel obtenerAccesoSelecc() {
        ArrayListTableModel model = new ArrayListTableModel();
        model.addColumn("Id");
        model.addColumn("Menu");
        model.addColumn("Id");
        model.addColumn("Item");
        for (int i = 0; i < accesos.size(); i++) {
            Object[] fila = {accesos.get(i).getIdMenu(),
                accesos.get(i).getMenuDescripcion(),
                accesos.get(i).getItemId(),
                accesos.get(i).getItemDescripcion()};
            model.addRow(fila);
        }
        return model;
    }
}
