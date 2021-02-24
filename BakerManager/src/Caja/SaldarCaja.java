/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import DB.DB_Caja;
import DB.DB_Cobro;
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
import Entities.M_egresoCabecera;
import Entities.Moneda;
import Interface.InterfaceCajaMovimientos;
import Interface.MovimientosCaja;
import MenuPrincipal.DatosUsuario;
import bakermanager.C_inicio;
import com.nitido.utils.toaster.Toaster;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class SaldarCaja implements ActionListener, KeyListener, InterfaceCajaMovimientos {

    private static final String TT_EFECTIVO_RENDIR = "Efectivo a rendir: suma de ventas y cobros en efectivo menos compras y pagos en efectivo";

    private ArqueoCajaTableModel tbmFondoApertura, tbmFondoCierre, tbmDepositar;
    private MovimientosCaja movimientosCaja;
    private V_SaldarCaja vista;

    public SaldarCaja(C_inicio inicio) {
        this.vista = new V_SaldarCaja(inicio);
        initializeVariables();
        addListeners();
        initializeLogic();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void initializeVariables() {
        //ARQUEO CAJA 
        tbmFondoApertura = new ArqueoCajaTableModel();
        tbmFondoCierre = new ArqueoCajaTableModel();
        tbmDepositar = new ArqueoCajaTableModel();
        vista.jtFondoCierre.setModel(tbmFondoCierre);
        vista.jtFondoApertura.setModel(tbmFondoApertura);
        vista.jtDepositar.setModel(tbmDepositar);
        this.vista.jftEfectivoRendir.setToolTipText(TT_EFECTIVO_RENDIR);
    }

    private void addListeners() {

        this.vista.jtfTotalCobrado.addKeyListener(this);
        this.vista.jtfTotalEgrIng2.addKeyListener(this);
        this.vista.jtfTotalEgrIng1.addKeyListener(this);
        this.vista.jtfEgresoTotal.addKeyListener(this);
        this.vista.jtfFondoCierre.addKeyListener(this);
        this.vista.jtfFondoApertura.addKeyListener(this);
        this.vista.jtfIngresoTotal.addKeyListener(this);
        this.vista.jtfTotalCobradoEfectivo.addKeyListener(this);
        this.vista.jtfTotalCobradoCheque.addKeyListener(this);
        this.vista.jtfTotalPagado.addKeyListener(this);
        this.vista.jtfTotalPagadoEfectivo.addKeyListener(this);
        this.vista.jtfTotalPagadoCheque.addKeyListener(this);
        this.vista.jbDetalle.addActionListener(this);
        this.vista.saveButton.addActionListener(this);
        this.vista.cancelButton.addActionListener(this);
        this.vista.jbFondoAnterior.addActionListener(this);
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
        this.vista.jtFondoApertura.addKeyListener(this);
        this.vista.jtFondoCierre.addKeyListener(this);
        this.vista.jpArqueoCaja.addKeyListener(this);
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
        this.vista.jtfTotalEgrIng1.setValue(totalEgrMasIng);
        this.vista.jtfTotalEgrIng2.setValue(totalEgrMenosIng);
        this.vista.jtfEgresoTotal.setValue(totalEgreso);
        this.vista.jtfEgresoContado.setValue(egresoContado);
        this.vista.jtfEgresoCredito.setValue(egresoCretdito);
        this.vista.jtfIngresoTotal.setValue(totalIngreso);
        this.vista.jtfIngresoContado.setValue(ingresoContado);
        this.vista.jtfIngresoCredito.setValue(ingresoCretdito);
        this.vista.jtfTotalCobrado.setValue(totalCobrado);
        this.vista.jtfTotalPagado.setValue(totalPagado);

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
        Utilities.c_packColumn.packColumns(this.vista.jtFondoApertura, 1);

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
        Utilities.c_packColumn.packColumns(this.vista.jtFondoCierre, 1);
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
        Utilities.c_packColumn.packColumns(this.vista.jtDepositar, 1);
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getIdFuncionario();
        movimientosCaja = new MovimientosCaja();
        movimientosCaja.setMovimientoVentas((ArrayList<E_facturaCabecera>) DB_Ingreso.obtenerMovimientoVentasCabeceras(idFuncionario, -1, fechaInicio, fechaFin, -1));
        movimientosCaja.setMovimientoCompras((ArrayList<M_egresoCabecera>) DB_Egreso.obtenerMovimientoComprasCabeceras(idFuncionario, -1, -1, fechaInicio, fechaFin));
        movimientosCaja.setMovimientoCobros((ArrayList<E_cuentaCorrienteCabecera>) DB_Cobro.obtenerMovimientoCobrosCabeceras(idFuncionario, -1, fechaInicio, fechaFin));
        movimientosCaja.setMovimientoPagos((ArrayList<E_reciboPagoCabecera>) DB_Pago.obtenerMovimientoPagosCabeceras(idFuncionario, -1, fechaInicio, fechaFin));
        actualizarMovimientos(movimientosCaja);

    }

    private void crearCaja() {
        /*
         * VALIDAR dineroTotal
         */
        Integer fondoInicial;
        try {
            String LongToString = String.valueOf(this.vista.jtfFondoApertura.getValue());
            fondoInicial = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.vista.jtfFondoApertura.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Coloque un dinero total válido",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR caja chico
         */
        Integer cajaChica;
        try {
            String LongToString = String.valueOf(this.vista.jtfFondoCierre.getValue());
            cajaChica = Integer.valueOf(LongToString.replace(".", ""));
        } catch (Exception e) {
            this.vista.jtfFondoCierre.setBackground(Color.red);
            javax.swing.JOptionPane.showMessageDialog(this.vista, "Coloque una caja chica válida",
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
        String fechaInicio = sdf.format(this.vista.jddInicio.getDate()) + " " + this.vista.jcbHoraInicio.getSelectedItem() + ":" + this.vista.jcbMinutoInicio.getSelectedItem() + ":00";
        try {
            apertura = sdfs.parse(fechaInicio);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Ingrese una fecha valida en el campo Tiempo apertura",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        /*
         * VALIDAR TIEMPO FIN
         */
        Date cierre = null;
        String fechaFin = sdf.format(this.vista.jddFinal.getDate()) + " " + this.vista.jcbHoraFin.getSelectedItem() + ":" + this.vista.jcbMinutoFin.getSelectedItem() + ":00";
        try {
            cierre = sdfs.parse(fechaFin);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Ingrese una fecha valida en el campo Tiempo cierre",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        if (apertura.after(cierre)) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "La fecha de apertura debe ser menor a la de cierre.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getIdFuncionario();
        Caja caja = new Caja();
        try {
            caja.setIdEmpleadoApertura(idFuncionario);
            caja.setIdEmpleadoCierre(idFuncionario);
            caja.setTiempoApertura(apertura);
            caja.setTiempoCierre(cierre);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "Hubo un problema creando la caja",
                    "Verifique los datos",
                    javax.swing.JOptionPane.OK_OPTION);
            return;
        }
        ArrayList<ArqueoCajaDetalle> arqueoCajaApertura = arqueoCajaApertura();
        ArrayList<ArqueoCajaDetalle> arqueoCajaCierre = arqueoCajaCierre();
        ArrayList<ArqueoCajaDetalle> arqueoDeposito = arqueoDepositar();

        if (!existenMovimientos()) {
            return;
        }
        if (!validarMontoADepositar()) {
            return;
        }
        try {
            DB_Caja.insertarArqueoCaja(caja, arqueoCajaApertura, arqueoCajaCierre, arqueoDeposito, movimientosCaja);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.vista, "Hubo un problema creando la caja", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mostrarMensaje("La caja se registró con éxito.");
        cerrar();
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
        this.vista.dispose();
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
        this.vista.jtfFondoApertura.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarCajaChicaAnterior() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmFondoCierre.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.vista.jtfFondoCierre.setValue(total);
        calcularDiferenciaCaja();
    }

    private void sumarDepositar() {
        int total = 0;
        for (ArqueoCajaDetalle arquDeta : this.tbmDepositar.arqueoCajaDetalleList) {
            total = total + (arquDeta.getCantidad() * arquDeta.getMoneda().getValor());
        }
        this.vista.jtfDepositar.setValue(total);
    }

    private void calcularDiferenciaCaja() {
        int fondoInicial = 0;
        int fondoFinal = 0;
        if (null != this.vista.jtfFondoApertura.getValue()) {
            try {
                fondoInicial = Integer.valueOf(String.valueOf(this.vista.jtfFondoApertura.getValue()));
            } catch (Exception e) {
                fondoInicial = 0;
            }
        }
        if (null != this.vista.jtfFondoCierre.getValue()) {
            try {
                fondoFinal = Integer.valueOf(String.valueOf(this.vista.jtfFondoCierre.getValue()));
            } catch (Exception e) {
                fondoFinal = 0;
            }
        }
        //this.jtfDifCaja.setValue(fondoInicial - fondoFinal);
    }

    private void invocarDetalle() {
        int horaFin = this.vista.jcbHoraFin.getSelectedIndex();
        int minutoFin = this.vista.jcbMinutoFin.getSelectedIndex();
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(this.vista.jddInicio.getDate());
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(this.vista.jddFinal.getDate());
        calendarFinal.set(Calendar.HOUR_OF_DAY, horaFin);
        calendarFinal.set(Calendar.MINUTE, minutoFin);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        CajaDetalle cd = new CajaDetalle(this.vista);
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
        for (M_egresoCabecera compra : movimientosCaja.getMovimientoCompras()) {
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
        this.vista.jtfTotalCobrado.setValue(totalCobro);
        this.vista.jtfTotalCobradoEfectivo.setValue(totalCobroEfectivo);
        this.vista.jtfTotalCobradoCheque.setValue(totalCobroCheque);
        //PAGOS
        this.vista.jtfTotalPagado.setValue(totalPago);
        this.vista.jtfTotalPagadoCheque.setValue(totalPagoCheque);
        this.vista.jtfTotalPagadoEfectivo.setValue(totalPagoEfectivo);
        //VENTAS
        this.vista.jtfIngresoTotal.setValue(totalVenta);
        this.vista.jtfIngresoContado.setValue(totalVentaContado);
        this.vista.jtfIngresoCredito.setValue(totalVentaCredito);
        //COMPRAS
        this.vista.jtfEgresoTotal.setValue(totalCompra);
        this.vista.jtfEgresoContado.setValue(totalCompraContado);
        this.vista.jtfEgresoCredito.setValue(totalCompraCredito);
        Integer aDepositar = totalCobroEfectivo + totalVentaContado - totalCompraContado - totalPagoEfectivo;
        this.vista.jftEfectivoRendir.setValue(aDepositar);
    }

    private boolean existenMovimientos() {
        boolean hayMovimientos = false;
        if (!movimientosCaja.getMovimientoCobros().isEmpty()) {
            hayMovimientos = true;
        } else if (!movimientosCaja.getMovimientoCompras().isEmpty()) {
            hayMovimientos = true;
        } else if (!movimientosCaja.getMovimientoVentas().isEmpty()) {
            hayMovimientos = true;
        } else if (!movimientosCaja.getMovimientoPagos().isEmpty()) {
            hayMovimientos = true;
        }
        if (!hayMovimientos) {
            int opcion = JOptionPane.showConfirmDialog(vista, "La caja no contiene movimientos. ¿Desea continuar?", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                return true;
            }
        }
        return hayMovimientos;
    }

    private boolean validarMontoADepositar() {
        System.out.println("Caja.SaldarCaja.validarMontoADepositar()");
        //aRendir es el monto de la suma de ventas al contado y cobros en efectivo (Ingresos en efectivo)
        int aRendir = (int) vista.jftEfectivoRendir.getValue();
        //aDepositar es la cantidad de billetes y monedas que el usuario registra a depositar para rendir la caja
        int aDepositar = (int) vista.jtfDepositar.getValue();
        System.out.println("aRendir: " + aRendir);
        System.out.println("aDepositar: " + aDepositar);
        if (aDepositar < aRendir) {
            System.out.println("Caja.SaldarCaja.validarMontoADepositar().1");
            int opcion = JOptionPane.showConfirmDialog(vista, "El monto depositado es menor al que tiene que rendir. ¿Desea continuar?", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            return opcion == JOptionPane.YES_OPTION;
        }
        if (aDepositar > aRendir) {
            System.out.println("Caja.SaldarCaja.validarMontoADepositar().2");
            int opcion = JOptionPane.showConfirmDialog(vista, "El monto depositado es mayor al que tiene que rendir. ¿Desea continuar?", "Atención", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
            return opcion == JOptionPane.YES_OPTION;
        }
        System.out.println("Caja.SaldarCaja.validarMontoADepositar().3: " + (aRendir > 0 && aDepositar == aRendir));
        //LOS MONTOS COINCIDEN
        if (aRendir == 0 && aDepositar == 0) {
            return true;
        }
        return aRendir > 0 && aDepositar == aRendir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.vista.cancelButton) {
            cerrar();
        } else if (src.equals(this.vista.saveButton)) {
            crearCaja();
        } else if (src.equals(this.vista.jbFondoAnterior)) {
            consultarUltimoFondo();
        } else if (src.equals(this.vista.jbDetalle)) {
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
        this.vista.jddInicio.setDate(tiempoInicio);
        this.vista.jddFinal.setDate(calendarFinal.getTime());
        this.vista.jcbHoraFin.setSelectedIndex(calendarFinal.get(Calendar.HOUR_OF_DAY));
        this.vista.jcbMinutoFin.setSelectedIndex(calendarFinal.get(Calendar.MINUTE));
    }

}
