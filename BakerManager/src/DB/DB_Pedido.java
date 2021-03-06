/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_estadoPedido;
import Entities.E_facturaDetalle;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_funcionario;
import Entities.M_pedidoCabecera;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
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
    public static List<M_pedidoCabecera> obtenerPedidos(boolean conFecha, boolean esTiempoRecepcionOEntrega, Date fechaInicio, Date fechaFin, int idTipoOperacion, int idPedido, int idEstado, int idVendedor, int idCliente, boolean conTotal) {
        List<M_pedidoCabecera> list = new ArrayList<>();
        String SQL_TOTAL = "";
        String SQL_TIEMPO = "";
        String SQL_TIOP = "";
        String SQL_ESTADO_PEDIDO = "";
        String SQL_CLIENTE = "";
        String SQL_VENDEDOR = "";
        String SQL_ID_PEDIDO = "";
        String SQL_ORDER_BY = " ORDER BY PEDI.TIEMPO_RECEPCION";
        if (conTotal) {
            SQL_TOTAL = " (SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO_CABECERA = PEDI.ID_PEDIDO_CABECERA)  \"TOTAL\", ";
        }
        if (conFecha) {
            if (esTiempoRecepcionOEntrega) {
                SQL_TIEMPO = "AND PEDI.TIEMPO_RECEPCION BETWEEN ? AND ? ";
            } else {
                SQL_TIEMPO = "AND PEDI.TIEMPO_ENTREGA BETWEEN ? AND ? ";
            }
        }
        if (idTipoOperacion > 0) {
            SQL_TIOP = "AND PEDI.ID_COND_VENTA = ? ";
        }
        if (idEstado > 0) {
            SQL_ESTADO_PEDIDO = "AND PEDI.ID_PEDIDO_ESTADO = ? ";
        }
        if (idCliente > 0) {
            SQL_CLIENTE = "AND PEDI.ID_CLIENTE = ? ";
        }
        if (idVendedor > 0) {
            SQL_VENDEDOR = "AND PEDI.ID_FUNCIONARIO = ? ";
        }
        if (idPedido > 0) {
            SQL_ID_PEDIDO = "AND PEDI.ID_PEDIDO_CABECERA = ? ";
        }
        String QUERY = "SELECT PEDI.ID_PEDIDO_CABECERA \"ID\", "
                + "(SELECT PERS.NOMBRE WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"VENDEDOR_NOMBRE\", "
                + "(SELECT PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA)\"VENDEDOR_APELLIDO\", "
                + "(SELECT CLIE.ENTIDAD FROM CLIENTE CLIE WHERE CLIE.ID_CLIENTE = PEDI.ID_CLIENTE) \"CLIENTE_ENTIDAD\", "
                + "PEDI.TIEMPO_RECEPCION, "
                + "PEDI.TIEMPO_ENTREGA, "
                + SQL_TOTAL
                + "PEDI.ID_PEDIDO_ESTADO \"ESTADO_ID\", "
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"ESTADO_DESCRIPCION\", "
                + "PEDI.ID_COND_VENTA \"COND_VENTA_ID\", "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = PEDI.ID_COND_VENTA) \"COND_VENTA_DESCRIPCION\" "
                + "FROM PEDIDO_CABECERA PEDI, FUNCIONARIO FUNC, PERSONA PERS "
                + "WHERE PEDI.ID_FUNCIONARIO = FUNC.ID_FUNCIONARIO "
                + "AND PERS.ID_PERSONA = FUNC.ID_PERSONA ";
        QUERY = QUERY + SQL_TIEMPO + SQL_TIOP + SQL_ESTADO_PEDIDO + SQL_CLIENTE + SQL_VENDEDOR + SQL_ID_PEDIDO + SQL_ORDER_BY;
        int pos = 1;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos++, new java.sql.Timestamp(fechaInicio.getTime()));
                pst.setTimestamp(pos++, new java.sql.Timestamp(fechaFin.getTime()));
            }
            if (idTipoOperacion > 0) {
                pst.setInt(pos++, idTipoOperacion);
            }
            if (idEstado > 0) {
                pst.setInt(pos++, idEstado);
            }
            if (idCliente > 0) {
                pst.setInt(pos++, idCliente);
            }
            if (idVendedor > 0) {
                pst.setInt(pos++, idVendedor);
            }
            if (idPedido > 0) {
                pst.setInt(pos++, idPedido);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_cliente cliente = new M_cliente();
                cliente.setEntidad(rs.getString("CLIENTE_ENTIDAD"));
                M_funcionario funcionario = new M_funcionario();
                funcionario.setNombre(rs.getString("VENDEDOR_NOMBRE"));
                funcionario.setApellido(rs.getString("VENDEDOR_APELLIDO"));
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("COND_VENTA_ID"));
                tiop.setDescripcion(rs.getString("COND_VENTA_DESCRIPCION"));
                E_estadoPedido estado = new E_estadoPedido();
                estado.setId(rs.getInt("ESTADO_ID"));
                estado.setDescripcion(rs.getString("ESTADO_DESCRIPCION"));
                M_pedidoCabecera pedidoCabecera = new M_pedidoCabecera();
                pedidoCabecera.setIdPedido(rs.getInt("ID"));
                pedidoCabecera.setTiempoRecepcion(rs.getTimestamp("TIEMPO_RECEPCION"));
                pedidoCabecera.setTiempoEntrega(rs.getTimestamp("TIEMPO_ENTREGA"));
                pedidoCabecera.setCliente(cliente);
                pedidoCabecera.setFuncionario(funcionario);
                pedidoCabecera.setTotal(rs.getInt("TOTAL"));
                pedidoCabecera.setEstadoPedido(estado);
                pedidoCabecera.setTipoOperacion(tiop);
                list.add(pedidoCabecera);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pedido.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Pedido.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static ResultSetTableModel obtenerPedidosPendientes(boolean conTotal) {
        ResultSetTableModel rstm = null;
        String total = "";
        if (conTotal) {
            total = " COALESCE( ROUND((SELECT SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)) FROM PEDIDO_DETALLE PEDE WHERE PEDE.ID_PEDIDO_CABECERA = PEDI.ID_PEDIDO_CABECERA)), 0 )  \"Total\", ";
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
                + "AND PEDI.ID_PEDIDO_ESTADO = (SELECT PEES.ID_PEDIDO_ESTADO FROM PEDIDO_ESTADO PEES WHERE PEES.DESCRIPCION LIKE'Pendiente') "
                + "ORDER BY PEDI.TIEMPO_RECEPCION";
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

    public static M_pedidoCabecera obtenerPedido(Integer idPedido) {
        M_pedidoCabecera pedido = null;
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
                + "PEDI.ID_FACTURA_CABECERA, "
                + "(SELECT PEES.DESCRIPCION FROM PEDIDO_ESTADO PEES WHERE PEES.ID_PEDIDO_ESTADO = PEDI.ID_PEDIDO_ESTADO) \"ESTADO\","
                + "PEDI.DIRECCION \"PDIRECCION\", "
                + "PEDI.REFERENCIA, "
                + "(SELECT TIOP.DESCRIPCION FROM TIPO_OPERACION TIOP WHERE TIOP.ID_TIPO_OPERACION = PEDI.ID_COND_VENTA) \"COND_VENTA_DESCRIPCION\","
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
                f.setIdFuncionario(rs.getInt("id_funcionario"));
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

                E_estadoPedido ep = new E_estadoPedido();
                ep.setId(rs.getInt("ID_PEDIDO_ESTADO"));
                ep.setDescripcion(rs.getString("ESTADO"));

                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("ID_COND_VENTA"));
                tiop.setDescripcion(rs.getString("COND_VENTA_DESCRIPCION"));

                pedido = new M_pedidoCabecera();
                pedido.setIdPedido(rs.getInt("ID_PEDIDO_CABECERA"));
                pedido.setCliente(cliente);
                pedido.getCliente().setIdCliente(rs.getInt("ID_CLIENTE"));
                pedido.setTipoOperacion(tiop);
                pedido.setEstadoPedido(ep);
                pedido.setFuncionario(f);
                pedido.setTiempoEntrega(rs.getTimestamp("TIEMPO_ENTREGA"));
                pedido.setTiempoRecepcion(rs.getTimestamp("TIEMPO_RECEPCION"));
                pedido.setDireccion(rs.getString("PDIRECCION"));
                pedido.setReferencia(rs.getString("REFERENCIA"));
                pedido.setIdFacturaCabecera(rs.getInt("ID_FACTURA_CABECERA"));
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

    public static List<E_facturaDetalle> obtenerPedidoDetalle(int idPedido) {
        String QUERY = "SELECT "
                + "PEDE.ID_PEDIDO_DETALLE \"ID\", "
                + "PEDE.ID_PRODUCTO \"ID_PRODUCTO\", "
                + "PROD.DESCRIPCION \"PRODUCTO\", "
                + "PROD.CODIGO \"CODIGO_PROD\", "
                + "PEDE.CANTIDAD \"CANTIDAD\", "
                + "PEDE.PRECIO \"PRECIO\", "
                + "PEDE.DESCUENTO \"DESCUENTO\","
                + "PROD.ID_IMPUESTO \"ID_IMPUESTO\","
                + "PEDE.OBSERVACION \"OBSERVACION\" "
                + "FROM PEDIDO_DETALLE PEDE, PRODUCTO PROD "
                + "WHERE PROD.ID_PRODUCTO = PEDE.ID_PRODUCTO "
                + "AND PEDE.ID_PEDIDO_CABECERA = ?;";
        List<E_facturaDetalle> detalles = new ArrayList<>();
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaDetalle facturaDetalle = new E_facturaDetalle();
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO_PROD"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                facturaDetalle.setIdFacturaDetalle(rs.getInt("ID"));
                facturaDetalle.setProducto(producto);
                facturaDetalle.setCantidad(rs.getDouble("CANTIDAD"));
                facturaDetalle.setPrecio(rs.getDouble("PRECIO"));
                facturaDetalle.setDescuento(rs.getDouble("DESCUENTO"));
                facturaDetalle.setObservacion(rs.getString("OBSERVACION"));
                detalles.add(facturaDetalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pedido.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Pedido.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return detalles;
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
    public static long insertarPedido(M_pedidoCabecera pedido, List<E_facturaDetalle> pedidoDetalle) {
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO PEDIDO_DETALLE(ID_PEDIDO_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        String INSERT_PEDIDO = "INSERT INTO PEDIDO_CABECERA(ID_CLIENTE, ID_FUNCIONARIO, TIEMPO_ENTREGA, ID_COND_VENTA, ID_PEDIDO_ESTADO, DIRECCION, REFERENCIA)VALUES (?, ?, ?, ?, ?, ?, ?);";
        long idPedido = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_PEDIDO, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, pedido.getCliente().getIdCliente());
            pst.setInt(2, pedido.getFuncionario().getIdFuncionario());
            pst.setTimestamp(3, new Timestamp(pedido.getTiempoEntrega().getTime()));
            pst.setInt(4, pedido.getTipoOperacion().getId());
            pst.setInt(5, pedido.getEstadoPedido().getId());
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
                if (pedido.getReferencia().isEmpty()) {
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
                pst.setDouble(4, pedidoDetalle.get(i).getPrecio());
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
    public static void actualizarPedido(M_pedidoCabecera pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_FUNCIONARIO= " + pedido.getFuncionario().getIdFuncionario() + ", "
                + "ID_CLIENTE=" + pedido.getCliente().getIdCliente() + ", "
                + "DIRECCION= '" + pedido.getDireccion() + "', "
                + "REFERENCIA= '" + pedido.getReferencia() + "', "
                + "TIEMPO_ENTREGA= '" + pedido.getTiempoEntrega() + "', "
                + "ID_PEDIDO_ESTADO = " + pedido.getEstadoPedido().getId() + ", "
                + "ID_COND_VENTA = " + pedido.getTipoOperacion().getId()
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

    public static void actualizarPedidoCliente(M_pedidoCabecera pedido) {
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
            pst.setDouble(2, pedidoDetalle.getPrecio());
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

    public static void actualizarPedidoEstado(M_pedidoCabecera pedido) {
        String UPDATE_PEDIDO = "UPDATE PEDIDO_CABECERA SET "
                + "ID_PEDIDO_ESTADO = " + pedido.getEstadoPedido().getId() + " "
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
            pst.setDouble(4, detalle.getPrecio());
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

    public static int pagarPedido(M_pedidoCabecera pedido, List<E_facturaDetalle> detalle, int nroFactura, int idTimbrado) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, NRO_FACTURA, ID_COND_VENTA, ID_TIMBRADO)VALUES (?, ?, ?, ?, ?);";

        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, pedido.getFuncionario().getIdFuncionario());
            pst.setInt(2, pedido.getCliente().getIdCliente());
            try {
                if (nroFactura < 0) {
                    pst.setNull(3, Types.BIGINT);
                } else {
                    pst.setInt(3, nroFactura);
                }
            } catch (Exception e) {
                pst.setNull(3, Types.BIGINT);
            }
            pst.setInt(4, pedido.getTipoOperacion().getId());
            if (idTimbrado < 0) {
                pst.setNull(5, Types.BIGINT);
            } else {
                pst.setInt(5, idTimbrado);
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
                pst.setInt(2, detalle.get(i).getProducto().getId());
                pst.setDouble(3, detalle.get(i).getCantidad());
                pst.setDouble(4, detalle.get(i).getPrecio());
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

            //se resta del stock lo que se vende
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")-" + detalle.get(i).getCantidad() + ") "
                        + "WHERE ID_PRODUCTO =" + detalle.get(i).getProducto().getId();
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
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
        return (int) sq_cabecera;
    }

    public static ArrayList<M_pedidoDetalle> obtenerPedidoDetalles(Integer idPedido) {
        ArrayList<M_pedidoDetalle> detalles = null;
        String query = "SELECT ID_PEDIDO_DETALLE, ID_PEDIDO_CABECERA, PD.ID_PRODUCTO,"
                + "P.DESCRIPCION \"PRODUCTO\", "
                + "(SELECT I.ID_IMPUESTO FROM IMPUESTO I WHERE I.ID_IMPUESTO = P.ID_IMPUESTO)\"ID_IMPUESTO\", "
                + "CANTIDAD, PRECIO, DESCUENTO, PD.OBSERVACION "
                + "FROM PEDIDO_DETALLE PD, PRODUCTO P "
                + "WHERE PD.ID_PRODUCTO = P.ID_PRODUCTO "
                + "AND PD.ID_PEDIDO_CABECERA = " + idPedido;
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
                detalle.setPrecio(rs.getDouble("PRECIO"));
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

    public static List<E_facturaDetalle> consultarDetalleAgrupado(List<M_pedidoCabecera> cadenaCabeceras) {
        List<E_facturaDetalle> list = new ArrayList<>();
        boolean b = true;
        StringBuilder builder = new StringBuilder();
        for (M_pedidoCabecera pedidoCabecera : cadenaCabeceras) {
            builder.append("?,");
            b = false;
        }
        //para controlar que la lista contenga por lo menos una venta seleccionada
        if (b) {
            return list;
        }
        String QUERY = "SELECT "
                + "PD.ID_PRODUCTO,"
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = PD.ID_PRODUCTO)\"PRODUCTO\", "
                + "(SELECT P.ID_IMPUESTO FROM PRODUCTO P WHERE P.ID_PRODUCTO = PD.ID_PRODUCTO)\"ID_IMPUESTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = PD.ID_PRODUCTO)\"CODIGO_PROD\", "
                + "SUM(PD.CANTIDAD) \"CANTIDAD\", "
                + "PD.PRECIO , "
                + "SUM(PD.DESCUENTO) \"DESCUENTO\" "
                + "FROM PEDIDO_DETALLE PD, PEDIDO_CABECERA PC "
                + "WHERE PC.ID_PEDIDO_CABECERA = PD.ID_PEDIDO_CABECERA "
                + "AND PC.ID_PEDIDO_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ") ";

        String PIE = "GROUP BY PD.ID_PRODUCTO, PD.PRECIO, \"PRODUCTO\", \"ID_IMPUESTO\", \"CODIGO_PROD\" "
                + "ORDER BY \"PRODUCTO\" ;";
        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (M_pedidoCabecera notaCredito : cadenaCabeceras) {
                pst.setInt(index, notaCredito.getIdPedido());
                index++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaDetalle fade = new E_facturaDetalle();
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO_PROD"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
                fade.setProducto(producto);
                fade.setCantidad(rs.getDouble("CANTIDAD"));
                fade.setPrecio(rs.getDouble("PRECIO"));
                fade.setDescuento(rs.getDouble("DESCUENTO"));
                fade.setObservacion("");
                list.add(fade);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Pedido.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
