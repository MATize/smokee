package at.mse.walchhofer.smokee.interception;

import javax.ejb.EJBContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;

import at.mse.walchhofer.smokee.mocks.FacesContextMock;
import at.mse.walchhofer.smokee.tests.control.TestContext;

@SmokEEInterceptorBinding
@Interceptor
public class SmokEEInterceptor {

	@Inject
	TestContext testCtx;

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {

		if (testCtx != null && testCtx.getSmokeTest()) {
			InitialContext initialContext = new javax.naming.InitialContext();
			Object result = null;
			if (ic.getMethod().getReturnType().equals(FacesContext.class)) {
				result = FacesContextMock.mockFacesContext();
			} else {
				Object transactionKey = ((javax.transaction.TransactionSynchronizationRegistry) initialContext
						.lookup("java:comp/TransactionSynchronizationRegistry"))
						.getTransactionKey();
				boolean rollback = testCtx.getManagedTransactions().add(
						transactionKey);
				result = ic.proceed();
				if (rollback) {
					EJBContext context = (EJBContext) initialContext
							.lookup("java:comp/EJBContext");
					context.setRollbackOnly();
					testCtx.getManagedTransactions().remove(transactionKey);
				}
			}
			return result;
		} else {
			return ic.proceed();
		}
	}
}
