/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_producto;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class DB_Producto {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static ResultSetTableModel consultarProducto(String query) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String estado = "(SELECT ESTA_DESCRIPCION FROM ESTADO WHERE ESTA_ID_ESTADO = PROD_ID_ESTADO)\"Estado\"";
            String impuesto = "(SELECT IMPU_DESCRIPCION FROM IMPUESTO WHERE IMPU_ID_IMPUESTO = PROD_IMPUESTO)\"Impuesto\"";
            String q2 = "SELECT PROD_ID_PRODUCTO \"ID\", "
                    + "PROD_DESCRIPCION \"Descripción\", "
                    + "PROD_CODIGO \"Código\", "
                    + "PROD_MARCA \"Marca\", "
                    + impuesto + ", "
                    + "PROD_RUBRO \"Rubro\", "
                    + "PROD_PRECIO_COSTO \"Precio costo\", "
                    + "PROD_PRECIO_MINORISTA \"Precio minorista\", "
                    + "PROD_PRECIO_MAYORISTA \"Precio mayorista\", "
                    + estado + ", "
                    + "PROD_CANT_ACTUAL \"Cant. actual\" "
                    + "FROM PRODUCTO "
                    + "WHERE LOWER(prod_DESCRIPCION) LIKE '" + query + "%' "
                    + "ORDER BY PROD_DESCRIPCION";
            //SELECT prod_id_producto   "ID producto"  ,  prod_descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(q2);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel consultarProducto(String descripcion, String proveedor, String marca, String rubro, String impuesto, String estado, String busqueda) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String finalQuery = "ORDER BY PROD_DESCRIPCION";
            String fromQuery = "FROM PRODUCTO ";
            String prov;
            if ("Todos".equals(proveedor)) {
                prov = "";
            } else {
                fromQuery = "FROM PROVEEDOR, PRODUCTO, PROVEEDOR_PRODUCTO ";
                prov = "PRPR_ID_PROVEEDOR = PROV_ID_PROVEEDOR AND PRPR_ID_PRODUCTO = PROD_ID_PRODUCTO "
                        + "AND PROV_ENTIDAD LIKE'" + proveedor + "' AND ";
            }

            String imp;
            if ("Todos".equals(impuesto)) {
                imp = "";
            } else {
                imp = "AND prod_impuesto =(SELECT IMPU_ID_IMPUESTO FROM IMPUESTO WHERE IMPU_DESCRIPCION = " + impuesto + ")";
            }

            String marc;
            if ("Todos".equals(marca)) {
                marc = "";
            } else {
                marc = "AND PROD_MARCA LIKE '" + marca + "' ";
            }

            String rubr;
            if ("Todos".equals(rubro)) {
                rubr = "";
            } else {
                rubr = "AND PROD_RUBRO LIKE '" + rubro + "'  ";
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD_ID_ESTADO = (SELECT ESTA_ID_ESTADO FROM ESTADO WHERE ESTA_DESCRIPCION LIKE '" + estado + "')";
            }
            String busqueda_;
            if ("Exclusiva".equals(busqueda)) {
                busqueda_ = descripcion + "%";
            } else {
                busqueda_ = "%" + descripcion + "%";
            }

            String queryEstado = "(SELECT ESTA_DESCRIPCION FROM ESTADO WHERE ESTA_ID_ESTADO = PROD_ID_ESTADO)\"Estado\"";
            String queryImpuesto = "(SELECT IMPU_DESCRIPCION FROM IMPUESTO WHERE IMPU_ID_IMPUESTO = PROD_IMPUESTO)\"Impuesto\"";
            String q2 = "SELECT PROD_ID_PRODUCTO \"ID\", "
                    + "PROD_DESCRIPCION \"Descripción\", "
                    + "PROD_CODIGO \"Código\", "
                    + "PROD_MARCA \"Marca\", "
                    + queryImpuesto + ", "
                    + "PROD_RUBRO \"Rubro\", "
                    + "PROD_PRECIO_COSTO \"Precio costo\", "
                    + "PROD_PRECIO_MINORISTA \"Precio minorista\", "
                    + "PROD_PRECIO_MAYORISTA \"Precio mayorista\", "
                    + queryEstado + ", "
                    + "PROD_CANT_ACTUAL \"Cant. actual\" "
                    + fromQuery
                    + "WHERE "
                    + prov
                    + "LOWER(prod_DESCRIPCION) LIKE '" + busqueda_ + "' "
                    + marc
                    + imp
                    + rubr
                    + estad
                    + finalQuery;
            //SELECT prod_id_producto   "ID producto"  ,  prod_descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(q2);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel consultaSimpleProducto(String query) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String q2 = "SELECT PROD_ID_PRODUCTO \"ID\", "
                    + "PROD_DESCRIPCION \"Descripción\" "
                    + "FROM PRODUCTO "
                    + "WHERE LOWER(prod_DESCRIPCION) LIKE '" + query + "%' "
                    + "ORDER BY PROD_DESCRIPCION";
            //SELECT prod_id_producto   "ID producto"  ,  prod_descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(q2);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel consultaSimpleProducto(String descripcion, String proveedor, String marca, String rubro, String impuesto, String estado) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String finalQuery = "ORDER BY PROD_DESCRIPCION";
            String fromQuery = "FROM PRODUCTO ";
            String prov;
            if ("Todos".equals(proveedor)) {
                prov = "";
            } else {
                fromQuery = "FROM PROVEEDOR, PRODUCTO, PROVEEDOR_PRODUCTO ";
                prov = "PRPR_ID_PROVEEDOR = PROV_ID_PROVEEDOR AND PRPR_ID_PRODUCTO = PROD_ID_PRODUCTO "
                        + "AND PROV_ENTIDAD LIKE'" + proveedor + "' AND ";
            }

            String imp;
            if ("Todos".equals(impuesto)) {
                imp = "";
            } else {
                imp = "AND prod_impuesto =(SELECT IMPU_ID_IMPUESTO FROM IMPUESTO WHERE IMPU_DESCRIPCION = " + impuesto + ")";
            }

            String marc;
            if ("Todos".equals(marca)) {
                marc = "";
            } else {
                marc = "AND PROD_MARCA LIKE '" + marca + "' ";
            }

            String rubr;
            if ("Todos".equals(rubro)) {
                rubr = "";
            } else {
                rubr = "AND PROD_RUBRO LIKE '" + rubro + "'  ";
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD_ESTADO = (SELECT ESTA_ID_ESTADO FROM ESTADO WHERE ESTA_DESCRIPCION LIKE '" + estado + "')";
            }

            String Query = "SELECT PROD_ID_PRODUCTO \"ID\", "
                    + "PROD_DESCRIPCION \"Descripción\" "
                    + fromQuery
                    + "WHERE "
                    + prov
                    + "LOWER(prod_DESCRIPCION) LIKE '" + descripcion + "%' "
                    + marc
                    + imp
                    + rubr
                    + estad
                    + finalQuery;
            //SELECT prod_id_producto   "ID producto"  ,  prod_descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static M_producto obtenerDatosProductoID(int idProducto) {
        M_producto producto = null;
        try {
            CallableStatement storedFunct = DB_manager.getConection().prepareCall("{ call consultarproducto(?) }");
            storedFunct.setInt(1, idProducto);
            rs = storedFunct.executeQuery();
            while (rs.next()) {
                producto = new M_producto();
                producto.setCantActual(rs.getDouble("cant_actual"));
                producto.setCodBarra(rs.getInt("codigo"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getString("estado"));
                producto.setId(rs.getInt("id_producto"));
                producto.setImpuesto(rs.getInt("impuesto"));
                producto.setMarca(rs.getString("marca"));
                producto.setPrecioCosto(rs.getInt("precio_costo"));
                producto.setPrecioMayorista(rs.getInt("precio_mayorista"));
                producto.setPrecioVenta(rs.getInt("precio_minorista"));
                producto.setRubro(rs.getString("rubro"));
            }
            storedFunct.close();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return producto;
    }

    public static long insertarProducto(M_producto prod) {
        long id_producto = -1L;
        //parche
        if (prod.getImpuesto().equals(0)) {
            prod.setImpuesto(1);
        } else if (prod.getImpuesto().equals(5)) {
            prod.setImpuesto(2);
        } else {
            prod.setImpuesto(3);
        }
        int prod_id_estado = Integer.valueOf(prod.getEstado());
        String stm = "INSERT INTO PRODUCTO("
                + "PROD_DESCRIPCION, "
                + "PROD_CODIGO, "
                + "PROD_MARCA, "
                + "PROD_IMPUESTO, "
                + "PROD_RUBRO, "
                + "PROD_PRECIO_COSTO, "
                + "PROD_PRECIO_MINORISTA, "
                + "PROD_PRECIO_MAYORISTA, "
                + "PROD_ID_ESTADO, "
                + "PROD_CANT_ACTUAL)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = DB_manager.getConection().prepareStatement(stm, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, prod.getDescripcion());
            try {
                if (prod.getCodBarra() == null) {
                    pst.setNull(2, Types.NUMERIC);
                } else {
                    pst.setInt(2, prod.getCodBarra());
                }
            } catch (Exception e) {
                pst.setNull(2, Types.NUMERIC);
            }
            try {
                if (prod.getMarca() == null) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, prod.getMarca());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            pst.setInt(4, prod.getImpuesto());
            pst.setString(5, prod.getRubro());
            pst.setInt(6, prod.getPrecioCosto());
            pst.setInt(7, prod.getPrecioVenta());
            pst.setInt(8, prod.getPrecioMayorista());
            pst.setInt(9, prod_id_estado);
            try {
                if (prod.getCantActual() == null) {
                    pst.setNull(10, Types.DOUBLE);
                } else {
                    pst.setDouble(10, prod.getCantActual());
                }
            } catch (Exception e) {
                pst.setNull(10, Types.DOUBLE);
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_producto = rs.getLong(1);
            }
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
        return id_producto;
    }

    public static void modificarProducto(M_producto producto) {
        System.out.println("actualizarProducto: " + producto.getId());
        String imp = "(SELECT IMPU_ID_IMPUESTO FROM IMPUESTO WHERE IMPU_DESCRIPCION = " + producto.getImpuesto() + ")";
        String query = "UPDATE  producto "
                + "SET prod_codigo = " + producto.getCodBarra() + " ,  "
                + " prod_marca = '" + producto.getMarca() + "' , "
                + " prod_impuesto = " + imp + " , "
                + " prod_rubro = '" + producto.getRubro() + "' , "
                + " prod_precio_costo = " + producto.getPrecioCosto() + " , "
                + " prod_precio_mayorista = " + producto.getPrecioMayorista() + " , "
                + " prod_precio_minorista = " + producto.getPrecioVenta() + " , "
                + " PROD_CANT_ACTUAL = " + producto.getCantActual() + "  "
                + " WHERE prod_id_producto = " + producto.getId() + "";
        try {
            DB_manager.getConection().setAutoCommit(false);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.getConection().commit();
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
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void sumarStock(ArrayList<Integer> id, ArrayList<Double> cantidad) {
        try {
            DB_manager.habilitarTransaccionManual();
            for (int i = 0; i < id.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "PROD_CANT_ACTUAL = "
                        + "((SELECT PROD_CANT_ACTUAL FROM PRODUCTO WHERE PROD_ID_PRODUCTO = " + id.get(i).toString() + ")+" + cantidad.get(i).toString() + ") "
                        + "WHERE PROD_ID_PRODUCTO =" + id.get(i).toString();
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
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void insertarCodigoProducto(long id_producto) {
        String query = "UPDATE  producto "
                + "SET prod_codigo = " + id_producto
                + " WHERE prod_id_producto = " + id_producto + "";
        try {
            DB_manager.getConection().setAutoCommit(false);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.getConection().commit();
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
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static boolean existeProducto(String prodDescripcion) {
        String Query = "SELECT PROD_DESCRIPCION FROM PRODUCTO "
                + "WHERE PROD_DESCRIPCION LIKE '" + prodDescripcion + "'";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            if (rs.next()) {
                return true;
            }else{
                return false;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }
}
