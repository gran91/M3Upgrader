package mak.db;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.Attributes;

public class SelectOmit {

    Vector selects = new Vector();
    String viewName;
    Table table;

    public SelectOmit(Table t, String viewName) {
        this.viewName = viewName;
        this.table = t;
    }

    public String getXML(int tabs, int counter) {
        String str = "";
        Enumeration e = this.selects.elements();
        SelectAttribute a = null;
        if (this.selects.isEmpty()) {
            return "";
        }
        while (e.hasMoreElements()) {
            a = (SelectAttribute) e.nextElement();
            str = str + a.getXML(tabs + 1, counter++);
        }
        return str;
    }

    public String[][] getMetaData() {
        int len = this.selects.size();
        String[][] ret = new String[len][];
        int counter = 0;
        Enumeration e = this.selects.elements();
        SelectAttribute a = null;
        while (e.hasMoreElements()) {
            a = (SelectAttribute) e.nextElement();
            ret[counter] = a.getMetaData();
            ret[counter][1] = ("" + counter);
            counter++;
        }
        return ret;
    }

    public void addSelection(String fieldName, String operator, String value, String property) {
        SelectAttribute a = new SelectAttribute();
        a.select_And = property;
        a.viewName = this.viewName;
        a.name = fieldName;
        a.operator = operator;
        a.value = value;
        this.selects.addElement(a);
    }

    public void setAttribute(Attributes attr) {
        SelectAttribute a = new SelectAttribute();
        if (this.selects.isEmpty()) {
            a.select_And = "S";
        }
        a.viewName = this.viewName;
        a.name = attr.getValue("name");
        a.operator = attr.getValue("operator");
        a.value = attr.getValue("value");
        a.select_And = attr.getValue("property");
        if (a.select_And == null) {
            a.select_And = "";
        }
        if (a.select_And.equals("")) {
            a.select_And = "S";
        } else if (a.select_And.equals("AND")) {
            a.select_And = "A";
        } else {
            a.select_And = "S";
        }
        if (a.value.equals("")) {
            a.value = " ";
        }
        if (this.table.getType(a.name).equals("s")) {
            a.value = ("'" + a.value + "'");
        }
        this.selects.addElement(a);
    }

    public class SelectAttribute {

        public String name;
        public String operator;
        public String value;
        public String viewName;
        public String select_And = "A";

        public SelectAttribute() {
        }

        public String getXML(int tabs, int counter) {
            if (counter == 0) {
                this.select_And = "S";
            } else if (this.select_And.equals("S")) {
                this.select_And = "O";
            }
            String str = new StringBuilder().append(Table.getTabs(tabs)).append("<column name=\"").append(MetaDataDef.XmlEscape.escapeString(this.name)).append("\" property=\"").append(this.select_And.equals("A") ? "AND" : this.select_And.equals("S") ? "" : "OR").append("\" operator=\"").append(MetaDataDef.XmlEscape.escapeString(this.operator)).append("\" value=\"").append(MetaDataDef.XmlEscape.escapeString(this.value)).toString();
            str = new StringBuilder().append(str).append("\" />").append(Table.LineFeed).append("").toString();
            return str;
        }

        public String[] getMetaData() {
            int size = 6;
            String[] str = new String[size];
            str[0] = this.viewName;
            str[1] = "0";
            if ((this.select_And.equals("")) || (this.select_And.equals("O"))) {
                str[2] = "S";
            }
            str[2] = this.select_And;
            str[3] = this.operator;
            str[4] = this.value;
            str[5] = this.name;
            return str;
        }
    }
}