package mak.db;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.Attributes;

public class Table {

    public static String LineFeed = System.getProperty("line.separator");
    public static String XML_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + LineFeed;
    public static String Tab = "    ";
    Vector<Field> fields = new Vector(10, 10);
    Vector<View> views = new Vector(10, 10);
    public String tableName;
    public String filetype;
    public String filedescription;
    public String tableSpace;
    public String tablePctFree;
    public String indexPctFree;
    public String defaultSchema;
    public String comment;
    public String maintainedByPgm = "";
    public String archivingFunction = "";
    public String massDeleteFunction = "";
    public String programHeading = "";
    int fieldCounter = 0;
    public static final String version = "$Revision: /main/DEV_12/SP_12.4.6/9 $";

    public String getFileType() {
        return this.filetype;
    }

    public String getFileDescription() {
        return this.filedescription;
    }

    public String getTableSpace() {
        return this.tableSpace;
    }

    public String getTablePctFree() {
        return this.tablePctFree;
    }

    public String getIndexPctFree() {
        return this.indexPctFree;
    }

    public String getDefaultSchema() {
        return this.defaultSchema;
    }

    public String getComment() {
        return this.comment;
    }

    public void addField(Field field) {
        if (field.tableName == null) {
            field.tableName = this.tableName;
        }
        if (field.refFile == null) {
            field.refFile = this.tableName;
        }
        field.sequence = ("" + this.fieldCounter++);
        this.fields.addElement(field);
    }

    public boolean addView(View view) {
        for (int i = 0; i < this.views.size(); i++) {
            if (view.getViewName().equals(((View) this.views.get(i)).getViewName())) {
                return false;
            }
        }
        if (view.getViewName().endsWith("RR")) {
            this.views.add(0, view);
        } else {
            this.views.addElement(view);
        }
        return true;
    }

    public String[][] getFieldMetaData() {
        int len = this.fields.size();
        String[][] ret = new String[50 * len][];
        int i = 0;
        Enumeration e = this.fields.elements();
        Field f = null;
        while (e.hasMoreElements()) {
            f = (Field) e.nextElement();
            ret[(i++)] = f.getMetaData();
        }
        Enumeration ev = this.views.elements();
        View v = null;
        while (ev.hasMoreElements()) {
            v = (View) ev.nextElement();
            Enumeration ej = v.getJoinFields().elements();
            Field jf = null;
            while (ej.hasMoreElements()) {
                jf = (Field) ej.nextElement();
                if (i + 1 >= ret.length) {
                    String[][] retTemp = new String[ret.length * 2][];
                    System.arraycopy(ret, 0, retTemp, 0, i);
                    ret = retTemp;
                }
                ret[(i++)] = jf.getMetaData();
            }
        }
        ret[i] = new String[ret[(i - 1)].length];
        return ret;
    }

    public String[][] getViewMetaData() {
        String[][] temp = (String[][]) null;
        String[][] ret = (String[][]) null;
        Vector lfs = new Vector();
        Enumeration e = this.views.elements();
        View v = null;
        while (e.hasMoreElements()) {
            v = (View) e.nextElement();
            temp = v.getKeyMetaData();
            for (int i = 0; i < temp.length; i++) {
                lfs.addElement(temp[i]);
            }
        }
        ret = new String[lfs.size() * 2][];
        int i = 0;
        for (i = 0; i < lfs.size(); i++) {
            ret[i] = ((String[]) (String[]) lfs.elementAt(i));
        }
        return ret;
    }

