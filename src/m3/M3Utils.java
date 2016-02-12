/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import com.app.main.Ressource;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3Utils {

    public static String getPgmLevel(String path) {
        Pattern p = Pattern.compile(Ressource.regex_sp);
        if (path.contains(Ressource.HFix)) {
            return Ressource.HFix;
        } else if (path.contains(Ressource.VFix)) {
            return Ressource.VFix;
        } else if (path.contains(Ressource.TFix)) {
            return Ressource.TFix;
        } else if (path.matches(Ressource.regex_sp)) {
            Matcher m = p.matcher(Ressource.regex_sp);
            ArrayList a = new ArrayList();
            if (m.find()) {
                return m.group();
            }
        } else if (path.contains(Ressource.base)) {
            return Ressource.base;
        }
        return "";
    }

    public static String getPgmType(String path) {
        if (path.contains(Ressource.packagePGM)) {
            return Ressource.PGM;
        } else if (path.contains(Ressource.packageDSP)) {
            return Ressource.DSP;
        } else if (path.contains(Ressource.packageDS)) {
            return Ressource.DS;
        } else if (path.contains(Ressource.packageOUT)) {
            return Ressource.OUT;
        } else if (path.contains(Ressource.packageDB)) {
            return Ressource.DB;
        }
        return "";
    }
}
