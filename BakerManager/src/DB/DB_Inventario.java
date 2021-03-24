/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_ajusteStockMotivo;
import Entities.E_ajusteStockCabecera;
import Entities.E_ajusteStockDetalle;
import Entities.Estado;
import Entities.M_funcionario;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Inventario {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    /*
    INSERTS
     */
    public static long insertarAjusteStockCabecera(E_ajusteStockCabecera cabecera) {
        String INSERT_CABECERA = "INSERT INTO ajuste_stock_cabecera(id_funcionario_responsable, id_funcionario_registro, tiempo_inicio, id_estado, tiempo_fin)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getResponsable().getIdFuncionario());
            pst.setInt(2, cabecera.getRegistradoPor().getIdFuncionario());
            pst.setTimestamp(3, new Timestamp(cabecera.getTiempoInicio().getTime()));
            pst.setInt(4, cabecera.getEstado().getId());
            pst.setTimestamp(5, new Timestamp(cabecera.getTiempoFin().getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
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
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }

    public static long insertarAjusteStockDetalle(E_ajusteStockDetalle detalle) {
        String INSERT_DETALLE = "INSERT INTO ajuste_stock_detalle(id_ajuste_stock_cabecera, id_producto, id_motivo, cantidad_vieja, cantidad_nueva, tiempo, observacion)VALUES (?, ?, ?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, detalle.getIdCabecera());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setInt(3, detalle.getMotivo().getId());
            pst.setDouble(4, detalle.getCantidadVieja());
            pst.setDouble(5, detalle.getCantidadNueva());
            pst.setTimestamp(6, new Timestamp(detalle.getTiempoRegistro().getTime()));
            if (detalle.getObservacion() == null || detalle.getObservacion().trim().isEmpty()) {
                pst.setNull(7, Types.VARCHAR);
            } else {
                pst.setString(7, detalle.getObservacion());
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
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
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }

    public static long insertarAjusteStockDetalleTemporal(E_ajusteStockDetalle detalle) {
        String INSERT_DETALLE = "INSERT INTO ajuste_stock_detalle_temporal(id_ajuste_stock_cabecera_temporal, id_producto, id_motivo, cantidad_vieja, cantidad_nueva, tiempo, observacion)VALUES (?, ?, ?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, detalle.getIdCabecera());
            pst.setInt(2, detalle.getProducto().getId());
            pst.setInt(3, detalle.getMotivo().getId());
            pst.setDouble(4, detalle.getCantidadVieja());
            pst.setDouble(5, detalle.getCantidadNueva());
            pst.setTimestamp(6, new Timestamp(detalle.getTiempoRegistro().getTime()));
            if (detalle.getObservacion() == null || detalle.getObservacion().trim().isEmpty()) {
                pst.setNull(7, Types.VARCHAR);
            } else {
                pst.setString(7, detalle.getObservacion());
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
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
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }
    
    public static long insertarAjusteStockMotivo(E_ajusteStockMotivo unMotivo) {
        String INSERT_DETALLE = "INSERT INTO ajuste_stock_motivo(descripcion, observacion)VALUES (?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, unMotivo.getDescripcion());
            if (unMotivo.getObservacion() == null || unMotivo.getObservacion().trim().isEmpty()) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, unMotivo.getObservacion());
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
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
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }

    public static long insertarAjusteStockCabeceraTemporal(E_ajusteStockCabecera cabecera) {
        String INSERT_CABECERA = "INSERT INTO ajuste_stock_cabecera_temporal(id_funcionario_responsable, id_funcionario_registro, tiempo_inicio, id_estado, tiempo_fin)VALUES (?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getResponsable().getIdFuncionario());
            pst.setInt(2, cabecera.getRegistradoPor().getIdFuncionario());
            pst.setTimestamp(3, new Timestamp(cabecera.getTiempoInicio().getTime()));
            pst.setInt(4, cabecera.getEstado().getId());
            pst.setTimestamp(5, new Timestamp(cabecera.getTiempoFin().getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
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
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }

    /*
    READS
     */
    public static E_ajusteStockCabecera obtenerAjusteStockCabecera(int idCabecera, boolean esTemporal) {
        E_ajusteStockCabecera cabecera = null;
        String QUERY_SELECT ="SELECT id_ajuste_stock_cabecera, ";
        String QUERY_FROM ="FROM ajuste_stock_cabecera a, estado e WHERE a.id_estado = e.id_estado "
                + "AND id_ajuste_stock_cabecera = ? ;";
        if(esTemporal){
            QUERY_SELECT ="SELECT id_ajuste_stock_cabecera_temporal, ";
            QUERY_FROM ="FROM ajuste_stock_cabecera_temporal a, estado e WHERE a.id_estado = e.id_estado "
                + "AND id_ajuste_stock_cabecera_temporal = ? ;";
        }
        String QUERY = QUERY_SELECT
                + "id_funcionario_responsable, "//2
                + "id_funcionario_registro, "//3
                + "tiempo_inicio, "//4
                + "a.id_estado, "//5
                + "e.DESCRIPCION, "//6
                + "(SELECT P.NOMBRE FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_responsable )\"RESPONSABLE_NOMBRE\", "//7
                + "(SELECT P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_responsable )\"RESPONSABLE_APELLIDO\", "//8
                + "(SELECT P.NOMBRE  FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_registro)\"USUARIO_NOMBRE\", "//9
                + "(SELECT P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_registro)\"USUARIO_APELLIDO\", "//10
                + "a.observacion, "//11
                + "tiempo_inicio "//12
                + QUERY_FROM;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {

                Estado estado = new Estado();
                estado.setId(rs.getInt(5));
                estado.setDescripcion(rs.getString(6));

                M_funcionario funcResponsable = new M_funcionario();
                funcResponsable.setIdFuncionario(rs.getInt(2));
                funcResponsable.setNombre(rs.getString(7));
                funcResponsable.setApellido(rs.getString(8));
                M_funcionario funcRegistro = new M_funcionario();
                funcRegistro.setIdFuncionario(rs.getInt(3));
                funcRegistro.setNombre(rs.getString(9));
                funcRegistro.setApellido(rs.getString(10));

                cabecera = new E_ajusteStockCabecera();
                cabecera.setId(rs.getInt(1));
                cabecera.setTiempoInicio(rs.getTimestamp(4));
                cabecera.setTiempoFin(rs.getTimestamp(12));
                cabecera.setResponsable(funcResponsable);
                cabecera.setRegistradoPor(funcRegistro);
                cabecera.setEstado(estado);
                cabecera.setObservacion(rs.getString(11));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return cabecera;
    }

    /**
     * Retorna una lista con las cabeceras de ajuste de stock con los parametros
     * deseados. El parametro esConFecha determina si se busca dentro del rango
     * establecido en los parametros fechaDesde y fechaHasta, por lo que las
     * misma deben estar establecidas. Si el parametro esConFecha es igual a
     * FALSE entonces se ignoran los parametros fechaDesde y fechaHasta por lo
     * que puede ser igual a NULL.
     *
     * @param idFuncionarioResponsable int con el id del funcionario
     * responsable. -1 para omitir
     * @param idFuncionarioRegistro int con el id del funcionario que registro
     * la información. -1 para omitir
     * @param esConFecha boolean
     * @param fechaDesde date
     * @param fechaHasta date
     * @param idEstado int con el id del estado de la cabecera. -1 para omitir
     * @param esTemporal boolean indica si es la tabla temporal o la permanente
     * @return List E_ajusteStockCabecera
     */
    public static List<E_ajusteStockCabecera> consultarAjusteStockCabecera(int idFuncionarioResponsable, int idFuncionarioRegistro, boolean esConFecha, Date fechaDesde, Date fechaHasta, int idEstado, boolean esTemporal) {
        List<E_ajusteStockCabecera> list = new ArrayList<>();
        String QUERY_RESPONSABLE = "";
        String QUERY_REGISTRADO_POR = "";
        String QUERY_ESTADO = "";
        String QUERY_TIEMPO = "";
        String QUERY_ORDERBY = "ORDER BY a.tiempo_inicio;";
        String QUERY_FROM = "FROM ajuste_stock_cabecera a, estado e ";
        String QUERY_SELECT = "SELECT id_ajuste_stock_cabecera, ";
        if (esTemporal) {
            QUERY_FROM = "FROM ajuste_stock_cabecera_temporal a, estado e ";
            QUERY_SELECT = "SELECT id_ajuste_stock_cabecera_temporal, ";
        }
        if (idFuncionarioResponsable > 0) {
            QUERY_RESPONSABLE = "AND id_funcionario_responsable = ? ";
        }
        if (idFuncionarioRegistro > 0) {
            QUERY_REGISTRADO_POR = "AND id_funcionario_registro = ? ";
        }
        if (idEstado > 0) {
            QUERY_ESTADO = "AND id_funcionario_registro = ? ";
        }
        if (esConFecha) {
            QUERY_TIEMPO = "AND tiempo BETWEEN ? AND ? ";
        }
        String QUERY = QUERY_SELECT
                + "id_funcionario_responsable, "//2
                + "id_funcionario_registro, "//3
                + "tiempo_inicio, "//4
                + "a.id_estado, "//5
                + "e.DESCRIPCION, "//6
                + "(SELECT P.NOMBRE FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_responsable )\"RESPONSABLE_NOMBRE\", "//7
                + "(SELECT P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_responsable )\"RESPONSABLE_APELLIDO\", "//8
                + "(SELECT P.NOMBRE  FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_registro)\"USUARIO_NOMBRE\", "//9
                + "(SELECT P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = a.id_funcionario_registro)\"USUARIO_APELLIDO\", "//10
                + "a.observacion, "//11
                + "tiempo_fin "//12
                + QUERY_FROM
                + "WHERE a.id_estado = e.id_estado ";
        QUERY = QUERY + QUERY_RESPONSABLE + QUERY_REGISTRADO_POR + QUERY_ESTADO + QUERY_TIEMPO + QUERY_ORDERBY;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int pos = 1;
            if (idFuncionarioResponsable > 0) {
                pst.setInt(pos++, idFuncionarioResponsable);
            }
            if (idFuncionarioRegistro > 0) {
                pst.setInt(pos++, idFuncionarioRegistro);
            }
            if (idEstado > 0) {
                pst.setInt(pos++, idEstado);
            }
            if (esConFecha) {
                pst.setTimestamp(pos++, new Timestamp(fechaDesde.getTime()));
                pst.setTimestamp(pos++, new Timestamp(fechaHasta.getTime()));
            }
            rs = pst.executeQuery();
            while (rs.next()) {

                Estado estado = new Estado();
                estado.setId(rs.getInt(5));
                estado.setDescripcion(rs.getString(6));

                M_funcionario funcResponsable = new M_funcionario();
                funcResponsable.setIdFuncionario(rs.getInt(2));
                funcResponsable.setNombre(rs.getString(7));
                funcResponsable.setApellido(rs.getString(8));
                M_funcionario funcRegistro = new M_funcionario();
                funcRegistro.setIdFuncionario(rs.getInt(3));
                funcRegistro.setNombre(rs.getString(9));
                funcRegistro.setApellido(rs.getString(10));

                E_ajusteStockCabecera cabecera = new E_ajusteStockCabecera();
                cabecera.setId(rs.getInt(1));
                cabecera.setTiempoInicio(rs.getTimestamp(4));
                cabecera.setTiempoFin(rs.getTimestamp(12));
                cabecera.setResponsable(funcResponsable);
                cabecera.setRegistradoPor(funcRegistro);
                cabecera.setEstado(estado);
                cabecera.setObservacion(rs.getString(11));
                list.add(cabecera);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_ajusteStockDetalle> consultarAjusteStockDetalle(int idCabecera, boolean esTemporal) {
        List<E_ajusteStockDetalle> list = new ArrayList<>();
        String QUERY_SELECT ="SELECT id_ajuste_stock_detalle, id_ajuste_stock_cabecera, ";
        String QUERY_FROM ="FROM ajuste_stock_detalle a WHERE id_ajuste_stock_cabecera = ?;";
        if(esTemporal){
            QUERY_SELECT ="SELECT id_ajuste_stock_detalle_temporal, id_ajuste_stock_cabecera_temporal, ";
            QUERY_FROM ="FROM ajuste_stock_detalle_temporal a WHERE id_ajuste_stock_cabecera_temporal = ?;";
        }
        String QUERY = QUERY_SELECT
                + "id_producto, "//3
                + "id_motivo, "//4
                + "cantidad_vieja, "//5
                + "cantidad_nueva, "//6
                + "tiempo, "//7
                + "observacion, "//8
                + "(SELECT am.descripcion FROM ajuste_stock_motivo am WHERE am.id_ajuste_stock_motivo = id_motivo)\"motivo\", "//9
                + "(SELECT p.descripcion FROM producto p WHERE p.id_producto = a.id_producto)\"producto\", "//10
                + "(SELECT p.codigo FROM producto p WHERE p.id_producto = a.id_producto)\"codigo\" 	"//11
                + QUERY_FROM;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt(3));
                producto.setDescripcion(rs.getString(10));
                producto.setCodigo(rs.getString(11));
                E_ajusteStockMotivo motivo = new E_ajusteStockMotivo();
                motivo.setId(rs.getInt(4));
                motivo.setDescripcion(rs.getString(9));
                E_ajusteStockDetalle detalle = new E_ajusteStockDetalle();
                detalle.setId(rs.getInt(1));
                detalle.setIdCabecera(rs.getInt(2));
                detalle.setCantidadVieja(rs.getDouble(4));
                detalle.setCantidadNueva(rs.getDouble(6));
                detalle.setTiempoRegistro(rs.getTimestamp(7));
                detalle.setObservacion(rs.getString(8));
                detalle.setProducto(producto);
                detalle.setMotivo(motivo);
                list.add(detalle);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static List<E_ajusteStockMotivo> consultarAjusteStockMotivo() {
        List<E_ajusteStockMotivo> list = new ArrayList<>();
        String QUERY = "SELECT id_ajuste_stock_motivo, descripcion, observacion	FROM ajuste_stock_motivo;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_ajusteStockMotivo motivo = new E_ajusteStockMotivo();
                motivo.setId(rs.getInt(1));
                motivo.setDescripcion(rs.getString(2));
                motivo.setObservacion(rs.getString(3));
                list.add(motivo);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static boolean existeAjusteMotivoEnUso(int idMotivo) {
        String Query = "SELECT id_ajuste_stock_detalle FROM ajuste_stock_detalle WHERE id_motivo = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, idMotivo);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static boolean existeAjusteMotivoEnUso(String descripcion) {
        String Query = "SELECT descripcion FROM ajuste_stock_motivo WHERE LOWER(descripcion) LIKE ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, descripcion.toLowerCase());
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    /*
    UPDATES
     */
    public static void actualizarAjusteStockCabecera(E_ajusteStockCabecera cabecera) {
        String UPDATE_DETALLE = "UPDATE ajuste_stock_cabecera SET "
                + "id_funcionario_responsable=?, "
                + "id_funcionario_registro=?, "
                + "tiempo_inicio=?, "
                + "tiempo_fin=?, "
                + "id_estado=?, "
                + "observacion=? "
                + "WHERE id_ajuste_stock_cabecera= ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getResponsable().getIdFuncionario());
            pst.setInt(2, cabecera.getRegistradoPor().getIdFuncionario());
            pst.setTimestamp(3, new Timestamp(cabecera.getTiempoInicio().getTime()));
            pst.setTimestamp(4, new Timestamp(cabecera.getTiempoFin().getTime()));
            pst.setInt(5, cabecera.getEstado().getId());
            if (cabecera.getObservacion() == null || cabecera.getObservacion().trim().isEmpty()) {
                pst.setNull(6, Types.VARCHAR);
            } else {
                pst.setString(6, cabecera.getObservacion());
            }
            pst.setInt(7, cabecera.getId());
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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

    public static void actualizarAjusteStockDetalle(E_ajusteStockDetalle detalle) {
        String UPDATE_DETALLE = "UPDATE ajuste_stock_detalle SET "
                + "id_motivo=?, "
                + "cantidad_vieja=?, "
                + "cantidad_nueva=?, "
                + "tiempo=?, "
                + "observacion=? "
                + "WHERE id_ajuste_stock_detalle=?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, detalle.getMotivo().getId());
            pst.setDouble(2, detalle.getCantidadVieja());
            pst.setDouble(3, detalle.getCantidadNueva());
            pst.setTimestamp(3, new Timestamp(detalle.getTiempoRegistro().getTime()));
            if (detalle.getObservacion() == null || detalle.getObservacion().trim().isEmpty()) {
                pst.setNull(4, Types.VARCHAR);
            } else {
                pst.setString(4, detalle.getObservacion());
            }
            pst.setInt(5, detalle.getId());
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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

    public static void actualizarAjusteStockMotivo(E_ajusteStockMotivo unMotivo) {
        String UPDATE_DETALLE = "UPDATE ajuste_stock_motivo SET "
                + "descripcion=?, "
                + "observacion=? "
                + "WHERE id_ajuste_stock_motivo=?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE_DETALLE);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, unMotivo.getDescripcion());
            if (unMotivo.getObservacion() == null || unMotivo.getObservacion().trim().isEmpty()) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, unMotivo.getObservacion());
            }
            pst.setInt(3, unMotivo.getId());
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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

    /*
    DELETES
     */
    public static void eliminarAjusteStockCabecera(int idCabecera) {
        String DELETE_DETALLE = "DELETE FROM ajuste_stock_detalle WHERE id_ajuste_stock_cabecera = ?;";
        String DELETE_CABECERA = "DELETE FROM ajuste_stock_cabecera WHERE id_ajuste_stock_cabecera = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(DELETE_DETALLE);
            pst.setInt(1, idCabecera);
            pst.executeUpdate();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(DELETE_CABECERA);
            pst.setInt(1, idCabecera);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void eliminarAjusteStockDetalle(int idDetalle) {
        String DELETE_DETALLE = "DELETE FROM ajuste_stock_detalle WHERE id_ajuste_stock_detalle = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(DELETE_DETALLE);
            pst.setInt(1, idDetalle);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void eliminarAjusteStockMotivo(int idMotivo) {
        String DELETE_DETALLE = "DELETE FROM ajuste_stock_motivo WHERE id_ajuste_stock_motivo = ?;";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(DELETE_DETALLE);
            pst.setInt(1, idMotivo);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Inventario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

}
