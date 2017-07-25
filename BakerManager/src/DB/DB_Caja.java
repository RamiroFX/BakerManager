/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Entities.Moneda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static long insertarArqueoCaja(Caja caja,
            ArrayList<ArqueoCajaDetalle> arqueoInicio,
            ArrayList<ArqueoCajaDetalle> arqueoFin,
            ArrayList<ArqueoCajaDetalle> arqueoDeposito) {
        String INSERT_ARQUEO = "INSERT INTO ARQUEO_CAJA( ID_CAJA, "
                + "ID_MONEDA, CANTIDAD, ID_ARQUEO_CAJA_TIPO)VALUES (?, ?, ?, ?)";
        String INSERT_CAJA = "INSERT INTO CAJA"
                + "(ID_FUNCIONARIO_APERTURA, ID_FUNCIONARIO_CIERRE, MONTO_INICIAL, "
                + "MONTO_FINAL, INGRESO_CONTADO, INGRESO_CREDITO, EGRESO_CONTADO, "
                + "EGRESO_CREDITO, TIEMPO_APERTURA, TIEMPO_CIERRE)"
                + "VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        long sq_cabecera = -1L;
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
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < arqueoInicio.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_ARQUEO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, arqueoInicio.get(i).getMoneda().getIdMoneda());
                pst.setInt(3, arqueoInicio.get(i).getCantidad());
                pst.setInt(4, arqueoInicio.get(i).getIdTipo());
                pst.executeUpdate();
                pst.close();
            }
            for (int i = 0; i < arqueoFin.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_ARQUEO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, arqueoFin.get(i).getMoneda().getIdMoneda());
                pst.setInt(3, arqueoFin.get(i).getCantidad());
                pst.setInt(4, arqueoFin.get(i).getIdTipo());
                pst.executeUpdate();
                pst.close();
            }
            for (int i = 0; i < arqueoDeposito.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_ARQUEO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, arqueoDeposito.get(i).getMoneda().getIdMoneda());
                pst.setInt(3, arqueoDeposito.get(i).getCantidad());
                pst.setInt(4, arqueoDeposito.get(i).getIdTipo());
                pst.executeUpdate();
                pst.close();
            }
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
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

    public static ArrayList<Moneda> obtenerMonedas() {
        ArrayList<Moneda> monedas = null;
        String q = "SELECT ID_MONEDA, VALOR, DESCRIPCION FROM MONEDA";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            monedas = new ArrayList();
            while (rs.next()) {
                Moneda moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setValor(rs.getInt("VALOR"));
                moneda.setDescripcion(rs.getString("DESCRIPCION"));
                monedas.add(moneda);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return monedas;
    }

    public static Moneda obtenerMoneda(int idMoneda) {
        Moneda moneda = null;
        String q = "SELECT ID_MONEDA, VALOR, DESCRIPCION FROM MONEDA WHERE ID_MONEDA = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idMoneda);
            rs = pst.executeQuery();
            while (rs.next()) {
                moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setValor(rs.getInt("VALOR"));
                moneda.setDescripcion(rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return moneda;
    }

    public static ArrayList<ArqueoCajaDetalle> consultarUltimoArqueoCaja() {
        ArrayList<ArqueoCajaDetalle> arqueo = null;
        String QUERY = "SELECT ID_ARQUEO_CAJA, ID_CAJA, ARQUEO_CAJA.ID_MONEDA \"ID_MONEDA\", "
                + "ID_ARQUEO_CAJA_TIPO, CANTIDAD, VALOR, DESCRIPCION FROM ARQUEO_CAJA, MONEDA "
                + "WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA "
                + "AND ID_CAJA = (SELECT ID_CAJA FROM CAJA ORDER BY ID_CAJA DESC LIMIT 1) "
                + "AND ID_ARQUEO_CAJA_TIPO = 2";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(QUERY);
            arqueo = new ArrayList();
            while (rs.next()) {
                ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
                acd.setIdArqueoCajaDetalle(rs.getInt("ID_ARQUEO_CAJA"));
                acd.setIdCaja(rs.getInt("ID_CAJA"));
                acd.setCantidad(rs.getInt("CANTIDAD"));
                acd.setIdTipo(rs.getInt("ID_ARQUEO_CAJA_TIPO"));

                Moneda moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setValor(rs.getInt("VALOR"));
                moneda.setDescripcion(rs.getString("DESCRIPCION"));
                acd.setMoneda(moneda);
                arqueo.add(acd);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return arqueo;
    }

    public static ArrayList<ArqueoCajaDetalle> obtenerArqueoCaja(int idCaja, int idTipo) {
        String QUERY = "SELECT ID_ARQUEO_CAJA, ID_CAJA, ARQUEO_CAJA.ID_MONEDA \"ID_MONEDA\", "
                + "ID_ARQUEO_CAJA_TIPO, CANTIDAD, VALOR, DESCRIPCION FROM ARQUEO_CAJA, MONEDA "
                + "WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA "
                + "AND ID_CAJA = ? AND ID_ARQUEO_CAJA_TIPO = ?";

        ArrayList<ArqueoCajaDetalle> arqueo = null;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            pst.setInt(2, idTipo);
            rs = pst.executeQuery();
            arqueo = new ArrayList();
            while (rs.next()) {
                ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
                acd.setIdArqueoCajaDetalle(rs.getInt("ID_ARQUEO_CAJA"));
                acd.setIdCaja(rs.getInt("ID_CAJA"));
                acd.setCantidad(rs.getInt("CANTIDAD"));
                acd.setIdTipo(rs.getInt("ID_ARQUEO_CAJA_TIPO"));

                Moneda moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setValor(rs.getInt("VALOR"));
                moneda.setDescripcion(rs.getString("DESCRIPCION"));
                acd.setMoneda(moneda);
                arqueo.add(acd);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return arqueo;
    }

    public static Caja obtenerCaja(int idCaja) {
        String QUERY = "SELECT ID_CAJA, ID_FUNCIONARIO_APERTURA, ID_FUNCIONARIO_CIERRE, "
                + "MONTO_INICIAL, MONTO_FINAL, INGRESO_CONTADO, INGRESO_CREDITO, "
                + "EGRESO_CONTADO, EGRESO_CREDITO, TIEMPO_APERTURA, TIEMPO_CIERRE "
                + "FROM CAJA "
                + "WHERE ID_CAJA = ?";
        Caja caja = null;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            rs = pst.executeQuery();
            while (rs.next()) {
                caja = new Caja();
                caja.setIdCaja(rs.getInt("ID_CAJA"));
                caja.setEgresoContado(rs.getInt("EGRESO_CONTADO"));
                caja.setEgresoCredito(rs.getInt("EGRESO_CREDITO"));
                caja.setIdEmpleadoApertura(rs.getInt("ID_FUNCIONARIO_APERTURA"));
                caja.setIdEmpleadoCierre(rs.getInt("ID_FUNCIONARIO_CIERRE"));
                caja.setIngresoContado(rs.getInt("INGRESO_CONTADO"));
                caja.setIngresoCredito(rs.getInt("INGRESO_CREDITO"));
                caja.setMontoFinal(rs.getInt("MONTO_FINAL"));
                caja.setMontoInicial(rs.getInt("MONTO_INICIAL"));
                caja.setTiempoApertura(rs.getTimestamp("TIEMPO_APERTURA"));
                caja.setTiempoCierre(rs.getTimestamp("TIEMPO_CIERRE"));
            }
        } catch (SQLException ex) {
        }
        return caja;
    }

}
