/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import java.util.List;
import model.entity.ActionerModel;
import model.entity.EntityTitleModel;

/**
 *
 * @author JCHAUT
 */
public interface IProcessModel {

    public void addActionCmd(ActionerModel e);

    public void addActionCmd(String s, String e);

    public void addEntityTitle(EntityTitleModel e);

    public void addEntityTitle(String s, String e);

    public EntityTitleModel getParentEntity();

    public void setParentEntity(EntityTitleModel parentEntity);

    public List<EntityTitleModel> getListEntity();

    public void setListEntity(List<EntityTitleModel> listEntity);

    public List<ActionerModel> getActioner();

    public void setActioner(List<ActionerModel> listCmd);

    public void process(String s);
}
