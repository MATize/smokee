package at.mse.walchhofer.smokee.security;

import java.util.Calendar;

import at.mse.walchhofer.smokee.api.security.IAuthTokenValue;

public class AuthTokenValue implements IAuthTokenValue {

    private String benutzer;
    private Calendar createDate;

    public AuthTokenValue(String benutzer, Calendar createDate) {
        this.benutzer = benutzer;
        this.createDate = createDate;
    }

    @Override
    public String getBenutzer() {
        return benutzer;
    }

    @Override
    public Calendar getCreateDate() {
        return createDate;
    }

}
