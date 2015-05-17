package at.mse.walchhofer.smokee.tests;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.junit.Test;

public class ZipFileReaderTest {

    private static final String WAR_PREFIX = "/WEB-INF/classes/";
    private static final String JAR_IN_WAR_PREFIX = "/WEB-INF/lib/";

    // @Test()
    public void testJarFile() throws ZipException, IOException {
        File jarFile = new File("target/smokee-tests-0.2.1.jar");
        URL url = jarFile.toURI().toURL();
        System.out.println(url.toString());
        ZipFile archive = new ZipFile(jarFile);
        Arrays.asList(archive).stream().forEach(elem -> {
            System.out.println(elem);
        });
    }

    // @Test
    // public void testJarFileSystem() throws IOException {
    // File jarFile = new File("target/smokee-tests-0.2.1.jar");
    // URL url = jarFile.toURI().toURL();
    // Map<String, String> env = new HashMap<>();
    // String uriString = "jar:"+url.getProtocol()+":"+url.getPath();
    // System.out.println("URI: " + uriString);
    // URI uri = URI.create(uriString);
    // try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
    // PathMatcher pathMatcher = fs.getPathMatcher("glob:*.class");
    // Files.walkFileTree(fs.getPath("at/mse/walchhofer/smokee/tests/"), new
    // SimpleFileVisitor<Path>() {
    // @Override
    // public FileVisitResult visitFile(Path file, BasicFileAttributes attribs)
    // {
    // Path name = file.getFileName();
    // if (pathMatcher.matches(name)) {
    // System.out.print(String.format("Found matched file: '%s'.%n", file));
    // }
    // return FileVisitResult.CONTINUE;
    // }
    // });
    // }
    // }
    //
    @Test
    public void testWarFileSystem() throws IOException {
        File warFile = new File("src/main/resources/secured-jsf-web-app.war");
        URL url = warFile.toURI().toURL();
        Map<String, String> env = new HashMap<>();
        String uriString = "jar:" + url.getProtocol() + ":" + url.getPath();
        System.out.println("URI: " + uriString);
        URI uri = URI.create(uriString);
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            PathMatcher pathMatcher = fs.getPathMatcher("glob:*.class");
            Files.walkFileTree(fs.getPath(WAR_PREFIX + "at/mse/walchhofer/"), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                    Path name = file.getFileName();
                    if (pathMatcher.matches(name)) {
                        System.out.print(String.format("Found matched file: '%s'.%n", file));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Test
    public void testJarInWarFileSystem() throws IOException {
        File warFile = new File("src/main/resources/secured-jsf-web-app.war");
        URL url = warFile.toURI().toURL();
        Map<String, String> env = new HashMap<>();
        String uriString = "jar:" + url.getProtocol() + ":" + url.getPath();
        System.out.println("URI: " + uriString);
        URI uri = URI.create(uriString);
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            PathMatcher pathMatcher = fs.getPathMatcher("glob:*.jar");
            Files.walkFileTree(fs.getPath(JAR_IN_WAR_PREFIX), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                    Path name = file.getFileName();
                    if (pathMatcher.matches(name)) {
                        System.out.print(String.format("Found matched file: '%s'.%n", file));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

}
