/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import DB.DB_manager;
import Entities.E_Empresa;
import Entities.M_proveedor;
import Interface.RecibirProveedorCallback;
import ModeloTabla.ProveedorTableModel;
import Proveedor.Seleccionar_proveedor;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Ramiro Ferreira
 */
public class FiltroReportesProveedores extends JDialog implements ActionListener, KeyListener, RecibirProveedorCallback {

    public static final int PENDIENTE = 1, VENCIDAS = 2;
    private JPanel jpProveedor, jpSouth;
    private JTable jtProveedores;
    private JButton jbQuitar, jbAgregar, jbGenerar, jbCancelar;
    private JToggleButton jtbTipoSeleccion;
    private ProveedorTableModel tm;
    private int reportType, modo;
    private E_Empresa empresa;
    private JDateChooser jdcFechaDesde, jdcFechaHasta;
    private JCheckBox jcbFechaDesde, jcbFechaHasta;

    public FiltroReportesProveedores(JFrame frame, int reportType, int modo) {
        super(frame, "Filtro estado de cuenta de proveedores", false);
        setSize(800, 600);
        setLocationRelativeTo(frame);
        this.reportType = reportType;
        this.modo = modo;
        inicializarVista();
        concederPermisos();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpProveedor, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        completarCampos();
        testInit();
    }

    private void testInit() {
        M_proveedor unProveedor = new M_proveedor();
        unProveedor.setId(6);
        unProveedor.setEntidad("");
        unProveedor.setNombre("");
        unProveedor.setRuc("123");
        unProveedor.setRuc_id("1");
        recibirProveedor(unProveedor);
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        firstDay.set(Calendar.MONTH, 2);
        Calendar secDay = Calendar.getInstance();
        secDay.set(Calendar.DAY_OF_MONTH, 30);
        secDay.set(Calendar.MONTH, 2);
        this.jdcFechaDesde.setDate(firstDay.getTime());
        this.jdcFechaHasta.setDate(secDay.getTime());
    }

    private void inicializarVista() {
        Calendar today = Calendar.getInstance();
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        this.jtProveedores = new JTable();
        JScrollPane jspProveedor = new JScrollPane(this.jtProveedores);
        this.jbAgregar = new JButton("Busca proveedores");
        this.jbQuitar = new JButton("Quitar");
        this.jtbTipoSeleccion = new JToggleButton("Inclusivo");
        this.jdcFechaDesde = new JDateChooser(firstDay.getTime());
        this.jdcFechaDesde.setPreferredSize(new Dimension(150, 25));
        this.jdcFechaHasta = new JDateChooser(today.getTime());
        this.jdcFechaHasta.setPreferredSize(new Dimension(150, 25));
        this.jcbFechaDesde = new JCheckBox("Fecha desde");
        this.jcbFechaHasta = new JCheckBox("Fecha hasta");
        this.jcbFechaHasta.setSelected(true);
        this.jcbFechaDesde.setSelected(true);
        JPanel jpProveedores = new JPanel();
        jpProveedores.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Filtro de proveedores"));
        JPanel jpBottom = new JPanel(new GridLayout(2, 1));
        JPanel jpFiltroFecha = new JPanel();
        jpFiltroFecha.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Filtro de fecha"));
        jpFiltroFecha.add(jcbFechaDesde);
        jpFiltroFecha.add(jdcFechaDesde);
        jpFiltroFecha.add(jcbFechaHasta);
        jpFiltroFecha.add(jdcFechaHasta);
        jpProveedores.add(jbAgregar);
        jpProveedores.add(jbQuitar);
        jpProveedores.add(jtbTipoSeleccion);
        jpBottom.add(jpFiltroFecha);
        jpBottom.add(jpProveedores);
        this.jpProveedor = new JPanel();
        this.jpProveedor.setLayout(new BorderLayout());
        this.jpProveedor.add(jspProveedor, BorderLayout.CENTER);
        this.jpProveedor.add(jpBottom, BorderLayout.SOUTH);
        /*
         * BOTONES
         */
        this.jbGenerar = new JButton("Generar");
        this.jbCancelar = new JButton("Cancelar");
        this.jpSouth = new JPanel();
        this.jpSouth.add(jbGenerar);
        this.jpSouth.add(jbCancelar);
    }

    private void completarCampos() {
        this.empresa = DB_manager.obtenerDatosEmpresa();
        this.reportType = 1;
        this.tm = new ProveedorTableModel();
        this.jtProveedores.setModel(tm);
    }

