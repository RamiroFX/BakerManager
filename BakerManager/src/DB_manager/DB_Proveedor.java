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
                + "PROV.OBSERVACION "
                + "FROM PROVEEDOR PROV "
                + "WHERE PROV.id_proveedor = " + idProveedor;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setDescripcion(rs.getString("DESCRIPCION"));
                proveedor.setDireccion(rs.getString("DIRECCION"));
                proveedor.setEmail(rs.getString("EMAIL"));
                proveedor.setPagWeb(rs.getString("PAG_WEB"));
                proveedor.setEntidad(rs.getString("ENTIDAD"));
                proveedor.setId(rs.getInt("ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("NOMBRE"));
                proveedor.setRuc(rs.getString("RUC"));
                proveedor.setRuc_id(rs.getString("RUC_IDENTIFICADOR"));
                proveedor.setObservacion(rs.getString("OBSERVACION"));
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
                + "FROM PROVEEDOR PROV "
                + "WHERE PROV.ENTIDAD LIKE '" + entidad + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                proveedor = new M_proveedor();
                proveedor.setPagWeb(rs.getString("PAG_WEB"));
                proveedor.setDescripcion(rs.getString("DESCRIPCION"));
                proveedor.setDireccion(rs.getString("DIRECCION"));
                proveedor.setEmail(rs.getString("EMAIL"));
                proveedor.setEntidad(rs.getString("ENTIDAD"));
                proveedor.setId(rs.getInt("ID_PROVEEDOR"));
                proveedor.setNombre(rs.getString("NOMBRE"));
                proveedor.setRuc(rs.getString("RUC"));
                proveedor.setRuc_id(rs.getString("RUC_IDENTIFICADOR"));
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
                + "(SELECT CIUD.DESCRIPCION FROM CIUDAD CIUD WHERE CIUD.ID_CIUDAD =PERS.ID_CIUDAD)\"CIUDAD\","
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
                contacto.setApellido(rs.getString("APELLIDO"));
                contacto.setCedula(rs.getInt("CI"));
                contacto.setDireccion(rs.getString("DIRECCION"));
                contacto.setEmail(rs.getString("EMAIL"));
                contacto.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                contacto.setId_ciudad(rs.getInt("ID_CIUDAD"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setId_contacto(rs.getInt("ID_PROVEEDOR_CONTACTO"));
                contacto.setId_estado_civil(rs.getInt("ID_ESTADO_CIVIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("ID_PAIS"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setId_persona(rs.getInt("ID_PERSONA"));
                contacto.setId_proveedor(rs.getInt("ID_PROVEEDOR"));
                contacto.setId_sexo(rs.getInt("ID_SEXO"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setNombre(rs.getString("NOMBRE"));
                contacto.setObservacion(rs.getString("OBSERVACION"));
                contacto.setTelefono(rs.getString("TELEFONO"));
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
                + "TECA.DESCRIPCION \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, PROVEEDOR PROV, PROVEEDOR_TELEFONO PRTE, TELEFONO_CATEGORIA TECA "
                + "WHERE TELE.ID_TELEFONO = PRTE.ID_TELEFONO "
                + "AND PROV.ID_PROVEEDOR = PRTE.ID_PROVEEDOR "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
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
                + "TECA.DESCRIPCION \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, PROVEEDOR PROV, PROVEEDOR_TELEFONO PRTE, TELEFONO_CATEGORIA TECA "
                + "WHERE TELE.ID_TELEFONO = PRTE.ID_TELEFONO "
                + "AND PROV.ID_PROVEEDOR = PRTE.ID_PROVEEDOR "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
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
        String INSERT_PROVEEDOR = "INSERT INTO PROVEEDOR(NOMBRE, ENTIDAD, RUC, RUC_IDENTIFICADOR, DESCRIPCION, DIRECCION, PAG_WEB, EMAIL, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(NUMERO, ID_CATEGORIA, OBSERVACION) VALUES (?, ?, ?)";
        String INSERT_PROVEEDOR_TELEFONO = "INSERT INTO PROVEEDOR_TELEFONO(ID_PROVEEDOR, ID_TELEFONO) VALUES (?, ?)";
        String INSERT_PERSONA = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(ID_PERSONA, ID_PROVEEDOR, EMAIL, DIRECCION, TELEFONO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        String INSERT_SUCURSAL = "INSERT INTO PROVEEDOR_SUCURSAL(ID_PROVEEDOR, DIRECCION, TELEFONO)VALUES (?, ?, ?)";
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
                    String telefonoCategoria = "SELECT ID_TELEFONO_CATEGORIA FROM TELEFONO_CATEGORIA WHERE DESCRIPCION LIKE '" + telefono[i].getCategoria() + "'";
                    st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    // se ejecuta el query y se obtienen los resultados en un ResultSet
                    rs = st.executeQuery(telefonoCategoria);
                    int id_categoria = 0;
                    while (rs.next()) {
                        id_categoria = rs.getInt("ID_TELEFONO_CATEGORIA");
                    }
                    pst = DB_manager.getConection().prepareStatement(INSERT_TELEFONO, PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, telefono[i].getNumero());//not null
                    pst.setInt(2, id_categoria);//not null
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
        String INSERT_PERSONA = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO PROVEEDOR_CONTACTO(ID_PERSONA, ID_PROVEEDOR, EMAIL, DIRECCION, TELEFONO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA, PreparedStatement.RETURN_GENERATED_KEYS);
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
                + "ID_PROVEEDOR, "
                + "DIRECCION, "
                + "TELEFONO)"
                + "VALUES (?, ?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(q);
            pst.setInt(1, idProveedor);
            pst.setString(2, direccion);
            if (telefono != null) {
                pst.setString(3, telefono);
            } else {
                pst.setNull(3, Types.VARCHAR);
            }
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

    public static void insertarTelefono(Integer idProveedor, String tipoTelefono, String nroTelefono, String observacion) {
        long id_telefono = -1L;
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(NUMERO, ID_CATEGORIA, OBSERVACION)VALUES (?, ?, ?)";
        String INSERT_TELEFONO_PROVEEDOR = "INSERT INTO PROVEEDOR_TELEFONO(ID_PROVEEDOR, ID_TELEFONO)VALUES (?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            String telefonoCategoria = "SELECT ID_TELEFONO_CATEGORIA FROM TELEFONO_CATEGORIA WHERE DESCRIPCION LIKE '" + tipoTelefono + "'";
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(telefonoCategoria);
            int id_categoria = 0;
            while (rs.next()) {
                id_categoria = rs.getInt("ID_TELEFONO_CATEGORIA");
            }
            pst = DB_manager.getConection().prepareStatement(INSERT_TELEFONO, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setString(1, nroTelefono);
            pst.setInt(2, id_categoria);
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

    public static void insertarCategoria(String categoria) {
        String insert = "INSERT INTO PRODUCTO_CATEGORIA("
                + "DESCRIPCION"
                + ")VALUES (?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insert);
            pst.setString(1, categoria);
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
        String updateProveedor = "UPDATE PROVEEDOR SET "
                + "NOMBRE = ?, "
                + "ENTIDAD = ?, "
                + "RUC = ?, "
                + "RUC_IDENTIFICADOR = ?, "
                + "DESCRIPCION = ?, "
                + "DIRECCION = ?, "
                + "PAG_WEB = ?, "
                + "EMAIL = ?, "
                + "OBSERVACION = ? "
                + "WHERE ID_PROVEEDOR = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updateProveedor);
            //nombre
            if (proveedor.getNombre() != null) {
                pst.setString(1, proveedor.getNombre());
            } else {
                pst.setNull(1, Types.VARCHAR);
            }
            pst.setString(2, proveedor.getEntidad());//not null
            //ruc
            if (proveedor.getRuc() != null) {
                pst.setString(3, proveedor.getRuc());
            } else {
                pst.setNull(3, Types.VARCHAR);
            }
            if (proveedor.getRuc_id() != null) {
                pst.setString(4, proveedor.getRuc_id());
            } else {
                pst.setNull(4, Types.VARCHAR);
            }
            if (proveedor.getDescripcion() != null) {
                pst.setString(5, proveedor.getDescripcion());
            } else {
                pst.setNull(5, Types.VARCHAR);
            }
            if (proveedor.getDireccion() != null) {
                pst.setString(6, proveedor.getDireccion());
            } else {
                pst.setNull(6, Types.VARCHAR);
            }
            if (proveedor.getPagWeb() != null) {
                pst.setString(7, proveedor.getPagWeb());
            } else {
                pst.setNull(7, Types.VARCHAR);
            }
            if (proveedor.getEmail() != null) {
                pst.setString(8, proveedor.getEmail());
            } else {
                pst.setNull(8, Types.VARCHAR);
            }
            if (proveedor.getObservacion() != null) {
                pst.setString(9, proveedor.getObservacion());
            } else {
                pst.setNull(9, Types.VARCHAR);
            }
            pst.setInt(10, proveedor.getId());
            pst.executeUpdate();
            pst.close();
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

    public static void modificarProveedorContacto(M_contacto contacto) {
        try {
            DB_manager.habilitarTransaccionManual();
            String updateContacto = "UPDATE PROVEEDOR_CONTACTO SET "
                    + "EMAIL = ?, "
                    + "DIRECCION = ?, "
                    + "OBSERVACION = ?, "
                    + "TELEFONO = ? "
                    + "WHERE ID_PROVEEDOR_CONTACTO = ? ;";
            String updatePersona = "UPDATE PERSONA SET "
                    + "CI = ?, "
                    + "NOMBRE = ?, "
                    + "APELLIDO = ?, "
                    + "ID_SEXO = ?, "
                    + "FECHA_NACIMIENTO = ?, "
                    + "ID_ESTADO_CIVIL = ?, "
                    + "ID_PAIS = ?, "
                    + "ID_CIUDAD = ? "
                    + "WHERE ID_PERSONA = ? ;";
            pst = DB_manager.getConection().prepareStatement(updatePersona);
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
            pst.setInt(9, contacto.getId_persona());
            pst.executeUpdate();
            pst.close();

            pst = DB_manager.getConection().prepareStatement(updateContacto);
            if (contacto.getEmail() != null) {
                pst.setString(1, contacto.getEmail());
            } else {
                pst.setNull(1, Types.VARCHAR);
            }
            if (contacto.getDireccion() != null) {
                pst.setString(2, contacto.getDireccion());
            } else {
                pst.setNull(2, Types.VARCHAR);
            }
            if (contacto.getObservacion() != null) {
                pst.setString(3, contacto.getObservacion());
            } else {
                pst.setNull(3, Types.VARCHAR);
            }
            if (contacto.getTelefono() != null) {
                pst.setString(4, contacto.getTelefono());
            } else {
                pst.setNull(4, Types.VARCHAR);
            }
            pst.setInt(5, contacto.getId_contacto());
            pst.executeUpdate();
            pst.close();
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
                + "DIRECCION = ?, "
                + "TELEFONO = ? "
                + "WHERE ID_PROVEEDOR_SUCURSAL = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(q);
            pst.setString(1, direccion);
            pst.setString(2, telefono);
            pst.setInt(3, idSucursal);
            pst.executeUpdate();
            pst.close();
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
                + "NUMERO = ?, "
                + "ID_CATEGORIA = ?, "
                + "OBSERVACION = ? "
                + "WHERE ID_TELEFONO = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            String telefonoCategoria = "SELECT ID_TELEFONO_CATEGORIA FROM TELEFONO_CATEGORIA WHERE DESCRIPCION LIKE '" + tipoTelefono + "'";
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(telefonoCategoria);
            int id_categoria = 0;
            while (rs.next()) {
                id_categoria = rs.getInt("ID_TELEFONO_CATEGORIA");
            }
            pst = DB_manager.getConection().prepareStatement(updateTelefono);
            pst.setString(1, nroTelefono);
            pst.setInt(2, id_categoria);
            if (observacion == null) {
                pst.setString(3, observacion);
            } else {
                pst.setNull(3, Types.VARCHAR);
            }
            pst.setInt(4, idTelefono);
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

    public static void modificarCategoria(int idCategoria, String descripcion) {
        String updateMarca = "UPDATE PRODUCTO_CATEGORIA SET "
                + "DESCRIPCION = ? "
                + "WHERE ID_PRODUCTO_CATEGORIA = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updateMarca);
            pst.setString(1, descripcion);
            pst.setInt(2, idCategoria);
            pst.executeUpdate();
            pst.close();
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
            st.executeUpdate(q2);
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

    public static void eliminarProductoCategoria(int idCategoria) {
        String delete = "DELETE FROM PRODUCTO_CATEGORIA WHERE ID_PRODUCTO_CATEGORIA = " + idCategoria;
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

    public static boolean existeRuc(String ruc) {
        String query = "SELECT RUC FROM PROVEEDOR WHERE RUC = ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(query);
            pst.setString(1, ruc);
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static ArrayList<M_telefono> obtenerTelefonos(int idProveedor) {
        ArrayList<M_telefono> telefonos = null;
        String QUERY = "SELECT TELE.ID_TELEFONO, TELE.NUMERO, TECA.ID_TELEFONO_CATEGORIA, TECA.DESCRIPCION, "
                + "TELE.OBSERVACION "
                + "FROM TELEFONO TELE, TELEFONO_CATEGORIA TECA, "
                + "PROVEEDOR_TELEFONO PRTE "
                + "WHERE TELE.ID_TELEFONO =  PRTE.ID_TELEFONO  "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
                + "AND PRTE.ID_PROVEEDOR = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idProveedor);
            rs = pst.executeQuery();
            telefonos = new ArrayList();
            while (rs.next()) {
                M_telefono telefono = new M_telefono();
                telefono.setId_telefono(rs.getInt("ID_TELEFONO"));
                telefono.setCategoria(rs.getString("DESCRIPCION"));
                telefono.setNumero(rs.getString("NUMERO"));
                telefono.setIdCategoria(rs.getInt("ID_TELEFONO_CATEGORIA"));
                telefono.setCategoria(rs.getString("OBSERVACION"));
                telefonos.add(telefono);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return telefonos;
    }
}
