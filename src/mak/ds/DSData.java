// Decompiled by Decompilator v0.9.2 Alpha Copyright 2012 Jérémy Chaut.
// Contact:jeremy.chaut@hotmail.fr 
// Source File Name:DSData.java

package mak.ds;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.intentia.mak.core.ds:
//            DSFieldData

public class DSData
{

    private String m_className;
    private String m_version;
    private List m_fieldList;

    DSData()
    {
        m_className = "";
        m_fieldList = new ArrayList();
    }

    public String getClassName()
    {
        return m_className;
    }

    public void setClassName(String className)
    {
        m_className = className;
    }

    public String getVersion()
    {
        return m_version;
    }

    public void setVersion(String version)
    {
        m_version = version;
    }

    public void addField(String name, String type, int length, int dec)
    {
        m_fieldList.add(new DSFieldData(name, type, (new StringBuilder()).append(length).toString(), (new StringBuilder()).append(dec).toString()));
    }
    
    public void addField(DSFieldData dsfield)
    {
        m_fieldList.add(dsfield);
    }

    public List getFields()
    {
        return m_fieldList;
    }

    public DSFieldData getField(String name)
    {
        for(int i = 0; i < m_fieldList.size(); i++)
        {
            DSFieldData field = (DSFieldData)m_fieldList.get(i);
            if(field.getFieldName().equalsIgnoreCase(name))
                return field;
        }

        return null;
    }
}
