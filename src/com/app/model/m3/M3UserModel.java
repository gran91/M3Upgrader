/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;

/**
 *
 * @author JCHAUT
 */
public class M3UserModel extends Observable implements IEntitiesModel {

    private IEntitiesModel entity;
    private List<String> listUser;
    private List<String> listUserSelect;

    public M3UserModel(IEntitiesModel e) {
        super();
        entity = e;
        listUser=new ArrayList<>();
        listUserSelect=new ArrayList<>();
    }

    @Override
    public void loadData() throws IOException {
        setChanged();
        notifyObservers("process");
        entity.loadData();
        listUser.clear();
        listUserSelect.clear();
        Object[][] o = entity.getData();
        if (o != null) {
            for (int i = 0; i < o.length; i++) {
                listUser.add(o[i][0].toString());
            }
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public Object[][] getData() {
        return entity.getData();
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

    public IEntitiesModel getEntity() {
        return entity;
    }

    public void setEntity(IEntitiesModel entity) {
        this.entity = entity;
    }

    public List<String> getListUser() {
        return listUser;
    }

    public void setListUser(List<String> listUser) {
        this.listUser = listUser;
    }

    public List<String> getListUserSelect() {
        return listUserSelect;
    }

    public void setListUserSelect(List<String> listUserSelect) {
        this.listUserSelect = listUserSelect;
    }
    
}
