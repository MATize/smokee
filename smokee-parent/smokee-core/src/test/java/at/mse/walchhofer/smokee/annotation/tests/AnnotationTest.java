package at.mse.walchhofer.smokee.annotation.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import at.mse.walchhofer.smokee.annotation.tests.foo.FindMe;
import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.utilities.javassist.AnnotationScanner;

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

	@Test
	public void testReflectionClasses() throws IOException,
			ClassNotFoundException, NoSuchMethodException {
		List<Method> testMethods = new ArrayList<>();
		ClassLoader classLoader = AnnotationTest.class.getClassLoader();
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
		List<Method> expectedMethods = new ArrayList<>();
		expectedMethods.add(AnnotationTest.class
				.getDeclaredMethod("testMethod"));
		expectedMethods.add(FindMe.class.getDeclaredMethod("testMethod"));
		assertTrue(expectedMethods.containsAll(testMethods)
				&& testMethods.containsAll(expectedMethods));

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
		if (listDirectories != null) {
			for (File dir : listDirectories) {
				foundClasses.addAll(getClasses(dir));
			}
		}
		if (listFiles != null) {
			foundClasses.addAll(Arrays.asList(listFiles));
		}
		return foundClasses;
	}
}
