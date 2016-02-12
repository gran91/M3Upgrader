package mak.db;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import mvx.db.common.GenericDef;
import mvx.db.common.GenericMDB;
import tools.generator.xml.ParamsByName;

public class MetaDataDef {

    private Class classDB;
    private GenericDef def;
    public static final String version = "$Revision: /main/DEV_12/SP_12.4.6/10 $";
    private ClassLoader loader;
    private Table t;

    public MetaDataDef(ClassLoader load, String dbName, String sourceName) {
        try {
            loader = load;
            BufferedWriter bw = new BufferedWriter(new FileWriter(sourceName));
            generate(dbName, bw);
        } catch (InstantiationException e) {
            System.err.println("Can not instantiate class file for " + dbName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Can not find class file for " + dbName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can not create file " + sourceName);
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Can not access file " + dbName);
            System.exit(1);
        }
    }

    public MetaDataDef(ClassLoader load, String dbName, BufferedWriter bw) {
        try {
            loader = load;
            generate(dbName, bw);
        } catch (InstantiationException e) {
            System.err.println("Can not instantiate class file for " + dbName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Can not find class file for " + dbName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can not create source file");
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Can not access file " + dbName);
            System.exit(1);
        }
    }
    
    
    public MetaDataDef(ClassLoader load, String dbName) {
        try {
            loader = load;
            generate(dbName, null);
        } catch (InstantiationException e) {
            System.err.println("Can not instantiate class file for " + dbName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Can not find class file for " + dbName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Can not create source file");
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Can not access file " + dbName);
            System.exit(1);
        }
    }

