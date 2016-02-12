// Decompiled by Decompilator v0.9.2 Alpha Copyright 2012 Jérémy Chaut.
// Contact:jeremy.chaut@hotmail.fr 
// Source File Name:DSASTVisitor.java

package mak.ds;

import java.util.List;
import org.eclipse.jdt.core.dom.*;

// Referenced classes of package com.intentia.mak.core.ds:
//            DSData

public final class DSASTVisitor extends ASTVisitor
{

    private DSData m_dsData;

    public DSASTVisitor(DSData dsData)
    {
        m_dsData = dsData;
    }

    public DSASTVisitor(boolean visitDocTags)
    {
        super(visitDocTags);
    }

    public boolean visit(TypeDeclaration node)
    {
        m_dsData.setClassName(node.getName().toString());
        return super.visit(node);
    }

    public boolean visit(FieldDeclaration node)
    {
        String versionNode = "version";
        List fragments = node.fragments();
        String content = fragments.get(0).toString();
        if(content.startsWith("version"))
        {
            int startOfVersionContent = content.indexOf("=");
            String versionString = content.substring(startOfVersionContent + 2, content.length() - 1);
            m_dsData.setVersion(versionString);
        }
        return super.visit(node);
    }

    public boolean visit(MethodDeclaration node)
    {
        String syncToDSName = "syncToVars";
        String metName = node.getName().toString();
        int fieldDecimals = 0;
        if("syncToVars".equalsIgnoreCase(metName))
        {
            List stmtList = node.getBody().statements();
            Statement is = (Statement)stmtList.toArray()[0];
            if(is.getNodeType() == 25)
            {
                IfStatement ifstmt = (IfStatement)is;
                Block setBlock = (Block)ifstmt.getThenStatement();
                List fieldStmts = setBlock.statements();
                for(int i = 0; i < fieldStmts.size(); i++)
                {
                    ExpressionStatement setField = (ExpressionStatement)fieldStmts.get(i);
                    String fieldType = getFieldType(setField);
                    String fieldName = getFieldName(setField);
                    int fieldLength = getFieldLength(setField);
                    fieldDecimals = getFieldDecimals(setField);
                    if(fieldType.length() > 0 && fieldName.length() > 0)
                        m_dsData.addField(fieldName, fieldType, fieldLength, fieldDecimals);
                }

            }
        }
        return super.visit(node);
    }

    private String getFieldType(ExpressionStatement expStatement)
    {
        String fieldType = "";
        if(expStatement.getExpression().getNodeType() == 7)
        {
            Assignment ass = (Assignment)expStatement.getExpression();
            if(ass.getRightHandSide().getNodeType() == 32)
            {
                MethodInvocation methodCall = (MethodInvocation)ass.getRightHandSide();
                String methodName = methodCall.getName().toString();
                if(methodName.equalsIgnoreCase("getInt"))
                    fieldType = "int";
                else
                if(methodName.equalsIgnoreCase("getLong"))
                    fieldType = "long";
                else
                if(methodName.equalsIgnoreCase("getDouble"))
                    fieldType = "double";
                else
                if(methodName.equalsIgnoreCase("charAt"))
                    fieldType = "char";
            }
        } else
        if(expStatement.getExpression().getNodeType() == 32)
        {
            MethodInvocation methodCall = (MethodInvocation)expStatement.getExpression();
            if(methodCall.getName().toString().equalsIgnoreCase("moveFromSubstring"))
                fieldType = "MvxString";
        }
        return fieldType;
    }

    private int getFieldLength(ExpressionStatement expStatement)
    {
        int fieldLength = 0;
        if(expStatement.getExpression().getNodeType() == 7)
        {
            Assignment ass = (Assignment)expStatement.getExpression();
            if(ass.getRightHandSide().getNodeType() == 32)
            {
                MethodInvocation methodCall = (MethodInvocation)ass.getRightHandSide();
                String methodName = methodCall.getName().toString();
                List methodArgs = methodCall.arguments();
                if(methodName.equalsIgnoreCase("getInt") || methodName.equalsIgnoreCase("getLong") || methodName.equalsIgnoreCase("getDouble"))
                    fieldLength = (new Integer(methodArgs.get(0).toString())).intValue();
                else
                if(methodCall.getName().toString().equalsIgnoreCase("charAt"))
                    fieldLength = 1;
            }
        } else
        if(expStatement.getExpression().getNodeType() == 32)
        {
            MethodInvocation methodCall = (MethodInvocation)expStatement.getExpression();
            if(methodCall.getName().toString().equalsIgnoreCase("moveFromSubstring"))
            {
                List methodArgs = methodCall.arguments();
                fieldLength = (new Integer(methodArgs.get(2).toString())).intValue();
            }
        }
        return fieldLength;
    }

    private int getFieldDecimals(ExpressionStatement expStatement)
    {
        int fieldDec = 0;
        if(expStatement.getExpression().getNodeType() == 7)
        {
            Assignment ass = (Assignment)expStatement.getExpression();
            if(ass.getRightHandSide().getNodeType() == 32)
            {
                MethodInvocation methodCall = (MethodInvocation)ass.getRightHandSide();
                List methodArgs = methodCall.arguments();
                String methodName = methodCall.getName().toString();
                if(methodName.equalsIgnoreCase("getLong") || methodName.equalsIgnoreCase("getDouble"))
                    fieldDec = (new Integer(methodArgs.get(1).toString())).intValue();
            }
        }
        return fieldDec;
    }

    private String getFieldName(ExpressionStatement expStatement)
    {
        String fieldName = "";
        if(expStatement.getExpression().getNodeType() == 7)
        {
            Assignment ass = (Assignment)expStatement.getExpression();
            if(ass.getRightHandSide().getNodeType() == 32)
            {
                MethodInvocation methodCall = (MethodInvocation)ass.getRightHandSide();
                String methodName = methodCall.getName().toString();
                if(methodName.equalsIgnoreCase("getInt") || methodName.equalsIgnoreCase("getLong") || methodName.equalsIgnoreCase("getDouble") || methodName.equalsIgnoreCase("charAt"))
                    fieldName = ass.getLeftHandSide().toString();
            }
        } else
        if(expStatement.getExpression().getNodeType() == 32)
        {
            MethodInvocation methodCall = (MethodInvocation)expStatement.getExpression();
            if(methodCall.getName().toString().equalsIgnoreCase("moveFromSubstring"))
                fieldName = methodCall.getExpression().toString();
        }
        return fieldName;
    }
}
