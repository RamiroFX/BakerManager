/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import Interface.CommonFormat;
import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Utilities.Impresora;
import bakermanager.C_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class VerDetalleCaja extends JDialog implements ActionListener, KeyListener {

    public JDateChooser jddInicio, jddFinal;
    public JComboBox jcbHoraInicio, jcbMinutoInicio, jcbHoraFin, jcbMinutoFin;
    private JButton printButton, cancelButton;
    private JLabel jlFondoInicial, jlCajaChica, jlEgresoTotal, jlDepositar,
            jlDifCaja, jlEgresoCredito, jlEgresoContado,
            jlIngresoTotal, jlIngresoCredito, jlIngresoContado, jlTotalEgrIng1,
            jlTotalEgrIng2;
    private JFormattedTextField jtfFondoInicial, jtfCajaChica, jtfDifCaja,
            jtfDepositar, jtfEgresoTotal, jtfEgresoCredito, jtfEgresoContado,
            jtfIngresoTotal, jtfIngresoCredito, jtfIngresoContado,
            jtfTotalEgrIng1, jtfTotalEgrIng2;
    //ARQUEO CAJA VARIABLES
    private JTable jtInicio, jtFin, jtDepositar;
    private JScrollPane jspInicio, jspFin, jspDepositar;
    private ArqueoCajaTableModel tbmInicio, tbmFin, tbmDepositar;
    //LOGIC VARIABLES
    private ArrayList<ArqueoCajaDetalle> acdInicio;
    private ArrayList<ArqueoCajaDetalle> acdFin;
    private ArrayList<ArqueoCajaDetalle> acdDepositar;
    private Caja caja;

    public VerDetalleCaja(C_inicio inicio, int idCaja) {
        super(inicio.vista, "Ver caja", true);
        initializeVariables();
        constructLayout();
        addListeners();
        initializeLogic(idCaja);
        setWindows(inicio.vista);
    }

    private void initializeVariables() {
        int prefCols = 20;
        Date today = Calendar.getInstance().getTime();
        jddInicio = new JDateChooser(today);
        jddInicio.setPreferredSize(new Dimension(150, 10));
        jddInicio.setEnabled(false);
        jddFinal = new JDateChooser(today);
        jddFinal.setPreferredSize(new Dimension(150, 10));
        jddFinal.setEnabled(false);
        this.printButton = new JButton("Imprimir");
        this.cancelButton = new JButton("Cancelar");
        this.jlFondoInicial = new JLabel("Fondo inicial");
        this.jlFondoInicial.setFont(CommonFormat.fuenteTitulo);
        this.jlCajaChica = new JLabel("Caja chica");
        this.jlCajaChica.setFont(CommonFormat.fuenteTitulo);
        this.jlDifCaja = new JLabel("Dif. de Caja");
        this.jlDifCaja.setFont(CommonFormat.fuenteSubTitulo);
        this.jlDepositar = new JLabel("A depositar");
        this.jlDepositar.setFont(CommonFormat.fuenteTitulo);
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
        this.jtfFondoInicial = new JFormattedTextField();
        this.jtfFondoInicial.setColumns(prefCols);
        this.jtfFondoInicial.addKeyListener(this);
        this.jtfCajaChica = new JFormattedTextField();
        this.jtfCajaChica.setColumns(prefCols);
        this.jtfCajaChica.addKeyListener(this);
        this.jtfDifCaja = new JFormattedTextField();
        this.jtfDifCaja.setColumns(prefCols);
        this.jtfDifCaja.addKeyListener(this);
        this.jtfDepositar = new JFormattedTextField();
        this.jtfDepositar.setColumns(prefCols);
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

        this.jtfFondoInicial.setEditable(false);
        this.jtfCajaChica.setEditable(false);
        this.jtfDifCaja.setEditable(false);
        this.jtfDepositar.setEditable(false);
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
        jcbHoraInicio.setEnabled(false);
        jcbMinutoInicio.setEnabled(false);
        jcbHoraFin.setEnabled(false);
        jcbMinutoFin.setEnabled(false);
        for (int i = 0; i < 10; i++) {
            jcbHoraInicio.addItem("0" + i);
            jcbHoraFin.addItem("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            jcbHoraInicio.addItem("" + i);
            jcbHoraFin.addItem("" + i);
        }
        for (int i = 0; i < 10; i++) {
            jcbMinutoFin.addItem("0" + i);
            jcbMinutoInicio.addItem("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            jcbMinutoFin.addItem("" + i);
            jcbMinutoInicio.addItem("" + i);
        }

        //ARQUEO CAJA 
        tbmInicio = new ArqueoCajaTableModel();
        jtInicio = new JTable(tbmInicio);
        jtInicio.setEnabled(false);
        jspInicio = new JScrollPane(jtInicio);
        tbmFin = new ArqueoCajaTableModel();
        jtFin = new JTable(tbmFin);
        jtFin.setEnabled(false);
        jspFin = new JScrollPane(jtFin);
        tbmDepositar = new ArqueoCajaTableModel();
        jtDepositar = new JTable(tbmDepositar);
        jtDepositar.setEnabled(false);
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

        JPanel studentInfoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        int space = 15;
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        studentInfoPanel.setBorder(spaceBorder);

        studentInfoPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);

        // ///// First row /////////////////////////////
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlFondoInicial, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfFondoInicial, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlCajaChica, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfCajaChica, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlDifCaja, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfDifCaja, gc);
        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlDepositar, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfDepositar, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlEgresoTotal, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfEgresoTotal, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlEgresoContado, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfEgresoContado, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlEgresoCredito, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfEgresoCredito, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlIngresoTotal, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfIngresoTotal, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlIngresoContado, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfIngresoContado, gc);

        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlIngresoCredito, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfIngresoCredito, gc);
        // ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlTotalEgrIng1, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfTotalEgrIng1, gc);
