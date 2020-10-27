/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_ingresoPamela {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    /*
     * INSERT
     */
    public static int insertarIngreso(M_facturaCabecera cabecera, ArrayList<M_facturaDetalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION, ID_IMPUESTO)VALUES (?, ?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA, NRO_FACTURA, ID_TIMBRADO)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getIdFuncionario());
            pst.setInt(2, cabecera.getIdCliente());
            pst.setInt(3, cabecera.getIdCondVenta());
            try {
                if (cabecera.getNroFactura() == null) {
                    pst.setNull(4, Types.BIGINT);
                } else {
                    pst.setInt(4, cabecera.getNroFactura());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.BIGINT);
            }
            pst.setInt(5, cabecera.getIdTimbrado());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < detalle.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, detalle.get(i).getIdProducto());
                pst.setDouble(3, detalle.get(i).getCantidad());
                pst.setDouble(4, detalle.get(i).getPrecio());
                pst.setDouble(5, detalle.get(i).getDescuento());
                try {
                    if (detalle.get(i).getObservacion() == null) {
                        pst.setNull(6, Types.VARCHAR);
                    } else if (detalle.get(i).getObservacion().trim().isEmpty()) {
                        pst.setNull(6, Types.VARCHAR);
                    } else {
                        pst.setString(6, detalle.get(i).getObservacion());
                    }
                } catch (Exception e) {
                    pst.setNull(6, Types.VARCHAR);
                }
                pst.setInt(7, detalle.get(i).getProducto().getIdImpuesto());
                pst.executeUpdate();
                pst.close();
            }
            //se resta del stock lo que se vende
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")-" + detalle.get(i).getCantidad() + ") "
                        + "WHERE ID_PRODUCTO =" + detalle.get(i).getProducto().getId();
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
                    Logger lgr = Logger.getLogger(DB_ingresoPamela.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_ingresoPamela.class.getName());
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
                Logger lgr = Logger.getLogger(DB_ingresoPamela.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    /*
     * READ
     */
    public static List<M_facturaDetalle> consultarIngresoDetalleAgrupado(List<M_facturaCabecera> cadenaCabeceras) {
        List<M_facturaDetalle> list = new ArrayList<>();
        boolean b = true;
        List<M_facturaCabecera> possibleValues = cadenaCabeceras;
        StringBuilder builder = new StringBuilder();

        for (M_facturaCabecera seleccionVenta : possibleValues) {
            builder.append("?,");
        }
        String QUERY = "SELECT PROD.CODIGO \"Codigo\", "
                + "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = FADE.ID_IMPUESTO)\"IMPUESTO\","
                + "PROD.DESCRIPCION \"PRODUCTO\", "
                + "SUM(FADE.CANTIDAD) \"CANTIDAD\", "
                + "FADE.ID_IMPUESTO \"ID_IMPUESTO\","
                + "FADE.PRECIO \"PRECIO\", "
                + "FADE.DESCUENTO \"DESCUENTO\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA, PRODUCTO PROD "
                + "WHERE FADE.ID_FACTURA_CABECERA = FACA.ID_FACTURA_CABECERA "
                + "AND FADE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND FACA.ID_FACTURA_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY PROD.DESCRIPCION, PROD.CODIGO, PROD.ID_IMPUESTO, FADE.PRECIO, FADE.DESCUENTO, FADE.ID_IMPUESTO  "
                + "ORDER BY PROD.DESCRIPCION";
        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (M_facturaCabecera ventaCabecera : possibleValues) {
                pst.setInt(index++, ventaCabecera.getIdFacturaCabecera());
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_facturaDetalle fade = new M_facturaDetalle();
                M_producto producto = new M_producto();
                producto.setCodBarra(rs.getString("Codigo"));
                producto.setDescripcion(rs.getString("Producto"));
                producto.setImpuesto(rs.getInt("IMPUESTO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                fade.setProducto(producto);
                fade.setCantidad(rs.getDouble("Cantidad"));
                fade.setPrecio(rs.getDouble("Precio"));
                fade.setDescuento(rs.getDouble("Descuento"));
                fade.setObservacion("");
                list.add(fade);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_ingresoPamela.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<M_facturaDetalle> obtenerIngresoDetalle(int idIngresoCabecera) {
        List<M_facturaDetalle> list = new ArrayList<>();
        String QUERY = "SELECT "
                + "FD.ID_PRODUCTO \"ID_PRODUCTO\", "
                + "P.CODIGO \"CODIGO\", "
                + "P.DESCRIPCION \"PRODUCTO\", "
                + "FD.CANTIDAD \"CANTIDAD\", "
                + "FD.ID_IMPUESTO \"ID_IMPUESTO\", "
                + "FD.PRECIO \"PRECIO\", "
                + "FD.DESCUENTO \"DESCUENTO\","
                + "FD.OBSERVACION \"OBSERVACION\" "
                + "FROM FACTURA_DETALLE FD, PRODUCTO P "
                + "WHERE FD.ID_PRODUCTO = P.ID_PRODUCTO "
                + "AND ID_FACTURA_CABECERA =  ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idIngresoCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_facturaDetalle fade = new M_facturaDetalle();
                M_producto producto = new M_producto();
                producto.setCodBarra(rs.getString("CODIGO"));
                producto.setDescripcion(rs.getString("Producto"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                fade.setProducto(producto);
                fade.setCantidad(rs.getDouble("Cantidad"));
                fade.setPrecio(rs.getDouble("Precio"));
                fade.setDescuento(rs.getDouble("Descuento"));
                fade.setObservacion(rs.getString("OBSERVACION"));
                list.add(fade);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_ingresoPamela.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
