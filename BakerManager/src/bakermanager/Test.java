/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakermanager;

import Caja.V_SaldarCaja;
import Charts.DiagramaVentaPorTiempo;
import Charts.Diagramas;
import Cobros.Retencion.GestionRetencion;
import DB.DB_Caja;
import DB.DB_Cliente;
import DB.DB_Cobro;
import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import DB.DB_Pago;
import DB.DB_manager;
import Entities.Caja;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_facturaCabecera;
import Entities.E_reciboPagoCabecera;
import Entities.M_cliente;
import Entities.M_egreso_cabecera;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import Excel.ExportarCaja;
import MenuPrincipal.DatosUsuario;
import Parametros.PedidoEstado;
import Pedido.C_gestionPedido;
import Pedido.GestionPedidos;
import Pedido.M_gestionPedido;
import Pedido.V_gestionPedido;
import Impresora.Impresora;
import Interface.MovimientosCaja;
import NotasCredito.GestionNotasCredito;
import Producto.ProductoParametros;
import Producto.pamela.CrearProducto;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramiro Ferreira
 */
public class Test {

    private final static SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) throws SQLException {

        testearCrearProducto();
        //testearDiagramaPedido();
        /*DB_manager.conectarBD("postgres", "postgres");
        M_facturaCabecera faca;
        M_cliente cliente;
        faca = DB_Ingreso.obtenerIngresoCabeceraID(1955);
        cliente = DB_Cliente.obtenerDatosClienteID(faca.getIdCliente());
        M_funcionario funcionario = DB_Funcionario.obtenerDatosFuncionarioID(faca.getIdFuncionario());
        faca.setCliente(cliente);
        faca.setFuncionario(funcionario);
        Impresora.imprimirVentaGuardada(DatosUsuario.getRol_usuario(), faca);
        Date today = Calendar.getInstance().getTime();
        String fechaEntrega = sdfs.format(today);
        System.out.println("fecha: " + fechaEntrega);*/
    }

    public static void testearDiagramaPedido() {
        Inicio i = new Inicio();
        i.conectarBD();
        C_inicio c_inicio = i.controlador;
        M_gestionPedido modelo = new M_gestionPedido();
        V_gestionPedido vista = new V_gestionPedido(c_inicio.vista);
        C_gestionPedido gestionPedidos = new C_gestionPedido(modelo, vista, c_inicio);
        /*Diagramas d = new Diagramas(gestionPedidos);
        d.mostrarVista();*/
        DiagramaVentaPorTiempo diagramaVentaPorTiempo = new DiagramaVentaPorTiempo(i.vista);
        diagramaVentaPorTiempo.mostrarVista();
    }

    public static void testearInformeCierreCaja() {
        Inicio i = new Inicio();
        i.conectarBD();

        Caja caja = DB_Caja.obtenerCaja(2);
        MovimientosCaja movimientosCaja = new MovimientosCaja();
        movimientosCaja.setMovimientoVentas((ArrayList<E_facturaCabecera>) DB_Ingreso.obtenerMovimientoVentasCabeceras(caja.getIdCaja()));
        movimientosCaja.setMovimientoCompras((ArrayList<M_egreso_cabecera>) DB_Egreso.obtenerMovimientoComprasCabeceras(caja.getIdCaja()));
        movimientosCaja.setMovimientoCobros((ArrayList<E_cuentaCorrienteCabecera>) DB_Cobro.obtenerMovimientoCobrosCabeceras(caja.getIdCaja()));
        movimientosCaja.setMovimientoPagos((ArrayList<E_reciboPagoCabecera>) DB_Pago.obtenerMovimientoPagosCabeceras(caja.getIdCaja()));
        ExportarCaja ec = new ExportarCaja();

        ec.exportarInformeCierreCaja(caja, movimientosCaja);
    }

    public static void testearGestionNotaCredito() {
        Inicio i = new Inicio();
        i.conectarBD();

        GestionNotasCredito gnc = new GestionNotasCredito(i.controlador);
        gnc.mostrarVista();
    }

    public static void testearParametrosProducto() {
        Inicio i = new Inicio();
        i.conectarBD();
        ProductoParametros param = new ProductoParametros(i.controlador);
        param.setVisible(true);
    }
    
    public static void testearRetencionVentas(){
        Inicio i = new Inicio();
        i.conectarBD();

        GestionRetencion gnc = new GestionRetencion(i.controlador);
        gnc.mostrarVista();
        
    }
    public static void testearCrearProducto(){
        Inicio i = new Inicio();
        i.conectarBD();

        CrearProducto gnc = new CrearProducto(i.controlador.vista);
        gnc.mostrarVista();
        
    }
}
