package at.mse.walchhofer.smokee.mocks;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class FacesContextMock extends FacesContext {

	private FacesContextMock() {
	}

	private static final Release RELEASE = new Release();

	private static class Release implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			setCurrentInstance(null);
			return null;
		}
	}

	public static FacesContext mockFacesContext() {
		Map<String, Object> session = new HashMap<>();
		FacesContext context = Mockito.mock(FacesContext.class);
		ExternalContext externalCtx = Mockito.mock(ExternalContext.class);
		Mockito.when(externalCtx.getSessionMap()).thenReturn(session);
		Mockito.when(context.getExternalContext()).thenReturn(externalCtx);
		setCurrentInstance(context);
		Mockito.doAnswer(RELEASE).when(context).release();
		return context;
	}

}
