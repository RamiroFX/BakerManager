/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

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
        String SELECT = "SELECT CLIE.ENTIDAD  \"Entidad\", ROUND((SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)))) \"Compra\" ";
        String FROM = "FROM PEDIDO_DETALLE PEDE, PEDIDO_CABECERA PEDI, CLIENTE CLIE ";
        String WHERE = "WHERE PEDI.ID_PEDIDO_CABECERA = PEDE.ID_PEDIDO_CABECERA AND CLIE.ID_CLIENTE =  PEDI.ID_CLIENTE ";
        String GROUPBY = "GROUP BY CLIE.ENTIDAD, CLIE.NOMBRE;";
        String TIEMPO = "AND PEDI.TIEMPO_RECEPCION BETWEEN '" + inicio + "'::timestamp "
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
        String SELECT = "SELECT PROV.ENTIDAD ||' ('||  PROV.NOMBRE ||')' \"Proveedor\", ROUND((SELECT SUM(EGDE.CANTIDAD*(EGDE.PRECIO-(EGDE.PRECIO*EGDE.DESCUENTO)/100)))) \"Compra\"";
        String FROM = "FROM EGRESO_DETALLE EGDE, EGRESO_CABECERA EGCA, PROVEEDOR PROV ";
        String WHERE = "WHERE EGCA.ID_EGRESO_CABECERA = EGDE.ID_EGRESO_CABECERA AND EGCA.ID_PROVEEDOR = PROV.ID_PROVEEDOR ";
        String GROUPBY = "GROUP BY \"Proveedor\" ";
        String TIEMPO = "AND EGCA.TIEMPO BETWEEN '" + inicio + "'::timestamp "
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

    public static XYDataset obtenerPedidoClientes(Timestamp inicio, Timestamp fin, ArrayList<Integer> idClientes) {
        String SELECT = "SELECT CLIE.ENTIDAD  \"Entidad\", ROUND((SUM(PEDE.CANTIDAD*(PEDE.PRECIO-(PEDE.PRECIO*PEDE.DESCUENTO)/100)))) \"Compra\", PEDI.TIEMPO_RECEPCION \"Fecha\" ";
        String FROM = "FROM PEDIDO_DETALLE PEDE, PEDIDO_CABECERA PEDI, CLIENTE CLIE ";
        String WHERE = "WHERE PEDI.ID_PEDIDO_CABECERA = PEDE.ID_PEDIDO_CABECERA AND CLIE.ID_CLIENTE =  PEDI.ID_CLIENTE ";
        String GROUPBY = "GROUP BY CLIE.ENTIDAD, CLIE.NOMBRE, PEDI.TIEMPO_RECEPCION;";
        String TIEMPO = "AND PEDI.TIEMPO_RECEPCION BETWEEN '" + inicio + "'::timestamp "
                + "AND '" + fin + "'::timestamp ";
        if (null != inicio && null != fin) {
            WHERE = WHERE + TIEMPO;
        }
        /*if (!idClientes.isEmpty()) {
            if (idClientes.size() == 1) {
                WHERE = WHERE + "AND CLIE.ID_CLIENTE = " + idClientes.get(0);
            } else if (idClientes.size() > 1) {
                String inClients = "";
                for (int idCliente : idClientes) {
                    inClients = inClients + idCliente + ",";
                }
                inClients = inClients.substring(0, inClients.length() - 1);
                WHERE = WHERE + "AND CLIE.ID_CLIENTE IN (" + inClients + ")";
            }
        }*/
        WHERE = WHERE + "AND CLIE.ID_CLIENTE = 14 ";
        String QUERY = SELECT + FROM + WHERE + GROUPBY;
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        System.out.println("115-charts: " + QUERY);
        try {
            st = DB_manager.getConection().createStatement();
            st.executeQuery(QUERY);
            rs = st.getResultSet();
            while (rs.next()) {
                try {
                    Day day = new Day(rs.getDate("Fecha"));
                    TimeSeries ts = new TimeSeries(rs.getString("Entidad"));
                    ts.addOrUpdate(day, rs.getDouble("Compra"));
                    dataset.addSeries(ts);
                } catch (SeriesException e) {
                    System.err.println("Error adding to series: " + e.getMessage());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_charts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }
}
