/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import com.intentia.mak.core.m3.classpath.M3Path;
import java.io.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3ClassPathFileZ implements M3Path {

    private String m_relativePath;
    private IPath m_realPath;

    public M3ClassPathFileZ(String relativePath) {
        this.m_relativePath = relativePath;
        m_realPath = Path.fromOSString(relativePath);
    }

    @Override
    public IPath getPath() {
        return m_realPath;
    }

    public void setPath(File file) {
        m_realPath = Path.fromOSString(file.getAbsolutePath());
        m_relativePath = file.getAbsolutePath();
    }

    public void setPath(String file) {
        m_realPath = Path.fromOSString(file);
        m_relativePath = file;
    }

    @Override
    public IPath getRealPath() {
        return m_realPath;
    }

}
