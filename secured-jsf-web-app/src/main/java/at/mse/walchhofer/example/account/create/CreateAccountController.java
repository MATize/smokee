package at.mse.walchhofer.example.account.create;

import java.util.Arrays;

import at.mse.walchhofer.example.jpa.model.Benutzer;
import at.mse.walchhofer.example.services.IUserManagementService;

public class CreateAccountController {

    private IUserManagementService userManagement;
    private ICreateAccountView accountView;

    public CreateAccountController(IUserManagementService userManagement,
            ICreateAccountView accountView) {
        this.userManagement = userManagement;
        this.accountView = accountView;

    }

    public String create() {
        String email = this.accountView.getEmail();
        String nachname = this.accountView.getNachname();
        String vorname = this.accountView.getVorname();
        char[] password = this.accountView.getPassword();
        char[] confirmPassword = this.accountView.getConfirmPassword();

        if (password == null || !Arrays.equals(password, confirmPassword)) {
            this.accountView.setPasswordValid(false);
            this.accountView.setConfirmPasswordValid(false);
            this.accountView.populateErrorMessage(
                    "Password and confirmed password do not match", "password");
            return this.accountView.createAccountFailed();
        }

        Benutzer b = new Benutzer();
        b.setEmail(email);
        b.setNachname(nachname);
        b.setVorname(vorname);
        b.setPass(password);
        b = userManagement.createBenutzer(b);
        if (b.getId() != null && b.getId() != 0) {
            return this.accountView.createAccountSuccess();
        } else {
            this.accountView
                    .populateErrorMessage("Anlegen des Benutzers fehlgeschlagen.");
            return this.accountView.createAccountFailed();
        }
    }
}
