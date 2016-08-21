/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

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
import javax.swing.JOptionPane;

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
        String Query = "SELECT EGCA.ID_EGRESO_CABECERA \"ID egreso\", "
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA.NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "to_char(EGCA.TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA EGCA, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE  EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA";
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
        String fromQuery = "FROM EGRESO_CABECERA EGCA ,FUNCIONARIO FUNC, PERSONA PERS ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA.TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND EGCA.TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor_entidad)) {
            prov = "";
        } else {
            fromQuery = "FROM EGRESO_CABECERA EGCA, FUNCIONARIO FUNC, PERSONA PERS, PROVEEDOR PROV ";
            prov = " AND PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR AND PROV.ENTIDAD LIKE'" + proveedor_entidad + "' ";
        }

        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            empleado = " AND FUNC.ID_FUNCIONARIO = " + idEmpleado;
        }

        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA.ID_COND_COMPRA = " + tipo_operacion;
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND EGCA.NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT EGCA.ID_EGRESO_CABECERA \"ID\", "
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA.NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "to_char(EGCA.TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "ROUND((SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM EGRESO_DETALLE EGDE WHERE EGDE.ID_EGRESO_CABECERA = EGCA.ID_EGRESO_CABECERA))\"Total\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA) \"Cond. compra\" "
                + fromQuery
                + "WHERE EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + numero_fac;
        try {
            Query = Query + " ORDER BY EGCA.TIEMPO";
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
        String Query = "SELECT "
                + "EGDE.ID_PRODUCTO \"ID art.\", "
                + "P.DESCRIPCION \"Producto\", "
                + "EGDE.CANTIDAD \"Cantidad\", "
                + "EGDE.PRECIO \"Precio\", "
                + "EGDE.DESCUENTO \"Descuento\","
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 1 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 2 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 3 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 10%\", "
                + "OBSERVACION \"Obs.\" "
                + "FROM EGRESO_DETALLE EGDE, PRODUCTO P "
                + "WHERE EGDE.ID_PRODUCTO = P.ID_PRODUCTO "
                + "AND EGDE.ID_EGRESO_CABECERA = " + idEgresoCabecera;
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

    public static ResultSetTableModel obtenerEgresoDetalleAvanzado(String producto, String proveedor, String marca, String impuesto, String categoria, String estado, String fechaInicio, String fechaFinal, String tipo_operacion, String idEmpleado, boolean busqDescripcion) {
        ResultSetTableModel rstm = null;
        String fromQuery = "FROM EGRESO_DETALLE EGDE,PRODUCTO PROD, EGRESO_CABECERA EGCA ";
        String whereQuery = "WHERE EGDE.ID_PRODUCTO = PROD.ID_PRODUCTO AND EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(fechaInicio)) {
            fInicio = "";
            if ("Todos".equals(fechaFinal)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA.TIEMPO <'" + fechaFinal + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA.TIEMPO BETWEEN '" + fechaInicio + "'::timestamp  ";
            fFinal = "AND '" + fechaFinal + "'::timestamp ";
            if ("Todos".equals(fechaFinal)) {
                fInicio = "AND EGCA.TIEMPO > '" + fechaInicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor)) {
            prov = "";
        } else {
            fromQuery = fromQuery + ", PROVEEDOR PROV, PROVEEDOR_PRODUCTO PRPR ";
            whereQuery = whereQuery + " AND PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR "
                    + "AND PRPR.ID_PRODUCTO = PROD.ID_PRODUCTO ";
            prov = " AND PROV.ENTIDAD LIKE'" + proveedor + "' ";
        }

        String marc;
        if ("Todos".equals(marca)) {
            marc = "";
        } else {
            marc = "AND PROD.ID_MARCA = (SELECT MARC.ID_MARCA FROM MARCA MARC WHERE MARC.DESCRIPCION = '" + marca + "') ";
        }
        String imp;
        if ("Todos".equals(impuesto)) {
            imp = "";
        } else {
            imp = "AND PROD.ID_IMPUESTO =(SELECT IMPU.ID_IMPUESTO FROM IMPUESTO IMPU WHERE IMPU.DESCRIPCION = " + impuesto + ") ";
        }

        String rubr;
        if ("Todos".equals(categoria)) {
            rubr = "";
        } else {
            rubr = "AND PROD.ID_CATEGORIA = (SELECT PRCA.ID_PRODUCTO_CATEGORIA FROM PRODUCTO_CATEGORIA PRCA WHERE PRCA.DESCRIPCION = '" + categoria + "') ";
        }
        String estad;
        if ("Todos".equals(estado)) {
            estad = "";
        } else {
            estad = "AND PROD.ID_ESTADO = (SELECT ESTA.ID_ESTADO FROM ESTADO ESTA WHERE ESTA.DESCRIPCION LIKE '" + estado + "') ";
        }

        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            fromQuery = fromQuery + ", FUNCIONARIO FUNC, PERSONA PERS ";
            whereQuery = whereQuery + " AND PERS.ID_PERSONA = FUNC.ID_PERSONA AND EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO ";
            empleado = " AND FUNC.ID_FUNCIONARIO = " + idEmpleado;
        }

        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA.ID_COND_COMPRA = " + tipo_operacion;
        }

        String busqueda;
        if (busqDescripcion) {
            busqueda = "AND LOWER(PROD.DESCRIPCION) LIKE LOWER (?) ESCAPE '!' ";
        } else {
            busqueda = "AND LOWER(EGDE.OBSERVACION) LIKE LOWER (?) ESCAPE '!' ";
        }

        String Query = "SELECT EGDE.ID_EGRESO_DETALLE\"ID det.\", "
                + "(SELECT PROD.DESCRIPCION FROM PRODUCTO PROD WHERE PROD.ID_PRODUCTO = EGDE.ID_PRODUCTO) \"Producto\", "
                + "EGDE.OBSERVACION \"Obs.\",EGDE.CANTIDAD \"Cantidad\", EGDE.PRECIO \"Precio\", (EGDE.PRECIO*EGDE.CANTIDAD)\"Total\", EGCA.TIEMPO \"Tiempo\" "
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
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, producto + "%");
            rs = pst.executeQuery();
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
        String query = "SELECT EGCA.ID_EGRESO_CABECERA, "
                + "EGCA.ID_PROVEEDOR, "
                + "EGCA.ID_FUNCIONARIO, "
                + "EGCA.TIEMPO, "
                + "EGCA.NRO_FACTURA, "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA)\"EGCA.ID_COND_COMPRA\", "
                + "EGCA.ID_COND_COMPRA "
                + "FROM EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = " + idEgresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                egreso_cabecera = new M_egreso_cabecera();
                egreso_cabecera.setId_cabecera(rs.getInt("ID_EGRESO_CABECERA"));
                egreso_cabecera.setId_condVenta(rs.getInt("ID_COND_COMPRA"));
                egreso_cabecera.setId_empleado(rs.getInt("ID_FUNCIONARIO"));
                egreso_cabecera.setId_proveedor(rs.getInt("ID_PROVEEDOR"));
                egreso_cabecera.setTiempo(rs.getTimestamp("TIEMPO"));
                egreso_cabecera.setNro_factura(rs.getInt("NRO_FACTURA"));
                egreso_cabecera.setCondVenta(rs.getString("ID_COND_COMPRA"));
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
        String fromQuery = "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA,FUNCIONARIO FUNC, PERSONA PERS, PROVEEDOR PROV, PRODUCTO P ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND EGCA.TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND EGCA.TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(proveedor_entidad)) {
            prov = "";
        } else {
            prov = " AND PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR AND PROV.ENTIDAD LIKE'" + proveedor_entidad + "' ";
        }
        String empleado;
        if ("Todos".equals(idEmpleado)) {
            empleado = "";
        } else {
            empleado = " AND FUNC.ID_FUNCIONARIO = " + idEmpleado;
        }
        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND EGCA.ID_COND_COMPRA = " + tipo_operacion;
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND EGCA.NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT EGDE.ID_EGRESO_DETALLE, "
                + "EGDE.ID_PRODUCTO , "
                + "P.DESCRIPCION  \"PRODUCTO\", "
                + "EGDE.CANTIDAD , "
                + "EGDE.PRECIO , "
                + "EGDE.DESCUENTO, "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 1 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"EXENTA\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 2 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"CINCO\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 3 THEN ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"DIEZ\", "
                + "(EGDE.CANTIDAD*EGDE.PRECIO ) \"TOTAL\", "
                + "PROV.ENTIDAD, "
                + "EGDE.OBSERVACION, "
                + "EGCA.tiempo, "
                + "EGCA.ID_EGRESO_CABECERA "
                + fromQuery
                + "WHERE EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND EGCA.ID_PROVEEDOR = PROV.ID_PROVEEDOR "
                + "AND EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGDE.ID_PRODUCTO = P.ID_PRODUCTO "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + numero_fac
                + " ORDER BY EGCA.TIEMPO";
        ArrayList Arraylist = null;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            Arraylist = new ArrayList();
            M_egreso_detalleFX egreso_detalle;
            while (rs.next()) {
                egreso_detalle = new M_egreso_detalleFX();
                egreso_detalle.setCantidad(rs.getDouble("CANTIDAD"));
                egreso_detalle.setDescuento(rs.getDouble("DESCUENTO"));
                egreso_detalle.setId_cabecera(rs.getInt("ID_EGRESO_CABECERA"));
                egreso_detalle.setId_detalle(rs.getInt("ID_EGRESO_DETALLE"));
                egreso_detalle.setProducto(rs.getString("Producto"));
                egreso_detalle.setIva_cinco(rs.getInt("CINCO"));
                egreso_detalle.setIva_diez(rs.getInt("DIEZ"));
                egreso_detalle.setIva_exenta(rs.getInt("EXENTA"));
                egreso_detalle.setPrecio(rs.getInt("PRECIO"));
                egreso_detalle.setTotal(rs.getInt("TOTAL"));
                egreso_detalle.setProveedor(rs.getString("ENTIDAD"));
                egreso_detalle.setObservacion(rs.getString("OBSERVACION"));
                egreso_detalle.setTiempo(rs.getDate("tiempo"));
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
        String Query = "SELECT EGCA.ID_EGRESO_CABECERA \"ID egreso\", "
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR)\"Proveedor\", "
                + "EGCA.NRO_FACTURA \"Nro. factura\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "EGCA.TIEMPO \"Tiempo\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA EGCA, EGRESO_DETALLE EGDE, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGDE.ID_EGRESO_DETALLE = " + idEgresoDetalle;
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
        String query = "SELECT SUM(EGDE.TOTAL)\"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND EGCA.ID_COND_COMPRA = " + tipo_operacion;
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
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR)\"Proveedor\","
                + "EGCA.NRO_FACTURA \"Nro. factura\", "
                + " (SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + " EGCA.TIEMPO \"Tiempo\","
                + "(SELECT SUM(EGDE.TOTAL) FROM EGRESO_DETALLE EGDE WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA)\"Total\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA) \"Cond. venta\" "
                + "FROM EGRESO_CABECERA EGCA, FUNCIONARIO FUNC, PERSONA PERS, EGRESO_DETALLE EGDE "
                + "WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
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
        String query = "SELECT ID_TIPO_OPERACION \"ID_TIPO_OPERACION\" "
                + "FROM TIPO_OPERACION "
                + "WHERE descripcion LIKE '" + tipoOperacion + "'";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                idTipoOperacion = rs.getInt("ID_TIPO_OPERACION");
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
        String insertDetalle = "INSERT INTO egreso_detalle(id_egreso_cabecera, id_producto, cantidad, precio, descuento, observacion)VALUES (?, ?, ?, ?, ?, ?)";
        String INSERT_CABECERA = "INSERT INTO egreso_cabecera(nro_factura, id_proveedor, id_funcionario, tiempo, ID_COND_COMPRA)VALUES (?, ?, ?, ?, ?)";
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
                pst.setString(6, egreso_detalle[i].getObservacion());
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

    public static boolean existeProveedorNroFactura(int idProveedor, Integer nroFactura) {
        String QUERY = "SELECT ID_EGRESO_CABECERA FROM EGRESO_CABECERA WHERE ID_PROVEEDOR = ? AND NRO_FACTURA = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY);
            pst.setInt(1, idProveedor);
            if (nroFactura == null) {
                pst.setNull(2, Types.INTEGER);
            } else {
                pst.setInt(2, nroFactura);
            }
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema verificando existencia de compra", "Error interno", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
