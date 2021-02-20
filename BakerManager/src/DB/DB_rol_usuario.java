/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.M_funcionario;
import Entities.M_rol;
import Entities.M_rol_usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_rol_usuario {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static M_rol_usuario obtenerRolUsuario(int idFuncionario) {
        M_funcionario f = null;
        M_rol r = null;
        ArrayList<M_rol> roles = new ArrayList<>();
        M_rol_usuario rol_usuario = null;
        String genero = "(SELECT DESCRIPCION  FROM SEXO WHERE ID_SEXO = P.ID_SEXO) \"sexo\"";
        String pais = "(SELECT DESCRIPCION FROM PAIS WHERE ID_PAIS=P.ID_PAIS) \"NACIONALIDAD\"";
        String ciudad = " (SELECT DESCRIPCION FROM CIUDAD WHERE ID_CIUDAD=P.ID_CIUDAD)\"CIUDAD\"";
        String estadoCivil = " (SELECT DESCRIPCION FROM ESTADO_CIVIL WHERE ID_ESTADO_CIVIL= P.ID_ESTADO_CIVIL)\"estado_civil\"";
        String queryFunc = "F.id_funcionario, P.id_persona, alias, fecha_ingreso, nro_celular, nro_telefono,email, direccion, observacion";
        String queryPers = "ci, nombre, apellido," + genero + ", fecha_nacimiento, " + pais + "," + ciudad + ", " + estadoCivil + "";
        String Q_FUNCIONARIO = "SELECT " + queryFunc + "," + queryPers + " FROM funcionario F, persona P "
                + "WHERE F.id_persona = P.id_persona "
                + "AND F.ID_FUNCIONARIO = " + idFuncionario + ";";
        String Q_ROL = "SELECT R.ID_ROL, R.DESCRIPCION FROM ROL R, ROL_USUARIO RU "
                + "WHERE R.ID_ROL = RU.ID_ROL "
                + "AND RU.ID_FUNCIONARIO = " + idFuncionario + ";";
        try {
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Q_FUNCIONARIO);
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
                f.setId(rs.getInt("id_funcionario"));
                f.setObservacion(rs.getString("OBSERVACION"));
            }
            st = DB_manager.getConection().createStatement();
            rs = st.executeQuery(Q_ROL);
            while (rs.next()) {
                r = new M_rol();
                r.setId(rs.getInt("ID_ROL"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                roles.add(r);
            }
            rol_usuario = new M_rol_usuario(f, null, roles);
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
        return rol_usuario;
    }
}
