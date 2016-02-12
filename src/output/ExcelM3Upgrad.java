/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import com.app.main.Ressource;
import com.app.model.m3.M3UpgradModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import m3.M3UpdObjModel;
import mail.GmailTLS;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ExcelM3Upgrad extends AbstractOutput {

    private final M3UpgradModel model;
    private Workbook workbook;
    private final int type;
    private final int beginROW = 4;
    private final int beginCOL = 2;
    private Map<String, CellStyle> styles;
    private Object[][] data;
    private InputStream in = null;
    private boolean endFile = false;

    public ExcelM3Upgrad(M3UpgradModel m) {
        model = m;
        type = 0;
        try {
            FileUtils.copyURLToFile(getClass().getClassLoader().getResource("template" + System.getProperty("file.separator") + "m3.xls"), new File("temp.xls"));
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    public ExcelM3Upgrad(M3UpgradModel m, int t) {
        model = m;
        type = t;
        try {
            if (type == 0) {
                FileUtils.copyURLToFile(getClass().getClassLoader().getResource("template" + System.getProperty("file.separator") + "m3.xls"), new File("temp.xls"));
            } else {
                FileUtils.copyURLToFile(getClass().getClassLoader().getResource("template" + System.getProperty("file.separator") + "m3.xlsx"), new File("temp.xlsx"));
            }
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    public void write() throws FileNotFoundException, IOException {
        if (type == 0) {
            in = new java.io.FileInputStream(new File("temp.xls"));
            workbook = new HSSFWorkbook(in);
        } else {
            in = new java.io.FileInputStream(new File("temp.xlsx"));
            workbook = new XSSFWorkbook(in);
        }
        dialStatus();
        writeMigration();
        dialStatus();
        writeGraph();
        dialStatus();
        endFile = true;
    }

    private void recalculate() {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            for (Row r : sheet) {
                for (Cell c : r) {
                    if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        evaluator.evaluateFormulaCell(c);
                    }
                    sheet.autoSizeColumn(c.getColumnIndex());
                }
            }
        }

    }

    private void writeMigration() {
        Sheet sheet = workbook.getSheetAt(0);
        workbook.setSheetName(0, "Migration");
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        styles = createStyles(workbook);

        int rownum = beginROW;
        int cellnum = beginCOL;
        Row row = sheet.createRow(rownum++);
        for (int k = 0; k < model.getListColumn().length; k++) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(i18n.Language.getLabel(model.getListColumn()[k].getIdLng()));
            cell.setCellStyle(styles.get("header"));
            sheet.setColumnHidden(cell.getColumnIndex(), model.getListColumn()[k].isHidden());
            sheet.autoSizeColumn(k);
            dialStatus();
        }
        ArrayList<Integer> listHeader = new ArrayList<>();
        for (int i = 0; i < M3UpdObjModel.header.length; i++) {
            listHeader.add(M3UpdObjModel.header[i]);
        }

        String[] listLevel = i18n.Language.traduce(Ressource.listLevel).toArray(new String[Ressource.listLevel.length]);

        data = model.getData();
        for (int i = 0; i < data.length; i++) {
            busyDial.setText("Alimentation de la ligne " + (i + 1) + " sur " + data.length);
            row = sheet.createRow(rownum++);
            Object[] objArr = data[i];
            cellnum = beginCOL;
            boolean first = true;
            int j = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    if (first) {
                        first = false;
                        if ((Boolean) obj) {
                            cell.setCellValue("Oui");
                        } else {
                            cell.setCellValue("Non");
                        }
                    } else {
                        if ((Boolean) obj) {
                            cell.setCellValue("OK");
                        } else {
                            cell.setCellValue("KO");
                        }
                    }
                } else if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                }
                if (listHeader.indexOf(218) == j) {
                    try {
                        int n = Integer.parseInt(obj.toString().trim());
                        if (n == -1) {
                            cell.setCellValue("ERROR");
                        } else {
                            cell.setCellValue(listLevel[n]);
                        }
                    } catch (NumberFormatException ex) {
                        cell.setCellValue("");
                    }

                }

                if (j < objArr.length - 3) {
                    cell.setCellStyle(styles.get("cell_b_centered_locked"));
                } else {
                    cell.setCellStyle(styles.get("cell_b_centered"));
                }
                j++;
                dialStatus();
            }
            dialStatus();
        }

        dialStatus();
        busyDial.setText("Formatage du document");
        CellRangeAddressList userList = new CellRangeAddressList(beginROW + 1, beginROW + data.length, beginCOL + data[0].length - 1, beginCOL + data[0].length - 1);
        DataValidationConstraint userConstraint;
        DataValidation userValidation;

        if (type == 0) {
            userConstraint = DVConstraint.createExplicitListConstraint((String[]) model.getM3UserModel().getListUserSelect().toArray(new String[model.getM3UserModel().getListUserSelect().size()]));
            userValidation = new HSSFDataValidation(userList, userConstraint);
        } else {
            XSSFDataValidationHelper userHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            userConstraint = (XSSFDataValidationConstraint) userHelper.createExplicitListConstraint((String[]) model.getM3UserModel().getListUserSelect().toArray(new String[model.getM3UserModel().getListUserSelect().size()]));
            userValidation = (XSSFDataValidation) userHelper.createValidation(
                    userConstraint, userList);
        }
        sheet.addValidationData(userValidation);

        CellRangeAddressList migList = new CellRangeAddressList(beginROW + 1, beginROW + data.length, beginCOL + data[0].length - 2, beginCOL + data[0].length - 2);
        DataValidationConstraint migConstraint;
        DataValidation migValidation;

        if (type == 0) {
            migConstraint = DVConstraint.createExplicitListConstraint(new String[]{"OK", "KO"});
            migValidation = new HSSFDataValidation(migList, migConstraint);
        } else {
            XSSFDataValidationHelper migHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            migConstraint = (XSSFDataValidationConstraint) migHelper.createExplicitListConstraint(new String[]{"OK", "KO"});
            migValidation = (XSSFDataValidation) migHelper.createValidation(
                    migConstraint, migList);
        }
        sheet.addValidationData(migValidation);

        CellRangeAddressList levelList = new CellRangeAddressList(beginROW + 1, beginROW + data.length, beginCOL + data[0].length - 3, beginCOL + data[0].length - 3);
        DataValidationConstraint levelConstraint;
        DataValidation levelValidation;

        ArrayList<String> listNameLevel = new ArrayList<>();
        listNameLevel.add("ERROR");
        listNameLevel.addAll(i18n.Language.traduce(Ressource.listLevel));//.toArray(new String[Ressource.listLevel.length])
        if (type == 0) {
            levelConstraint = DVConstraint.createExplicitListConstraint(listNameLevel.toArray(new String[listNameLevel.size()]));
            levelValidation = new HSSFDataValidation(levelList, levelConstraint);
        } else {
            XSSFDataValidationHelper levelHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            levelConstraint = (XSSFDataValidationConstraint) levelHelper.createExplicitListConstraint(i18n.Language.traduce(Ressource.listLevel).toArray(new String[Ressource.listLevel.length]));
            levelValidation = (XSSFDataValidation) levelHelper.createValidation(
                    levelConstraint, levelList);
        }
        sheet.addValidationData(levelValidation);

        int irow = beginROW;
        int icol = beginCOL + model.getListColumn().length + 2;
        row = sheet.getRow(irow);
        Cell cell = row.createCell(icol);
        sheet.addMergedRegion(new CellRangeAddress(irow, irow, icol, icol + 1));
        cell.setCellValue("Estimation de la charge");
        cell.setCellStyle(styles.get("header"));

        irow++;
        row = sheet.getRow(irow);

        int cpt = 0;
        ArrayList<String> listStringLevel = i18n.Language.traduce(Ressource.listLevel);
        for (String s : listStringLevel) {
            cell = row.createCell(icol);
            cell.setCellValue(s);
            cell.setCellStyle(styles.get("cell_b_centered_locked"));
            cell = row.createCell(icol + 1);
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            String columnLetter = CellReference.convertNumToColString(listHeader.indexOf(218) + beginCOL);
            cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + s + "\")*" + Ressource.listWeightLevel[cpt]);
            cell.setCellStyle(styles.get("cell_b_centered_locked"));
            irow++;
            row = sheet.getRow(irow);
            cpt++;
        }
        row = sheet.getRow(irow);
        cell = row.createCell(icol);
        cell.setCellValue("Total des charges");
        cell.setCellStyle(styles.get("cell_b_centered_locked"));
        cell = row.createCell(icol + 1);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        String columnLetter = CellReference.convertNumToColString(listHeader.indexOf(icol + 1));
        cell.setCellFormula("SUM(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + Ressource.listLevel.length + 1) + ")");
        cell.setCellStyle(styles.get("cell_b_centered_locked"));

        for (int k = 0; k < model.getListColumn().length + 3; k++) {
            sheet.autoSizeColumn(k);
        }

        sheet.protectSheet("3kles2014");
    }

    private void writeGraph() {
        busyDial.setText("Génération des graphiques statistiques");
        Sheet s = workbook.getSheetAt(1);
        workbook.setSheetName(1, "Statistiques");

        ArrayList<Integer> listHeader = new ArrayList<>();
        for (int i = 0; i < M3UpdObjModel.header.length; i++) {
            listHeader.add(M3UpdObjModel.header[i]);
        }

        int irow = 4;
        Row row = s.createRow(irow);
        Cell cell = row.createCell(2);
        s.addMergedRegion(new CellRangeAddress(irow, irow, 2, 7));
        cell.setCellValue("Répartition des spécifiques");
        cell.setCellStyle(styles.get("cell_centered_locked"));

        irow = 8;
        row = s.createRow(irow);
        for (int i = 0; i < com.app.main.Ressource.listTypeM3Entity.length; i++) {
            cell = row.createCell(3);
            cell.setCellValue(com.app.main.Ressource.listTypeM3Entity[i]);
            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            String columnLetter = CellReference.convertNumToColString(beginCOL);
            cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + com.app.main.Ressource.listTypeM3Entity[i] + "\")");
            irow++;
            row = s.createRow(irow);
            dialStatus();
        }

        irow = 4;
        row = s.getRow(irow);
        cell = row.createCell(10);
        s.addMergedRegion(new CellRangeAddress(irow, irow, 10, 15));
        cell.setCellValue("Existance des sources");
        cell.setCellStyle(styles.get("cell_centered_locked"));

        int posVal = listHeader.indexOf(199);
        posVal += beginCOL;

        irow = 8;
        row = s.getRow(irow);
        cell = row.createCell(12);
        cell.setCellValue("OK");
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        String columnLetter = CellReference.convertNumToColString(posVal);
        cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + "Oui" + "\")");

        irow++;
        row = s.getRow(irow);
        cell = row.createCell(12);
        cell.setCellValue("NOK");
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
//        columnLetter = CellReference.convertNumToColString(posVal);
        cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + "Non" + "\")");

        irow = 24;
        row = s.createRow(irow);
        cell = row.createCell(2);
        s.addMergedRegion(new CellRangeAddress(irow, irow, 2, 7));
        cell.setCellValue("Synthèse de migration");
        cell.setCellStyle(styles.get("cell_centered_locked"));

        int posMig = listHeader.indexOf(201);
        posMig += beginCOL;
        int posUser = listHeader.indexOf(202);
        posUser += beginCOL;

        irow = 28;
        row = s.createRow(irow);
        cell = row.createCell(3);
        cell.setCellValue("OK+USER");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        String columnMig = CellReference.convertNumToColString(posMig);
        String columnUser = CellReference.convertNumToColString(posUser);
        cell.setCellFormula("SUMPRODUCT((" + workbook.getSheetName(0) + "!" + columnMig + (beginROW + 2) + ":" + columnMig + (beginROW + data.length + 1) + "=\"" + "OK" + "\")*(" + workbook.getSheetName(0) + "!" + columnUser + (beginROW + 2) + ":" + columnUser + (beginROW + data.length + 1) + "<>\"" + "" + "\"))");

        irow++;
        row = s.createRow(irow);
        cell = row.createCell(3);
        cell.setCellValue("OK");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula("SUMPRODUCT((" + workbook.getSheetName(0) + "!" + columnMig + (beginROW + 2) + ":" + columnMig + (beginROW + data.length + 1) + "=\"" + "OK" + "\")*(" + workbook.getSheetName(0) + "!" + columnUser + (beginROW + 2) + ":" + columnUser + (beginROW + data.length + 1) + "=\"" + "" + "\"))");

        irow++;
        row = s.createRow(irow);
        cell = row.createCell(3);
        cell.setCellValue("NOK");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnMig + (beginROW + 2) + ":" + columnMig + (beginROW + data.length + 1) + ",\"KO\")");

        irow++;
        row = s.createRow(irow);
        cell = row.createCell(3);
        cell.setCellValue("Somme");
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        String posSum = CellReference.convertNumToColString(4);
        cell.setCellFormula("SUM(" + posSum + (irow - 2) + ":" + posSum + (irow) + ")");

        posVal = listHeader.indexOf(217);
        posVal += beginCOL;

        irow = 24;
        row = s.getRow(irow);
        cell = row.createCell(10);
        s.addMergedRegion(new CellRangeAddress(irow, irow, 10, 15));
        cell.setCellValue("Analyse des objets instanciables");
        cell.setCellStyle(styles.get("cell_centered_locked"));

        irow = 28;
        row = s.getRow(irow);
        cell = row.createCell(12);
        cell.setCellValue("Class OK");
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        columnLetter = CellReference.convertNumToColString(posVal);
        cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + "" + "\")");

        irow++;
        row = s.getRow(irow);
        cell = row.createCell(12);
        cell.setCellValue("Class NOK");
        cell = row.createCell(13);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        columnLetter = CellReference.convertNumToColString(posVal);
