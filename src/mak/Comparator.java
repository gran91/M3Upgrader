/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author JCHAUT
 */
public class Comparator {

    public static ArrayList<String> compareHashMap(HashMap<String, String[]> spe, HashMap<String, String[]> std) {
        ArrayList<String> diff = new ArrayList<>();
        for (String key : spe.keySet()) {
            if (std.containsKey(key)) {
                if (!Arrays.asList(spe.get(key)).toString().equals(Arrays.asList(std.get(key)).toString())) {
                    diff.add(Arrays.asList(spe.get(key)).toString());
                }
            } else {
                diff.add(Arrays.asList(spe.get(key)).toString());
            }
        }
        return diff;
    }
}
