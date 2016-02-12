/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3UpgradModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import m3.M3UpdObjModel;
import main.Ressource;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXLabel;
import output.Column;
import output.ExcelM3Upgrad;
import tools.Config;
import ui.checklist.CheckListManager;
import ui.panel.model.PanelButton;
import ui.tools.AutoComboBox;
import ui.tools.BusyDialog;
import ui.tools.JTextFieldFileChooser;
import ui.tools.UITools;

/**
 *
 * @author Jeremy.CHAUT
 */
public class UIExport extends JPanel implements ActionListener {

    private GridBagConstraints gbc;
    private ui.checklist.CheckListManager checklistmanager;
    protected PanelButton pButtonList = new PanelButton(PanelButton.BT_CUSTOM);
    protected PanelButton pButton = new PanelButton(PanelButton.BT_OK_CANCEL);
    private JXLabel lheader, lpath, ltype;
    private JList theader;
    private JTextFieldFileChooser tpath;
    private AutoComboBox ttype;
    private JScrollPane sheader;
    private String[] ext = {"xls", "xlsx"};
    protected static int spaceTop = 5;
    protected static int spaceBottom = 0;
    protected static int spaceLeft = 0;
    protected static int spaceRight = 5;
    private M3UpgradModel model;
    private Config importConfig = new Config("import.properties", new String[]{"column", "path", "type"});

    public UIExport(M3UpgradModel m) {
        super(new GridBagLayout(), true);
        model = m;
        load();
    }

    private void load() {
        lheader = new JXLabel(i18n.Language.getLabel(184) + ":");
        theader = new JList(loadLng());
        lpath = new JXLabel(i18n.Language.getLabel(184) + ":");
        tpath = new JTextFieldFileChooser(Ressource.FILE, "");
        ltype = new JXLabel(i18n.Language.getLabel(184) + ":");
        ttype = new AutoComboBox(new ArrayList(Arrays.asList(ext)));
        checklistmanager = new CheckListManager(theader);
        checklistmanager.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!checklistmanager.getSelectionModel().isSelectionEmpty()) {
                    Column[] a = new Column[M3UpdObjModel.header.length];
                    for (int i = 0; i < M3UpdObjModel.header.length; i++) {
                        Column c = new Column(i18n.Language.getLabel(M3UpdObjModel.header[i], "EN"), M3UpdObjModel.header[i], 0, !checklistmanager.getSelectionModel().isSelectedIndex(i));
                        a[i] = c;
                    }
                    model.setListColumn(a);
                }
            }
        });

        sheader = new JScrollPane(theader);
        pButtonList.addButton(pButtonList.bSelect, "selectAll");
        pButtonList.addButton(pButtonList.bUnselect, "unselectAll");
        pButtonList.bSelect.addActionListener(this);
        pButtonList.bUnselect.addActionListener(this);

        int y = 0;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(lheader, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.gridheight = 2;
        gbc.weightx = 0.9;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(sheader, gbc);

        y += 2;
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(pButtonList, gbc);

        y += 1;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(lpath, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 0.9;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(tpath, gbc);

        y += 1;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(ltype, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 0.9;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(ttype, gbc);

        y += 1;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        this.add(pButton, gbc);
        PanelButton.bOk.addActionListener(this);
        PanelButton.bCancel.addActionListener(this);
        loadData();
    }

    private void loadData() {
        if (importConfig.getConfig().get(importConfig.getListConf()[0]).toString().isEmpty()) {
            if (M3UpdObjModel.header != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().setSelectionInterval(0, M3UpdObjModel.header.length - 1);
            }
        } else {

            String[] indCol = importConfig.getConfig().get(importConfig.getListConf()[0]).toString().split(",");
            if (indCol.length > 1) {
                for (int i = 0; i < indCol.length; i++) {
                    try {
                        int n = Integer.parseInt(indCol[i].trim());
                        checklistmanager.getSelectionModel().addSelectionInterval(n, n);
                    } catch (NumberFormatException e) {
                    }
                }
            } else {
                checklistmanager.getSelectionModel().setSelectionInterval(0, M3UpdObjModel.header.length - 1);
            }

        }
        tpath.setText(importConfig.getConfig().get(importConfig.getListConf()[1]).toString());
        int n = 0;
        try {
            n = Integer.parseInt(importConfig.getConfig().get(importConfig.getListConf()[2]).toString().trim());
        } catch (NumberFormatException e) {
            n = 0;
        }
        ttype.setSelectedIndex(n);
    }

    private void saveData() {
        Column[] col = model.getListColumn();
        String s = "";
        for (int i = 0; i < col.length; i++) {
            if (!col[i].isHidden()) {
                s += i + ",";
            }
        }
        importConfig.setConfig(importConfig.getListConf()[0], s);
        importConfig.setConfig(importConfig.getListConf()[1], tpath.getText());
        importConfig.setConfig(importConfig.getListConf()[2], "" + ttype.getSelectedIndex());
    }

    private String[] loadLng() {
        String[] s = new String[M3UpdObjModel.header.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = i18n.Language.getLabel(M3UpdObjModel.header[i]);
        }
        return s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        if (act.equals("selectAll")) {
            if (M3UpdObjModel.header != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().setSelectionInterval(0, M3UpdObjModel.header.length - 1);
            }
        } else if (act.equals("unselectAll")) {
            if (M3UpdObjModel.header != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().removeSelectionInterval(0, M3UpdObjModel.header.length - 1);
            }
        } else if (act.equals("OK")) {
            saveData();
            JXBusyLabel busy = UITools.createComplexBusyLabel(75);
            BusyDialog busyDial = new BusyDialog((Window) SwingUtilities.getRoot(this));
            busyDial.setBusy(busy);
            busyDial.load();
            ExcelM3Upgrad xls = new ExcelM3Upgrad(model, ttype.getSelectedIndex());
            xls.setFilePath(tpath.getText());
            xls.setAction(ExcelM3Upgrad.WRITE);
            xls.setBusyDial(busyDial);
            Thread t = new Thread(xls);
            busyDial.setProc(t);
            busyDial.start();
        } else if (act.equals("Cancel")) {
            ((Window) SwingUtilities.getRoot(this)).dispose();
        }
    }
}
