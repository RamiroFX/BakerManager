/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import Entities.M_cliente;
import Entities.M_cliente_contacto;
import Entities.M_sucursal;
import Entities.M_telefono;
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
 * @author Ramiro Ferreir
 */
public class DB_Cliente {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static ResultSetTableModel consultarCliente(String busqueda, boolean isExclusivo, boolean entidad, boolean ruc) {
        ResultSetTableModel rstm = null;
        String SELECT = "SELECT CLIE_ID_CLIENTE \"ID\", CLIE_NOMBRE \"Nombre Cliente\", CLIE_ENTIDAD  \"Entidad\", CLIE_RUC || '-' || CLIE_RUC_IDENTIFICADOR \"R.U.C.\" ";
        String FROM = "FROM CLIENTE ";
        String WHERE = "WHERE ";
        String ORDER_BY = " ORDER BY CLIE_ENTIDAD ";
        if (isExclusivo) {
            busqueda = busqueda + "%";
        } else {
            busqueda = "%" + busqueda + "%";
        }
        if (entidad && ruc) {
            WHERE = WHERE + "LOWER(CLIE_NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE_ENTIDAD) LIKE '" + busqueda + "' OR LOWER(CLIE_RUC) LIKE '" + busqueda + "'";
        } else if (entidad) {
            WHERE = WHERE + "LOWER(CLIE_NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE_ENTIDAD) LIKE '" + busqueda + "'";
        } else if (ruc) {
            WHERE = WHERE + "LOWER(CLIE_RUC) LIKE '" + busqueda + "'";
        } else if (!entidad && !ruc) {
            WHERE = WHERE + "LOWER(CLIE_NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE_ENTIDAD) LIKE '" + busqueda + "' OR LOWER(CLIE_RUC) LIKE '" + busqueda + "'";
        }

        String QUERY = SELECT + FROM + WHERE + ORDER_BY;
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/
        return rstm;
    }

