/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui;

import com.app.controler.ProcessControler;
import com.app.main.Ressource;
import com.app.model.m3.M3UpgradModel;
import com.app.ui.menu.Menu;
import controler.entities.DefaultControlerEntities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.app.ui.m3.UIM3Config;
import com.app.ui.m3.UIM3ActionNumberList;
import com.app.ui.m3.UIM3Analyze;
import com.app.ui.m3.UIM3User;
import controler.MenuControlerEntity;
import java.io.File;
import java.util.Observable;
import javax.swing.JOptionPane;
import model.entity.EntityTitleModel;
import org.jdesktop.swingx.JXTaskPane;
import ui.tools.UITools;

/**
 *
 * @author JCHAUT
 */
public class PMain extends UIProcess {

    private final M3UpgradModel model;
    private UIM3Config m3Config;
    private UIM3ActionNumberList m3actionnumber;
    private UIM3User m3user;
    private boolean changeLangage = false;

    public PMain(M3UpgradModel m) {
        super(m);
        model = m;
    }

    public void loadMain() {
        Menu m = new Menu();
        new MenuControlerEntity(m);
        this.setMenu(m);
        super.loadMain();
        m3Config = new UIM3Config(model.getConfigM3Model());
        m3Config.load();
        if (this.getpMain().getUitype() == 0) {
            JXTaskPane tTask = new JXTaskPane();
            tTask.setTitle("M3Config");
            tTask.setCollapsed(true);
            tTask.add(m3Config);
            this.getpMain().getContainer().add(tTask);
        } else {
            ((JTabbedPane) this.getpMain().getContainer()).add("M3Config", m3Config);
        }

        JPanel pdev = new JPanel(new BorderLayout(), true);
        m3actionnumber = new UIM3ActionNumberList(model.getM3ActionNumberModel());
        new DefaultControlerEntities(m3actionnumber);

        m3user = new UIM3User(model.getM3UserModel());
        m3user.load();

        pdev.add(m3actionnumber, BorderLayout.NORTH);
        pdev.add(m3user, BorderLayout.CENTER);

        if (this.getpMain().getUitype() == 0) {
            JXTaskPane tTask = new JXTaskPane();
            tTask.setTitle("Workspace MAK");
            tTask.setCollapsed(true);
            tTask.add(pdev);
            this.getpMain().getContainer().add(tTask);
        } else {
            ((JTabbedPane) this.getpMain().getContainer()).add("Workspace MAK", pdev);
        }
    }

    public void loadData() {
        super.loadData();
        m3Config.loadData();
        m3actionnumber.loadData();
        m3user.loadData();
        if (model.getData() != null && !changeLangage) {
            int n = JOptionPane.showConfirmDialog(this, i18n.Language.getLabel(210) + "\n" + i18n.Language.getLabel(211), i18n.Language.getLabel(208), JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                UIM3Analyze m = new UIM3Analyze(model);
                UITools.createFrame(Ressource.title, m);
            }
        }
    }

    public void quit() {
        if (!changeLangage) {
            int n = JOptionPane.showConfirmDialog(this, i18n.Language.getLabel(212), i18n.Language.getLabel(141), JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                model.save();
            }
            super.quit();
        }
    }

    public void update(Observable o, Object arg) {
        super.update(o, arg);
        if (arg.toString().equals("title")) {
            setTitle(Ressource.title + " " + new File(model.getFilePath()).getName());
        } else if (arg.toString().equals("new")) {
            quit();
            EntityTitleModel e = new EntityTitleModel(Ressource.title, "customer");
            M3UpgradModel model = new M3UpgradModel(e);
            PMain p = new PMain(model);
            ProcessControler processControler = new ProcessControler(p);
            Ressource.mainView = p;
        } else if (arg.toString().equals("error")) {
            JOptionPane.showMessageDialog(this, model.getError(), "Error", JOptionPane.ERROR_MESSAGE);
        } else if (arg.toString().startsWith("lng_")) {
            super.quit();
            String[] tab = arg.toString().split("_");
            if (tab.length > 1) {
                main.Ressource.conf.setConfig("config.lang", tab[1]);
            }
            PMain p = new PMain(model);
            p.setChangeLangage(true);
            Integer[] listId = null;
            if (listView != null) {
                listId = new Integer[listView.size()];
                for (int i = 0; i < listView.size(); i++) {
                    listId[i] = model.getListEntity().get(i).getEntity().getModelEntity().getId();
                    model.getListEntity().get(i).setTitle(i18n.Language.getLabel(model.getListEntity().get(i).getIdLng()));
                }
            }

            for (int i = 0; i < model.getActioner().size(); i++) {
                model.getActioner().get(i).setLabel(i18n.Language.getLabel(model.getActioner().get(i).getIdLng()));
            }

            ProcessControler processControler = new ProcessControler(p);
            for (int i = 0; i < listView.size(); i++) {
                model.getListEntity().get(i).getEntity().getModelEntity().setId(listId[i]);
            }
            p.loadData();
            p.setChangeLangage(false);
            Ressource.mainView = p;
        }
    }

    public boolean isChangeLangage() {
        return changeLangage;
    }

    public void setChangeLangage(boolean changeLangage) {
        this.changeLangage = changeLangage;
    }
}
