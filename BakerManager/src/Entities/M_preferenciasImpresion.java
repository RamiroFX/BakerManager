/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro
 */
public class M_preferenciasImpresion {

    private int id;
    private int letterSize;
    private int maxProducts;
    private int distanceBetweenCopies;
    private int idDuplicado;
    private int idTriplicado;
    private int imprimirMoneda;
    private int anchoPagina;
    private int largoPagina;
    private double margenX;
    private double margenY;
    private String letterFont;
    private String formatoFecha;
    private String nombreImpresora;
    private E_Divisa divisa;
    private E_impresionOrientacion orientacion;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the letterSize
     */
    public int getLetterSize() {
        return letterSize;
    }

    /**
     * @param letterSize the letterSize to set
     */
    public void setLetterSize(int letterSize) {
        this.letterSize = letterSize;
    }

    /**
     * @return the maxProducts
     */
    public int getMaxProducts() {
        return maxProducts;
    }

    /**
     * @param maxProducts the maxProducts to set
     */
    public void setMaxProducts(int maxProducts) {
        this.maxProducts = maxProducts;
    }

    /**
     * @return the distanceBetweenCopies
     */
    public int getDistanceBetweenCopies() {
        return distanceBetweenCopies;
    }

    /**
     * @param distanceBetweenCopies the distanceBetweenCopies to set
     */
    public void setDistanceBetweenCopies(int distanceBetweenCopies) {
        this.distanceBetweenCopies = distanceBetweenCopies;
    }

    /**
     * @return the idDuplicado
     */
    public int getIdDuplicado() {
        return idDuplicado;
    }

    /**
     * @param idDuplicado the idDuplicado to set
     */
    public void setIdDuplicado(int idDuplicado) {
        this.idDuplicado = idDuplicado;
    }

    /**
     * @return the idTriplicado
     */
    public int getIdTriplicado() {
        return idTriplicado;
    }

    /**
     * @param idTriplicado the idTriplicado to set
     */
    public void setIdTriplicado(int idTriplicado) {
        this.idTriplicado = idTriplicado;
    }

    /**
     * @return the imprimirMoneda
     */
    public int getImprimirMoneda() {
        return imprimirMoneda;
    }

    /**
     * @param imprimirMoneda the imprimirMoneda to set
     */
    public void setImprimirMoneda(int imprimirMoneda) {
        this.imprimirMoneda = imprimirMoneda;
    }

    /**
     * @return the letterFont
     */
    public String getLetterFont() {
        return letterFont;
    }

    /**
     * @param letterFont the letterFont to set
     */
    public void setLetterFont(String letterFont) {
        this.letterFont = letterFont;
    }

    public E_Divisa getDivisa() {
        return divisa;
    }

    public void setDivisa(E_Divisa divisa) {
        this.divisa = divisa;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    @Override
    public String toString() {
        return "LetterFont:" + getLetterFont()
                + " DistanceBetweenCopies " + getDistanceBetweenCopies()
                + " getLetterSize " + getLetterSize()
                + " getFormatoFecha " + getFormatoFecha()
                + " getMaxProducts " + getMaxProducts()
                + " Divisa " + getDivisa()
                + " ImprimirMoneda " + getImprimirMoneda()
                + " Orientacion " + getOrientacion().getDescripcion()
                + " Triplicado " + getIdTriplicado()
                + " Duplicado" + getIdDuplicado();
    }

    public int getAnchoPagina() {
        return anchoPagina;
    }

    public void setAnchoPagina(int anchoPagina) {
        this.anchoPagina = anchoPagina;
    }

    public int getLargoPagina() {
        return largoPagina;
    }

    public void setLargoPagina(int largoPagina) {
        this.largoPagina = largoPagina;
    }

    public double getMargenX() {
        return margenX;
    }

    public void setMargenX(double margenX) {
        this.margenX = margenX;
    }

    public double getMargenY() {
        return margenY;
    }

    public void setMargenY(double margenY) {
        this.margenY = margenY;
    }

    public String getNombreImpresora() {
        return nombreImpresora;
    }

    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }

    public E_impresionOrientacion getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(E_impresionOrientacion orientacion) {
        this.orientacion = orientacion;
    }
}
