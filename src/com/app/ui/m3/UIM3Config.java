/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3ConfigModel;
import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import controler.AbstractControler;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import ui.checklist.CheckListManager;
import ui.entities.IUIEntities;
import ui.entity.IUIEntity;
import ui.panel.model.PanelButton;
import ui.popup.UIPopup;
import ui.table.UITable;
import ui.table.model.DefaultTableModel;
import ui.tools.AutoComboBox;
import ui.tools.UITools;

/**
 *
 * @author JCHAUT
 */
public class UIM3Config extends JPanel implements ActionListener, Observer, IUIEntities {

    protected GridBagConstraints gbc;
    protected static int spaceTop = 5;
    protected static int spaceBottom = 0;
    protected static int spaceLeft = 0;
    protected static int spaceRight = 5;
    private JXLabel llevel, lconfig;
    private AutoComboBox tconfig;
    private JList tlevel;
    private JScrollPane slevel;
    private ui.checklist.CheckListManager checklistmanager;
    private M3ConfigModel model;
    protected JXBusyLabel busyConfig;
    protected PanelButton pButton = new PanelButton(PanelButton.BT_CUSTOM);
    private JXButton bRefresh = new JXButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(main.Ressource.imgRefresh))));

    public UIM3Config(M3ConfigModel m) {
        super(new GridBagLayout(), true);
        model = m;
        model.addObserver(this);
    }

    public void load() {
        lconfig = new JXLabel(i18n.Language.getLabel(178) + ":");
        tconfig = new AutoComboBox(new ArrayList());
        tconfig.setActionCommand("changeconfig");
        tconfig.addActionListener(this);
        llevel = new JXLabel(i18n.Language.getLabel(184) + ":");
        tlevel = new JList();
        tlevel.setEnabled(false);
        checklistmanager = new CheckListManager(tlevel);
        checklistmanager.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!checklistmanager.getSelectionModel().isSelectionEmpty()) {
                    LinkedHashMap<String, M3ConfigurationInfo> a = new LinkedHashMap<>();
                    for (int i = checklistmanager.getSelectionModel().getMinSelectionIndex(); i <= checklistmanager.getSelectionModel().getMaxSelectionIndex(); i++) {
                        if (checklistmanager.getSelectionModel().isSelectedIndex(i)) {

                            String key = model.getListUnderConfig().keySet().toArray()[i].toString();
                            a.put(key, model.getListUnderConfig().get(key));
                        }
                    }
                    model.setListUnderConfigSelect(a);
                }
            }
        });

        slevel = new JScrollPane(tlevel);
        pButton.addButton(pButton.bSelect, "selectAll");
        pButton.addButton(pButton.bUnselect, "unselectAll");
        pButton.bSelect.addActionListener(this);
        pButton.bUnselect.addActionListener(this);

        gbc = new GridBagConstraints();
        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(lconfig, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 0.8;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(tconfig, gbc);

        busyConfig = UITools.createComplexBusyLabel();
        busyConfig.setEnabled(false);
        busyConfig.setVisible(false);

        gbc.gridx = 2;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(busyConfig, gbc);

        bRefresh.addActionListener(this);
        bRefresh.setActionCommand("refresh");
        bRefresh.setVisible(false);

        gbc.gridx = 3;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(bRefresh, gbc);

        y += 1;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(llevel, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.gridheight = 2;
        gbc.weightx = 0.9;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(slevel, gbc);

        y += 2;
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(pButton, gbc);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg.toString().equals("process")) {
                tconfig.setEnabled(false);
                busyConfig.setVisible(true);
                busyConfig.setEnabled(true);
                busyConfig.setBusy(true);
            } else if (arg.toString().equals("changeconfig")) {
                tlevel.setListData(M3ConfigModel.formatUIConfig(model.getListUnderConfig()).toArray());
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            } else if (arg.toString().equals("error")) {
                JOptionPane.showMessageDialog(this, model.getError(), "Error", JOptionPane.ERROR_MESSAGE);
                tconfig.setEnabled(true);
                busyConfig.setVisible(false);
                busyConfig.setEnabled(false);
                busyConfig.setBusy(false);
                bRefresh.setVisible(true);
            }
        } else {
            tconfig.setEnabled(true);
            busyConfig.setVisible(false);
            busyConfig.setEnabled(false);
            busyConfig.setBusy(false);
            tconfig.setDataList(M3ConfigModel.formatUIConfig(model.getListConfig()));
            if (model.getListUnderConfig() != null) {
                tlevel.setListData(M3ConfigModel.formatUIConfig(model.getListUnderConfig()).toArray());
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        if (act.equals("changeconfig")) {
            model.setConfigSelect(model.getListConfig().keySet().toArray()[tconfig.getSelectedIndex()].toString());
//model.setConfigSelect(tconfig.getSelectedItem().toString());
        } else if (act.equals("selectAll")) {
            if (model.getListUnderConfig() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        } else if (act.equals("unselectAll")) {
            if (model.getListUnderConfig() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        } else if (act.equals("refresh")) {
            try {
                bRefresh.setVisible(false);
                model.loadData();
            } catch (IOException ex) {
                Logger.getLogger(UIM3Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public IUIEntity getViewEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addModelEntityToUIEntity(IEntityModel e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UITable getTableView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DefaultTableModel getTableModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UIPopup getPopup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPopup(UIPopup p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData(Object[][] o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modifyEntity(int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IEntitiesModel getModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IEntityModel getModelEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void control(AbstractControler controller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() {
        if (model.getListConfig() != null) {
            tconfig.setDataList(M3ConfigModel.formatUIConfig(model.getListConfig()));
        }
        if (model.getListUnderConfig() != null) {
            tlevel.setListData(M3ConfigModel.formatUIConfig(model.getListUnderConfig()).toArray());
            tconfig.setSelectedItem(model.getConfigSelect());
            checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUnderConfig().size() - 1);
//            for (int i = 0; i < model.getListUnderConfig().size(); i++) {
//                if (model.getListUnderConfigSelect().contains(model.getListUnderConfig().get(i))) {
//                    checklistmanager.getSelectionModel().addSelectionInterval(i, i);
//                }
//            }
            int i = 0;
            for (String s : model.getListUnderConfig().keySet()) {
                if (model.getListUnderConfigSelect().containsKey(s)) {
                    checklistmanager.getSelectionModel().addSelectionInterval(i, i);
                }
                i++;
            }
        }
    }

    @Override
    public ArrayList<String> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showInDialog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showInFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setContainerType(int containerType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIdLink(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIdLink() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setShowType(int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTitle(java.lang.String t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Window getWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
