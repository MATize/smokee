package at.mse.walchhofer.utilities.javassist;

import java.util.Arrays;
import java.util.List;

public class MethodDescriptor {

	private String className;
	private String methodName;
	private List<String> parameterNames;
	private Class<?>[] parameterTypes;

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(List<String> parameterNames) {
		this.parameterNames = parameterNames;
	}

	@Override
	public int hashCode() {
		return className.hashCode() + methodName.hashCode()
				+ parameterNames.hashCode() + parameterTypes.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()) {
			MethodDescriptor other = (MethodDescriptor) obj;
			if (className != null) {
				if (other.className == null)
					return false;
				if (!other.className.equals(className))
					return false;
			} else if (other.className != null) {
				return false;
			}
			if (methodName != null) {
				if (other.methodName == null)
					return false;
				if (!other.methodName.equals(methodName))
					return false;
			} else if (other.methodName != null) {
				return false;
			}
			if (parameterNames != null) {
				if (other.parameterNames == null)
					return false;
				if (other.parameterNames.size() != parameterNames.size())
					return false;
				for (String string : parameterNames) {
					if (!other.parameterNames.contains(string))
						return false;
				}
			} else if (other.parameterNames != null) {
				return false;
			}
			if (parameterTypes != null) {
				if (other.parameterTypes == null)
					return false;
				if (other.parameterTypes.length != other.parameterTypes.length)
					return false;
				List<Class<?>> otherParameterTypesList = Arrays
						.asList(other.parameterTypes);
				for (Class<?> clazz : parameterTypes) {
					if (!otherParameterTypesList.contains(clazz))
						return false;
				}
			} else if (other.parameterTypes != null) {
				return false;
			}
			return true;
		}
		return false;
	}
}
