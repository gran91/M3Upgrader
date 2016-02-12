package mak.db;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.Attributes;

public class View {

    Vector keys = new Vector(10, 10);
    Vector joinFields = new Vector(10, 10);
    String viewName;
    String unique;
    int keyFieldCounter = 0;
    Vector selectOmits = new Vector(10, 10);
    Vector joinConditions = new Vector(10, 10);
    String join;

    public void addKeyField(KeyField key) {
        key.isJoin = ((this.join != null) && (this.join.equals("true")));
        key.unique = (this.unique.equals("true") ? 'Y' : 'N');
        key.sequence = ("" + this.keyFieldCounter++);
        this.keys.addElement(key);
    }

    public void addField(Field f) {
        this.joinFields.addElement(f);
    }

    public Vector getJoinFields() {
        return this.joinFields;
    }

    public void addSelectOmit(SelectOmit so) {
        this.selectOmits.addElement(so);
    }

    public void addJoin(Join join) {
        this.joinConditions.addElement(join);
    }

    public void setAttribute(Attributes attr) {
        this.viewName = attr.getValue("name");
        this.unique = attr.getValue("isUnique");
        this.join = attr.getValue("isJoin");
    }

    public String getViewName() {
        return this.viewName;
    }

    public String getXML(int tabs) {
        String x = Table.getTabs(tabs) + "<index name=\"" + MetaDataDef.XmlEscape.escapeString(this.viewName) + "\" isUnique=\"" + this.unique + "\" isJoin=\"" + this.join + "\">" + Table.LineFeed + "";
        Enumeration en = this.joinConditions.elements();
        Join j = null;
        String conds = "";
        while (en.hasMoreElements()) {
            j = (Join) en.nextElement();
            x = x + j.getJoinTableXML(tabs + 1);
            conds = conds + j.getJoinConditionsXML(tabs + 2);
        }
        if (!conds.equals("")) {
            x = x + Table.getTabs(tabs + 1) + "<joinConditions>" + Table.LineFeed + "" + conds + Table.getTabs(tabs + 1) + "</joinConditions>" + Table.LineFeed + "";
        }
        x = x + Table.getTabs(tabs + 1) + "<keys>" + Table.LineFeed + "";
        Enumeration e = this.keys.elements();
        KeyField f = null;
        while (e.hasMoreElements()) {
            f = (KeyField) e.nextElement();
            x = x + f.getXML(tabs + 2);
        }
        x = x + Table.getTabs(tabs + 1) + "</keys>" + Table.LineFeed + "";
        if (!this.selectOmits.isEmpty()) {
            x = x + Table.getTabs(tabs + 1) + "<conditions><condition>" + Table.LineFeed + "";
            Enumeration enu = this.selectOmits.elements();
            SelectOmit s = null;
            int counter = 0;
            String so = "";
            while (enu.hasMoreElements()) {
                s = (SelectOmit) enu.nextElement();
                so = s.getXML(tabs + 2, counter);
                if (!so.equals("")) {
                    counter++;
                }
                x = x + so;
            }
            x = x + Table.getTabs(tabs + 1) + "</condition></conditions>" + Table.LineFeed + "";
        }
        x = x + Table.getTabs(tabs) + "</index>" + Table.LineFeed;
        return x;
    }

    public String[][] getKeyMetaData() {
        int len = this.keys.size();
        String[][] ret = new String[len][];
        int i = 0;
        Enumeration e = this.keys.elements();
        KeyField f = null;
        while (e.hasMoreElements()) {
            f = (KeyField) e.nextElement();
            ret[(i++)] = f.getMetaData();
        }
        return ret;
    }

    public String[][] getSelectOmitMetaData() {
        String[][] temp = (String[][]) null;
        String[][] ret = (String[][]) null;
        Vector so = new Vector();
        Enumeration e = this.selectOmits.elements();
        SelectOmit s = null;
        while (e.hasMoreElements()) {
            s = (SelectOmit) e.nextElement();
            temp = s.getMetaData();
            if (temp != null) {
                for (int i = 0; i < temp.length; i++) {
                    so.addElement(temp[i]);
                }
            }
        }
        ret = new String[so.size() + 1][];
        int i = 0;
        for (i = 0; i < so.size(); i++) {
            ret[i] = ((String[]) (String[]) so.elementAt(i));
        }
        return ret;
    }

    public String[][] getJoinMetaData() {
        String[][] temp = (String[][]) null;
        String[][] ret = (String[][]) null;
        Vector join = new Vector();
        Enumeration e = this.joinConditions.elements();
        Join j = null;
        while (e.hasMoreElements()) {
            j = (Join) e.nextElement();
            temp = j.getMetaData();
            if (temp != null) {
                for (int i = 0; i < temp.length; i++) {
                    join.addElement(temp[i]);
                }
            }
        }
        ret = new String[join.size()][];
        int i = 0;
        for (i = 0; i < join.size(); i++) {
            ret[i] = ((String[]) (String[]) join.elementAt(i));
        }
        return ret;
    }
}