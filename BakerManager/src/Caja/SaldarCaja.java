/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Interface.CommonFormat;
import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.Caja;
import bakermanager.C_inicio;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class SaldarCaja extends JDialog implements ActionListener, KeyListener {

    public JDateChooser jddInicio, jddFinal;
    public JComboBox jcbHoraInicio, jcbMinutoInicio;
    public JComboBox jcbHoraFin, jcbMinutoFin;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel jlDineroTotal;
    private JLabel jlCajaChica;
    private JLabel jlEgresoTotal, jlEgresoCredito, jlEgresoContado;
    private JLabel jlIngresoTotal, jlIngresoCredito, jlIngresoContado;
    private JLabel jlTotalEgrIng;
    private JFormattedTextField jtfDineroTotal;
    private JFormattedTextField jtfCajaChica;
    private JFormattedTextField jtfEgresoTotal, jtfEgresoCredito, jtfEgresoContado;
    private JFormattedTextField jtfIngresoTotal, jtfIngresoCredito, jtfIngresoContado;
    private JFormattedTextField jtfTotalEgrIng;

    public SaldarCaja(C_inicio inicio) {
        super(inicio.vista, "Saldar caja", true);
        initializeVariables();
        constructLayout();
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
        this.saveButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.jlDineroTotal = new JLabel("Dinero total");
        this.jlDineroTotal.setFont(CommonFormat.fuenteTitulo);
        this.jlCajaChica = new JLabel("Caja chica");
        this.jlCajaChica.setFont(CommonFormat.fuenteTitulo);
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
        this.jlTotalEgrIng = new JLabel("Egreso+Ingreso");
        this.jlTotalEgrIng.setFont(CommonFormat.fuenteTitulo);
        this.jtfDineroTotal = new JFormattedTextField();
        this.jtfDineroTotal.setColumns(prefCols);
        this.jtfDineroTotal.addKeyListener(this);
        this.jtfCajaChica = new JFormattedTextField();
        this.jtfCajaChica.setColumns(prefCols);
        this.jtfCajaChica.addKeyListener(this);
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
        this.jtfTotalEgrIng = new JFormattedTextField();
        this.jtfTotalEgrIng.setColumns(prefCols);
        this.jtfTotalEgrIng.addKeyListener(this);

        this.jtfTotalEgrIng.setEditable(false);
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
        studentInfoPanel.add(jlDineroTotal, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfDineroTotal, gc);

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
        studentInfoPanel.add(jlTotalEgrIng, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        studentInfoPanel.add(jtfTotalEgrIng, gc);

        // ////////// Buttons Panel ///////////////
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        saveButton.setPreferredSize(btnSize);

        // Add sub panels to dialog
        setLayout(new BorderLayout());
        add(jpFiltros, BorderLayout.NORTH);
        add(studentInfoPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
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

        int totalEgrIng = totalEgreso + totalIngreso;
        this.jtfTotalEgrIng.setValue(totalEgrIng);
        this.jtfEgresoTotal.setValue(totalEgreso);
        this.jtfEgresoContado.setValue(egresoContado);
        this.jtfEgresoCredito.setValue(egresoCretdito);
        this.jtfIngresoTotal.setValue(totalIngreso);
        this.jtfIngresoContado.setValue(ingresoContado);
        this.jtfIngresoCredito.setValue(ingresoCretdito);
    }

    private void setWindows(JFrame parentFrame) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(parentFrame);
    }

    private void crearCaja() {
        /*
         * VALIDAR dineroTotal
         */
        Integer dineroTotal;
        try {
            String LongToString = String.valueOf(this.jtfDineroTotal.getValue());
            dineroTotal = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.jtfDineroTotal.setBackground(Color.red);
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
            cierre = sdfs.parse(fechaInicio);
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
        //CONTINUAR
        Caja caja = new Caja();
        caja.setMontoFinal(cajaChica);
        caja.setMontoFinal(cajaChica);
    }

    private void checkJFTDineroTotal() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = jtfDineroTotal.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                jtfDineroTotal.setValue(StringToLong);
                String valorJFT = jtfDineroTotal.getText();
                jtfDineroTotal.select(valorJFT.length(), valorJFT.length());
            }
        });
    }

    private void checkJFTCajaChica() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = jtfCajaChica.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                jtfCajaChica.setValue(StringToLong);
                String valorJFT = jtfCajaChica.getText();
                jtfCajaChica.select(valorJFT.length(), valorJFT.length());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.cancelButton) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Object src = e.getSource();
        if (src == this.jtfDineroTotal) {
            checkJFTDineroTotal();
        } else if (src == this.jtfCajaChica) {
            checkJFTCajaChica();
        } else if (src == this.jtfEgresoTotal) {
            checkJFTEgresoTotal();
        } else if (src == this.jtfIngresoTotal) {
            checkJFTIngresoTotal();
        } else if (src == this.jtfTotalEgrIng) {
            checkJFTItotalEgrIng();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void addListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkJFTEgresoTotal() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = jtfEgresoTotal.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                jtfEgresoTotal.setValue(StringToLong);
                String valorJFT = jtfEgresoTotal.getText();
                jtfEgresoTotal.select(valorJFT.length(), valorJFT.length());
            }
        });
    }

    private void checkJFTIngresoTotal() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = jtfIngresoTotal.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                jtfIngresoTotal.setValue(StringToLong);
                String valorJFT = jtfIngresoTotal.getText();
                jtfIngresoTotal.select(valorJFT.length(), valorJFT.length());
            }
        });
    }

    private void checkJFTItotalEgrIng() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String valorIngresado = jtfTotalEgrIng.getText().replace(".", "");
                valorIngresado = valorIngresado.replace(",", "");
                Long StringToLong = null;
                try {
                    StringToLong = Long.valueOf(valorIngresado);
                } catch (NumberFormatException numberFormatException) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Ingrese solo numeros",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                }
                jtfTotalEgrIng.setValue(StringToLong);
                String valorJFT = jtfTotalEgrIng.getText();
                jtfTotalEgrIng.select(valorJFT.length(), valorJFT.length());
            }
        });
    }
}
