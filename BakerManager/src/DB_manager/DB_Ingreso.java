/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_mesa;
import Entities.M_mesa_detalle;
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
 * @author Ramiro Ferreira
 */
public class DB_Ingreso {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    /*
     * READ
     */

    public static ResultSetTableModel obtenerIngreso(String inicio, String fin, String tipo_operacion, String nroFactura, M_facturaCabecera factura_cabecera) {
        ResultSetTableModel rstm = null;

        String Query = "SELECT FACA_ID_FACTURA_CABECERA \"ID\", "
                + "(SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE_ENTIDAD FROM CLIENTE WHERE CLIE_ID_CLIENTE = FACA_ID_CLIENTE) \"Cliente\", "
                + "to_char(FACA_TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "ROUND((SELECT SUM (FADE_CANTIDAD*(FADE_PRECIO-(FADE_PRECIO*FADE_DESCUENTO)/100)) FROM FACTURA_DETALLE WHERE FADE_ID_FACTURA_CABECERA = FACA_ID_FACTURA_CABECERA))\"Total\", "
                + "(SELECT TIOP_DESCRIPCION FROM TIPO_OPERACION WHERE TIOP_ID_TIPO_OPERACION = FACA_ID_COND_VENTA) \"Cond. venta\" "
                + "FROM FACTURA_CABECERA,FUNCIONARIO, PERSONA "
                + "WHERE  FACA_TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND FACA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA ";
        if (!nroFactura.isEmpty()) {
            Query = Query + " AND FACA_ID_FACTURA_CABECERA = " + nroFactura;
        }
        if (!"Todos".equals(tipo_operacion)) {
            Query = Query + " AND FACA_ID_COND_VENTA = (SELECT TIOP_ID_TIPO_OPERACION FROM TIPO_OPERACION WHERE TIOP_DESCRIPCION LIKE'" + tipo_operacion + "')";
        }
        if (null != factura_cabecera) {
            if (null != factura_cabecera.getCliente()) {
                Query = Query + " AND FACA_ID_CLIENTE = " + factura_cabecera.getCliente().getIdCliente();
            }
            if (null != factura_cabecera.getFuncionario()) {
                Query = Query + " AND FACA_ID_FUNCIONARIO = " + factura_cabecera.getFuncionario().getId_funcionario();
            }
        }
        try {
            System.out.println("64: " + Query);
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
    /*
     * INSERT
     */

    public static void insertarIngreso(M_facturaCabecera cabecera, ArrayList<M_facturaDetalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(FADE_ID_FACTURA_CABECERA, FADE_ID_PRODUCTO, FADE_CANTIDAD, FADE_PRECIO, FADE_DESCUENTO, FADE_EXENTA, FADE_IVA5, FADE_IVA10, FADE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(FACA_ID_FUNCIONARIO, FACA_ID_CLIENTE, FACA_ID_COND_VENTA)VALUES (?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getIdFuncionario());
            pst.setInt(2, cabecera.getIdCliente());
            pst.setInt(3, cabecera.getIdCondVenta());
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
                pst.setInt(6, detalle.get(i).getExenta());
                pst.setInt(7, detalle.get(i).getIva5());
                pst.setInt(8, detalle.get(i).getIva10());
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

    public static M_facturaCabecera obtenerIngresoCabeceraID(Integer idIngresoCabecera) {
        M_facturaCabecera ingreso_cabecera = null;
        String query = "SELECT FACA_ID_FACTURA_CABECERA, "
                + "FACA_ID_FUNCIONARIO, "
                + "FACA_ID_CLIENTE, "
                + "FACA_TIEMPO, "
                + "FACA_ID_COND_VENTA "
                + "FROM FACTURA_CABECERA "
                + "WHERE FACA_ID_FACTURA_CABECERA = " + idIngresoCabecera;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                ingreso_cabecera = new M_facturaCabecera();
                ingreso_cabecera.setIdFacturaCabecera(rs.getInt("FACA_ID_FACTURA_CABECERA"));
                ingreso_cabecera.setIdCliente(rs.getInt("FACA_ID_CLIENTE"));
                ingreso_cabecera.setIdCondVenta(rs.getInt("FACA_ID_COND_VENTA"));
                ingreso_cabecera.setIdFuncionario(rs.getInt("FACA_ID_FUNCIONARIO"));
                ingreso_cabecera.setTiempo(rs.getTimestamp("FACA_TIEMPO"));
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
        return ingreso_cabecera;
    }

    public static ResultSetTableModel obtenerIngresoDetalle(Integer idIngresoCabecera) {
        String queryProducto = "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = FADE_ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "FADE_ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "FADE_CANTIDAD \"Cantidad\", "
                + "FADE_PRECIO \"Precio\", "
                + "FADE_DESCUENTO \"Descuento\","
                + "FADE_EXENTA \"Exenta\", "
                + "FADE_IVA5 \"IVA 5%\", "
                + "FADE_IVA10 \"IVA 10%\", "
                + "FADE_OBSERVACION \"Obs.\" "
                + "FROM FACTURA_DETALLE "
                + "WHERE FADE_ID_FACTURA_CABECERA = " + idIngresoCabecera;
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
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerMesa(String inicio, String fin, String tipo_operacion) {
        ResultSetTableModel rstm = null;
        String q = "SELECT MESA_ID_MESA \"ID\", (SELECT PERS_NOMBRE || ' '|| PERS_APELLIDO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA)\"Empleado\", "
                + "(SELECT CLIE_ENTIDAD FROM CLIENTE WHERE CLIE_ID_CLIENTE = MESA_ID_CLIENTE) \"Cliente\", "
                + "to_char(MESA_TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "MESA_NUMERO \"Nro. mesa\", "
                + "ROUND((SELECT SUM(MEDE_CANTIDAD*(MEDE_PRECIO-(MEDE_PRECIO*MEDE_DESCUENTO)/100)) FROM MESA_DETALLE WHERE MEDE_ID_MESA = MESA_ID_MESA))\"Total\" "
                + "FROM MESA,FUNCIONARIO, PERSONA "
                + "WHERE MESA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND PERS_ID_PERSONA = FUNC_ID_PERSONA ";
        String tiempo = "AND MESA_TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp ";
        if ((!inicio.isEmpty() && !fin.isEmpty())) {
            q = q + tiempo;
        }
        try {
            System.out.println("241: " + q);
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(q);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerMesaDetalle(int idMesa) {
        String queryProducto = "(SELECT PROD_DESCRIPCION FROM PRODUCTO WHERE PROD_ID_PRODUCTO = MEDE_ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "MEDE_ID_MESA_DETALLE \"ID\", "
                + "MEDE_ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "MEDE_CANTIDAD \"Cantidad\", "
                + "MEDE_PRECIO \"Precio\", "
                + "MEDE_DESCUENTO \"Descuento\","
                + "MEDE_EXENTA \"Exenta\", "
                + "MEDE_IVA5 \"IVA 5%\", "
                + "MEDE_IVA10 \"IVA 10%\", "
                + "MEDE_OBSERVACION \"Obs.\" "
                + "FROM MESA_DETALLE "
                + "WHERE MEDE_ID_MESA = " + idMesa;
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

    public static long insertarMesa(M_mesa mesa, ArrayList<M_mesa_detalle> detalle) {
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETAIL = "INSERT INTO MESA_DETALLE(MEDE_ID_MESA, MEDE_ID_PRODUCTO, MEDE_CANTIDAD, MEDE_PRECIO, MEDE_DESCUENTO, MEDE_EXENTA, MEDE_IVA5, MEDE_IVA10, MEDE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_MESA = "INSERT INTO MESA(MESA_ID_FUNCIONARIO, MESA_ID_CLIENTE, MESA_ID_COND_VENTA, MESA_NUMERO)VALUES (?, ?, ?, ?);";
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
                pst.setInt(6, detalle.get(i).getExenta());
                pst.setInt(7, detalle.get(i).getIva5());
                pst.setInt(8, detalle.get(i).getIva10());
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
        return sq_cabecera;
    }

    public static M_mesa obtenerMesaID(Integer idMesa) {
        M_mesa mesa = null;
        String genero = "(SELECT SEXO_DESCRIPCION  FROM SEXO WHERE SEXO_ID_SEXO = PERS_ID_SEXO) \"pers_sexo\"";
        String pais = "(SELECT PAIS_DESCRIPCION FROM PAIS WHERE PERS_ID_PAIS=PAIS_ID_PAIS) \"PERS_NACIONALIDAD\"";
        String ciudad = " (SELECT CIUD_DESCRIPCION FROM CIUDAD WHERE PERS_ID_CIUDAD=CIUD_ID_CIUDAD)\"PERS_CIUDAD\"";
        String estadoCivil = " (SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL=PERS_ID_ESTADO_CIVIL)\"pers_estado_civil\"";
        String estado = " (SELECT ESTA_DESCRIPCION FROM ESTADO WHERE ESTA_ID_ESTADO=FUNC_ID_ESTADO)\"func_estado\"";

        String categoria = "(SELECT CLCA_DESCRIPCION FROM CLIENTE_CATEGORIA WHERE CLCA_ID_CLIENTE_CATEGORIA = CLIE_ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI_DESCRIPCION FROM CLIENTE_TIPO WHERE CLTI_ID_CLIENTE_TIPO = CLIE_ID_TIPO) \"TIPO\" ";

        String q = "SELECT MESA_ID_MESA, MESA_ID_FUNCIONARIO, MESA_ID_CLIENTE, MESA_TIEMPO,MESA_NUMERO, "
                + "       MESA_ID_COND_VENTA, "
                + "CLIE_ID_CLIENTE, CLIE_NOMBRE, CLIE_ENTIDAD, CLIE_RUC, CLIE_RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       CLIE_DIRECCION, CLIE_EMAIL, CLIE_PAG_WEB, CLIE_ID_TIPO, CLIE_ID_CATEGORIA, "
                + "       CLIE_OBSERVACION, "
                + "FUNC_ID_FUNCIONARIO, FUNC_ID_PERSONA, FUNC_ALIAS, FUNC_FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estado + "," + estadoCivil + ","
                + "       FUNC_ID_ESTADO, FUNC_FECHA_SALIDA, FUNC_SALARIO, FUNC_NRO_CELULAR, "
                + "       FUNC_NRO_TELEFONO, FUNC_EMAIL, FUNC_DIRECCION, FUNC_OBSERVACION,PERS_ID_PERSONA, PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, "
                + "       PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD "
                + "  FROM MESA,FUNCIONARIO,CLIENTE,PERSONA "
                + "  WHERE MESA_ID_FUNCIONARIO = FUNC_ID_FUNCIONARIO "
                + "AND MESA_ID_CLIENTE = CLIE_ID_CLIENTE "
                + "AND FUNC_ID_PERSONA = PERS_ID_PERSONA "
                + "AND MESA_ID_MESA = " + idMesa;
        try {
            pst = DB_manager.getConection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

                mesa = new M_mesa();
                mesa.setIdCondVenta(rs.getInt("MESA_ID_COND_VENTA"));
                mesa.setIdMesa(rs.getInt("MESA_ID_MESA"));
                mesa.setNumeroMesa(rs.getInt("MESA_NUMERO"));
                mesa.setTiempo(rs.getTimestamp("MESA_TIEMPO"));
                mesa.setFuncionario(f);
                mesa.setCliente(cliente);
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
        return mesa;
    }

    public static void actualizarMesa(M_mesa mesa) {
        String UPDATE_MESA = "UPDATE MESA SET "
                + "MESA_ID_FUNCIONARIO= " + mesa.getFuncionario().getId_funcionario() + ", "
                + "MESA_ID_CLIENTE=" + mesa.getCliente().getIdCliente() + ", "
                + "MESA_NUMERO=" + mesa.getNumeroMesa() + ", "
                + "MESA_ID_COND_VENTA = " + mesa.getIdCondVenta()
                + " WHERE MESA_ID_MESA = " + mesa.getIdMesa();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_MESA);
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

    public static void actualizarMesaDetalle(M_mesa_detalle mesaDetalle) {
        String UPDATE_MESA = "UPDATE MESA_DETALLE SET "
                + "MEDE_CANTIDAD= " + mesaDetalle.getCantidad() + ", "
                + "MEDE_PRECIO=" + mesaDetalle.getPrecio() + ", "
                + "MEDE_DESCUENTO=" + mesaDetalle.getDescuento() + ", "
                + "MEDE_EXENTA=" + mesaDetalle.getExenta() + ", "
                + "MEDE_IVA5=" + mesaDetalle.getIva5() + ", "
                + "MEDE_IVA10=" + mesaDetalle.getIva10() + ", "
                + "MEDE_OBSERVACION= '" + mesaDetalle.getObservacion() + "' "
                + "WHERE MEDE_ID_MESA_DETALLE = " + mesaDetalle.getIdMesaDetalle();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_MESA);
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

    public static void eliminarMesa(int idMesa) {
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE MEDE_ID_MESA = " + idMesa;
        String DELETE_HEADER = "DELETE FROM MESA WHERE MESA_ID_MESA = " + idMesa;
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
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE MEDE_ID_MESA_DETALLE = " + idMesaDetalle;
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
            rs = st.executeQuery(QUERY);//Bolsa p/ hielo 10*35
            if (!rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void insertarMesaDetalle(int idMesa, M_mesa_detalle mesaDetalle) {
        String INSERT_MESA_DETALLE = "INSERT INTO MESA_DETALLE(MEDE_ID_MESA, MEDE_ID_PRODUCTO, MEDE_CANTIDAD, MEDE_PRECIO, MEDE_DESCUENTO, MEDE_EXENTA, MEDE_IVA5, MEDE_IVA10, MEDE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_MESA_DETALLE);
            pst.setInt(1, idMesa);
            pst.setInt(2, mesaDetalle.getProducto().getId());
            pst.setDouble(3, mesaDetalle.getCantidad());
            pst.setInt(4, mesaDetalle.getPrecio());
            pst.setDouble(5, mesaDetalle.getDescuento());
            pst.setInt(6, mesaDetalle.getExenta());
            pst.setInt(7, mesaDetalle.getIva5());
            pst.setInt(8, mesaDetalle.getIva10());
            try {
                if (mesaDetalle.getObservacion() == null) {
                    pst.setNull(9, Types.VARCHAR);
                } else {
                    pst.setString(9, mesaDetalle.getObservacion());
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

    public static void transferirMesaAVenta(M_mesa mesa, ArrayList<M_mesa_detalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(FADE_ID_FACTURA_CABECERA, FADE_ID_PRODUCTO, FADE_CANTIDAD, FADE_PRECIO, FADE_DESCUENTO, FADE_EXENTA, FADE_IVA5, FADE_IVA10, FADE_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(FACA_ID_FUNCIONARIO, FACA_ID_CLIENTE, FACA_ID_COND_VENTA)VALUES (?, ?, ?);";
        String DELETE_DETAIL = "DELETE FROM MESA_DETALLE WHERE MEDE_ID_MESA = " + mesa.getIdMesa();
        String DELETE_HEADER = "DELETE FROM MESA WHERE MESA_ID_MESA = " + mesa.getIdMesa();
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
                pst.setInt(6, detalle.get(i).getExenta());
                pst.setInt(7, detalle.get(i).getIva5());
                pst.setInt(8, detalle.get(i).getIva10());
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
