/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.m3;

import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import com.intentia.mak.util.MAKException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;
import m3.M3Connector;
import model.entities.IEntitiesModel;
import model.entity.IEntityModel;
import model.entity.XMLEntityModel;

/**
 *
 * @author JCHAUT
 */
public class M3ConfigModel extends Observable implements IEntitiesModel {

    private IEntityModel entity;
    private M3Connector config;
    private LinkedHashMap<String, M3ConfigurationInfo> listConfig;
    private String configSelect;
    private LinkedHashMap<String, M3ConfigurationInfo> listUnderConfig;
    private LinkedHashMap<String, M3ConfigurationInfo> listUnderConfigSelect;
    private String error;

    public M3ConfigModel(int custId, int envId) {
        super();
        this.listUnderConfigSelect = new LinkedHashMap<String, M3ConfigurationInfo>();
        entity = new XMLEntityModel("environnment");
        entity.setIdLink(custId);
        entity.loadData(envId);
    }

    public M3ConfigModel(IEntityModel e) {
        super();
        this.listUnderConfigSelect = new LinkedHashMap<String, M3ConfigurationInfo>();
        entity = e;
    }

    @Override
    public void loadData() throws IOException {
        setChanged();
        notifyObservers("process");
        config = new M3Connector(entity);
        try {
            config.init();
            listConfig = config.lstM3Config();
        } catch (MAKException ex) {
            error = "Impossible de lister les configurations!\n" + ex.getCause().getLocalizedMessage();
            setChanged();
            notifyObservers("error");
            addConfiguration("ALS", "ALS", "\\env\\DVLP\\Fix\\ALS\\HFix\\bindbg;\\env\\DVLP\\Fix\\ALS\\VFix\\bindbg;\\ALS\\1310\\SP1310C60189\\bindbg;\\ALS\\1310\\base\\bindbg;\\env\\DVLP\\Fix\\CERG\\HFix\\bindbg;\\env\\DVLP\\Fix\\CERG\\VFix\\bindbg;\\CERG\\1310\\base\\bindbg;\\env\\DVLP\\Properties;\\env\\DVLP\\Fix\\MVX\\HFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\TFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\VFix\\bindbg;\\MVX\\1310\\SP1310S51031\\bindbg;\\MVX\\1310\\base\\bindbg;\\env\\DVLP\\Fix\\FND\\TFix\\Foundation;\\FND\\133\\Foundation.jar;\\FND\\133\\common\\activation.jar;\\FND\\133\\common\\adonix.jar;\\FND\\133\\common\\asm.jar;\\FND\\133\\common\\axis-ant.jar;\\FND\\133\\common\\axis.jar;\\FND\\133\\common\\backport-util-concurrent-3.1-osgi.jar;\\FND\\133\\common\\commons-codec-1.3.jar;\\FND\\133\\common\\commons-discovery-0.2.jar;\\FND\\133\\common\\commons-lang-2.1.jar;\\FND\\133\\common\\commons-logging-1.0.4.jar;\\FND\\133\\common\\commons-net-2.0.jar;\\FND\\133\\common\\commons-net-ftp-2.0.jar;\\FND\\133\\common\\commonsCollections.jar;\\FND\\133\\common\\commonsLogging.jar;\\FND\\133\\common\\commonsPool.jar;\\FND\\133\\common\\cryptixJceProvider.jar;\\FND\\133\\common\\derby.jar;\\FND\\133\\common\\freemarker.jar;\\FND\\133\\common\\FndAPI.jar;\\FND\\133\\common\\javamail.jar;\\FND\\133\\common\\jaxrpc.jar;\\FND\\133\\common\\jcifs.jar;\\FND\\133\\common\\json.jar;\\FND\\133\\common\\jsr173_1.0_api.jar;\\FND\\133\\common\\jt400.jar;\\FND\\133\\common\\jtds.jar;\\FND\\133\\common\\junit.jar;\\FND\\133\\common\\log4j.jar;\\FND\\133\\common\\mneMetadataUtil.jar;\\FND\\133\\common\\mprolog.jar;\\FND\\133\\common\\MvxMpdCommon.jar;\\FND\\133\\common\\org.apache.commons.httpclient_3.1.0.v20080605-1935.jar;\\FND\\133\\common\\org.jdom_1.0.0.v201005080400.jar;\\FND\\133\\common\\PDFBox-0.7.3.jar;\\FND\\133\\common\\saaj.jar;\\FND\\133\\common\\servitrax.jar;\\FND\\133\\common\\servlet.jar;\\FND\\133\\common\\snmp.jar;\\FND\\133\\common\\soap.jar;\\FND\\133\\common\\sqljdbc4.jar;\\FND\\133\\common\\stax-1.2.0.jar;\\FND\\133\\common\\SysInfo.jar;\\FND\\133\\common\\wsdl4j-1.5.1.jar;\\FND\\133\\common\\xercesXercesImpl.jar;\\FND\\133\\common\\xercesXmlApis.jar;\\FND\\133\\common\\xmlenc.jar;\\env\\DVLP\\Drivers\\jdbc\\axis2-jaxws-api-1.2.jar;\\env\\DVLP\\Drivers\\jdbc\\Chronopost_2.0.jar;\\env\\DVLP\\Drivers\\jdbc\\InterfaceM3OptiTime.V2_R4.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-api.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-impl-2.2.6.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-xjc-2.2.jar;\\env\\DVLP\\Drivers\\jdbc\\jms.jar;\\env\\DVLP\\Drivers\\jdbc\\neocaseClient.jar;\\env\\DVLP\\Drivers\\jdbc\\opt-ffa-api-external-jdk5-8.0.2.4.jar;\\env\\DVLP\\Drivers\\jdbc\\OAGI_JAXB.jar;\\env\\DVLP\\Drivers\\jdbc\\tibcrypt.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjms.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsadmin.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsapps.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsufo.jar;\\env\\DVLP\\Drivers\\jdbc\\tibrvjms.jar");
            //config.addClassPathToConfig("ALS", ";\\env\\DVLP\\Fix\\ALS\\HFix\\bindbg;\\env\\DVLP\\Fix\\ALS\\VFix\\bindbg;\\ALS\\1310\\SP1310C60189\\bindbg;\\ALS\\1310\\base\\bindbg;\\env\\DVLP\\Fix\\CERG\\HFix\\bindbg;\\env\\DVLP\\Fix\\CERG\\VFix\\bindbg;\\CERG\\1310\\base\\bindbg;\\env\\DVLP\\Properties;\\env\\DVLP\\Fix\\MVX\\HFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\TFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\VFix\\bindbg;\\MVX\\1310\\SP1310S51031\\bindbg;\\MVX\\1310\\base\\bindbg;\\env\\DVLP\\Fix\\FND\\TFix\\Foundation;\\FND\\133\\Foundation.jar;\\FND\\133\\common\\activation.jar;\\FND\\133\\common\\adonix.jar;\\FND\\133\\common\\asm.jar;\\FND\\133\\common\\axis-ant.jar;\\FND\\133\\common\\axis.jar;\\FND\\133\\common\\backport-util-concurrent-3.1-osgi.jar;\\FND\\133\\common\\commons-codec-1.3.jar;\\FND\\133\\common\\commons-discovery-0.2.jar;\\FND\\133\\common\\commons-lang-2.1.jar;\\FND\\133\\common\\commons-logging-1.0.4.jar;\\FND\\133\\common\\commons-net-2.0.jar;\\FND\\133\\common\\commons-net-ftp-2.0.jar;\\FND\\133\\common\\commonsCollections.jar;\\FND\\133\\common\\commonsLogging.jar;\\FND\\133\\common\\commonsPool.jar;\\FND\\133\\common\\cryptixJceProvider.jar;\\FND\\133\\common\\derby.jar;\\FND\\133\\common\\freemarker.jar;\\FND\\133\\common\\FndAPI.jar;\\FND\\133\\common\\javamail.jar;\\FND\\133\\common\\jaxrpc.jar;\\FND\\133\\common\\jcifs.jar;\\FND\\133\\common\\json.jar;\\FND\\133\\common\\jsr173_1.0_api.jar;\\FND\\133\\common\\jt400.jar;\\FND\\133\\common\\jtds.jar;\\FND\\133\\common\\junit.jar;\\FND\\133\\common\\log4j.jar;\\FND\\133\\common\\mneMetadataUtil.jar;\\FND\\133\\common\\mprolog.jar;\\FND\\133\\common\\MvxMpdCommon.jar;\\FND\\133\\common\\org.apache.commons.httpclient_3.1.0.v20080605-1935.jar;\\FND\\133\\common\\org.jdom_1.0.0.v201005080400.jar;\\FND\\133\\common\\PDFBox-0.7.3.jar;\\FND\\133\\common\\saaj.jar;\\FND\\133\\common\\servitrax.jar;\\FND\\133\\common\\servlet.jar;\\FND\\133\\common\\snmp.jar;\\FND\\133\\common\\soap.jar;\\FND\\133\\common\\sqljdbc4.jar;\\FND\\133\\common\\stax-1.2.0.jar;\\FND\\133\\common\\SysInfo.jar;\\FND\\133\\common\\wsdl4j-1.5.1.jar;\\FND\\133\\common\\xercesXercesImpl.jar;\\FND\\133\\common\\xercesXmlApis.jar;\\FND\\133\\common\\xmlenc.jar;\\env\\DVLP\\Drivers\\jdbc\\axis2-jaxws-api-1.2.jar;\\env\\DVLP\\Drivers\\jdbc\\Chronopost_2.0.jar;\\env\\DVLP\\Drivers\\jdbc\\InterfaceM3OptiTime.V2_R4.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-api.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-impl-2.2.6.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-xjc-2.2.jar;\\env\\DVLP\\Drivers\\jdbc\\jms.jar;\\env\\DVLP\\Drivers\\jdbc\\neocaseClient.jar;\\env\\DVLP\\Drivers\\jdbc\\opt-ffa-api-external-jdk5-8.0.2.4.jar;\\env\\DVLP\\Drivers\\jdbc\\OAGI_JAXB.jar;\\env\\DVLP\\Drivers\\jdbc\\tibcrypt.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjms.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsadmin.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsapps.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsufo.jar;\\env\\DVLP\\Drivers\\jdbc\\tibrvjms.jar");
            //addConfiguration("CERG", "CERG", "");
            addConfiguration("MVX", "MVX", "\\env\\DVLP\\Fix\\CERG\\HFix\\bindbg;\\env\\DVLP\\Fix\\CERG\\VFix\\bindbg;\\CERG\\1310\\base\\bindbg;\\env\\DVLP\\Properties;\\env\\DVLP\\Fix\\MVX\\HFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\TFix\\bindbg;\\env\\DVLP\\Fix\\MVX\\VFix\\bindbg;\\MVX\\1310\\SP1310S51031\\bindbg;\\MVX\\1310\\base\\bindbg;\\env\\DVLP\\Fix\\FND\\TFix\\Foundation;\\FND\\133\\Foundation.jar;\\FND\\133\\common\\activation.jar;\\FND\\133\\common\\adonix.jar;\\FND\\133\\common\\asm.jar;\\FND\\133\\common\\axis-ant.jar;\\FND\\133\\common\\axis.jar;\\FND\\133\\common\\backport-util-concurrent-3.1-osgi.jar;\\FND\\133\\common\\commons-codec-1.3.jar;\\FND\\133\\common\\commons-discovery-0.2.jar;\\FND\\133\\common\\commons-lang-2.1.jar;\\FND\\133\\common\\commons-logging-1.0.4.jar;\\FND\\133\\common\\commons-net-2.0.jar;\\FND\\133\\common\\commons-net-ftp-2.0.jar;\\FND\\133\\common\\commonsCollections.jar;\\FND\\133\\common\\commonsLogging.jar;\\FND\\133\\common\\commonsPool.jar;\\FND\\133\\common\\cryptixJceProvider.jar;\\FND\\133\\common\\derby.jar;\\FND\\133\\common\\freemarker.jar;\\FND\\133\\common\\FndAPI.jar;\\FND\\133\\common\\javamail.jar;\\FND\\133\\common\\jaxrpc.jar;\\FND\\133\\common\\jcifs.jar;\\FND\\133\\common\\json.jar;\\FND\\133\\common\\jsr173_1.0_api.jar;\\FND\\133\\common\\jt400.jar;\\FND\\133\\common\\jtds.jar;\\FND\\133\\common\\junit.jar;\\FND\\133\\common\\log4j.jar;\\FND\\133\\common\\mneMetadataUtil.jar;\\FND\\133\\common\\mprolog.jar;\\FND\\133\\common\\MvxMpdCommon.jar;\\FND\\133\\common\\org.apache.commons.httpclient_3.1.0.v20080605-1935.jar;\\FND\\133\\common\\org.jdom_1.0.0.v201005080400.jar;\\FND\\133\\common\\PDFBox-0.7.3.jar;\\FND\\133\\common\\saaj.jar;\\FND\\133\\common\\servitrax.jar;\\FND\\133\\common\\servlet.jar;\\FND\\133\\common\\snmp.jar;\\FND\\133\\common\\soap.jar;\\FND\\133\\common\\sqljdbc4.jar;\\FND\\133\\common\\stax-1.2.0.jar;\\FND\\133\\common\\SysInfo.jar;\\FND\\133\\common\\wsdl4j-1.5.1.jar;\\FND\\133\\common\\xercesXercesImpl.jar;\\FND\\133\\common\\xercesXmlApis.jar;\\FND\\133\\common\\xmlenc.jar;\\env\\DVLP\\Drivers\\jdbc\\axis2-jaxws-api-1.2.jar;\\env\\DVLP\\Drivers\\jdbc\\Chronopost_2.0.jar;\\env\\DVLP\\Drivers\\jdbc\\InterfaceM3OptiTime.V2_R4.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-api.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-impl-2.2.6.jar;\\env\\DVLP\\Drivers\\jdbc\\jaxb-xjc-2.2.jar;\\env\\DVLP\\Drivers\\jdbc\\jms.jar;\\env\\DVLP\\Drivers\\jdbc\\neocaseClient.jar;\\env\\DVLP\\Drivers\\jdbc\\opt-ffa-api-external-jdk5-8.0.2.4.jar;\\env\\DVLP\\Drivers\\jdbc\\OAGI_JAXB.jar;\\env\\DVLP\\Drivers\\jdbc\\tibcrypt.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjms.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsadmin.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsapps.jar;\\env\\DVLP\\Drivers\\jdbc\\tibjmsufo.jar;\\env\\DVLP\\Drivers\\jdbc\\tibrvjms.jar");
        }

        if (listConfig != null && !listConfig.isEmpty()) {
            configSelect = listConfig.keySet().toArray()[0].toString();
            loadUnderConfig();
            setChanged();
            notifyObservers();
        }
    }

