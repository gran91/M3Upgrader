package mak.core.movex;

import com.intentia.mak.core.m3.M3VersionFactory;
import com.intentia.mak.core.movex.ConfigurationProperties;
import com.intentia.mak.core.movex.EnvironmentProperties;
import com.intentia.mak.core.movex.MovexSystem;
import com.intentia.mak.core.movex.ui.MovexUISiteList;
import com.intentia.mak.util.MAKException;
import org.apache.log4j.Logger;

public final class MovexSystemFactory {

    private static final Logger LOGGER = Logger.getLogger(MovexSystemFactory.class);

    public static MovexSystem newMovexSystem(EnvironmentProperties ep, ConfigurationProperties cp, MovexUISiteList uiSites)
            throws MAKException {
        try {
            cp.setEnvironmentId(ep.getId());
            M3VersionFactory[] supportedVersionFactories = M3Versions.FACTORIES;
            for (int i = 0; i < supportedVersionFactories.length; i++) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Creating a M3 system instance for " + cp.getId());
                }
                M3VersionFactory factory = supportedVersionFactories[i];
                MovexSystem system = factory.create(ep, cp, uiSites);
                if (system != null) {
                    return system;
                }
            }
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Supported versions are: " + M3Versions.supportedVersions());
            errMsg.append(". Failed to find an appropriate M3 system matching the supplied description");
            errMsg.append(ep.dump());
            errMsg.append(cp.dump());
            throw new MAKException(errMsg.toString());
        } catch (MAKException e) {
            throw new MAKException("M3 system identification failed", e);
        }

    }
}
