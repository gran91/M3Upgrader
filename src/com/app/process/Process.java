package com.app.process;

import com.app.model.m3.M3UpgradModel;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.app.ui.m3.UIM3Analyze;
import com.intentia.mak.core.m3.classpath.M3ClassPathEntry;
import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import com.intentia.mak.util.MAKException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import m3.M3Connector;
import m3.M3UpdObjModel;
import m3.M3UpgradObject;
import mak.db.MetaDataDef;
import model.entity.IEntityModel;
import ui.tools.BusyDialog;
import znio.ProcessFile;

/**
 *
 * @author Jeremy.CHAUT
 */
public class Process implements Runnable, Observer {

    private final BusyDialog dial;
    private List<Path> lstSpe = null, lstStd = null;
    private final M3UpgradModel model;
    private Object[][] data;
    private final IEntityModel envSrc, envTarg;
    private final M3Connector m3Src, m3Targ;
    private String action;
    public final String ANALYSE = "analyze";
    public final String MIGRATE = "migrate";
    private long runtime = 0l;
    public static Logger log;
    private File tempDir;
    private M3UpgradObject m3updobj;

    public Process(BusyDialog d, M3UpgradModel m) {
        dial = d;
        model = m;
        envSrc = model.getListEntity().get(0).getEntity().getModelEntity();
        envTarg = model.getListEntity().get(1).getEntity().getModelEntity();
        envSrc.loadData();
        envTarg.loadData();

        if (model.getConfigM3Model().getConfig() != null) {
            m3Src = model.getConfigM3Model().getConfig();
        } else {
            m3Src = new M3Connector(envSrc);
        }
        m3Src.setMainConfig(model.getConfigM3Model().getConfigSelect());
        m3Src.setListUnderConfigSelect(model.getConfigM3Model().getListUnderConfigSelect());
        m3Src.addObserver(this);
        m3Targ = new M3Connector(envTarg);
        try {
            m3Targ.init();
        } catch (MAKException ex) {
            Logger.getLogger(ProcessField.class.getName()).log(Level.SEVERE, null, ex);
        }
        m3Targ.addObserver(this);
        tempDir = new File("D:\\tempMigration" + System.getProperty("file.separator") + System.currentTimeMillis());
        tempDir.mkdirs();
        log = Logger.getLogger("processLogger");
    }

