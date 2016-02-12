package parser;

public class Variable implements java.lang.Comparable {

    String name = "";
    String source = "";
    int beginLine = 0;
    int endLine = 0;

    public Variable(String n, int beg,int end,String s) {
        name=n;
        beginLine=beg;
        endLine=end;
        source = s;
    }

    public int compareTo(Object other) {
        int nombre1 = ((Variable) other).getBeginLine();
        int nombre2 = this.getBeginLine();
        if (nombre1 > nombre2) {
            return -1;
        } else if (nombre1 == nombre2) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    

}
