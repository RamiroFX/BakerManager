/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import Entities.E_facturaDetalle;
import Entities.M_egreso_detalle;
import Entities.M_facturaDetalle;
import Entities.M_producto;
import Interface.RecibirProductoCallback;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class SeleccionarCantidadProduducto extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final int PRECIO_COSTO = 1;
    public static final int PRECIO_VENTA_MINORISTA = 2;
    public static final int PRECIO_VENTA_MAYORISTA = 3;
    public static final int MODIFICAR_INGRESO = 7;
    public static final int MODIFICAR_EGRESO = 8;
    public static final int MODIFICAR_MESA_DETALLE = 9;
    public static final int MODIFICAR_PEDIDO_DETALLE = 10;
    public static final int VER_PEDIDO_DETALLE = 11;
    public static final int AGREGAR_MESA_DETALLE = 13;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlCantidad, jlDescuento, jlPrecio, jlObservacion,
            jlPrecioCosto, jlPrecioMayorista;
    private javax.swing.JTextField jtfCantidad, jtfDescuento, jtfObservacion, jtfPrecio,
            jtfPrecioCosto, jtfPrecioMayotista;
    int row;
    int tipo;
    M_producto producto;
    //OPCIONAL PARA VENTAS
    private javax.swing.JLabel jlTotal;
    private javax.swing.JTextField jtfTotal;
    Double cantidad;
    Double descuento;
    Double precio;
    String observacion;
    RecibirProductoCallback callback;

    public SeleccionarCantidadProduducto(JDialog dialog, M_producto producto, RecibirProductoCallback callback, int tipo) {
        super(dialog, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(dialog);
        this.row = -1;
        this.producto = producto;
        this.callback = callback;
        this.tipo = tipo;
        initComponents();
        inicializarVista(producto);
    }

    public SeleccionarCantidadProduducto(JDialog dialog, M_producto producto, RecibirProductoCallback callback, int tipo, int index) {
        super(dialog, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(dialog);
        this.producto = producto;
        this.callback = callback;
        this.tipo = tipo;
        this.row = index;
        initComponents();
        inicializarVista(producto);
    }

    private void inicializarVista(M_producto producto) {
        switch (tipo) {
            case PRECIO_COSTO: {
                jtfPrecio.setText(producto.getPrecioCosto() + "");
                break;
            }
            case PRECIO_VENTA_MAYORISTA: {
                jtfPrecio.setText(producto.getPrecioMayorista() + "");
                break;
            }
            case PRECIO_VENTA_MINORISTA: {
                jtfPrecio.setText(producto.getPrecioMinorista() + "");
                break;
            }
            default: {
                jtfPrecio.setText(producto.getPrecioVenta().toString());
                break;
            }
        }
    }

    public void mostrarPrecioAdicional() {
        setSize(new java.awt.Dimension(300, 310));
        getContentPane().removeAll();
        getContentPane().add(jlCantidad);
        getContentPane().add(jtfCantidad, "width :200:,grow,wrap");
        getContentPane().add(jlPrecio);
        getContentPane().add(jtfPrecio, "width :200:,grow,wrap");
        getContentPane().add(jlPrecioCosto);
        getContentPane().add(jtfPrecioCosto, "width :200:,grow,wrap");
        getContentPane().add(jlPrecioMayorista);
        getContentPane().add(jtfPrecioMayotista, "width :200:,grow,wrap");
        getContentPane().add(jlDescuento);
        getContentPane().add(jtfDescuento, "width :200:,grow,wrap");
        getContentPane().add(jlTotal);
        getContentPane().add(jtfTotal, "width :200:,grow,wrap");
        getContentPane().add(jlObservacion);
        getContentPane().add(jtfObservacion, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlCantidad = new javax.swing.JLabel("Cantidad");
        jlDescuento = new javax.swing.JLabel("Descuento(%)");
        jlPrecio = new javax.swing.JLabel("Precio");
        jlPrecioCosto = new javax.swing.JLabel("Precio Costo");
        jlPrecioMayorista = new javax.swing.JLabel("Precio Mayorista");
        jlTotal = new javax.swing.JLabel("Total");
        jlObservacion = new javax.swing.JLabel("Observación");
        jtfPrecioCosto = new javax.swing.JTextField(producto.getPrecioCosto() + "");
        jtfPrecioCosto.setEditable(false);
        jtfPrecioCosto.setEnabled(false);
        jtfPrecioMayotista = new javax.swing.JTextField(producto.getPrecioMayorista() + "");
        jtfPrecioMayotista.setEditable(false);
        jtfPrecioMayotista.setEnabled(false);
        switch (tipo) {
            case C_seleccionarProducto.CREAR_INGRESO_POR_CODIGO: {
                jtfCantidad = new javax.swing.JTextField("1.0");
                jtfPrecio = new javax.swing.JTextField(producto.getPrecioVenta() + "");
                break;
            }
            default: {
                jtfCantidad = new javax.swing.JTextField("0");
                jtfPrecio = new javax.swing.JTextField();
                break;
            }
        }
        jtfDescuento = new javax.swing.JTextField("0.00");
        jtfTotal = new javax.swing.JTextField();
        jtfObservacion = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbOK.addActionListener(this);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(this);
        jtfCantidad.addKeyListener(this);
        jtfDescuento.addKeyListener(this);
        jtfPrecio.addKeyListener(this);
        jtfTotal.addKeyListener(this);
        jtfObservacion.addKeyListener(this);
        getContentPane().add(jlCantidad);
        getContentPane().add(jtfCantidad, "width :200:,grow,wrap");
        getContentPane().add(jlPrecio);
        getContentPane().add(jtfPrecio, "width :200:,grow,wrap");
        getContentPane().add(jlDescuento);
        getContentPane().add(jtfDescuento, "width :200:,grow,wrap");
        getContentPane().add(jlTotal);
        getContentPane().add(jtfTotal, "width :200:,grow,wrap");
        getContentPane().add(jlObservacion);
        getContentPane().add(jtfObservacion, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfCantidad.selectAll();
    }

    public void enviarCantidad() {
        if (checkearCantidad() && checkearDescuento()) {
            try {
                cantidad = Double.valueOf(String.valueOf(jtfCantidad.getText().trim()));
                if (cantidad <= 0.0) {
                    JOptionPane.showMessageDialog(this, "Inserte un valor mayor a 0 en Cantidad.", "Atención", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Inserte un valor válido para Cantidad.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                descuento = Double.valueOf(String.valueOf(jtfDescuento.getText().trim()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Inserte un valor válido para Descuento.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                precio = Double.valueOf(String.valueOf(jtfPrecio.getText().trim()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Inserte un valor válido para Precio.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (precio < 1) {
                JOptionPane.showMessageDialog(this, "Inserte un valor positivo para Precio.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            observacion = String.valueOf(jtfObservacion.getText().trim());
            if (observacion.length() > 120) {
                JOptionPane.showMessageDialog(null, "Observacion sobrepaso el máximo de 120 caracteres permitidos.", "Atención", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (observacion.isEmpty()) {
                observacion = null;
            }
            //TODO remove
            if (callback != null) {
                if (row > -1) {
                    callback.modificarProducto(row, cantidad, precio, descuento, producto, observacion);
                } else {
                    callback.recibirProducto(cantidad, precio, descuento, producto, observacion);
                }
                dispose();
                return;
            }
        }
        dispose();
    }

    private boolean checkearCantidad() {
        Double d = null;
        if (this.jtfCantidad.getText().isEmpty()) {
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
        return true;
    }

    private boolean checkearDescuento() {
        Double d = null;
        if (this.jtfDescuento.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Descuento.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfDescuento.requestFocusInWindow();
            return false;
        }
        try {
            d = Double.valueOf(String.valueOf(this.jtfDescuento.getText()));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique en uno de los campos el parametro:"
                    + e.getMessage().substring(17) + "\n"
                    + "Asegurese de colocar un numero valido\n"
                    + "en el campo Descuento.",
                    "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            this.jtfDescuento.setText("");
            this.jtfDescuento.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private void checkearTotal() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Double descuento = null;
                Double cantidad = null;
                Double total = null;
                Double precio = null;
                try {
                    total = Double.valueOf(jtfTotal.getText());
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Verifique en uno de los campos el parametro:"
                            + e.getMessage().substring(12) + "\n"
                            + "Asegurese de colocar un numero valido\n"
                            + "en el campo Total.",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    jtfTotal.setText("");
                    return;
                }
                try {
                    descuento = Double.valueOf(String.valueOf(jtfDescuento.getText()));
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Verifique en uno de los campos el parametro:"
                            + e.getMessage().substring(12) + "\n"
                            + "Asegurese de colocar un numero valido\n"
                            + "en el campo Descuento.",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    jtfDescuento.setText("0.0");
                    return;
                }
                try {
                    String cantidadfx = jtfCantidad.getText().replace(',', '.');
                    cantidad = Double.valueOf(cantidadfx);
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Verifique en uno de los campos el parametro:"
                            + e.getMessage().substring(12) + "\n"
                            + "Asegurese de colocar un numero valido\n"
                            + "en el campo Cantidad.",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    jtfCantidad.setText("0");
                    return;
                }
                try {
                    precio = Double.valueOf(String.valueOf(jtfPrecio.getText()));
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Verifique en uno de los campos el parametro:"
                            + e.getMessage().substring(12) + "\n"
                            + "Asegurese de colocar un numero valido\n"
                            + "en el campo Precio.",
                            "Parametros incorrectos",
                            javax.swing.JOptionPane.OK_OPTION);
                    jtfPrecio.setText("");
                    return;
                }
                Double precioTemporal = precio - ((precio * descuento) / 100);
                cantidad = Double.valueOf(total / precioTemporal);
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);
                jtfCantidad.setText(df.format(cantidad).replace(',', '.'));
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
        if (jtfCantidad.hasFocus() || jtfDescuento.hasFocus() || jtfPrecio.hasFocus() || jtfTotal.hasFocus() || jtfObservacion.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarCantidad();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                this.dispose();
            }
        }
        if (ke.getSource().equals(jtfTotal)) {
            checkearTotal();
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void EnviarProductoConVerificacionPermisoModificacionPrecio() {
        /*ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (Parametros.MenuItem.MODIFICAR_PRODUCTO.getDescripcion().equals(acceso.getItemDescripcion())) {
                double precioActual = producto.getPrecioCosto();
                double precioNuevo = precio;
                if (precioActual != precioNuevo) {
                    int option = JOptionPane.showConfirmDialog(this, "El precio de costo es diferente \n ¿Desea modificar el precio?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        DB_Producto.modificarPrecioCostoProducto(producto.getId(), precioNuevo);
                    }
                }
            }
        }
        switch (tipo) {
            case (C_seleccionarProducto.CREAR_EGRESO): {
                selecProd.c_egresos.recibirProducto(cantidad, precio, descuento, observacion, selecProd.producto);
                break;
            }
            case (SeleccionarCantidadProduducto.MODIFICAR_EGRESO): {
                this.crear_egreso.modificarCelda(cantidad, precio, descuento, observacion, row);
                break;
            }
        }*/
    }

    public void cargarDatos(E_facturaDetalle fd) {
        jtfCantidad.setText(fd.getCantidad() + "");
        jtfDescuento.setText(fd.getDescuento() + "");
        jtfPrecio.setText(fd.getPrecio() + "");
        if (fd.getObservacion() != null) {
            jtfObservacion.setText(fd.getObservacion());
        }
    }
    
    public void loadData(double cantidad, double descuento, double precio, String observacion) {
        jtfCantidad.setText(cantidad + "");
        jtfDescuento.setText(descuento + "");
        jtfPrecio.setText(precio + "");
        if (observacion != null) {
            jtfObservacion.setText(observacion);
        }
    }


    public void cargarDatosCompra(M_egreso_detalle ed) {
        jtfCantidad.setText(ed.getCantidad() + "");
        jtfDescuento.setText(ed.getDescuento() + "");
        jtfPrecio.setText(ed.getPrecio() + "");
        if (ed.getObservacion() != null) {
            jtfObservacion.setText(ed.getObservacion());
        }
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
