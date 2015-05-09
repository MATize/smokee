package at.mse.walchhofer.smokee.tests.control;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TestContext {

	private boolean smokeTest;
	
	private Set<Object> managedTransactions = new HashSet<>();
	
	private boolean preMarkForRollback = false;
	
	private String rollbackBoundaryName;

	public boolean getSmokeTest() {
		return smokeTest;
	}

	public void setSmokeTest(boolean smokeTest) {
		this.smokeTest = smokeTest;
	}

	public boolean isPreMarkForRollback() {
		return preMarkForRollback;
	}

	public void setPreMarkForRollback(boolean preMarkForRollback) {
		this.preMarkForRollback = preMarkForRollback;
	}

	public void setRollbackBoundaryName(String name) {
		this.rollbackBoundaryName = name;
	}

	public String getRollbackBoundaryName() {
		return this.rollbackBoundaryName;
	}

	public Set<Object> getManagedTransactions() {
		return managedTransactions;
	}

	public void setManagedTransactions(Set<Object> managedTransactions) {
		this.managedTransactions = managedTransactions;
	}
}
