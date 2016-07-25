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

        String Query = "SELECT ID_FACTURA_CABECERA \"ID\", "
                + "(SELECT NOMBRE || ' '|| APELLIDO WHERE F.ID_PERSONA = P.ID_PERSONA)\"Empleado\", "
                + "(SELECT ENTIDAD FROM CLIENTE WHERE FC.ID_CLIENTE = C.ID_CLIENTE) \"Cliente\", "
                + "to_char(TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "ROUND((SELECT SUM (CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM FACTURA_DETALLE FCC WHERE FCC.ID_FACTURA_CABECERA = FC.ID_FACTURA_CABECERA))\"Total\", "
                + "(SELECT TO.DESCRIPCION FROM TIPO_OPERACION TO WHERE TO.ID_TIPO_OPERACION = FC.ID_COND_VENTA) \"Cond. venta\" "
                + "FROM FACTURA_CABECERA FC ,FUNCIONARIO F, PERSONA P "
                + "WHERE  FC.TIEMPO BETWEEN '" + inicio + "'::timestamp  "
                + "AND '" + fin + "'::timestamp "
                + "AND FC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA ";
        if (!nroFactura.isEmpty()) {
            Query = Query + " AND FC.ID_FACTURA_CABECERA = " + nroFactura;
        }
        if (!"Todos".equals(tipo_operacion)) {
            Query = Query + " AND FC.ID_COND_VENTA = (SELECT TO.ID_TIPO_OPERACION FROM TIPO_OPERACION TO WHERE TO.DESCRIPCION LIKE'" + tipo_operacion + "')";
        }
        if (null != factura_cabecera) {
            if (null != factura_cabecera.getCliente()) {
                Query = Query + " AND FC.ID_CLIENTE = " + factura_cabecera.getCliente().getIdCliente();
            }
            if (null != factura_cabecera.getFuncionario()) {
                Query = Query + " AND FC.ID_FUNCIONARIO = " + factura_cabecera.getFuncionario().getId_funcionario();
            }
        }
        try {
            System.out.println("64: " + Query);
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
    /*
     * INSERT
     */

    public static void insertarIngreso(M_facturaCabecera cabecera, ArrayList<M_facturaDetalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA)VALUES (?, ?, ?);";
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
    }

    public static M_facturaCabecera obtenerIngresoCabeceraID(Integer idIngresoCabecera) {
        M_facturaCabecera ingreso_cabecera = null;
        String query = "SELECT ID_FACTURA_CABECERA, "
                + "ID_FUNCIONARIO, "
                + "ID_CLIENTE, "
                + "TIEMPO, "
                + "ID_COND_VENTA "
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

    public static ResultSetTableModel obtenerIngresoDetalle(Integer idIngresoCabecera) {
        String queryProducto = "(SELECT PR.DESCRIPCION FROM PRODUCTO PR WHERE PR.ID_PRODUCTO = FC.ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "CANTIDAD \"Cantidad\", "
                + "PRECIO \"Precio\", "
                + "DESCUENTO \"Descuento\","
                + "EXENTA \"Exenta\", "
                + "IVA5 \"IVA 5%\", "
                + "IVA10 \"IVA 10%\", "
                + "OBSERVACION \"Obs.\" "
                + "FROM FACTURA_DETALLE "
                + "WHERE ID_FACTURA_CABECERA = " + idIngresoCabecera;
        ResultSetTableModel rstm = null;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerMesa(String inicio, String fin, String tipo_operacion) {
        ResultSetTableModel rstm = null;
        String q = "SELECT M.ID_MESA \"ID\", (SELECT NOMBRE || ' '|| APELLIDO WHERE M.ID_PERSONA = P.ID_PERSONA)\"Empleado\", "
                + "(SELECT C.ENTIDAD FROM CLIENTE C WHERE C.ID_CLIENTE = M.ID_CLIENTE) \"Cliente\", "
                + "to_char(TIEMPO,'DD/MM/YYYY HH24:MI:SS:MS') \"Tiempo\", "
                + "NUMERO \"Nro. mesa\", "
                + "ROUND((SELECT SUM(CANTIDAD*(PRECIO-(PRECIO*DESCUENTO)/100)) FROM DETALLE WHERE ID_MESA = ID_MESA))\"Total\" "
                + "FROM MESA M,FUNCIONARIO F, PERSONA P"
                + "WHERE ID_FUNCIONARIO = ID_FUNCIONARIO "
                + "AND ID_PERSONA = ID_PERSONA ";
        String tiempo = "AND TIEMPO BETWEEN '" + inicio + "'::timestamp  "
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
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerMesaDetalle(int idMesa) {
        String queryProducto = "(SELECT DESCRIPCION FROM PRODUCTO WHERE ID_PRODUCTO = ID_PRODUCTO) \"Producto\", ";
        String Query = "SELECT "
                + "ID_DETALLE \"ID\", "
                + "ID_PRODUCTO \"ID art.\", "
                + queryProducto
                + "CANTIDAD \"Cantidad\", "
                + "PRECIO \"Precio\", "
                + "DESCUENTO \"Descuento\","
                + "EXENTA \"Exenta\", "
                + "IVA5 \"IVA 5%\", "
                + "IVA10 \"IVA 10%\", "
                + "OBSERVACION \"Obs.\" "
                + "FROM DETALLE "
                + "WHERE ID_MESA = " + idMesa;
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
        String INSERT_DETAIL = "INSERT INTO DETALLE(ID_MESA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, EXENTA, IVA5, IVA10, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_MESA = "INSERT INTO MESA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA, NUMERO)VALUES (?, ?, ?, ?);";
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
        String genero = "(SELECT SEXO_DESCRIPCION  FROM SEXO WHERE SEXO_ID_SEXO = ID_SEXO) \"pers_sexo\"";
        String pais = "(SELECT PAIS_DESCRIPCION FROM PAIS WHERE ID_PAIS=PAIS_ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT CIUD_DESCRIPCION FROM CIUDAD WHERE ID_CIUDAD=CIUD_ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL=ID_ESTADO_CIVIL)\"pers_estado_civil\"";
        String estado = " (SELECT ESTA_DESCRIPCION FROM ESTADO WHERE ESTA_ID_ESTADO=ID_ESTADO)\"func_estado\"";

        String categoria = "(SELECT CLCA_DESCRIPCION FROM CLIENTE_CATEGORIA WHERE CLCA_ID_CLIENTE_CATEGORIA = ID_CATEGORIA) \"CATEGORIA\" ";
        String tipo = "(SELECT CLTI_DESCRIPCION FROM CLIENTE_TIPO WHERE CLTI_ID_CLIENTE_TIPO = ID_TIPO) \"TIPO\" ";

        String q = "SELECT ID_MESA, ID_FUNCIONARIO, ID_CLIENTE, TIEMPO,NUMERO, "
                + "       ID_COND_VENTA, "
                + "ID_CLIENTE, NOMBRE, ENTIDAD, RUC, RUC_IDENTIFICADOR, " + categoria + "," + tipo + ","
                + "       DIRECCION, EMAIL, PAG_WEB, ID_TIPO, ID_CATEGORIA, "
                + "       OBSERVACION, "
                + "ID_FUNCIONARIO, ID_PERSONA, ALIAS, FECHA_INGRESO, " + genero + "," + pais + "," + ciudad + "," + estado + "," + estadoCivil + ","
                + "       ID_ESTADO, FECHA_SALIDA, SALARIO, NRO_CELULAR, "
                + "       NRO_TELEFONO, EMAIL, DIRECCION, OBSERVACION,ID_PERSONA, CI, NOMBRE, APELLIDO, ID_SEXO, "
                + "       FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD "
                + "  FROM MESA,FUNCIONARIO,CLIENTE,PERSONA "
                + "  WHERE ID_FUNCIONARIO = ID_FUNCIONARIO "
                + "AND ID_CLIENTE = ID_CLIENTE "
                + "AND ID_PERSONA = ID_PERSONA "
                + "AND ID_MESA = " + idMesa;
        try {
            pst = DB_manager.getConection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setPais(rs.getString("NACIONALIDAD"));
                f.setCiudad(rs.getString("CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("pers_sexo"));
                f.setNro_celular(rs.getString("func_nro_celular"));
                f.setNro_telefono(rs.getString("func_nro_telefono"));
                f.setEmail(rs.getString("func_email"));
                f.setDireccion(rs.getString("DIRECCION"));
                f.setAlias(rs.getString("func_alias"));
                f.setNombre(rs.getString("pers_nombre"));
                f.setApellido(rs.getString("pers_apellido"));
                f.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                f.setId_persona(rs.getInt("pers_id_persona"));
                f.setCedula(rs.getInt("pers_ci"));
                f.setEstado_civil(rs.getString("pers_estado_civil"));
                f.setId_funcionario(rs.getInt("func_id_funcionario"));
                f.setObservacion(rs.getString("OBSERVACION"));

                M_cliente cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("DIRECCION"));
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

                mesa = new M_mesa();
                mesa.setIdCondVenta(rs.getInt("ID_COND_VENTA"));
                mesa.setIdMesa(rs.getInt("ID_MESA"));
                mesa.setNumeroMesa(rs.getInt("NUMERO"));
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
        String UPDATE_MESA = "UPDATE MESA SET "
                + "ID_FUNCIONARIO= " + mesa.getFuncionario().getId_funcionario() + ", "
                + "ID_CLIENTE=" + mesa.getCliente().getIdCliente() + ", "
                + "NUMERO=" + mesa.getNumeroMesa() + ", "
                + "ID_COND_VENTA = " + mesa.getIdCondVenta()
                + " WHERE ID_MESA = " + mesa.getIdMesa();
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
        String UPDATE_MESA = "UPDATE DETALLE SET "
                + "CANTIDAD= " + mesaDetalle.getCantidad() + ", "
                + "PRECIO=" + mesaDetalle.getPrecio() + ", "
                + "DESCUENTO=" + mesaDetalle.getDescuento() + ", "
                + "EXENTA=" + mesaDetalle.getExenta() + ", "
                + "IVA5=" + mesaDetalle.getIva5() + ", "
                + "IVA10=" + mesaDetalle.getIva10() + ", "
                + "OBSERVACION= '" + mesaDetalle.getObservacion() + "' "
                + "WHERE ID_DETALLE = " + mesaDetalle.getIdMesaDetalle();
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
        String DELETE_DETAIL = "DELETE FROM DETALLE WHERE ID_MESA = " + idMesa;
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
        String DELETE_DETAIL = "DELETE FROM DETALLE WHERE ID_DETALLE = " + idMesaDetalle;
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
        String QUERY = "SELECT NUMERO FROM MESA WHERE NUMERO = " + numeroMesa;
        try {
            st = DB_manager.getConection().createStatement();
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(QUERY);//Bolsa p/ hielo 10*35
            if (!rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void insertarMesaDetalle(int idMesa, M_mesa_detalle mesaDetalle) {
        String INSERT_DETALLE = "INSERT INTO DETALLE(ID_MESA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, EXENTA, IVA5, IVA10, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);
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

    public static void transferirMesaAVenta(M_mesa mesa, ArrayList<M_mesa_detalle> detalle) {
        String INSERT_DETALLE = "INSERT INTO FACTURA_DETALLE(ID_FACTURA_CABECERA, ID_PRODUCTO, CANTIDAD, PRECIO, DESCUENTO, EXENTA, IVA5, IVA10, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_CABECERA = "INSERT INTO FACTURA_CABECERA(ID_FUNCIONARIO, ID_CLIENTE, ID_COND_VENTA)VALUES (?, ?, ?);";
        String DELETE_DETAIL = "DELETE FROM DETALLE WHERE ID_MESA = " + mesa.getIdMesa();
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
}
