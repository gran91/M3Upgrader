package com.app.ui;

import com.app.main.Ressource;
import com.app.model.AbstractProcessModel;
import com.app.model.IProcessModel;
import controler.AbstractControler;
import controler.entities.DefaultControlerEntities;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.entities.XMLEntitiesModel;
import model.entity.ActionerModel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import ui.entities.XMLUIChooseEntity;
import ui.entities.XMLUILink;
import ui.entity.XMLUIEntity;
import ui.tools.MenuBarDefault;
import ui.tools.UITools;

public class UIProcess extends JPanel implements IUIProcess, Observer {

    private final AbstractProcessModel model;
    protected GridBagConstraints gbc;
    protected static int spaceTop = 5;
    protected static int spaceBottom = 0;
    protected static int spaceLeft = 0;
    protected static int spaceRight = 5;
    protected Window parentContainer;
    private XMLUILink mainView;
    protected ArrayList<XMLUIChooseEntity> listView;
    protected ArrayList<AbstractControler> listControler;
    protected JXPanel pBouton;
    protected JXBusyLabel busyCon;
    protected JXButton[] listButton;
    protected int showtype;
    protected String msgErr;
    protected MenuBarDefault menu;

    public UIProcess(AbstractProcessModel m) {
        model = m;
        model.addObserver(this);
    }

    public void loadMain() {
        listControler = new ArrayList<>();
        listView = new ArrayList<>();
        for (int i = 0; i < model.getListEntity().size(); i++) {
            XMLUIChooseEntity view = new XMLUIChooseEntity((XMLEntitiesModel) model.getListEntity().get(i).getEntity());
            view.setTitle(model.getListEntity().get(i).getTitle());
            view.setContainerType(-1);
            listControler.add(new DefaultControlerEntities(view));
            listView.add(view);
        }
        if (model.getParentEntity().getNameEntity() != null && !model.getParentEntity().getNameEntity().isEmpty()) {
            mainView = new XMLUILink((XMLEntitiesModel) model.getParentEntity().getEntity(), listControler);
            mainView.setContainerType(-1);
            new DefaultControlerEntities(mainView);
        }
    }

    public void loadButton() {
        this.pBouton = new JXPanel(new GridBagLayout(), true);
        this.listButton = new JXButton[model.getActioner().size()];
        int x = 1;
        for (int i = 0; i < model.getActioner().size(); i++) {
            this.gbc = new GridBagConstraints();
            this.gbc.gridx = x;
            this.gbc.gridy = 0;
            this.gbc.anchor = 17;
            ActionerModel act = model.getActioner().get(i);
            JXButton b = new JXButton(act.getLabel());
            b.setActionCommand(act.getActionCmd());
            listButton[i] = b;
            this.gbc.insets = new Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
            this.pBouton.add(b, this.gbc);
            x++;
        }
        this.busyCon = UITools.createComplexBusyLabel();
        this.busyCon.setEnabled(false);

        this.gbc = new GridBagConstraints();
        this.gbc.gridx = x;
        this.gbc.gridy = 0;
        this.gbc.anchor = 17;
        this.gbc.insets = new Insets(spaceTop, spaceLeft, spaceBottom, spaceRight);
        this.pBouton.add(this.busyCon, this.gbc);
    }

    @Override
    public void load() {
        loadMain();
        loadButton();
        setLayout(new BorderLayout());
        add(this.mainView, BorderLayout.CENTER);
        add(this.pBouton, BorderLayout.PAGE_END);
    }

