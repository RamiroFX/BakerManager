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
    
    public static int insertarProduccionFilm(E_produccionCabecera produccionCabecera, List<E_produccionFilm> detalle) {
        String INSERT_CABECERA = "INSERT INTO produccion_cabecera(nro_orden_trabajo, fecha_produccion, id_funcionario_responsable, id_funcionario_usuario, id_produccion_tipo)VALUES( ?, ?, ?, ?, ?);";
        //LA SGBD SE ENCARGA DE INSERTAR EL TIMESTAMP.
        String INSERT_DETALLE = "INSERT INTO produccion_detalle(id_produccion_cabecera, id_producto, cantidad)VALUES (?, ?, ?);";
        String INSERT_FILM = "INSERT INTO produccion_film(nro_film, id_produccion_cabecera, id_produccion_detalle, peso, fecha_creacion, id_funcionario_responsable, cono, medida, micron, id_producto_categoria, id_estado)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_PROD_FILM_PROD = "INSERT INTO produccion_film_producto(id_produccion_film, id_producto)VALUES (?, ?);";
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
            for (int i = 0; i < detalle.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_DETALLE, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, (int) sq_cabecera);
                pst.setInt(2, detalle.get(i).getProducto().getId());
                pst.setDouble(3, detalle.get(i).getPeso());
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    detalle.get(i).setId((int) rs.getLong(1));
                }
                pst.close();
            }
            for (int i = 0; i < detalle.size(); i++) {
                E_produccionFilm prodFilm = detalle.get(i);
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
            for (E_produccionFilm e_produccionFilm : detalle) {
                pst = DB_manager.getConection().prepareStatement(INSERT_PROD_FILM_PROD);
                pst.setInt(1, prodFilmKeys.get(prodFilmIndex));
                prodFilmIndex++;
                pst.setInt(2, e_produccionFilm.getProducto().getId());
                pst.executeUpdate();
                pst.close();
            }
            //se suma al stock lo que se produce
            for (int i = 0; i < detalle.size(); i++) {
                int idProducto = detalle.get(i).getProducto().getId();
                double cantidad = detalle.get(i).getPeso();
                String query = "UPDATE PRODUCTO SET "
                        + "CANT_ACTUAL = "
                        + "((SELECT CANT_ACTUAL FROM PRODUCTO WHERE ID_PRODUCTO = " + idProducto + ")+" + cantidad + ") "
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

    public static ArrayList<E_produccionFilm> consultarFilmDisponible(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String estado) {
        ArrayList<E_produccionFilm> filmList = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }

            String fromQuery = "FROM v_film_actual V ";
            String finalQuery = "ORDER BY V.producto ";
            String buscarSQL = "";
            String estadoSQL = "";
            //BUSCAR 
            switch (buscarPor) {
                case "Todos": {
                    buscarSQL = "((LOWER(CAST(v.nro_orden_trabajo AS CHARACTER VARYING)) LIKE ?) OR (LOWER(CAST(v.nro_film AS CHARACTER VARYING)) LIKE ?) "
                            + "OR (LOWER(v.producto) LIKE ?) )";
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
                    estadoSQL = "AND peso_actual > 0 ";
                    break;
                }
                case "Agotado": {
                    estadoSQL = "AND peso_actual <= 0 ";
                    break;
                }
                case "Todos": {
                    estadoSQL = " ";
                    break;
                }
            }

            String Query = "SELECT id_cabecera, nro_orden_trabajo, nro_film, fecha, "
                    + "producto, cono, medida, micron, peso, peso_utilizado, peso_actual,"
                    + "id_categoria, categoria "
                    + fromQuery
                    + "WHERE "
                    + buscarSQL
                    + estadoSQL
                    + finalQuery;

            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (buscarPor.equals("Todos")) {
                pst.setString(1, "%" + descripcion + "%");
                pst.setString(2, "%" + descripcion + "%");
                pst.setString(3, "%" + descripcion + "%");
            } else {
                pst.setString(1, "%" + descripcion + "%");
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

    public static List<E_produccionFilm> consultarProduccionFilmBaja(Integer idProduccion) {
        List<E_produccionFilm> list = new ArrayList<>();
        String QUERY = "SELECT id_cabecera, id_produccion_cabecera,nro_orden_trabajo, nro_film, fecha, "
                + "producto, id_categoria, categoria, peso, cono, medida, micron, producto_codigo "
                + "FROM v_produccion_film_baja WHERE id_produccion_cabecera = ?;";

        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProduccion);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_producto producto = new M_producto();
                producto.setCodigo(rs.getString("producto_codigo"));
                producto.setDescripcion(rs.getString("producto"));
                E_productoClasificacion pc = new E_productoClasificacion();
                pc.setId(rs.getInt("id_categoria"));
                pc.setDescripcion(rs.getString("categoria"));
                E_produccionFilm pd = new E_produccionFilm();
                pd.setId(rs.getInt("id_cabecera"));
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
