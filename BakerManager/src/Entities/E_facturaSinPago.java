/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_facturaSinPago extends E_facturaCabecera {

    private int monto;
    private int pago;
    private int saldo;

    public E_facturaSinPago() {
        super();
    }

    /**
     * @return the idCabecera
     */
    public int getIdCabecera() {
        return getIdFacturaCabecera();
    }

    /**
     * @param idCabecera the idCabecera to set
     */
    public void setIdCabecera(int idCabecera) {
        this.setIdFacturaCabecera(idCabecera);
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return getCliente().getIdCliente();
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.getCliente().setIdCliente(idCliente);
    }

    /**
     * @return the monto
     */
    public int getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(int monto) {
        this.monto = monto;
    }

    /**
     * @return the pago
     */
    public int getPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(int pago) {
        this.pago = pago;
    }

    /**
     * @return the saldo
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the ruc
     */
    public String getRuc() {
        return this.getCliente().getRuc();
    }

    /**
     * @param ruc the ruc to set
     */
    public void setRuc(String ruc) {
        this.getCliente().setRuc(ruc);
    }

    /**
     * @return the rucDiv
     */
    public String getRucDiv() {
        return this.getCliente().getRucId();
    }

    /**
     * @param rucDiv the rucDiv to set
     */
    public void setRucDiv(String rucDiv) {
        this.getCliente().setRucId(rucDiv);
    }

    /**
     * @return the clienteEntidad
     */
    public String getClienteEntidad() {
        return this.getCliente().getEntidad();
    }

    /**
     * @param clienteEntidad the clienteEntidad to set
     */
    public void setClienteEntidad(String clienteEntidad) {
        this.getCliente().setEntidad(clienteEntidad);
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return getTiempo();
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.setTiempo(fecha);
    }

    public boolean estaVencida() {
        System.out.println("Entities.E_facturaSinPago.estaVencida()");
        switch (getTipoOperacion().getId()) {
            case E_tipoOperacion.CREDITO_30: {
                Calendar c = Calendar.getInstance();
                c.setTime(getTiempo());
                c.add(Calendar.DAY_OF_MONTH, 30);
                Date currentDate = new Date();
                System.out.println("c:"+c.getTimeInMillis());
                System.out.println("currentDate:"+currentDate.getTime());
                if (c.getTime().compareTo(currentDate) > 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
