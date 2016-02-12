/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.menu;

import controler.AbstractControler;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import model.entity.IEntityModel;
import ui.entity.IUIEntity;
import ui.tools.MenuBarDefault;

/**
 *
 * @author jeremy.chaut
 */
public class Menu extends MenuBarDefault implements IUIEntity {

    JMenuItem itemWork, itemConfig, itemAnalyze, itemMigrate, itemListField, itemLang, itemCust;

    public Menu() {
        super();
    }

    public void allMenu() {
        mainMenu();

        mainMenu = new JMenu(i18n.Language.getLabel(183));
        itemCust = new JMenuItem(i18n.Language.getLabel(54));
        itemCust.setActionCommand("entities_customer");
        itemCust.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
        listItem.add(itemCust);
        mainMenu.add(itemCust);
        itemNew = new JMenuItem(i18n.Language.getLabel(9));
        itemNew.setActionCommand("entities_environnment");
        itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
        listItem.add(itemNew);
        mainMenu.add(itemNew);
        itemOpen = new JMenuItem(i18n.Language.getLabel(82));
        itemOpen.setActionCommand("entities_typesys");
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
        listItem.add(itemOpen);
        mainMenu.add(itemOpen);
        itemWork = new JMenuItem(i18n.Language.getLabel(182));
        itemWork.setActionCommand("entities_workspace");
        itemWork.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
        listItem.add(itemWork);
        mainMenu.add(itemWork);
        itemLang = new JMenuItem(i18n.Language.getLabel(38));
        itemLang.setActionCommand("entities_langage");
        itemLang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));
        listItem.add(itemLang);
        mainMenu.add(itemLang);

        this.add(mainMenu);
        runMenu();
        lngMenu();
    }

    private void runMenu() {
        JMenu runMenu = new JMenu(i18n.Language.getLabel(203));
        itemAnalyze = new JMenuItem(i18n.Language.getLabel(208));
        itemAnalyze.setActionCommand("analyze");
        itemAnalyze.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
        listItem.add(itemAnalyze);
        runMenu.add(itemAnalyze);
        itemMigrate = new JMenuItem(i18n.Language.getLabel(209));
        itemMigrate.setActionCommand("migrate");
        itemMigrate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        listItem.add(itemMigrate);
        runMenu.add(itemMigrate);
        
        itemListField = new JMenuItem("Liste field");
        itemListField.setActionCommand("listfield");
        itemListField.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        listItem.add(itemListField);
        runMenu.add(itemListField);
        
        this.add(runMenu);
    }

    @Override
    public void load() {
        allMenu();
    }

    @Override
    public IEntityModel getModelEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void control(AbstractControler controller) {
        for (int i = 0; i < listItem.size(); i++) {
            listItem.get(i).addActionListener(controller);
        }
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
