/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
import Entities.E_produccionTipoBaja;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_producto;
import ModeloTabla.RolloProducidoTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Produccion {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static boolean existeOrdenTrabajo(int ordenTrabajo) {
        String Query = "SELECT nro_orden_trabajo FROM PRODUCCION_CABECERA WHERE nro_orden_trabajo = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, ordenTrabajo);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static boolean existeOrdenTrabajo(int ordenTrabajo, E_produccionTipo tipoProduccion) {
        String Query = "SELECT nro_orden_trabajo FROM PRODUCCION_CABECERA WHERE nro_orden_trabajo = ? AND id_produccion_tipo = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, ordenTrabajo);
            pst.setInt(2, tipoProduccion.getId());
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static ArrayList<E_produccionTipo> obtenerTipoProduccion() {
        ArrayList<E_produccionTipo> list = null;
        String q = "SELECT *  "
                + "FROM PRODUCCION_TIPO;";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            list = new ArrayList();
            while (rs.next()) {
                E_produccionTipo tiop = new E_produccionTipo();
                tiop.setId(rs.getInt("id_produccion_tipo"));
                tiop.setDescripcion(rs.getString("descripcion"));
                list.add(tiop);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static int insertarProduccion(E_produccionCabecera produccionCabecera, List<E_produccionDetalle> detalle) {

        String INSERT_CABECERA = "INSERT INTO produccion_cabecera(nro_orden_trabajo, fecha_produccion, id_funcionario_responsable, id_funcionario_usuario, id_produccion_tipo)VALUES( ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, produccionCabecera.getNroOrdenTrabajo());
            pst.setTimestamp(2, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
            pst.setInt(3, produccionCabecera.getFuncionarioProduccion().getId_funcionario());
            pst.setInt(4, produccionCabecera.getFuncionarioSistema().getId_funcionario());
            pst.setInt(5, produccionCabecera.getTipo().getId());
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
                pst.executeUpdate();
                pst.close();
            }
            //se suma al stock lo que se produce
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")+" + detalle.get(i).getCantidad() + ") "
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
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static List<E_produccionCabecera> consultarProduccion(Date inicio, Date fin, int idProdTipo, int nroPedido, int idEstado, int idFuncionario, boolean conFecha) {
        int pos = 1;
        List<E_produccionCabecera> list = new ArrayList<>();
        String Query = "SELECT id_produccion_cabecera, "
                + "nro_orden_trabajo, "
                + "fecha_registro, "
                + "fecha_produccion, "
                + "fecha_vencimiento, "
                + "id_funcionario_responsable, "
                + "id_funcionario_usuario, "
                + "id_produccion_tipo, "
                + "id_estado, "
                + "(SELECT PRTI.DESCRIPCION FROM PRODUCCION_TIPO PRTI WHERE PRTI.ID_PRODUCCION_TIPO = PC.id_produccion_tipo) \"TIPO_PRODUCCION\", "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PC.ID_ESTADO) \"ESTADO\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_responsable )\"RESPONSABLE\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_usuario)\"USUARIO\" "
                + "FROM produccion_cabecera PC "
                + "WHERE  1=1 ";

        if (conFecha) {
            Query = Query + "AND PC.fecha_produccion BETWEEN ?  AND ? ";
        }
        if (idProdTipo > -1) {
            Query = Query + " AND PC.ID_PRODUCCION_TIPO = ? ";
        }
        if (idEstado > -1) {
            Query = Query + " AND PC.ID_ESTADO = ? ";
        }
        if (idFuncionario > -1) {
            Query = Query + " AND PC.id_funcionario_responsable = ? ";
        }
        if (nroPedido > -1) {
            Query = Query + " AND PC.nro_orden_trabajo = ? ";
        }
        Query = Query + " ORDER BY fecha_produccion ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos, new Timestamp(inicio.getTime()));
                pos++;
                pst.setTimestamp(pos, new Timestamp(fin.getTime()));
                pos++;
            }
            if (idProdTipo > -1) {
                pst.setInt(pos, idProdTipo);
                pos++;
            }
            if (idEstado > -1) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (nroPedido > -1) {
                pst.setInt(pos, nroPedido);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario responsable = new M_funcionario();
                responsable.setId_funcionario(rs.getInt("id_funcionario_responsable"));
                responsable.setNombre(rs.getString("RESPONSABLE"));
                M_funcionario usuario = new M_funcionario();
                usuario.setId_funcionario(rs.getInt("id_funcionario_usuario"));
                usuario.setNombre(rs.getString("USUARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_produccionTipo pt = new E_produccionTipo();
                pt.setId(rs.getInt("id_produccion_tipo"));
                pt.setDescripcion(rs.getString("TIPO_PRODUCCION"));
                E_produccionCabecera pc = new E_produccionCabecera();
                pc.setFuncionarioProduccion(responsable);
                pc.setFuncionarioSistema(usuario);
                pc.setTipo(pt);
                pc.setEstado(estado);
                pc.setId(rs.getInt("id_produccion_cabecera"));
                pc.setNroOrdenTrabajo(rs.getInt("nro_orden_trabajo"));
                pc.setFechaProduccion(rs.getDate("fecha_produccion"));
                pc.setFechaRegistro(rs.getDate("fecha_registro"));
                pc.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                list.add(pc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_produccionCabecera> consultarRollosUtilizados(Date inicio, Date fin, int nroOT, int idEstado, int idFuncionario, boolean conFecha) {
        int pos = 1;
        List<E_produccionCabecera> list = new ArrayList<>();
        String Query = "SELECT id_cabecera, "
                + "id_produccion_cabecera, "
                + "nro_orden_trabajo, "
                + "nro_film, "
                + "estado, "
                + "fecha, "
                + "id_producto, "
                + "producto_codigo, "
                + "producto, "
                + "id_categoria, "
                + "categoria, "
                + "peso, "
                + "cono, "
                + "medida, "
                + "micron "
                + "FROM v_produccion_film_baja "
                + "WHERE  1=1 ";

        if (conFecha) {
            Query = Query + "AND fecha BETWEEN ?  AND ? ";
        }
        if (idEstado > -1) {
            Query = Query + " AND estado = ? ";
        }
        if (idFuncionario > -1) {
            Query = Query + " AND id_funcionario_responsable = ? ";
        }
        if (nroOT > -1) {
            Query = Query + " AND nro_orden_trabajo = ? ";
        }
        Query = Query + " ORDER BY fecha ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos, new Timestamp(inicio.getTime()));
                pos++;
                pst.setTimestamp(pos, new Timestamp(fin.getTime()));
                pos++;
            }
            if (idEstado > -1) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (nroOT > -1) {
                pst.setInt(pos, nroOT);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario responsable = new M_funcionario();
                responsable.setId_funcionario(rs.getInt("id_funcionario_responsable"));
                responsable.setNombre(rs.getString("RESPONSABLE"));
                M_funcionario usuario = new M_funcionario();
                usuario.setId_funcionario(rs.getInt("id_funcionario_usuario"));
                usuario.setNombre(rs.getString("USUARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_produccionTipo pt = new E_produccionTipo();
                pt.setId(E_produccionTipo.ROLLO);
                pt.setDescripcion(E_produccionTipo.ROLLO_STRING);
                E_produccionCabecera pc = new E_produccionCabecera();
                pc.setFuncionarioProduccion(responsable);
                pc.setFuncionarioSistema(usuario);
                pc.setTipo(pt);
                pc.setEstado(estado);
                pc.setId(rs.getInt("id_produccion_cabecera"));
                pc.setNroOrdenTrabajo(rs.getInt("nro_orden_trabajo"));
                pc.setFechaProduccion(rs.getDate("fecha_produccion"));
                pc.setFechaRegistro(rs.getDate("fecha_registro"));
                pc.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                list.add(pc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_produccionDetalle> consultarProduccionDetalle(Integer idProduccion) {
        List<E_produccionDetalle> list = new ArrayList<>();
        String QUERY = "SELECT ID_PRODUCCION_DETALLE, "
                + "ID_PRODUCTO, "
                + "CANTIDAD, "
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = PRODUCCION_DETALLE.ID_PRODUCTO )\"PRODUCTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = PRODUCCION_DETALLE.ID_PRODUCTO )\"CODIGO\" "
                + "FROM PRODUCCION_DETALLE "
                + "WHERE ID_PRODUCCION_CABECERA = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO"));
                E_produccionDetalle pd = new E_produccionDetalle();
                pd.setId(rs.getInt("ID_PRODUCCION_DETALLE"));
                pd.setCantidad(rs.getDouble("CANTIDAD"));
                pd.setProducto(producto);
                list.add(pd);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Egreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_produccionDetalle> consultarUtilizacionMP(int idProduccion) {
        List<E_produccionDetalle> list = new ArrayList<>();
        String QUERY = "SELECT ID_PRODUCCION_FILM_MP_BAJA, "
                + "ID_PRODUCTO, "
                + "CANTIDAD, "
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = PRODUCCION_FILM_MP_BAJA.ID_PRODUCTO )\"PRODUCTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = PRODUCCION_FILM_MP_BAJA.ID_PRODUCTO )\"CODIGO\" "
                + "FROM PRODUCCION_FILM_MP_BAJA "
                + "WHERE ID_PRODUCCION_CABECERA = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO"));
                E_produccionDetalle pd = new E_produccionDetalle();
                pd.setId(rs.getInt("ID_PRODUCCION_FILM_MP_BAJA"));
                pd.setCantidad(rs.getDouble("CANTIDAD"));
                pd.setProducto(producto);
                list.add(pd);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static E_produccionCabecera obtenerProduccionCabecera(int idProduccion) {
        E_produccionCabecera pc = null;
        String Query = "SELECT id_produccion_cabecera, "
                + "nro_orden_trabajo, "
                + "fecha_registro, "
                + "fecha_produccion, "
                + "fecha_vencimiento, "
                + "id_funcionario_responsable, "
                + "id_funcionario_usuario, "
                + "id_produccion_tipo, "
                + "id_estado, "
                + "(SELECT PRTI.DESCRIPCION FROM PRODUCCION_TIPO PRTI WHERE PRTI.ID_PRODUCCION_TIPO = PC.id_produccion_tipo) \"TIPO_PRODUCCION\", "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PC.ID_ESTADO) \"ESTADO\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_responsable )\"RESPONSABLE\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_usuario)\"USUARIO\" "
                + "FROM produccion_cabecera PC "
                + "WHERE  PC.id_produccion_cabecera = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario responsable = new M_funcionario();
                responsable.setId_funcionario(rs.getInt("id_funcionario_responsable"));
                responsable.setNombre(rs.getString("RESPONSABLE"));
                M_funcionario usuario = new M_funcionario();
                usuario.setId_funcionario(rs.getInt("id_funcionario_usuario"));
                usuario.setNombre(rs.getString("USUARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_produccionTipo pt = new E_produccionTipo();
                pt.setId(rs.getInt("id_produccion_tipo"));
                pt.setDescripcion(rs.getString("TIPO_PRODUCCION"));
                pc = new E_produccionCabecera();
                pc.setFuncionarioProduccion(responsable);
                pc.setFuncionarioSistema(usuario);
                pc.setTipo(pt);
                pc.setEstado(estado);
                pc.setId(rs.getInt("id_produccion_cabecera"));
                pc.setNroOrdenTrabajo(rs.getInt("nro_orden_trabajo"));
                pc.setFechaProduccion(rs.getTimestamp("fecha_produccion"));
                pc.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                pc.setFechaVencimiento(rs.getTimestamp("fecha_vencimiento"));;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return pc;
    }

    public static void anularProduccion(int idProduccion, List<E_produccionDetalle> detalle) {
        String UPDATE_PRODUCCION = "UPDATE PRODUCCION_CABECERA SET "
                + "ID_ESTADO = 2, "
                + "NRO_ORDEN_TRABAJO = NULL "
                + "WHERE ID_PRODUCCION_CABECERA = ?; ";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_PRODUCCION);
            pst.setInt(1, idProduccion);
            pst.executeUpdate();
            pst.close();

            //se suma al stock lo que se produce
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")-" + detalle.get(i).getCantidad() + ") "
                        + "WHERE ID_PRODUCTO =" + detalle.get(i).getProducto().getId();
                pst = DB_manager.getConection().prepareStatement(query);
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
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void anularProduccionFilm(int idProduccion) {
        String UPDATE_PRODUCCION = "UPDATE PRODUCCION_FILM SET "
                + "ID_ESTADO = 2 "
                + "WHERE ID_PRODUCCION_CABECERA = ?; ";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_PRODUCCION);
            pst.setInt(1, idProduccion);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static List<E_produccionDetalle> consultarProduccionDetalleAgrupado(List<E_produccionCabecera> cadenaCabeceras) {
        List<E_produccionDetalle> list = new ArrayList<>();
        boolean b = true;
        StringBuilder builder = new StringBuilder();
        for (E_produccionCabecera seleccionVenta : cadenaCabeceras) {
            builder.append("?,");
            b = false;
        }
        //para controlar que la lista contenga por lo menos una venta seleccionada
        if (b) {
            return list;
        }
        String QUERY = "SELECT PROD.CODIGO \"Codigo\", "
                + "PROD.DESCRIPCION \"Producto\", "
                + "SUM(PRDE.CANTIDAD) \"Cantidad\" "
                + "FROM PRODUCCION_DETALLE PRDE, PRODUCCION_CABECERA PRCA, PRODUCTO PROD "
                + "WHERE PRDE.ID_PRODUCCION_CABECERA = PRCA.ID_PRODUCCION_CABECERA "
                + "AND PRDE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND PRCA.ID_PRODUCCION_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY PROD.DESCRIPCION, PROD.CODIGO "
                + "ORDER BY PROD.DESCRIPCION";
        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (E_produccionCabecera seleccionVenta : cadenaCabeceras) {
                pst.setInt(index, seleccionVenta.getId());
                index++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_produccionDetalle prde = new E_produccionDetalle();
                M_producto producto = new M_producto();
                producto.setCodBarra(rs.getString("Codigo"));
                producto.setDescripcion(rs.getString("Producto"));
                prde.setProducto(producto);
                prde.setCantidad(rs.getDouble("Cantidad"));
                list.add(prde);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static int insertarProduccionFilm(E_produccionCabecera produccionCabecera, List<E_produccionFilm> detalleRollo,
            List<E_produccionDetalle> detalleMP) {
        String INSERT_CABECERA = "INSERT INTO produccion_cabecera(nro_orden_trabajo, fecha_produccion, id_funcionario_responsable, id_funcionario_usuario, id_produccion_tipo)VALUES( ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        String INSERT_FILM = "INSERT INTO produccion_film(nro_film, id_produccion_cabecera, id_produccion_detalle, peso, fecha_creacion, id_funcionario_responsable, cono, medida, micron, id_producto_categoria, id_estado)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_PROD_FILM_PROD = "INSERT INTO produccion_film_producto(id_produccion_film, id_producto)VALUES (?, ?);";
        String INSERT_FILM_MP_BAJA = "INSERT INTO PRODUCCION_FILM_MP_BAJA(ID_PRODUCCION_CABECERA, ID_PRODUCTO, CANTIDAD)VALUES (?, ?, ?);";
        long sq_cabecera = -1L;
        ArrayList<Integer> prodFilmKeys = new ArrayList<>();
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, produccionCabecera.getNroOrdenTrabajo());
            pst.setTimestamp(2, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
            pst.setInt(3, produccionCabecera.getFuncionarioProduccion().getId_funcionario());
            pst.setInt(4, produccionCabecera.getFuncionarioSistema().getId_funcionario());
            pst.setInt(5, E_produccionTipo.ROLLO);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < detalleRollo.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, detalleRollo.get(i).getProducto().getId());
                pst.setDouble(3, detalleRollo.get(i).getPeso());
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    detalleRollo.get(i).setId((int) rs.getLong(1));
                }
                pst.close();
            }
            for (int i = 0; i < detalleRollo.size(); i++) {
                E_produccionFilm prodFilm = detalleRollo.get(i);
                pst = DB_manager.getConection().prepareStatement(INSERT_FILM, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, prodFilm.getNroFilm());
                pst.setInt(2, (int) sq_cabecera);
                pst.setInt(3, prodFilm.getId());
                pst.setDouble(4, prodFilm.getPeso());
                pst.setTimestamp(5, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
                pst.setInt(6, produccionCabecera.getFuncionarioProduccion().getId_funcionario());
                pst.setInt(7, prodFilm.getCono());
                pst.setInt(8, prodFilm.getMedida());
                pst.setInt(9, prodFilm.getMicron());
                pst.setInt(10, prodFilm.getProductoClasificacion().getId());
                pst.setInt(11, prodFilm.getEstado().getId());
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    prodFilmKeys.add((int) rs.getLong(1));
                }
                pst.close();
            }
            int prodFilmIndex = 0;
            for (E_produccionFilm e_produccionFilm : detalleRollo) {
                pst = DB_manager.getConection().prepareStatement(INSERT_PROD_FILM_PROD);
                pst.setInt(1, prodFilmKeys.get(prodFilmIndex));
                prodFilmIndex++;
                pst.setInt(2, e_produccionFilm.getProducto().getId());
                pst.executeUpdate();
                pst.close();
            }
            //Insertar utilizacion de materia prima
            for (E_produccionDetalle mpDetail : detalleMP) {
                pst = DB_manager.getConection().prepareStatement(INSERT_FILM_MP_BAJA);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, mpDetail.getProducto().getId());
                pst.setDouble(3, mpDetail.getCantidad());
                pst.executeUpdate();
                pst.close();
            }
            //se suma al stock lo que se produce
            for (int i = 0; i < detalleRollo.size(); i++) {
                int idProducto = detalleRollo.get(i).getProducto().getId();
                double cantidad = detalleRollo.get(i).getPeso();
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")+" + cantidad + ") "
                        + "WHERE ID_PRODUCTO =" + idProducto;
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }

            //se resta al stock lo que se utiliza
            for (E_produccionDetalle mpDetail : detalleMP) {
                int idProducto = mpDetail.getProducto().getId();
                double cantidad = mpDetail.getCantidad();
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                        + "WHERE ID_PRODUCTO =" + idProducto;
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
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static int insertarProduccionProdTerminados(E_produccionCabecera produccionCabecera, List<E_produccionDetalle> prodTerminadosList, List<E_produccionFilm> rollosList) {
        String INSERT_CABECERA = "INSERT INTO produccion_cabecera(nro_orden_trabajo, fecha_produccion, id_funcionario_responsable, id_funcionario_usuario, id_produccion_tipo)VALUES( ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        String INSERT_PRODUCCION_FILM_BAJA = "INSERT INTO produccion_film_baja(id_produccion_film, id_produccion_cabecera, peso_utilizado, fecha_utilizado, id_produccion_tipo_baja)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, produccionCabecera.getNroOrdenTrabajo());
            pst.setTimestamp(2, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
            pst.setInt(3, produccionCabecera.getFuncionarioProduccion().getId_funcionario());
            pst.setInt(4, produccionCabecera.getFuncionarioSistema().getId_funcionario());
            pst.setInt(5, E_produccionTipo.PRODUCTO_TERMINADO);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < prodTerminadosList.size(); i++) {
                E_produccionDetalle detalle = prodTerminadosList.get(i);
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, detalle.getProducto().getId());
                pst.setDouble(3, detalle.getCantidad());
                pst.executeUpdate();
                /*rs = pst.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    rollosList.get(i).setId((int) rs.getLong(1));
                }*/
                pst.close();
            }
            for (int i = 0; i < rollosList.size(); i++) {
                //id_produccion_film, id_produccion_cabecera, peso_utilizado, fecha_utilizado, id_produccion_tipo_baja
                E_produccionFilm prodFilm = rollosList.get(i);
                pst = DB_manager.getConection().prepareStatement(INSERT_PRODUCCION_FILM_BAJA);
                pst.setInt(1, prodFilm.getId());
                pst.setInt(2, (int) sq_cabecera);
                pst.setDouble(3, prodFilm.getPeso());
                pst.setTimestamp(4, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
                pst.setInt(5, E_produccionTipoBaja.PRODUCCION);
                pst.executeUpdate();
                pst.close();
            }
            //ACTUALIZAR PESO DE ROLLOS UTILIZADOS
            for (int i = 0; i < rollosList.size(); i++) {
                E_produccionFilm prodFilm = rollosList.get(i);
                double pesoUtilizado = prodFilm.getPeso();
                double pesoActual = prodFilm.getPesoActual();
                if ((pesoActual - pesoUtilizado) <= 0) {
                    int idProduccionFilm = rollosList.get(i).getId();
                    String query = "UPDATE produccion_film SET ID_ESTADO = 2 WHERE id_produccion_film =" + idProduccionFilm;
                    st = DB_manager.getConection().createStatement();
                    st.executeUpdate(query);
                }
            }
            //se suma al stock lo que se produce
            for (int i = 0; i < prodTerminadosList.size(); i++) {
                int idProducto = prodTerminadosList.get(i).getProducto().getId();
                double cantidad = prodTerminadosList.get(i).getCantidad();
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")+" + cantidad + ") "
                        + "WHERE ID_PRODUCTO =" + idProducto;
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            //se resta al stock lo que se gasta (rollos) para productos terminados
            for (int i = 0; i < rollosList.size(); i++) {
                int idProducto = rollosList.get(i).getProducto().getId();
                double cantidad = rollosList.get(i).getPeso();
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                        + "WHERE ID_PRODUCTO =" + idProducto;
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
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static void insertarProdTerminadoPosterior(E_produccionCabecera produccionCabecera, E_produccionFilm detalle) {
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        String INSERT_FILM = "INSERT INTO produccion_film(nro_film, id_produccion_cabecera, id_produccion_detalle, peso, fecha_creacion, id_funcionario_responsable, cono, medida, micron, id_producto_categoria, id_estado)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_PROD_FILM_PROD = "INSERT INTO produccion_film_producto(id_produccion_film, id_producto)VALUES (?, ?);";
        long sqDetalle = -1L, sqFilm = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, produccionCabecera.getId());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setDouble(3, detalle.getPeso());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sqDetalle = rs.getLong(1);
            }
            pst.close();
            rs.close();
            //(nro_film, id_produccion_cabecera, id_produccion_detalle, peso, fecha_creacion, id_funcionario_responsable, cono, medida, micron, id_producto_categoria, id_estado)
            //id_funcionario_responsable, cono, medida, micron, id_producto_categoria, id_estado
            pst = DB_manager.getConection().prepareStatement(INSERT_FILM, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, detalle.getNroFilm());
            pst.setInt(2, produccionCabecera.getId());
            pst.setInt(3, (int) sqDetalle);
            pst.setDouble(4, detalle.getPeso());
            pst.setTimestamp(5, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
            pst.setInt(6, produccionCabecera.getFuncionarioProduccion().getId_funcionario());
            pst.setInt(7, detalle.getCono());
            pst.setInt(8, detalle.getMedida());
            pst.setInt(9, detalle.getProducto().getProductoCategoria().getId());
            pst.setInt(10, Estado.ACTIVO);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sqFilm = rs.getLong(1);
            }
            pst.close();
            rs.close();
            pst = DB_manager.getConection().prepareStatement(INSERT_PROD_FILM_PROD);
            pst.setInt(1, (int) sqFilm);
            pst.setInt(2, detalle.getProducto().getId());
            pst.executeUpdate();
            pst.close();
            rs.close();
            //se suma al stock lo que se produce
            int idProducto = detalle.getProducto().getId();
            double cantidad = detalle.getPeso();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")+" + cantidad + ") "
                    + "WHERE ID_PRODUCTO =" + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarProductoTerminadoPosterior(E_produccionDetalle produccion, double newCant) {
        String UPDATE_DETALLE = "UPDATE produccion_detalle SET cantidad = ? WHERE id_produccion_detalle = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);
            pst.setDouble(1, newCant);
            pst.setInt(2, produccion.getId());
            pst.executeUpdate();
            pst.close();
            //se actualiza el stock
            int idProducto = produccion.getProducto().getId();
            String UPDATE_STOCK = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ") ";
            double cantActual = produccion.getCantidad();
            double cantNueva = newCant;
            double diferencia = Math.abs(cantActual - cantNueva);
            if (cantNueva > cantActual) {
                UPDATE_STOCK = UPDATE_STOCK + "-" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            } else {
                UPDATE_STOCK = UPDATE_STOCK + "+" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            }
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_STOCK);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarRolloPosterior(E_produccionFilm currentFilm, E_produccionFilm newFilm) {
        String UPDATE_DETALLE = "UPDATE produccion_detalle SET cantidad = ? WHERE id_produccion_detalle = ?;";
        String UPDATE_DETALLE_FILM = "UPDATE produccion_film SET nro_film = ?, peso = ?, cono = ?, medida= ?, micron = ?, id_producto_categoria = ?  WHERE id_produccion_detalle = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);
            pst.setDouble(1, newFilm.getPeso());
            pst.setInt(2, currentFilm.getOrdenTrabajoDetalle());
            pst.executeUpdate();
            pst.close();

            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE_FILM);
            pst.setInt(1, newFilm.getNroFilm());
            pst.setDouble(2, newFilm.getPeso());
            pst.setInt(3, newFilm.getCono());
            pst.setInt(4, newFilm.getMedida());
            pst.setInt(5, newFilm.getMicron());
            pst.setInt(6, newFilm.getProductoClasificacion().getId());
            pst.setInt(7, currentFilm.getOrdenTrabajoDetalle());
            pst.executeUpdate();
            pst.close();
            //se actualiza el stock
            int idProducto = currentFilm.getProducto().getId();
            String UPDATE_STOCK = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ") ";
            double cantActual = currentFilm.getPeso();
            double cantNueva = newFilm.getPeso();
            double diferencia = Math.abs(cantActual - cantNueva);
            if (cantNueva > cantActual) {
                UPDATE_STOCK = UPDATE_STOCK + "-" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            } else {
                UPDATE_STOCK = UPDATE_STOCK + "+" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            }
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_STOCK);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarRolloUtilizadoPosterior(E_produccionFilm currentFilm, E_produccionFilm newFilm) {
        String UPDATE_DETALLE_FILM = "UPDATE produccion_film_baja SET peso_utilizado = ? WHERE id_produccion_film_baja = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE_FILM);
            pst.setDouble(1, newFilm.getPeso());
            pst.setInt(2, currentFilm.getId());
            pst.executeUpdate();
            pst.close();
            //se actualiza el stock
            int idProducto = currentFilm.getProducto().getId();
            String UPDATE_STOCK = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ") ";
            double cantActual = currentFilm.getPeso();
            double cantNueva = newFilm.getPeso();
            double diferencia = Math.abs(cantActual - cantNueva);
            if (cantNueva > cantActual) {
                UPDATE_STOCK = UPDATE_STOCK + "-" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            } else {
                UPDATE_STOCK = UPDATE_STOCK + "+" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            }
            //ACTUALIZAR PESO DE ROLLOS UTILIZADOS
            double pesoUtilizado = newFilm.getPeso();
            double pesoActual = currentFilm.getPesoActual();
            int idProduccionFilm = currentFilm.getOrdenTrabajoDetalle();
            if ((pesoActual - pesoUtilizado) <= 0) {
                String query = "UPDATE produccion_film SET ID_ESTADO = 2 WHERE id_produccion_film =" + idProduccionFilm;
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            } else {
                String query = "UPDATE produccion_film SET ID_ESTADO = 1 WHERE id_produccion_film =" + idProduccionFilm;
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_STOCK);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void eliminarRolloUtilizadoPosterior(E_produccionFilm rolloUtilizado) {
        String DELETE_PROD_FILM_PROD = "DELETE FROM produccion_film_baja WHERE id_produccion_film_baja = ?;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(DELETE_PROD_FILM_PROD);
            pst.setInt(1, rolloUtilizado.getId());
            pst.executeUpdate();
            pst.close();
            String UPDATE_FILM_STATUS = "UPDATE produccion_film SET ID_ESTADO = 1 WHERE id_produccion_film =" + rolloUtilizado.getOrdenTrabajoDetalle();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_FILM_STATUS);
            //se resta al stock lo que se elimina
            int idProducto = rolloUtilizado.getProducto().getId();
            double cantidad = rolloUtilizado.getPeso();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                    + "WHERE ID_PRODUCTO =" + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarProductoTerminadoPosterior(E_produccionDetalle currentProd) {
        String DELETE_DETAIL = "DELETE FROM produccion_detalle WHERE id_produccion_detalle = ?;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(DELETE_DETAIL);
            pst.setInt(1, currentProd.getId());
            pst.executeUpdate();
            pst.close();
            //se resta al stock lo que se elimina
            int idProducto = currentProd.getProducto().getId();
            double cantidad = currentProd.getCantidad();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                    + "WHERE ID_PRODUCTO =" + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarRolloPosterior(E_produccionFilm currentPF) {
        String DELETE_PROD_FILM_PROD = "DELETE FROM produccion_film_producto WHERE id_produccion_film = ?;";
        String DELETE_DETAIL_FILM = "DELETE FROM produccion_film WHERE id_produccion_film = ?;";
        String DELETE_DETAIL = "DELETE FROM produccion_detalle WHERE id_produccion_detalle = ?;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(DELETE_PROD_FILM_PROD);
            pst.setInt(1, currentPF.getId());
            pst.executeUpdate();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(DELETE_DETAIL_FILM);
            pst.setInt(1, currentPF.getId());
            pst.executeUpdate();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(DELETE_DETAIL);
            pst.setInt(1, currentPF.getOrdenTrabajoDetalle());
            pst.executeUpdate();
            pst.close();
            //se resta al stock lo que se elimina
            int idProducto = currentPF.getProducto().getId();
            double cantidad = currentPF.getPeso();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                    + "WHERE ID_PRODUCTO =" + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int insertarProduccionRolloPosterior(E_produccionCabecera produccionCabecera, E_produccionFilm prodFilm) {
        String INSERT_PRODUCCION_FILM_BAJA = "INSERT INTO produccion_film_baja(id_produccion_film, id_produccion_cabecera, peso_utilizado, fecha_utilizado, id_produccion_tipo_baja)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = produccionCabecera.getId();
        try {
            DB_manager.getConection().setAutoCommit(false);
            //id_produccion_film, id_produccion_cabecera, peso_utilizado, fecha_utilizado, id_produccion_tipo_baja
            pst = DB_manager.getConection().prepareStatement(INSERT_PRODUCCION_FILM_BAJA);
            pst.setInt(1, prodFilm.getId());
            pst.setInt(2, (int) sq_cabecera);
            pst.setDouble(3, prodFilm.getPeso());
            pst.setTimestamp(4, new Timestamp(produccionCabecera.getFechaProduccion().getTime()));
            pst.setInt(5, E_produccionTipoBaja.PRODUCCION);
            pst.executeUpdate();
            pst.close();
            //ACTUALIZAR PESO DE ROLLOS UTILIZADOS
            double pesoUtilizado = prodFilm.getPeso();
            double pesoActual = prodFilm.getPesoActual();
            if ((pesoActual - pesoUtilizado) <= 0) {
                int idProduccionFilm = prodFilm.getId();
                String query = "UPDATE produccion_film SET ID_ESTADO = 2 WHERE id_produccion_film =" + idProduccionFilm;
                st = DB_manager.getConection().createStatement();
                st.executeUpdate(query);
            }
            //se resta al stock lo que se gasta (rollos) para productos terminados
            int idProducto = prodFilm.getProducto().getId();
            double cantidad = prodFilm.getPeso();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                    + "WHERE ID_PRODUCTO =" + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static int insertarProduccionProdTerminadoPosterior(E_produccionCabecera produccionCabecera, E_produccionDetalle detalle) {
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        long sq_cabecera = produccionCabecera.getId();
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, (int) sq_cabecera);
            pst.setInt(2, detalle.getProducto().getId());
            pst.setDouble(3, detalle.getCantidad());
            pst.executeUpdate();
            pst.close();
            //se suma al stock lo que se produce para productos terminados
            int idProducto = detalle.getProducto().getId();
            double cantidad = detalle.getCantidad();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")+" + cantidad + ") "
                    + "WHERE ID_PRODUCTO = " + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static void insertarUsoMateriaPrimaPosterior(E_produccionCabecera produccionCabecera, E_produccionDetalle detalle) {
        String INSERT_DETALLE = "INSERT INTO produccion_film_mp_baja(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, produccionCabecera.getId());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setDouble(3, detalle.getCantidad());
            pst.executeUpdate();
            pst.close();
            //se suma al stock lo que se produce para productos terminados
            int idProducto = detalle.getProducto().getId();
            double cantidad = detalle.getCantidad();
            String query = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")-" + cantidad + ") "
                    + "WHERE ID_PRODUCTO = " + idProducto;
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(query);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarUsoMateriaPrimaPosterior(E_produccionDetalle usoMP, double cantidad) {
        String UPDATE_DETALLE = "UPDATE produccion_film_mp_baja SET cantidad = ? WHERE id_produccion_film_mp_baja = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setDouble(1, cantidad);
            pst.setInt(2, usoMP.getId());
            pst.executeUpdate();
            pst.close();
            //se actualiza el stock
            int idProducto = usoMP.getProducto().getId();
            String UPDATE_STOCK = "UPDATE PRODUCTO SET "
                    + "CANT_ACTUAL = "
                    + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ") ";
            double cantActual = usoMP.getCantidad();
            double cantNueva = cantidad;
            double diferencia = Math.abs(cantActual - cantNueva);
            if (cantNueva > cantActual) {
                UPDATE_STOCK = UPDATE_STOCK + "-" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            } else {
                UPDATE_STOCK = UPDATE_STOCK + "+" + diferencia + ") WHERE ID_PRODUCTO = " + idProducto;
            }
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_STOCK);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void eliminarUsoMateriaPrimaDetalle(int idUsoMPDetalle) {
        String DELETE_DETAIL = "DELETE FROM produccion_film_mp_baja WHERE id_produccion_film_mp_baja = " + idUsoMPDetalle;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_DETAIL);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Produccion.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Produccion.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static ArrayList<E_produccionFilm> consultarFilmDisponible(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String estado,
            boolean porFecha, Date fechaInicio, Date fechaFinal) {
        ArrayList<E_produccionFilm> filmList = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }

            String fromQuery = "FROM v_produccion_detalle V ";
            String finalQuery = "ORDER BY V.producto ";
            String buscarSQL = "";
            String estadoSQL = "";
            String buscarPorFechaSQL = "";
            //BUSCAR 
            switch (buscarPor) {
                case "Todos": {
                    buscarSQL = "((LOWER(CAST(v.nro_orden_trabajo AS CHARACTER VARYING)) LIKE ?) OR (LOWER(CAST(v.nro_film AS CHARACTER VARYING)) LIKE ?) "
                            + "OR (LOWER(v.producto) LIKE ?) OR (LOWER(v.codigo) LIKE ?) )";
                    break;
                }
                case "OT": {
                    buscarSQL = "LOWER(CAST(v.nro_orden_trabajo AS CHARACTER VARYING)) LIKE ? ";
                    break;
                }
                case "Producto": {
                    buscarSQL = "LOWER(v.producto) LIKE ? ";
                    break;
                }
                case "Nro. Film": {
                    buscarSQL = "LOWER(CAST(v.nro_film AS CHARACTER VARYING)) LIKE ? ";
                    break;
                }
                case "Código": {
                    buscarSQL = "LOWER(v.codigo) LIKE ? ";
                    break;
                }
            }
            if (porFecha) {
                buscarPorFechaSQL = "AND fecha between ? AND ? ";
            }
            //CLASIFICAR 
            switch (clasificarPor) {
                case "OT": {
                    finalQuery = "ORDER BY V.nro_orden_trabajo ";
                    break;
                }
                case "Fecha": {
                    finalQuery = "ORDER BY V.fecha ";
                    break;
                }
                case "Producto": {
                    finalQuery = "ORDER BY V.producto ";
                    break;
                }
                case "Nro. Film": {
                    finalQuery = "ORDER BY V.nro_film ";
                    break;
                }
                case "Código": {
                    finalQuery = "ORDER BY v.codigo ";
                    break;
                }
            }
            //ORDENAR
            switch (ordenarPor) {
                case "Ascendente": {
                    finalQuery = finalQuery + "ASC ";
                    break;
                }
                case "Descendente": {
                    finalQuery = finalQuery + "DESC ";
                    break;
                }
            }
            //ESTADO
            switch (estado) {
                case "Disponible": {
                    estadoSQL = "AND peso_disponible > 0 ";
                    break;
                }
                case "Agotado": {
                    estadoSQL = "AND peso_disponible <= 0 ";
                    break;
                }
                case "Todos": {
                    estadoSQL = " ";
                    break;
                }
            }

            String Query = "SELECT id_cabecera, nro_orden_trabajo, nro_film, fecha, id_producto, codigo,"
                    + "producto, cono, medida, micron, peso_producido, peso_utilizado, peso_disponible,"
                    + "id_categoria, categoria "
                    + fromQuery
                    + "WHERE "
                    + buscarSQL
                    + estadoSQL
                    + buscarPorFechaSQL
                    + finalQuery;
            int pos = 1;
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (buscarPor.equals("Todos")) {
                pst.setString(pos++, "%" + descripcion + "%");
                pst.setString(pos++, "%" + descripcion + "%");
                pst.setString(pos++, "%" + descripcion + "%");
                pst.setString(pos++, "%" + descripcion + "%");
            } else {
                pst.setString(pos++, "%" + descripcion + "%");
            }
            if (porFecha) {
                pst.setTimestamp(pos++, new Timestamp(fechaInicio.getTime()));
                pst.setTimestamp(pos++, new Timestamp(fechaFinal.getTime()));
            }
            rs = pst.executeQuery();
            filmList = new ArrayList();
            while (rs.next()) {
                E_produccionFilm film = new E_produccionFilm();
                film.setId(rs.getInt("id_cabecera"));
                film.setOrdenTrabajoCabecera(rs.getInt("nro_orden_trabajo"));
                film.setNroFilm(rs.getInt("nro_film"));
                film.setFechaCreacion(rs.getDate("fecha"));
                M_producto prod = new M_producto();
                prod.setId(rs.getInt("id_producto"));
                prod.setCodigo(rs.getString("codigo"));
                prod.setDescripcion(rs.getString("producto"));
                film.setProducto(prod);
                film.setCono(rs.getInt("cono"));
                film.setMedida(rs.getInt("medida"));
                film.setMicron(rs.getInt("micron"));
                film.setPeso(rs.getDouble("peso_producido"));
                film.setPesoUtilizado(rs.getDouble("peso_utilizado"));
                film.setPesoActual(rs.getDouble("peso_disponible"));
                E_productoClasificacion productoClasificacion = new E_productoClasificacion();
                productoClasificacion.setId(rs.getInt("id_categoria"));
                productoClasificacion.setDescripcion(rs.getString("categoria"));
                film.setProductoClasificacion(productoClasificacion);
                filmList.add(film);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return filmList;
    }

    public static E_produccionFilm obtenerFilm(int idFilm) {
        E_produccionFilm film = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String Query = "SELECT id_cabecera, nro_orden_trabajo, nro_film, fecha, id_producto, codigo,"
                    + "producto, cono, medida, micron, peso_producido, peso_utilizado, peso_disponible,"
                    + "id_categoria, categoria "
                    + "FROM v_produccion_detalle V WHERE id_cabecera = ?;";
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idFilm);
            rs = pst.executeQuery();
            while (rs.next()) {
                film = new E_produccionFilm();
                film.setId(rs.getInt("id_cabecera"));
                film.setOrdenTrabajoCabecera(rs.getInt("nro_orden_trabajo"));
                film.setNroFilm(rs.getInt("nro_film"));
                film.setFechaCreacion(rs.getDate("fecha"));
                M_producto prod = new M_producto();
                prod.setId(rs.getInt("id_producto"));
                prod.setCodigo(rs.getString("codigo"));
                prod.setDescripcion(rs.getString("producto"));
                film.setProducto(prod);
                film.setCono(rs.getInt("cono"));
                film.setMedida(rs.getInt("medida"));
                film.setMicron(rs.getInt("micron"));
                film.setPeso(rs.getDouble("peso_producido"));
                film.setPesoUtilizado(rs.getDouble("peso_utilizado"));
                film.setPesoActual(rs.getDouble("peso_disponible"));
                E_productoClasificacion productoClasificacion = new E_productoClasificacion();
                productoClasificacion.setId(rs.getInt("id_categoria"));
                productoClasificacion.setDescripcion(rs.getString("categoria"));
                film.setProductoClasificacion(productoClasificacion);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return film;
    }

    public static ArrayList<E_produccionFilm> consultarFilmDisponibleAgrupado(RolloProducidoTableModel cabecera, String descripcion) {
        ArrayList<E_produccionFilm> filmList = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            StringBuilder builder = new StringBuilder();
            for (E_produccionFilm seleccionVenta : cabecera.getList()) {
                builder.append("?,");
            }
            String Query = "SELECT codigo, producto, id_categoria, categoria, cono, medida, micron, "
                    + "sum(peso)as peso , "
                    + "sum(peso_utilizado)as peso_utilizado , "
                    + "sum(peso_actual)as peso_actual "
                    + "FROM v_film_actual "
                    + "WHERE "
                    + "v_film_actual.id_cabecera IN ("
                    + builder.substring(0, builder.length() - 1) + ") "
                    + "GROUP BY codigo, producto, id_categoria, categoria, cono, medida, micron "
                    + "ORDER BY producto ;";

            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (E_produccionFilm ventaCabecera : cabecera.getList()) {
                pst.setInt(index++, ventaCabecera.getId());
            }
            rs = pst.executeQuery();
            filmList = new ArrayList();
            while (rs.next()) {
                E_produccionFilm film = new E_produccionFilm();
                film.setId(0);
                film.setOrdenTrabajoCabecera(0);
                film.setNroFilm(0);
                film.setFechaCreacion(Calendar.getInstance().getTime());
                M_producto prod = new M_producto();
                prod.setCodigo(rs.getString("codigo"));
                prod.setDescripcion(rs.getString("producto"));
                film.setProducto(prod);
                film.setCono(rs.getInt("cono"));
                film.setMedida(rs.getInt("medida"));
                film.setMicron(rs.getInt("micron"));
                film.setPeso(rs.getDouble("peso"));
                film.setPesoUtilizado(rs.getDouble("peso_utilizado"));
                film.setPesoActual(rs.getDouble("peso_actual"));
                E_productoClasificacion productoClasificacion = new E_productoClasificacion();
                productoClasificacion.setId(rs.getInt("id_categoria"));
                productoClasificacion.setDescripcion(rs.getString("categoria"));
                film.setProductoClasificacion(productoClasificacion);
                filmList.add(film);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return filmList;
    }

    public static List<E_produccionFilm> consultarProduccionFilmBaja(int idProduccion) {
        List<E_produccionFilm> list = new ArrayList<>();
        String QUERY = "SELECT id_cabecera, id_produccion_cabecera, nro_orden_trabajo, nro_film, fecha, "
                + "id_producto, producto, id_categoria, categoria, peso, cono, medida, micron, producto_codigo, id_produccion_film "
                + "FROM v_produccion_film_baja WHERE id_produccion_cabecera = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setCodigo(rs.getString("producto_codigo"));
                producto.setDescripcion(rs.getString("producto"));
                E_productoClasificacion pc = new E_productoClasificacion();
                pc.setId(rs.getInt("id_categoria"));
                pc.setDescripcion(rs.getString("categoria"));
                E_produccionFilm pd = new E_produccionFilm();
                pd.setId(rs.getInt("id_cabecera"));
                pd.setOrdenTrabajoDetalle(rs.getInt("id_produccion_film"));
                pd.setOrdenTrabajoCabecera(rs.getInt("nro_orden_trabajo"));
                pd.setNroFilm(rs.getInt("nro_film"));
                pd.setFechaCreacion(rs.getDate("fecha"));
                pd.setProducto(producto);
                pd.setPeso(rs.getDouble("peso"));
                pd.setCono(rs.getInt("cono"));
                pd.setMedida(rs.getInt("medida"));
                pd.setMicron(rs.getInt("micron"));
                pd.setProducto(producto);
                pd.setProductoClasificacion(pc);
                list.add(pd);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_produccionFilm> consultarProduccionFilm(Integer idProduccion) {
        List<E_produccionFilm> list = new ArrayList<>();
        String QUERY = "SELECT id_produccion_film, nro_film, id_produccion_cabecera, "
                + "id_produccion_detalle, producto, peso, fecha_creacion, cono, medida, "
                + "micron, id_producto_categoria, categoria "
                + "FROM v_produccion_film WHERE id_produccion_cabecera = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setDescripcion(rs.getString("producto"));
                E_productoClasificacion pc = new E_productoClasificacion();
                pc.setId(rs.getInt("id_producto_categoria"));
                pc.setDescripcion(rs.getString("categoria"));
                E_produccionFilm pd = new E_produccionFilm();
                pd.setId(rs.getInt("id_produccion_film"));
                pd.setOrdenTrabajoCabecera(rs.getInt("id_produccion_cabecera"));
                pd.setOrdenTrabajoDetalle(rs.getInt("id_produccion_detalle"));
                pd.setNroFilm(rs.getInt("nro_film"));
                pd.setFechaCreacion(rs.getDate("fecha_creacion"));
                pd.setProducto(producto);
                pd.setPeso(rs.getDouble("peso"));
                pd.setCono(rs.getInt("cono"));
                pd.setMedida(rs.getInt("medida"));
                pd.setMicron(rs.getInt("micron"));
                pd.setProducto(producto);
                pd.setProductoClasificacion(pc);
                list.add(pd);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
