/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import Interface.InterfaceRecibirProduccionTerminados;
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

    public static final int ROLLO = 1,
            PRODUCTO = 2,
            PRODUCCION_TERMINADOS = 4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlProducto, jlCantidad;
    private javax.swing.JTextField jtfProducto, jtfCantidad;
    int index;
    int tipo;
    M_producto producto;
    E_produccionFilm rollo;
    E_produccionDetalle produccionDetalle;
    Double cantidad;
    //String observacion;
    private RecibirProductoCallback productoCallback;
    private InterfaceRecibirProduccionFilm filmCallback;
    private InterfaceRecibirProduccionTerminados terminadosCallback;
    boolean esModoCreacion;

    public SeleccionCantidadProductoSimple(JDialog vista, boolean esModoCreacion) {
        super(vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(350, 150));
        setLocationRelativeTo(vista);
        this.esModoCreacion = esModoCreacion;
        initComponents();
    }

    public void setUpdateIndex(int index) {
        this.index = index;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public void setFilm(E_produccionFilm film) {
        // = film;
        rollo= new E_produccionFilm();
        this.rollo.setId(film.getId());
        this.rollo.setNroFilm(film.getNroFilm());
        this.rollo.setCono(film.getCono());
        this.rollo.setMedida(film.getMedida());
        this.rollo.setMicron(film.getMicron());
        this.rollo.setProductoClasificacion(film.getProductoClasificacion());
        this.rollo.setProducto(film.getProducto());
        this.rollo.setPeso(film.getPeso());
        this.rollo.setPesoActual(film.getPesoActual());
    }

    public void setProduccionTerminados(E_produccionDetalle produccionDetalle) {
        //this.produccionDetalle = produccionDetalle;
        this.produccionDetalle = new E_produccionDetalle();
        this.produccionDetalle.setId(produccionDetalle.getId());
        this.produccionDetalle.setProducto(produccionDetalle.getProducto());
        this.produccionDetalle.setCantidad(produccionDetalle.getCantidad());
    }

    public void setProductoCallback(RecibirProductoCallback callback) {
        this.productoCallback = callback;
    }

    public void setFilmCallback(InterfaceRecibirProduccionFilm filmCallback) {
        this.filmCallback = filmCallback;
    }

    public void setProduccionTerminadosCallback(InterfaceRecibirProduccionTerminados terminadosCallback) {
        this.terminadosCallback = terminadosCallback;
    }

    public void inicializarVista() {
        switch (tipo) {
            case PRODUCTO: {
                jtfProducto.setText(producto.getDescripcion());
                break;
            }
            case ROLLO: {
                jtfProducto.setText(rollo.getProducto().getDescripcion());
                break;
            }
            case PRODUCCION_TERMINADOS: {
                jtfProducto.setText(produccionDetalle.getProducto().getDescripcion());
                break;
            }
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
        String cantAux = jtfCantidad.getText().trim().replace(",", ".");
        cantidad = Double.valueOf(cantAux);
        switch (tipo) {
            case PRODUCTO: {
                if (esModoCreacion) {
                    productoCallback.recibirProducto(cantidad, 0, 0, producto, "");
                } else {
                    productoCallback.modificarProducto(index, cantidad, 0, 0.0, producto, "");
                }
                break;
            }
            case ROLLO: {
                if (!validarPeso()) {
                    return;
                }
                if (esModoCreacion) {
                    rollo.setPeso(cantidad);
                    filmCallback.recibirFilm(rollo);
                } else {
                    rollo.setPeso(cantidad);
                    filmCallback.modificarFilm(index, rollo);
                }
                break;
            }
            case PRODUCCION_TERMINADOS: {
                produccionDetalle.setCantidad(cantidad);
                if (esModoCreacion) {
                    terminadosCallback.recibirProductoTerminado(produccionDetalle);
                } else {
                    terminadosCallback.modificarProductoTerminado(index, produccionDetalle);
                }
                break;
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
        String cantAux = jtfCantidad.getText().trim().replace(",", ".");
        Double peso = Double.valueOf(cantAux);
        if (peso > rollo.getPesoActual()) {
            JOptionPane.showMessageDialog(this, "La cantidad seleccionada supera a la disponible \n Cantidad seleccionada: " + peso + " \n Cantidad disponible: " + rollo.getPesoActual(), "Parametros incorrectos", JOptionPane.ERROR_MESSAGE);
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
