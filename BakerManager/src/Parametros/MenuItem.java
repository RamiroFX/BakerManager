/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parametros;

/**
 *
 * @author Ramiro Ferreira
 */
public enum MenuItem {

    CREAR_EMPLEADO(1, "crear empleado"),
    BUSCAR_EMPLEADO(1, "buscar empleado"),
    MODIFICAR_EMPLEADO(1, "modificar empleado"),
    MODIFICAR_CONTRASEÑA(1, "modificar contrasenha"),
    BORRAR_EMPLEADO(1, "borrar empleado"),
    ROLES_EMPLEADO(1, "roles"),
    CANCELADO(1, "modificar empleado"),
    CREAR_PRODUCTO(2, "crear producto"),
    BUSCAR_PRODUCTO(2, "buscar producto"),
    MODIFICAR_PRODUCTO(2, "modificar producto"),
    BORRAR_PRODUCTO(2, "borrar producto"),
    PARAMETROS_PRODUCTO(2, "parametros producto");
    private int id;
    private String descripcion;

    private MenuItem(int idGrupo, String descripcion) {
        this.id = idGrupo;
        this.descripcion = descripcion;
    }

    public int getIdGrupo() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