    private void concederPermisos() {
        this.jbGenerar.addActionListener(this);
        this.jbCancelar.addActionListener(this);
        this.jbAgregar.addActionListener(this);
        this.jbQuitar.addActionListener(this);
        this.jtbTipoSeleccion.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.jbGenerar.addKeyListener(this);
        this.jbCancelar.addKeyListener(this);
        this.jbAgregar.addKeyListener(this);
        this.jbQuitar.addKeyListener(this);
        this.jtProveedores.addKeyListener(this);
        this.jtbTipoSeleccion.addKeyListener(this);
    }

    private boolean validarSeleccion() {
        if (tm.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un proveedor", "Atención", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private void establecerTipoReporte() {
        if (jtbTipoSeleccion.isSelected()) {
            reportType = 2;
            jtbTipoSeleccion.setText("Exclusivo");
        } else {
            reportType = 1;
            jtbTipoSeleccion.setText("Inclusivo");
        }
    }

    private void generarReporte() {
//        if (!validarSeleccion()) {
//            return;
//        }
        ArrayList idProveedores = new ArrayList();
        tm.getList().forEach((unProveedor) -> {
            idProveedores.add(unProveedor.getId());
        });
        Date fechaDesde = null;
        if (jcbFechaDesde.isSelected()) {
            fechaDesde = jdcFechaDesde.getDate();
        }
        Date fechaHasta = null;
        if (jcbFechaHasta.isSelected()) {
            fechaHasta = jdcFechaHasta.getDate();
        }
        File file, subFile;
        JasperReport reporte = null;
        JasperReport subReports = null;
        try {
            file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\ccp.jasper");
            subFile = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\ccp_subtotal.jasper");
            File fileReporte = new File("Assets/Reportes/ccp.jasper");
            File fileSubReporte = new File("Assets/Reportes/ccp_subtotal.jasper");
            String path = fileReporte.getAbsolutePath();
            reporte = (JasperReport) JRLoader.loadObject(fileReporte);
            subReports = (JasperReport) JRLoader.loadObject(fileSubReporte);
            System.out.println("path: " + path);
        } catch (JRException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("empresa_nombre", empresa.getEntidad());
            map.put("empresa_descripcion", empresa.getDescripcion());
            map.put("fecha_desde", new java.sql.Date(fechaDesde.getTime()));
            map.put("fecha_hasta", new java.sql.Date(fechaHasta.getTime()));
            map.put("id_proveedores", idProveedores);
            map.put("subReport", subReports);
            System.out.println("Reportes.FiltroReportesProveedores.generarReporte()");
            System.out.println("empresa.getEntidad(): " + empresa.getEntidad());
            System.out.println("empresa.getDescripcion(): " + empresa.getDescripcion());
            System.out.println("fecha_desde: " + new java.sql.Date(fechaDesde.getTime()));
            System.out.println("fecha_hasta: " + new java.sql.Date(fechaHasta.getTime()));
            System.out.println("id_proveedores: " + idProveedores);
            System.out.println("subReports: " + subReports);
            System.out.println("report: " + reporte);
            JasperPrint jp = JasperFillManager.fillReport(reporte, map, DB_manager.getConection());
            JRViewer jv = new JRViewer(jp);
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv);
            jf.validate();
            jf.setVisible(true);
            jf.setSize(new Dimension(800, 600));
            jf.setLocation(300, 100);
            jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Hubo un problema al generar el reporte, intentelo nuevamente", "Alerta", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Hubo un error al generar el reporte, intentelo nuevamente", "Alerta", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.jbGenerar)) {
            generarReporte();
        } else if (src.equals(this.jbQuitar)) {
            quitarCliente();
        } else if (src.equals(this.jbAgregar)) {
            buscarProveedor();
        } else if (src.equals(this.jbCancelar)) {
            cerrar();
        } else if (src.equals(this.jtbTipoSeleccion)) {
            establecerTipoReporte();
        }

    }

    private void quitarCliente() {
        int row = jtProveedores.getSelectedRow();
        if (row > -1) {
            tm.quitarDatos(row);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para remover", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cerrar() {
        this.dispose();
    }

    private void buscarProveedor() {
        Seleccionar_proveedor sc = new Seleccionar_proveedor(this);
        sc.setCallback(this);
        sc.establecerSiempreVisible();
        sc.mostrarVista();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void recibirProveedor(M_proveedor proveedor) {
        boolean existe = false;
        for (M_proveedor unProveedor : tm.getList()) {
            if (Objects.equals(unProveedor.getId(), proveedor.getId())) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            tm.agregarDatos(proveedor);
        }
        Utilities.c_packColumn.packColumns(this.jtProveedores, 1);
    }
}
