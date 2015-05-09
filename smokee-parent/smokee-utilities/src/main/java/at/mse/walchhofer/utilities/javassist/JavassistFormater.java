package at.mse.walchhofer.utilities.javassist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import at.mse.walchhofer.utilities.strings.StringFormater;

public class JavassistFormater {

	public static MethodDescriptor getMethodDescriptor(ClassFile cls, MethodInfo method) {
		MethodDescriptor methodDesc = new MethodDescriptor();
		methodDesc.setClassName(cls.getName());
		methodDesc.setMethodName(getMethodName(method));
		methodDesc.setParameterNames(getParameterNames(method));
		List<Class<?>> classes = getParameterTypes(getParameterNames(method));
		methodDesc.setParameterTypes(classes.toArray(new Class<?>[0]));
		return methodDesc;
	}
	
	private static Map<String,Class<?>> primitives = null;
	
	public static Map<String,Class<?>> getPrimitives() {
		if(primitives == null) {
			primitives = new HashMap<String,Class<?>>();
			primitives.put("boolean",boolean.class);
			primitives.put("char",char.class);
			primitives.put("byte",byte.class);
			primitives.put("short",short.class);
			primitives.put("int",int.class);
			primitives.put("long",long.class);
			primitives.put("float",float.class);
			primitives.put("double",double.class);
			primitives.put("boolean[]",boolean[].class);
			primitives.put("char[]",char[].class);
			primitives.put("byte[]",byte[].class);
			primitives.put("short[]",short[].class);
			primitives.put("int[]",int[].class);
			primitives.put("long[]",long[].class);
			primitives.put("float[]",float[].class);
			primitives.put("double[]",double[].class);
			primitives.put("void",void.class);
		}
		return primitives;
	}
	
    private static List<Class<?>> getParameterTypes(List<String> parameterNames) {
    	List<Class<?>> classes = new ArrayList<Class<?>>();
    	for (String type : parameterNames) {
    		try {
    			Class<?> x = null;
    			if(getPrimitives().containsKey(type))
    				x = getPrimitives().get(type);
    			else
    				x = Class.forName(type);
    			classes.add(x);
    		} catch(ClassNotFoundException ex) {
    			ex.printStackTrace();
    		}
		}
    	return classes;
	}

	public static String getMethodKey(ClassFile cls, MethodInfo method) {
    	String parameters = StringFormater.listToString(getParameterNames(method),", ");
        return getMethodName(method) + "(" + parameters + ")";
    }

    public static String getMethodFullKey(ClassFile cls, MethodInfo method) {
        return getClassName(cls) + "." + getMethodKey(cls, method);
    }
    
    public static String getClassName(final ClassFile cls) {
        return cls.getName();
    }
    
    public static String getMethodName(final MethodInfo method) {
        return method.getName();
    }
    
    public static List<String> getParameterNames(final MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        return splitDescriptorToTypeNames(descriptor);
    }
    
    private static List<String> splitDescriptorToTypeNames(final String descriptors) {
        List<String> result = new ArrayList<>();

        if (descriptors != null && descriptors.length() != 0) {

            List<Integer> indices = new ArrayList<>();
            Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);
            while (iterator.hasNext()) {
                indices.add(iterator.next());
            }
            indices.add(descriptors.length());

            for (int i = 0; i < indices.size() - 1; i++) {
                String s1 = Descriptor.toString(descriptors.substring(indices.get(i), indices.get(i + 1)));
                result.add(s1);
            }

        }

        return result;
    }
}
