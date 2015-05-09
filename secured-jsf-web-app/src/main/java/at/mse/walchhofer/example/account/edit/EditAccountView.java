package at.mse.walchhofer.example.account.edit;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import at.mount.matize.jsf.UrlParamStore;
import at.mse.walchhofer.example.account.AccountView;
import at.mse.walchhofer.example.account.list.ListAccountView;
import at.mse.walchhofer.example.services.IUserManagementService;

@Model
public class EditAccountView extends AccountView implements IEditAccountView,
Serializable {

	private static final String URL_PARAM_ID = "accountid";

	public static final String VIEW_ID="editAccount";

	private static final long serialVersionUID = 1L;

	@NotNull
	private char[] confirmPassword;

	private Boolean emailValid = true;
	private Boolean vornameValid = true;
	private Boolean nachnameValid = true;
	private Boolean passwordValid = true;
	private Boolean confirmPasswordValid = true;

	@Inject
	FacesContext facesContext;

	@EJB
	IUserManagementService userManagement;

	@Inject
	UrlParamStore urlParamStore;

	private EditAccountController accountController;

	private String nextNavigationTarget;

	@PostConstruct
	public void init() {
		this.setAccountController(new EditAccountController(userManagement,
				this));
		super.setFacesContext(facesContext);
	}

	public void edit() {
		if (!facesContext.isPostback()) {
			this.getAccountController().startEdit();
		}
	}

	public String save() {
		this.getAccountController().saveAccount();
		return nextNavigationTarget;
	}
	
	public String delete(Long id) {
		this.setId(id);
		this.getAccountController().deleteAccount();
		return nextNavigationTarget;
	}


	@Override
	public void editAccountFailed() {
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		this.nextNavigationTarget = this.viewName() + ".xhtml?faces-redirect=true";
	}

	@Override
	public void editAccountSuccess() {
		urlParamStore.pushParamsToIgnore(facesContext, getUrlParamId());
		this.nextNavigationTarget = ListAccountView.VIEW_ID+".xhtml?faces-redirect=true";
	}
	
	@Override
	public void deleteAccountSuccess() {
		urlParamStore.disableUrlParams();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		this.nextNavigationTarget = ListAccountView.VIEW_ID+".xhtml?faces-redirect=true";
	}
	
	@Override
	public void deleteAccountFailed() {
		urlParamStore.disableUrlParams();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		this.nextNavigationTarget = facesContext.getViewRoot().getViewId() + "?faces-redirect=true".replace("/views/","");
	}

	@Override
	public String viewName() {
		return VIEW_ID;
	}

	@Override
	public String formId() {
		return "editBenutzerForm";
	}

	@Override
	public String editForm() {
		return viewName() + ".xhtml?faces-redirect=true";
	}

	@Override
	public String getBaseClientId() {
		return "" + formId();
	}

	@Override
	protected String getClientIdForField(String field) {
		return this.getBaseClientId() + ":" + field;
	}

	@Override
	public char[] getConfirmPassword() {
		return confirmPassword;
	}

	@Override
	public void setConfirmPassword(char[] confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public EditAccountController getAccountController() {
		return accountController;
	}

	public void setAccountController(EditAccountController accountController) {
		this.accountController = accountController;
	}

	@Override
	public void setEmailValid(Boolean valid) {
		this.emailValid = valid;
	}

	@Override
	public void setVornameValid(Boolean valid) {
		this.vornameValid = valid;
	}

	@Override
	public void setNachnameValid(Boolean valid) {
		this.nachnameValid = valid;
	}

	@Override
	public void setPasswordValid(Boolean valid) {
		this.passwordValid = valid;
	}

	@Override
	public void setConfirmPasswordValid(Boolean valid) {
		this.confirmPasswordValid = valid;
	}

	public Boolean getEmailValid() {
		return emailValid;
	}

	public Boolean getVornameValid() {
		return vornameValid;
	}

	public Boolean getNachnameValid() {
		return nachnameValid;
	}

	public Boolean getPasswordValid() {
		return passwordValid;
	}

	public Boolean getConfirmPasswordValid() {
		return confirmPasswordValid;
	}

	public static String getUrlParamId() {
		return URL_PARAM_ID;
	}

	@Override
	public void editAccountMissingParameter() {
		//		urlParamStore.pushParamsToIgnore(facesContext, getUrlParamId());
		urlParamStore.disableUrlParams();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		this.navigateToView(ListAccountView.VIEW_ID);
	}

}
