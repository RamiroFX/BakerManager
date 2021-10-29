/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import DB.DB_manager;
import Entities.E_Empresa;
import Entities.M_proveedor;
import Entities.ProductoCategoria;
import Excel.C_create_excel;
import Interface.RecibirProductoCategoriaCallback;
import Interface.RecibirProveedorCallback;
import ModeloTabla.ProductoCategoriaTableModel;
import ModeloTabla.ProveedorTableModel;
import Producto.Categoria.SeleccionarCategoria;
import Proveedor.Seleccionar_proveedor;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
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
public class FiltroCompras extends JDialog implements ActionListener, KeyListener, 
        RecibirProveedorCallback, RecibirProductoCategoriaCallback {

    public static final int PENDIENTE = 1, VENCIDAS = 2;
    private JPanel  jpSouth, jpContainer, jpProveedores, jpCategorias ;
    JTabbedPane jtpParams;
    JTable jtProveedores, jtCategorias;
    private JButton jbQuitarProv, jbAgregarProv, jbGenerar, jbCancelar;
    private JButton jbQuitarCate, jbAgregarCate, jbGenerarCate, jbCancelarCate;
    private JToggleButton jtbTipoSeleccionProv,jtbTipoSeleccionCate;
    private ProveedorTableModel tm;
    private ProductoCategoriaTableModel pctm;
    private int reportType, modo;
    private E_Empresa empresa;
    private JDateChooser jdcFechaDesde, jdcFechaHasta;
    private JCheckBox jcbFechaDesde, jcbFechaHasta;

    public FiltroCompras(JFrame frame, int reportType, int modo) {
        super(frame, "Filtro", false);
        setSize(800, 600);
        setLocationRelativeTo(frame);
        this.reportType = reportType;
        this.modo = modo;
        inicializarVista();
        concederPermisos();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpContainer, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        completarCampos();
        testInit();
    }

    private void testInit() {
        //555
        M_proveedor unProveedor = new M_proveedor();
        unProveedor.setId(16);
        unProveedor.setEntidad("Invipint S.A.C.I");
        unProveedor.setNombre("Bambi");
        unProveedor.setRuc("80022935");
        unProveedor.setRuc_id("5");
        recibirProveedor(unProveedor);
        ProductoCategoria pc = new ProductoCategoria(4, "Materia Prima");        
        recibirProductoCategoria(pc);
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        firstDay.set(Calendar.MONTH, 1);
        Calendar secDay = Calendar.getInstance();
        secDay.set(Calendar.DAY_OF_MONTH, 30);
        secDay.set(Calendar.MONTH, 2);
        this.jdcFechaDesde.setDate(firstDay.getTime());
        //this.jdcFechaHasta.setDate(secDay.getTime());
    }

    private void inicializarVista() {
        this.jpContainer = new JPanel(new BorderLayout());
        this.jtpParams = new JTabbedPane();
        /*
        * PROVEEDORES
        */
        this.jpProveedores = new JPanel(new BorderLayout());
        this.jtProveedores = new JTable();
        JScrollPane jspProveedor = new JScrollPane(this.jtProveedores);
        JPanel jpBtnProveedor = new JPanel();
        jpBtnProveedor.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Filtro de proveedores"));
        jpProveedores.add(jspProveedor, BorderLayout.CENTER);
        jpProveedores.add(jpBtnProveedor, BorderLayout.SOUTH);
        this.jbAgregarProv = new JButton("Busca");
        this.jbQuitarProv = new JButton("Quitar");
        this.jtbTipoSeleccionProv = new JToggleButton("Inclusivo");
        jpBtnProveedor.add(jbAgregarProv);
        jpBtnProveedor.add(jbQuitarProv);
        jpBtnProveedor.add(jtbTipoSeleccionProv);
        
        /*
        * CATEGORIAS
        */        
        this.jpCategorias = new JPanel(new BorderLayout());
        this.jtCategorias = new JTable();
        JScrollPane jspCategorias = new JScrollPane(this.jtCategorias);
        jpCategorias.add(jspCategorias, BorderLayout.CENTER);
        JPanel jpBtnCategorias = new JPanel();
        jpBtnCategorias.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Filtro de categorias de productos"));
        jpCategorias.add(jspCategorias, BorderLayout.CENTER);
        jpCategorias.add(jpBtnCategorias, BorderLayout.SOUTH);
        this.jbAgregarCate = new JButton("Busca");
        this.jbQuitarCate = new JButton("Quitar");
        this.jtbTipoSeleccionCate = new JToggleButton("Inclusivo");
        jpBtnCategorias.add(jbAgregarCate);
        jpBtnCategorias.add(jbQuitarCate);
        jpBtnCategorias.add(jtbTipoSeleccionCate);
        
        /*
         * BOTONES
         */
        this.jbGenerar = new JButton("Generar");
        this.jbCancelar = new JButton("Cancelar");
        this.jpSouth = new JPanel();
        this.jpSouth.add(jbGenerar);
        this.jpSouth.add(jbCancelar);
        
        /*
         * FILTRO DE FECHA
         */
        Calendar today = Calendar.getInstance();
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        this.jdcFechaDesde = new JDateChooser(firstDay.getTime());
        this.jdcFechaDesde.setPreferredSize(new Dimension(150, 25));
        this.jdcFechaHasta = new JDateChooser(today.getTime());
        this.jdcFechaHasta.setPreferredSize(new Dimension(150, 25));
        this.jcbFechaDesde = new JCheckBox("Fecha desde");
        this.jcbFechaHasta = new JCheckBox("Fecha hasta");
        this.jcbFechaHasta.setSelected(true);
        this.jcbFechaDesde.setSelected(true);
        //JPanel jpBottom = new JPanel(new GridLayout(2, 1));
        JPanel jpFiltroFecha = new JPanel();
        jpFiltroFecha.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Filtro de fecha"));
        jpFiltroFecha.add(jcbFechaDesde);
        jpFiltroFecha.add(jdcFechaDesde);
        jpFiltroFecha.add(jcbFechaHasta);
        jpFiltroFecha.add(jdcFechaHasta);
        //jpBottom.add(jpFiltroFecha);
        
        this.jtpParams.addTab("Proveedores", jpProveedores);
        this.jtpParams.addTab("Categorias", jpCategorias);
        this.jpContainer.add(jtpParams, BorderLayout.CENTER);
        this.jpContainer.add(jpFiltroFecha, BorderLayout.SOUTH);
    }

    private void completarCampos() {
        this.empresa = DB_manager.obtenerDatosEmpresa();
        this.reportType = 1;
        this.tm = new ProveedorTableModel();
        this.pctm = new ProductoCategoriaTableModel();
        this.jtProveedores.setModel(tm);
        this.jtCategorias.setModel(pctm);
    }

    private void concederPermisos() {
        this.jbGenerar.addActionListener(this);
        this.jbCancelar.addActionListener(this);
        this.jbAgregarProv.addActionListener(this);
        this.jbQuitarProv.addActionListener(this);
        this.jtbTipoSeleccionProv.addActionListener(this);
        this.jbAgregarCate.addActionListener(this);
        this.jbQuitarCate.addActionListener(this);
        /*
        KEYLISTENERS
         */
        this.jbGenerar.addKeyListener(this);
        this.jbCancelar.addKeyListener(this);
        this.jbAgregarProv.addKeyListener(this);
        this.jbQuitarProv.addKeyListener(this);
        this.jtProveedores.addKeyListener(this);
        this.jtbTipoSeleccionProv.addKeyListener(this);
        this.jbAgregarCate.addKeyListener(this);
        this.jbQuitarCate.addKeyListener(this);
    }

    private boolean validarSeleccion() {
        /*if (tm.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un cliente", "Atención", JOptionPane.PLAIN_MESSAGE);
            return false;
        }*/
        return true;
    }

    private void establecerTipoReporte() {
        if (jtbTipoSeleccionProv.isSelected()) {
            reportType = 2;
            jtbTipoSeleccionProv.setText("Exclusivo");
        } else {
            reportType = 1;
            jtbTipoSeleccionProv.setText("Inclusivo");
        }
    }

    private void generarReporte() {
        if (!validarSeleccion()) {
            return;
        }
        Date fechaDesde = null;
        if (jcbFechaDesde.isSelected()) {
            fechaDesde = jdcFechaDesde.getDate();
        }
        Date fechaHasta = null;
        if (jcbFechaHasta.isSelected()) {
            fechaHasta = jdcFechaHasta.getDate();
        }        
        C_create_excel c = new C_create_excel();
        c.exportacionProveedoresPorCategorias(tm.getList(), pctm.getList(), fechaDesde, fechaHasta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.jbGenerar)) {
            generarReporte();
        } else if (src.equals(this.jbCancelar)) {
            cerrar();
        } else if (src.equals(this.jbQuitarProv)) {
            quitarCliente();
        } else if (src.equals(this.jbAgregarProv)) {
            buscarProveedor();
        } else if (src.equals(this.jtbTipoSeleccionProv)) {
            establecerTipoReporte();
        }else if (src.equals(this.jbAgregarCate)) {
            buscarCategoria();
        } else if (src.equals(this.jbQuitarCate)) {
            quitarCategoria();
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

    private void quitarCategoria() {
        int row = jtCategorias.getSelectedRow();
        if (row > -1) {
            pctm.quitarDatos(row);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para remover", "Atención", JOptionPane.INFORMATION_MESSAGE);
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

    private void buscarCategoria() {
        SeleccionarCategoria sc = new SeleccionarCategoria(this);
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
        for (M_proveedor unCliente : tm.getList()) {
            if (Objects.equals(unCliente.getId(), proveedor.getId())) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            tm.agregarDatos(proveedor);
        }
        Utilities.c_packColumn.packColumns(this.jtProveedores, 1);
    }

    @Override
    public void recibirProductoCategoria(ProductoCategoria pc) {
        boolean existe = false;
        for (ProductoCategoria unCliente : pctm.getList()) {
            if (Objects.equals(unCliente.getId(), pc.getId())) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            pctm.agregarDatos(pc);
        }
        Utilities.c_packColumn.packColumns(this.jtCategorias, 1);        
    }
    
    
}
