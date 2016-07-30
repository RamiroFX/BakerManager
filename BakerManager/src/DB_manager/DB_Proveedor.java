/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_contacto;
import Entities.M_sucursal;
import Entities.M_telefono;
import Entities.M_proveedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class DB_Proveedor {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static M_proveedor obtenerDatosProveedorID(int idProveedor) {
        M_proveedor proveedor = null;
        String query = "SELECT PROV.ID_PROVEEDOR, "
                + "PROV.NOMBRE, "
                + "PROV.ENTIDAD, "
                + "PROV.RUC, "
                + "PROV.RUC_IDENTIFICADOR, "
                + "PROV.DESCRIPCION, "
                + "PROV.DIRECCION, "
                + "PROV.PAG_WEB, "
                + "PROV.EMAIL, "
                + "PROV.NOTA "
                + "FROM PROVEEDOR PROV "
                + "WHERE PROV.id_proveedor = " + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setDescripcion(rs.getString("PROV.DESCRIPCION"));
                proveedor.setDireccion(rs.getString("PROV.DIRECCION"));
                proveedor.setEmail(rs.getString("PROV.EMAIL"));
                proveedor.setPagWeb(rs.getString("PROV.PAG_WEB"));
                proveedor.setEntidad(rs.getString("PROV.ENTIDAD"));
                proveedor.setId(rs.getInt("PROV.ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("PROV.NOMBRE"));
                proveedor.setRuc(rs.getString("PROV.RUC"));
                proveedor.setRuc_id(rs.getString("PROV.RUC_IDENTIFICADOR"));
                proveedor.setObservacion(rs.getString("PROV.NOTA"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return proveedor;
    }

    public static M_proveedor obtenerDatosProveedor(String entidad) {
        M_proveedor proveedor = null;
        String query = "SELECT PROV.ID_PROVEEDOR, "
                + "PROV.NOMBRE, "
                + "PROV.ENTIDAD, "
                + "PROV.RUC, "
                + "PROV.RUC_IDENTIFICADOR, "
                + "PROV.DESCRIPCION, "
                + "PROV.DIRECCION, "
                + "PROV.PAG_WEB, "
                + "PROV.EMAIL "
                + "FROM PROVEEDOR PROV"
                + "WHERE PROV.ENTIDAD LIKE '" + entidad + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setDescripcion(rs.getString("PROV.DESCRIPCION"));
                proveedor.setDireccion(rs.getString("PROV.DIRECCION"));
                proveedor.setEmail(rs.getString("PROV.EMAIL"));
                proveedor.setEntidad(rs.getString("PROV.ENTIDAD"));
                proveedor.setId(rs.getInt("PROV.ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("PROV.NOMBRE"));
                proveedor.setRuc(rs.getString("PROV.RUC"));
                proveedor.setRuc_id(rs.getString("PROV.RUC_IDENTIFICADOR"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return proveedor;
    }

    public static M_contacto obtenerDatosContactoIdContacto(int idContacto) {
        M_contacto contacto = null;
        String query = "SELECT PERS.NOMBRE, "
                + "PERS.APELLIDO, "
                + "PERS.CI, "
                + "PERS.ID_SEXO, "
                + "(SELECT SEXO.DESCRIPCION FROM SEXO SEXO WHERE SEXO.ID_SEXO = PERS.ID_SEXO)\"SEXO\", "
                + "PERS.FECHA_NACIMIENTO, "
                + "PERS.ID_PAIS, "
                + "(SELECT PAIS.DESCRIPCION FROM PAIS PAIS WHERE PAIS.ID_PAIS =PERS.ID_PAIS)\"PAIS\","
                + "PERS.ID_ESTADO_CIVIL, "
                + "(SELECT ESCI.DESCRIPCION FROM ESTADO_CIVIL ESCI WHERE ESCI.ID_ESTADO_CIVIL =PERS.ID_ESTADO_CIVIL)\"ESTADO_CIVIL\","
                + "PERS.ID_CIUDAD, "
                + "(SELECT CIUD.DESCRIPCION FROM CIUDAD CIU DWHERE CIUD.ID_CIUDAD =PERS.ID_CIUDAD)\"CIUDAD\","
                + "PRCO.ID_PERSONA, "
                + "PRCO.EMAIL, "
                + "PRCO.DIRECCION, "
                + "PRCO.OBSERVACION, "
                + "PRCO.ID_PROVEEDOR, "
                + "PRCO.TELEFONO, "
                + "PRCO.ID_PROVEEDOR_CONTACTO, "
                + "PERS.ID_PERSONA "
                + "FROM PROVEEDOR_CONTACTO PRCO, PERSONA PERS "
                + "WHERE PRCO.ID_PERSONA = PERS.ID_PERSONA "
                + "AND PRCO.ID_PROVEEDOR_CONTACTO = " + idContacto;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                contacto = new M_contacto();
                contacto.setApellido(rs.getString("PERS.APELLIDO"));
                contacto.setCedula(rs.getInt("PERS.CI"));
                contacto.setDireccion(rs.getString("PRCO.DIRECCION"));
                contacto.setEmail(rs.getString("PRCO.EMAIL"));
                contacto.setFecha_nacimiento(rs.getDate("PERS.FECHA_NACIMIENTO"));
                contacto.setId_ciudad(rs.getInt("PERS.ID_CIUDAD"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setId_contacto(rs.getInt("PRCO.ID_PROVEEDOR_CONTACTO"));
                contacto.setId_estado_civil(rs.getInt("PERS.ID_ESTADO_CIVIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("PERS.ID_PAIS"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setId_persona(rs.getInt("PRCO.ID_PERSONA"));
                contacto.setId_proveedor(rs.getInt("PRCO.ID_PROVEEDOR"));
                contacto.setId_sexo(rs.getInt("PERS.ID_SEXO"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setNombre(rs.getString("PERS.NOMBRE"));
                contacto.setObservacion(rs.getString("PRCO.OBSERVACION"));
                contacto.setTelefono(rs.getString("PRCO.TELEFONO"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return contacto;
    }

    public static ResultSetTableModel consultarProveedor(String proveedor, boolean entidad, boolean ruc, boolean isExclusivo) {
        ResultSetTableModel rstm = null;
        String select = "SELECT PROV.ID_PROVEEDOR \"ID\",PROV.ENTIDAD \"Entidad\",PROV.NOMBRE \"Nombre proveedor\",PROV.RUC || '-' || PROV.RUC_IDENTIFICADOR \"R.U.C.\" ";
        String from = "FROM PROVEEDOR PROV ";
        String where = "WHERE ";
        String orderBy = "ORDER BY PROV.ENTIDAD ";
        String prov;
        if (isExclusivo) {
            prov = proveedor + "%";
        } else {
            prov = "%" + proveedor + "%";
        }
        if (entidad && ruc) {
            where = where + "(LOWER(PROV.NOMBRE) LIKE '" + prov + "' OR LOWER(PROV.ENTIDAD) LIKE '" + prov + "') OR LOWER(PROV.RUC) LIKE '" + prov + "'";
        } else if (entidad) {
            where = where + "(LOWER(PROV.NOMBRE) LIKE '" + prov + "' OR LOWER(PROV.ENTIDAD) LIKE '" + prov + "') ";
        } else if (ruc) {
            where = where + "LOWER(PROV.RUC) LIKE '" + prov + "' ";
        } else if (!entidad && !ruc) {
            where = where + "(LOWER(PROV.NOMBRE) LIKE '" + prov + "' OR LOWER(PROV.ENTIDAD) LIKE '" + prov + "') OR LOWER(PROV.RUC) LIKE '" + prov + "'";
        }
        String query = select + from + where + orderBy;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return rstm;
    }

    public static ResultSetTableModel obtenerProveedorContacto(int idProveedor) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT "
                + "PRCO.ID_PROVEEDOR_CONTACTO \"ID\", "
                + "PERS.NOMBRE \"Nombre\", "
                + "PERS.APELLIDO \"Apellido\", "
                + "PRCO.TELEFONO \"Telefono\", "
                + "PRCO.EMAIL \"E-mail\" "
                + "FROM  PERSONA PERS, PROVEEDOR PROV, PROVEEDOR_CONTACTO PRCO "
                + "WHERE PERS.ID_PERSONA = PRCO.ID_PERSONA "
                + "AND PRCO.ID_PROVEEDOR = PROV.ID_PROVEEDOR "
                + "AND PROV.ID_PROVEEDOR =" + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerSucursal(int idProveedor) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT PRSU.ID_PROVEEDOR_SUCURSAL \"ID\", "
                + "PRSU.DIRECCION \"Dirección\", "
                + "PRSU.TELEFONO \"Telefono\" "
                + "FROM PROVEEDOR_SUCURSAL PRSU "
                + "WHERE PRSU.ID_PROVEEDOR =" + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerProveedorTelefono(int idProveedor) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT TELE.NUMERO \"Número\", "
                + "TELE.CATEGORIA \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, PROVEEDOR PROV, PROVEEDOR_TELEFONO PRTE"
                + "WHERE TELE.ID_TELEFONO = PRTE.ID_TELEFONO "
                + "AND PROV.ID_PROVEEDOR = PRTE.ID_PROVEEDOR "
                + "AND PROV.ID_PROVEEDOR = " + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel obtenerProveedorTelefonoCompleto(int idProveedor) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT TELE.ID_TELEFONO \"ID\", "
                + "TELE.NUMERO \"Número\", "
                + "TELE.CATEGORIA \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, PROVEEDOR PROV, PROVEEDOR_TELEFONO PRTE"
                + "WHERE TELE.ID_TELEFONO = PRTE.ID_TELEFONO "
                + "AND PROV.ID_PROVEEDOR = PRTE.ID_PROVEEDOR "
                + "AND PROV.ID_PROVEEDOR = " + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static Vector obtenerProveedores() {
        Vector proveedor = null;
        String q = "SELECT PROV.ENTIDAD "
                + "FROM PROVEEDOR PROV ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            proveedor = new Vector();
            while (rs.next()) {
                proveedor.add(rs.getString("PROV.ENTIDAD"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return proveedor;
    }

    public static Vector obtenerEmpleados() {
        Vector proveedor = null;
        String q = "SELECT FUNC.ID_FUNCIONARIO|| '-'|| PERS.NOMBRE || ' '|| PERS.APELLIDO \"Empleado\" FROM PERSONA PERS, FUNCIONARIO FUNC WHERE PERS.ID_PERSONA = FUNC.ID_PERSONA ";
        try {
            System.out.println("q: " + q);
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            proveedor = new Vector();
            while (rs.next()) {
                proveedor.add(rs.getString("Empleado"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (rs != null) {
         rs.close();
         }
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return proveedor;
    }

    /*
     * falta refacturiuzar
     */
    public static void insertarProveedor(M_proveedor proveedor, M_sucursal[] sucursal, M_telefono[] telefono, ArrayList<M_contacto> contactos) {
        ArrayList<Long> id_telefono = new ArrayList();
        ArrayList<Long> id_persona = new ArrayList();
        long id_proveedor = -1L;
        String INSERT_PROVEEDOR = "INSERT INTO PROVEEDOR(PROV.NOMBRE, PROV.ENTIDAD, PROV.RUC, PROV.RUC_IDENTIFICADOR, PROV.DESCRIPCION, PROV.DIRECCION,PROV.PAG_WEB, PROV.EMAIL, PROV.NOTA)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(TELE.NUMERO, TELE.CATEGORIA, TELE.OBSERVACION) VALUES (?, ?, ?)";
        String INSERT_PROVEEDOR_TELEFONO = "INSERT INTO PROVEEDOR_TELEFONO(PRTE.ID_PROVEEDOR, PRTE.ID_TELEFONO) VALUES (?, ?)";
        String INSERT_PERSONA = "INSERT INTO PERSONA(PERS.CI, PERS.NOMBRE, PERS.APELLIDO, PERS.ID_SEXO, PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(PRCO.ID_PERSONA, PRCO.ID_PROVEEDOR, PRCO.EMAIL, PRCO.DIRECCION, PRCO.TELEFONO, PRCO.OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        String INSERT_SUCURSAL = "INSERT INTO PROVEEDOR_SUCURSAL(PRSU.ID_PROVEEDOR, PRSU.DIRECCION, PRSU.TELEFONO)VALUES (?, ?, ?)";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_PROVEEDOR, Statement.RETURN_GENERATED_KEYS);
            try {
                if (proveedor.getNombre() == null) {
                    pst.setNull(1, Types.VARCHAR);
                } else {
                    pst.setString(1, proveedor.getNombre());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.VARCHAR);
            }
            try {
                if (proveedor.getEntidad() == null) {
                    pst.setNull(2, Types.VARCHAR);
                } else {
                    pst.setString(2, proveedor.getEntidad());
                }
            } catch (Exception e) {
                pst.setNull(2, Types.VARCHAR);
            }
            try {
                if (proveedor.getRuc() == null) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, proveedor.getRuc());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            try {
                if (proveedor.getRuc_id() == null) {
                    pst.setNull(4, Types.VARCHAR);
                } else {
                    pst.setString(4, proveedor.getRuc_id());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.VARCHAR);
            }
            try {
                if (proveedor.getDescripcion() == null) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, proveedor.getDescripcion());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (proveedor.getDireccion() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, proveedor.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (proveedor.getPagWeb() == null) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, proveedor.getPagWeb());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            try {
                if (proveedor.getEmail() == null) {
                    pst.setNull(8, Types.VARCHAR);
                } else {
                    pst.setString(8, proveedor.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(8, Types.VARCHAR);
            }
            try {
                if (proveedor.getObservacion() == null) {
                    pst.setNull(9, Types.VARCHAR);
                } else {
                    pst.setString(9, proveedor.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(9, Types.VARCHAR);
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_proveedor = rs.getLong(1);
            }
            rs.close();
            pst.close();
            if (telefono.length > 0) {
                for (int i = 0; i < telefono.length; i++) {
                    pst = DB_manager.getConection().prepareStatement(INSERT_TELEFONO, PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, telefono[i].getNumero());//not null
                    try {
                        if (telefono[i].getCategoria() == null) {
                            pst.setNull(2, Types.VARCHAR);
                        } else {
                            pst.setString(2, telefono[i].getCategoria());
                        }
                    } catch (Exception e) {
                        pst.setNull(2, Types.VARCHAR);
                    }
                    try {
                        if (telefono[i].getObservacion() == null) {
                            pst.setNull(3, Types.VARCHAR);
                        } else {
                            pst.setString(3, telefono[i].getObservacion());
                        }
                    } catch (Exception e) {
                        pst.setNull(3, Types.VARCHAR);
                    }
                    pst.executeUpdate();
                    rs = pst.getGeneratedKeys();
                    if (rs != null && rs.next()) {
                        id_telefono.add(rs.getLong(1));
                    }
                    rs.close();
                    pst.close();
                }
                for (int i = 0; i < telefono.length; i++) {
                    pst = DB_manager.getConection().prepareStatement(INSERT_PROVEEDOR_TELEFONO);
                    pst.setInt(1, (int) id_proveedor);
                    pst.setInt(2, (int) id_telefono.get(i).longValue());
                    pst.executeUpdate();
                    pst.close();
                }
            }
            for (int i = 0; i < sucursal.length; i++) {
                pst = DB_manager.getConection().prepareStatement(INSERT_SUCURSAL);
                pst.setInt(1, (int) id_proveedor);
                try {
                    if (sucursal[i].getDireccion() == null) {
                        pst.setNull(2, Types.VARCHAR);
                    } else {
                        pst.setString(2, sucursal[i].getDireccion());
                    }
                } catch (Exception e) {
                    pst.setNull(2, Types.VARCHAR);
                }
                try {
                    if (sucursal[i].getTelefono() == null) {
                        pst.setNull(3, Types.VARCHAR);
                    } else {
                        pst.setString(3, sucursal[i].getTelefono());
                    }
                } catch (Exception e) {
                    pst.setNull(3, Types.VARCHAR);
                }
                pst.executeUpdate();
                pst.close();
            }
            if (contactos.size() > 0) {
                for (int i = 0; i < contactos.size(); i++) {
                    pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA, PreparedStatement.RETURN_GENERATED_KEYS);
                    try {
                        if (contactos.get(i).getCedula() == null) {
                            pst.setNull(1, Types.INTEGER);
                        } else {
                            pst.setInt(1, (int) contactos.get(i).getCedula());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pst.setNull(1, Types.INTEGER);
                    }
                    pst.setString(2, contactos.get(i).getNombre());
                    pst.setString(3, contactos.get(i).getApellido());
                    pst.setInt(4, contactos.get(i).getId_sexo());
                    try {
                        if (contactos.get(i).getFecha_nacimiento() == null) {
                            pst.setNull(5, Types.DATE);
                        } else {
                            pst.setDate(5, new java.sql.Date(contactos.get(i).getFecha_nacimiento().getTime()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pst.setNull(5, Types.DATE);
                    }
                    pst.setInt(6, contactos.get(i).getId_estado_civil());
                    pst.setInt(7, contactos.get(i).getId_pais());
                    pst.setInt(8, contactos.get(i).getId_ciudad());
                    pst.executeUpdate();
                    rs = pst.getGeneratedKeys();
                    if (rs != null && rs.next()) {
                        id_persona.add(rs.getLong(1));
                    }
                    pst.close();
                    rs.close();
                }
                for (int i = 0; i < contactos.size(); i++) {
                    pst = DB_manager.getConection().prepareStatement(INSERT_PROVEEDOR_CONTACTO);
                    int idPersona = Integer.valueOf(String.valueOf(id_persona.get(i)));
                    pst.setInt(1, idPersona);
                    pst.setInt(2, (int) id_proveedor);
                    pst.setString(3, contactos.get(i).getEmail());
                    pst.setString(4, contactos.get(i).getDireccion());
                    pst.setString(5, contactos.get(i).getTelefono());
                    pst.setString(6, contactos.get(i).getObservacion());
                    pst.executeUpdate();
                    pst.close();
                }
            }
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarProveedorContacto(Integer idProveedor, M_contacto contacto) {
        long id_persona = -1L;
        String INSERT_PERSONA = "INSERT INTO PERSONA(PERS.CI, PERS.NOMBRE, PERS.APELLIDO, PERS.ID_SEXO, PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(PRCO.ID_PERSONA, PRCO.ID_PROVEEDOR, PRCO.EMAIL, PRCO.DIRECCION, PRCO.TELEFONO, PRCO.OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA, PreparedStatement.RETURN_GENERATED_KEYS);
            //PERS.CI, PERS.NOMBRE, PERS.APELLIDO, PERS.ID_SEXO, PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD
            try {
                if (contacto.getCedula() == null) {
                    pst.setNull(1, Types.INTEGER);
                } else {
                    pst.setInt(1, (int) contacto.getCedula());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.INTEGER);
            }
            pst.setString(2, contacto.getNombre());
            pst.setString(3, contacto.getApellido());
            pst.setInt(4, contacto.getId_sexo());
            try {
                if (contacto.getFecha_nacimiento() == null) {
                    pst.setNull(5, Types.DATE);
                } else {
                    pst.setDate(5, new java.sql.Date(contacto.getFecha_nacimiento().getTime()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                pst.setNull(5, Types.DATE);
            }
            pst.setInt(6, contacto.getId_estado_civil());
            pst.setInt(7, contacto.getId_pais());
            pst.setInt(8, contacto.getId_ciudad());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_persona = rs.getLong(1);
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(INSERT_PROVEEDOR_CONTACTO);
            pst.setInt(1, (int) id_persona);
            pst.setInt(2, idProveedor);
            try {
                if (contacto.getEmail() == null) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, contacto.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            try {
                if (contacto.getDireccion() == null) {
                    pst.setNull(4, Types.VARCHAR);
                } else {
                    pst.setString(4, contacto.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.VARCHAR);
            }
            try {
                if (contacto.getTelefono() == null) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, contacto.getTelefono());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (contacto.getObservacion() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, contacto.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarSucursal(Integer idProveedor, String direccion, String telefono) {
        String q = "INSERT INTO PROVEEDOR_SUCURSAL("
                + "PRSU.ID_PROVEEDOR, "
                + "PRSU.DIRECCION, "
                + "PRSU.TELEFONO)"
                + "VALUES ("
                + idProveedor + ", '"
                + direccion + "', '"
                + telefono + "')";
        try {
            DB_manager.habilitarTransaccionManual();
            PreparedStatement pstmt = DB_manager.getConection().prepareStatement(q);
            pstmt.executeUpdate();
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();


         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();


         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarTelefono(Integer idProveedor, String tipoTelefono, String nroTelefono, String observacion) {
        long id_telefono = -1L;
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(TELE.NUMERO, TELE.CATEGORIA, TELE.OBSERVACION)VALUES (?, ?, ?)";
        String INSERT_TELEFONO_PROVEEDOR = "INSERT INTO PROVEEDOR_TELEFONO(PRTE.ID_PROVEEDOR, PRTE.ID_TELEFONO)VALUES (?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(INSERT_TELEFONO, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, nroTelefono);
            pst.setString(2, tipoTelefono);
            pst.setString(3, observacion);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_telefono = rs.getLong(1);
            }
            pst.close();
            rs.close();
            pst = DB_manager.getConection().prepareStatement(INSERT_TELEFONO_PROVEEDOR);
            pst.setInt(1, idProveedor);
            pst.setInt(2, (int) id_telefono);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();


         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();


         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarMarca(String marca) {
        String insert = "INSERT INTO MARCA("
                + "DESCRIPCION"
                + ")VALUES (?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setString(1, marca);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarImpuesto(String impuesto) {
        String insert = "INSERT INTO IMPUESTO("
                + "DESCRIPCION"
                + ")VALUES ('"
                + impuesto + "')";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void insertarRubro(String rubro) {
        String insert = "INSERT INTO RUBRO("
                + "DESCRIPCION"
                + ")VALUES ('"
                + rubro + "')";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    /*
     * falta refacturiuzar
     */
    public static void insertarProveedorProducto(int idProveedor, int idProdcuto) {
        String query = "INSERT INTO PROVEEDOR_PRODUCTO("
                + "ID_PROVEEDOR, "
                + "ID_PRODUCTO)"
                + "VALUES ("
                + idProveedor + ", "
                + idProdcuto + ")";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(query);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }
    /*
     * falta refacturiuzar
     */

    public static void modificarProveedor(M_proveedor proveedor) {
        String updateProveedor = "UPDATE PROVEEDOR PROV SET "
                + "PROV.ID_PROVEEDOR=" + proveedor.getId() + ", "
                + "PROV.NOMBRE='" + proveedor.getNombre() + "', "
                + "PROV.ENTIDAD='" + proveedor.getEntidad() + "', "
                + "PROV.RUC='" + proveedor.getRuc() + "', "
                + "PROV.RUC_IDENTIFICADOR='" + proveedor.getRuc_id() + "', "
                + "PROV.DESCRIPCION='" + proveedor.getDescripcion() + "', "
                + "PROV.DIRECCION='" + proveedor.getDireccion() + "', "
                + "PROV.PAG_WEB='" + proveedor.getPagWeb() + "', "
                + "PROV.EMAIL='" + proveedor.getEmail() + "', "
                + "PROV.NOTA='" + proveedor.getObservacion() + "' "
                + "WHERE PROV.ID_PROVEEDOR = " + proveedor.getId();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateProveedor);
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
        } /*finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (pst != null) {
         pst.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void modificarProveedorContacto(Integer id, M_contacto contacto) {
        try {
            DB_manager.habilitarTransaccionManual();
            String updateContacto = "UPDATE PROVEEDOR_CONTACTO PRCO SET "
                    + "PRCO.EMAIL='" + contacto.getEmail() + "', "
                    + "PRCO.DIRECCION='" + contacto.getDireccion() + "', "
                    + "PRCO.OBSERVACION='" + contacto.getObservacion() + "', "
                    + "PRCO.TELEFONO='" + contacto.getTelefono() + "' "
                    + "WHERE PRCO.ID_PROVEEDOR_CONTACTO = " + contacto.getId_contacto();
            String updatePersona = "UPDATE PERSONA SET "
                    + "PERS.CI=" + contacto.getCedula() + ", "
                    + "PERS.NOMBRE='" + contacto.getNombre() + "', "
                    + "PERS.APELLIDO='" + contacto.getApellido() + "', "
                    + "PERS.ID_SEXO=" + contacto.getId_sexo() + ", "
                    + "PERS.FECHA_NACIMIENTO='" + contacto.getFecha_nacimiento() + "', "
                    + "PERS.ID_ESTADO_CIVIL=" + contacto.getId_estado_civil() + ", "
                    + "PERS.ID_PAIS=" + contacto.getId_pais() + ", "
                    + "PERS.ID_CIUDAD=" + contacto.getId_ciudad() + " "
                    + "WHERE PERS.ID_PERSONA = " + contacto.getId_persona();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updatePersona);
            st.close();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateContacto);
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

    public static void modificarSucursal(int idSucursal, String direccion, String telefono) {
        String q = "UPDATE PROVEEDOR_SUCURSAL SET "
                + "DIRECCION='" + direccion + "', "
                + "TELEFONO='" + telefono + "' "
                + "WHERE ID_PROVEEDOR_SUCURSAL = " + idSucursal;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(q);
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
        } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void modificarTelefono(int idTelefono, String tipoTelefono, String nroTelefono, String observacion) {
        String updateTelefono = "UPDATE TELEFONO SET "
                + "NUMERO='" + nroTelefono + "', "
                + "CATEGORIA= '" + tipoTelefono + "', "
                + "OBSERVACION='" + observacion + "' "
                + "WHERE ID_TELEFONO = " + idTelefono;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateTelefono);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
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
        } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void modificarMarca(int idMarca, String descripcion) {
        String updateMarca = "UPDATE MARCA SET "
                + "DESCRIPCION= '" + descripcion + "' "
                + "WHERE ID_MARCA =" + idMarca;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateMarca);
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
        } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void modificarImpuesto(int idImpuesto, String descripcion) {
        String updateImpuesto = "UPDATE IMPUESTO SET "
                + "DESCRIPCION= " + descripcion + " "
                + "WHERE ID_IMPUESTO =" + idImpuesto;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateImpuesto);
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
        } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         if (rs != null) {
         rs.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void modificarRubro(int idRubro, String descripcion) {
        String updateMarca = "UPDATE RUBRO SET "
                + "DESCRIPCION= '" + descripcion + "' "
                + "WHERE ID_RUBRO =" + idRubro;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateMarca);
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
        } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarTelefonoProveedor(int id_telefono) {
        String q = "DELETE FROM TELEFONO WHERE ID_TELEFONO =" + id_telefono;
        String q2 = "DELETE FROM PROVEEDOR_TELEFONO WHERE ID_TELEFONO =" + id_telefono;
        System.out.println("SQL: " + q);
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(q);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(q2);
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }/**/

    }

    public static void eliminarSucursal(int id_sucursal) {
        String q = "DELETE FROM PROVEEDOR_SUCURSAL WHERE ID_PROVEEDOR_SUCURSAL =" + id_sucursal;
        System.out.println("SQL: " + q);
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(q);
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarMarca(int id_marca) {
        String delete = "DELETE FROM MARCA WHERE ID_MARCA =" + id_marca;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarImpuesto(int idImpuesto) {
        String delete = "DELETE FROM IMPUESTO WHERE ID_IMPUESTO =" + idImpuesto;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
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
        }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (st != null) {
         st.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarRubro(int idRubro) {
        String delete = "DELETE FROM RUBRO WHERE ID_RUBRO =" + idRubro;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarProveedorProducto(Integer idProveedor, Integer idProducto) {
        String q = "DELETE FROM PROVEEDOR_PRODUCTO "
                + "WHERE ID_PROVEEDOR = " + idProveedor
                + " AND ID_PRODUCTO =" + idProducto;
        System.out.println("SQL: " + q);
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(q);
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
        }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }/* finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }

    public static void eliminarProveedorContacto(M_contacto contacto) {
        String delete = "DELETE FROM PROVEEDOR_CONTACTO WHERE ID_PROVEEDOR_CONTACTO =" + contacto.getId_contacto();
        String DELETE_PERSONA = "DELETE FROM PERSONA WHERE ID_PERSONA = " + contacto.getId_persona();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(DELETE_PERSONA);
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
        } /*finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         } /*finally {
         try {
         if (pst != null) {
         pst.close();
         }
         } catch (SQLException ex) {
         Logger lgr = Logger.getLogger(DB_Proveedor.class
         .getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

    }
}