//cell.setCellFormula("COUNTIF(" + workbook.getSheetName(0) + "!" + columnLetter + (beginROW + 2) + ":" + columnLetter + (beginROW + data.length + 1) + ",\"" + "<>" + "\"&\"" + "*" + "\")");
        cell.setCellFormula("E32-N29");

        s.protectSheet("3kles2014");
    }

    public boolean save() {
        dialStatus();
        try {
            if (workbook instanceof HSSFWorkbook) {
                if (!filepath.endsWith("xls")) {
                    filepath += ".xls";
                }
            } else if (workbook instanceof XSSFWorkbook) {
                if (!filepath.endsWith("xlsx")) {
                    filepath += ".xlsx";
                }
            }
            busyDial.setText("Sauvegarde du fichier " + filepath);
            FileOutputStream out = new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();
            if (endFile) {
                Object[] options = {i18n.Language.getLabel(140),
                    i18n.Language.getLabel(23),
                    i18n.Language.getLabel(130)};
                int n = JOptionPane.showOptionDialog(busyDial,
                        i18n.Language.getLabel(213),
                        "Excel",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == 0) {
                    busyDial.setText(i18n.Language.getLabel(214) + "...");
                    Desktop dt = Desktop.getDesktop();
                    dt.open(new File(filepath));
                } else if (n == 1) {
                    try {
                        busyDial.setText(i18n.Language.getLabel(215) + "...");
                        GmailTLS mail = new GmailTLS();
                        Message msg = mail.writeMail("j.chaut@3kles-consulting.com", "M3Upgrader", "Résultat de M3Upgrader");
                        msg = mail.addAttachment(msg, new File(filepath));
                        busyDial.setText(i18n.Language.getLabel(216) + "...");
                        mail.sendMail(msg);
                    } catch (MessagingException ex) {
                        Ressource.logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * create a library of cell styles
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        DataFormat df = wb.createDataFormat();

        Font font1 = wb.createFont();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        style.setLocked(false);
        styles.put("cell_centered", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        style.setLocked(true);
        styles.put("cell_centered_locked", style);
//        style = createBorderedStyle(wb);
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setFont(headerFont);
//        style.setDataFormat(df.getFormat("d-mmm"));
//        styles.put("header_date", style);
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        style.setLocked(false);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        style.setLocked(true);
        styles.put("cell_b_centered_locked", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_b_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_bg", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short) 14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        styles.put("cell_normal", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setIndention((short) 1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
}
