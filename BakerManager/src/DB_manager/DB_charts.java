/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB_manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_charts {

    public static final int ORDER_COMPRAS = 1;
    public static final int ORDER_ENTIDAD = 1;
    public static final int ORDER_NOMBRE = 1;
    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static DefaultPieDataset obtenerComprasClientes(Timestamp inicio, Timestamp fin) {
        String SELECT = "SELECT CLIE_ENTIDAD ||' ('||  CLIE_NOMBRE ||')' \"Entidad\", ROUND((SELECT SUM(PEDE_CANTIDAD*(PEDE_PRECIO-(PEDE_PRECIO*PEDE_DESCUENTO)/100)))) \"Compra\" ";
        String FROM = "FROM PEDIDO_DETALLE, PEDIDO, CLIENTE ";
        String WHERE = "WHERE PEDI_ID_PEDIDO = PEDE_ID_PEDIDO AND CLIE_ID_CLIENTE =  PEDI_ID_CLIENTE ";
        String GROUPBY = "GROUP BY CLIE_ENTIDAD, CLIE_NOMBRE;";
        String TIEMPO = "AND PEDI_TIEMPO_RECEPCION BETWEEN '" + inicio + "'::timestamp "
                + "AND '" + fin + "'::timestamp ";
        if (null != inicio && null != fin) {
            WHERE = WHERE + TIEMPO;
        }
        String QUERY = SELECT + FROM + WHERE + GROUPBY;
        DefaultPieDataset dataset = new DefaultPieDataset();
        System.out.println("41-charts: " + QUERY);
        try {
            st = DB_manager.getConection().createStatement();
            st.executeQuery(QUERY);
            rs = st.getResultSet();
            while (rs.next()) {
                dataset.setValue(rs.getString("Entidad"), rs.getDouble("Compra"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_charts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    public static DefaultPieDataset obtenerComprasProveedores(Timestamp inicio, Timestamp fin) {
        String SELECT = "SELECT PROV_ENTIDAD ||' ('||  PROV_NOMBRE ||')' \"Proveedor\", ROUND((SELECT SUM(EGDE_CANTIDAD*(EGDE_PRECIO-(EGDE_PRECIO*EGDE_DESCUENTO)/100)))) \"Compra\"";
        String FROM = "FROM EGRESO_DETALLE, EGRESO_CABECERA, PROVEEDOR ";
        String WHERE = "WHERE EGCA_ID_EGRE_CABE = EGDE_ID_EGRESO_CABE AND EGCA_ID_PROVEEDOR = PROV_ID_PROVEEDOR ";
        String GROUPBY = "GROUP BY \"Proveedor\" ";
        String TIEMPO = "AND EGCA_TIEMPO BETWEEN '" + inicio + "'::timestamp "
                + "AND '" + fin + "'::timestamp ";
        String ORDERBY = "ORDER BY \"Compra\" DESC ";
        if (null != inicio && null != fin) {
            WHERE = WHERE + TIEMPO;
        }
        String QUERY = SELECT + FROM + WHERE + GROUPBY + ORDERBY;
        DefaultPieDataset dataset = new DefaultPieDataset();
        System.out.println("68-charts: " + QUERY);
        try {
            st = DB_manager.getConection().createStatement();
            st.executeQuery(QUERY);
            rs = st.getResultSet();
            while (rs.next()) {
                dataset.setValue(rs.getString("Proveedor"), rs.getDouble("Compra"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_charts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }
}
