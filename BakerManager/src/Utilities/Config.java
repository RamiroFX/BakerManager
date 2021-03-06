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
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Config {

    public static final Properties propiedades;
    public static final String path;
    public static String usuario, lookAndFeel, traybar;
    public static FileOutputStream fos;
    public static FileInputStream fis;
    private static Writer w;
    private static Reader r;
    private boolean b;

    static {
        path = System.getProperty("user.dir") + "\\Assets\\newproperties.properties";
        propiedades = new Properties();
        try {
            fis = new FileInputStream(path);
            propiedades.load(fis);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + e, "Atención", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String getUser() {
        usuario = propiedades.getProperty("newproperties.usuario", "");
        return usuario;
    }

    public static void setUser(String user) {
        propiedades.setProperty("newproperties.usuario", user);
        try {
            w = new FileWriter(path);
            propiedades.store(w, user);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al cerrar el fichero: " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public static String getUi() {
        lookAndFeel = propiedades.getProperty("newproperties.lookAndFeel", "1");
        return lookAndFeel;
    }

    public static void setUi(int valor) {
        propiedades.setProperty("newproperties.lookAndFeel", String.valueOf(valor));
        try {
            w = new FileWriter(path);
            propiedades.store(w, String.valueOf(valor));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String getTraybar() {
        traybar = propiedades.getProperty("newproperties.traybar", "0");
        return traybar;
    }

    public static void setTraybar(String aTraybar) {
        propiedades.setProperty("newproperties.traybar", aTraybar);
        try {
            w = new FileWriter(path);
            propiedades.store(w, aTraybar);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String getHost() {
        usuario = propiedades.getProperty("newproperties.host", "localhost");
        return usuario;
    }

    public static void setHost(String host) {
        propiedades.setProperty("newproperties.host", host);
        try {
            w = new FileWriter(path);
            propiedades.store(w, host);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al cerrar el fichero: " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public static String getPort() {
        usuario = propiedades.getProperty("newproperties.port", "5432");
        return usuario;
    }

    public static void setPort(String port) {
        propiedades.setProperty("newproperties.port", port);
        try {
            w = new FileWriter(path);
            propiedades.store(w, port);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al cerrar el fichero: " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public static String getDB() {
        usuario = propiedades.getProperty("newproperties.database", "bakermanager");
        return usuario;
    }

    public static void setDB(String database) {
        propiedades.setProperty("newproperties.database", database);
        try {
            w = new FileWriter(path);
            propiedades.store(w, database);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido una excepcion al cerrar el fichero: " + ex, "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}
