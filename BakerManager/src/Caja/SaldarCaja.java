/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import Interface.CommonFormat;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.ArqueoCajaDetalle;
import Entities.Caja;
import Entities.Moneda;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
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
public class SaldarCaja extends JDialog implements ActionListener, KeyListener {

    public JDateChooser jddInicio, jddFinal;
    public JComboBox jcbHoraInicio, jcbMinutoInicio, jcbHoraFin, jcbMinutoFin;
    private JButton saveButton, cancelButton, jbFondoAnterior;
    private JLabel jlFondoInicial, jlCajaChica, jlEgresoTotal, jlDifCaja, jlEgresoCredito, jlEgresoContado,
            jlIngresoTotal, jlIngresoCredito, jlIngresoContado, jlTotalEgrIng1, jlTotalEgrIng2;
    private JFormattedTextField jtfFondoInicial, jtfCajaChica, jtfDifCaja, jtfEgresoTotal,
            jtfEgresoCredito, jtfEgresoContado, jtfIngresoTotal,
            jtfIngresoCredito, jtfIngresoContado, jtfTotalEgrIng1, jtfTotalEgrIng2;
    //ARQUEO CAJA VARIABLES
    private JTabbedPane jpArqueoCaja;
    private JTable jtInicio, jtFin;
    private JScrollPane jspInicio, jspFin;
    private ArqueoCajaTableModel tbmInicio, tbmFin;

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
        this.saveButton = new JButton("Guardar");
        this.cancelButton = new JButton("Cancelar");
        this.jbFondoAnterior = new JButton("Caja chica anterior");
        this.jlFondoInicial = new JLabel("Fondo inicial");
        this.jlFondoInicial.setFont(CommonFormat.fuenteTitulo);
        this.jlCajaChica = new JLabel("Caja chica");
        this.jlCajaChica.setFont(CommonFormat.fuenteTitulo);
        this.jlDifCaja = new JLabel("Dif. de Caja");
        this.jlDifCaja.setFont(CommonFormat.fuenteSubTitulo);
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
            jcbHoraInicio.addItem(i);
            jcbHoraFin.addItem(i);
        }
        for (int i = 0; i < 10; i++) {
            jcbMinutoInicio.addItem("0" + i);
            jcbMinutoFin.addItem("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            jcbMinutoInicio.addItem(i);
            jcbMinutoFin.addItem(i);
        }
        int horaFin = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutoFin = Calendar.getInstance().get(Calendar.MINUTE);
        jcbHoraFin.setSelectedItem(horaFin);
        jcbMinutoFin.setSelectedItem(minutoFin);

        //ARQUEO CAJA 
        tbmInicio = new ArqueoCajaTableModel();
        jtInicio = new JTable(tbmInicio);
        jspInicio = new JScrollPane(jtInicio);
        tbmFin = new ArqueoCajaTableModel();
        jtFin = new JTable(tbmFin);
        jspFin = new JScrollPane(jtFin);
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
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        saveButton.setPreferredSize(btnSize);

        // Add sub panels to dialog
        JPanel jpSaldarCaja = new JPanel(new BorderLayout());
        jpSaldarCaja.add(jpFiltros, BorderLayout.NORTH);
        jpSaldarCaja.add(studentInfoPanel, BorderLayout.CENTER);
        jpSaldarCaja.add(buttonsPanel, BorderLayout.SOUTH);

        //ARQUEO CAJA
        jpArqueoCaja = new JTabbedPane();
        JPanel jpCajaInicial = new JPanel(new BorderLayout());
        JPanel jpCajaInicialSouth = new JPanel();
        jpCajaInicialSouth.add(jbFondoAnterior);
        jpCajaInicial.add(jspFin, BorderLayout.CENTER);
        jpCajaInicial.add(jpCajaInicialSouth, BorderLayout.SOUTH);
        jpArqueoCaja.addTab("Caja chica", jspInicio);
        jpArqueoCaja.addTab("Fondo inicial", jpCajaInicial);
        setLayout(new GridLayout(1, 2));
        add(jpArqueoCaja);
        add(jpSaldarCaja);
    }

    private void addListeners() {
        this.saveButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.jbFondoAnterior.addActionListener(this);
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
        /*
        KEYLISTENER
         */
        this.jtInicio.addKeyListener(this);
        this.jtFin.addKeyListener(this);
        this.jpArqueoCaja.addKeyListener(this);
    }

    private void initializeLogic() {
        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        Calendar fin = Calendar.getInstance();
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        Timestamp ini = new Timestamp(inicio.getTimeInMillis());
        Timestamp fi = new Timestamp(fin.getTimeInMillis());
        int egresoContado = DB_Egreso.obtenerTotalEgreso(ini, fi, 1);
        int egresoCretdito = DB_Egreso.obtenerTotalEgreso(ini, fi, 2);
        int totalEgreso = egresoContado + egresoCretdito;
        int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1);
        int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2);
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

        ArrayList<ArqueoCajaDetalle> arqueCajaDetaInicio = new ArrayList<>();
        ArrayList<Moneda> monedas = DB_Caja.obtenerMonedas();
        for (int i = 0; i < monedas.size(); i++) {
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(0);
            acd.setMoneda(monedas.get(i));
            acd.setIdTipo(1);//INICIO
            arqueCajaDetaInicio.add(acd);
        }
        this.tbmInicio.setArqueoCajaList(arqueCajaDetaInicio);
        this.tbmInicio.updateTable();
        Utilities.c_packColumn.packColumns(jtInicio, 1);

        //Fondo fijo
        ArrayList<ArqueoCajaDetalle> arqueCajaDetaFondoAnterior = new ArrayList<>();
        for (int i = 0; i < monedas.size(); i++) {
            ArqueoCajaDetalle acd = new ArqueoCajaDetalle();
            acd.setCantidad(0);
            acd.setMoneda(monedas.get(i));
            acd.setIdTipo(2);//INICIO
            arqueCajaDetaFondoAnterior.add(acd);
        }
        this.tbmFin.setArqueoCajaList(arqueCajaDetaFondoAnterior);
        this.tbmFin.updateTable();
        Utilities.c_packColumn.packColumns(jtFin, 1);

    }

    private void setWindows(JFrame parentFrame) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(parentFrame);
    }

    private void crearCaja() {
        /*
         * VALIDAR dineroTotal
         */
        Integer fondoInicial;
        try {
            String LongToString = String.valueOf(this.jtfFondoInicial.getValue());
            fondoInicial = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.jtfFondoInicial.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this, "Coloque un dinero total válido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR dineroTotal
         */
        Integer cajaChica;
        try {
            String LongToString = String.valueOf(this.jtfCajaChica.getValue());
            cajaChica = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.jtfCajaChica.setBackground(Color.red);
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
         * VALIDAR TIEMPO INICIO
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
        int egresoContado = (int) jtfEgresoContado.getValue();
        int egresoCredito = (int) jtfEgresoCredito.getValue();
        int ingresoContado = (int) jtfIngresoContado.getValue();
        int ingresoCredito = (int) jtfIngresoCredito.getValue();
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getId_funcionario();
        Caja caja = new Caja();
        try {
            caja.setIdEmpleadoApertura(idFuncionario);
            caja.setIdEmpleadoCierre(idFuncionario);
            caja.setMontoInicial(fondoInicial);
            caja.setMontoFinal(cajaChica);
            caja.setIngresoContado(ingresoContado);
            caja.setIngresoCredito(ingresoCredito);
            caja.setEgresoContado(egresoContado);
            caja.setEgresoCredito(egresoCredito);
            caja.setTiempoApertura(apertura);
            caja.setTiempoCierre(cierre);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Hubo un problema creando la caja",
                    "Verifique los datos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        DB_Caja.insertarArqueoCaja(caja, arqueoCajaInicio(), arqueoCajaFin());
        this.dispose();
    }

    private ArrayList<ArqueoCajaDetalle> arqueoCajaInicio() {
        ArrayList<ArqueoCajaDetalle> arqueoCajaInicio = new ArrayList<>();
        for (ArqueoCajaDetalle arqueoCajaDetalle : tbmInicio.arqueoCajaDetalleList) {
            if (arqueoCajaDetalle.getCantidad() != 0) {
                arqueoCajaInicio.add(arqueoCajaDetalle);
            }
        }
        return arqueoCajaInicio;
    }

    private ArrayList<ArqueoCajaDetalle> arqueoCajaFin() {
        ArrayList<ArqueoCajaDetalle> arqueoCajaFin = new ArrayList<>();
        for (ArqueoCajaDetalle arqueoCajaDetalle : tbmFin.arqueoCajaDetalleList) {
            if (arqueoCajaDetalle.getCantidad() != 0) {
                arqueoCajaFin.add(arqueoCajaDetalle);
            }
            arqueoCajaFin.add(arqueoCajaDetalle);
        }
        return arqueoCajaFin;
    }

    private void cerrar() {
        this.dispose();
    }

    private void consultarUltimoFondo() {
        ArrayList<ArqueoCajaDetalle> acda = DB_Caja.consultarUltimoArqueoCaja();
        if (!acda.isEmpty()) {
            this.tbmFin.setArqueoCajaList(acda);
            this.tbmFin.updateTable();
        }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.cancelButton) {
            this.dispose();
        } else if (src.equals(this.saveButton)) {
            crearCaja();
        } else if (src.equals(this.jbFondoAnterior)) {
            consultarUltimoFondo();
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
