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
            //se resta del stock lo que se vende
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
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA AND FUNC.ID_FUNCIONARIO = id_funcionario_responsable)\"RESPONSABLE\", "
                + "(SELECT PERS.NOMBRE || ' '|| PERS.APELLIDO WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA AND FUNC.ID_FUNCIONARIO = id_funcionario_usuario)\"USUARIO\" "
                + "FROM produccion_cabecera PC,FUNCIONARIO F, PERSONA P "
                + "WHERE  PC.TIEMPO BETWEEN ?  AND ? "
                + "AND PC.ID_FUNCIONARIO = F.ID_FUNCIONARIO "
                + "AND F.ID_PERSONA = P.ID_PERSONA ";

        if (idProdTipo > -1) {
            Query = Query + " AND PC.ID_PRODUCCION_TIPO = ? ";
        }
        if (idEstado > -1) {
            Query = Query + " AND PC.ID_ESTADO = ? ";
        }
        if (idFuncionario > -1) {
            Query = Query + " AND PC.ID_FUNCIONARIO = ? ";
        }
        Query = Query + " ORDER BY \"ID\"";
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
            Logger lgr = Logger.getLogger(DB_Ingreso.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
