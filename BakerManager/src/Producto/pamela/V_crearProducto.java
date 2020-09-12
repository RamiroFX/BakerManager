/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import Entities.E_Marca;
import Entities.E_impuesto;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearProducto extends JDialog {

    private JLabel jlProducto, jlCodigo, jlImpuesto, jlRubro, jlPrecioCosto, jlMarca,
            jlPrecioVta, jlPrecioMayorista;
    public JTextField jtfProducto, jtfCodigo, jtfPrecioCosto,
            jtfPrecioVta, jtfPrecioMayorista;
    public JTextArea jtaIngredientes;
    public JComboBox jcbCategoria;
    public JComboBox<E_impuesto> jcbImpuesto;
    public JComboBox<E_Marca> jcbMarca;
    public JButton jbAceptar, jbCancelar, jbCopiar;
    private JPanel jpProducto, jpBotones, jpPrincipal;

    private JPanel jpCliente;
    public JButton jbAgregarCliente, jbQuitarCliente, jbModificar;
    private JScrollPane jspCliente;
    public JTable jtCliente;

    public V_crearProducto(JFrame mainFrame) {
        super(mainFrame, "Agregar Producto", DEFAULT_MODALITY_TYPE);
        setName("jdCrearProducto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeVariables();
        construcLayout();
    }

    public V_crearProducto(JDialog mainFrame) {
        super(mainFrame, "Agregar Producto", DEFAULT_MODALITY_TYPE);
        setName("jdCrearProducto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeVariables();
        construcLayout();
    }

    private void construcLayout() {
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpPrincipal, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void initializeCustomerVariables() {
        jbAgregarCliente = new JButton("Agregar cliente");
        jbQuitarCliente = new JButton("Quitar cliente");
        jbModificar = new JButton("Modificar cliente");
        jtCliente = new JTable();
        jspCliente = new JScrollPane(jtCliente);
        JPanel jpBotonesCliente = new JPanel();
        jpBotonesCliente.add(jbAgregarCliente);
        jpBotonesCliente.add(jbModificar);
        jpBotonesCliente.add(jbQuitarCliente);
        jpCliente = new JPanel(new BorderLayout());
        jpCliente.add(jpBotonesCliente, BorderLayout.NORTH);
        jpCliente.add(jspCliente, BorderLayout.CENTER);
    }

    private void initializeVariables() {
        jpPrincipal = new JPanel(new GridLayout(1, 2));
        jpProducto = new JPanel(new MigLayout("", "", "30"));

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

        jtaIngredientes = new JTextArea();

        jpProducto.add(jlProducto);
        jpProducto.add(jtfProducto, "spanx, growx, pushx, wrap");
        jpProducto.add(jlCodigo);
        jpProducto.add(jtfCodigo, "spanx, growx,  pushx, wrap");
        jpProducto.add(jlImpuesto);
        jpProducto.add(jcbImpuesto, "spanx, growx,  pushx,wrap");
        jpProducto.add(jlRubro);
        jpProducto.add(jcbCategoria, "spanx, growx,  pushx,wrap");
        jpProducto.add(jlMarca);
        jpProducto.add(jcbMarca, "spanx, growx,  pushx,wrap");
        jpProducto.add(jlPrecioCosto);
        jpProducto.add(jtfPrecioCosto, "spanx, growx,  pushx, wrap");
        jpProducto.add(jlPrecioVta);
        jpProducto.add(jtfPrecioVta, "spanx, growx, pushx, wrap");
        jpProducto.add(jlPrecioMayorista);
        jpProducto.add(jtfPrecioMayorista, "spanx, pushx, growx");

        jpBotones = new JPanel();
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jbCopiar = new JButton("Copiar de otro");
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
        jpBotones.add(jbCopiar);
        initializeCustomerVariables();

        jpPrincipal.add(jpProducto, BorderLayout.WEST);
        jpPrincipal.add(jpCliente, BorderLayout.EAST);
    }

}
