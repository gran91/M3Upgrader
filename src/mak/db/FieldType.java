package mak.db;

import org.xml.sax.Attributes;

public class FieldType {

    public String dataType;
    public String fieldLength = "0";
    public String nrOfDecimals = "0";
    public String totalDigits = "0";

    public FieldType(String dataType, String fieldLength, String nrOfDecimals, String totalDigits) {
        this.dataType = dataType;
        this.fieldLength = fieldLength;
        this.totalDigits = totalDigits;
        this.nrOfDecimals = nrOfDecimals;
        if (dataType.equals("decimal")) {
            this.fieldLength = totalDigits;
            this.totalDigits = totalDigits;
            this.nrOfDecimals = nrOfDecimals;
        } else {
            this.fieldLength = fieldLength;
            this.totalDigits = "0";
            this.nrOfDecimals = "0";
        }
    }

    public FieldType() {
    }

    public void setDataType(String type) {
        this.dataType = type;
    }

    public void setAttribute(Attributes attr) {
        if (this.dataType.equals("decimal")) {
            this.fieldLength = attr.getValue("totalDigits");
            this.totalDigits = this.fieldLength;
            this.nrOfDecimals = attr.getValue("fractionDigits");
        } else {
            this.fieldLength = attr.getValue("maxLength");
            this.totalDigits = "0";
            this.nrOfDecimals = "0";
        }
    }

    public String getXML(int tabs) {
        String str = Table.LineFeed + Table.getTabs(tabs);
        if (this.dataType.equals("string")) {
            str = str + "<type>" + Table.LineFeed + Table.getTabs(tabs + 1) + "<string maxLength=\"" + this.fieldLength + "\" />" + Table.LineFeed + Table.getTabs(tabs) + "</type>";
        } else if (this.dataType.equals("decimal")) {
            str = str + "<type>" + Table.LineFeed + Table.getTabs(tabs + 1) + "<decimal maxLength=\"" + this.fieldLength + "\" totalDigits=\"" + this.totalDigits + "\" fractionDigits=\"" + this.nrOfDecimals + "\"  />" + Table.LineFeed + Table.getTabs(tabs) + "</type>";
        } else if (this.dataType.equals("binary")) {
            str = str + "<type>" + Table.LineFeed + Table.getTabs(tabs + 1) + "<binary maxLength=\"" + this.fieldLength + "\" />" + Table.LineFeed + Table.getTabs(tabs) + "</type>";
        } else {
            str = str + "<type>missing</type>";
        }
        return str + Table.LineFeed;
    }
}