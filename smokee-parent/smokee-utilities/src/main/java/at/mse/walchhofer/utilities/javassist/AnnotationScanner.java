package at.mse.walchhofer.utilities.javassist;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javassist.ClassPool;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.NoSuchClassError;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import at.mse.walchhofer.utilities.files.ClassFilenameFilter;
import at.mse.walchhofer.utilities.files.DirectoryFileFilter;

public class AnnotationScanner {

	private Set<File> packageDirectories = new HashSet<File>();
	private Set<File> fileCache = new HashSet<File>();
	private boolean scanSubPackages = false;

	private Map<String, HashMap<String, Set<MethodDescriptor>>> annotationMethodNameStore = new HashMap<>();
	private Map<String, Set<Method>> annotationMethodStore = new HashMap<>();
	private Map<Class<?>, List<Class<?>>> annotationClassStore = new HashMap<>();
	private List<?> annotationFilter = null;

	public AnnotationScanner() {
	}

	/**
	 * Initialise AnnotationScanner for given packagenames, Scanning for files
	 * inclusive sub-packages.
	 *
	 * @param packageNames
	 */
	public AnnotationScanner(final String... packageNames) {
		this(true, packageNames);
	}

	public AnnotationScanner(final String packageName) {
		this(true, packageName);
	}

	public AnnotationScanner(boolean scanSubDirs, final String... packageNames) {
		try {
			packageDirectories = this.detect(packageNames);
			this.scanSubPackages = scanSubDirs;
			this.setup(packageDirectories, false);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("keine files gefunden fuer package(s)",
					ex);
		}
	}

	public AnnotationScanner(String pkg2scan, Class<?>... annotations) {
		try {
			this.annotationFilter = new ArrayList<Class<?>>(
					Arrays.asList(annotations));
			packageDirectories = this.detect(pkg2scan);
			this.scanSubPackages = true;
			this.setup(packageDirectories, true);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("keine files gefunden fuer package(s)",
					ex);
		}
	}

	public Set<Method> getMethodsAnnotatedWith(
			Class<? extends Annotation> annotation) {
		return getMethodsAnnotatedWith(annotation.getCanonicalName());
	}

	public Set<Method> buildMethodSetFor(
			HashMap<String, Set<MethodDescriptor>> classMethodMap) {
		Set<Method> methodsFound = new HashSet<>();
		for (Entry<String, Set<MethodDescriptor>> methods : classMethodMap
				.entrySet()) {
			String className = methods.getKey();
			for (MethodDescriptor methodDesc : methods.getValue()) {
				try {
					methodDesc.getMethodName();
					methodDesc.getParameterNames();
					Class<?> c = Class.forName(className);
					Method m = c.getDeclaredMethod(methodDesc.getMethodName(),
							methodDesc.getParameterTypes());
					methodsFound.add(m);
				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException ex) {
					ex.printStackTrace();
				}
			}
		}
		return methodsFound;
	}

	public Set<Method> getMethodsAnnotatedWith(String annotation) {
		if (!annotationMethodStore.containsKey(annotation)) {
			if (annotationMethodNameStore.containsKey(annotation)) {
				Set<Method> methodList = buildMethodSetFor(annotationMethodNameStore
						.get(annotation));
				annotationMethodStore.put(annotation, methodList);
			} else {
				return new HashSet<Method>(0);
			}
		}
		return annotationMethodStore.get(annotation);

	}

	private void indexClassAnnoation(File file) {
		InputStream fs;
		try {
			fs = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(new BufferedInputStream(
					fs));
			ClassFile cf = new ClassFile(dis);
			AnnotationsAttribute visible = (AnnotationsAttribute) cf
					.getAttribute(AnnotationsAttribute.visibleTag);
			if (visible != null) {
				javassist.bytecode.annotation.Annotation[] javassistAnnotations = visible
						.getAnnotations();
				for (javassist.bytecode.annotation.Annotation javassistAnnotation : javassistAnnotations) {
					Annotation annotation = (Annotation) javassistAnnotation
							.toAnnotationType(
									AnnotationScanner.getContextClassLoader(),
									ClassPool.getDefault());
					if (annotationFilter != null
							&& annotationFilter.contains(annotation
									.annotationType())) {
						addToAnnotationClassStore(annotation.annotationType(),
								Class.forName(cf.getName()));
					}
				}
			}
		} catch (IOException | ClassNotFoundException | NoSuchClassError e) {
			e.printStackTrace();
		}
	}

	private void addToAnnotationClassStore(
			Class<? extends Annotation> annotation, Class<?> clazz) {
		if (!annotationClassStore.containsKey(annotation)
				|| annotationClassStore.get(annotation) == null) {
			annotationClassStore.put(annotation, new ArrayList<Class<?>>());
		}
		annotationClassStore.get(annotation).add(clazz);
	}

	private void index(File file, Boolean classAnnotation) {
		if (classAnnotation)
			indexClassAnnoation(file);
		else
			index(file);
	}

