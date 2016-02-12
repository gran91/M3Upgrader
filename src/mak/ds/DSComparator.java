/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mak.ds;

import java.util.HashMap;
import mak.Comparator;

/**
 *
 * @author JCHAUT
 */
public class DSComparator extends Comparator{
    
    public static HashMap<String, String[]> convertToHashMap(String[][] fields) {
        HashMap<String, String[]> map = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != null) {
                if (fields[i][1] != null) {
                    map.put(fields[i][1], fields[i]);
                }
            }
        }
        return map;
    }
}
