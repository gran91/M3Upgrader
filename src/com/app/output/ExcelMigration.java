/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.output;

import java.io.IOException;
import java.util.ArrayList;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import main.Ressource;
import zio.Excel;

/**
 *
 * @author jeremy.chaut
 */
public class ExcelMigration {

    public Excel xls = new Excel();
    public WritableWorkbook wrkbk;
    public ArrayList<String> ok_nok;

    public ExcelMigration(String name) {
        wrkbk = xls.createExcelFile(name);
        wrkbk.setProtected(false);
        ok_nok=new ArrayList<>();
        ok_nok.add("X");
        ok_nok.add("");
    }

    public WritableSheet writeEnv(WritableSheet sheet, String title, ArrayList<String> data, int col, int line) throws WriteException, IOException {
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        a.add(data);
        xls.writeXlsTable(sheet, title, i18n.Language.traduce(Ressource.envColXls), a, col, line, Excel.XLS_TABLE_VERTICAL);
        return sheet;
    }

    public WritableSheet writeEnv(String title, ArrayList<String> data, int col, int line) throws WriteException, IOException {
        WritableSheet sheet = wrkbk.createSheet("Paramètre", wrkbk.getSheets().length);
        return writeEnv(sheet, title, data, col, line);

    }

    public WritableSheet writeWorkspace(WritableSheet sheet, ArrayList<ArrayList<String>> data, int col, int line) throws WriteException, IOException {
        xls.writeXlsTable(sheet, i18n.Language.getLabel(182), i18n.Language.traduce(Ressource.envWorkXls), data, col, line, Excel.XLS_TABLE_HORIZONTAL);
        return sheet;
    }

    public WritableSheet writeMigrate(WritableSheet sheet, ArrayList<ArrayList<String>> data, ArrayList<String> work, int col, int line) throws WriteException, IOException {
        xls.writeXlsTable(sheet, i18n.Language.getLabel(182), i18n.Language.traduce(Ressource.envMigrateXls), data, col, line, Excel.XLS_TABLE_HORIZONTAL);
        int lineTemp = line + 2;
        col += data.get(0).size();
        WritableCellFeatures wcf_ok = new WritableCellFeatures();
        wcf_ok.setDataValidationList(ok_nok);
        for (int i = 0; i < data.size(); i++) {
            Label lbl = new Label(col, lineTemp, work.get(0));
            lbl.setCellFeatures(wcf_ok);
            sheet.addCell(lbl);
            lineTemp++;
        }
        col++;
        lineTemp = line + 2;
        WritableCellFeatures wcf_work = new WritableCellFeatures();
        wcf_work.setDataValidationList(work);
        for (int i = 0; i < data.size(); i++) {
            Blank lbl = new Blank(col, lineTemp);
            lbl.setCellFeatures(wcf_work);
            sheet.addCell(lbl);
            lineTemp++;
        }
        return sheet;
    }

    public WritableSheet writeMigrate(ArrayList<ArrayList<String>> data, ArrayList<String> work, int col, int line) throws WriteException, IOException {
        WritableSheet sheet = wrkbk.createSheet("Liste spécifique", wrkbk.getSheets().length);
        return writeMigrate(sheet, data, work, col, line);
    }

    public WritableSheet writeConfiguration(WritableSheet sheet, String title, ArrayList<String> lbl, ArrayList<String> data, int col, int line) throws WriteException, IOException {
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        a.add(data);
        xls.writeXlsTable(sheet, title, lbl, a, col, line, Excel.XLS_TABLE_VERTICAL);
        return sheet;
    }

    public WritableSheet writeActionNumber(WritableSheet sheet, String data, int col, int line) throws WriteException, IOException {
        Excel.addCell(sheet, Excel.titleFormat, col, line, i18n.Language.getLabel(185));
        Excel.addCell(sheet, Border.ALL, BorderLineStyle.MEDIUM, col + 1, line, data);
        return sheet;
    }
}
