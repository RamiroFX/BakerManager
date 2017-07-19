package Caja;

import DB.DB_Caja;
import DB.ResultSetTableModel;
import Entities.Caja;
import Entities.M_funcionario;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionCaja {

    private Caja caja;

    private M_funcionario funcionario;

    public M_gestionCaja() {
        this.caja = new Caja();
        this.funcionario = new M_funcionario();
    }

    public M_gestionCaja(Caja caja, M_funcionario funcionario) {
        this.caja = caja;
        this.funcionario = funcionario;
    }

    public void borrarDatos() {
        this.funcionario = null;
        this.caja = null;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public ResultSetTableModel consultarCajas(int idFuncionario, String fecha_inicio, String fecha_fin) {
        return DB_Caja.consultarCajas(idFuncionario, fecha_inicio, fecha_fin);
    }

}
