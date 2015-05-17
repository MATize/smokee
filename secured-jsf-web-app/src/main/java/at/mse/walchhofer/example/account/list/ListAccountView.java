package at.mse.walchhofer.example.account.list;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import at.mount.matize.jsf.UrlParamStore;
import at.mse.walchhofer.example.account.AccountView;
import at.mse.walchhofer.example.account.IAccountView;
import at.mse.walchhofer.example.services.IUserManagementService;
import at.mse.walchhofer.smokee.api.SmokeValue;
import at.mse.walchhofer.smokee.api.SmokeValue.SmokeValueType;
import at.mse.walchhofer.smokee.api.SmokeTest;

@Model
public class ListAccountView extends AccountView implements IListAccountView {

    private List<IAccountView> accounts;

    public static final String VIEW_ID = "listAccounts";

    @Inject
    FacesContext facesContext;

    @Inject
    UrlParamStore urlParamStore;

    @EJB
    IUserManagementService userManagement;

    private ListAccountController accountController;

    @PostConstruct
    public void init() {
        this.setAccountController(new ListAccountController(userManagement,
                this));
        super.setFacesContext(facesContext);
        urlParamStore.disableUrlParams();
        this.getAccountController().listBenutzer();
    }

    @Override
    public List<IAccountView> getAccounts() {
        return accounts;
    }

    @Override
    public void setAccounts(List<IAccountView> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String getBaseClientId() {
        return "" + formId();
    }

    @Override
    public String formId() {
        return VIEW_ID;
    }

    @Override
    public String viewName() {
        return VIEW_ID;
    }

    public ListAccountController getAccountController() {
        return accountController;
    }

    public void setAccountController(ListAccountController accountController) {
        this.accountController = accountController;
    }

    @SmokeTest(expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), enabled = true)
    private boolean testInit() {
        // da init @PostConstruct ist wurde sie zum testzeitpunkt bereits
        // aufgerufen
        // dies ermöglicht das ergebnis direkt zu ueberpruefen ohne einen
        // expliziten aufruf der methode durchzuführen
        // accounts muss gesetzt sein und mindesten ein account enthalten sein.
        // dadurch kann auch sicher gestellt werden, dass die
        // datenbankverbindung korrekt konfiguriert ist.
        return this.getAccounts() != null && this.getAccounts().size() > 0;
    }

}
