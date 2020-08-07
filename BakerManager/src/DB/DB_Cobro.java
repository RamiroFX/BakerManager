/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_banco;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.E_formaPago;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
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
public class DB_Cobro {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static boolean existeNroRecibo(int nroRecibo) {
        String Query = "SELECT nro_recibo  FROM cuenta_corriente_cabecera WHERE nro_recibo = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, nroRecibo);
            rs = pst.executeQuery();
            return rs.next();
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
        return false;
    }

    public static List<E_facturaSinPago> consultarFacturasPendiente(int idCliente) {
        List<E_facturaSinPago> list = new ArrayList<>();
        String Query = "SELECT * FROM v_facturas_sin_pago WHERE id_cliente = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaSinPago fsp = new E_facturaSinPago();
                fsp.setIdCabecera(0);
                fsp.setIdCliente(rs.getInt("id_cliente"));
                fsp.setNroFactura(rs.getInt("nro_factura"));
                fsp.setClienteEntidad(rs.getString("cliente"));
                fsp.setFecha(rs.getTimestamp("fecha"));
                fsp.setMonto(rs.getInt("monto"));
                fsp.setPago(rs.getInt("pago"));
                fsp.setSaldo(rs.getInt("saldo"));
                fsp.setRuc(rs.getString("ruc"));
                fsp.setRuc(rs.getString("ruc_identificador"));
                list.add(fsp);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_facturaSinPago> consultarFacturasPendiente(Date fechaInicio, Date fechaFin, int idCliente, int nroFactura, boolean conFecha) {
        List<E_facturaSinPago> list = new ArrayList<>();
        String Query = "SELECT * FROM v_facturas_sin_pago WHERE 1=1 ";
        if (conFecha) {
            Query = Query + "AND FECHA BETWEEN ? AND ? ";
        }
        if (idCliente > 0) {
            Query = Query + " AND ID_CLIENTE = ? ";
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
            if (idCliente > 0) {
                pst.setInt(pos, idCliente);
                pos++;
            }
            if (nroFactura > 0) {
                pst.setInt(pos, nroFactura);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaSinPago fsp = new E_facturaSinPago();
                fsp.setIdCabecera(0);
                fsp.setIdCliente(rs.getInt("id_cliente"));
                fsp.setNroFactura(rs.getInt("nro_factura"));
                fsp.setClienteEntidad(rs.getString("cliente"));
                fsp.setFecha(rs.getTimestamp("fecha"));
                fsp.setMonto(rs.getInt("monto"));
                fsp.setPago(rs.getInt("pago"));
                fsp.setSaldo(fsp.getMonto() - fsp.getPago());
                fsp.setRuc(rs.getString("ruc"));
                fsp.setRuc(rs.getString("ruc_identificador"));
                list.add(fsp);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_cuentaCorrienteDetalle> consultarCobroDetalleAgrupado(List<E_cuentaCorrienteCabecera> cadenaCabeceras) {
        List<E_cuentaCorrienteDetalle> list = new ArrayList<>();
        boolean b = true;
        StringBuilder builder = new StringBuilder();
        for (E_cuentaCorrienteCabecera seleccionCtaCte : cadenaCabeceras) {
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
                + "FROM CUENTA_CORRIENTE_CABECERA CCC, CUENTA_CORRIENTE_DETALLE CCD "
                + "WHERE CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA "
                + "AND CCD.ID_CTA_CTE_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY CCD.ID_BANCO  "
                + "ORDER BY CCD.ID_BANCO";

        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (E_cuentaCorrienteCabecera seleccionCtaCte : cadenaCabeceras) {
                pst.setInt(index, seleccionCtaCte.getId());
                index++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_cuentaCorrienteDetalle ctaCteDetalle = new E_cuentaCorrienteDetalle();
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

    public static List<E_cuentaCorrienteCabecera> obtenerCobros(int idCliente, int idFuncionario, Date fechaInicio, Date fechaFinal, int nroRecibo, int idEstado, boolean conFecha) {
        List<E_cuentaCorrienteCabecera> list = new ArrayList();
        String query = "SELECT "
                + "CCC.ID_CTA_CTE_CABECERA, "//1
                + "CCC.ID_CLIENTE, "//2
                + "CCC.ID_FUNCIONARIO_COBRADOR, "//3
                + "CCC.ID_FUNCIONARIO_REGISTRO, "//4
                + "CCC.NRO_RECIBO, "//5
                + "CCC.ID_ESTADO, "//6
                + "CCC.FECHA_COBRO, "//7
                + "CCC.FECHA_REGISTRO, "//8
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = CCC.ID_ESTADO) \"ESTADO\", "//9
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = CCC.id_funcionario_cobrador )\"COBRADOR\", "//10
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = CCC.id_funcionario_registro)\"USUARIO\", "//11
                + "(SUM (CCD.MONTO))\"TOTAL\", "//12
                + "C.ENTIDAD "//13
                + "FROM CUENTA_CORRIENTE_CABECERA CCC, CUENTA_CORRIENTE_DETALLE CCD, CLIENTE C "
                + "WHERE CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA "
                + "AND CCC.ID_CLIENTE = C.ID_CLIENTE ";
        if (conFecha) {
            query = query + " AND CCC.FECHA_COBRO BETWEEN ?  AND ? ";
        }
        String groupBy = " GROUP BY CCC.ID_CTA_CTE_CABECERA, CCC.ID_CLIENTE, "
                + "CCC.ID_FUNCIONARIO_COBRADOR, CCC.ID_FUNCIONARIO_REGISTRO, CCC.NRO_RECIBO, "
                + "CCC.ID_ESTADO, CCC.FECHA_COBRO, CCC.FECHA_REGISTRO, C.ENTIDAD ";
        String orderBy = "ORDER BY CCC.ID_CTA_CTE_CABECERA";

        if (idCliente > 0) {
            query = query + " AND CCC.ID_CLIENTE = ? ";
        }
        if (idFuncionario > 0) {
            query = query + " AND CCC.ID_FUNCIONARIO_COBRADOR = ? ";
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
            if (conFecha) {
                pst.setTimestamp(pos, new java.sql.Timestamp(fechaInicio.getTime()));
                pos++;
                pst.setTimestamp(pos, new java.sql.Timestamp(fechaFinal.getTime()));
                pos++;
            }
            if (idCliente > 0) {
                pst.setInt(pos, idCliente);
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
                M_cliente cliente = new M_cliente();
                cliente.setEntidad(rs.getString(13));
                cliente.setIdCliente(rs.getInt(2));
                M_funcionario cobrador = new M_funcionario();
                cobrador.setId_funcionario(rs.getInt(3));
                cobrador.setNombre(rs.getString(10));
                M_funcionario usuario = new M_funcionario();
                usuario.setId_funcionario(rs.getInt(4));
                usuario.setNombre(rs.getString(11));
                Estado estado = new Estado();
                estado.setId(rs.getInt(6));
                estado.setDescripcion(rs.getString(9));
                E_cuentaCorrienteCabecera ccc = new E_cuentaCorrienteCabecera();
                ccc.setId(rs.getInt(1));
                ccc.setFechaPago(rs.getTimestamp(7));
                ccc.setFechaOperacion(rs.getTimestamp(8));
                ccc.setNroRecibo(rs.getInt(5));
                ccc.setCliente(cliente);
                ccc.setFuncionario(usuario);
                ccc.setEstado(estado);
                ccc.setCobrador(cobrador);
                ccc.setDebito(rs.getInt(12));
                list.add(ccc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_cuentaCorrienteDetalle> obtenerCobroDetalle(Integer idCabecera) {
        List<E_cuentaCorrienteDetalle> list = new ArrayList();
        String query = "SELECT "
                + "CCD.ID_CTA_CTE_DETALLE, "//1
                + "CCD.ID_CTA_CTE_CABECERA, "//2
                + "CCD.NRO_FACTURA, "//3
                + "CCD.NRO_RECIBO, "//4
                + "CCD.MONTO, "//5
                + "CCD.NRO_CHEQUE, "//6
                + "CCD.ID_BANCO, "//7
                + "CCD.CHEQUE_FECHA, "//8
                + "CCD.CHEQUE_FECHA_DIFERIDA, "//9
                + "(SELECT B.DESCRIPCION FROM BANCO B WHERE B.ID_BANCO = CCD.ID_BANCO) \"BANCO\", "//10
                + "(SELECT FC.NRO_FACTURA FROM FACTURA_CABECERA FC WHERE FC.ID_FACTURA_CABECERA = CCD.NRO_FACTURA) \"NRO_FACTURA\" "//11
                + "FROM CUENTA_CORRIENTE_DETALLE CCD "
                + "WHERE CCD.ID_CTA_CTE_CABECERA = ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_cuentaCorrienteDetalle detalle = new E_cuentaCorrienteDetalle();
                E_banco banco = new E_banco();
                banco.setId(rs.getInt(7));
                banco.setDescripcion(rs.getString(10));
                detalle.setId(rs.getInt(1));
                detalle.setIdCuentaCorrienteCabecera(rs.getInt(2));
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

    public static List<E_cuentaCorrienteDetalle> obtenerChequesPendientes() {
        List<E_cuentaCorrienteDetalle> list = new ArrayList();
        String query = "SELECT "
                + "CCD.ID_CTA_CTE_DETALLE, "//1
                + "CCD.ID_CTA_CTE_CABECERA, "//2
                + "CCD.ID_FACTURA_CABECERA, "//3
                + "CCD.NRO_RECIBO, "//4
                + "CCD.MONTO, "//5
                + "CCD.NRO_CHEQUE, "//6
                + "CCD.ID_BANCO, "//7
                + "CCD.CHEQUE_FECHA, "//8
                + "CCD.CHEQUE_FECHA_DIFERIDA, "//9
                + "(SELECT B.DESCRIPCION FROM BANCO B WHERE B.ID_BANCO = CCD.ID_BANCO) \"BANCO\", "//10
                + "CCD.ID_ESTADO_CHEQUE, "//11
                + "(SELECT FC.NRO_FACTURA FROM FACTURA_CABECERA FC WHERE FC.ID_FACTURA_CABECERA = CCD.ID_FACTURA_CABECERA) \"NRO_FACTURA\" "//12
                + "FROM CUENTA_CORRIENTE_DETALLE CCD, CUENTA_CORRIENTE_CABECERA CCC "
                + "WHERE cheque_fecha_diferida >= now() "
                + "AND CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA "
                + "AND id_estado_cheque = 2 "
                + "AND CCC.ID_ESTADO = 1 "
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

    public static int guardarCobro(E_cuentaCorrienteCabecera cabecera, ArrayList<E_cuentaCorrienteDetalle> detalle) {
        String INSERT_CABECERA = "INSERT INTO cuenta_corriente_cabecera(id_cliente, id_funcionario_cobrador, "
                + "id_funcionario_registro, nro_recibo, id_estado, fecha_cobro, control, id_caja_y)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE_CHEQUE_DIFERIDO = "INSERT INTO cuenta_corriente_detalle(id_cta_cte_cabecera, id_factura_cabecera, nro_recibo, monto, nro_cheque, id_banco, cheque_fecha, cheque_fecha_diferida, id_estado_cheque, observacion)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_DETALLE_CHEQUE = "INSERT INTO cuenta_corriente_detalle(id_cta_cte_cabecera, id_factura_cabecera, nro_recibo, monto, nro_cheque, id_banco, cheque_fecha, observacion)VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_DETALLE_EFECTIVO = "INSERT INTO cuenta_corriente_detalle(id_cta_cte_cabecera, id_factura_cabecera, nro_recibo, monto, observacion)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getCliente().getIdCliente());
            pst.setInt(2, cabecera.getCobrador().getId_funcionario());
            pst.setInt(3, cabecera.getFuncionario().getId_funcionario());
            pst.setInt(4, cabecera.getNroRecibo());
            pst.setInt(5, Estado.ACTIVO);//ESTADO ACTIVO
            pst.setTimestamp(6, new Timestamp(cabecera.getFechaPago().getTime()));
            pst.setInt(7, 1);//TODO
            pst.setInt(8, 1);//TODO
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (E_cuentaCorrienteDetalle unDetalle : detalle) {
                int idFormaPago = unDetalle.getFormaPago().getId();
                switch (idFormaPago) {
                    case E_formaPago.EFECTIVO: {
                        pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_EFECTIVO);
                        pst.setInt(1, (int) sq_cabecera);
                        /*
                            en caso de que pague el saldo inicial entonces no tendrà numero de factura
                            y debera ser null
                         */
                        if (unDetalle.getIdFacturaCabecera() == 0) {
                            pst.setNull(2, java.sql.Types.INTEGER);
                        } else {
                            pst.setInt(2, unDetalle.getIdFacturaCabecera());
                        }
                        pst.setInt(3, cabecera.getNroRecibo());
                        pst.setInt(4, (int) unDetalle.getMonto());
                        /*
                            en caso de que pague el saldo inicial entonces se comentara en la observacion
                         */
                        if (unDetalle.getIdFacturaCabecera() == 0) {
                            pst.setString(5, "Saldo inicial");
                        } else {
                            pst.setNull(5, java.sql.Types.VARCHAR);
                        }
                        pst.executeUpdate();
                        pst.close();
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        if (unDetalle.esChequeDiferido()) {
                            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_CHEQUE_DIFERIDO);
                            pst.setInt(1, (int) sq_cabecera);
                            /*
                            en caso de que pague el saldo inicial entonces no tendrà numero de factura
                            y debera ser null
                             */
                            if (unDetalle.getIdFacturaCabecera() == 0) {
                                pst.setNull(2, java.sql.Types.INTEGER);
                            } else {
                                pst.setInt(2, unDetalle.getIdFacturaCabecera());
                            }
                            pst.setInt(3, cabecera.getNroRecibo());
                            pst.setInt(4, (int) unDetalle.getMonto());
                            pst.setInt(5, unDetalle.getNroCheque());
                            pst.setInt(6, unDetalle.getBanco().getId());
                            pst.setTimestamp(7, new Timestamp(unDetalle.getFechaCheque().getTime()));
                            pst.setTimestamp(8, new Timestamp(unDetalle.getFechaDiferidaCheque().getTime()));
                            pst.setInt(9, Estado.INACTIVO);
                            /*
                            en caso de que pague el saldo inicial entonces se comentara en la observacion
                             */
                            if (unDetalle.getIdFacturaCabecera() == 0) {
                                pst.setString(10, "Saldo inicial");
                            } else {
                                pst.setNull(10, java.sql.Types.VARCHAR);
                            }
                            pst.executeUpdate();
                            pst.close();
                        } else {
                            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE_CHEQUE);
                            pst.setInt(1, (int) sq_cabecera);
                            /*
                            en caso de que pague el saldo inicial entonces no tendrà numero de factura
                            y debera ser null
                             */
                            if (unDetalle.getIdFacturaCabecera() == 0) {
                                pst.setNull(2, java.sql.Types.INTEGER);
                            } else {
                                pst.setInt(2, unDetalle.getIdFacturaCabecera());
                            }
                            pst.setInt(3, cabecera.getNroRecibo());
                            pst.setInt(4, (int) unDetalle.getMonto());
                            pst.setInt(5, unDetalle.getNroCheque());
                            pst.setInt(6, unDetalle.getBanco().getId());
                            pst.setTimestamp(7, new Timestamp(unDetalle.getFechaCheque().getTime()));
                            /*
                            en caso de que pague el saldo inicial entonces se comentara en la observacion
                             */
                            if (unDetalle.getIdFacturaCabecera() == 0) {
                                pst.setString(8, "Saldo inicial");
                            } else {
                                pst.setNull(8, java.sql.Types.VARCHAR);
                            }
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
                    Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static void anularCobro(int idUtilizacionCabecera, boolean anularNroRecibo) {
        String UPDATE_COBRO_NRO_RECIBO = "UPDATE cuenta_corriente_cabecera SET "
                + "ID_ESTADO = 2, "
                + "NRO_RECIBO = 0 "
                + "WHERE id_cta_cte_cabecera = ?; ";

        String UPDATE_COBRO = "UPDATE cuenta_corriente_cabecera SET "
                + "ID_ESTADO = 2 "
                + "WHERE id_cta_cte_cabecera = ?; ";
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
                    Logger lgr = Logger.getLogger(DB_Cobro.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cobro.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void cobrarCheque(int idCtaCteDetalle) {
        String UPDATE_COBRO = "UPDATE cuenta_corriente_detalle SET "
                + "id_estado_cheque = 1 "
                + "WHERE id_cta_cte_detalle = ?; ";

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
                    Logger lgr = Logger.getLogger(DB_Cobro.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cobro.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int obtenerTotalCobrado(Timestamp fechaInicio, Timestamp fechaFin, int idEstado) {
        Integer totalCobrado = 0;
        String query = "SELECT SUM(CCD.MONTO)\"Total\" "
                + "FROM CUENTA_CORRIENTE_DETALLE CCD, CUENTA_CORRIENTE_CABECERA CCC "
                + "WHERE CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA "
                + "AND CCC.FECHA_COBRO BETWEEN '" + fechaInicio + "'::timestamp  "
                + "AND '" + fechaFin + "'::timestamp "
                + "AND CCC.ID_ESTADO = " + idEstado;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                totalCobrado = rs.getInt("Total");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return totalCobrado;
    }

    public static List<E_cuentaCorrienteCabecera> obtenerMovimientoCobrosCabeceras(int idFuncionario, int idCliente, Date fechaInicio, Date fechaFinal) {
        List<E_cuentaCorrienteCabecera> list = new ArrayList<>();
        String Query = "SELECT CCC.ID_CTA_CTE_CABECERA \"ID\", "
                + "CCC.NRO_RECIBO \"NRO_RECIBO\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"EMPLEADO\", "
                + "(SELECT ENTIDAD FROM CLIENTE C WHERE CCC.ID_CLIENTE= C.ID_CLIENTE) \"CLIENTE\", "
                + "FECHA_COBRO, "
                + "(SELECT SUM (MONTO) FROM CUENTA_CORRIENTE_DETALLE CCD WHERE CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA)\"TOTAL\" "
                + "FROM CUENTA_CORRIENTE_CABECERA CCC ,FUNCIONARIO F, PERSONA P "
                + "WHERE  CCC.FECHA_COBRO BETWEEN ?  "
                + "AND ? "
                + "AND CCC.ID_FUNCIONARIO_REGISTRO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND CCC.ID_ESTADO = 1 "
                + "AND CCC.ID_CTA_CTE_CABECERA NOT IN(select id_movimiento from caja_movimiento where id_movimiento_contable_tipo = 3 AND id_estado = 1)";

        if (idFuncionario > -1) {
            Query = Query + " AND CCC.ID_FUNCIONARIO_REGISTRO = ? ";
        }
        if (idCliente > -1) {
            Query = Query + " AND CCC.ID_CLIENTE = ? ";
        }
        Query = Query + " ORDER BY \"ID\"";
        int pos = 3;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, new java.sql.Timestamp(fechaInicio.getTime()));
            pst.setTimestamp(2, new java.sql.Timestamp(fechaFinal.getTime()));
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (idCliente > 0) {
                pst.setInt(pos, idCliente);
                pos++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_cliente cliente = new M_cliente();
                M_funcionario funcionario = new M_funcionario();
                funcionario.setNombre(rs.getString("EMPLEADO"));
                cliente.setEntidad(rs.getString("CLIENTE"));
                E_cuentaCorrienteCabecera ccc = new E_cuentaCorrienteCabecera();
                ccc.setId(rs.getInt("ID"));
                ccc.setNroRecibo(rs.getInt("NRO_RECIBO"));
                ccc.setDebito(rs.getInt("TOTAL"));
                ccc.setCliente(cliente);
                ccc.setFuncionario(funcionario);
                ccc.setFechaPago(rs.getTimestamp("FECHA_COBRO"));
                list.add(ccc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_cuentaCorrienteCabecera> obtenerMovimientoCobrosCabeceras(int idCaja) {
        List<E_cuentaCorrienteCabecera> list = new ArrayList<>();
        String Query = "SELECT CCC.ID_CTA_CTE_CABECERA \"ID\", "
                + "CCC.NRO_RECIBO \"NRO_RECIBO\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"EMPLEADO\", "
                + "(SELECT ENTIDAD FROM CLIENTE C WHERE CCC.ID_CLIENTE= C.ID_CLIENTE) \"CLIENTE\", "
                + "(SELECT RUC FROM CLIENTE C WHERE CCC.ID_CLIENTE = C.ID_CLIENTE) \"RUC_CLIENTE\", "
                + "(SELECT RUC_IDENTIFICADOR FROM CLIENTE C WHERE CCC.ID_CLIENTE = C.ID_CLIENTE) \"RUC_ID_CLIENTE\", "
                + "FECHA_COBRO, "
                + "(SELECT SUM (MONTO) FROM CUENTA_CORRIENTE_DETALLE CCD WHERE CCC.ID_CTA_CTE_CABECERA = CCD.ID_CTA_CTE_CABECERA)\"TOTAL\" "
                + "FROM CUENTA_CORRIENTE_CABECERA CCC ,FUNCIONARIO F, PERSONA P "
                + "WHERE CCC.ID_FUNCIONARIO_REGISTRO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND CCC.ID_ESTADO = 1 "
                + "AND CCC.ID_CTA_CTE_CABECERA IN(select id_movimiento from caja_movimiento where id_movimiento_contable_tipo = 3 AND id_caja = ?)";

        Query = Query + " ORDER BY \"ID\"";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_cliente cliente = new M_cliente();
                M_funcionario funcionario = new M_funcionario();
                funcionario.setNombre(rs.getString("EMPLEADO"));
                cliente.setEntidad(rs.getString("CLIENTE"));
                cliente.setRuc(rs.getString("RUC_CLIENTE"));
                cliente.setRucId(rs.getString("RUC_ID_CLIENTE"));
                E_cuentaCorrienteCabecera ccc = new E_cuentaCorrienteCabecera();
                ccc.setId(rs.getInt("ID"));
                ccc.setNroRecibo(rs.getInt("NRO_RECIBO"));
                ccc.setDebito(rs.getInt("TOTAL"));
                ccc.setCliente(cliente);
                ccc.setFuncionario(funcionario);
                ccc.setFechaPago(rs.getTimestamp("FECHA_COBRO"));
                list.add(ccc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

}
