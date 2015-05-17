package at.mse.walchhofer.smokee.tests.control;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TestContext {

    private boolean smokeTest;

    private Set<Object> managedTransactions = new HashSet<>();

    public boolean getSmokeTest() {
        return smokeTest;
    }

    public void setSmokeTest(boolean smokeTest) {
        this.smokeTest = smokeTest;
    }

    public Set<Object> getManagedTransactions() {
        return managedTransactions;
    }

}
