/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import DB.DB_manager;
import Entities.E_Empresa;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
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
public class ReportesCobros {

    public static void cobrosPendientes(JFrame frame) {
        File file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes.jasper");
        E_Empresa empresa = DB_manager.obtenerDatosEmpresa();
        try {
            JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("empresa_nombre", empresa.getEntidad());
            map.put("empresa_descripcion", empresa.getDescripcion());
            JasperPrint jp = JasperFillManager.fillReport(reporte, map, DB_manager.getConection());
            JRViewer jv = new JRViewer(jp);
            JDialog jd = new JDialog(frame);
            jd.getContentPane().add(jv);
            jd.validate();
            jd.setVisible(true);
            jd.setSize(new Dimension(800, 600));
            jd.setLocation(300, 100);
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public static void cobrosPendientesVencidos(JFrame frame) {
        File file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_vencidos.jasper");
        E_Empresa empresa = DB_manager.obtenerDatosEmpresa();
        try {
            JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("empresa_nombre", empresa.getEntidad());
            map.put("empresa_descripcion", empresa.getDescripcion());
            JasperPrint jp = JasperFillManager.fillReport(reporte, map, DB_manager.getConection());
            JRViewer jv = new JRViewer(jp);
            JDialog jf = new JDialog(frame);
            jf.getContentPane().add(jv);
            jf.validate();
            jf.setVisible(true);
            jf.setSize(new Dimension(800, 600));
            jf.setLocation(300, 100);
            jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public static void cobrosPendientesPorCliente(JFrame frame) {
        FiltroCliente fc = new FiltroCliente(frame, 1, FiltroCliente.PENDIENTE);
        fc.setVisible(true);
    }

    public static void cobrosPendientesPorClienteVencidos(JFrame frame) {
        FiltroCliente fc = new FiltroCliente(frame, 1, FiltroCliente.VENCIDAS);
        fc.setVisible(true);
    }

    public static void estadoCuentaClientes(JFrame frame) {
        FiltroReportes fc = new FiltroReportes(frame, 1, FiltroCliente.VENCIDAS);
        fc.setVisible(true);
    }
    public static void estadoCuentaClientesXLS(JFrame frame) {
        FiltroReportes fc = new FiltroReportes(frame, 1, FiltroReportes.ESTADO_CUENTA_XLS);
        fc.setVisible(true);
    }
}
