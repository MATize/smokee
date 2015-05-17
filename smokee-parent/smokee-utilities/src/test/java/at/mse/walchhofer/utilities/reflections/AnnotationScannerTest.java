package at.mse.walchhofer.utilities.reflections;

import java.util.Set;

import org.junit.Test;

import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.utilities.javassist.MethodDescriptor;

public class AnnotationScannerTest {

    @Test
    public void testIterationCount() {
        AnnotationScanner scanner = new AnnotationScanner("at.mse.tobefound");
        Set<MethodDescriptor> methodsAnnotatedWith = scanner.getMethodsAnnotatedWith(SmokeTest.class);
        System.out.println(methodsAnnotatedWith.size() + " Tests found!");
    }
}