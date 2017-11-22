/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import DB.DB_manager;
import Entities.ProductoCategoria;
import ModeloTabla.ProductoCategoriaTableModel;
import ModeloTabla.SeleccionProductoCategoria;
import bakermanager.C_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
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
public class FiltroReporte_Cate extends JDialog implements ActionListener {

    JPanel jpCategoria, jpFecha, jpSouth;
    JDateChooser jdcFechaInicio, jdcFechaFin;
    JTable jtProdCategorias;
    JButton jbQuitar, jbAgregar, jbGenerar, jbCancelar;

    public FiltroReporte_Cate(C_inicio c_inicio) {
        super(c_inicio.vista, "Filtro", false);
        setSize(400, 600);
        setLocationRelativeTo(c_inicio.vista);
        inicializarVista();
        concederPermisos();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpFecha, BorderLayout.NORTH);
        getContentPane().add(jpCategoria, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        completarCampos();
    }

    private void inicializarVista() {
        /*
         * RANGO DE FECHAS
         */
        this.jdcFechaInicio = new JDateChooser();
        this.jdcFechaFin = new JDateChooser();
        this.jdcFechaFin.setDate(Calendar.getInstance().getTime());
        this.jpFecha = new JPanel(new MigLayout());
        this.jpFecha.setBorder(javax.swing.BorderFactory.createTitledBorder("Rango de fechas"));
        this.jpFecha.add(jdcFechaInicio, "pushx,growx");
        this.jpFecha.add(jdcFechaFin, "pushx,growx");
        /*
         * CATEGORIAS
         */
        this.jtProdCategorias = new JTable();
        JScrollPane jspProveedor = new JScrollPane(this.jtProdCategorias);
        this.jbAgregar = new JButton("Agregar");
        this.jbQuitar = new JButton("Quitar");
        JPanel jpBotones = new JPanel();
        jpBotones.add(jbAgregar);
        jpBotones.add(jbQuitar);
        this.jpCategoria = new JPanel();
        this.jpCategoria.setLayout(new BorderLayout());
        this.jpCategoria.setBorder(javax.swing.BorderFactory.createTitledBorder("Categorías"));
        this.jpCategoria.add(jspProveedor, BorderLayout.CENTER);
        this.jpCategoria.add(jpBotones, BorderLayout.SOUTH);
        /*
         * BOTONES
         */
        this.jbGenerar = new JButton("Generar");
        this.jbCancelar = new JButton("Cancelar");
        this.jpSouth = new JPanel();
        this.jpSouth.add(jbCancelar);
        this.jpSouth.add(jbGenerar);
    }

    private void completarCampos() {
        ProductoCategoriaTableModel tm = new ProductoCategoriaTableModel();
        ArrayList<SeleccionProductoCategoria> seleccionProductoCategorias = new ArrayList<>();
        for (ProductoCategoria obtenerCategoria : DB_manager.obtenerCategorias()) {
            seleccionProductoCategorias.add(new SeleccionProductoCategoria(obtenerCategoria, true));
        }
        tm.setProductoCategoriaList(seleccionProductoCategorias);
        this.jtProdCategorias.setModel(tm);
    }

    private void concederPermisos() {
        this.jbGenerar.addActionListener(this);
        this.jbCancelar.addActionListener(this);
        this.jbAgregar.addActionListener(this);
        this.jbQuitar.addActionListener(this);
    }

    private void generarReporte() {
        if (validarFechas(jdcFechaInicio.getDate(), jdcFechaFin.getDate())) {
            int row = this.jtProdCategorias.getModel().getRowCount();
            ArrayList categories = new ArrayList();
            for (int i = 0; i < row; i++) {
                if (Boolean.valueOf((boolean) this.jtProdCategorias.getModel().getValueAt(i, 2)) == true) {
                    categories.add(Integer.valueOf(String.valueOf(this.jtProdCategorias.getModel().getValueAt(i, 0))));
                }
            }
            if (categories.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione al menos una categoría", "Atención", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            File file = new File(System.getProperty("user.dir") + "\\src\\Assets\\Reportes\\ResumenComprasSimpleCategoria.jasper");
            JasperReport reporte = null;
            try {
                reporte = (JasperReport) JRLoader.loadObject(file);
            } catch (JRException ex) {
                JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
            }

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(jdcFechaInicio.getDate());
            calendarStart.set(Calendar.HOUR_OF_DAY, 0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            calendarStart.set(Calendar.MILLISECOND, 0);

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(jdcFechaFin.getDate());
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarEnd.set(Calendar.MILLISECOND, 250);
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sDate", new Timestamp(calendarStart.getTime().getTime()));
                map.put("eDate", new Timestamp(calendarEnd.getTime().getTime()));
                map.put("categorias", categories);
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
                JOptionPane.showMessageDialog(this, "Hubo un problema al generar el reporte, intentelo nuevamente", "Alerta", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            jdcFechaFin.setDate(this.jdcFechaInicio.getDate());
            jdcFechaFin.updateUI();
            JOptionPane.showMessageDialog(this, "La fecha inicio debe ser menor que fecha final", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.jbGenerar)) {
            generarReporte();
        } else if (src.equals(this.jbQuitar)) {
            quitarCategoria();
        } else if (src.equals(this.jbAgregar)) {
            agregarCategorias();
        } else if (src.equals(this.jbCancelar)) {
            cerrar();
        }

    }

    private void quitarCategoria() {
        int rows = this.jtProdCategorias.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.jtProdCategorias.getModel().setValueAt(false, i, 2);
        }
    }

    private void cerrar() {
        this.dispose();
    }

    private void agregarCategorias() {
        int rows = this.jtProdCategorias.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            this.jtProdCategorias.getModel().setValueAt(true, i, 2);
        }
    }
}
