/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Entities.E_facturaSinPago;
import Entities.E_utilizacionMateriaPrimaCabecera;
import Entities.Estado;
import Entities.M_funcionario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class DB_Cobro {

    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    
    public static List<E_facturaSinPago> consultarFacturasPendiente(int idCliente) {
        List<E_facturaSinPago> list = new ArrayList<>();
        String Query = "SELECT * FROM v_facturas_sin_pago WHERE id_cliente = ? ;";
        try {
            pst = DB_manager.getConection().prepareStatement(Query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();
            while (rs.next()) {
                E_facturaSinPago fsp = new E_facturaSinPago();
                fsp.setIdCabecera(rs.getInt("id_cabecera"));
                fsp.setIdCliente(rs.getInt("id_cliente"));
                fsp.setNroFactura(rs.getInt("nro_factura"));
                fsp.setClienteEntidad(rs.getString("cliente"));
                fsp.setFecha(rs.getTimestamp("fecha"));
                fsp.setMonto(rs.getInt("monto"));
                fsp.setPago(rs.getInt("pago"));
                fsp.setSaldo(rs.getInt("saldo"));
                fsp.setRuc(rs.getString("ruc"));
                fsp.setRuc(rs.getString("ruc_identificador"));
                list.add(fsp);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DB_Cobro.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return list;
    }
    
}
