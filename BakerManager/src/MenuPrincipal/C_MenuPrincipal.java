/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuPrincipal;

import Caja.GestionCaja;
import Cliente.Gestion_cliente;
import Cobros.GestionCobroPago;
import Egresos.Gestion_Egreso;
import Entities.M_menu_item;
import Pedido.GestionPedidos;
import Producto.Gestion_Producto;
import Proveedor.Gestion_proveedores;
import Ventas.Gestion_Ventas;
import bakermanager.C_inicio;
import Empleado.Gestion_empleado;
import Produccion.GestionProduccion;
import Reportes.GestionReporte;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class C_MenuPrincipal implements ActionListener, KeyListener {

    public V_MenuPrincipal vista;
    public C_inicio c_inicio;

    public C_MenuPrincipal(V_MenuPrincipal vista, C_inicio inicio) {
        this.vista = vista;
        this.c_inicio = inicio;
        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        this.vista.jftFecha.setValue(Calendar.getInstance().getTime());
        this.vista.jbEmpleados.setEnabled(false);
        this.vista.jbVentas.setEnabled(false);
        this.vista.jbProveedores.setEnabled(false);
        this.vista.jbProductos.setEnabled(false);
        this.vista.jbClientes.setEnabled(false);
        this.vista.jbCompras.setEnabled(false);
        this.vista.jbPedidos.setEnabled(false);
//        this.vista.jbCaja.setEnabled(false);
//        this.vista.jbCobroPago.setEnabled(false);
        this.vista.jbReportes.setEnabled(false);
        ArrayList<M_menu_item> accesos = c_inicio.modelo.getRol_usuario().getAccesos();
        for (int i = 0; i < accesos.size(); i++) {
            if (this.vista.jbEmpleados.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbEmpleados.setEnabled(true);
            }
            if (this.vista.jbVentas.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbVentas.setEnabled(true);
            }
            if (this.vista.jbProductos.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbProductos.setEnabled(true);
            }
            if (this.vista.jbProveedores.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbProveedores.setEnabled(true);
            }
            if (this.vista.jbClientes.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbClientes.setEnabled(true);
            }
            if (this.vista.jbCompras.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbCompras.setEnabled(true);
            }
            if (this.vista.jbPedidos.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbPedidos.setEnabled(true);
            }
            if (this.vista.jbCaja.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbCaja.setEnabled(true);
            }
            if (this.vista.jbReportes.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbReportes.setEnabled(true);
            }
            if (this.vista.jbCobro.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbCobro.setEnabled(true);
            }
            if (this.vista.jbProduccion.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbProduccion.setEnabled(true);
            }
            if (this.vista.jbPago.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.vista.jbPago.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiConfigImpresion.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiConfigImpresion.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiConfigImpresionTicket.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiConfigImpresionTicket.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiEmpresa.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiEmpresa.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiConfigImpresionBoleta.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiConfigImpresionBoleta.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiPersonalizar.getName().equals(accesos.get(i).getItemDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiPersonalizar.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiProduccion.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiProduccion.setEnabled(true);
            }
            if (this.c_inicio.vista.getJMenuBar().jmiMateriaPrima.getName().equals(accesos.get(i).getMenuDescripcion())) {
                this.c_inicio.vista.getJMenuBar().jmiMateriaPrima.setEnabled(true);
            }
        }
    }

    private void agregarListeners() {
        this.vista.jbClientes.addActionListener(this);
        this.vista.jbProveedores.addActionListener(this);
        this.vista.jbCompras.addActionListener(this);
        this.vista.jbPedidos.addActionListener(this);
        this.vista.jbProductos.addActionListener(this);
        this.vista.jbVentas.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbEmpleados.addActionListener(this);
        this.vista.jbCaja.addActionListener(this);
        this.vista.jbCobro.addActionListener(this);
        this.vista.jbReportes.addActionListener(this);
        this.vista.jbProduccion.addActionListener(this);
        this.vista.jbPago.addActionListener(this);
        //////

        this.vista.jbClientes.addKeyListener(this);
        this.vista.jbProveedores.addKeyListener(this);
        this.vista.jbCompras.addKeyListener(this);
        this.vista.jbPedidos.addKeyListener(this);
        this.vista.jbProductos.addKeyListener(this);
        this.vista.jbVentas.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jbEmpleados.addKeyListener(this);
        this.vista.jbCaja.addKeyListener(this);
        this.vista.jbCobro.addKeyListener(this);
        this.vista.jbReportes.addKeyListener(this);
        this.vista.jbProduccion.addKeyListener(this);
        this.vista.jbPago.addKeyListener(this);
    }

    void mostrarVista() {
        this.vista.setVisible(true);
        this.c_inicio.agregarVentana(vista);
        this.vista.setLocation(this.c_inicio.centrarPantalla(this.vista));
        //this.c_inicio.centrarPantalla(vista);
    }

    private void shutdown() {
        int op = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea salir?",
                "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(this.vista.jbEmpleados)) {
            Gestion_empleado ges_usuario = new Gestion_empleado(c_inicio);
            ges_usuario.mostrarVista();
        } else if (src.equals(this.vista.jbVentas)) {
            Gestion_Ventas gestionVenta = new Gestion_Ventas(c_inicio);
            gestionVenta.mostrarVista();
        } else if (src.equals(this.vista.jbProductos)) {
            Gestion_Producto gestionProducto = new Gestion_Producto(c_inicio);
            gestionProducto.mostrarVista();
        } else if (src.equals(this.vista.jbProveedores)) {
            Gestion_proveedores gestionProveedor = new Gestion_proveedores(c_inicio);
            gestionProveedor.mostrarVista();
        } else if (src.equals(this.vista.jbClientes)) {
            Gestion_cliente gestionCliente = new Gestion_cliente(c_inicio);
            gestionCliente.mostrarVista();
        } else if (src.equals(this.vista.jbCompras)) {
            Gestion_Egreso gestionEgreso = new Gestion_Egreso(c_inicio);
            gestionEgreso.mostrarVista();
        } else if (src.equals(this.vista.jbPedidos)) {
            GestionPedidos gestionPedidos = new GestionPedidos(c_inicio);
            gestionPedidos.mostrarVista();
        } else if (src.equals(this.vista.jbCaja)) {
            GestionCaja gestionCaja = new GestionCaja(c_inicio);
            gestionCaja.mostrarVista();
        } else if (src.equals(this.vista.jbCobro)) {
            GestionCobroPago gestionCobro = new GestionCobroPago(c_inicio);
            gestionCobro.mostrarVista();
        } else if (src.equals(this.vista.jbProduccion)) {
            GestionProduccion gestionProduccion = new GestionProduccion(c_inicio);
            gestionProduccion.mostrarVista();
        } else if (src.equals(this.vista.jbReportes)) {
            /*POSTTS POS = new POSTTS();
             POS.setVisible(true);
             c_inicio.agregarVentana(POS);
             POS.setLocation(this.c_inicio.centrarPantalla(this.vista));*/
            GestionReporte gestionReportes = new GestionReporte(c_inicio);
            gestionReportes.mostrarVista();
        } else if (src.equals(this.vista.jbSalir)) {
            shutdown();
        }
    }

    public Point centrarPantalla(JInternalFrame i) {
        /*con este codigo centramos el panel en el centro del contenedor
         la anchura del contenedor menos la anchura de nuestro componente divido a 2
         lo mismo con la altura.*/
        return new Point((this.vista.getWidth() - i.getWidth()) / 2, (this.vista.getHeight() - i.getHeight() - 45) / 2);
    }

    public Dimension establecerTamañoPanel() {
        return new Dimension((int) (this.vista.getWidth() * 0.8), (int) (this.vista.getHeight() * 0.8));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_F1): {
                if (vista.jbVentas.isEnabled()) {
                    Gestion_Ventas gestionVenta = new Gestion_Ventas(c_inicio);
                    gestionVenta.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F2): {
                if (vista.jbCompras.isEnabled()) {
                    Gestion_Egreso gestionEgreso = new Gestion_Egreso(c_inicio);
                    gestionEgreso.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F3): {
                if (vista.jbPedidos.isEnabled()) {
                    GestionPedidos gestionPedidos = new GestionPedidos(c_inicio);
                    gestionPedidos.mostrarVista();

                }
                break;
            }
            case (KeyEvent.VK_F4): {
                if (vista.jbProductos.isEnabled()) {
                    Gestion_Producto gestionProducto = new Gestion_Producto(c_inicio);
                    gestionProducto.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F5): {
                if (vista.jbProduccion.isEnabled()) {
                    GestionProduccion gestionProduccion = new GestionProduccion(c_inicio);
                    gestionProduccion.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F6): {
                if (vista.jbCaja.isEnabled()) {
                    GestionCaja gestionCaja = new GestionCaja(c_inicio);
                    gestionCaja.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F7): {
                if (vista.jbProveedores.isEnabled()) {
                    Gestion_proveedores gestionProveedor = new Gestion_proveedores(c_inicio);
                    gestionProveedor.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F8): {
                if (vista.jbClientes.isEnabled()) {
                    Gestion_cliente gestionCliente = new Gestion_cliente(c_inicio);
                    gestionCliente.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F9): {
                if (vista.jbEmpleados.isEnabled()) {
                    Gestion_empleado ges_usuario = new Gestion_empleado(c_inicio);
                    ges_usuario.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F10): {
                if (vista.jbReportes.isEnabled()) {
                    GestionReporte gestionReportes = new GestionReporte(c_inicio);
                    gestionReportes.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_F11): {
                if (vista.jbCobro.isEnabled()) {
                    GestionCobroPago gestionCobroPago = new GestionCobroPago(c_inicio);
                    gestionCobroPago.mostrarVista();
                }
                break;
            }
            case (KeyEvent.VK_ESCAPE): {
                shutdown();
                break;
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
