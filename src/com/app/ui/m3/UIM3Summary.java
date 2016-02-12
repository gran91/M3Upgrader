/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3UpgradModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.entity.XMLEntityModel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import ui.entity.XMLUIEntity;

/**
 *
 * @author Jeremy.CHAUT
 */
public class UIM3Summary extends JPanel {

    private final M3UpgradModel model;
    private GridBagConstraints gbc;
    private final JPanel pEnv = new JPanel(new GridBagLayout(), true);
    private final JPanel pMig = new JPanel(new GridBagLayout(), true);
    private JLabel lactn, lconf, tactn, tconf, tcust;
    private JXTaskPaneContainer taskcontainer = new JXTaskPaneContainer();

    public UIM3Summary(M3UpgradModel m) {
        super(new BorderLayout(), true);
        model = m;
        load();
    }

    private void load() {
        int idCust = model.getListEntity().get(0).getEntity().getModelEntity().getIdLink();

        XMLEntityModel mclient = new XMLEntityModel("customer");
        mclient.loadData(idCust);
        ArrayList<String> a = mclient.getData();
        if (!a.isEmpty()) {
            tcust = new JLabel(a.get(0));
            tcust.setHorizontalAlignment(JLabel.CENTER);
            tcust.setFont(new Font(this.getFont().getFontName(), Font.BOLD, 16));
        }

        XMLUIEntity xmlSrc = new XMLUIEntity(model.getListEntity().get(0).getEntity().getModelEntity());
        xmlSrc.setContainerType(-1);
        xmlSrc.load();
        xmlSrc.removeButton();
        xmlSrc.loadData();

        XMLUIEntity xmlTarg = new XMLUIEntity(model.getListEntity().get(1).getEntity().getModelEntity());
        xmlTarg.setContainerType(-1);
        xmlTarg.load();
        xmlTarg.removeButton();
        xmlTarg.loadData();

        gbc = new GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new java.awt.Insets(5, 10, 5, 5);
        pEnv.add(xmlSrc, gbc);

        gbc = new GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new java.awt.Insets(5, 10, 5, 5);
        pEnv.add(xmlTarg, gbc);

        JXTaskPane tTask = new JXTaskPane();
        tTask.setTitle(i18n.Language.getLabel(model.getListEntity().get(0).getEntity().getModelEntity().getIdLng()));
        tTask.add(pEnv);
        taskcontainer.add(tTask);

        lactn = new JLabel(i18n.Language.getLabel(185));
        lconf = new JLabel(i18n.Language.getLabel(178));
        tactn = new JLabel(model.getM3ActionNumberModel().getActionNumberSelect());
        tconf = new JLabel(model.getConfigM3Model().getConfigSelect());

        gbc = new GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.insets = new java.awt.Insets(5, 10, 5, 5);
        pMig.add(lactn, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        pMig.add(tactn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        pMig.add(lconf, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        pMig.add(tconf, gbc);

        JXTaskPane tTaskMig = new JXTaskPane();
        tTaskMig.setTitle("Information de migration");
        tTaskMig.add(pMig);
        taskcontainer.add(tTaskMig);
        
        this.add(tcust, BorderLayout.NORTH);
        this.add(taskcontainer, BorderLayout.CENTER);
    }
}
