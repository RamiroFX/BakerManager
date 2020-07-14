/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.M_producto;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.ProductoCategoria;
import java.sql.CallableStatement;
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
 * @author Usuario
 */
public class DB_Producto {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static int obtenerUltimoIdProducto() {
        String Query = "SELECT id_producto FROM producto WHERE id_producto IS NOT NULL  ORDER BY id_producto DESC LIMIT 1;";
        long idProducto = 0;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                idProducto = rs.getInt(1);
            }
            pst.close();
            rs.close();
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
        return (int) idProducto;
    }

    public static ResultSetTableModel consultarProducto(String prodDescripcion) {
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
                    + "WHERE LOWER(PROD.DESCRIPCION) LIKE ? "
                    + "ORDER BY PROD.DESCRIPCION";
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            pst = DB_manager.getConection().prepareStatement(q2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, prodDescripcion + "%");
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = pst.executeQuery();
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
                    + "AND (LOWER(PROD.DESCRIPCION) LIKE ? "
                    + "OR LOWER(PROD.CODIGO) LIKE ? ) "
                    + marc
                    + imp
                    + categ
                    + estad
                    + finalQuery;
            //se crea una sentencia
            pst = DB_manager.getConection().prepareStatement(FINAL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, busqueda_);
            pst.setString(2, busqueda_);
            rs = pst.executeQuery();
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

    public static ResultSetTableModel consultaSimpleProducto(String descripcion, String proveedor, String marca, int idCategoria, int idSubCategoria, String impuesto, String estado, String ordenarPor, String existencia) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String existenciaSQL = "";
            switch (existencia) {
                case "Todos": {
                    existenciaSQL = "";
                    break;
                }
                case "Positiva": {
                    existenciaSQL = "AND PROD.CANT_ACTUAL > 0 ";
                    break;
                }
                case "Negativa": {
                    existenciaSQL = "AND PROD.CANT_ACTUAL <= 0 ";
                    break;
                }
                default: {
                    existenciaSQL = "";
                    break;
                }
            }
            String finalQuery = "ORDER BY PROD.DESCRIPCION ";
            if (ordenarPor.equals("Descripción")) {
                finalQuery = "ORDER BY PROD.DESCRIPCION ";
            } else if (ordenarPor.equals("ID")) {
                finalQuery = "ORDER BY PROD.ID_PRODUCTO ";
            }
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
                marc = "AND PROD.ID_MARCA = (SELECT MARC.ID_MARCA FROM MARCA MARC WHERE MARC.DESCRIPCION LIKE '" + marca + "' )";
            }
            String rubr = "";
            if (idCategoria > 0) {
                if (idSubCategoria > 0) {
                    rubr = "AND PROD.ID_CATEGORIA = " + idSubCategoria + "";
                } else {
                    rubr = "AND PROD.ID_CATEGORIA IN (SELECT PRCA.ID_PRODUCTO_CATEGORIA FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PADRE = " + idCategoria + ")";
                }
            } else {
                if (idSubCategoria > 0) {
                    rubr = "AND PROD.ID_CATEGORIA = " + idSubCategoria + "";
                } else {
                    rubr = "";
                }
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD.ID_ESTADO = (SELECT ESTA.ID_ESTADO FROM ESTADO ESTA WHERE ESTA.DESCRIPCION LIKE '" + estado + "') ";
            }

            String Query = "SELECT PROD.ID_PRODUCTO \"ID\", "
                    + "PROD.DESCRIPCION \"Descripción\" "
                    + fromQuery
                    + "WHERE "
                    + prov
                    + "(LOWER(PROD.DESCRIPCION) LIKE ? "
                    + "OR LOWER(PROD.CODIGO) LIKE ? )"
                    + marc
                    + imp
                    + rubr
                    + estad
                    + existenciaSQL
                    + finalQuery;
            //SELECT PROD.id_producto   "ID producto"  ,  PROD.descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, "%" + descripcion + "%");
            pst.setString(2, "%" + descripcion + "%");
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ArrayList<M_producto> consultaSimpleProductos(String descripcion, String proveedor, String marca, String rubro, String impuesto, String estado, String ordenarPor, String existencia) {
        ArrayList productos = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String existenciaSQL = "";
            switch (existencia) {
                case "Todos": {
                    existenciaSQL = "";
                    break;
                }
                case "Positiva": {
                    existenciaSQL = "AND PROD.CANT_ACTUAL > 0 ";
                    break;
                }
                case "Negativa": {
                    existenciaSQL = "AND PROD.CANT_ACTUAL <= 0 ";
                    break;
                }
                default: {
                    existenciaSQL = "";
                    break;
                }
            }
            String finalQuery = "ORDER BY PROD.DESCRIPCION ";
            if (ordenarPor.equals("Descripción")) {
                finalQuery = "ORDER BY PROD.DESCRIPCION ";
            } else if (ordenarPor.equals("ID")) {
                finalQuery = "ORDER BY PROD.ID_PRODUCTO ";
            }
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
                marc = "AND PROD.ID_MARCA = (SELECT MARC.ID_MARCA FROM MARCA MARC WHERE MARC.DESCRIPCION LIKE '" + marca + "' )";
            }

            String rubr;
            if ("Todos".equals(rubro)) {
                rubr = "";
            } else {
                rubr = "AND PROD.ID_CATEGORIA = (SELECT PRCA.ID_PRODUCTO_CATEGORIA FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.DESCRIPCION LIKE '" + rubro + "' )";
            }
            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD.ID_ESTADO = (SELECT ESTA.ID_ESTADO FROM ESTADO ESTA WHERE ESTA.DESCRIPCION LIKE '" + estado + "') ";
            }

            String Query = "SELECT PROD.ID_PRODUCTO \"id_producto\", "
                    + "PROD.CODIGO \"codigo\", "
                    + "PROD.DESCRIPCION \"descripcion\", "
                    + "PROD.PRECIO_COSTO \"precio_costo\", "
                    + "PROD.PRECIO_MINORISTA \"precio_minorista\", "
                    + "PROD.PRECIO_MAYORISTA \"precio_mayorista\", "
                    + "PROD.CANT_ACTUAL \"cant_actual\", "
                    + "PROD.ID_MARCA \"id_marca\", "
                    + "PROD.ID_IMPUESTO \"id_impuesto\", "
                    + "PROD.ID_ESTADO \"id_estado\", "
                    + "PROD.ID_CATEGORIA \"id_categoria\", "
                    + "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = PROD.ID_IMPUESTO) \"impuesto\", "
                    + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PROD.ID_ESTADO) \"estado\", "
                    + "(SELECT MARC.DESCRIPCION FROM MARCA MARC WHERE MARC.ID_MARCA = PROD.ID_MARCA) \"marca\", "
                    + "(SELECT PRCA.DESCRIPCION FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PRODUCTO_CATEGORIA = PROD.ID_CATEGORIA) \"categoria\" "
                    + fromQuery
                    + "WHERE "
                    + prov
                    + "LOWER(PROD.DESCRIPCION) LIKE ? "
                    + marc
                    + imp
                    + rubr
                    + estad
                    + existenciaSQL
                    + finalQuery;

            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, descripcion + "%");
            rs = pst.executeQuery();
            productos = new ArrayList();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setCodBarra(rs.getString("codigo"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setMarca(rs.getString("marca"));
                producto.setEstado(rs.getString("estado"));
                producto.setImpuesto(rs.getInt("impuesto"));
                producto.setPrecioCosto(rs.getInt("precio_costo"));
                producto.setPrecioMayorista(rs.getInt("precio_mayorista"));
                producto.setPrecioVenta(rs.getInt("precio_minorista"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setIdEstado(rs.getInt("id_estado"));
                producto.setIdImpuesto(rs.getInt("id_impuesto"));
                producto.setIdMarca(rs.getInt("id_marca"));
                producto.setCantActual(rs.getDouble("cant_actual"));
                productos.add(producto);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return productos;
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
                producto.setObservacion(rs.getString("observacion"));
            }
            storedFunct.close();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return producto;
    }

    public static M_producto obtenerProductoPorCodigo(String codigoProducto) {
        M_producto producto = null;
        String Query = "SELECT PROD.ID_PRODUCTO \"id_prod\", "
                + "PROD.CODIGO \"cod_prod\", "
                + "PROD.DESCRIPCION \"descripcion\", "
                + "PROD.PRECIO_COSTO \"precio_costo\", "
                + "PROD.PRECIO_MINORISTA \"precio_minorista\", "
                + "PROD.PRECIO_MAYORISTA \"precio_mayorista\", "
                + "PROD.CANT_ACTUAL \"cant_actual\", "
                + "PROD.ID_MARCA \"id_marca\", "
                + "PROD.ID_IMPUESTO \"id_impuesto\", "
                + "PROD.ID_ESTADO \"id_estado\", "
                + "PROD.ID_CATEGORIA \"id_categoria\", "
                + "PROD.OBSERVACION \"observacion\", "
                + "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = PROD.ID_IMPUESTO) \"impuesto\", "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PROD.ID_ESTADO) \"estado\", "
                + "(SELECT MARC.DESCRIPCION FROM MARCA MARC WHERE MARC.ID_MARCA = PROD.ID_MARCA) \"marca\", "
                + "(SELECT PRCA.DESCRIPCION FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PRODUCTO_CATEGORIA = PROD.ID_CATEGORIA) \"categoria\" "
                + "FROM PRODUCTO PROD "
                + "WHERE PROD.CODIGO LIKE ? ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, codigoProducto);
            rs = pst.executeQuery();
            while (rs.next()) {
                producto = new M_producto();
                producto.setCantActual(rs.getDouble("cant_actual"));
                producto.setCodBarra(rs.getString("cod_prod"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getString("estado"));
                producto.setId(rs.getInt("id_prod"));
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
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return producto;
    }

    public static M_producto obtenerProductoPorDescripcion(String descripcion) {
        M_producto producto = null;
        String Query = "SELECT PROD.ID_PRODUCTO \"id_prod\", "
                + "PROD.CODIGO \"cod_prod\", "
                + "PROD.DESCRIPCION \"descripcion\", "
                + "PROD.PRECIO_COSTO \"precio_costo\", "
                + "PROD.PRECIO_MINORISTA \"precio_minorista\", "
                + "PROD.PRECIO_MAYORISTA \"precio_mayorista\", "
                + "PROD.CANT_ACTUAL \"cant_actual\", "
                + "PROD.ID_MARCA \"id_marca\", "
                + "PROD.ID_IMPUESTO \"id_impuesto\", "
                + "PROD.ID_ESTADO \"id_estado\", "
                + "PROD.ID_CATEGORIA \"id_categoria\", "
                + "PROD.OBSERVACION \"observacion\", "
                + "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = PROD.ID_IMPUESTO) \"impuesto\", "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PROD.ID_ESTADO) \"estado\", "
                + "(SELECT MARC.DESCRIPCION FROM MARCA MARC WHERE MARC.ID_MARCA = PROD.ID_MARCA) \"marca\", "
                + "(SELECT PRCA.DESCRIPCION FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PRODUCTO_CATEGORIA = PROD.ID_CATEGORIA) \"categoria\" "
                + "FROM PRODUCTO PROD "
                + "WHERE PROD.DESCRIPCION LIKE ? ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, descripcion);
            rs = pst.executeQuery();
            while (rs.next()) {
                producto = new M_producto();
                producto.setCantActual(rs.getDouble("cant_actual"));
                producto.setCodBarra(rs.getString("cod_prod"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getString("estado"));
                producto.setId(rs.getInt("id_prod"));
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
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return producto;
    }

    public static boolean productoEnUso(int idProducto) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_PRODUCTO "
                + "FROM FACTURA_DETALLE "
                + "WHERE ID_PRODUCTO = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(q);
            pst.setInt(1, idProducto);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
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
                + "CANT_ACTUAL = ?, OBSERVACION = ?, DESCRIPCION = ? WHERE ID_PRODUCTO = ? ;";
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
            pst.setString(10, producto.getObservacion());
            pst.setString(11, producto.getDescripcion());
            pst.setInt(12, producto.getId());
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
        String Query = "SELECT DESCRIPCION FROM PRODUCTO WHERE LOWER(DESCRIPCION) LIKE ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, prodDescripcion.toLowerCase());
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static boolean existeCodigo(String prodCodigo) {
        String Query = "SELECT CODIGO FROM PRODUCTO WHERE CODIGO LIKE ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, prodCodigo);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static int modificarPrecioCostoProducto(Integer id, int precioNuevo) {
        String UPDATE = "UPDATE  producto SET "
                + "PRECIO_COSTO = ? WHERE ID_PRODUCTO = ? ;";
        int result = -1;
        try {

            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, precioNuevo);
            pst.setInt(2, id);
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

    public static ArrayList<E_productoClasificacion> obtenerProductoClasificacion() {
        ArrayList<E_productoClasificacion> list = null;
        String q = "SELECT *  "
                + "FROM PRODUCTO_CLASIFICACION;";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            list = new ArrayList();
            while (rs.next()) {
                E_productoClasificacion tiop = new E_productoClasificacion();
                tiop.setId(rs.getInt("id_producto_clasificacion"));
                tiop.setDescripcion(rs.getString("descripcion"));
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<ProductoCategoria> obtenerProductoCategoria() {
        List<ProductoCategoria> list = null;
        String q = "SELECT id_producto_categoria, id_padre, descripcion FROM producto_categoria "
                + "WHERE id_padre = 0 "
                + "ORDER BY id_producto_categoria;";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            list = new ArrayList();
            while (rs.next()) {
                ProductoCategoria padre = new ProductoCategoria();
                padre.setId(rs.getInt("id_padre"));
                ProductoCategoria tiop = new ProductoCategoria();
                tiop.setId(rs.getInt("id_producto_categoria"));
                tiop.setDescripcion(rs.getString("descripcion"));
                tiop.setPadre(padre);
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<ProductoCategoria> obtenerProductoSubCategoria() {
        List<ProductoCategoria> list = null;
        String q = "SELECT P.id_producto_categoria, "
                + "P.descripcion, "
                + "P.id_padre,  "
                + "(SELECT producto_categoria.descripcion FROM producto_categoria WHERE producto_categoria.id_producto_categoria = P.id_padre ) \"DESCRIPCION_PADRE\" "
                + "FROM producto_categoria P "
                + "WHERE P.id_padre > 0 "
                + "ORDER BY \"DESCRIPCION_PADRE\";";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            list = new ArrayList();
            while (rs.next()) {
                ProductoCategoria padre = new ProductoCategoria();
                padre.setId(rs.getInt("id_padre"));
                padre.setDescripcion(rs.getString("descripcion_padre"));
                ProductoCategoria tiop = new ProductoCategoria();
                tiop.setId(rs.getInt("id_producto_categoria"));
                tiop.setDescripcion(rs.getString("descripcion"));
                tiop.setPadre(padre);
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<ProductoCategoria> obtenerProductoSubCategoria(int idPadre) {
        List<ProductoCategoria> list = null;
        String q = "SELECT P.id_producto_categoria, "
                + "P.descripcion, "
                + "P.id_padre,  "
                + "(SELECT producto_categoria.descripcion FROM producto_categoria WHERE producto_categoria.id_producto_categoria = P.id_padre ) \"DESCRIPCION_PADRE\" "
                + "FROM producto_categoria P "
                + "WHERE  ";
        if (idPadre > 0) {
            q = q + "P.id_padre = ? ";
        } else {
            q = q + "P.id_padre > 0 ";
        }
        q = q + "ORDER BY \"DESCRIPCION_PADRE\";";
        try {
            pst = DB_manager.getConection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (idPadre > 0) {
                pst.setInt(1, idPadre);
            }
            rs = pst.executeQuery();
            list = new ArrayList();
            while (rs.next()) {
                ProductoCategoria padre = new ProductoCategoria();
                padre.setId(rs.getInt("id_padre"));
                padre.setDescripcion(rs.getString("descripcion_padre"));
                ProductoCategoria tiop = new ProductoCategoria();
                tiop.setId(rs.getInt("id_producto_categoria"));
                tiop.setDescripcion(rs.getString("descripcion"));
                tiop.setPadre(padre);
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static ArrayList<E_productoClasificacion> obtenerProductoCategoriaBauplast() {
        ArrayList<E_productoClasificacion> list = null;
        String q = "SELECT *  "
                + "FROM PRODUCTO_CATEGORIA WHERE ID_PADRE = 23 ;";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            list = new ArrayList();
            while (rs.next()) {
                E_productoClasificacion tiop = new E_productoClasificacion();
                tiop.setId(rs.getInt("id_producto_categoria"));
                tiop.setDescripcion(rs.getString("descripcion"));
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static ArrayList<M_producto> consultarProductoPorClasificacion(String descripcion, Estado estado, String ordenarPor, E_productoClasificacion clasificacion) {
        ArrayList productos = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }

            String fromQuery = "FROM PRODUCTO PROD ";
            String finalQuery = "ORDER BY PROD.DESCRIPCION ";
            String buscarPor = "LOWER(PROD.DESCRIPCION) LIKE ? ";
            switch (ordenarPor) {
                case "ID": {
                    buscarPor = "CAST(PROD.ID_PRODUCTO AS CHARACTER VARYING) LIKE ? ";
                    break;
                }
                case "Código": {
                    buscarPor = "LOWER(PROD.CODIGO) LIKE ? ";
                    break;
                }
                case "Descripción": {
                    buscarPor = "LOWER(PROD.DESCRIPCION) LIKE ? ";
                    break;
                }
            }

            String estad;
            if ("Todos".equals(estado)) {
                estad = "";
            } else {
                estad = "AND PROD.ID_ESTADO = (SELECT ESTA.ID_ESTADO FROM ESTADO ESTA WHERE ESTA.DESCRIPCION LIKE '" + estado + "') ";
            }
            String productoClasificacion;
            switch (clasificacion.getId()) {
                case -1: {
                    productoClasificacion = "";
                    break;
                }
                case E_productoClasificacion.PROD_TERMINADO: {
                    productoClasificacion = "AND PROD.ID_CATEGORIA = " + E_productoClasificacion.PROD_TERMINADO + " ";
                    break;
                }
                case E_productoClasificacion.MATERIA_PRIMA: {
                    productoClasificacion = "AND PROD.ID_CATEGORIA = " + E_productoClasificacion.MATERIA_PRIMA + " ";
                    break;
                }
                default: {
                    productoClasificacion = "AND PROD.ID_CATEGORIA = " + clasificacion.getId() + " ";
                    break;
                }
            }

            String Query = "SELECT PROD.ID_PRODUCTO \"id_producto\", "
                    + "PROD.CODIGO \"codigo\", "
                    + "PROD.DESCRIPCION \"descripcion\", "
                    + "PROD.CANT_ACTUAL \"cant_actual\", "
                    + "PROD.ID_ESTADO \"id_estado\", "
                    + "PROD.ID_CATEGORIA \"id_categoria\", "
                    + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PROD.ID_ESTADO) \"estado\", "
                    + "(SELECT PRCA.DESCRIPCION FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.ID_PRODUCTO_CATEGORIA = PROD.ID_CATEGORIA) \"categoria\" "
                    + fromQuery
                    + "WHERE "
                    + buscarPor
                    + estad
                    + productoClasificacion
                    + finalQuery;

            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, "%" + descripcion.toLowerCase() + "%");
            rs = pst.executeQuery();
            productos = new ArrayList();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setCodBarra(rs.getString("codigo"));
                producto.setDescripcion(rs.getString("descripcion"));

                producto.setEstado(rs.getString("estado"));

                producto.setCategoria(rs.getString("categoria"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setIdEstado(rs.getInt("id_estado"));
                producto.setCantActual(rs.getDouble("cant_actual"));
                productos.add(producto);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return productos;
    }
}
