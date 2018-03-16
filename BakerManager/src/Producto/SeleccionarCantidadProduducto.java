/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Producto;
import Egresos.C_crear_egreso;
import Entities.M_facturaDetalle;
import Entities.M_menu_item;
import Entities.M_pedidoDetalle;
import Entities.M_producto;
import MenuPrincipal.DatosUsuario;
import Pedido.C_crearPedido;
import Pedido.C_verPedido;
import Ventas.C_crearVentaRapida;
import Ventas.C_verMesa;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro
 */
public class SeleccionarCantidadProduducto extends javax.swing.JDialog implements ActionListener, KeyListener {

    public static final int MODIFICAR_INGRESO = 7;
    public static final int MODIFICAR_EGRESO = 8;
    public static final int MODIFICAR_MESA_DETALLE = 9;
    public static final int MODIFICAR_PEDIDO_DETALLE = 10;
    public static final int VER_PEDIDO_DETALLE = 11;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlCantidad, jlDescuento, jlPrecio, jlObservacion;
    private javax.swing.JTextField jtfCantidad, jtfDescuento, jtfObservacion, jtfPrecio;
    C_seleccionarProducto selecProd;
    C_crear_egreso crear_egreso;
    C_crearVentaRapida crear_ingreso;
    C_verMesa verMesa;
    C_crearPedido crearPedido;
    C_verPedido verPedido;
    int row;
    int tipo;
    M_producto producto;
    //OPCIONAL PARA VENTAS
    private javax.swing.JLabel jlTotal;
    private javax.swing.JTextField jtfTotal;
    Double cantidad;
    Double descuento;
    Integer precio;
    String observacion;

