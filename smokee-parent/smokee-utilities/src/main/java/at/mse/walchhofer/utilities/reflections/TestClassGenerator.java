package at.mse.walchhofer.utilities.reflections;

import java.io.IOException;
import java.util.Random;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

public class TestClassGenerator {

    public static void generate(String className, int methodCnt, int annotationCnt, String annotationFQN) throws CannotCompileException, IOException,
            ClassNotFoundException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(className);
        for (int i = 0; i < methodCnt; i++) {
            cc.addMethod(generateMethod(cc, "testMethod" + i));
        }
        Random rng = new Random();
        for (int j = 0; j < annotationCnt; j++) {
            // get random method
            CtMethod ctMethod = cc.getDeclaredMethods()[rng.nextInt(methodCnt - 1)];
            while (ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag) != null) {
                ctMethod = cc.getMethods()[rng.nextInt(methodCnt - 1)];
            }
            addAnnotation(cc, ctMethod, annotationFQN);
        }
        cc.writeFile("target/classes");
        cc.detach();
    }

    private static void addAnnotation(CtClass cc, CtMethod ctMethod,
            String annotationFQN) {
        ConstPool cp = cc.getClassFile().getConstPool();
        AnnotationsAttribute annoAttr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(annotationFQN, cp);
        annoAttr.addAnnotation(annotation);
        ctMethod.getMethodInfo().addAttribute(annoAttr);
    }

    private static CtMethod generateMethod(CtClass declaring, String methodName)
            throws CannotCompileException {
        StringBuffer sb = new StringBuffer();
        sb.append("private void ").append(methodName).append("() { }");
        return CtMethod.make(sb.toString(), declaring);
    }

}
