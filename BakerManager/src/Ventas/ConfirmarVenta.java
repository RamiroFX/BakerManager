/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Interface.InterfaceNotificarCambio;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class ConfirmarVenta extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final String TITULO = "Confirmar venta",
            S_ALERTA = "¿Desea confirma esta venta?",
            S_VUELTO = "Vuelto:",
            S_TOTAL_PAGAR = "Monto a pagar:",
            S_MONTO = "Condición de venta:";

    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    Font fontError = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlTotalPagar, jlMonto, jlVuelto, jtfMensaje;
    private javax.swing.JFormattedTextField jtfTotalPagar, jtfVuelto;
    private javax.swing.JTextField jtfMonto;
    int monto;

    private InterfaceNotificarCambio interfaceNotificarCambio;

    public ConfirmarVenta(JDialog vista) {
        super(vista, true);
        setTitle(TITULO);
        setSize(new java.awt.Dimension(380, 220));
        setLocationRelativeTo(vista);
        initComponents();
        agregarListeners();
    }

    private void agregarListeners() {
        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);
        jtfTotalPagar.addKeyListener(this);
        jtfMonto.addKeyListener(this);
        jtfMensaje.addKeyListener(this);
        jtfVuelto.addKeyListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                jtfMonto.requestFocusInWindow();
                jtfMonto.selectAll();
            }
        });
    }

    public void mostrarVista() {
        this.setVisible(true);
    }

    public void inicializarVista(String totalPagar) {
<<<<<<< HEAD
        DecimalFormat decimalFormat = new DecimalFormat("###0.##");
        double totalPagarVal = Double.valueOf(totalPagar);
        jtfTotalPagar.setValue(totalPagarVal);
        jtfMonto.setText(decimalFormat.format(totalPagarVal));
=======
        double totalPagarVal = Double.valueOf(totalPagar);
        jtfTotalPagar.setValue(totalPagarVal);
        jtfMonto.setText(Math.round(totalPagarVal) + "");
>>>>>>> c192f4ebee0e1fd87a7d367e9fadfa13b958ab14
        calcularVuelto();
    }

    public void setInterface(InterfaceNotificarCambio interfaceNotificarCambio) {
        this.interfaceNotificarCambio = interfaceNotificarCambio;
    }

    private void initComponents() {
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##")));
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton("OK");
        jbCancel = new javax.swing.JButton("Cancel");
        jtfMensaje = new javax.swing.JLabel(S_ALERTA);
        jlTotalPagar = new javax.swing.JLabel(S_TOTAL_PAGAR);
        jlTotalPagar.setFont(font);
        jlMonto = new javax.swing.JLabel(S_MONTO);
        jlMonto.setFont(font);
        jlVuelto = new javax.swing.JLabel(S_VUELTO);
        jlVuelto.setFont(font);
        jtfTotalPagar = new javax.swing.JFormattedTextField();
        jtfTotalPagar.setFont(font);
        jtfTotalPagar.setEditable(false);
        jtfTotalPagar.setFormatterFactory(dff);
        //DecimalFormat format = new DecimalFormat("#");
        jtfVuelto = new javax.swing.JFormattedTextField();
        jtfVuelto.setFont(font);
        jtfVuelto.setFormatterFactory(dff);
        jtfMonto = new javax.swing.JTextField();
        jtfMonto.setFont(font);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(jtfMensaje, "spanx,growx,wrap");
        getContentPane().add(jlTotalPagar);
        getContentPane().add(jtfTotalPagar, "width :200:,grow,wrap");
        getContentPane().add(jlMonto);
        getContentPane().add(jtfMonto, "width :200:,grow,wrap");
        getContentPane().add(jlVuelto);
        getContentPane().add(jtfVuelto, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);
    }

    public void enviarCantidad() {
        if (!controlarMonto()) {
            return;
        }
        interfaceNotificarCambio.notificarCambio();
        dispose();
    }

    private boolean controlarMonto() {
        Double monto = null;
        if (this.jtfMonto.getText().trim().isEmpty()) {
            return true;
        }
        try {
            String cantidad = this.jtfMonto.getText().trim().replace(",", ".");
            System.out.println("Ventas.ConfirmarVenta.controlarMonto()");
            System.out.println(cantidad);
            monto = Double.valueOf(cantidad);
        } catch (Exception e) {
            System.err.println(e);
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Monto a pagar.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfMonto.setText("");
            return false;
        }
<<<<<<< HEAD
        double totalPagar = (double) this.jtfTotalPagar.getValue();
=======
        double tp = (double) jtfTotalPagar.getValue();
        int totalPagar = (int) Math.round(tp);
>>>>>>> c192f4ebee0e1fd87a7d367e9fadfa13b958ab14
        if (monto < totalPagar) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "El monto a pagar no puede ser menor al total.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfMonto.setText("");
            return false;
        }
        return true;
    }

    /*
    TODO
     */
    private void calcularVuelto() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                double monto;
                try {
                    double cantidad = Double.valueOf(jtfMonto.getText().trim().replace(",", "."));
                    System.out.println(".run().cantidad: " + cantidad);
                    monto = Double.valueOf(cantidad);
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Verifique en uno de los campos el parametro:"
                            + "Asegurese de colocar un numero valido\n"
                            + "en el campo Monto a pagar.",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    return;
                }
<<<<<<< HEAD
                double totalPagar = (double) jtfTotalPagar.getValue();
                Double vuelto = monto - totalPagar;
=======
                double tp = (double) jtfTotalPagar.getValue();
                int totalPagar = (int) Math.round(tp);
                Integer vuelto = monto - totalPagar;
>>>>>>> c192f4ebee0e1fd87a7d367e9fadfa13b958ab14
                if (vuelto < 0) {
                    jtfVuelto.setForeground(Color.RED);
                } else {
                    jtfVuelto.setForeground(Color.BLACK);
                }
                System.out.println(".totalPagar(): " + totalPagar);
                System.out.println(".monto(): " + monto);
                System.out.println(".vuelto(): " + vuelto);
                jtfVuelto.setValue(vuelto);
                System.out.println(".run().fin");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbOK)) {
            enviarCantidad();
        } else if (e.getSource().equals(jbCancel)) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (jtfTotalPagar.hasFocus() || jtfMonto.hasFocus() || jtfMensaje.hasFocus() || jtfVuelto.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarCantidad();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                this.dispose();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (jtfMonto.hasFocus()) {
            calcularVuelto();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
