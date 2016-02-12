/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.m3.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mak.db.MetaDataDef;
import mak.db.Table;

/**
 *
 * @author Jeremy.CHAUT
 */
public class DBComparator {

    public DBComparator(MetaDataDef src, MetaDataDef targ) {

    }

    /**
     *
     * @param src
     * @param targ
     * @return
     */
    public HashMap<String, List<Diff>> compare(MetaDataDef src, MetaDataDef targ) {
        HashMap<String, List<Diff>> mapDiff = new HashMap<>();
        Table srcTable = src.getTable();
        Table targTable = targ.getTable();
        List<Diff> listDiffHeader = new ArrayList<>();
        if (!srcTable.getComment().equals(targTable.getComment())) {
            listDiffHeader.add(new Diff(Diff.CHANGE, srcTable.getComment(), targTable.getComment(), null, null));
        }

        List<Diff> listDiffField = new ArrayList<>();
        for (int i = 0; i < srcTable.getFields().size(); i++) {
            String[] metaDataSrc = srcTable.getFields().get(i).getMetaData();
            boolean test = false;
            for (int j = 0; j < targTable.getFields().size(); j++) {
                String[] metaDataTarg = targTable.getFields().get(j).getMetaData();
                System.out.println("Source=" + metaDataSrc[12] + " Current Target=" + metaDataTarg[12]);
                if (metaDataSrc[12].equals(metaDataTarg[12])) {
                    test = true;
                    break;
                }
            }
            if (!test) {
                listDiffField.add(new Diff(Diff.ADD, srcTable.getFields().get(i), null, (i == 0) ? null : srcTable.getFields().get(i - 1), (i + 1 >= srcTable.getFields().size()) ? null : srcTable.getFields().get(i + 1)));
            }
        }

        List<Diff> listDiffView = new ArrayList<>();
        for (int i = 0; i < srcTable.getViews().size(); i++) {
            boolean test = false;
            for (int j = 0; j < targTable.getViews().size(); j++) {
                if (srcTable.getViews().get(i).getViewName().equals(targTable.getViews().get(j).getViewName())) {
                    test = true;
                    break;
                }
            }
            if (!test) {
                listDiffView.add(new Diff(Diff.ADD, srcTable.getViews().get(i), null, (i == 0) ? null : srcTable.getViews().get(i - 1), (i + 1 >= srcTable.getViews().size()) ? null : srcTable.getViews().get(i + 1)));
            }
        }
        mapDiff.put("header", listDiffHeader);
        mapDiff.put("field", listDiffField);
        mapDiff.put("view", listDiffView);
        return mapDiff;
    }

    class Diff {

        public static final int CHANGE = 0;
        public static final int ADD = 1;
        public static final int DELETE = 2;

        private final int type;
        private Object oldObj, newObj;
        private Object beforeObj, afterObj;

        public Diff(int type, Object oldObject, Object newObject, Object before, Object after) {
            this.type = type;
            this.oldObj = oldObject;
            this.newObj = newObject;
            this.beforeObj = before;
            this.afterObj = after;
        }
    }

}
