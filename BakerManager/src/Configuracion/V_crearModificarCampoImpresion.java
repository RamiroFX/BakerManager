/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Entities.Estado;
import Entities.M_campoImpresion;
import Interface.crearModificarParametroCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearModificarCampoImpresion extends javax.swing.JDialog implements ActionListener {

    private static final int CREAR_PARAMETRO = 1, MODIFICAR_PARAMETRO = 2;
    private static final String ERROR_TITLE = "Parametros incorrectos",
            ERROR_MESSAGE = "Verifique en uno de los campos el parametro:" + "\n"
            + "Asegurese de colocar un numero válido\n"
            + "en el campo coordenada X o Y.",
            ERROR_MESSAGE_2 = "Verifique el nombre del parametro: mínimo 1 caracter, máximo 30 caracteres.";
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlCoordenadaX, jlCoordenadaY, jlCampo;
    private javax.swing.JTextField jtfCoordenadaX, jtfCoordenadaY, jtfCampo;
    int row;
    int tipo;
    private crearModificarParametroCallback callback;
    private M_campoImpresion ci;

    public V_crearModificarCampoImpresion(int tipo, JDialog parent) {
        super(parent, true);
        inicializarVista(parent, tipo);
        initComponents(tipo);
    }

    public V_crearModificarCampoImpresion(int tipo, JDialog parent, M_campoImpresion ci) {
        super(parent, true);
        this.ci = ci;
        inicializarVista(parent, tipo);
        initComponents(tipo);
    }

    private void inicializarVista(JDialog parent, int tipo) {
        this.tipo = tipo;
        switch (tipo) {
            case CREAR_PARAMETRO: {
                setTitle("Crear parametro");
                break;
            }
            case MODIFICAR_PARAMETRO: {
                setTitle("Modificar parametro");
                break;
            }
        }
        setSize(new java.awt.Dimension(300, 200));
        setLocationRelativeTo(parent);
    }

    private void initComponents(int tipo) {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlCoordenadaX = new javax.swing.JLabel("Coordenada X");
        jlCoordenadaY = new javax.swing.JLabel("Coordenada Y");
        jlCampo = new javax.swing.JLabel("Parámetro");
        jtfCoordenadaY = new javax.swing.JTextField("0.00");
        jtfCoordenadaX = new javax.swing.JTextField("0.00");
        jtfCampo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        getContentPane().add(jlCampo);
        getContentPane().add(jtfCampo, "width :200:,grow,wrap");
        getContentPane().add(jlCoordenadaX);
        getContentPane().add(jtfCoordenadaX, "width :200:,grow,wrap");
        getContentPane().add(jlCoordenadaY);
        getContentPane().add(jtfCoordenadaY, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);
        if (tipo == MODIFICAR_PARAMETRO) {
            jtfCampo.setText(this.ci.getCampo());
            jtfCoordenadaX.setText(this.ci.getX() + "");
            jtfCoordenadaY.setText(this.ci.getY() + "");
        }
    }

    public void enviarCantidad() {
        if (checkearCampos()) {
            switch (tipo) {
                case CREAR_PARAMETRO: {
                    M_campoImpresion ci = new M_campoImpresion();
                    ci.setCampo(jtfCampo.getText().trim());
                    ci.setX(Double.valueOf(String.valueOf(this.jtfCoordenadaX.getText().trim())));
                    ci.setY(Double.valueOf(String.valueOf(this.jtfCoordenadaY.getText().trim())));
                    ci.setEstado(new Estado(1, "Activo"));
                    this.callback.recibirParametroImpresion(ci);
                    break;
                }
                case MODIFICAR_PARAMETRO: {
                    this.ci.setCampo(jtfCampo.getText().trim());
                    this.ci.setX(Double.valueOf(String.valueOf(this.jtfCoordenadaX.getText().trim())));
                    this.ci.setY(Double.valueOf(String.valueOf(this.jtfCoordenadaY.getText().trim())));
                    this.callback.modificarParametroImpresion(ci);
                    break;
                }
            }
            this.dispose();
        }
    }

    private boolean checkearCampos() {
        String parametro = String.valueOf(jtfCampo.getText().trim());
        if (parametro.length() > 30 || parametro.length() < 1) {
            javax.swing.JOptionPane.showMessageDialog(null, ERROR_MESSAGE_2,
                    ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Double d = null;
        if (this.jtfCoordenadaX.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, ERROR_MESSAGE,
                    ERROR_TITLE,
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCoordenadaX.requestFocusInWindow();
            return false;
        }
        try {
            d = Double.valueOf(String.valueOf(this.jtfCoordenadaX.getText()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, ERROR_MESSAGE,
                    ERROR_TITLE,
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCoordenadaX.setText("");
            this.jtfCoordenadaX.requestFocusInWindow();
            return false;
        }
        Double cy = null;
        if (this.jtfCoordenadaY.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, ERROR_MESSAGE,
                    ERROR_TITLE,
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCoordenadaY.requestFocusInWindow();
            return false;
        }
        try {
            cy = Double.valueOf(String.valueOf(this.jtfCoordenadaY.getText()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, ERROR_MESSAGE,
                    ERROR_TITLE,
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCoordenadaY.setText("");
            this.jtfCoordenadaY.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbOK)) {
            enviarCantidad();
        } else if (e.getSource().equals(jbCancel)) {
            this.dispose();
        }
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(crearModificarParametroCallback callback) {
        this.callback = callback;
    }
}
