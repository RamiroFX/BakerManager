/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearRollo;

import Entities.Estado;
import Entities.E_productoClasificacion;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearFilm extends JDialog {

    protected static final String TITLE = "Crear Film", WINDOWS_NAME = "jdCrearFilm";

    private JLabel jlProducto;
    public JTextField jtfProducto;
    private JLabel jlIdFilm, jlNroFilm, jlResponsable, jlMedida, jlMicron,
            jlTipoMateriaPrima, jlPeso, jlCono, jlEstadoFilm;//jlFechaCreacion;
    public JTextField jtfIdFilm, jtfNroFilm, jtfMedida,
            jtfResponsable, jtfMicron, jtfCono, jtfPeso;//jtfFechaCreacion
    public JComboBox<E_productoClasificacion> jcbTipoMateriaPrima;
    public JComboBox<Estado> jcbEstadoFilm;
    public JButton jbAceptar, jbCancelar;
    private JPanel jpPrincipal, jpBotones;

    public V_crearFilm(JFrame mainFrame) {
        super(mainFrame, TITLE, DEFAULT_MODALITY_TYPE);
        setName(WINDOWS_NAME);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpPrincipal, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    public V_crearFilm(JDialog mainFrame) {
        super(mainFrame, TITLE, DEFAULT_MODALITY_TYPE);
        setName(WINDOWS_NAME);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeComponents();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpPrincipal, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initializeComponents() {
        jpPrincipal = new JPanel(new GridLayout(10, 2));
        //Labels
        jlProducto = new JLabel("Producto");
        jlProducto.setHorizontalAlignment(JLabel.CENTER);
        jlIdFilm = new JLabel("ID Film");
        jlIdFilm.setHorizontalAlignment(JLabel.CENTER);
        jlNroFilm = new JLabel("Nro. Film");
        jlNroFilm.setHorizontalAlignment(JLabel.CENTER);
        /*jlFechaCreacion = new JLabel("Fecha de creación");
        jlFechaCreacion.setHorizontalAlignment(JLabel.CENTER);*/
        jlResponsable = new JLabel("Responsable");
        jlResponsable.setHorizontalAlignment(JLabel.CENTER);
        jlMedida = new JLabel("Medida");
        jlMedida.setHorizontalAlignment(JLabel.CENTER);
        jlMicron = new JLabel("Micron");
        jlMicron.setHorizontalAlignment(JLabel.CENTER);
        jlTipoMateriaPrima = new JLabel("Tipo de materia prima");
        jlTipoMateriaPrima.setHorizontalAlignment(JLabel.CENTER);
        jlPeso = new JLabel("Peso");
        jlPeso.setHorizontalAlignment(JLabel.CENTER);
        jlCono = new JLabel("Cono");
        jlCono.setHorizontalAlignment(JLabel.CENTER);
        jlEstadoFilm = new JLabel("Estado de Film");
        jlEstadoFilm.setHorizontalAlignment(JLabel.CENTER);
        //TextFields
        jtfProducto = new JTextField();
        jtfProducto.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        jtfProducto.setEditable(false);
        jtfIdFilm = new JTextField();
        jtfIdFilm.setEditable(false);
        jtfNroFilm = new JTextField();
        /*jtfFechaCreacion = new JTextField();
        jtfFechaCreacion.setEditable(false);*/
        jtfResponsable = new JTextField();
        jtfResponsable.setEditable(false);
        jtfMedida = new JTextField();
        jtfMicron = new JTextField();
        jtfCono = new JTextField();
        jtfPeso = new JTextField();

        //comboboxes
        jcbTipoMateriaPrima = new JComboBox();
        jcbEstadoFilm = new JComboBox();
        jcbEstadoFilm.setEnabled(false);

        jpPrincipal.add(jlProducto);
        jpPrincipal.add(jtfProducto);

        /*jpPrincipal.add(jlIdFilm);
        jpPrincipal.add(jtfIdFilm);*/
        jpPrincipal.add(jlNroFilm);
        jpPrincipal.add(jtfNroFilm);

        /*jpPrincipal.add(jlFechaCreacion);
        jpPrincipal.add(jtfFechaCreacion);*/

        /*jpPrincipal.add(jlResponsable);
        jpPrincipal.add(jtfResponsable);*/
        jpPrincipal.add(jlMedida);
        jpPrincipal.add(jtfMedida);

        jpPrincipal.add(jlMicron);
        jpPrincipal.add(jtfMicron);

        jpPrincipal.add(jlTipoMateriaPrima);
        jpPrincipal.add(jcbTipoMateriaPrima);

        jpPrincipal.add(jlPeso);
        jpPrincipal.add(jtfPeso);

        jpPrincipal.add(jlCono);
        jpPrincipal.add(jtfCono);

        jpPrincipal.add(jlEstadoFilm);
        jpPrincipal.add(jcbEstadoFilm);

        /*
        SOUTH PANEL
         */
        jpBotones = new JPanel();
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
    }
}
