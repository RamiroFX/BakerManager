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
        String query = "SELECT PROV_ID_PROVEEDOR, "
                + "PROV_NOMBRE, "
                + "PROV_ENTIDAD, "
                + "PROV_RUC, "
                + "PROV_RUC_IDENTIFICADOR, "
                + "PROV_DESCRIPCION, "
                + "PROV_DIRECCION, "
                + "PROV_PAG_WEB, "
                + "PROV_EMAIL, "
                + "PROV_NOTA "
                + "FROM PROVEEDOR "
                + "WHERE prov_id_proveedor = " + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setDescripcion(rs.getString("PROV_DESCRIPCION"));
                proveedor.setDireccion(rs.getString("PROV_DIRECCION"));
                proveedor.setEmail(rs.getString("PROV_EMAIL"));
                proveedor.setPagWeb(rs.getString("PROV_PAG_WEB"));
                proveedor.setEntidad(rs.getString("PROV_ENTIDAD"));
                proveedor.setId(rs.getInt("PROV_ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("PROV_NOMBRE"));
                proveedor.setRuc(rs.getString("PROV_RUC"));
                proveedor.setRuc_id(rs.getString("PROV_RUC_IDENTIFICADOR"));
                proveedor.setObservacion(rs.getString("PROV_NOTA"));
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
        String query = "SELECT PROV_ID_PROVEEDOR, "
                + "PROV_NOMBRE, "
                + "PROV_ENTIDAD, "
                + "PROV_RUC, "
                + "PROV_RUC_IDENTIFICADOR, "
                + "PROV_DESCRIPCION, "
                + "PROV_DIRECCION, "
                + "PROV_PAG_WEB, "
                + "PROV_EMAIL "
                + "FROM PROVEEDOR "
                + "WHERE PROV_ENTIDAD LIKE '" + entidad + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setDescripcion(rs.getString("PROV_DESCRIPCION"));
                proveedor.setDireccion(rs.getString("PROV_DIRECCION"));
                proveedor.setEmail(rs.getString("PROV_EMAIL"));
                proveedor.setEntidad(rs.getString("PROV_ENTIDAD"));
                proveedor.setId(rs.getInt("PROV_ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("PROV_NOMBRE"));
                proveedor.setRuc(rs.getString("PROV_RUC"));
                proveedor.setRuc_id(rs.getString("PROV_RUC_IDENTIFICADOR"));
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
        String query = "SELECT PERS_NOMBRE, "
                + "PERS_APELLIDO, "
                + "PERS_CI, "
                + "PERS_ID_SEXO, "
                + "(SELECT SEXO_DESCRIPCION FROM SEXO WHERE SEXO_ID_SEXO = PERS_ID_SEXO)\"SEXO\", "
                + "PERS_FECHA_NACIMIENTO, "
                + "PERS_ID_PAIS, "
                + "(SELECT PAIS_DESCRIPCION FROM PAIS WHERE PAIS_ID_PAIS =PERS_ID_PAIS)\"PAIS\","
                + "PERS_ID_ESTADO_CIVIL, "
                + "(SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL =PERS_ID_ESTADO_CIVIL)\"ESTADO_CIVIL\","
                + "PERS_ID_CIUDAD, "
                + "(SELECT CIUD_DESCRIPCION FROM CIUDAD WHERE CIUD_ID_CIUDAD =PERS_ID_CIUDAD)\"CIUDAD\","
                + "PRCO_ID_PERSONA, "
                + "PRCO_EMAIL, "
                + "PRCO_DIRECCION, "
                + "PRCO_OBSERVACION, "
                + "PRCO_ID_PROVEEDOR, "
                + "PRCO_TELEFONO, "
                + "PRCO_ID_PROVEEDOR_CONTACTO, "
                + "PERS_ID_PERSONA "
                + "FROM PROVEEDOR_CONTACTO, PERSONA "
                + "WHERE PRCO_ID_PERSONA = PERS_ID_PERSONA "
                + "AND PRCO_ID_PROVEEDOR_CONTACTO = " + idContacto;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                contacto = new M_contacto();
                contacto.setApellido(rs.getString("PERS_APELLIDO"));
                contacto.setCedula(rs.getInt("PERS_CI"));
                contacto.setDireccion(rs.getString("PRCO_DIRECCION"));
                contacto.setEmail(rs.getString("PRCO_EMAIL"));
                contacto.setFecha_nacimiento(rs.getDate("PERS_FECHA_NACIMIENTO"));
                contacto.setId_ciudad(rs.getInt("PERS_ID_CIUDAD"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setId_contacto(rs.getInt("PRCO_ID_PROVEEDOR_CONTACTO"));
                contacto.setId_estado_civil(rs.getInt("PERS_ID_ESTADO_CIVIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("PERS_ID_PAIS"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setId_persona(rs.getInt("PRCO_ID_PERSONA"));
                contacto.setId_proveedor(rs.getInt("PRCO_ID_PROVEEDOR"));
                contacto.setId_sexo(rs.getInt("PERS_ID_SEXO"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setNombre(rs.getString("PERS_NOMBRE"));
                contacto.setObservacion(rs.getString("PRCO_OBSERVACION"));
                contacto.setTelefono(rs.getString("PRCO_TELEFONO"));
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
        String select = "SELECT PROV_ID_PROVEEDOR \"ID\",PROV_ENTIDAD \"Entidad\",PROV_NOMBRE \"Nombre proveedor\",PROV_RUC || '-' || PROV_RUC_IDENTIFICADOR \"R.U.C.\" ";
        String from = "FROM PROVEEDOR ";
        String where = "WHERE ";
        String orderBy = "ORDER BY PROV_ENTIDAD ";
        String prov;
        if (isExclusivo) {
            prov = proveedor + "%";
        } else {
            prov = "%" + proveedor + "%";
        }
        if (entidad && ruc) {
            where = where + "(LOWER(PROV_NOMBRE) LIKE '" + prov + "' OR LOWER(PROV_ENTIDAD) LIKE '" + prov + "') OR LOWER(PROV_RUC) LIKE '" + prov + "'";
        } else if (entidad) {
            where = where + "(LOWER(PROV_NOMBRE) LIKE '" + prov + "' OR LOWER(PROV_ENTIDAD) LIKE '" + prov + "') ";
        } else if (ruc) {
            where = where + "LOWER(PROV_RUC) LIKE '" + prov + "' ";
        } else if (!entidad && !ruc) {
            where = where + "(LOWER(PROV_NOMBRE) LIKE '" + prov + "' OR LOWER(PROV_ENTIDAD) LIKE '" + prov + "') OR LOWER(PROV_RUC) LIKE '" + prov + "'";
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
                + "PRCO_ID_PROVEEDOR_CONTACTO \"ID\", "
                + "PERS_NOMBRE \"Nombre\", "
                + "PERS_APELLIDO \"Apellido\", "
                + "PRCO_TELEFONO \"Telefono\", "
                + "PRCO_EMAIL \"E-mail\" "
                + "FROM  PERSONA, PROVEEDOR, PROVEEDOR_CONTACTO "
                + "WHERE PERS_ID_PERSONA = PRCO_ID_PERSONA "
                + "AND PRCO_ID_PROVEEDOR = PROV_ID_PROVEEDOR "
                + "AND PROV_ID_PROVEEDOR =" + idProveedor;
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
        String Query = "SELECT PRSU_ID_PROVEEDOR_SUCURSAL \"ID\", "
                + "PRSU_DIRECCION \"Dirección\", "
                + "PRSU_TELEFONO \"Telefono\" "
                + "FROM PROVEEDOR_SUCURSAL "
                + "WHERE PRSU_ID_PROVEEDOR =" + idProveedor;
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
        String Query = "SELECT TELE_NUMERO \"Número\", "
                + "TELE_CATEGORIA \"Categoría\", "
                + "TELE_OBSERVACION \"Observación\" "
                + "FROM TELEFONO, PROVEEDOR, PROVEEDOR_TELEFONO "
                + "WHERE TELE_ID_TELEFONO = PRTE_ID_TELEFONO "
                + "AND PROV_ID_PROVEEDOR = PRTE_ID_PROVEEDOR "
                + "AND PROV_ID_PROVEEDOR = " + idProveedor;
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
        String Query = "SELECT TELE_ID_TELEFONO \"ID\", "
                + "TELE_NUMERO \"Número\", "
                + "TELE_CATEGORIA \"Categoría\", "
                + "TELE_OBSERVACION \"Observación\" "
                + "FROM TELEFONO, PROVEEDOR, PROVEEDOR_TELEFONO "
                + "WHERE TELE_ID_TELEFONO = PRTE_ID_TELEFONO "
                + "AND PROV_ID_PROVEEDOR = PRTE_ID_PROVEEDOR "
                + "AND PROV_ID_PROVEEDOR = " + idProveedor;
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
        String q = "SELECT PROV_ENTIDAD "
                + "FROM PROVEEDOR ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            proveedor = new Vector();
            while (rs.next()) {
                proveedor.add(rs.getString("PROV_ENTIDAD"));
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
        String q = "SELECT FUNC_ID_FUNCIONARIO|| '-'|| PERS_NOMBRE || ' '|| PERS_APELLIDO \"Empleado\" FROM PERSONA, FUNCIONARIO WHERE PERS_ID_PERSONA = FUNC_ID_PERSONA ";
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
        String INSERT_PROVEEDOR = "INSERT INTO PROVEEDOR(PROV_NOMBRE, PROV_ENTIDAD, PROV_RUC, PROV_RUC_IDENTIFICADOR, PROV_DESCRIPCION, PROV_DIRECCION,PROV_PAG_WEB, PROV_EMAIL, PROV_NOTA)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(TELE_NUMERO, TELE_CATEGORIA, TELE_OBSERVACION) VALUES (?, ?, ?)";
        String INSERT_PROVEEDOR_TELEFONO = "INSERT INTO PROVEEDOR_TELEFONO(PRTE_ID_PROVEEDOR, PRTE_ID_TELEFONO) VALUES (?, ?)";
        String INSERT_PERSONA = "INSERT INTO PERSONA(PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(PRCO_ID_PERSONA, PRCO_ID_PROVEEDOR, PRCO_EMAIL, PRCO_DIRECCION, PRCO_TELEFONO, PRCO_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        String INSERT_SUCURSAL = "INSERT INTO PROVEEDOR_SUCURSAL(PRSU_ID_PROVEEDOR, PRSU_DIRECCION, PRSU_TELEFONO)VALUES (?, ?, ?)";
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
        String INSERT_PERSONA = "INSERT INTO PERSONA(PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(PRCO_ID_PERSONA, PRCO_ID_PROVEEDOR, PRCO_EMAIL, PRCO_DIRECCION, PRCO_TELEFONO, PRCO_OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA, PreparedStatement.RETURN_GENERATED_KEYS);
            //PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD
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
                + "PRSU_ID_PROVEEDOR, "
                + "PRSU_DIRECCION, "
                + "PRSU_TELEFONO)"
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
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(TELE_NUMERO, TELE_CATEGORIA, TELE_OBSERVACION)VALUES (?, ?, ?)";
        String INSERT_TELEFONO_PROVEEDOR = "INSERT INTO PROVEEDOR_TELEFONO(PRTE_ID_PROVEEDOR, PRTE_ID_TELEFONO)VALUES (?, ?)";
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
                + "MARC_DESCRIPCION"
                + ")VALUES ('"
                + marca + "')";
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

    public static void insertarImpuesto(String impuesto) {
        String insert = "INSERT INTO IMPUESTO("
                + "IMPU_DESCRIPCION"
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
                + "RUBR_DESCRIPCION"
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
                + "PRPR_ID_PROVEEDOR, "
                + "PRPR_ID_PRODUCTO)"
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
        String updateProveedor = "UPDATE PROVEEDOR SET "
                + "PROV_ID_PROVEEDOR=" + proveedor.getId() + ", "
                + "PROV_NOMBRE='" + proveedor.getNombre() + "', "
                + "PROV_ENTIDAD='" + proveedor.getEntidad() + "', "
                + "PROV_RUC='" + proveedor.getRuc() + "', "
                + "PROV_RUC_IDENTIFICADOR='" + proveedor.getRuc_id() + "', "
                + "PROV_DESCRIPCION='" + proveedor.getDescripcion() + "', "
                + "PROV_DIRECCION='" + proveedor.getDireccion() + "', "
                + "PROV_PAG_WEB='" + proveedor.getPagWeb() + "', "
                + "PROV_EMAIL='" + proveedor.getEmail() + "', "
                + "PROV_NOTA='" + proveedor.getObservacion() + "' "
                + "WHERE PROV_ID_PROVEEDOR = " + proveedor.getId();
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
            String updateContacto = "UPDATE PROVEEDOR_CONTACTO SET "
                    + "PRCO_EMAIL='" + contacto.getEmail() + "', "
                    + "PRCO_DIRECCION='" + contacto.getDireccion() + "', "
                    + "PRCO_OBSERVACION='" + contacto.getObservacion() + "', "
                    + "PRCO_TELEFONO='" + contacto.getTelefono() + "' "
                    + "WHERE PRCO_ID_PROVEEDOR_CONTACTO = " + contacto.getId_contacto();
            String updatePersona = "UPDATE PERSONA SET "
                    + "PERS_CI=" + contacto.getCedula() + ", "
                    + "PERS_NOMBRE='" + contacto.getNombre() + "', "
                    + "PERS_APELLIDO='" + contacto.getApellido() + "', "
                    + "PERS_ID_SEXO=" + contacto.getId_sexo() + ", "
                    + "PERS_FECHA_NACIMIENTO='" + contacto.getFecha_nacimiento() + "', "
                    + "PERS_ID_ESTADO_CIVIL=" + contacto.getId_estado_civil() + ", "
                    + "PERS_ID_PAIS=" + contacto.getId_pais() + ", "
                    + "PERS_ID_CIUDAD=" + contacto.getId_ciudad() + " "
                    + "WHERE PERS_ID_PERSONA = " + contacto.getId_persona();
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
                + "PRSU_DIRECCION='" + direccion + "', "
                + "PRSU_TELEFONO='" + telefono + "' "
                + "WHERE PRSU_ID_PROVEEDOR_SUCURSAL = " + idSucursal;
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
                + "TELE_NUMERO='" + nroTelefono + "', "
                + "TELE_CATEGORIA= '" + tipoTelefono + "', "
                + "TELE_OBSERVACION='" + observacion + "' "
                + "WHERE TELE_ID_TELEFONO = " + idTelefono;
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
                + "MARC_DESCRIPCION= '" + descripcion + "' "
                + "WHERE MARC_ID_MARCA =" + idMarca;
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
                + "IMPU_DESCRIPCION= " + descripcion + " "
                + "WHERE IMPU_ID_IMPUESTO =" + idImpuesto;
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
                + "RUBR_DESCRIPCION= '" + descripcion + "' "
                + "WHERE RUBR_ID_RUBRO =" + idRubro;
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
        String q = "DELETE FROM TELEFONO WHERE TELE_ID_TELEFONO =" + id_telefono;
        String q2 = "DELETE FROM PROVEEDOR_TELEFONO WHERE PRTE_ID_TELEFONO =" + id_telefono;
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
        String q = "DELETE FROM PROVEEDOR_SUCURSAL WHERE PRSU_ID_PROVEEDOR_SUCURSAL =" + id_sucursal;
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
        String delete = "DELETE FROM MARCA WHERE MARC_ID_MARCA =" + id_marca;
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
        String delete = "DELETE FROM IMPUESTO WHERE IMPU_ID_IMPUESTO =" + idImpuesto;
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
        String delete = "DELETE FROM RUBRO WHERE RUBR_ID_RUBRO =" + idRubro;
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
                + "WHERE PRPR_ID_PROVEEDOR = " + idProveedor
                + " AND PRPR_ID_PRODUCTO =" + idProducto;
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
        String delete = "DELETE FROM PROVEEDOR_CONTACTO WHERE PRCO_ID_PROVEEDOR_CONTACTO =" + contacto.getId_contacto();
        String DELETE_PERSONA = "DELETE FROM PERSONA WHERE PERS_ID_PERSONA = " + contacto.getId_persona();
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
