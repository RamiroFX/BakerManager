/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import Entities.E_formaPago;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_producto;
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
public class DB_NotaCredito {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static List<E_NotaCreditoCabecera> obtenerNotasCredito(int idCliente,
            int idFuncionario, int nroNotaCredito, Date fechaInicio, Date fechaFinal,
            int idTipoOperacion, int idEstado) {
        List<E_NotaCreditoCabecera> list = new ArrayList<>();
        String query = "SELECT "
                + "nc.id_nota_credito_cabecera, "
                + "nc.id_factura_cabecera, "
                + "nro_nota_credito, "
                + "fc.nro_factura, "
                + "c.entidad, "
                + "(SELECT nombre ||' '||apellido FROM persona pers WHERE pers.id_persona = f.id_persona) \"FUNCIONARIO\", "
                + "nc.tiempo, "
                + "fc.id_cond_venta, "
                + "(SELECT descripcion FROM tipo_operacion tiop WHERE tiop.id_tipo_operacion = fc.id_cond_venta) \"COND_VENTA\", "
                + "nc.id_estado, "
                + "(SELECT descripcion FROM estado esta WHERE esta.id_estado = nc.id_estado) \"ESTADO\", "
                + "SUM (nd.CANTIDAD*(nd.PRECIO-(nd.PRECIO*nd.DESCUENTO)/100)) \"TOTAL\" "
                + "FROM  "
                + "nota_credito_cabecera nc, "
                + "nota_credito_detalle nd, "
                + "factura_cabecera fc, "
                + "funcionario f, "
                + "cliente c "
                + "WHERE "
                + "nc.id_factura_cabecera = fc.id_factura_cabecera AND "
                + "nc.id_nota_credito_cabecera = nd.id_nota_credito_cabecera AND "
                + "nc.id_funcionario = f.id_funcionario AND "
                + "nc.id_cliente = c.id_cliente AND "
                + "nc.tiempo BETWEEN ?  AND ? ";
        String orderBy = "ORDER BY nc.tiempo";
        String groupBy = "GROUP BY nc.id_nota_credito_cabecera, nro_nota_credito, nro_factura, c.entidad, \"FUNCIONARIO\", nc.tiempo, fc.id_cond_venta, \"COND_VENTA\", fc.id_estado,\"ESTADO\" ";
        if (idCliente > 0) {
            query = query + " AND nc.ID_CLIENTE = ? ";
        }
        if (idFuncionario > 0) {
            query = query + " AND nc.ID_FUNCIONARIO = ? ";
        }
        if (idTipoOperacion > 0) {
            query = query + " AND fc.ID_COND_VENTA = ? ";
        }
        if (nroNotaCredito > 0) {
            query = query + " AND nc.nro_nota_credito = ? ";
        }
        if (idEstado > 0) {
            query = query + " AND nc.id_estado = ? ";
        }
        query = query + groupBy + orderBy;
        int pos = 3;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, new java.sql.Timestamp(fechaInicio.getTime()));
            pst.setTimestamp(2, new java.sql.Timestamp(fechaFinal.getTime()));
            if (idCliente > 0) {
                pst.setInt(pos, idCliente);
                pos++;
            }
            if (idFuncionario > 0) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (idTipoOperacion > 0) {
                pst.setInt(pos, idTipoOperacion);
                pos++;
            }
            if (nroNotaCredito > 0) {
                pst.setInt(pos, nroNotaCredito);
                pos++;
            }
            if (idEstado > 0) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_cliente cliente = new M_cliente();
                cliente.setEntidad(rs.getString("entidad"));
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString("FUNCIONARIO"));
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("id_cond_venta"));
                tiop.setDescripcion(rs.getString("COND_VENTA"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_facturaCabecera facturaCabecera = new E_facturaCabecera();
                facturaCabecera.setIdFacturaCabecera(rs.getInt("id_factura_cabecera"));
                facturaCabecera.setNroFactura(rs.getInt("nro_factura"));
                facturaCabecera.setTipoOperacion(tiop);
                E_NotaCreditoCabecera nc = new E_NotaCreditoCabecera();
                nc.setId(rs.getInt("id_nota_credito_cabecera"));
                nc.setTiempo(rs.getTimestamp("tiempo"));
                nc.setFacturaCabecera(facturaCabecera);
                nc.setNroNotaCredito(rs.getInt("nro_nota_credito"));
                nc.setCliente(cliente);
                nc.setFuncionario(f);
                nc.setTotal(rs.getInt("TOTAL"));
                nc.setEstado(estado);
                list.add(nc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_NotaCreditoDetalle> obtenerNotasCreditoDetalle(int idNotaCreditoCabecera) {
        List<E_NotaCreditoDetalle> detalles = null;
        String QUERY = "SELECT ND.ID_NOTA_CREDITO_DETALLE, "
                + "ND.ID_FACTURA_DETALLE, "
                + "ND.ID_NOTA_CREDITO_CABECERA, "
                + "ND.ID_PRODUCTO,"
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = ND.ID_PRODUCTO)\"PRODUCTO\", "
                + "(SELECT P.ID_IMPUESTO FROM PRODUCTO P WHERE P.ID_PRODUCTO = ND.ID_PRODUCTO)\"ID_IMPUESTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = ND.ID_PRODUCTO)\"CODIGO_PROD\", "
                + "ND.CANTIDAD, "
                + "ND.PRECIO, "
                + "ND.DESCUENTO "
                + "FROM NOTA_CREDITO_DETALLE ND, FACTURA_DETALLE FD "
                + "WHERE ND.ID_NOTA_CREDITO_CABECERA = ? "
                + "AND ND.ID_FACTURA_DETALLE = FD.ID_FACTURA_DETALLE;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idNotaCreditoCabecera);
            rs = pst.executeQuery();
            detalles = new ArrayList<>();
            while (rs.next()) {
                E_facturaDetalle fd = new E_facturaDetalle();
                fd.setIdFacturaDetalle(rs.getInt("ID_FACTURA_DETALLE"));
                E_NotaCreditoDetalle notaCreditoDetalle = new E_NotaCreditoDetalle();
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO_PROD"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                notaCreditoDetalle.setId(rs.getInt("ID_NOTA_CREDITO_DETALLE"));
                notaCreditoDetalle.setFacturaDetalle(fd);
                notaCreditoDetalle.setProducto(producto);
                notaCreditoDetalle.setCantidad(rs.getDouble("CANTIDAD"));
                notaCreditoDetalle.setPrecio(rs.getInt("PRECIO"));
                notaCreditoDetalle.setDescuento(rs.getDouble("DESCUENTO"));
                notaCreditoDetalle.setObservacion("");
                detalles.add(notaCreditoDetalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return detalles;
    }

    public static E_NotaCreditoDetalle obtenerNotaCreditoDetalle(int idFacturaDetalle) {
        E_NotaCreditoDetalle nd = null;
        String query = "SELECT "
                + "nd.id_factura_detalle,  "
                + "SUM(nd.cantidad) \"CANTIDAD\", nd.id_producto, nd.precio, nd.descuento, "
                + "p.descripcion, p.codigo, p.id_producto "
                + "FROM nota_credito_cabecera nc, nota_credito_detalle nd, producto p "
                + "WHERE nd.id_producto = p.id_producto "
                + "AND nd.id_factura_detalle = ? "
                + "AND nd.id_nota_credito_cabecera = nc.id_nota_credito_cabecera "
                + "AND nc.id_estado = 1 "
                + "GROUP BY nd.id_factura_detalle, "
                + "nd.id_producto, nd.precio, nd.descuento, p.descripcion, p.codigo, p.id_producto;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idFacturaDetalle);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaDetalle fd = new E_facturaDetalle();
                fd.setIdFacturaDetalle(rs.getInt("id_factura_detalle"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setCodigo(rs.getString("codigo"));
                nd = new E_NotaCreditoDetalle();
                nd.setFacturaDetalle(fd);
                nd.setCantidad(rs.getDouble("CANTIDAD"));
                nd.setDescuento(rs.getDouble("descuento"));
                nd.setProducto(producto);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return nd;
    }

    public static boolean existeNroNotaCredito(int nroNotaCredito) {
        String Query = "SELECT nro_nota_credito  FROM nota_credito_cabecera WHERE nro_nota_credito = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, nroNotaCredito);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static int guardarNotaCredito(E_NotaCreditoCabecera cabecera, ArrayList<E_NotaCreditoDetalle> listaDetalles) {
        String INSERT_CABECERA = "INSERT INTO nota_credito_cabecera "
                + "(nro_nota_credito, id_factura_cabecera, id_cliente, id_funcionario, tiempo, id_estado) VALUES"
                + "(?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO nota_credito_detalle "
                + "(id_nota_credito_cabecera, id_factura_detalle, id_producto, cantidad, monto, precio, descuento) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getNroNotaCredito());
            pst.setInt(2, cabecera.getFacturaCabecera().getIdFacturaCabecera());
            pst.setInt(3, cabecera.getCliente().getIdCliente());
            pst.setInt(4, cabecera.getFuncionario().getId_funcionario());
            pst.setTimestamp(5, new Timestamp(cabecera.getTiempo().getTime()));
            pst.setInt(6, Estado.ACTIVO);//ESTADO ACTIVO
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (E_NotaCreditoDetalle unDetalle : listaDetalles) {
                //id_nota_credito_cabecera, id_factura_detalle, id_producto, cantidad, monto, precio, descuento
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, unDetalle.getFacturaDetalle().getIdFacturaDetalle());
                pst.setInt(3, unDetalle.getProducto().getId());
                pst.setDouble(4, unDetalle.getCantidad());
                pst.setInt(5, unDetalle.getSubTotal());
                pst.setInt(6, unDetalle.getPrecio());
                pst.setDouble(7, unDetalle.getDescuento());
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
                    Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static void anularNotaCredito(int idCabecera, int idEstado, boolean recuperarNroNotaCredito) {
        String UPDATE_NOTACREDITO = "UPDATE NOTA_CREDITO_CABECERA SET ID_ESTADO = ? WHERE ID_NOTA_CREDITO_CABECERA = ?";
        if (recuperarNroNotaCredito) {
            UPDATE_NOTACREDITO = "UPDATE NOTA_CREDITO_CABECERA SET ID_ESTADO = ?, NRO_NOTA_CREDITO = 0 WHERE ID_NOTA_CREDITO_CABECERA = ?";
        }
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_NOTACREDITO);
            pst.setInt(1, idEstado);
            pst.setInt(2, idCabecera);
            pst.executeUpdate();
            pst.close();
            //se devuelve al stock lo que se anuló
            /*ArrayList<E_NotaCreditoDetalle> detalle = new ArrayList<>(obtenerNotasCreditoDetalle(idCabecera));
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")-" + detalle.get(i).getCantidad() + ") "
                        + "WHERE ID_PRODUCTO =" + detalle.get(i).getProducto().getId();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }*/
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Egreso.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Egreso.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
