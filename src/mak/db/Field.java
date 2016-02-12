package mak.db;

import org.xml.sax.Attributes;

public class Field {

    String tableName;
    FieldType type;
    String fieldName;
    String editCode = " ";
    String messageId;
    String sequence;
    String refFile;
    String arrayField;
    String arrayIndex;
    String filePrefix;

    public Field(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public Field(String viewName, String joinTableName) {
        this.tableName = viewName;
        this.refFile = joinTableName;
    }

    public Field(String tableName, String name, String dataType, String fieldLength, String nrOfDecimals, String editCode, String messageId, String sequence, String refFile, String arrayField, String arrayIndex, String filePrefix) {
        this.tableName = tableName;
        this.fieldName = name;
        this.type = new FieldType(dataType, fieldLength, nrOfDecimals, fieldLength);
        this.editCode = editCode;
        this.messageId = messageId;
        this.sequence = sequence;
        this.refFile = refFile;
        this.arrayField = arrayField;
        this.arrayIndex = arrayIndex;
        this.filePrefix = filePrefix;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getXML(int tabs) {
        String str = Table.getTabs(tabs);
        str = str + "<column name=\"" + this.fieldName + "\" fieldHeading=\"" + MetaDataDef.XmlEscape.escapeString(this.messageId) + "\" " + "editCode=\"" + MetaDataDef.XmlEscape.escapeString(this.editCode) + "\" ";
        if (this.arrayField != null) {
            str = str + " arrayField=\"" + MetaDataDef.XmlEscape.escapeString(this.arrayField) + "\"" + " arrayIndex=\"" + this.arrayIndex + "\"";
        }
        str = str + ">";
        str = str + this.type.getXML(tabs + 1);
        str = str + Table.getTabs(tabs) + "</column>" + Table.LineFeed + "";
        return str;
    }

    private String getDataType(String dataType) {
        if (dataType.equals("decimal")) {
            return "P";
        }
        if (dataType.equals("string")) {
            return "A";
        }
        if (dataType.equals("binary")) {
            return "B";
        }
        return "?";
    }

    private String getMaster(String arrayField, String arrayIndex) {
        if ((arrayField == null) || (arrayField.equals(""))) {
            return "";
        }
        return arrayField + "," + arrayIndex;
    }

    private String getFieldLength(String dataType, String fieldLength) {
        int len = Integer.parseInt(fieldLength);
        if (dataType.equals("decimal")) {
            return "" + (len / 2 + 1);
        }
        if (dataType.equals("string")) {
            return "" + len;
        }
        if (dataType.equals("binary")) {
            return "" + len;
        }
        return "-1";
    }

    private String getNrOfDigits(String dataType, String fieldLength) {
        return fieldLength;
    }

    public String[] getMetaData() {
        int size = 18;
        String[] str = new String[size];
        str[0] = this.tableName;
        str[1] = this.fieldName;
        str[2] = getDataType(this.type.dataType);
        str[3] = getFieldLength(this.type.dataType, this.type.fieldLength);
        str[4] = getNrOfDigits(this.type.dataType, this.type.fieldLength);
        str[5] = this.type.nrOfDecimals;
        str[6] = "";
        str[7] = "";
        str[8] = (this.sequence == null ? "0" : this.sequence);
        str[9] = this.refFile;
        str[10] = this.fieldName;
        str[11] = "";
        if (this.refFile.equals(this.tableName)) {
            if (!this.filePrefix.equals(this.fieldName.substring(0, 2))) {
                str[12] = this.fieldName;
            } else {
                str[12] = this.fieldName.substring(2);
            }
        } else {
            str[12] = this.fieldName;
        }
        str[13] = getMaster(this.arrayField, this.arrayIndex);
        str[14] = this.messageId;
        str[15] = "";
        str[16] = this.editCode;
        str[17] = "";
        return str;
    }

    public void setAttribute(Attributes attr) {
        this.fieldName = attr.getValue("name");
        this.editCode = attr.getValue("editCode");
        this.messageId = attr.getValue("fieldHeading");
        this.arrayField = attr.getValue("arrayField");
        this.arrayIndex = attr.getValue("arrayIndex");
    }
}