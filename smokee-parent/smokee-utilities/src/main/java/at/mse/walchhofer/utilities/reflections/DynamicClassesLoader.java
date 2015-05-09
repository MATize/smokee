package at.mse.walchhofer.utilities.reflections;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassesLoader extends URLClassLoader {
	
	public DynamicClassesLoader(ClassLoader parentClassLoader) throws MalformedURLException {
			super(new URL[] {new File("target/dynamic-classes/").toURI().toURL()}, parentClassLoader);
	}
}