    public void analyse() {
        long begtime = System.currentTimeMillis();
        if (loadClassPath()) {
            lstSpe = m3Src.getLstSpeClass();
            dial.dialStatus();
            if (dial.getStatus() == dial.STOP) {
                return;
            }

            if (lstSpe != null && !lstSpe.isEmpty()) {
                ArrayList<M3UpdObjModel> allDataSpe = new ArrayList<>();
                dial.dialStatus();
                if (dial.getStatus() == dial.STOP) {
                    return;
                }

                dial.setText("Démarrage du traitement des classes spécifiques...");

                int nbSpec = 0;
                for (Path lstSpe1 : lstSpe) {
                    dial.dialStatus();
                    if (dial.getStatus() == dial.STOP) {
                        return;
                    }
                    nbSpec++;
                    dial.setText("Traitement de " + lstSpe1.getFileName().toString() + "(" + nbSpec + "/" + lstSpe.size() + ")");

                    M3UpdObjModel obj = m3updobj.analyseM3Object(lstSpe1);
                    if (obj != null) {
                        allDataSpe.add(obj);
                    }
                }

//data = new Object[allDataSpe.size()][model.getHeader().length];
                data = new Object[allDataSpe.size()][M3UpdObjModel.header.length];
                for (int i = 0; i < allDataSpe.size(); i++) {
                    data[i] = allDataSpe.get(i).getTableData();
                }
                runtime = Math.abs(System.currentTimeMillis() - begtime);
                model.setData(data);
                if (runtime != 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(com.app.main.Ressource.mainView.getWindow(), "Le temps d'exécution de l'analyse est de " + tools.ToolString.getDurationBreakdown(runtime), "Process", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                if (data != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            UIM3Analyze m3 = new UIM3Analyze(model);
                            ui.tools.UITools.createDialog(com.app.main.Ressource.mainView.getWindow(), com.app.main.Ressource.title, m3);
                        }
                    });
                }

            } else {
                dial.setText("Aucune classe spécifique n'a été détectée!");
                dial.stop();
            }
            dial.stop();
        } else {
            dial.stop();
        }
    }

    public void process() {
        long begtime = System.currentTimeMillis();
        if (loadClassPath()) {
            lstSpe = m3Src.getLstSpeClass();
            dial.dialStatus();
            if (dial.getStatus() == dial.STOP) {
                return;
            }

            if (lstSpe != null && !lstSpe.isEmpty()) {
                ArrayList<M3UpdObjModel> allDataSpe = new ArrayList<>();
                dial.dialStatus();
                if (dial.getStatus() == dial.STOP) {
                    return;
                }

                dial.setText("Démarrage du traitement des classes spécifiques...");

                int nbSpec = 0;
                for (Path lstSpe1 : lstSpe) {
                    dial.dialStatus();
                    if (dial.getStatus() == dial.STOP) {
                        return;
                    }
                    nbSpec++;
                    dial.setText("Traitement de " + lstSpe1.getFileName().toString() + "(" + nbSpec + "/" + lstSpe.size() + ")");

                    M3UpdObjModel obj = m3updobj.processM3Object(lstSpe1);
                    if (obj != null) {
                        allDataSpe.add(obj);
                    }
                }

                 // data = new Object[allDataSpe.size()][model.getHeader().length];
                data = new Object[allDataSpe.size()][M3UpdObjModel.header.length];
                for (int i = 0; i < allDataSpe.size(); i++) {
                    data[i] = allDataSpe.get(i).getTableData();
                }
                runtime = Math.abs(System.currentTimeMillis() - begtime);
                model.setData(data);
                if (runtime != 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(com.app.main.Ressource.mainView.getWindow(), "Le temps d'exécution de l'analyse est de " + tools.ToolString.getDurationBreakdown(runtime), "Process", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                if (data != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            UIM3Analyze m3 = new UIM3Analyze(model);
                            ui.tools.UITools.createDialog(com.app.main.Ressource.mainView.getWindow(), com.app.main.Ressource.title, m3);
                        }
                    });
                }

            } else {
                dial.setText("Aucune classe spécifique n'a été détectée!");
                dial.stop();
            }
            dial.stop();
        } else {
            dial.stop();
        }
    }

    public void listField() {
        ArrayList<String> excluTable = new ArrayList<>();
        long begtime = System.currentTimeMillis();
        if (loadClassPath()) {
            List<Path> lst = m3Src.getLstAllDBClass();
            dial.dialStatus();
            if (dial.getStatus() == dial.STOP) {
                return;
            }

            if (lst != null && !lst.isEmpty()) {
                HashMap<String, ArrayList<String[]>> map = new HashMap<>();
                dial.dialStatus();
                if (dial.getStatus() == dial.STOP) {
                    return;
                }

                dial.setText("Démarrage de la recherche des champs...");

                int nbSpec = 0;
                NEXT:
                for (Path lst1 : lst) {
                    if (excluTable.contains(lst1.getFileName().toString())) {
                        continue NEXT;
                    } else {
                        excluTable.add(lst1.getFileName().toString());
                    }
                    dial.dialStatus();
                    if (dial.getStatus() == dial.STOP) {
                        return;
                    }
                    nbSpec++;
                    dial.setText("Traitement de " + lst1.getFileName().toString() + "(" + nbSpec + "/" + lst.size() + ")");
                    MetaDataDef defDB = new MetaDataDef(m3Src.getClassloaderSpe(), lst1.getFileName().toString().replace(".class", ""));
                    String[][] temp = defDB.getTable().getFieldMetaData();
                    for (String[] tempField : temp) {
                        boolean test = true;
                        if (tempField != null) {
                            if (tempField[0] == null) {
                                break;
                            }
                            if (tempField.length > 12) {
                                String[] t = new String[5];
                                t[0] = tempField[12];
                                t[1] = tempField[2];
                                t[2] = tempField[3];
                                t[3] = tempField[4];
                                t[4] = tempField[5];
                                ArrayList<String[]> listFields = new ArrayList<>();
                                if (map.containsKey(t[0])) {
                                    listFields = map.get(t[0]);
                                    for (String[] s : listFields) {
                                        if (Arrays.toString(s).equals(Arrays.toString(t))) {
                                            test = false;
                                            break;
                                        }
                                    }
                                }
                                if (test) {
                                    map.remove(t[0]);
                                    listFields.add(t);
                                    map.put(t[0], listFields);
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }

                runtime = Math.abs(System.currentTimeMillis() - begtime);
                if (runtime != 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(com.app.main.Ressource.mainView.getWindow(), "Le temps d'exécution de l'analyse est de " + tools.ToolString.getDurationBreakdown(runtime), "Process", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
            } else {
                dial.setText("Aucune classe spécifique n'a été détectée!");
                dial.stop();
            }
            dial.stop();
        } else {
            dial.stop();
        }
    }

    private boolean loadClassPath() {
        dial.setText("Vérification de connexion à l'environnement");
        if (m3Src.isInitOK()) {
            dial.setText("Récupération du classpath spécifique...");
            try {
                M3ClassPathEntry[] classpathAllSpe = m3Src.listClassPathConfig(model.getConfigM3Model().getConfigSelect());
                ArrayList<ArrayList<M3ClassPathEntry>> allClass = m3Src.parseClassPath(classpathAllSpe, model.getConfigM3Model().getListUnderConfig().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfig().size()]), model.getConfigM3Model().getListUnderConfigSelect().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfigSelect().size()]));
                ArrayList<M3ClassPathEntry> classpathStd = new ArrayList<>();

                if (allClass.size() > 1) {
                    for (int j = 1; j < allClass.size(); j++) {
                        classpathStd.addAll(allClass.get(j));
                        dial.dialStatus();
                    }
                } else {
                    classpathStd.addAll(allClass.get(0));
                }

                m3Src.setClassPathAll(classpathAllSpe);
                m3Src.setClassPathSpe(allClass.get(0));
                m3Src.setClassPathStd(classpathStd);

                ArrayList<Path> sourceAllSpe = m3Src.listSourcePathConfig(model.getConfigM3Model().getConfigSelect());
                ArrayList<ArrayList<Path>> allsrc = m3Src.parseSourcePath(sourceAllSpe, model.getConfigM3Model().getListUnderConfig().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfig().size()]), model.getConfigM3Model().getListUnderConfigSelect().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfigSelect().size()]));
                ArrayList<Path> sourcepathStd = new ArrayList<>();
                if (allsrc.size() > 1) {
                    for (int j = 1; j < allsrc.size(); j++) {
                        sourcepathStd.addAll(allsrc.get(j));
                        dial.dialStatus();
                    }
                } else {
                    sourcepathStd.addAll(allsrc.get(0));
                }
                m3Src.setSourcePathSpe(allsrc.get(0));
                m3Src.setSourcePathStd(sourcepathStd);

                m3Targ.setInitOK(false);
                if (m3Targ.isInitOK()) {
                    try {
                        LinkedHashMap<String, M3ConfigurationInfo> lstTargConfig = m3Targ.lstM3Config();
                        M3ClassPathEntry[] classpathStdTarg = m3Targ.listClassPathConfig(lstTargConfig.keySet().iterator().next());
                        m3Targ.setClassPathStd(classpathStdTarg);
                    } catch (IOException ex) {
                        Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (MAKException ex) {
                dial.setError(ex.getMessage());
                return false;
            }
            if (m3Targ.isInitOK()) {
                m3updobj = new M3UpgradObject(m3Src, m3Targ);
            } else {
                m3updobj = new M3UpgradObject(m3Src);
            }
            m3updobj.setMigrateDirectory(tempDir);
            return true;
        } else {
            dial.setText("L'environnement n'est pas accessible!");
            return false;
        }
    }

    public ArrayList<Path> getListFileClass(ArrayList<M3ClassPathEntry> lstDirClass) {
        boolean insertOK = false;
        File f = null;
        ArrayList<Path> lstClass = new ArrayList<>();
        ArrayList<String> lstNameClass = new ArrayList<>();
        for (int i = 0; i < lstDirClass.size(); i++) {
            f = lstDirClass.get(i).getPath().toFile();
            if (f.exists() && f.isDirectory()) {
                System.out.println("Récupération des classes de " + f.getPath());
                dial.setText("Récupération des classes de " + f.getPath());
                ProcessFile fileProcessor = new ProcessFile();
                fileProcessor.setExtFilter(".class");
                try {
                    Files.walkFileTree(Paths.get(f.getAbsolutePath()), fileProcessor);
                } catch (IOException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Path> lstFile = fileProcessor.getList();
                if (dial.getStatus() == dial.STOP) {
                    return null;
                } else if (dial.getStatus() == dial.PAUSE) {
                    while (dial.getStatus() == dial.PAUSE) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            com.app.main.Ressource.logger.error(ex.getLocalizedMessage(), ex);
                        }
                    }
                }
                for (int j = 0; j < lstFile.size(); j++) {
                    if (lstSpe != null) {
                        if (!lstNameClass.contains(lstFile.get(j).getFileName().toString())) {
                            for (int k = 0; k < lstSpe.size(); k++) {
                                if (lstSpe.get(k).getName(lstSpe.get(k).getNameCount() - 1).toString().equals(lstFile.get(j).getName(lstFile.get(j).getNameCount() - 1).toString())) {
                                    insertOK = true;
                                    break;
                                }
                            }
                        }
                        if (lstClass.size() == lstSpe.size()) {
                            return lstClass;
                        }
                    } else if (!lstNameClass.contains(lstFile.get(j).getFileName().toString())) {
                        insertOK = true;
                    }
                    if (insertOK) {
                        lstClass.add(lstFile.get(j));
                        lstNameClass.add(lstFile.get(j).getFileName().toString());
                        insertOK = false;
                    }
                    if (dial.getStatus() == dial.STOP) {
                        return null;
                    } else if (dial.getStatus() == dial.PAUSE) {
                        while (dial.getStatus() == dial.PAUSE) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                com.app.main.Ressource.logger.error(ex.getLocalizedMessage(), ex);
                            }
                        }
                    }
                }
            }
        }
        return lstClass;
    }

    @Override
    public void run() {
        if (action.equals("analyze")) {
            analyse();
        }
        if (action.equals("listfield")) {
            listField();
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg.toString().equals("dial")) {
                dial.setText("");
            }
        }
    }

}