    public static M_cliente obtenerDatosClienteID(int idCliente) {
        M_cliente cliente = null;
        String categoria = "(SELECT CLCA_DESCRIPCION FROM CLIENTE_CATEGORIA WHERE CLCA_ID_CLIENTE_CATEGORIA = CLIE_ID_CATEGORIA) \"CATEGORIA\", ";
        String tipo = "(SELECT CLTI_DESCRIPCION FROM CLIENTE_TIPO WHERE CLTI_ID_CLIENTE_TIPO = CLIE_ID_TIPO) \"TIPO\" ";
        String query = "SELECT CLIE_ID_CLIENTE, "
                + "CLIE_NOMBRE, "
                + "CLIE_ENTIDAD, "
                + "CLIE_RUC, "
                + "CLIE_RUC_IDENTIFICADOR, "
                + "CLIE_DIRECCION, "
                + "CLIE_EMAIL, "
                + "CLIE_PAG_WEB, "
                + "CLIE_ID_TIPO, "
                + "CLIE_ID_CATEGORIA, "
                + "CLIE_OBSERVACION, "
                + categoria
                + tipo
                + "FROM CLIENTE "
                + "WHERE CLIE_ID_CLIENTE = " + idCliente;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("CLIE_DIRECCION"));
                cliente.setEmail(rs.getString("CLIE_EMAIL"));
                cliente.setEntidad(rs.getString("CLIE_ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("CLIE_ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("CLIE_ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("CLIE_ID_TIPO"));
                cliente.setNombre(rs.getString("CLIE_NOMBRE"));
                cliente.setObservacion(rs.getString("CLIE_OBSERVACION"));
                cliente.setPaginaWeb(rs.getString("CLIE_PAG_WEB"));
                cliente.setRuc(rs.getString("CLIE_RUC"));
                cliente.setRucId(rs.getString("CLIE_RUC_IDENTIFICADOR"));
                cliente.setTipo(rs.getString("TIPO"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return cliente;
    }

    public static Vector obtenerCategoriaCliente() {
        Vector categoria = null;
        String q = "SELECT clca_descripcion  "
                + "FROM cliente_categoria ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            categoria = new Vector();
            while (rs.next()) {
                categoria.add(rs.getString("clca_descripcion"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return categoria;
    }

    public static Vector obtenerTipoCliente() {
        Vector tipo = null;
        String q = "SELECT clti_descripcion  "
                + "FROM cliente_tipo ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tipo = new Vector();
            while (rs.next()) {
                tipo.add(rs.getString("clti_descripcion"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return tipo;
    }

    /*
     * INSERT
     */
    public static void insertarCliente(M_cliente cliente, M_telefono[] telefono, M_sucursal[] sucursal, ArrayList<M_cliente_contacto> contactos) {
        long id_cliente = -1L;
        ArrayList id_persona = new ArrayList();
        ArrayList id_telefono = new ArrayList();
        String insert_cliente = "INSERT INTO CLIENTE("
                + "CLIE_NOMBRE, "
                + "CLIE_ENTIDAD, "
                + "CLIE_RUC, "
                + "CLIE_RUC_IDENTIFICADOR, "
                + "CLIE_DIRECCION, "
                + "CLIE_EMAIL, "
                + "CLIE_PAG_WEB, "
                + "CLIE_ID_TIPO, "
                + "CLIE_ID_CATEGORIA, "
                + "CLIE_OBSERVACION"
                + ")VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String id_categoria = "SELECT CLCA_ID_CLIENTE_CATEGORIA FROM CLIENTE_CATEGORIA WHERE CLCA_DESCRIPCION LIKE '" + cliente.getCategoria() + "'";
        String id_tipo = "SELECT CLTI_ID_CLIENTE_TIPO FROM CLIENTE_TIPO WHERE CLTI_DESCRIPCION LIKE '" + cliente.getTipo() + "'";
        String insert_telefono = "INSERT INTO TELEFONO( TELE_NUMERO, TELE_CATEGORIA, TELE_OBSERVACION)VALUES (?, ?, ?)";
        String insert_telefono_cliente = "INSERT INTO CLIENTE_TELEFONO(CLTE_ID_CLIENTE, CLTE_ID_TELEFONO)VALUES (?, ?)";
        String insert_sucursal = "INSERT INTO CLIENTE_SUCURSAL(CLSU_ID_CLIENTE, CLSU_DIRECCION, CLSU_TELEFONO)VALUES (?, ?, ?)";
        String insert_contacto = "INSERT INTO CLIENTE_CONTACTO(CLCO_ID_PERSONA, CLCO_ID_CLIENTE, CLCO_DIRECCION, CLCO_TELEFONO, CLCO_EMAIL, CLCO_OBSERVACION) VALUES (?, ?, ?, ?, ?, ?)";
        String insert_persona = "INSERT INTO PERSONA(PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(id_categoria, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                cliente.setIdCategoria(rs.getInt("CLCA_ID_CLIENTE_CATEGORIA"));
            }
            rs.close();
            pst = DB_manager.getConection().prepareStatement(id_tipo, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                cliente.setIdTipo(rs.getInt("CLTI_ID_CLIENTE_TIPO"));
            }
            rs.close();
            pst = DB_manager.getConection().prepareStatement(insert_cliente, PreparedStatement.RETURN_GENERATED_KEYS);
            try {
                if (cliente.getNombre() == null) {
                    pst.setNull(1, Types.VARCHAR);
                } else {
                    pst.setString(1, cliente.getNombre());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.VARCHAR);
            }
            pst.setString(2, cliente.getEntidad());//not null
            try {
                if (cliente.getRuc() == null) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, cliente.getRuc());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            try {
                if (cliente.getRucId() == null) {
                    pst.setNull(4, Types.VARCHAR);
                } else {
                    pst.setString(4, cliente.getRucId());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.VARCHAR);
            }
            try {
                if (cliente.getDireccion() == null) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, cliente.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (cliente.getEmail() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, cliente.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (cliente.getPaginaWeb() == null) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, cliente.getPaginaWeb());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            pst.setInt(8, cliente.getIdTipo());//not null
            pst.setInt(9, cliente.getIdCategoria());//not null
            try {
                if (cliente.getObservacion() == null) {
                    pst.setNull(10, Types.VARCHAR);
                } else {
                    pst.setString(10, cliente.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(10, Types.VARCHAR);
            }
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_cliente = rs.getLong(1);
            }
            pst.close();
            rs.close();
            if (telefono.length > 0) {
                for (int i = 0; i < telefono.length; i++) {
                    pst = DB_manager.getConection().prepareStatement(insert_telefono, PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, telefono[i].getNumero());
                    pst.setString(2, telefono[i].getCategoria());
                    pst.setString(3, telefono[i].getObservacion());
                    pst.executeUpdate();
                    rs = pst.getGeneratedKeys();
                    if (rs != null && rs.next()) {
                        id_telefono.add(rs.getLong(1));
                    }
                }
                pst.close();
                rs.close();
                for (int i = 0; i < telefono.length; i++) {
                    pst = DB_manager.getConection().prepareStatement(insert_telefono_cliente);
                    pst.setInt(1, (int) id_cliente);
                    int idTel = Integer.valueOf(String.valueOf(id_telefono.get(i)));
                    pst.setInt(2, idTel);
                    pst.executeUpdate();
                    pst.close();
                }
            }
            for (int i = 0; i < sucursal.length; i++) {
                pst = DB_manager.getConection().prepareStatement(insert_sucursal);
                pst.setInt(1, (int) id_cliente);
                pst.setString(2, sucursal[i].getDireccion());
                pst.setString(3, sucursal[i].getTelefono());
                pst.executeUpdate();
                pst.close();
            }
            if (contactos.size() > 0) {
                for (int i = 0; i < contactos.size(); i++) {
                    pst = DB_manager.getConection().prepareStatement(insert_persona, PreparedStatement.RETURN_GENERATED_KEYS);
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
                    pst = DB_manager.getConection().prepareStatement(insert_contacto);
                    int idPersona = Integer.valueOf(String.valueOf(id_persona.get(i)));
                    pst.setInt(1, idPersona);
                    pst.setInt(2, (int) id_cliente);
                    pst.setString(3, contactos.get(i).getDireccion());
                    pst.setString(4, contactos.get(i).getTelefono());
                    pst.setString(5, contactos.get(i).getEmail());
                    pst.setString(6, contactos.get(i).getObservacion());
                    pst.executeUpdate();
                    pst.close();
                }
            }
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static ResultSetTableModel obtenerSucursal(int idCliente) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT CLSU_ID_CLIENTE_SUCURSAL \"ID\", "
                + "CLSU_DIRECCION \"Dirección\", "
                + "CLSU_TELEFONO \"Telefono\" "
                + "FROM CLIENTE_SUCURSAL "
                + "WHERE CLSU_ID_CLIENTE =" + idCliente;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/
        return rstm;
    }

    public static ResultSetTableModel obtenerClienteTelefono(int idCliente) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT TELE_NUMERO \"Número\", "
                + "TELE_CATEGORIA \"Categoría\", "
                + "TELE_OBSERVACION \"Observación\" "
                + "FROM TELEFONO, CLIENTE, CLIENTE_TELEFONO "
                + "WHERE TELE_ID_TELEFONO = CLTE_ID_TELEFONO "
                + "AND CLIE_ID_CLIENTE = CLTE_ID_CLIENTE "
                + "AND CLIE_ID_CLIENTE = " + idCliente;
        try {
            Statement statement = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            ResultSet r = statement.executeQuery(Query);
            rstm = new ResultSetTableModel(r);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rstm;
    }

    public static ResultSetTableModel obtenerClienteContacto(int idCliente) {
        ResultSetTableModel rstm = null;
        String select = "SELECT CLCO_ID_CLIENTE_CONTACTO \"ID\","
                + "PERS_NOMBRE\"Nombre\","
                + "PERS_APELLIDO\"Apellido\", "
                + "CLCO_TELEFONO\"Telefono\" "
                + "FROM CLIENTE_CONTACTO,PERSONA,CLIENTE "
                + "WHERE CLCO_ID_CLIENTE = CLIE_ID_CLIENTE "
                + "AND CLCO_ID_PERSONA = PERS_ID_PERSONA "
                + "AND CLIE_ID_CLIENTE = " + idCliente;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(select);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/
        return rstm;
    }

    public static M_cliente_contacto obtenerDatosClienteContactoID(int idClienteContacto) {
        M_cliente_contacto contacto = null;
        String query = "SELECT PERS_ID_PERSONA,CLCO_ID_CLIENTE,CLCO_ID_PERSONA, "
                + " CLCO_ID_CLIENTE_CONTACTO, PERS_CI, PERS_NOMBRE, PERS_APELLIDO, "
                + " PERS_ID_SEXO, "
                + " (SELECT SEXO_DESCRIPCION FROM SEXO WHERE SEXO_ID_SEXO = PERS_ID_SEXO) \"SEXO\","
                + " PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL,(SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL = PERS_ID_ESTADO_CIVIL)\"ESTADO_CIVIL\", "
                + " PERS_ID_PAIS,(SELECT PAIS_DESCRIPCION FROM PAIS WHERE PAIS_ID_PAIS = PERS_ID_PAIS)\"PAIS\" ,"
                + " PERS_ID_CIUDAD,(SELECT CIUD_DESCRIPCION FROM CIUDAD WHERE CIUD_ID_CIUDAD = PERS_ID_CIUDAD)\"CIUDAD\" , "
                + " CLCO_DIRECCION, "
                + " CLCO_TELEFONO, CLCO_EMAIL, CLCO_OBSERVACION"
                + " FROM PERSONA,CLIENTE_CONTACTO"
                + " WHERE PERS_ID_PERSONA = CLCO_ID_PERSONA"
                + " AND CLCO_ID_CLIENTE_CONTACTO = " + idClienteContacto;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                contacto = new M_cliente_contacto();
                contacto.setApellido(rs.getString("PERS_APELLIDO"));
                contacto.setCedula(rs.getInt("PERS_CI"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setDireccion(rs.getString("CLCO_DIRECCION"));
                contacto.setEmail(rs.getString("CLCO_EMAIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setFecha_nacimiento(rs.getDate("PERS_FECHA_NACIMIENTO"));
                contacto.setIdCliente(rs.getInt("CLCO_ID_CLIENTE"));
                contacto.setIdClienteContacto(rs.getInt("CLCO_ID_CLIENTE_CONTACTO"));
                contacto.setId_ciudad(rs.getInt("PERS_ID_CIUDAD"));
                contacto.setId_estado_civil(rs.getInt("PERS_ID_ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("PERS_ID_PAIS"));
                contacto.setId_persona(rs.getInt("PERS_ID_PERSONA"));
                contacto.setId_sexo(rs.getInt("PERS_ID_SEXO"));
                contacto.setNombre(rs.getString("PERS_NOMBRE"));
                contacto.setObservacion(rs.getString("CLCO_OBSERVACION"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setTelefono(rs.getString("CLCO_TELEFONO"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return contacto;
    }

    public static void insertarContacto(int idCliente, M_cliente_contacto contacto) {
        String insert_persona = "INSERT INTO PERSONA(PERS_CI, PERS_NOMBRE, PERS_APELLIDO, PERS_ID_SEXO, PERS_FECHA_NACIMIENTO, PERS_ID_ESTADO_CIVIL, PERS_ID_PAIS, PERS_ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertContacto = "INSERT INTO CLIENTE_CONTACTO"
                + "( CLCO_ID_PERSONA, "
                + "CLCO_ID_CLIENTE, "
                + "CLCO_DIRECCION, "
                + "CLCO_TELEFONO, "
                + "CLCO_EMAIL, "
                + "CLCO_OBSERVACION"
                + ")VALUES ("
                + "?, ?, ?, ?, ?, ?)";
        long id_persona = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(insert_persona, PreparedStatement.RETURN_GENERATED_KEYS);
            try {
                if (contacto.getCedula() == null) {
                    pst.setNull(1, Types.INTEGER);
                } else {
                    pst.setInt(1, (int) contacto.getCedula());
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            pst = DB_manager.getConection().prepareStatement(insertContacto, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, (int) id_persona);
            pst.setInt(2, idCliente);
            pst.setString(3, contacto.getDireccion());
            pst.setString(4, contacto.getTelefono());
            pst.setString(5, contacto.getEmail());
            pst.setString(6, contacto.getObservacion());
            rs = pst.executeQuery();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void eliminarContacto(int idPersona, int idContacto, int idCliente) {
        String delete_contacto = "DELETE FROM CLIENTE_CONTACTO WHERE CLCO_ID_CLIENTE_CONTACTO =" + idContacto + " "
                + "AND CLCO_ID_CLIENTE = " + idCliente;
        String delete_persona = "DELETE FROM PERSONA WHERE PERS_ID_PERSONA =" + idPersona;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete_contacto);
            st.close();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(delete_persona);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void modificarContacto(M_cliente_contacto contacto) {
        String UPDATE_CONTACTO = "UPDATE CLIENTE_CONTACTO SET "
                + "CLCO_DIRECCION=" + contacto.getDireccion() + ", "
                + "CLCO_TELEFONO=" + contacto.getTelefono() + ", "
                + "CLCO_EMAIL=" + contacto.getEmail() + ", "
                + "CLCO_OBSERVACION= " + contacto.getObservacion() + " "
                + "WHERE CLCO_ID_CLIENTE_CONTACTO = " + contacto.getIdClienteContacto();
        String UPDATE_PERSONA = "UPDATE PERSONA SET "
                + "PERS_CI=" + contacto.getCedula() + ", "
                + "PERS_NOMBRE=" + contacto.getNombre() + ", "
                + "PERS_APELLIDO=" + contacto.getApellido() + ", "
                + "PERS_ID_SEXO=" + contacto.getId_sexo() + ", "
                + "PERS_FECHA_NACIMIENTO=" + contacto.getFecha_nacimiento() + ", "
                + "PERS_ID_ESTADO_CIVIL=" + contacto.getCedula() + ", "
                + "PERS_ID_PAIS=" + contacto.getId_pais() + ", "
                + "PERS_ID_CIUDAD" + contacto.getId_ciudad() + " "
                + "WHERE PERS_ID_PERSONA = " + contacto.getId_persona();
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_CONTACTO);
            st.close();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_PERSONA);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarCliente(M_cliente cliente) {
        String UPDATE_CLIENTE = "UPDATE CLIENTE SET "
                + "CLIE_NOMBRE= '" + cliente.getNombre() + "', "
                + "CLIE_ENTIDAD= '" + cliente.getEntidad() + "', "
                + "CLIE_RUC= '" + cliente.getRuc() + "', "
                + "CLIE_RUC_IDENTIFICADOR= '" + cliente.getRucId() + "', "
                + "CLIE_DIRECCION= '" + cliente.getDireccion() + "', "
                + "CLIE_EMAIL= '" + cliente.getEmail() + "', "
                + "CLIE_PAG_WEB= '" + cliente.getPaginaWeb() + "', "
                + "CLIE_ID_TIPO= " + cliente.getIdTipo() + ", "
                + "CLIE_ID_CATEGORIA= " + cliente.getIdCategoria() + ", "
                + "CLIE_OBSERVACION= '" + cliente.getObservacion() + "' "
                + "WHERE CLIE_ID_CLIENTE = " + cliente.getIdCliente();
        try {
            DB_manager.getConection().setAutoCommit(false);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(UPDATE_CLIENTE);
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void actualizarTelefono(int idTelefono, String tipoTelefono, String nroTelefono, String observacion) {
        String updateTelefono = "UPDATE TELEFONO SET "
                + "TELE_NUMERO='" + nroTelefono + "', "
                + "TELE_CATEGORIA= '" + tipoTelefono + "', "
                + "TELE_OBSERVACION='" + observacion + "' "
                + "WHERE TELE_ID_TELEFONO = " + idTelefono;
        try {
            DB_manager.getConection().setAutoCommit(false);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(updateTelefono);
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void insertarTelefono(Integer idCliente, String tipoTelefono, String nroTelefono, String observacion) {
        long id_telefono = -1L;
        String insertTelefono = "INSERT INTO telefono("
                + "tele_numero, "
                + "tele_categoria, "
                + "tele_observacion"
                + ")VALUES ("
                + nroTelefono + "', '"
                + tipoTelefono + "', '"
                + observacion + "')";
        String insertTelProv = "INSERT INTO CLIENTE_TELEFONO("
                + "CLTE_ID_CLIENTE, "
                + "CLTE_ID_TELEFONO"
                + ")VALUES ("
                + idCliente + ", "
                + id_telefono + ")";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insertTelefono, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id_telefono = rs.getLong(1);
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(insertTelProv);
            pst.executeUpdate();
            pst.close();
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
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
    /*
     public static ResultSetTableModel consultarCliente(String toLowerCase, boolean entidad, boolean ruc, boolean exclusivo) {
     ResultSetTableModel rstm = null;
     String select = "SELECT CLIE_ID_CLIENTE \"ID\",CLIE_ENTIDAD \"Entidad\",CLIE_NOMBRE \"Nombre cliente\", CLIE_RUC \"R.U.C.\" ";
     String from = "FROM CLIENTE ";
     String where = "WHERE ";
     String orderBy = "ORDER BY CLIE_ENTIDAD ";
     String prov;
     if (exclusivo) {
     prov = toLowerCase + "%";
     } else {
     prov = "%" + toLowerCase + "%";
     }
     if (entidad && ruc) {
     where = where + "LOWER(CLIE_NOMBRE) LIKE '" + prov + "' OR LOWER(CLIE_ENTIDAD) LIKE '" + prov + "'  OR LOWER(CLIE_RUC)LIKE '" + prov + "' ";
     } else if (entidad) {
     where = where + "LOWER(CLIE_NOMBRE) LIKE '" + prov + "' ";
     } else if (ruc) {
     where = where + " LOWER(CLIE_RUC)LIKE '" + prov + "' ";
     } else if (!entidad && !ruc) {
     where = where + "LOWER(CLIE_NOMBRE) LIKE '" + prov + "' OR LOWER(CLIE_ENTIDAD) LIKE '" + prov + "'  OR LOWER(CLIE_RUC)LIKE '" + prov + "' ";
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
     }*/

    public static ArrayList<M_telefono> obtenerTelefonoCliente(int idCliente) {
        ArrayList telefonos = null;
        String query = "SELECT TELE_ID_TELEFONO, TELE_NUMERO, TELE_CATEGORIA, TELE_OBSERVACION  FROM TELEFONO, CLIENTE, CLIENTE_TELEFONO  WHERE CLIE_ID_CLIENTE = CLTE_ID_CLIENTE  AND CLTE_ID_TELEFONO = TELE_ID_TELEFONO"
                + " AND CLIE_ID_CLIENTE = " + idCliente + ";";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            telefonos = new ArrayList();
            while (rs.next()) {
                M_telefono telefono = new M_telefono();
                telefono.setId_telefono(rs.getInt("CLIE_ID_CLIENTE"));
                telefono.setCategoria(rs.getString("TELE_CATEGORIA"));
                telefono.setNumero(rs.getString("TELE_NUMERO"));
                telefono.setObservacion(rs.getString("TELE_OBSERVACION"));
                telefonos.add(telefono);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return telefonos;
    }
}
