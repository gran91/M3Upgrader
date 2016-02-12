/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3ActionNumberListModel;
import controler.AbstractControler;
import controler.entity.DefaultControlerEntity;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXLabel;
import ui.entities.IUIEntities;
import ui.entity.IUIEntity;
import ui.list.JComboboxXML;
import ui.popup.UIPopup;
import ui.table.UITable;
import ui.table.model.DefaultTableModel;
import ui.tools.AutoComboBox;
import ui.tools.UITools;

/**
 *
 * @author JCHAUT
 */
public class UIM3ActionNumberList extends JPanel implements Observer, IUIEntities {

    protected GridBagConstraints gbc;
    protected static int spaceTop = 5;
    protected static int spaceBottom = 0;
    protected static int spaceLeft = 0;
    protected static int spaceRight = 5;
    private JXLabel lactn;
    private JComboboxXML tactn;
//    private AutoComboBox tactn;
    private final M3ActionNumberListModel actionModel;
    private UIM3ActionNumber uiActionNumber;
    private JXBusyLabel busyActionNumber;

    public UIM3ActionNumberList(M3ActionNumberListModel a) {
        super(new GridBagLayout(), true);
        actionModel = a;
        actionModel.addObserver(this);
    }

    public void load() {
        lactn = new JXLabel(i18n.Language.getLabel(185) + ":");
//        tactn=new AutoComboBox(actionModel.getListAct());
//        tactn.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                actionModel.setActionNumberSelect(tactn.getSelectedItem().toString());
//            }
//        });
        tactn = new JComboboxXML(actionModel);
        tactn.load();
        tactn.getCombobox().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionModel.setActionNumberSelect(tactn.getCombobox().getSelectedItem().toString());
            }
        });

        gbc = new GridBagConstraints();
        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(lactn, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 0.8;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(tactn, gbc);
        
        tactn.setButtonEnabled(false);

        busyActionNumber = UITools.createComplexBusyLabel();
        busyActionNumber.setEnabled(false);
        busyActionNumber.setVisible(false);
        gbc.gridx = 2;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.insets = new java.awt.Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.add(busyActionNumber, gbc);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg.toString().equals("process")) {
                tactn.setEnabled(false);
                busyActionNumber.setVisible(true);
                busyActionNumber.setEnabled(true);
                busyActionNumber.setBusy(true);
            }
        } else {
            busyActionNumber.setVisible(false);
            busyActionNumber.setEnabled(false);
            busyActionNumber.setBusy(false);
            tactn.setEnabled(true);
            tactn.loadData();
        }
    }

    @Override
    public IUIEntity getViewEntity() {
        return uiActionNumber;
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
        actionModel.addEntity();
        uiActionNumber = new UIM3ActionNumber(actionModel);
        new DefaultControlerEntity(uiActionNumber);
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
        return actionModel;
    }

    @Override
    public IEntityModel getModelEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void control(AbstractControler controller) {
        tactn.getButton().addActionListener(controller);
    }

    @Override
    public void loadData() {
        if (actionModel.getActionNumberSelect() != null) {
            tactn.getCombobox().setSelectedItem(actionModel.getActionNumberSelect());
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

    class UIM3ActionWorker extends SwingWorker<Integer, String> {

        @Override
        protected Integer doInBackground() throws Exception {
            tactn.loadData();

            return 0;
        }

        @Override
        protected void done() {
            try {
                setProgress(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
