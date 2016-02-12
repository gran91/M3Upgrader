package mak.core.m3.foundation.factory;

import com.intentia.mak.core.http.MAKCredentialsProvider;
import com.intentia.mak.core.m3.foundation.M3Runtime;
import com.intentia.mak.core.m3.foundation.api.M3APIRuntime;
import com.intentia.mak.core.m3.foundation.factory.M3RuntimeHandler;
import com.intentia.mak.core.m3.foundation.http.M3HTTPRuntime;
import com.intentia.mak.util.MAKException;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public final class M3RuntimeFactory {

    private static final int API = 1;
    private static final int HTTP = 2;

    public static M3Runtime getRuntime(String rootPath, String address, int port, MAKCredentialsProvider credentialsProvider)
            throws MAKException {
        try {
            M3Runtime[] runtimes = getAvailableRuntimes(rootPath, address, port, credentialsProvider);
            if (runtimes.length == 1) {
                return runtimes[0];
            }
            return new M3RuntimeHandler(runtimes);
        } catch (MAKException e) {
            throw new MAKException("Failed to get a foundation runtime communicator", e);
        }
    }

    private static M3Runtime[] getAvailableRuntimes(String rootPath, String address, int port, MAKCredentialsProvider credentialsProvider) throws MAKException {
        try {
            File foundationFolder = new File(rootPath, "FND");
            if (!foundationFolder.exists()) {
                throw new MAKException("No foundation found under " + rootPath);
            }

            int availableTypes = getAvailableTypes(foundationFolder);
            List runtimes = new ArrayList();
            if ((availableTypes & 0x1) == 1) {
                runtimes.add(new M3APIRuntime(address, port, credentialsProvider));
            }
            if ((availableTypes & 0x2) == 2) {
                runtimes.add(new M3HTTPRuntime(address, port, credentialsProvider));
            }
            return (M3Runtime[]) runtimes.toArray(new M3Runtime[runtimes.size()]);
        } catch (MAKException e) {
            throw new MAKException("Failed to get available foundation runtimes for M3 root path=" + rootPath
                    + ", address=" + address + " and port=" + port, e);
        }

    }

    private static int getAvailableTypes(File foundationFolder) throws MAKException {
        int availableTypes = 0;
        if (isHttp(foundationFolder)) {
            availableTypes |= 2;
        }
        if (isAPI(foundationFolder)) {
            availableTypes |= 1;
        }
        if (availableTypes == 0) {
            throw new MAKException("Unable to identify foundation type (Http/API) for " + foundationFolder);
        }
        return availableTypes;
    }

    private static boolean isAPI(File foundationFolder) {
        if (new File(foundationFolder, "1250").exists()) {
            return true;
        }
        if (find12Foundation(foundationFolder, "Foundation.jar")) {
            return true;
        }

        return findFile(foundationFolder, "FndAPI.jar", new String[]{"javadoc"}) != null;
    }

    private static boolean isHttp(File foundationFolder) {
        return new File(foundationFolder, "1243").exists();
    }

//    private static boolean find12Foundation(File foundationFolder, String fileName) {
//        File[] fnd12Folders = foundationFolder.listFiles(new FileFilter(fileName) {
//            public boolean accept(File file) {
//                return (file.isDirectory()) && (new File(file, M3RuntimeFactory.this).exists());
//            }
//        });
//        return (fnd12Folders != null) && (fnd12Folders.length > 0);
//    }
    
    private static boolean find12Foundation(File foundationFolder, String fileName) {
        File[] fnd12Folders =foundationFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return (file.isDirectory()) && (file.exists());
            }
        });
        return (fnd12Folders != null) && (fnd12Folders.length > 0);
    }

    private static File findFile(File file, String name, String[] excludeFolders) {
        File result = null;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int i = 0;
            do {
                if (!isExcluded(excludeFolders, files[i])) {
                    result = findFile(files[i], name, excludeFolders);
                    if (result != null) {
                        break;
                    }
                }
                i++;
                if ((files == null) || (result != null)) {
                    break;
                }
            } while (i < files.length);
        } else if (file.getName().equals(name)) {
            return file;
        }

        return result;
    }

    private static boolean isExcluded(String[] excludeFolders, File file) {
        for (int i = 0; i < excludeFolders.length; i++) {
            if (file.getName().equals(excludeFolders[i])) {
                return true;
            }
        }
        return false;
    }
}
