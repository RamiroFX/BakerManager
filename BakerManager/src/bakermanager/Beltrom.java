/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author Ramiro
 */
public class Beltrom {
    
    private static UIManager.LookAndFeelInfo apariencias[] = UIManager.getInstalledLookAndFeels();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(apariencias[1].getClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Beltrom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Beltrom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Beltrom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Beltrom.class.getName()).log(Level.SEVERE, null, ex);
        }
        Inicio inicio = new Inicio();
        inicio.mostrarLogin();
    }
}
