package at.mount.matize.jsf;

public interface IView {

    void populateFatalMessage(String message, String... fields);

    void populateWarnMessage(String message, String... fields);

    void populateInfoMessage(String message, String... fields);

    void populateErrorMessage(String message, String... fields);

}
