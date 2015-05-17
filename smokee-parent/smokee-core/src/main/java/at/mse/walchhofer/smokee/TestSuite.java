package at.mse.walchhofer.smokee;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import at.mse.walchhofer.smokee.api.SmokeTest;

@ApplicationScoped
public class TestSuite {

    public TestSuite() {
    }

    private Set<Method> testCases;

    /**
     * Only returning enabled tests
     * 
     * @return
     */
    public Set<Method> getTestCases() {
        return testCases.stream().filter(m -> {
            return m.getAnnotation(SmokeTest.class).enabled();
        }).collect(Collectors.toCollection(HashSet::new));
    }

    public void setTestCases(Set<Method> testCases) {
        this.testCases = testCases;
    }

}
