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

    public static ResultSetTableModel consultarCajas(Integer idFuncionario, String fechaInicio, String fechaFin) {
        String Query = "SELECT ID_CAJA \"ID\", (SELECT NOMBRE ||' '|| APELLIDO \"Func. Apertura\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA),\n"
                + "	(SELECT NOMBRE ||' '|| APELLIDO \"Func. Cierre\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA), \n"
                + "	MONTO_INICIAL \"Monto inicial\", 	MONTO_FINAL \"Monto final\", INGRESO_CONTADO \"Ingreso contado\", INGRESO_CREDITO \"Ingreso crédito\", EGRESO_CONTADO \"Egreso contado\", \n"
                + "	EGRESO_CREDITO \"Egreso crédito\", TIEMPO_APERTURA \"Tiempo apertura\", TIEMPO_CIERRE \"Tiempo cierre\"\n"
                + "  FROM CAJA, FUNCIONARIO , PERSONA\n"
                + "  WHERE CAJA.ID_FUNCIONARIO_APERTURA = FUNCIONARIO.ID_FUNCIONARIO\n"
                + "  AND  CAJA.ID_FUNCIONARIO_CIERRE = FUNCIONARIO.ID_FUNCIONARIO\n"
                + "  AND FUNCIONARIO.ID_PERSONA = PERSONA.ID_PERSONA"
                + "  AND CAJA.TIEMPO_CIERRE BETWEEN '" + fechaInicio + " 00:00:00.00'::timestamp  AND '" + fechaFin + " 23:59:59.00'::timestamp  ";
        String func = "AND CAJA.ID_FUNCIONARIO_CIERRE = " + idFuncionario;
        if (idFuncionario > -1) {
            Query = Query + func;
        }
        ResultSetTableModel rstm = null;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static Integer ultimoFondo() {
        int ultimoFondo = 0;
        String QUERY = "SELECT MONTO_FINAL FROM CAJA ORDER BY ID_CAJA DESC LIMIT 1";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                ultimoFondo = rs.getInt("MONTO_FINAL");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Caja.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Caja.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return ultimoFondo;
    }
}