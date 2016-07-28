/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

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
            total = "ROUND((SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO = PEDI.ID_PEDIDO))\"Total\", ";
        }
        String Query = "SELECT PEDI.ID_PEDIDO \"ID\", "
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
            Query = Query + " AND PEDI.ID_PEDIDO = " + nroFactura;
        }
        Query = Query + " ORDER BY PEDI.TIEMPO_RECEPCION";
        try {
            System.out.println("82-pedido: " + Query);
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
            total = "ROUND((SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO = PEDI.ID_PEDIDO))\"Total\", ";
        }
        String Query = "SELECT PEDI.ID_PEDIDO \"ID\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE.ENTIDAD FROM CLIENTE CLIE WHERE CLIE.ID_CLIENTE = PEDI.ID_CLIENTE) \"Cliente\", "
                + "to_char(PEDI.TIEMPO_RECEPCION ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo recepción\", "
                + "to_char(PEDI.TIEMPO_ENTREGA ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo entrega\", "
                + total
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"Estado\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = PEDI.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM PEDIDO PEDI,FUNCIONARIO FUNC, PERSONA PERS "
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
        String genero = "(SELECT SEXO.DESCRIPCION  FROM SEXO SEXO WHERE SEXO.ID_SEXO = PERS.ID_SEXO) \"pers_sexo\"";
        String pais = "(SELECT PAIS.DESCRIPCION FROM PAIS PAIS WHERE PERS.ID_PAIS=PAIS.ID_PAIS) \"PERS.NACIONALIDAD\"";
        String ciudad = " (SELECT CIUD.DESCRIPCION FROM CIUDAD CIUD WHERE PERS.ID_CIUDAD=CIUD.ID_CIUDAD)\"PERS.CIUDAD\"";
        String estadoCivil = " (SELECT ESCI.DESCRIPCION FROM ESTADO_CIVIL ESCI WHERE ESCI.ID_ESTADO_CIVIL=PERS.ID_ESTADO_CIVIL)\"pers_estado_civil\"";
        String categoria = "(SELECT CLCA.DESCRIPCION FROM CLIENTE_CATEGORIA CLCA WHERE CLCA.ID_CLIENTE_CATEGORIA = CLIE.ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI.DESCRIPCION FROM CLIENTE_TIPO CLTI WHERE CLTI.ID_CLIENTE_TIPO = CLIE.ID_TIPO) \"TIPO\" ";
        String query = "SELECT PEDI.ID_PEDIDO, "
                + "PEDI.ID_CLIENTE, "
                + "PEDI.ID_FUNCIONARIO, "
                + "PEDI.TIEMPO_RECEPCION, "
                + "PEDI.TIEMPO_ENTREGA, "
                + "PEDI.ID_COND_VENTA, "
                + "PEDI.ID_PEDIDO_ESTADO, "
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"PEDI.ESTADO\","
                + "PEDI.DIRECCION, "
                + "PEDI.REFERENCIA, "
                + "CLIE.ID_CLIENTE, CLIE.NOMBRE, CLIE.ENTIDAD, CLIE.RUC, CLIE.RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       CLIE.DIRECCION, CLIE.EMAIL, CLIE.PAG_WEB, CLIE.ID_TIPO, CLIE.ID_CATEGORIA, "
                + "       CLIE.OBSERVACION, "
                + "FUNC.ID_FUNCIONARIO, FUNC.ID_PERSONA, FUNC.ALIAS, FUNC.FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estadoCivil + ","
                + "       FUNC.ID_ESTADO, FUNC.FECHA_SALIDA, FUNC.SALARIO, FUNC.NRO_CELULAR, "
                + "       FUNC.NRO_TELEFONO, FUNC.EMAIL, FUNC.DIRECCION, FUNC.OBSERVACION,PERS.ID_PERSONA, PERS.CI, PERS.NOMBRE, PERS.APELLIDO, PERS.ID_SEXO, "
                + "       PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD "
                + "FROM PEDIDO PEDI, FUNCIONARIO FUNC, CLIENTE CLIE, PERSONA PERS "
                + "WHERE  PEDI.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PEDI.ID_CLIENTE = CLIE.ID_CLIENTE "
                + "AND FUNC.ID_PERSONA = PERS.ID_PERSONA "
                + "AND  PEDI.ID_PEDIDO = " + idPedido;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setPais(rs.getString("PERS.NACIONALIDAD"));
                f.setCiudad(rs.getString("PERS.CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("PERS.FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("pers.sexo"));
                f.setNro_celular(rs.getString("FUNC.nro_celular"));
                f.setNro_telefono(rs.getString("FUNC.nro_telefono"));
                f.setEmail(rs.getString("FUNC.email"));
                f.setDireccion(rs.getString("FUNC.DIRECCION"));
                f.setAlias(rs.getString("FUNC.alias"));
                f.setNombre(rs.getString("pers.nombre"));
                f.setApellido(rs.getString("pers.apellido"));
                f.setFecha_ingreso(rs.getDate("FUNC.FECHA_INGRESO"));
                f.setId_persona(rs.getInt("pers.id_persona"));
                f.setCedula(rs.getInt("pers.ci"));
                f.setEstado_civil(rs.getString("pers.estado_civil"));
                f.setId_funcionario(rs.getInt("FUNC.id_funcionario"));
                f.setObservacion(rs.getString("FUNC.OBSERVACION"));

                M_cliente cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CLCA.CATEGORIA"));
                cliente.setDireccion(rs.getString("CLIE.DIRECCION"));
                cliente.setEmail(rs.getString("CLIE.EMAIL"));
                cliente.setEntidad(rs.getString("CLIE.ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("CLIE.ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("CLIE.ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("CLIE.ID_TIPO"));
                cliente.setNombre(rs.getString("CLIE.NOMBRE"));
                cliente.setObservacion(rs.getString("CLIE.OBSERVACION"));
                cliente.setPaginaWeb(rs.getString("CLIE.PAG_WEB"));
                cliente.setRuc(rs.getString("CLIE.RUC"));
                cliente.setRucId(rs.getString("CLIE.RUC_IDENTIFICADOR"));
                cliente.setTipo(rs.getString("TIPO"));

                pedido = new M_pedido();
                pedido.setIdPedido(rs.getInt("PEDI.ID_PEDIDO"));
                pedido.setCliente(cliente);
                pedido.getCliente().setIdCliente(rs.getInt("PEDI.ID_CLIENTE"));
                pedido.setIdCondVenta(rs.getInt("PEDI.ID_COND_VENTA"));
                pedido.setIdEstado(rs.getInt("PEDI.ID_PEDIDO_ESTADO"));
                pedido.setEstado(rs.getString("PEDI.ESTADO"));
                pedido.setFuncionario(f);
                pedido.setTiempoEntrega(rs.getTimestamp("PEDI.TIEMPO_ENTREGA"));
                pedido.setTiempoRecepcion(rs.getTimestamp("PEDI.TIEMPO_RECEPCION"));
                pedido.setDireccion(rs.getString("PEDI.DIRECCION"));
                pedido.setReferencia(rs.getString("PEDI.REFERENCIA"));
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
        //String queryProducto = "(SELECT PROD.DESCRIPCION FROM PRODUCTO PROD WHERE PROD.ID_PRODUCTO = PEDE.ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "PEDE.ID_PEDIDO_DETALLE \"ID\", "
                + "PEDE.ID_PRODUCTO \"ID art.\", "
                //      + queryProducto
                + "PROD.DESCRIPCION \"Producto\""
                + "PEDE.CANTIDAD \"Cantidad\", "
                + "PEDE.PRECIO \"Precio\", "
                + "PEDE.DESCUENTO \"Descuento\","
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 1 THEN round((PEDE.PRECIO*PEDE.DESCUENTO)/100) "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 2 THEN round((PEDE.PRECIO*PEDE.DESCUENTO)/100) "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 3 THEN round((PEDE.PRECIO*PEDE.DESCUENTO)/100) "
                + "END AS \"IVA 10%\", "
                + "PEDE.OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE PEDE, PRODUCTO PROD "
                + "WHERE PROD.ID_PRODUCTO = PEDE.ID_PRODUCTO "
                + "AND PEDE.ID_PEDIDO = " + idPedido;
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
        String Query = "SELECT (SELECT PROD.DESCRIPCION FROM PRODUCTO PROD WHERE PROD.ID_PRODUCTO = PEDE.ID_PRODUCTO) \"Producto\",\n"
                + "SUM(PEDE.CANTIDAD) \"Cantidad\", "
                + "                PEDE.PRECIO \"Precio\", "
                + "                SUM(PEDE.DESCUENTO) \"Descuento\","
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 1 THEN SUM(round((PEDE.PRECIO*PEDE.DESCUENTO)/100)) "
                + "END AS \"Exenta\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 2 THEN SUM(round((PEDE.PRECIO*PEDE.DESCUENTO)/100)) "
                + "END AS \"IVA 5%\", "
                + "CASE "
                + "	WHEN PROD.ID_IMPUESTO = 3 THEN SUM(round((PEDE.PRECIO*PEDE.DESCUENTO)/100)) "
                + "END AS \"IVA 10%\", "
                + "PEDE.OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE PEDE, PEDIDO PEDI, CLIENTE CLIE"
                + "WHERE PEDI.ID_PEDIDO = PEDE.ID_PEDIDO "
                + "AND CLIE.ID_CLIENTE =  PEDI.ID_CLIENTE "
                + "AND PEDI.TIEMPO_RECEPCION BETWEEN '" + fechaInicio + " 00:00:00.00'::timestamp  AND '" + fechaFin + " 23:59:59.00'::timestamp  "
                + "AND PEDI.ID_CLIENTE =" + idCliente
                + " GROUP BY CLIE.NOMBRE,PEDE.PRECIO,\"Producto\",\"Obs.\";";
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
        String q = "SELECT pees.descripcion  "
                + "FROM PEDIDO_ESTADO ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estado = new Vector();
            while (rs.next()) {
                estado.add(rs.getString("pees.descripcion"));
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
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(PEDE.ID_PEDIDO, PEDE.ID_PRODUCTO, PEDE.CANTIDAD, PEDE.PRECIO, PEDE.DESCUENTO, PEDE.OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        String INSERT_PEDIDO = "INSERT INTO PEDIDO_CABECERA(PEDI.ID_CLIENTE, PEDI.ID_FUNCIONARIO, PEDI.TIEMPO_ENTREGA, PEDI.ID_COND_VENTA, PEDI.ID_PEDIDO_ESTADO, PEDI.DIRECCION, PEDI.REFERENCIA)VALUES (?, ?, ?, ?, ?, ?, ?);";
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
                + "PEDI.ID_FUNCIONARIO= " + pedido.getFuncionario().getId_funcionario() + ", "
                + "PEDI.ID_CLIENTE=" + pedido.getCliente().getIdCliente() + ", "
                + "PEDI.DIRECCION= '" + pedido.getDireccion() + "', "
                + "PEDI.REFERENCIA= '" + pedido.getReferencia() + "', "
                + "PEDI.TIEMPO_ENTREGA= '" + pedido.getTiempoEntrega() + "', "
                + "PEDI.ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + ", "
                + "PEDI.ID_COND_VENTA = " + pedido.getIdCondVenta()
                + " WHERE PEDI.ID_PEDIDO = " + pedido.getIdPedido();
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
                + "PEDI.ID_CLIENTE=" + pedido.getCliente().getIdCliente()
                + " WHERE PEDI.ID_PEDIDO = " + pedido.getIdPedido();
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
                + "PEDE.CANTIDAD= " + pedidoDetalle.getCantidad() + ", "
                + "PEDE.PRECIO=" + pedidoDetalle.getPrecio() + ", "
                + "PEDE.DESCUENTO=" + pedidoDetalle.getDescuento() + ", "
                + "PEDE.OBSERVACION= '" + pedidoDetalle.getObservacion() + "' "
                + "WHERE PEDE.ID_PEDIDO_DETALLE = " + pedidoDetalle.getIdPedioDetalle();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PEDIDO_DETALLE);
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

    public static void actualizarPedidoEstado(M_pedido pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "PEDI.ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + " "
                + " WHERE PEDI.ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
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
        String DELETE_DETAIL = "DELETE FROM PEDIDO_DETALLE WHERE PEDE.ID_PEDIDO_DETALLE = " + idPedidoDetalle;
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
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(PEDE.ID_PEDIDO, PEDE.ID_PRODUCTO, PEDE.CANTIDAD, PEDE.PRECIO, PEDE.DESCUENTO, PEDE.OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
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
                + "PEDI.ID_PEDIDO_ESTADO = 3 "
                + " WHERE PEDI.ID_PEDIDO = " + idPedido;
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
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(FADE_ID_FACTURA_CABECERA, FADE_ID_PRODUCTO, FADE_CANTIDAD, FADE_PRECIO, FADE_DESCUENTO, FADE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(FACA_ID_FUNCIONARIO, FACA_ID_CLIENTE, FACA_ID_COND_VENTA)VALUES (?, ?, ?);";

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
            String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET PEDI.ID_FACTURA_CABECERA = " + sq_cabecera + ", pedi_id_pedido_estado = 2 WHERE PEDI.ID_PEDIDO_CABECERA = " + pedido.getIdPedido();
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
        String query = "SELECT PEDE.ID_PEDIDO_DETALLE, PEDE.ID_PEDIDO_CABECERA, PEDE.ID_PRODUCTO, PEDE.CANTIDAD, PEDE.PRECIO, PEDE.DESCUENTO, PEDE.OBSERVACION FROM PEDIDO_DETALLE WHERE PEDE.ID_PEDIDO = " + idPedido;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_pedidoDetalle detalle = new M_pedidoDetalle();
                detalle.setCantidad(rs.getDouble("PEDE.CANTIDAD"));
                detalle.setDescuento(rs.getDouble("PEDE.DESCUENTO"));
                detalle.setIdPedido(rs.getInt("PEDE.ID_PEDIDO"));
                detalle.setIdPedioDetalle(rs.getInt("PEDE.ID_PEDIDO_DETALLE"));
                detalle.setObservacion(rs.getString("PEDE.OBSERVACION"));
                detalle.setPrecio(rs.getInt("PEDE.PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("PEDE.ID_PRODUCTO"));
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
