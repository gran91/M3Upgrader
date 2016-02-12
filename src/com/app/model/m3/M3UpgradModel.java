/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import com.app.main.Ressource;
import com.app.model.AbstractProcessModel;
import com.app.model.IProcessModel;
import com.app.process.ProcessField;
import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import m3.M3UpdObjModel;
import model.entities.XMLEntitiesModel;
import model.entity.ActionerModel;
import model.entity.EntityTitleModel;
import org.apache.commons.io.FilenameUtils;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdom.Element;
import output.Column;
import ui.tools.BusyDialog;
import zio.XMLManage;

/**
 *
 * @author JCHAUT
 */
public class M3UpgradModel extends AbstractProcessModel implements IProcessModel {

    private int curIdSrc = -1;
    private int curIdTarg = -1;
    private M3ConfigModel configm3 = new M3ConfigModel(null);
    private M3ActionNumberListModel actionNumber = new M3ActionNumberListModel(null);
    private M3UserModel m3user = new M3UserModel(new XMLEntitiesModel("workspace"));
    private com.app.process.Process process;
    private Thread t;
    private Thread taskActionNumber, taskM3Config, taskM3User, checkThread;
    private Column[] listColumn;
    private boolean activeCheck = true;
    private final String[] extension = {".m3up"};
    private String filePath = "";
    private String error = "";

