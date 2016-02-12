// Decompiled by Decompilator v0.9.2 Alpha Copyright 2012 Jérémy Chaut.
// Contact:jeremy.chaut@hotmail.fr 
// Source File Name:DSFieldData.java
package mak.ds;

final class DSFieldData {

    private String m_fieldName;
    private String m_fieldType;
    private String m_fieldLength;
    private String m_fieldDec;

    DSFieldData(String fieldName, String fieldType, String fieldLength, String fieldDec) {
        m_fieldName = fieldName;
        m_fieldType = fieldType;
        m_fieldLength = fieldLength;
        m_fieldDec = fieldDec;
    }

    public String getFieldName() {
        return m_fieldName;
    }

    public String getFieldType() {
        return m_fieldType;
    }

    public String getFieldLength() {
        return m_fieldLength;
    }

    public String getFieldDec() {
        return m_fieldDec;
    }

    public String[] getData() {
        String[] s = new String[4];
        s[0] = m_fieldName;
        s[1] = m_fieldType;
        s[2] = m_fieldLength;
        s[3] = m_fieldDec;
        return s;
    }
}