    public String[][] getSelectOmitMetaData() {
        String[][] temp = (String[][]) null;
        String[][] ret = (String[][]) null;
        Vector so = new Vector();
        Enumeration e = this.views.elements();
        View v = null;
        while (e.hasMoreElements()) {
            v = (View) e.nextElement();
            temp = v.getSelectOmitMetaData();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] != null) {
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
        Enumeration e = this.views.elements();
        View v = null;
        while (e.hasMoreElements()) {
            v = (View) e.nextElement();
            temp = v.getJoinMetaData();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] != null) {
                    for (int j = 0; j < temp[i].length; j++);
                    join.addElement(temp[i]);
                }
            }
        }
        ret = new String[join.size() + 1][];
        int i = 0;
        for (i = 0; i < join.size(); i++) {
            ret[i] = ((String[]) (String[]) join.elementAt(i));
        }
        return ret;
    }

    public String getXML(int tabs) {
        String x = getTabs(tabs) + "<table name=\"" + this.tableName + "\" filetype=\"" + MetaDataDef.XmlEscape.escapeString(this.filetype) + "\" filedescription=\"" + MetaDataDef.XmlEscape.escapeString(this.filedescription) + "\" tableSpace=\"" + MetaDataDef.XmlEscape.escapeString(this.tableSpace) + "\" tablePctFree=\"" + MetaDataDef.XmlEscape.escapeString(this.tablePctFree) + "\" indexPctFree=\"" + MetaDataDef.XmlEscape.escapeString(this.indexPctFree) + "\" defaultSchema=\"" + MetaDataDef.XmlEscape.escapeString(this.defaultSchema) + "\" maintainedByPgm=\"" + MetaDataDef.XmlEscape.escapeString(this.maintainedByPgm) + "\" archivingFunction=\"" + MetaDataDef.XmlEscape.escapeString(this.archivingFunction) + "\" massDeleteFunction=\"" + MetaDataDef.XmlEscape.escapeString(this.massDeleteFunction) + "\" programHeading=\"" + MetaDataDef.XmlEscape.escapeString(this.programHeading) + "\" comment=\"" + MetaDataDef.XmlEscape.escapeString(this.comment) + "\">" + LineFeed + "";
        x = x + getTabs(tabs + 1) + "<table.columns>" + LineFeed + "";
        Enumeration e = this.fields.elements();
        Field f = null;
        while (e.hasMoreElements()) {
            f = (Field) e.nextElement();
            x = x + f.getXML(tabs + 2);
        }
        x = x + getTabs(tabs + 1) + "</table.columns>" + LineFeed;
        x = x + getTabs(tabs + 1) + "<table.indexes>" + LineFeed + "";
        Enumeration en = this.views.elements();
        View v = null;
        while (en.hasMoreElements()) {
            v = (View) en.nextElement();
            x = x + v.getXML(tabs + 2);
        }
        x = x + getTabs(tabs + 1) + "</table.indexes>" + LineFeed + getTabs(tabs) + "</table>";
        return XML_DECL + x;
    }

    public String getType(String fieldName) {
        Enumeration e = this.fields.elements();
        Field f = null;
        while (e.hasMoreElements()) {
            f = (Field) e.nextElement();
            if (f.fieldName.equals(fieldName)) {
                return "" + f.type.dataType.charAt(0);
            }
        }
        return " ";
    }

    public String[] getFunctionProgramsMetadata() {
        return new String[]{this.maintainedByPgm, this.archivingFunction, this.massDeleteFunction, this.programHeading};
    }

    public void setAttribute(Attributes attr) {
        this.tableName = attr.getValue("name");
        this.filetype = attr.getValue("filetype");
        this.filedescription = attr.getValue("filedescription");
        this.tableSpace = attr.getValue("tableSpace");
        this.tablePctFree = attr.getValue("tablePctFree");
        this.indexPctFree = attr.getValue("indexPctFree");
        this.defaultSchema = attr.getValue("defaultSchema");
        this.maintainedByPgm = attr.getValue("maintainedByPgm");
        this.archivingFunction = attr.getValue("archivingFunction");
        this.massDeleteFunction = attr.getValue("massDeleteFunction");
        this.programHeading = attr.getValue("programHeading");
        this.comment = attr.getValue("comment");
    }

    public static String getTabs(int nrOfTabs) {
        String str = "";
        for (int i = 0; i < nrOfTabs; i++) {
            str = str + Tab;
        }
        return str;
    }

    public Vector<Field> getFields() {
        return fields;
    }

    public void setFields(Vector<Field> fields) {
        this.fields = fields;
    }

    public int getFieldCounter() {
        return fieldCounter;
    }

    public void setFieldCounter(int fieldCounter) {
        this.fieldCounter = fieldCounter;
    }

    public Vector<View> getViews() {
        return views;
    }

    public void setViews(Vector<View> views) {
        this.views = views;
    }
}
