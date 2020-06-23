/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import DB.DB_Cobro;
import Interface.CommonFormat;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import DB.DB_Pago;
import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaCabecera;
import Entities.E_formaPago;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egreso_cabecera;
import Entities.Moneda;
import Interface.InterfaceCajaMovimientos;
import Interface.MovimientosCaja;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.SeleccionCompraCabecera;
import bakermanager.C_inicio;
import com.nitido.utils.toaster.Toaster;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class SaldarCaja extends JDialog implements ActionListener, KeyListener, InterfaceCajaMovimientos {

    private static final String TT_EFECTIVO_RENDIR = "Efectivo a rendir: suma de ventas y cobros en efectivo menos compras y pagos en efectivo";

    public JDateChooser jddInicio, jddFinal;
    public JComboBox jcbHoraInicio, jcbMinutoInicio, jcbHoraFin, jcbMinutoFin;
    private JButton saveButton, cancelButton, jbFondoAnterior, jbDetalle;
    private JLabel jlFondoApertura, jlFondoCierre, jlEgresoTotal, //jlDifCaja,
            jlDepositar, jlEfectivoRendir, jlEgresoCredito, jlEgresoContado, jlIngresoTotal,
            jlIngresoCredito, jlIngresoContado, jlTotalEgrIng1, jlTotalEgrIng2,
            jlTotalCobrado, jlTotalCobradoEfectivo, jlTotalCobradoCheque,
            jlTotalPagado, jlTotalPagadoEfectivo, jlTotalPagadoCheque;
    private JFormattedTextField jtfFondoApertura, jtfFondoCierre, jtfDepositar, jtfEgresoTotal, //jtfDifCaja,
            jtfEgresoCredito, jtfEgresoContado, jtfIngresoTotal, jftEfectivoRendir,
            jtfIngresoCredito, jtfIngresoContado, jtfTotalEgrIng1, jtfTotalEgrIng2,
            jtfTotalCobrado, jtfTotalCobradoEfectivo, jtfTotalCobradoCheque,
            jtfTotalPagado, jtfTotalPagadoEfectivo, jtfTotalPagadoCheque;
    //ARQUEO CAJA VARIABLES
    private JTabbedPane jpArqueoCaja;
    private JTable jtFondoApertura, jtFondoCierre, jtDepositar;
    private JScrollPane jspFondoApertura, jspFondoCierre, jspDepositar;
    private ArqueoCajaTableModel tbmFondoApertura, tbmFondoCierre, tbmDepositar;
    private MovimientosCaja movimientosCaja;

    public SaldarCaja(C_inicio inicio) {
        super(inicio.vista, "Saldar caja", true);
        initializeVariables();
        constructLayout();
        addListeners();
        initializeLogic();
        setWindows(inicio.vista);
    }

    private void initializeVariables() {
        int prefCols = 20;
        Date today = Calendar.getInstance().getTime();
        jddInicio = new JDateChooser(today);
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser(today);
        jddFinal.setPreferredSize(new Dimension(150, 10));
        this.jbDetalle = new JButton("Detalle");
        this.saveButton = new JButton("Guardar");
        this.cancelButton = new JButton("Cancelar");
        this.jbFondoAnterior = new JButton("Caja chica anterior");
        this.jlFondoApertura = new JLabel("Fondo apertura");
        this.jlFondoApertura.setFont(CommonFormat.fuenteTitulo);
        this.jlFondoCierre = new JLabel("Fondo cierre");
        this.jlFondoCierre.setFont(CommonFormat.fuenteTitulo);
        //this.jlDifCaja = new JLabel("Dif. de Caja");
        //this.jlDifCaja.setFont(CommonFormat.fuenteSubTitulo);
        this.jlDepositar = new JLabel("Depositado");
        this.jlDepositar.setFont(CommonFormat.fuenteTitulo);
        this.jlEfectivoRendir = new JLabel("Efectivo a rendir");
        this.jlEfectivoRendir.setFont(CommonFormat.fuenteTitulo);
        this.jlEgresoTotal = new JLabel("Egreso total");
        this.jlEgresoTotal.setFont(CommonFormat.fuenteTitulo);
        this.jlEgresoCredito = new JLabel("Egreso credito");
        this.jlEgresoCredito.setFont(CommonFormat.fuenteSubTitulo);
        this.jlEgresoContado = new JLabel("Egreso contado");
        this.jlEgresoContado.setFont(CommonFormat.fuenteSubTitulo);
        this.jlIngresoTotal = new JLabel("Ingreso total");
        this.jlIngresoTotal.setFont(CommonFormat.fuenteTitulo);
        this.jlIngresoCredito = new JLabel("Ingreso credito");
        this.jlIngresoCredito.setFont(CommonFormat.fuenteSubTitulo);
        this.jlIngresoContado = new JLabel("Ingreso contado");
        this.jlIngresoContado.setFont(CommonFormat.fuenteSubTitulo);
        this.jlTotalEgrIng1 = new JLabel("Egreso+Ingreso");
        this.jlTotalEgrIng1.setFont(CommonFormat.fuenteTitulo);
        this.jlTotalEgrIng2 = new JLabel("Egreso-Ingreso");
        this.jlTotalEgrIng2.setFont(CommonFormat.fuenteTitulo);
        this.jtfFondoApertura = new JFormattedTextField();
        this.jtfFondoApertura.setColumns(prefCols);
        this.jtfFondoApertura.addKeyListener(this);
        this.jtfFondoCierre = new JFormattedTextField();
        this.jtfFondoCierre.setColumns(prefCols);
        this.jtfFondoCierre.addKeyListener(this);
        //this.jtfDifCaja = new JFormattedTextField();
        //this.jtfDifCaja.setColumns(prefCols);
        this.jtfDepositar = new JFormattedTextField();
        this.jtfDepositar.setColumns(prefCols);
        this.jftEfectivoRendir = new JFormattedTextField();
        this.jftEfectivoRendir.setColumns(prefCols);
        this.jftEfectivoRendir.setToolTipText(TT_EFECTIVO_RENDIR);
        this.jtfEgresoTotal = new JFormattedTextField();
        this.jtfEgresoTotal.setColumns(prefCols);
        this.jtfEgresoTotal.addKeyListener(this);
        this.jtfEgresoCredito = new JFormattedTextField();
        this.jtfEgresoCredito.setColumns(prefCols);
        this.jtfEgresoContado = new JFormattedTextField();
        this.jtfEgresoContado.setColumns(prefCols);
        this.jtfIngresoTotal = new JFormattedTextField();
        this.jtfIngresoTotal.setColumns(prefCols);
        this.jtfIngresoTotal.addKeyListener(this);
        this.jtfIngresoContado = new JFormattedTextField();
        this.jtfIngresoContado.setColumns(prefCols);
        this.jtfIngresoCredito = new JFormattedTextField();
        this.jtfIngresoCredito.setColumns(prefCols);
        this.jtfTotalEgrIng1 = new JFormattedTextField();
        this.jtfTotalEgrIng1.setColumns(prefCols);
        this.jtfTotalEgrIng1.addKeyListener(this);
        this.jtfTotalEgrIng2 = new JFormattedTextField();
        this.jtfTotalEgrIng2.setColumns(prefCols);
        this.jtfTotalEgrIng2.addKeyListener(this);
        /*
        TOTAL COBRADO VARIABLES
         */
        this.jlTotalCobrado = new JLabel("Total cobrado");
        this.jlTotalCobrado.setFont(CommonFormat.fuenteTitulo);
        this.jlTotalCobradoEfectivo = new JLabel("Cobrado efectivo");
        this.jlTotalCobradoCheque = new JLabel("Cobrado cheque");
        this.jtfTotalCobrado = new JFormattedTextField();
        this.jtfTotalCobrado.setColumns(prefCols);
        this.jtfTotalCobrado.addKeyListener(this);
        this.jtfTotalCobradoEfectivo = new JFormattedTextField();
        this.jtfTotalCobradoEfectivo.setColumns(prefCols);
        this.jtfTotalCobradoEfectivo.addKeyListener(this);
        this.jtfTotalCobradoCheque = new JFormattedTextField();
        this.jtfTotalCobradoCheque.setColumns(prefCols);
        this.jtfTotalCobradoCheque.addKeyListener(this);

        /*
        TOTAL PAGADO VARIABLES
         */
        this.jlTotalPagado = new JLabel("Total pagado");
        this.jlTotalPagado.setFont(CommonFormat.fuenteTitulo);
        this.jlTotalPagadoEfectivo = new JLabel("Pagado efectivo");
        this.jlTotalPagadoCheque = new JLabel("Pagado cheque");
        this.jtfTotalPagado = new JFormattedTextField();
        this.jtfTotalPagado.setColumns(prefCols);
        this.jtfTotalPagado.addKeyListener(this);
        this.jtfTotalPagadoEfectivo = new JFormattedTextField();
        this.jtfTotalPagadoEfectivo.setColumns(prefCols);
        this.jtfTotalPagadoEfectivo.addKeyListener(this);
        this.jtfTotalPagadoCheque = new JFormattedTextField();
        this.jtfTotalPagadoCheque.setColumns(prefCols);
        this.jtfTotalPagadoCheque.addKeyListener(this);

        this.jtfFondoApertura.setEditable(false);
        this.jtfFondoCierre.setEditable(false);
        //this.jtfDifCaja.setEditable(false);
        this.jtfDepositar.setEditable(false);
        this.jftEfectivoRendir.setEditable(false);
        this.jtfTotalEgrIng1.setEditable(false);
        this.jtfTotalEgrIng2.setEditable(false);
        this.jtfEgresoTotal.setEditable(false);
        this.jtfEgresoContado.setEditable(false);
        this.jtfEgresoCredito.setEditable(false);
        this.jtfIngresoTotal.setEditable(false);
        this.jtfIngresoContado.setEditable(false);
        this.jtfIngresoCredito.setEditable(false);
        jcbHoraInicio = new JComboBox();
        jcbMinutoInicio = new JComboBox();
        jcbHoraFin = new JComboBox();
        jcbMinutoFin = new JComboBox();
        for (int i = 0; i < 10; i++) {
            jcbHoraInicio.addItem("0" + i);
            jcbHoraFin.addItem("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            jcbHoraInicio.addItem("" + i);
            jcbHoraFin.addItem("" + i);
        }
        for (int i = 0; i < 10; i++) {
            jcbMinutoInicio.addItem("0" + i);
            jcbMinutoFin.addItem("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            jcbMinutoInicio.addItem("" + i);
            jcbMinutoFin.addItem("" + i);
        }
        ///INICIO FU
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        String horaT = sdf.format(currentTime).substring(0, 2);
        int horas = Integer.valueOf(horaT);
        if (horas >= 0 && horas < 10) {
            this.jcbHoraFin.setSelectedItem("0" + horas);
        } else {
            this.jcbHoraFin.setSelectedItem("" + horas);
        }
        int minutoFin = Calendar.getInstance().get(Calendar.MINUTE);
        if (minutoFin < 10) {
            jcbMinutoFin.setSelectedItem("0" + minutoFin);
        } else {
            jcbMinutoFin.setSelectedItem("" + minutoFin);
        }
        //ARQUEO CAJA 
        tbmFondoApertura = new ArqueoCajaTableModel();
        jtFondoApertura = new JTable(tbmFondoApertura);
        jspFondoApertura = new JScrollPane(jtFondoApertura);
        tbmFondoCierre = new ArqueoCajaTableModel();
        jtFondoCierre = new JTable(tbmFondoCierre);
        jspFondoCierre = new JScrollPane(jtFondoCierre);
        tbmDepositar = new ArqueoCajaTableModel();
        jtDepositar = new JTable(tbmDepositar);
        jspDepositar = new JScrollPane(jtDepositar);
    }

    private void constructLayout() {
        JPanel jpHoraInicio = new JPanel(new GridLayout(1, 2));
        jpHoraInicio.add(jcbHoraInicio);
        jpHoraInicio.add(jcbMinutoInicio);
        JPanel jpHoraFin = new JPanel(new GridLayout(1, 2));
        jpHoraFin.add(jcbHoraFin);
        jpHoraFin.add(jcbMinutoFin);

        Border borde = new EtchedBorder();
        JPanel jpFiltros1 = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[][grow][][grow]", // Column constraints
                "[][]"));    // Row constraints);
        jpFiltros1.setBorder(borde);
        JPanel jpFiltros2 = new JPanel(new MigLayout(
                "", // Layout Constraints
                "[][grow][][grow]", // Column constraints
                "[][]"));    // Row constraints);
        jpFiltros2.setBorder(borde);
        jpFiltros1.add(new JLabel("Fecha inicio:"));
        jpFiltros1.add(jddInicio, "wrap");
        jpFiltros1.add(new JLabel("Hora inicio:"));
        jpFiltros1.add(jpHoraInicio);
        jpFiltros2.add(new JLabel("Fecha final:"));
        jpFiltros2.add(jddFinal, "wrap");
        jpFiltros2.add(new JLabel("Hora final:"));
        jpFiltros2.add(jpHoraFin);
        JPanel jpFiltros = new JPanel(new GridLayout(1, 2));
        jpFiltros.add(jpFiltros1);
        jpFiltros.add(jpFiltros2);

        JPanel jpEgresos = new JPanel(new MigLayout());
        jpEgresos.setBorder(borde);
        jpEgresos.add(jlEgresoTotal);
        jpEgresos.add(jtfEgresoTotal, "wrap");
        jpEgresos.add(jlEgresoContado);
        jpEgresos.add(jtfEgresoContado, "wrap");
        jpEgresos.add(jlEgresoCredito);
        jpEgresos.add(jtfEgresoCredito, "wrap");
        JPanel jpIngresos = new JPanel(new MigLayout());
        jpIngresos.setBorder(borde);
        jpIngresos.add(jlIngresoTotal);
        jpIngresos.add(jtfIngresoTotal, "wrap");
        jpIngresos.add(jlIngresoContado);
        jpIngresos.add(jtfIngresoContado, "wrap");
        jpIngresos.add(jlIngresoCredito);
        jpIngresos.add(jtfIngresoCredito, "wrap");
        JPanel jpTotalCobrado = new JPanel(new MigLayout());
        jpTotalCobrado.setBorder(borde);
        jpTotalCobrado.add(jlTotalCobrado);
        jpTotalCobrado.add(jtfTotalCobrado, "wrap");
        jpTotalCobrado.add(jlTotalCobradoEfectivo);
        jpTotalCobrado.add(jtfTotalCobradoEfectivo, "wrap");
        jpTotalCobrado.add(jlTotalCobradoCheque);
        jpTotalCobrado.add(jtfTotalCobradoCheque, "wrap");
        JPanel jpTotalPagado = new JPanel(new MigLayout());
        jpTotalPagado.setBorder(borde);
        jpTotalPagado.add(jlTotalPagado);
        jpTotalPagado.add(jtfTotalPagado, "wrap");
        jpTotalPagado.add(jlTotalPagadoEfectivo);
        jpTotalPagado.add(jtfTotalPagadoEfectivo, "wrap");
        jpTotalPagado.add(jlTotalPagadoCheque);
        jpTotalPagado.add(jtfTotalPagadoCheque, "wrap");

        JPanel studentInfoPanel = new JPanel(new MigLayout());
        JPanel buttonsPanel = new JPanel();

        int space = 15;
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        //studentInfoPanel.setBorder(spaceBorder);
        studentInfoPanel.add(jlFondoApertura, "span 2");
        studentInfoPanel.add(jtfFondoApertura, "wrap");
        studentInfoPanel.add(jlFondoCierre, "span 2");
        studentInfoPanel.add(jtfFondoCierre, "wrap");
        studentInfoPanel.add(jlDepositar, "span 2");
        studentInfoPanel.add(jtfDepositar, "wrap");
        studentInfoPanel.add(jlEfectivoRendir, "span 2");
        studentInfoPanel.add(jftEfectivoRendir, "wrap");
        studentInfoPanel.add(jpEgresos, "spanx, split 2");
        studentInfoPanel.add(jpIngresos, "spanx, wrap");
        studentInfoPanel.add(jpTotalCobrado, "spanx, split 2");
        studentInfoPanel.add(jpTotalPagado, "spanx, wrap");

        // ////////// Buttons Panel ///////////////
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(jbDetalle);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        saveButton.setPreferredSize(btnSize);
        jbDetalle.setPreferredSize(btnSize);

        // Add sub panels to dialog
        JPanel jpSaldarCaja = new JPanel(new BorderLayout());
        jpSaldarCaja.add(jpFiltros, BorderLayout.NORTH);
        jpSaldarCaja.add(studentInfoPanel, BorderLayout.CENTER);
        jpSaldarCaja.add(buttonsPanel, BorderLayout.SOUTH);

        //ARQUEO CAJA
        jpArqueoCaja = new JTabbedPane();
        JPanel jpFondoApertura = new JPanel(new BorderLayout());
        JPanel jpCajaInicialSouth = new JPanel();
        jpCajaInicialSouth.add(jbFondoAnterior);
        jpFondoApertura.add(jspFondoApertura, BorderLayout.CENTER);
        jpFondoApertura.add(jpCajaInicialSouth, BorderLayout.SOUTH);
        jpArqueoCaja.addTab("Fondo apertura", jpFondoApertura);
        jpArqueoCaja.addTab("Fondo cierre", jspFondoCierre);
        jpArqueoCaja.addTab("Depositar", jspDepositar);
        setLayout(new GridLayout(1, 2));
        add(jpArqueoCaja);
        add(jpSaldarCaja);
    }

    private void addListeners() {
        this.jbDetalle.addActionListener(this);
        this.saveButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.jbFondoAnterior.addActionListener(this);
        this.tbmFondoApertura.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                sumarCajaChica();
            }
        });
        this.tbmFondoCierre.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                sumarCajaChicaAnterior();
            }
        });
        this.tbmDepositar.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                sumarDepositar();
            }
        });
        /*
        KEYLISTENER
         */
        this.jtFondoApertura.addKeyListener(this);
        this.jtFondoCierre.addKeyListener(this);
        this.jpArqueoCaja.addKeyListener(this);
    }

    private void initializeLogic() {
        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        Calendar fin = Calendar.getInstance();
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        Timestamp fechaInicio = new Timestamp(inicio.getTimeInMillis());
        Timestamp fechaFin = new Timestamp(fin.getTimeInMillis());
        int egresoContado = DB_Egreso.obtenerTotalEgreso(fechaInicio, fechaFin, 1);
        int egresoCretdito = DB_Egreso.obtenerTotalEgreso(fechaInicio, fechaFin, 2);
        int totalEgreso = egresoContado + egresoCretdito;
        int ingresoContado = DB_Ingreso.obtenerTotalIngreso(fechaInicio, fechaFin, 1, Estado.ACTIVO);
        int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(fechaInicio, fechaFin, 2, Estado.ACTIVO);
        int totalIngreso = ingresoContado + ingresoCretdito;
        int totalCobrado = DB_Cobro.obtenerTotalCobrado(fechaInicio, fechaFin, Estado.ACTIVO);
        int totalPagado = DB_Pago.obtenerTotalPagado(fechaInicio, fechaFin, Estado.ACTIVO);

        int totalEgrMasIng = totalEgreso + totalIngreso;
        int totalEgrMenosIng = totalEgreso - totalIngreso;
        this.jtfTotalEgrIng1.setValue(totalEgrMasIng);
        this.jtfTotalEgrIng2.setValue(totalEgrMenosIng);
        this.jtfEgresoTotal.setValue(totalEgreso);
        this.jtfEgresoContado.setValue(egresoContado);
        this.jtfEgresoCredito.setValue(egresoCretdito);
        this.jtfIngresoTotal.setValue(totalIngreso);
        this.jtfIngresoContado.setValue(ingresoContado);
        this.jtfIngresoCredito.setValue(ingresoCretdito);
        this.jtfTotalCobrado.setValue(totalCobrado);
        this.jtfTotalPagado.setValue(totalPagado);

        ArrayList<ArqueoCajaDetalle> arqueCajaDetaApertura = new ArrayList<>();
        ArrayList<Moneda> monedas = DB_Caja.obtenerMonedas();
        for (int i = 0; i < monedas.size(); i++) {
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(0);
            acd.setMoneda(monedas.get(i));
            acd.setIdTipo(1);//APERTURA
            arqueCajaDetaApertura.add(acd);
        }
        this.tbmFondoApertura.setArqueoCajaList(arqueCajaDetaApertura);
        this.tbmFondoApertura.updateTable();
        Utilities.c_packColumn.packColumns(jtFondoApertura, 1);

        //Fondo fijo
        ArrayList<ArqueoCajaDetalle> arqueoCajaDetaFondoCierre = new ArrayList<>();
        for (int i = 0; i < monedas.size(); i++) {
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(0);
            acd.setMoneda(monedas.get(i));
            acd.setIdTipo(2);//FIN
            arqueoCajaDetaFondoCierre.add(acd);
        }
        this.tbmFondoCierre.setArqueoCajaList(arqueoCajaDetaFondoCierre);
        this.tbmFondoCierre.updateTable();
        Utilities.c_packColumn.packColumns(jtFondoCierre, 1);
        //Deposito
        ArrayList<ArqueoCajaDetalle> arqueoDeposito = new ArrayList<>();
        for (int i = 0; i < monedas.size(); i++) {
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(0);
            acd.setMoneda(monedas.get(i));
            acd.setIdTipo(3);//DEPOSITO
            arqueoDeposito.add(acd);
        }
        this.tbmDepositar.setArqueoCajaList(arqueoDeposito);
        this.tbmDepositar.updateTable();
        Utilities.c_packColumn.packColumns(jtDepositar, 1);
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getId_funcionario();
        movimientosCaja = new MovimientosCaja();
        movimientosCaja.setMovimientoVentas((ArrayList<E_facturaCabecera>) DB_Ingreso.obtenerMovimientoVentasCabeceras(idFuncionario, -1, fechaInicio, fechaFin, -1));
        movimientosCaja.setMovimientoCompras((ArrayList<M_egreso_cabecera>) DB_Egreso.obtenerMovimientoComprasCabeceras(idFuncionario, -1, -1, fechaInicio, fechaFin));
        movimientosCaja.setMovimientoCobros((ArrayList<E_cuentaCorrienteCabecera>) DB_Cobro.obtenerMovimientoCobrosCabeceras(idFuncionario, -1, fechaInicio, fechaFin));
        movimientosCaja.setMovimientoPagos((ArrayList<E_reciboPagoCabecera>) DB_Pago.obtenerMovimientoPagosCabeceras(idFuncionario, -1, fechaInicio, fechaFin));
        actualizarMovimientos(movimientosCaja);

    }

    private void setWindows(JFrame parentFrame) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(parentFrame);
    }

    private void crearCaja() {
        /*
         * VALIDAR dineroTotal
         */
        Integer fondoInicial;
        try {
            String LongToString = String.valueOf(this.jtfFondoApertura.getValue());
            fondoInicial = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.jtfFondoApertura.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this, "Coloque un dinero total válido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR caja chico
         */
        Integer cajaChica;
        try {
            String LongToString = String.valueOf(this.jtfFondoCierre.getValue());
            cajaChica = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.jtfFondoCierre.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this, "Coloque una caja chica válida",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR TIEMPO INICIO
         */
        Date apertura = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String fechaInicio = sdf.format(jddInicio.getDate()) + " " + jcbHoraInicio.getSelectedItem() + ":" + jcbMinutoInicio.getSelectedItem() + ":00";
        try {
            apertura = sdfs.parse(fechaInicio);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese una fecha valida en el campo Tiempo apertura",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR TIEMPO FIN
         */
        Date cierre = null;
        String fechaFin = sdf.format(jddFinal.getDate()) + " " + jcbHoraFin.getSelectedItem() + ":" + jcbMinutoFin.getSelectedItem() + ":00";
        try {
            cierre = sdfs.parse(fechaFin);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese una fecha valida en el campo Tiempo cierre",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (apertura.after(cierre)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "La fecha de apertura debe ser menor a la de cierre.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getId_funcionario();
        Caja caja = new Caja();
        try {
            caja.setIdEmpleadoApertura(idFuncionario);
            caja.setIdEmpleadoCierre(idFuncionario);
            caja.setTiempoApertura(apertura);
            caja.setTiempoCierre(cierre);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Hubo un problema creando la caja",
                    "Verifique los datos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        ArrayList<ArqueoCajaDetalle> arqueoCajaApertura = arqueoCajaApertura();
        ArrayList<ArqueoCajaDetalle> arqueoCajaCierre = arqueoCajaCierre();
        ArrayList<ArqueoCajaDetalle> arqueoDeposito = arqueoDepositar();
        try {
            DB_Caja.insertarArqueoCaja(caja, arqueoCajaApertura, arqueoCajaCierre, arqueoDeposito, movimientosCaja);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un problema creando la caja", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mostrarMensaje("La caja se registró con éxito.");
        this.dispose();
    }

    private ArrayList<ArqueoCajaDetalle> arqueoCajaApertura() {
        ArrayList<ArqueoCajaDetalle> arqueoCajaInicio = new ArrayList<>();
        for (ArqueoCajaDetalle arqueoCajaDetalle : tbmFondoApertura.arqueoCajaDetalleList) {
            if (arqueoCajaDetalle.getCantidad() != 0) {
                arqueoCajaInicio.add(arqueoCajaDetalle);
            }
        }
        return arqueoCajaInicio;
    }

    private ArrayList<ArqueoCajaDetalle> arqueoCajaCierre() {
        ArrayList<ArqueoCajaDetalle> arqueoCajaFin = new ArrayList<>();
        for (ArqueoCajaDetalle arqueoCajaDetalle : tbmFondoCierre.arqueoCajaDetalleList) {
            if (arqueoCajaDetalle.getCantidad() != 0) {
                arqueoCajaFin.add(arqueoCajaDetalle);
            }
        }
        return arqueoCajaFin;
    }

    private ArrayList<ArqueoCajaDetalle> arqueoDepositar() {
        ArrayList<ArqueoCajaDetalle> arqueoDeposito = new ArrayList<>();
        for (ArqueoCajaDetalle arqueoCajaDetalle : tbmDepositar.arqueoCajaDetalleList) {
            if (arqueoCajaDetalle.getCantidad() != 0) {
                arqueoDeposito.add(arqueoCajaDetalle);
            }
        }
        return arqueoDeposito;
    }

    private void mostrarMensaje(String message) {
        Toaster popUp = new Toaster();
        popUp.showToaster(message);
    }

    private void cerrar() {
        this.dispose();
    }

    private void consultarUltimoFondo() {
        ArrayList<ArqueoCajaDetalle> acda = DB_Caja.consultarUltimoArqueoCaja();
        for (int i = 0; i < acda.size(); i++) {
            ArqueoCajaDetalle get = acda.get(i);
            get.setIdTipo(1);//APERTURA
        }
        if (!acda.isEmpty()) {
            this.tbmFondoApertura.setArqueoCajaList(acda);
            this.tbmFondoApertura.updateTable();
        }
    }

    private void sumarCajaChica() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFondoApertura.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfFondoApertura.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarCajaChicaAnterior() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFondoCierre.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfFondoCierre.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarDepositar() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmDepositar.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfDepositar.setValue(total);
    }

    private void calcularDiferenciaCaja() {
        int fondoInicial = 0;
        int fondoFinal = 0;
        if (null != this.jtfFondoApertura.getValue()) {
            try {
                fondoInicial = Integer.valueOf(String.valueOf(this.jtfFondoApertura.getValue()));
            } catch (Exception e) {
                fondoInicial = 0;
            }
        }
        if (null != this.jtfFondoCierre.getValue()) {
            try {
                fondoFinal = Integer.valueOf(String.valueOf(this.jtfFondoCierre.getValue()));
            } catch (Exception e) {
                fondoFinal = 0;
            }
        }
        //this.jtfDifCaja.setValue(fondoInicial - fondoFinal);
    }

    private void invocarDetalle() {
        int horaFin = jcbHoraFin.getSelectedIndex();
        int minutoFin = jcbMinutoFin.getSelectedIndex();
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(jddInicio.getDate());
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(jddFinal.getDate());
        calendarFinal.set(Calendar.HOUR_OF_DAY, horaFin);
        calendarFinal.set(Calendar.MINUTE, minutoFin);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        CajaDetalle cd = new CajaDetalle(this);
        cd.setRangoTiempo(calendarInicio.getTime(), calendarFinal.getTime());
        cd.setInterface(this);
        cd.mostrarVista();
    }

    private void actualizarMovimientos(MovimientosCaja movimientosCaja) {
        this.movimientosCaja = movimientosCaja;
        int totalVenta = 0, totalVentaContado = 0, totalVentaCredito = 0;
        int totalPago = 0, totalPagoEfectivo = 0, totalPagoCheque = 0;
        int totalCompra = 0, totalCompraContado = 0, totalCompraCredito = 0;
        int totalCobro = 0, totalCobroEfectivo = 0, totalCobroCheque = 0;
        /*
        MOVIMIENTO DE COBROS
         */
        for (E_cuentaCorrienteCabecera cobro : movimientosCaja.getMovimientoCobros()) {
            totalCobro = totalCobro + cobro.getDebito();
            for (E_cuentaCorrienteDetalle e_cuentaCorrienteDetalle : DB_Cobro.obtenerCobroDetalle(cobro.getId())) {
                switch (e_cuentaCorrienteDetalle.calcularFormaPago().getId()) {
                    case E_formaPago.EFECTIVO: {
                        totalCobroEfectivo = (int) (totalCobroEfectivo + e_cuentaCorrienteDetalle.getMonto());
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        totalCobroCheque = (int) (totalCobroCheque + e_cuentaCorrienteDetalle.getMonto());
                        break;
                    }
                }

            }
        }
        /*
        MOVIMIENTO DE PAGOS        
         */
        for (E_reciboPagoCabecera pago : movimientosCaja.getMovimientoPagos()) {
            totalPago = totalPago + pago.getMonto();
            for (E_reciboPagoDetalle reciboPagoDetalle : DB_Pago.obtenerPagoDetalle(pago.getId())) {
                switch (reciboPagoDetalle.calcularFormaPago().getId()) {
                    case E_formaPago.EFECTIVO: {
                        totalPagoEfectivo = (int) (totalPagoEfectivo + reciboPagoDetalle.getMonto());
                        break;
                    }
                    case E_formaPago.CHEQUE: {
                        totalPagoCheque = (int) (totalPagoCheque + reciboPagoDetalle.getMonto());
                        break;
                    }
                }

            }
        }
        /*
        MOVIMIENTO DE COMPRAS
         */
        for (M_egreso_cabecera compra : movimientosCaja.getMovimientoCompras()) {
            totalCompra = totalCompra + compra.getTotal();
            switch (compra.getId_condVenta()) {
                case E_tipoOperacion.CONTADO: {
                    totalCompraContado = totalCompraContado + compra.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalCompraCredito = totalCompraCredito + compra.getTotal();
                    break;
                }
            }
        }
        /*
        MOVIMIENTO DE VENTAS
         */
        for (E_facturaCabecera venta : movimientosCaja.getMovimientoVentas()) {
            totalVenta = totalVenta + venta.getTotal();
            switch (venta.getTipoOperacion().getId()) {
                case E_tipoOperacion.CONTADO: {
                    totalVentaContado = totalVentaContado + venta.getTotal();
                    break;
                }
                case E_tipoOperacion.CREDITO_30: {
                    totalVentaCredito = totalVentaCredito + venta.getTotal();
                    break;
                }
            }
        }
        //COBROS
        this.jtfTotalCobrado.setValue(totalCobro);
        this.jtfTotalCobradoEfectivo.setValue(totalCobroEfectivo);
        this.jtfTotalCobradoCheque.setValue(totalCobroCheque);
        //PAGOS
        this.jtfTotalPagado.setValue(totalPago);
        this.jtfTotalPagadoCheque.setValue(totalPagoCheque);
        this.jtfTotalPagadoEfectivo.setValue(totalPagoEfectivo);
        //VENTAS
        this.jtfIngresoTotal.setValue(totalVenta);
        this.jtfIngresoContado.setValue(totalVentaContado);
        this.jtfIngresoCredito.setValue(totalVentaCredito);
        //COMPRAS
        this.jtfEgresoTotal.setValue(totalCompra);
        this.jtfEgresoContado.setValue(totalCompraContado);
        this.jtfEgresoCredito.setValue(totalCompraCredito);
        Integer aDepositar = totalCobroEfectivo + totalVentaContado - totalCompraContado - totalPagoEfectivo;
        this.jftEfectivoRendir.setValue(aDepositar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.cancelButton) {
            this.dispose();
        } else if (src.equals(this.saveButton)) {
            crearCaja();
        } else if (src.equals(this.jbFondoAnterior)) {
            consultarUltimoFondo();
        } else if (src.equals(this.jbDetalle)) {
            invocarDetalle();
        }
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
    public void recibirMovimientos(MovimientosCaja movimientosCaja, Date tiempoInicio, Date tiempoFin) {
        actualizarMovimientos(movimientosCaja);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(tiempoFin);
        jddInicio.setDate(tiempoInicio);
        jddFinal.setDate(calendarFinal.getTime());
        jcbHoraFin.setSelectedIndex(calendarFinal.get(Calendar.HOUR_OF_DAY));
        jcbMinutoFin.setSelectedIndex(calendarFinal.get(Calendar.MINUTE));
    }

}
