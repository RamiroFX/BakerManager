/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.E_produccionFilm;

/**
 *
 * @author Ramiro Ferreira
 */
public interface InterfaceRecibirProduccionFilm {

    public void recibirFilm(E_produccionFilm detalle);

    public void modificarFilm(int index, E_produccionFilm detalle);

    /*
    Se utiliza cuando se crea un producto terminado antes de la carga de rollos
    */
    public void recibirFilmPosterior(E_produccionFilm detalle);

}
