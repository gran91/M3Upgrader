/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mak.core.movex;

import com.intentia.mak.core.m3.M3Version1243Factory;
import com.intentia.mak.core.m3.M3Version1250Factory;
import com.intentia.mak.core.m3.M3Version1310Factory;
import com.intentia.mak.core.m3.M3Version1401Factory;
import com.intentia.mak.core.m3.M3Version1412Factory;
import com.intentia.mak.core.m3.M3Version1420Factory;
import com.intentia.mak.core.m3.M3Version1510Factory;
import com.intentia.mak.core.m3.M3VersionFactory;

public final class M3Versions {

    public static final M3VersionFactory[] FACTORIES = {
        new M3Version1243Factory(), new M3Version1250Factory(), new M3Version1310Factory(), new M3Version1401Factory(), new M3Version1412Factory(), new M3Version1420Factory(), new M3Version1510Factory()};

    public static String supportedVersions() {
        StringBuffer string = new StringBuffer();
        for (int i = 0; i < FACTORIES.length; i++) {
            if (i > 0) {
                string.append(", ");
            }
            string.append(FACTORIES[i].getVersion());
        }
        return string.toString();
    }
}
