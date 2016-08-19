/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author Ramiro
 */
import java.awt.Desktop;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSize;

/**
 *
 * @author Administrador
 */
public class Impresora {

    public static void imprimirCocina(String textoAImprimir) {
        agregarLogoATicket(textoAImprimir);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion

        byte[] bytes = textoAImprimir.getBytes();

//Especificamos el tipo de dato a imprimir
//Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Doc doc = new SimpleDoc(bytes, flavor, null);
//Creamos un trabajo de impresión
        DocPrintJob job = null;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                
                if (services[i].getName().equals("cocina")) {//aqui escribimos/elegimos la impresora por la que queremos imprimir
                    //manejar error en caso de que no esté conectada
                    //Desktop.getDesktop().print(null);//para imprimir un archivo ya existente  
                    job = services[i].createPrintJob();// System.out.println(i+": "+services[i].getName());

                } else {
                    System.out.println("No se encontró la impresora cocina");
                }
            }

            PrinterJob pj = PrinterJob.getPrinterJob();

            PageFormat mPageFormat = pj.defaultPage();
            //pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    //PrintService service = pj.getPrintService();//PrintServiceLookup.lookupDefaultPrintService();
                    pj.print();
                    //DocPrintJob pjb;
                    // job = service.createPrintJob();
                } catch (PrinterException ex) {
                    Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

//Imprimimos dentro de un try obligatoriamente
//        try {
//            if (job != null) {
//                job.print(doc, null);
//            }
//        } catch (PrintException ex) {
//            System.out.println(ex);
//        }
    }

    public static void imprimirCaja(String textoAImprimir) {
        agregarLogoATicket(textoAImprimir);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion

        byte[] bytes = textoAImprimir.getBytes();

//Especificamos el tipo de dato a imprimir
//Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        Doc doc = new SimpleDoc(bytes, flavor, null);
//Creamos un trabajo de impresión
        DocPrintJob job = null;
        if (services.length > 0) {
//            for (int i = 0; i < services.length; i++) {
//                if (services[i].getName().equals("caja")) {//aqui escribimos/elegimos la impresora por la que queremos imprimir
//                    job = services[i].createPrintJob();// System.out.println(i+": "+services[i].getName());
//                }
//            }
            for (PrintService service : services) {
                if (service.getName().equals("caja")) {
                    //aqui escribimos/elegimos la impresora por la que queremos imprimir
                    job = service.createPrintJob(); // System.out.println(i+": "+services[i].getName());
                }
            }
        }

//Imprimimos dentro de un try obligatoriamente
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            System.out.println(ex);
        }

    }

    public static void imprimirGenerico(String contentTicket) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); //nos da el array de los servicios de impresion
//Creamos un arreglo de tipo byte 
//y le agregamos el string convertido (cuerpo del ticket) a bytes tal como 
//lo maneja la impresora(mas bien ticketera :p)
        byte[] bytes = contentTicket.getBytes(); //Especificamos el tipo de dato a imprimir 
//Tipo: bytes; Subtipo: autodetectado 
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null); //Creamos un trabajo de impresión
        DocPrintJob job = null;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals("GP-5890X Series")) {
//aqui escribimos/elegimos la impresora por la que queremos imprimir 
                    job = services[i].createPrintJob();
                    System.out.println(i + ": " + services[i].getName());
                }
            }
        }
//Imprimimos dentro de un try obligatoriamente 
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            System.out.println(ex);
        }
    }

    //TODO arreglo de impresoras de caja
    //TODO arreglo de impresoras de cocina
    public static void main(String[] args) {
        //ServicioDeImpresion impresion = new ServicioDeImpresion();
        Impresora.imprimirGenerico("hola mundo!");

    }

    private static String agregarLogoATicket(String textoAImprimir) {
        //TODO hacer pruebas de impresión de logo
        //TODO tal vez también se necesite imprimir una parte común al final
        return textoAImprimir;
    }
}
