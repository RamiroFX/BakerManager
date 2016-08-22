/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import DB.DB_manager;
import Utilities.Impresora;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class Test {

    public static void main(String[] args) {
        try {
            //ServicioDeImpresion impresion = new ServicioDeImpresion();
            //1552 vico c
            DB_manager.conectarBD("postgres", "postgres");
        } catch (SQLException ex) {
            Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
        }

        String categoria = "Panadería".toLowerCase();
        System.out.println("categoria: " + categoria);
        int idCategoria = DB_manager.obtenerIdProductoCategoria(categoria);
        System.out.println("id: " + idCategoria);
    }
}
