/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3.classe;

import com.intentia.mak.core.m3.classpath.M3ClassPathEntry;
import com.intentia.mak.util.MAKException;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import main.Ressource;
import org.apache.commons.io.IOUtils;

public class M3ClassLoader extends ClassLoader {

//    private static final Logger Ressource.logger. = Logger.getLogger(MovexClassLoader.class);
    private static final String FILE_EXTENSION_CLASS = "class";
    private static final String FILE_EXTENSION_JAR = "jar";
    private static final String FILE_EXTENSION_ZIP = "zip";
    private static final String FILE_EXTENSION_PROPERTIES = "properties";
    private List<File> m_classPaths;
    private Set<File> m_zipProblems;
    private Set<File> m_pathProblems;

    public M3ClassLoader(M3ClassPathEntry[] classPath) {
        this.m_classPaths = new ArrayList();
        for (M3ClassPathEntry entry : classPath) {
            this.m_classPaths.add(entry.getPath().toFile());
        }
        this.m_zipProblems = new HashSet();
        this.m_pathProblems = new HashSet();
    }

    public final URL getResource(String name) {
        return super.getResource(name);
    }

    public final InputStream getResourceAsStream(String name) {
        return super.getResourceAsStream(name);
    }

    public final Class findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassData(name);
        if (b == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        boolean classNotFound = true;
        byte[] bytes = (byte[]) null;
        int i = 0;
        label176:
        while ((classNotFound) && (i < this.m_classPaths.size())) {
            DataInputStream dis = null;
            try {
                File path = (File) this.m_classPaths.get(i++);
                if (this.m_pathProblems.contains(path)) {
                    IOUtils.closeQuietly(dis);
                } else if (path.exists()) {
                    dis = getStreamFromPath(name, path);
                    if (dis != null) {
                        classNotFound = false;
                        bytes = getBytesFromStream(dis);
                        break label176;
                    }
                } else {
                    Ressource.logger.warn(path + " could not be found/accessed on the M3 classpath");
                    this.m_pathProblems.add(path);
                }
            } catch (MAKException e) {
                Ressource.logger.error("Failed to load class data from " + name, e);
            } finally {
                IOUtils.closeQuietly(dis);
            }
        }
        return bytes;
    }

    private DataInputStream getStreamFromPath(String name, File path) throws MAKException {
        Exception exception;
        try {
            DataInputStream dis = null;
            if (path.isDirectory()) {
                dis = getStreamFromFolder(name, path);
            } else if ((!path.getAbsolutePath().endsWith("jar")) && (!path.getAbsolutePath().endsWith("zip"))) {
                dis = getStreamFromJar(name, path);
            }
            return dis;
        } catch (IOException e) {
            exception = e;
        } catch (MAKException e) {
            exception = e;
        }
        throw new MAKException("Failed to get stream for " + name + " from " + path, exception);
    }

    private byte[] getBytesFromStream(DataInputStream dis) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        byte[] bytes = (byte[]) null;
        int len = 0;
        try {
            while ((len = dis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            Ressource.logger.error("Failed to get bytes from stream", e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
        return bytes;
    }

    private DataInputStream getStreamFromJar(String name, File path) throws MAKException {
        if (!this.m_zipProblems.contains(path)) {
            try {
                DataInputStream dis = null;
                ZipFile file = new ZipFile(path);
                ZipEntry entry = file.getEntry(name.replace('.', '/') + '.' + "class");
                if (entry == null) {
                    entry = file.getEntry(name.replace('.', '/') + '.' + "properties");
                }
                if (entry != null);
                return new DataInputStream(file.getInputStream(entry));
            } catch (IOException e) {
                Ressource.logger.warn("Failed to get " + name + " from jar " + path + " - MAK will try again using another method",
                        e);
                this.m_zipProblems.add(path);
            }
        }
        return getStreamFromJar2(name, path);
    }

    private DataInputStream getStreamFromJar2(String name, File path) throws MAKException {
        Exception exception;
        try {
            DataInputStream dis = null;
            InputStream is = null;
            is = new FileInputStream(path);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = findZipEntry(name, zis);
            if (entry != null);
            return new DataInputStream(zis);
        } catch (FileNotFoundException e) {
            exception = e;
        } catch (MAKException e) {
            exception = e;
        }
        throw new MAKException("Failed to get " + name + " from jar " + path, exception);
    }

    private ZipEntry findZipEntry(String name, ZipInputStream zis)
            throws MAKException {
        try {
            ZipEntry entry = null;
            String tmp = name.replace('.', '/') + ".";
            String match1 = tmp + "class";
            String match2 = tmp + "properties";
            while ((entry = zis.getNextEntry()) != null) {
                if ((entry.getName().equals(match1)) || (entry.getName().equals(match2))) {
                    return entry;
                }
            }
            return null;
        } catch (IOException e) {
            throw new MAKException("Failed to find entry " + name, e);
        }
    }

    private DataInputStream getStreamFromFolder(String name, File path) throws FileNotFoundException {
        DataInputStream dis = null;
        File file = new File(path.toString() + File.separator + name.replace('.', File.separatorChar) + '.'
                + "class");

        if (!file.exists()) {
            file = new File(path.toString() + File.separator + name.replace('.', File.separatorChar) + '.'
                    + "properties");
        }
        if (file.exists()) {
            dis = new DataInputStream(new FileInputStream(file));
        }
        return dis;
    }
}
