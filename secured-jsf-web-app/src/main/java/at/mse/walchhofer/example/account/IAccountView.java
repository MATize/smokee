package at.mse.walchhofer.example.account;

import at.mount.matize.jsf.IView;

public interface IAccountView extends IView {

    Long getId();

    void setId(Long id);

    String getEmail();

    void setEmail(String email);

    String getVorname();

    void setVorname(String vorname);

    String getNachname();

    void setNachname(String nachname);

    char[] getPassword();

    void setPassword(char[] password);

}
