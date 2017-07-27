package Caja;

import DB.DB_Caja;
import DB.ResultSetTableModel;
import Entities.Caja;
import Entities.CierreCaja;
import Entities.M_funcionario;
import Excel.ExportarCaja;
import Utilities.Impresora;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

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

    public void exportarExcel(Integer idFuncionario, Date fecha_inicio, Date fecha_fin) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha_inicio);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 000);
        java.sql.Timestamp fInicio = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        calendario.setTime(fecha_fin);
        calendario.set(Calendar.HOUR_OF_DAY, 24);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp fFin = java.sql.Timestamp.valueOf(sdfs.format(calendario.getTime()));
        ArrayList<CierreCaja> acd = DB_Caja.consultarCajasExportacion(idFuncionario, fInicio, fFin);
        ExportarCaja ec = new ExportarCaja("asd", acd);
        ec.exportar();
    }

    public boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
    }
}
