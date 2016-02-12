/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author JCHAUT
 */
public class Ressource extends main.Ressource {

    public static String title = "M3Upgrader";
    public static Logger logger = LogManager.getLogger("main");

    public static final String regex_classpath_table = "(<tr><td>java.class.path</td><td>)(.*?)(</td><tr><td>)";
    public static final String regex_m3be = "(M3BE)(.*?)(:)";
//public static final String regex_m3be = "(Movex_v12)(.*?)(:)";
    public static final String regex_sub = "Sub:([ABIM]):(.*?)(</td><td>)(?i)<a([^>]+)>((.*?):(.*?))</a>";
    public static final String regex_sub_begin = "Sub:([ABIM]):";
    public static final String regex_sub_end = "(</td><td>)(?i)<a([^>]+)>((.*?):(.*?))</a>";
    public static final String regex_config = "<a href=([^>]*)&p2([^>]*)>([^<]*)</a>";
    public static final String regex_sp = "/SP[0-9]{4}[A-Z][0-9]{5}.*?/";

    public static final String[] listTypeM3Entity = {"Program", "View Definition", "Database Interface", "Out Interface", "Data Structure"};
    /*
     * M3
     */
    public static final String M3BEAS400 = "root" + System.getProperty("file.separator") + "M3BE";
    public static final String MAKTOOLS = "tools" + System.getProperty("file.separator") + "mak";

    /*M3 Analyse Level
     */
    public static final int LEVEL_0 = 219;
    public static final int LEVEL_1 = 220;
    public static final int LEVEL_2 = 221;
    public static final int LEVEL_3 = 222;
    public static final int LEVEL_4 = 223;
    public static final int[] listLevel = {LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4};
    public static final int[] listWeightLevel = {1, 2, 4, 6, 8};
    /*M3 Level
     */
    public static final String HFix = "HFix";
    public static final String VFix = "VFix";
    public static final String TFix = "TFix";
    public static final String HFixDir = System.getProperty("file.separator") + HFix;
    public static final String VFixDir = System.getProperty("file.separator") + VFix;
    public static final String TFixDir = System.getProperty("file.separator") + TFix;
    public static final String base = "base";

    /*M3 pgmType
     */
    public static final String packagePGM = "mvx" + System.getProperty("file.separator") + "app" + System.getProperty("file.separator") + "pgm" + System.getProperty("file.separator") + "customer";
    public static final String packageDSP = "mvx" + System.getProperty("file.separator") + "dsp";
    public static final String packageOUT = "mvx" + System.getProperty("file.separator") + "out";
    public static final String packageDB = "mvx" + System.getProperty("file.separator") + "db";
    public static final String packageDS = "mvx" + System.getProperty("file.separator") + "app" + System.getProperty("file.separator") + "ds";

    public static final String PGM = "Program";
    public static final String DSP = "View Definition";
    public static final String OUT = "Out Interface";
    public static final String DB = "Database Interface";
    public static final String DS = "Data Structure";

    /*ActionNumber M3
    */
    public static final String M3ACTDIR = System.getProperty("file.separator") + "actions";
    public static final String[] headActn = {"plateform", "product"};
    public static final String[] sessionActn = {"id", "operation", "description", "timestamp", "environment", "name", "makstatus"};
    public static final String[] componentActn = {"name", "timestamp", "status"};
    public static final String[] fileActn = {"description", "filename", "operation", "status", "targetpath", "timestamp", "type"};
}
