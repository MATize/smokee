package at.mse.walchhofer.utilities.files;

import java.io.File;
import java.io.FilenameFilter;

public class ClassFilenameFilter implements FilenameFilter {

    public static final String CLASS_EXTENSION = ".class";

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(CLASS_EXTENSION);
    }

}
