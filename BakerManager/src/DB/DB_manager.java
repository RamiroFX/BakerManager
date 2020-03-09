/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_Divisa;
import Entities.E_Empresa;
import Entities.E_banco;
import Entities.E_formaPago;
import Entities.E_tipoCheque;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_campoImpresion;
import Entities.ProductoCategoria;
import Utilities.Config;
import Utilities.MyConstants;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class DB_manager {

    private static Connection con = null;
    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    private static ResultSetMetaData rsmd = null;
    private static DatabaseMetaData dbmt = null;

    public static boolean conectarBD(String usuario, String password) throws SQLException {
        try {
            Conexiones.cargarDriver(Conexiones.SGBD_POSTGRES);
            String port = Config.getPort();
            String host = Config.getHost();
            String url = "jdbc:postgresql://" + host + ":" + port + "/bakermanager2";
            con = Conexiones.obtenerConexion(Conexiones.SGBD_POSTGRES, url, usuario, password);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar driver: " + e);
            JOptionPane.showMessageDialog(null, "Error al conectarse: problema con el driver", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.out.println("Error al conectarse: " + e);
            JOptionPane.showMessageDialog(null, "Error al conectarse: problema de red", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static boolean conectarBD(int dbms, String url, String usuario, String password) throws SQLException {
        boolean b = false;
        switch (dbms) {
            case Conexiones.SGBD_ORACLE: {
                try {
                    Conexiones.cargarDriver(Conexiones.SGBD_ORACLE);
                } catch (ClassNotFoundException e) {
                    System.out.println("Error al cargar driver" + e);
                }
            }
            break;
            case Conexiones.SGBD_POSTGRES: {
                try {
                    Conexiones.cargarDriver(Conexiones.SGBD_POSTGRES);
                } catch (ClassNotFoundException e) {
                    System.out.println("Error al cargar driver" + e);
                }
            }
            break;
        }
        try {
            con = Conexiones.obtenerConexion(url, usuario, password);
            b = true;
        } catch (SQLException e) {
            System.out.println("Error al conectarse" + e);
        }
        return b;
    }

    public static Connection getConection() {
        return con;
    }

    public static String obtenerNombreBaseDatos() throws SQLException {
        String BaseDatos = null;
        BaseDatos = con.getMetaData().getDatabaseProductName();
        return BaseDatos;
    }

    public static String obtenerNombreUsuarioDB() throws SQLException {
        String usuario = null;
        usuario = con.getMetaData().getUserName();
        return usuario;
    }

    public static String obtenerNombreServidor() throws SQLException {
        String servidor = null;
        servidor = con.getCatalog();
        return servidor;
    }

    /**
     * TOma una sentencia SQL, la pasa a la BD, y obtiene los resultados.
     * Retorna un objeto c_ResultSetTableModel, que contiene los resultados de
     * forma que el componente JTable pueda mostrarlos.
     *
     */
    public static ResultSetTableModel getResultSetTableModel(String query)
            throws SQLException {

        if (con == null) {
            throw new IllegalStateException("Connection already closed.");
        }

        //se crea una sentencia
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        // se ejecuta el query y se obtienen los resultados en un ResultSet
        ResultSet r = statement.executeQuery(query);

        ResultSetTableModel rstm = new ResultSetTableModel(r);

        return rstm;
    }

    public static void cerrarConexiones() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Vector obtenerCategoria() {
        Vector rubro = null;
        String q = "SELECT descripcion "
                + "FROM PRODUCTO_CATEGORIA ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rubro = new Vector();
            while (rs.next()) {
                rubro.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rubro;
    }

    public static ArrayList<ProductoCategoria> obtenerCategorias() {
        ArrayList<ProductoCategoria> rubro = null;
        String q = "SELECT * "
                + "FROM PRODUCTO_CATEGORIA ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rubro = new ArrayList();
            while (rs.next()) {
                ProductoCategoria pc = new ProductoCategoria();
                pc.setId(rs.getInt("id_producto_categoria"));
                pc.setDescripcion(rs.getString("descripcion"));
                rubro.add(pc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rubro;
    }

    public static ArrayList<M_campoImpresion> obtenerCampoImpresion(int idImpresionTipo, int status) {
        ArrayList<M_campoImpresion> campoImpresionList = null;
        String Query = "";

        if (status == MyConstants.TODOS) {
            Query = "SELECT IMPRESION_CAMPO.ID_IMPRESION_CAMPO, IMPRESION_CAMPO.DESCRIPCION, IMPRESION_CAMPO.COORDENADA_X, IMPRESION_CAMPO.COORDENADA_Y, "
                    + "ESTADO.ID_ESTADO , ESTADO.DESCRIPCION FROM IMPRESION_CAMPO, ESTADO "
                    + "WHERE IMPRESION_CAMPO.ID_IMPRESION_TIPO = ? AND IMPRESION_CAMPO.ID_ESTADO= ESTADO.ID_ESTADO "
                    + "ORDER BY IMPRESION_CAMPO.ID_IMPRESION_CAMPO;";
        } else {
            Query = "SELECT IMPRESION_CAMPO.ID_IMPRESION_CAMPO, IMPRESION_CAMPO.DESCRIPCION, IMPRESION_CAMPO.COORDENADA_X, IMPRESION_CAMPO.COORDENADA_Y, "
                    + "ESTADO.ID_ESTADO , ESTADO.DESCRIPCION FROM IMPRESION_CAMPO, ESTADO "
                    + "WHERE IMPRESION_CAMPO.ID_IMPRESION_TIPO = ? "
                    + "AND IMPRESION_CAMPO.ID_ESTADO = ? "
                    + "AND IMPRESION_CAMPO.ID_ESTADO = ESTADO.ID_ESTADO "
                    + "ORDER BY IMPRESION_CAMPO.ID_IMPRESION_CAMPO;";
        }
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, idImpresionTipo);
            if (status == MyConstants.TODOS) {
                pst.setInt(1, idImpresionTipo);
            } else {
                pst.setInt(1, idImpresionTipo);
                pst.setInt(2, status);
            }
            rs = pst.executeQuery();
            campoImpresionList = new ArrayList();
            while (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getInt(5));
                estado.setDescripcion(rs.getString(6));
                M_campoImpresion ci = new M_campoImpresion();
                ci.setId(rs.getInt(1));
                ci.setCampo(rs.getString(2));
                ci.setX(rs.getDouble(3));
                ci.setY(rs.getDouble(4));
                ci.setEstado(estado);
                campoImpresionList.add(ci);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return campoImpresionList;
    }

    public static boolean existeCampoParametro(int idTipo, String campoParametroDescripcion) {
        String Query = "SELECT DESCRIPCION FROM IMPRESION_CAMPO WHERE DESCRIPCION LIKE ? AND id_impresion_tipo = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, campoParametroDescripcion);
            pst.setInt(2, idTipo);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static M_campoImpresion obtenerCampoParametro(int idTipo, String campoParametroDescripcion) {
        M_campoImpresion ci = null;
        String Query = "SELECT IMPRESION_CAMPO.ID_IMPRESION_CAMPO, IMPRESION_CAMPO.DESCRIPCION, IMPRESION_CAMPO.COORDENADA_X, IMPRESION_CAMPO.COORDENADA_Y, "
                + "ESTADO.ID_ESTADO, ESTADO.DESCRIPCION FROM IMPRESION_CAMPO, ESTADO "
                + "WHERE IMPRESION_CAMPO.DESCRIPCION LIKE ? AND IMPRESION_CAMPO.ID_IMPRESION_TIPO = ? "
                + "AND IMPRESION_CAMPO.ID_ESTADO= ESTADO.ID_ESTADO ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setString(1, campoParametroDescripcion);
            pst.setInt(2, idTipo);
            rs = pst.executeQuery();
            while (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getInt(5));
                estado.setDescripcion(rs.getString(6));
                ci = new M_campoImpresion();
                ci.setId(rs.getInt(1));
                ci.setCampo(rs.getString(2));
                ci.setX(rs.getDouble(3));
                ci.setY(rs.getDouble(4));
                ci.setEstado(estado);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return ci;
    }

    public static void insertarCampoImpresion(int idImpresionTipo, M_campoImpresion campoImpresion) {
        String insert = "INSERT INTO IMPRESION_CAMPO("
                + "ID_IMPRESION_TIPO, DESCRIPCION, COORDENADA_X, COORDENADA_Y, ID_ESTADO"
                + ")VALUES (?,?,?,?,?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setInt(1, idImpresionTipo);
            pst.setString(2, campoImpresion.getCampo());
            pst.setDouble(3, campoImpresion.getX());
            pst.setDouble(4, campoImpresion.getY());
            pst.setInt(5, campoImpresion.getEstado().getId());
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int modificarCampoImpresion(M_campoImpresion campoImpresion) {
        String UPDATE = "UPDATE IMPRESION_CAMPO SET DESCRIPCION = ?, COORDENADA_x = ?,"
                + " COORDENADA_Y = ? WHERE ID_IMPRESION_CAMPO = ? ;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setString(1, campoImpresion.getCampo());
            pst.setDouble(2, campoImpresion.getX());
            pst.setDouble(3, campoImpresion.getY());
            pst.setInt(4, campoImpresion.getId());
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int eliminarCampoImpresion(int idCampoImpresion) {
        String DELETE_QUERY = "DELETE FROM IMPRESION_CAMPO WHERE ID_IMPRESION_CAMPO = ? ;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(DELETE_QUERY);
            pst.setInt(1, idCampoImpresion);
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int habilitarDeshabilitarCampoImpresion(int idCampoImpresion, int idEstado) {
        String UPDATE = "UPDATE IMPRESION_CAMPO SET ID_ESTADO = ? WHERE ID_IMPRESION_CAMPO = ? ;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, idEstado);
            pst.setInt(2, idCampoImpresion);
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static ResultSetTableModel consultarCategoria() {
        ResultSetTableModel rubro = null;
        String q = "SELECT ID_PRODUCTO_CATEGORIA \"ID\" ,descripcion \"Descripcion\" "
                + "FROM PRODUCTO_CATEGORIA ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rubro = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rubro;
    }

    public static DefaultTableModel consultarCategorias() {
        DefaultTableModel rubro = null;
        String q = "SELECT ID_PRODUCTO_CATEGORIA \"ID\" ,descripcion \"Descripcion\" "
                + "FROM PRODUCTO_CATEGORIA ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rubro = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rubro;
    }

    public static Vector obtenerImpuesto() {
        Vector impuesto = null;
        String q = "SELECT descripcion "
                + "FROM impuesto ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new Vector();
            while (rs.next()) {
                impuesto.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static ResultSetTableModel consultarImpuesto() {
        ResultSetTableModel impuesto = null;
        String q = "SELECT id_impuesto \"ID\" , descripcion\"Descripcion\" "
                + "FROM impuesto ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static ResultSetTableModel consultarPais() {
        ResultSetTableModel pais = null;
        String q = "SELECT id_pais \"ID\" , descripcion\"Descripcion\" "
                + "FROM pais ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            pais = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pais;
    }

    public static ResultSetTableModel consultarCiudad() {
        ResultSetTableModel pais = null;
        String q = "SELECT id_ciudad \"ID\" , descripcion\"Descripcion\" "
                + "FROM CIUDAD ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            pais = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pais;
    }

    public static Vector obtenerEstado() {
        Vector estado = null;
        String q = "SELECT descripcion "
                + "FROM estado ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estado = new Vector();
            while (rs.next()) {
                estado.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return estado;
    }

    public static ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estado = null;
        String q = "SELECT * FROM estado ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estado = new ArrayList();
            while (rs.next()) {
                Estado e = new Estado();
                e.setId(rs.getInt("id_estado"));
                e.setDescripcion(rs.getString("descripcion"));
                estado.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return estado;
    }

    public static Vector consultarRespuesta() {
        Vector respuesta = null;
        String q = "SELECT descripcion "
                + "FROM respuesta ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            respuesta = new Vector();
            while (rs.next()) {
                respuesta.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return respuesta;
    }

    public static ResultSetTableModel consultarMarca() {
        ResultSetTableModel impuesto = null;
        String q = "SELECT id_marca \"ID\" ,descripcion \"Descripcion\" "
                + "FROM marca ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static ResultSetTableModel consultarBanco() {
        ResultSetTableModel impuesto = null;
        String q = "SELECT id_banco \"ID\" ,descripcion \"Descripcion\" "
                + "FROM banco ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static Vector obtenerMarca() {
        Vector impuesto = null;
        String q = "SELECT descripcion  "
                + "FROM marca ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new Vector();
            while (rs.next()) {
                impuesto.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static Vector obtenerPais() {
        Vector pais = null;
        String q = "SELECT descripcion  "
                + "FROM pais ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            pais = new Vector();
            while (rs.next()) {
                pais.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pais;
    }

    public static Vector obtenerTelefonoCategoria() {
        Vector telefonCategoria = null;
        String q = "SELECT descripcion  "
                + "FROM TELEFONO_CATEGORIA ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            telefonCategoria = new Vector();
            while (rs.next()) {
                telefonCategoria.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return telefonCategoria;
    }

    public static Integer obtenerIdPais(String nombrePais) {
        Integer pais = null;
        String q = "SELECT id_pais  "
                + "FROM pais "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, nombrePais);
            rs = pst.executeQuery();
            while (rs.next()) {
                pais = (rs.getInt("id_pais"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pais;
    }

    public static Integer obtenerIdGenero(String genero) {
        Integer sexo = null;
        String q = "SELECT ID_SEXO "
                + "FROM SEXO "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, genero);
            rs = pst.executeQuery();
            while (rs.next()) {
                sexo = (rs.getInt("ID_SEXO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sexo;
    }

    public static Integer obtenerIdEstadoCivil(String estado_civil) {
        Integer est_civil = null;
        String q = "SELECT ID_ESTADO_CIVIL "
                + "FROM ESTADO_CIVIL "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, estado_civil);
            rs = pst.executeQuery();
            while (rs.next()) {
                est_civil = (rs.getInt("ID_ESTADO_CIVIL"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return est_civil;
    }

    public static Integer obtenerIdCiudad(String ciudad) {
        Integer id_ciudad = null;
        String q = "SELECT ID_CIUDAD "
                + "FROM CIUDAD "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, ciudad);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_ciudad = (rs.getInt("ID_CIUDAD"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id_ciudad;
    }

    public static Integer obtenerIdMarca(String marca) {
        Integer idMarca = null;
        String q = "SELECT ID_MARCA "
                + "FROM MARCA "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, marca);
            rs = pst.executeQuery();
            while (rs.next()) {
                idMarca = (rs.getInt("ID_MARCA"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idMarca;
    }

    public static Integer obtenerIdBanco(String banco) {
        Integer idMarca = null;
        String q = "SELECT ID_BANCO "
                + "FROM BANCO "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, banco);
            rs = pst.executeQuery();
            while (rs.next()) {
                idMarca = (rs.getInt("ID_BANCO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idMarca;
    }

    public static void modificarBanco(int idBanco, String descripcion) {
        String updateMarca = "UPDATE BANCO SET "
                + "DESCRIPCION = ? "
                + "WHERE ID_BANCO = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updateMarca);
            pst.setString(1, descripcion);
            pst.setInt(2, idBanco);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean marcaEnUso(int idMarca) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_MARCA "
                + "FROM PRODUCTO "
                + "WHERE ID_MARCA = ? ;";
        try {
            pst = con.prepareStatement(q);
            pst.setInt(1, idMarca);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
    }

    public static boolean bancoEnUso(int idBanco) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_BANCO "
                + "FROM cuenta_corriente_detalle "
                + "WHERE ID_BANCO = ? ;";
        try {
            pst = con.prepareStatement(q);
            pst.setInt(1, idBanco);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
    }

    public static void eliminarBanco(int idBanco) {
        String delete = "DELETE FROM BANCO WHERE ID_BANCO =" + idBanco;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static Integer obtenerIdProductoCategoria(String categoria) {
        Integer idCategoria = null;
        String q = "SELECT ID_PRODUCTO_CATEGORIA "
                + "FROM PRODUCTO_CATEGORIA "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, categoria);
            rs = pst.executeQuery();
            while (rs.next()) {
                idCategoria = (rs.getInt("ID_PRODUCTO_CATEGORIA"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idCategoria;
    }

    public static boolean productCategoriaEnUso(int idCategoria) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_CATEGORIA "
                + "FROM PRODUCTO "
                + "WHERE ID_CATEGORIA = ? ;";
        try {
            pst = con.prepareStatement(q);
            pst.setInt(1, idCategoria);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
    }

    public static Integer obtenerIdImpuesto(int impuesto) {
        Integer idMarca = null;
        String q = "SELECT ID_IMPUESTO "
                + "FROM IMPUESTO "
                + " WHERE DESCRIPCION = ? ;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, impuesto);
            rs = pst.executeQuery();
            while (rs.next()) {
                idMarca = (rs.getInt("ID_IMPUESTO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idMarca;
    }

    public static Integer obtenerIdEstado(String estado) {
        Integer idMarca = null;
        String q = "SELECT ID_ESTADO "
                + "FROM ESTADO "
                + " WHERE LOWER(DESCRIPCION) LIKE ?;";
        try {
            pst = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, estado);
            rs = pst.executeQuery();
            while (rs.next()) {
                idMarca = (rs.getInt("ID_ESTADO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idMarca;
    }

    public static String obtenerCiudad(int id) {
        String ciudad = null;
        String q = "SELECT DESCRIPCION "
                + "FROM CIUDAD "
                + "WHERE ID_CIUDAD = " + id;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                ciudad = (rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ciudad;
    }

    public static String obtenerEstadoCivil(Integer id_estado_civil) {
        String estado_civil = null;
        String q = "SELECT DESCRIPCION "
                + "FROM ESTADO_CIVIL "
                + "WHERE ID_ESTADO_CIVIL = " + id_estado_civil;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                estado_civil = (rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return estado_civil;
    }

    public static String obtenerGenero(Integer id_genero) {
        String genero = null;
        String q = "SELECT DESCRIPCION "
                + "FROM SEXO "
                + "WHERE ID_SEXO = " + id_genero;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                genero = (rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return genero;
    }

    public static String obtenerPais(Integer id_pais) {
        String pais = null;
        String q = "SELECT DESCRIPCION "
                + "FROM PAIS "
                + "WHERE ID_PAIS = " + id_pais;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                pais = (rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pais;
    }

    public static Vector obtenerCiudad() {
        Vector ciudad = null;
        String q = "SELECT descripcion  "
                + "FROM Ciudad ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            ciudad = new Vector();
            while (rs.next()) {
                ciudad.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ciudad;
    }

    public static Vector obtenerEstadoCivil() {
        Vector estadoCivil = null;
        String q = "SELECT descripcion  "
                + "FROM estado_civil ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            estadoCivil = new Vector();
            while (rs.next()) {
                estadoCivil.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return estadoCivil;
    }

    public static Vector obtenerGenero() {
        Vector impuesto = null;
        String q = "SELECT descripcion "
                + "FROM sexo ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impuesto = new Vector();
            while (rs.next()) {
                impuesto.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impuesto;
    }

    public static Vector obtenerRoles() {
        Vector rol = null;
        String q = "SELECT descripcion "
                + "FROM rol ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rol = new Vector();
            while (rs.next()) {
                rol.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rol;
    }

    /*
     * Metodo para manejo de transaccion manual
     */
    public static void habilitarTransaccionManual() {
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deshabilita el autocommit de la base de datos
     *
     * @exception SQLException Si no se pudo establecer autocommit
     */
    public static void deshabilitarTransaccionManual() {
        try {

            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//	***Metodo para establecer transaccion***
    public static void establecerTransaccion() throws SQLException {
        con.commit();
    }

//	***Metodo para manejo deshacer la transaccion***
    public static void deshacerTransaccion() {
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean verificarUsuario(String alias, String password) {
        String query = "SELECT ALIAS FROM FUNCIONARIO WHERE ALIAS = ? AND PASSWORD = ?;";
        try {
            pst = getConection().prepareStatement(query);
            pst.setString(1, alias);
            pst.setString(2, password);
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean existCi(int cedula) {
        String query = "SELECT CI FROM PERSONA WHERE CI = ?;";
        try {
            pst = getConection().prepareStatement(query);
            pst.setInt(1, cedula);
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean existeTelefono(String telefono) {
        String query = "SELECT NUMERO FROM TELEFONO WHERE NUMERO = ? ;";
        try {
            pst = getConection().prepareStatement(query);
            pst.setString(1, telefono);
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void insertarPais(String pais) {
        String insert = "INSERT INTO PAIS("
                + "DESCRIPCION"
                + ")VALUES (?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setString(1, pais);
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void insertarCiudad(String ciudad) {
        String insert = "INSERT INTO CIUDAD("
                + "DESCRIPCION"
                + ")VALUES (?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setString(1, ciudad);
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void modificarPais(int idPais, String descripcion) {
        String updatePais = "UPDATE PAIS SET "
                + "DESCRIPCION = ? "
                + "WHERE ID_PAIS = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updatePais);
            pst.setString(1, descripcion);
            pst.setInt(2, idPais);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void modificarCiudad(int idCiudad, String descripcion) {
        String updateMarca = "UPDATE CIUDAD SET "
                + "DESCRIPCION = ? "
                + "WHERE ID_CIUDAD = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updateMarca);
            pst.setString(1, descripcion);
            pst.setInt(2, idCiudad);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean paisEnUso(int idPais) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_PAIS "
                + "FROM PERSONA "
                + "WHERE ID_PAIS = ? ;";
        try {
            pst = con.prepareStatement(q);
            pst.setInt(1, idPais);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
    }

    public static boolean ciudadEnUso(int idCiudad) {
        boolean enUso = false;
        String q = "SELECT DISTINCT ID_CIUDAD "
                + "FROM PERSONA "
                + "WHERE ID_CIUDAD = ? ;";
        try {
            pst = con.prepareStatement(q);
            pst.setInt(1, idCiudad);
            rs = pst.executeQuery();
            return !rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enUso;
    }

    public static void eliminarPais(int idPais) {
        String delete = "DELETE FROM PAIS WHERE ID_PAIS =" + idPais;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarCiudad(int idCiudad) {
        String delete = "DELETE FROM CIUDAD WHERE ID_CIUDAD =" + idCiudad;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_manager.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_manager.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static Integer obtenerTipoOperacion(String tipoOperacion) {
        Integer idTipoOperacion = 0;
        String query = "SELECT ID_TIPO_OPERACION \"ID_TIPO_OPERACION\" "
                + "FROM TIPO_OPERACION "
                + "WHERE descripcion LIKE '" + tipoOperacion + "'";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                idTipoOperacion = rs.getInt("ID_TIPO_OPERACION");
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
        return idTipoOperacion;
    }

    public static E_tipoOperacion obtenerTipoOperaccion(int idTipoOperacion) {
        E_tipoOperacion tiop = null;
        String query = "SELECT * FROM TIPO_OPERACION WHERE ID_TIPO_OPERACION =  ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idTipoOperacion);
            rs = pst.executeQuery();
            while (rs.next()) {
                tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("ID_TIPO_OPERACION"));
                tiop.setDuracion(rs.getInt("DURACION"));
                tiop.setDescripcion(rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_manager.class.getName());
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
        return tiop;
    }

    public static E_tipoOperacion obtenerTipoOperaccion(String tipoOperacion) {
        E_tipoOperacion tiop = null;
        String query = "SELECT * FROM TIPO_OPERACION WHERE DESCRIPCION LIKE ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, tipoOperacion);
            rs = pst.executeQuery();
            while (rs.next()) {
                tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("ID_TIPO_OPERACION"));
                tiop.setDuracion(rs.getInt("DURACION"));
                tiop.setDescripcion(rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_manager.class.getName());
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
        return tiop;
    }

    public static Vector obtenerTipoOperacion() {
        Vector tiop = null;
        String q = "SELECT descripcion  "
                + "FROM TIPO_OPERACION ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tiop = new Vector();
            while (rs.next()) {
                tiop.add(rs.getString("descripcion"));
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
        return tiop;
    }

    public static ArrayList<E_tipoOperacion> obtenerTipoOperaciones() {
        ArrayList<E_tipoOperacion> tiopList = null;
        String q = "SELECT *  "
                + "FROM TIPO_OPERACION ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tiopList = new ArrayList();
            while (rs.next()) {
                E_tipoOperacion tiop = new E_tipoOperacion();
                tiop.setId(rs.getInt("id_tipo_operacion"));
                tiop.setDuracion(rs.getInt("duracion"));
                tiop.setDescripcion(rs.getString("descripcion"));
                tiopList.add(tiop);
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
        return tiopList;
    }

    /*
    public static Vector obtenerTipoVenta() {
        Vector tipoVenta = null;
        String q = "SELECT descripcion  "
                + "FROM impresion_tipo ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tipoVenta = new Vector();
            while (rs.next()) {
                tipoVenta.add(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tipoVenta;
    }*/
    public static E_Empresa obtenerDatosEmpresa() {
        E_Empresa e = null;
        String Query = "SELECT id_empresa, razon_social, nombre_fantasia, "
                + "direccion, ruc, email, pag_web, descripcion, telefono "
                + "FROM empresa "
                + "WHERE id_empresa = 1";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                e = new E_Empresa();
                e.setDescripcion(rs.getString("descripcion"));
                e.setDireccion(rs.getString("direccion"));
                e.setEmail(rs.getString("email"));
                e.setEntidad(rs.getString("razon_social"));
                e.setId(rs.getInt("id_empresa"));
                e.setNombre(rs.getString("nombre_fantasia"));
                e.setPagWeb(rs.getString("pag_web"));
                e.setRuc(rs.getString("ruc"));
                e.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return e;
    }

    public static int modificarDatosEmpresa(E_Empresa empresa) {
        String UPDATE = "UPDATE empresa SET "
                + "razon_social=?, "
                + "nombre_fantasia=?, "
                + "direccion=?, "
                + "ruc=?, "
                + "email=?, "
                + "pag_web=?, "
                + "descripcion=?, "
                + "telefono=? "
                + "WHERE id_empresa = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setString(1, empresa.getEntidad());
            pst.setString(2, empresa.getNombre());
            try {
                if (empresa.getDireccion().isEmpty()) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, empresa.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            pst.setString(4, empresa.getRuc());
            try {
                if (empresa.getEmail().isEmpty()) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, empresa.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (empresa.getPagWeb().isEmpty()) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, empresa.getPagWeb());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (empresa.getDescripcion().isEmpty()) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, empresa.getDescripcion());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            try {
                if (empresa.getTelefono().isEmpty()) {
                    pst.setNull(8, Types.VARCHAR);
                } else {
                    pst.setString(8, empresa.getTelefono());
                }
            } catch (Exception e) {
                pst.setNull(8, Types.VARCHAR);
            }
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int recordarSeleccionImpresion(Integer tipoVenta) {
        String UPDATE = "UPDATE preferencia_general SET id_impresion_tipo=? WHERE id_preferencia_general = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            try {
                if (tipoVenta != null) {
                    pst.setInt(1, tipoVenta);
                } else {
                    pst.setNull(1, Types.INTEGER);
                }
            } catch (Exception e) {
                pst.setNull(1, Types.INTEGER);
            }
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static ArrayList<E_formaPago> obtenerFormaPagos() {
        ArrayList<E_formaPago> formaPagos = null;
        String q = "SELECT * FROM forma_pago ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            formaPagos = new ArrayList();
            while (rs.next()) {
                E_formaPago e = new E_formaPago();
                e.setId(rs.getInt("id_forma_pago"));
                e.setDescripcion(rs.getString("descripcion"));
                formaPagos.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return formaPagos;
    }

    public static ArrayList<E_Divisa> obtenerDivisas() {
        ArrayList<E_Divisa> divisas = null;
        String q = "SELECT * FROM divisa ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            divisas = new ArrayList();
            while (rs.next()) {
                E_Divisa e = new E_Divisa();
                e.setId(rs.getInt("id_divisa"));
                e.setDescripcion(rs.getString("descripcion"));
                divisas.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return divisas;
    }

    public static ArrayList<E_banco> obtenerBancos() {
        ArrayList<E_banco> bancos = null;
        String q = "SELECT * FROM banco ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            bancos = new ArrayList();
            while (rs.next()) {
                E_banco e = new E_banco();
                e.setId(rs.getInt("id_banco"));
                e.setDescripcion(rs.getString("descripcion"));
                bancos.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return bancos;
    }

    public static ArrayList<E_tipoCheque> obtenerTipoCheques() {
        ArrayList<E_tipoCheque> tipoCheques = null;
        String q = "SELECT * FROM tipo_cheque ";
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tipoCheques = new ArrayList();
            while (rs.next()) {
                E_tipoCheque e = new E_tipoCheque();
                e.setId(rs.getInt("id_tipo_cheque"));
                e.setDescripcion(rs.getString("descripcion"));
                tipoCheques.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tipoCheques;
    }

    public static void insertarBanco(String banco) {
        String insert = "INSERT INTO BANCO("
                + "DESCRIPCION"
                + ")VALUES (?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setString(1, banco);
            pst.executeUpdate();
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

}