    private void generate(String fileName, BufferedWriter bw) throws InstantiationException, IOException, IllegalAccessException, ClassNotFoundException {
        String com = null;
        String tableName = fileName;
        this.def = null;
        classDB = loader.loadClass("mvx.db.dta." + fileName);
        Class c = loader.loadClass("mvx.db.def.d" + fileName);
        this.def = ((GenericDef) c.newInstance());
        try {
            java.lang.reflect.Field comment = c.getDeclaredField("comment");
            com = (String) comment.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        t = buildTable(tableName, com);
        buildFields(t);
        buildViews(t);
        if (bw != null) {
            bw.write(t.getXML(0));
            bw.flush();
        }
    }

    public void print(GenericDef def) {
        int nrOfJoins = def.names.length - 1;
        for (int j = 0; j < def.names.length; j++) {
            for (int i = 0; i < def.names[j].length; i++) {
                System.out.println(def.names[j][i]);
            }
        }
    }

    public Table buildTable(String tableName, String com) {
        Table t = new Table();
        t.tableName = tableName;
        t.filetype = this.def.getFileType();
        t.filedescription = this.def.getFileDescription();
        t.tableSpace = this.def.getTableSpace();
        t.tablePctFree = ("" + this.def.getTablePctFree());
        t.indexPctFree = ("" + this.def.getIndexPctFree());
        t.defaultSchema = this.def.getDefaultSchema();
        t.maintainedByPgm = this.def.getMaintainedByPgm();
        t.archivingFunction = this.def.getArchivingFunction();
        t.massDeleteFunction = this.def.getMassDeleteFunction();
        t.programHeading = this.def.getProgramHeading();
        t.comment = com;
        return t;
    }

    void printMatrix(String[][] matrix) {
        if (matrix != null) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i] != null) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        System.out.print(matrix[i][j] + ", ");
                    }
                    System.out.println();
                }
            }
        }
    }

    public void buildFields(Table t) {
        boolean checkForArray = false;
        DBParams p = new DBParams();
        p.newParam(classDB);
        Vector v = p.getAllArrayFields();
        if (!v.isEmpty()) {
            checkForArray = true;
            Enumeration e = v.elements();
            int counter = 1;
            while (e.hasMoreElements()) {
                String arrayName = (String) e.nextElement();
//                p.fill(arrayName, counter++);
            }
        }
        int nrOfFields = this.def.getNrOfFields(0);
        int counter = 0;
        int index = 0;
        int oldIndex = 0;
        String prefix = null;
        for (int i = 0; i < nrOfFields; i++) {
            String fieldName = this.def.getFieldName(i, 0);
            if (i == 0) {
                prefix = fieldName.substring(0, 2);
            }
            String master = null;
            if (checkForArray) {
//                if ((index = p.check(fieldName.substring(2))) > 0) {
//                    if (oldIndex != index) {
//                        counter = 0;
//                    }
//                    oldIndex = index;
//                    counter++;
//                    master = (String) v.elementAt(index - 1);
//                } else {
//                    counter = 0;
//                }
            }
            String fieldType = "?";
            switch (this.def.getFieldType(i, 0)) {
                case 0:
                    fieldType = "string";
                    break;
                case 1:
                    fieldType = "decimal";
                    break;
                case 3:
                    fieldType = "binary";
                case 2:
            }
            String noll = "000";
            String arrayIndex = null;
            try {
                arrayIndex = "" + (counter - 1);
                arrayIndex = noll.substring(0, 3 - arrayIndex.length()) + arrayIndex;
            } catch (Exception e) {
                arrayIndex = "0";
            }
            Field f = new Field(t.tableName, fieldName, "" + fieldType, "" + this.def.getFieldLength(i, 0), "" + this.def.getFieldNrOfDecimals(i, 0), "" + this.def.getEditCode(0, i), this.def.getFieldHeading(i, 0), "" + i, null, master, arrayIndex, prefix);
            t.addField(f);
        }
    }

    public void buildViews(Table t) {
        View v = null;
        int nrOfLFs = this.def.getNrOfLFs();
        for (int i = 0; i < nrOfLFs; i++) {
            if ((i != 0) || (this.def.isRR())) {
                v = new View();
                v.viewName = (t.tableName + this.def.getLFName(i));
                if (t.addView(v)) {
                    v.join = (this.def.isJoinFile(i) ? "true" : "false");
                    v.unique = (this.def.isLFUnique(i) ? "true" : "false");
                    if (this.def.isJoinFile(i)) {
                        addJoin(t, v, i);
                    }
                    addKeyFields(t, v, i);
                    if (this.def.isSelectOmit(i)) {
                        SelectOmit so = new SelectOmit(t, v.viewName);
                        for (int j = 0; j < this.def.getNrOfSelectOmits(i); j++) {
                            int fieldIndex = this.def.getSelectOmitFieldIndex(i, j);
                            boolean neg = false;
                            String soField = this.def.getFieldName(fieldIndex, i);
                            String value = this.def.getSelectOmitValue(i, j, fieldIndex);
                            String property = "";
                            if (this.def.getSelectOmitAndOrOr(i, j) == 1) {
                                neg = true;
                            } else if (this.def.getSelectOmitAndOrOr(i, j) == 2) {
                                v.addSelectOmit(so);
                                so = new SelectOmit(t, v.viewName);
                                property = "S";
                            } else {
                                property = "A";
                            }
                            int op = this.def.getSelectOmitOperator(i, j);
                            String operator;
                            switch (op) {
                                case 0:
                                    operator = neg ? "<>" : "=";
                                    break;
                                case 1:
                                    operator = neg ? "=" : "<>";
                                    break;
                                case 2:
                                    operator = neg ? ">=" : "<";
                                    break;
                                case 4:
                                    operator = neg ? ">" : "<=";
                                    break;
                                case 8:
                                    operator = neg ? "<=" : ">";
                                    break;
                                case 16:
                                    operator = neg ? "<" : ">=";
                                    break;
                                case 32:
                                    operator = neg ? "NOT LIKE" : "LIKE";
                                    break;
                                default:
                                    operator = "?";
                            }
                            so.addSelection(soField, operator, value, property);
                        }
                        v.addSelectOmit(so);
                    }
                }
            }
        }
    }

    public void addJoin(Table t, View v, int lfIndex) {
        Join j = null;
        String[] joins = this.def.getJoinTables(lfIndex);
        String joinCond = this.def.getJoinConditions(lfIndex);
        for (int i = 0; i < joins.length; i++) {
            j = new Join(t, t.tableName, v.getViewName());
            String fromField = null;
            String toTableName = null;
            String toField = null;
            StringTokenizer st = new StringTokenizer(joinCond, ")(and");
            while (st.hasMoreElements()) {
                String elem = (String) st.nextElement();
                if (elem.indexOf(joins[i]) >= 0) {
                    StringTokenizer st2 = new StringTokenizer(elem, "=");
                    String delElem = (String) st2.nextElement();
                    fromField = delElem.substring(delElem.indexOf(".") + 1).trim();
                    String delElem2 = (String) st2.nextElement();
                    toTableName = delElem2.substring(0, delElem2.indexOf(".")).trim();
                    toField = delElem2.substring(delElem2.indexOf(".") + 1).trim();
                    j.addJoinCondition(fromField, toTableName, toField);
                }
            }
            j.toTableName = joins[i];
            addJoinFields(t, lfIndex, joins[i], j);
            v.addJoin(j);
        }
    }

    private void addJoinFields(Table t, int lfIndex, String joinTable, Join j) {
        int nrOfFields = this.def.getNrOfFields(lfIndex);
        Field f = null;
        for (int i = 0; i < nrOfFields; i++) {
            if (this.def.getTableNameForField(i, lfIndex).equals(joinTable)) {
                String fieldName = this.def.getFieldName(i, lfIndex);
                String master = null;
                String fieldType = "?";
                switch (this.def.getFieldType(i, lfIndex)) {
                    case 0:
                        fieldType = "string";
                        break;
                    case 1:
                        fieldType = "decimal";
                        break;
                    case 3:
                        fieldType = "binary";
                    case 2:
                }
                f = new Field(t.tableName + this.def.getLFName(lfIndex), fieldName, "" + fieldType, "" + this.def.getFieldLength(i, lfIndex), "" + this.def.getFieldNrOfDecimals(i, lfIndex), "" + this.def.getEditCode(lfIndex, i), this.def.getFieldHeading(i, lfIndex), "" + i, joinTable, "", "", "");
                j.addJoinField(f);
            }
        }
    }

    public void addKeyFields(Table t, View v, int lfIndex) {
        KeyField k = null;
        int nrOfKeys = this.def.getNrOfKeysInLF(lfIndex);
        for (int i = 0; i < nrOfKeys; i++) {
            k = new KeyField(this.def.getLFName(lfIndex));
            v.addKeyField(k);
            k.fieldName = this.def.getKeyName(lfIndex, i);
            k.lfName = this.def.getLFName(lfIndex);
            k.sorting = (this.def.getKeySortorder(lfIndex, i) == 1 ? "desc" : "asc");
            k.tableName = t.tableName;
        }
    }

    public void addSelectOmit() {
    }

    static final class XmlEscape {

        private static final char[] AMP = "&amp;".toCharArray();
        private static final char[] LT = "&lt;".toCharArray();
        private static final char[] GT = "&gt;".toCharArray();
        private static final char[] NBSP = "&nbsp;".toCharArray();
        private static final int AMP_length = AMP.length;
        private static final int LT_length = LT.length;
        private static final int GT_length = GT.length;
        private static final int NBSP_length = NBSP.length;
        private static final String CHAR_ESC_START = "&#";

        public static String escapeString(String inStr) {
            if (inStr == null) {
                return "";
            }
            int off = 0;
            int len = inStr.length();
            char[] ch = inStr.toCharArray();
            StringBuffer bufOut = new StringBuffer(len + len / 4);
            int limit = off + len;
            int offset = off;
            if (limit > ch.length) {
                limit = ch.length;
            }
            for (int i = off; i < limit; i++) {
                char current = ch[i];
                switch (current) {
                    case '&':
                        bufOut.append(ch, offset, i - offset);
                        bufOut.append(AMP, 0, AMP_length);
                        offset = i + 1;
                        break;
                    case '<':
                        bufOut.append(ch, offset, i - offset);
                        bufOut.append(LT, 0, LT_length);
                        offset = i + 1;
                        break;
                    case '>':
                        bufOut.append(ch, offset, i - offset);
                        bufOut.append(GT, 0, GT_length);
                        offset = i + 1;
                        break;
                    case ' ':
                        bufOut.append(ch, offset, i - offset);
                        bufOut.append(NBSP, 0, NBSP_length);
                        offset = i + 1;
                        break;
                    default:
                        if (current >= '') {
                            bufOut.append(ch, offset, i - offset);
                            bufOut.append("&#");
                            bufOut.append(Integer.toString(ch[i]));
                            bufOut.append(';');
                            offset = i + 1;
                        }
                        break;
                }
            }
            if (offset < limit) {
                bufOut.append(ch, offset, limit - offset);
            }
            return bufOut.toString();
        }
    }

    public Table getTable() {
        return t;
    }

    public void setTable(Table t) {
        this.t = t;
    }
    
}