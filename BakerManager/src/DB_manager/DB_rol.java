/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_menu_item;
import Entities.M_rol;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro
 */
public class DB_rol {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static ResultSetTableModel consultarRoles(String busqueda) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String SELECT = "SELECT ID_ROL\"ID\",DESCRIPCION \"Descripcion\" FROM ROL"
                    + " WHERE DESCRIPCION LIKE '" + busqueda + "%'";
            String ORDER_BY = " ORDER BY DESCRIPCION ";

            String query = SELECT + ORDER_BY;
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ResultSetTableModel consultarPermisos(int idRol) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String SELECT = "SELECT M.ID_MENU \"Id\",M.DESCRIPCION \"Menu\", MI.ID_MENU_ITEM \"Id\",MI.DESCRIPCION \"Acceso\" "
                    + "FROM PERMISO_ROL PR, MENU_ITEM MI, ROL R, MENU M "
                    + "WHERE PR.ID_ROL = R.ID_ROL "
                    + "AND PR.ID_MENU = M.ID_MENU "
                    + "AND PR.ID_MENU_ITEM = MI.ID_MENU_ITEM "
                    + "AND R.ID_ROL = " + idRol + " ";
            String ORDER_BY = " ORDER BY MI.ID_MENU_ITEM ";
            String query = SELECT + ORDER_BY;
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static ArrayList<M_menu_item> obtenerAccesos(int idRol) {
        ArrayList<M_menu_item> accesos = null;
        String Q_ACCESOS = "SELECT M.ID_MENU \"idMenu\",M.DESCRIPCION \"Menu\", MI.ID_MENU_ITEM \"IdAcceso\",MI.DESCRIPCION \"Acceso\" "
                    + "FROM PERMISO_ROL PR, MENU_ITEM MI, ROL R, MENU M "
                    + "WHERE PR.ID_ROL = R.ID_ROL "
                    + "AND PR.ID_MENU = M.ID_MENU "
                    + "AND PR.ID_MENU_ITEM = MI.ID_MENU_ITEM "
                    + "AND R.ID_ROL = " + idRol + " ";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Q_ACCESOS);
            accesos = new ArrayList<>();
            while (rs.next()) {
                M_menu_item m = new M_menu_item();
                m.setIdMenu(rs.getInt("idMenu"));
                m.setMenuDescripcion(rs.getString("Menu"));
                m.setItemId(rs.getInt("IdAcceso"));
                m.setItemDescripcion(rs.getString("Acceso"));
                accesos.add(m);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return accesos;
    }

    public static boolean existeRolPrimeraVez(String nombreRol) {
        String Query = "SELECT DESCRIPCION FROM ROL "
                + "WHERE DESCRIPCION LIKE '" + nombreRol + "'; ";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static boolean existeRol(int idRol, String nombreRol) {
        String Query = "SELECT DESCRIPCION FROM ROL "
                + "WHERE DESCRIPCION LIKE '" + nombreRol + "' "
                + "AND ID_ROL != '" + idRol + "';";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static long crearRol(String nombreRol) {
        long id_producto = -1L;
        String stm = "INSERT INTO ROL(DESCRIPCION)VALUES (?);";
        try {
            pst = DB_manager.getConection().prepareStatement(stm, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombreRol);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_producto = rs.getLong(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    System.err.println("no se pudo agregar nuevo rol");
                }
            }
            System.err.println("no se pudo agregar nuevo rol");
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                System.err.println("no se pudo agregar nuevo rol");
            }
        }
        return id_producto;
    }

    public static ResultSetTableModel obtenerAccesosDisponibles() {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String query = "SELECT M.ID_MENU \"Id\", M.DESCRIPCION \"Menu\", "
                    + "MI.ID_MENU_ITEM \"Id\", MI.DESCRIPCION \"Acceso\" "
                    + "FROM MENU_ITEM MI, MENU M "
                    + "WHERE MI.ID_MENU = M.ID_MENU "
                    + "ORDER BY MI.ID_MENU_ITEM";
            //SELECT prod_id_producto   "ID producto"  ,  prod_descripcion  "Descripcion"   FROM producto
            //se crea una sentencia
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
    }

