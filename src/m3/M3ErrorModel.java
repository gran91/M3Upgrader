/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import java.nio.file.Path;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3ErrorModel {

    public static int WARNING = 0;
    public static int ERROR = 1;
    private int errorType = -1;
    private Path file;
    private String description;

    public M3ErrorModel(int type, Path f, String desc) {
        errorType = type;
        file = f;
        description = desc;
    }

    public M3ErrorModel(int type, String desc) {
        errorType = type;
        file = null;
        description = desc;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
