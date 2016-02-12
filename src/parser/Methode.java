package parser;

public class Methode implements java.lang.Comparable {

    String source = "";
    int beginLine = 0;
    int endLine = 0;
    String name = "";

    public Methode(String n, int beg,int end, String s) {
        name = n;
        source = s;
        beginLine = beg;
        endLine = end;
        
    }

    public int compareTo(Object other) {
        int nombre1 = ((Methode) other).getBeginLine();
        int nombre2 = this.getBeginLine();
        if (nombre1 > nombre2) {
            return -1;
        } else if (nombre1 == nombre2) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