    public static long crearRol(String nombreRol, ArrayList<M_menu_item> accesos) {

        String INSERT_PERMISO_ROL = "INSERT INTO PERMISO_ROL(ID_ROL, ID_MENU, ID_MENU_ITEM)VALUES (?, ?, ?);";
        String INSERT_ROL = "INSERT INTO ROL(DESCRIPCION)VALUES (?);";
        long SQ_ROL = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_ROL, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombreRol);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                SQ_ROL = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (int i = 0; i < accesos.size(); i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_PERMISO_ROL);
                pst.setInt(1, (int) SQ_ROL);
                pst.setInt(2, (int) accesos.get(i).getIdMenu());
                pst.setInt(3, (int) accesos.get(i).getItemId());
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
                    Logger lgr = Logger.getLogger(DB_rol.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
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
                Logger lgr = Logger.getLogger(DB_rol.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return SQ_ROL;
    }

    public static M_rol obtenerRol(int idRol) {

        M_rol rol = null;
        String query = "SELECT ID_ROL, DESCRIPCION "
                + "FROM ROL "
                + "WHERE ID_ROL = " + idRol + ";";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                rol = new M_rol();
                rol.setId(rs.getInt("ID_ROL"));
                rol.setDescripcion(rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
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
                Logger lgr = Logger.getLogger(DB_rol.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return rol;
    }

    public static M_rol obtenerRol(String descripcion) {

        M_rol rol = null;
        String query = "SELECT ID_ROL, DESCRIPCION "
                + "FROM ROL "
                + "WHERE DESCRIPCION = '" + descripcion + "';";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                rol = new M_rol();
                rol.setId(rs.getInt("ID_ROL"));
                rol.setDescripcion(rs.getString("DESCRIPCION"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
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
                Logger lgr = Logger.getLogger(DB_rol.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return rol;
    }

    public static void agregarAccesoRol(int idRol, int idMenu, int idMenuItem) {
        String INSERT_PERMISO_ROL = "INSERT INTO PERMISO_ROL(ID_ROL, ID_MENU, ID_MENU_ITEM)VALUES (?, ?, ?);";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_PERMISO_ROL);
            pst.setInt(1, idRol);
            pst.setInt(2, idMenu);
            pst.setInt(3, idMenuItem);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_rol.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_rol.class.getName());
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
                Logger lgr = Logger.getLogger(DB_rol.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void quitarAccesoRol(int idRol, int idMenu, int idMenuItem) {
        String DELETE_ROL_ACCESS = "DELETE FROM PERMISO_ROL "
                + "WHERE ID_ROL = " + idRol + " "
                + "AND ID_MENU = " + idMenu + " "
                + "AND ID_MENU_ITEM = " + idMenuItem + ";";
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_ROL_ACCESS);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_rol.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_rol.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void eliminarRol(int idRol) {
        String DELETE_ROL = "DELETE FROM ROL WHERE ID_ROL = " + idRol + ";";
        String DELETE_ROL_ACCESS = "DELETE FROM PERMISO_ROL "
                + "WHERE ID_ROL = " + idRol + "; ";
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_ROL_ACCESS);
            st.close();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_ROL);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_rol.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_rol.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void modificarNombreRol(int idRol, String nombreRol) {
        String UPDATE_ROL = "UPDATE ROL SET "
                + "DESCRIPCION = '" + nombreRol + "' "
                + " WHERE ID_ROL = " + idRol + ";";
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_ROL);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_rol.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_rol.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean rolEnUso(int idRol) {
        String Query = "SELECT ID_ROL FROM ROL_USUARIO WHERE ID_ROL = " + idRol + ";";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            if (!rs.isBeforeFirst()) {
                return false;
            } else {
                return true;
            }
            /*if (rs.next()) {
             return true;
             } else {
             return false;
             }*/
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }
}
