/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Proveedor;
import DB.DB_manager;
import Entities.E_tipoOperacion;
import Impresora.Impresora;
import Ventas.V_crearVentaRapida;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
class C_ver_egreso implements ActionListener {

    public V_crearEgresoPorFecha vista;
    M_Egresos modelo;
    int idEgresoCabecera;

    public C_ver_egreso(V_crearEgresoPorFecha vista, M_Egresos modelo) {
        this.vista = vista;
        this.modelo = modelo;
        initComp();
        agregarListeners();
    }

    public C_ver_egreso(int idEgresoCabecera, V_crearEgresoPorFecha vista, M_Egresos modelo) {
        this.idEgresoCabecera = idEgresoCabecera;
        this.vista = vista;
        this.modelo = modelo;
        initComp();
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbImprimir.addActionListener(this);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void sumarTotal() {
        int cantFilas = this.vista.jtProductos.getRowCount();
        Integer totalExenta = 0, total5 = 0, total10 = 0, total = 0;
        for (int i = 0; i < cantFilas; i++) {
            Integer ivaExenta = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 5)));
            totalExenta = totalExenta + ivaExenta;
            Integer iva5 = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 6)));
            total5 = total5 + iva5;
            Integer iva10 = Integer.valueOf(String.valueOf(this.vista.jtProductos.getValueAt(i, 7)));
            total10 = total10 + iva10;
        }
        total = totalExenta + total5 + total10;
        this.vista.jftTotal.setValue(total);
        this.vista.jftExenta.setValue(totalExenta);
        this.vista.jftIva5.setValue(total5);
        this.vista.jftIva10.setValue(total10);
    }

    private void initComp() {
        this.modelo.egreso_cabecera = DB_Egreso.obtenerEgresoCabeceraID(idEgresoCabecera);
        this.modelo.egresoDetalles = DB_Egreso.obtenerEgresoDetalles(idEgresoCabecera);
        this.modelo.proveedor = DB_Proveedor.obtenerDatosProveedorID(this.modelo.egreso_cabecera.getId_proveedor());
        this.modelo.empleado = DB_Funcionario.obtenerDatosFuncionarioID(modelo.egreso_cabecera.getId_empleado());
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String tiempoRegistro = " (Tiempo de registro: " + dateFormater.format(modelo.egreso_cabecera.getTiempo()) + ")";
        String registradoPor = "(Registrado por: " + modelo.empleado.getNombre() + ")";
        this.vista.setTitle(V_crearEgresoPorFecha.TITLE_READ + tiempoRegistro + " - " + registradoPor);
        this.vista.jtfNroFactura.setText(this.modelo.egreso_cabecera.getNro_factura().toString());
        this.vista.jdcFecha.setDate(modelo.egreso_cabecera.getTiempo());
        this.vista.jtfProveedor.setText(this.modelo.proveedor.getEntidad());
        this.vista.jtfProvDireccion.setText(this.modelo.proveedor.getDireccion());
        this.vista.jtfProvRuc.setText(this.modelo.proveedor.getRucCompleto());
        this.vista.jtProductos.setModel(DB_Egreso.obtenerEgresoDetalle(idEgresoCabecera));        
        ArrayList<E_tipoOperacion> condVenta = DB_manager.obtenerTipoOperaciones();
        for (int i = 0; i < condVenta.size(); i++) {
            this.vista.jcbTipoCompra.addItem(condVenta.get(i));
        }
        this.vista.jcbTipoCompra.setSelectedItem(modelo.egreso_cabecera.getCondCompra());
        this.vista.jbAceptar.setVisible(false);
        this.vista.jbAgregarProv.setEnabled(false);
        this.vista.jcbTipoCompra.setEnabled(false);
        this.vista.jbAgregarProducto.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        Utilities.c_packColumn.packColumns(this.vista.jtProductos, 1);
        sumarTotal();
    }

    private void imprimir() {

        Object[] options = {"Ticket",
            "Boleta"};
        int n = JOptionPane.showOptionDialog(this.vista,
                "Eliga tipo de impresion",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        switch (n) {
            case 0: {
                //Ticket
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Impresora.imprimirTicketCompra(modelo.egreso_cabecera, modelo.egresoDetalles);
                    }
                });
                break;
            }
            case 1: {
                //Boleta
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Impresora.imprimirBoletaCompra(modelo.egreso_cabecera, modelo.egresoDetalles);
                    }
                });
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbSalir)) {
            cerrar();
        } else if (e.getSource().equals(this.vista.jbImprimir)) {
            imprimir();
        }
    }
}
