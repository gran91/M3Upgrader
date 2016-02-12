package com.app.process;

import com.app.model.m3.M3UpgradModel;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.app.ui.m3.UIM3Analyze;
import com.intentia.mak.core.m3.classpath.M3ClassPathEntry;
import com.intentia.mak.util.MAKException;
import java.nio.file.Path;
import java.util.logging.Level;
import m3.M3Connector;
import m3.M3UpgradObject;
import mak.db.MetaDataDef;
import model.entity.IEntityModel;
import org.apache.commons.io.FilenameUtils;
import ui.tools.BusyDialog;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ProcessField implements Runnable, Observer {

    private final BusyDialog dial;
    private final M3UpgradModel model;
    private Object[][] data;
    private final IEntityModel envSrc, envTarg;
    private final M3Connector m3Src, m3Targ;
    private String action;
    public final String ANALYSE = "analyze";
    public final String MIGRATE = "migrate";
    private long runtime = 0l;
    public static Logger log;
    private M3UpgradObject m3updobj;

    public ProcessField(BusyDialog d, M3UpgradModel m) {
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
        m3Src.addObserver(this);
        m3Targ = new M3Connector(envTarg);
        try {
            m3Targ.init();
        } catch (MAKException ex) {
            Logger.getLogger(ProcessField.class.getName()).log(Level.SEVERE, null, ex);
        }
        m3Targ.addObserver(this);
        log = Logger.getLogger("processLogger");
    }

    public void analyse() {
        long begtime = System.currentTimeMillis();
        if (loadClassPath()) {
            List<Path> lstSrc = m3Src.getLstAllDBClass();
            List<Path> lstTarg = m3Targ.getLstAllDBClass();

            dial.dialStatus();
            if (dial.getStatus() == dial.STOP) {
                return;
            }

            if (lstSrc != null && !lstSrc.isEmpty()) {
                dial.dialStatus();
                if (dial.getStatus() == dial.STOP) {
                    return;
                }

                dial.setText("Démarrage du traitement...");

                int nbSpec = 0;
                LinkedHashMap<String, String[][]> mapSrc = new LinkedHashMap();
                for (Path lstSrc1 : lstSrc) {
                    dial.dialStatus();
                    if (dial.getStatus() == dial.STOP) {
                        return;
                    }
                    nbSpec++;
                    String db = FilenameUtils.removeExtension(lstSrc1.getFileName().toString());
                    dial.setText("Traitement de " + db + "(" + nbSpec + "/" + lstSrc.size() + ")");
                    MetaDataDef defSpe = new MetaDataDef(m3Src.getClassloaderSpe(), db);
                    mapSrc.put(db, defSpe.getTable().getFieldMetaData());

                }
                LinkedHashMap<String, String[][]> mapTarg = new LinkedHashMap();
                for (Path lstTarg1 : lstTarg) {
                    dial.dialStatus();
                    if (dial.getStatus() == dial.STOP) {
                        return;
                    }
                    nbSpec++;
                    String db = FilenameUtils.removeExtension(lstTarg1.getFileName().toString());
                    dial.setText("Traitement de " + db + "(" + nbSpec + "/" + lstTarg.size() + ")");
                    MetaDataDef defSpe = new MetaDataDef(m3Src.getClassloaderSpe(), db);
                    mapTarg.put(db, defSpe.getTable().getFieldMetaData());

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
                dial.stop();
            } else {
                dial.stop();
            }
        }
    }

    private boolean loadClassPath() {
        dial.setText("Vérification de connexion à l'environnement");
        if (m3Src.isInitOK()) {
            dial.setText("Récupération du classpath spécifique...");
//loadM3Env(m3Src);
            if (m3Targ.isInitOK()) {
                loadM3Env(m3Targ);
                m3updobj = new M3UpgradObject(m3Src, m3Targ);
            } else {
                m3updobj = new M3UpgradObject(m3Src);
            }
            return true;
        } else {
            dial.setText("L'environnement n'est pas accessible!");
            return false;
        }
    }

    private void loadM3Env(M3Connector m3) {
        if (m3.isInitOK()) {
            dial.setText("Chargement de l'environnement " + m3.toString());
            try {
                M3ClassPathEntry[] classpathAllSpe = m3.listClassPathConfig(model.getConfigM3Model().getConfigSelect());
                ArrayList<ArrayList<M3ClassPathEntry>> allClass = m3.parseClassPath(classpathAllSpe, model.getConfigM3Model().getListUnderConfig().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfig().size()]), model.getConfigM3Model().getListUnderConfigSelect().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfigSelect().size()]));
                ArrayList<M3ClassPathEntry> classpathStd = new ArrayList<>();
                for (int j = 1; j < allClass.size(); j++) {
                    classpathStd.addAll(allClass.get(j));
                    dial.dialStatus();
                }

                m3.setClassPathAll(classpathAllSpe);
                m3.setClassPathSpe(allClass.get(0));
                m3.setClassPathStd(classpathStd);

                ArrayList<Path> sourceAllSpe = m3.listSourcePathConfig(model.getConfigM3Model().getConfigSelect());
                ArrayList<ArrayList<Path>> allsrc = m3.parseSourcePath(sourceAllSpe, model.getConfigM3Model().getListUnderConfig().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfig().size()]), model.getConfigM3Model().getListUnderConfigSelect().keySet().toArray(new String[model.getConfigM3Model().getListUnderConfigSelect().size()]));
                ArrayList<Path> sourcepathStd = new ArrayList<>();
                for (int j = 1; j < allClass.size(); j++) {
                    sourcepathStd.addAll(allsrc.get(j));
                    dial.dialStatus();
                }
                m3.setSourcePathSpe(allsrc.get(0));
                m3.setSourcePathStd(sourcepathStd);

            } catch (MAKException ex) {
                dial.setError(ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        analyse();
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
