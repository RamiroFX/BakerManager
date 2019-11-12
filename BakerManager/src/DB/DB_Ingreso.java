/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_facturaCabeceraFX;
import Entities.E_facturaDetalleFX;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
import Entities.M_producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Ingreso {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    /*
     * READ
     */
    public static int obtenerUltimoNroFactura() {
        String Query = "SELECT nro_factura FROM factura_cabecera WHERE nro_factura IS NOT NULL  ORDER BY id_factura_cabecera DESC LIMIT 1;";
        long nroFactura = 0;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                nroFactura = rs.getInt(1);
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
        return (int) nroFactura;
    }

    public static ResultSetTableModel obtenerIngreso(String inicio, String fin, String tipo_operacion, M_facturaCabecera factura_cabecera, int idEstado) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT ID_FACTURA_CABECERA \"ID\", "
                + "NRO_FACTURA \"Nro. Factura\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"Empleado\", "
                + "(SELECT ENTIDAD FROM CLIENTE C WHERE FC.ID_CLIENTE = C.ID_CLIENTE) \"Cliente\", "
                + "to_char(TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "ROUND((SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM FACTURA_DETALLE FCC WHERE FCC.ID_FACTURA_CABECERA = FC.ID_FACTURA_CABECERA))\"Total\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = FC.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM FACTURA_CABECERA FC ,FUNCIONARIO F, PERSONA P "
                + "WHERE  FC.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND FC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA ";
        if (null != factura_cabecera) {
            if (null != factura_cabecera.getNroFactura()) {
                Query = Query + " AND FC.NRO_FACTURA= " + factura_cabecera.getNroFactura();
            }
        }
        if (!"Todos".equals(tipo_operacion)) {
            Query = Query + " AND FC.ID_COND_VENTA = (SELECT TIOP.ID_TIPO_OPERACION FROM TIPO_OPERACION TIOP WHERE TIOP.DESCRIPCION LIKE'" + tipo_operacion + "')";
        }
        if (null != factura_cabecera) {
            if (null != factura_cabecera.getCliente()) {
                Query = Query + " AND FC.ID_CLIENTE = " + factura_cabecera.getCliente().getIdCliente();
            }
            if (null != factura_cabecera.getFuncionario()) {
                Query = Query + " AND FC.ID_FUNCIONARIO = " + factura_cabecera.getFuncionario().getId_funcionario();
            }
        }
        if (idEstado != Estado.TODOS) {
            Query = Query + " AND FC.ID_ESTADO = " + idEstado;
        }
        Query = Query + " ORDER BY \"ID\"";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ArrayList<E_facturaCabeceraFX> obtenerVentaCabeceras(String clienteEntidad,
            Integer nro_factura, String idEmpleado, String inicio, String fin,
            String tipo_operacion, Estado estado) {
        String fromQuery = "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA,FUNCIONARIO FUNC, PERSONA PERS, CLIENTE CLIE, PRODUCTO P ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND FACA.TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND FACA.TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND FACA.TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(clienteEntidad)) {
            prov = "";
        } else {
            prov = " AND CLIE.ID_CLIENTE = FACA.ID_CLIENTE AND CLIE.ENTIDAD LIKE'" + clienteEntidad + "' ";
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
            tiop = " AND FACA.ID_COND_VENTA = " + tipo_operacion;
        }
        String esta;
        if (estado.getId() == Estado.TODOS) {
            esta = "";
        } else {
            esta = " AND FACA.ID_ESTADO = " + estado.getId();
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND FACA.NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT FACA.ID_FACTURA_CABECERA, "
                + "FACA.NRO_FACTURA, "
                + "ROUND(SUM (FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"TOTAL\", "
                + "CLIE.ENTIDAD, "
                + "FACA.TIEMPO, "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = FACA.ID_COND_VENTA) \"COND_VENTA\" "
                + fromQuery
                + "WHERE FACA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND FACA.ID_CLIENTE = CLIE.ID_CLIENTE "
                + "AND FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FADE.ID_PRODUCTO = P.ID_PRODUCTO "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + esta
                + numero_fac
                + "GROUP BY FACA.ID_FACTURA_CABECERA, FACA.NRO_FACTURA, CLIE.ENTIDAD, FACA.TIEMPO "
                + " ORDER BY FACA.TIEMPO";
        ArrayList result = new ArrayList();
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            while (rs.next()) {
                E_facturaCabeceraFX fadex = new E_facturaCabeceraFX();
                fadex.setIdFacturaCabecera(rs.getInt("ID_FACTURA_CABECERA"));
                fadex.setNroFactura(rs.getInt("NRO_FACTURA"));
                fadex.setTotal(rs.getInt("TOTAL"));
                fadex.setClienteEntidad(rs.getString("ENTIDAD"));
                fadex.setCondVenta(rs.getString("COND_VENTA"));
                fadex.setTiempo(rs.getDate("TIEMPO"));
                result.add(fadex);
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
        return result;
    }

    public static ResultSetTableModel obtenerIngresoCabecera(Integer idIngresoDetalle) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT FACA.ID_FACTURA_CABECERA \"ID ingreso\", "
                + "FACA.NRO_FACTURA \"Nro. Factura\", "
                + "(SELECT CLIE.ENTIDAD FROM CLIENTE CLIE WHERE CLIE.ID_CLIENTE = FACA.ID_CLIENTE)\"Cliente\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "FACA.TIEMPO \"Tiempo\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = FACA.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM FACTURA_CABECERA FACA, FACTURA_DETALLE FADE, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE FACA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FADE.ID_FACTURA_DETALLE = " + idIngresoDetalle;
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

    /*
     * INSERT
     */
    public static int insertarIngreso(M_facturaCabecera cabecera, ArrayList<M_facturaDetalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA, NRO_FACTURA)VALUES (?, ?, ?, ?);";
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
                pst.setInt(4, detalle.get(i).getPrecio());
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
        return (int) sq_cabecera;
    }

    public static M_facturaCabecera obtenerIngresoCabeceraID(Integer idIngresoCabecera) {
        M_facturaCabecera ingreso_cabecera = null;
        String query = "SELECT ID_FACTURA_CABECERA, "
                + "ID_FUNCIONARIO, "
                + "ID_CLIENTE, "
                + "TIEMPO, "
                + "ID_COND_VENTA, "
                + "NRO_FACTURA "
                + "FROM FACTURA_CABECERA "
                + "WHERE ID_FACTURA_CABECERA = " + idIngresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                ingreso_cabecera = new M_facturaCabecera();
                ingreso_cabecera.setIdFacturaCabecera(rs.getInt("ID_FACTURA_CABECERA"));
                ingreso_cabecera.setIdCliente(rs.getInt("ID_CLIENTE"));
                ingreso_cabecera.setIdCondVenta(rs.getInt("ID_COND_VENTA"));
                ingreso_cabecera.setIdFuncionario(rs.getInt("ID_FUNCIONARIO"));
                ingreso_cabecera.setNroFactura(rs.getInt("NRO_FACTURA"));
                ingreso_cabecera.setTiempo(rs.getTimestamp("TIEMPO"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return ingreso_cabecera;
    }

    public static M_facturaCabecera obtenerIngresoCabeceraCompleto(Integer idIngresoCabecera) {
        M_facturaCabecera fc = null;
        String q_cliente = "C.ID_CLIENTE, C.NOMBRE, C.ENTIDAD, C.RUC, C.RUC_IDENTIFICADOR, C.DIRECCION, C.EMAIL, C.PAG_WEB, C.OBSERVACION, ";
        String q_tipo = "(SELECT CLTI.DESCRIPCION FROM CLIENTE_TIPO CLTI WHERE CLTI.ID_CLIENTE_TIPO = C.ID_TIPO) \"TIPO\", ";
        String q_categoria = "(SELECT CLCA.DESCRIPCION FROM CLIENTE_CATEGORIA CLCA WHERE CLCA.ID_CLIENTE_CATEGORIA = C.ID_CATEGORIA) \"CATEGORIA\", ";
        String query = "SELECT "
                + q_cliente
                + q_tipo
                + q_categoria
                + "FC.ID_FACTURA_CABECERA, "//12
                + "FC.ID_FUNCIONARIO, "//13
                + "FC.ID_CLIENTE, "//14
                + "FC.TIEMPO, "//15
                + "FC.ID_COND_VENTA, "//16
                + "FC.NRO_FACTURA, "//17
                + "(SELECT NOMBRE FROM PERSONA WHERE PERSONA.ID_PERSONA = F.ID_PERSONA)\"NOMBRE_FUNCIONARIO\" "//18
                + "FROM FACTURA_CABECERA FC, CLIENTE C, FUNCIONARIO F "
                + "WHERE ID_FACTURA_CABECERA = " + idIngresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_cliente cliente = new M_cliente();
                cliente.setIdCliente(rs.getInt(1));
                cliente.setNombre(rs.getString(2));
                cliente.setEntidad(rs.getString(3));
                cliente.setRuc(rs.getString(4));
                cliente.setRucId(rs.getString(5));
                cliente.setDireccion(rs.getString(6));
                cliente.setEmail(rs.getString(7));
                cliente.setPaginaWeb(rs.getString(8));
                cliente.setObservacion(rs.getString(9));
                cliente.setTipo(rs.getString(10));
                cliente.setCategoria(rs.getString(11));
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString(18));
                fc = new M_facturaCabecera();
                fc.setIdFacturaCabecera(rs.getInt(12));
                fc.setIdFuncionario(rs.getInt(13));
                fc.setIdCliente(rs.getInt(14));
                fc.setTiempo(rs.getTimestamp(15));
                fc.setIdCondVenta(rs.getInt(16));
                fc.setNroFactura(rs.getInt(17));
                fc.setCliente(cliente);
                fc.setFuncionario(f);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return fc;
    }

    public static ResultSetTableModel consultarIngresoDetalleAgrupado(Timestamp inicio, Timestamp fin, M_cliente cliente, Estado estado) {
        int pos = 1;
        String QUERY = "SELECT PROD.DESCRIPCION \"Producto\", SUM(FADE.CANTIDAD) \"Cantidad\", FADE.PRECIO \"Precio\", FADE.DESCUENTO \"Descuento\", "
                + "CASE WHEN PROD.ID_IMPUESTO = 1 THEN SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))) ELSE '0' END AS \"Exenta\", "
                + "CASE WHEN PROD.ID_IMPUESTO = 2 THEN SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))) ELSE '0' END AS \"IVA 5%\", "
                + "CASE WHEN PROD.ID_IMPUESTO = 3 THEN SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))) ELSE '0' END AS \"IVA 10%\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA, PRODUCTO PROD "
                + "WHERE FADE.ID_FACTURA_CABECERA = FACA.ID_FACTURA_CABECERA "
                + "AND FADE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND FACA.TIEMPO BETWEEN ? AND ? ";

        String PIE = "GROUP BY PROD.DESCRIPCION, FADE.PRECIO, FADE.DESCUENTO,PROD.ID_IMPUESTO "
                + "ORDER BY PROD.DESCRIPCION";
        if (cliente != null) {
            if (cliente.getIdCliente() != null) {
                QUERY = QUERY + "AND FACA.ID_CLIENTE = ? ";
            }
        }
        if (estado.getId() != Estado.TODOS) {
            QUERY = QUERY + "AND FACA.ID_ESTADO = ? ";
        }
        QUERY = QUERY + PIE;
        ResultSetTableModel rstm = null;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(pos, inicio);
            pos++;
            pst.setTimestamp(pos, fin);
            pos++;
            if (cliente != null) {
                if (cliente.getIdCliente() != null) {
                    pst.setInt(pos, cliente.getIdCliente());
                    pos++;
                }
            }
            if (estado.getId() != Estado.TODOS) {
                pst.setInt(pos, estado.getId());
            }
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerIngresoDetalle(Integer idIngresoCabecera) {
        String Query = "SELECT "
                + "FD.ID_PRODUCTO \"ID art.\", "
                + "P.DESCRIPCION \"Producto\", "
                + "FD.CANTIDAD \"Cantidad\", "
                + "FD.PRECIO \"Precio\", "
                + "FD.DESCUENTO \"Descuento\","
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 1 THEN ROUND(FD.CANTIDAD*(FD.PRECIO-(FD.PRECIO*FD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 2 THEN ROUND(FD.CANTIDAD*(FD.PRECIO-(FD.PRECIO*FD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 3 THEN ROUND(FD.CANTIDAD*(FD.PRECIO-(FD.PRECIO*FD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 10%\", "
                + "FD.OBSERVACION \"Obs.\" "
                + "FROM FACTURA_DETALLE FD, PRODUCTO P "
                + "WHERE FD.ID_PRODUCTO = P.ID_PRODUCTO "
                + "AND ID_FACTURA_CABECERA = " + idIngresoCabecera;
        ResultSetTableModel rstm = null;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static Integer obtenerTotalIngreso(Timestamp inicio, Timestamp fin, int tipo_operacion, int idEstado) {
        Integer totalEgreso = 0;
        String query = "SELECT SUM(ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100)))\"Total\" "
                + "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA "
                + "WHERE FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FACA.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND FACA.ID_COND_VENTA = " + tipo_operacion;
        if (idEstado != 3) {
            query = query + " AND FACA.ID_ESTADO = " + idEstado;
        }
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

    public static ResultSetTableModel obtenerMesa(String inicio, String fin, String tipo_operacion) {
        ResultSetTableModel rstm = null;
        String q = "SELECT M.ID_MESA \"ID\", (SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"Empleado\", "
                + "(SELECT C.ENTIDAD FROM CLIENTE C WHERE C.ID_CLIENTE = M.ID_CLIENTE) \"Cliente\", "
                + "to_char(TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "MESA_NUMERO \"Nro. mesa\", "
                + "ROUND((SELECT SUM(MD.CANTIDAD*(MD.PRECIO-(MD.PRECIO*MD.DESCUENTO)/100)) FROM MESA_DETALLE MD WHERE MD.ID_MESA = M.ID_MESA))\"Total\" "
                + "FROM MESA M,FUNCIONARIO F, PERSONA P "
                + "WHERE M.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA ";
        String tiempo = "AND M.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp ";
        if ((!inicio.isEmpty() && !fin.isEmpty())) {
            q = q + tiempo;
        }
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(q);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerMesaDetalle(int idMesa) {
        String Query = "SELECT "
                + "MD.ID_MESA_DETALLE \"ID\", "
                + "MD.ID_PRODUCTO \"ID art.\", "
                + "P.DESCRIPCION  \"Producto\", "
                + "MD.CANTIDAD \"Cantidad\", "
                + "MD.PRECIO \"Precio\", "
                + "MD.DESCUENTO \"Descuento\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 1 THEN round(MD.CANTIDAD*(MD.PRECIO-(MD.PRECIO*MD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 2 THEN round(MD.CANTIDAD*(MD.PRECIO-(MD.PRECIO*MD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 3 THEN round(MD.CANTIDAD*(MD.PRECIO-(MD.PRECIO*MD.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 10%\", "
                + "MD.OBSERVACION \"Obs.\" "
                + "FROM MESA_DETALLE MD, PRODUCTO P "
                + "WHERE  P.ID_PRODUCTO = MD.ID_PRODUCTO "
                + "AND MD.ID_MESA = " + idMesa;
        ResultSetTableModel rstm = null;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static long insertarMesa(M_mesa mesa, ArrayList<M_mesa_detalle> detalle) {
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETAIL = "INSERT INTO MESA_DETALLE(ID_MESA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        String INSERT_MESA = "INSERT INTO MESA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA, MESA_NUMERO)VALUES (?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_MESA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, mesa.getFuncionario().getId_funcionario());
            pst.setInt(2, mesa.getCliente().getIdCliente());
            pst.setInt(3, mesa.getIdCondVenta());
            pst.setInt(4, mesa.getNumeroMesa());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < detalle.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_DETAIL);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, detalle.get(i).getProducto().getId());
                pst.setDouble(3, detalle.get(i).getCantidad());
                pst.setInt(4, detalle.get(i).getPrecio());
                pst.setDouble(5, detalle.get(i).getDescuento());
                try {
                    if (detalle.get(i).getObservacion() == null) {
                        pst.setNull(6, Types.VARCHAR);
                    } else {
                        pst.setString(6, detalle.get(i).getObservacion());
                    }
                } catch (Exception e) {
                    pst.setNull(6, Types.VARCHAR);
                }
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
        return sq_cabecera;
    }

    public static M_mesa obtenerMesaID(Integer idMesa) {
        M_mesa mesa = null;
        String genero = "(SELECT S.DESCRIPCION  FROM SEXO S WHERE S.ID_SEXO = P.ID_SEXO) \"sexo\"";
        String pais = "(SELECT PA.DESCRIPCION FROM PAIS PA WHERE PA.ID_PAIS = P.ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT CI.DESCRIPCION FROM CIUDAD CI WHERE CI.ID_CIUDAD = P.ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT EC.DESCRIPCION FROM ESTADO_CIVIL EC WHERE EC.ID_ESTADO_CIVIL = P.ID_ESTADO_CIVIL)\"estado_civil\"";

        String categoria = "(SELECT CLCA.DESCRIPCION FROM CLIENTE_CATEGORIA CLCA WHERE CLCA.ID_CLIENTE_CATEGORIA = C.ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI.DESCRIPCION FROM CLIENTE_TIPO CLTI WHERE CLTI.ID_CLIENTE_TIPO = C.ID_TIPO) \"TIPO\" ";

        String q = "SELECT M.ID_MESA, M.ID_FUNCIONARIO, M.ID_CLIENTE, M.TIEMPO,M.MESA_NUMERO, "
                + "       M.ID_COND_VENTA, "
                + "C.ID_CLIENTE, C.NOMBRE \"CNOMBRE\", C.ENTIDAD, C.RUC, C.RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       C.DIRECCION \"CDIRECCION\", C.EMAIL \"CEMAIL\", C.PAG_WEB, C.ID_TIPO, C.ID_CATEGORIA, "
                + "       C.OBSERVACION \"COBSERVACION\", "
                + "F.ID_FUNCIONARIO, F.ID_PERSONA, F.ALIAS, F.FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estadoCivil + ","
                + "      F.NRO_CELULAR, "
                + "       F.NRO_TELEFONO\"FNRO_TELEFONO\", F.EMAIL \"FEMAIL\", F.DIRECCION \"FDIRECCION\", F.OBSERVACION \"FOBSERVACION\",P.ID_PERSONA, P.CI, P.NOMBRE \"FNOMBRE\", P.APELLIDO, P.ID_SEXO, "
                + "       P.FECHA_NACIMIENTO, P.ID_ESTADO_CIVIL, P.ID_PAIS, P.ID_CIUDAD "
                + "  FROM MESA M,FUNCIONARIO F,CLIENTE C,PERSONA P "
                + "  WHERE M.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND M.ID_CLIENTE = C.ID_CLIENTE "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND M.ID_MESA = " + idMesa;
        try {
            pst = DB_manager.getConection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setPais(rs.getString("NACIONALIDAD"));
                f.setCiudad(rs.getString("CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("sexo"));
                f.setNro_celular(rs.getString("nro_celular"));
                f.setNro_telefono(rs.getString("FNRO_TELEFONO"));
                f.setEmail(rs.getString("FEMAIL"));
                f.setDireccion(rs.getString("FDIRECCION"));
                f.setAlias(rs.getString("alias"));
                f.setNombre(rs.getString("FNOMBRE"));
                f.setApellido(rs.getString("apellido"));
                f.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                f.setId_persona(rs.getInt("id_persona"));
                f.setCedula(rs.getInt("ci"));
                f.setEstado_civil(rs.getString("estado_civil"));
                f.setId_funcionario(rs.getInt("id_funcionario"));
                f.setObservacion(rs.getString("FOBSERVACION"));

                M_cliente cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("CDIRECCION"));
                cliente.setEmail(rs.getString("CEMAIL"));
                cliente.setEntidad(rs.getString("ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("ID_TIPO"));
                cliente.setNombre(rs.getString("CNOMBRE"));
                cliente.setObservacion(rs.getString("COBSERVACION"));
                cliente.setPaginaWeb(rs.getString("PAG_WEB"));
                cliente.setRuc(rs.getString("RUC"));
                cliente.setRucId(rs.getString("RUC_IDENTIFICADOR"));
                cliente.setTipo(rs.getString("TIPO"));

                mesa = new M_mesa();
                mesa.setIdCondVenta(rs.getInt("ID_COND_VENTA"));
                mesa.setIdMesa(rs.getInt("ID_MESA"));
                mesa.setNumeroMesa(rs.getInt("MESA_NUMERO"));
                mesa.setTiempo(rs.getTimestamp("TIEMPO"));
                mesa.setFuncionario(f);
                mesa.setCliente(cliente);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return mesa;
    }

    public static void actualizarMesa(M_mesa mesa) {
        String UPDATE_MESA = "UPDATE MESA SET ID_FUNCIONARIO = ?, ID_CLIENTE = ?, MESA_NUMERO = ?, ID_COND_VENTA = ? WHERE ID_MESA = ?";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_MESA);
            pst.setInt(1, mesa.getFuncionario().getId_funcionario());
            pst.setInt(2, mesa.getCliente().getIdCliente());
            pst.setInt(3, mesa.getNumeroMesa());
            pst.setInt(4, mesa.getIdCondVenta());
            pst.setInt(5, mesa.getIdMesa());
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void actualizarMesaDetalle(M_mesa_detalle mesaDetalle) {
        String UPDATE_MESA = "UPDATE MESA_DETALLE SET "
                + "CANTIDAD= ?, "
                + "PRECIO= ?, "
                + "DESCUENTO= ?, "
                + "OBSERVACION= ? "
                + "WHERE ID_MESA_DETALLE = ?";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_MESA);
            pst.setDouble(1, mesaDetalle.getCantidad());
            pst.setInt(2, mesaDetalle.getPrecio());
            pst.setDouble(3, mesaDetalle.getDescuento());
            if (mesaDetalle.getObservacion() == null) {
                pst.setNull(4, Types.NULL);
            } else {
                pst.setString(4, mesaDetalle.getObservacion());
            }
            pst.setInt(5, mesaDetalle.getIdMesaDetalle());
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarMesa(int idMesa) {
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE ID_MESA = " + idMesa;
        String DELETE_HEADER = "DELETE FROM MESA WHERE ID_MESA = " + idMesa;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_DETAIL);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_HEADER);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarMesaDetalle(int idMesaDetalle) {
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE ID_MESA_DETALLE = " + idMesaDetalle;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_DETAIL);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean estaLibreMesa(int numeroMesa) {
        String QUERY = "SELECT MESA_NUMERO FROM MESA WHERE MESA_NUMERO = " + numeroMesa;
        try {
            st = DB_manager.getConection().createStatement();
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(QUERY);
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void insertarMesaDetalle(int idMesa, M_mesa_detalle mesaDetalle) {
        String INSERT_DETALLE = "INSERT INTO MESA_DETALLE(ID_MESA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
            pst.setInt(1, idMesa);
            pst.setInt(2, mesaDetalle.getProducto().getId());
            pst.setDouble(3, mesaDetalle.getCantidad());
            pst.setInt(4, mesaDetalle.getPrecio());
            pst.setDouble(5, mesaDetalle.getDescuento());
            try {
                if (mesaDetalle.getObservacion() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, mesaDetalle.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            pst.executeUpdate();
            pst.close();
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
    }

    public static int transferirMesaAVenta(M_mesa mesa, ArrayList<M_mesa_detalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA)VALUES (?, ?, ?);";
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE ID_MESA = " + mesa.getIdMesa();
        String DELETE_HEADER = "DELETE FROM MESA WHERE ID_MESA = " + mesa.getIdMesa();
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, mesa.getFuncionario().getId_funcionario());
            pst.setInt(2, mesa.getCliente().getIdCliente());
            pst.setInt(3, mesa.getIdCondVenta());
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
                pst.setInt(2, detalle.get(i).getProducto().getId());
                pst.setDouble(3, detalle.get(i).getCantidad());
                pst.setInt(4, detalle.get(i).getPrecio());
                pst.setDouble(5, detalle.get(i).getDescuento());
                try {
                    if (detalle.get(i).getObservacion() == null) {
                        pst.setNull(6, Types.VARCHAR);
                    } else {
                        pst.setString(6, detalle.get(i).getObservacion());
                    }
                } catch (Exception e) {
                    pst.setNull(6, Types.VARCHAR);
                }
                pst.executeUpdate();
                pst.close();
            }
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_DETAIL);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_HEADER);
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
        return (int) sq_cabecera;
    }

    public static ArrayList<M_facturaDetalle> obtenerVentaDetalles(Integer idFacturaCabecera) {
        ArrayList<M_facturaDetalle> detalles = null;
        String query = "SELECT ID_FACTURA_DETALLE, "
                + "ID_FACTURA_CABECERA, ID_PRODUCTO,"
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = FD.ID_PRODUCTO)\"PRODUCTO\", "
                + "(SELECT P.ID_IMPUESTO FROM PRODUCTO P WHERE P.ID_PRODUCTO = FD.ID_PRODUCTO)\"ID_IMPUESTO\", "
                + "CANTIDAD, "
                + "PRECIO, "
                + "DESCUENTO, "
                + "OBSERVACION "
                + "FROM FACTURA_DETALLE FD "
                + "WHERE FD.ID_FACTURA_CABECERA = " + idFacturaCabecera;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_facturaDetalle detalle = new M_facturaDetalle();
                detalle.setCantidad(rs.getDouble("CANTIDAD"));
                detalle.setDescuento(rs.getDouble("DESCUENTO"));
                detalle.setIdFacturaCabecera(rs.getInt("ID_FACTURA_CABECERA"));
                detalle.setIdFacturaDetalle(rs.getInt("ID_FACTURA_DETALLE"));
                detalle.setObservacion(rs.getString("OBSERVACION"));
                detalle.setPrecio(rs.getInt("PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
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

    public static ArrayList<E_facturaDetalleFX> obtenerVentaDetalles(String clienteEntidad,
            Integer nro_factura, String idEmpleado, String inicio, String fin,
            String tipo_operacion, Estado estado) {
        String fromQuery = "FROM FACTURA_DETALLE FADE, FACTURA_CABECERA FACA,FUNCIONARIO FUNC, PERSONA PERS, CLIENTE CLIE, PRODUCTO P ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(inicio)) {
            fInicio = "";
            if ("Todos".equals(fin)) {
                fFinal = "";
            } else {
                fFinal = " AND FACA.TIEMPO <'" + fin + "'::timestamp ";
            }
        } else {
            fInicio = "AND FACA.TIEMPO BETWEEN '" + inicio + "'::timestamp  ";
            fFinal = "AND '" + fin + "'::timestamp ";
            if ("Todos".equals(fin)) {
                fInicio = "AND FACA.TIEMPO > '" + inicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(clienteEntidad)) {
            prov = "";
        } else {
            prov = " AND CLIE.ID_CLIENTE = FACA.ID_CLIENTE AND CLIE.ENTIDAD LIKE'" + clienteEntidad + "' ";
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
            tiop = " AND FACA.ID_COND_VENTA = " + tipo_operacion;
        }
        String esta;
        if (estado.getId() == Estado.TODOS) {
            esta = "";
        } else {
            esta = " AND FACA.ID_ESTADO = " + estado.getId();
        }
        String numero_fac = "";
        try {
            if (nro_factura != null) {
                numero_fac = " AND FACA.NRO_FACTURA = " + nro_factura;
            } else {
                numero_fac = "";
            }
        } catch (Exception e) {
            numero_fac = "";
        }
        String Query = "SELECT FADE.ID_FACTURA_DETALLE, "
                + "FADE.ID_PRODUCTO , "
                + "P.DESCRIPCION  \"PRODUCTO\", "
                + "FADE.CANTIDAD , "
                + "FADE.PRECIO , "
                + "FADE.DESCUENTO, "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 1 THEN ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"EXENTA\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 2 THEN ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"CINCO\", "
                + "CASE "
                + "	WHEN P.ID_IMPUESTO = 3 THEN ROUND(FADE.CANTIDAD*(FADE.PRECIO-(FADE.PRECIO*FADE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"DIEZ\", "
                + "(FADE.CANTIDAD*FADE.PRECIO ) \"TOTAL\", "
                + "CLIE.ENTIDAD, "
                + "FADE.OBSERVACION, "
                + "FACA.tiempo, "
                + "FACA.ID_FACTURA_CABECERA "
                + fromQuery
                + "WHERE FACA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND FACA.ID_CLIENTE = CLIE.ID_CLIENTE "
                + "AND FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA "
                + "AND FADE.ID_PRODUCTO = P.ID_PRODUCTO "
                + prov
                + fInicio
                + fFinal
                + empleado
                + tiop
                + esta
                + numero_fac
                + " ORDER BY FACA.TIEMPO";
        ArrayList facturaDetalles = null;
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            facturaDetalles = new ArrayList();
            E_facturaDetalleFX fadex;
            while (rs.next()) {
                fadex = new E_facturaDetalleFX();
                fadex.setCantidad(rs.getDouble("CANTIDAD"));
                fadex.setDescuento(rs.getDouble("DESCUENTO"));
                fadex.setIdFacturaCabecera(rs.getInt("ID_FACTURA_CABECERA"));
                fadex.setIdFacturaDetalle(rs.getInt("ID_FACTURA_DETALLE"));
                fadex.setProductoDescripcion(rs.getString("Producto"));
                fadex.setIva5(rs.getInt("CINCO"));
                fadex.setIva10(rs.getInt("DIEZ"));
                fadex.setExenta(rs.getInt("EXENTA"));
                fadex.setPrecio(rs.getInt("PRECIO"));
                fadex.setTotal(rs.getInt("TOTAL"));
                fadex.setClienteEntidad(rs.getString("ENTIDAD"));
                fadex.setObservacion(rs.getString("OBSERVACION"));
                fadex.setTiempo(rs.getDate("tiempo"));
                facturaDetalles.add(fadex);
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
        return facturaDetalles;
    }

    public static ArrayList<M_mesa_detalle> obtenerMesaDetalles(Integer idMesa) {
        ArrayList<M_mesa_detalle> detalles = null;
        String query = "SELECT ID_MESA_DETALLE, ID_MESA, ID_PRODUCTO,(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = MD.ID_PRODUCTO)\"PRODUCTO\", CANTIDAD, PRECIO, DESCUENTO, OBSERVACION FROM MESA_DETALLE MD WHERE MD.ID_MESA = " + idMesa;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_mesa_detalle detalle = new M_mesa_detalle();
                detalle.setCantidad(rs.getDouble("CANTIDAD"));
                detalle.setDescuento(rs.getDouble("DESCUENTO"));
                detalle.setMesa(new M_mesa());
                detalle.getMesa().setIdMesa(rs.getInt("ID_MESA"));
                detalle.setIdMesaDetalle(rs.getInt("ID_MESA_DETALLE"));
                detalle.setObservacion(rs.getString("OBSERVACION"));
                detalle.setPrecio(rs.getInt("PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
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

    public static ResultSetTableModel obtenerIngresoDetalleAvanzado(String producto, String cliente, String marca, String impuesto, String categoria, String estado, String fechaInicio, String fechaFinal, String tipo_operacion, String idEmpleado, boolean busqDescripcion) {
        ResultSetTableModel rstm = null;
        String fromQuery = "FROM FACTURA_DETALLE FADE, PRODUCTO PROD, FACTURA_CABECERA FACA ";
        String whereQuery = "WHERE FADE.ID_PRODUCTO = PROD.ID_PRODUCTO AND FACA.ID_FACTURA_CABECERA = FADE.ID_FACTURA_CABECERA ";
        String fInicio = "";
        String fFinal;
        if ("Todos".equals(fechaInicio)) {
            fInicio = "";
            if ("Todos".equals(fechaFinal)) {
                fFinal = "";
            } else {
                fFinal = " AND FACA.TIEMPO <'" + fechaFinal + "'::timestamp ";
            }
        } else {
            fInicio = "AND FACA.TIEMPO BETWEEN '" + fechaInicio + "'::timestamp  ";
            fFinal = "AND '" + fechaFinal + "'::timestamp ";
            if ("Todos".equals(fechaFinal)) {
                fInicio = "AND FACA.TIEMPO > '" + fechaInicio + "'::timestamp ";
                fFinal = "";
            }
        }
        String prov;
        if ("Todos".equals(cliente)) {
            prov = "";
        } else {
            fromQuery = fromQuery + ", CLIENTE CLIE ";
            whereQuery = whereQuery + " AND CLIE.ID_CLIENTE = FACA.ID_CLIENTE ";
            prov = " AND CLIE.ENTIDAD LIKE '" + cliente + "' ";
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
            whereQuery = whereQuery + " AND PERS.ID_PERSONA = FUNC.ID_PERSONA AND FACA.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO ";
            empleado = " AND FUNC.ID_FUNCIONARIO = " + idEmpleado;
        }

        String tiop;
        if ("Todos".equals(tipo_operacion)) {
            tiop = "";
        } else {
            tiop = " AND FACA.ID_COND_VENTA = " + tipo_operacion;
        }

        String busqueda;
        if (busqDescripcion) {
            busqueda = "AND LOWER(PROD.DESCRIPCION) LIKE LOWER (?) ESCAPE '!' ";
        } else {
            busqueda = "AND LOWER(FADE.OBSERVACION) LIKE LOWER (?) ESCAPE '!' ";
        }

        String Query = "SELECT FADE.ID_FACTURA_DETALLE\"ID det.\", "
                + "(SELECT PROD.DESCRIPCION FROM PRODUCTO PROD WHERE PROD.ID_PRODUCTO = FADE.ID_PRODUCTO) \"Producto\", "
                + "FADE.OBSERVACION \"Obs.\",FADE.CANTIDAD \"Cantidad\", FADE.PRECIO \"Precio\", (FADE.PRECIO*FADE.CANTIDAD)\"Total\", FACA.TIEMPO \"Tiempo\" "
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
        }
        return rstm;
    }

    public static boolean nroFacturaEnUso(int nroFactura) {
        String QUERY = "SELECT nro_factura FROM factura_cabecera WHERE nro_factura = " + nroFactura;
        try {
            st = DB_manager.getConection().createStatement();
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(QUERY);
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void anularVenta(int idVenta, int idEstado, boolean recuperarNroFact) {
        String UPDATE_VENTA = "UPDATE FACTURA_CABECERA SET ID_ESTADO = ? WHERE ID_FACTURA_CABECERA = ?";
        if (recuperarNroFact) {
            UPDATE_VENTA = "UPDATE FACTURA_CABECERA SET ID_ESTADO = ?, NRO_FACTURA = NULL WHERE ID_FACTURA_CABECERA = ?";
        }
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_VENTA);
            pst.setInt(1, idEstado);
            pst.setInt(2, idVenta);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static ResultSetTableModel obtenerCobro(M_cliente cliente, M_funcionario funcionario, String fechaInicio, String fechaFinal, E_tipoOperacion condVenta, String nroFactura) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT ID_FACTURA_CABECERA \"ID\", "
                + "NRO_FACTURA \"Nro. Factura\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"Empleado\", "
                + "(SELECT ENTIDAD FROM CLIENTE C WHERE FC.ID_CLIENTE = C.ID_CLIENTE) \"Cliente\", "
                + "to_char(TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "ROUND((SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM FACTURA_DETALLE FCC WHERE FCC.ID_FACTURA_CABECERA = FC.ID_FACTURA_CABECERA))\"Total\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = FC.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM FACTURA_CABECERA FC ,FUNCIONARIO F, PERSONA P "
                + "WHERE FC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA "
                + "AND FC.ID_ESTADO = 1 ";

        if (!fechaInicio.equals("Todos")) {
            Query = Query + " AND FC.TIEMPO BETWEEN '" + fechaInicio + "'::timestamp  "
                    + "AND '" + fechaFinal + "'::timestamp ";
        }
        //NUMERO DE FACTURA
        if (!nroFactura.isEmpty()) {
            Query = Query + " AND FC.NRO_FACTURA= " + nroFactura;
        }
        //CONDICION DE VENTA
        if (condVenta.getDescripcion().equals("Todos")) {
            Query = Query + " AND FC.ID_COND_VENTA NOT IN (1)";
        } else {
            Query = Query + " AND FC.ID_COND_VENTA = " + condVenta.getId();
        }
        //CLIENTE
        if (null != cliente) {
            if (null != cliente.getIdCliente()) {
                Query = Query + " AND FC.ID_CLIENTE = " + cliente.getIdCliente();
            }
        }
        //FUNCIONARIO
        if (null != funcionario) {
            if (null != funcionario.getId_funcionario()) {
                Query = Query + " AND FC.ID_FUNCIONARIO = " + funcionario.getId_funcionario();
            }
        }
        Query = Query + " ORDER BY \"ID\"";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }
}
