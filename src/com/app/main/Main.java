package com.app.main;

import com.app.controler.ProcessControler;
import com.app.model.m3.M3UpgradModel;
import com.app.ui.PMain;
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import model.entity.EntityTitleModel;

/**
 *
 * @author jchaut
 */
public class Main {

    String dir;

    public Main() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                init();
                start();
                changeUIdefaults();
                EntityTitleModel e = new EntityTitleModel(Ressource.title, "customer");
                M3UpgradModel model = new M3UpgradModel(e);

                PMain p = new PMain(model);
                ProcessControler processControler = new ProcessControler(p);
                Ressource.mainView = p;
            }
        });
    }

    public static void main(String[] args) {
///new EntityCreator();
        new Main();
    }

    public void init() {
        dir = Ressource.conf.getConfig().getProperty("config.skin");
//        PropertyConfigurator.configure("log4j.properties");
    }

    private void start() {
        try {
            File f = new File(dir);
            if (f.exists()) {
                Skin theSkinToUse = SkinLookAndFeel.loadThemePack(dir);
                SkinLookAndFeel.setSkin(theSkinToUse);
                UIManager.setLookAndFeel(new SkinLookAndFeel());
            } else {
                for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(laf.getName())) {
                        try {
                            UIManager.setLookAndFeel(laf.getClassName());
                        } catch (Exception e) {
                             ///TODO                          : handle exception
                        }

                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Skin: " + dir + " Error", "Skin", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeUIdefaults() {
         // setting taskpanecontainer defaults
        UIManager.put("TaskPaneContainer.useGradient", Boolean.FALSE);
        UIManager.put("TaskPaneContainer.background", Color.LIGHT_GRAY);
        UIManager.put("TaskPaneContainer.backgroundPainter", Color.LIGHT_GRAY);
         // setting taskpane defaults
        UIManager.put("TaskPane.font", new FontUIResource(new Font("Verdana", Font.BOLD, 16)));
        UIManager.put("TaskPane.titleBackgroundGradientStart", Color.WHITE);
        UIManager.put("TaskPane.titleBackgroundGradientEnd", Color.GRAY);
    }
}
