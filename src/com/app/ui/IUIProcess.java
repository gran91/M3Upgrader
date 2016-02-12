/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui;

import com.app.model.IProcessModel;
import controler.AbstractControler;
import ui.IUIMain;


/**
 *
 * @author Jeremy.CHAUT
 */
public interface IUIProcess extends IUIMain{

    public void load();

    public void loadData();

    public void control(AbstractControler controller);

    public void show();

    public void close();

    public void setShowType(int type);

    public IProcessModel getModel();
    
}
