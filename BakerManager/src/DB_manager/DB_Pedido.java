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
            total = "ROUND((SELECT SUM(PEDE_CANTIDAD*(PEDE_PRECIO-(PEDE_PRECIO*PEDE_DESCUENTO)/100)) FROM PEDIDO_DETALLE WHERE PEDE_ID_PEDIDO = PEDI_ID_PEDIDO))\"Total\", ";
        }
        String Query = "SELECT PEDI_ID_PEDIDO \"ID\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE_ENTIDAD FROM CLIENTE WHERE CLIE_ID_CLIENTE = PEDI_ID_CLIENTE) \"Cliente\", "
                + "to_char(PEDI_TIEMPO_RECEPCION ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo recepción\", "
                + "to_char(PEDI_TIEMPO_ENTREGA ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo entrega\", "
                + total
                + "(SELECT PEES_DESCRIPCION FROM PEDIDO_ESTADO WHERE PEES_ID_PEDIDO_ESTADO = PEDI_ID_PEDIDO_ESTADO) \"Estado\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = PEDI_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM PEDIDO,FUNCIONARIO, PERSONA "
                + "WHERE PEDI_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA ";
        if (!inicio.isEmpty() && !fin.isEmpty()) {
            if (esTiempoRecepcionOEntrega) {
                Query = Query + " AND PEDI_TIEMPO_RECEPCION BETWEEN '" + inicio + "'::timestamp  "
                        + "AND '" + fin + "'::timestamp ";
            } else {
                Query = Query + " AND PEDI_TIEMPO_ENTREGA BETWEEN '" + inicio + "'::timestamp  "
                        + "AND '" + fin + "'::timestamp ";
            }
        }
        if (!"Todos".equals(tipo_operacion)) {
            Query = Query + " AND PEDI_ID_COND_VENTA = (SELECT TIOP_ID_TIPO_OPERACION FROM TIPO_OPERACION WHERE TIOP_DESCRIPCION LIKE'" + tipo_operacion + "')";
        }
        if (!"Todos".equals(estado)) {
            Query = Query + " AND PEDI_ID_PEDIDO_ESTADO = (SELECT PEES_ID_PEDIDO_ESTADO FROM PEDIDO_ESTADO WHERE PEES_DESCRIPCION LIKE'" + estado + "')";
        }
        if (null != pedido) {
            if (null != pedido.getCliente()) {
                if (null != pedido.getCliente().getIdCliente()) {
                    Query = Query + " AND PEDI_ID_CLIENTE = " + pedido.getCliente().getIdCliente();
                }
            }
            if (null != pedido.getFuncionario()) {
                if (null != pedido.getFuncionario().getId_funcionario()) {
                    Query = Query + " AND PEDI_ID_FUNCIONARIO = " + pedido.getFuncionario().getId_funcionario();
                }
            }
        }
        if (!nroFactura.isEmpty()) {
            Query = Query + " AND PEDI_ID_PEDIDO = " + nroFactura;
        }
        Query = Query+" ORDER BY PEDI_TIEMPO_RECEPCION";
        try {
            System.out.println("83-pedido: " + Query);
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
            total = "ROUND((SELECT SUM(PEDE_CANTIDAD*(PEDE_PRECIO-(PEDE_PRECIO*PEDE_DESCUENTO)/100)) FROM PEDIDO_DETALLE WHERE PEDE_ID_PEDIDO = PEDI_ID_PEDIDO))\"Total\", ";
        }
        String Query = "SELECT PEDI_ID_PEDIDO \"ID\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE_ENTIDAD FROM CLIENTE WHERE CLIE_ID_CLIENTE = PEDI_ID_CLIENTE) \"Cliente\", "
                + "to_char(PEDI_TIEMPO_RECEPCION ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo recepción\", "
                + "to_char(PEDI_TIEMPO_ENTREGA ,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo entrega\", "
                + total
                + "(SELECT PEES_DESCRIPCION FROM PEDIDO_ESTADO WHERE PEES_ID_PEDIDO_ESTADO = PEDI_ID_PEDIDO_ESTADO) \"Estado\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = PEDI_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM PEDIDO,FUNCIONARIO, PERSONA "
                + "WHERE PEDI_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA "
                + "AND PEDI_ID_PEDIDO_ESTADO = (SELECT PEES_ID_PEDIDO_ESTADO FROM PEDIDO_ESTADO WHERE PEES_DESCRIPCION LIKE'Pendiente')";
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
        String genero = "(SELECT SEXO_DESCRIPCION  FROM SEXO WHERE SEXO_ID_SEXO = PERS_ID_SEXO) \"pers_sexo\"";
        String pais = "(SELECT PAIS_DESCRIPCION FROM PAIS WHERE PERS_ID_PAIS=PAIS_ID_PAIS) \"PERS_NACIONALIDAD\"";
        String ciudad = " (SELECT CIUD_DESCRIPCION FROM CIUDAD WHERE PERS_ID_CIUDAD=CIUD_ID_CIUDAD)\"PERS_CIUDAD\"";
        String estadoCivil = " (SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL=PERS_ID_ESTADO_CIVIL)\"pers_estado_civil\"";
        String estado = " (SELECT ESTA_DESCRIPCION FROM ESTADO WHERE ESTA_ID_ESTADO=FUNC_ID_ESTADO)\"func_estado\"";
        String categoria = "(SELECT CLCA_DESCRIPCION FROM CLIENTE_CATEGORIA WHERE CLCA_ID_CLIENTE_CATEGORIA = CLIE_ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI_DESCRIPCION FROM CLIENTE_TIPO WHERE CLTI_ID_CLIENTE_TIPO = CLIE_ID_TIPO) \"TIPO\" ";
        String query = "SELECT PEDI_ID_PEDIDO, "
                + "PEDI_ID_CLIENTE, "
                + "PEDI_ID_FUNCIONARIO, "
                + "PEDI_TIEMPO_RECEPCION, "
                + "PEDI_TIEMPO_ENTREGA, "
                + "PEDI_ID_COND_VENTA, "
                + "PEDI_ID_PEDIDO_ESTADO, "
                + "(SELECT PEES_DESCRIPCION FROM PEDIDO_ESTADO WHERE PEES_ID_PEDIDO_ESTADO = PEDI_ID_PEDIDO_ESTADO) \"PEDI_ESTADO\","
                + "PEDI_DIRECCION, "
                + "PEDI_REFERENCIA, "
                + "CLIE_ID_CLIENTE, CLIE_NOMBRE, CLIE_ENTIDAD, CLIE_RUC, CLIE_RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       CLIE_DIRECCION, CLIE_EMAIL, CLIE_PAG_WEB, CLIE_ID_TIPO, CLIE_ID_CATEGORIA, "
                + "       CLIE_OBSERVACION, "
                + "FUNC_ID_FUNCIONARIO, FUNC_ID_PERSONA, FUNC_ALIAS, FUNC_FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estado + "," + estadoCivil + ","
                + "       FUNC_ID_ESTADO, FUNC_FECHA_SALIDA, FUNC_SALARIO, FUNC_NRO_CELULAR, "
                + "       FUNC_NRO_TELEFONO, FUNC_EMAIL, FUNC_DIRECCION, FUNC_OBSERVACION,PERS_ID_PERSONA, PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, "
                + "       PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD "
                + "FROM PEDIDO ,FUNCIONARIO,CLIENTE,PERSONA "
                + "WHERE  PEDI_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PEDI_ID_CLIENTE = CLIE_ID_CLIENTE "
                + "AND FUNC_ID_PERSONA = PERS_ID_PERSONA "
                + "AND  PEDI_ID_PEDIDO = " + idPedido;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setPais(rs.getString("PERS_NACIONALIDAD"));
                f.setCiudad(rs.getString("PERS_CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("PERS_FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("pers_sexo"));
                f.setNro_celular(rs.getString("func_nro_celular"));
                f.setNro_telefono(rs.getString("func_nro_telefono"));
                f.setEmail(rs.getString("func_email"));
                f.setDireccion(rs.getString("FUNC_DIRECCION"));
                f.setAlias(rs.getString("func_alias"));
                f.setNombre(rs.getString("pers_nombre"));
                f.setApellido(rs.getString("pers_apellido"));
                f.setFecha_ingreso(rs.getDate("FUNC_FECHA_INGRESO"));
                f.setId_persona(rs.getInt("pers_id_persona"));
                f.setCedula(rs.getInt("pers_ci"));
                f.setEstado_civil(rs.getString("pers_estado_civil"));
                f.setId_funcionario(rs.getInt("func_id_funcionario"));
                f.setObservacion(rs.getString("FUNC_OBSERVACION"));

                M_cliente cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("CLIE_DIRECCION"));
                cliente.setEmail(rs.getString("CLIE_EMAIL"));
                cliente.setEntidad(rs.getString("CLIE_ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("CLIE_ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("CLIE_ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("CLIE_ID_TIPO"));
                cliente.setNombre(rs.getString("CLIE_NOMBRE"));
                cliente.setObservacion(rs.getString("CLIE_OBSERVACION"));
                cliente.setPaginaWeb(rs.getString("CLIE_PAG_WEB"));
                cliente.setRuc(rs.getString("CLIE_RUC"));
                cliente.setRucId(rs.getString("CLIE_RUC_IDENTIFICADOR"));
                cliente.setTipo(rs.getString("TIPO"));

                pedido = new M_pedido();
                pedido.setIdPedido(rs.getInt("PEDI_ID_PEDIDO"));
                pedido.setCliente(cliente);
                pedido.getCliente().setIdCliente(rs.getInt("PEDI_ID_CLIENTE"));
                pedido.setIdCondVenta(rs.getInt("PEDI_ID_COND_VENTA"));
                pedido.setIdEstado(rs.getInt("PEDI_ID_PEDIDO_ESTADO"));
                pedido.setEstado(rs.getString("PEDI_ESTADO"));
                pedido.setFuncionario(f);
                pedido.setTiempoEntrega(rs.getTimestamp("PEDI_TIEMPO_ENTREGA"));
                pedido.setTiempoRecepcion(rs.getTimestamp("PEDI_TIEMPO_RECEPCION"));
                pedido.setDireccion(rs.getString("PEDI_DIRECCION"));
                pedido.setReferencia(rs.getString("PEDI_REFERENCIA"));
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
        String queryProducto = "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = PEDE_ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "PEDE_ID_PEDIDO_DETALLE \"ID\", "
                + "PEDE_ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "PEDE_CANTIDAD \"Cantidad\", "
                + "PEDE_PRECIO \"Precio\", "
                + "PEDE_DESCUENTO \"Descuento\","
                + "PEDE_EXENTA \"Exenta\", "
                + "PEDE_IVA5 \"IVA 5%\", "
                + "PEDE_IVA10 \"IVA 10%\", "
                + "PEDE_OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE "
                + "WHERE PEDE_ID_PEDIDO = " + idPedido;
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
        String Query = "SELECT (SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = PEDE_ID_PRODUCTO) \"Producto\",\n"
                + "SUM(PEDE_CANTIDAD) \"Cantidad\", "
                + "                PEDE_PRECIO \"Precio\", "
                + "                SUM(PEDE_DESCUENTO) \"Descuento\","
                + "                SUM(PEDE_EXENTA) \"Exenta\", "
                + "                SUM(PEDE_IVA5) \"IVA 5%\", "
                + "                SUM(PEDE_IVA10) \"IVA 10%\", "
                + "                PEDE_OBSERVACION \"Obs.\" "
                + "FROM PEDIDO_DETALLE, PEDIDO, CLIENTE "
                + "WHERE PEDI_ID_PEDIDO = PEDE_ID_PEDIDO "
                + "AND CLIE_ID_CLIENTE =  PEDI_ID_CLIENTE "
                + "AND PEDI_TIEMPO_RECEPCION BETWEEN '" + fechaInicio + " 00:00:00.00'::timestamp  AND '" + fechaFin + " 23:59:59.00'::timestamp  "
                + "AND PEDI_ID_CLIENTE =" + idCliente
                + " GROUP BY CLIE_NOMBRE,PEDE_PRECIO,\"Producto\",\"Obs.\";";
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
        String q = "SELECT pees_descripcion  "
                + "FROM PEDIDO_ESTADO ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estado = new Vector();
            while (rs.next()) {
                estado.add(rs.getString("pees_descripcion"));
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
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(PEDE_ID_PEDIDO, PEDE_ID_PRODUCTO, PEDE_CANTIDAD, PEDE_PRECIO, PEDE_DESCUENTO, PEDE_EXENTA, PEDE_IVA5, PEDE_IVA10, PEDE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_PEDIDO = "INSERT INTO PEDIDO(PEDI_ID_CLIENTE, PEDI_ID_FUNCIONARIO, PEDI_TIEMPO_ENTREGA, PEDI_ID_COND_VENTA, PEDI_ID_PEDIDO_ESTADO, PEDI_DIRECCION, PEDI_REFERENCIA)VALUES (?, ?, ?, ?, ?, ?, ?);";
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
                pst.setInt(6, pedidoDetalle.get(i).getIva_exenta());
                pst.setInt(7, pedidoDetalle.get(i).getIva_cinco());
                pst.setInt(8, pedidoDetalle.get(i).getIva_diez());
                try {
                    if (pedidoDetalle.get(i).getObservacion() == null) {
                        pst.setNull(9, Types.VARCHAR);
                    } else {
                        pst.setString(9, pedidoDetalle.get(i).getObservacion());
                    }
                } catch (Exception e) {
                    pst.setNull(9, Types.VARCHAR);
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
        String UPDATE_PEDIDO = "UPDATE PEDIDO SET "
                + "PEDI_ID_FUNCIONARIO= " + pedido.getFuncionario().getId_funcionario() + ", "
                + "PEDI_ID_CLIENTE=" + pedido.getCliente().getIdCliente() + ", "
                + "PEDI_DIRECCION= '" + pedido.getDireccion() + "', "
                + "PEDI_REFERENCIA= '" + pedido.getReferencia() + "', "
                + "PEDI_TIEMPO_ENTREGA= '" + pedido.getTiempoEntrega() + "', "
                + "PEDI_ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + ", "
                + "PEDI_ID_COND_VENTA = " + pedido.getIdCondVenta()
                + " WHERE PEDI_ID_PEDIDO = " + pedido.getIdPedido();
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
        String UPDATE_PEDIDO = "UPDATE PEDIDO SET "
                + "PEDI_ID_CLIENTE=" + pedido.getCliente().getIdCliente()
                + " WHERE PEDI_ID_PEDIDO = " + pedido.getIdPedido();
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
                + "PEDE_CANTIDAD= " + pedidoDetalle.getCantidad() + ", "
                + "PEDE_PRECIO=" + pedidoDetalle.getPrecio() + ", "
                + "PEDE_DESCUENTO=" + pedidoDetalle.getDescuento() + ", "
                + "PEDE_EXENTA=" + pedidoDetalle.getIva_exenta() + ", "
                + "PEDE_IVA5=" + pedidoDetalle.getIva_cinco() + ", "
                + "PEDE_IVA10=" + pedidoDetalle.getIva_diez() + ", "
                + "PEDE_OBSERVACION= '" + pedidoDetalle.getObservacion() + "' "
                + "WHERE PEDE_ID_PEDIDO_DETALLE = " + pedidoDetalle.getIdPedioDetalle();
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
        String UPDATE_PEDIDO = "UPDATE PEDIDO SET "
                + "PEDI_ID_PEDIDO_ESTADO = " + pedido.getIdEstado() + " "
                + " WHERE PEDI_ID_PEDIDO = " + pedido.getIdPedido();
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
        String DELETE_DETAIL = "DELETE FROM PEDIDO_DETALLE WHERE PEDE_ID_PEDIDO_DETALLE = " + idPedidoDetalle;
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
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(PEDE_ID_PEDIDO, PEDE_ID_PRODUCTO, PEDE_CANTIDAD, PEDE_PRECIO, PEDE_DESCUENTO, PEDE_EXENTA, PEDE_IVA5, PEDE_IVA10, PEDE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
            pst.setInt(1, detalle.getIdPedido());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setDouble(3, detalle.getCantidad());
            pst.setInt(4, detalle.getPrecio());
            pst.setDouble(5, detalle.getDescuento());
            pst.setInt(6, detalle.getIva_exenta());
            pst.setInt(7, detalle.getIva_cinco());
            pst.setInt(8, detalle.getIva_diez());
            try {
                if (detalle.getObservacion() == null) {
                    pst.setNull(9, Types.VARCHAR);
                } else {
                    pst.setString(9, detalle.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(9, Types.VARCHAR);
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
        String UPDATE_PEDIDO = "UPDATE PEDIDO SET "
                + "PEDI_ID_PEDIDO_ESTADO = 3 "
                + " WHERE PEDI_ID_PEDIDO = " + idPedido;
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
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(FADE_ID_FACTURA_CABECERA, FADE_ID_PRODUCTO, FADE_CANTIDAD, FADE_PRECIO, FADE_DESCUENTO, FADE_EXENTA, FADE_IVA5, FADE_IVA10, FADE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
                pst.setInt(6, detalle.get(i).getIva_exenta());
                pst.setInt(7, detalle.get(i).getIva_cinco());
                pst.setInt(8, detalle.get(i).getIva_diez());
                try {
                    if (detalle.get(i).getObservacion() == null) {
                        pst.setNull(9, Types.VARCHAR);
                    } else {
                        pst.setString(9, detalle.get(i).getObservacion());
                    }
                } catch (Exception e) {
                    pst.setNull(9, Types.VARCHAR);
                }
                pst.executeUpdate();
                pst.close();
            }
            String UPDATE_PEDIDO = "UPDATE PEDIDO SET PEDI_ID_FACTURA_CABECERA = " + sq_cabecera + ", pedi_id_pedido_estado = 2 WHERE PEDI_ID_PEDIDO = " + pedido.getIdPedido();
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
        String query = "SELECT PEDE_ID_PEDIDO_DETALLE, PEDE_ID_PEDIDO, PEDE_ID_PRODUCTO, PEDE_CANTIDAD, PEDE_PRECIO, PEDE_DESCUENTO, PEDE_EXENTA, PEDE_IVA5, PEDE_IVA10, PEDE_OBSERVACION FROM PEDIDO_DETALLE WHERE PEDE_ID_PEDIDO = " + idPedido;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            detalles = new ArrayList();
            while (rs.next()) {
                M_pedidoDetalle detalle = new M_pedidoDetalle();
                detalle.setCantidad(rs.getDouble("PEDE_CANTIDAD"));
                detalle.setDescuento(rs.getDouble("PEDE_DESCUENTO"));
                detalle.setIdPedido(rs.getInt("PEDE_ID_PEDIDO"));
                detalle.setIdPedioDetalle(rs.getInt("PEDE_ID_PEDIDO_DETALLE"));
                detalle.setIva_cinco(rs.getInt("PEDE_IVA5"));
                detalle.setIva_diez(rs.getInt("PEDE_IVA10"));
                detalle.setIva_exenta(rs.getInt("PEDE_EXENTA"));
                detalle.setObservacion(rs.getString("PEDE_OBSERVACION"));
                detalle.setPrecio(rs.getInt("PEDE_PRECIO"));
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("PEDE_ID_PRODUCTO"));
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
