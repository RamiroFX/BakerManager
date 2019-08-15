/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.Divisa;
import Entities.M_campoImpresion;
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

    public static int modificarPreferenciaImpresion(M_preferenciasImpresion prefPrint) {
        String UPDATE = "UPDATE preferencia_impresion "
                + "SET tamanho_letra=?, "
                + "tipo_letra=?, "
                + "max_producto=?, "
                + "id_estado_duplicado=?, "
                + "id_estado_triplicado=?, "
                + "distancia_entre_copias=?, "
                + "id_imprimir_moneda=?, "
                + "id_divisa=?"
                + "WHERE id_preferencia_impresion = 1;";
        int result = -1;
        try {
            DB_manager.getConection().setAutoCommit(false);
            pst = DB_manager.getConection().prepareStatement(UPDATE);
            pst.setInt(1, prefPrint.getLetterSize());
            pst.setString(2, prefPrint.getLetterFont());
            pst.setInt(3, prefPrint.getMaxProducts());
            pst.setInt(4, prefPrint.getIdDuplicado());
            pst.setInt(5, prefPrint.getIdTriplicado());
            pst.setInt(6, prefPrint.getDistanceBetweenCopies());
            pst.setInt(7, prefPrint.getImprimirMoneda());
            pst.setInt(8, prefPrint.getDivisa().getId());
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

    public static M_preferenciasImpresion obtenerPreferenciaImpresion() {
        M_preferenciasImpresion prefPrint = null;
        String Query = "SELECT id_preferencia_impresion, "
                + "tamanho_letra, "
                + "tipo_letra, "
                + "max_producto, "
                + "id_estado_duplicado, "
                + "id_estado_triplicado, "
                + "distancia_entre_copias, "
                + "id_imprimir_moneda, "
                + "id_divisa, "
                + "(SELECT descripcion FROM divisa WHERE id_divisa = preferencia_impresion.id_divisa)\"divisa_descripcion\" "
                + "FROM preferencia_impresion WHERE id_preferencia_impresion =1 ";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pst.executeQuery();
            while (rs.next()) {
                Divisa d = new Divisa();
                d.setId(rs.getInt("id_divisa"));
                d.setDescripcion(rs.getString("divisa_descripcion"));
                prefPrint = new M_preferenciasImpresion();
                prefPrint.setDistanceBetweenCopies(rs.getInt("distancia_entre_copias"));
                prefPrint.setDivisa(d);
                prefPrint.setId(rs.getInt("id_preferencia_impresion"));
                prefPrint.setIdDuplicado(rs.getInt("id_estado_duplicado"));
                prefPrint.setIdTriplicado(rs.getInt("id_estado_triplicado"));
                prefPrint.setImprimirMoneda(rs.getInt("id_imprimir_moneda"));
                prefPrint.setLetterFont(rs.getString("tipo_letra"));
                prefPrint.setLetterSize(rs.getInt("tamanho_letra"));
                prefPrint.setMaxProducts(rs.getInt("max_producto"));
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Producto.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prefPrint;
    }
}
