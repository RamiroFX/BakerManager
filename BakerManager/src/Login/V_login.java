/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Ramiro Ferreira
 */
/**
 * Esta ventana es la encargada de gestionar el ingreso de los usuarios al
 * siste- ma.
 */
public class V_login extends JInternalFrame {

    public JTextField txtNombre; //Campo de nombre
    public JPasswordField txtPassword; //Campo de password
    public JButton btnAceptar, btnSalir; //botones salir y aceptar
    private JPanel jpLogin, pnIzq, pnDer, jpBotones; //paneles
    private JTabbedPane jtpPaneles;
    public JFrame frame; //referencia a la ventana principal
    public String Conexion;

    public V_login(JFrame jframe) {
        super("Identificacion", true, true, false);
        setName("Login");
        this.frame = jframe;
        inicializarVista();
        getContentPane().setName("myContentPane");
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(jpLogin);
    }

    public Point centrarPantalla() {
        /*con este codigo centramos el panel en el centro del contenedor
         la anchura del contenedor menos la anchura de nuestro componente divido a 2
         lo mismo con la altura. Obs. si es que tenemos un jMenuBar sumamos 25 (la altura
         de nuestra barra de menu) a nuestra altura.*/
        return new Point((this.frame.getWidth() - this.getWidth()) / 2, (this.frame.getHeight() - this.getHeight() - 45) / 2);
    }

    public Dimension establecerTamañoPanel() {
        return new Dimension((int) (this.frame.getWidth() * 0.3), (int) (this.frame.getHeight() * 0.30));
    }

    //se sobrecarga este metodo para que el panel de Login mantenga
    //su esquema de dimension(0.3,0.25)
    @Override
    public void setSize(Dimension d) {
        super.setSize(establecerTamañoPanel());
    }

    /**
     * @return the jtpPaneles
     */
    public JTabbedPane getJtpPaneles() {
        return jtpPaneles;
    }

    /**
     * @param jtpPaneles the jtpPaneles to set
     */
    public void setJtpPaneles(JTabbedPane jtpPaneles) {
        this.jtpPaneles = jtpPaneles;
    }

    private void inicializarVista() {
        pnIzq = new JPanel();
        pnIzq.setLayout(new GridLayout(3, 1));
        pnIzq.add(new JLabel("Nombre", SwingConstants.CENTER));
        pnIzq.add(new JLabel("Password", SwingConstants.CENTER));
        pnDer = new JPanel();
        pnDer.setLayout(new GridLayout(3, 1));
        txtNombre = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtPassword.setEchoChar('*');
        //txtNombre.addKeyListener(c);
        jpBotones = new JPanel(new GridLayout(1, 1));
        btnAceptar = new JButton("Aceptar");
        btnSalir = new JButton(" Salir ");
        jpBotones.add(btnAceptar);
        jpBotones.add(btnSalir);
        pnDer.add(txtNombre);
        pnDer.add(txtPassword);
        pnDer.add(jpBotones);
        jpLogin = new JPanel(new GridLayout(1, 1));
        jpLogin.add(pnIzq);
        jpLogin.add(pnDer);
    }
}
/*class MainWindowListener extends InternalFrameAdapter{
 v_jifMasterLogin jif=null;

 public MainWindowListener(v_jifMasterLogin jif) {
 this.jif=jif;
 }

 @Override
 public void internalFrameClosing( javax.swing.event.InternalFrameEvent e){
 System.err.println("cerrando login jifLogin.java linea:124");
 jif.dispose();
 jif.frame.setLoginActivo(false);
 }
 }*/
