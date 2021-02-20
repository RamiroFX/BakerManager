/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Entities.CierreCaja;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_facturaCabecera;
import Entities.E_reciboPagoCabecera;
import Entities.Estado;
import Entities.M_egresoCabecera;
import Entities.M_funcionario;
import Entities.Moneda;
import Interface.MovimientosCaja;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
            ArrayList<ArqueoCajaDetalle> arqueoDeposito,
            MovimientosCaja movimientosCaja) {
        String INSERT_ARQUEO = "INSERT INTO ARQUEO_CAJA( ID_CAJA, "
                + "ID_MONEDA, CANTIDAD, ID_ARQUEO_CAJA_TIPO)VALUES (?, ?, ?, ?)";
        String INSERT_CAJA = "INSERT INTO CAJA"
                + "(ID_FUNCIONARIO_APERTURA, ID_FUNCIONARIO_CIERRE, TIEMPO_APERTURA, TIEMPO_CIERRE)"
                + "VALUES "
                + "(?, ?, ?, ?)";
        String INSERT_CAJA_MOVIMIENTO = "INSERT INTO caja_movimiento (id_caja, id_movimiento_contable_tipo, id_movimiento) VALUES(?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CAJA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, caja.getIdEmpleadoApertura());
            pst.setInt(2, caja.getIdEmpleadoCierre());
            pst.setTimestamp(3, new Timestamp(caja.getTiempoApertura().getTime()));
            pst.setTimestamp(4, new Timestamp(caja.getTiempoCierre().getTime()));
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
            for (E_facturaCabecera movimientoVentas : movimientosCaja.getMovimientoVentas()) {
                pst = DB_manager.getConection().prepareStatement(INSERT_CAJA_MOVIMIENTO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, 1);
                pst.setInt(3, movimientoVentas.getIdFacturaCabecera());
                pst.executeUpdate();
                pst.close();
            }
            for (M_egresoCabecera movimientoCompras : movimientosCaja.getMovimientoCompras()) {
                pst = DB_manager.getConection().prepareStatement(INSERT_CAJA_MOVIMIENTO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, 2);
                pst.setInt(3, movimientoCompras.getId_cabecera());
                pst.executeUpdate();
                pst.close();
            }
            for (E_cuentaCorrienteCabecera movimientoCobro : movimientosCaja.getMovimientoCobros()) {
                pst = DB_manager.getConection().prepareStatement(INSERT_CAJA_MOVIMIENTO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, 3);
                pst.setInt(3, movimientoCobro.getId());
                pst.executeUpdate();
                pst.close();
            }
            for (E_reciboPagoCabecera movimientoPagos : movimientosCaja.getMovimientoPagos()) {
                pst = DB_manager.getConection().prepareStatement(INSERT_CAJA_MOVIMIENTO);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, 4);
                pst.setInt(3, movimientoPagos.getId());
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

    public static ResultSetTableModel consultarCajas(Integer idFuncionario, Timestamp fechaInicio, Timestamp fechaFin, int idEstado) {
        String Q_CAJA_APERTURA = "(SELECT SUM(CANTIDAD*VALOR) FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 1)";
        String Q_CAJA_CIERRE = "(SELECT SUM(CANTIDAD*VALOR) FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 2)";
        /*String Q_INGRESO_CONTADO = "(SELECT SUM(ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))\"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA  AND CAJA.TIEMPO_CIERRE "
                + "AND EGCA.ID_COND_COMPRA = 1)";
        String Q_INGRESO_CREDITO = "(SELECT SUM(ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))\"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA  AND CAJA.TIEMPO_CIERRE "
                + "AND EGCA.ID_COND_COMPRA = 2)";
        String Q_EGRESO_CONTADO = "(SELECT SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"Total\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA "
                + "WHERE FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FACA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA AND CAJA.TIEMPO_CIERRE "
                + "AND FACA.ID_COND_VENTA = 1)";
        String Q_EGRESO_CREDITO = "(SELECT SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"Total\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA "
                + "WHERE FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FACA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA AND CAJA.TIEMPO_CIERRE "
                + "AND FACA.ID_COND_VENTA = 2)";*/
        String Query = "SELECT ID_CAJA \"ID\", (SELECT NOMBRE ||' '|| APELLIDO \"Func. Apertura\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA), "
                + "	(SELECT NOMBRE ||' '|| APELLIDO \"Func. Cierre\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA), "
                + "COALESCE(" + Q_CAJA_APERTURA + ",0) \"Monto inicial\", "
                + "COALESCE(" + Q_CAJA_CIERRE + ",0) \"Monto final\", "
                //+ "COALESCE(" + Q_INGRESO_CONTADO + ",0) \"Ingreso contado\", "
                //+ "COALESCE(" + Q_INGRESO_CREDITO + ",0) \"Ingreso crédito\", "
                //+ "COALESCE(" + Q_EGRESO_CONTADO + ",0) \"Egreso contado\", "
                //+ "COALESCE(" + Q_EGRESO_CREDITO + ",0) \"Egreso crédito\", "
                + " TIEMPO_APERTURA \"Tiempo apertura\", TIEMPO_CIERRE \"Tiempo cierre\""
                + "  FROM CAJA, FUNCIONARIO , PERSONA"
                + "  WHERE CAJA.ID_FUNCIONARIO_APERTURA = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND  CAJA.ID_FUNCIONARIO_CIERRE = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND FUNCIONARIO.ID_PERSONA = PERSONA.ID_PERSONA"
                + "  AND CAJA.TIEMPO_CIERRE BETWEEN ?  AND ?  ";
        String func = " AND CAJA.ID_FUNCIONARIO_CIERRE = ? ";
        String estad = " AND CAJA.ID_ESTADO = ? ";
        String ORDER = " ORDER BY TIEMPO_CIERRE ";
        if (idFuncionario > -1) {
            Query = Query + func;
        }
        if (idEstado > -1) {
            Query = Query + estad;
        }
        Query = Query + ORDER;
        ResultSetTableModel rstm = null;
        int pos = 3;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, fechaInicio);
            pst.setTimestamp(2, fechaFin);
//            pst.setTimestamp(3, fechaInicio);
//            pst.setTimestamp(4, fechaFin);
//            pst.setTimestamp(5, fechaInicio);
//            pst.setTimestamp(6, fechaFin);
//            pst.setTimestamp(7, fechaInicio);
//            pst.setTimestamp(8, fechaFin);
//            pst.setTimestamp(9, fechaInicio);
//            pst.setTimestamp(10, fechaFin);
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }

            if (idEstado > -1) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ArrayList<Caja> consultarCajas2(Integer idFuncionario, Timestamp fechaInicio, Timestamp fechaFin, int idEstado) {
        ArrayList<Caja> cajas = new ArrayList<>();
        String Q_CAJA_APERTURA = "(SELECT SUM(CANTIDAD*VALOR) FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 1)";
        String Q_CAJA_CIERRE = "(SELECT SUM(CANTIDAD*VALOR) FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 2)";
        String Q_CAJA_DEPOSITADO = "(SELECT SUM(CANTIDAD*VALOR) FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 3)";
        String Query = "SELECT ID_CAJA \"ID\", "
                + "CAJA.ID_ESTADO, "
                + "(SELECT DESCRIPCION FROM ESTADO WHERE ESTADO.ID_ESTADO= CAJA.ID_ESTADO) \"ESTADO\", "
                + "(SELECT NOMBRE ||' '|| APELLIDO WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA) \"FUNC APERTURA\", "
                + "(SELECT NOMBRE ||' '|| APELLIDO WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_CIERRE)\"FUNC CIERRE\" , "
                + "COALESCE(" + Q_CAJA_APERTURA + ",0) \"MONTO_INICIAL\", "
                + "COALESCE(" + Q_CAJA_CIERRE + ",0) \"MONTO_FINAL\", "
                + "COALESCE(" + Q_CAJA_DEPOSITADO + ",0) \"MONTO_DEPOSITADO\", "
                + " TIEMPO_APERTURA, "
                + " TIEMPO_CIERRE "
                + "  FROM CAJA, FUNCIONARIO , PERSONA"
                + "  WHERE CAJA.ID_FUNCIONARIO_APERTURA = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND  CAJA.ID_FUNCIONARIO_CIERRE = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND FUNCIONARIO.ID_PERSONA = PERSONA.ID_PERSONA"
                + "  AND CAJA.TIEMPO_CIERRE BETWEEN ?  AND ?  ";
        String func = " AND CAJA.ID_FUNCIONARIO_CIERRE = ? ";
        String estad = " AND CAJA.ID_ESTADO = ? ";
        String ORDER = " ORDER BY TIEMPO_CIERRE ";
        if (idFuncionario > -1) {
            Query = Query + func;
        }
        if (idEstado > -1) {
            Query = Query + estad;
        }
        Query = Query + ORDER;
        int pos = 3;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, fechaInicio);
            pst.setTimestamp(2, fechaFin);
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (idEstado > -1) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = pst.executeQuery();
            while (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getInt("ID_ESTADO"));
                estado.setDescripcion(rs.getString("ESTADO"));
                M_funcionario funcAper = new M_funcionario();
                funcAper.setNombre(rs.getString("FUNC APERTURA"));
                M_funcionario funcCierre = new M_funcionario();
                funcCierre.setNombre(rs.getString("FUNC CIERRE"));
                Caja unaCaja = new Caja();
                unaCaja.setFuncionarioApertura(funcAper);
                unaCaja.setFuncionarioCierre(funcCierre);
                unaCaja.setEstado(estado);
                unaCaja.setMontoApertura(rs.getInt("MONTO_INICIAL"));
                unaCaja.setMontoCierre(rs.getInt("MONTO_FINAL"));
                unaCaja.setMontoDepositado(rs.getInt("MONTO_DEPOSITADO"));
                unaCaja.setIdCaja(rs.getInt("ID"));
                unaCaja.setTiempoApertura(rs.getTimestamp("TIEMPO_APERTURA"));
                unaCaja.setTiempoCierre(rs.getTimestamp("TIEMPO_CIERRE"));
                cajas.add(unaCaja);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Caja.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return cajas;
    }

    public static ArrayList<CierreCaja> consultarCajas2(Integer idFuncionario, Timestamp fechaInicio, Timestamp fechaFin) {
        ArrayList<CierreCaja> cierreCajas = null;
        String Q_CAJA_APERTURA = "(SELECT CANTIDAD, VALOR FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 1)";
        String Q_CAJA_CIERRE = "(SELECT CANTIDAD, VALOR FROM ARQUEO_CAJA, MONEDA WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA AND ARQUEO_CAJA.ID_CAJA = CAJA.ID_CAJA AND ID_ARQUEO_CAJA_TIPO = 2)";
        String Q_INGRESO_CONTADO = "(SELECT EGDE.CANTIDAD, EGDE.PRECIO, EGDE.DESCUENTO \"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA  AND CAJA.TIEMPO_CIERRE "
                + "AND EGCA.ID_COND_COMPRA = 1)";
        String Q_INGRESO_CREDITO = "(SELECT SUM(ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))\"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA  AND CAJA.TIEMPO_CIERRE "
                + "AND EGCA.ID_COND_COMPRA = 2)";
        String Q_EGRESO_CONTADO = "(SELECT SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"Total\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA "
                + "WHERE FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FACA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA AND CAJA.TIEMPO_CIERRE "
                + "AND FACA.ID_COND_VENTA = 1)";
        String Q_EGRESO_CREDITO = "(SELECT SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"Total\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA "
                + "WHERE FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FACA.TIEMPO BETWEEN CAJA.TIEMPO_APERTURA AND CAJA.TIEMPO_CIERRE "
                + "AND FACA.ID_COND_VENTA = 2)";
        String Query = "SELECT ID_CAJA \"ID\", (SELECT NOMBRE ||' '|| APELLIDO \"Func. Apertura\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA), "
                + "	(SELECT NOMBRE ||' '|| APELLIDO \"Func. Cierre\" WHERE PERSONA.ID_PERSONA = FUNCIONARIO.ID_PERSONA AND FUNCIONARIO.ID_FUNCIONARIO = ID_FUNCIONARIO_APERTURA), "
                + "COALESCE(" + Q_CAJA_APERTURA + ",0) \"Monto inicial\", "
                + "COALESCE(" + Q_CAJA_CIERRE + ",0) \"Monto final\", "
                + "COALESCE(" + Q_INGRESO_CONTADO + ",0) \"Ingreso contado\", "
                + "COALESCE(" + Q_INGRESO_CREDITO + ",0) \"Ingreso crédito\", "
                + "COALESCE(" + Q_EGRESO_CONTADO + ",0) \"Egreso contado\", "
                + "COALESCE(" + Q_EGRESO_CREDITO + ",0) \"Egreso crédito\", "
                + " TIEMPO_APERTURA \"Tiempo apertura\", TIEMPO_CIERRE \"Tiempo cierre\""
                + "  FROM CAJA, FUNCIONARIO , PERSONA"
                + "  WHERE CAJA.ID_FUNCIONARIO_APERTURA = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND  CAJA.ID_FUNCIONARIO_CIERRE = FUNCIONARIO.ID_FUNCIONARIO"
                + "  AND FUNCIONARIO.ID_PERSONA = PERSONA.ID_PERSONA"
                + "  AND CAJA.TIEMPO_CIERRE BETWEEN ?  AND ?  ";
        String func = "AND CAJA.ID_FUNCIONARIO_CIERRE = ?";
        if (idFuncionario > -1) {
            Query = Query + func;
        }
        ResultSetTableModel rstm = null;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, fechaInicio);
            pst.setTimestamp(2, fechaFin);
            if (idFuncionario > -1) {
                pst.setInt(3, idFuncionario);
            }
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return cierreCajas;
    }

    public static ArrayList consultarCajasExportacion(Integer idFuncionario, Timestamp inicio, Timestamp fin) {
        ArrayList<CierreCaja> cajas = new ArrayList<>();
        String ATTACH_FUNCIONARIO = "AND CAJA.ID_FUNCIONARIO_CIERRE = ? ";
        String QUERY_CAJA = "SELECT ID_CAJA, ID_FUNCIONARIO_APERTURA, ID_FUNCIONARIO_CIERRE, "
                + "TIEMPO_APERTURA, TIEMPO_CIERRE "
                + "FROM CAJA "
                + "WHERE CAJA.TIEMPO_CIERRE BETWEEN ?  AND ? "
                + "ORDER BY ID_CAJA";
        String QUERY_ARQUEO = "SELECT ID_ARQUEO_CAJA, ID_CAJA, ARQUEO_CAJA.ID_MONEDA \"ID_MONEDA\", "
                + "ID_ARQUEO_CAJA_TIPO, CANTIDAD, VALOR, DESCRIPCION FROM ARQUEO_CAJA, MONEDA "
                + "WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA "
                + "AND ID_CAJA = ? AND ID_ARQUEO_CAJA_TIPO = ?";
        if (idFuncionario != null) {
            QUERY_CAJA = QUERY_CAJA + ATTACH_FUNCIONARIO;
        }
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY_CAJA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, inicio);
            pst.setTimestamp(2, fin);
            if (idFuncionario != null) {
                pst.setInt(3, idFuncionario);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                Caja caja = new Caja();
                caja.setIdCaja(rs.getInt("ID_CAJA"));
                caja.setIdEmpleadoApertura(rs.getInt("ID_FUNCIONARIO_APERTURA"));
                caja.setIdEmpleadoCierre(rs.getInt("ID_FUNCIONARIO_CIERRE"));
                caja.setTiempoApertura(rs.getTimestamp("TIEMPO_APERTURA"));
                caja.setTiempoCierre(rs.getTimestamp("TIEMPO_CIERRE"));

                //QUERY APERTURA
                PreparedStatement pst1 = DB_manager.getConection().prepareStatement(QUERY_ARQUEO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst1.setInt(1, caja.getIdCaja());
                pst1.setInt(2, 1);//APERTURA
                ResultSet rs1 = pst1.executeQuery();
                ArrayList<ArqueoCajaDetalle> arqueoApertura = new ArrayList<>();
                while (rs1.next()) {
                    ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
                    acd.setIdArqueoCajaDetalle(rs1.getInt("ID_ARQUEO_CAJA"));
                    acd.setIdCaja(rs1.getInt("ID_CAJA"));
                    acd.setCantidad(rs1.getInt("CANTIDAD"));
                    acd.setIdTipo(rs1.getInt("ID_ARQUEO_CAJA_TIPO"));

                    Moneda moneda = new Moneda();
                    moneda.setIdMoneda(rs1.getInt("ID_MONEDA"));
                    moneda.setValor(rs1.getInt("VALOR"));
                    moneda.setDescripcion(rs1.getString("DESCRIPCION"));
                    acd.setMoneda(moneda);
                    arqueoApertura.add(acd);
                }
                //QUERY CIERRE
                PreparedStatement pst2 = DB_manager.getConection().prepareStatement(QUERY_ARQUEO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst2.setInt(1, caja.getIdCaja());
                pst2.setInt(2, 2);//CIERRE
                ResultSet rs2 = pst2.executeQuery();
                ArrayList<ArqueoCajaDetalle> arqueoCierre = new ArrayList<>();
                while (rs2.next()) {
                    ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
                    acd.setIdArqueoCajaDetalle(rs2.getInt("ID_ARQUEO_CAJA"));
                    acd.setIdCaja(rs2.getInt("ID_CAJA"));
                    acd.setCantidad(rs2.getInt("CANTIDAD"));
                    acd.setIdTipo(rs2.getInt("ID_ARQUEO_CAJA_TIPO"));

                    Moneda moneda = new Moneda();
                    moneda.setIdMoneda(rs2.getInt("ID_MONEDA"));
                    moneda.setValor(rs2.getInt("VALOR"));
                    moneda.setDescripcion(rs2.getString("DESCRIPCION"));
                    acd.setMoneda(moneda);
                    arqueoCierre.add(acd);
                }
                //QUERY DEPOSITO
                PreparedStatement pst3 = DB_manager.getConection().prepareStatement(QUERY_ARQUEO, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst3.setInt(1, caja.getIdCaja());
                pst3.setInt(2, 3);//DEPOSITO
                ResultSet rs3 = pst3.executeQuery();
                ArrayList<ArqueoCajaDetalle> arqueoDeposito = new ArrayList<>();
                while (rs3.next()) {
                    ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
                    acd.setIdArqueoCajaDetalle(rs3.getInt("ID_ARQUEO_CAJA"));
                    acd.setIdCaja(rs3.getInt("ID_CAJA"));
                    acd.setCantidad(rs3.getInt("CANTIDAD"));
                    acd.setIdTipo(rs3.getInt("ID_ARQUEO_CAJA_TIPO"));

                    Moneda moneda = new Moneda();
                    moneda.setIdMoneda(rs3.getInt("ID_MONEDA"));
                    moneda.setValor(rs3.getInt("VALOR"));
                    moneda.setDescripcion(rs3.getString("DESCRIPCION"));
                    acd.setMoneda(moneda);
                    arqueoDeposito.add(acd);
                }
                CierreCaja cierreCaja = new CierreCaja(caja, arqueoApertura, arqueoCierre, arqueoDeposito);
                cajas.add(cierreCaja);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cajas;
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
                + " TIEMPO_APERTURA, TIEMPO_CIERRE "
                + " FROM CAJA "
                + " WHERE ID_CAJA = ?";
        Caja caja = null;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            rs = pst.executeQuery();
            while (rs.next()) {
                caja = new Caja();
                caja.setIdCaja(rs.getInt("ID_CAJA"));
                caja.setIdEmpleadoApertura(rs.getInt("ID_FUNCIONARIO_APERTURA"));
                caja.setIdEmpleadoCierre(rs.getInt("ID_FUNCIONARIO_CIERRE"));
                caja.setTiempoApertura(rs.getTimestamp("TIEMPO_APERTURA"));
                caja.setTiempoCierre(rs.getTimestamp("TIEMPO_CIERRE"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return caja;
    }

    public static int obtenerTotalArqueoCaja(int idCaja, int idTipo) {
        String QUERY = "SELECT CANTIDAD, VALOR FROM ARQUEO_CAJA, MONEDA "
                + "WHERE MONEDA.ID_MONEDA = ARQUEO_CAJA.ID_MONEDA "
                + "AND ID_CAJA = ? AND ID_ARQUEO_CAJA_TIPO = ?";
        int arqueo = 0;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            pst.setInt(2, idTipo);
            rs = pst.executeQuery();
            while (rs.next()) {
                int cantidad = (rs.getInt("CANTIDAD"));
                int valor = (rs.getInt("VALOR"));
                arqueo = arqueo + (cantidad * valor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return arqueo;
    }

    public static void cambiarEstadoCaja(Integer idCaja, int idEstado) {
        String UPDATE_VENTA = "UPDATE caja SET id_estado = ? WHERE id_caja = ?";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_VENTA);
            pst.setInt(1, idEstado);
            pst.setInt(2, idCaja);
            pst.executeUpdate();
            pst.close();
            //se anulan los movimientos de caja tambien
            List<E_facturaCabecera> ventas = DB_Ingreso.obtenerMovimientoVentasCabeceras(idCaja);
            for (int i = 0; i < ventas.size(); i++) {
                String query = "UPDATE caja_movimiento SET "
                        + "id_estado = 2 WHERE id_movimiento_contable_tipo = 1 AND id_movimiento = " + ventas.get(i).getIdFacturaCabecera();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            List<M_egresoCabecera> compras = DB_Egreso.obtenerMovimientoComprasCabeceras(idCaja);
            for (int i = 0; i < compras.size(); i++) {
                String query = "UPDATE caja_movimiento SET "
                        + "id_estado = 2 WHERE id_movimiento_contable_tipo = 2 AND id_movimiento = " + compras.get(i).getId_cabecera();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            List<E_cuentaCorrienteCabecera> cobros = DB_Cobro.obtenerMovimientoCobrosCabeceras(idCaja);
            for (int i = 0; i < cobros.size(); i++) {
                String query = "UPDATE caja_movimiento SET "
                        + "id_estado = 2 WHERE id_movimiento_contable_tipo = 3 AND id_movimiento = " + cobros.get(i).getId();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            List<E_reciboPagoCabecera> pagos = DB_Pago.obtenerMovimientoPagosCabeceras(idCaja);
            for (int i = 0; i < pagos.size(); i++) {
                String query = "UPDATE caja_movimiento SET "
                        + "id_estado = 2 WHERE id_movimiento_contable_tipo = 4 AND id_movimiento = " + pagos.get(i).getId();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Caja.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Caja.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
