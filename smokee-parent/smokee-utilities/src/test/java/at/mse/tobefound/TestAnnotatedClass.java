package at.mse.tobefound;

import at.mse.walchhofer.smokee.api.SmokeTest;

public class TestAnnotatedClass {

    @SmokeTest
    private void testA() {

    }

    @SmokeTest
    void testB() {

    }

    protected void testC() {
        // no annotation present
    }

}
