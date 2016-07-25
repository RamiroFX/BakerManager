/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_egreso_cabecera;
import Entities.M_egreso_detalle;
import Entities.M_egreso_detalleFX;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class DB_Egreso {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    /*
     * READ
     */

    public static ResultSetTableModel obtenerEgreso(Timestamp inicio, Timestamp fin, int tipo_operacion) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT EGCA_ID_EGRE_CABE \"ID egreso\", "
                + "(SELECT PROV_ENTIDAD FROM PROVEEDOR WHERE PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA_NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "to_char(EGCA_TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = EGCA_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA,FUNCIONARIO, PERSONA "
                + "WHERE  EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerEgreso(String proveedor_entidad, Integer nro_factura, String idEmpleado, String inicio, String fin, String tipo_operacion) {
        ResultSetTableModel rstm = null;
        String fromQuery = "FROM EGRESO_CABECERA,FUNCIONARIO, PERSONA ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA_TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND EGCA_TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor_entidad)) {
            prov = "";
        } else {
            fromQuery = "FROM EGRESO_CABECERA,FUNCIONARIO, PERSONA, PROVEEDOR ";
            prov = " AND PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR AND PROV_ENTIDAD LIKE'" + proveedor_entidad + "' ";
        }

        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            empleado = " AND FUNC_ID_FUNCIONARIO = " + idEmpleado;
        }

        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA_ID_COND_VENTA = " + tipo_operacion;
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND EGCA_NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT EGCA_ID_EGRE_CABE \"ID\", "
                + "(SELECT PROV_ENTIDAD FROM PROVEEDOR WHERE PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA_NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "to_char(EGCA_TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "(SELECT SUM(EGDE_TOTAL) FROM EGRESO_DETALLE WHERE EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE)\"Total\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = EGCA_ID_COND_VENTA) \"Cond. venta\" "
                + fromQuery
                + "WHERE EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + numero_fac;
        try {
            System.out.println("140- q: " + Query);
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerEgresoDetalle(int idEgresoCabecera) {
        String queryProducto = "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = EGDE_ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "EGDE_ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "EGDE_CANTIDAD \"Cantidad\", "
                + "EGDE_PRECIO \"Precio\", "
                + "EGDE_DESCUENTO \"Descuento\","
                + "EGDE_EXENTA \"Exenta\", "
                + "EGDE_CINCO \"IVA 5%\", "
                + "EGDE_DIEZ \"IVA 10%\", "
                + "EGDE_OBSERVACION \"Obs.\" "
                + "FROM EGRESO_DETALLE "
                + "WHERE EGDE_ID_EGRESO_CABE = " + idEgresoCabecera;
        ResultSetTableModel rstm = null;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerEgresoDetalleAvanzado(String producto, String proveedor, String marca, String impuesto, String rubro, String estado, String fechaInicio, String fechaFinal, String tipo_operacion, String idEmpleado, boolean busqDescripcion) {
        ResultSetTableModel rstm = null;
        String fromQuery = "FROM EGRESO_DETALLE,PRODUCTO, EGRESO_CABECERA ";
        String whereQuery = "WHERE EGDE_ID_PRODUCTO = PROD_ID_PRODUCTO AND EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(fechaInicio)) {
            fInicio = "";
            if ("Todos".equals(fechaFinal)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA_TIEMPO <'" + fechaFinal + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA_TIEMPO BETWEEN '" + fechaInicio + "'::timestamp  ";
            fFinal = "AND '" + fechaFinal + "'::timestamp ";
            if ("Todos".equals(fechaFinal)) {
                fInicio = "AND EGCA_TIEMPO > '" + fechaInicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor)) {
            prov = "";
        } else {
            fromQuery = fromQuery + ", PROVEEDOR, PROVEEDOR_PRODUCTO ";
            whereQuery = whereQuery + " AND PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR "
                    + "AND PRPR_ID_PRODUCTO = PROD_ID_PRODUCTO ";
            prov = " AND PROV_ENTIDAD LIKE'" + proveedor + "' ";
        }

        String marc;
        if ("Todos".equals(marca)) {
            marc = "";
        } else {
            marc = "AND PROD_MARCA LIKE '" + marca + "' ";
        }
        String imp;
        if ("Todos".equals(impuesto)) {
            imp = "";
        } else {
            imp = "AND prod_impuesto =(SELECT IMPU_ID_IMPUESTO FROM IMPUESTO WHERE IMPU_DESCRIPCION = " + impuesto + ") ";
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
            estad = "AND PROD_ESTADO = (SELECT ESTA_ID_ESTADO FROM ESTADO WHERE ESTA_DESCRIPCION LIKE '" + estado + "') ";
        }

        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            fromQuery = fromQuery + ", FUNCIONARIO, PERSONA ";
            whereQuery = whereQuery + " AND PERS_ID_PERSONA = FUNC_ID_PERSONA AND EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO ";
            empleado = " AND FUNC_ID_FUNCIONARIO = " + idEmpleado;
        }

        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA_ID_COND_VENTA = " + tipo_operacion;
        }

        String busqueda;
        if (busqDescripcion) {
            busqueda = "AND LOWER(prod_DESCRIPCION) LIKE '" + producto + "%' ";
        } else {
            busqueda = "AND LOWER(EGDE_OBSERVACION) LIKE '" + producto + "%' ";
        }

        String Query = "SELECT EGDE_ID_EGRESO_DETALLE\"ID det.\", "
                + "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = EGDE_ID_PRODUCTO) \"Producto\", "
                + "EGDE_OBSERVACION \"Obs.\",EGDE_CANTIDAD \"Cantidad\", EGDE_PRECIO \"Precio\", EGDE_TOTAL \"Total\", EGCA_TIEMPO \"Tiempo\" "
                + fromQuery
                + whereQuery
                + busqueda
                + prov
                + marc
                + imp
                + rubr
                + estad
                + fInicio
                + fFinal
                + tiop
                + empleado;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static M_egreso_cabecera obtenerEgresoCabeceraID(int idEgresoCabecera) {
        M_egreso_cabecera egreso_cabecera = null;
        String query = "SELECT EGCA_ID_EGRE_CABE, "
                + "EGCA_ID_PROVEEDOR, "
                + "EGCA_ID_FUNCIONARIO, "
                + "EGCA_TIEMPO, "
                + "EGCA_NRO_FACTURA, "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = EGCA_ID_COND_VENTA)\"EGCA_COND_VENTA\", "
                + "EGCA_ID_COND_VENTA "
                + "FROM EGRESO_CABECERA "
                + "WHERE EGCA_ID_EGRE_CABE = " + idEgresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                egreso_cabecera = new M_egreso_cabecera();
                egreso_cabecera.setId_cabecera(rs.getInt("EGCA_ID_EGRE_CABE"));
                egreso_cabecera.setId_condVenta(rs.getInt("EGCA_ID_COND_VENTA"));
                egreso_cabecera.setId_empleado(rs.getInt("EGCA_ID_FUNCIONARIO"));
                egreso_cabecera.setId_proveedor(rs.getInt("EGCA_ID_PROVEEDOR"));
                egreso_cabecera.setTiempo(rs.getTimestamp("EGCA_TIEMPO"));
                egreso_cabecera.setNro_factura(rs.getInt("EGCA_NRO_FACTURA"));
                egreso_cabecera.setCondVenta(rs.getString("EGCA_COND_VENTA"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return egreso_cabecera;
    }

    public static ArrayList obtenerEgresosDetalle(String proveedor_entidad, Integer nro_factura, String idEmpleado, String inicio, String fin, String tipo_operacion) {
        String fromQuery = "FROM EGRESO_DETALLE, EGRESO_CABECERA,FUNCIONARIO, PERSONA, PROVEEDOR ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA_TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND EGCA_TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor_entidad)) {
            prov = "";
        } else {
            prov = " AND PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR AND PROV_ENTIDAD LIKE'" + proveedor_entidad + "' ";
        }
        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            empleado = " AND FUNC_ID_FUNCIONARIO = " + idEmpleado;
        }
        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA_ID_COND_VENTA = " + tipo_operacion;
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND EGCA_NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT EGDE_ID_EGRESO_DETALLE, "
                + "EGDE_ID_PRODUCTO , "
                + "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = EGDE_ID_PRODUCTO) \"PRODUCTO\", "
                + "EGDE_CANTIDAD , "
                + "EGDE_PRECIO , "
                + "EGDE_DESCUENTO, "
                + "EGDE_EXENTA, "
                + "EGDE_CINCO, "
                + "EGDE_DIEZ, "
                + "EGDE_TOTAL, "
                + "PROV_ENTIDAD, "
                + "EGDE_OBSERVACION, "
                + "egca_tiempo, "
                + "egca_id_egre_cabe "
                + fromQuery
                + "WHERE EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA "
                + "AND EGCA_ID_PROVEEDOR = PROV_ID_PROVEEDOR "
                + "AND EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + numero_fac
                + " ORDER BY EGCA_TIEMPO";
        ArrayList Arraylist = null;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            Arraylist = new ArrayList();
            M_egreso_detalleFX egreso_detalle;
            while (rs.next()) {
                egreso_detalle = new M_egreso_detalleFX();
                egreso_detalle.setCantidad(rs.getDouble("EGDE_CANTIDAD"));
                egreso_detalle.setDescuento(rs.getDouble("EGDE_DESCUENTO"));
                egreso_detalle.setId_cabecera(rs.getInt("egca_id_egre_cabe"));
                egreso_detalle.setId_detalle(rs.getInt("EGDE_ID_EGRESO_DETALLE"));
                egreso_detalle.setProducto(rs.getString("Producto"));
                egreso_detalle.setIva_cinco(rs.getInt("EGDE_CINCO"));
                egreso_detalle.setIva_diez(rs.getInt("EGDE_DIEZ"));
                egreso_detalle.setIva_exenta(rs.getInt("EGDE_EXENTA"));
                egreso_detalle.setPrecio(rs.getInt("EGDE_PRECIO"));
                egreso_detalle.setTotal(rs.getInt("EGDE_TOTAL"));
                egreso_detalle.setProveedor(rs.getString("PROV_ENTIDAD"));
                egreso_detalle.setObservacion(rs.getString("EGDE_OBSERVACION"));
                egreso_detalle.setTiempo(rs.getDate("egca_tiempo"));
                Arraylist.add(egreso_detalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return Arraylist;
    }

    public static ResultSetTableModel obtenerEgresoCabecera(Integer idEgresoDetalle) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT EGCA_ID_EGRE_CABE \"ID egreso\", "
                + "(SELECT PROV_ENTIDAD FROM PROVEEDOR WHERE PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA_NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "EGCA_TIEMPO \"Tiempo\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = EGCA_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA,EGRESO_DETALLE,FUNCIONARIO, PERSONA "
                + "WHERE EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA "
                + "AND EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE "
                + "AND EGDE_ID_EGRESO_DETALLE = " + idEgresoDetalle;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static Integer obtenerTotalEgreso(Timestamp inicio, Timestamp fin, int tipo_operacion) {
        Integer totalEgreso = 0;
        String query = "SELECT SUM(EGDE_TOTAL)\"Total\" "
                + "FROM EGRESO_DETALLE, EGRESO_CABECERA "
                + "WHERE EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE "
                + "AND EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND EGCA_ID_COND_VENTA = " + tipo_operacion;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                totalEgreso = rs.getInt("Total");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return totalEgreso;
    }

    public static ResultSetTableModel consultarResumenEgreso(Timestamp inicio, Timestamp fin) {
        String Query = "SELECT DISTINCT"
                + "(SELECT PROV_ENTIDAD FROM PROVEEDOR WHERE PROV_ID_PROVEEDOR = EGCA_ID_PROVEEDOR)\"Proveedor\","
                + "EGCA_NRO_FACTURA \"Nro. factura\", "
                + " (SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + " EGCA_TIEMPO \"Tiempo\","
                + "(SELECT SUM(EGDE_TOTAL) FROM EGRESO_DETALLE WHERE EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE)\"Total\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = EGCA_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA,FUNCIONARIO, PERSONA, EGRESO_DETALLE "
                + "WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA "
                + "AND EGCA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE "
                + "AND EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp";
        ResultSetTableModel rstm = null;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static Vector obtenerTipoOperacion() {
        Vector tiop = null;
        String q = "SELECT descripcion  "
                + "FROM TIPO_OPERACION ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tiop = new Vector();
            while (rs.next()) {
                tiop.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return tiop;
    }

    public static Integer obtenerTipoOperacion(String tipoOperacion) {
        Integer idTipoOperacion = 0;
        String query = "SELECT TIOP_ID_TIPO_OPERACION \"TIOP_ID_TIPO_OPERACION\" "
                + "FROM TIPO_OPERACION "
                + "WHERE tiop_descripcion LIKE '" + tipoOperacion + "'";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                idTipoOperacion = rs.getInt("TIOP_ID_TIPO_OPERACION");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return idTipoOperacion;

    }
    /*
     * CREATE
     */

    public static void insertarEgresoTEMPORAL(M_egreso_cabecera egreso_cabecera, M_egreso_detalle[] egreso_detalle) {
        String insertDetalle = "INSERT INTO egreso_detalle(egde_id_egreso_cabe, egde_id_producto, egde_cantidad, egde_precio, egde_descuento, egde_exenta, egde_cinco, egde_diez, egde_total, egde_observacion)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_CABECERA = "INSERT INTO egreso_cabecera(egca_nro_factura, egca_id_proveedor, egca_id_funcionario, egca_tiempo, egca_id_cond_venta)VALUES (?, ?, ?, ?, ?)";
        long sq_egreso_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            //pst.setInt(1, egreso_cabecera.getId_cabecera());
            try {
                if (egreso_cabecera.getNro_factura() == null) {
                    pst.setNull(1, Types.NUMERIC);
                } else {
                    pst.setInt(1, egreso_cabecera.getNro_factura());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.NUMERIC);
            }
            pst.setInt(2, egreso_cabecera.getId_proveedor());
            pst.setInt(3, egreso_cabecera.getId_empleado());
            pst.setTimestamp(4, egreso_cabecera.getTiempo());
            pst.setInt(5, egreso_cabecera.getId_condVenta());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_egreso_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < egreso_detalle.length; i++) {
                pst = DB_manager.getConection().prepareStatement(insertDetalle);
                pst.setInt(1, (int) sq_egreso_cabecera);
                pst.setInt(2, egreso_detalle[i].getId_producto());
                pst.setDouble(3, egreso_detalle[i].getCantidad());
                pst.setInt(4, egreso_detalle[i].getPrecio());
                pst.setDouble(5, egreso_detalle[i].getDescuento());
                pst.setInt(6, egreso_detalle[i].getIva_exenta());
                pst.setInt(7, egreso_detalle[i].getIva_cinco());
                pst.setInt(8, egreso_detalle[i].getIva_diez());
                pst.setInt(9, egreso_detalle[i].getTotal());
                pst.setString(10, egreso_detalle[i].getObservacion());
                pst.executeUpdate();
                pst.close();
            }
            System.out.println("Se inserto exitosamente");
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
