package m3;

import com.app.main.Main;
import com.intentia.mak.core.movex.MovexSystem;
import com.intentia.mak.core.movex.objects.FileObject;
import com.intentia.mak.fom.xml.impl.RecordFormatTypeImpl;
import com.intentia.mak.util.MAKException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Ressource;
import mak.core.movex.M3Progress;
import mak.db.MetaDataDef;
import mak.ds.DSComparator;
import mak.ds.DSXmlBuilder;
import mak.out.OUTComparator;
import mak.out.OUTXmlBuilder;
import org.apache.commons.io.FilenameUtils;
import org.kles.m3.comparator.DBComparator;
import parser.JParser;
import tools.Methode;

/**
 *
 * @author JCHAUT
 */
public class M3UpgradObject {

    private M3Connector m3Src, m3Targ;
    private JParser parser;
    private boolean srcExist = false;
    private M3UpdObjModel currentModel;
    private File migrateDirectory;

    public M3UpgradObject(M3Connector m3) {
        m3Src = m3;
        m3Targ = null;
    }

    public M3UpgradObject(M3Connector msrc, M3Connector mtarg) {
        m3Src = msrc;
        m3Targ = mtarg;
    }

    public M3UpdObjModel processM3Object(Path f) {
        M3UpdObjModel objModel = new M3UpdObjModel();
        currentModel = objModel;
        boolean migrate = true;

        objModel.setTypeObj(M3Utils.getPgmType(f.toAbsolutePath().toString()));
        if (objModel.getTypeObj().equals(com.app.main.Ressource.DB) && (f.getFileName().toString().contains("$Inst") || f.getFileName().toString().startsWith("d"))) {
            return null;
        }
        objModel.setFileName(f.getFileName().toString());
        objModel.setFilePath(f.toAbsolutePath().toString());
        Class superclass = null;
        try {
            superclass = m3.classe.ClassUtils.getSuperClass(m3Src.getClassloaderSpe(), f.toFile());
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            migrate = false;
            addError(M3ErrorModel.ERROR, f, ex.getMessage());
            com.app.main.Ressource.logger.error(ex.getLocalizedMessage(), ex);
        }
        boolean tmp = true;
        Path superClassFile = null;
        if (superclass != null) {
            try {
                FileObject[] lstFileSrc = null;
                FileObject[] lstSuperFileSrc = null;
                FileObject[] lstFileTarg = null;
                MovexSystem mvxSrc = m3Src.getM3System(m3Src.getMainConfig());
                MovexSystem mvxTarg = m3Targ.getM3System("MVX");
                FileObject[] test = mvxSrc.listDBInterfaces("OCUSMA", new M3Progress());
                MetaDataDef def = new MetaDataDef(m3Src.getClassloaderSpe(), "OCUSMA");
                MetaDataDef def2 = new MetaDataDef(m3Src.getClassLoaderStd(), "OCUSMA");
                DBComparator dbcomp = new DBComparator(def, def2);
                dbcomp.compare(def, def2);

                for (int i = 0; i <= def.getTable().getFieldCounter(); i++) {
//def.getTable().getFieldMetaData()[0]
                }
                if (!Arrays.asList(Ressource.lstSuperClassStd).contains(superclass.getName()) && objModel.getTypeObj().equals(com.app.main.Ressource.PGM)) {
//lstFileSrc = mvxSrc.listPrograms(FilenameUtils.getBaseName(f.getFileName().toString()), null);
//lstSuperFileSrc = mvxSrc.listPrograms(FilenameUtils.getBaseName(superclass.getSimpleName()), null);
//lstFileTarg = mvxTarg.listPrograms(f.getFileName().toString(), null);
                } else if (objModel.getTypeObj().equals(com.app.main.Ressource.DS)) {
                    lstFileSrc = mvxSrc.listDataStructures(FilenameUtils.getBaseName(f.getFileName().toString()), null);
                    lstFileTarg = mvxTarg.listDataStructures(f.getFileName().toString(), null);
                    if (lstFileSrc.length > 1) {
                        compareM3DS(lstFileSrc, lstFileTarg);
                    } else {
                        Path fileSrcSpe = Paths.get(lstFileSrc[0].getPath().toOSString());
                        Path fileTargSpe = Paths.get(migrateDirectory.getAbsolutePath() + System.getProperty("file.separator") + "ds");
                        try {
                            Files.copy(fileSrcSpe, fileTargSpe.resolve(fileSrcSpe.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Logger.getLogger(M3UpgradObject.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (objModel.getTypeObj().equals(com.app.main.Ressource.DB)) {
                    lstFileSrc = mvxSrc.listDBInterfaces(FilenameUtils.getBaseName(f.getFileName().toString()), new M3Progress());
                    lstFileTarg = mvxTarg.listDBInterfaces(f.getFileName().toString(), new M3Progress());
                } else if (objModel.getTypeObj().equals(com.app.main.Ressource.OUT)) {
//lstFileSrc = mvxSrc.listOutInterfaces(FilenameUtils.getBaseName(f.getFileName().toString()), null);
//lstFileTarg = mvxTarg.listOutInterfaces(f.getFileName().toString(), null);
                } else if (objModel.getTypeObj().equals(com.app.main.Ressource.DSP)) {
//lstFileSrc = mvxSrc.listViewDefinitions(FilenameUtils.getBaseName(f.getFileName().toString()), null);
//lstFileTarg = mvxTarg.listViewDefinitions(f.getFileName().toString(), null);
                }
            } catch (MAKException ex) {
                Logger.getLogger(M3UpgradObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (tmp) {
            objModel.setUpdgradeLevel(0);
            objModel.setTypeM3Obj("Pur Specifique");
        } else {
            File fstdsrc = new File(objModel.getStdClassPath().toAbsolutePath().toString().replace("bindbg", "src").replace("binopt", "src"));
            if (fstdsrc.exists()) {
                objModel.setStdSrcPath(fstdsrc.toPath());
            } else {
                for (Path pstd : m3Src.getLstStdSource()) {
                    if (pstd.getFileName().toString().replaceAll("\\.java", "").equals(f.getFileName().toString().replaceAll("\\.class", ""))) {
                        objModel.setStdSrcPath(pstd);
                        break;
                    }
                }
            }
        }
        // Niveau Spé 
        objModel.setM3ClassLevel(M3Utils.getPgmLevel(f.toAbsolutePath().toString()));
        // Superclass et niveau
        if (superclass != null) {
            objModel.setSuperclass(superclass.getSimpleName());
        }
        if (tmp) {
            objModel.setStdClassLevel("FND");
        } else if (!tmp && superClassFile != null) {
            objModel.setStdClassLevel(M3Utils.getPgmLevel(superClassFile.toAbsolutePath().toString()));
        }

        String pathstd = f.toAbsolutePath().toString().replace("bindbg", "src").replace("binopt", "src");
        pathstd = pathstd.trim().replace("class", "java");
        File sp = new File(pathstd);
        if (sp.exists()) {
            objModel.setSrcExist(true);
            objModel.setSpeSrcPath(sp.toPath());

        } else {
            objModel.setSrcExist(false);
            for (Path pspe : m3Src.getLstSpeSource()) {
                if (pspe.getFileName().toString().replaceAll("\\.java", "").equals(f.getFileName().toString().replaceAll("\\.class", ""))) {
                    objModel.setSrcExist(true);
                    objModel.setSpeSrcPath(pspe);
                    break;
                }
            }
        }

        objModel.setExistingFields("");
        objModel.setMigrate(migrate);
        objModel.setUpdgradeLevel(compareM3Class(objModel));
        return objModel;
    }

    public M3UpdObjModel analyseM3Object(Path f) {
        M3UpdObjModel objModel = new M3UpdObjModel();
        currentModel = objModel;
        boolean migrate = true;

        objModel.setTypeObj(M3Utils.getPgmType(f.toAbsolutePath().toString()));
        if (objModel.getTypeObj().equals(com.app.main.Ressource.DB) && f.getFileName().toString().contains("$Inst")) {
            return null;
        }
        objModel.setFileName(f.getFileName().toString());
        objModel.setFilePath(f.toAbsolutePath().toString());
        Class superclass = null;
        try {
            superclass = m3.classe.ClassUtils.getSuperClass(m3Src.getClassloaderSpe(), f.toFile());
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            migrate = false;
            addError(M3ErrorModel.ERROR, f, ex.getMessage());
            com.app.main.Ressource.logger.error(ex.getLocalizedMessage(), ex);
        }
        boolean tmp = true;
        Path superClassFile = null;
        if (superclass != null) {
            if (!Arrays.asList(Ressource.lstSuperClassStd).contains(superclass.getName()) && objModel.getTypeObj().equals(com.app.main.Ressource.PGM)) {
                for (Path lstStd1 : m3Src.getLstStdClass()) {
                    if (f.getFileName().toString().equals(lstStd1.getFileName().toString())) {
                        superClassFile = lstStd1;
                        objModel.setTypeM3Obj("Specifique");
                        objModel.setStdClassPath(superClassFile);
                        tmp = false;
                        break;
                    }
                    if (superclass.getSimpleName().equals(lstStd1.getFileName().toString())) {
                        superClassFile = lstStd1;
                        objModel.setTypeM3Obj("Specifique");
                        objModel.setStdClassPath(superClassFile);
                        tmp = false;
                        break;
                    }
                }
            }
        }
        if (tmp) {
            objModel.setUpdgradeLevel(0);
            objModel.setTypeM3Obj("Pur Specifique");
        } else {
            File fstdsrc = new File(objModel.getStdClassPath().toAbsolutePath().toString().replace("bindbg", "src").replace("binopt", "src"));
            if (fstdsrc.exists()) {
                objModel.setStdSrcPath(fstdsrc.toPath());
            } else {
                for (Path pstd : m3Src.getLstStdSource()) {
                    if (pstd.getFileName().toString().replaceAll("\\.java", "").equals(f.getFileName().toString().replaceAll("\\.class", ""))) {
                        objModel.setStdSrcPath(pstd);
                        break;
                    }
                }
            }
        }
        // Niveau Spé 
        objModel.setM3ClassLevel(M3Utils.getPgmLevel(f.toAbsolutePath().toString()));
        // Superclass et niveau
        if (superclass != null) {
            objModel.setSuperclass(superclass.getSimpleName());
        }
        if (tmp) {
            objModel.setStdClassLevel("FND");
        } else if (!tmp && superClassFile != null) {
            objModel.setStdClassLevel(M3Utils.getPgmLevel(superClassFile.toAbsolutePath().toString()));
        }

        String pathstd = f.toAbsolutePath().toString().replace("bindbg", "src").replace("binopt", "src");
        pathstd = pathstd.trim().replace("class", "java");
        File sp = new File(pathstd);
        if (sp.exists()) {
            objModel.setSrcExist(true);
            objModel.setSpeSrcPath(sp.toPath());

        } else {
            objModel.setSrcExist(false);
            for (Path pspe : m3Src.getLstSpeSource()) {
                if (pspe.getFileName().toString().replaceAll("\\.java", "").equals(f.getFileName().toString().replaceAll("\\.class", ""))) {
                    objModel.setSrcExist(true);
                    objModel.setSpeSrcPath(pspe);
                    break;
                }
            }
        }

        objModel.setExistingFields("");
        objModel.setMigrate(migrate);
        objModel.setUpdgradeLevel(compareM3Class(objModel));
        return objModel;
    }

    private void addError(int type, Path f, String desc) {
        List<M3ErrorModel> err = new ArrayList<>();
        if (currentModel.getListError() != null) {
            err = currentModel.getListError();
        }
        err.add(new M3ErrorModel(type, f, desc));
        currentModel.setListError(err);
    }

    public static int compareM3Class(M3UpdObjModel m) {
        if (m.getTypeM3Obj().equals("Pur Specifique")) {
            return 0;
        }
        if (m.getTypeObj().equals(com.app.main.Ressource.PGM) || m.getTypeObj().equals(com.app.main.Ressource.DSP)) {
            return compareM3PGM(m);
        } else if (m.getTypeObj().equals(com.app.main.Ressource.DS) || m.getTypeObj().equals(com.app.main.Ressource.DB)) {
            return 0;
        } else if (m.getTypeObj().equals(com.app.main.Ressource.OUT)) {
            return compareM3OUT(m);
        }
        return 4;
    }

    private FileObject getFileByComponent(FileObject[] lst, String conf) {
        for (FileObject o : lst) {
            if (o.getComponent().equals(conf)) {
                return o;
            }
        }
        return null;
    }

    private FileObject getFileByComponent(FileObject[] lst, ArrayList<String> conf) {
        for (FileObject o : lst) {
            if (conf.contains(o.getComponent())) {
                return o;
            }
        }
        return null;
    }

    private  int compareM3DS(FileObject[] filesrc, FileObject[] fileTarg) {
        DSXmlBuilder dsSrc = new DSXmlBuilder();
        DSXmlBuilder dsTarg = new DSXmlBuilder();
        //standard source 
        FileObject fileSrcSpe = getFileByComponent(filesrc, m3Src.getListUnderConfigSelect());
        if (fileSrcSpe != null) {
            dsSrc.parse(fileSrcSpe.getPath().toFile());
            LinkedHashMap<String, String[]> spe = new LinkedHashMap<>(dsSrc.getFieldsMap());
            // standard spe 
            dsTarg.parse(fileTarg[0].getPath().toFile());
            LinkedHashMap<String, String[]> std = new LinkedHashMap<>(dsTarg.getFieldsMap());

            if (!DSComparator.compareHashMap(spe, std).isEmpty()) {
                // spe src 
                dsSrc.parse(filesrc[0].getPath().toFile());
                spe = new LinkedHashMap<>(dsSrc.getFieldsMap());
                // compare spe / std src 
                ArrayList<String> a = DSComparator.compareHashMap(spe, std);
                // std target
                for (int i = 0; i < a.size(); i++) {
                    dsTarg.getM_dsData().addField(dsSrc.getM_dsData().getField(a.get(i)));

                }
            } else {
                Path fsrc = Paths.get(fileSrcSpe.getPath().toOSString());
                Path fileTargSpe = Paths.get(migrateDirectory.getAbsolutePath() + System.getProperty("file.separator") + "ds" + System.getProperty("file.separator") + fileSrcSpe.getName());
                try {
                    Files.copy(fsrc, fileTargSpe, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(M3UpgradObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0;
    }

    private static int compareM3OUT(M3UpdObjModel m) {
        InputStream ins = null;
        int nbFormat = 0;
        int nbField = 0;
        try {
            OUTXmlBuilder xmlout = new OUTXmlBuilder();
            ins = new FileInputStream(m.getSpeSrcPath().toFile());
            xmlout.parse(ins);
            HashMap<String, RecordFormatTypeImpl> listSpe = OUTComparator.convertToHashMap(new ArrayList(xmlout.buildXml().getOutDatas().getOutData().getRecordFormats().getRecordFormat()));
            ins.close();

            ins = new FileInputStream(m.getStdSrcPath().toFile());
            xmlout.parse(ins);
            HashMap<String, RecordFormatTypeImpl> listStd = OUTComparator.convertToHashMap(new ArrayList(xmlout.buildXml().getOutDatas().getOutData().getRecordFormats().getRecordFormat()));
            ins.close();

            for (String key : listSpe.keySet()) {
                if (listStd.containsKey(key)) {
                    nbField += Math.abs(listSpe.get(key).getFields().getField().size() - listStd.get(key).getFields().getField().size());
                } else {
                    nbFormat++;
                    nbField += listSpe.get(key).getFields().getField().size();

                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);

            return -1;
        } catch (IOException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);

            return -1;
        }
        double poids = nbFormat * 4 + nbField * 0.5;
        return weightToLevel(poids);
    }

    private static int compareM3PGM(M3UpdObjModel m) {
        boolean parse = true;
        int superOK = 0;
        int superKO = 0;
        HashMap<String, Methode> stdMethodName = null;
        HashMap<String, Methode> speMethodName = null;
        JParser parser = new JParser();
        try {
            parser.parseSource(m.getStdSrcPath().toFile());
            stdMethodName = new HashMap<>(parser.getListMethod());
            parser.parseSource(m.getSpeSrcPath().toFile());
            speMethodName = new HashMap<>(parser.getListMethod());
        } catch (japa.parser.TokenMgrError ex) {
            parse = false;
        } catch (Exception ex) {
            parse = false;
        }

        if (parse) {
            ArrayList<Methode> listMethodFullSpe = new ArrayList<>();
            ArrayList<Methode> listMethodStdSpe = new ArrayList<>();

            for (String key : speMethodName.keySet()) {
                if (stdMethodName.containsKey(key)) {
                    listMethodStdSpe.add(speMethodName.get(key));
                } else {
                    listMethodFullSpe.add(speMethodName.get(key));
                }
            }

            double tempLine = 0;
            for (int j = 0; j < listMethodStdSpe.size(); j++) {
                Methode mStd = stdMethodName.get(listMethodStdSpe.get(j).getName());
                if (listMethodStdSpe.get(j).getSource().contains("super." + listMethodStdSpe.get(j).getName())) {
                    if (!listMethodStdSpe.get(j).getSource().trim().equals(mStd.getSource().trim())) {
                        superOK++;
                        tempLine += Math.abs(Math.abs(mStd.getEndLine() - mStd.getBeginLine()) - Math.abs(listMethodStdSpe.get(j).getEndLine() - listMethodStdSpe.get(j).getBeginLine())) * 0.5;
                    }
                } else {
                    if (!listMethodStdSpe.get(j).getSource().trim().equals(mStd.getSource().trim())) {
                        superKO++;
                        tempLine += Math.abs(Math.abs(mStd.getEndLine() - mStd.getBeginLine()) - Math.abs(listMethodStdSpe.get(j).getEndLine() - listMethodStdSpe.get(j).getBeginLine())) * 0.5;
                    }
                }
            }
            double poids = superOK * 2 + superKO * 4 + listMethodFullSpe.size() * 2 + tempLine;
            return weightToLevel(poids);
        }
        return -1;
    }

    private static int weightToLevel(double d) {
        if (d < 50) {
            return 1;
        } else if (d < 70) {
            return 2;
        } else if (d < 90) {
            return 3;
        } else if (d >= 90) {
            return 4;
        }
        return 4;
    }

    public static ArrayList<String> getSimpleNameMethod(Method[] lst) {
        ArrayList<String> a = new ArrayList<>();
        for (Method lst1 : lst) {
            a.add(lst1.getName());
        }
        return a;
    }

    public static ArrayList<String> getSimpleNameMethod(ArrayList<Method> lst) {
        return getSimpleNameMethod(lst.toArray(new Method[lst.size()]));
    }

    public JParser getParser() {
        return parser;
    }

    public void setParser(JParser parser) {
        this.parser = parser;
    }

    public boolean isSrcExist() {
        return srcExist;
    }

    public void setSrcExist(boolean srcExist) {
        this.srcExist = srcExist;

    }

    public File getMigrateDirectory() {
        return migrateDirectory;
    }

    public void setMigrateDirectory(File migrateDirectory) {
        this.migrateDirectory = migrateDirectory;
    }
}
