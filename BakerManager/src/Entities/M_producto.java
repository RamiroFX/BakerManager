/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Entities;

/**
 *
 * @author Ramiro
 */
public class M_producto {
    private String descripcion,marca,categoria,estado,codigo;
    private Integer id,impuesto,precioCosto,precioMinorista,precioMayorista;
    private Integer idMarca;
    private Integer idEstado;
    private Integer idCategoria;
    private Integer idImpuesto;
    private Double cantActual;

    public M_producto() {
    }

    public M_producto(String descripcion, String marca, String rubro, String estado, Integer id, String codigo, Integer impuesto, int precioCosto, Integer precioMinorista, Integer precioMayorista, Double cantActual) {
        this.descripcion = descripcion;
        this.marca = marca;
        this.categoria = rubro;
        this.estado = estado;
        this.id = id;
        this.codigo = codigo;
        this.impuesto = impuesto;
        this.precioCosto = precioCosto;
        this.precioMinorista = precioMinorista;
        this.precioMayorista = precioMayorista;
        this.cantActual = cantActual;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param rubro the categoria to set
     */
    public void setCategoria(String rubro) {
        this.categoria = rubro;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the codBarra
     */
    public String getCodBarra() {
        return codigo;
    }

    /**
     * @param codBarra the codBarra to set
     */
    public void setCodBarra(String codBarra) {
        this.codigo = codBarra;
    }

    /**
     * @return the impuesto
     */
    public Integer getImpuesto() {
        return impuesto;
    }

    /**
     * @param impuesto the impuesto to set
     */
    public void setImpuesto(Integer impuesto) {
        this.impuesto = impuesto;
    }

    /**
     * @return the precioCosto
     */
    public Integer getPrecioCosto() {
        return precioCosto;
    }

    /**
     * @param precioCosto the precioCosto to set
     */
    public void setPrecioCosto(Integer precioCosto) {
        this.precioCosto = precioCosto;
    }

    /**
     * @return the precioVenta
     */
    public Integer getPrecioVenta() {
        return precioMinorista;
    }

    /**
     * @param precioVenta the precioVenta to set
     */
    public void setPrecioVenta(Integer precioVenta) {
        this.precioMinorista = precioVenta;
    }

    /**
     * @return the precioMayorista
     */
    public Integer getPrecioMayorista() {
        return precioMayorista;
    }

    /**
     * @param precioMayorista the precioMayorista to set
     */
    public void setPrecioMayorista(Integer precioMayorista) {
        this.precioMayorista = precioMayorista;
    }

    /**
     * @return the cantActual
     */
    public Double getCantActual() {
        return cantActual;
    }

    /**
     * @param cantActual the cantActual to set
     */
    public void setCantActual(Double cantActual) {
        this.cantActual = cantActual;
    }

    /**
     * @return the idMarca
     */
    public Integer getIdMarca() {
        return idMarca;
    }

    /**
     * @param idMarca the idMarca to set
     */
    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    /**
     * @return the idEstado
     */
    public Integer getIdEstado() {
        return idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return the idCategoria
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * @param idCategoria the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * @return the idImpuesto
     */
    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    /**
     * @param idImpuesto the idImpuesto to set
     */
    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

}
