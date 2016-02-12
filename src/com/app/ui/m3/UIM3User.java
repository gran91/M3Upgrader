/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3UserModel;
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
import org.jdesktop.swingx.JXLabel;
import ui.checklist.CheckListManager;
import ui.entities.IUIEntities;
import ui.entity.IUIEntity;
import ui.panel.model.PanelButton;
import ui.popup.UIPopup;
import ui.table.UITable;
import ui.table.model.DefaultTableModel;

/**
 *
 * @author JCHAUT
 */
public class UIM3User extends JPanel implements ActionListener, Observer, IUIEntities {

    protected GridBagConstraints gbc;
    protected static int spaceTop = 5;
    protected static int spaceBottom = 0;
    protected static int spaceLeft = 0;
    protected static int spaceRight = 5;
    private JXLabel luser;
    private JList tuser;
    private JScrollPane suser;
    private ui.checklist.CheckListManager checklistmanager;
    private final M3UserModel model;
    protected PanelButton pButton = new PanelButton(PanelButton.BT_CUSTOM);

    public UIM3User(M3UserModel m) {
        super(new GridBagLayout(), true);
        model = m;
        model.addObserver(this);
    }

    public void load() {
        luser = new JXLabel(i18n.Language.getLabel(model.getEntity().getModelEntity().getIdLng()) + ":");
        tuser = new JList();
        tuser.setEnabled(false);
        checklistmanager = new CheckListManager(tuser);
        checklistmanager.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!checklistmanager.getSelectionModel().isSelectionEmpty()) {
                    ArrayList<String> a = new ArrayList<>();
                    for (int i = checklistmanager.getSelectionModel().getMinSelectionIndex(); i <= checklistmanager.getSelectionModel().getMaxSelectionIndex(); i++) {
                        if (checklistmanager.getSelectionModel().isSelectedIndex(i)) {
                            a.add(model.getListUser().get(i));
                        }
                    }
                    model.setListUserSelect(a);
                }
            }
        });

        suser = new JScrollPane(tuser);
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
        gbc.insets = new java.awt.Insets(spaceTop + 10, spaceLeft + 10, spaceBottom, spaceRight);
        this.add(luser, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.gridheight = 2;
        gbc.weightx = 0.9;
        gbc.insets = new java.awt.Insets(spaceTop + 10, spaceLeft, spaceBottom, spaceRight);
        this.add(suser, gbc);

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
        tuser.setListData(model.getListUser().toArray());
        checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUser().size() - 1);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        if (act.equals("selectAll")) {
            if (model.getListUser() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().setSelectionInterval(0, model.getListUser().size() - 1);
            }
        } else if (act.equals("unselectAll")) {
            if (model.getListUser() != null && checklistmanager != null) {
                checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUser().size() - 1);
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
        return model;
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
        tuser.setListData(model.getListUser().toArray());
        checklistmanager.getSelectionModel().removeSelectionInterval(0, model.getListUser().size() - 1);
        ArrayList userSelectCopy=new ArrayList(model.getListUserSelect());
        for (int i = 0; i < model.getListUser().size(); i++) {
            if (userSelectCopy.contains(model.getListUser().get(i))) {
                checklistmanager.getSelectionModel().addSelectionInterval(i, i);
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
