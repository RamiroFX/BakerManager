
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Login;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */

/**
 * Esta ventana es la encargada de gestionar el ingreso de los usuarios al siste-
 * ma.
 */
public class V_jifSeleccionarRol extends JInternalFrame{
    public JTextField txtNombre; //Campo de nombre
    public JPasswordField txtPassword; //Campo de password
    public JButton jbAceptar, jbSalir; //botones salir y aceptar
    public JComboBox jcbRol;
    private  JPanel jpTop,jpBot; //paneles

    public V_jifSeleccionarRol(){
        super("Rol", false,true,false);
        setName("Login");
        setName("SeleccionRol");
        getContentPane().setLayout(new GridLayout(2,1));
        getContentPane().add(getTop());
        getContentPane().add(getBot());
        setName("Login");
    }
    private JPanel getTop(){
        if(jpTop==null){
            jpTop = new JPanel(new GridLayout(1,2));
            jpTop.add(new JLabel("Rol", SwingConstants.CENTER));
            jcbRol = new JComboBox();
            jpTop.add(jcbRol);
        }
        return jpTop;
    }
    private JPanel getBot(){
        if(jpBot==null){
            jpBot= new JPanel();
            jbAceptar = new JButton("Aceptar");
            jbSalir = new JButton(" Salir ");
            jpBot.add(jbAceptar);
            jpBot.add(jbSalir);
        }
        return jpBot;
    }

    public void cerrar(){
        dispose();
    }


    //se sobrecarga este metodo para que el panel de Login mantenga
    //su esquema de dimension(0.3,0.25)
   /* @Override
    public void setSize(Dimension d) {
        super.setSize(c_ventanaTamaño.centrarPantalla(this));
    }*/

 
}