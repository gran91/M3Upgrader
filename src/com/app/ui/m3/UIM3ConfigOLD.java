/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3ConfigModelOLD;
import controler.AbstractControler;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import org.jdesktop.swingx.JXBusyLabel;
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
public class UIM3ConfigOLD extends JPanel implements ActionListener, Observer, IUIEntities {

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
    private UIM3ConfigOLD m3config;
    private M3ConfigModelOLD model;
    protected JXBusyLabel busyConfig;
    protected PanelButton pButton = new PanelButton(PanelButton.BT_CUSTOM);

    public UIM3ConfigOLD(M3ConfigModelOLD m) {
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
                    ArrayList<String> a = new ArrayList<>();
                    for (int i = checklistmanager.getSelectionModel().getMinSelectionIndex(); i <= checklistmanager.getSelectionModel().getMaxSelectionIndex(); i++) {
                        if (checklistmanager.getSelectionModel().isSelectedIndex(i)) {
                            a.add(model.getListUnderConfig().get(i));
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
                tlevel.setListData(model.getListUnderConfig().toArray());
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        } else {
            tconfig.setEnabled(true);
            busyConfig.setVisible(false);
            busyConfig.setEnabled(false);
            busyConfig.setBusy(false);
            tconfig.setDataList(model.getListConfig());
            if (model.getListUnderConfig() != null) {
                tlevel.setListData(model.getListUnderConfig().toArray());
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        if (act.equals("changeconfig")) {
            model.setConfigSelect(tconfig.getSelectedItem().toString());
        } else if (act.equals("selectAll")) {
            if (model.getListUnderConfig() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUnderConfig().size() - 1);
            }
        } else if (act.equals("unselectAll")) {
            if (model.getListUnderConfig() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUnderConfig().size() - 1);
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
            tconfig.setDataList(model.getListConfig());
        }
        if (model.getListUnderConfig() != null) {
            tlevel.setListData(model.getListUnderConfig().toArray());
            tconfig.setSelectedItem(model.getConfigSelect());
            checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUnderConfig().size() - 1);
            for (int i = 0; i < model.getListUnderConfig().size(); i++) {
                if (model.getListUnderConfigSelect().contains(model.getListUnderConfig().get(i))) {
                    checklistmanager.getSelectionModel().addSelectionInterval(i, i);
                }
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

//    class UIM3ConfigWorker extends SwingWorker<Integer, String> {
//
//        @Override
//        protected Integer doInBackground() throws Exception {
//            try {
//                model.loadData();
//
//                return 0;
//            } catch (IOException ex) {
//                Logger.getLogger(UIM3Config.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return -1;
//        }
//
//        @Override
//        protected void done() {
//            try {
//                setProgress(100);
//                tconfig.setDataList(model.getListConfig());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    
}