    @Override
    public void loadData() {
        Integer[] listId = null;
        if (listView != null) {
            listId = new Integer[listView.size()];
            for (int i = 0; i < listView.size(); i++) {
                listId[i] = model.getListEntity().get(i).getEntity().getModelEntity().getId();
            }
        }
        if (mainView != null) {
            if (mainView.getcLink().getModel().getData() != null) {
                if (mainView.getcLink().getModel().getData().length > 0) {
                    Object[] o = tools.Tools.extractColumn(mainView.getcLink().getModel().getData(), mainView.getcLink().getModel().getData()[0].length - 1);
                    int n = -1;
                    if ((n = new ArrayList<>(Arrays.asList(o)).indexOf(model.getParentEntity().getId())) != -1) {
                        mainView.getcLink().setSelectedIndex(n + 1);
                    }
                }

            }

        }
        for (int i = 0; i < listView.size(); i++) {
            if (listView.get(i).getcEntity().getModel().getData() != null) {
                if (listView.get(i).getcEntity().getModel().getData().length > 0) {
                    Object[] o = tools.Tools.extractColumn(listView.get(i).getcEntity().getModel().getData(), listView.get(i).getcEntity().getModel().getData()[0].length - 1);
                    int n = -1;
                    if ((n = new ArrayList<>(Arrays.asList(o)).indexOf(listId[i])) != -1) {
                        listView.get(i).getcEntity().setSelectedIndex(n + 1);
                    }
                }

            }
        }
    }

    @Override
    public void control(AbstractControler controler) {
        for (JXButton listButton1 : listButton) {
            listButton1.addActionListener(controler);
        }
        for (XMLUIChooseEntity listView1 : listView) {
            listView1.control(controler);
        }
        if (menu != null) {
            if (menu.getListItem() != null) {
                for (int i = 0; i < menu.getListItem().size(); i++) {
                    menu.getListItem().get(i).addActionListener(controler);
                }
            }
        }
    }

    @Override
    public void show() {
        if (showtype == XMLUIEntity.FRAME) {
            showInFrame();
        } else if (showtype == XMLUIEntity.DIALOG) {
            showInDialog();
        }
    }

    public void showInDialog() {
        parentContainer = UITools.createDialog(null, Ressource.title, this);
        if (menu != null) {
            ((JDialog) parentContainer).setJMenuBar(menu);
        }
    }

    public void showInFrame() {
        parentContainer = UITools.createFrame(Ressource.title, this);

//        if (new File(getClass().getClassLoader().getResource(Ressource.imgLogo).getPath()).exists()) {
//            parentContainer.setIconImage(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(main.Ressource.imgLogo))).getImage());
//        }
        if (new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(main.Ressource.imgLogo))).getImage() != null) {
            parentContainer.setIconImage(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(main.Ressource.imgLogo))).getImage());
        }

        ((JFrame) parentContainer).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (menu != null) {
            ((JFrame) parentContainer).setJMenuBar(menu);
        }
    }

    @Override
    public void close() {
        ((Window) this.getParent()).dispose();
    }

    @Override
    public void setShowType(int type) {
        showtype = type;
    }

    @Override
    public IProcessModel getModel() {
        return model;
    }

    public XMLUILink getpMain() {
        return mainView;
    }

    public void setpMain(XMLUILink pMain) {
        this.mainView = pMain;
    }

    public void setMenu(MenuBarDefault m) {
        menu = m;
    }

    @Override
    public Window getWindow() {
        return parentContainer;
    }

    public XMLUILink getMainView() {
        return mainView;
    }

    public void setMainView(XMLUILink mainView) {
        this.mainView = mainView;
    }

    public ArrayList<XMLUIChooseEntity> getListView() {
        return listView;
    }

    public void setListView(ArrayList<XMLUIChooseEntity> listView) {
        this.listView = listView;
    }

    public ArrayList<AbstractControler> getListControler() {
        return listControler;
    }

    public void setListControler(ArrayList<AbstractControler> listControler) {
        this.listControler = listControler;
    }

    public JXButton[] getListButton() {
        return listButton;
    }

    public void setListButton(JXButton[] listButton) {
        this.listButton = listButton;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            switch (arg.toString()) {
                case "loadData":
                    loadData();
                    break;
                case "title":
                    setTitle(Ressource.title);
                    break;
                case "quit":
                    quit();
                    break;
            }
        }
    }

    @Override
    public void setTitle(java.lang.String t) {
        Window w = (Window) SwingUtilities.getRoot(this);
        if (w instanceof JFrame) {
            ((JFrame) w).setTitle(t);
        } else if (w instanceof JDialog) {
            ((JDialog) w).setTitle(t);
        }
    }

    public void quit() {
        Window w = (Window) SwingUtilities.getRoot(this);
        w.dispose();
    }
}
