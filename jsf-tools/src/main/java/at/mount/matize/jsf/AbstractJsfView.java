package at.mount.matize.jsf;

import java.lang.reflect.Field;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public abstract class AbstractJsfView implements IView {

	private FacesContext facesContext;

	protected abstract String getClientIdForField(String field);

	public void navigateToView(String view) {
		facesContext
		.getApplication()
		.getNavigationHandler()
		.handleNavigation(facesContext, null,
				view + "?faces-redirect=true");
	}

	/**
	 * Checkt ob ein Feld mit uebergebenen Namen in dieser Klasse deklariert
	 * ist. Wird NULL uebergeben wird eben falls <code>TRUE</code> retrourniert.
	 *
	 * @param fields
	 * @return true wenn Feld existiert oder null ist, false wenn nicht present
	 */
	protected boolean fieldsAvailable(String... fields) {
		if (fields != null) {
			for (String field : fields) {
				Class<? extends AbstractJsfView> curClazz = this.getClass();
				Optional<Field> declaredField = getDeclaredField(curClazz, field);
				while (!declaredField.isPresent() && curClazz != AbstractJsfView.class) {
					curClazz = (Class<? extends AbstractJsfView>) curClazz
							.getSuperclass();
					declaredField = getDeclaredField(curClazz,field);
				}
				return declaredField.isPresent();

			}
			return false;
		} else {
			return true;
		}
	}

	private Optional<Field> getDeclaredField(Class<? extends AbstractJsfView> clazz,
			String fieldname) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldname);
		} catch (SecurityException| NoSuchFieldException ex) {
			//nothing
		}

		return Optional.ofNullable(f);
	}

	protected void populateFacesmessage(String clientId, Severity serverty,
			String msg, String detailMsg) {
		this.facesContext.addMessage(clientId, new FacesMessage(serverty, msg,
				detailMsg));
	}

	protected void populateFacesmessageForFields(Severity serverty, String msg,
			String... fields) {
		if (fields != null && fields.length > 0) {
			populateFacesmessageForFields(serverty, msg, null, fields);
		} else {
			populateFacesmessage(null, serverty, msg, null);
		}
	}

	protected void populateFacesmessageForFields(Severity serverty, String msg,
			String detailMsg, String... fields) {
		if (this.fieldsAvailable(fields)) {
			for (String field : fields) {
				populateFacesmessage(this.getClientIdForField(field), serverty,
						msg, detailMsg);
			}
		}
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	@Override
	public void populateWarnMessage(String message, String... fields) {
		populateFacesmessageForFields(FacesMessage.SEVERITY_WARN, message,
				fields);
	}

	@Override
	public void populateErrorMessage(String message, String... fields) {
		populateFacesmessageForFields(FacesMessage.SEVERITY_ERROR, message,
				fields);
	}

	@Override
	public void populateFatalMessage(String message, String... fields) {
		populateFacesmessageForFields(FacesMessage.SEVERITY_FATAL, message,
				fields);
	}

	@Override
	public void populateInfoMessage(String message, String... fields) {
		populateFacesmessageForFields(FacesMessage.SEVERITY_INFO, message,
				fields);
	}
}
