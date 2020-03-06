/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_banco;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_egresoSinPago;
import Entities.E_formaPago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_proveedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Pago {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static boolean existeNroRecibo(int nroRecibo, int idProveedor) {
        String Query = "SELECT nro_recibo FROM recibo_pago_cabecera WHERE nro_recibo = ? AND id_proveedor = ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, nroRecibo);
            pst.setInt(2, idProveedor);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Pago.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static void anularPago(int idUtilizacionCabecera, boolean anularNroRecibo) {
        String UPDATE_COBRO_NRO_RECIBO = "UPDATE recibo_pago_cabecera SET "
                + "ID_ESTADO = 2, "
                + "NRO_RECIBO = 0 "
                + "WHERE id_recibo_pago_cabecera = ?; ";

        String UPDATE_COBRO = "UPDATE recibo_pago_cabecera SET "
                + "ID_ESTADO = 2 "
                + "WHERE id_recibo_pago_cabecera = ?; ";
        String QUERY = "";
        if (anularNroRecibo) {
            QUERY = UPDATE_COBRO_NRO_RECIBO;
        } else {
            QUERY = UPDATE_COBRO;
        }
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(QUERY);
            pst.setInt(1, idUtilizacionCabecera);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Pago.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Pago.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static List<E_reciboPagoCabecera> obtenerPago(int idProveedor, int idFuncionario, Date fechaInicio, Date fechaFinal, int nroRecibo, int idEstado) {
        List<E_reciboPagoCabecera> list = new ArrayList();
        String query = "SELECT "
                + "CCC.ID_RECIBO_PAGO_CABECERA, "//1
                + "CCC.ID_PROVEEDOR, "//2
                + "CCC.ID_FUNCIONARIO_REGISTRO, "//3
                + "CCC.NRO_RECIBO, "//4
                + "CCC.ID_ESTADO, "//5
                + "CCC.FECHA_PAGO, "//6
                + "CCC.FECHA_REGISTRO, "//7
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = CCC.ID_ESTADO) \"ESTADO\", "//8
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = CCC.id_funcionario_registro)\"USUARIO\", "//9
                + "(SUM (CCD.MONTO))\"TOTAL\", "//10
                + "C.ENTIDAD "//11
                + "FROM RECIBO_PAGO_CABECERA CCC, RECIBO_PAGO_DETALLE CCD, PROVEEDOR C "
                + "WHERE CCC.ID_RECIBO_PAGO_CABECERA = CCD.ID_RECIBO_PAGO_CABECERA "
                + "AND CCC.ID_PROVEEDOR = C.ID_PROVEEDOR "
                + "AND CCC.FECHA_PAGO BETWEEN ?  AND ? ";

        String groupBy = " GROUP BY CCC.ID_RECIBO_PAGO_CABECERA, CCC.ID_PROVEEDOR, "
                + "CCC.ID_FUNCIONARIO_REGISTRO, CCC.NRO_RECIBO, "
                + "CCC.ID_ESTADO, CCC.FECHA_PAGO, CCC.FECHA_REGISTRO, C.ENTIDAD ";
        String orderBy = "ORDER BY CCC.ID_RECIBO_PAGO_CABECERA";

        if (idProveedor > 0) {
            query = query + " AND CCC.ID_PROVEEDOR = ? ";
        }
        if (idFuncionario > 0) {
            query = query + " AND CCC.ID_FUNCIONARIO_REGISTRO = ? ";
        }
        if (nroRecibo > 0) {
            query = query + " AND CCC.NRO_RECIBO = ? ";
        }
        if (idEstado > 0) {
            query = query + " AND CCC.ID_ESTADO = ? ";
        }
        query = query + groupBy + orderBy;
        int pos = 1;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            pst.setTimestamp(pos, new java.sql.Timestamp(fechaInicio.getTime()));
            pos++;
            pst.setTimestamp(pos, new java.sql.Timestamp(fechaFinal.getTime()));
            pos++;
            if (idProveedor > 0) {
                pst.setInt(pos, idProveedor);
                pos++;
            }
            if (idFuncionario > 0) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (nroRecibo > 0) {
                pst.setInt(pos, nroRecibo);
                pos++;
            }
            if (idEstado > 0) {
                pst.setInt(pos, idEstado);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                proveedor.setEntidad(rs.getString(11));
                proveedor.setId(rs.getInt(2));
                M_funcionario usuario = new M_funcionario();
                usuario.setId_funcionario(rs.getInt(3));
                usuario.setNombre(rs.getString(9));
                Estado estado = new Estado();
                estado.setId(rs.getInt(5));
                estado.setDescripcion(rs.getString(8));
                E_reciboPagoCabecera ccc = new E_reciboPagoCabecera();
                ccc.setId(rs.getInt(1));
                ccc.setFechaPago(rs.getTimestamp(6));
                ccc.setFechaOperacion(rs.getTimestamp(7));
                ccc.setNroRecibo(rs.getInt(4));
                ccc.setProveedor(proveedor);
                ccc.setFuncionario(usuario);
                ccc.setEstado(estado);
                ccc.setMonto(rs.getInt(10));
                list.add(ccc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Pago.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_reciboPagoDetalle> obtenerPagoDetalle(Integer idCabecera) {
        List<E_reciboPagoDetalle> list = new ArrayList();
        String query = "SELECT "
                + "CCD.ID_RECIBO_PAGO_DETALLE, "//1
                + "CCD.ID_RECIBO_PAGO_CABECERA, "//2
                + "CCD.ID_EGRESO_CABECERA, "//3
                + "CCD.NRO_RECIBO, "//4
                + "CCD.MONTO, "//5
                + "CCD.NRO_CHEQUE, "//6
                + "CCD.ID_BANCO, "//7
                + "CCD.CHEQUE_FECHA, "//8
                + "CCD.CHEQUE_FECHA_DIFERIDA, "//9
                + "(SELECT B.DESCRIPCION FROM BANCO B WHERE B.ID_BANCO = CCD.ID_BANCO) \"BANCO\", "//10
                + "(SELECT EC.NRO_FACTURA FROM EGRESO_CABECERA EC WHERE EC.ID_EGRESO_CABECERA = CCD.ID_EGRESO_CABECERA) \"NRO_FACTURA\" "//11
                + "FROM RECIBO_PAGO_DETALLE CCD "
                + "WHERE CCD.ID_RECIBO_PAGO_CABECERA = ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_reciboPagoDetalle detalle = new E_reciboPagoDetalle();
                E_banco banco = new E_banco();
                banco.setId(rs.getInt(7));
                banco.setDescripcion(rs.getString(10));
                detalle.setId(rs.getInt(1));
                detalle.setIdReciboPagoCabecera(rs.getInt(2));
                detalle.setIdFacturaCabecera(rs.getInt(3));
                detalle.setNroFactura(rs.getInt(11));
                detalle.setNroRecibo(rs.getInt(4));
                detalle.setMonto(rs.getInt(5));
                detalle.setNroCheque(rs.getInt(6));
                detalle.setFechaCheque(rs.getTimestamp(8));
                detalle.setFechaDiferidaCheque(rs.getTimestamp(9));
                detalle.setBanco(banco);
                list.add(detalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Pago.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_egresoSinPago> consultarFacturasPendiente(int idCliente) {
        List<E_egresoSinPago> list = new ArrayList<>();
        String Query = "SELECT * FROM v_compras_sin_pago WHERE id_proveedor = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_egresoSinPago fsp = new E_egresoSinPago();
                fsp.setIdCabecera(rs.getInt("id_cabecera"));
                fsp.setIdProveedor(rs.getInt("id_proveedor"));
                fsp.setNroFactura(rs.getInt("nro_factura"));
                fsp.setProveedorEntidad(rs.getString("proveedor"));
                fsp.setFecha(rs.getTimestamp("fecha"));
                fsp.setMonto(rs.getInt("monto"));
                fsp.setPago(rs.getInt("pago"));
                fsp.setSaldo(rs.getInt("saldo"));
                fsp.setRuc(rs.getString("ruc"));
                fsp.setRuc(rs.getString("ruc_identificador"));
                list.add(fsp);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_egresoSinPago> consultarPagosPendiente(Date fechaInicio, Date fechaFin, int idProveedor, int nroFactura, boolean conFecha) {
        List<E_egresoSinPago> list = new ArrayList<>();
        String Query = "SELECT * FROM v_compras_sin_pago WHERE 1=1 ";
        if (conFecha) {
            Query = Query + "AND FECHA BETWEEN ? AND ? ";
        }
        if (idProveedor > 0) {
            Query = Query + " AND ID_PROVEEDOR = ? ";
        }
        if (nroFactura > 0) {
            Query = Query + " AND NRO_FACTURA = ? ";
        }
        int pos = 1;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos, new Timestamp(fechaInicio.getTime()));
                pos++;
                pst.setTimestamp(pos, new Timestamp(fechaFin.getTime()));
                pos++;
            }
            if (idProveedor > 0) {
                pst.setInt(pos, idProveedor);
                pos++;
            }
            if (nroFactura > 0) {
                pst.setInt(pos, nroFactura);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_egresoSinPago fsp = new E_egresoSinPago();
                fsp.setIdCabecera(rs.getInt("id_cabecera"));
                fsp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                fsp.setNroFactura(rs.getInt("nro_factura"));
                fsp.setProveedorEntidad(rs.getString("proveedor"));
                fsp.setFecha(rs.getTimestamp("fecha"));
                fsp.setMonto(rs.getInt("monto"));
                fsp.setPago(rs.getInt("pago"));
                fsp.setSaldo(rs.getInt("saldo"));
                fsp.setRuc(rs.getString("ruc"));
                fsp.setRuc(rs.getString("ruc_identificador"));
                list.add(fsp);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_reciboPagoDetalle> consultarPagoDetalleAgrupado(List<E_reciboPagoCabecera> cadenaCabeceras) {
        List<E_reciboPagoDetalle> list = new ArrayList<>();
        boolean b = true;
        StringBuilder builder = new StringBuilder();
        for (E_reciboPagoCabecera seleccionCtaCte : cadenaCabeceras) {
            builder.append("?,");
            b = false;
        }
        //para controlar que la lista contenga por lo menos una venta seleccionada
        if (b) {
            return list;
        }
        String QUERY = "SELECT "
                + "SUM(CCD.MONTO), "//1
                + "CCD.ID_BANCO, "//2
                + "(SELECT B.DESCRIPCION FROM BANCO B WHERE B.ID_BANCO = CCD.ID_BANCO) \"BANCO\" "//3
                + "FROM RECIBO_PAGO_CABECERA CCC, RECIBO_PAGO_DETALLE CCD "
                + "WHERE CCC.ID_RECIBO_PAGO_CABECERA = CCD.ID_RECIBO_PAGO_CABECERA "
                + "AND CCD.ID_RECIBO_PAGO_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY CCD.ID_BANCO  "
                + "ORDER BY CCD.ID_BANCO";

        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (E_reciboPagoCabecera seleccionCtaCte : cadenaCabeceras) {
                pst.setInt(index, seleccionCtaCte.getId());
                index++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_reciboPagoDetalle ctaCteDetalle = new E_reciboPagoDetalle();
                E_banco banco = new E_banco();
                banco.setId(rs.getInt(2));
                banco.setDescripcion(rs.getString(3));
                ctaCteDetalle.setMonto(rs.getInt(1));
                ctaCteDetalle.setBanco(banco);
                list.add(ctaCteDetalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_cuentaCorrienteDetalle> obtenerChequesPendientes() {
        List<E_cuentaCorrienteDetalle> list = new ArrayList();
        String query = "SELECT "
                + "CCD.id_recibo_pago_detalle, "//1
                + "CCD.id_recibo_pago_cabecera, "//2
                + "CCD.id_egreso_cabecera, "//3
                + "CCD.NRO_RECIBO, "//4
                + "CCD.MONTO, "//5
                + "CCD.NRO_CHEQUE, "//6
                + "CCD.ID_BANCO, "//7
                + "CCD.CHEQUE_FECHA, "//8
                + "CCD.CHEQUE_FECHA_DIFERIDA, "//9
                + "(SELECT B.DESCRIPCION FROM BANCO B WHERE B.ID_BANCO = CCD.ID_BANCO) \"BANCO\", "//10
                + "CCD.ID_ESTADO_CHEQUE, "//11
                + "(SELECT EC.NRO_FACTURA FROM EGRESO_CABECERA EC WHERE EC.ID_EGRESO_CABECERA = CCD.ID_EGRESO_CABECERA) \"NRO_FACTURA\" "//12
                + "FROM RECIBO_PAGO_DETALLE CCD, RECIBO_PAGO_CABECERA RPC "
                + "WHERE cheque_fecha_diferida >= now() "
                + "AND RPC.id_recibo_pago_cabecera = CCD.id_recibo_pago_cabecera "
                + "AND id_estado_cheque = 2"
                + "AND RPC.id_estado = 1 "
                + "ORDER BY cheque_fecha_diferida;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_cuentaCorrienteDetalle detalle = new E_cuentaCorrienteDetalle();
                E_banco banco = new E_banco();
                banco.setId(rs.getInt(7));
                banco.setDescripcion(rs.getString(10));
                detalle.setId(rs.getInt(1));
                detalle.setIdCuentaCorrienteCabecera(rs.getInt(2));
                detalle.setIdFacturaCabecera(rs.getInt(3));
                detalle.setNroFactura(rs.getInt(12));
                detalle.setNroRecibo(rs.getInt(4));
                detalle.setMonto(rs.getInt(5));
                detalle.setNroCheque(rs.getInt(6));
                detalle.setFechaCheque(rs.getTimestamp(8));
                detalle.setFechaDiferidaCheque(rs.getTimestamp(9));
                detalle.setBanco(banco);
                list.add(detalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static void cobrarCheque(int idCtaCteDetalle) {
        String UPDATE_COBRO = "UPDATE recibo_pago_detalle SET "
                + "id_estado_cheque = 1 "
                + "WHERE id_recibo_pago_detalle = ?; ";

        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_COBRO);
            pst.setInt(1, idCtaCteDetalle);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Pago.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Pago.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int guardarPago(E_reciboPagoCabecera cabecera, ArrayList<E_reciboPagoDetalle> detalle) {
        String INSERT_CABECERA = "INSERT INTO recibo_pago_cabecera"
                + "(id_proveedor, id_funcionario_registro, nro_recibo, id_estado, fecha_pago)"
                + "VALUES (?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE_CHEQUE_DIFERIDO = "INSERT INTO recibo_pago_detalle(id_recibo_pago_cabecera, id_egreso_cabecera, nro_recibo, monto, nro_cheque, id_banco, cheque_fecha, cheque_fecha_diferida, id_estado_cheque)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_DETALLE_CHEQUE = "INSERT INTO recibo_pago_detalle(id_recibo_pago_cabecera, id_egreso_cabecera, nro_recibo, monto, nro_cheque, id_banco, cheque_fecha)VALUES (?, ?, ?, ?, ?, ?, ?);";
        String INSERT_DETALLE_EFECTIVO = "INSERT INTO recibo_pago_detalle(id_recibo_pago_cabecera, id_egreso_cabecera, nro_recibo, monto)VALUES (?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getProveedor().getId());
            pst.setInt(2, cabecera.getFuncionario().getId_funcionario());
            pst.setInt(3, cabecera.getNroRecibo());
            pst.setInt(4, Estado.ACTIVO);//ESTADO ACTIVO
            pst.setTimestamp(5, new Timestamp(cabecera.getFechaPago().getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (E_reciboPagoDetalle unDetalle : detalle) {
                int idFormaPago = unDetalle.getFormaPago().getId();
                switch (idFormaPago) {
                    case E_formaPago.EFECTIVO: {
                        pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_EFECTIVO);
                        pst.setInt(1, (int) sq_cabecera);
                        pst.setInt(2, unDetalle.getIdFacturaCabecera());
                        pst.setInt(3, cabecera.getNroRecibo());
                        pst.setInt(4, (int) unDetalle.getMonto());
                        pst.executeUpdate();
                        pst.close();
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        if (unDetalle.esChequeDiferido()) {
                            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_CHEQUE_DIFERIDO);
                            pst.setInt(1, (int) sq_cabecera);
                            pst.setInt(2, unDetalle.getIdFacturaCabecera());
                            pst.setInt(3, cabecera.getNroRecibo());
                            pst.setInt(4, (int) unDetalle.getMonto());
                            pst.setInt(5, unDetalle.getNroCheque());
                            pst.setInt(6, unDetalle.getBanco().getId());
                            pst.setTimestamp(7, new Timestamp(unDetalle.getFechaCheque().getTime()));
                            pst.setTimestamp(8, new Timestamp(unDetalle.getFechaDiferidaCheque().getTime()));
                            pst.setInt(9, Estado.INACTIVO);
                            pst.executeUpdate();
                            pst.close();
                        } else {
                            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_CHEQUE);
                            pst.setInt(1, (int) sq_cabecera);
                            pst.setInt(2, unDetalle.getIdFacturaCabecera());
                            pst.setInt(3, cabecera.getNroRecibo());
                            pst.setInt(4, (int) unDetalle.getMonto());
                            pst.setInt(5, unDetalle.getNroCheque());
                            pst.setInt(6, unDetalle.getBanco().getId());
                            pst.setTimestamp(7, new Timestamp(unDetalle.getFechaCheque().getTime()));
                            pst.executeUpdate();
                            pst.close();
                        }
                        break;
                    }
                }
            }
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Pago.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Pago.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Pago.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }
}
