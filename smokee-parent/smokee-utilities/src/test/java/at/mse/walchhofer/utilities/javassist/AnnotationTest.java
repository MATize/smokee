package at.mse.walchhofer.utilities.javassist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.NoSuchClassError;

import org.junit.Test;

import at.mse.tobefound.TestAnnotatedClass;
import at.mse.tobefound.subpkg.TestAnnotatedSubClass;
import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.utilities.files.ClassFilenameFilter;
import at.mse.walchhofer.utilities.files.DirectoryFileFilter;
import at.mse.walchhofer.utilities.reflections.TestClassGenerator;

public class AnnotationTest {

    private int packageIterationen = 0;
    private int classesIterationen = 0;
    private int candiateIterationen = 0;
    private int methodIterationen = 0;

    @Test
    public void testReflection() throws NoSuchMethodException,
            SecurityException {
        List<Method> testMethods = new ArrayList<>();
        Method[] methods = AnnotationTest.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(SmokeTest.class) != null) {
                testMethods.add(method);
            }
        }
        // nothing
        assertEquals(AnnotationTest.class.getDeclaredMethod("testMethod"),
                testMethods.get(0));
    }

    @SmokeTest
    private void testMethod() {

    }

    @Test
    public void testReflections() throws IOException {
        AnnotationScanner scanner = new AnnotationScanner();
        Set<File> allFiles = new HashSet<>();
        Set<File> curFiles = scanner.detect("at");
        for (File file : curFiles) {
            if (!file.isDirectory()) {
                allFiles.add(file);
            } else {
            }
        }
    }

    FileFilter dirFilter = new DirectoryFileFilter();
    FilenameFilter classFilter = new ClassFilenameFilter();

    public List<File> findClasses(File directory) {
        List<File> foundClasses = new ArrayList<>();
        File[] listFiles = directory.listFiles(classFilter);
        File[] listDirectories = directory.listFiles(dirFilter);
        for (File dir : listDirectories) {
            foundClasses.addAll(findClasses(dir));
        }
        foundClasses.addAll(Arrays.asList(listFiles));
        return foundClasses;
    }

    @Test
    public void testClassLoading() throws IOException, ClassNotFoundException {
        List<Class<?>> loadedClasses = new ArrayList<>();
        String rootPkg = "at.mse.tobefound";
        List<File> foundClasses = findClasses(rootPkg);
        for (File file : foundClasses) {
            String rootPkgPath = File.separator
                    + rootPkg.replace('.', File.separatorChar) + File.separator;
            String fqn = file.getPath().substring(
                    file.getPath().indexOf(rootPkgPath) + 1);
            fqn = fqn.substring(0, fqn.length() - 6); // .class entfernen
            fqn = fqn.replace(File.separatorChar, '.');
            loadedClasses.add(Class.forName(fqn));
        }
        assertTrue(loadedClasses.size() == 2);
        assertTrue(loadedClasses.stream().filter(f -> {
            return f.getName().equals(TestAnnotatedClass.class.getName());
        }).findFirst().isPresent());
        assertTrue(loadedClasses.stream().filter(f -> {
            return f.getName().equals(TestAnnotatedSubClass.class.getName());
        }).findFirst().isPresent());
    }

    private List<File> findClasses(String pkg) throws IOException {
        List<File> foundClasses = new ArrayList<>();
        List<File> packageFiles = getTestPackageResources(pkg);
        for (File file : packageFiles) {
            foundClasses.addAll(findClasses(file));
        }
        assertTrue(foundClasses.size() == 2);
        assertTrue(foundClasses
                .stream()
                .filter(f -> {
                    return f.getName()
                            .equals(TestAnnotatedClass.class.getSimpleName()
                                    + ".class");
                }).findFirst().isPresent());
        assertTrue(foundClasses
                .stream()
                .filter(f -> {
                    return f.getName().equals(
                            TestAnnotatedSubClass.class.getSimpleName()
                                    + ".class");
                }).findFirst().isPresent());
        return foundClasses;
    }

    @Test
    public void testFindClasses() throws IOException {
        List<File> foundClasses = new ArrayList<>();
        List<File> packageFiles = getTestPackageResources("at.mse.tobefound");
        for (File file : packageFiles) {
            foundClasses.addAll(findClasses(file));
        }
        assertTrue(foundClasses.size() == 2);
        assertTrue(foundClasses
                .stream()
                .filter(f -> {
                    return f.getName()
                            .equals(TestAnnotatedClass.class.getSimpleName()
                                    + ".class");
                }).findFirst().isPresent());
        assertTrue(foundClasses
                .stream()
                .filter(f -> {
                    return f.getName().equals(
                            TestAnnotatedSubClass.class.getSimpleName()
                                    + ".class");
                }).findFirst().isPresent());
    }

    public List<File> findClasses(File directory, FileFilter dirFilter,
            FilenameFilter classFilter) throws ClassNotFoundException {
        List<File> foundClasses = new ArrayList<>();
        File[] listFiles = directory.listFiles(classFilter);
        File[] listDirectories = directory.listFiles(dirFilter);
        for (File dir : listDirectories) {
            foundClasses.addAll(findClasses(dir, dirFilter, classFilter));
        }
        foundClasses.addAll(Arrays.asList(listFiles));
        return foundClasses;
    }

    public List<File> getTestPackageResources(String packageName)
            throws IOException {
        List<File> candidateDirectories = new ArrayList<>();
        String pkgNameFilter = "";
        pkgNameFilter = packageName.replace('.', '/');
        if (!pkgNameFilter.endsWith("/")) {
            pkgNameFilter = pkgNameFilter.concat("/");
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resourceEnum = loader.getResources(pkgNameFilter);
        while (resourceEnum.hasMoreElements()) {
            final URL url = resourceEnum.nextElement();
            candidateDirectories.add(new File(url.getFile()));
        }
        return candidateDirectories;
    }

    @Test
    public void testPackageScanning() throws IOException {
        List<File> candidateDirectories = new ArrayList<>();
        String pkgNameFilter = "";
        String packageName = "at.mse.tobefound";
        pkgNameFilter = packageName.replace('.', '/');
        if (!pkgNameFilter.endsWith("/")) {
            pkgNameFilter = pkgNameFilter.concat("/");
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resourceEnum = loader.getResources(pkgNameFilter);
        while (resourceEnum.hasMoreElements()) {
            final URL url = resourceEnum.nextElement();
            candidateDirectories.add(new File(url.getFile()));
        }
        assertTrue(candidateDirectories.size() == 1);
        assertEquals("tobefound", candidateDirectories.get(0).getName());
    }

    @Test
    public void testReflectionClasses() throws IOException,
            ClassNotFoundException, NoSuchMethodException,
            CannotCompileException, NotFoundException {
        List<Method> testMethods = new ArrayList<>();
        TestClassGenerator.generate(
                "at.mse.walchhofer.smokee.annotation.tests.TestKlasse", 4, 4,
                SmokeTest.class.getName());
        ClassLoader classLoader = // new
                                  // DynamicClassesLoader(AnnotationTest.class.getClassLoader());
        AnnotationTest.class.getClassLoader();
        // String pkg = "at.mse.walchhofer.smokee.annotation.tests";
        String pkg = "at.mse.walchhofer.smokee.annotation.tests";
        Enumeration<URL> resources = classLoader.getResources(pkg.replace(".",
                "/"));
        List<File> candidateDirectories = new ArrayList<>();
        List<File> candidateFiles = new ArrayList<>();

        while (resources.hasMoreElements()) {
            packageIterationen++;
            URL url = (URL) resources.nextElement();
            candidateDirectories.add(new File(url.getFile()));
            File cur = new File(url.getFile());
            candidateFiles.addAll(getClasses(cur));
        }

        String baseDir = File.separatorChar
                + pkg.replace('.', File.separatorChar) + File.separatorChar;
        for (File clz : candidateFiles) {
            candiateIterationen++;
            // skip dynamic classes
            if (!clz.getName().contains("$")) {
                int startindex = clz.getPath().indexOf(baseDir) + 1;
                String fqcn = clz.getPath().substring(startindex)
                        .replace(".class", "").replace(File.separatorChar, '.');
                System.out.println(fqcn);
                Class<?> curClz = Class.forName(fqcn);
                for (Method method : curClz.getDeclaredMethods()) {
                    methodIterationen++;
                    if (method.getAnnotation(SmokeTest.class) != null) {
                        testMethods.add(method);
                    }
                }

            }
        }

        assertTrue(testMethods
                .stream()
                .allMatch(
                        m -> {
                            return m.getDeclaringClass()
                                    .getName()
                                    .equals("at.mse.walchhofer.smokee.annotation.tests.TestKlasse")
                                    && m.getName().startsWith("testMethod");
                        }));

        System.out.println("Anzahl Durchläufe: ");
        System.out.println("Packages: " + packageIterationen);
        System.out.println("Klassen: " + classesIterationen);
        System.out.println("Kandidaten: " + candiateIterationen);
        System.out.println("Annotationen suche: " + methodIterationen);
    }

    public List<File> getClasses(File directory) throws ClassNotFoundException {
        classesIterationen++;
        List<File> foundClasses = new ArrayList<>();
        File[] listFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null && name.toLowerCase().endsWith(".class")) {
                    return true;
                }
                return false;
            }
        });
        File[] listDirectories = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File dir : listDirectories) {
            foundClasses.addAll(getClasses(dir));
        }
        foundClasses.addAll(Arrays.asList(listFiles));
        return foundClasses;
    }

    @Test
    public void javassistAnnotationScanningTest() {
        try (InputStream fs = this.getClass().getResourceAsStream(
                "/at/mse/walchhofer/utilities/javassist/AnnotationTest.class");
                BufferedInputStream bis = new BufferedInputStream(fs);
                DataInputStream dis = new DataInputStream(bis);) {
            ClassFile cf = new ClassFile(dis);
            @SuppressWarnings("unchecked")
            List<MethodInfo> methodInfoList = cf.getMethods();
            for (MethodInfo methodInfo : methodInfoList) {
                AnnotationsAttribute attrs = (AnnotationsAttribute) methodInfo
                        .getAttribute(AnnotationsAttribute.visibleTag);
                if (attrs == null) {
                    continue;
                }
                javassist.bytecode.annotation.Annotation[] annotations = attrs
                        .getAnnotations();
                for (javassist.bytecode.annotation.Annotation annotation : annotations) {
                    MethodDescriptor methodDesc = JavassistFormater
                            .getMethodDescriptor(cf, methodInfo);
                    Annotation javaAnnotation = (Annotation) annotation
                            .toAnnotationType(this.getClass().getClassLoader(),
                                    ClassPool.getDefault());
                    addToAnnotationMethodStore(methodDesc, javaAnnotation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchClassError e) {
            e.printStackTrace();
        }
    }

    private void addToAnnotationMethodStore(MethodDescriptor methodDescriptor,
            Annotation annotation) {
        Map<Class<? extends Annotation>, Set<MethodDescriptor>> annotationMethodStore = new HashMap<>();
        if (!annotationMethodStore.containsKey(annotation.annotationType())) {
            annotationMethodStore.put(annotation.annotationType(),
                    new HashSet<MethodDescriptor>());
        }
        annotationMethodStore.get(annotation.annotationType()).add(
                methodDescriptor);
    }
}
