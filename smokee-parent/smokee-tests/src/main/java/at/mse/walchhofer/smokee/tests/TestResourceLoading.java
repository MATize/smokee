package at.mse.walchhofer.smokee.tests;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

public class TestResourceLoading {

    public static void main(String[] args) throws IOException {
        // List<File> candidateDirectories = new ArrayList<>();
        String pkgNameFilter = "";
        String packageName = "at.mse";
        pkgNameFilter = packageName.replace('.', '/');
        if (!pkgNameFilter.endsWith("/")) {
            pkgNameFilter = pkgNameFilter.concat("/");
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resourceEnum = loader.getResources(pkgNameFilter);
        while (resourceEnum.hasMoreElements()) {
            final URL url = resourceEnum.nextElement();
            System.out.println(url.getProtocol());
            System.out.println(url.getPath());
            if ("jar".equals(url.getProtocol().toLowerCase())) {
                listArchiveEntries(url);
            } else {
                File file = new File(url.getFile());
                Arrays.asList(file.list()).stream().forEach(elem -> {
                    System.out.println(elem);
                });
            }
        }
    }

    public static void listArchiveEntries(URL url) throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        String uriString = url.getProtocol() + ":" + url.getPath();
        System.out.println("URI: " + uriString);
        URI uri = URI.create(uriString);
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            // for debugging only
            @SuppressWarnings("unused")
            ZipFile f;
        }
    }
}
