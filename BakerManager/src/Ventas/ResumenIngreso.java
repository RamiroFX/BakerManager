/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Ingreso;
import Entities.E_facturacionCabecera;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_rol_usuario;
import Excel.ExportarVentas;
import Impresora.Impresora;
import Interface.InterfaceFacturaDetalle;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaCabeceraTableModel;
import ModeloTabla.FacturaDetalleTableModel;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */
public class ResumenIngreso extends JDialog implements ActionListener, KeyListener, InterfaceFacturaDetalle {

    JScrollPane jspEgreso, jspDetalle;
    JTable jtIngreso, jtDetalle;
    JButton jbSalir, jbImportarXLS, jbImprimirFacturacion;
    JLabel jlContado, jlCredito, jlTotal;
    JFormattedTextField jftTotalEgreso, jftTotalEgCont, jftTotalEgCred;
    Date inicio, fin;
    String idEmpleado, tipo_operacion;
    Integer nro_factura;
    JTabbedPane jtpPanel;
    M_cliente cliente;
    E_facturacionCabecera facturacionCabecera;
    FacturaCabeceraTableModel tm;
    Estado estado;

    public ResumenIngreso(C_inicio c_inicio, FacturaCabeceraTableModel tm, M_cliente cliente_entidad,
            Integer nro_factura, String idEmpleado, Date inicio, Date fin,
            String tipo_operacion, Estado estado) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Resumen de ingresos");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        this.inicio = inicio;
        this.fin = fin;
        this.cliente = cliente_entidad;
        this.idEmpleado = idEmpleado;
        this.tipo_operacion = tipo_operacion;
        this.nro_factura = nro_factura;
        this.estado = estado;
        inicializarComponentes();
        this.tm = tm;
        inicializarVista(tm, inicio, fin);
        agregarListener();
    }

    public ResumenIngreso(C_inicio c_inicio) {
        super(c_inicio.vista, DEFAULT_MODALITY_TYPE);
        setTitle("Detalle de facturación");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(c_inicio.vista);
        inicializarComponentes();
        agregarListener();
    }

    public void inicializarDatos(E_facturacionCabecera facturacionCabecera) {
        this.facturacionCabecera = facturacionCabecera;
        FacturaCabeceraTableModel tm = new FacturaCabeceraTableModel(FacturaCabeceraTableModel.COMPLETO);
        tm.setFacturaCabeceraList(DB_Ingreso.obtenerVentasPorFacturacion(facturacionCabecera.getId()));
        jtIngreso.setModel(tm);
        Utilities.c_packColumn.packColumns(jtIngreso, 1);
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal totalContado = new BigDecimal("0");
        BigDecimal totalCredito = new BigDecimal("0");
        for (M_facturaCabecera faca : tm.getFacturaCabeceraList()) {
            total = total.add(new BigDecimal(faca.getTotal()+""));
            switch (faca.getCondVenta().getId()) {
                case E_tipoOperacion.CONTADO: {//contado
                    totalContado = totalContado.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {//credito
                    totalCredito = totalCredito.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
                default: {//credito
                    totalCredito = totalCredito.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
            }
        }
        FacturaDetalleTableModel tmDetalle = new FacturaDetalleTableModel(this);
        tmDetalle.setFacturaDetalleList(DB_Ingreso.consultarIngresoDetalleAgrupado(tm.getFacturaCabeceraList()));
        jtDetalle.setModel(tmDetalle);
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
        this.jbImportarXLS.setVisible(false);
        this.jbImprimirFacturacion.setVisible(true);
        this.jbImprimirFacturacion.addActionListener(this);
    }

    private void inicializarComponentes() {
        jtIngreso = new JTable();
        jtIngreso.getTableHeader().setReorderingAllowed(false);
        jspEgreso = new JScrollPane(jtIngreso);
        jtDetalle = new JTable();
        jtDetalle.getTableHeader().setReorderingAllowed(false);
        jspDetalle = new JScrollPane(jtDetalle);
        JPanel jpTotalEgreso = new JPanel(new GridLayout(3, 2));
        jftTotalEgreso = new JFormattedTextField();
        jftTotalEgreso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        jftTotalEgCont = new JFormattedTextField();
        jftTotalEgCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        //jftTotalEgCont.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
        jftTotalEgCred = new JFormattedTextField();
        jftTotalEgCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        jlContado = new JLabel("Ingresos al contado");
        jlContado.setHorizontalAlignment(SwingConstants.CENTER);
        jlCredito = new JLabel("Ingresos a crédito");
        jlCredito.setHorizontalAlignment(SwingConstants.CENTER);
        jlTotal = new JLabel("Total ingresos");
        jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
        jpTotalEgreso.add(jlContado);
        jpTotalEgreso.add(jftTotalEgCont);
        jpTotalEgreso.add(jlCredito);
        jpTotalEgreso.add(jftTotalEgCred);
        jpTotalEgreso.add(jlTotal);
        jpTotalEgreso.add(jftTotalEgreso);
        jbSalir = new JButton("Salir");
        jbImportarXLS = new JButton("Importar a excel");
        jbImportarXLS.setName("exportar venta");
        jbImprimirFacturacion = new JButton("Imprimir facturación");
        jbImprimirFacturacion.setName("imprimir facturacion");
        jbImprimirFacturacion.setVisible(false);
        jtpPanel = new JTabbedPane();
        jtpPanel.addKeyListener(this);

        JPanel jpCenter = new JPanel(new BorderLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpSouth.add(jbImportarXLS);
        jpSouth.add(jbImprimirFacturacion);
        jpSouth.add(jbSalir);
        jpCenter.add(jspEgreso, BorderLayout.CENTER);
        jpCenter.add(jpTotalEgreso, BorderLayout.SOUTH);

        jtpPanel.addTab("Resumen", jpCenter);
        jtpPanel.addTab("Detalle", jspDetalle);
        getContentPane().add(jtpPanel, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    private void inicializarVista(FacturaCabeceraTableModel tm, Date inicio, Date fin) {
        jtIngreso.setModel(tm);
        Utilities.c_packColumn.packColumns(jtIngreso, 1);
        BigDecimal total = new BigDecimal("0");
        BigDecimal totalContado = new BigDecimal("0");
        BigDecimal totalCredito = new BigDecimal("0");
        for (M_facturaCabecera faca : tm.getFacturaCabeceraList()) {
            total = total.add(new BigDecimal(faca.getTotal()+""));
            switch (faca.getCondVenta().getId()) {
                case E_tipoOperacion.CONTADO: {//contado
                    totalContado = totalContado.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {//credito
                    totalCredito = totalCredito.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
                default: {//credito
                    totalCredito = totalCredito.add(new BigDecimal(faca.getTotal()+""));
                    break;
                }
            }
        }
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(inicio);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 000);
        java.sql.Timestamp fInicio = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        calendario.setTime(fin);
        calendario.set(Calendar.HOUR_OF_DAY, 24);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp fFin = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        jtDetalle.setModel(DB_Ingreso.consultarIngresoDetalleAgrupado(fInicio, fFin, cliente, estado));
        Utilities.c_packColumn.packColumns(jtDetalle, 1);
        jftTotalEgCred.setValue(totalCredito);
        jftTotalEgCont.setValue(totalContado);
        jftTotalEgreso.setValue(total);
    }

    private void agregarListener() {
        jbSalir.addActionListener(this);
        jbImportarXLS.addActionListener(this);
    }

    private void importarExcelCompleto(M_cliente cliente, Integer nro_factura, String idEmpleado, String tipo_operacion, Estado estado) {
        ArrayList<M_facturaCabecera> ed = new ArrayList(this.tm.getFacturaCabeceraList());
        ExportarVentas ce = new ExportarVentas("Resumen de ingresos", ed);
        ce.exportacionCompleta();
    }

    private void importarExcelResumido(M_cliente cliente, Integer nro_factura, String idEmpleado, String tipo_operacion, Estado estado) {
        ArrayList<M_facturaCabecera> ed = new ArrayList(this.tm.getFacturaCabeceraList());
        ExportarVentas ce = new ExportarVentas("Resumen de ingresos", ed);
        ce.exportacionResumida();
    }

    private void exportHandler() {
        Object[] options = {"Completo",
            "Resumido"};
        int n = JOptionPane.showOptionDialog(this,
                "Eliga tipo de reporte",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                //Completo
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelCompleto(cliente, nro_factura, idEmpleado, tipo_operacion, estado);
                    }
                });
                break;
            }
            case 1: {
                //Minimalista
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        importarExcelResumido(cliente, nro_factura, idEmpleado, tipo_operacion, estado);
                    }
                });
                break;
            }
        }
    }

    private void keyPressedHandler(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: {
                        cerrar();
                    }
                }
            }
        });
    }

    private void imprimirFacturacion() {
        Object[] options = {"Ticket", "Boleta", "Factura"};
        int n = JOptionPane.showOptionDialog(this,
                "Eliga tipo de impresión",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FacturaCabeceraTableModel tmCabecera = (FacturaCabeceraTableModel) jtIngreso.getModel();
                        FacturaDetalleTableModel tmDetalle = (FacturaDetalleTableModel) jtDetalle.getModel();
                        if (tmCabecera.getFacturaCabeceraList().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay datos para impirmir");
                        } else {
                            M_facturaCabecera facturaCabecera = tmCabecera.getFacturaCabeceraList().get(0);
                            facturaCabecera.setTiempo(new Timestamp(facturacionCabecera.getTiempo().getTime()));
                            ArrayList<M_facturaDetalle> facturaDetalle = (ArrayList<M_facturaDetalle>) tmDetalle.getFacturaDetalleList();
                            M_rol_usuario rol_usuario = DatosUsuario.getRol_usuario();
                            Impresora.imprimirTicketVenta(rol_usuario, facturaCabecera, facturaDetalle);
                        }
                    }
                });
                break;
            }
            case 1: {
                //Boleta
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FacturaCabeceraTableModel tmCabecera = (FacturaCabeceraTableModel) jtIngreso.getModel();
                        FacturaDetalleTableModel tmDetalle = (FacturaDetalleTableModel) jtDetalle.getModel();
                        if (tmCabecera.getFacturaCabeceraList().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay datos para impirmir");
                        } else {
                            M_facturaCabecera facturaCabecera = tmCabecera.getFacturaCabeceraList().get(0);
                            facturaCabecera.setTiempo(new Timestamp(facturacionCabecera.getTiempo().getTime()));
                            ArrayList<M_facturaDetalle> facturaDetalle = (ArrayList<M_facturaDetalle>) tmDetalle.getFacturaDetalleList();
                            Impresora.imprimirBoletaVenta(facturaCabecera, facturaDetalle);
                        }
                    }
                });
                break;
            }
            case 2: {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FacturaCabeceraTableModel tmCabecera = (FacturaCabeceraTableModel) jtIngreso.getModel();
                        FacturaDetalleTableModel tmDetalle = (FacturaDetalleTableModel) jtDetalle.getModel();
                        if (tmCabecera.getFacturaCabeceraList().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay datos para impirmir");
                        } else {
                            M_facturaCabecera facturaCabecera = tmCabecera.getFacturaCabeceraList().get(0);
                            facturaCabecera.setTiempo(new Timestamp(facturacionCabecera.getTiempo().getTime()));
                            ArrayList<M_facturaDetalle> facturaDetalle = (ArrayList<M_facturaDetalle>) tmDetalle.getFacturaDetalleList();
                            Impresora.imprimirFacturaVenta(facturaCabecera, facturaDetalle);
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(jbSalir)) {
            dispose();
        } else if (ae.getSource().equals(jbImportarXLS)) {
            exportHandler();
        } else if (ae.getSource().equals(jbImprimirFacturacion)) {
            imprimirFacturacion();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedHandler(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void cerrar() {
        this.dispose();
    }

    @Override
    public void notificarCambioFacturaDetalle() {
    }
}
