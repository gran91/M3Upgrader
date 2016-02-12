/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import com.intentia.mak.util.MAKException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;
import m3.M3Connector;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import model.entity.XMLEntityModel;

/**
 *
 * @author JCHAUT
 */
public class M3ConfigModel extends Observable implements IEntitiesModel {

    private IEntityModel entity;
    private M3Connector config;
    private LinkedHashMap<String, M3ConfigurationInfo> listConfig;
    private String configSelect;
    private LinkedHashMap<String, M3ConfigurationInfo> listUnderConfig;
    private LinkedHashMap<String, M3ConfigurationInfo> listUnderConfigSelect;
    private String error;

    public M3ConfigModel(int custId, int envId) {
        super();
        this.listUnderConfigSelect = new LinkedHashMap<String, M3ConfigurationInfo>();
        entity = new XMLEntityModel("environnment");
        entity.setIdLink(custId);
        entity.loadData(envId);
    }

    public M3ConfigModel(IEntityModel e) {
        super();
        this.listUnderConfigSelect = new LinkedHashMap<String, M3ConfigurationInfo>();
        entity = e;
    }

    @Override
    public void loadData() throws IOException {
        setChanged();
        notifyObservers("process");
        config = new M3Connector(entity);
        try {
            config.init();
            listConfig = config.lstM3Config();
        } catch (MAKException ex) {
            error = "Impossible de lister les configurations!\n" + ex.getCause().getLocalizedMessage();
            setChanged();
            notifyObservers("error");
        }

        if (listConfig != null && !listConfig.isEmpty()) {
            configSelect = listConfig.keySet().toArray()[0].toString();
            loadUnderConfig();
            setChanged();
            notifyObservers();
        }
    }

    private void loadUnderConfig() {
        if (listConfig != null && !listConfig.isEmpty()) {
            listUnderConfig = new LinkedHashMap<>();
            boolean insert = false;
            for (String s : listConfig.keySet()) {
                if (insert) {
                    listUnderConfig.put(s, listConfig.get(s));
                }
                if (s.equals(configSelect)) {
                    insert = true;
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

    public static List<String> formatUIConfig(LinkedHashMap<String, M3ConfigurationInfo> conf) {
        ArrayList<String> l = new ArrayList<>();
        if (conf != null) {
            for (String c : conf.keySet()) {
                l.add(conf.get(c).getName() + " (" + conf.get(c).getDescription() + ")");
            }
        }
        return l;
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

    public LinkedHashMap<String, M3ConfigurationInfo> getListConfig() {
        return listConfig;
    }

    public void setListConfig(LinkedHashMap<String, M3ConfigurationInfo> listConf) {
        this.listConfig = listConf;
    }

    public M3Connector getConfig() {
        return config;
    }

    public void setConfig(M3Connector config) {
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

    public LinkedHashMap<String, M3ConfigurationInfo> getListUnderConfig() {
        return listUnderConfig;
    }

    public void setListUnderConfig(LinkedHashMap<String, M3ConfigurationInfo> listUnderConfig) {
        this.listUnderConfig = listUnderConfig;
    }

    public LinkedHashMap<String, M3ConfigurationInfo> getListUnderConfigSelect() {
        return listUnderConfigSelect;
    }

    public void setListUnderConfigSelect(LinkedHashMap<String, M3ConfigurationInfo> listUnderConfigSelect) {
        this.listUnderConfigSelect = listUnderConfigSelect;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
