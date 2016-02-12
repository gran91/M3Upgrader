/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package m3;

import java.io.IOException;
import java.util.ArrayList;
import com.app.main.Ressource;
import com.intentia.mak.core.m3.foundation.M3ConfigurationInfo;
import com.intentia.mak.core.m3.foundation.M3Runtime;
import mak.core.m3.foundation.factory.M3RuntimeFactory;
import com.intentia.mak.core.movex.EnvironmentProperties;
import com.intentia.mak.ui.util.http.UserCredentialsProvider;
import com.intentia.mak.util.MAKException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.IEntityModel;

/**
 *
 * @author Jeremy.CHAUT
 */
public class M3ConfigOLD {

    private IEntityModel env;

    public M3ConfigOLD(IEntityModel e) {
        env = e;
    }

    public HashMap<String, M3ConfigurationInfo> lstM3ConfigNew() throws IOException {
        HashMap<String, M3ConfigurationInfo> map = new HashMap<>();
        if (env.getData() != null) {
            try {
                if (env.getData().isEmpty()) {
                    env.loadData();
                }
                int port = 0;
                try {
                    port = Integer.parseInt(env.getData().get(4).toString());
                } catch (NumberFormatException e) {
                }
                EnvironmentProperties e = new EnvironmentProperties();
                e.setServerAddress(env.getData().get(3));
                e.setServerPort(port);
                e.setRootPath(env.getData().get(3));
                UserCredentialsProvider credentialsProvider = new UserCredentialsProvider();
                M3Runtime m3Runtime = M3RuntimeFactory.getRuntime(e.getServerRootPath().toString(), e.getServerAddress(), e.getServerPort(),
                        credentialsProvider);                
                M3ConfigurationInfo[] configurations = ensureStdConfiguration(m3Runtime.listConfigurations());
                for(M3ConfigurationInfo conf:configurations){
                    map.put(conf.getName(), conf);
                }
            } catch (MAKException ex) {
                Logger.getLogger(M3ConfigOLD.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return map;
    }

//    private boolean getConfigurations(EnvironmentProperties e) throws MAKException {
//        IPath rootPath = e.getServerRootPath();
//        UserCredentialsProvider credentialsProvider = new UserCredentialsProvider();
//        M3Runtime m3Runtime = M3RuntimeFactory.getRuntime(rootPath.toString(), e.getServerAddress(), e.getServerPort(),
//                credentialsProvider);
//
//        M3ConfigurationInfo[] configurations = m3Runtime.listConfigurations();
//        this.m_configurations = ensureStdConfiguration(configurations);
//        this.m_configurationProperties.setName(this.m_configurations[0].getName());
//        this.m_configurationProperties.setDescription(this.m_configurations[0].getDescription());
//        this.m_configurationProperties.setClassPath(m3Runtime.getClassPath(this.m_configurationProperties.getName()));
//        this.m_configurationProperties.getClassPath();
//        MovexSystem mvxSys = MovexSystemFactory.newMovexSystem(m_environmentProperties, m_configurationProperties, null);
//        return (this.m_configurations != null) && (this.m_configurations.length > 0);
//    }

    private M3ConfigurationInfo[] ensureStdConfiguration(M3ConfigurationInfo[] configurations) {
        for (int i = 0; i < configurations.length; i++) {
            if ("MVX".equals(configurations[i].getName())) {
                return configurations;
            }
        }
        M3ConfigurationInfo[] stdAdded = new M3ConfigurationInfo[configurations.length + 1];
        System.arraycopy(configurations, 0, stdAdded, 1, configurations.length);
        stdAdded[0] = new M3ConfigurationInfo();
        stdAdded[0].setName("MVX");
        stdAdded[0].setDescription("Standard Configuration");
        return stdAdded;
    }

    public ArrayList<String> lstM3Config() throws IOException {
        if (env.getData() != null) {
            if (env.getData().isEmpty()) {
                env.loadData();
            }
            String url = Ressource.protocole + "://" + env.getData().get(3) + ":" + env.getData().get(4) + "/configs";
            String content = "";
            content = tools.ToolsRegParser.getURLContent(url, env.getData().get(5), env.getData().get(6));
            return tools.ToolsRegParser.parseContent(content, Ressource.regex_config, 3);
        } else {
            return new ArrayList<>();
        }
    }
}
