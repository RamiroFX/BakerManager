/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ramiro Ferreira
 */
public class reportTest {

    public static void main(String[] args) {
        try {
            anotherTest();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(reportTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(reportTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(reportTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(reportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void anotherTest() throws ClassNotFoundException, SQLException, JRException, ParseException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/bakermanager";
        Connection conexion = DriverManager.getConnection(url, "postgres", "postgresql");
        File file = new File(System.getProperty("user.dir") + "\\src\\Assets\\Reportes\\ResumenComprasSimpleCategoria.jasper");
        //File file = new File(System.getProperty("user.dir") + "\\src\\Assets\\Reportes\\ResumenVentasSimpleCategoria.jasper");
        JasperReport reporte = (JasperReport) JRLoader.loadObject(file);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.DAY_OF_MONTH, 11);
        calendarStart.set(Calendar.MONTH, 0);//AGOSTO
        calendarStart.set(Calendar.YEAR, 2015);
        calendarStart.set(Calendar.MILLISECOND, 250);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 11);
        calendarEnd.set(Calendar.MONTH, 0);//AGOSTO
        calendarEnd.set(Calendar.YEAR, 2016);
        calendarEnd.set(Calendar.MILLISECOND, 250);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sDate", new Timestamp(calendarStart.getTime().getTime()));
            map.put("eDate", new Timestamp(calendarEnd.getTime().getTime()));
            map.put("categorias", Arrays.asList(1));
            JasperPrint jp = JasperFillManager.fillReport(reporte, map, conexion);
            JRViewer jv = new JRViewer(jp);
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv);
            jf.validate();
            jf.setVisible(true);
            jf.setSize(new Dimension(800, 600));
            jf.setLocation(300, 100);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
}
