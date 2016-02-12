/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import com.app.main.Ressource;
import com.app.process.Process;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import model.entity.IEntityModel;
import ui.tools.FileDialog;
import zio.XMLManage;
import znio.ProcessFile;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ActionNumber {

    private String name, disk, host, port, login, pass;
    private int type;
    private FileDialog m3beFile;
    private Frame frame;
    private zio.XMLManage xml;
    private ArrayList<String> allAct, hFixActn;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private IEntityModel entity;

    public ActionNumber(IEntityModel e) {
        entity = e;
        name = "";//Nom env dans M3BE
    }

    public ArrayList<String> lstHFixAction() {
        hFixActn = lstAction(Ressource.HFixDir);
        return hFixActn;
    }

    public ArrayList<String> lstAllAction() {
        allAct = lstAction("");
        return allAct;
    }

    public ArrayList<String> lstAction(String dir) {
        ArrayList<String> a = new ArrayList<>();
        ProcessFile pFile = new ProcessFile();
        pFile.setExtFilter(".xml");
        if (entity.getData() != null) {
            name = entity.getData().get(0);
            File f = new File(entity.getData().get(7) + System.getProperty("file.separator") + Ressource.MAKTOOLS + System.getProperty("file.separator") + name + Ressource.M3ACTDIR + dir);
            if (f.exists() && f.isDirectory()) {
                try {
                    Files.walkFileTree(f.toPath(), pFile);
                } catch (IOException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (Path file : pFile.getList()) {
                    String[] s = file.getFileName().toString().split("_");
                    if (s.length >= 2) {
                        a.add(s[0]);
                    }
                }
            }
        }
        return a;
    }

    public boolean createActionNumber(String conf, String nb, String id) {
        xml = new XMLManage("logfile", "head");
        ArrayList<String> header = new ArrayList<>();
        header.add("1310");
        header.add("M3");
        xml.add(new ArrayList(Arrays.asList(Ressource.headActn)), header);
        ArrayList<String> session = new ArrayList<>();
        session.add(nb);
        session.add("");
        session.add("Migration");
        session.add(sdf.format(new Date()));
        session.add(name);
        session.add(id);
        session.add("development");
        xml.add("session", new ArrayList(Arrays.asList(Ressource.sessionActn)), session);
        ArrayList<String> component = new ArrayList<String>();
        component.add("HFix_" + nb + "_" + conf);
        component.add(sdf.format(new Date()));
        component.add("enabled");
        xml.add("component", new ArrayList(Arrays.asList(Ressource.componentActn)), component);
        ArrayList<String> file = new ArrayList<String>();
        file.add("");
        file.add(".java");
        file.add("update");
        file.add("enabled");
        file.add("path");
        component.add(sdf.format(new Date()));
        file.add("file");
        xml.add("component", "file", new ArrayList(Arrays.asList(Ressource.fileActn)), file);
        try {
            xml.save("Test.xml");
        } catch (IOException ex) {
            Ressource.logger.error(ex.getLocalizedMessage(), ex);
        }
        return true;
    }
    
    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public ArrayList<String> getAllAct() {
        return allAct;
    }

    public void setAllAct(ArrayList<String> allAct) {
        this.allAct = allAct;
    }

    public ArrayList<String> gethFixActn() {
        return hFixActn;
    }

    public void sethFixActn(ArrayList<String> hFixActn) {
        this.hFixActn = hFixActn;
    }
}