// ////// Next row ////////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        studentInfoPanel.add(jlTotalEgrIng2, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfTotalEgrIng2, gc);

        // ////////// Buttons Panel ///////////////
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(printButton);
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        printButton.setPreferredSize(btnSize);

        // Add sub panels to dialog
        JPanel jpSaldarCaja = new JPanel(new BorderLayout());
        jpSaldarCaja.add(jpFiltros, BorderLayout.NORTH);
        jpSaldarCaja.add(studentInfoPanel, BorderLayout.CENTER);
        jpSaldarCaja.add(buttonsPanel, BorderLayout.SOUTH);

        //ARQUEO CAJA
        JTabbedPane jpArqueoCaja = new JTabbedPane();
        jpArqueoCaja.addTab("Caja chica", jspInicio);
        jpArqueoCaja.addTab("Fondo inicial", jspFin);
        jpArqueoCaja.addTab("Depositado", jspDepositar);
        setLayout(new GridLayout(1, 2));
        add(jpArqueoCaja);
        add(jpSaldarCaja);
    }

    private void addListeners() {
        this.printButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.tbmInicio.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                sumarCajaChica();
            }
        });
        this.tbmFin.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                sumarCajaChicaAnterior();
            }
        });
        this.tbmDepositar.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                arqueoDepositar();
            }
        });
        /*
        KEYLISTENER
         */
        this.jtInicio.addKeyListener(this);
    }

    private void setWindows(JFrame parentFrame) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(parentFrame);
    }

    private void initializeLogic(int idCaja) {
        acdInicio = DB_Caja.obtenerArqueoCaja(idCaja, 1);
        acdFin = DB_Caja.obtenerArqueoCaja(idCaja, 2);
        acdDepositar = DB_Caja.obtenerArqueoCaja(idCaja, 3);
        caja = DB_Caja.obtenerCaja(idCaja);
        int egresoContado = caja.getEgresoContado();
        int egresoCretdito = caja.getEgresoCredito();
        int totalEgreso = egresoContado + egresoCretdito;
        int ingresoContado = caja.getIngresoContado();
        int ingresoCretdito = caja.getIngresoCredito();
        int totalIngreso = ingresoContado + ingresoCretdito;

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

        this.tbmInicio.setArqueoCajaList(acdInicio);
        this.tbmInicio.updateTable();
        Utilities.c_packColumn.packColumns(jtInicio, 1);

        this.tbmFin.setArqueoCajaList(acdFin);
        this.tbmFin.updateTable();
        Utilities.c_packColumn.packColumns(jtFin, 1);

        this.tbmDepositar.setArqueoCajaList(acdDepositar);
        this.tbmDepositar.updateTable();
        Utilities.c_packColumn.packColumns(jtDepositar, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(caja.getTiempoCierre());
        Date currentTime = calendar.getTime();
        String horaFin = sdf.format(currentTime).substring(0, 2);
        int horasFin = Integer.valueOf(horaFin);
        if (horasFin >= 0 && horasFin < 10) {
            this.jcbHoraFin.setSelectedItem("" + horasFin);
        } else {
            this.jcbHoraFin.setSelectedItem("" + horasFin);
        }
        int minutoFin = calendar.get(Calendar.MINUTE);
        if (minutoFin < 10) {
            jcbMinutoFin.setSelectedItem("0" + minutoFin);
        } else {
            jcbMinutoFin.setSelectedItem("" + minutoFin);
        }
        calendar.setTime(caja.getTiempoApertura());
        currentTime = calendar.getTime();
        String horaInicio = sdf.format(currentTime).substring(0, 2);
        int horasInicio = Integer.valueOf(horaInicio);
        if (horasInicio >= 0 && horasFin < 10) {
            this.jcbHoraInicio.setSelectedItem("" + horasInicio);
        } else {
            this.jcbHoraInicio.setSelectedItem("" + horasInicio);
        }
        int minutoInicio = calendar.get(Calendar.MINUTE);
        if (minutoInicio < 10) {
            jcbMinutoInicio.setSelectedItem("0" + minutoInicio);
        } else {
            jcbMinutoInicio.setSelectedItem("" + minutoInicio);
        }
    }

    private void imprimirCaja() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir el pedido?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    Impresora.imprimirCaja(caja);
                }
            }
        });
    }

    private void cerrar() {
        this.dispose();
    }

    private void sumarCajaChica() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmInicio.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfCajaChica.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarCajaChicaAnterior() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFin.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfFondoInicial.setValue(total);
        calcularDiferenciaCaja();
    }

    private void calcularDiferenciaCaja() {
        int fondoInicial = 0;
        int fondoFinal = 0;
        if (null != this.jtfFondoInicial.getValue()) {
            try {
                fondoInicial = Integer.valueOf(String.valueOf(this.jtfFondoInicial.getValue()));
            } catch (Exception e) {
                fondoInicial = 0;
            }
        }
        if (null != this.jtfCajaChica.getValue()) {
            try {
                fondoFinal = Integer.valueOf(String.valueOf(this.jtfCajaChica.getValue()));
            } catch (Exception e) {
                fondoFinal = 0;
            }
        }
        this.jtfDifCaja.setValue(fondoInicial - fondoFinal);
    }

    private void arqueoDepositar() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmDepositar.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.jtfDepositar.setValue(total);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.cancelButton) {
            this.dispose();
        } else if (src.equals(this.printButton)) {
            imprimirCaja();
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
}
