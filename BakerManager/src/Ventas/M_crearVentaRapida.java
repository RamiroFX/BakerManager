/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import DB.DB_Cliente;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import Entities.M_funcionario;
import Entities.M_telefono;
import MenuPrincipal.DatosUsuario;
import Utilities.Impresora;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearVentaRapida {

    private M_facturaCabecera cabecera;
    private M_facturaDetalle detalle;
    private M_telefono telefono;
    private ArrayList<M_facturaDetalle> detalles;
    private DefaultTableModel dtm;

    public M_crearVentaRapida() {
        this.cabecera = new M_facturaCabecera();
        this.cabecera.setCliente(DB_Cliente.obtenerDatosClienteID(1));//mostrador
        this.cabecera.setIdCondVenta(1);//contado
        try {
            this.telefono = DB_Cliente.obtenerTelefonoCliente(this.cabecera.getCliente().getIdCliente()).get(1);
        } catch (Exception e) {
            this.telefono = null;
        }
        this.detalle = new M_facturaDetalle();
        this.detalles = new ArrayList<>();
        dtm = new DefaultTableModel();
        dtm.addColumn("ID");
        dtm.addColumn("Cantidad");
        dtm.addColumn("Descripción");
        dtm.addColumn("Precio");
        dtm.addColumn("Descuento");
        dtm.addColumn("Exenta");
        dtm.addColumn("IVA 5%");
        dtm.addColumn("IVA 10%");
    }

    public M_facturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(M_facturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public M_facturaDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(M_facturaDetalle detalle) {
        this.detalle = detalle;
    }

    public ArrayList<M_facturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<M_facturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public void validarDatos() {
    }

    public void insertarVenta() {
    }

    /**
     * @return the dtm
     */
    public DefaultTableModel getDtm() {
        return dtm;
    }

    /**
     * @param dtm the dtm to set
     */
    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public boolean guardarVenta() {
        if (getDetalles().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Seleccione por lo menos un artículo.", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            int response = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea confirmar la venta?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                int nroTicket = DB_Ingreso.insertarIngreso(getCabecera(), getDetalles());
                getCabecera().setIdFacturaCabecera(nroTicket);
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea imprimir el ticket?", "Atención", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    Impresora.imprimirVenta(DatosUsuario.getRol_usuario(), getCabecera(), getDetalles());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * @return the telefono
     */
    public M_telefono getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(M_telefono telefono) {
        this.telefono = telefono;
    }
}
