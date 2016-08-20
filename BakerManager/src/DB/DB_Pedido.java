/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedido;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Pedido {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    /*
     * READ
     */

    public static ResultSetTableModel obtenerPedidos(boolean esTiempoRecepcionOEntrega, String inicio, String fin, String tipo_operacion, String nroFactura, String estado, M_pedido pedido, boolean conTotal) {
        ResultSetTableModel rstm = null;
        String total = "";
        if (conTotal) {
            total = "ROUND((SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO_CABECERA = PEDI.ID_PEDIDO_CABECERA))\"Total\", ";
        }
        String Query = "SELECT PEDI.ID_PEDIDO_CABECERA \"ID\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE.ENTIDAD FROM CLIENTE CLIE WHERE CLIE.ID_CLIENTE = PEDI.ID_CLIENTE) \"Cliente\", "
                + "to_char(PEDI.TIEMPO_RECEPCION ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo recepción\", "
                + "to_char(PEDI.TIEMPO_ENTREGA ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo entrega\", "
                + total
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"Estado\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = PEDI.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM PEDIDO_CABECERA PEDI, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE PEDI.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA ";
        if (!inicio.isEmpty() && !fin.isEmpty()) {
            if (esTiempoRecepcionOEntrega) {
                Query = Query + " AND PEDI.TIEMPO_RECEPCION BETWEEN '" + inicio + "'::timestamp  "
                        + "AND '" + fin + "'::timestamp ";
            } else {
                Query = Query + " AND PEDI.TIEMPO_ENTREGA BETWEEN '" + inicio + "'::timestamp  "
                        + "AND '" + fin + "'::timestamp ";
            }
        }
        if (!"Todos".equals(tipo_operacion)) {
            Query = Query + " AND PEDI.ID_COND_VENTA = (SELECT TIOP.ID_TIPO_OPERACION FROM TIPO_OPERACION TIOP WHERE TIOP.DESCRIPCION LIKE'" + tipo_operacion + "')";
        }
        if (!"Todos".equals(estado)) {
            Query = Query + " AND PEDI.ID_PEDIDO_ESTADO = (SELECT PEES.ID_PEDIDO_ESTADO FROM PEDIDO_ESTADO PEES WHERE PEES.DESCRIPCION LIKE'" + estado + "')";
        }
        if (null != pedido) {
            if (null != pedido.getCliente()) {
                if (null != pedido.getCliente().getIdCliente()) {
                    Query = Query + " AND PEDI.ID_CLIENTE = " + pedido.getCliente().getIdCliente();
                }
            }
            if (null != pedido.getFuncionario()) {
                if (null != pedido.getFuncionario().getId_funcionario()) {
                    Query = Query + " AND PEDI.ID_FUNCIONARIO = " + pedido.getFuncionario().getId_funcionario();
                }
            }
        }
        if (!nroFactura.isEmpty()) {
            Query = Query + " AND PEDI.ID_PEDIDO_CABECERA = " + nroFactura;
        }
        Query = Query + " ORDER BY PEDI.TIEMPO_RECEPCION";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerPedidosPendientes(boolean conTotal) {
        ResultSetTableModel rstm = null;
        String total = "";
        if (conTotal) {
            total = "ROUND((SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO_CABECERA = PEDI.ID_PEDIDO_CABECERA))\"Total\", ";
        }
        String Query = "SELECT PEDI.ID_PEDIDO_CABECERA \"ID\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE.ENTIDAD FROM CLIENTE CLIE WHERE CLIE.ID_CLIENTE = PEDI.ID_CLIENTE) \"Cliente\", "
                + "to_char(PEDI.TIEMPO_RECEPCION ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo recepción\", "
                + "to_char(PEDI.TIEMPO_ENTREGA ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo entrega\", "
                + total
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"Estado\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = PEDI.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM PEDIDO_CABECERA PEDI,FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE PEDI.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA "
                + "AND PEDI.ID_PEDIDO_ESTADO = (SELECT PEES.ID_PEDIDO_ESTADO FROM PEDIDO_ESTADO PEES WHERE PEES.DESCRIPCION LIKE'Pendiente')";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static M_pedido obtenerPedido(Integer idPedido) {
        M_pedido pedido = null;
        String genero = "(SELECT SEXO.DESCRIPCION  FROM SEXO SEXO WHERE SEXO.ID_SEXO = PERS.ID_SEXO) \"SEXO\"";
        String pais = "(SELECT PAIS.DESCRIPCION FROM PAIS PAIS WHERE PERS.ID_PAIS=PAIS.ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT CIUD.DESCRIPCION FROM CIUDAD CIUD WHERE PERS.ID_CIUDAD=CIUD.ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT ESCI.DESCRIPCION FROM ESTADO_CIVIL ESCI WHERE ESCI.ID_ESTADO_CIVIL=PERS.ID_ESTADO_CIVIL)\"estado_civil\"";
        String categoria = "(SELECT CLCA.DESCRIPCION FROM CLIENTE_CATEGORIA CLCA WHERE CLCA.ID_CLIENTE_CATEGORIA = CLIE.ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI.DESCRIPCION FROM CLIENTE_TIPO CLTI WHERE CLTI.ID_CLIENTE_TIPO = CLIE.ID_TIPO) \"TIPO\" ";
        String query = "SELECT PEDI.ID_PEDIDO_CABECERA, "
                + "PEDI.ID_CLIENTE, "
                + "PEDI.ID_FUNCIONARIO, "
                + "PEDI.TIEMPO_RECEPCION, "
                + "PEDI.TIEMPO_ENTREGA, "
                + "PEDI.ID_COND_VENTA, "
                + "PEDI.ID_PEDIDO_ESTADO, "
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"ESTADO\","
                + "PEDI.DIRECCION \"PDIRECCION\", "
                + "PEDI.REFERENCIA, "
                + "CLIE.ID_CLIENTE, CLIE.NOMBRE, CLIE.ENTIDAD, CLIE.RUC, CLIE.RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       CLIE.DIRECCION \"CDIRECCION\", CLIE.EMAIL, CLIE.PAG_WEB, CLIE.ID_TIPO, CLIE.ID_CATEGORIA, "
                + "       CLIE.OBSERVACION, "
                + "FUNC.ID_FUNCIONARIO, FUNC.ID_PERSONA, FUNC.ALIAS, FUNC.FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estadoCivil + ","
                + "       FUNC.NRO_CELULAR, "
                + "       FUNC.NRO_TELEFONO, FUNC.EMAIL \"FEMAIL\", FUNC.DIRECCION \"FDIRECCION\", FUNC.OBSERVACION,PERS.ID_PERSONA, PERS.CI, PERS.NOMBRE \"FNOMBRE\", PERS.APELLIDO, PERS.ID_SEXO, "
                + "       PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD "
                + "FROM PEDIDO_CABECERA PEDI, FUNCIONARIO FUNC, CLIENTE CLIE, PERSONA PERS "
                + "WHERE  PEDI.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PEDI.ID_CLIENTE = CLIE.ID_CLIENTE "
                + "AND FUNC.ID_PERSONA = PERS.ID_PERSONA "
                + "AND  PEDI.ID_PEDIDO_CABECERA = " + idPedido;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setPais(rs.getString("NACIONALIDAD"));
                f.setCiudad(rs.getString("CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("sexo"));
                f.setNro_celular(rs.getString("nro_celular"));
                f.setNro_telefono(rs.getString("nro_telefono"));
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
                f.setObservacion(rs.getString("OBSERVACION"));

                M_cliente cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("CDIRECCION"));
                cliente.setEmail(rs.getString("EMAIL"));
                cliente.setEntidad(rs.getString("ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("ID_TIPO"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setObservacion(rs.getString("OBSERVACION"));
                cliente.setPaginaWeb(rs.getString("PAG_WEB"));
                cliente.setRuc(rs.getString("RUC"));
                cliente.setRucId(rs.getString("RUC_IDENTIFICADOR"));
                cliente.setTipo(rs.getString("TIPO"));

                pedido = new M_pedido();
                pedido.setIdPedido(rs.getInt("ID_PEDIDO_CABECERA"));
                pedido.setCliente(cliente);
                pedido.getCliente().setIdCliente(rs.getInt("ID_CLIENTE"));
                pedido.setIdCondVenta(rs.getInt("ID_COND_VENTA"));
                pedido.setIdEstado(rs.getInt("ID_PEDIDO_ESTADO"));
                pedido.setEstado(rs.getString("ESTADO"));
                pedido.setFuncionario(f);
                pedido.setTiempoEntrega(rs.getTimestamp("TIEMPO_ENTREGA"));
                pedido.setTiempoRecepcion(rs.getTimestamp("TIEMPO_RECEPCION"));
                pedido.setDireccion(rs.getString("PDIRECCION"));
                pedido.setReferencia(rs.getString("REFERENCIA"));
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
        return pedido;
    }

    public static ResultSetTableModel obtenerPedidoDetalle(Integer idPedido) {
        String Query = "SELECT "
                + "PEDE.ID_PEDIDO_DETALLE \"ID\", "
                + "PEDE.ID_PRODUCTO \"ID art.\", "
                + "PROD.DESCRIPCION \"Producto\", "
                + "PEDE.CANTIDAD \"Cantidad\", "
                + "PEDE.PRECIO \"Precio\", "
                + "PEDE.DESCUENTO \"Descuento\","
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 1 THEN round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 2 THEN round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 3 THEN round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100))ELSE '0' "
                + "END AS \"IVA 10%\", "
                + "PEDE.OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE PEDE, PRODUCTO PROD "
                + "WHERE PROD.ID_PRODUCTO = PEDE.ID_PRODUCTO "
                + "AND PEDE.ID_PEDIDO_CABECERA = " + idPedido;
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
        return rstm;
    }

    public static ResultSetTableModel obtenerPedidoDetalleAgrupado(Integer idCliente, String fechaInicio, String fechaFin) {
        String Query = "SELECT PROD.DESCRIPCION \"Producto\", "
                + "SUM(PEDE.CANTIDAD) \"Cantidad\", "
                + "PEDE.PRECIO \"Precio\", "
                + "SUM(PEDE.DESCUENTO) \"Descuento\","
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 1 THEN SUM(round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)))ELSE '0' "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 2 THEN SUM(round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)))ELSE '0' "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 3 THEN SUM(round(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)))ELSE '0' "
                + "END AS \"IVA 10%\", "
                + "PEDE.OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE PEDE, PEDIDO_CABECERA PEDI, CLIENTE CLIE, PRODUCTO PROD "
                + "WHERE PEDI.ID_PEDIDO_CABECERA = PEDE.ID_PEDIDO_CABECERA "
                + "AND CLIE.ID_CLIENTE =  PEDI.ID_CLIENTE "
                + "AND PEDE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND PEDI.TIEMPO_RECEPCION BETWEEN '" + fechaInicio + " 00:00:00.00'::timestamp  AND '" + fechaFin + " 23:59:59.00'::timestamp  "
                + "AND PEDI.ID_CLIENTE =" + idCliente
                + " GROUP BY CLIE.NOMBRE,PEDE.PRECIO,\"Producto\",PROD.ID_IMPUESTO,\"Obs.\";";
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
        return rstm;
    }

    public static Vector obtenerEstado() {
        Vector estado = null;
        String q = "SELECT descripcion  "
                + "FROM PEDIDO_ESTADO ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estado = new Vector();
            while (rs.next()) {
                estado.add(rs.getString("descripcion"));
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
        return estado;
    }

    /*
     * CREATE
     */
    public static long insertarPedido(M_pedido pedido, ArrayList<M_pedidoDetalle> pedidoDetalle) {
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(ID_PEDIDO_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        String INSERT_PEDIDO = "INSERT INTO PEDIDO_CABECERA(ID_CLIENTE, ID_FUNCIONARIO, TIEMPO_ENTREGA, ID_COND_VENTA, ID_PEDIDO_ESTADO, DIRECCION, REFERENCIA)VALUES (?, ?, ?, ?, ?, ?, ?);";
        long idPedido = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_PEDIDO, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, pedido.getCliente().getIdCliente());
            pst.setInt(2, pedido.getFuncionario().getId_funcionario());
            pst.setTimestamp(3, pedido.getTiempoEntrega());
            pst.setInt(4, pedido.getIdCondVenta());
            pst.setInt(5, pedido.getIdEstado());
            try {
                if (pedido.getDireccion().isEmpty()) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, pedido.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (pedido.getDireccion().isEmpty()) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, pedido.getReferencia());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                idPedido = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < pedidoDetalle.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
                pst.setInt(1, (int) idPedido);
                pst.setInt(2, pedidoDetalle.get(i).getProducto().getId());
                pst.setDouble(3, pedidoDetalle.get(i).getCantidad());
                pst.setInt(4, pedidoDetalle.get(i).getPrecio());
                pst.setDouble(5, pedidoDetalle.get(i).getDescuento());
                try {
                    if (pedidoDetalle.get(i).getObservacion() == null) {
                        pst.setNull(6, Types.VARCHAR);
                    } else {
                        pst.setString(6, pedidoDetalle.get(i).getObservacion());
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
        return idPedido;
    }

    /*
     * UPDATE
     */
    public static void actualizarPedido(M_pedido pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_FUNCIONARIO= " + pedido.getFuncionario().getId_funcionario() + ", "
                + "ID_CLIENTE=" + pedido.getCliente().getIdCliente() + ", "
                + "DIRECCION= '" + pedido.getDireccion() + "', "
                + "REFERENCIA= '" + pedido.getReferencia() + "', "
                + "TIEMPO_ENTREGA= '" + pedido.getTiempoEntrega() + "', "
                + "ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + ", "
                + "ID_COND_VENTA = " + pedido.getIdCondVenta()
                + " WHERE ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO);
            st.close();
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

    public static void actualizarPedidoCliente(M_pedido pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_CLIENTE=" + pedido.getCliente().getIdCliente()
                + " WHERE ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO);
            st.close();
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

    public static void actualizarPedidoDetalle(M_pedidoDetalle pedidoDetalle) {
        String UPDATE_PEDIDO_DETALLE = "UPDATE PEDIDO_DETALLE SET "
                + "CANTIDAD = ?, "
                + "PRECIO = ?, "
                + "DESCUENTO = ?, "
                + "OBSERVACION = ? "
                + "WHERE ID_PEDIDO_DETALLE = " + pedidoDetalle.getIdPedioDetalle();
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_PEDIDO_DETALLE);
            pst.setDouble(1, pedidoDetalle.getCantidad());
            pst.setInt(2, pedidoDetalle.getPrecio());
            pst.setDouble(3, pedidoDetalle.getDescuento());
            pst.setString(4, pedidoDetalle.getObservacion());
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

    public static void actualizarPedidoEstado(M_pedido pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + " "
                + " WHERE ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO);
            st.close();
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

    public static void eliminarPedidoDetalle(int idPedidoDetalle) {
        String DELETE_DETAIL = "DELETE FROM PEDIDO_DETALLE WHERE ID_PEDIDO_DETALLE = " + idPedidoDetalle;
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

    public static void insertarPedidoDetalle(M_pedidoDetalle detalle) {
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(ID_PEDIDO_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
            pst.setInt(1, detalle.getIdPedido());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setDouble(3, detalle.getCantidad());
            pst.setInt(4, detalle.getPrecio());
            pst.setDouble(5, detalle.getDescuento());
            try {
                if (detalle.getObservacion() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, detalle.getObservacion());
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

    public static void cancelarPedido(int idPedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_PEDIDO_ESTADO = 3 "
                + " WHERE ID_PEDIDO_CABECERA = " + idPedido;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO);
            st.close();
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

    public static void pagarPedido(M_pedido pedido, ArrayList<M_pedidoDetalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA)VALUES (?, ?, ?);";

        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, pedido.getFuncionario().getId_funcionario());
            pst.setInt(2, pedido.getCliente().getIdCliente());
            pst.setInt(3, pedido.getIdCondVenta());
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
            String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET ID_FACTURA_CABECERA = " + sq_cabecera + ", id_pedido_estado = 2 WHERE ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO);
            st.close();
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

    public static ArrayList<M_pedidoDetalle> obtenerPedidoDetalles(Integer idPedido) {
        ArrayList<M_pedidoDetalle> detalles = null;
        String query = "SELECT ID_PEDIDO_DETALLE, ID_PEDIDO_CABECERA, ID_PRODUCTO,(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = PD.ID_PRODUCTO)\"PRODUCTO\", CANTIDAD, PRECIO, DESCUENTO, OBSERVACION FROM PEDIDO_DETALLE PD WHERE PD.ID_PEDIDO_CABECERA = " + idPedido;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_pedidoDetalle detalle = new M_pedidoDetalle();
                detalle.setCantidad(rs.getDouble("CANTIDAD"));
                detalle.setDescuento(rs.getDouble("DESCUENTO"));
                detalle.setIdPedido(rs.getInt("ID_PEDIDO_CABECERA"));
                detalle.setIdPedioDetalle(rs.getInt("ID_PEDIDO_DETALLE"));
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
}
