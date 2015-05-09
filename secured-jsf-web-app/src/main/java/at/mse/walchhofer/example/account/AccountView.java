package at.mse.walchhofer.example.account;

import javax.validation.constraints.NotNull;

import at.mount.matize.jsf.AbstractJsfView;

public class AccountView extends AbstractJsfView implements IAccountView {

	private Long id;

	@NotNull
	private String email;
	@NotNull
	private String vorname;
	@NotNull
	private String nachname;
	@NotNull
	private char[] password;

	@Override
	public String getVorname() {
		return vorname;
	}

	@Override
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	@Override
	public String getNachname() {
		return nachname;
	}

	@Override
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	@Override
	public char[] getPassword() {
		return password;
	}

	@Override
	public void setPassword(char[] password) {
		this.password = password;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	protected String getClientIdForField(String field) {
		return field;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
