/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_utilizacionMateriaPrimaCabecera;
import Entities.E_utilizacionMateriaPrimaDetalle;
import Entities.Estado;
import Entities.M_funcionario;
import Entities.M_producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_UtilizacionMateriaPrima {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static boolean existeOrdenTrabajo(int ordenTrabajo) {
        String Query = "SELECT nro_orden_trabajo FROM UTILIZACION_MATERIA_PRIMA_CABECERA WHERE nro_orden_trabajo = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, ordenTrabajo);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
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
                Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static int insertarUtilizcionMateriaPrima(E_utilizacionMateriaPrimaCabecera utilizacionCabecera, List<E_utilizacionMateriaPrimaDetalle> detalle) {

        String INSERT_CABECERA = "INSERT INTO utilizacion_materia_prima_cabecera(nro_orden_trabajo, fecha_utilizacion, id_funcionario_responsable, id_funcionario_usuario)VALUES (?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO utilizacion_materia_prima_detalle(id_utilizacion_materia_prima_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, utilizacionCabecera.getNroOrdenTrabajo());
            pst.setTimestamp(2, new Timestamp(utilizacionCabecera.getFechaUtilizacion().getTime()));
            pst.setInt(3, utilizacionCabecera.getFuncionarioProduccion().getIdFuncionario());
            pst.setInt(4, utilizacionCabecera.getFuncionarioSistema().getIdFuncionario());
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
            //se resta al stock lo que se utiliza
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
                    Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
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
                Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }

    public static List<E_utilizacionMateriaPrimaCabecera> consultarUtilizacionMateriaCabeceras(Date inicio, Date fin, int nroOrdenTrabajo, int idEstado, int idFuncionario) {
        int pos = 1;
        List<E_utilizacionMateriaPrimaCabecera> list = new ArrayList<>();
        String Query = "SELECT id_utilizacion_materia_prima_cabecera, "
                + "nro_orden_trabajo, "
                + "fecha_registro, "
                + "fecha_utilizacion, "
                + "id_funcionario_responsable, "
                + "id_funcionario_usuario, "
                + "id_estado, "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = UC.ID_ESTADO) \"ESTADO\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = UC.id_funcionario_responsable )\"RESPONSABLE\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = UC.id_funcionario_usuario)\"USUARIO\" "
                + "FROM utilizacion_materia_prima_cabecera UC "
                + "WHERE  UC.fecha_utilizacion BETWEEN ?  AND ? ";

        if (idEstado > -1) {
            Query = Query + " AND UC.ID_ESTADO = ? ";
        }
        if (idFuncionario > -1) {
            Query = Query + " AND UC.id_funcionario_responsable = ? ";
        }
        if (nroOrdenTrabajo > -1) {
            Query = Query + " AND UC.nro_orden_trabajo = ? ";
        }
        Query = Query + " ORDER BY fecha_utilizacion ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setTimestamp(pos, new Timestamp(inicio.getTime()));
            pos++;
            pst.setTimestamp(pos, new Timestamp(fin.getTime()));
            pos++;
            if (idEstado > -1) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            if (idFuncionario > -1) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (nroOrdenTrabajo > -1) {
                pst.setInt(pos, nroOrdenTrabajo);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario responsable = new M_funcionario();
                responsable.setIdFuncionario(rs.getInt("id_funcionario_responsable"));
                responsable.setNombre(rs.getString("RESPONSABLE"));
                M_funcionario usuario = new M_funcionario();
                usuario.setIdFuncionario(rs.getInt("id_funcionario_usuario"));
                usuario.setNombre(rs.getString("USUARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_utilizacionMateriaPrimaCabecera umpc = new E_utilizacionMateriaPrimaCabecera();
                umpc.setFuncionarioProduccion(responsable);
                umpc.setFuncionarioSistema(usuario);
                umpc.setEstado(estado);
                umpc.setId(rs.getInt("id_utilizacion_materia_prima_cabecera"));
                umpc.setNroOrdenTrabajo(rs.getInt("nro_orden_trabajo"));
                umpc.setFechaRegistro(rs.getDate("fecha_registro"));
                umpc.setFechaUtilizacion(rs.getDate("fecha_utilizacion"));
                list.add(umpc);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }

    public static List<E_utilizacionMateriaPrimaDetalle> consultarUtilizacionMateriaPrimaDetalle(Integer idUtilizacionCabecera) {
        List<E_utilizacionMateriaPrimaDetalle> list = new ArrayList<>();
        String QUERY = "SELECT ID_UTILIZACION_MATERIA_PRIMA_DETALLE, "
                + "ID_PRODUCTO, "
                + "CANTIDAD, "
                + "(SELECT P.DESCRIPCION FROM PRODUCTO P WHERE P.ID_PRODUCTO = UTILIZACION_MATERIA_PRIMA_DETALLE.ID_PRODUCTO )\"PRODUCTO\", "
                + "(SELECT P.CODIGO FROM PRODUCTO P WHERE P.ID_PRODUCTO = UTILIZACION_MATERIA_PRIMA_DETALLE.ID_PRODUCTO )\"CODIGO\" "
                + "FROM UTILIZACION_MATERIA_PRIMA_DETALLE  "
                + "WHERE ID_UTILIZACION_MATERIA_PRIMA_CABECERA = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idUtilizacionCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setId(rs.getInt("ID_PRODUCTO"));
                producto.setDescripcion(rs.getString("PRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO"));
                E_utilizacionMateriaPrimaDetalle pd = new E_utilizacionMateriaPrimaDetalle();
                pd.setId(rs.getInt("ID_UTILIZACION_MATERIA_PRIMA_DETALLE"));
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

    public static E_utilizacionMateriaPrimaCabecera obtenerUtilizacionMateriaCabecera(int idUtilizacionCabecera) {
        E_utilizacionMateriaPrimaCabecera pc = null;
        String Query = "SELECT id_utilizacion_materia_prima_cabecera, "
                + "nro_orden_trabajo, "
                + "fecha_registro, "
                + "fecha_utilizacion, "
                + "id_funcionario_responsable, "
                + "id_funcionario_usuario, "
                + "id_estado, "
                + "(SELECT ESTA.DESCRIPCION FROM ESTADO ESTA WHERE ESTA.ID_ESTADO = PC.ID_ESTADO) \"ESTADO\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_responsable )\"RESPONSABLE\", "
                + "(SELECT P.NOMBRE || ' '|| P.APELLIDO FROM FUNCIONARIO F, PERSONA P WHERE P.ID_PERSONA = F.ID_PERSONA AND F.ID_FUNCIONARIO = PC.id_funcionario_usuario)\"USUARIO\" "
                + "FROM utilizacion_materia_prima_cabecera PC "
                + "WHERE  PC.id_utilizacion_materia_prima_cabecera = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idUtilizacionCabecera);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario responsable = new M_funcionario();
                responsable.setIdFuncionario(rs.getInt("id_funcionario_responsable"));
                responsable.setNombre(rs.getString("RESPONSABLE"));
                M_funcionario usuario = new M_funcionario();
                usuario.setIdFuncionario(rs.getInt("id_funcionario_usuario"));
                usuario.setNombre(rs.getString("USUARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                pc = new E_utilizacionMateriaPrimaCabecera();
                pc.setFuncionarioProduccion(responsable);
                pc.setFuncionarioSistema(usuario);
                pc.setEstado(estado);
                pc.setId(rs.getInt("id_utilizacion_materia_prima_cabecera"));
                pc.setNroOrdenTrabajo(rs.getInt("nro_orden_trabajo"));
                pc.setFechaUtilizacion(rs.getTimestamp("fecha_utilizacion"));
                pc.setFechaRegistro(rs.getTimestamp("fecha_registro"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Produccion.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return pc;
    }

    public static void anularUtilizacionMateriaPrima(int idUtilizacionCabecera, List<E_utilizacionMateriaPrimaDetalle> detalle) {
        String UPDATE_PRODUCCION = "UPDATE UTILIZACION_MATERIA_PRIMA_CABECERA SET "
                + "ID_ESTADO = 2, "
                + "NRO_ORDEN_TRABAJO = NULL "
                + "WHERE ID_UTILIZACION_MATERIA_PRIMA_CABECERA = ?; ";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_PRODUCCION);
            pst.setInt(1, idUtilizacionCabecera);
            pst.executeUpdate();
            pst.close();

            //se suma al stock lo que se anula
            for (int i = 0; i < detalle.size(); i++) {
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + detalle.get(i).getProducto().getId() + ")+" + detalle.get(i).getCantidad() + ") "
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

    public static List<E_utilizacionMateriaPrimaDetalle> consultarUtilizacionMateriaPrimaDetalleAgrupado(List<E_utilizacionMateriaPrimaCabecera> cadenaCabeceras) {
        List<E_utilizacionMateriaPrimaDetalle> list = new ArrayList<>();
        boolean b = true;
        StringBuilder builder = new StringBuilder();
        for (E_utilizacionMateriaPrimaCabecera seleccionVenta : cadenaCabeceras) {
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
                + "FROM UTILIZACION_MATERIA_PRIMA_DETALLE PRDE, UTILIZACION_MATERIA_PRIMA_CABECERA PRCA, PRODUCTO PROD "
                + "WHERE PRDE.ID_UTILIZACION_MATERIA_PRIMA_CABECERA = PRCA.ID_UTILIZACION_MATERIA_PRIMA_CABECERA "
                + "AND PRDE.ID_PRODUCTO = PROD.ID_PRODUCTO "
                + "AND PRCA.ID_UTILIZACION_MATERIA_PRIMA_CABECERA IN ("
                + builder.substring(0, builder.length() - 1) + ")";

        String PIE = "GROUP BY PROD.DESCRIPCION, PROD.CODIGO "
                + "ORDER BY PROD.DESCRIPCION";
        QUERY = QUERY + PIE;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            for (E_utilizacionMateriaPrimaCabecera seleccionVenta : cadenaCabeceras) {
                pst.setInt(index, seleccionVenta.getId());
                index++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                E_utilizacionMateriaPrimaDetalle umpd = new E_utilizacionMateriaPrimaDetalle();
                M_producto producto = new M_producto();
                producto.setCodBarra(rs.getString("Codigo"));
                producto.setDescripcion(rs.getString("Producto"));
                umpd.setProducto(producto);
                umpd.setCantidad(rs.getDouble("Cantidad"));
                list.add(umpd);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_UtilizacionMateriaPrima.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
