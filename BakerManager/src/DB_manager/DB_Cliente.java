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
        String SELECT = "SELECT CLIE.ID_CLIENTE \"ID\", CLIE.NOMBRE \"Nombre Cliente\", CLIE.ENTIDAD  \"Entidad\", CLIE.RUC || '-' || CLIE.RUC_IDENTIFICADOR \"R.U.C.\" ";
        String FROM = "FROM CLIENTE CLIE ";
        String WHERE = "WHERE ";
        String ORDER_BY = " ORDER BY CLIE.ENTIDAD ";
        if (isExclusivo) {
            busqueda = busqueda + "%";
        } else {
            busqueda = "%" + busqueda + "%";
        }
        if (entidad && ruc) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE.ENTIDAD) LIKE '" + busqueda + "' OR LOWER(CLIE.RUC) LIKE '" + busqueda + "'";
        } else if (entidad) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE.ENTIDAD) LIKE '" + busqueda + "'";
        } else if (ruc) {
            WHERE = WHERE + "LOWER(CLIE.RUC) LIKE '" + busqueda + "'";
        } else if (!entidad && !ruc) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE '" + busqueda + "' OR LOWER(CLIE.ENTIDAD) LIKE '" + busqueda + "' OR LOWER(CLIE.RUC) LIKE '" + busqueda + "'";
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
        String categoria = "(SELECT CLCA.DESCRIPCION FROM CLIENTE_CATEGORIA CLCA WHERE CLCA.ID_CLIENTE_CATEGORIA = CLIE.ID_CATEGORIA) \"CATEGORIA\", ";
        String tipo = "(SELECT CLTI.DESCRIPCION FROM CLIENTE_TIPO CLTI WHERE CLTI.ID_CLIENTE_TIPO = CLIE.ID_TIPO) \"TIPO\" ";
        String query = "SELECT CLIE.ID_CLIENTE, "
                + "CLIE.NOMBRE, "
                + "CLIE.ENTIDAD, "
                + "CLIE.RUC, "
                + "CLIE.RUC_IDENTIFICADOR, "
                + "CLIE.DIRECCION, "
                + "CLIE.EMAIL, "
                + "CLIE.PAG_WEB, "
                + "CLIE.ID_TIPO, "
                + "CLIE.ID_CATEGORIA, "
                + "CLIE.OBSERVACION, "
                + categoria
                + tipo
                + "FROM CLIENTE CLIE "
                + "WHERE CLIE.ID_CLIENTE = " + idCliente;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                cliente = new M_cliente();
                cliente.setCategoria(rs.getString("CATEGORIA"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setEmail(rs.getString("EMAIL"));
                cliente.setEntidad(rs.getString("ENTIDAD"));
                cliente.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
                cliente.setIdTipo(rs.getInt("ID_TIPO"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setObservacion(rs.getString("OBSERVACION"));
                cliente.setPaginaWeb(rs.getString("PAG_WEB"));
                cliente.setRuc(rs.getString("RUC"));
                cliente.setRucId(rs.getString("RUC_IDENTIFICADOR"));
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
        String q = "SELECT CLCA.descripcion  "
                + "FROM cliente_categoria CLCA ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            categoria = new Vector();
            while (rs.next()) {
                categoria.add(rs.getString("CLCA.descripcion"));
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
        String q = "SELECT CLTI.descripcion  "
                + "FROM cliente_tipo  CLTI ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            tipo = new Vector();
            while (rs.next()) {
                tipo.add(rs.getString("CLTI.descripcion"));
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
                + "NOMBRE, "
                + "ENTIDAD, "
                + "RUC, "
                + "RUC_IDENTIFICADOR, "
                + "DIRECCION, "
                + "EMAIL, "
                + "PAG_WEB, "
                + "ID_TIPO, "
                + "ID_CATEGORIA, "
                + "OBSERVACION"
                + ")VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String id_categoria = "SELECT CLCA.ID_CLIENTE_CATEGORIA FROM CLIENTE_CATEGORIA WHERE CLCA.DESCRIPCION LIKE '" + cliente.getCategoria() + "'";
        String id_tipo = "SELECT CLTI.ID_CLIENTE_TIPO FROM CLIENTE_TIPO WHERE CLTI.DESCRIPCION LIKE '" + cliente.getTipo() + "'";
        String insert_telefono = "INSERT INTO TELEFONO( TELE.NUMERO, TELE.CATEGORIA, TELE.OBSERVACION)VALUES (?, ?, ?)";
        String insert_telefono_cliente = "INSERT INTO CLIENTE_TELEFONO(CLTE.ID_CLIENTE, CLTE.ID_TELEFONO)VALUES (?, ?)";
        String insert_sucursal = "INSERT INTO CLIENTE_SUCURSAL(CLSU.ID_CLIENTE, CLSU.DIRECCION, CLSU.TELEFONO)VALUES (?, ?, ?)";
        String insert_contacto = "INSERT INTO CLIENTE_CONTACTO(CLCO.ID_PERSONA, CLCO.ID_CLIENTE, CLCO.DIRECCION, CLCO.TELEFONO, CLCO.EMAIL, CLCO.OBSERVACION) VALUES (?, ?, ?, ?, ?, ?)";
        String insert_persona = "INSERT INTO PERSONA(PERS.CI, PERS.NOMBRE, PERS.APELLIDO, PERS.ID_SEXO, PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL, PERS.ID_PAIS, PERS.ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(id_categoria, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                cliente.setIdCategoria(rs.getInt("ID_CLIENTE_CATEGORIA"));
            }
            rs.close();
            pst = DB_manager.getConection().prepareStatement(id_tipo, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                cliente.setIdTipo(rs.getInt("ID_CLIENTE_TIPO"));
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
        String Query = "SELECT CLSU.ID_CLIENTE_SUCURSAL \"ID\", "
                + "CLSU.DIRECCION \"Dirección\", "
                + "CLSU.TELEFONO \"Telefono\" "
                + "FROM CLIENTE_SUCURSAL CLSU "
                + "WHERE CLSU.ID_CLIENTE =" + idCliente;
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
        String Query = "SELECT TELE.NUMERO \"Número\", "
                + "TELE.CATEGORIA \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, CLIENTE CLIE, CLIENTE_TELEFONO CLTE "
                + "WHERE TELE.ID_TELEFONO = CLTE.ID_TELEFONO "
                + "AND CLIE.ID_CLIENTE = CLTE.ID_CLIENTE "
                + "AND CLIE.ID_CLIENTE = " + idCliente;
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
        String select = "SELECT CLCO.ID_CLIENTE_CONTACTO \"ID\","
                + "PERS.NOMBRE\"Nombre\","
                + "PERS.APELLIDO\"Apellido\", "
                + "CLCO.TELEFONO\"Telefono\" "
                + "FROM CLIENTE_CONTACTO CLCO, PERSONA PERS, CLIENTE CLIE "
                + "WHERE CLCO.ID_CLIENTE = CLIE.ID_CLIENTE "
                + "AND CLCO.ID_PERSONA = PERS.ID_PERSONA "
                + "AND CLIE.ID_CLIENTE = " + idCliente;
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
        String query = "SELECT PERS.ID_PERSONA,CLCO.ID_CLIENTE,CLCO.ID_PERSONA, "
                + " CLCO.ID_CLIENTE_CONTACTO, PERS.CI, PERS.NOMBRE, PERS.APELLIDO, "
                + " PERS.ID_SEXO, "
                + " (SELECT SEXO.DESCRIPCION FROM SEXO SEXO WHERE SEXO.ID_SEXO = PERS.ID_SEXO) \"SEXO\","
                + " PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL,(SELECT ESCI_DESCRIPCION FROM ESTADO_CIVIL WHERE ESCI_ID_ESTADO_CIVIL = PERS.ID_ESTADO_CIVIL)\"ESTADO_CIVIL\", "
                + " PERS.ID_PAIS,(SELECT PAIS.DESCRIPCION FROM PAIS PAIS WHERE PAIS.ID_PAIS = PERS.ID_PAIS)\"PAIS\" ,"
                + " PERS.ID_CIUDAD,(SELECT CIUD.DESCRIPCION FROM CIUDAD CIUD WHERE CIUD.ID_CIUDAD = PERS.ID_CIUDAD)\"CIUDAD\" , "
                + " CLCO.DIRECCION, "
                + " CLCO.TELEFONO, CLCO.EMAIL, CLCO.OBSERVACION"
                + " FROM PERSONA PERS, CLIENTE_CONTACTO CLCO "
                + " WHERE PERS.ID_PERSONA = CLCO.ID_PERSONA"
                + " AND CLCO.ID_CLIENTE_CONTACTO = " + idClienteContacto;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                contacto = new M_cliente_contacto();
                contacto.setApellido(rs.getString("PERS.APELLIDO"));
                contacto.setCedula(rs.getInt("PERS.CI"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setDireccion(rs.getString("CLCO.DIRECCION"));
                contacto.setEmail(rs.getString("CLCO.EMAIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setFecha_nacimiento(rs.getDate("PERS.FECHA_NACIMIENTO"));
                contacto.setIdCliente(rs.getInt("CLCO.ID_CLIENTE"));
                contacto.setIdClienteContacto(rs.getInt("CLCO.ID_CLIENTE_CONTACTO"));
                contacto.setId_ciudad(rs.getInt("PERS.ID_CIUDAD"));
                contacto.setId_estado_civil(rs.getInt("PERS.ID_ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("PERS.ID_PAIS"));
                contacto.setId_persona(rs.getInt("PERS.ID_PERSONA"));
                contacto.setId_sexo(rs.getInt("PERS.ID_SEXO"));
                contacto.setNombre(rs.getString("PERS.NOMBRE"));
                contacto.setObservacion(rs.getString("CLCO.OBSERVACION"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setTelefono(rs.getString("CLCO.TELEFONO"));
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
        String insert_persona = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertContacto = "INSERT INTO CLIENTE_CONTACTO"
                + "( ID_PERSONA, "
                + "ID_CLIENTE, "
                + "DIRECCION, "
                + "TELEFONO, "
                + "EMAIL, "
                + "OBSERVACION"
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
        String delete_contacto = "DELETE FROM CLIENTE_CONTACTO WHERE ID_CLIENTE_CONTACTO =" + idContacto + " "
                + "AND ID_CLIENTE = " + idCliente;
        String delete_persona = "DELETE FROM PERSONA WHERE ID_PERSONA =" + idPersona;
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
                + "DIRECCION=" + contacto.getDireccion() + ", "
                + "TELEFONO=" + contacto.getTelefono() + ", "
                + "EMAIL=" + contacto.getEmail() + ", "
                + "OBSERVACION= " + contacto.getObservacion() + " "
                + "WHERE ID_CLIENTE_CONTACTO = " + contacto.getIdClienteContacto();
        String UPDATE_PERSONA = "UPDATE PERSONA SET "
                + "CI=" + contacto.getCedula() + ", "
                + "NOMBRE=" + contacto.getNombre() + ", "
                + "APELLIDO=" + contacto.getApellido() + ", "
                + "ID_SEXO=" + contacto.getId_sexo() + ", "
                + "FECHA_NACIMIENTO=" + contacto.getFecha_nacimiento() + ", "
                + "ID_ESTADO_CIVIL=" + contacto.getCedula() + ", "
                + "ID_PAIS=" + contacto.getId_pais() + ", "
                + "ID_CIUDAD" + contacto.getId_ciudad() + " "
                + "WHERE ID_PERSONA = " + contacto.getId_persona();
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
                + "NOMBRE= '" + cliente.getNombre() + "', "
                + "ENTIDAD= '" + cliente.getEntidad() + "', "
                + "RUC= '" + cliente.getRuc() + "', "
                + "RUC_IDENTIFICADOR= '" + cliente.getRucId() + "', "
                + "DIRECCION= '" + cliente.getDireccion() + "', "
                + "EMAIL= '" + cliente.getEmail() + "', "
                + "PAG_WEB= '" + cliente.getPaginaWeb() + "', "
                + "ID_TIPO= " + cliente.getIdTipo() + ", "
                + "ID_CATEGORIA= " + cliente.getIdCategoria() + ", "
                + "OBSERVACION= '" + cliente.getObservacion() + "' "
                + "WHERE ID_CLIENTE = " + cliente.getIdCliente();
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
                + "NUMERO='" + nroTelefono + "', "
                + "CATEGORIA= '" + tipoTelefono + "', "
                + "OBSERVACION='" + observacion + "' "
                + "WHERE ID_TELEFONO = " + idTelefono;
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
                + "numero, "
                + "categoria, "
                + "observacion"
                + ")VALUES ("
                + nroTelefono + "', '"
                + tipoTelefono + "', '"
                + observacion + "')";
        String insertTelProv = "INSERT INTO CLIENTE_TELEFONO("
                + "ID_CLIENTE, "
                + "ID_TELEFONO"
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
     String select = "SELECT CLIE.ID_CLIENTE \"ID\",CLIE.ENTIDAD \"Entidad\",CLIE.NOMBRE \"Nombre cliente\", CLIE.RUC \"R.U.C.\" ";
     String from = "FROM CLIENTE ";
     String where = "WHERE ";
     String orderBy = "ORDER BY CLIE.ENTIDAD ";
     String prov;
     if (exclusivo) {
     prov = toLowerCase + "%";
     } else {
     prov = "%" + toLowerCase + "%";
     }
     if (entidad && ruc) {
     where = where + "LOWER(CLIE.NOMBRE) LIKE '" + prov + "' OR LOWER(CLIE.ENTIDAD) LIKE '" + prov + "'  OR LOWER(CLIE.RUC)LIKE '" + prov + "' ";
     } else if (entidad) {
     where = where + "LOWER(CLIE.NOMBRE) LIKE '" + prov + "' ";
     } else if (ruc) {
     where = where + " LOWER(CLIE.RUC)LIKE '" + prov + "' ";
     } else if (!entidad && !ruc) {
     where = where + "LOWER(CLIE.NOMBRE) LIKE '" + prov + "' OR LOWER(CLIE.ENTIDAD) LIKE '" + prov + "'  OR LOWER(CLIE.RUC)LIKE '" + prov + "' ";
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
        String query = "SELECT TELE.ID_TELEFONO, TELE.NUMERO, TELE.ID_CATEGORIA, TELE.OBSERVACION  FROM TELEFONO TELE, CLIENTE CLIE, CLIENTE_TELEFONO CLTE WHERE CLIE.ID_CLIENTE = CLTE.ID_CLIENTE  AND CLTE.ID_TELEFONO = TELE.ID_TELEFONO"
                + " AND CLIE.ID_CLIENTE = " + idCliente + ";";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            telefonos = new ArrayList();
            while (rs.next()) {
                M_telefono telefono = new M_telefono();
                telefono.setId_telefono(rs.getInt("CLIE.ID_CLIENTE"));
                telefono.setCategoria(rs.getString("TELE.ID_CATEGORIA"));
                telefono.setNumero(rs.getString("TELE.NUMERO"));
                telefono.setObservacion(rs.getString("TELE.OBSERVACION"));
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
