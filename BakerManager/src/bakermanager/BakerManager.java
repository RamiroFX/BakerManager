/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import DB_manager.DB_manager;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ramiro
 */
public class BakerManager {

    private static UIManager.LookAndFeelInfo apariencias[] = UIManager.getInstalledLookAndFeels();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(apariencias[1].getClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BakerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(BakerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BakerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(BakerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date today = Calendar.getInstance().getTime();
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.DAY_OF_MONTH, 19);
        calendario.set(Calendar.MONTH, 7);//AGOSTO
        calendario.set(Calendar.YEAR, 2016);
        if (today.before(calendario.getTime())) {
            Calendar temp = Calendar.getInstance();
            int diaActual = temp.get(Calendar.DAY_OF_MONTH);
            int diaLimite = calendario.get(Calendar.DAY_OF_MONTH);
            int diferencia = diaLimite - diaActual;
            String mensaje = "Le quedan " + diferencia + " día/s de prueba.";
            JOptionPane.showMessageDialog(null, mensaje, "Baker Manager", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(mensaje);
            Inicio inicio = new Inicio();
            inicio.mostrarLogin();
        } else {
            JOptionPane.showMessageDialog(null, "Ah expirado su tiempo de prueba. Contacte con soporte tecnico", "Baker Manager", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
