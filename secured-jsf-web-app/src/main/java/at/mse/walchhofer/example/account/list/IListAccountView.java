package at.mse.walchhofer.example.account.list;

import java.util.List;

import at.mount.matize.jsf.IIdentifiable;
import at.mount.matize.jsf.IView;
import at.mse.walchhofer.example.account.IAccountView;

public interface IListAccountView extends IView, IIdentifiable {

    List<IAccountView> getAccounts();

    void setAccounts(List<IAccountView> accounts);

}
