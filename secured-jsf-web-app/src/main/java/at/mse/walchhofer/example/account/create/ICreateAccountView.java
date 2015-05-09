package at.mse.walchhofer.example.account.create;

import at.mse.walchhofer.example.account.IAccountFormView;

public interface ICreateAccountView extends IAccountFormView {

	String createAccountFailed();

	String createAccountSuccess();

}
