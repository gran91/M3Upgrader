package m3.classe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.app.main.Ressource;
import java.net.MalformedURLException;
import java.util.Observable;

/**
 *
 * @author Jeremy.CHAUT
 */
public class Classpath extends Observable {

    public static final int CLASS_PATH_STD = 0;
    public static final int CLASS_PATH_SPE = 1;
    private String host;
    private int port;
    private String user;
    private String mdp;

    public Classpath(String h, int p) {
        host = h;
        port = p;
        user = "";
        mdp = "";
    }

    public Classpath(String h, int p, String u, String m) {
        host = h;
        port = p;
        user = u;
        mdp = m;
    }

    public ArrayList<String> getClassPath(int type, String config) throws IOException {
        if (type == CLASS_PATH_STD) {
            return getListClassPath(getSupervisorPort(String.valueOf(port)));
        } else if (type == CLASS_PATH_SPE) {
            String url = formatServerView();
//String content = getURLContent(url);
            String content = tools.ToolsRegParser.getURLContent(url, user, mdp);
            String portSub;
            if (config != null && !config.equals("")) {
                portSub = parseContent(content, Ressource.regex_sub_begin + config + Ressource.regex_sub_end, 6).get(0);
            } else {
                portSub = parseContent(content, Ressource.regex_sub, 7).get(0);
            }
            return getListClassPath(portSub);
        }
        return null;
    }

    public ArrayList<String> getListClassPath(String portSub) throws IOException {
        String url = formatURLClassPath(portSub);
//        String content = getURLContent(url);
        String content = tools.ToolsRegParser.getURLContent(url, user, mdp);
        ArrayList<String> td = parseContent(content, Ressource.regex_classpath_table, 2);
        ArrayList<String> lstPath = new ArrayList<String>();
        for (int i = 0; i < td.size(); i++) {
            ArrayList<String> tempPath = parseContent(td.get(i), Ressource.regex_m3be, 2);
            lstPath = tools.Tools.concatArrayList(lstPath, tempPath);
        }
        return lstPath;
    }

    public String formatURLClassPath(String portSub) {
        return Ressource.protocole + "://" + host + ":" + port + "/" + Ressource.jvmInfo + host + "&port=" + portSub + "&p1=" + Ressource.jvmSysProp;
    }

    public String formatServerView() {
        return Ressource.protocole + "://" + host + ":" + port;
    }

    public String getURLContent(String url) throws IOException {
        String content = "";
        URL u = new URL(url);
        URLConnection uCon = u.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content += inputLine;
            System.out.println(inputLine);
        }
        in.close();
        return content;
    }

    public ArrayList<String> parseContent(String content, String regex, int group) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        ArrayList a = new ArrayList();
        while (m.find()) {
            content = m.group(group);
            a.add(content);
            System.out.println(content);
        }
        return a;
    }

    public ArrayList<ArrayList<String>> parseClassPath(ArrayList<String> classpath, String config, String[] otherconfig) {
        boolean ok = false;
        ArrayList<ArrayList<String>> lstClassPathConfig = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();
        String temp = config;
        int cpt = -1;
        for (int i = 0; i < classpath.size(); i++) {
            if (!ok || temp.equals("MVX")) {
                a.add(classpath.get(i));
                if (classpath.get(i).contains(temp)) {
                    ok = true;
                }
            } else if (ok && classpath.get(i).contains(temp)) {
                a.add(classpath.get(i));
            } else {
                lstClassPathConfig.add(a);
                cpt++;
                temp = otherconfig[cpt];
                a = new ArrayList<>();
                ok = false;
            }

        }
        if (a != null) {
            lstClassPathConfig.add(a);
        }
        return lstClassPathConfig;
    }

    public boolean envIsUp() throws MalformedURLException, IOException {
        URL u = new URL(Ressource.protocole + "://" + host + ":" + port);
        URLConnection uCon = u.openConnection();
        uCon.connect();
        return true;
    }

    private String getSupervisorPort(String port) {
        return port.substring(0, port.length() - 3) + "500";
    }
}
