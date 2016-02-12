package mak.db;

import org.xml.sax.Attributes;

public class KeyField {

    String lfName;
    String tableName;
    String fieldName;
    String sorting;
    char unique;
    String sequence;
    boolean isJoin = false;

    public KeyField(String lfName) {
        this.lfName = lfName;
    }

    public void setAttribute(Attributes attr) {
        this.fieldName = attr.getValue("name");
        this.sorting = attr.getValue("order");
    }

    public String getXML(int tabs) {
        String str = Table.getTabs(tabs) + "<column table=\"" + MetaDataDef.XmlEscape.escapeString(this.tableName) + "\" name=\"" + MetaDataDef.XmlEscape.escapeString(this.fieldName) + "\" keyIndex=\"" + this.sequence + "\" order=\"" + this.sorting;
        str = str + "\" />" + Table.LineFeed + "";
        return str;
    }

    public String[] getMetaData() {
        int size = 7;
        String[] str = new String[size];
        str[0] = this.lfName.substring(this.lfName.length() - 2);
        str[1] = this.fieldName;
        str[2] = (this.sorting.equals("asc") ? "A" : "D");
        str[3] = ("" + this.unique);
        str[4] = this.sequence;
        str[5] = (this.isJoin ? "Y" : "N");
        str[6] = "1";
        return str;
    }
}