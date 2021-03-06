/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock.SeleccionarCantidadAjuste;

import DB.DB_Inventario;
import Entities.E_ajusteStockDetalle;
import Entities.E_ajusteStockMotivo;
import Interface.RecibirAjusteStockDetalleCB;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarProductoAjusteStock extends javax.swing.JDialog implements ActionListener, KeyListener {

    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlProducto, jlCantidadNueva, jlCantidadVieja,
            jlObservacion, jlMotivo, jlFecha, jlHora, jlProductoCodigo;
    private javax.swing.JTextField jtfProducto, jtfCantidadNueva, jtfCantidadVieja,
            jtfObservacion, jtfProductoCodigo;
    private JComboBox<E_ajusteStockMotivo> jcbMotivo;
    private JDateChooser jdcFecha;
    private JComboBox<String> jcbHora, jcbMinuto;
    int index;
    E_ajusteStockDetalle detalle;
    private RecibirAjusteStockDetalleCB productoCallback;

    public SeleccionarProductoAjusteStock(JDialog vista) {
        super(vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(350, 350));
        setLocationRelativeTo(vista);
        initComponents();
        initLogic();
    }

    public void setUpdateIndex(int index) {
        this.index = index;
    }

    public void setProducto(E_ajusteStockDetalle detalle) {
        this.detalle = detalle;
    }

    public void setProductoCallback(RecibirAjusteStockDetalleCB callback) {
        this.productoCallback = callback;
    }

    private void initLogic() {
        this.index = -1;
    }

    public void inicializarVista() {
        jtfProducto.setText(detalle.getProducto().getDescripcion());
        jtfProductoCodigo.setText(detalle.getProducto().getCodigo());
        jtfCantidadVieja.setText(detalle.getProducto().getCantActual() + "");
        List<E_ajusteStockMotivo> motivos = DB_Inventario.consultarAjusteStockMotivo();
        motivos.forEach((unMotivo) -> {
            jcbMotivo.addItem(unMotivo);
        });
        for (int i = 1; i < 10; i++) {
            jcbHora.addItem("0" + i);
        }
        for (int i = 10; i < 24; i++) {
            jcbHora.addItem("" + i);
        }
        for (int i = 0; i < 10; i++) {
            jcbMinuto.addItem("0" + i);
        }
        for (int i = 10; i < 60; i++) {
            jcbMinuto.addItem("" + i);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        String horaT = sdf.format(currentTime).substring(0, 2);
        int horas = Integer.valueOf(horaT);
        //Se suma una hora para que la hora de entrega tenga una hora mas que la hora actual
        int horaAux = horas + 1;
        if (horaAux >= 0 && horaAux < 10) {
            this.jcbHora.setSelectedItem("0" + horaAux);
        } else {
            this.jcbHora.setSelectedItem("" + horaAux);
        }
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlProducto = new javax.swing.JLabel("Producto");
        jlProductoCodigo = new javax.swing.JLabel("Código");
        jlCantidadNueva = new javax.swing.JLabel("Cantidad nueva");
        jlCantidadVieja = new javax.swing.JLabel("Cantidad vieja");
        jlObservacion = new javax.swing.JLabel("Observación");
        jlMotivo = new javax.swing.JLabel("Motivo");
        jlFecha = new javax.swing.JLabel("Fecha");
        jlHora = new javax.swing.JLabel("Tiempo");
        jtfProducto = new javax.swing.JTextField();
        jtfProducto.setEditable(false);
        jtfProducto.setFocusable(false);
        jtfCantidadNueva = new javax.swing.JTextField();
        jtfCantidadVieja = new javax.swing.JTextField();
        jtfCantidadVieja.setEditable(false);
        jtfCantidadVieja.setFocusable(false);
        jtfObservacion = new javax.swing.JTextField();
        jtfProductoCodigo = new javax.swing.JTextField();
        jtfProductoCodigo.setEditable(false);
        jtfProductoCodigo.setFocusable(false);
        jcbMotivo = new javax.swing.JComboBox<>();
        jdcFecha = new JDateChooser(Calendar.getInstance().getTime());
        jcbHora = new javax.swing.JComboBox<>();
        jcbMinuto = new javax.swing.JComboBox<>();

        JPanel jpTiempo = new JPanel();
        jpTiempo.add(jcbHora);
        jpTiempo.add(jcbMinuto);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        jtfCantidadNueva.addKeyListener(this);
        //jtfObservacion.addKeyListener(this);
        getContentPane().add(jlProducto);
        getContentPane().add(jtfProducto, "width :250:,grow,wrap");
        getContentPane().add(jlProductoCodigo);
        getContentPane().add(jtfProductoCodigo, "width :250:,grow,wrap");
        getContentPane().add(jlCantidadVieja);
        getContentPane().add(jtfCantidadVieja, "width :250:,grow,wrap");
        getContentPane().add(jlCantidadNueva);
        getContentPane().add(jtfCantidadNueva, "width :250:,grow,wrap");
        getContentPane().add(jlMotivo);
        getContentPane().add(jcbMotivo, "width :250:,grow,wrap");
        getContentPane().add(jlFecha);
        getContentPane().add(jdcFecha, "width :250:,grow,wrap");
        getContentPane().add(jlHora);
        getContentPane().add(jpTiempo, "width :250:,grow,wrap");
        getContentPane().add(jlObservacion);
        getContentPane().add(jtfObservacion, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfCantidadNueva.selectAll();
    }

    private boolean validarCantidadNueva() {
        double d = -1;
        if (this.jtfCantidadNueva.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidadNueva.setText("0");
            this.jtfCantidadNueva.requestFocusInWindow();
            return false;
        }
        try {
            String cantidadAux = this.jtfCantidadNueva.getText().replace(',', '.');
            d = Double.valueOf(cantidadAux);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidadNueva.setText("0");
            this.jtfCantidadNueva.requestFocusInWindow();
            return false;
        }
        if (d < 0.0) {
            JOptionPane.showMessageDialog(this, "Inserte un valor mayor o igual a 0 en Cantidad.", "Parametros incorrectos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarObservacion() {
        String obs = jtfObservacion.getText().trim();
        if (obs.length() > 150) {
            JOptionPane.showMessageDialog(this, "Campo Observación: Máximo permitido 150 caracteres. Total (" + obs.length() + ")", "Parametros incorrectos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void enviarCantidad() {
        if (!validarCantidadNueva()) {
            return;
        }
        if (!validarObservacion()) {
            return;
        }
        double cantidadVieja = Double.valueOf(jtfCantidadVieja.getText().trim().replace(",", "."));
        double cantidadNueva = Double.valueOf(jtfCantidadNueva.getText().trim().replace(",", "."));
        String observacion = jtfObservacion.getText().trim();
        Calendar tiempo = Calendar.getInstance();
        tiempo.setTime(jdcFecha.getDate());
        E_ajusteStockMotivo motivo = jcbMotivo.getItemAt(jcbMotivo.getSelectedIndex());
        if (index < 0) {
            productoCallback.recibirAjusteStock(detalle.getProducto(), cantidadVieja, cantidadNueva, motivo, tiempo.getTime(), observacion);

        } else {
            productoCallback.modificarAjusteStock(index, detalle.getProducto(), cantidadVieja, cantidadNueva, motivo, tiempo.getTime(), observacion);
        }
        dispose();
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
        if (jtfCantidadNueva.hasFocus()) {
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
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
