package at.mse.walchhofer.example.account.list;

import java.util.ArrayList;
import java.util.List;

import at.mse.walchhofer.example.account.AccountView;
import at.mse.walchhofer.example.account.IAccountView;
import at.mse.walchhofer.example.jpa.model.Benutzer;
import at.mse.walchhofer.example.services.IUserManagementService;

public class ListAccountController {
    private IUserManagementService userManagement;
    private IListAccountView accountView;

    public ListAccountController(IUserManagementService userManagement,
            IListAccountView accountView) {
        this.userManagement = userManagement;
        this.accountView = accountView;

    }

    public void listBenutzer() {
        List<IAccountView> list = new ArrayList<>();
        for (Benutzer b : userManagement.getBenutzerListe()) {
            IAccountView account = new AccountView();
            account.setId(b.getId());
            account.setEmail(b.getEmail());
            account.setNachname(b.getNachname());
            // never ever expose passwords
            account.setPassword(null);
            account.setVorname(b.getVorname());
            list.add(account);
        }
        this.accountView.setAccounts(list);
    }

}
