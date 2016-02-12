package mak.db;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class MDBParser {

    public void parse(String fileName, String[] logicalFiles) {
        Table t = new Table();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.crimson.parser.XMLReaderImpl");
            DBContentHandler ch = new DBContentHandler(t);
            parser.setContentHandler(ch);
            parser.parse(fileName);
            for (int i = 0; i < logicalFiles.length; i++) {
                parser.parse(logicalFiles[i]);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[][] a = t.getViewMetaData();
        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                for (int j = 0; j < a[i].length; j++);
            }
        }
    }
}