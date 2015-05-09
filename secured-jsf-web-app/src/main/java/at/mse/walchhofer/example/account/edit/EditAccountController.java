package at.mse.walchhofer.example.account.edit;

import java.util.Arrays;

import at.mse.walchhofer.example.jpa.model.Benutzer;
import at.mse.walchhofer.example.services.IUserManagementService;

public class EditAccountController {

	private IUserManagementService userManagement;
	private IEditAccountView accountView;

	public EditAccountController(IUserManagementService userManagement,
			IEditAccountView accountView) {
		this.userManagement = userManagement;
		this.accountView = accountView;

	}

	public void saveAccount() {
		Long id = this.accountView.getId();
		String email = this.accountView.getEmail();
		String nachname = this.accountView.getNachname();
		String vorname = this.accountView.getVorname();
		char[] password = this.accountView.getPassword();
		char[] confirmPassword = this.accountView.getConfirmPassword();

		if (password == null || !Arrays.equals(password, confirmPassword)) {
			this.accountView.setPasswordValid(false);
			this.accountView.setConfirmPasswordValid(false);
			this.accountView.populateErrorMessage(
					"Password and confirmed password do not match","password");
			this.accountView.editAccountFailed();
		} else {

			Benutzer b = new Benutzer();
			b.setId(id);
			b.setEmail(email);
			b.setNachname(nachname);
			b.setVorname(vorname);
			b.setPass(password);
			if (userManagement.update(b)) {
				this.accountView.editAccountSuccess();
			} else {
				this.accountView
				.populateErrorMessage("Anlegen des Benutzers fehlgeschlagen.");
				this.accountView.editAccountFailed();
			}
		}
	}

	public void startEdit() {
		if (this.accountView.getId() == null) {
			this.accountView
			.populateErrorMessage("Der Account konnte nicht geladen werden!");
			this.accountView.editAccountMissingParameter();
		} else {
			Benutzer b = userManagement.getBenutzerById(this.accountView
					.getId());
			if (b == null) {
				this.accountView
				.populateInfoMessage("Der Account mit id "+this.accountView.getId()+" konnte nicht geladen werden!");
				this.accountView.editAccountMissingParameter();
			} else {
				this.accountView.setId(b.getId());
				this.accountView.setVorname(b.getVorname());
				this.accountView.setNachname(b.getNachname());
				this.accountView.setEmail(b.getEmail());
			}
		}
	}

	public void deleteAccount() {
		boolean success = false;
		if(this.accountView.getId() != null) {
			Benutzer b = userManagement.getBenutzerById(this.accountView.getId());
			if(b != null) {
				if(userManagement.deleteBenutzer(b)) {
					this.accountView.populateInfoMessage("Benutzer mit id "+this.accountView.getId()+" wurde geloescht!");
					this.accountView.deleteAccountSuccess();
					success = true;
				}
			}
		}
		if(!success) {
			this.accountView.populateErrorMessage("Der Account mit id "+this.accountView.getId()+" konnte nicht geloescht werden!");
			this.accountView.deleteAccountFailed();
		}
		
	}

}
