package at.mse.walchhofer.utilities.javassist;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;

import javax.enterprise.util.AnnotationLiteral;

public class JavassistHelper {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> addAnnotation(Class<T> clazz,
            AnnotationLiteral<?> annotation, String newName) {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass cc;
        try {
            cc = cp.getAndRename(clazz.getName(), newName);
            ClassFile cf = cc.getClassFile();
            ConstPool constPool = cf.getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
                    AnnotationsAttribute.visibleTag);
            Annotation annotationInterceptors = new Annotation("javax.interceptor.Interceptors", constPool);
            ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
            arrayMemberValue.setValue(new ClassMemberValue[] { new ClassMemberValue("at.mse.walchhofer.smokee.transaction.RollbackInterceptor", constPool) });
            annotationInterceptors.addMemberValue("value", arrayMemberValue);
            attr.addAnnotation(annotationInterceptors);
            Annotation annotationAlternative = new Annotation("javax.enterprise.inject.Alternative", constPool);
            attr.addAnnotation(annotationAlternative);
            cf.addAttribute(attr);
            cc.writeFile();
            return (Class<T>) cc.toClass(Thread.currentThread().getContextClassLoader(), JavassistHelper.class.getProtectionDomain());
        } catch (NotFoundException | IOException | CannotCompileException e) {
            e.printStackTrace();
            return null;
        }
    }

}
