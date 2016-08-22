/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 *
 * @author Ramiro Ferreira
 */
public interface Gestion extends ActionListener, MouseListener, KeyListener {

    void inicializarVista();
    void concederPermisos();
    void mostrarVista();
    void cerrar();
}
