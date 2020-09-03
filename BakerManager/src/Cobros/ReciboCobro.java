/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_Divisa;
import Entities.E_banco;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_formaPago;
import Entities.E_movimientoContable;
import Entities.E_reciboTipoPago;
import Entities.E_tipoCheque;
import Entities.M_facturaDetalle;
import Interface.RecibirCtaCteDetalleCallback;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ReciboCobro extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final String TITULO = "Confirmar cobro",
            S_ALERTA = "¿Está seguro que desea continuar?",
            S_ID_VENTA = "ID venta",
            S_NRO_FACTURA = "Nro Factura",
            S_TOTAL_FACTURA = "Total factura",
            S_TOTAL_PENDIENTE = "Total pendiente",
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
            S_FECHA_CHEQUE = "Fecha del cheque",
            S_CHEQUE_FECHA_DIFERIDA = "Fecha diferida";
    private javax.swing.JButton jbCancel, jbOK;
    private javax.swing.JLabel jlFecha, jlCliente, jlNroFactura, jlFormaPago, jlTotalFactura,
            jlTotalPendiente, jlIdVenta, jlImporteEfectivo, jlImporteCheque, jlObservacion, jlDivisa;
    private javax.swing.JTextField jtfIdVenta, jtfCliente, jtfNroFactura, jtfTotalFactura, jtfTotalPendiente;
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
    //VARIABLES TARJETA TODO

    private RecibirCtaCteDetalleCallback callback;
    private E_movimientoContable facturaCabecera;
    private boolean modificarDetalle;
    private DecimalFormat decimalFormat;
    private int index;//para modificar filas

    public ReciboCobro(JDialog vista) {
        super(vista, TITULO, true);
        setTitle(TITULO);
        setSize(new java.awt.Dimension(800, 450));
        setLocationRelativeTo(vista);
        initComponents();
        inicializarVista();
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    private void inicializarVista() {
        modificarDetalle = false;
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

    public void nuevoPago(E_movimientoContable fsp) {
        modificarDetalle = false;
        facturaCabecera = fsp;
        switch (fsp.getTipo()) {
            case E_movimientoContable.TIPO_VENTA: {
                jtfIdVenta.setText(decimalFormat.format(facturaCabecera.getVenta().getIdCabecera()));
                jtfNroFactura.setText(decimalFormat.format(facturaCabecera.getVenta().getNroFactura()));
                break;
            }
            case E_movimientoContable.TIPO_SALDO_INICIAL: {
                jtfIdVenta.setText("Saldo inicial");
                jtfNroFactura.setText("Saldo inicial");
                break;
            }
        }
        jtfCliente.setText(facturaCabecera.getVenta().getClienteEntidad());
        jtfTotalFactura.setText(decimalFormat.format(facturaCabecera.getVenta().getMonto()));
        jtfTotalPendiente.setText(decimalFormat.format(facturaCabecera.getVenta().getSaldo()));
    }

    public void modificarDetalle(int index, E_movimientoContable fsp, E_cuentaCorrienteDetalle detalle) {
        this.index = index;
        modificarDetalle = true;
        facturaCabecera = new E_movimientoContable();
        /*
        ACTUALIZAR VISTA CON LOS DATOS
         */
        switch (fsp.getTipo()) {
            case E_movimientoContable.TIPO_VENTA: {
                facturaCabecera.setVenta(DB_Cobro.obtenerFacturaSinPagoPorId(fsp.getVenta().getIdCabecera()));
                jtfIdVenta.setText(decimalFormat.format(facturaCabecera.getVenta().getIdCabecera()));
                jtfNroFactura.setText(decimalFormat.format(facturaCabecera.getVenta().getNroFactura()));
                break;
            }
            case E_movimientoContable.TIPO_SALDO_INICIAL: {
                facturaCabecera.setVenta(DB_Cobro.obtenerSaldoInicialPendiente(fsp.getVenta().getIdCliente()));
                jtfIdVenta.setText("Saldo inicial");
                jtfNroFactura.setText("Saldo inicial");
                break;
            }
        }
        //facturaCabecera = DB_Cobro.obtenerFacturaSinPago(fsp.getNroFactura());
        //el id no esta presente en la vista V_facturas sinpago, por eso lo extraemos del detalle recibido
        //facturaCabecera.setIdFacturaCabecera(detalle.getIdFacturaCabecera());
        jtfCliente.setText(facturaCabecera.getVenta().getClienteEntidad());
        jtfTotalFactura.setText(decimalFormat.format(facturaCabecera.getVenta().getMonto()));
        jtfTotalPendiente.setText(decimalFormat.format(facturaCabecera.getVenta().getSaldo()));
        E_formaPago fp = detalle.getFormaPago();
        jcbFormaPago.setSelectedItem(fp);
        jcbFormaPago.setEnabled(false);
        switch (fp.getId()) {
            case E_formaPago.CHEQUE: {
                jdcChequeFecha.setDate(detalle.getFechaCheque());
                jcbBanco.setSelectedItem(detalle.getBanco());
                jtfNroCheque.setText(detalle.getNroCheque() + "");
                jcbTipoCheque.setSelectedItem(detalle.getTipoCheque());
                jftImporteCheque.setText(detalle.getMonto() + "");
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
                jftImporteEfectivo.setText(((int) detalle.getMonto()) + "");
                break;
            }
            case E_formaPago.TARJETA: {
                break;
            }
        }
        /*
        REMOVER LISTENERS DE LOS COMBOBOX
         */

        jcbFormaPago.removeKeyListener(this);
        jcbFormaPago.removeActionListener(this);
    }

    public void setInterface(RecibirCtaCteDetalleCallback interfaceNotificarCambio) {
        this.callback = interfaceNotificarCambio;
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        //FECHA
        Calendar calendar = Calendar.getInstance();
        decimalFormat = new DecimalFormat("###,###");
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
        jlTotalPendiente = new javax.swing.JLabel(S_TOTAL_PENDIENTE);
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
        jtfTotalPendiente = new javax.swing.JTextField();
        jtfTotalPendiente.setEditable(false);
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
        jdcChequeFecha.setDate(calendar.getTime());
        jdcChequeFecha.setEnabled(false);
        jdcChequeFechaDiferida = new com.toedter.calendar.JDateChooser();
        jdcChequeFechaDiferida.setDate(calendar.getTime());
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
        jpDatosGenerales.add(jlTotalPendiente);
        jpDatosGenerales.add(jtfTotalPendiente, "width :300:,grow,wrap");
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
        if (!validarNroCheque()) {
            return;
        }
        if (!validarFechaCheque()) {
            return;
        }
        if (!validarFechaChequeDiferida()) {
            return;
        }
        E_cuentaCorrienteDetalle detalle = new E_cuentaCorrienteDetalle();
        switch (facturaCabecera.getTipo()) {
            case E_movimientoContable.TIPO_VENTA: {
                detalle.setTipoPago(new E_reciboTipoPago(E_reciboTipoPago.TIPO_FACTURA, ""));
                detalle.setIdFacturaCabecera(facturaCabecera.getVenta().getIdCabecera());
                detalle.setNroFactura(facturaCabecera.getVenta().getNroFactura());
                break;
            }
            case E_movimientoContable.TIPO_SALDO_INICIAL: {
                detalle.setTipoPago(new E_reciboTipoPago(E_reciboTipoPago.TIPO_SALDO_INICIAL, ""));
                detalle.setIdFacturaCabecera(0);
                detalle.setNroFactura(0);
                break;
            }
        }
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        detalle.setFormaPago(fp);
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
                detalle.setTipoCheque(tc);
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
        if (modificarDetalle) {
            callback.modificarCtaCteDetalle(index, detalle, facturaCabecera.getVenta().getMonto());
        } else {
            callback.recibirCtaCteDetalle(detalle, facturaCabecera.getVenta().getMonto());
        }
        dispose();
    }

    private boolean isValidPayAmount() {
        int importe = 0;
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        switch (fp.getId()) {
            case E_formaPago.EFECTIVO: {
                try {
                    importe = Integer.valueOf(jftImporteEfectivo.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Inserte un número válido para el importe", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            }
            case E_formaPago.CHEQUE: {
                try {
                    importe = Integer.valueOf(jftImporteCheque.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Inserte un número válido para el importe", "Atención", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            }
        }
        if (importe <= 0) {
            JOptionPane.showMessageDialog(this, "El importe debe ser mayor a 0 (cero)", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (importe > facturaCabecera.getVenta().getMonto()) {
            JOptionPane.showMessageDialog(this, "El importe ingresado supera al total a pagar", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (importe > facturaCabecera.getVenta().getSaldo()) {
            JOptionPane.showMessageDialog(this, "El importe ingresado supera al saldo pendiente", "Atención", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarNroCheque() {
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        if (fp.getId() == E_formaPago.CHEQUE) {
            int nroCheque = 0;
            try {
                nroCheque = Integer.valueOf(jtfNroCheque.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Inserte un número de cheque válido", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (nroCheque <= 0) {
                JOptionPane.showMessageDialog(this, "El número de cheque debe ser mayor a 0 (cero)", "Atención", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarFechaCheque() {
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        if (fp.getId() == E_formaPago.CHEQUE) {
            Date entrega = null;
            try {
                entrega = jdcChequeFecha.getDate();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La fecha del cheque no es válida. Intente nuevamente", "Fecha de cheque inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (entrega == null) {
                JOptionPane.showMessageDialog(this, "La fecha del cheque no es válida. Intente nuevamente", "Fecha de cheque inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validarFechaChequeDiferida() {
        E_formaPago fp = jcbFormaPago.getItemAt(jcbFormaPago.getSelectedIndex());
        if (fp.getId() == E_formaPago.CHEQUE) {
            E_tipoCheque tc = jcbTipoCheque.getItemAt(jcbTipoCheque.getSelectedIndex());
            if (tc.getId() == E_tipoCheque.DIFERIDO) {
                Date entrega = null;
                try {
                    entrega = jdcChequeFechaDiferida.getDate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "La fecha del cheque diferido no es válida. Intente nuevamente", "Fecha de cheque diferido inválida", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if (entrega == null) {
                    JOptionPane.showMessageDialog(this, "La fecha del cheque diferido no es válida. Intente nuevamente", "Fecha de cheque diferido inválida", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if (!validarFechaCheque()) {
                    return false;
                }
                if (entrega.before(jdcChequeFecha.getDate())) {
                    JOptionPane.showMessageDialog(this, "La fecha del cheque diferido no puede ser menor a la fecha de cheque. Intente nuevamente", "Fecha de cheque diferido inválida", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

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

    /**
     * Funcion que retorna el total de la factura pendiente, utilizado cuando se
     * modifica un cobro ya que no se guarda la factura sin pagar duante la
     * seleccion de cada detalle
     */
    private int totalFactura(int idFacturaCabecera) {
        int total = 0;
        ArrayList<M_facturaDetalle> fade = DB_Ingreso.obtenerVentaDetalles(idFacturaCabecera);
        for (M_facturaDetalle detalle : fade) {
            Integer Precio = detalle.getPrecio() - Math.round(Math.round(((detalle.getPrecio() * detalle.getDescuento()) / 100)));
            Integer subTotal = Math.round(Math.round((detalle.getCantidad() * Precio)));
            total = total + subTotal;
        }
        return total;
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
