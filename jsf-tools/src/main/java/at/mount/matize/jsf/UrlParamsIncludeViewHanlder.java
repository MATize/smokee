package at.mount.matize.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class UrlParamsIncludeViewHanlder extends ViewHandlerWrapper {
	ViewHandler wrapped;

	@Inject
	UrlParamStore urlParamStore;

	public UrlParamsIncludeViewHanlder(ViewHandler viewHandler) {
		this.wrapped = viewHandler;
	}

	private UrlParamStore getUrlParamStore() {
		if (urlParamStore == null) {
			urlParamStore = getBean(UrlParamStore.class);
		}
		return urlParamStore;
	}

	private BeanManager getBeanManager() {
		try {
			return (BeanManager) new InitialContext()
			.lookup("java:comp/BeanManager");

		} catch (NamingException e) {
			return null;
		}
	}

	private <T> T getBean(Class<T> beanClass) {
		BeanManager beanManager = getBeanManager();
		if (beanManager != null) {
			Set<Bean<?>> beans = beanManager.getBeans(beanClass);
			if (beans != null && !beans.isEmpty()) {
				Bean<T> bean = (Bean<T>) beanManager.getBeans(beanClass)
						.iterator().next();
				CreationalContext<T> ctx = beanManager
						.createCreationalContext(bean);
				return (T) beanManager.getReference(bean, beanClass, ctx);
			}
		}
		return null;
	}

	@Override
	public String getActionURL(javax.faces.context.FacesContext context,
			String viewId) {
		String originalActionURL = super.getActionURL(context, viewId);
		String newActionURL = includeViewParamsIfNecessary(context,
				originalActionURL);
		return newActionURL;
	}

	private String includeViewParamsIfNecessary(FacesContext context,
			String originalActionURL) {
		String parameterString = "";

		if (!getUrlParamStore().pullDisableUrlParamsSwitch()) {

			List<String> ignoreParmasMarker = new ArrayList<>();
			if (getUrlParamStore() != null) {
				ignoreParmasMarker.addAll(getUrlParamStore()
						.pullParamsToIgnore(context));
			}
			for (String name : context.getExternalContext()
					.getRequestParameterMap().keySet()) {
				UIComponent component = context.getViewRoot().findComponent(
						name);
				if (component != null || "javax.faces.ViewState".equals(name)) {
					ignoreParmasMarker.add(name);
				}
			}

			for (String key : context.getExternalContext()
					.getRequestParameterValuesMap().keySet()) {
				if (!ignoreParmasMarker.contains(key)) {
					if ("".equals(parameterString)) {
						parameterString = "?";
					} else {
						parameterString += "&";
					}
					String keyVal = key
							+ "="
							+ context.getExternalContext()
							.getRequestParameterMap().get(key);
					parameterString += keyVal;
				}
			}
		}

		return originalActionURL + parameterString;
	}

	@Override
	public ViewHandler getWrapped() {
		return wrapped;
	}

}
