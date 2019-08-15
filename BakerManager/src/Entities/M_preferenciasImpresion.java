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
    private String letterFont;
    private Divisa divisa;

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

    public Divisa getDivisa() {
        return divisa;
    }

    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    @Override
    public String toString() {
        return "LetterFont:" + getLetterFont()
                + " DistanceBetweenCopies " + getDistanceBetweenCopies()
                + " getLetterSize " + getLetterSize()
                + " getMaxProducts " + getMaxProducts()
                + " Divisa " + getDivisa()
                + " ImprimirMoneda " + getImprimirMoneda()
                + " Triplicado " + getIdTriplicado()
                + " Duplicado" + getIdDuplicado();
    }
}
