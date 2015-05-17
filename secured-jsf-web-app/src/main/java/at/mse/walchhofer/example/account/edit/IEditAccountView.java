package at.mse.walchhofer.example.account.edit;

import at.mse.walchhofer.example.account.IAccountFormView;

public interface IEditAccountView extends IAccountFormView {

    void editAccountFailed();

    void editAccountSuccess();

    String editForm();

    void editAccountMissingParameter();

    void deleteAccountSuccess();

    void deleteAccountFailed();

}
