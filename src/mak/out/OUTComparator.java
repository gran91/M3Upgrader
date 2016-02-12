/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mak.out;

import com.intentia.mak.fom.xml.impl.RecordFormatTypeImpl;
import java.util.HashMap;
import java.util.List;
import mak.Comparator;

/**
 *
 * @author JCHAUT
 */
public class OUTComparator extends Comparator{
    
    public static HashMap<String, RecordFormatTypeImpl> convertToHashMap(List<RecordFormatTypeImpl> l) {
        HashMap<String, RecordFormatTypeImpl> map = new HashMap<>();
        for(RecordFormatTypeImpl l1:l){
            map.put(l1.getName(), l1);
        }
        return map;
    }
}