    public M3UpgradModel(EntityTitleModel e) {
        super(e);
        model = this;
        addEntityTitle(new EntityTitleModel(206, "environnment"));
        addEntityTitle(new EntityTitleModel(207, "environnment"));
        addActionCmd(new ActionerModel(208, "analyze"));
        addActionCmd(new ActionerModel(209, "migrate"));
        buildThreadSrc();
        buildThreadTarg();
        checkThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (activeCheck) {
                    checkChange();
                }
            }
        });
        checkThread.start();
    }

    private void buildThreadSrc() {
        taskM3Config = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    configm3.loadData();
                } catch (IOException ex) {
                    Ressource.logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        });

        taskM3User = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    m3user.loadData();
                } catch (IOException ex) {
                    Ressource.logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        });
    }

    private void buildThreadTarg() {
        taskActionNumber = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    actionNumber.loadData();
                } catch (IOException ex) {
                    Ressource.logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        });
    }

    private void checkChange() {
        int newIdSrc = this.listEntity.get(0).getEntity().getModelEntity().getId();
        int newIdTarg = this.listEntity.get(1).getEntity().getModelEntity().getId();
        if (newIdSrc != -1 && newIdSrc != curIdSrc) {
            curIdSrc = newIdSrc;
            System.out.println("Changement d'environnement source id=" + curIdSrc);
            buildThreadSrc();
            configm3.setEntity(this.listEntity.get(0).getEntity().getModelEntity());
            m3user.getEntity().setIdLink(this.listEntity.get(0).getEntity().getModelEntity().getIdLink());
            while (!taskM3Config.isAlive()) {
                taskM3Config.start();
            }
            while (!taskM3User.isAlive()) {
                taskM3User.start();
            }

        }
        if (newIdTarg != -1 && newIdTarg != curIdTarg) {
            curIdTarg = newIdTarg;
            System.out.println("Changement d'environnement cible id=" + curIdTarg);
            buildThreadTarg();
            actionNumber.setEntity(this.listEntity.get(1).getEntity().getModelEntity());
            while (!taskActionNumber.isAlive()) {
                taskActionNumber.start();
            }
        }
    }

    @Override
    public void process(String s) {
        switch (s) {
            case "analyze":
                m3process(s);
                break;
            case "migrate":
                m3process(s);
                break;
            case "listfield":
                processField(s);
                break;
            case "new":
                newModel();
                break;
            case "open":
                open();
                break;
            case "save":
                save();
                break;
            case "saveas":
                saveas();
                break;
            case "quit":
                quit();
                break;
            case "change":
                if (activeCheck) {
                    checkChange();
                }
                break;
            case "change_config":
                System.out.println("Launch migration");
                break;
        }

        if (s.startsWith("lng_")) {
            setChanged();
            notifyObservers(s);
        }
    }

    private boolean checkProcess(String s) {
        error = "";
        switch (s) {
            case "analyze":
                if (this.getListEntity().get(0).getEntity().getModelEntity().getData() == null) {
                    error = i18n.Language.getLabel(206) + " " + i18n.Language.getLabel(176);
                }
                if (this.configm3.getConfigSelect() == null || this.configm3.getConfigSelect().isEmpty()) {
                    error += "\n" + i18n.Language.getLabel(178) + " " + i18n.Language.getLabel(176);
                }
                break;
            case "migrate":
                if (this.getListEntity().get(0).getEntity().getModelEntity().getData() == null) {
                    error = i18n.Language.getLabel(206) + " " + i18n.Language.getLabel(176);
                }
                if (this.getListEntity().get(1).getEntity().getModelEntity().getData() == null) {
                    error += "\n" + i18n.Language.getLabel(207) + " " + i18n.Language.getLabel(176);
                }
                if (this.configm3.getConfigSelect() == null || this.configm3.getConfigSelect().isEmpty()) {
                    error += "\n" + i18n.Language.getLabel(178) + " " + i18n.Language.getLabel(176);
                }
                break;
        }

        return error.isEmpty();
    }

    private void m3process(String s) {
        if (checkProcess(s)) {
            JXBusyLabel busy = ui.tools.UITools.createComplexBusyLabel(75);
            BusyDialog busyDial = new BusyDialog((JFrame) Ressource.mainView.getWindow());
            busyDial.setBusy(busy);
            busyDial.load();
            process = new com.app.process.Process(busyDial, this);
            process.setAction(s);
            t = new Thread(process);
            busyDial.setProc(t);
            busyDial.start();
        } else {
            if (!error.isEmpty()) {
                setChanged();
                notifyObservers("error");
            }
        }
    }

    private void processField(String s) {
        if (checkProcess(s)) {
            JXBusyLabel busy = ui.tools.UITools.createComplexBusyLabel(75);
            BusyDialog busyDial = new BusyDialog((JFrame) Ressource.mainView.getWindow());
            busyDial.setBusy(busy);
            busyDial.load();
            ProcessField proc = new com.app.process.ProcessField(busyDial, this);
            proc.setAction(s);
            Thread t1 = new Thread(proc);
            busyDial.setProc(t1);
            busyDial.start();
        } else {
            if (!error.isEmpty()) {
                setChanged();
                notifyObservers("error");
            }
        }
    }

    public M3ConfigModel getConfigM3Model() {
        return configm3;
    }

    public void setConfigM3Model(M3ConfigModel configm3) {
        this.configm3 = configm3;
    }

    public M3ActionNumberListModel getM3ActionNumberModel() {
        return actionNumber;
    }

    public void setM3ActionNumberModel(M3ActionNumberListModel actionNumber) {
        this.actionNumber = actionNumber;
    }

    public M3UserModel getM3UserModel() {
        return m3user;
    }

    public void setM3UserModel(M3UserModel m3user) {
        this.m3user = m3user;
    }

    private void newModel() {
        setChanged();
        notifyObservers("new");
    }

    private void open() {
        File[] f = zio.ToolsFile.fileChoice(null, Ressource.conf.getConfig().getProperty("open.dir"), new ArrayList(Arrays.asList(extension)));
        if (f != null) {
            if (f.length > 0) {
                XMLManage xml = new XMLManage("m3upgrade", "envs");
                try {
                    filePath = f[0].getAbsolutePath();
                    setChanged();
                    notifyObservers("title");
                    xml.readFile(f[0].getAbsolutePath());
                    Element elmtEnvs = xml.getRoot().getChild("envs");
                    for (int i = 0; i < elmtEnvs.getChildren().size(); i++) {
                        if (i == 0) {
                            this.parentEntity.setId(tools.Tools.convertToInt(((Element) elmtEnvs.getChildren().get(i)).getValue()));
                        } else {
                            this.getListEntity().get((i - 1)).getEntity().getModelEntity().setIdLink(this.parentEntity.getId());
                            this.getListEntity().get((i - 1)).getEntity().getModelEntity().setId(tools.Tools.convertToInt(((Element) elmtEnvs.getChildren().get(i)).getValue()));
                        }
                    }

                    setChanged();
                    notifyObservers("loadData");
                    activeCheck = false;
                    curIdSrc = this.getListEntity().get(0).getEntity().getModelEntity().getId();

                    Iterator it = xml.getRoot().getChildren("Config").iterator();
                    Element elmtConfig = null;
                    String configSelect = "";
                    LinkedHashMap<String, M3ConfigurationInfo> listConfig = new LinkedHashMap<>();
                    LinkedHashMap<String, M3ConfigurationInfo> listUnderConfig = new LinkedHashMap<>();
                    LinkedHashMap<String, M3ConfigurationInfo> listUnderConfigSelect = new LinkedHashMap<>();
                    String conf = "";
                    M3ConfigurationInfo info = null;
                    while (it.hasNext()) {
                        elmtConfig = (Element) it.next();
                        for (int j = 0; j < elmtConfig.getChildren().size(); j++) {
                            if (j == 0) {
                                conf = ((Element) elmtConfig.getChildren().get(j)).getValue();
                            } else if (j == 1) {
                                info = new M3ConfigurationInfo();
                                info.setName(conf);
                                info.setDescription(((Element) elmtConfig.getChildren().get(j)).getValue());
                                listConfig.put(conf, info);
                            } else if (j == 2) {
                                if (tools.Tools.convertToInt(((Element) elmtConfig.getChildren().get(j)).getValue()) == 1) {
                                    configSelect = conf;
                                }
                            } else if (j == 3) {
                                if (tools.Tools.convertToInt(((Element) elmtConfig.getChildren().get(j)).getValue()) == 1) {
                                    listUnderConfig.put(conf, info);
                                }
                            } else if (j == 4) {
                                if (tools.Tools.convertToInt(((Element) elmtConfig.getChildren().get(j)).getValue()) == 1) {
                                    listUnderConfigSelect.put(conf, info);
                                }
                            }
                        }
                    }
                    this.configm3.setListConfig(listConfig);
                    this.configm3.setListUnderConfig(listUnderConfig);
                    this.configm3.setConfigSelect(configSelect);
                    this.configm3.setListUnderConfigSelect(listUnderConfigSelect);

                    it = xml.getRoot().getChildren("ActionNumber").iterator();
                    Element elmtAction = null;
                    while (it.hasNext()) {
                        elmtAction = (Element) it.next();
                        this.actionNumber.setActionNumberSelect(elmtAction.getChild("actionumberId").getValue());
                    }

                    it = xml.getRoot().getChildren("User").iterator();
                    Element elmtUser = null;
                    ArrayList<String> listUser = new ArrayList<>();
                    ArrayList<String> listUserSelect = new ArrayList<>();

                    while (it.hasNext()) {
                        elmtUser = (Element) it.next();
                        for (int j = 0; j < elmtUser.getChildren().size(); j++) {
                            if (j == 0) {
                                listUser.add(((Element) elmtUser.getChildren().get(j)).getValue());
                            } else if (j == 1) {
                                if (tools.Tools.convertToInt(((Element) elmtUser.getChildren().get(j)).getValue()) == 1) {
                                    listUserSelect.add(listUser.get(listUser.size() - 1));
                                }
                            }
                        }
                    }
                    this.m3user.setListUser(listUser);
                    this.m3user.setListUserSelect(listUserSelect);

                    Element eltData = xml.getRoot().getChild("Data");
                    if (eltData != null) {
                        Element elmtId = null;
                        Element elmtCol = null;
                        String value = "";
                        it = eltData.getChildren("Id").iterator();
                        data = new Object[eltData.getChildren("Id").size()][eltData.getChild("Id").getChildren().size()];
                        int cptLin = 0;
                        while (it.hasNext()) {
                            elmtId = (Element) it.next();
                            Iterator itCol = elmtId.getChildren().iterator();
                            int cptCol = 0;
                            while (itCol.hasNext()) {
                                elmtCol = (Element) itCol.next();
                                value = elmtCol.getValue();
                                data[cptLin][cptCol] = (value.equals("true") || value.equals("false")) ? Boolean.valueOf(value) : value;
                                cptCol++;
                            }
                            cptLin++;
                        }
                    }
                    if (f[0].getParent() != null) {
                        Ressource.conf.setConfig("open.dir", f[0].getParent());
                    }
                } catch (Exception ex) {
                    Ressource.logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        }
        setChanged();
        notifyObservers("loadData");
        activeCheck = true;
    }

    public void saveas() {
        File[] f = zio.ToolsFile.fileChoice(SwingUtilities.getRoot(Ressource.mainView.getWindow()), null, new ArrayList(Arrays.asList(extension)));
        if (f != null) {
            if (f.length > 0) {
                filePath = f[0].getAbsolutePath();
                if (!FilenameUtils.isExtension(filePath, extension)) {
                    filePath += extension[0];
                }
                setChanged();
                notifyObservers("title");
                save(filePath);
            }
        }
    }

    public void save() {
        if (filePath.isEmpty()) {
            saveas();
        }
    }

    public void save(String path) {
        XMLManage xml = new XMLManage("m3upgrade", "envs");
        ArrayList<String> env = new ArrayList<>();
        env.add("" + this.listEntity.get(0).getEntity().getModelEntity().getIdLink());
        env.add("" + this.listEntity.get(0).getEntity().getModelEntity().getId());
        env.add("" + this.listEntity.get(1).getEntity().getModelEntity().getId());
        xml.add(new ArrayList(Arrays.asList(new String[]{"customer", "envsrc", "envtarg"})), env);

//        for (int i = 0; i < configm3.getListConfig().size(); i++) {
//            ArrayList<String> a = new ArrayList<>();
//            a.add(configm3.getListConfig().get(i));
//            a.add(configm3.getListConfig().get(i).equals(configm3.getConfigSelect()) ? "1" : "0");
//            a.add(configm3.getListUnderConfig().contains(configm3.getListConfig().get(i)) ? "1" : "0");
//            a.add(configm3.getListUnderConfigSelect().contains(configm3.getListConfig().get(i)) ? "1" : "0");
//            xml.add("Config", new ArrayList(Arrays.asList(new String[]{"configId", "selected", "under", "underselected"})), a);
//        }
//        
        for (String s : configm3.getListConfig().keySet()) {
            ArrayList<String> a = new ArrayList<>();
            a.add(s);
            a.add(configm3.getListConfig().get(s).getDescription());
            a.add(s.equals(configm3.getConfigSelect()) ? "1" : "0");
            a.add(configm3.getListUnderConfig().containsKey(s) ? "1" : "0");
            a.add(configm3.getListUnderConfigSelect().containsKey(s) ? "1" : "0");
            xml.add("Config", new ArrayList(Arrays.asList(new String[]{"configId", "description", "selected", "under", "underselected"})), a);
        }

        ArrayList<String> a = new ArrayList<>();
        a.add(actionNumber.getActionNumberSelect());
        xml.add("ActionNumber", new ArrayList(Arrays.asList(new String[]{"actionumberId"})), a);

        for (int j = 0; j < m3user.getListUser().size(); j++) {
            a = new ArrayList<>();
            a.add(m3user.getListUser().get(j));
            a.add(m3user.getListUserSelect().contains(m3user.getListUser().get(j)) ? "1" : "0");
            xml.add("User", new ArrayList(Arrays.asList(new String[]{"userId", "userselected"})), a);
        }

        String[] headerLng = new String[M3UpdObjModel.header.length];
        for (int k = 0; k < M3UpdObjModel.header.length; k++) {
            headerLng[k] = i18n.Language.getLabel(M3UpdObjModel.header[k], "EN");
        }

        if (data != null) {
            for (Object[] data1 : data) {
                xml.add("Data", "Id", new ArrayList(Arrays.asList(headerLng)), new ArrayList(Arrays.asList(data1)));
            }
        }

        try {
            xml.save(path);
        } catch (IOException ex) {
            Ressource.logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    public void quit() {
        setChanged();
        notifyObservers("quit");
    }

    public boolean isActiveCheck() {
        return activeCheck;
    }

    public void setActiveCheck(boolean activeCheck) {
        this.activeCheck = activeCheck;
    }

    public Column[] getListColumn() {
        return listColumn;
    }

    public void setListColumn(Column[] listColumn) {
        this.listColumn = listColumn;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
