/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import com.toedter.calendar.JDateChooser;
import Interface.CommonFormat;
import bakermanager.C_inicio;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_SaldarCaja extends JDialog {

    public JDateChooser jddInicio, jddFinal;
    public JComboBox jcbHoraInicio, jcbMinutoInicio, jcbHoraFin, jcbMinutoFin;
    public JButton saveButton, cancelButton, jbFondoAnterior, jbDetalle, printButton,
            jbExportar;
    public JLabel jlFondoApertura, jlFondoCierre, jlEgresoTotal, //jlDifCaja,
            jlDepositar, jlEfectivoRendir, jlEgresoCredito, jlEgresoContado, jlIngresoTotal,
            jlIngresoCredito, jlIngresoContado, jlTotalEgrIng1, jlTotalEgrIng2,
            jlTotalCobrado, jlTotalCobradoEfectivo, jlTotalCobradoCheque,
            jlTotalPagado, jlTotalPagadoEfectivo, jlTotalPagadoCheque;
    public JFormattedTextField jtfFondoApertura, jtfFondoCierre, jtfDepositar, jtfEgresoTotal, //jtfDifCaja,
            jtfEgresoCredito, jtfEgresoContado, jtfIngresoTotal, jftEfectivoRendir,
            jtfIngresoCredito, jtfIngresoContado, jtfTotalEgrIng1, jtfTotalEgrIng2,
            jtfTotalCobrado, jtfTotalCobradoEfectivo, jtfTotalCobradoCheque,
            jtfTotalPagado, jtfTotalPagadoEfectivo, jtfTotalPagadoCheque;
    //ARQUEO CAJA VARIABLES
    public JTabbedPane jpArqueoCaja;
    public JTable jtFondoApertura, jtFondoCierre, jtDepositar;
    public JScrollPane jspFondoApertura, jspFondoCierre, jspDepositar;

    public V_SaldarCaja(C_inicio inicio) {
        super(inicio.vista, "Saldar caja", true);
        initializeVariables();
        constructLayout();
        setWindows(inicio.vista);
    }

    private void setWindows(JFrame parentFrame) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(parentFrame);
    }

    private void initializeVariables() {
        int prefCols = 20;
        Date today = Calendar.getInstance().getTime();
        jddInicio = new JDateChooser(today);
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddFinal = new JDateChooser(today);
        jddFinal.setPreferredSize(new Dimension(150, 10));
        this.jbExportar = new JButton("Exportar");
        this.jbExportar.setVisible(false);
        this.printButton = new JButton("Imprimir");
        this.printButton.setVisible(false);
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
        this.jtfFondoCierre = new JFormattedTextField();
        this.jtfFondoCierre.setColumns(prefCols);
        //this.jtfDifCaja = new JFormattedTextField();
        //this.jtfDifCaja.setColumns(prefCols);
        this.jtfDepositar = new JFormattedTextField();
        this.jtfDepositar.setColumns(prefCols);
        this.jftEfectivoRendir = new JFormattedTextField();
        this.jftEfectivoRendir.setColumns(prefCols);
        this.jtfEgresoTotal = new JFormattedTextField();
        this.jtfEgresoTotal.setColumns(prefCols);
        this.jtfEgresoCredito = new JFormattedTextField();
        this.jtfEgresoCredito.setColumns(prefCols);
        this.jtfEgresoContado = new JFormattedTextField();
        this.jtfEgresoContado.setColumns(prefCols);
        this.jtfIngresoTotal = new JFormattedTextField();
        this.jtfIngresoTotal.setColumns(prefCols);
        this.jtfIngresoContado = new JFormattedTextField();
        this.jtfIngresoContado.setColumns(prefCols);
        this.jtfIngresoCredito = new JFormattedTextField();
        this.jtfIngresoCredito.setColumns(prefCols);
        this.jtfTotalEgrIng1 = new JFormattedTextField();
        this.jtfTotalEgrIng1.setColumns(prefCols);
        this.jtfTotalEgrIng2 = new JFormattedTextField();
        this.jtfTotalEgrIng2.setColumns(prefCols);
        /*
        TOTAL COBRADO VARIABLES
         */
        this.jlTotalCobrado = new JLabel("Total cobrado");
        this.jlTotalCobrado.setFont(CommonFormat.fuenteTitulo);
        this.jlTotalCobradoEfectivo = new JLabel("Cobrado efectivo");
        this.jlTotalCobradoCheque = new JLabel("Cobrado cheque");
        this.jtfTotalCobrado = new JFormattedTextField();
        this.jtfTotalCobrado.setColumns(prefCols);
        this.jtfTotalCobradoEfectivo = new JFormattedTextField();
        this.jtfTotalCobradoEfectivo.setColumns(prefCols);
        this.jtfTotalCobradoCheque = new JFormattedTextField();
        this.jtfTotalCobradoCheque.setColumns(prefCols);

        /*
        TOTAL PAGADO VARIABLES
         */
        this.jlTotalPagado = new JLabel("Total pagado");
        this.jlTotalPagado.setFont(CommonFormat.fuenteTitulo);
        this.jlTotalPagadoEfectivo = new JLabel("Pagado efectivo");
        this.jlTotalPagadoCheque = new JLabel("Pagado cheque");
        this.jtfTotalPagado = new JFormattedTextField();
        this.jtfTotalPagado.setColumns(prefCols);
        this.jtfTotalPagadoEfectivo = new JFormattedTextField();
        this.jtfTotalPagadoEfectivo.setColumns(prefCols);
        this.jtfTotalPagadoCheque = new JFormattedTextField();
        this.jtfTotalPagadoCheque.setColumns(prefCols);

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
        this.jtfTotalPagado.setEditable(false);
        this.jtfTotalPagadoCheque.setEditable(false);
        this.jtfTotalPagadoEfectivo.setEditable(false);
        this.jtfTotalCobrado.setEditable(false);
        this.jtfTotalCobradoCheque.setEditable(false);
        this.jtfTotalCobradoEfectivo.setEditable(false);
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
        jtFondoApertura = new JTable();
        jspFondoApertura = new JScrollPane(jtFondoApertura);
        jtFondoCierre = new JTable();
        jspFondoCierre = new JScrollPane(jtFondoCierre);
        jtDepositar = new JTable();
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
        buttonsPanel.add(jbExportar);
        buttonsPanel.add(printButton);
        buttonsPanel.add(jbDetalle);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        saveButton.setPreferredSize(btnSize);
        jbDetalle.setPreferredSize(btnSize);
        jbExportar.setPreferredSize(btnSize);
        printButton.setPreferredSize(btnSize);

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
}
