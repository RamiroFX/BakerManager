/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.Caja;
import Entities.M_producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Caja {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static long insertarCaja(Caja caja) {
        long id_caja = -1L;

        String INSERT_CAJA = "INSERT INTO CAJA"
                + "(ID_FUNCIONARIO_APERTURA, ID_FUNCIONARIO_CIERRE, MONTO_INICIAL, "
                + "MONTO_FINAL, INGRESO_CONTADO, INGRESO_CREDITO, EGRESO_CONTADO, "
                + "EGRESO_CREDITO, TIEMPO_APERTURA, TIEMPO_CIERRE)"
                + "VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CAJA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, caja.getIdEmpleadoApertura());
            pst.setInt(2, caja.getIdEmpleadoCierre());
            pst.setInt(3, caja.getMontoInicial());
            pst.setInt(4, caja.getMontoFinal());
            pst.setInt(5, caja.getIngresoContado());
            pst.setInt(6, caja.getIngresoCredito());
            pst.setInt(7, caja.getEgresoContado());
            pst.setInt(8, caja.getEgresoCredito());
            pst.setTimestamp(9, new Timestamp(caja.getTiempoApertura().getTime()));
            pst.setTimestamp(10, new Timestamp(caja.getTiempoCierre().getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_caja = rs.getLong(1);
            }
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return id_caja;
    }
}
