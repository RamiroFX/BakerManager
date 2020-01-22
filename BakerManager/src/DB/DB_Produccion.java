/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionTipo;
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

    public static List<E_produccionCabecera> consultarProduccion(Date inicio, Date fin, int idProdTipo, int nroPedido, int idEstado, int idFuncionario) {
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
                + "WHERE  PC.fecha_produccion BETWEEN ?  AND ? ";

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
            pst.setTimestamp(pos, new Timestamp(inicio.getTime()));
            pos++;
            pst.setTimestamp(pos, new Timestamp(fin.getTime()));
            pos++;
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
}
