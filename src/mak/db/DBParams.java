/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mak.db;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import mvx.db.common.GenericMDB;
import mvx.util.MvxIndexHashtable;
import mvx.util.MvxString;

public class DBParams {

    Class c;
    public static final int LONG = 1;
    public static final int LONG_A = 2;
    public static final int INTEGER = 3;
    public static final int INTEGER_A = 4;
    public static final int DOUBLE = 5;
    public static final int DOUBLE_A = 6;
    public static final int STRING = 7;
    public static final int STRING_A = 8;
    public static final int CHAR = 9;
    public static final int CHAR_A = 10;
    public static final int NO_TYPE = 0;
    MvxIndexHashtable typs = new MvxIndexHashtable();
    Vector arrays = new Vector();
    public static final String version = "$Revision: /main/DEV_12/SP_12.4.6/3 $";

    public void newParam(Class p) {
        this.c = p;
        ensureMeta();
    }

    private void ensureMeta() {
        Field[] fields = c.getFields();
        Method[] ms = c.getDeclaredMethods();
        for (int i = 0; i < ms.length; i++) {
            String methodName = ms[i].getName();
            if (methodName.startsWith("get")) {
                String var = methodName.substring(3);
                Class rs = ms[i].getReturnType();
                if (rs == Integer.TYPE) {
                    this.typs.put(var, 3);
                } else if (rs == Integer[].class) {
                    this.typs.put(var, 4);
                    this.arrays.addElement(var);
                } else if (rs == Long.TYPE) {
                    this.typs.put(var, 1);
                } else if (rs == Long[].class) {
                    this.typs.put(var, 2);
                    this.arrays.addElement(var);
                } else if (rs == Double.TYPE) {
                    this.typs.put(var, 5);
                } else if (rs == Double[].class) {
                    this.typs.put(var, 6);
                    this.arrays.addElement(var);
                } else if (rs == Character.TYPE) {
                    this.typs.put(var, 9);
                } else if (rs == Character[].class) {
                    this.typs.put(var, 10);
                    this.arrays.addElement(var);
                } else if (rs == MvxString.class) {
                    this.typs.put(var, 7);
                } else if (rs == MvxString[].class) {
                    this.typs.put(var, 8);
                    this.arrays.addElement(var);
                } else {
                    this.typs.put(var, 0);
                }
            }
        }
    }

    public Vector getAllArrayFields() {
        return this.arrays;
    }
}