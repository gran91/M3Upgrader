/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package m3.classe;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 *
 * @author jeremy.chaut
 */
public class ClassUtils {

    public static ClassLoader createLoader(File filePath, ArrayList<String> list) throws MalformedURLException {
        return createLoader(filePath.getAbsolutePath(), list);
    }

    public static ClassLoader createLoader(String path, ArrayList<String> list) throws MalformedURLException {
        URL[] urlspe = new URL[list.size()];
        for (int i = 0; i < list.size(); i++) {
            urlspe[i] = new File(path + list.get(i)).toURL();
        }
        return new URLClassLoader(urlspe);
    }

    public static Method[] listMethod(ClassLoader loader, File f) throws ClassNotFoundException, NoClassDefFoundError {
        return listMethod(loader, getCompleteClassName(f));
    }

    public static Method[] listMethod(ClassLoader loader, String cl) throws ClassNotFoundException, NoClassDefFoundError {
        Class classspe = loader.loadClass(cl);
        return classspe.getDeclaredMethods();
    }

    public static Field[] listField(ClassLoader loader, File f) throws ClassNotFoundException, NoClassDefFoundError {
        return listField(loader, getCompleteClassName(f));
    }

    public static Field[] listField(ClassLoader loader, String cl) throws ClassNotFoundException, NoClassDefFoundError {
        Class classspe = loader.loadClass(cl);
        return classspe.getDeclaredFields();
    }

    public static Class getSuperClass(ClassLoader loader, File f) throws ClassNotFoundException, NoClassDefFoundError {
        return getSuperClass(loader, getCompleteClassName(f));
    }

    public static Class getSuperClass(ClassLoader loader, String cl) throws ClassNotFoundException, NoClassDefFoundError {
        Class classspe = loader.loadClass(cl);
        return classspe.getSuperclass();
    }

    public static String getCompleteClassName(File f) {
        String name = f.getName().replaceAll("\\.class", "");
        return getPackage(f) + name;
    }

    public static String getPackage(File f) {
        boolean test = true;
        String pack = "";
        while (test) {
            f = new File(f.getParent());
            if (!f.getName().equals(f.getName().toUpperCase()) && !f.getName().equals("base") && !f.getName().equals("binopt") && !f.getName().equals("bindbg")) {
                pack = f.getName() + "." + pack;
            } else {
                test = false;
            }
        }
        return pack;
    }
}
