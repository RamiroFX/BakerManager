/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Ramiro Ferreira
 */
public class Elegir_persona extends JDialog implements ActionListener {

    JButton jbPersonaExistente, jbPersonaNueva;
    JLabel jlTipoPersona;
    public Elegir_persona(Window owner) {
        super(owner);
    }

    private void inicializarVista(){
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
