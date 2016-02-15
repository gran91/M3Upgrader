/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import com.app.main.Ressource;
import com.app.model.m3.M3ClassPathFileZ;
import com.app.process.Process;
import com.intentia.mak.core.m3.classpath.M3ClassPathEntry;
import java.io.IOException;
import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import com.intentia.mak.core.m3.foundation.M3Runtime;
import com.intentia.mak.core.m3.foundation.factory.M3RuntimeFactory;
import com.intentia.mak.core.movex.ConfigurationProperties;
import com.intentia.mak.core.movex.EnvironmentProperties;
import com.intentia.mak.core.movex.MovexSystem;
//import com.intentia.mak.core.movex.MovexSystemFactory;
import mak.core.movex.MovexSystemFactory;
import com.intentia.mak.core.util.FilePaths;
import com.intentia.mak.util.MAKException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import m3.classe.M3ClassLoader;
import model.entity.IEntityModel;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import znio.ProcessFile;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3Connector extends Observable {

    private final IEntityModel env;
    private EnvironmentProperties e;
    private M3Runtime m3Runtime;
    private MovexSystem mvxSys;
    private final LinkedHashMap<String, M3ConfigurationInfo> map = new LinkedHashMap<>();
    private final LinkedHashMap<String, ConfigurationProperties> mapConfigProp = new LinkedHashMap<>();
    private boolean initOK = false;
    private ClassLoader classloaderSpe, classLoaderStd;
    private List<Path> lstSpeClass, lstSpeSource;
    private List<Path> lstStdClass, lstStdSource;
    private ArrayList<M3ClassPathEntry> classPathAll, classPathSpe, classPathStd;
    private List<Path> sourcePathSpe, sourcePathStd;
    private List<Path> lstAllDBClass, lstAllDSClass;
    private String mainConfig = "MVX";
    private ArrayList<String> listUnderConfigSelect;

    public M3Connector(IEntityModel e) {
        env = e;
    }

    public void init() throws MAKException {
        if (env.getData() != null) {
            if (env.getData().isEmpty()) {
                env.loadData();
            }
            int port = 0;
            try {
                port = Integer.parseInt(env.getData().get(4));
            } catch (NumberFormatException e) {
            }

            e = new EnvironmentProperties();
            e.setServerAddress(env.getData().get(3));
            e.setServerPort(port);
            e.setRootPath(env.getData().get(7));

            ZUserCredentialsProvider credentialsProvider = new ZUserCredentialsProvider();
            credentialsProvider.setUserPwd(new UsernamePasswordCredentials(env.getData().get(5), env.getData().get(6)));
            m3Runtime = M3RuntimeFactory.getRuntime(e.getServerRootPath().toString(), e.getServerAddress(), e.getServerPort(), credentialsProvider);
            initOK = true;
        }
    }

    public LinkedHashMap<String, M3ConfigurationInfo> lstM3Config() throws IOException, MAKException {
        map.clear();
        if (m3Runtime != null) {
            M3ConfigurationInfo[] configurations = ensureStdConfiguration(m3Runtime.listConfigurations());
            for (M3ConfigurationInfo conf : configurations) {
                map.put(conf.getName(), conf);
            }
        }
        return map;
    }

    private M3ConfigurationInfo[] ensureStdConfiguration(M3ConfigurationInfo[] configurations) {
        for (M3ConfigurationInfo configuration : configurations) {
            if ("MVX".equals(configuration.getName())) {
                return configurations;
            }
        }
        M3ConfigurationInfo[] stdAdded = new M3ConfigurationInfo[configurations.length + 1];
        System.arraycopy(configurations, 0, stdAdded, 1, configurations.length);
        stdAdded[0] = new M3ConfigurationInfo();
        stdAdded[0].setName("MVX");
        stdAdded[0].setDescription("Standard Configuration");
        return stdAdded;
    }

    public M3ClassPathEntry[] listClassPathConfig(String config) throws MAKException {
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            ConfigurationProperties configurationProperties = new ConfigurationProperties();
            configurationProperties.setName(confInfo.getName());
            configurationProperties.setDescription(confInfo.getDescription());
            if (m3Runtime != null) {
                try {
                    m3Runtime.getClassPath(config);
                    configurationProperties.setClassPath(m3Runtime.getClassPath(configurationProperties.getName()));
                    MovexSystem mvxSys = MovexSystemFactory.newMovexSystem(e, configurationProperties, null);
                    return mvxSys.getClasspath();
                } catch (MAKException e) {
                    return classPathAppendToM3ClassPath(mapConfigProp.get(config).getClassPath());
                }
            }
        }
        return null;
    }

    public M3ClassPathEntry[] classPathAppendToM3ClassPath(String s) {
        String[] tab = s.split(";");
        M3ClassPathEntry[] lstClassPath = new M3ClassPathEntry[tab.length];
        for (int i = 0; i < tab.length; i++) {
            lstClassPath[i] = new M3ClassPathEntry(new M3ClassPathFileZ(env.getData().get(7)+tab[i]));
        }
        return lstClassPath;
    }

    public void addClassPathToConfig(String config, String a) {
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            ConfigurationProperties configurationProperties = new ConfigurationProperties();
            configurationProperties.setName(confInfo.getName());
            configurationProperties.setDescription(confInfo.getDescription());
            configurationProperties.setClassPath(a);
            mapConfigProp.put(config, configurationProperties);
        }
    }

    public M3ClassPathEntry[] addClassPathToConfig(String config, ArrayList<String> a) throws MAKException {
        M3ClassPathEntry[] lstClassPath = new M3ClassPathEntry[a.size()];
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            ConfigurationProperties configurationProperties = new ConfigurationProperties();
            configurationProperties.setName(confInfo.getName());
            configurationProperties.setDescription(confInfo.getDescription());
            if (m3Runtime != null) {
                m3Runtime.getClassPath(config);
                configurationProperties.setClassPath(m3Runtime.getClassPath(configurationProperties.getName()));
            } else {
                String path = "";

                for (int i = 0; i < a.size(); i++) {
                    path += a + ";";
                    lstClassPath[i] = new M3ClassPathEntry(new M3ClassPathFileZ(env.getData().get(7)+path));
                }
                configurationProperties.setClassPath(path);
            }
        }
        return lstClassPath;
    }

    public MovexSystem getM3System(String config) throws MAKException {
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            m3Runtime.getClassPath(config);
            ConfigurationProperties configurationProperties = new ConfigurationProperties();
            configurationProperties.setName(confInfo.getName());
            configurationProperties.setDescription(confInfo.getDescription());
            configurationProperties.setClassPath(m3Runtime.getClassPath(configurationProperties.getName()));
            return MovexSystemFactory.newMovexSystem(e, configurationProperties, null);
        }
        return null;
    }

    public ArrayList<Path> listSourcePathConfig(String config) throws MAKException {
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            try {
                m3Runtime.getClassPath(config);
                ConfigurationProperties configurationProperties = new ConfigurationProperties();
                configurationProperties.setName(confInfo.getName());
                configurationProperties.setDescription(confInfo.getDescription());
                configurationProperties.setClassPath(m3Runtime.getClassPath(configurationProperties.getName()));
                MovexSystem mvxSys = MovexSystemFactory.newMovexSystem(e, configurationProperties, null);
                ArrayList<Path> lstPath = new ArrayList<>();
                for (File f : mvxSys.getSourcePath().toFileArray()) {
                    lstPath.add(Paths.get(f.getAbsolutePath()));
                }
                return lstPath;
            } catch (MAKException e) {
                return M3ClassPathToSrcPath(classPathAppendToM3ClassPath(mapConfigProp.get(config).getClassPath()));
            }
        }
        return null;
    }

    public ArrayList<Path> M3ClassPathToSrcPath(M3ClassPathEntry[] tab) {
        ArrayList<Path> lstPath = new ArrayList<>();
        for (M3ClassPathEntry entry : tab) {
            lstPath.add(Paths.get(entry.getPath().toOSString().replaceAll("bindbg", "src")));
        }
        return lstPath;
    }

    public FilePaths listViewDefPathConfig(String config) throws MAKException {
        if (map.containsKey(config)) {
            M3ConfigurationInfo confInfo = map.get(config);
            m3Runtime.getClassPath(config);
            ConfigurationProperties configurationProperties = new ConfigurationProperties();
            configurationProperties.setName(confInfo.getName());
            configurationProperties.setDescription(confInfo.getDescription());
            configurationProperties.setClassPath(m3Runtime.getClassPath(configurationProperties.getName()));
            MovexSystem mvxSys = MovexSystemFactory.newMovexSystem(e, configurationProperties, null);
            return null;//mvxSys.getViewPath();
        }
        return null;
    }

    public ClassLoader buildClassLoader(String conf) throws MAKException {
        return buildClassLoader(listClassPathConfig(conf));
    }

    public ClassLoader buildClassLoader(M3ClassPathEntry[] urls) throws MAKException {
        return new M3ClassLoader(urls);
    }

    public ArrayList<ArrayList<M3ClassPathEntry>> parseClassPath(ArrayList<M3ClassPathEntry> classpath, String[] conf, String[] confSelect) throws MAKException {
        return parseClassPath(classpath.toArray(new M3ClassPathEntry[classpath.size()]), conf, confSelect);
    }

    public ArrayList<ArrayList<M3ClassPathEntry>> parseClassPath(M3ClassPathEntry[] classpath, String[] conf, String[] confSelect) throws MAKException {
        LinkedHashMap<String, M3ClassPathEntry[]> a = new LinkedHashMap<>();
        ArrayList<ArrayList<M3ClassPathEntry>> result = new ArrayList<>();
        a.put("", classpath);
        for (String s : conf) {
            a.put(s, listClassPathConfig(s));
        }

        for (String key : a.keySet()) {
            M3ClassPathEntry[] val = a.get(key);

            ArrayList<M3ClassPathEntry> array = new ArrayList<>();
            boolean test = false;
            boolean writeOk = true;

            for (M3ClassPathEntry m3 : val) {
                for (String keytemp : a.keySet()) {
                    if (test) {
                        M3ClassPathEntry[] valTemp = a.get(keytemp);
                        for (M3ClassPathEntry m3Temp : valTemp) {
                            if (m3.getPath().toString().equals(m3Temp.getPath().toString())) {
                                writeOk = false;
                                break;
                            }
                        }
                    }

                    if (keytemp.equals(key) || key.isEmpty()) {
                        test = true;
                    }
                }
                test = false;
                if (writeOk) {
                    array.add(m3);
                }
            }
            if (key.isEmpty() || new ArrayList<>(Arrays.asList(confSelect)).contains(key)) {
                result.add(new ArrayList(array));
            }
        }

        return result;
    }

    public ArrayList<ArrayList<Path>> parseSourcePath(ArrayList<Path> classpath, String[] conf, String[] confSelect) throws MAKException {
        LinkedHashMap<String, ArrayList<Path>> a = new LinkedHashMap<>();
        ArrayList<ArrayList<Path>> result = new ArrayList<>();
        a.put("", classpath);
        for (String s : conf) {
            a.put(s, listSourcePathConfig(s));
        }

        for (String key : a.keySet()) {
            ArrayList<Path> val = a.get(key);

            ArrayList<Path> array = new ArrayList<>();
            boolean test = false;
            boolean writeOk = true;

            for (Path m3 : val) {
                for (String keytemp : a.keySet()) {
                    if (test) {
                        ArrayList<Path> valTemp = a.get(keytemp);
                        for (Path m3Temp : valTemp) {
                            if (m3.toAbsolutePath().toString().equals(m3Temp.toAbsolutePath().toString())) {
                                writeOk = false;
                                break;
                            }
                        }
                    }

                    if (keytemp.equals(key) || key.isEmpty()) {
                        test = true;
                    }
                }
                test = false;
                if (writeOk) {
                    array.add(m3);
                }
            }
            if (key.isEmpty() || new ArrayList<String>(Arrays.asList(confSelect)).contains(key)) {
                result.add(new ArrayList(array));
            }
        }

        return result;
    }

    private List<Path> deleteClassNoNeed(List<Path> in, List<Path> out) {
        boolean insertOK = false;
        ArrayList<Path> lstClass = new ArrayList<>();
        ArrayList<String> lstNameClass = new ArrayList<>();
        for (Path in1 : in) {
            if (out != null) {
                if (!lstNameClass.contains(in1.getFileName().toString())) {
                    for (Path out1 : out) {
                        if (out1.getName(out1.getNameCount() - 1).toString().equals(in1.getName(in1.getNameCount() - 1).toString())) {
                            insertOK = true;
                            break;
                        }
                    }
                }
                if (lstClass.size() == out.size()) {
                    return lstClass;
                }
            } else if (!lstNameClass.contains(in1.getFileName().toString())) {
                insertOK = true;
            }
            if (insertOK) {
                lstClass.add(in1);
                lstNameClass.add(in1.getFileName().toString());
                insertOK = false;
            }
        }
        return lstClass;
    }

    private void checkClassLoaderIsNull() throws MAKException {
        if (classloaderSpe == null && classPathAll != null) {
            classloaderSpe = buildClassLoader(classPathAll.toArray(new M3ClassPathEntry[classPathAll.size()]));
            lstAllDBClass = new ArrayList<>();
            ArrayList<Path> all = new ArrayList();
            ProcessFile f = new ProcessFile();
            f.setExtFilter(".class");
            for (M3ClassPathEntry cl1 : classPathAll) {
                try {
                    Files.walkFileTree(Paths.get(cl1.getPath().toString()), f);
                } catch (IOException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                all.addAll(f.getList());
            }

            for (Path p : all) {
                if (M3Utils.getPgmType(p.toAbsolutePath().toString()).equals(Ressource.DB) && !p.getFileName().toString().contains("$Inst") && p.getFileName().toString().charAt(0) != 'd') {
                    lstAllDBClass.add(p);
                }
            }
        }
        if (classLoaderStd == null && classPathStd != null) {
            classLoaderStd = buildClassLoader(classPathStd.toArray(new M3ClassPathEntry[classPathStd.size()]));
        }
    }

    public boolean isInitOK() {
        return initOK;
    }

    public void setInitOK(boolean initOK) {
        this.initOK = initOK;
    }

    public ClassLoader getClassloaderSpe() {
        return classloaderSpe;
    }

    public void setClassloaderSpe(ClassLoader classloaderSpe) {
        this.classloaderSpe = classloaderSpe;
    }

    public ClassLoader getClassLoaderStd() {
        return classLoaderStd;
    }

    public void setClassLoaderStd(ClassLoader classLoaderStd) {
        this.classLoaderStd = classLoaderStd;
    }

    public List<Path> getLstSpeClass() {
        return lstSpeClass;
    }

    public void setLstSpeClass(List<Path> lstSpeClass) {
        this.lstSpeClass = lstSpeClass;
    }

    public List<Path> getLstSpeSource() {
        return lstSpeSource;
    }

    public void setLstSpeSource(List<Path> lstSpeSource) {
        this.lstSpeSource = lstSpeSource;
    }

    public List<Path> getLstStdClass() {
        return lstStdClass;
    }

    public void setLstStdClass(List<Path> lstStdClass) {
        this.lstStdClass = lstStdClass;
    }

    public List<Path> getLstStdSource() {
        return lstStdSource;
    }

    public void setLstStdSource(List<Path> lstStdSource) {
        this.lstStdSource = lstStdSource;
    }

    public ArrayList<M3ClassPathEntry> getClassPathAll() {
        return classPathAll;
    }

    public void setClassPathAll(M3ClassPathEntry[] classPathAll) throws MAKException {
        this.classPathAll = new ArrayList<>(Arrays.asList(classPathAll));
        checkClassLoaderIsNull();
    }

    public void setClassPathAll(ArrayList<M3ClassPathEntry> classPathAll) throws MAKException {
        this.classPathAll = classPathAll;
        checkClassLoaderIsNull();
    }

    public ArrayList<M3ClassPathEntry> getClassPathSpe() {
        return classPathSpe;
    }

    public void setClassPathSpe(ArrayList<M3ClassPathEntry> classPathSpe) {
        this.classPathSpe = classPathSpe;
        lstSpeClass = null;
        ArrayList<Path> all = new ArrayList();
        ProcessFile fileProcessor = new ProcessFile();
        fileProcessor.setExtFilter(".class");
        for (M3ClassPathEntry cl1 : classPathSpe) {
            try {
                Files.walkFileTree(Paths.get(cl1.getPath().toString()), fileProcessor);
            } catch (IOException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            all.addAll(fileProcessor.getList());
        }
        lstSpeClass = deleteClassNoNeed(all, lstSpeClass);
    }

    public ArrayList<M3ClassPathEntry> getClassPathStd() {
        return classPathStd;
    }

    public void setClassPathStd(ArrayList<M3ClassPathEntry> ClassPathStd) throws MAKException {
        this.classPathStd = ClassPathStd;
        lstStdClass = new ArrayList<>();
        ProcessFile fileProcessor = new ProcessFile();
        fileProcessor.setExtFilter(".class");
        for (M3ClassPathEntry cl1 : ClassPathStd) {
            try {
                Files.walkFileTree(Paths.get(cl1.getPath().toString()), fileProcessor);
            } catch (IOException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            lstStdClass.addAll(fileProcessor.getList());
        }
        checkClassLoaderIsNull();
    }

    public void setClassPathStd(M3ClassPathEntry[] classPathStd) throws MAKException {
        setClassPathStd(new ArrayList<>(Arrays.asList(classPathStd)));
    }

    public List<Path> getSourcePathSpe() {
        return sourcePathSpe;
    }

    public void setSourcePathSpe(List<Path> sourcePathSpe) {
        this.sourcePathSpe = sourcePathSpe;
        lstSpeSource = new ArrayList<>();
        ProcessFile fileProcessor = new ProcessFile();
        fileProcessor.setExtFilter(".java");
        for (Path cl1 : sourcePathSpe) {
            try {
                if (cl1.toFile().exists()) {
                    Files.walkFileTree(Paths.get(cl1.toAbsolutePath().toString()), fileProcessor);
                }
            } catch (IOException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            lstSpeSource.addAll(fileProcessor.getList());
        }
    }

    public List<Path> getSourcePathStd() {
        return sourcePathStd;
    }

    public void setSourcePathStd(List<Path> sourcePathStd) {
        this.sourcePathStd = sourcePathStd;
        lstStdSource = new ArrayList<>();
        ProcessFile fileProcessor = new ProcessFile();
        fileProcessor.setExtFilter(".java");
        for (Path cl1 : sourcePathStd) {
            try {
                if (cl1.toFile().exists()) {
                    Files.walkFileTree(Paths.get(cl1.toAbsolutePath().toString()), fileProcessor);
                }
            } catch (IOException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            lstStdSource.addAll(fileProcessor.getList());
        }
    }

    public List<Path> getLstAllDBClass() {
        return lstAllDBClass;
    }

    public void setLstAllDBClass(List<Path> lstAllDBClass) {
        this.lstAllDBClass = lstAllDBClass;
    }

    public List<Path> getLstAllDSClass() {
        return lstAllDSClass;
    }

    public void setLstAllDSClass(List<Path> lstAllDSClass) {
        this.lstAllDSClass = lstAllDSClass;
    }

    public M3Runtime getM3Runtime() {
        return m3Runtime;
    }

    public void setM3Runtime(M3Runtime m3Runtime) {
        this.m3Runtime = m3Runtime;
    }

    public String getMainConfig() {
        return mainConfig;
    }

    public void setMainConfig(String mainConfig) {
        this.mainConfig = mainConfig;
    }

    public ArrayList<String> getListUnderConfigSelect() {
        return listUnderConfigSelect;
    }

    public LinkedHashMap<String, M3ConfigurationInfo> getConfigMap() {
        return map;
    }

    public LinkedHashMap<String, ConfigurationProperties> getMapConfigProp() {
        return mapConfigProp;
    }

    public void setListUnderConfigSelect(LinkedHashMap<String, M3ConfigurationInfo> list) {
        this.listUnderConfigSelect = new ArrayList<>();
        for (Map.Entry<String, M3ConfigurationInfo> entry : list.entrySet()) {
            listUnderConfigSelect.add(entry.getKey());
        }
    }
}
