package at.mse.walchhofer.utilities.reflections;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.NoSuchClassError;

import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileFilter;

import at.mse.walchhofer.utilities.javassist.JavassistFormater;
import at.mse.walchhofer.utilities.javassist.MethodDescriptor;

public class AnnotationScanner {

	private boolean scanSubPackages = false;
	private Map<Class<? extends Annotation>, Set<MethodDescriptor>> annotationMethodStore = new HashMap<>();
	private Map<Class<? extends Annotation>, Set<String>> annotationClassStore = new HashMap<>();
	private Map<Class<? extends Annotation>, Set<Method>> annotationMethodInstanceStore = new HashMap<>();
	static Logger LOG = Logger.getLogger(AnnotationScanner.class.getName());

	public AnnotationScanner() {
	}

	/**
	 * Initialize AnnotationScanner for given packagenames, Scanning for files
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

	public AnnotationScanner(boolean scanSubDirs, String... packageNames) {
		try {
			this.scanSubPackages = true;
			this.detectClasses(packageNames);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("keine files gefunden fuer package(s)",
					ex);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Fehlerhafte URI beim Scanning", e);
		}
	}

	public Set<Method> getMethodInstancesByAnnotation(
			Class<? extends Annotation> annotation) {
		if (!annotationMethodInstanceStore.containsKey(annotation)) {
			HashSet<Method> methods = new HashSet<Method>();
			if (annotationMethodStore.containsKey(annotation)) {
				for (MethodDescriptor methodDescriptor : annotationMethodStore
						.get(annotation)) {
					Class<?> clazz = null;
					try {
						clazz = Class.forName(methodDescriptor.getClassName());
						if (clazz != null)
							methods.add(clazz.getDeclaredMethod(
									methodDescriptor.getMethodName(),
									methodDescriptor.getParameterTypes()));
					} catch (ClassNotFoundException ex) {
						LOG.log(Level.WARNING, "cant load class named "
								+ methodDescriptor.getClassName(), ex);
					} catch (NoSuchMethodException | SecurityException ex) {
						LOG.log(Level.WARNING,
								"cant load method named "
										+ methodDescriptor.getMethodName()
										+ " from class named "
										+ methodDescriptor.getClassName(), ex);
					} finally {
						clazz = null;
					}
				}

			}
			annotationMethodInstanceStore.put(annotation, methods);
		}
		return annotationMethodInstanceStore.get(annotation);
	}

	public Set<MethodDescriptor> getMethodsAnnotatedWith(
			Class<? extends Annotation> annotation) {
		if (annotationMethodStore.containsKey(annotation)) {
			return annotationMethodStore.get(annotation);
		} else {
			return new HashSet<MethodDescriptor>();
		}
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public List<String> getClassesAnnotatedWith(
			Class<? extends Annotation> annotation) {
		return new ArrayList<String>(annotationClassStore.get(annotation));
	}

	public void detectClasses(String... packageNames) throws IOException,
			URISyntaxException {
		VirtualFileFilter virtualClassFileFilter = new VirtualFileFilter() {
			@Override
			public boolean accepts(VirtualFile file) {
				return file.getName().endsWith(".class");
			}
		};

		final String[] pkgNameFilter = new String[packageNames.length];
		for (int i = 0; i < pkgNameFilter.length; ++i) {
			pkgNameFilter[i] = packageNames[i].replace('.', '/');
			if (!pkgNameFilter[i].endsWith("/")) {
				pkgNameFilter[i] = pkgNameFilter[i].concat("/");
			}
		}
		for (final String packageName : pkgNameFilter) {
			final Enumeration<URL> resourceEnum = getContextClassLoader()
					.getResources(packageName);
			while (resourceEnum.hasMoreElements()) {
				VirtualFile virtualFile;
				Closeable handle = null;
				try {
					final URL url = resourceEnum.nextElement();
					String protocol = url.getProtocol();
					// oeffnen des Files als virtualfile
					if ("vfs".equals(protocol)) {
						URLConnection connection = url.openConnection();
						virtualFile = (VirtualFile) connection.getContent();
					} else if ("file".equals(protocol)) {
						virtualFile = VFS.getChild(url.toURI());
						if (!virtualFile.isDirectory()) {
							File archiveFile = virtualFile.getPhysicalFile();
							TempFileProvider provider = TempFileProvider
									.create("tmp",
											Executors.newScheduledThreadPool(2));
							handle = VFS.mountZip(archiveFile, virtualFile,
									provider);
						}
					} else if ("jar".equals(protocol)) {
						String uri = url.getPath().substring(0,
								url.getPath().indexOf(".jar!") + 4);
						uri = uri.replace("file:/", "");
						File jarFile = new File(uri);
						String jarUri = uri.replace(".jar!", ".jar");
						VirtualFile virtualJarFile = VFS.getChild(uri);
						TempFileProvider provider = TempFileProvider.create(
								"tmp", Executors.newScheduledThreadPool(2));
						handle = VFS
								.mountZip(jarFile, virtualJarFile, provider);
						virtualFile = virtualJarFile.getChild(jarUri);
					} else {
						// todo
						throw new UnsupportedOperationException();
					}

					// files in package auslesen
					List<VirtualFile> children = null;
					// sub packages beruecksichtigen
					if (scanSubPackages) {
						children = virtualFile
								.getChildrenRecursively(virtualClassFileFilter);
					} else {
						children = virtualFile
								.getChildren(virtualClassFileFilter);
					}
					// aus filenamen fully qualified names fuer klassen erzeugen
					// und speichern
					for (VirtualFile childFile : children) {
						// String relativePath = childFile
						// .getPathNameRelativeTo(virtualFile);
						// relativePath = relativePath.substring(0,
						// relativePath.length() - 6);
						// relativePath = packageName + relativePath;
						// String fqn = relativePath.replace("/", ".");
						setupClassAnnotation(childFile);
					}
				} finally {
					if (handle != null) {
						handle.close();
					}
				}
			}
		}
	}

	private void setupClassAnnotation(VirtualFile vFile) {
		try (InputStream fs = vFile.openStream();
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
							.toAnnotationType(
									AnnotationScanner.getContextClassLoader(),
									ClassPool.getDefault());
					if (!annotationClassStore.containsKey(javaAnnotation
							.annotationType())) {
						annotationClassStore.put(
								javaAnnotation.annotationType(),
								new HashSet<String>());
					}
					annotationClassStore.get(javaAnnotation.annotationType())
							.add(methodDesc.getClassName());
					if (!annotationMethodStore.containsKey(javaAnnotation
							.annotationType())) {
						annotationMethodStore.put(
								javaAnnotation.annotationType(),
								new HashSet<MethodDescriptor>());
					}
					annotationMethodStore.get(javaAnnotation.annotationType())
							.add(methodDesc);
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

}
