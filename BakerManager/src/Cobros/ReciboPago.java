/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_Divisa;
import Entities.E_banco;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.E_formaPago;
import Entities.E_tipoCheque;
import Interface.RecibirCtaCteDetalleCallback;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JDialog;
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
public class ReciboPago extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final String TITULO = "Confirmar cobro",
            S_ALERTA = "¿Está seguro que desea continuar?",
            S_ID_VENTA = "ID venta",
            S_NRO_FACTURA = "Nro Factura",
            S_TOTAL_FACTURA = "Total factura",
            S_CLIENTE = "Cliente:",
            S_FORMA_PAGO = "Forma de pago:",
            S_FUNCIONARIO = "Cobrador",
            S_FECHA = "Fecha:",
            S_IMPORTE = "Importe:",
            S_OBS = "Observación",
            S_DIVISA = "Divisa",
            S_DATOS_GENERALES = "Datos generales",
            S_DATOS_TARJETA = "Datos de tarjeta",
            S_DATOS_EFECTIVO = "Datos de efectivo",
            S_DATOS_CHEQUE = "Datos de cheque";
    private static final String S_BANCO = "Banco",
            S_TIPO_CHEQUE = "Tipo de cheque",
            S_NRO_CHEQUE = "Nro. de cheque",
            S_FECHA_CHEQUE = "Fecha diferida",
            S_CHEQUE_FECHA_DIFERIDA = "Fecha diferida";
    private javax.swing.JButton jbCancel, jbOK;
    private javax.swing.JLabel jlFecha, jlCliente, jlNroFactura, jlFormaPago, jlTotalFactura,
            jlIdVenta, jlImporteEfectivo, jlImporteCheque, jlObservacion, jlDivisa;
    private javax.swing.JTextField jtfIdVenta, jtfCliente, jtfNroFactura, jtfTotalFactura;
    private javax.swing.JTextField jftImporteEfectivo;
    //private javax.swing.JTextField jtfObservacion;
    private javax.swing.JFormattedTextField jftFecha;
    private javax.swing.JComboBox<E_Divisa> jcbDivisa;
    private javax.swing.JComboBox<E_formaPago> jcbFormaPago;

    //VARIABLES CHEQUE
    private javax.swing.JLabel jlBanco, jlFechaCheque, jlChequeFechaDiferida, jlNroCheque, jlTipoCheque;
    private javax.swing.JTextField jtfNroCheque;
    private com.toedter.calendar.JDateChooser jdcChequeFechaDiferida, jdcChequeFecha;
    private javax.swing.JComboBox<E_banco> jcbBanco;
    private javax.swing.JComboBox<E_tipoCheque> jcbTipoCheque;
    private javax.swing.JTextField jftImporteCheque;
    //VARIABLES TARJETA

    private RecibirCtaCteDetalleCallback callback;
    private E_facturaSinPago facturaCabecera;

    public ReciboPago(JDialog vista) {
        super(vista, TITULO, true);
        setTitle(TITULO);
        setSize(new java.awt.Dimension(775, 450));
        setLocationRelativeTo(vista);
        initComponents();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void inicializarVista(E_facturaSinPago fsp) {
        facturaCabecera = fsp;
        jtfCliente.setText(facturaCabecera.getClienteEntidad());
        jtfIdVenta.setText(facturaCabecera.getIdCabecera() + "");
        jtfNroFactura.setText(facturaCabecera.getNroFactura() + "");
        jtfTotalFactura.setText(facturaCabecera.getMonto() + "");
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

    public void setInterface(RecibirCtaCteDetalleCallback interfaceNotificarCambio) {
        this.callback = interfaceNotificarCambio;
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
        jlImporteEfectivo = new javax.swing.JLabel(S_IMPORTE);
        jlObservacion = new javax.swing.JLabel(S_OBS);
        jlCliente = new javax.swing.JLabel(S_CLIENTE);
        jlNroFactura = new javax.swing.JLabel(S_NRO_FACTURA);
        jlTotalFactura = new javax.swing.JLabel(S_TOTAL_FACTURA);
        jlFormaPago = new javax.swing.JLabel(S_FORMA_PAGO);
        jlIdVenta = new javax.swing.JLabel(S_ID_VENTA);
        jlDivisa = new javax.swing.JLabel(S_DIVISA);
        jtfIdVenta = new javax.swing.JTextField();
        jtfIdVenta.setEditable(false);
        jtfNroFactura = new javax.swing.JTextField();
        jtfNroFactura.setEditable(false);
        jtfCliente = new javax.swing.JTextField();
        jtfCliente.setEditable(false);
        jtfTotalFactura = new javax.swing.JTextField();
        jtfTotalFactura.setEditable(false);
        jcbFormaPago = new javax.swing.JComboBox();
        //jtfObservacion = new javax.swing.JTextField();
        jcbDivisa = new javax.swing.JComboBox<>();
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));
        jftImporteEfectivo = new javax.swing.JTextField();
        //DATOS CHEQUE
        jlBanco = new javax.swing.JLabel(S_BANCO);
        jlFechaCheque = new javax.swing.JLabel(S_FECHA_CHEQUE);
        jlChequeFechaDiferida = new javax.swing.JLabel(S_CHEQUE_FECHA_DIFERIDA);
        jlNroCheque = new javax.swing.JLabel(S_NRO_CHEQUE);
        jlTipoCheque = new javax.swing.JLabel(S_TIPO_CHEQUE);
        jdcChequeFecha = new com.toedter.calendar.JDateChooser();
        jdcChequeFecha.setEnabled(false);
        jdcChequeFechaDiferida = new com.toedter.calendar.JDateChooser();
        jdcChequeFechaDiferida.setEnabled(false);
        jtfNroCheque = new javax.swing.JTextField();
        jcbBanco = new javax.swing.JComboBox<>();
        jcbTipoCheque = new javax.swing.JComboBox<>();
        jlImporteCheque = new javax.swing.JLabel(S_IMPORTE);
        jftImporteCheque = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);
        jtfCliente.addKeyListener(this);
        jcbFormaPago.addKeyListener(this);
        jlObservacion.addKeyListener(this);
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
        jpDatosGenerales.add(jlIdVenta);
        jpDatosGenerales.add(jtfIdVenta, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlNroFactura);
        jpDatosGenerales.add(jtfNroFactura, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlCliente);
        jpDatosGenerales.add(jtfCliente, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlTotalFactura);
        jpDatosGenerales.add(jtfTotalFactura, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlFormaPago);
        jpDatosGenerales.add(jcbFormaPago, "width :300:,grow,wrap");
        jpDatosGenerales.add(jlDivisa);
        jpDatosGenerales.add(jcbDivisa, "width :300:,grow,wrap");
        //jpDatosGenerales.add(jlObservacion);
        //jpDatosGenerales.add(jtfObservacion, "width :300:,grow,wrap");
        JPanel jpDatosCheque = new JPanel(new MigLayout());
        jpDatosCheque.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), S_DATOS_CHEQUE));
        jpDatosCheque.add(jlFechaCheque);
        jpDatosCheque.add(jdcChequeFecha, "width :300:,grow,wrap");
        jpDatosCheque.add(jlBanco);
        jpDatosCheque.add(jcbBanco, "width :300:,grow,wrap");
        jpDatosCheque.add(jlNroCheque);
        jpDatosCheque.add(jtfNroCheque, "width :300:,grow,wrap");
        jpDatosCheque.add(jlTipoCheque);
        jpDatosCheque.add(jcbTipoCheque, "width :300:,grow,wrap");
        jpDatosCheque.add(jlChequeFechaDiferida);
        jpDatosCheque.add(jdcChequeFechaDiferida, "width :300:,grow,wrap");
        jpDatosCheque.add(jlImporteCheque);
        jpDatosCheque.add(jftImporteCheque, "width :300:,grow,wrap");
        JPanel jpDatosPagoEfectivo = new JPanel(new MigLayout());
        jpDatosPagoEfectivo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), S_DATOS_EFECTIVO));
        jpDatosPagoEfectivo.add(jlImporteEfectivo);
        jpDatosPagoEfectivo.add(jftImporteEfectivo, "width :300:,grow,wrap");
        JPanel jpCenterLeft = new JPanel(new MigLayout());
        jpCenterLeft.add(jpDatosCheque, "wrap");
        jpCenterLeft.add(jpDatosPagoEfectivo);
        JPanel jpCenter = new JPanel(new GridLayout(1, 2));
        jpCenter.add(jpDatosGenerales);
        jpCenter.add(jpCenterLeft);
        getContentPane().add(jpFecha, "width :300:,grow,wrap");
        getContentPane().add(jpCenter, "width :600:,grow,wrap");
        getContentPane().add(jpBotonesSur, "width :300:,grow");

        jtfCliente.selectAll();
    }

    public void registrarCobro() {
        if (!isValidPayAmount()) {
            return;
        }
        //TODO validar nro cheque
        E_cuentaCorrienteDetalle detalle = new E_cuentaCorrienteDetalle();
        detalle.setIdFacturaCabecera(facturaCabecera.getIdCabecera());
        detalle.setNroFactura(facturaCabecera.getNroFactura());
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        switch (fp.getId()) {
            case E_formaPago.CHEQUE: {
                int importeCheque = Integer.valueOf(jftImporteCheque.getText());
                int nroCheque = Integer.valueOf(jtfNroCheque.getText());
                E_banco banco = jcbBanco.getItemAt(jcbBanco.getSelectedIndex());
                detalle.setBanco(banco);
                detalle.setMonto(importeCheque);
                detalle.setNroCheque(nroCheque);
                detalle.setFechaCheque(jdcChequeFecha.getDate());
                E_tipoCheque tc = jcbTipoCheque.getItemAt(jcbTipoCheque.getSelectedIndex());
                if (tc.getId() == E_tipoCheque.DIFERIDO) {
                    detalle.setFechaDiferidaCheque(jdcChequeFechaDiferida.getDate());
                }
                break;
            }
            case E_formaPago.EFECTIVO: {
                int importeEfectivo = Integer.valueOf(String.valueOf(jftImporteEfectivo.getText()));
                detalle.setMonto(importeEfectivo);
                break;
            }
            case E_formaPago.TARJETA: {
                break;
            }
        }
        callback.recibirCtaCteDetalle(detalle, facturaCabecera.getMonto());
        dispose();
    }

    private boolean isValidPayAmount() {
        int importe = 0;
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        switch (fp.getId()) {
            case E_formaPago.EFECTIVO: {
                try {
                    importe = Integer.valueOf(String.valueOf(jftImporteEfectivo.getText()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Inserte un valor válido para el importe", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            case E_formaPago.CHEQUE: {
                try {
                    importe = Integer.valueOf(String.valueOf(jftImporteCheque.getText()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Inserte un valor válido para el importe", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        if (importe <= 0) {
            JOptionPane.showMessageDialog(this, "El importe debe ser mayor a 0 (cero)", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (importe > facturaCabecera.getMonto()) {
            JOptionPane.showMessageDialog(this, "El importe ingresado supera al total a pagar", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /*
    private boolean isValidObservation() {
        if (jtfObservacion.getText().trim().length() > 120) {
            JOptionPane.showMessageDialog(this, "El número de caracteres ingresados supera el limite(120)", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
     */
    private void formaPagoHandler() {
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        switch (fp.getId()) {
            case E_formaPago.CHEQUE: {
                jdcChequeFecha.setEnabled(true);
                jcbBanco.setEnabled(true);
                jtfNroCheque.setEnabled(true);
                jcbTipoCheque.setEnabled(true);
                jftImporteCheque.setEnabled(true);
                jftImporteEfectivo.setEnabled(false);
                break;
            }
            case E_formaPago.EFECTIVO: {
                jcbBanco.setEnabled(false);
                jtfNroCheque.setEnabled(false);
                jcbTipoCheque.setEnabled(false);
                jdcChequeFechaDiferida.setEnabled(false);
                jdcChequeFecha.setEnabled(false);
                jftImporteCheque.setEnabled(false);
                break;
            }
            case E_formaPago.TARJETA: {
                jcbBanco.setEnabled(false);
                jtfNroCheque.setEnabled(false);
                jcbTipoCheque.setEnabled(false);
                jdcChequeFecha.setEnabled(false);
                jdcChequeFechaDiferida.setEnabled(false);
                jftImporteCheque.setEnabled(false);
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
        if (jtfCliente.hasFocus() || jcbFormaPago.hasFocus()) {
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

    /*private boolean isValidNroRecibo() {
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
    }*/
}
