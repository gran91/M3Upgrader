/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import com.app.main.Ressource;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import ui.tools.BusyDialog;

/**
 *
 * @author Jeremy.CHAUT
 */
public abstract class AbstractOutput implements IOutput, Runnable {

    protected String action;
    protected String filepath;
    protected BusyDialog busyDial;
    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String COPYFILE = "COPYFILE";
    public static final String DELETEFILE = "DELETEFILE";

    @Override
    public void run() {
        switch (action) {
            case WRITE:
                try {
                    write();
                    save();
                    busyDial.stop();
                } catch (IOException ex) {
                    error(ex.getMessage());
                }
                break;
            case READ:
                try {
                    write();
                    save();
                    busyDial.stop();
                } catch (IOException ex) {
                    error(ex.getMessage());
                }
                break;
        }
    }

    public void error(String s) {
        busyDial.setError(s);
    }
    
    public void dialStatus(){
        busyDial.dialStatus();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFilePath() {
        return filepath;
    }

    public void setFilePath(String filepath) {
        this.filepath = filepath;
    }

    public BusyDialog getBusyDial() {
        return busyDial;
    }

    public void setBusyDial(BusyDialog busyDial) {
        this.busyDial = busyDial;
    }
}
