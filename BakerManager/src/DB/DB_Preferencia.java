/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_Divisa;
import Entities.E_impresionOrientacion;
import Entities.E_impresionPlantilla;
import Entities.E_impresionTipo;
import Entities.E_preferenciaGeneral;
import Entities.E_ticketPreferencia;
import Entities.M_campoImpresion;
import Entities.M_preferenciasImpresion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Preferencia {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static long insertarPlantilla(E_impresionPlantilla plantilla, M_preferenciasImpresion cabecera, List<M_campoImpresion> campos) {
        String INSERT_TEMPLATE = "INSERT INTO impresion_plantilla (id_impresion_tipo, descripcion, id_estado) VALUES(?, ?, ?);";
        String INSERT_PRINT_FIELD = "INSERT INTO IMPRESION_CAMPO("
                + "ID_IMPRESION_PLANTILLA, DESCRIPCION, COORDENADA_X, COORDENADA_Y, ID_ESTADO"
                + ")VALUES (?,?,?,?,?);";
        String QUERY = "INSERT INTO preferencia_impresion (tamanho_letra, tipo_letra, formato_fecha, max_producto, "
                + "id_estado_duplicado, id_estado_triplicado, distancia_entre_copias, id_imprimir_moneda, "
                + "nombre_impresora, ancho_pagina, largo_pagina, margen_x, margen_y, id_divisa, "
                + "id_impresion_orientacion, distancia_triplicado, id_impresion_plantilla) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        long sq_cabecera = -1L;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(INSERT_TEMPLATE, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, plantilla.getTipo().getId());
            pst.setString(2, plantilla.getDescripcion());
            pst.setInt(3, plantilla.getEstado().getId());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs != null && rs.next()) {
                sq_cabecera = rs.getLong(1);
            }
            pst.close();
            rs.close();
            for (M_campoImpresion unCampo : campos) {
                pst = DB_manager.getConection().prepareStatement(INSERT_PRINT_FIELD);
                pst.setInt(1, (int) sq_cabecera);
                pst.setString(2, unCampo.getCampo());
                pst.setDouble(3, unCampo.getX());
                pst.setDouble(4, unCampo.getY());
                pst.setInt(5, unCampo.getEstado().getId());
                pst.executeUpdate();
            }
            pst = DB_manager.getConection().prepareStatement(QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cabecera.getLetterSize());
            pst.setString(2, cabecera.getLetterFont());
            pst.setString(3, cabecera.getFormatoFecha());
            pst.setInt(4, cabecera.getMaxProducts());
            pst.setInt(5, cabecera.getIdDuplicado());
            pst.setInt(6, cabecera.getIdTriplicado());
            pst.setInt(7, cabecera.getDistanceBetweenCopies());
            pst.setInt(8, cabecera.getImprimirMoneda());
            pst.setString(9, cabecera.getNombreImpresora());
            pst.setInt(10, cabecera.getAnchoPagina());
            pst.setInt(11, cabecera.getLargoPagina());
            pst.setDouble(12, cabecera.getMargenX());
            pst.setDouble(13, cabecera.getMargenY());
            pst.setInt(14, cabecera.getDivisa().getId());
            pst.setInt(15, cabecera.getOrientacion().getId());
            pst.setInt(16, cabecera.getIdTriplicado());
            pst.setInt(17, (int) sq_cabecera);
            pst.executeUpdate();
            pst.close();
            rs.close();
            DB_manager.establecerTransaccion();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return sq_cabecera;
    }

    public static int modificarPreferenciaImpresionFactura(M_preferenciasImpresion prefPrint) {
        String UPDATE = "UPDATE preferencia_impresion "
                + "SET tamanho_letra=?, "
                + "tipo_letra=?, "
                + "formato_fecha=?, "
                + "max_producto=?, "
                + "id_estado_duplicado=?, "
                + "id_estado_triplicado=?, "
                + "distancia_entre_copias=?, "
                + "id_imprimir_moneda=?, "
                + "id_divisa=?, "
                + "nombre_impresora=?, "
                + "ancho_pagina=?, "
                + "largo_pagina=?, "
                + "margen_x=?, "
                + "margen_y=?, "
                + "id_impresion_orientacion=? "
                + "WHERE id_preferencia_impresion = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, prefPrint.getLetterSize());
            pst.setString(2, prefPrint.getLetterFont());
            pst.setString(3, prefPrint.getFormatoFecha());
            pst.setInt(4, prefPrint.getMaxProducts());
            pst.setInt(5, prefPrint.getIdDuplicado());
            pst.setInt(6, prefPrint.getIdTriplicado());
            pst.setInt(7, prefPrint.getDistanceBetweenCopies());
            pst.setInt(8, prefPrint.getImprimirMoneda());
            pst.setInt(9, prefPrint.getDivisa().getId());
            pst.setString(10, prefPrint.getNombreImpresora());
            pst.setInt(11, prefPrint.getAnchoPagina());
            pst.setInt(12, prefPrint.getLargoPagina());
            pst.setDouble(13, prefPrint.getMargenX());
            pst.setDouble(14, prefPrint.getMargenY());
            pst.setDouble(15, prefPrint.getOrientacion().getId());
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int modificarPreferenciaImpresionBoleta(M_preferenciasImpresion prefPrint) {
        String UPDATE = "UPDATE preferencia_impresion "
                + "SET tamanho_letra=?, "
                + "tipo_letra=?, "
                + "formato_fecha=?, "
                + "max_producto=?, "
                + "id_estado_duplicado=?, "
                + "id_estado_triplicado=?, "
                + "distancia_entre_copias=?, "
                + "id_imprimir_moneda=?, "
                + "id_divisa=?, "
                + "nombre_impresora=?, "
                + "ancho_pagina=?, "
                + "largo_pagina=?, "
                + "margen_x=?, "
                + "margen_y=?, "
                + "id_impresion_orientacion=? "
                + "WHERE id_preferencia_impresion = 2;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, prefPrint.getLetterSize());
            pst.setString(2, prefPrint.getLetterFont());
            pst.setString(3, prefPrint.getFormatoFecha());
            pst.setInt(4, prefPrint.getMaxProducts());
            pst.setInt(5, prefPrint.getIdDuplicado());
            pst.setInt(6, prefPrint.getIdTriplicado());
            pst.setInt(7, prefPrint.getDistanceBetweenCopies());
            pst.setInt(8, prefPrint.getImprimirMoneda());
            pst.setInt(9, prefPrint.getDivisa().getId());
            pst.setString(10, prefPrint.getNombreImpresora());
            pst.setInt(11, prefPrint.getAnchoPagina());
            pst.setInt(12, prefPrint.getLargoPagina());
            pst.setDouble(13, prefPrint.getMargenX());
            pst.setDouble(14, prefPrint.getMargenY());
            pst.setDouble(15, prefPrint.getOrientacion().getId());
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int modificarPreferenciaImpresion(M_preferenciasImpresion prefPrint) {
        String UPDATE = "UPDATE preferencia_impresion "
                + "SET tamanho_letra=?, "
                + "tipo_letra=?, "
                + "formato_fecha=?, "
                + "max_producto=?, "
                + "id_estado_duplicado=?, "
                + "id_estado_triplicado=?, "
                + "distancia_entre_copias=?, "
                + "id_imprimir_moneda=?, "
                + "id_divisa=?, "
                + "nombre_impresora=?, "
                + "ancho_pagina=?, "
                + "largo_pagina=?, "
                + "margen_x=?, "
                + "margen_y=?, "
                + "id_impresion_orientacion=? "
                + "WHERE id_preferencia_impresion = ?;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, prefPrint.getLetterSize());
            pst.setString(2, prefPrint.getLetterFont());
            pst.setString(3, prefPrint.getFormatoFecha());
            pst.setInt(4, prefPrint.getMaxProducts());
            pst.setInt(5, prefPrint.getIdDuplicado());
            pst.setInt(6, prefPrint.getIdTriplicado());
            pst.setInt(7, prefPrint.getDistanceBetweenCopies());
            pst.setInt(8, prefPrint.getImprimirMoneda());
            pst.setInt(9, prefPrint.getDivisa().getId());
            pst.setString(10, prefPrint.getNombreImpresora());
            pst.setInt(11, prefPrint.getAnchoPagina());
            pst.setInt(12, prefPrint.getLargoPagina());
            pst.setDouble(13, prefPrint.getMargenX());
            pst.setDouble(14, prefPrint.getMargenY());
            pst.setDouble(15, prefPrint.getOrientacion().getId());
            pst.setInt(16, prefPrint.getId());
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static int modificarPreferenciaImpresionTicket(E_ticketPreferencia ticketPreferencia) {
        String UPDATE = "UPDATE ticket_preferencia SET "
                + "cabecera=?, "
                + "pie=?, "
                + "nombre_impresora=? "
                + "WHERE id_ticket_preferencia = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setString(1, ticketPreferencia.getCabecera());
            pst.setString(2, ticketPreferencia.getPie());
            pst.setString(3, ticketPreferencia.getNombreImpresora());
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Producto.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public static M_preferenciasImpresion obtenerPreferenciaImpresionFactura() {
        M_preferenciasImpresion prefPrint = null;
        String Query = "SELECT id_preferencia_impresion, "
                + "tamanho_letra, "
                + "tipo_letra, "
                + "formato_fecha, "
                + "max_producto, "
                + "id_estado_duplicado, "
                + "id_estado_triplicado, "
                + "distancia_entre_copias, "
                + "id_imprimir_moneda, "
                + "id_divisa, "
                + "nombre_impresora, "
                + "ancho_pagina, "
                + "largo_pagina, "
                + "margen_x, "
                + "margen_y, "
                + "id_impresion_orientacion, "
                + "(SELECT descripcion FROM divisa WHERE id_divisa = preferencia_impresion.id_divisa)\"divisa_descripcion\", "
                + "(SELECT descripcion FROM impresion_orientacion WHERE id_impresion_orientacion = preferencia_impresion.id_impresion_orientacion)\"orientacion_descripcion\", "
                + "distancia_triplicado "
                + "FROM preferencia_impresion WHERE id_preferencia_impresion =1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_Divisa d = new E_Divisa();
                d.setId(rs.getInt("id_divisa"));
                d.setDescripcion(rs.getString("divisa_descripcion"));
                E_impresionOrientacion o = new E_impresionOrientacion();
                o.setId(rs.getInt("id_impresion_orientacion"));
                o.setDescripcion(rs.getString("orientacion_descripcion"));
                prefPrint = new M_preferenciasImpresion();
                prefPrint.setDistanceBetweenCopies(rs.getInt("distancia_entre_copias"));
                prefPrint.setDivisa(d);
                prefPrint.setOrientacion(o);
                prefPrint.setId(rs.getInt("id_preferencia_impresion"));
                prefPrint.setIdDuplicado(rs.getInt("id_estado_duplicado"));
                prefPrint.setIdTriplicado(rs.getInt("id_estado_triplicado"));
                prefPrint.setImprimirMoneda(rs.getInt("id_imprimir_moneda"));
                prefPrint.setLetterFont(rs.getString("tipo_letra"));
                prefPrint.setFormatoFecha(rs.getString("formato_fecha"));
                prefPrint.setNombreImpresora(rs.getString("nombre_impresora"));
                prefPrint.setLetterSize(rs.getInt("tamanho_letra"));
                prefPrint.setMaxProducts(rs.getInt("max_producto"));
                prefPrint.setAnchoPagina(rs.getInt("ancho_pagina"));
                prefPrint.setLargoPagina(rs.getInt("largo_pagina"));
                prefPrint.setMargenX(rs.getDouble("margen_x"));
                prefPrint.setMargenY(rs.getDouble("margen_y"));
                prefPrint.setDistanceForTriplicate(rs.getInt("distancia_triplicado"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefPrint;
    }

    public static M_preferenciasImpresion obtenerPreferenciaImpresionBoleta() {
        M_preferenciasImpresion prefPrint = null;
        String Query = "SELECT id_preferencia_impresion, "
                + "tamanho_letra, "
                + "tipo_letra, "
                + "formato_fecha, "
                + "max_producto, "
                + "id_estado_duplicado, "
                + "id_estado_triplicado, "
                + "distancia_entre_copias, "
                + "id_imprimir_moneda, "
                + "id_divisa, "
                + "nombre_impresora, "
                + "ancho_pagina, "
                + "largo_pagina, "
                + "margen_x, "
                + "margen_y, "
                + "id_impresion_orientacion, "
                + "(SELECT descripcion FROM divisa WHERE id_divisa = preferencia_impresion.id_divisa)\"divisa_descripcion\", "
                + "(SELECT descripcion FROM impresion_orientacion WHERE id_impresion_orientacion = preferencia_impresion.id_impresion_orientacion)\"orientacion_descripcion\", "
                + "distancia_triplicado "
                + "FROM preferencia_impresion WHERE id_preferencia_impresion =2 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_Divisa d = new E_Divisa();
                d.setId(rs.getInt("id_divisa"));
                d.setDescripcion(rs.getString("divisa_descripcion"));
                E_impresionOrientacion o = new E_impresionOrientacion();
                o.setId(rs.getInt("id_impresion_orientacion"));
                o.setDescripcion(rs.getString("orientacion_descripcion"));
                prefPrint = new M_preferenciasImpresion();
                prefPrint.setDistanceBetweenCopies(rs.getInt("distancia_entre_copias"));
                prefPrint.setDivisa(d);
                prefPrint.setOrientacion(o);
                prefPrint.setId(rs.getInt("id_preferencia_impresion"));
                prefPrint.setIdDuplicado(rs.getInt("id_estado_duplicado"));
                prefPrint.setIdTriplicado(rs.getInt("id_estado_triplicado"));
                prefPrint.setImprimirMoneda(rs.getInt("id_imprimir_moneda"));
                prefPrint.setLetterFont(rs.getString("tipo_letra"));
                prefPrint.setFormatoFecha(rs.getString("formato_fecha"));
                prefPrint.setNombreImpresora(rs.getString("nombre_impresora"));
                prefPrint.setLetterSize(rs.getInt("tamanho_letra"));
                prefPrint.setMaxProducts(rs.getInt("max_producto"));
                prefPrint.setAnchoPagina(rs.getInt("ancho_pagina"));
                prefPrint.setLargoPagina(rs.getInt("largo_pagina"));
                prefPrint.setMargenX(rs.getDouble("margen_x"));
                prefPrint.setMargenY(rs.getDouble("margen_y"));
                prefPrint.setDistanceForTriplicate(rs.getInt("distancia_triplicado"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefPrint;
    }

    public static M_preferenciasImpresion obtenerPreferenciaImpresion(int idPlantilla) {
        M_preferenciasImpresion prefPrint = null;
        String Query = "SELECT id_preferencia_impresion, "
                + "tamanho_letra, "
                + "tipo_letra, "
                + "formato_fecha, "
                + "max_producto, "
                + "id_estado_duplicado, "
                + "id_estado_triplicado, "
                + "distancia_entre_copias, "
                + "id_imprimir_moneda, "
                + "id_divisa, "
                + "nombre_impresora, "
                + "ancho_pagina, "
                + "largo_pagina, "
                + "margen_x, "
                + "margen_y, "
                + "id_impresion_orientacion, "
                + "(SELECT descripcion FROM divisa WHERE id_divisa = preferencia_impresion.id_divisa)\"divisa_descripcion\", "
                + "(SELECT descripcion FROM impresion_orientacion WHERE id_impresion_orientacion = preferencia_impresion.id_impresion_orientacion)\"orientacion_descripcion\", "
                + "distancia_triplicado "
                + "FROM preferencia_impresion WHERE id_impresion_plantilla = ? ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idPlantilla);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_Divisa d = new E_Divisa();
                d.setId(rs.getInt("id_divisa"));
                d.setDescripcion(rs.getString("divisa_descripcion"));
                E_impresionOrientacion o = new E_impresionOrientacion();
                o.setId(rs.getInt("id_impresion_orientacion"));
                o.setDescripcion(rs.getString("orientacion_descripcion"));
                prefPrint = new M_preferenciasImpresion();
                prefPrint.setDistanceBetweenCopies(rs.getInt("distancia_entre_copias"));
                prefPrint.setDivisa(d);
                prefPrint.setOrientacion(o);
                prefPrint.setId(rs.getInt("id_preferencia_impresion"));
                prefPrint.setIdDuplicado(rs.getInt("id_estado_duplicado"));
                prefPrint.setIdTriplicado(rs.getInt("id_estado_triplicado"));
                prefPrint.setImprimirMoneda(rs.getInt("id_imprimir_moneda"));
                prefPrint.setLetterFont(rs.getString("tipo_letra"));
                prefPrint.setFormatoFecha(rs.getString("formato_fecha"));
                prefPrint.setNombreImpresora(rs.getString("nombre_impresora"));
                prefPrint.setLetterSize(rs.getInt("tamanho_letra"));
                prefPrint.setMaxProducts(rs.getInt("max_producto"));
                prefPrint.setAnchoPagina(rs.getInt("ancho_pagina"));
                prefPrint.setLargoPagina(rs.getInt("largo_pagina"));
                prefPrint.setMargenX(rs.getDouble("margen_x"));
                prefPrint.setMargenY(rs.getDouble("margen_y"));
                prefPrint.setDistanceForTriplicate(rs.getInt("distancia_triplicado"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefPrint;
    }

    public static E_ticketPreferencia obtenerPreferenciaImpresionTicket() {
        E_ticketPreferencia ticketPref = null;
        String Query = "SELECT id_ticket_preferencia, "
                + "cabecera, "
                + "pie, "
                + "nombre_impresora "
                + "FROM ticket_preferencia WHERE id_ticket_preferencia = 1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                ticketPref = new E_ticketPreferencia();
                ticketPref.setId(rs.getInt("id_ticket_preferencia"));
                ticketPref.setCabecera(rs.getString("cabecera"));
                ticketPref.setPie(rs.getString("pie"));
                ticketPref.setNombreImpresora(rs.getString("nombre_impresora"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ticketPref;
    }

    public static E_preferenciaGeneral obtenerPreferenciaGeneral() {
        E_preferenciaGeneral prefGeneral = null;
        String QUERY = "SELECT id_preferencia_general, "
                + "id_impresion_tipo, "
                + "id_timbrado "
                + "FROM preferencia_general WHERE id_preferencia_general = 1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                prefGeneral = new E_preferenciaGeneral();
                prefGeneral.setIdPreferenciaGeneral(rs.getInt(1));
                prefGeneral.setIdImpresionTipo(rs.getInt(2));
                prefGeneral.setIdTimbradoVenta(rs.getInt(3));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefGeneral;
    }

    public static int establecerTimbradoVentaPredeterminado(int idTimbradoVenta) {
        String UPDATE = "UPDATE preferencia_general "
                + "SET id_timbrado = ? "
                + "WHERE id_preferencia_general = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, idTimbradoVenta);
            result = pst.executeUpdate();
            DB_manager.getConection().commit();
        } catch (SQLException ex) {
            System.out.println(ex.getNextException());
            if (DB_manager.getConection() != null) {
                try {
                    DB_manager.getConection().rollback();
                } catch (SQLException ex1) {
                    Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }
            Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
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
                Logger lgr = Logger.getLogger(DB_Preferencia.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;

    }

    public static ArrayList<E_impresionOrientacion> obtenerImpresionOrientacion() {
        ArrayList<E_impresionOrientacion> impresionOrientacion = null;
        String q = "SELECT * "
                + "FROM impresion_orientacion ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impresionOrientacion = new ArrayList();
            while (rs.next()) {
                E_impresionOrientacion pc = new E_impresionOrientacion();
                pc.setId(rs.getInt("id_impresion_orientacion"));
                pc.setDescripcion(rs.getString("descripcion"));
                impresionOrientacion.add(pc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impresionOrientacion;
    }

    public static ArrayList<E_impresionTipo> obtenerImpresionTipo() {
        ArrayList<E_impresionTipo> impresionTipos = null;
        String q = "SELECT * "
                + "FROM impresion_tipo ";
        try {
            st = DB_manager.getConection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(q);
            impresionTipos = new ArrayList();
            while (rs.next()) {
                E_impresionTipo pc = new E_impresionTipo();
                pc.setId(rs.getInt("id_impresion_tipo"));
                pc.setDescripcion(rs.getString("descripcion"));
                impresionTipos.add(pc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return impresionTipos;
    }
}
