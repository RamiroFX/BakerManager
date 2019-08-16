package Utilities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ramiro Ferreira
 */
public class CombertirNumeroATexto {

    static Map<Character, String> unidad;
    static Map<Character, String> decena;
    static Map<Character, String> centena;

    static final String MIL = "mil";
    static final String MILLON = "millon";

    static {
        decena = new HashMap();
        centena = new HashMap();
        unidad = new HashMap();
        unidad.put('0', "");
        unidad.put('1', "uno");
        unidad.put('2', "dos");
        unidad.put('3', "tres");
        unidad.put('4', "cuatro");
        unidad.put('5', "cinco");
        unidad.put('6', "seis");
        unidad.put('7', "siete");
        unidad.put('8', "ocho");
        unidad.put('9', "nueve");
        decena.put('1', "diez");
        decena.put('2', "veinte");
        decena.put('3', "treinta");
        decena.put('4', "cuarenta");
        decena.put('5', "cincuenta");
        decena.put('6', "sesenta");
        decena.put('7', "setenta");
        decena.put('8', "ochenta");
        decena.put('9', "noventa");
        centena.put('1', "ciento");
        centena.put('2', "doscientos");
        centena.put('3', "trecientos");
        centena.put('4', "cuatrocientos");
        centena.put('5', "quinientos");
        centena.put('6', "seiscientos");
        centena.put('7', "setecientos");
        centena.put('8', "ochocientos");
        centena.put('9', "novecientos");
    }

    /**
     * Devuelve el número en texto
     *
     * @param n
     * @return
     */
    public static String numberToText(Integer n) {
        char[] c = n.toString().toCharArray();
        String s = "";
        //Dependiendo del largo empezamos
        switch (c.length) {
            case 1:
                s = unidad.get(c[0]);
                break;
            case 2:
                s = decenas(n);
                break;
            case 3:
                s = centenas(n);
                break;
            case 4:
                s = mil(n);
                break;
            case 5:
                s = decena_mil(n);
                break;
            case 6:
                s = centena_mil(n);
                break;
            case 7:
                s = millon(n);
                break;
            default:
                System.out.println("Utilities.CombertirNumeroATexto.numberToText(): default");
                break;
        }
        return s;
    }

    private static String decenas(Integer n) {
        char[] c = n.toString().toCharArray();
        String s = "";
        if (n < 10) {
            s = unidad.get(n.toString().charAt(0));
        } else if (n == 10 || n % 10 == 0) {
            s = decena.get(c[0]);
        } else if (n >= 11 && n <= 15) {
            switch (n) {
                case 11:
                    s = "once";
                    break;
                case 12:
                    s = "doce";
                    break;
                case 13:
                    s = "trece";
                    break;
                case 14:
                    s = "catorce";
                    break;
                case 15:
                    s = "quince";
            }
        } else if (n > 20 && n < 30) {
            s = "veinti" + unidad.get(c[1]);
        } else {
            /**
             * n > 15
             */
            s = decena.get(c[0]) + " y " + unidad.get(c[1]);
        }
        return s;
    }

    private static String centenas(Integer n) {
        if (n < 100) {
            return decenas(n);
        } else {
            char[] c = n.toString().toCharArray();
            int d = Integer.parseInt(c[1] + "" + c[2]);
            String s = n == 100 ? "cien" : centena.get(c[0]) + " ";
            s += decenas(d);
            return s;
        }
    }

    private static String mil(Integer n) {
        if (n < 1000) {
            return centenas(n);
        } else {
            char[] c = n.toString().toCharArray();
            int d = Integer.parseInt(c[1] + "" + c[2] + "" + c[3]);
            String s = n == 1000 ? "mil" : ((c[0] == '1' ? "" : unidad.get(c[0]) + " ") + "mil " + centenas(d));
            return s;
        }
    }

    private static String decena_mil(Integer n) {
        if (n < 10000) {
            return mil(n);
        } else {
            char[] c = n.toString().toCharArray();
            int d = Integer.parseInt(c[0] + "" + c[1]);
            String s = decenas(d);
            s += " mil " + centenas(Integer.parseInt(c[2] + "" + c[3] + "" + c[4]));
            return s;
        }
    }

    private static String centena_mil(Integer n) {
        if (n < 100000) {
            return decena_mil(n);
        } else {
            char[] c = n.toString().toCharArray();
            int d = Integer.parseInt(c[0] + "" + c[1] + "" + c[2]);
            String s = centenas(d);
            s += " mil " + centenas(Integer.parseInt(c[3] + "" + c[4] + "" + c[5]));
            return s;
        }
    }

    private static String millon(Integer n) {
        String s;
        char[] c = n.toString().toCharArray();
        s = c[0] == '1' ? "un millón " : unidad.get(c[0]) + " millones ";
        int d = Integer.parseInt(c[1] + "" + c[2] + "" + c[3] + c[4] + "" + c[5] + "" + c[6]);
        s += centena_mil(d);
        return s;
    }
}
