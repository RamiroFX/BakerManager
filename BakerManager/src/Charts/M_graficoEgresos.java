/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Charts;

import DB.DB_charts;
import Entities.M_funcionario;
import Entities.M_proveedor;
import java.sql.Timestamp;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_graficoEgresos {

    public PieDataset pieDataset;
    public M_proveedor proveedor;
    public M_funcionario funcionario;

    public M_graficoEgresos() {
    }
    

    public PieDataset establecerPieDataset(Timestamp inicio, Timestamp fin) {
        return this.pieDataset = DB_charts.obtenerComprasProveedores(inicio, fin);
    }
    
}
