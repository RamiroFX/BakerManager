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
            jbClientes, jbPedidos, jbCompras, jbCaja, jbReportes;
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
        jbSalir = new JButton("Salir");
        jbProveedores = new JButton("Proveedores");
        jbProveedores.setName("Gestión proveedor");
        jbProveedores.setPreferredSize(dim);
        jbProductos = new JButton("Productos");
        jbProductos.setName("Gestión producto");
        jbProductos.setPreferredSize(dim);
        jbVentas = new JButton("Ventas");
        jbVentas.setName("Gestión venta");
        jbVentas.setPreferredSize(dim);
        jbClientes = new JButton("Clientes");
        jbClientes.setName("Gestión cliente");
        jbClientes.setPreferredSize(dim);
        jbPedidos = new JButton("Pedidos");
        jbPedidos.setName("Gestión pedido");
        jbPedidos.setPreferredSize(dim);
        jbCompras = new JButton("Compras");
        jbCompras.setName("Gestión compra");
        jbCompras.setPreferredSize(dim);
        jbEmpleados = new JButton("Empleados");
        jbEmpleados.setName("Gestión empleado");
        jbEmpleados.setPreferredSize(dim);
        jbCaja = new JButton("Caja");
        jbCaja.setName("Gestión caja");
        jbCaja.setPreferredSize(dim);
        jbReportes = new JButton("Reportes");
        jbReportes.setName("Gestión reporte");
        jbReportes.setPreferredSize(dim);
        jftFecha = new JFormattedTextField(
                new DefaultFormatterFactory(
                        new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));
        jftFecha.setFont(new java.awt.Font("Monospaced", 1, 14));
        jftFecha.setEditable(false);

        jpNorth.add(jftFecha);
        jpSouth.add(jbSalir);
        jpCenter.add(jbProveedores);
        jpCenter.add(jbProductos);
        jpCenter.add(jbVentas);
        jpCenter.add(jbClientes);
        jpCenter.add(jbCompras);
        jpCenter.add(jbPedidos);
        jpCenter.add(jbEmpleados);
        jpCenter.add(jbCaja);
        jpCenter.add(jbReportes);
    }
}
