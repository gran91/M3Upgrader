/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import tools.Methode;

/**
 *
 *
 */
public class JParser {

    private CompilationUnit cu;
    private static HashMap<String, Methode> listMethod = new HashMap<>();
    private static HashMap<String, Variable> listVariable = new HashMap<>();

    public void parseSource(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        listMethod.clear();
        listVariable.clear();
        try {
            cu = JavaParser.parse(in,"UTF-8");
        } finally {
            in.close();
        }
    }

    public void listMethod() {
        MethodVisitor n = new MethodVisitor();
        n.visit(cu, null);
    }

    public void getMethod(String name) {
        MethodVisitor n = new MethodVisitor();
        n.visit(cu, name);
    }

    public void listField() {
        FieldVisitor n = new FieldVisitor();
        n.visit(cu, null);
    }

    public String getClassOrInterface() {
        ClassOrInterfaceDeclarationVisitor n = new ClassOrInterfaceDeclarationVisitor();
        n.visit(cu, null);
        return n.result;
    }

    public String getImports() {
        String importsFormat = cu.getImports().toString();
        importsFormat = importsFormat.replace("[", "");
        importsFormat = importsFormat.replace(",", "");
        importsFormat = importsFormat.replace("]", "");
        return "\n" + importsFormat;
    }

    public String getPackage() {
        return cu.getPackage().toString();
    }
    
    public String getFormatPackage() {
        return cu.getPackage().toString().replace("package", "").replace(";","").trim();
    }

    public String getPrincipalCommentaire() {
        String packageFormat = "\n";
        if (cu.getComments().get(0) != null) {
            packageFormat = cu.getComments().get(0).toString();
        }

        return packageFormat;
    }

    private static class MethodVisitor extends VoidVisitorAdapter {

        String result = "";
        int begLine = 0;
        int endLine = 0;
        String name = "";

        @Override
        public void visit(MethodDeclaration m, Object arg) {
            if (m.getName().equals(((String) arg)) || arg == null) {
                DumpVisitor d = new DumpVisitor();
                d.visit(m, arg);
                result = d.getSource();
                begLine = m.getBeginLine();
                endLine = m.getEndLine();
                name = m.getName();
                Methode methode = new Methode(name, begLine, endLine, result);
                listMethod.put(name, methode);
            }
        }

    }

    private static class FieldVisitor extends VoidVisitorAdapter {

        String name = "";
        String source = "";
        int beginLine = 0;
        int endLine = 0;

        @Override
        public void visit(FieldDeclaration f, Object arg) {

            DumpVisitor d = new DumpVisitor();
            d.visit(f, arg);
            name = f.getVariables().get(0).getId().getName();
            source = d.getSource();
            beginLine = f.getBeginLine();
            endLine = f.getBeginLine();
            listVariable.put(name, new Variable(name, beginLine, endLine, source));
        }
    }

    private static class ClassOrInterfaceDeclarationVisitor extends VoidVisitorAdapter {

        String result = "";

        @Override
        public void visit(ClassOrInterfaceDeclaration m, Object arg) {
            DumpVisitor d = new DumpVisitor();
            d.visit(m, arg);
            result += d.getSource();
        }
    }

    public HashMap<String, Methode> getListMethod() {
        return listMethod;
    }

    public  void setListMethod(HashMap<String, Methode> listMethod) {
        listMethod = listMethod;
    }

    public HashMap<String, Variable> getListVariable() {
        return listVariable;
    }

    public void setListVariable(HashMap<String, Variable> listVariable) {
        JParser.listVariable = listVariable;
    }

    
}
