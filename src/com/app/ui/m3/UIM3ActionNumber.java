/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3ActionNumberListModel;
import controler.AbstractControler;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.entity.IEntityModel;
import ui.entity.IUIEntity;
import ui.tools.LimitedTextField;

/**
 *
 * @author JCHAUT
 */
public class UIM3ActionNumber extends JPanel implements IUIEntity {

    private GridBagConstraints gbc;
    private JLabel lactn, ldesc;
    private LimitedTextField tactn;
    private JTextArea tdesc;
    private M3ActionNumberListModel model;

    public UIM3ActionNumber(M3ActionNumberListModel m) {
        super(new GridBagLayout(), true);
        model = m;
    }

    @Override
    public void load() {
        lactn = new JLabel(i18n.Language.getLabel(185));
        tactn = new LimitedTextField(20, LimitedTextField.ALPHA_WITHOUT_SPECHAR, true);
        ldesc = new JLabel(i18n.Language.getLabel(43));
        tdesc = new JTextArea();

        gbc = new GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.insets = new java.awt.Insets(0, 10, 0, 0);
        this.add(lactn, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        this.add(tactn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.insets = new java.awt.Insets(0, 10, 0, 0);
        this.add(ldesc, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        this.add(tdesc, gbc);
    }

    @Override
    public IEntityModel getModelEntity() {
        return (IEntityModel)model;
    }

    @Override
    public void control(AbstractControler controller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
