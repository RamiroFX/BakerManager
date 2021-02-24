/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Cliente;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Timbrado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import ModeloTabla.FacturaDetalleTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_verIngreso {

    private int idEgresoCabecera;
    private int nroFactura;
    private int idTimbrado;
    private M_facturaCabecera faca;
    private M_cliente cliente;
    private FacturaDetalleTableModel fadeTM;
    private NumberFormat nfSmall;
    private NumberFormat nfLarge;

    public M_verIngreso() {
        this.faca = new M_facturaCabecera();
        this.cliente = new M_cliente();
        fadeTM = new FacturaDetalleTableModel();
        this.nfSmall = new DecimalFormat("000");
        this.nfLarge = new DecimalFormat("0000000");
    }

    public void establecerModeloTabla() {
        fadeTM.setFacturaDetalleList(DB_Ingreso.obtenerVentaDetalles(faca.getIdFacturaCabecera()));
    }

    public void establecerVentaPorNroFactura() {
        this.setFaca(DB_Ingreso.obtenerIngresoCabeceraNroFactura(getNroFactura(), getIdTimbrado()));
        setCliente(DB_Cliente.obtenerDatosClienteID(getFaca().getIdCliente()));
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(getFaca().getIdFuncionario());
        int idTimbradoAux = getFaca().getIdTimbrado();
        if (idTimbradoAux > 0) {
            getFaca().setTimbrado(DB_Timbrado.obtenerTimbrado(idTimbradoAux));
        }
        getFaca().setCliente(getCliente());
        getFaca().setFuncionario(funcionario);
    }

    public void establecerVentaPorID() {
        this.setFaca(DB_Ingreso.obtenerIngresoCabeceraID(getIdIngresoCabecera()));
        setCliente(DB_Cliente.obtenerDatosClienteID(getFaca().getIdCliente()));
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(getFaca().getIdFuncionario());
        M_funcionario vendedor = DB_Funcionario.obtenerDatosFuncionarioID(getFaca().getVendedor().getIdFuncionario());
        int idTimbradoAux = getFaca().getIdTimbrado();
        if (idTimbradoAux > 0) {
            getFaca().setTimbrado(DB_Timbrado.obtenerTimbrado(idTimbradoAux));
        }
        getFaca().setCliente(getCliente());
        getFaca().setFuncionario(funcionario);
        getFaca().setVendedor(vendedor);
    }

    public String obtenerNroFacturaCompleto() {
        String value = "";
        if (getFaca().getTimbrado() != null && getFaca().getTimbrado().getId() > 0) {
            String nroTimbrado = getNfLarge().format(this.getFaca().getTimbrado().getNroTimbrado());
            String nroSucursal = getNfSmall().format(this.getFaca().getTimbrado().getNroSucursal());
            String nroPVTA = getNfSmall().format(this.getFaca().getTimbrado().getNroPuntoVenta());
            String nroFacturaString = getNfLarge().format(this.getFaca().getNroFactura());
            value = nroTimbrado + "-" + nroSucursal + "-" + nroPVTA + "-" + nroFacturaString;
            return value;
        } else {
            return getNfLarge().format(this.getFaca().getNroFactura());
        }
    }

    /**
     * @return the idEgresoCabecera
     */
    public int getIdIngresoCabecera() {
        return idEgresoCabecera;
    }

    /**
     * @param idEgresoCabecera the idEgresoCabecera to set
     */
    public void setIdEgresoCabecera(int idEgresoCabecera) {
        this.idEgresoCabecera = idEgresoCabecera;
    }

    /**
     * @return the nroFactura
     */
    public int getNroFactura() {
        return nroFactura;
    }

    /**
     * @param nroFactura the nroFactura to set
     */
    public void setNroFactura(int nroFactura) {
        this.nroFactura = nroFactura;
    }

    /**
     * @return the idTimbrado
     */
    public int getIdTimbrado() {
        return idTimbrado;
    }

    /**
     * @param idTimbrado the idTimbrado to set
     */
    public void setIdTimbrado(int idTimbrado) {
        this.idTimbrado = idTimbrado;
    }

    /**
     * @return the faca
     */
    public M_facturaCabecera getFaca() {
        return faca;
    }

    /**
     * @param faca the faca to set
     */
    public void setFaca(M_facturaCabecera faca) {
        this.faca = faca;
    }

    /**
     * @return the cliente
     */
    public M_cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the fadeTM
     */
    public FacturaDetalleTableModel getFadeTM() {
        return fadeTM;
    }

    /**
     * @param fadeTM the fadeTM to set
     */
    public void setFadeTM(FacturaDetalleTableModel fadeTM) {
        this.fadeTM = fadeTM;
    }

    /**
     * @return the nfSmall
     */
    public NumberFormat getNfSmall() {
        return nfSmall;
    }

    /**
     * @param nfSmall the nfSmall to set
     */
    public void setNfSmall(NumberFormat nfSmall) {
        this.nfSmall = nfSmall;
    }

    /**
     * @return the nfLarge
     */
    public NumberFormat getNfLarge() {
        return nfLarge;
    }

    /**
     * @param nfLarge the nfLarge to set
     */
    public void setNfLarge(NumberFormat nfLarge) {
        this.nfLarge = nfLarge;
    }
}
