/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Cliente.SeleccionarCliente;
import DB.DB_manager;
import Entities.E_Empresa;
import Entities.M_cliente;
import Interface.RecibirClienteCallback;
import ModeloTabla.ClienteTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
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
public class FiltroCliente extends JDialog implements ActionListener, KeyListener, RecibirClienteCallback {

    public static final int PENDIENTE = 1, VENCIDAS = 2;
    JPanel jpCliente, jpSouth;
    JTable jtClientes;
    JButton jbQuitar, jbAgregar, jbGenerar, jbCancelar;
    JToggleButton jtbTipoSeleccion;
    ClienteTableModel tm;
    private int reportType, modo;
    E_Empresa empresa;

    public FiltroCliente(JFrame frame, int reportType, int modo) {
        super(frame, "Filtro", false);
        setSize(800, 600);
        setLocationRelativeTo(frame);
        this.reportType = reportType;
        this.modo = modo;
        inicializarVista();
        concederPermisos();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpCliente, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
        completarCampos();
    }

    private void inicializarVista() {
        /*
         * CATEGORIAS
         */
        this.jtClientes = new JTable();
        JScrollPane jspProveedor = new JScrollPane(this.jtClientes);
        this.jbAgregar = new JButton("Busca clientes");
        this.jbQuitar = new JButton("Quitar");
        this.jtbTipoSeleccion = new JToggleButton("Inclusivo");
        JPanel jpBotones = new JPanel();
        jpBotones.add(jbAgregar);
        jpBotones.add(jbQuitar);
        jpBotones.add(jtbTipoSeleccion);
        this.jpCliente = new JPanel();
        this.jpCliente.setLayout(new BorderLayout());
        this.jpCliente.add(jspProveedor, BorderLayout.CENTER);
        this.jpCliente.add(jpBotones, BorderLayout.SOUTH);
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
        this.tm = new ClienteTableModel();
        this.jtClientes.setModel(tm);
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
        this.jtClientes.addKeyListener(this);
        this.jtbTipoSeleccion.addKeyListener(this);
    }

    private boolean validarSeleccion() {
        if (tm.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un cliente", "Atención", JOptionPane.PLAIN_MESSAGE);
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
        if (!validarSeleccion()) {
            return;
        }
        ArrayList idClientes = new ArrayList();
        tm.getList().forEach((unCliente) -> {
            idClientes.add(unCliente.getIdCliente());
        });
        File file;
        JasperReport reporte = null;
        switch (reportType) {
            case 1: {
                switch (modo) {
                    case 1: {
                        System.out.println("inclusivo pendiente");
                        try {
                            file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_inclusivo.jasper");

                            System.out.println("file: " + file);
                            reporte = (JasperReport) JRLoader.loadObject(file);
                        } catch (JRException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("inclusivo vencida");
                        try {
                            file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_vencidas_inclusivo.jasper");
                            reporte = (JasperReport) JRLoader.loadObject(file);
                        } catch (JRException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        break;
                    }
                }
                break;
            }
            case 2: {
                switch (modo) {
                    case 1: {
                        System.out.println("exclusivo pendiente");
                        try {
                            //file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_vencidos.jasper");
                            file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_exclusivo.jasper");
                            reporte = (JasperReport) JRLoader.loadObject(file);
                        } catch (JRException ex) {
                            JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("exclusivo vencida");
                        try {
                            //file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_vencidos.jasper");
                            file = new File(System.getProperty("user.dir") + "\\Assets\\Reportes\\cobros_pendientes_por_cliente_vencidas_exclusivo.jasper");
                            reporte = (JasperReport) JRLoader.loadObject(file);
                        } catch (JRException ex) {
                            JOptionPane.showMessageDialog(this, "No se encontró la ubicación del reporte", "Atención", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        break;
                    }
                }
                break;
            }
        }

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("empresa_nombre", empresa.getEntidad());
            map.put("empresa_descripcion", empresa.getDescripcion());
            map.put("id_clientes", idClientes);
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
            buscarClientes();
        } else if (src.equals(this.jbCancelar)) {
            cerrar();
        } else if (src.equals(this.jtbTipoSeleccion)) {
            establecerTipoReporte();
        }

    }

    private void quitarCliente() {
        int row = jtClientes.getSelectedRow();
        if (row > -1) {
            tm.quitarDatos(row);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para remover", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cerrar() {
        this.dispose();
    }

    private void buscarClientes() {
        SeleccionarCliente sc = new SeleccionarCliente(this);
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

    private boolean clienteRepetido() {
        return true;
    }

    @Override
    public void recibirCliente(M_cliente cliente) {
        boolean existe = false;
        for (M_cliente unCliente : tm.getList()) {
            if (Objects.equals(unCliente.getIdCliente(), cliente.getIdCliente())) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            tm.agregarDatos(cliente);
        }
        Utilities.c_packColumn.packColumns(this.jtClientes, 1);
    }
}
