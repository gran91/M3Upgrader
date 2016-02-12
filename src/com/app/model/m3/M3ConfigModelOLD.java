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
import m3.M3ConfigOLD;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import model.entity.XMLEntityModel;

/**
 *
 * @author JCHAUT
 */
public class M3ConfigModelOLD extends Observable implements IEntitiesModel {

    private IEntityModel entity;
    private M3ConfigOLD config;
    private List<String> listConfig;
    private String configSelect;
    private List<String> listUnderConfig;
    private List<String> listUnderConfigSelect;

    public M3ConfigModelOLD(int custId, int envId) {
        super();
        entity = new XMLEntityModel("environnment");
        entity.setIdLink(custId);
        entity.loadData(envId);
    }

    public M3ConfigModelOLD(IEntityModel e) {
        super();
        entity = e;
    }

    @Override
    public void loadData() throws IOException {
        setChanged();
        notifyObservers("process");
        config = new M3ConfigOLD(entity);
        listConfig = config.lstM3Config();
        if (listConfig != null && !listConfig.isEmpty()) {
            configSelect = listConfig.get(0);
            loadUnderConfig();
        }
        setChanged();
        notifyObservers();
    }

    private void loadUnderConfig() {
        if (listConfig != null && !listConfig.isEmpty()) {
            int n = listConfig.indexOf(configSelect);
            if (n != -1) {
                n++;
                listUnderConfig = new ArrayList<>();
                for (int i = n; i < listConfig.size(); i++) {
//                    if (!listConfig.get(i).equals("MVX")) {
                        listUnderConfig.add(listConfig.get(i));
//                    }
                }
            }
        }
    }

    @Override
    public Object[][] getData() {
        if (listConfig != null) {
            Object[][] o = new Object[listConfig.size()][1];
            for (int i = 0; i < listConfig.size(); i++) {
                o[i][0] = listConfig.get(i);
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

    public List<String> getListConfig() {
        return listConfig;
    }

    public void setListConfig(List<String> listConf) {
        this.listConfig = listConf;
    }

    public M3ConfigOLD getConfig() {
        return config;
    }

    public void setConfig(M3ConfigOLD config) {
        this.config = config;
    }

    public String getConfigSelect() {
        return configSelect;
    }

    public void setConfigSelect(String configSelect) {
        this.configSelect = configSelect;
        loadUnderConfig();
        setChanged();
        notifyObservers("changeconfig");
    }

    public List<String> getListUnderConfig() {
        return listUnderConfig;
    }

    public void setListUnderConfig(List<String> listUnderConfig) {
        this.listUnderConfig = listUnderConfig;
    }

    public List<String> getListUnderConfigSelect() {
        return listUnderConfigSelect;
    }

    public void setListUnderConfigSelect(List<String> listUnderConfigSelect) {
        this.listUnderConfigSelect = listUnderConfigSelect;
    }
}
