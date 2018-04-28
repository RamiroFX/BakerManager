/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_menu_item {

    private int idItem, idMenu;
    private String itemDescripcion, menuDescripcion;

    public M_menu_item() {
    }

    
    public M_menu_item(int idMenu, String menuDescripcion, int idItem, String itemDescripcion) {
        this.idMenu = idMenu;
        this.menuDescripcion = menuDescripcion;
        this.idItem = idItem;
        this.itemDescripcion = itemDescripcion;
    }

    /**
     * @return the id
     */
    public int getItemId() {
        return idItem;
    }

    /**
     * @param id the id to set
     */
    public void setItemId(int id) {
        this.idItem = id;
    }

    /**
     * @return the descripcion
     */
    public String getItemDescripcion() {
        return itemDescripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setItemDescripcion(String descripcion) {
        this.itemDescripcion = descripcion;
    }

    /**
     * @return the idMenu
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * @param idMenu the idMenu to set
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    /**
     * @return the menuDescripcion
     */
    public String getMenuDescripcion() {
        return menuDescripcion;
    }

    /**
     * @param menuDescripcion the menuDescripcion to set
     */
    public void setMenuDescripcion(String menuDescripcion) {
        this.menuDescripcion = menuDescripcion;
    }

    @Override
    public String toString() {
        return "M_menu_item{" + "idItem=" + idItem + ", idMenu=" + idMenu + ", itemDescripcion=" + itemDescripcion + ", menuDescripcion=" + menuDescripcion + '}';
    }
    
    
}
