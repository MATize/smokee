package at.mse.walchhofer.smokee.annotation.tests.foo;

import at.mse.walchhofer.smokee.api.SmokeTest;

public class FindMe {

    @SuppressWarnings("unused")
    private void testMethod(String x) {

    }

    @SmokeTest
    private void testMethod() {

    }

}
