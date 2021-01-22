/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_NotaCreditoCabecera;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaCabecera;
import Entities.E_facturaSinPago;
import Entities.E_movimientoContable;
import Entities.E_retencionVenta;
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
        String SELECT = "SELECT CLIE.ID_CLIENTE \"ID\", CLIE.ENTIDAD  \"Entidad\", CLIE.NOMBRE \"Nombre Cliente\", CLIE.RUC || '-' || CLIE.RUC_IDENTIFICADOR \"R.U.C.\" ";
        String FROM = "FROM CLIENTE CLIE ";
        String WHERE = "WHERE ";
        String ORDER_BY = " ORDER BY CLIE.ENTIDAD ";
        if (isExclusivo) {
            busqueda = busqueda + "%";
        } else {
            busqueda = "%" + busqueda + "%";
        }
        if (entidad && ruc) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE ? OR LOWER(CLIE.ENTIDAD) LIKE ? OR LOWER(CLIE.RUC) LIKE ? ";
        } else if (entidad) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE ? OR LOWER(CLIE.ENTIDAD) LIKE ? ";
        } else if (ruc) {
            WHERE = WHERE + "LOWER(CLIE.RUC) LIKE ? ";
        } else if (!entidad && !ruc) {
            WHERE = WHERE + "LOWER(CLIE.NOMBRE) LIKE ? OR LOWER(CLIE.ENTIDAD) LIKE ? OR LOWER(CLIE.RUC) LIKE ? ";
        }
        String QUERY = SELECT + FROM + WHERE + ORDER_BY;
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (entidad && ruc) {
                pst.setString(1, busqueda);
                pst.setString(2, busqueda);
                pst.setString(3, busqueda);
            } else if (entidad) {
                pst.setString(1, busqueda);
                pst.setString(2, busqueda);
            } else if (ruc) {
                pst.setString(1, busqueda);
            } else if (!entidad && !ruc) {
                pst.setString(1, busqueda);
                pst.setString(2, busqueda);
                pst.setString(3, busqueda);
            }
            rs = pst.executeQuery();
            rstm = new ResultSetTableModel(rs);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
        /*finally {
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
                categoria.add(rs.getString("descripcion"));
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
                tipo.add(rs.getString("descripcion"));
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
        String id_categoria = "SELECT ID_CLIENTE_CATEGORIA FROM CLIENTE_CATEGORIA WHERE DESCRIPCION LIKE '" + cliente.getCategoria() + "'";
        String id_tipo = "SELECT ID_CLIENTE_TIPO FROM CLIENTE_TIPO WHERE DESCRIPCION LIKE '" + cliente.getTipo() + "'";
        String insert_telefono = "INSERT INTO TELEFONO( NUMERO, ID_CATEGORIA, OBSERVACION)VALUES (?, ?, ?)";
        String insert_telefono_cliente = "INSERT INTO CLIENTE_TELEFONO(ID_CLIENTE, ID_TELEFONO)VALUES (?, ?)";
        String insert_sucursal = "INSERT INTO CLIENTE_SUCURSAL(ID_CLIENTE, DIRECCION, TELEFONO)VALUES (?, ?, ?)";
        String insert_contacto = "INSERT INTO CLIENTE_CONTACTO(ID_PERSONA, ID_CLIENTE, DIRECCION, TELEFONO, EMAIL, OBSERVACION) VALUES (?, ?, ?, ?, ?, ?)";
        String insert_persona = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
                    String telefonoCategoria = "SELECT ID_TELEFONO_CATEGORIA FROM TELEFONO_CATEGORIA WHERE DESCRIPCION LIKE '" + telefono[i].getCategoria() + "'";
                    st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    // se ejecuta el query y se obtienen los resultados en un ResultSet
                    rs = st.executeQuery(telefonoCategoria);
                    int id_categoria2 = 0;
                    while (rs.next()) {
                        id_categoria2 = rs.getInt("ID_TELEFONO_CATEGORIA");
                    }
                    pst = DB_manager.getConection().prepareStatement(insert_telefono, PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, telefono[i].getNumero());
                    pst.setInt(2, id_categoria2);
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
        }
        /*finally {
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
                + "TECA.DESCRIPCION \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, CLIENTE CLIE, CLIENTE_TELEFONO CLTE, TELEFONO_CATEGORIA TECA "
                + "WHERE TELE.ID_TELEFONO = CLTE.ID_TELEFONO "
                + "AND CLIE.ID_CLIENTE = CLTE.ID_CLIENTE "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
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
        }
        /*finally {
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
                + " PERS.FECHA_NACIMIENTO, PERS.ID_ESTADO_CIVIL,(SELECT ESCI.DESCRIPCION FROM ESTADO_CIVIL ESCI WHERE ESCI.ID_ESTADO_CIVIL = PERS.ID_ESTADO_CIVIL)\"ESTADO_CIVIL\", "
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
                contacto.setApellido(rs.getString("APELLIDO"));
                contacto.setCedula(rs.getInt("CI"));
                contacto.setCiudad(rs.getString("CIUDAD"));
                contacto.setDireccion(rs.getString("DIRECCION"));
                contacto.setEmail(rs.getString("EMAIL"));
                contacto.setEstado_civil(rs.getString("ESTADO_CIVIL"));
                contacto.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                contacto.setIdCliente(rs.getInt("ID_CLIENTE"));
                contacto.setIdClienteContacto(rs.getInt("ID_CLIENTE_CONTACTO"));
                contacto.setId_ciudad(rs.getInt("ID_CIUDAD"));
                contacto.setId_estado_civil(rs.getInt("ID_ESTADO_CIVIL"));
                contacto.setId_pais(rs.getInt("ID_PAIS"));
                contacto.setId_persona(rs.getInt("ID_PERSONA"));
                contacto.setId_sexo(rs.getInt("ID_SEXO"));
                contacto.setNombre(rs.getString("NOMBRE"));
                contacto.setObservacion(rs.getString("OBSERVACION"));
                contacto.setPais(rs.getString("PAIS"));
                contacto.setSexo(rs.getString("SEXO"));
                contacto.setTelefono(rs.getString("TELEFONO"));
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
        long id_persona = -1L;
        String INSERT_PERSONA = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String INSERT_PROVEEDOR_CONTACTO = "INSERT INTO CLIENTE_CONTACTO(ID_PERSONA, ID_CLIENTE, EMAIL, DIRECCION, TELEFONO, OBSERVACION)VALUES (?, ?, ?, ?, ?, ?)";
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
            pst.setInt(2, idCliente);
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
        }
    }

    public static void insertarSucursal(Integer idCliente, String direccion, String telefono) {
        String q = "INSERT INTO CLIENTE_SUCURSAL("
                + "ID_CLIENTE, "
                + "DIRECCION, "
                + "TELEFONO)"
                + "VALUES (?, ?, ?)";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(q);
            pst.setInt(1, idCliente);
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
        String updateContacto = "UPDATE CLIENTE_CONTACTO SET "
                + "EMAIL = ?, "
                + "DIRECCION = ?, "
                + "OBSERVACION = ?, "
                + "TELEFONO = ? "
                + "WHERE ID_CLIENTE_CONTACTO = ? ;";
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
        try {
            DB_manager.habilitarTransaccionManual();
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
            pst.setInt(5, contacto.getIdClienteContacto());
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

    public static void actualizarCliente(M_cliente cliente) {
        String updateProveedor = "UPDATE CLIENTE SET "
                + "NOMBRE = ?, "
                + "ENTIDAD = ?, "
                + "RUC = ?, "
                + "RUC_IDENTIFICADOR = ?, "
                + "DIRECCION = ?, "
                + "PAG_WEB = ?, "
                + "EMAIL = ?, "
                + "OBSERVACION = ?, "
                + "ID_TIPO= ?, "
                + "ID_CATEGORIA= ? "
                + "WHERE ID_CLIENTE = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(updateProveedor);
            //nombre
            if (cliente.getNombre() != null) {
                pst.setString(1, cliente.getNombre());
            } else {
                pst.setNull(1, Types.VARCHAR);
            }
            pst.setString(2, cliente.getEntidad());//not null
            //ruc
            if (cliente.getRuc() != null) {
                pst.setString(3, cliente.getRuc());
            } else {
                pst.setNull(3, Types.VARCHAR);
            }
            if (cliente.getRucId() != null) {
                pst.setString(4, cliente.getRucId());
            } else {
                pst.setNull(4, Types.VARCHAR);
            }
            if (cliente.getDireccion() != null) {
                pst.setString(5, cliente.getDireccion());
            } else {
                pst.setNull(5, Types.VARCHAR);
            }
            if (cliente.getPaginaWeb() != null) {
                pst.setString(6, cliente.getPaginaWeb());
            } else {
                pst.setNull(6, Types.VARCHAR);
            }
            if (cliente.getEmail() != null) {
                pst.setString(7, cliente.getEmail());
            } else {
                pst.setNull(7, Types.VARCHAR);
            }
            if (cliente.getObservacion() != null) {
                pst.setString(8, cliente.getObservacion());
            } else {
                pst.setNull(8, Types.VARCHAR);
            }
            pst.setInt(9, cliente.getIdTipo());
            pst.setInt(10, cliente.getIdCategoria());
            pst.setInt(11, cliente.getIdCliente());
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
        String INSERT_TELEFONO = "INSERT INTO TELEFONO(NUMERO, ID_CATEGORIA, OBSERVACION)VALUES (?, ?, ?)";
        String INSERT_TELEFONO_PROVEEDOR = "INSERT INTO CLIENTE_TELEFONO(ID_CLIENTE, ID_TELEFONO)VALUES (?, ?)";
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
            pst.setInt(1, idCliente);
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
                telefono.setId_telefono(rs.getInt("ID_TELEFONO"));
                telefono.setCategoria(rs.getString("ID_CATEGORIA"));
                telefono.setNumero(rs.getString("NUMERO"));
                telefono.setObservacion(rs.getString("OBSERVACION"));
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

    public static M_cliente obtenerDatosCliente(String entidad) {
        M_cliente cliente = null;
        String query = "SELECT CLIE.ID_CLIENTE, "
                + "CLIE.NOMBRE, "
                + "CLIE.ENTIDAD, "
                + "CLIE.RUC, "
                + "CLIE.RUC_IDENTIFICADOR, "
                + "CLCA.DESCRIPCION \"CLCA_DESCRIPCION\", "
                + "CLTI.DESCRIPCION \"CLTI_DESCRIPCION\", "
                + "CLIE.ID_TIPO, "
                + "CLIE.ID_CATEGORIA, "
                + "CLIE.DIRECCION, "
                + "CLIE.PAG_WEB, "
                + "CLIE.EMAIL "
                + "FROM CLIENTE CLIE, CLIENTE_TIPO CLTI, CLIENTE_CATEGORIA CLCA "
                + "WHERE CLIE.ID_CATEGORIA = CLCA.ID_CLIENTE_CATEGORIA "
                + "AND CLIE.ID_TIPO = CLTI.ID_CLIENTE_TIPO "
                + "AND CLIE.ENTIDAD LIKE '" + entidad + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            while (rs.next()) {
                cliente = new M_cliente();
                cliente.setPaginaWeb(rs.getString("PAG_WEB"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setEmail(rs.getString("EMAIL"));
                cliente.setEntidad(rs.getString("ENTIDAD"));
                cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setRuc(rs.getString("RUC"));
                cliente.setRucId(rs.getString("RUC_IDENTIFICADOR"));
                cliente.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cliente.setIdTipo(rs.getInt("ID_TIPO"));
                cliente.setCategoria(rs.getString("CLCA_DESCRIPCION"));
                cliente.setTipo(rs.getString("CLTI_DESCRIPCION"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return cliente;
    }

    public static boolean existeRuc(String ruc) {
        String query = "SELECT RUC FROM CLIENTE WHERE RUC = ?;";
        try {
            pst = DB_manager.getConection().prepareStatement(query);
            pst.setString(1, ruc);
            rs = pst.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void modificarSucursal(int id_sucursal, String direccion, String telefono) {
        String q = "UPDATE CLIENTE_SUCURSAL SET "
                + "DIRECCION = ?, "
                + "TELEFONO = ? "
                + "WHERE ID_CLIENTE_SUCURSAL = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(q);
            pst.setString(1, direccion);
            pst.setString(2, telefono);
            pst.setInt(3, id_sucursal);
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

    public static void eliminarSucursal(int id_sucursal) {
        String q = "DELETE FROM CLIENTE_SUCURSAL WHERE ID_CLIENTE_SUCURSAL =" + id_sucursal;
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
        }
    }

    public static ResultSetTableModel obtenerClienteTelefonoCompleto(Integer idCliente) {
        ResultSetTableModel rstm = null;
        String Query = "SELECT TELE.ID_TELEFONO \"ID\", "
                + "TELE.NUMERO \"Número\", "
                + "TECA.DESCRIPCION \"Categoría\", "
                + "TELE.OBSERVACION \"Observación\" "
                + "FROM TELEFONO TELE, CLIENTE CLIE, CLIENTE_TELEFONO CLTE, TELEFONO_CATEGORIA TECA "
                + "WHERE TELE.ID_TELEFONO = CLTE.ID_TELEFONO "
                + "AND CLIE.ID_CLIENTE = CLTE.ID_CLIENTE "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
                + "AND CLIE.ID_CLIENTE = " + idCliente;
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(Query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Proveedor.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rstm;
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
        }
    }

    public static ArrayList<M_telefono> obtenerTelefonos(Integer idCliente) {
        ArrayList<M_telefono> telefonos = null;
        String QUERY = "SELECT TELE.ID_TELEFONO, TELE.NUMERO, TECA.ID_TELEFONO_CATEGORIA, TECA.DESCRIPCION, "
                + "TELE.OBSERVACION "
                + "FROM TELEFONO TELE, TELEFONO_CATEGORIA TECA, "
                + "CLIENTE_TELEFONO CLTE "
                + "WHERE TELE.ID_TELEFONO =  CLTE.ID_TELEFONO  "
                + "AND TELE.ID_CATEGORIA = TECA.ID_TELEFONO_CATEGORIA "
                + "AND CLTE.ID_CLIENTE = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCliente);
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
            Logger lgr = Logger.getLogger(DB_Cliente.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return telefonos;
    }

    public static void eliminarTelefono(int id_telefono) {
        String Q2 = "DELETE FROM TELEFONO WHERE ID_TELEFONO =" + id_telefono;
        String Q1 = "DELETE FROM CLIENTE_TELEFONO WHERE ID_TELEFONO =" + id_telefono;
        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(Q1);
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(Q2);
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
            Logger lgr = Logger.getLogger(DB_Cliente.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int obtenerIdCategoria(String categoria) {
        Integer idCategoria = null;
        String q = "SELECT ID_CLIENTE_CATEGORIA "
                + "FROM CLIENTE_CATEGORIA "
                + "WHERE DESCRIPCION LIKE '" + categoria + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                idCategoria = (rs.getInt("ID_CLIENTE_CATEGORIA"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idCategoria;
    }

    public static int obtenerIdTipo(String tipo) {
        Integer idCategoria = null;
        String q = "SELECT ID_CLIENTE_TIPO "
                + "FROM CLIENTE_TIPO "
                + "WHERE DESCRIPCION LIKE '" + tipo + "'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                idCategoria = (rs.getInt("ID_CLIENTE_TIPO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idCategoria;
    }

    public static ArrayList<E_movimientoContable> obtenerEstadoCuenta(int idCliente) {
        ArrayList<E_movimientoContable> list = new ArrayList<>();
        String QUERY = "SELECT tipo_documento, nro_factura, nro_recibo, nro_nota_credito, nro_retencion, "
                + "fecha, id_cliente, cliente, monto, pago, saldo, ruc, "
                + "ruc_identificador "
                + "FROM public.v_documentos_comerciales_ventas "
                + "WHERE id_cliente = ? "
                + "ORDER BY fecha, nro_recibo, nro_factura, nro_nota_credito, nro_retencion  asc;";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_movimientoContable movCont = new E_movimientoContable();
                switch (rs.getString(1)) {
                    case E_movimientoContable.STR_TIPO_SALDO_INICIAL: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        cliente.setSaldoInicial(rs.getInt("monto"));
                        movCont.setTipo(E_movimientoContable.TIPO_SALDO_INICIAL);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_SALDO_INICIAL);
                        movCont.setClienteSaldoInicial(cliente);
                        movCont.setFechaSaldoInicial(rs.getDate("fecha"));
                        break;
                    }
                    case E_movimientoContable.STR_TIPO_VENTA: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        E_facturaSinPago fsp = new E_facturaSinPago();
                        fsp.setCliente(cliente);
                        fsp.setMonto(rs.getInt("monto"));
                        fsp.setFecha(rs.getDate("fecha"));
                        fsp.setIdCabecera(0);
                        fsp.setNroFactura(rs.getInt("nro_factura"));
                        movCont.setTipo(E_movimientoContable.TIPO_VENTA);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_VENTA);
                        movCont.setVenta(fsp);
                        break;
                    }/*
                    case E_movimientoContable.STR_TIPO_COBRO: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        E_cuentaCorrienteCabecera recibo = new E_cuentaCorrienteCabecera();
                        recibo.setCliente(cliente);
                        recibo.setDebito(rs.getInt("pago"));
                        recibo.setFechaPago(rs.getDate("fecha"));
                        recibo.setId(0);
                        recibo.setNroRecibo(rs.getInt("nro_recibo"));
                        recibo.setNroRecibo(rs.getInt("nro_recibo"));
                        movCont.setTipo(E_movimientoContable.TIPO_COBRO);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_COBRO);
                        movCont.setCobro(recibo);
                        break;
                    }*/
                    case E_movimientoContable.STR_TIPO_COBRO: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        E_facturaCabecera facturaCabecera = new E_facturaCabecera();
                        facturaCabecera.setCliente(cliente);
                        facturaCabecera.setNroFactura(rs.getInt("nro_factura"));
                        E_cuentaCorrienteCabecera reciboCabecera = new E_cuentaCorrienteCabecera();
                        reciboCabecera.setNroRecibo(rs.getInt("nro_recibo"));
                        reciboCabecera.setFechaPago(rs.getDate("fecha"));
                        E_cuentaCorrienteDetalle recibo = new E_cuentaCorrienteDetalle();
                        recibo.setCuentaCorrienteCabecera(reciboCabecera);
                        recibo.setFacturaVenta(facturaCabecera);
                        recibo.setMonto(rs.getInt("pago"));
                        recibo.setId(0);
                        movCont.setTipo(E_movimientoContable.TIPO_COBRO);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_COBRO);
                        movCont.setCobro(recibo);
                        break;
                    }
                    case E_movimientoContable.STR_TIPO_NOTA_CREDITO: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        E_NotaCreditoCabecera notaCredito = new E_NotaCreditoCabecera();
                        notaCredito.setCliente(cliente);
                        E_facturaCabecera facturaCabecera = new E_facturaCabecera();
                        facturaCabecera.setNroFactura(rs.getInt("nro_factura"));
                        notaCredito.setFacturaCabecera(facturaCabecera);
                        notaCredito.setTotal(rs.getInt("pago"));
                        notaCredito.setTiempo(rs.getDate("fecha"));
                        notaCredito.setId(0);
                        notaCredito.setNroNotaCredito(rs.getInt("nro_nota_credito"));
                        movCont.setTipo(E_movimientoContable.TIPO_NOTA_CREDITO);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_NOTA_CREDITO);
                        movCont.setNotaCredito(notaCredito);
                        break;
                    }
                    case E_movimientoContable.STR_TIPO_RETENCION_VENTA: {
                        M_cliente cliente = new M_cliente();
                        cliente.setIdCliente(rs.getInt("id_cliente"));
                        cliente.setEntidad(rs.getString("cliente"));
                        E_facturaCabecera facturaCabecera = new E_facturaCabecera();
                        facturaCabecera.setNroFactura(rs.getInt("nro_factura"));
                        facturaCabecera.setCliente(cliente);
                        E_retencionVenta retencion = new E_retencionVenta();
                        retencion.setVenta(facturaCabecera);
                        retencion.setMonto(rs.getInt("pago"));
                        retencion.setTiempo(rs.getDate("fecha"));
                        retencion.setId(0);
                        retencion.setNroRetencion(rs.getInt("nro_retencion"));
                        movCont.setTipo(E_movimientoContable.TIPO_RETENCION_VENTA);
                        movCont.setTipoDescripcion(E_movimientoContable.STR_TIPO_RETENCION_VENTA);
                        movCont.setRetencionVenta(retencion);
                        break;
                    }
                }
                list.add(movCont);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
}
