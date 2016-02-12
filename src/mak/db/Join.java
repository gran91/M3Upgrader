package mak.db;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.Attributes;

public class Join {

    String viewName;
    String fromTableName;
    String toTableName;
    Vector joinConds = new Vector();
    Table table;
    Vector joinFields = new Vector();
    public static final String version = "$Revision: /main/DEV_12/SP_12.4.6/4 $";

    public Join(Table table, String fromTableName, String viewName) {
        this.table = table;
        this.viewName = viewName;
        this.fromTableName = fromTableName;
    }

    public void addJoinField(Field f) {
        this.joinFields.addElement(f);
    }

    public Vector getJoinFields() {
        return this.joinFields;
    }

    public void setAttribute(Attributes a) {
        this.toTableName = a.getValue("name");
    }

    public void addJoinCondition(Attributes a) {
        JoinCondition cond = new JoinCondition(this.viewName, this.fromTableName, a.getValue("fromField"), this.toTableName, a.getValue("toField"));
        this.joinConds.add(cond);
    }

    public void addJoinCondition(String fromField, String toTableName, String toField) {
        JoinCondition cond = new JoinCondition(this.viewName, this.fromTableName, fromField, toTableName, toField);
        if (!this.joinConds.contains(cond)) {
            this.joinConds.add(cond);
        }
    }

    public String[][] getMetaData() {
        int len = this.joinConds.size();
        String[][] ret = new String[len][];
        int counter = 0;
        Enumeration e = this.joinConds.elements();
        JoinCondition a = null;
        while (e.hasMoreElements()) {
            a = (JoinCondition) e.nextElement();
            ret[counter] = a.getMetaData();
            counter++;
        }
        return ret;
    }

    public String getJoinTableXML(int tabs) {
        String ret = Table.getTabs(tabs) + "<joinTable name=\"" + MetaDataDef.XmlEscape.escapeString(this.toTableName) + "\">" + Table.LineFeed + "";
        Enumeration e = this.joinFields.elements();
        if (this.joinFields.isEmpty()) {
            return "";
        }
        Field f = null;
        while (e.hasMoreElements()) {
            f = (Field) e.nextElement();
            ret = ret + f.getXML(tabs + 1);
        }
        ret = ret + Table.LineFeed + Table.getTabs(tabs) + "</joinTable>" + Table.LineFeed;
        return ret;
    }

    public String getJoinConditionsXML(int tabs) {
        if (!this.toTableName.equals(this.fromTableName)) {
            String ret = Table.getTabs(tabs) + "<joinedTable name=\"" + MetaDataDef.XmlEscape.escapeString(this.toTableName) + "\">" + Table.LineFeed + "";
            Enumeration e = this.joinConds.elements();
            if (this.joinConds.isEmpty()) {
                return "";
            }
            JoinCondition jc = null;
            while (e.hasMoreElements()) {
                jc = (JoinCondition) e.nextElement();
                ret = ret + jc.getXML(tabs + 1);
            }
            ret = ret + Table.getTabs(tabs) + "</joinedTable>" + Table.LineFeed;
            return ret;
        }
        return "";
    }

    public class JoinCondition {

        String viewName;
        String fromField;
        String toField;
        String fromTable;
        String toTable;

        public JoinCondition(String viewName, String fromTable, String fromField, String toTable, String toField) {
            this.viewName = viewName;
            this.fromField = fromField;
            this.toField = toField;
            this.fromTable = fromTable;
            this.toTable = toTable;
        }

        public boolean equals(Object o) {
            try {
                JoinCondition jc = (JoinCondition) o;
                return (jc.viewName.equals(this.viewName)) && (jc.fromField.equals(this.fromField)) && (jc.toField.equals(this.toField)) && (jc.fromTable.equals(this.fromTable)) && (jc.toTable.equals(this.toTable));
            } catch (Exception e) {
            }
            return false;
        }

        public String[] getMetaData() {
            int size = 6;
            String[] str = new String[size];
            str[0] = this.viewName;
            str[1] = this.fromTable;
            str[2] = this.fromField;
            str[3] = this.toTable;
            str[4] = this.toField;
            str[5] = "";
            return str;
        }

        public String getXML(int tabs) {
            return Table.getTabs(tabs) + "<joinCondition fromField=\"" + MetaDataDef.XmlEscape.escapeString(this.fromField) + "\" toField=\"" + MetaDataDef.XmlEscape.escapeString(this.toField) + "\" />" + Table.LineFeed + "";
        }
    }
}