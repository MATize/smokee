package at.mse.walchhofer.smokee.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;

import at.mse.walchhofer.utilities.javassist.AnnotationScanner;

public class FileUtils {

    public static File getFileForUrl(URL url) {
        try {
            final boolean isVfs = "vfs".equals(url.getProtocol());

            if ("file".equals(url.getProtocol()) || isVfs) {
                final File dir = AnnotationScanner.toFile(url);
                if (isVfs) {
                    return new File(dir.getPath());
                } else {
                    throw new IllegalArgumentException("Not a recognized file URL: " + url);
                }
            } else {
                // Resource in Jar File
                File jarFile = AnnotationScanner.toFile(((JarURLConnection) url.openConnection()).getJarFileURL());
                if (jarFile.isFile()) {
                    return jarFile;
                } else {
                    throw new IllegalArgumentException("Not a File: " + jarFile);
                }
            }

        } catch (IOException e) {
            return null;
        }
    }

}