    public SeleccionarCantidadProduducto(C_seleccionarProducto selecProd, M_producto producto) {
        super(selecProd.vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(selecProd.vista);
        this.selecProd = selecProd;
        this.producto = selecProd.producto;
        tipo = selecProd.tipo;//CREAR EGRESO
        initComponents();
        inicializarVista(producto);
    }

    public SeleccionarCantidadProduducto(C_crear_egreso crear_egreso, int row, M_producto producto) {
        super(crear_egreso.vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(crear_egreso.vista);
        this.producto = producto;
        tipo = MODIFICAR_EGRESO;
        this.crear_egreso = crear_egreso;
        this.row = row;
        initComponents();
    }

    public SeleccionarCantidadProduducto(C_crearVentaRapida crear_ingreso, int row) {
        super(crear_ingreso.vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(crear_ingreso.vista);
        tipo = MODIFICAR_INGRESO;
        this.crear_ingreso = crear_ingreso;
        this.row = row;
        this.producto = crear_ingreso.modelo.getDetalles().get(row).getProducto();
        initComponents();
    }

    public SeleccionarCantidadProduducto(C_crearPedido crearPedido, int row) {
        super(crearPedido.vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(crearPedido.vista);
        tipo = MODIFICAR_PEDIDO_DETALLE;
        this.crearPedido = crearPedido;
        this.row = row;
        this.producto = crearPedido.modelo.getDetalles().get(row).getProducto();
        initComponents();
    }

    public SeleccionarCantidadProduducto(C_verMesa verMesa, int idProducto, int idMesaDetalle) {
        super(verMesa.vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(verMesa.vista);
        tipo = MODIFICAR_MESA_DETALLE;
        this.row = idMesaDetalle;//para almacenar el id de la mesa detalle
        this.verMesa = verMesa;
        this.producto = DB_Producto.obtenerDatosProductoID(idProducto);
        initComponents();
    }

    public SeleccionarCantidadProduducto(C_verPedido verPedido, int idProducto, int idPedidoDetalle) {
        super(verPedido.vista, true);
        System.out.println("119-SeleccionarCantidadProduducto");
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(verPedido.vista);
        tipo = VER_PEDIDO_DETALLE;
        this.row = idPedidoDetalle;//para almacenar el id de la mesa detalle
        this.verPedido = verPedido;
        this.producto = DB_Producto.obtenerDatosProductoID(idProducto);
        initComponents();
    }

    private void inicializarVista(M_producto producto) {
        switch (tipo) {
            case (C_seleccionarProducto.CREAR_PEDIDO): {
                if (this.selecProd.crearPedido.modelo.getPedido() != null) {
                    if (this.selecProd.crearPedido.modelo.getPedido().getCliente() != null) {
                        if (this.selecProd.crearPedido.modelo.getPedido().getCliente().getCategoria() != null) {
                            switch (this.selecProd.crearPedido.modelo.getPedido().getCliente().getCategoria()) {
                                case "Mayorista":
                                    jtfPrecio.setText(producto.getPrecioMayorista().toString());
                                    break;
                                case "Minorista":
                                    jtfPrecio.setText(producto.getPrecioVenta().toString());
                                    break;
                            }
                        } else {
                            jtfPrecio.setText(producto.getPrecioVenta().toString());
                        }
                    }
                }
                break;
            }
            case (C_seleccionarProducto.CREAR_EGRESO): {
                jtfPrecio.setText(producto.getPrecioCosto().toString());
                break;
            }
            default: {
                jtfPrecio.setText(producto.getPrecioVenta().toString());
                break;
            }
        }
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlCantidad = new javax.swing.JLabel("Cantidad");
        jlDescuento = new javax.swing.JLabel("Descuento");
        jlPrecio = new javax.swing.JLabel("Precio");
        jlTotal = new javax.swing.JLabel("Total");
        jlObservacion = new javax.swing.JLabel("Observación");
        jtfCantidad = new javax.swing.JTextField("0");
        jtfDescuento = new javax.swing.JTextField("0.00");
        jtfPrecio = new javax.swing.JTextField();
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
        final SeleccionarCantidadProduducto scp = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (checkearCantidad() && checkearDescuento()) {
                    try {
                        cantidad = Double.valueOf(String.valueOf(jtfCantidad.getText().trim()));
                        if (cantidad <= 0.0) {
                            JOptionPane.showMessageDialog(scp, "Inserte un valor mayor a 0 en Cantidad.", "Atención", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(scp, "Inserte un valor válido para Cantidad.", "Atención", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        descuento = Double.valueOf(String.valueOf(jtfDescuento.getText().trim()));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(scp, "Inserte un valor válido para Descuento.", "Atención", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        precio = Integer.valueOf(String.valueOf(jtfPrecio.getText().trim()));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(scp, "Inserte un valor válido para Precio.", "Atención", JOptionPane.ERROR_MESSAGE);
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
                    switch (tipo) {
                        case (C_seleccionarProducto.CREAR_INGRESO_RAPIDO): {
                            M_facturaDetalle detalle = new M_facturaDetalle();
                            detalle.setCantidad(cantidad);
                            detalle.setDescuento(descuento);
                            detalle.setPrecio(precio);
                            detalle.setObservacion(observacion);
                            detalle.setProducto(scp.producto);
                            detalle.setIdProducto(scp.producto.getId());
                            selecProd.crearVenta.recibirDetalle(detalle);
                            break;
                        }
                        case (C_seleccionarProducto.CREAR_EGRESO): {
                            EnviarProductoConVerificacionPermisoModificacionPrecio();
                            break;
                        }
                        case (SeleccionarCantidadProduducto.MODIFICAR_EGRESO): {
                            EnviarProductoConVerificacionPermisoModificacionPrecio();
                            break;
                        }
                        case (SeleccionarCantidadProduducto.MODIFICAR_INGRESO): {
                            crear_ingreso.modificarDetalle(cantidad, precio, descuento, observacion, row);
                            break;
                        }
                        case (C_seleccionarProducto.VER_MESA): {
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    M_facturaDetalle detalle = new M_facturaDetalle();
                                    detalle.setCantidad(cantidad);
                                    detalle.setDescuento(descuento);
                                    detalle.setPrecio(precio);
                                    detalle.setObservacion(observacion);
                                    detalle.setProducto(producto);
                                    detalle.setIdProducto(producto.getId());
                                    selecProd.verMesa.recibirDetalle(detalle);
                                }
                            });
                            break;
                        }
                        case (MODIFICAR_MESA_DETALLE): {
                            verMesa.modificarDetalle(producto, cantidad, precio, descuento, observacion, row);
                            break;
                        }
                        case (C_seleccionarProducto.CREAR_PEDIDO): {
                            M_pedidoDetalle detalle = new M_pedidoDetalle();
                            detalle.setCantidad(cantidad);
                            detalle.setDescuento(descuento);
                            detalle.setPrecio(precio);
                            detalle.setObservacion(observacion);
                            detalle.setProducto(producto);
                            selecProd.crearPedido.recibirDetalle(detalle);
                            break;
                        }
                        case (MODIFICAR_PEDIDO_DETALLE): {
                            crearPedido.modificarDetalle(cantidad, precio, descuento, observacion, row);
                            break;
                        }
                        case (C_seleccionarProducto.AGREGAR_PEDIDO_DETALLE): {
                            M_pedidoDetalle detalle = new M_pedidoDetalle();
                            detalle.setCantidad(cantidad);
                            detalle.setDescuento(descuento);
                            detalle.setPrecio(precio);
                            detalle.setObservacion(observacion);
                            detalle.setProducto(producto);
                            selecProd.verPedido.recibirDetalle(detalle);
                            break;
                        }
                        case (VER_PEDIDO_DETALLE): {
                            verPedido.modificarDetalle(cantidad, precio, descuento, observacion, row);
                            break;
                        }
                        default: {
                            dispose();
                            break;
                        }
                    }

                }
                dispose();
            }
        });
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
            String cantidad = this.jtfCantidad.getText().replace(',', '.');
            d = Double.valueOf(cantidad);
        } catch (Exception e) {
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
                Integer total = null;
                Integer precio = null;
                try {
                    total = Integer.valueOf(jtfTotal.getText());
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
                    precio = Integer.valueOf(String.valueOf(jtfPrecio.getText()));
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
        ArrayList<M_menu_item> accesos = DatosUsuario.getRol_usuario().getAccesos();
        for (M_menu_item acceso : accesos) {
            if (Parametros.MenuItem.MODIFICAR_PRODUCTO.getDescripcion().equals(acceso.getItemDescripcion())) {
                int precioActual = producto.getPrecioCosto();
                int precioNuevo = precio;
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
        }
    }
}
