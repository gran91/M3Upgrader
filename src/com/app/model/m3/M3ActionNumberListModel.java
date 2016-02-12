/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import com.intentia.mak.core.actionlog.ActionLogManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import m3.ActionNumber;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import model.entity.XMLEntityModel;

/**
 *
 * @author JCHAUT
 */
public class M3ActionNumberListModel extends Observable implements IEntitiesModel {

    private IEntityModel entity;
    private ActionNumber act;
    private List<String> listAct;
    private String actionNumberSelect;

    public M3ActionNumberListModel(int custId, int envId) {
        super();
        entity = new XMLEntityModel("environnment");
        entity.setIdLink(custId);
        entity.loadData(envId);
    }

    public M3ActionNumberListModel(IEntityModel e) {
        super();
        entity = e;
    }

    @Override
    public void loadData() throws IOException {
        setChanged();
        notifyObservers("process");
        act = new ActionNumber(entity);
        listAct = act.lstAllAction();
        setChanged();
        notifyObservers();
    }

    @Override
    public Object[][] getData() {
        if (listAct != null) {
            Object[][] o = new Object[listAct.size()][1];
            for (int i = 0; i < listAct.size(); i++) {
                o[i][0] = listAct.get(i);
            }
            return o;
        } else {
            return null;
        }
    }

    @Override
    public void setFileName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<IEntityModel> getEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEntities(ArrayList<IEntityModel> entities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addEntity() {
       
    }

    @Override
    public void delEntity(IEntityModel entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delEntity(int idLink, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getColumnsHeader() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setColumnsHeader(String[] h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IEntityModel getModelEntity() {
        return null;
    }

    @Override
    public String getLink() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLink(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNameEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIdLink() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIdLink(int n) {

    }

    public IEntityModel getEntity() {
        return entity;
    }

    public void setEntity(IEntityModel entity) {
        this.entity = entity;
    }

    public List<String> getListAct() {
        return listAct;
    }

    public void setListAct(List<String> listAct) {
        this.listAct = listAct;
    }

    public String getActionNumberSelect() {
        return actionNumberSelect;
    }

    public void setActionNumberSelect(String actionNumberSelect) {
        this.actionNumberSelect = actionNumberSelect;
        if(!listAct.contains(actionNumberSelect)){
            listAct.add(actionNumberSelect);
        }
    }

}
