/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_Timbrado;
import Entities.Estado;
import Entities.M_funcionario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Timbrado {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static List<E_Timbrado> obtenerTimbrados(int idFuncionario, int nroTimbrado, Date fechaInicio, Date fechaFinal,
            String tipoFecha, int idEstado, boolean conFecha) {
        List<E_Timbrado> list = new ArrayList<>();
        String query = "SELECT "
                + "TV.ID_TIMBRADO, "
                + "TV.DESCRIPCION, "
                + "TV.NRO_TIMBRADO, "
                + "TV.NRO_SUCURSAL, "
                + "TV.NRO_PUNTO_VENTA, "
                + "TV.NRO_BOLETA_INICIAL, "
                + "TV.NRO_BOLETA_FINAL, "
                + "(SELECT nombre ||' '||apellido FROM persona pers WHERE pers.id_persona = f.id_persona) \"FUNCIONARIO\", "
                + "TV.TIEMPO_CREACION, "
                + "TV.TIEMPO_VENCIMIENTO, "
                + "(SELECT descripcion FROM ESTADO ESTA WHERE ESTA.id_estado = TV.id_estado) \"ESTADO\", "
                + "TV.id_estado "
                + "FROM  "
                + "TIMBRADO TV, "
                + "funcionario f "
                + "WHERE TV.id_funcionario = f.id_funcionario ";
        String orderBy = "ORDER BY TV.ID_TIMBRADO";
        if (conFecha) {
            switch (tipoFecha) {
                case "Fecha vencimiento": {
                    query = query + " AND TV.TIEMPO_VENCIMIENTO BETWEEN ?  AND ? ";
                    break;
                }
                case "Fecha creación": {
                    query = query + " AND TV.TIEMPO_CREACION BETWEEN ?  AND ?  ";
                    break;
                }
            }
        }
        if (idFuncionario > 0) {
            query = query + " AND TV.ID_FUNCIONARIO = ? ";
        }
        if (nroTimbrado > 0) {
            query = query + " AND TV.NRO_TIMBRADO = ? ";
        }
        if (idEstado > 0) {
            query = query + " AND TV.id_estado = ? ";
        }
        query = query + orderBy;
        int pos = 1;
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (conFecha) {
                pst.setTimestamp(pos, new java.sql.Timestamp(fechaInicio.getTime()));
                pos++;
                pst.setTimestamp(pos, new java.sql.Timestamp(fechaFinal.getTime()));
                pos++;
            }
            if (idFuncionario > 0) {
                pst.setInt(pos, idFuncionario);
                pos++;
            }
            if (nroTimbrado > 0) {
                pst.setInt(pos, nroTimbrado);
                pos++;
            }
            if (idEstado > 0) {
                pst.setInt(pos, idEstado);
                pos++;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString("FUNCIONARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                E_Timbrado unTimbrado = new E_Timbrado();
                unTimbrado.setId(rs.getInt("ID_TIMBRADO"));
                unTimbrado.setCreador(f);
                unTimbrado.setFechaCreacion(rs.getTimestamp("TIEMPO_CREACION"));
                unTimbrado.setFechaVencimiento(rs.getTimestamp("TIEMPO_VENCIMIENTO"));
                unTimbrado.setNroBoletaFinal(rs.getInt("NRO_BOLETA_FINAL"));
                unTimbrado.setNroBoletaInicial(rs.getInt("NRO_BOLETA_INICIAL"));
                unTimbrado.setNroPuntoVenta(rs.getInt("NRO_PUNTO_VENTA"));
                unTimbrado.setNroSucursal(rs.getInt("NRO_SUCURSAL"));
                unTimbrado.setNroTimbrado(rs.getInt("NRO_TIMBRADO"));
                unTimbrado.setDescripcion(rs.getString("DESCRIPCION"));
                unTimbrado.setEstado(estado);
                list.add(unTimbrado);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return list;
    }

    public static E_Timbrado obtenerTimbrado(int nroTimbrado, int nroSucursal, int nroPVTA, int nroBoletaInicial, int nroBoletaFinal) {
        E_Timbrado unTimbrado = null;
        String query = "SELECT "
                + "TV.ID_TIMBRADO, "
                + "TV.NRO_TIMBRADO, "
                + "TV.NRO_SUCURSAL, "
                + "TV.NRO_PUNTO_VENTA, "
                + "TV.NRO_BOLETA_INICIAL, "
                + "TV.NRO_BOLETA_FINAL, "
                + "(SELECT nombre ||' '||apellido FROM persona pers WHERE pers.id_persona = f.id_persona) \"FUNCIONARIO\", "
                + "TV.TIEMPO_CREACION, "
                + "TV.TIEMPO_VENCIMIENTO, "
                + "(SELECT descripcion FROM ESTADO ESTA WHERE ESTA.id_estado = TV.id_estado) \"ESTADO\", "
                + "TV.id_estado "
                + "FROM  "
                + "TIMBRADO TV, "
                + "funcionario f "
                + "WHERE TV.id_funcionario = f.id_funcionario "
                + "AND TV.NRO_TIMBRADO = ? "
                + "AND TV.NRO_SUCURSAL = ? "
                + "AND TV.NRO_PUNTO_VENTA = ? "
                + "AND TV.NRO_BOLETA_INICIAL = ? "
                + "AND TV.NRO_BOLETA_FINAL = ? "
                + "AND TV.id_estado = 1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, nroTimbrado);
            pst.setInt(2, nroSucursal);
            pst.setInt(3, nroPVTA);
            pst.setInt(4, nroBoletaInicial);
            pst.setInt(5, nroBoletaFinal);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString("FUNCIONARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                unTimbrado = new E_Timbrado();
                unTimbrado.setId(rs.getInt("ID_TIMBRADO"));
                unTimbrado.setCreador(f);
                unTimbrado.setFechaCreacion(rs.getDate("TIEMPO_CREACION"));
                unTimbrado.setFechaVencimiento(rs.getDate("TIEMPO_VENCIMIENTO"));
                unTimbrado.setNroBoletaFinal(rs.getInt("NRO_BOLETA_FINAL"));
                unTimbrado.setNroBoletaInicial(rs.getInt("NRO_BOLETA_INICIAL"));
                unTimbrado.setNroPuntoVenta(rs.getInt("NRO_PUNTO_VENTA"));
                unTimbrado.setNroSucursal(rs.getInt("NRO_SUCURSAL"));
                unTimbrado.setNroTimbrado(rs.getInt("NRO_TIMBRADO"));
                unTimbrado.setEstado(estado);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return unTimbrado;
    }

    public static E_Timbrado obtenerTimbrado(int idTimbrado) {
        E_Timbrado unTimbrado = null;
        String query = "SELECT "
                + "TV.ID_TIMBRADO, "
                + "TV.NRO_TIMBRADO, "
                + "TV.NRO_SUCURSAL, "
                + "TV.NRO_PUNTO_VENTA, "
                + "TV.NRO_BOLETA_INICIAL, "
                + "TV.NRO_BOLETA_FINAL, "
                + "(SELECT nombre ||' '||apellido FROM persona pers WHERE pers.id_persona = f.id_persona) \"FUNCIONARIO\", "
                + "TV.TIEMPO_CREACION, "
                + "TV.TIEMPO_VENCIMIENTO, "
                + "(SELECT descripcion FROM ESTADO ESTA WHERE ESTA.id_estado = TV.id_estado) \"ESTADO\", "
                + "TV.id_estado "
                + "FROM  "
                + "TIMBRADO TV, "
                + "funcionario f "
                + "WHERE TV.id_funcionario = f.id_funcionario "
                + "AND TV.ID_TIMBRADO = ? ";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idTimbrado);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString("FUNCIONARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                unTimbrado = new E_Timbrado();
                unTimbrado.setId(rs.getInt("ID_TIMBRADO"));
                unTimbrado.setCreador(f);
                unTimbrado.setFechaCreacion(rs.getDate("TIEMPO_CREACION"));
                unTimbrado.setFechaVencimiento(rs.getDate("TIEMPO_VENCIMIENTO"));
                unTimbrado.setNroBoletaFinal(rs.getInt("NRO_BOLETA_FINAL"));
                unTimbrado.setNroBoletaInicial(rs.getInt("NRO_BOLETA_INICIAL"));
                unTimbrado.setNroPuntoVenta(rs.getInt("NRO_PUNTO_VENTA"));
                unTimbrado.setNroSucursal(rs.getInt("NRO_SUCURSAL"));
                unTimbrado.setNroTimbrado(rs.getInt("NRO_TIMBRADO"));
                unTimbrado.setEstado(estado);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return unTimbrado;
    }

    public static E_Timbrado obtenerUltimoTimbradoUsado() {
        E_Timbrado unTimbrado = null;
        String query = "SELECT "
                + "TV.ID_TIMBRADO, "
                + "TV.NRO_TIMBRADO, "
                + "TV.NRO_SUCURSAL, "
                + "TV.NRO_PUNTO_VENTA, "
                + "TV.NRO_BOLETA_INICIAL, "
                + "TV.NRO_BOLETA_FINAL, "
                + "(SELECT nombre ||' '||apellido FROM persona pers WHERE pers.id_persona = f.id_persona) \"FUNCIONARIO\", "
                + "TV.TIEMPO_CREACION, "
                + "TV.TIEMPO_VENCIMIENTO, "
                + "(SELECT descripcion FROM ESTADO ESTA WHERE ESTA.id_estado = TV.id_estado) \"ESTADO\", "
                + "TV.id_estado "
                + "FROM  "
                + "TIMBRADO TV, "
                + "funcionario f "
                + "WHERE TV.id_funcionario = f.id_funcionario "
                + "ORDER BY ID DESC LIMIT 1";
        try {
            pst = DB_manager.getConection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                M_funcionario f = new M_funcionario();
                f.setNombre(rs.getString("FUNCIONARIO"));
                Estado estado = new Estado();
                estado.setId(rs.getInt("id_estado"));
                estado.setDescripcion(rs.getString("ESTADO"));
                unTimbrado = new E_Timbrado();
                unTimbrado.setId(rs.getInt("ID_TIMBRADO"));
                unTimbrado.setCreador(f);
                unTimbrado.setFechaCreacion(rs.getDate("TIEMPO_CREACION"));
                unTimbrado.setFechaVencimiento(rs.getDate("TIEMPO_VENCIMIENTO"));
                unTimbrado.setNroBoletaFinal(rs.getInt("NRO_BOLETA_FINAL"));
                unTimbrado.setNroBoletaInicial(rs.getInt("NRO_BOLETA_INICIAL"));
                unTimbrado.setNroPuntoVenta(rs.getInt("NRO_PUNTO_VENTA"));
                unTimbrado.setNroSucursal(rs.getInt("NRO_SUCURSAL"));
                unTimbrado.setNroTimbrado(rs.getInt("NRO_TIMBRADO"));
                unTimbrado.setEstado(estado);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return unTimbrado;
    }

    public static boolean existeNroTimbrado(int nroTimbrado) {
        String Query = "SELECT nro_timbrado  FROM timbrado WHERE nro_timbrado = ?";
        try {
            pst = DB_manager.getConection().prepareStatement(Query);
            pst.setInt(1, nroTimbrado);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
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
                Logger lgr = Logger.getLogger(DB_NotaCredito.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return false;
    }

    public static void anularTimbrado(int idCabecera, int idEstado, boolean recuperarNroNotaCredito) {
        String UPDATE_NOTACREDITO = "UPDATE timbrado SET ID_ESTADO = ? WHERE id_timbrado = ?";
        if (recuperarNroNotaCredito) {
            UPDATE_NOTACREDITO = "UPDATE timbrado SET ID_ESTADO = ?, NRO_TIMBRADO = 0 WHERE id_timbrado = ?";
        }
        try {
            DB_manager.habilitarTransaccionManual();
            pst = DB_manager.getConection().prepareStatement(UPDATE_NOTACREDITO);
            pst.setInt(1, idEstado);
            pst.setInt(2, idCabecera);
            pst.executeUpdate();
            pst.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Egreso.class
                            .getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Egreso.class
                    .getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int insertarTimbrado(E_Timbrado cabecera) {
        String INSERT_CABECERA = "INSERT INTO timbrado"
                + "(nro_timbrado, "
                + "nro_sucursal, "
                + "nro_punto_venta, "
                + "nro_boleta_inicial, "
                + "nro_boleta_final, "
                + "tiempo_vencimiento, "
                + "id_funcionario, "
                + "descripcion,"
                + "id_timbrado_tipo)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_CABECERA, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getNroTimbrado());
            pst.setInt(2, cabecera.getNroSucursal());
            pst.setInt(3, cabecera.getNroPuntoVenta());
            pst.setInt(4, cabecera.getNroBoletaInicial());
            pst.setInt(5, cabecera.getNroBoletaFinal());
            pst.setTimestamp(6, new Timestamp(cabecera.getFechaVencimiento().getTime()));
            pst.setInt(7, cabecera.getCreador().getId_funcionario());
            pst.setString(8, cabecera.getDescripcion());
            pst.setInt(9, 1);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Timbrado.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return (int) sq_cabecera;
    }
}
