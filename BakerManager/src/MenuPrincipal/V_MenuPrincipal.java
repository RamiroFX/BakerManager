/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author Ramiro Ferreira
 */
class V_MenuPrincipal extends JInternalFrame {

    JPanel jpSouth, jpCenter, jpNorth;
    JButton jbSalir, jbProveedores, jbProductos, jbVentas, jbEmpleados,
            jbClientes, jbPedidos, jbCompras, jbCaja, jbReportes,jbCobroPago;
    JFormattedTextField jftFecha;

    public V_MenuPrincipal() {
        super("Menú principal", true, true, true, true);
        setSize(850, 600);
        setName("menuPrincipal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarVista();
        getContentPane().add(jpNorth, BorderLayout.NORTH);
        getContentPane().add(jpCenter, BorderLayout.CENTER);
        getContentPane().add(jpSouth, BorderLayout.SOUTH);
    }

    /**
     * Este metodo se encargará de obtener la dimension de la pantalla en
     * pixeles, para ello utilizamos la clase java.awt.Toolkit.
     *
     * Una vez obtenida la dimension de la pantalla, reducimos el alto de
     * nuestra aplicación puesto que la barra de tareas ocupa parte de la
     * pantalla, comúnmente el alto promedio es de 25 pixeles, por lo tanto a la
     * altura la reducimos 25
     *
     * @return Dimension
     */
    private Dimension obtenerDimensionDePantalla() {
        Dimension pantalla = null;
        pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        pantalla.height = pantalla.height - 80;
        return pantalla;
    }

    private void inicializarVista() {
        Dimension dim = new Dimension(150, 200);
        jpNorth = new JPanel();
        jpSouth = new JPanel();
        jpSouth.setBorder(new EtchedBorder());
        jpCenter = new JPanel();
        jbVentas = new JButton("Ventas [F1]");
        jbVentas.setName("Gestión venta");
        jbVentas.setPreferredSize(dim);
        jbCompras = new JButton("Compras [F2]");
        jbCompras.setName("Gestión compra");
        jbCompras.setPreferredSize(dim);
        jbPedidos = new JButton("Pedidos [F3]");
        jbPedidos.setName("Gestión pedido");
        jbPedidos.setPreferredSize(dim);
        jbProductos = new JButton("Productos [F4]");
        jbProductos.setName("Gestión producto");
        jbProductos.setPreferredSize(dim);
        jbCaja = new JButton("Caja [F5]");
        jbCaja.setName("Gestión caja");
        jbCaja.setPreferredSize(dim);
        jbProveedores = new JButton("Proveedores [F6]");
        jbProveedores.setName("Gestión proveedor");
        jbProveedores.setPreferredSize(dim);
        jbClientes = new JButton("Clientes [F7]");
        jbClientes.setName("Gestión cliente");
        jbClientes.setPreferredSize(dim);
        jbEmpleados = new JButton("Empleados [F8]");
        jbEmpleados.setName("Gestión empleado");
        jbEmpleados.setPreferredSize(dim);
        jbReportes = new JButton("Reportes [F9]");
        jbReportes.setName("Gestión reporte");
        jbReportes.setPreferredSize(dim);
        jbCobroPago = new JButton("Cobros/Pagos [F10]");
        jbCobroPago.setName("Gestión CobroPago");
        jbCobroPago.setPreferredSize(dim);
        jbSalir = new JButton("Salir [ESC]");
        jftFecha = new JFormattedTextField(
                new DefaultFormatterFactory(
                        new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));
        jftFecha.setFont(new java.awt.Font("Monospaced", 1, 14));
        jftFecha.setEditable(false);
        jftFecha.setFocusable(false);

        jpCenter.add(jbVentas);
        jpCenter.add(jbCompras);
        jpCenter.add(jbPedidos);
        jpCenter.add(jbProductos);
        jpCenter.add(jbCaja);
        jpCenter.add(jbProveedores);
        jpCenter.add(jbClientes);
        jpCenter.add(jbEmpleados);
        jpCenter.add(jbReportes);
        jpCenter.add(jbCobroPago);
        jpNorth.add(jftFecha);
        jpSouth.add(jbSalir);
    }
}
