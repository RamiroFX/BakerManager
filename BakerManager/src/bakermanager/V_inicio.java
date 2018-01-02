/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

/**
 * Para ello iniciamos con crear la ventana (JFrame) encargado de contener todos
 * los componentes que se convertirán en parte de la aplicación, de este modo a
 * nuestra clase la heredamos de JFrame, a parte de ello definimos propiedades
 * que son útiles para el buen funcionamiento de la misma como: - Tamaño
 * setSize(ancho,alto) - Posición setLocation(x,y) - Operacion a realizarse
 * cuando se presione la X de cerrar setDefaultCloseOperation(int operacion)
 *
 * A este punto deberemos tener una aplicación que arranque con una ventana y de
 * acuerdo con nuestras propiedades definidas si tal es el caso de que hayamos
 * puesto EXIT_ON_CLOSE como operacion por defecto al cerrar, finalizaría la
 * aplicación.
 *
 * Posteriormente a esto debemos agregar un menú, una barra de herramientas, un
 * JDesktopPane que será el encargado de contener a las ventanas internas
 * JInternalFrame.
 */
import MenuPrincipal.V_BarraMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class V_inicio extends JFrame {

    private JDesktopPane desktop = null;
    private JInternalFrame currentJIF = null;
    private JToolBar jtbBarraHerramientas = null;
    private JTextField jtfUsuario = null;
    private ImageIcon icono;
    private V_BarraMenu barraMenu;

    /**
     * Constructor que se encarga de inicializar la aplicación definiendo los
     * parametros iniciales
     */
    public V_inicio() {
        super("Baker Manager");
        setName("jfMainFrame");
        setLocation(0, 0);
        setExtendedState(MAXIMIZED_BOTH);
        try {
            String path = System.getProperty("user.dir") + "\\Assets\\Icono.png";
            icono = new ImageIcon(path);
        } catch (Exception e) {
            icono = new ImageIcon();
        }
        barraMenu = new V_BarraMenu(this);
        setJMenuBar(barraMenu);
        setIconImage(icono.getImage());
        getDesktop().setLayout(null);
        getDesktop().setBackground(Color.WHITE);
        getContentPane().setName("myContenPane");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getDesktop(), BorderLayout.CENTER);
        getContentPane().add(getJtbBarraHerramientas(), BorderLayout.SOUTH);
    }

    /**
     * @return the desktop
     */
    public JDesktopPane getDesktop() {
        if (desktop == null) {
            desktop = new JDesktopPane();
            desktop.setName("PanelDesktop");
            desktop.addContainerListener(new ContainerAdapter() {
                //remover despues los contadores, solo para debugging.
                int a, b;

                @Override
                public void componentAdded(ContainerEvent e) {
                    a++;
                    System.err.println("desktoPane: Agregando ventana " + a + " :" + e.paramString());
                }

                @Override
                public void componentRemoved(ContainerEvent e) {
                    b++;
                    System.err.println("desktoPane: quitando ventana " + b + " :" + e.paramString());
                }
            });
        }
        return desktop;
    }

    /**
     * @return the jtbBarraHerramientas
     */
    public JToolBar getJtbBarraHerramientas() {
        if (jtbBarraHerramientas == null) {
            jtbBarraHerramientas = new JToolBar("Barra de Herramientas", 0);
            jtbBarraHerramientas.setPreferredSize(new Dimension(this.getWidth(), 25));
            jtbBarraHerramientas.setFloatable(false);
            jtbBarraHerramientas.add(getJtfUsuario());
        }
        return jtbBarraHerramientas;
    }

    /**
     * @return the jtfUsuario
     */
    public JTextField getJtfUsuario() {
        if (jtfUsuario == null) {
            jtfUsuario = new JTextField("Conectado como: ....");
            jtfUsuario.setEditable(false);
        }
        return jtfUsuario;
    }

    /**
     * @param jtfUsuario the jtfUsuario to set
     */
    public void setJtfUsuario(String jtfUsuario) {
        this.jtfUsuario.setText("Conectado como: " + jtfUsuario);
    }

    /**
     * @return the currentJIF
     */
    public JInternalFrame getCurrentJIF() {
        return currentJIF;
    }

    /**
     * @param currentJIF the currentJIF to set
     */
    public void setCurrentJIF(JInternalFrame currentJIF) {
        this.currentJIF = currentJIF;
    }

    public V_BarraMenu getJMenuBar() {
        return this.barraMenu;
    }
}
