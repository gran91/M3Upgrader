/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controler;

import com.app.ui.IUIProcess;
import controler.AbstractControler;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.isEventDispatchThread;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author JCHAUT
 */
public class ProcessControler extends AbstractControler implements ActionListener {

    private IUIProcess view;
    private ProcessControler controler;

    public ProcessControler(IUIProcess w) {
        view = w;
        controler = this;
        if (isEventDispatchThread() || EventQueue.isDispatchThread()) {
            view.load();
            view.loadData();
            view.show();
            view.control(controler);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    view.load();
                    view.loadData();
                    view.show();
                    view.control(controler);
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("loadData")) {
            view.loadData();
        } else {
            view.getModel().process(e.getActionCommand());
        }

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