    public M3ConfigurationInfo addConfiguration(String conf, String desc, String path) {
        if (listConfig == null) {
            listConfig = new LinkedHashMap<>();
        }
        M3ConfigurationInfo alsConf = new M3ConfigurationInfo();
        alsConf.setName(conf);
        alsConf.setName(desc);
        listConfig.put(conf, alsConf);
        config.getConfigMap().put(conf, alsConf);
        config.addClassPathToConfig(conf, path);
        return alsConf;
    }

    private void loadUnderConfig() {
        if (listConfig != null && !listConfig.isEmpty()) {
            listUnderConfig = new LinkedHashMap<>();
            boolean insert = false;
            for (String s : listConfig.keySet()) {
                if (insert) {
                    listUnderConfig.put(s, listConfig.get(s));
                }
                if (s.equals(configSelect)) {
                    insert = true;
                }
            }
        }
    }

    @Override
    public Object[][] getData() {
        if (listConfig != null) {
            Object[][] o = new Object[listConfig.size()][1];
            for (int i = 0; i < listConfig.size(); i++) {
                o[i][0] = listConfig.get(i);
            }
            return o;
        } else {
            return null;
        }
    }

    public static List<String> formatUIConfig(LinkedHashMap<String, M3ConfigurationInfo> conf) {
        ArrayList<String> l = new ArrayList<>();
        if (conf != null) {
            for (String c : conf.keySet()) {
                l.add(conf.get(c).getName() + " (" + conf.get(c).getDescription() + ")");
            }
        }
        return l;
    }

