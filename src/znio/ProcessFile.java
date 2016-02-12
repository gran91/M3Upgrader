package znio;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ProcessFile extends SimpleFileVisitor<Path> {

    private ArrayList<Path> list = new ArrayList<>();
    private String extFilter = "";

    @Override
    public FileVisitResult visitFile(
            Path aFile, BasicFileAttributes aAttrs
    ) throws IOException {
//System.out.println("Processing file:" + aFile);
        if (extFilter.isEmpty() || aFile.getFileName().toString().endsWith(extFilter)) {
            list.add(aFile);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(
            Path aDir, BasicFileAttributes aAttrs
    ) throws IOException {
//System.out.println("Processing directory:" + aDir);
        return FileVisitResult.CONTINUE;
    }

    public ArrayList<Path> getList() {
        return list;
    }

    public void clearList() {
        if (list != null) {
            list.clear();
        }
    }

    public void setList(ArrayList<Path> list) {
        this.list = list;
    }

    public String getExtFilter() {
        return extFilter;
    }

    public void setExtFilter(String extFilter) {
        this.extFilter = extFilter;
    }
}
