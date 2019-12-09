/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import DB.DB_Ingreso;
import DB.DB_manager;
import Empleado.Seleccionar_funcionario;
import Entities.E_Divisa;
import Entities.E_banco;
import Entities.E_formaPago;
import Entities.E_reciboPago;
import Entities.E_tipoCheque;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import Interface.InterfaceNotificarCambio;
import Interface.RecibirEmpleadoCallback;
import bakermanager.C_inicio;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class ReciboPago extends javax.swing.JDialog implements ActionListener, KeyListener, RecibirEmpleadoCallback {

    public static final String TITULO = "Confirmar cobro",
            S_ALERTA = "¿Está seguro que desea continuar?",
            S_NRO_FACTURA = "Nro. de recibo:",
            S_CLIENTE = "Cliente:",
            S_FORMA_PAGO = "Forma de pago:",
            S_FUNCIONARIO = "Cobrador",
            S_FECHA = "Fecha:",
            S_IMPORTE = "Importe:",
            S_OBS = "Observación",
            S_DIVISA = "Divisa",
            S_DATOS_GENERALES = "Datos generales",
            S_DATOS_TARJETA = "Datos de tarjeta",
            S_DATOS_CHEQUE = "Datos de cheque";
    private static final String S_BANCO = "Banco",
            S_TIPO_CHEQUE = "Tipo de cheque",
            S_NRO_CHEQUE = "Nro. de cheque",
            S_CHEQUE_FECHA_DIFERIDA = "Fecha diferida";
    private javax.swing.JButton jbCancel, jbOK, jbFuncionario;
    private javax.swing.JLabel jlFecha, jlCliente, jlFormaPago, jlNroRecibo,
            jlImporte, jlObservacion, jlDivisa;
    private javax.swing.JTextField jtfCliente, jtfFuncionario,
            jtfNroRecibo, jtfObservacion;
    private javax.swing.JFormattedTextField jftFecha, jftImporte;
    private javax.swing.JComboBox<E_Divisa> jcbDivisa;
    private javax.swing.JComboBox<E_formaPago> jcbFormaPago;

    //VARIABLES CHEQUE
    private javax.swing.JLabel jlBanco, jlChequeFechaDiferida, jlNroCheque, jlTipoCheque;
    private javax.swing.JTextField jtfNroCheque;
    private com.toedter.calendar.JDateChooser jdcChequeFechaDiferida;
    private javax.swing.JComboBox<E_banco> jcbBanco;
    private javax.swing.JComboBox<E_tipoCheque> jcbTipoCheque;
    //VARIABLES TARJETA

    private InterfaceNotificarCambio interfaceNotificarCambio;
    private M_facturaCabecera facturaCabecera;

    public ReciboPago(C_inicio c_inicio) {
        super(c_inicio.vista, TITULO, true);
        setTitle(TITULO);
        setSize(new java.awt.Dimension(750, 460));
        setLocationRelativeTo(c_inicio.vista);
        initComponents();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void inicializarVista(int idFactura, int totalFactura) {
        facturaCabecera = DB_Ingreso.obtenerIngresoCabeceraCompleto(idFactura);
        facturaCabecera.setTotal(totalFactura);
        jtfCliente.setText(facturaCabecera.getCliente().getEntidad());
        jtfFuncionario.setText(facturaCabecera.getFuncionario().getNombre());
        jtfNroRecibo.setText(getNroRecibo() + "");
        jftFecha.setValue(Calendar.getInstance().getTime());
        ArrayList<E_formaPago> formaPagoList = DB_manager.obtenerFormaPagos();
        for (int i = 0; i < formaPagoList.size(); i++) {
            E_formaPago get = formaPagoList.get(i);
            jcbFormaPago.addItem(get);
        }
        ArrayList<E_Divisa> divisaList = DB_manager.obtenerDivisas();
        for (int i = 0; i < divisaList.size(); i++) {
            E_Divisa get = divisaList.get(i);
            jcbDivisa.addItem(get);
        }
        ArrayList<E_banco> bancoList = DB_manager.obtenerBancos();
        for (int i = 0; i < bancoList.size(); i++) {
            E_banco get = bancoList.get(i);
            jcbBanco.addItem(get);
        }
        ArrayList<E_tipoCheque> chequeTipoList = DB_manager.obtenerTipoCheques();
        for (int i = 0; i < chequeTipoList.size(); i++) {
            E_tipoCheque get = chequeTipoList.get(i);
            jcbTipoCheque.addItem(get);
        }
    }

    public void setInterface(InterfaceNotificarCambio interfaceNotificarCambio) {
        this.interfaceNotificarCambio = interfaceNotificarCambio;
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        //FECHA
        jlFecha = new javax.swing.JLabel(S_FECHA);
        jftFecha = new JFormattedTextField(
                new DefaultFormatterFactory(
                        new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));
        jftFecha.setFocusable(false);
        jftFecha.setEditable(false);
        //BOTONES SUR
        jbOK = new javax.swing.JButton("OK");
        jbCancel = new javax.swing.JButton("Cancel");
        //DATOS GENERALES
        jbFuncionario = new javax.swing.JButton(S_FUNCIONARIO);
        jlImporte = new javax.swing.JLabel(S_IMPORTE);
        jlObservacion = new javax.swing.JLabel(S_OBS);
        jlCliente = new javax.swing.JLabel(S_CLIENTE);
        jlFormaPago = new javax.swing.JLabel(S_FORMA_PAGO);
        jlNroRecibo = new javax.swing.JLabel(S_NRO_FACTURA);
        jlDivisa = new javax.swing.JLabel(S_DIVISA);
        jtfCliente = new javax.swing.JTextField();
        jtfCliente.setEditable(false);
        jcbFormaPago = new javax.swing.JComboBox();
        jtfNroRecibo = new javax.swing.JTextField();
        jtfFuncionario = new javax.swing.JTextField();
        jtfFuncionario.setEditable(false);
        jtfObservacion = new javax.swing.JTextField();
        jcbDivisa = new javax.swing.JComboBox<>();
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));
        jftImporte = new JFormattedTextField(dff, 0);
        //DATOS CHEQUE
        jlBanco = new javax.swing.JLabel(S_BANCO);
        jlChequeFechaDiferida = new javax.swing.JLabel(S_CHEQUE_FECHA_DIFERIDA);
        jlNroCheque = new javax.swing.JLabel(S_NRO_CHEQUE);
        jlTipoCheque = new javax.swing.JLabel(S_TIPO_CHEQUE);
        jdcChequeFechaDiferida = new com.toedter.calendar.JDateChooser();
        jdcChequeFechaDiferida.setEnabled(false);
        jtfNroCheque = new javax.swing.JTextField();
        jcbBanco = new javax.swing.JComboBox<>();
        jcbTipoCheque = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jbOK.addActionListener(this);
        jbFuncionario.addActionListener(this);
        jbCancel.addActionListener(this);
        jtfCliente.addKeyListener(this);
        jcbFormaPago.addKeyListener(this);
        jlObservacion.addKeyListener(this);
        jtfNroRecibo.addKeyListener(this);
        jcbTipoCheque.addActionListener(this);
        jcbFormaPago.addActionListener(this);
        JPanel jpFecha = new JPanel();
        jpFecha.add(jlFecha);
        jpFecha.add(jftFecha);
        JPanel jpBotonesSur = new JPanel();
        jpBotonesSur.add(jbOK);
        jpBotonesSur.add(jbCancel);
        JPanel jpDatosGenerales = new JPanel(new MigLayout());
        jpDatosGenerales.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), S_DATOS_GENERALES));
        jpDatosGenerales.add(jlNroRecibo);
        jpDatosGenerales.add(jtfNroRecibo, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlCliente);
        jpDatosGenerales.add(jtfCliente, "width :300:,grow,wrap");
        jpDatosGenerales.add(jbFuncionario);
        jpDatosGenerales.add(jtfFuncionario, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlFormaPago);
        jpDatosGenerales.add(jcbFormaPago, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlImporte);
        jpDatosGenerales.add(jftImporte, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlDivisa);
        jpDatosGenerales.add(jcbDivisa, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlObservacion);
        jpDatosGenerales.add(jtfObservacion, "width :300:,grow,wrap");
        JPanel jpDatosCheque = new JPanel(new MigLayout());
        jpDatosCheque.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), S_DATOS_CHEQUE));
        jpDatosCheque.add(jlBanco);
        jpDatosCheque.add(jcbBanco, "width :300:,grow,wrap");
        jpDatosCheque.add(jlNroCheque);
        jpDatosCheque.add(jtfNroCheque, "width :300:,grow,wrap");
        jpDatosCheque.add(jlTipoCheque);
        jpDatosCheque.add(jcbTipoCheque, "width :300:,grow,wrap");
        jpDatosCheque.add(jlChequeFechaDiferida);
        jpDatosCheque.add(jdcChequeFechaDiferida, "width :300:,grow,wrap");
        JPanel jpCenterLeft = new JPanel(new GridLayout(2, 1));
        jpCenterLeft.add(jpDatosCheque);
        JPanel jpCenter = new JPanel(new GridLayout(1, 2));
        jpCenter.add(jpDatosGenerales);
        jpCenter.add(jpCenterLeft);
        getContentPane().add(jpFecha, "width :300:,grow,wrap");
        getContentPane().add(jpCenter, "width :600:,grow,wrap");
        getContentPane().add(jpBotonesSur, "width :300:,grow");

        jtfCliente.selectAll();
    }

    private void seleccionarFuncionario() {
        Seleccionar_funcionario sf = new Seleccionar_funcionario(this);
        sf.setCallback(this);
        sf.mostrarVista();
    }

    public void registrarCobro() {
        if (!isValidNroRecibo()) {
            return;
        }
        if (!isValidPayAmount()) {
            return;
        }
        if (!isValidObservation()) {
            return;
        }
        int nroRecibo = Integer.valueOf(String.valueOf(jtfNroRecibo.getText().trim()));
        int importe = Integer.valueOf(String.valueOf(jftImporte.getValue()));
        String observacion = jtfObservacion.getText().trim();
        if (observacion.isEmpty()) {
            observacion = null;
        }
        E_reciboPago rp = new E_reciboPago();
        rp.setCliente(facturaCabecera.getCliente());
        rp.setFechaPago(Calendar.getInstance().getTime());
        rp.setFormaPago(jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex()));
        rp.setFuncionario(facturaCabecera.getFuncionario());
        rp.setImporte(importe);
        rp.setNroRecibo(nroRecibo);
        rp.setObservacion(observacion);
        //TODO DB_ingreso.registrarCobro();
        interfaceNotificarCambio.notificarCambio();
        dispose();
    }

    private boolean isValidNroRecibo() {
        Integer nroRecibo = null;
        if (this.jtfNroRecibo.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro: Nro. de recibo \n"
                    + "Asegurese de colocar un numero valido en el campo Nro. recibo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroRecibo.setText(getNroRecibo() + "");
            return false;
        }
        try {
            String cantidad = this.jtfNroRecibo.getText().trim();
            nroRecibo = Integer.valueOf(cantidad);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro: Nro. de recibo \n"
                    + "Asegurese de colocar un numero valido en el campo Nro. recibo.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroRecibo.setText(getNroRecibo() + "");
            return false;
        }
        if (!nroFacturaEnUso(nroRecibo)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro: Nro. de recibo \n"
                    + "El número de recibo introducido ya se encuentra en uso.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfNroRecibo.setText(getNroRecibo() + "");
            return false;
        }
        return true;
    }

    private boolean isValidPayAmount() {
        int importe = 0;
        try {
            importe = Integer.valueOf(String.valueOf(jftImporte.getValue()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Inserte un valor válido para el importe", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (importe > facturaCabecera.getTotal()) {
            JOptionPane.showMessageDialog(this, "El importe ingresado supera al total a pagar", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidObservation() {
        if (jtfObservacion.getText().trim().length() > 120) {
            JOptionPane.showMessageDialog(this, "El número de caracteres ingresados supera el limite(120)", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void formaPagoHandler() {
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        switch (fp.getId()) {
            case E_formaPago.CHEQUE: {
                jcbBanco.setEnabled(true);
                jtfNroCheque.setEnabled(true);
                jcbTipoCheque.setEnabled(true);
                break;
            }
            case E_formaPago.EFECTIVO: {
                jcbBanco.setEnabled(false);
                jtfNroCheque.setEnabled(false);
                jcbTipoCheque.setEnabled(false);
                jdcChequeFechaDiferida.setEnabled(false);
                break;
            }
            case E_formaPago.TARJETA: {
                jcbBanco.setEnabled(false);
                jtfNroCheque.setEnabled(false);
                jcbTipoCheque.setEnabled(false);
                jdcChequeFechaDiferida.setEnabled(false);
                break;
            }
        }
    }

    private void tipoChequeHandler() {
        E_tipoCheque tc = jcbTipoCheque.getItemAt(jcbTipoCheque.getSelectedIndex());
        if (tc.getId() == E_tipoCheque.DIFERIDO) {
            jdcChequeFechaDiferida.setEnabled(true);
        } else {
            jdcChequeFechaDiferida.setEnabled(false);
        }
    }

    public boolean nroFacturaEnUso(int nroFactura) {
        return DB_Ingreso.nroFacturaEnUso(nroFactura);
    }

    public int getNroRecibo() {
        return DB_Ingreso.obtenerUltimoNroFactura() + 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbOK)) {
            registrarCobro();
        } else if (e.getSource().equals(jbFuncionario)) {
            seleccionarFuncionario();
        } else if (e.getSource().equals(jcbFormaPago)) {
            formaPagoHandler();
        } else if (e.getSource().equals(jcbTipoCheque)) {
            tipoChequeHandler();
        } else if (e.getSource().equals(jbCancel)) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (jtfCliente.hasFocus() || jcbFormaPago.hasFocus() || jlObservacion.hasFocus() || jtfNroRecibo.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                registrarCobro();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                this.dispose();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void recibirFuncionario(M_funcionario funcionario) {
        this.facturaCabecera.setFuncionario(funcionario);
        this.jtfFuncionario.setText(funcionario.getNombre());
    }

}
