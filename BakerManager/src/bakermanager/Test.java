/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import DB.DB_manager;
import Parametros.PedidoEstado;
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
        for (PedidoEstado pedidoest : PedidoEstado.values()) {
            System.out.println(pedidoest+": "+pedidoest.getId()+" - "+pedidoest.getDescripcion());
        }
    }
}
