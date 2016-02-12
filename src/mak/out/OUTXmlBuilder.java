package mak.out;

import com.intentia.mak.fom.parser.SchemaEvaluator;
import com.intentia.mak.fom.parser.SrcLexer;
import com.intentia.mak.fom.parser.tokens.DataBaseToken;
import com.intentia.mak.fom.parser.tokens.DecToken;
import com.intentia.mak.fom.parser.tokens.Token;
import com.intentia.mak.fom.xml.Base;
import com.intentia.mak.fom.xml.FieldType;
import com.intentia.mak.fom.xml.FieldsType;
import com.intentia.mak.fom.xml.OutDataType;
import com.intentia.mak.fom.xml.PageRecordFormatType;
import com.intentia.mak.fom.xml.PageType;
import com.intentia.mak.fom.xml.PagesType;
import com.intentia.mak.fom.xml.RecordFormatType;
import com.intentia.mak.fom.xml.RecordFormatsType;
import com.intentia.mak.fom.xml.impl.BaseImpl;
import com.intentia.mak.fom.xml.impl.FieldHeadingsTypeImpl;
import com.intentia.mak.fom.xml.impl.FieldTypeImpl;
import com.intentia.mak.fom.xml.impl.FieldsTypeImpl;
import com.intentia.mak.fom.xml.impl.IndicatorsTypeImpl;
import com.intentia.mak.fom.xml.impl.OutDataTypeImpl;
import com.intentia.mak.fom.xml.impl.OutDatasTypeImpl;
import com.intentia.mak.fom.xml.impl.PageRecordFormatTypeImpl;
import com.intentia.mak.fom.xml.impl.PageTypeImpl;
import com.intentia.mak.fom.xml.impl.PagesTypeImpl;
import com.intentia.mak.fom.xml.impl.ProgramHeadingsTypeImpl;
import com.intentia.mak.fom.xml.impl.RecordFormatTypeImpl;
import com.intentia.mak.fom.xml.impl.RecordFormatsTypeImpl;
import com.intentia.mak.util.generators.TableDetails;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public final class OUTXmlBuilder {

    private static final Logger LOG = Logger.getLogger("OUTXmlBuilder.class");
    private List m_schemas;
    private SrcLexer m_lexer;
    private boolean m_verbouse;
    private String m_outDataName;

    public OUTXmlBuilder() {
    }

    public boolean parse(InputStream inputStream) {
        this.m_lexer = new SrcLexer(inputStream);
        this.m_lexer.setVerbouse(this.m_verbouse);
        return this.m_lexer.parse();
    }

    public Base buildXml() {
        if (this.m_lexer == null) {
            return null;
        }
        this.m_outDataName = this.m_lexer.getClassName().substring(0, 6);
        Base base = (Base) new BaseImpl();
        base.setOutDatas(new OutDatasTypeImpl());

        OutDataType outData = new OutDataTypeImpl();
        outData.setName(this.m_outDataName);
        base.getOutDatas().setOutData(outData);

        RecordFormatsType recordFormats = new RecordFormatsTypeImpl();
        outData.setRecordFormats(recordFormats);
        if (!buildRecordFormats(recordFormats)) {
            return null;
        }

        PagesType pages = new PagesTypeImpl();
        buildPages(pages);
        outData.setPages(pages);

        return base;
    }

    public void setVerbouse(boolean verbouse) {
        this.m_verbouse = verbouse;
    }

    public static boolean write(OutputStream os, Base base) {
        try {
            JAXBContext pContext = JAXBContext.newInstance("com.intentia.mak.fom.xml", OUTXmlBuilder.class.getClassLoader());
            Marshaller marshaller = pContext.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.marshal(base, os);
            os.close();

            return true;
        } catch (IOException ex) {
            Logger.getLogger(OUTXmlBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(OUTXmlBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean buildRecordFormats(RecordFormatsType recordFormats) {
        Collection values = this.m_lexer.getFormats().values();
        int scale = 1000;
//progress.beginTask("Building record formats", values.size() * 1000);
        try {
            Iterator i = values.iterator();
            List rFormats = recordFormats.getRecordFormat();

            while (i.hasNext()) {
                Token format = (Token) i.next();

                RecordFormatType recordFormat = new RecordFormatTypeImpl();
                recordFormat.setName(format.getValue());

                recordFormat.setStreamName(this.m_outDataName + format.getChildType(29).getValue());

                if (format.hasChildType(27)) {
                    recordFormat.setRecordFormattype("Header");
                } else {
                    recordFormat.setRecordFormattype("Line");
                }

                if (!buildFields(format, recordFormat)) {
                    return false;
                }

                recordFormat.setFieldHeadings(new FieldHeadingsTypeImpl());
                recordFormat.getFieldHeadings().setRecordFormat(format.getValue());
                recordFormat.setProgramHeadings(new ProgramHeadingsTypeImpl());
                recordFormat.getProgramHeadings().setRecordFormat(format.getValue());
                recordFormat.setIndicators(new IndicatorsTypeImpl());
                recordFormat.getIndicators().setRecordFormat(format.getValue());

                rFormats.add(recordFormat);
            }

            return true;
        } finally {
//progress.done();
        }
    }

    private static int sign(Token t) {
        if (t.hasChildType(20)) {
            return t.getChildren().size() - 1;
        }

        return t.getChildren().size();
    }

    private boolean buildFields(Token format, RecordFormatType recordFormat) {
        List children = format.getChildren();
        int scale = 1000;
//progress.beginTask("Building fields", children.size() * 1000);
        try {
            Iterator i = children.iterator();
            FieldsType fields = new FieldsTypeImpl();
            fields.setRecordFormat(format.getValue());
            recordFormat.setFields(fields);

            while (i.hasNext()) {
                Token c = (Token) i.next();
                if (c.getType() == 30) {
                    int sign = sign(c);
                    Token c2 = c.getChildAt(1);
                    Token dec = (Token) this.m_lexer.getDeclarations().get(c2.getValue());

                    FieldType ft = getFieldType(c, sign, dec);
                    if (ft != null) {
                        ft.setRecordFormat(format.getValue());
                        String indStr = "";
                        if (c.hasChildType(20)) {
                            indStr = c.getChildType(20).getValue();
                            indStr = indStr.replaceAll("PB.", "");

                            indStr = indStr.trim();
                            ft.setVisibilityIndicators(indStr);
                        }
                        ft.setVisibilityIndicators(indStr);
                        ft.setInvisibilityIndicators("");
                        fields.getField().add(ft);
                    } else {
                        return false;
                    }
                }

            }

            return true;
        } finally {
//progress.done();
        }
    }

    private FieldType getFieldType(Token c, int sign, Token dec) {
        switch (sign) {
            case 2:
                return doString(c, dec);
            case 5:
            case 7:
                return doDouble(c, dec);
            case 3:
            case 4:
                return doInteger(c, dec);
            case 6:
        }
        StringWriter sw = new StringWriter();
        c.trace(new PrintWriter(sw));
//LOG.warn("Unknown signature: " + sign + "\n" + sw.toString());
//        progress.done();

        return null;
    }

    private FieldType doInteger(Token c, Token dec) {
        if (!isInteger(dec)) {
//LOG.debug("Token might not be a long/int: " + dec);
        }

        return buildFieldInt(c);
    }

    private FieldType doDouble(Token c, Token dec) {
        if (!isDouble(dec)) {
//LOG.debug("Token might not be a double: " + dec);
        }

        return buildFieldDouble(c);
    }

    private FieldType doString(Token c, Token dec) {
        if (!isString(dec)) {
//LOG.debug("Token might not be a MvxString/char: " + dec + " " + c + " (probably a reference field)");
        }

        return buildFieldSrting(c);
    }

    private boolean isInteger(Token dec) {
        if (dec == null) {
            return false;
        }

        int type = dec.getType();
        return (type == 8) || (type == 10);
    }

    private boolean isDouble(Token dec) {
        return (dec != null) && (dec.getType() == 9);
    }

    private boolean isString(Token dec) {
        if (dec == null) {
            return false;
        }

        int type = dec.getType();
        return (type == 7) || (type == 11);
    }

    private int lookForLength(String name) {
        if (this.m_schemas != null) {
            Iterator sch = this.m_schemas.iterator();
            while (sch.hasNext()) {
                SchemaEvaluator se = (SchemaEvaluator) sch.next();
                int l = se.getLength(name);
                if (l > -1) {
                    return l;
                }
            }
        }
        return -1;
    }

    private FieldType buildFieldInt(Token s) {
        FieldType field = new FieldTypeImpl();
        Token n = s.getChildAt(0);
        field.setName(n.getValue());
        field.setOverriddenName(n.getValue());

        field.setDatatype("Decimal");

        Token r = s.getChildAt(1);
        if (!createRefs(field, r)) {
            StringWriter sw = new StringWriter();
            r.trace(new PrintWriter(sw));

//LOG.error("Could not create int/long reference: " + s + "\n" + sw.toString());
            return null;
        }

        Token ec = s.getChildType(19);
        if (ec != null) {
            field.setEditCode(ec.getValue());
        } else {
            field.setEditCode("");
        }

        field.setLength(new BigInteger(s.getChildAt(2).getValue()));
        field.setDecimals(new BigInteger("0"));

        return field;
    }

    private FieldType buildFieldDouble(Token s) {
        FieldType field = new FieldTypeImpl();
        Token n = s.getChildAt(0);
        field.setName(n.getValue());
        field.setOverriddenName(n.getValue());

        field.setDatatype("Decimal");
        Token r = s.getChildAt(1);
        if (!createRefs(field, r)) {
            StringWriter sw = new StringWriter();
            r.trace(new PrintWriter(sw));

//      LOG.error("Could not create double reference: " + s + "\n" + sw.toString());
            return null;
        }

        Token ec = s.getChildType(19);
        if (ec != null) {
            field.setEditCode(ec.getValue());
        } else {
            field.setEditCode("");
        }

        field.setLength(new BigInteger(s.getChildAt(3).getValue()));
        field.setDecimals(new BigInteger(s.getChildAt(2).getValue()));

        return field;
    }

    private FieldType buildFieldSrting(Token s) {
        FieldType field = new FieldTypeImpl();
        Token n = s.getChildAt(0);
        String name = n.getValue();
        field.setName(name);
        field.setOverriddenName(name);

        field.setDatatype("Char");
        Token r = s.getChildAt(1);
        if (!createRefs(field, r)) {
            StringWriter sw = new StringWriter();
            r.trace(new PrintWriter(sw));

//      LOG.error("Could not create char reference: " + s + "\n" + sw.toString());
            return null;
        }

        field.setDecimals(new BigInteger("0"));
        field.setEditCode("");
        int length = field.getLength().intValue();
        if (length <= 0) {
            int sLength = lookForLength(name);
            if (sLength > 0) {
                field.setLength(new BigInteger(""+sLength));
            } else {
//LOG.debug("Warning no proper length for " + name + " (probably a reference field)");
            }
        }

        return field;
    }

    private boolean createRefs(FieldType field, Token r) {
        try {
            if (r.getType() == 3) {
                Token dec = (Token) this.m_lexer.getDeclarations().get(r.getValue());
                field.setLength(new BigInteger(""+((DecToken) dec).getLength()));
            } else if (r.getType() == 15) {
                DataBaseToken table =
                        (DataBaseToken) this.m_lexer.getDatabases().get(r.getValue().substring(0, r.getValue().length() - 1));
                field.setReferenceFile(table.getDbName());

                setReferenceField(field, r, table);
                field.setLength(new BigInteger("0"));
            } else if ((r.getType() == 6) && (r.getValue().equals("PB."))) {
                Token child = r.getChildAt(0);
                if (child.getType() == 6) {
                    field.setReferenceField(child.getChildAt(0).getValue());
                } else {
                    field.setReferenceField(field.getName().substring(2));
                }

                field.setLength(new BigInteger("0"));
            } else {
                field.setLength(new BigInteger("-1"));
                return false;
            }
            return true;
        } finally {
//            progress.done();
        }
    }

    private void setReferenceField(FieldType field, Token r, DataBaseToken table) {
        try {
            String prefix = "";
            String refFieldName = r.getChildAt(0).getValue().substring(3);
            if ((refFieldName != null) && (refFieldName.length() <= 4)) {
                TableDetails.Column[] columns = getColumns(table.getDbName(), field.getName());
                if ((columns != null) && (columns.length > 0)) {
                    prefix = columns[0].getName().substring(0, 2);

                    for (int i = 0; i < columns.length; i++) {
                        if (columns[i].getName().equals(field.getName())) {
                            field.setGeneralWord(columns[i].getFieldHeading());
                        }
                    }
                }
            }
            field.setReferenceField(prefix + refFieldName);
        } finally {
//            progress.done();
        }
    }

    private TableDetails.Column[] getColumns(String refTable, String fieldName) {
/*M3ClassPathEntry[] classpath = this.m_movexSystem.getClasspath();
        File[] files = new File[classpath.length + 1];
        files[0] = MAKCorePlugin.getPath("lib").append("generatorV12.jar").toFile();
        for (int i = 0; i < classpath.length; i++) {
            files[(i + 1)] = classpath[i].getOriginalPath().toFile();
        }
        DataDictionary dictionary = new DataDictionary(this.m_movexSystem.getDataDictionaryPath().toFile(),
                files, this.m_movexSystem.getSourcePath().toFileArray());
        try {
/progress.subTask("Retrieving data dictionary for " + refTable);
            TableDetails details = dictionary.getTableDetails(refTable);
            if (details == null) {
/progress.subTask("Generating data dictionary for " + refTable);
                details = dictionary.generateTableDetails(refTable);
            }
            if (details != null) {
                TableDetails.Column[] columns = details.getColumns();
                if ((columns != null) && (columns.length > 0)) {
                    return columns;
                }
            }
        } catch (MAKUtilException e) {
/LOG.error("Could not open Data Dictionary. Reference field of " + fieldName + " may be wrong", e);
        } finally {
//            progress.done();
        }
  //      progress.done();
*/
        return null;
    }

    private boolean buildPages(PagesType pages) {
        Iterator events = this.m_lexer.getFormats().values().iterator();
        Map ePages = new HashMap();
        while (events.hasNext()) {
            Token event = (Token) events.next();
            if (event.hasChildType(27)) {
                String eventName = event.getValue();
                PageType page = new PageTypeImpl();
                page.setName(eventName);
                pages.getPage().add(page);
                ePages.put(eventName, page);
            }
        }

        Iterator formats = this.m_lexer.getFormats().values().iterator();
        while (formats.hasNext()) {
            Token format = (Token) formats.next();
            List sInfo = format.getChildTypes(21);
            if (sInfo.size() > 0) {
                buildInfo(ePages, format, sInfo);
            }

        }

        return true;
    }

    private void buildInfo(Map ePages, Token format, List sInfo) {
        for (int i = 0; i < sInfo.size(); i++) {
            Token si = (Token) sInfo.get(i);
            String pgfName = si.getValue();
            if (pgfName == null) {
                pgfName = format.getValue();
            }

            if (ePages.containsKey(pgfName)) {
                PageRecordFormatType prgt = createPageRecordFormat(format, si, pgfName);
                ((PageType) ePages.get(pgfName)).getPageRecordFormat().add(prgt);
            } else {
                buildPageInfo(ePages, format, si, pgfName);
                break;
            }
        }
    }

    private void buildPageInfo(Map ePages, Token format, Token si, String pgfName) {
        if (this.m_schemas != null) {
            Iterator sch = this.m_schemas.iterator();
            while (sch.hasNext()) {
                SchemaEvaluator se = (SchemaEvaluator) sch.next();
                if (se.hasListLine(si.getChildType(22).getValue(), si.getChildType(23).getValue())) {
                    PageRecordFormatType prgt = createPageRecordFormat(format, si, pgfName);
                    ((PageType) ePages.get(se.getName())).getPageRecordFormat().add(prgt);
                }
            }
        }
    }

    private PageRecordFormatType createPageRecordFormat(Token format, Token si, String pgfName) {
        PageRecordFormatType prgt = new PageRecordFormatTypeImpl();
        prgt.setPage(pgfName);
        prgt.setName(format.getValue());
        prgt.setStreamName(format.getValue().substring(0, 6) + format.getChildType(29).getValue());
        prgt.setLineName(si.getChildType(23).getValue());
        prgt.setLineType(si.getChildType(24).getValue());
        prgt.setListName(si.getChildType(22).getValue());

        return prgt;
    }
}