	@SuppressWarnings("unchecked")
	private void index(File file) {
		InputStream fs;
		try {
			fs = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(new BufferedInputStream(
					fs));
			ClassFile cf = new ClassFile(dis);
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
					addToMethodNameStore(cf.getName(), methodDesc,
							annotation.getTypeName());
					Annotation javaAnnotation = (Annotation) annotation
							.toAnnotationType(
									AnnotationScanner.getContextClassLoader(),
									ClassPool.getDefault());
					addToAnnotationClassStore(javaAnnotation.annotationType(),
							Class.forName(cf.getName()));
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

	private void addToMethodNameStore(String className,
			MethodDescriptor methodName, String annotationName) {
		if (!annotationMethodNameStore.containsKey(annotationName)
				|| annotationMethodNameStore.get(annotationName) == null) {
			annotationMethodNameStore.put(annotationName,
					new HashMap<String, Set<MethodDescriptor>>());
		}
		if (!annotationMethodNameStore.get(annotationName).containsKey(
				className)
				|| annotationMethodNameStore.get(annotationName).get(className) == null) {
			annotationMethodNameStore.get(annotationName).put(className,
					new HashSet<MethodDescriptor>());
		}
		annotationMethodNameStore.get(annotationName).get(className)
				.add(methodName);

	}

	private void setup(File[] files, Boolean classAnnotation) {
		DirectoryFileFilter dirFilter = new DirectoryFileFilter();
		ClassFilenameFilter classFilter = new ClassFilenameFilter();
		for (int i = 0; i < files.length; i++) {
			File cur = files[i];
			if (cur.isDirectory()) {
				if (scanSubPackages) {
					setup(cur.listFiles(dirFilter), classAnnotation);
				}
				setup(cur.listFiles(classFilter), classAnnotation);
			} else {
				addToFileCache(cur);
				index(cur, classAnnotation);
			}
		}
	}

	private void addToFileCache(File file) {
		if (fileCache == null) {
			fileCache = new HashSet<File>();
		}
		this.fileCache.add(file);
	}

	private void setup(Set<File> files, Boolean classAnnotation) {
		setup(files.toArray(new File[files.size()]), classAnnotation);
	}

	public Set<File> detect(final String... packageNames) throws IOException {
		final String[] pkgNameFilter = new String[packageNames.length];
		for (int i = 0; i < pkgNameFilter.length; ++i) {
			pkgNameFilter[i] = packageNames[i].replace('.', '/');
			if (!pkgNameFilter[i].endsWith("/")) {
				pkgNameFilter[i] = pkgNameFilter[i].concat("/");
			}
		}
		final Set<File> files = new HashSet<File>();
		for (final String packageName : pkgNameFilter) {
			final ClassLoader loader = Thread.currentThread()
					.getContextClassLoader();
			final Enumeration<URL> resourceEnum = loader
					.getResources(packageName);
			while (resourceEnum.hasMoreElements()) {
				final URL url = resourceEnum.nextElement();
				final boolean isVfs = "vfs".equals(url.getProtocol());
//				File dir = toFile(url);
				if ("file".equals(url.getProtocol()) || isVfs) {
					final File dir = toFile(url);
					if (dir.isDirectory()) {
						files.add(dir);
					} else if (isVfs) {
						String archivePath = dir.getPath();
						final int idx = archivePath.contains(".jar") ? archivePath
								.indexOf(".jar") : (archivePath
								.contains(".war") ? archivePath.indexOf(".war")
								: (archivePath.contains(".ear") ? archivePath
										.indexOf(".ear") : -1));
						if (idx > -1) {
							archivePath = archivePath.substring(0, idx + 4);
							final File archiveFile = new File(archivePath);
							if (archiveFile.isFile()) {
								files.add(archiveFile);
							}
						}
					} else {
						throw new AssertionError("Not a recognized file URL: "
								+ url);
					}
				} else {
					// Resource in Jar File
					final File jarFile = toFile(((JarURLConnection) url
							.openConnection()).getJarFileURL());
					if (jarFile.isFile()) {
						files.add(jarFile);
					} else {
						throw new AssertionError("Not a File: " + jarFile);
					}
				}

			}
		}
		return files;
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	private static File vfsFileToFile(final URL url) {
		try {
			VirtualFile content = null;
			if ("vfs".equals(url.getProtocol())) {
				content = (VirtualFile) url.openConnection().getContent();
			} else if ("file".equals(url.getProtocol())) {
				content = VFS.getChild(url.toURI());
			}
			File physicalFile = (File) content.getPhysicalFile();
			String name = content.getName();
			File file = new File(physicalFile.getParentFile(), name);
			if (!file.exists() || !file.canRead()) {
				file = physicalFile;
			}
			return file;
		} catch (Throwable e) {
			throw new RuntimeException("could not open url as VirtualFile ["
					+ url + "]", e);
		}
	}

	public static File toFile(final URL url) throws MalformedURLException {
		try {
			File file = null;
			switch (url.getProtocol()) {
			case "vfs":
				file = AnnotationScanner.vfsFileToFile(url);
				break;
			case "file":
				file = AnnotationScanner.vfsFileToFile(url);
				break;
			case "jar":
				file = AnnotationScanner.vfsFileToFile(url);
				break;
			default:
				file = new File(url.toURI());
				break;
			}

			return file;
		} catch (URISyntaxException ex) {
			throw new MalformedURLException(ex.getMessage());
		}
	}

	public List<Class<?>> getClassesAnnotatedWith(
			Class<? extends Annotation> annotation) {
		return annotationClassStore.get(annotation);
	}

	public Set<Class<?>> getIndexedClasses() {
		Set<Class<?>> classes = new HashSet<>();
		for (List<Class<?>> clazzList : annotationClassStore.values()) {
			classes.addAll(clazzList);
		}
		return classes;
	}
}
