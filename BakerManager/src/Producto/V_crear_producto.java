/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crear_producto extends JDialog {

    private JLabel jlProducto, jlCodigo, jlImpuesto, jlRubro, jlPrecioCosto, jlMarca,
            jlPrecioVta, jlPrecioMayorista;
    public JTextField jtfProducto, jtfCodigo, jtfPrecioCosto,
            jtfPrecioVta, jtfPrecioMayorista, jtfProveedor;
    public JTextArea jtaIngredientes;
    public JComboBox jcbImpuesto, jcbMarca, jcbCategoria;
    public JButton jbAceptar, jbCancelar, jbProveedor, jbCopiar;
    private JPanel jpPrincipal, jpBotones;
    JCheckBox jckBProveedor;

    public V_crear_producto(JFrame mainFrame) {
        super(mainFrame, "Agregar Producto", DEFAULT_MODALITY_TYPE);
        setName("jdCrearProducto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getJpPrincipal(), "Center");
        getContentPane().add(getJpBotones(), "South");
    }

    public V_crear_producto(JDialog mainFrame) {
        super(mainFrame, "Agregar Producto", DEFAULT_MODALITY_TYPE);
        setName("jdCrearProducto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getJpPrincipal(), "Center");
        getContentPane().add(getJpBotones(), "South");
    }

    private JPanel getJpPrincipal() {
        if (jpPrincipal == null) {
            jpPrincipal = new JPanel(new GridLayout(10, 2));

            //Labels
            jlProducto = new JLabel("Producto");
            jlProducto.setHorizontalAlignment(JLabel.CENTER);
            jlCodigo = new JLabel("Codigo");
            jlCodigo.setHorizontalAlignment(JLabel.CENTER);
            jlImpuesto = new JLabel("Impuesto");
            jlImpuesto.setHorizontalAlignment(JLabel.CENTER);
            jlRubro = new JLabel("Rubro");
            jlRubro.setHorizontalAlignment(JLabel.CENTER);
            jlPrecioCosto = new JLabel("Precio Costo");
            jlPrecioCosto.setHorizontalAlignment(JLabel.CENTER);
            jlMarca = new JLabel("Marca");
            jlMarca.setHorizontalAlignment(JLabel.CENTER);
            jlPrecioVta = new JLabel("Precio Venta");
            jlPrecioVta.setHorizontalAlignment(JLabel.CENTER);
            jlPrecioMayorista = new JLabel("Prec. May");
            jlPrecioMayorista.setHorizontalAlignment(JLabel.CENTER);
            jbProveedor = new JButton("Proveedor");
            jbProveedor.setEnabled(false);
            jckBProveedor = new JCheckBox();
            jckBProveedor.setSelected(false);
            JPanel jpProv = new JPanel();
            jpProv.add(jbProveedor);
            jpProv.add(jckBProveedor);
            //TextFields
            jtfProducto = new JTextField();
            jtfCodigo = new JTextField();
            jtfPrecioVta = new JTextField("1");
            jtfPrecioMayorista = new JTextField("1");
            jtfPrecioCosto = new JTextField("1");

            //comboboxes
            jcbImpuesto = new JComboBox();
            jcbCategoria = new JComboBox();
            jcbMarca = new JComboBox();
            jtfProveedor = new JTextField();
            jtfProveedor.setEditable(false);

            jtaIngredientes = new JTextArea();

            jpPrincipal.add(jlProducto);
            jpPrincipal.add(jtfProducto);
            jpPrincipal.add(jlCodigo);
            jpPrincipal.add(jtfCodigo);
            jpPrincipal.add(jlImpuesto);
            jpPrincipal.add(jcbImpuesto);
            jpPrincipal.add(jlRubro);
            jpPrincipal.add(jcbCategoria);
            jpPrincipal.add(jlMarca);
            jpPrincipal.add(jcbMarca);
            jpPrincipal.add(jlPrecioCosto);
            jpPrincipal.add(jtfPrecioCosto);
            jpPrincipal.add(jlPrecioVta);
            jpPrincipal.add(jtfPrecioVta);
            jpPrincipal.add(jlPrecioMayorista);
            jpPrincipal.add(jtfPrecioMayorista);
            jpPrincipal.add(jpProv);
            jpPrincipal.add(jtfProveedor);
        }
        return jpPrincipal;
    }

    private JPanel getJpBotones() {
        if (jpBotones == null) {
            jpBotones = new JPanel();
            jbAceptar = new JButton("Aceptar");
            jbCancelar = new JButton("Cancelar");
            jbCopiar = new JButton("Copiar de otro");
            jpBotones.add(jbAceptar);
            jpBotones.add(jbCancelar);
            jpBotones.add(jbCopiar);
        }
        return jpBotones;
    }
}
