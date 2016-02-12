/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mak.core.movex;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3Progress implements IProgressMonitor{

    @Override
    public void beginTask(String string, int i) {
    }

    @Override
    public void done() {
    }

    @Override
    public void internalWorked(double d) {
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void setCanceled(boolean bln) {
    }

    @Override
    public void setTaskName(String string) {
    }

    @Override
    public void subTask(String string) {
    }

    @Override
    public void worked(int i) {
    }
    
}
