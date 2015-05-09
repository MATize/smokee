package at.mse.walchhofer.example.account;

import at.mount.matize.jsf.IIdentifiable;

public interface IAccountFormView extends IAccountView, IIdentifiable {

void setEmailValid(Boolean valid);
void setVornameValid(Boolean valid);
void setNachnameValid(Boolean valid);
void setPasswordValid(Boolean valid);
char[] getConfirmPassword();
void setConfirmPassword(char[] confirmPassword);
void setConfirmPasswordValid(Boolean valid);
	
}
