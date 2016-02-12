package m3;

import com.intentia.mak.core.http.AbstractCredentialsProvider;
import org.apache.commons.httpclient.UsernamePasswordCredentials;


public final class ZUserCredentialsProvider extends AbstractCredentialsProvider {

//    protected static final Logger LOGGER = Logger.getLogger(UserCredentialsProvider.class);
    protected UsernamePasswordCredentials m_tmpCredentials;

    public synchronized UsernamePasswordCredentials getCredentialsFromUser(String address, int port) {
//        Display.getDefault().syncExec(new Runnable(address, port) {
//            public void run() {
//                Shell shell = UserCredentialsProvider.this.getShell();
//                if (shell != null) {
//                    AuthenticationDialog dlg = new AuthenticationDialog(shell, this.val$address, this.val$port);
//                    if (dlg.open() == 0) {
//                        UserCredentialsProvider.this.m_tmpCredentials = new UsernamePasswordCredentials(dlg.getUid(), dlg.getPWD());
//                    } else {
//                        UserCredentialsProvider.this.m_tmpCredentials = null;
//                    }
//                } else {
//                    UserCredentialsProvider.LOGGER.error("No shell");
//                }
//            }
//        });
        if (this.m_tmpCredentials != null) {
            addToCache(address, port, this.m_tmpCredentials);
        }
        return this.m_tmpCredentials;
    }

//    protected Shell getShell() {
//        Shell shell = Display.getDefault().getActiveShell();
//        if (shell != null) {
//            return shell;
//        }
//
//        shell = Display.getCurrent().getActiveShell();
//        if (shell != null) {
//            return shell;
//        }
//
//        Shell[] shells = Display.getDefault().getShells();
//        if (shells.length > 0) {
//            return shells[0];
//        }
//        return null;
//    }

    public void setUserPwd(String user,String pwd) {
        ZUserCredentialsProvider.this.m_tmpCredentials = new UsernamePasswordCredentials(user, pwd);
    }
    
    public void setUserPwd(UsernamePasswordCredentials user) {
        ZUserCredentialsProvider.this.m_tmpCredentials = user;
    }
}
