/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_impresionPlantilla;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearTimbrado extends JDialog {

    private JLabel jlDescripcion, jlNroTimbrado, jlNroSucursal, jlPuntoVenta, jlBoletaInicial, jlBoletaFinal, 
            jlFechaVencimiento, jlPlantilla;
    public JTextField jtfDescripcion, jtfNroTimbrado, jtfNroSucursal, jtfPuntoVenta, jtfBoletaInicial, jtfBoletaFinal;
    public JDateChooser jdcFechaVencimiento;
    public JButton jbAceptar, jbCancelar;
    private JPanel jpPrincipal, jpBotones;
    public JComboBox<E_impresionPlantilla> jcbPlantillas;
    public static final String CREATE_TITLE = "Crear timbrado de venta", READ_TITLE = "Ver timbrado de venta";

    public V_crearTimbrado(JDialog vista) {
        super(vista);
        initializeComponents();
        constructLayout();
        constructAppWindow();
    }

    public V_crearTimbrado(JFrame vista) {
        super(vista);
        initializeComponents();
        constructLayout();
        constructAppWindow();
    }

    private void initializeComponents() {
        jpPrincipal = new JPanel(new MigLayout());
        //Labels
        jlDescripcion = new JLabel("Descripción");
        jlDescripcion.setHorizontalAlignment(JLabel.CENTER);
        jlNroTimbrado = new JLabel("Nro. de timbrado");
        jlNroTimbrado.setHorizontalAlignment(JLabel.CENTER);
        jlNroSucursal = new JLabel("Nro. de sucursal");
        jlNroSucursal.setHorizontalAlignment(JLabel.CENTER);
        jlPuntoVenta = new JLabel("Nro. de punto de venta");
        jlPuntoVenta.setHorizontalAlignment(JLabel.CENTER);
        jlBoletaInicial = new JLabel("Nro. de boleta inicial");
        jlBoletaInicial.setHorizontalAlignment(JLabel.CENTER);
        jlBoletaFinal = new JLabel("Nro. de boleta final");
        jlBoletaFinal.setHorizontalAlignment(JLabel.CENTER);
        jlFechaVencimiento = new JLabel("Fecha de vencimiento");
        jlFechaVencimiento.setHorizontalAlignment(JLabel.CENTER);
        jlPlantilla = new JLabel("Plantilla de impresión");
        jlPlantilla.setHorizontalAlignment(JLabel.CENTER);
        //TextFields
        jtfDescripcion = new JTextField();
        jtfNroTimbrado = new JTextField();
        jtfNroSucursal = new JTextField();
        jtfPuntoVenta = new JTextField();
        jtfBoletaInicial = new JTextField();
        jtfBoletaFinal = new JTextField();
        jdcFechaVencimiento = new JDateChooser();
        jcbPlantillas= new JComboBox<>();
        
        jpPrincipal.setBorder(new EtchedBorder());
        jpPrincipal.add(jlDescripcion);
        jpPrincipal.add(jtfDescripcion, "pushx, growx, wrap");
        jpPrincipal.add(jlNroTimbrado);
        jpPrincipal.add(jtfNroTimbrado, "pushx, growx, wrap");
        jpPrincipal.add(jlNroSucursal);
        jpPrincipal.add(jtfNroSucursal, "pushx, growx, wrap");
        jpPrincipal.add(jlPuntoVenta);
        jpPrincipal.add(jtfPuntoVenta, "pushx, growx, wrap");
        jpPrincipal.add(jlBoletaInicial);
        jpPrincipal.add(jtfBoletaInicial, "pushx, growx, wrap");
        jpPrincipal.add(jlBoletaFinal);
        jpPrincipal.add(jtfBoletaFinal, "pushx, growx, wrap");
        jpPrincipal.add(jlFechaVencimiento);
        jpPrincipal.add(jdcFechaVencimiento, "pushx, growx, wrap");
        jpPrincipal.add(jlPlantilla);
        jpPrincipal.add(jcbPlantillas, "pushx, growx, wrap");

        jpBotones = new JPanel();
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cerrar");
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
    }

    private void constructLayout() {
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpPrincipal, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void constructAppWindow() {
        setTitle(CREATE_TITLE);
        setName("jdCrearTimbradoVenta");
        setSize(new Dimension(360, 340));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
    }
}
