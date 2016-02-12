package m3;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class M3UpdObjModel {

    private ArrayList<Object> data;
    private String typeObj = "";
    private String typeM3Obj = "";
    private String m3ClassLevel = "";
    private String superclass = "";
    private String stdClassLevel = "";
    private String fileName = "";
    private String filePath = "";
    private String existingFields = "";
    private int updgradeLevel = -1;
    private String workspace = "";
    private Path speSrcPath;
    private Path stdSrcPath,stdTargPath;
    private Path stdClassPath,stdTargClassPath;
    private boolean srcExist = true;
    private boolean migrate = true;
    private List<M3ErrorModel> listError=new ArrayList<>();
    public static int[] header = {192, 193, 194, 217, 195, 196, 197, 198, 199, 200, 218, 201, 202};
    
    public M3UpdObjModel() {
        data = new ArrayList<Object>();
        data.add(typeObj);  
        data.add(fileName);
        data.add(filePath);
        data.add(listError);
        data.add(typeM3Obj);
        data.add(m3ClassLevel);
        data.add(superclass);
        data.add(stdClassLevel);
        data.add(srcExist);
        data.add(existingFields);
        data.add(updgradeLevel);
        data.add(migrate);
        data.add(workspace);
    }

    private void parseData() {
        data = new ArrayList<Object>();
        data.add(typeObj);
        data.add(fileName);
        data.add(filePath);
        data.add(listError);
        data.add(typeM3Obj);
        data.add(m3ClassLevel);
        data.add(superclass);
        data.add(stdClassLevel);
        data.add(srcExist);
        data.add(existingFields);
        data.add(updgradeLevel);
        data.add(migrate);
        data.add(workspace);
    }

    public String toString() {
        parseData();
        return data.toString();
    }

    public ArrayList<Object> getData() {
        parseData();
        return data;
    }

    public Object[] getTableData() {
        return getData().toArray();
    }

    public String getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(String typeObj) {
        this.typeObj = typeObj;
    }

    public String getTypeM3Obj() {
        return typeM3Obj;
    }

    public void setTypeM3Obj(String typeM3Obj) {
        this.typeM3Obj = typeM3Obj;
    }

    public String getM3ClassLevel() {
        return m3ClassLevel;
    }

    public void setM3ClassLevel(String m3ClassLevel) {
        this.m3ClassLevel = m3ClassLevel;
    }

    public List<M3ErrorModel> getListError() {
        return listError;
    }

    public void setListError(List<M3ErrorModel> listError) {
        this.listError = listError;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public String getStdClassLevel() {
        return stdClassLevel;
    }

    public void setStdClassLevel(String stdClassLevel) {
        this.stdClassLevel = stdClassLevel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExistingFields() {
        return existingFields;
    }

    public void setExistingFields(String existingFields) {
        this.existingFields = existingFields;
    }

    public int getUpdgradeLevel() {
        return updgradeLevel;
    }

    public void setUpdgradeLevel(int updgradeLevel) {
        this.updgradeLevel = updgradeLevel;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public boolean isSrcExist() {
        return srcExist;
    }

    public void setSrcExist(boolean srcExist) {
        this.srcExist = srcExist;
    }

    public boolean isMigrate() {
        return migrate;
    }

    public void setMigrate(boolean migrate) {
        this.migrate = migrate;
    }

    public Path getSpeSrcPath() {
        return speSrcPath;
    }

    public void setSpeSrcPath(Path speSrcPath) {
        this.speSrcPath = speSrcPath;
    }

    public Path getStdSrcPath() {
        return stdSrcPath;
    }

    public void setStdSrcPath(Path stdSrcPath) {
        this.stdSrcPath = stdSrcPath;
    }

    public Path getStdClassPath() {
        return stdClassPath;
    }

    public void setStdClassPath(Path stdClassPath) {
        this.stdClassPath = stdClassPath;
    }   

    public Path getStdTargPath() {
        return stdTargPath;
    }

    public void setStdTargPath(Path stdTargPath) {
        this.stdTargPath = stdTargPath;
    }

    public Path getStdTargClassPath() {
        return stdTargClassPath;
    }

    public void setStdTargClassPath(Path stdTargClassPath) {
        this.stdTargClassPath = stdTargClassPath;
    }
}
