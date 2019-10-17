/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Cliente;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_telefono;
import MenuPrincipal.DatosUsuario;
import Impresora.Impresora;
import Parametros.TipoVenta;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_ver_ingreso implements ActionListener, KeyListener {

    public V_ver_ingreso vista;
    int idEgresoCabecera;
    M_facturaCabecera faca;
    M_cliente cliente;

    public C_ver_ingreso(int idEgresoCabecera, V_ver_ingreso vista) {
        this.idEgresoCabecera = idEgresoCabecera;
        this.vista = vista;
        inicializarVista();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
        this.vista.jtfClieDireccion.addKeyListener(this);
        this.vista.jtfClieRuc.addKeyListener(this);
        this.vista.jtfClieTelefono.addKeyListener(this);
        this.vista.jtfCliente.addKeyListener(this);
        this.vista.jtfFuncionario.addKeyListener(this);
        //this.vista.jbImprimir.addKeyListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void sumarTotal() {
        Integer exenta = 0;
        Integer iva5 = 0;
        Integer iva10 = 0;
        Integer total = 0;
        int cantRow = this.vista.jtFacturaDetalle.getRowCount();
        for (int i = 0; i < cantRow; i++) {
            exenta = exenta + Integer.valueOf(String.valueOf(this.vista.jtFacturaDetalle.getValueAt(i, 5)));
            iva5 = iva5 + Integer.valueOf(String.valueOf(this.vista.jtFacturaDetalle.getValueAt(i, 6)));
            iva10 = iva10 + Integer.valueOf(String.valueOf(this.vista.jtFacturaDetalle.getValueAt(i, 7)));
        }
        total = exenta + iva5 + iva10;
        this.vista.jftExenta.setValue(exenta);
        this.vista.jftIva5.setValue(iva5);
        this.vista.jftIva10.setValue(iva10);
        this.vista.jftTotal.setValue(total);
    }

    private void inicializarVista() {
        faca = DB_Ingreso.obtenerIngresoCabeceraID(idEgresoCabecera);
        cliente = DB_Cliente.obtenerDatosClienteID(faca.getIdCliente());
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(faca.getIdFuncionario());
        faca.setCliente(cliente);
        faca.setFuncionario(funcionario);
        this.vista.jtfCliente.setText(cliente.getNombre() + " - " + cliente.getEntidad());
        this.vista.jtFacturaDetalle.setModel(DB_Ingreso.obtenerIngresoDetalle(idEgresoCabecera));
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
                this.vista.jrbContado.setSelected(true);
                break;
            }
            case Parametros.TipoOperacion.CREDITO: {
                this.vista.jrbCredito.setSelected(true);
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
        this.vista.jtfFuncionario.setText(funcionario.getNombre() + " " + funcionario.getApellido());
        sumarTotal();
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
                switch(1){
                    case TipoVenta.FACTURA:{
                        
                        break;
                    }
                    case TipoVenta.BOLETA:{
                        
                        break;
                    }
                    case TipoVenta.TICKET:{
                        
                        break;
                    }
                }
                if (faca.getNroFactura() != null) {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir la factura?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        ArrayList<M_facturaDetalle> fade = DB_Ingreso.obtenerVentaDetalles(idEgresoCabecera);
                        Impresora.imprimirFacturaVenta(faca, fade);
                    }
                } else {
                    int opcion = JOptionPane.showConfirmDialog(vista, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        Impresora.imprimirTicketVentaGuardada(DatosUsuario.getRol_usuario(), faca);
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
