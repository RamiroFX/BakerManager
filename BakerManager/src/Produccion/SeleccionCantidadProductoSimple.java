/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionFilm;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.RecibirProductoCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCantidadProductoSimple extends javax.swing.JDialog implements ActionListener, KeyListener {

    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlProducto, jlCantidad;
    private javax.swing.JTextField jtfProducto, jtfCantidad;
    int row;
    M_producto producto;
    E_produccionFilm film;
    Double cantidad;
    //String observacion;
    private RecibirProductoCallback productoCallback;
    private InterfaceRecibirProduccionFilm filmCallback;
    boolean isProductoTerminado;// productoTerminado=true;rollo=false

    public SeleccionCantidadProductoSimple(JDialog vista, int index) {
        super(vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(350, 150));
        setLocationRelativeTo(vista);
        this.row = index;//row > -1 si es para modificar
        this.isProductoTerminado = false;
        initComponents();
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public void setFilm(E_produccionFilm film) {
        this.film = film;
    }

    public void setProductoCallback(RecibirProductoCallback callback) {
        this.isProductoTerminado = true;
        this.productoCallback = callback;
    }

    public void setFilmCallback(InterfaceRecibirProduccionFilm filmCallback) {
        this.isProductoTerminado = false;
        this.filmCallback = filmCallback;
    }

    public void inicializarVista() {
        if (isProductoTerminado) {
            jtfProducto.setText(producto.getDescripcion());
        } else {
            jtfProducto.setText(film.getProducto().getDescripcion());
        }
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlProducto = new javax.swing.JLabel("Producto");
        jlCantidad = new javax.swing.JLabel("Cantidad");
        //jlObservacion = new javax.swing.JLabel("Observación");
        jtfProducto = new javax.swing.JTextField();
        jtfProducto.setEditable(false);
        jtfProducto.setFocusable(false);
        jtfCantidad = new javax.swing.JTextField();
        //jtfObservacion = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        jtfCantidad.addKeyListener(this);
        //jtfObservacion.addKeyListener(this);
        getContentPane().add(jlProducto);
        getContentPane().add(jtfProducto, "width :250:,grow,wrap");
        getContentPane().add(jlCantidad);
        getContentPane().add(jtfCantidad, "width :250:,grow,wrap");
        //getContentPane().add(jlObservacion);
        //getContentPane().add(jtfObservacion, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfCantidad.selectAll();
    }

    public void enviarCantidad() {
        if (!checkearCantidad()) {
            return;
        }
        cantidad = Double.valueOf(String.valueOf(jtfCantidad.getText().trim()));
        //observacion = String.valueOf(jtfObservacion.getText().trim());
        /*if (observacion.length() > 120) {
            JOptionPane.showMessageDialog(null, "Observacion sobrepaso el máximo de 120 caracteres permitidos.", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (observacion.isEmpty()) {
            observacion = null;
        }*/
        if (isProductoTerminado) {
            if (row > -1) {
                productoCallback.modificarProducto(row, cantidad, 0, 0, producto, "");
            } else {
                productoCallback.recibirProducto(cantidad, 0, 0, producto, "");
            }
        } else {
            if (!validarPeso()) {
                return;
            }
            if (row > -1) {
                film.setPeso(cantidad);
                filmCallback.modificarFilm(row, film);
            } else {
                film.setPeso(cantidad);
                filmCallback.recibirFilm(film);
            }
        }
        dispose();
    }

    private boolean checkearCantidad() {
        Double d = null;
        if (this.jtfCantidad.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidad.setText("0");
            this.jtfCantidad.requestFocusInWindow();
            return false;
        }
        try {
            String cantidadAux = this.jtfCantidad.getText().replace(',', '.');
            d = Double.valueOf(cantidadAux);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Cantidad.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfCantidad.setText("0");
            this.jtfCantidad.requestFocusInWindow();
            return false;
        }
        if (d <= 0.0) {
            JOptionPane.showMessageDialog(this, "Inserte un valor mayor a 0 en Cantidad.", "Parametros incorrectos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarPeso() {
        //validar que la cantidad ingresada sea menor o igual a la cantidad disponible
        Double peso = Double.valueOf(String.valueOf(jtfCantidad.getText().trim()));
        if (peso > film.getPesoActual()) {
            JOptionPane.showMessageDialog(this, "El peso seleccionado supera el disponible", "Parametros incorrectos", JOptionPane.ERROR_MESSAGE);
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

    @Override
    public void keyTyped(KeyEvent ke) {
        if (jtfCantidad.hasFocus()) {
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