    @Override
    public void setFileName(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<IEntityModel> getEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEntities(ArrayList<IEntityModel> entities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addEntity() {

    }

    @Override
    public void delEntity(IEntityModel entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delEntity(int idLink, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getColumnsHeader() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setColumnsHeader(String[] h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IEntityModel getModelEntity() {
        return null;
    }

    @Override
    public String getLink() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLink(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNameEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIdLink() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIdLink(int n) {

    }

    public IEntityModel getEntity() {
        return entity;
    }

    public void setEntity(IEntityModel entity) {
        this.entity = entity;
    }

    public LinkedHashMap<String, M3ConfigurationInfo> getListConfig() {
        return listConfig;
    }

    public void setListConfig(LinkedHashMap<String, M3ConfigurationInfo> listConf) {
        this.listConfig = listConf;
    }

    public M3Connector getConfig() {
        return config;
    }

    public void setConfig(M3Connector config) {
        this.config = config;
    }

    public String getConfigSelect() {
        return configSelect;
    }

    public void setConfigSelect(String configSelect) {
        this.configSelect = configSelect;
        loadUnderConfig();
        setChanged();
        notifyObservers("changeconfig");
    }

    public LinkedHashMap<String, M3ConfigurationInfo> getListUnderConfig() {
        return listUnderConfig;
    }

    public void setListUnderConfig(LinkedHashMap<String, M3ConfigurationInfo> listUnderConfig) {
        this.listUnderConfig = listUnderConfig;
    }

    public LinkedHashMap<String, M3ConfigurationInfo> getListUnderConfigSelect() {
        return listUnderConfigSelect;
    }

    public void setListUnderConfigSelect(LinkedHashMap<String, M3ConfigurationInfo> listUnderConfigSelect) {
        this.listUnderConfigSelect = listUnderConfigSelect;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
