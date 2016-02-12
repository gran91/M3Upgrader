/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mak.db;

import java.util.HashMap;
import mak.Comparator;

/**
 *
 * @author Jeremy.CHAUT
 */
public class DBComparator extends Comparator{

    public static int dataType=0;
    
    public static HashMap<String, String[]> convertToHashMap(String[][] fields) {
        HashMap<String, String[]> map = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != null) {
                if (fields[i][1] != null && dataType==0) {
                    map.put(fields[i][1], fields[i]);
                }
                if (fields[i][1] != null && dataType==1) {
                    map.put(fields[i][1]+fields[i][0], fields[i]);
                }
            }
        }
        return map;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    
    
}
