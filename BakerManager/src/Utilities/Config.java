/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 *
 * @author Administrador
 */
public class Config {
    public static final Properties propiedades;
    public static final  String path;
    public static String usuario,lookAndFeel,traybar;
    private static String baseDatos;
    private static String driver;
    private static String IP;
    private static String puerto;
    private static String dbms;
    public static FileOutputStream fos;
    public static FileInputStream fis;
    private static  Writer w;
    private static  Reader r;
    private boolean b;
    static  {
        path=System.getProperty("user.dir") + "\\src\\Assets\\newproperties.properties";
        System.out.println(path);
        propiedades = new Properties();
        try {
            fis=new FileInputStream(path);
            propiedades.load(fis);
        } catch (Exception e) {
            System.out.println("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + e);
        }finally{
            if( null != fis ){ 
                try { 
                    fis.close(); 
                }catch (IOException ex) {
                        ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the baseDatos
     */
    public static String getBaseDatos() {
        baseDatos=propiedades.getProperty("newproperties.baseDatos","xe");
        return baseDatos;
    }

    /**
     * @param aBaseDatos the baseDatos to set
     */
    public static void setBaseDatos(String aBaseDatos) {
        propiedades.setProperty("newproperties.baseDatos", aBaseDatos);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aBaseDatos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the driver
     */
    public static String getDriver() {
        driver=propiedades.getProperty("newproperties.driver","jdbc:oracle:thin:@");
        return driver;
    }

    /**
     * @param aDriver the driver to set
     */
    public static void setDriver(String aDriver) {
        propiedades.setProperty("newproperties.driver", aDriver);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aDriver);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the IP
     */
    public static String getIP() {
        IP=propiedades.getProperty("newproperties.IP","localhost:");
        return IP;
    }

    /**
     * @param aIP the IP to set
     */
    public static void setIP(String aIP) {
        propiedades.setProperty("newproperties.IP", aIP);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aIP);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the puerto
     */
    public static String getPuerto() {
        puerto=propiedades.getProperty("newproperties.puerto","1521:");
        return puerto;
    }

    /**
     * @param aPuerto the puerto to set
     */
    public static void setPuerto(String aPuerto) {
        propiedades.setProperty("newproperties.puerto", aPuerto);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aPuerto);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the dbms
     */
    public static String getDbms() {
        dbms=propiedades.getProperty("newproperties.dbms","Oracle");
        return dbms;
    }

    /**
     * @param aDbms the dbms to set
     */
    public static void setDbms(String aDbms) {
        propiedades.setProperty("newproperties.dbms", aDbms);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aDbms);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public  static String getUser() {
        usuario=propiedades.getProperty("newproperties.usuario","username");
        return usuario;
    }
    public static void setUser(String user){
          propiedades.setProperty("newproperties.usuario", user);
        try {
            w= new FileWriter(path);
            propiedades.store(w, user);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static void  setUi(int valor) {
        propiedades.setProperty("newproperties.lookAndFeel", String.valueOf(valor));
        try {
            w= new FileWriter(path);
            propiedades.store(w, String.valueOf(valor));
        } catch (IOException ex) {
             ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
        public static String getUi() {
            lookAndFeel=propiedades.getProperty("newproperties.lookAndFeel","1");
        return lookAndFeel;
    }
        /**
     * @return the traybar
     */
    public static String getTraybar() {
        traybar=propiedades.getProperty("newproperties.traybar","0");
        return traybar;
    }

    /**
     * @param aTraybar the traybar to set
     */
    public static void setTraybar(String aTraybar) {
        propiedades.setProperty("newproperties.traybar", aTraybar);
        try {
            w= new FileWriter(path);
            propiedades.store(w, aTraybar);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(w!=null){
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}



