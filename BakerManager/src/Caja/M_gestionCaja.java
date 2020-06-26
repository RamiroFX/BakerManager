package Caja;

import DB.DB_Caja;
import DB.ResultSetTableModel;
import Entities.Caja;
import Entities.CierreCaja;
import Entities.M_funcionario;
import Excel.ExportarCaja;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public String obtenerNombreFuncionario() {
        String alias = this.getFuncionario().getAlias();
        String nombre = this.getFuncionario().getNombre();
        String apellido = this.getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public ResultSetTableModel consultarCajas(int idFuncionario, Timestamp fecha_inicio, Timestamp fecha_fin) {
        return DB_Caja.consultarCajas(idFuncionario, fecha_inicio, fecha_fin, 1);
    }

    public void exportarExcel(Integer idFuncionario, Date fecha_inicio, Date fecha_fin) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha_inicio);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp fInicio = new Timestamp(calendario.getTimeInMillis());
        calendario.setTime(fecha_fin);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 59);
        java.sql.Timestamp fFin = new Timestamp(calendario.getTimeInMillis());
        ArrayList<CierreCaja> acd = DB_Caja.consultarCajasExportacion(idFuncionario, fInicio, fFin);
        ExportarCaja ec = new ExportarCaja(acd);
        ec.exportar();
    }

    public void exportarExcelMinimalista(Integer idFuncionario, Date fecha_inicio, Date fecha_fin) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha_inicio);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        java.sql.Timestamp fInicio = new Timestamp(calendario.getTimeInMillis());
        calendario.setTime(fecha_fin);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 59);
        java.sql.Timestamp fFin = new Timestamp(calendario.getTimeInMillis());
        ArrayList<CierreCaja> acd = DB_Caja.consultarCajasExportacion(idFuncionario, fInicio, fFin);
        ExportarCaja ec = new ExportarCaja(acd);
        ec.exportarMinimalista();
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

    public void anularCaja(Integer idCaja) {
        //id 2 es inactivo
        DB_Caja.cambiarEstadoCaja(idCaja, 2);
    }
}
