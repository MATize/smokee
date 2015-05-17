package at.mse.walchhofer.example.account.create;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import at.mse.walchhofer.example.account.AccountView;
import at.mse.walchhofer.example.account.list.ListAccountView;
import at.mse.walchhofer.example.services.IUserManagementService;
import at.mse.walchhofer.smokee.api.SmokeValue;
import at.mse.walchhofer.smokee.api.SmokeValue.SmokeValueType;
import at.mse.walchhofer.smokee.api.SmokeTest;

@Model
public class CreateAccountView extends AccountView implements
        ICreateAccountView {

    public static final String VIEW_ID = "createAccount";

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

    private CreateAccountController accountController;

    @PostConstruct
    public void init() {
        this.setAccountController(new CreateAccountController(userManagement,
                this));
        super.setFacesContext(facesContext);
    }

    public String createAccount() {
        return this.getAccountController().create();
    }

    @Override
    public String createAccountFailed() {
        return this.viewName() + ".xhtml";
    }

    @Override
    public String createAccountSuccess() {
        return ListAccountView.VIEW_ID + ".xhtml?faces-redirect=true";
    }

    @Override
    public String viewName() {
        return VIEW_ID;
    }

    @Override
    public String formId() {
        return "createBenutzerForm";
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

    public CreateAccountController getAccountController() {
        return accountController;
    }

    public void setAccountController(CreateAccountController accountController) {
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

    @SmokeTest(expectedResult = @SmokeValue(value = "true", type = SmokeValueType.BOOLEAN), rollback = true, enabled = true)
    private boolean testCreate() {
        this.init();
        this.setVorname("Matthias");
        this.setNachname("Walchhofer");
        this.setEmail("matthias@walchhofer.co.at");
        this.setPassword("password".toCharArray());
        this.setConfirmPassword(this.getPassword());
        String result = this.createAccount();
        return result != null && result.equals(this.createAccountSuccess());
    }

    @SmokeTest(expectedResult = @SmokeValue(value = "true", type = SmokeValueType.BOOLEAN), rollback = true, enabled = true)
    private boolean testCreateFail() {
        this.init();
        this.setVorname("Matthias");
        this.setNachname("Walchhofer");
        this.setEmail("matthias@walchhofer.co.at");
        this.setPassword("password".toCharArray());
        this.setConfirmPassword("password_falsch".toCharArray());
        String result = this.createAccount();
        return result != null && result.equals(this.createAccountFailed());
    }

}
