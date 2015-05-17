package at.mse.walchhofer.smokee.interception;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import at.mse.walchhofer.smokee.mocks.FacesContextMock;
import at.mse.walchhofer.smokee.tests.control.TestContext;

@SmokEEMockingInterceptorBinding
@Interceptor
public class SmokEEMockingInterceptor {

    @Inject
    TestContext testCtx;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {

        if (testCtx != null && testCtx.getSmokeTest()) {
            if (ic.getMethod().getReturnType().equals(FacesContext.class)) {
                return FacesContextMock.mockFacesContext();
            }
        }
        return ic.proceed();
    }
}
