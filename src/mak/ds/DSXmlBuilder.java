// Decompiled by Decompilator v0.9.2 Alpha Copyright 2012 Jérémy Chaut.
// Contact:jeremy.chaut@hotmail.fr 
// Source File Name:DSXmlBuilder.java

package mak.ds;

import com.intentia.mak.util.XMLUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package com.intentia.mak.core.ds:
//            DSData, DSFieldData, DSASTVisitor

public final class DSXmlBuilder
{

    private DSASTVisitor m_dsVisit;
    private DSData m_dsData;
    private Document m_document;
    private LinkedHashMap<String,String[]> fieldsMap=new LinkedHashMap<String,String[]>();
    
    public DSXmlBuilder()
    {
    }

    public void parse(File fileToParse)
    {
        parseDSToAST(fileToParse);
        buildXML(fileToParse);
    }

    public Document getXMLDocument()
    {
        return m_document;
    }

    private void buildXML(File sourceFile)
    {
        try
        {
            String dsName = m_dsData.getClassName();
            dsName.length();
            List fields = m_dsData.getFields();
            m_document = XMLUtil.createNewDocument();
            fieldsMap.clear();
            Element rootElement = m_document.createElement("Root");
            rootElement.setAttribute("ClassName", dsName);
            rootElement.setAttribute("ClassType", "DS");
            rootElement.setAttribute("ClassFile", sourceFile.toString());
            rootElement.setAttribute("Version", m_dsData.getVersion());
            m_document.appendChild(rootElement);
            Element fieldsElement = m_document.createElement("Fields");
            rootElement.appendChild(fieldsElement);
            for(int i = 0; i < fields.size(); i++)
            {
                DSFieldData fieldData = (DSFieldData)fields.get(i);
                Element fieldElement = m_document.createElement("Field");
                fieldElement.setAttribute("name", fieldData.getFieldName());
                fieldsMap.put(fieldData.getFieldName(),fieldData.getData());
                fieldElement.setAttribute("length", fieldData.getFieldLength());
                fieldElement.setAttribute("type", fieldData.getFieldType());
                if(fieldData.getFieldType().equalsIgnoreCase("double"))
                    fieldElement.setAttribute("decimals", fieldData.getFieldDec());
                fieldsElement.appendChild(fieldElement);
            }

            return;
        }
        catch(Exception e)
        {
        }
    }

    private boolean parseDSToAST(File sourceFile)
    {
        try
        {
            boolean vRes = false;
            String fileContent = null;
            fileContent = FileUtils.readFileToString(sourceFile, "UTF-8");
            char source[] = fileContent.toCharArray();
            ASTParser parser = ASTParser.newParser(3);
            parser.setSource(source);
            CompilationUnit cu = (CompilationUnit)parser.createAST(null);
            m_dsData = new DSData();
            m_dsVisit = new DSASTVisitor(m_dsData);
            cu.accept(m_dsVisit);
        }
        catch(IOException ioe)
        {
        }
        return false;
    }
    
    public LinkedHashMap<String,String[]> getFieldsMap(){
        return fieldsMap;
    }

    public DSData getM_dsData() {
        return m_dsData;
    }

    public void setM_dsData(DSData m_dsData) {
        this.m_dsData = m_dsData;
    }
        
}
