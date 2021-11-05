/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egresoCabecera;
import Entities.M_egreso_detalle;
import Entities.M_egreso_detalleFX;
import Entities.M_funcionario;
import Entities.M_producto;
import Entities.M_proveedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        }
        /*finally {
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

    public static ResultSetTableModel obtenerEgreso(String proveedor_entidad, Integer nro_factura, String idEmpleado, String inicio, String fin, String tipo_operacion, int idEstado) {
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
        String estadoQuery = "";
        if (idEstado > 0) {
            estadoQuery = " AND EGCA.ID_ESTADO = " + idEstado;
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
                + numero_fac
                + estadoQuery;
        try {
            Query = Query + " ORDER BY EGCA.TIEMPO";
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        /*finally {
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

    public static List<M_egresoCabecera> obtenerComprasCabecera(int idProveedor, int nroFactura, int idEmpleado, int idTipoOoperacion, int idEstado, Date inicio, Date fin, boolean conFecha) {
        List<M_egresoCabecera> list = new ArrayList<>();
        String fromQuery = "FROM EGRESO_CABECERA EGCA ,FUNCIONARIO FUNC, PERSONA PERS ";
        String fechaString = "";
        String proveedorString = "";
        String empleadoString = "";
        String tiopString = "";
        String nuneroFacturaString = "";
        String estadoQuery = "";
        if (conFecha) {
            fechaString = "AND EGCA.TIEMPO BETWEEN ?  AND ? ";
        }
        //PROVEEDOR
        if (idProveedor > 0) {
            fromQuery = "FROM EGRESO_CABECERA EGCA, FUNCIONARIO FUNC, PERSONA PERS, PROVEEDOR PROV ";
            proveedorString = " AND PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR AND PROV.ID_PROVEEDOR = ?";
        }
        //FUNCIONARIO
        if (idEmpleado > 0) {
            empleadoString = " AND FUNC.ID_FUNCIONARIO = ? ";
        }
        //CONDICION DE COMPRA
        if (idTipoOoperacion > 0) {
            tiopString = " AND EGCA.ID_COND_COMPRA = ? ";
        }
        //NUMERO DE FACTURA
        if (nroFactura > 0) {
            nuneroFacturaString = " AND EGCA.NRO_FACTURA = ? ";
        }
        //ESTADO
        if (idEstado > 0) {
            estadoQuery = " AND EGCA.ID_ESTADO = ? ";
        }
        String Query = "SELECT EGCA.ID_EGRESO_CABECERA, "//1
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR), "//2
                + "(SELECT PROV.NOMBRE FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR), "//3
                + "EGCA.NRO_FACTURA, "//4
                + "(SELECT PERS.NOMBRE WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA), "//5
                + "(SELECT PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA), "//6
                + "FUNC.ALIAS, "//7
                + "EGCA.TIEMPO, "//8
                + "(SELECT TIOP.ID_TIPO_OPERACION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA), "//9
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA), "//10
                + "(SELECT ESTADO.ID_ESTADO FROM ESTADO WHERE ESTADO.ID_ESTADO = EGCA.ID_ESTADO), "//11
                + "(SELECT ESTADO.DESCRIPCION FROM ESTADO WHERE ESTADO.ID_ESTADO = EGCA.ID_ESTADO), "//12
                + "(SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM EGRESO_DETALLE EGDE WHERE EGDE.ID_EGRESO_CABECERA = EGCA.ID_EGRESO_CABECERA) "//13
                + fromQuery
                + "WHERE EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + fechaString
                + proveedorString
                + empleadoString
                + tiopString
                + nuneroFacturaString
                + estadoQuery;
        Query = Query + " ORDER BY EGCA.TIEMPO, EGCA.NRO_FACTURA ";
        int pos = 1;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos++, new java.sql.Timestamp(inicio.getTime()));
                pst.setTimestamp(pos++, new java.sql.Timestamp(fin.getTime()));
            }
            if (idProveedor > 0) {
                pst.setInt(pos++, idProveedor);
            }
            //FUNCIONARIO
            if (idEmpleado > 0) {
                pst.setInt(pos++, idEmpleado);
            }
            //CONDICION DE COMPRA
            if (idTipoOoperacion > 0) {
                pst.setInt(pos++, idTipoOoperacion);
            }
            //NUMERO DE FACTURA
            if (nroFactura > 0) {
                pst.setInt(pos++, nroFactura);
            }
            //ESTADO
            if (idEstado > 0) {
                pst.setInt(pos, idEstado);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                proveedor.setEntidad(rs.getString(2));
                proveedor.setNombre(rs.getString(3));
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString(5));
                f.setApellido(rs.getString(6));
                f.setAlias(rs.getString(7));
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt(9));
                tiop.setDescripcion(rs.getString(10));
                Estado estado = new Estado();
                estado.setId(rs.getInt(11));
                estado.setDescripcion(rs.getString(12));
                M_egresoCabecera egresoCabecera = new M_egresoCabecera();
                egresoCabecera.setId_cabecera(rs.getInt(1));
                egresoCabecera.setNroFactura(rs.getInt(4));
                egresoCabecera.setCondCompra(tiop);
                egresoCabecera.setTiempo(rs.getTimestamp(8));
                egresoCabecera.setProveedor(proveedor);
                egresoCabecera.setFuncionario(f);
                egresoCabecera.setTotal(rs.getInt(13));
                egresoCabecera.setEstado(estado);
                list.add(egresoCabecera);
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

    public static List<M_egresoCabecera> obtenerComprasCabeceraPorCategoria(int idProveedor, int nroFactura, int idEmpleado, int idTipoOoperacion, int idEstado, Date inicio, Date fin, boolean conFecha, int idCategoria) {
        List<M_egresoCabecera> list = new ArrayList<>();
        String fromQuery = "FROM EGRESO_CABECERA EGCA ,FUNCIONARIO FUNC, PERSONA PERS ";
        String fechaString = "";
        String proveedorString = "";
        String empleadoString = "";
        String tiopString = "";
        String nuneroFacturaString = "";
        String estadoQuery = "";
        String categoriaQuery = "";
        if (conFecha) {
            fechaString = "AND EGCA.TIEMPO BETWEEN ?  AND ? ";
        }
        //PROVEEDOR
        if (idProveedor > 0) {
            fromQuery = "FROM EGRESO_CABECERA EGCA, FUNCIONARIO FUNC, PERSONA PERS, PROVEEDOR PROV, "
                    + "EGRESO_DETALLE EGDE, PRODUCTO P ";
            proveedorString = " AND PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR AND PROV.ID_PROVEEDOR = ?";
        }
        //FUNCIONARIO
        if (idEmpleado > 0) {
            empleadoString = " AND FUNC.ID_FUNCIONARIO = ? ";
        }
        //CONDICION DE COMPRA
        if (idTipoOoperacion > 0) {
            tiopString = " AND EGCA.ID_COND_COMPRA = ? ";
        }
        //NUMERO DE FACTURA
        if (nroFactura > 0) {
            nuneroFacturaString = " AND EGCA.NRO_FACTURA = ? ";
        }
        //ESTADO
        if (idEstado > 0) {
            estadoQuery = " AND EGCA.ID_ESTADO = ? ";
        }
        //CATEGORIAS
        if (idCategoria > 0) {
            categoriaQuery = " AND P.ID_CATEGORIA = ? ";
        }
        String Query = "SELECT EGCA.ID_EGRESO_CABECERA, "//1
                + "(SELECT PROV.ENTIDAD FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR), "//2
                + "(SELECT PROV.NOMBRE FROM PROVEEDOR PROV WHERE PROV.ID_PROVEEDOR = EGCA.ID_PROVEEDOR), "//3
                + "EGCA.NRO_FACTURA, "//4
                + "(SELECT PERS.NOMBRE WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA), "//5
                + "(SELECT PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA), "//6
                + "FUNC.ALIAS, "//7
                + "EGCA.TIEMPO, "//8
                + "(SELECT TIOP.ID_TIPO_OPERACION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA), "//9
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA), "//10
                + "(SELECT ESTADO.ID_ESTADO FROM ESTADO WHERE ESTADO.ID_ESTADO = EGCA.ID_ESTADO), "//11
                + "(SELECT ESTADO.DESCRIPCION FROM ESTADO WHERE ESTADO.ID_ESTADO = EGCA.ID_ESTADO), "//12
                + "(SELECT SUM (EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)) ) "//13
                + fromQuery
                + "WHERE EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGDE.ID_PRODUCTO = P.ID_PRODUCTO "
                + fechaString
                + proveedorString
                + empleadoString
                + tiopString
                + nuneroFacturaString
                + estadoQuery
                + categoriaQuery;
        Query = Query +" GROUP BY 1,2,3,4,5,6,7,8,9,10,11,12 "+ " ORDER BY EGCA.TIEMPO, EGCA.NRO_FACTURA ";
        int pos = 1;
        System.out.println("DB.DB_Egreso.obtenerComprasCabeceraPorCategoria()");
        System.out.println(Query);
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos++, new java.sql.Timestamp(inicio.getTime()));
                pst.setTimestamp(pos++, new java.sql.Timestamp(fin.getTime()));
            }
            if (idProveedor > 0) {
                pst.setInt(pos++, idProveedor);
            }
            //FUNCIONARIO
            if (idEmpleado > 0) {
                pst.setInt(pos++, idEmpleado);
            }
            //CONDICION DE COMPRA
            if (idTipoOoperacion > 0) {
                pst.setInt(pos++, idTipoOoperacion);
            }
            //NUMERO DE FACTURA
            if (nroFactura > 0) {
                pst.setInt(pos++, nroFactura);
            }
            //ESTADO
            if (idEstado > 0) {
                pst.setInt(pos, idEstado);
            }
            //CATEGORIAS
            if (idCategoria > 0) {
                pst.setInt(pos, idCategoria);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                proveedor.setEntidad(rs.getString(2));
                proveedor.setNombre(rs.getString(3));
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString(5));
                f.setApellido(rs.getString(6));
                f.setAlias(rs.getString(7));
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt(9));
                tiop.setDescripcion(rs.getString(10));
                Estado estado = new Estado();
                estado.setId(rs.getInt(11));
                estado.setDescripcion(rs.getString(12));
                M_egresoCabecera egresoCabecera = new M_egresoCabecera();
                egresoCabecera.setId_cabecera(rs.getInt(1));
                egresoCabecera.setNroFactura(rs.getInt(4));
                egresoCabecera.setCondCompra(tiop);
                egresoCabecera.setTiempo(rs.getTimestamp(8));
                egresoCabecera.setProveedor(proveedor);
                egresoCabecera.setFuncionario(f);
                egresoCabecera.setTotal(rs.getInt(13));
                egresoCabecera.setEstado(estado);
                list.add(egresoCabecera);
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
                + "EGDE.OBSERVACION \"Obs.\" "
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
        }
        /*finally {
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
            busqueda = "AND LOWER(PROD.DESCRIPCION) LIKE (?) ";
        } else {
            busqueda = "AND LOWER(EGDE.OBSERVACION) LIKE (?)";
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
            pst.setString(1, "%" + producto + "%");
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        /*finally {
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

    public static M_egresoCabecera obtenerEgresoCabeceraID(int idEgresoCabecera) {
        M_egresoCabecera egreso_cabecera = null;
        String query = "SELECT PROV.ID_PROVEEDOR, "
                + "PROV.ENTIDAD, "
                + "PROV.NOMBRE, "
                + "PROV.RUC, "
                + "PROV.RUC_IDENTIFICADOR, "
                + "PROV.DESCRIPCION, "
                + "PROV.EMAIL, "
                + "PROV.PAG_WEB, "
                + "PROV.OBSERVACION, "
                + "EGCA.ID_EGRESO_CABECERA, "
                + "EGCA.ID_PROVEEDOR, "
                + "EGCA.ID_FUNCIONARIO, "
                + "EGCA.TIEMPO, "
                + "EGCA.NRO_FACTURA, "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EGCA.ID_COND_COMPRA)\"EGCA.ID_COND_COMPRA\", "
                + "EGCA.ID_COND_COMPRA, "//16
                + "FUNC.ID_FUNCIONARIO, "
                + "FUNC.ID_PERSONA, "
                + "FUNC.ALIAS, "
                + "FUNC.FECHA_INGRESO, "
                + "FUNC.NRO_CELULAR, "
                + "FUNC.NRO_TELEFONO, "
                + "FUNC.EMAIL, "
                + "FUNC.DIRECCION, "
                + "FUNC.OBSERVACION, "
                + "PERS.NOMBRE, "
                + "PERS.APELLIDO "
                + "FROM EGRESO_CABECERA EGCA, PROVEEDOR PROV, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE EGCA.ID_PROVEEDOR = PROV.ID_PROVEEDOR "
                + "AND EGCA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND FUNC.ID_PERSONA = PERS.ID_PERSONA "
                + "AND EGCA.ID_EGRESO_CABECERA = " + idEgresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                M_funcionario funcionario = new M_funcionario();
                proveedor.setId(rs.getInt(1));
                proveedor.setEntidad(rs.getString(2));
                proveedor.setNombre(rs.getString(3));
                proveedor.setRuc(rs.getString(4));
                proveedor.setRuc_id(rs.getString(5));
                proveedor.setDescripcion(rs.getString(6));
                proveedor.setEmail(rs.getString(7));
                proveedor.setPagWeb(rs.getString(8));
                proveedor.setObservacion(rs.getString(9));
                funcionario.setIdFuncionario(rs.getInt(17));
                funcionario.setId_persona(rs.getInt(18));
                funcionario.setAlias(rs.getString(19));
                funcionario.setFecha_ingreso(rs.getDate(20));
                funcionario.setNro_celular(rs.getString(21));
                funcionario.setNro_telefono(rs.getString(22));
                funcionario.setEmail(rs.getString(23));
                funcionario.setDireccion(rs.getString(24));
                funcionario.setObservacion(rs.getString(25));
                funcionario.setNombre(rs.getString(26));
                funcionario.setApellido(rs.getString(27));
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt(16));
                tiop.setDescripcion(rs.getString(15));
                egreso_cabecera = new M_egresoCabecera();
                egreso_cabecera.setProveedor(proveedor);
                egreso_cabecera.setFuncionario(funcionario);
                egreso_cabecera.setId_cabecera(rs.getInt(10));
                egreso_cabecera.setId_proveedor(rs.getInt(11));;
                egreso_cabecera.setId_empleado(rs.getInt(12));
                egreso_cabecera.setTiempo(rs.getTimestamp(13));
                egreso_cabecera.setNroFactura(rs.getInt(14));
                egreso_cabecera.setCondVenta(rs.getString(15));
                egreso_cabecera.setId_condVenta(rs.getInt(16));
                egreso_cabecera.setCondCompra(tiop);
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
                egreso_detalle.setPrecio(rs.getDouble("PRECIO"));
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

    public static ArrayList<M_egreso_detalle> obtenerEgresoDetalles(Integer idEgresoCabecera) {
        ArrayList<M_egreso_detalle> detalles = null;
        String query = "SELECT ID_EGRESO_DETALLE, "
                + "ID_EGRESO_CABECERA, ID_PRODUCTO,"
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = ED.ID_PRODUCTO)\"PRODUCTO\", "
                + "(SELECT P.ID_IMPUESTO FROM PRODUCTO P WHERE P.ID_PRODUCTO = ED.ID_PRODUCTO)\"ID_IMPUESTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = ED.ID_PRODUCTO)\"CODIGO\", "
                + "CANTIDAD, "
                + "PRECIO, "
                + "DESCUENTO, "
                + "OBSERVACION "
                + "FROM EGRESO_DETALLE ED "
                + "WHERE ED.ID_EGRESO_CABECERA = " + idEgresoCabecera;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_egreso_detalle detalle = new M_egreso_detalle();
                detalle.setCantidad(rs.getDouble("CANTIDAD"));
                detalle.setDescuento(rs.getDouble("DESCUENTO"));
                detalle.setId_cabecera(rs.getInt("ID_EGRESO_CABECERA"));
                detalle.setId_detalle(rs.getInt("ID_EGRESO_DETALLE"));
                detalle.setObservacion(rs.getString("OBSERVACION"));
                detalle.setPrecio(rs.getDouble("PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                detalle.setProducto(producto);
                detalles.add(detalle);
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
        return detalles;
    }

    public static ArrayList<M_egreso_detalle> obtenerEgresoDetalles(Integer idEgresoCabecera, int idProductoCategoria) {
        ArrayList<M_egreso_detalle> detalles = null;
        String query = "SELECT ID_EGRESO_DETALLE, "
                + "ID_EGRESO_CABECERA, ED.ID_PRODUCTO, P.DESCRIPCION, P.ID_IMPUESTO, P.CODIGO , "
                + "CANTIDAD, "
                + "PRECIO, "
                + "DESCUENTO, "
                + "ED.OBSERVACION "
                + "FROM EGRESO_DETALLE ED, PRODUCTO P "
                + "WHERE ED.ID_PRODUCTO = P.ID_PRODUCTO "
                + "AND P.id_categoria = " + idProductoCategoria
                + " AND ED.ID_EGRESO_CABECERA = " + idEgresoCabecera;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_egreso_detalle detalle = new M_egreso_detalle();
                detalle.setCantidad(rs.getDouble("CANTIDAD"));
                detalle.setDescuento(rs.getDouble("DESCUENTO"));
                detalle.setId_cabecera(rs.getInt("ID_EGRESO_CABECERA"));
                detalle.setId_detalle(rs.getInt("ID_EGRESO_DETALLE"));
                detalle.setObservacion(rs.getString("OBSERVACION"));
                detalle.setPrecio(rs.getDouble("PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("DESCRIPCION"));
                producto.setCodigo(rs.getString("CODIGO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                detalle.setProducto(producto);
                detalles.add(detalle);
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
        return detalles;
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
        }
        /*finally {
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
//        String query = "SELECT SUM(ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))\"Total\" "
//                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
//                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
//                + "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
//                + "AND '" + fin + "'::timestamp "
//                + "AND EGCA.ID_COND_COMPRA = " + tipo_operacion;
        String query = "SELECT SUM(ROUND(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))\"Total\" "
                + "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA "
                + "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA "
                + "AND EGCA.TIEMPO BETWEEN ?  AND ? "
                + "AND EGCA.ID_COND_COMPRA = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(1, inicio);
            pst.setTimestamp(2, fin);
            pst.setInt(3, tipo_operacion);
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
        }
        /*finally {
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
    public static void insertarEgresoTEMPORAL(M_egresoCabecera egreso_cabecera, M_egreso_detalle[] egreso_detalle) {
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
                pst.setDouble(4, egreso_detalle[i].getPrecio());
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

    public static void insertarEgreso(M_egresoCabecera egreso_cabecera, ArrayList<M_egreso_detalle> detalles) {
        String INSERT_CABECERA = "INSERT INTO egreso_cabecera(nro_factura, id_proveedor, id_funcionario, tiempo, ID_COND_COMPRA, nro_timbrado, nro_sucursal, nro_punto_venta)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertDetalle = "INSERT INTO egreso_detalle(id_egreso_cabecera, id_producto, cantidad, precio, descuento, observacion)VALUES (?, ?, ?, ?, ?, ?)";
        long sq_egreso_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            if (egreso_cabecera.getNro_factura() < 1) {
                pst.setNull(1, Types.NUMERIC);
            } else {
                pst.setInt(1, egreso_cabecera.getNro_factura());
            }
            pst.setInt(2, egreso_cabecera.getProveedor().getId());
            pst.setInt(3, egreso_cabecera.getFuncionario().getIdFuncionario());
            pst.setTimestamp(4, egreso_cabecera.getTiempo());
            pst.setInt(5, egreso_cabecera.getId_condVenta());
            if (egreso_cabecera.getTimbrado().getNroTimbrado() < 1) {
                pst.setNull(6, Types.NUMERIC);
            } else {
                pst.setInt(6, egreso_cabecera.getTimbrado().getNroTimbrado());
            }
            if (egreso_cabecera.getTimbrado().getNroSucursal() < 1) {
                pst.setNull(7, Types.NUMERIC);
            } else {
                pst.setInt(7, egreso_cabecera.getTimbrado().getNroSucursal());
            }
            if (egreso_cabecera.getTimbrado().getNroPuntoVenta() < 1) {
                pst.setNull(8, Types.NUMERIC);
            } else {
                pst.setInt(8, egreso_cabecera.getTimbrado().getNroPuntoVenta());
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_egreso_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (M_egreso_detalle unDetalle : detalles) {
                pst = DB_manager.getConection().prepareStatement(insertDetalle);
                pst.setInt(1, (int) sq_egreso_cabecera);
                pst.setInt(2, unDetalle.getId_producto());
                pst.setDouble(3, unDetalle.getCantidad());
                pst.setDouble(4, unDetalle.getPrecio());
                pst.setDouble(5, unDetalle.getDescuento());
                pst.setString(6, unDetalle.getObservacion());
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

    public static boolean existeProveedorNroFactura(int idProveedor, int nroTimbrado, int nroSucursal, int nroPuntoVenta, int nroFactura) {
        String QUERY = "SELECT ID_EGRESO_CABECERA FROM EGRESO_CABECERA "
                + "WHERE ID_PROVEEDOR = ? "
                + "AND NRO_FACTURA = ? "
                + "AND NRO_TIMBRADO = ? "
                + "AND NRO_SUCURSAL = ? "
                + "AND NRO_PUNTO_VENTA = ? ";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY);
            pst.setInt(1, idProveedor);
            if (nroFactura < 1) {
                pst.setNull(2, Types.INTEGER);
            } else {
                pst.setInt(2, nroFactura);
            }
            if (nroTimbrado < 1) {
                pst.setNull(3, Types.INTEGER);
            } else {
                pst.setInt(3, nroTimbrado);
            }
            if (nroSucursal < 1) {
                pst.setNull(4, Types.INTEGER);
            } else {
                pst.setInt(4, nroSucursal);
            }
            if (nroPuntoVenta < 1) {
                pst.setNull(5, Types.INTEGER);
            } else {
                pst.setInt(5, nroPuntoVenta);
            }
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema verificando existencia de compra", "Error interno", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static List<M_egreso_detalle> consultarEgresoDetalleAgrupado(List<M_egresoCabecera> cadenaCabeceras) {
        List<M_egreso_detalle> list = new ArrayList<>();
        boolean b = true;
        List<M_egresoCabecera> possibleValues = cadenaCabeceras;
        StringBuilder builder = new StringBuilder();

        for (M_egresoCabecera seleccionVenta : possibleValues) {
            builder.append("?,");
        }
        String QUERY = "SELECT PROD.CODIGO \"Codigo\", "
                + "(SELECT IMPU.DESCRIPCION FROM IMPUESTO IMPU WHERE IMPU.ID_IMPUESTO = PROD.ID_IMPUESTO)\"IMPUESTO\","
                + "PROD.DESCRIPCION \"Producto\", SUM(FADE.CANTIDAD) \"Cantidad\", "
                + "PROD.ID_IMPUESTO \"ID_IMPUESTO\","
                + "FADE.PRECIO \"Precio\", "
                + "FADE.DESCUENTO \"Descuento\" "
                + "FROM EGRESO_DETALLE FADE, EGRESO_CABECERA FACA, PRODUCTO PROD "
                + "WHERE FADE.ID_EGRESO_CABECERA = FACA.ID_EGRESO_CABECERA "
                + "AND FADE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND FACA.ID_EGRESO_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY PROD.DESCRIPCION, PROD.CODIGO, PROD.ID_IMPUESTO, FADE.PRECIO, FADE.DESCUENTO,PROD.ID_IMPUESTO  "
                + "ORDER BY PROD.DESCRIPCION";
        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (M_egresoCabecera ventaCabecera : possibleValues) {
                pst.setInt(index++, ventaCabecera.getId_cabecera());
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_egreso_detalle fade = new M_egreso_detalle();
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
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static void anularCompra(int idCompra, int idEstado, boolean recuperarNroFact) {
        String UPDATE_VENTA = "UPDATE EGRESO_CABECERA SET ID_ESTADO = ? WHERE ID_EGRESO_CABECERA = ?";
        if (recuperarNroFact) {
            UPDATE_VENTA = "UPDATE EGRESO_CABECERA SET ID_ESTADO = ?, NRO_FACTURA = NULL WHERE ID_EGRESO_CABECERA = ?";
        }
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_VENTA);
            pst.setInt(1, idEstado);
            pst.setInt(2, idCompra);
            pst.executeUpdate();
            pst.close();
            //se devuelve al stock lo que se anuló
            ArrayList<M_egreso_detalle> detalle = obtenerEgresoDetalles(idCompra);
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

    public static List<M_egresoCabecera> obtenerMovimientoComprasCabeceras(int idFuncionario, int idProveedor, int idTipoOperacion, Date fechaInicio, Date fechaFinal) {
        List<M_egresoCabecera> list = new ArrayList<>();
        String Query = "SELECT EC.ID_EGRESO_CABECERA \"ID\", "
                + "EC.NRO_FACTURA \"NRO_FACTURA\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"EMPLEADO\", "
                + "(SELECT ENTIDAD FROM PROVEEDOR PV WHERE EC.ID_PROVEEDOR = PV.ID_PROVEEDOR) \"PROVEEDOR\", "
                + "TIEMPO, "
                + "(SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM EGRESO_DETALLE ED WHERE ED.ID_EGRESO_CABECERA = EC.ID_EGRESO_CABECERA)\"TOTAL\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EC.ID_COND_COMPRA) \"COND_COMPRA\", "
                + "EC.ID_COND_COMPRA \"ID_COND_COMPRA\" "
                + "FROM EGRESO_CABECERA EC ,FUNCIONARIO F, PERSONA P "
                + "WHERE  EC.TIEMPO BETWEEN ?  "
                + "AND ? "
                + "AND EC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND EC.ID_ESTADO = 1 "
                + "AND EC.ID_EGRESO_CABECERA NOT IN(select id_movimiento from caja_movimiento where id_movimiento_contable_tipo = 2 AND id_estado = 1)";

        if (idFuncionario > -1) {
            Query = Query + " AND EC.ID_FUNCIONARIO = ? ";
        }
        if (idProveedor > -1) {
            Query = Query + " AND EC.ID_PROVEEDOR = ? ";
        }
        if (idTipoOperacion > -1) {
            Query = Query + "AND EC.ID_COND_COMPRA = ? ";
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
            if (idProveedor > 0) {
                pst.setInt(pos, idProveedor);
                pos++;
            }
            if (idTipoOperacion > 0) {
                pst.setInt(pos, idTipoOperacion);
                pos++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                M_funcionario funcionario = new M_funcionario();
                funcionario.setNombre(rs.getString("EMPLEADO"));
                proveedor.setEntidad(rs.getString("PROVEEDOR"));
                M_egresoCabecera egca = new M_egresoCabecera();
                egca.setId_cabecera(rs.getInt("ID"));
                egca.setNroFactura(rs.getInt("NRO_FACTURA"));
                egca.setTotal(rs.getInt("TOTAL"));
                egca.setProveedor(proveedor);
                egca.setFuncionario(funcionario);
                egca.setId_condVenta(rs.getInt("ID_COND_COMPRA"));
                egca.setCondVenta(rs.getString("COND_COMPRA"));
                egca.setTiempo(rs.getTimestamp("TIEMPO"));
                list.add(egca);
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
        return list;
    }

    public static List<M_egresoCabecera> obtenerMovimientoComprasCabeceras(int idCaja) {
        List<M_egresoCabecera> list = new ArrayList<>();
        String Query = "SELECT EC.ID_EGRESO_CABECERA \"ID\", "
                + "EC.NRO_FACTURA \"NRO_FACTURA\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"EMPLEADO\", "
                + "(SELECT ENTIDAD FROM PROVEEDOR PV WHERE EC.ID_PROVEEDOR = PV.ID_PROVEEDOR) \"PROVEEDOR\", "
                + "(SELECT RUC FROM PROVEEDOR PV WHERE EC.ID_PROVEEDOR = PV.ID_PROVEEDOR) \"RUC_PROVEEDOR\", "
                + "(SELECT RUC_IDENTIFICADOR FROM PROVEEDOR PV WHERE EC.ID_PROVEEDOR = PV.ID_PROVEEDOR) \"RUC_ID_PROVEEDOR\", "
                + "TIEMPO, "
                + "(SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM EGRESO_DETALLE ED WHERE ED.ID_EGRESO_CABECERA = EC.ID_EGRESO_CABECERA)\"TOTAL\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = EC.ID_COND_COMPRA) \"COND_COMPRA\", "
                + "EC.ID_COND_COMPRA \"ID_COND_COMPRA\" "
                + "FROM EGRESO_CABECERA EC ,FUNCIONARIO F, PERSONA P "
                + "WHERE EC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND EC.ID_ESTADO = 1 "
                + "AND EC.ID_EGRESO_CABECERA IN(select id_movimiento from caja_movimiento where id_movimiento_contable_tipo = 2 AND id_caja =?)";
        Query = Query + " ORDER BY \"ID\"";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCaja);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_proveedor proveedor = new M_proveedor();
                M_funcionario funcionario = new M_funcionario();
                funcionario.setNombre(rs.getString("EMPLEADO"));
                proveedor.setEntidad(rs.getString("PROVEEDOR"));
                proveedor.setRuc(rs.getString("RUC_PROVEEDOR"));
                proveedor.setRuc_id(rs.getString("RUC_ID_PROVEEDOR"));
                M_egresoCabecera egca = new M_egresoCabecera();
                egca.setId_cabecera(rs.getInt("ID"));
                egca.setNroFactura(rs.getInt("NRO_FACTURA"));
                egca.setTotal(rs.getInt("TOTAL"));
                egca.setProveedor(proveedor);
                egca.setFuncionario(funcionario);
                egca.setId_condVenta(rs.getInt("ID_COND_COMPRA"));
                egca.setCondVenta(rs.getString("COND_COMPRA"));
                egca.setTiempo(rs.getTimestamp("TIEMPO"));
                list.add(egca);
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
        return list;
    }
}
