/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import model.entity.ActionerModel;
import model.entity.EntityTitleModel;

/**
 *
 * @author JCHAUT
 */
public abstract class AbstractProcessModel extends Observable implements IProcessModel {

    protected EntityTitleModel parentEntity;
    protected List<EntityTitleModel> listEntity = new ArrayList<>();
    protected List<ActionerModel> listCmd = new ArrayList<>();
    protected AbstractProcessModel model;
    protected Object[][] data;

    public AbstractProcessModel(EntityTitleModel e) {
        parentEntity = e;
        model = this;
    }

    public void addEntityTitle(EntityTitleModel e) {
        listEntity.add(e);
    }

    public void addEntityTitle(String s, String e) {
        listEntity.add(new EntityTitleModel(s, e));
    }

    @Override
    public void addActionCmd(ActionerModel e) {
        listCmd.add(e);
    }

    @Override
    public void addActionCmd(String s, String e) {
        listCmd.add(new ActionerModel(s, e));
    }

    public List<EntityTitleModel> getListEntity() {
        return listEntity;
    }

    public void setListEntity(List<EntityTitleModel> listEntity) {
        this.listEntity = listEntity;
    }

    @Override
    public EntityTitleModel getParentEntity() {
        return parentEntity;
    }

    @Override
    public void setParentEntity(EntityTitleModel parentEntity) {
        this.parentEntity = parentEntity;
    }

    @Override
    public List<ActionerModel> getActioner() {
        return listCmd;
    }

    @Override
    public void setActioner(List<ActionerModel> listCmd) {
        this.listCmd = listCmd;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}
