package at.mse.walchhofer.smokee.interception;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;

import at.mse.walchhofer.smokee.tests.control.TestContext;

@SmokEETransactionInterceptorBinding
@Interceptor
public class SmokEETransactionInterceptor {

    @Inject
    TestContext testCtx;

    @Resource(mappedName = "java:comp/TransactionSynchronizationRegistry")
    TransactionSynchronizationRegistry transactionRegistry;

    @Resource(mappedName = "java:comp/EJBContext")
    EJBContext ejbContext;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {

        if (testCtx != null && testCtx.getSmokeTest()) {

            Object transactionKey = transactionRegistry.getTransactionKey();
            boolean rollback = testCtx.getManagedTransactions().add(
                    transactionKey);
            Object result = ic.proceed();
            if (rollback) {
                ejbContext.setRollbackOnly();
                testCtx.getManagedTransactions().remove(transactionKey);
            }
            return result;
        } else {
            return ic.proceed();
        }
    }
}
