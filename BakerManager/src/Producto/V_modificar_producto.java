/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import bakermanager.C_inicio;
import bakermanager.V_inicio;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Administrador
 */
public class V_modificar_producto extends javax.swing.JDialog {

    private JPanel jpMid, jpMid1, jpMid3,
            jpPrincipal, jpBot;
    public JLabel jlProducto, jlCodigo, jlImpuesto, jlRubro, jlPrecioCosto, jlMarca,
            jlPrecioVta, jlPrecioMayorista, jlSuspendido, jlCalcularStock,
            jlTituloProducto, jlCantActual;
    public JTextField jtfProducto, jtfCodigo, jtfPrecioCosto,
            jtfPrecioVta, jtfPrecioMayorista, jtfCantActual;
    public JTextArea jtaIngredientes;
    public JComboBox jcbImpuesto, jcbMarca, jcbEstado, jcbCalcularStock, jcbRubro;
    public JButton jbAceptar, jbCancelar;

    public V_modificar_producto(V_inicio mainFrame) {
        super(mainFrame, "Modificar Producto", DEFAULT_MODALITY_TYPE);
        setName("jdModificarProducto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());
        initTop();
        getContentPane().add(jpPrincipal);
    }

    private void initTop() {
        jpPrincipal = new JPanel(new BorderLayout());
        jpBot = new JPanel();
        jpBot.setBorder(new EtchedBorder(2));
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Salir");
        jpMid = new JPanel(new BorderLayout());
        jpMid3 = new JPanel();
        jpMid3.setBorder(new EtchedBorder(2));
        jpMid1 = new JPanel(new GridLayout(11, 2));
        jlTituloProducto = new JLabel();
        jlTituloProducto.setFont(new Font(Font.MONOSPACED, 1, 20));
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
        jlSuspendido = new JLabel("Suspendido");
        jlSuspendido.setHorizontalAlignment(JLabel.CENTER);
        jlCalcularStock = new JLabel("Calcular Stock");
        jlCalcularStock.setHorizontalAlignment(JLabel.CENTER);
        jlCantActual = new JLabel("Cantidad actual");
        jlCantActual.setHorizontalAlignment(JLabel.CENTER);
        jtfProducto = new JTextField();
        jtfProducto.setEditable(false);
        jtfCodigo = new JTextField();
        jtfCodigo.setEditable(false);
        jtfPrecioCosto = new JTextField();
        jtfPrecioVta = new JTextField();
        jtfPrecioMayorista = new JTextField();
        jtfCantActual = new JTextField();
        jcbImpuesto = new JComboBox();
        jcbRubro = new JComboBox();
        jcbMarca = new JComboBox();
        jcbEstado = new JComboBox();
        jcbCalcularStock = new JComboBox();
        jpMid1.add(jlProducto);
        jpMid1.add(jtfProducto);
        jpMid1.add(jlCodigo);
        jpMid1.add(jtfCodigo);
        jpMid1.add(jlImpuesto);
        jpMid1.add(jcbImpuesto);
        jpMid1.add(jlRubro);
        jpMid1.add(jcbRubro);
        jpMid1.add(jlPrecioCosto);
        jpMid1.add(jtfPrecioCosto);
        jpMid1.add(jlMarca);
        jpMid1.add(jcbMarca);
        jpMid1.add(jlPrecioVta);
        jpMid1.add(jtfPrecioVta);
        jpMid1.add(jlPrecioMayorista);
        jpMid1.add(jtfPrecioMayorista);
        jpMid1.add(jlSuspendido);
        jpMid1.add(jcbEstado);
        jpMid1.add(jlCalcularStock);
        jpMid1.add(jcbCalcularStock);
        jpMid1.add(jlCantActual);
        jpMid1.add(jtfCantActual);
        jpMid3.add(jlTituloProducto);
        jpBot.add(jbAceptar);
        jpBot.add(jbCancelar);
        jpMid.add(jpMid3, "North");
        jpMid.add(jpMid1, "Center");
        jpPrincipal.add(jpMid, "Center");
        jpPrincipal.add(jpBot, "South");
    }
}
