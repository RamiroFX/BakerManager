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
            String categoria = "(SELECT PRCA.DESCRIPCION FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PRODUCTO_CATEGORIA = PROD.ID_CATEGORIA)\"Categoria\", ";
            String marca = "(SELECT MARC.DESCRIPCION FROM MARCA MARC WHERE MARC.ID_MARCA = PROD.ID_MARCA)\"Marca\", ";
            String estado = "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PROD.ID_ESTADO)\"Estado\"";
            String impuesto = "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = PROD.ID_IMPUESTO)\"Impuesto\", ";
            String q2 = "SELECT PROD.ID_PRODUCTO \"ID\", "
                    + "PROD.DESCRIPCION \"Descripción\", "
                    + "PROD.CODIGO \"Código\", "
                    + marca
                    + impuesto
                    + categoria
                    + "PROD.PRECIO_COSTO \"Precio costo\", "
                    + "PROD.PRECIO_MINORISTA \"Precio minorista\", "
                    + "PROD.PRECIO_MAYORISTA \"Precio mayorista\", "
                    + estado + ", "
                    + "PROD.CANT_ACTUAL \"Cant. actual\" "
                    + "FROM PRODUCTO PROD "
                    + "WHERE LOWER(PROD.DESCRIPCION) LIKE '" + query + "%' "
                    + "ORDER BY PROD.DESCRIPCION";
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
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

    public static ResultSetTableModel consultarProducto(String descripcion, String proveedor, String marca, String categoria, String impuesto, String estado, String busqueda) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String finalQuery = "ORDER BY PROD.DESCRIPCION";
            String fromQuery = "FROM PRODUCTO PROD, MARCA MARC, PRODUCTO_CATEGORIA PRCA, ESTADO ESTA, IMPUESTO IMPU ";
            String prov;
            if ("Todos".equals(proveedor)) {
                prov = "";
            } else {
                fromQuery = fromQuery + ", PROVEEDOR PROV, PROVEEDOR_PRODUCTO PRPR ";
                prov = "AND PRPR.ID_PROVEEDOR = PROV.ID_PROVEEDOR AND PRPR.ID_PRODUCTO = PROD.ID_PRODUCTO "
                        + "AND PROV.ENTIDAD LIKE'" + proveedor + "' ";
            }

            String imp;
            if ("Todos".equals(impuesto)) {
                imp = "";
            } else {
                imp = "AND PROD.ID_IMPUESTO =(SELECT IMP.ID_IMPUESTO FROM IMPUESTO IMP WHERE IMP.DESCRIPCION = " + impuesto + ")";
            }

            String marc;
            if ("Todos".equals(marca)) {
                marc = "";
            } else {
                marc = "AND PROD.ID_MARCA =(SELECT M.ID_MARCA FROM MARCA M WHERE M.DESCRIPCION LIKE '" + marca + "')";
            }

            String categ;
            if ("Todos".equals(categoria)) {
                categ = "";
            } else {
                categ = "AND PROD.ID_CATEGORIA =(SELECT C.ID_PRODUCTO_CATEGORIA FROM PRODUCTO_CATEGORIA C WHERE C.DESCRIPCION LIKE '" + categoria + "')";
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD.ID_ESTADO = (SELECT EST.ID_ESTADO FROM ESTADO EST WHERE EST.DESCRIPCION LIKE '" + estado + "')";
            }
            String busqueda_;
            if ("Exclusiva".equals(busqueda)) {
                busqueda_ = descripcion + "%";
            } else {
                busqueda_ = "%" + descripcion + "%";
            }
            String FINAL_QUERY = "SELECT PROD.ID_PRODUCTO \"ID\", "
                    + "PROD.DESCRIPCION \"Descripción\", "
                    + "PROD.CODIGO \"Código\", "
                    + "MARC.DESCRIPCION \"Marca\", "
                    + "IMPU.DESCRIPCION \"Impuesto\", "
                    + "PRCA.DESCRIPCION \"Categoría\", "
                    + "PROD.PRECIO_COSTO \"Precio costo\", "
                    + "PROD.PRECIO_MINORISTA \"Precio minorista\", "
                    + "PROD.PRECIO_MAYORISTA \"Precio mayorista\", "
                    + "ESTA.DESCRIPCION \"Estado\", "
                    + "PROD.CANT_ACTUAL \"Cant. actual\" "
                    + fromQuery
                    + "WHERE PROD.ID_MARCA = MARC.ID_MARCA "
                    + "AND PROD.ID_IMPUESTO = IMPU.ID_IMPUESTO "
                    + "AND PROD.ID_CATEGORIA = PRCA.ID_PRODUCTO_CATEGORIA "
                    + "AND PROD.ID_ESTADO = ESTA.ID_ESTADO "
                    + prov
                    + "AND LOWER(PROD.DESCRIPCION) LIKE '" + busqueda_ + "' "
                    + marc
                    + imp
                    + categ
                    + estad
                    + finalQuery;
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(FINAL_QUERY);
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
            String q2 = "SELECT PROD.ID_PRODUCTO \"ID\", "
                    + "PROD.DESCRIPCION \"Descripción\" "
                    + "FROM PRODUCTO PROD "
                    + "WHERE LOWER(PROD.DESCRIPCION) LIKE '" + query + "%' "
                    + "ORDER BY PROD.DESCRIPCION";
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
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
            String finalQuery = "ORDER BY PROD.DESCRIPCION ";
            String fromQuery = "FROM PRODUCTO PROD ";
            String prov;
            if ("Todos".equals(proveedor)) {
                prov = "";
            } else {
                fromQuery = "FROM PROVEEDOR PROV, PRODUCTO PROD, PROVEEDOR_PRODUCTO PRPR ";
                prov = "PRPR.ID_PROVEEDOR = PROV.ID_PROVEEDOR AND PRPR.ID_PRODUCTO = PROD.ID_PRODUCTO "
                        + "AND PROV.ENTIDAD LIKE'" + proveedor + "' AND ";
            }

            String imp;
            if ("Todos".equals(impuesto)) {
                imp = "";
            } else {
                imp = "AND PROD.ID_IMPUESTO =(SELECT IMPU.ID_IMPUESTO FROM IMPUESTO IMPU WHERE IMPU.DESCRIPCION = " + impuesto + ") ";
            }

            String marc;
            if ("Todos".equals(marca)) {
                marc = "";
            } else {
                marc = "AND PROD.ID_MARCA LIKE '" + marca + "' ";
            }

            String rubr;
            if ("Todos".equals(rubro)) {
                rubr = "";
            } else {
                rubr = "AND PROD.ID_CATEGORIA LIKE '" + rubro + "'  ";
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD.ESTADO = (SELECT ESTA.ID_ESTADO FROM ESTADO ESTA WHERE ESTA.DESCRIPCION LIKE '" + estado + "') ";
            }

            String Query = "SELECT PROD.ID_PRODUCTO \"ID\", "
                    + "PROD.DESCRIPCION \"Descripción\" "
                    + fromQuery
                    + "WHERE "
                    + prov
                    + "LOWER(PROD.DESCRIPCION) LIKE '" + descripcion + "%' "
                    + marc
                    + imp
                    + rubr
                    + estad
                    + finalQuery;
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
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
                producto.setCodBarra(rs.getString("codigo"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getString("estado"));
                producto.setId(rs.getInt("id_producto"));
                producto.setImpuesto(rs.getInt("impuesto"));
                producto.setMarca(rs.getString("marca"));
                producto.setPrecioCosto(rs.getInt("precio_costo"));
                producto.setPrecioMayorista(rs.getInt("precio_mayorista"));
                producto.setPrecioVenta(rs.getInt("precio_minorista"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setIdEstado(rs.getInt("id_estado"));
                producto.setIdImpuesto(rs.getInt("id_impuesto"));
                producto.setIdMarca(rs.getInt("id_marca"));
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
        String stm = "INSERT INTO PRODUCTO("
                + "DESCRIPCION, "
                + "CODIGO, "
                + "ID_MARCA, "
                + "ID_IMPUESTO, "
                + "ID_CATEGORIA, "
                + "PRECIO_COSTO, "
                + "PRECIO_MINORISTA, "
                + "PRECIO_MAYORISTA, "
                + "ID_ESTADO, "
                + "CANT_ACTUAL)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(stm, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, prod.getDescripcion());
            try {
                if (prod.getCodBarra() == null) {
                    pst.setNull(2, Types.VARCHAR);
                } else {
                    pst.setString(2, prod.getCodBarra());
                }
            } catch (Exception e) {
                pst.setNull(2, Types.VARCHAR);
            }
            pst.setInt(3, prod.getIdMarca());
            pst.setInt(4, prod.getIdImpuesto());
            pst.setInt(5, prod.getIdCategoria());
            pst.setInt(6, prod.getPrecioCosto());
            pst.setInt(7, prod.getPrecioVenta());
            pst.setInt(8, prod.getPrecioMayorista());
            pst.setInt(9, prod.getIdEstado());
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
        return id_producto;
    }

    public static int modificarProducto(M_producto producto) {
        String UPDATE = "UPDATE  producto SET CODIGO = ?, ID_MARCA = ?,"
                + " ID_ESTADO = ?, ID_IMPUESTO = ?, ID_CATEGORIA = ?, "
                + "PRECIO_COSTO = ?, PRECIO_MAYORISTA = ?, PRECIO_MINORISTA = ?, "
                + "CANT_ACTUAL = ? WHERE ID_PRODUCTO = ? ;";
        int result = -1;
        try {

            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setString(1, producto.getCodBarra());
            pst.setInt(2, producto.getIdMarca());
            pst.setInt(3, producto.getIdEstado());
            pst.setInt(4, producto.getIdImpuesto());
            pst.setInt(5, producto.getIdCategoria());
            pst.setInt(6, producto.getPrecioCosto());
            pst.setInt(7, producto.getPrecioMayorista());
            pst.setInt(8, producto.getPrecioVenta());
            pst.setDouble(9, producto.getCantActual());
            pst.setInt(10, producto.getId());
            result = pst.executeUpdate();
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
        return result;
    }

    public static void sumarStock(ArrayList<Integer> id, ArrayList<Double> cantidad) {
        try {
            DB_manager.habilitarTransaccionManual();
            for (int i = 0; i < id.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + id.get(i).toString() + ")+" + cantidad.get(i).toString() + ") "
                        + "WHERE ID_PRODUCTO =" + id.get(i).toString();
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
                + "SET codigo = " + id_producto
                + " WHERE id_producto = " + id_producto + "";
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
        String Query = "SELECT DESCRIPCION FROM PRODUCTO WHERE DESCRIPCION LIKE ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, prodDescripcion);
            rs = pst.executeQuery();
            return rs.next();
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
