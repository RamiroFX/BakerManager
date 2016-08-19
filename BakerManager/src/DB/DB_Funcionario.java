/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.M_funcionario;
import Entities.M_rol;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class DB_Funcionario {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    /*
     * CREATE
     */

    public static void insertarFuncionario(M_funcionario funcionario, ArrayList rol, String pass) {
        String idSexo = "(SELECT ID_SEXO FROM SEXO WHERE DESCRIPCION LIKE'" + funcionario.getSexo() + "%')";
        String idEstadoCivil = "(SELECT ID_ESTADO_CIVIL FROM ESTADO_CIVIL WHERE DESCRIPCION LIKE'" + funcionario.getEstado_civil() + "%')";
        String idPais = "(SELECT ID_PAIS FROM PAIS WHERE DESCRIPCION LIKE'" + funcionario.getPais() + "%')";
        String idCiudad = "(SELECT id_ciudad FROM CIUDAD WHERE DESCRIPCION LIKE'" + funcionario.getCiudad() + "%')";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String insPersona = "INSERT INTO PERSONA "
                + "(CI,NOMBRE,APELLIDO,"
                + "SEXO,FECHA_NACIMIENTO,ESTADO_CIVIL,"
                + "ID_PAIS,ID_CIUDAD)"
                + "VALUES (" + funcionario.getCedula() + ",'" + funcionario.getNombre() + "','" + funcionario.getApellido() + "'," + idSexo + ",to_date('" + sdf.format(funcionario.getFecha_nacimiento()) + "','dd/mm/yyyy')," + idEstadoCivil + "," + idPais + "," + idCiudad + ")";
        String insFuncionario = "INSERT INTO FUNCIONARIO"
                + "( id_persona, alias,fecha_ingreso, estado,salario, nro_celular, nro_telefono,email, direccion, observacion)"
                + "VALUES(" + funcionario.getId_persona() + ",'" + funcionario.getAlias() + "',TO_DATE('" + sdf.format(funcionario.getFecha_ingreso()) + "','dd/mm/yyyy')," + funcionario.getNro_celular() + "," + funcionario.getNro_telefono() + ",'" + funcionario.getEmail() + "','" + funcionario.getDireccion() + "','" + funcionario.getObservacion() + "')";
        System.out.println("Insert persona: " + insPersona);
        System.out.println("Insert cliente: " + insFuncionario);
        String createUser = "CREATE USER " + funcionario.getAlias() + " "
                + " PASSWORD '" + pass + "'"
                + " NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN";

        try {
            System.out.println("insertarFuncionario(): Insertando datos personales: ");
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(insPersona, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.executeUpdate();
            pst.close();
            System.out.println("insertarFuncionario(): Insertando datos funcionario: ");
            pst = DB_manager.getConection().prepareStatement(insFuncionario, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.executeUpdate();
            pst.close();
            System.out.println("insertarFuncionario(): Creando usuario: ");
            pst = DB_manager.getConection().prepareStatement(createUser, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.executeUpdate();
            pst.close();
            System.out.println("insertarFuncionario(): Asignando roles");
            for (int i = 0; i < rol.size(); i++) {
                M_rol _rol = (M_rol) rol.get(i);
                String as = "INSERT INTO ROL_USUARIO (ID_ROL,ID_FUNCIONARIO)VALUES(" + _rol.getId() + ",+" + funcionario.getId_funcionario() + ")";
                pst = DB_manager.getConection().prepareStatement(as, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst.executeUpdate();
                pst.close();
            }
            for (int i = 0; i < rol.size(); i++) {
                M_rol _rol = (M_rol) rol.get(i);
                System.out.println("_rol.getDescripcion() = " + _rol.getDescripcion());
                if (_rol.getDescripcion().equals("ADMINISTRADOR")) {
                    String select = "GRANT administrador TO " + funcionario.getAlias() + "";
                    System.out.println("GRANTING DBA: " + select);
                    pst = DB_manager.getConection().prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.executeUpdate();
                    pst.close();
                } else if (_rol.getDescripcion().equals("COMPRAS")) {
                    String select = "GRANT compras TO " + funcionario.getAlias() + "";
                    System.out.println("GRANTING compras: " + select);
                    pst = DB_manager.getConection().prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.executeUpdate();
                    pst.close();
                } else if (_rol.getDescripcion().equals("VENTAS")) {
                    String select = "GRANT ventas TO " + funcionario.getAlias() + "";
                    System.out.println("GRANTING ventas: " + select);
                    pst = DB_manager.getConection().prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.executeUpdate();
                    pst.close();
                } else if (_rol.getDescripcion().equals("PRODUCTOS")) {
                    String select = "GRANT productos TO " + funcionario.getAlias() + "";
                    System.out.println("GRANTING productos: " + select);
                    pst = DB_manager.getConection().prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.executeUpdate();
                    pst.close();
                }
            }
            System.out.println("insertarFuncionario(): Se inserto exitosamente");
            JOptionPane.showMessageDialog(null, "Funcionario insertado", "Exito", JOptionPane.INFORMATION_MESSAGE);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    /*
     * Esta funcion solo inserta el funcionario en la tabla persona y tabla funcionario
     * sin crear un usuario de base de datos.
     */
    public static void insertarFuncionarioFX(M_funcionario funcionario, ArrayList rol, String pass) {
        long id_persona = -1L;
        long id_funcionario = -1L;
        String id_ciudad = "SELECT ID_CIUDAD FROM CIUDAD WHERE DESCRIPCION LIKE'" + funcionario.getCiudad() + "'";
        String id_pais = "SELECT ID_PAIS FROM PAIS WHERE DESCRIPCION LIKE'" + funcionario.getPais() + "'";
        String id_estado_civil = "SELECT ID_ESTADO_CIVIL FROM ESTADO_CIVIL WHERE DESCRIPCION LIKE'" + funcionario.getEstado_civil() + "'";
        String id_sexo = "SELECT ID_SEXO FROM SEXO WHERE DESCRIPCION LIKE'" + funcionario.getSexo() + "'";
        String INSERT_FUNCIONARIO = "INSERT INTO FUNCIONARIO(ID_PERSONA, ALIAS, FECHA_INGRESO, NRO_CELULAR, NRO_TELEFONO, EMAIL, DIRECCION, OBSERVACION, PASSWORD)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String INSERT_PERSONA = "INSERT INTO PERSONA(CI, NOMBRE, APELLIDO, ID_SEXO, FECHA_NACIMIENTO, ID_ESTADO_CIVIL, ID_PAIS, ID_CIUDAD)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(id_ciudad, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_ciudad(rs.getInt("ID_CIUDAD"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_pais, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_pais(rs.getInt("ID_PAIS"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_estado_civil, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_estado_civil(rs.getInt("ID_ESTADO_CIVIL"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_sexo, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_sexo(rs.getInt("ID_SEXO"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA, PreparedStatement.RETURN_GENERATED_KEYS);
            try {
                if (funcionario.getCedula() == null) {
                    pst.setNull(1, Types.INTEGER);
                } else {
                    pst.setInt(1, (int) funcionario.getCedula());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.INTEGER);
            }
            pst.setString(2, funcionario.getNombre());
            pst.setString(3, funcionario.getApellido());
            pst.setInt(4, funcionario.getId_sexo());
            try {
                if (funcionario.getFecha_nacimiento() == null) {
                    pst.setNull(5, Types.DATE);
                } else {
                    pst.setDate(5, new java.sql.Date(funcionario.getFecha_nacimiento().getTime()));
                }
            } catch (Exception e) {
                pst.setNull(5, Types.DATE);
            }
            pst.setInt(6, funcionario.getId_estado_civil());
            pst.setInt(7, funcionario.getId_pais());
            pst.setInt(8, funcionario.getId_ciudad());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs != null && rs.next()) {
                id_persona = rs.getLong(1);
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(INSERT_FUNCIONARIO, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, (int) id_persona);
            pst.setString(2, funcionario.getAlias());//NOT NULL
            try {
                if (funcionario.getFecha_ingreso() == null) {
                    pst.setNull(3, Types.DATE);
                } else {
                    pst.setDate(3, new java.sql.Date(funcionario.getFecha_ingreso().getTime()));
                }
            } catch (Exception e) {
                pst.setNull(3, Types.DATE);
            }
            try {
                if (funcionario.getNro_celular() == null) {
                    pst.setNull(4, Types.VARCHAR);
                } else {
                    pst.setString(4, funcionario.getNro_celular());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.VARCHAR);
            }
            try {
                if (funcionario.getNro_telefono() == null) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, funcionario.getNro_telefono());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (funcionario.getEmail() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, funcionario.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (funcionario.getDireccion() == null) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, funcionario.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            try {
                if (funcionario.getObservacion() == null) {
                    pst.setNull(8, Types.VARCHAR);
                } else {
                    pst.setString(8, funcionario.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(8, Types.VARCHAR);
            }
            pst.setString(9, funcionario.getPassword());//NOT NULL
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs != null && rs.next()) {
                id_funcionario = rs.getLong(1);
            }
            rs.close();
            pst.close();
            for (int i = 0; i < rol.size(); i++) {
                M_rol _rol = (M_rol) rol.get(i);
                String as = "INSERT INTO ROL_USUARIO (ID_ROL,ID_FUNCIONARIO)VALUES(" + _rol.getId() + "," + id_funcionario + ")";
                pst = DB_manager.getConection().prepareStatement(as);
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
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static void insertarRolUsuario(M_funcionario funcionario, M_rol rol) {
        String INSERT_PERSONA = "INSERT INTO ROL_USUARIO(ID_ROL, ID_FUNCIONARIO) VALUES (?, ?);";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(INSERT_PERSONA);
            pst.setInt(1, rol.getId());
            pst.setInt(2, funcionario.getId_funcionario());
            pst.executeUpdate();
            rs.close();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /*
     * READ
     */
    public static M_funcionario obtenerDatosFuncionarioID(int idFuncionario) {
        M_funcionario f = null;
        String genero = "(SELECT DESCRIPCION  FROM SEXO WHERE ID_SEXO = P.ID_SEXO) \"sexo\"";
        String pais = "(SELECT DESCRIPCION FROM PAIS WHERE ID_PAIS=P.ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT DESCRIPCION FROM CIUDAD WHERE ID_CIUDAD=P.ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT DESCRIPCION FROM ESTADO_CIVIL WHERE ID_ESTADO_CIVIL=P.ID_ESTADO_CIVIL)\"estado_civil\"";
        String queryFunc = "id_funcionario, f.id_persona, alias,fecha_ingreso, nro_celular, nro_telefono,email, direccion, observacion";
        String queryPers = "p.id_persona, ci, nombre, apellido," + genero + ", fecha_nacimiento, " + pais + "," + ciudad + ", " + estadoCivil + "";
        String Query = "SELECT " + queryFunc + "," + queryPers + " FROM funcionario f, persona p " + "WHERE f.id_persona = p.id_persona " + "AND id_funcionario =" + idFuncionario;

        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            while (rs.next()) {
                f = new M_funcionario();
                f.setPais(rs.getString("NACIONALIDAD"));
                f.setCiudad(rs.getString("CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("sexo"));
                f.setNro_celular(rs.getString("nro_celular"));
                f.setNro_telefono(rs.getString("nro_telefono"));
                f.setEmail(rs.getString("email"));
                f.setDireccion(rs.getString("DIRECCION"));
                f.setAlias(rs.getString("alias"));
                f.setNombre(rs.getString("nombre"));
                f.setApellido(rs.getString("apellido"));
                f.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                f.setId_persona(rs.getInt("id_persona"));
                f.setCedula(rs.getInt("ci"));
                f.setEstado_civil(rs.getString("estado_civil"));
                f.setId_funcionario(rs.getInt("id_funcionario"));
                f.setObservacion(rs.getString("OBSERVACION"));
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
        return f;
    }

    public static M_funcionario obtenerDatosFuncionario(String alias, String password) {
        M_funcionario f = null;
        String genero = "(SELECT DESCRIPCION  FROM SEXO WHERE ID_SEXO = P.ID_SEXO) \"sexo\"";
        String pais = "(SELECT DESCRIPCION FROM PAIS WHERE ID_PAIS=P.ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT DESCRIPCION FROM CIUDAD WHERE ID_CIUDAD=P.ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT DESCRIPCION FROM ESTADO_CIVIL WHERE ID_ESTADO_CIVIL= P.ID_ESTADO_CIVIL)\"estado_civil\"";
        String queryFunc = "F.id_funcionario, P.id_persona, alias, fecha_ingreso, nro_celular, nro_telefono,email, direccion, observacion";
        String queryPers = "ci, nombre, apellido," + genero + ", fecha_nacimiento, " + pais + "," + ciudad + ", " + estadoCivil + "";
        String Query = "SELECT " + queryFunc + "," + queryPers + " FROM funcionario F, persona P " + "WHERE F.id_persona = P.id_persona "
                + "AND (ALIAS = '" + alias + "' AND PASSWORD ='" + password + "');";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Query);
            while (rs.next()) {
                f = new M_funcionario();
                f.setPais(rs.getString("NACIONALIDAD"));
                f.setCiudad(rs.getString("CIUDAD"));
                f.setFecha_nacimiento(rs.getDate("FECHA_NACIMIENTO"));
                f.setSexo(rs.getString("sexo"));
                f.setNro_celular(rs.getString("nro_celular"));
                f.setNro_telefono(rs.getString("nro_telefono"));
                f.setEmail(rs.getString("email"));
                f.setDireccion(rs.getString("DIRECCION"));
                f.setAlias(rs.getString("alias"));
                f.setNombre(rs.getString("nombre"));
                f.setApellido(rs.getString("apellido"));
                f.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                f.setId_persona(rs.getInt("id_persona"));
                f.setCedula(rs.getInt("ci"));
                f.setEstado_civil(rs.getString("estado_civil"));
                f.setId_funcionario(rs.getInt("id_funcionario"));
                f.setObservacion(rs.getString("OBSERVACION"));
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
        return f;
    }

    public static Vector obtenerRolFuncionario(M_funcionario f) {
        Vector rol = null;
        String q = "SELECT descripcion "
                + "FROM funcionario f, rol r, rol_usuario ru "
                + "WHERE ru.id_funcionario = f.id_funcionario "
                + "AND ru.id_rol = r.id_rol "
                + "AND f.alias LIKE ('" + f.getAlias() + "')";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rol = new Vector();
            while (rs.next()) {
                rol.add(rs.getString("descripcion"));
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
        return rol;
    }

    public static M_rol obtenerRolesFuncionario(M_funcionario f) {
        M_rol roles = null;
        String q = "SELECT descripcion "
                + "FROM funcionario f, rol r, usuario ru "
                + "WHERE ru.id_funcionario = f.id_funcionario "
                + "AND ru.id_rol = r.id_rol "
                + "AND f.alias LIKE ('" + f.getAlias() + "')";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            rs.last();
            roles = new M_rol();
            rs.beforeFirst();
            while (rs.next()) {
                //roles.getRol()[rs.getRow()-1]=(rs.getString("descripcion"));
                roles = new M_rol(rs.getInt("id_rol"), rs.getString("descripcion"));
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
        return roles;
    }

    public static M_rol obtenerRol(String rolDescripcion) {
        M_rol roles = null;
        String q = "SELECT id_rol,descripcion "
                + "FROM rol "
                + "WHERE descripcion  LIKE ('" + rolDescripcion + "')";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            while (rs.next()) {
                roles = new M_rol(rs.getInt("id_rol"), rs.getString("descripcion"));
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
        return roles;
    }

    public static Vector obtenerChoferes() {
        Vector funcionario = null;
        String q = "SELECT "
                + "NOMBRE, "
                + "APELLIDO "
                + "FROM PERSONA P, FUNCIONARIO F, ROL_USUARIO, ROL "
                + "WHERE P.ID_PERSONA = F.ID_PERSONA "
                + "AND ID_FUNCIONARIO = ID_FUNCIONARIO "
                + "AND ID_ROL = ROL_ID_ROL "
                + "AND ROL_DESCRIPCION LIKE 'CHOFER'";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            funcionario = new Vector();
            while (rs.next()) {
                funcionario.add(rs.getString("nombre") + " " + rs.getString("apellido"));
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
        return funcionario;
    }

    public static ResultSetTableModel consultarFuncionario(String busqueda, boolean isExclusivo, boolean entidad, boolean ci) {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            /*String genero = "(SELECT DESCRIPCION  FROM SEXO WHERE ID_SEXO = SEXO) \"Genero\"";
             String pais = "(SELECT DESCRIPCION FROM PAIS WHERE ID_PAIS=ID_PAIS) \"Nacionalidad\"";
             String ciudad = " (SELECT DESCRIPCION FROM CIUDAD WHERE ID_CIUDAD=ID_CIUDAD)\"Ciudad\"";
             String estadoCivil = " (SELECT DESCRIPCION FROM ESTADO_CIVIL WHERE ID_ESTADO_CIVIL=ESTADO_CIVIL)\"Estado civil\"";
             String estado = " (SELECT DESCRIPCION FROM ESTADO WHERE ID_ESTADO=ESTADO)\"Estado\"";
             String q2 = "SELECT ID_FUNCIONARIO\"ID\",ALIAS \"Alias\", CI\"CI\",NOMBRE\"Nombre\",APELLIDO\"Apellido\" "
             + "FROM funcionario, persona " + ""
             + "WHERE id_persona = id_persona " + "AND nombre LIKE '" + busqueda + "%'";
             */

            String SELECT = "SELECT ID_FUNCIONARIO\"ID\",ALIAS \"Alias\", CI\"CI\",NOMBRE\"Nombre\",APELLIDO\"Apellido\" ";
            String FROM = "FROM funcionario f, persona p ";
            String WHERE = "WHERE f.id_persona = p.id_persona AND ";
            String ORDER_BY = " ORDER BY NOMBRE ";
            if (isExclusivo) {
                busqueda = busqueda + "%";
            } else {
                busqueda = "%" + busqueda + "%";
            }
            if (entidad && ci) {
                WHERE = WHERE + "(LOWER(nombre) LIKE '" + busqueda + "' OR LOWER(APELLIDO) LIKE '" + busqueda + "' OR CAST(CI AS TEXT) LIKE '" + busqueda + "')";
            } else if (entidad) {
                WHERE = WHERE + "(LOWER(nombre) LIKE '" + busqueda + "' OR LOWER(APELLIDO) LIKE '" + busqueda + "')";
            } else if (ci) {
                WHERE = WHERE + "CAST(CI AS TEXT) LIKE  LIKE '" + busqueda + "'";
            } else if (!entidad && !ci) {
                WHERE = WHERE + "(LOWER(nombre) LIKE '" + busqueda + "' OR LOWER(APELLIDO) LIKE '" + busqueda + "' OR CAST(CI AS TEXT) LIKE '" + busqueda + "')";
            }
            String query = SELECT + FROM + WHERE + ORDER_BY;
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            System.out.println("632-DB_funcionario: "+query);
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static ResultSetTableModel consultarFuncionarioChofer() {
        ResultSetTableModel rstm = null;
        try {
            if (DB_manager.getConection() == null) {
                throw new IllegalStateException("Connection already closed.");
            }
            String query = "SELECT "
                    + "ID_FUNCIONARIO \"ID\", "
                    + "NOMBRE \"Nombre\", "
                    + "APELLIDO \"Apellido\", "
                    + "CI \"Cedula de identidad\", "
                    + "FECHA_NACIMIENTO \"Fecha de nacimiento\" "
                    + "FROM "
                    + "FUNCIONARIO, "
                    + "PERSONA, "
                    + "ROL, "
                    + "ROL_USUARIO "
                    + "WHERE "
                    + "ID_PERSONA = ID_PERSONA AND "
                    + "ID_FUNCIONARIO = ID_FUNCIONARIO AND "
                    + "ID_ROL = ROL_ID_ROL AND "
                    + "ROL_DESCRIPCION LIKE 'CHOFER' AND "
                    + "ESTADO = 2";
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    /*
     * UPDATE
     */
    public static void actualizarFuncionario(M_funcionario funcionario) {
        System.out.println("actualizarFuncionario: " + funcionario.getId_funcionario());
        String id_ciudad = "SELECT ID_CIUDAD FROM CIUDAD WHERE DESCRIPCION LIKE'" + funcionario.getCiudad() + "'";
        String id_pais = "SELECT ID_PAIS FROM PAIS WHERE DESCRIPCION LIKE'" + funcionario.getPais() + "'";
        String id_estado_civil = "SELECT ID_ESTADO_CIVIL FROM ESTADO_CIVIL WHERE DESCRIPCION LIKE'" + funcionario.getEstado_civil() + "'";
        String id_sexo = "SELECT ID_SEXO FROM SEXO WHERE DESCRIPCION LIKE'" + funcionario.getSexo() + "'";
        String UPDATE_FUNCIONARIO = "UPDATE FUNCIONARIO SET ALIAS=?, "
                + "FECHA_INGRESO=?, NRO_CELULAR=?, NRO_TELEFONO=?, "
                + "EMAIL=?, DIRECCION=?, OBSERVACION=? "
                + "WHERE ID_FUNCIONARIO = ?;";
        String UPDATE_PERSONA = "UPDATE PERSONA SET CI=?, NOMBRE=?, "
                + "APELLIDO=?, ID_SEXO=?, FECHA_NACIMIENTO=?, "
                + "ID_ESTADO_CIVIL=?, ID_PAIS=?, ID_CIUDAD=? "
                + "WHERE ID_PERSONA = ?;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(id_ciudad, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_ciudad(rs.getInt("ID_CIUDAD"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_pais, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_pais(rs.getInt("ID_PAIS"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_estado_civil, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_estado_civil(rs.getInt("ID_ESTADO_CIVIL"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(id_sexo, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                funcionario.setId_sexo(rs.getInt("ID_SEXO"));
            }
            rs.close();
            pst.close();
            pst = DB_manager.getConection().prepareStatement(UPDATE_FUNCIONARIO);
            pst.setString(1, funcionario.getAlias());//NOT NULL
            try {
                if (funcionario.getFecha_ingreso() == null) {
                    pst.setNull(2, Types.DATE);
                } else {
                    pst.setDate(2, new java.sql.Date(funcionario.getFecha_ingreso().getTime()));
                }
            } catch (Exception e) {
                pst.setNull(2, Types.DATE);
            }
            try {
                if (funcionario.getNro_celular() == null) {
                    pst.setNull(3, Types.VARCHAR);
                } else {
                    pst.setString(3, funcionario.getNro_celular());
                }
            } catch (Exception e) {
                pst.setNull(3, Types.VARCHAR);
            }
            try {
                if (funcionario.getNro_telefono() == null) {
                    pst.setNull(4, Types.VARCHAR);
                } else {
                    pst.setString(4, funcionario.getNro_telefono());
                }
            } catch (Exception e) {
                pst.setNull(4, Types.VARCHAR);
            }
            try {
                if (funcionario.getEmail() == null) {
                    pst.setNull(5, Types.VARCHAR);
                } else {
                    pst.setString(5, funcionario.getEmail());
                }
            } catch (Exception e) {
                pst.setNull(5, Types.VARCHAR);
            }
            try {
                if (funcionario.getDireccion() == null) {
                    pst.setNull(6, Types.VARCHAR);
                } else {
                    pst.setString(6, funcionario.getDireccion());
                }
            } catch (Exception e) {
                pst.setNull(6, Types.VARCHAR);
            }
            try {
                if (funcionario.getObservacion() == null) {
                    pst.setNull(7, Types.VARCHAR);
                } else {
                    pst.setString(7, funcionario.getObservacion());
                }
            } catch (Exception e) {
                pst.setNull(7, Types.VARCHAR);
            }
            pst.setInt(8, funcionario.getId_funcionario());
            pst.executeUpdate();
            pst = DB_manager.getConection().prepareStatement(UPDATE_PERSONA);
            try {
                if (funcionario.getCedula() == null) {
                    pst.setNull(1, Types.INTEGER);
                } else {
                    pst.setInt(1, (int) funcionario.getCedula());
                }
            } catch (Exception e) {
                pst.setNull(1, Types.INTEGER);
            }
            pst.setString(2, funcionario.getNombre());
            pst.setString(3, funcionario.getApellido());
            pst.setInt(4, funcionario.getId_sexo());
            try {
                if (funcionario.getFecha_nacimiento() == null) {
                    pst.setNull(5, Types.DATE);
                } else {
                    pst.setDate(5, new java.sql.Date(funcionario.getFecha_nacimiento().getTime()));
                }
            } catch (Exception e) {
                pst.setNull(5, Types.DATE);
            }
            pst.setInt(6, funcionario.getId_estado_civil());
            pst.setInt(7, funcionario.getId_pais());
            pst.setInt(8, funcionario.getId_ciudad());
            pst.setInt(9, funcionario.getId_persona());
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static boolean isPasswordCorrect(int idFuncionario, String alias, String password) {
        String QUERY = "SELECT ID_FUNCIONARIO FROM FUNCIONARIO WHERE ID_FUNCIONARIO = " + idFuncionario + " AND ALIAS ='" + alias + "' AND PASSWORD ='" + password + "';";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(QUERY);
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void actualizarContraseña(int idFuncionario, String alias, String password) {
        String UPDATE_FUNCIONARIO = "UPDATE FUNCIONARIO SET PASSWORD = ?  "
                + "WHERE ID_FUNCIONARIO = " + idFuncionario + " AND ALIAS = '" + alias + "';";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_FUNCIONARIO);
            pst.setString(1, password);//NOT NULL
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    /*
     * DELETE
     */
    public static void eliminarFuncionario(M_funcionario funcionario) {
        System.out.println("eliminarFuncionario: " + funcionario.getId_funcionario());
        String deleteFuncionario = "DELETE FROM  funcionario "
                + " WHERE id_funcionario = " + funcionario.getId_funcionario() + "";
        //String idPersona="(SELECT ID_PERSONA FROM PERSONA,FUNCIONARIO WHERE ID_PERSONA = ID_PERSONA AND ID_FUNCIONARIO ="+funcionario+")";
        String deletePersona = "DELETE FROM  persona "
                + " WHERE id_persona =" + funcionario.getId_persona();
        String borrarPermisos = "DELETE FROM ROL_USUARIO WHERE ID_FUNCIONARIO = " + funcionario.getId_funcionario();

        String borrarUSUARIO = "DROP USER " + funcionario.getAlias() + " CASCADE ";

        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            System.out.println("Query(borrarPermisos): " + borrarPermisos);
            st.executeUpdate(borrarPermisos);
            st = DB_manager.getConection().createStatement();
            System.out.println("Query(Delete funcionario): " + deleteFuncionario);
            st.executeUpdate(deleteFuncionario);
            System.out.println("Query(Delete persona): " + deletePersona);
            st.executeUpdate(deletePersona);
            st = DB_manager.getConection().createStatement();
            System.out.println("Query(borrarUSUARIO): " + borrarUSUARIO);
            st.executeUpdate(borrarUSUARIO);
            System.out.println("DBmanagerFuncionario.eliminarFuncionario()");
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    /*
     * miscelanea
     */
    public static void insertarImagenFuncionario(int idUsuario, String pathname) {
        FileInputStream fis = null;
        try {
            File file = new File(pathname);
            fis = new FileInputStream(file);
            PreparedStatement ps = DB_manager.getConection().prepareStatement("UPDATE funcionario "
                    + "SET nombre_imagen=?, imagen=? "
                    + "WHERE id_funcionario = ?");
            ps.setString(1, file.getName());
            ps.setBinaryStream(2, fis, file.length());
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
            ps.close();
        } catch (IOException ex) {
            Logger.getLogger(DB_Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DB_Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(DB_Funcionario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /* Metodo que convierte una cadena de bytes en una imagen JPG
     * input:
     * bytes: array que contiene los binarios de la imagen
     * Output: la foto JPG en formato IMAGE
     */
    private static Image ConvertirImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        return reader.read(0, param);
    }

    public static void actualizarImagen(byte[] img, int id_funcionario, String imgName) {
        try {

            String sql = "UPDATE funcionario "
                    + "SET nombre_imagen=?, imagen=? "
                    + "WHERE id_funcionario = ?";
            pst = DB_manager.getConection().prepareStatement(sql);

            pst.setString(1, imgName);
            pst.setBytes(2, img);
            pst.setInt(3, id_funcionario);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static byte[] getImage(int id) {
        byte[] byteImg = null;
        try {
            // String byteImg="";
            pst = DB_manager.getConection().prepareStatement("SELECT image FROM image WHERE id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                byteImg = rs.getBytes(1);
                // use the data in some way here
            }
            rs.close();
            pst.close();

            return byteImg;
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return byteImg;
    }

    public static M_funcionario consultarFuncionarioPorID(Integer id_chofer) {
        M_funcionario f = null;
        String q = "SELECT *  FROM PERSONA P, FUNCIONARIO F "
                + "WHERE p.id_persona = f.id_persona "
                + "AND id_funcionario = " + id_chofer + "";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(q);
            while (rs.next()) {
                f = new M_funcionario();
                f.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
                f.setSexo(rs.getString("sexo"));
                f.setNro_celular(rs.getString("nro_celular"));
                f.setNro_telefono(rs.getString("nro_telefono"));
                f.setEmail(rs.getString("email"));
                f.setDireccion(rs.getString("DIRECCION"));
                f.setAlias(rs.getString("alias"));
                f.setNombre(rs.getString("nombre"));
                f.setApellido(rs.getString("apellido"));
                f.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                f.setId_persona(rs.getInt("id_persona"));
                f.setCedula(rs.getInt("ci"));
                f.setEstado_civil(rs.getString("estado_civil"));
                f.setId_funcionario(rs.getInt("id_funcionario"));
                f.setObservacion(rs.getString("OBSERVACION"));
                //f.setRol(obtenerRolesFuncionario(f));
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
        return f;
    }

    public static void eliminarRolFuncionario(M_funcionario m_funcionario, M_rol rol) {
        String DELETE_ROL_USUARIO = "DELETE FROM ROL_USUARIO WHERE ID_ROL = ? AND ID_FUNCIONARIO = ? ;";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(DELETE_ROL_USUARIO);
            pst.setInt(1, rol.getId());
            pst.setInt(2, m_funcionario.getId_funcionario());
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void cambiarPassword(String alias, String newPass) {
        String DELETE_ROL_USUARIO = "ALTER USER " + alias + "  WITH PASSWORD '" + newPass + "';";
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(DELETE_ROL_USUARIO);
            pst.executeUpdate();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean existeEmpleado(M_funcionario funcionario) {
        String QUERY = "SELECT alias,ci FROM funcionario F, persona P WHERE F.ID_PERSONA = P.ID_PERSONA AND (ALIAS = '" + funcionario.getAlias() + "' OR CI = " + funcionario.getCedula() + ");";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(QUERY);
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    /*
     * 
     * 
     */

    public static boolean verificarAlias(M_funcionario funcionario) {
        String QUERY = "SELECT alias FROM funcionario F, persona P WHERE F.ID_PERSONA = P.ID_PERSONA AND (ALIAS = '" + funcionario.getAlias() + "');";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(QUERY);
            //retorna TRUE si contiene filas, si no contiene filas retorna FALSE
            boolean b = rs.isBeforeFirst();
            System.out.println("1226-verificarAlias: " + QUERY);
            System.out.println("1226-verificarAlias: " + b);
            return b;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean verificarCI(M_funcionario funcionario) {
        String QUERY = "SELECT P.CI FROM funcionario F, persona P WHERE F.ID_PERSONA = P.ID_PERSONA AND (CI = " + funcionario.getCedula() + ");";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(QUERY);
            //retorna TRUE si contiene filas, si no contiene filas retorna FALSE
            boolean b = rs.isBeforeFirst();
            System.out.println("1226-verificarCI: " + QUERY);
            System.out.println("1226-verificarCI: " + b);
            return b;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static ResultSetTableModel consultarRolUsuario(int idFuncionario) {
        ResultSetTableModel rstm = null;
        String q = "SELECT r.id_rol \"ID\", r.descripcion \"Descripción\" "
                + "FROM funcionario f, rol r, rol_usuario ru "
                + "WHERE ru.id_funcionario = f.id_funcionario "
                + "AND ru.id_rol = r.id_rol "
                + "AND F.ID_FUNCIONARIO =" + idFuncionario + ";";
        try {
            String query = q;
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // se ejecuta el query y se obtienen los resultados en un ResultSet
            rs = st.executeQuery(query);
            rstm = new ResultSetTableModel(rs);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
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
         Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
         lgr.log(Level.WARNING, ex.getMessage(), ex);
         }
         }*/

        return rstm;
    }

    public static void quitarRol(Integer id_funcionario, int idRol) {
        String QUITAR_ROL_USUARIO = "DELETE FROM ROL_USUARIO WHERE ID_ROL = " + idRol + " AND ID_FUNCIONARIO = " + id_funcionario + ";";

        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            st.executeUpdate(QUITAR_ROL_USUARIO);
            st.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    public static int eliminarFuncionarioFX(M_funcionario funcionario) {
        int result = 0;
        String DELETE_FUNCIONARIO = "DELETE FROM  funcionario "
                + " WHERE id_funcionario = " + funcionario.getId_funcionario() + "";
        //String idPersona="(SELECT ID_PERSONA FROM PERSONA,FUNCIONARIO WHERE ID_PERSONA = ID_PERSONA AND ID_FUNCIONARIO ="+funcionario+")";
        String DELETE_PERSONA = "DELETE FROM  persona "
                + " WHERE id_persona =" + funcionario.getId_persona();
        String DELETE_ROL_USUARIO = "DELETE FROM ROL_USUARIO WHERE ID_FUNCIONARIO = " + funcionario.getId_funcionario();


        try {
            DB_manager.habilitarTransaccionManual();
            st = DB_manager.getConection().createStatement();
            result = st.executeUpdate(DELETE_ROL_USUARIO);
            st = DB_manager.getConection().createStatement();
            result = st.executeUpdate(DELETE_FUNCIONARIO);
            st = DB_manager.getConection().createStatement();
            result = st.executeUpdate(DELETE_PERSONA);
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DB_Funcionario.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }
}
