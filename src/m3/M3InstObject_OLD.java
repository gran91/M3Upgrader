/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import parser.JParser;

/**
 *
 * @author JCHAUT
 */
public class M3InstObject_OLD {

    private ClassLoader classloader;
    private File pathClass;
    private File pathSrc;
    private String type;
    private JParser parser;
    private boolean srcExist = false;
    
    public M3InstObject_OLD(ClassLoader cl, File path) {
        init(cl, path.getAbsolutePath());
    }

    public M3InstObject_OLD(ClassLoader cl, Path path) {
        init(cl, path.toAbsolutePath().toString());
    }

    public M3InstObject_OLD(ClassLoader cl, String path) {
        init(cl, path);
    }

    private void init(ClassLoader cl, String path) {
        classloader = cl;
        if (path.endsWith(".class")) {
            pathClass = new File(path);
            pathSrc = retrieveSrcOrClass(path);
        } else {
            pathClass = retrieveSrcOrClass(path);
            pathSrc = new File(path);
        }
        if (pathSrc.exists()) {
            srcExist = true;
        }
        type = M3Utils.getPgmType(pathClass.getAbsolutePath());
    }

    public void loadDataM3Object() throws japa.parser.TokenMgrError, Exception {
        if (srcExist) {
            parser = new JParser();
            parser.parseSource(pathSrc);
            parser.listMethod();
            parser.listField();
        }
    }

    public Class getM3Class() throws ClassNotFoundException {
        if (srcExist) {
            return classloader.loadClass(parser.getFormatPackage() + "." + pathClass.getName().replace(".class", ""));
        }
        return null;
    }

    public static ArrayList<String> getSimpleNameMethod(Method[] lst) {
        ArrayList<String> a = new ArrayList<>();
        for (Method lst1 : lst) {
            a.add(lst1.getName());
        }
        return a;
    }

    public static ArrayList<String> getSimpleNameMethod(ArrayList<Method> lst) {
        return getSimpleNameMethod(lst.toArray(new Method[lst.size()]));
    }

    private File retrieveSrcOrClass(String path) {
        if (path.endsWith(".class")) {
            return new File(path.replace("bindbg", "src").replace("binopt", "src").replace(".class", ".java"));
        } else {
            return new File(path.replace("src", "bindbg").replace(".java", ".class"));
        }
    }

    public ClassLoader getClassloader() {
        return classloader;
    }

    public void setClassloader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public File getPathClass() {
        return pathClass;
    }

    public void setPathClass(File pathClass) {
        this.pathClass = pathClass;
    }

    public File getPathSrc() {
        return pathSrc;
    }

    public void setPathSrc(File pathSrc) {
        this.pathSrc = pathSrc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JParser getParser() {
        return parser;
    }

    public void setParser(JParser parser) {
        this.parser = parser;
    }

    public boolean isSrcExist() {
        return srcExist;
    }

    public void setSrcExist(boolean srcExist) {
        this.srcExist = srcExist;
    }
}
