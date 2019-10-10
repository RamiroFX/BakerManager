/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.Divisa;
import Entities.E_impresionOrientacion;
import Entities.E_preferenciaGeneral;
import Entities.E_ticketPreferencia;
import Entities.M_preferenciasImpresion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                + "(SELECT descripcion FROM impresion_orientacion WHERE id_impresion_orientacion = preferencia_impresion.id_impresion_orientacion)\"orientacion_descripcion\" "
                + "FROM preferencia_impresion WHERE id_preferencia_impresion =1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                Divisa d = new Divisa();
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
                + "(SELECT descripcion FROM impresion_orientacion WHERE id_impresion_orientacion = preferencia_impresion.id_impresion_orientacion)\"orientacion_descripcion\" "
                + "FROM preferencia_impresion WHERE id_preferencia_impresion =2 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                Divisa d = new Divisa();
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
        String Query = "SELECT id_preferencia_general, "
                + "id_impresion_tipo "
                + "FROM preferencia_general WHERE id_preferencia_general = 1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                prefGeneral = new E_preferenciaGeneral();
                prefGeneral.setIdPreferenciaGeneral(rs.getInt(1));
                prefGeneral.setIdImpresionTipo(rs.getInt(2));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefGeneral;
    }
}
