package org.parabot.core.lib.javafx;

import java.io.File;
import java.net.URL;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.build.BuildPath;
import org.parabot.core.lib.Library;
import org.parabot.environment.api.utils.JavaUtil;

/**
 * Jython util class
 *
 * @author Everel
 */
public class Theme extends Library {
    private static boolean valid;
    private final String name = "Theme";

    public static boolean isValid() {
        return valid;
    }

    @Override
    public void init() {
        if (!hasJar()) {
            System.err.println("Failed to load "+name+"... [jar missing]");
            return;
        }
        Core.verbose("Adding "+name+" jar file to build path: "
                + getJarFileURL().getPath());
        BuildPath.add(getJarFileURL());

        try {
            // Test
            Class.forName("org.jvnet.substance.SubstanceBorder");
            valid = true;
        } catch (ClassNotFoundException e) {
            System.err
                    .println("Failed to add "+name+" to build path, or incorrupt download");
        }

        Core.verbose(name+" initialized.");
    }

    @Override
    public boolean isAdded() {
        return valid;
    }

    @Override
    public File getJarFile() {
        return new File(Directories.getCachePath(), name+".jar");
    }

    @Override
    public URL getDownloadLink() {
        try {
            return new URL("file:C:/Users/Jak/Documents/Parabot/other/Theme.jar");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean requiresJar() {
        return true;
    }

    @Override
    public String getLibraryName() {
        return "JavaFX";
    }
}
