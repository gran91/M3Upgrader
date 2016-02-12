package mak.db;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class DBContentHandler extends DefaultHandler {

    Table t = null;
    View view = null;
    SelectOmit so = null;
    Field f = null;
    FieldType type = null;
    String joinTableName;
    String status = null;
    Join join = null;
    String filePrefix = null;
    public static final String version = "$Revision: /main/DEV_12/SP_12.4.6/5 $";

    public DBContentHandler(Table tbl) {
        this.t = tbl;
        this.filePrefix = null;
    }

    public void startElement(String a1, String element, String a3, Attributes a) {
        try {
            if (element.equals("table")) {
                this.t.setAttribute(a);
            } else if (element.equals("table.columns")) {
                this.status = "table.columns";
            } else if (element.equals("index")) {
                this.view = new View();
                this.view.setAttribute(a);
                if ((this.t.addView(this.view)) && (this.view.join.equals("true"))) {
                    this.join = new Join(this.t, this.t.tableName, this.view.viewName);
                    this.view.addJoin(this.join);
                }
            } else if (element.equals("keys")) {
                this.status = "keys";
            } else if (element.equals("column")) {
                if ((this.status != null) && (this.status.equals("table.columns"))) {
                    if (this.filePrefix == null) {
                        this.filePrefix = a.getValue("name");
                        if ((this.filePrefix != null) && (this.filePrefix.length() > 2)) {
                            this.filePrefix = this.filePrefix.substring(0, 2);
                        }
                    }
                    this.f = new Field(this.filePrefix);
                    this.f.setAttribute(a);
                    this.t.addField(this.f);
                } else if ((this.status != null) && (this.status.equals("keys"))) {
                    KeyField kf = new KeyField(this.view.getViewName());
                    kf.setAttribute(a);
                    this.view.addKeyField(kf);
                } else if ((this.status != null) && (this.status.equals("condition"))) {
                    this.so.setAttribute(a);
                } else if ((this.status != null) && (this.status.equals("joinTable"))) {
                    this.f = new Field(this.view.viewName, this.joinTableName);
                    this.f.setAttribute(a);
                    this.join.addJoinField(this.f);
                    this.view.addField(this.f);
                }
            } else if (element.equals("type")) {
                this.type = new FieldType();
                this.f.setType(this.type);
            } else if (element.equals("string")) {
                this.type.setDataType("string");
                this.type.setAttribute(a);
            } else if (element.equals("binary")) {
                this.type.setDataType("binary");
                this.type.setAttribute(a);
            } else if (element.equals("decimal")) {
                this.type.setDataType("decimal");
                this.type.setAttribute(a);
            } else if (element.equals("condition")) {
                this.status = "condition";
                this.so = new SelectOmit(this.t, this.view.getViewName());
                this.view.addSelectOmit(this.so);
            } else if (element.equals("joinTable")) {
                this.status = "joinTable";
                this.joinTableName = a.getValue("name");
            } else if (!element.equals("joinConditions")) {
                if (element.equals("joinedTable")) {
                    this.join.setAttribute(a);
                } else if (element.equals("joinCondition")) {
                    this.join.addJoinCondition(a);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void endElement(String a1, String element, String a3) {
        if (!element.equals("Table")) {
            if (element.equals("TableFields")) {
                String[][] a = this.t.getFieldMetaData();
                for (int i = 0; i < a.length; i++) {
                    if (a[i] != null) {
                        for (int j = 0; j < a[i].length; j++);
                    }
                }
            } else if ((!element.equals("TableField")) && (element.equals("View"))) {
                String[][] a = this.view.getKeyMetaData();
                for (int i = 0; i < a.length; i++) {
                    if (a[i] != null) {
                        for (int j = 0; j < a[i].length; j++);
                    }
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) {
    }

    void check(Attributes a) {
        int s = a.getLength();
        for (int i = 0; i < s; i++) {
            System.out.println("local=" + a.getLocalName(i) + " qname=" + a.getQName(i) + " value=" + a.getValue(i));
        }
    }
}