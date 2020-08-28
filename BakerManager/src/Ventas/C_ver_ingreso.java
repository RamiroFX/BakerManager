/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Cliente;
import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Preferencia;
import Entities.E_impresionTipo;
import Entities.E_impuesto;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_telefono;
import MenuPrincipal.DatosUsuario;
import Impresora.Impresora;
import ModeloTabla.FacturaDetalleTableModel;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_ver_ingreso implements ActionListener, KeyListener {

    public V_crearVentaRapida vista;
    int idEgresoCabecera, nroFactura;
    M_facturaCabecera faca;
    M_cliente cliente;
    FacturaDetalleTableModel fadeTM;

    public C_ver_ingreso(int idEgresoCabecera, V_crearVentaRapida vista) {
        this.idEgresoCabecera = idEgresoCabecera;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public C_ver_ingreso(V_crearVentaRapida vista, int nroFactura) {
        this.nroFactura = nroFactura;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
        this.vista.jbSalir.requestFocusInWindow();
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtfClieDireccion.addKeyListener(this);
        this.vista.jtfClieRuc.addKeyListener(this);
        this.vista.jtfClieTelefono.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        //this.vista.jbImprimir.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer total5 = 0;
        Integer total10 = 0;
        Integer totalIva5 = 0;
        Integer totalIva10 = 0;
        Integer total = 0;
        for (M_facturaDetalle m_facturaDetalle : fadeTM.getFacturaDetalleList()) {
            switch (m_facturaDetalle.getProducto().getIdImpuesto()) {
                case E_impuesto.EXENTA: {
                    exenta = exenta + m_facturaDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA5: {
                    total5 = total5 + m_facturaDetalle.calcularSubTotal();
                    break;
                }
                case E_impuesto.IVA10: {
                    total10 = total10 + m_facturaDetalle.calcularSubTotal();
                    break;
                }
            }
        }
        total = exenta + total5 + total10;
        totalIva5 = total5 / 21;
        totalIva10 = total10 / 11;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(total5);
        this.vista.jftImpIva5.setValue(totalIva5);
        this.vista.jftIva10.setValue(total10);
        this.vista.jftImpIva10.setValue(totalIva10);
        this.vista.jftTotal.setValue(total);
        this.vista.jftIvaTotal.setValue(totalIva5 + totalIva10);
    }

    private void inicializarVista() {
        this.vista.setTitle(V_crearVentaRapida.TITLE_READ);
        if (idEgresoCabecera < 1) {
            faca = DB_Ingreso.obtenerIngresoCabeceraNroFactura(nroFactura);
        } else {
            faca = DB_Ingreso.obtenerIngresoCabeceraID(idEgresoCabecera);
        }
        cliente = DB_Cliente.obtenerDatosClienteID(faca.getIdCliente());
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(faca.getIdFuncionario());
        faca.setCliente(cliente);
        faca.setFuncionario(funcionario);
        this.vista.jtfCliente.setText(cliente.getNombre() + " - " + cliente.getEntidad());
        fadeTM = new FacturaDetalleTableModel();
        fadeTM.setFacturaDetalleList(DB_Ingreso.obtenerVentaDetalles(faca.getIdFacturaCabecera()));
        this.vista.jtFacturaDetalle.setModel(fadeTM);
        Vector condCompra = obtenerTipoOperacion();
        for (int i = 0; i < condCompra.size(); i++) {
            this.vista.jcbCondVenta.addItem(condCompra.get(i));
        }
        ArrayList<E_impresionTipo> tipoVenta = obtenerTipoVenta();
        for (int i = 0; i < tipoVenta.size(); i++) {
            this.vista.jcbTipoVenta.addItem(tipoVenta.get(i));
        }
        Utilities.c_packColumn.packColumns(this.vista.jtFacturaDetalle, 1);
        java.awt.Font fuente = new java.awt.Font("Times New Roman", 0, 18);
        javax.swing.text.DefaultFormatterFactory dff = new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance()));
        this.vista.jftExenta.setFormatterFactory(dff);
        this.vista.jftExenta.setFont(fuente); // NOI18N
        this.vista.jftIva5.setFormatterFactory(dff);
        this.vista.jftIva5.setFont(fuente); // NOI18N
        this.vista.jftIva10.setFormatterFactory(dff);
        this.vista.jftIva10.setFont(fuente); // NOI18N
        this.vista.jftTotal.setFormatterFactory(dff);
        this.vista.jftTotal.setFont(fuente); // NOI18N
        switch (faca.getIdCondVenta()) {
            case Parametros.TipoOperacion.CONTADO: {
                this.vista.jcbCondVenta.setSelectedIndex(0);
                break;
            }
            case Parametros.TipoOperacion.CREDITO: {
                this.vista.jcbCondVenta.setSelectedIndex(1);
                break;
            }
        }
        String nombre = cliente.getNombre();
        String entidad = cliente.getEntidad();
        String ruc = cliente.getRuc() + "-" + cliente.getRucId();
        String direccion = cliente.getDireccion();
        ArrayList<M_telefono> telefono = DB_Cliente.obtenerTelefonoCliente(cliente.getIdCliente());
        this.vista.jtfCliente.setText(nombre + " (" + entidad + ")");
        this.vista.jtfClieRuc.setText(ruc);
        this.vista.jtfClieDireccion.setText(direccion);
        if (!telefono.isEmpty()) {
            this.vista.jtfClieTelefono.setText(telefono.get(0).getNumero());
        }
        this.vista.jtfNroFactura.setText(faca.getNroFactura() + "");
        this.vista.jtfNroFactura.setEditable(false);
        if (faca.getNroFactura() < 1) {
            E_impresionTipo tipoFactura = new E_impresionTipo(2, "factura");
            this.vista.jcbTipoVenta.removeItem(tipoFactura);
        }
        this.vista.jtfCodProd.setEnabled(false);
        this.vista.jbAgregarProducto.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jcbCondVenta.setEnabled(false);
        this.vista.jbCliente.setEnabled(false);
        this.vista.jbAceptar.setEnabled(false);
        sumarTotal();
    }

    public ArrayList<E_impresionTipo> obtenerTipoVenta() {
        return DB_Preferencia.obtenerImpresionTipo();
    }

    public Vector obtenerTipoOperacion() {
        return DB_Egreso.obtenerTipoOperacion();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        }
        if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimirVenta();
        }
    }

    private void imprimirVenta() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                E_impresionTipo impreTipo = vista.jcbTipoVenta.getItemAt(vista.jcbTipoVenta.getSelectedIndex());
                switch (impreTipo.getDescripcion()) {
                    case "factura": {
                        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la factura?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opcion == JOptionPane.YES_OPTION) {
                            ArrayList<M_facturaDetalle> fade = DB_Ingreso.obtenerVentaDetalles(idEgresoCabecera);
                            Impresora.imprimirFacturaVenta(faca, fade);
                        }
                        break;
                    }
                    case "boleta": {
                        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la boleta?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opcion == JOptionPane.YES_OPTION) {
                            ArrayList<M_facturaDetalle> facturaDetalle = DB_Ingreso.obtenerVentaDetalles(idEgresoCabecera);
                            Impresora.imprimirBoletaVenta(faca, facturaDetalle);
                        }
                        break;
                    }
                    case "ticket": {
                        int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opcion == JOptionPane.YES_OPTION) {
                            Impresora.imprimirTicketVentaGuardada(DatosUsuario.getRol_usuario(), faca);
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F2: {
                imprimirVenta();
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
