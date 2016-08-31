/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import DB.DB_Cliente;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Utilities.Impresora;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class Test {

    public static void main(String[] args) throws SQLException {
        DB_manager.conectarBD("postgres", "postgres");
        M_facturaCabecera faca;
        M_cliente cliente;
        faca = DB_Ingreso.obtenerIngresoCabeceraID(1955);
        cliente = DB_Cliente.obtenerDatosClienteID(faca.getIdCliente());
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(faca.getIdFuncionario());
        faca.setCliente(cliente);
        faca.setFuncionario(funcionario);
        Impresora.imprimirVentaGuardada(DatosUsuario.getRol_usuario(), faca);
    }
}
