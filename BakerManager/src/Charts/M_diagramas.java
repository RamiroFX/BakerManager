/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import DB.DB_charts;
import Entities.M_cliente;
import Entities.M_funcionario;
import Pedido.M_gestionPedido;
import java.sql.Timestamp;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_diagramas {

    public PieDataset pieDataset;
    public M_cliente cliente;
    public M_funcionario funcionario;
    public M_gestionPedido modelo;

    public M_diagramas() {
    }

    public M_diagramas(M_gestionPedido modelo) {
        this.modelo = modelo;
    }

    public PieDataset establecerPieDataset(Timestamp inicio, Timestamp fin) {
        return this.pieDataset = DB_charts.obtenerComprasClientes(inicio, fin);
    }
}
