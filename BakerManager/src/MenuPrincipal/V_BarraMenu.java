package MenuPrincipal;

import bakermanager.V_inicio;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_BarraMenu extends JMenuBar {

    public JMenu jmInicio, jmAyuda, jmHerramientas, jmOpciones, jmReportes, jmiCobro;
    public JMenuItem jmiCerrar, jmiAcerca, jmiPersonalizar, jmiConfigImpresion,
            jmiConfigImpresionTicket, jmiConfigImpresionBoleta, jmiEmpresa,
            jmiProduccion, jmiMateriaPrima, jmiGestionTimbrado,
            jmiCobroPendiente, jmiCobroPendienteVencidos, jmiCobroPendientePorCliente,
            jmiCobroPendientePorClienteVencidos;
    //public JMenuItem  jmiLogIn,jmiLogOut, jmiMenuPrincipal;
    private V_inicio frame; //referencia al frame principal.
    //private boolean loginActivo=true;

    public V_BarraMenu(V_inicio frame) {
        this.frame = frame;
        inicializarVista();
        this.add(jmInicio);
        this.add(jmHerramientas);
        this.add(jmOpciones);
        this.add(jmReportes);
        this.add(jmAyuda);
    }

    private void inicializarVista() {
        inicializarInicio();
        inicializarHerramientas();
        inicializarOpciones();
        inicializarAyuda();
        inicializarReportes();
    }

    private void inicializarInicio() {
        jmInicio = new JMenu("Inicio");
        /*jmiMenuPrincipal = new JMenuItem("Menu principal");
            jmiMenuPrincipal.setEnabled(false);
            jmiLogIn = new JMenuItem("LogIn");
            jmiLogIn.setEnabled(false);
            jmiLogOut= new JMenuItem("LogOut");
            jmiLogOut.setEnabled(false);*/
        jmiCerrar = new JMenuItem("Cerrar");
        /*jmInicio.add(jmiMenuPrincipal);
            jmInicio.add(jmiLogOut);
            jmInicio.add(jmiLogIn);*/
        jmInicio.add(jmiCerrar);
    }

    private void inicializarHerramientas() {
        jmHerramientas = new JMenu("Herramientas");
        jmiPersonalizar = new JMenuItem("Personalizar");
        jmiPersonalizar.setEnabled(false);
        jmiPersonalizar.setName("configuracion personalizar");
        jmiConfigImpresion = new JMenuItem("Config. factura");
        jmiConfigImpresion.setEnabled(false);
        jmiConfigImpresion.setName("configuracion factura");
        jmiGestionTimbrado = new JMenuItem("Gestión de timbrado");
        //jmiGestionTimbrado.setEnabled(false);
        jmiGestionTimbrado.setName("gestion timbrado");
        jmiConfigImpresionTicket = new JMenuItem("Config. ticket");
        jmiConfigImpresionTicket.setEnabled(false);
        jmiConfigImpresionTicket.setName("configuracion ticket");
        jmiConfigImpresionBoleta = new JMenuItem("Config. boleta");
        jmiConfigImpresionBoleta.setEnabled(false);
        jmiConfigImpresionBoleta.setName("configuracion boleta");
        jmHerramientas.add(jmiPersonalizar);
        jmHerramientas.add(jmiConfigImpresion);
        jmHerramientas.add(jmiConfigImpresionTicket);
        jmHerramientas.add(jmiConfigImpresionBoleta);
        jmHerramientas.add(jmiGestionTimbrado);
    }

    private void inicializarAyuda() {
        jmAyuda = new JMenu("Ayuda");
        jmiAcerca = new JMenuItem("Acerca de..");
        jmAyuda.add(jmiAcerca);
    }

    private void inicializarOpciones() {
        jmOpciones = new JMenu("Opciones");
        jmiEmpresa = new JMenuItem("Empresa");
        //TODO remove
        jmiProduccion = new JMenuItem("Produccion");
        jmiProduccion.setName("Gestión producción");
        jmiMateriaPrima = new JMenuItem("Materia Prima");
        jmiMateriaPrima.setName("Gestión uso materia prima");
        jmiEmpresa.setEnabled(false);
        jmiEmpresa.setName("configuracion empresa");
        jmOpciones.add(jmiEmpresa);
        jmOpciones.add(jmiProduccion);
        jmOpciones.add(jmiMateriaPrima);
    }

    private void inicializarAcercade() {
        final JDialog acercade = new JDialog(frame, "Acerca de...", ModalityType.APPLICATION_MODAL);
        ImageIcon icon = new ImageIcon(getClass().getResource("/recursos/LogoPanaderia.png"));
        JLabel label1 = new JLabel(icon);
        JLabel label2 = new JLabel(" Sistema elaborado por Ramiro Ferreira");
        JLabel label3 = new JLabel(" Universidad Columbia del Paraguay.");
        JLabel label4 = new JLabel(" Ingeniería en Informática.");
        JButton close = new JButton("Cerrar");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                acercade.dispose();
                System.runFinalization();
            }
        });
        close.setBounds(180, 80, 40, 20);
        close.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
        label1.setBounds(0, 0, 120, 120);
        label2.setBounds(125, 20, 340, 25);
        label3.setBounds(125, 35, 340, 25);
        label4.setBounds(125, 50, 340, 25);
        acercade.add(label1);
        acercade.add(label2);
        acercade.add(label3);
        acercade.add(label4);
        acercade.add(close);
        acercade.setLayout(null);
        acercade.setSize(new Dimension(350, 140));
        acercade.setLocation(100, 50);
        acercade.setLocationRelativeTo(frame);
        acercade.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        acercade.setVisible(true);
    }

    private void inicializarReportes() {
        jmReportes = new JMenu("Reportes");
        jmiCobro = new JMenu("Cobros");
        jmiCobroPendiente = new JMenuItem("Cobros pendientes");
        jmiCobroPendienteVencidos = new JMenuItem("Cobros pendientes vencidos(30Días)");
        jmiCobroPendientePorCliente = new JMenuItem("Cobros pendientes por Cliente");
        jmiCobroPendientePorClienteVencidos = new JMenuItem("Cobros pendientes por Cliente vencidos(30Días)");
        jmReportes.add(jmiCobro);
        jmiCobro.add(jmiCobroPendiente);
        jmiCobro.add(jmiCobroPendienteVencidos);
        jmiCobro.addSeparator();
        jmiCobro.add(jmiCobroPendientePorCliente);
        jmiCobro.add(jmiCobroPendientePorClienteVencidos);
    }

}
